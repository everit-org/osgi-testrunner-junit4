package org.everit.osgi.dev.testrunner.junit4.internal;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.everit.osgi.dev.testrunner.TestDuringDevelopment;
import org.everit.osgi.dev.testrunner.TestRunnerConstants;
import org.everit.osgi.dev.testrunner.engine.TestCaseResult;
import org.everit.osgi.dev.testrunner.engine.TestClassResult;
import org.everit.osgi.dev.testrunner.engine.TestEngine;
import org.junit.Test;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

/**
 * Runs all JUnit4 based tests that are provided as a service in this OSGI container.
 */
public class Junit4TestEngine implements TestEngine {

  /**
   * The logger of the class.
   */
  private static final Logger LOGGER = Logger.getLogger(Junit4TestEngine.class.getName());

  private TestClassResult createNonExecutedErrorTestClassResult(final Class<?> klass,
      final List<TestCaseResult> testCaseResults) {
    long now = new Date().getTime();
    TestClassResult result = new TestClassResult();
    result.className = klass.getName();
    result.runCount = 0;
    result.errorCount = testCaseResults.size();
    result.failureCount = 0;
    result.ignoreCount = 0;
    result.startTime = now;
    result.finishTime = now;
    result.testCaseResults = testCaseResults;
    return result;
  }

  private TestClassResult handleInitializationError(final boolean developmentMode,
      final Class<?> klass, final InitializationError e) {

    TestClass testClass = new TestClass(klass);

    boolean allMethods =
        (!developmentMode) || (klass.getAnnotation(TestDuringDevelopment.class) != null);

    List<Throwable> causes = e.getCauses();
    long now = System.currentTimeMillis();
    List<TestCaseResult> testCaseResults = new ArrayList<TestCaseResult>();
    List<FrameworkMethod> annotatedMethods = testClass.getAnnotatedMethods(Test.class);

    Exception wrapperException = new Exception("Error during initialization. See log for details");
    wrapperException.setStackTrace(new StackTraceElement[0]);

    for (FrameworkMethod frameworkMethod : annotatedMethods) {
      if (allMethods || (frameworkMethod.getAnnotation(TestDuringDevelopment.class) != null)) {

        TestCaseResult testCaseResult = new TestCaseResult();
        testCaseResult.testMethodName = frameworkMethod.getName();
        testCaseResult.startTime = now;
        testCaseResult.finishTime = now;
        testCaseResult.failure = wrapperException;

        testCaseResults.add(testCaseResult);
      }
    }

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    pw.write("Error during initialization of test class '" + klass.getName() + "':\n");
    for (Throwable throwable : causes) {
      throwable.printStackTrace(pw);
    }
    LOGGER.log(Level.SEVERE, sw.toString());

    TestClassResult result = createNonExecutedErrorTestClassResult(klass, testCaseResults);
    return result;
  }

  @Override
  public TestClassResult runTestsOfInstance(final Object testObject,
      final Map<String, ?> properties) {

    Filter developmentModeFilter = null;
    if (TestRunnerConstants.IN_DEVELOPMENT_MODE) {
      developmentModeFilter = new DevelopmentModeFilter();
    }

    Class<?> klass = testObject.getClass();

    try {
      BlockJUnit4ObjectRunner runner = new BlockJUnit4ObjectRunner(klass, testObject);
      if (developmentModeFilter != null) {
        try {
          runner.filter(developmentModeFilter);
        } catch (NoTestsRemainException e) {
          LOGGER.warning("Skipping all methods from class "
              + klass
              + " due to development mode. To run the tests in development mode, annotate or "
              + "methods or the class with @TestDuringDevelopment");
        }
      }

      RunNotifier notifier = new RunNotifier();
      ExtendedResultListener extendedResultListener = new ExtendedResultListener();
      notifier.addListener(extendedResultListener);
      runner.run(notifier);
      ExtendedResult extendedResult = extendedResultListener.getResult();
      extendedResult.finishRunning();

      List<TestCaseResult> testCaseResults = new ArrayList<TestCaseResult>();

      for (FlowTestCaseResult flowTestCaseResult : extendedResult.getTestCaseResults()) {
        TestCaseResult testCaseResult = new TestCaseResult();
        testCaseResult.testMethodName = flowTestCaseResult.getMethodName();
        testCaseResult.startTime = flowTestCaseResult.getStartTime();
        testCaseResult.finishTime = flowTestCaseResult.getFinishTime();
        testCaseResult.failure = flowTestCaseResult.getFailure();
        testCaseResults.add(testCaseResult);
      }

      long runCount = extendedResult.getRunCount() + extendedResult.getIgnoreCount();
      TestClassResult result = new TestClassResult();
      result.className = klass.getName();
      result.runCount = runCount;
      result.errorCount = extendedResult.getErrorCount();
      result.failureCount = extendedResult.getFailureCount();
      result.ignoreCount = extendedResult.getIgnoreCount();
      result.startTime = extendedResult.getStartTime();
      result.finishTime = extendedResult.getFinishTime();
      result.testCaseResults = testCaseResults;
      return result;
    } catch (InitializationError e) {
      return handleInitializationError(TestRunnerConstants.IN_DEVELOPMENT_MODE, klass, e);
    }
  }
}
