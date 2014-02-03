/**
 * This file is part of Junit4 TestRunner Engine.
 *
 * Junit4 TestRunner Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Junit4 TestRunner Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Junit4 TestRunner Engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.everit.osgi.dev.testrunner.junit4.internal;

import java.util.Date;

public class FlowTestCaseResult {

    /**
     * The name of the class that contained the test methods.
     */
    private String className;

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
    private String methodName;

    /**
     * The starting time of the TestCase.
     */
    private Long startTime;

    /**
     * Constructor of the class that sets the properties that should be available already.
     * 
     * @param startTime
     *            See {@link #startTime}.
     * @param className
     *            See {@link #className}.
     * @param methodName
     *            See {@link #methodName}.
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
     * Gives back the amount of time while the TestCase was running or null if the TestCase has not finished yet.
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
