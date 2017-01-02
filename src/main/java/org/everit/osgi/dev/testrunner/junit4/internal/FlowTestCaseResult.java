package org.everit.osgi.dev.testrunner.junit4.internal;

import java.util.Date;

public class FlowTestCaseResult {

  /**
   * The name of the class that contained the test methods.
   */
  private final String className;

  /**
   * An exception that was thrown and not caughed by the test method.
   */
  private Throwable failure;

  /**
   * The time the TestCase stopped to run.
   */
  private Long finishTime;

  /**
   * The name of the test method.
   */
  private final String methodName;

  /**
   * The starting time of the TestCase.
   */
  private final Long startTime;

  /**
   * Constructor of the class that sets the properties that should be available already.
   *
   * @param startTime
   *          See {@link #startTime}.
   * @param className
   *          See {@link #className}.
   * @param methodName
   *          See {@link #methodName}.
   */
  public FlowTestCaseResult(final String className, final String methodName, final Long startTime) {
    this.className = className;
    this.methodName = methodName;
    this.startTime = startTime;
  }

  /**
   * Should be called when the TestCase running has finished. Sets the finishTime property.
   */
  public void finishRun() {
    finishTime = new Date().getTime();
  }

  public String getClassName() {
    return className;
  }

  public Throwable getFailure() {
    return failure;
  }

  public Long getFinishTime() {
    return finishTime;
  }

  public String getMethodName() {
    return methodName;
  }

  /**
   * Gives back the amount of time while the TestCase was running or null if the TestCase has not
   * finished yet.
   *
   * @return FinishTime - StartTime.
   */
  public Long getRunningTime() {
    if ((startTime == null) || (finishTime == null)) {
      return null;
    } else {
      return finishTime.longValue() - startTime.longValue();
    }
  }

  public long getStartTime() {
    return startTime;
  }

  public void setFailure(final Throwable failure) {
    this.failure = failure;
  }
}
