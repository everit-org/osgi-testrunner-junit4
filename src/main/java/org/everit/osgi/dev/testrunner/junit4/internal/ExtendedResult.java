/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.osgi.dev.testrunner.junit4.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An extended type of {@link org.junit.runner.Result} to be able to provide more information after
 * the tests of a provided OSGI Test Service finished running.
 */
public class ExtendedResult {

  /**
   * The count of errors.
   */
  private long errorCount = 0;

  /**
   * The count of failures.
   */
  private long failureCount = 0;

  /**
   * The time when the test finished running.
   */
  private Long finishTime;

  /**
   * The count of tests that were ignored.
   */
  private long ignoreCount = 0;

  /**
   * The count of tests that ran.
   */
  private long runCount = 0;

  /**
   * The time the test was started.
   */
  private final long startTime;

  /**
   * The results of TestCases that ran.
   */
  private final List<FlowTestCaseResult> testCaseResults = new ArrayList<FlowTestCaseResult>();

  /**
   * Constructor of the class.
   *
   * @param startTime
   *          The time when the test was started.
   */
  public ExtendedResult(final long startTime) {
    this.startTime = startTime;
  }

  /**
   * Called when all tests finished running.
   */
  public void finishRunning() {
    finishTime = new Date().getTime();
  }

  public long getErrorCount() {
    return errorCount;
  }

  public long getFailureCount() {
    return failureCount;
  }

  public Long getFinishTime() {
    return finishTime;
  }

  public long getIgnoreCount() {
    return ignoreCount;
  }

  public long getRunCount() {
    return runCount;
  }

  /**
   * Getting the amount of time the tests were running.
   *
   * @return Current time - startTime if the test running has not finished yet or the finishTime -
   *         startTime.
   */
  public long getRunTime() {
    if (finishTime == null) {
      return new Date().getTime() - startTime;
    } else {
      return finishTime.longValue() - startTime;
    }
  }

  public long getStartTime() {
    return startTime;
  }

  public List<FlowTestCaseResult> getTestCaseResults() {
    return testCaseResults;
  }

  /**
   * Incrementing the value of errorCount property.
   */
  public void incrementErrorCount() {
    errorCount++;
  }

  /**
   * Incrementing the value of failureCount property.
   */
  public void incrementFailureCount() {
    failureCount++;
  }

  /**
   * Incrementing the ignoreCount property value by one.
   */
  public void incrementIgnoreCount() {
    ignoreCount++;
  }

  /**
   * Incrementing the runCount property value by one.
   */
  public void incrementRunCount() {
    runCount++;
  }
}
