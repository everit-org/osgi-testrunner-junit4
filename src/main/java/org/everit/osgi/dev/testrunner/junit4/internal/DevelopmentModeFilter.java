package org.everit.osgi.dev.testrunner.junit4.internal;

import org.everit.osgi.dev.testrunner.TestDuringDevelopment;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

/**
 * A filter that allows to run functions annotated with {@link TestDuringDevelopment}.
 */
public class DevelopmentModeFilter extends Filter {

  @Override
  public String describe() {
    return "In case the development mode flag is true, a method annotated with @Test will run only if the "
        + "@TestMethodInDevelopmentMode is present or the class is annotated with @TestClassInDevelopmentMode";
  }

  @Override
  public boolean shouldRun(final Description description) {
    boolean result = false;

    if (description.getAnnotation(TestDuringDevelopment.class) != null) {
      result = true;
    } else {
      Class<?> testClass = description.getTestClass();

      if (testClass.getAnnotation(TestDuringDevelopment.class) != null) {
        result = true;
      }
    }

    return result;
  }

}
