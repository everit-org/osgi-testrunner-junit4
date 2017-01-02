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

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Extended listener for test running to be able to provide more information that JUnit provides by
 * default.
 */
public class ExtendedResultListener extends RunListener {

  /**
   * The extended result.
   */
  private final ExtendedResult result = new ExtendedResult(new Date().getTime());

  /**
   * The result of each test cases by their {@link Description}s.
   */
  private final Map<Description, FlowTestCaseResult> testCaseResults =
      new ConcurrentHashMap<Description, FlowTestCaseResult>();

  public ExtendedResult getResult() {
    return result;
  }

  /**
   * Handling assumption and simple failures.
   *
   * @param failure
   *          The failure object.
   */
  protected void handleFailure(final Failure failure) {
    FlowTestCaseResult testCaseResult = testCaseResults.get(failure.getDescription());
    testCaseResult.finishRun();
    testCaseResult.setFailure(failure.getException());
    Throwable exception = failure.getException();
    if (exception instanceof AssertionError) {
      result.incrementFailureCount();
    } else {
      result.incrementErrorCount();
    }
  }

  @Override
  public void testAssumptionFailure(final Failure failure) {
    handleFailure(failure);
  }

  @Override
  public void testFailure(final Failure failure) throws Exception {
    handleFailure(failure);
  }

  @Override
  public void testFinished(final Description description) throws Exception {
    FlowTestCaseResult testCaseResult = testCaseResults.get(description);
    testCaseResult.finishRun();
  }

  @Override
  public void testIgnored(final Description description) throws Exception {
    FlowTestCaseResult testCaseResult =
        new FlowTestCaseResult(description.getClassName(), description.getMethodName(), null);
    testCaseResults.put(description, testCaseResult);
    result.incrementIgnoreCount();
  }

  @Override
  public void testRunFinished(final Result presult) throws Exception {
    // this.result.setResult(result);
  }

  @Override
  public void testRunStarted(final Description description) throws Exception {
    // result = new ExtendedResult(description, new Date().getTime());
  }

  @Override
  public void testStarted(final Description description) throws Exception {
    FlowTestCaseResult testCaseResult =
        new FlowTestCaseResult(description.getClassName(), description.getMethodName(),
            new Date().getTime());
    testCaseResults.put(description, testCaseResult);
    result.getTestCaseResults().add(testCaseResult);
    result.incrementRunCount();
  }

}
