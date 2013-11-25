package org.everit.osgi.dev.testrunner.junit4.internal;

/*
 * Copyright (c) 2011, Everit Kft.
 *
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.everit.osgi.dev.testrunner.engine.TestCaseResult;
import org.everit.osgi.dev.testrunner.engine.TestClassResult;
import org.everit.osgi.dev.testrunner.engine.TestEngine;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs all JUnit4 based tests that are provided as a service in this OSGI container.
 */
public class Junit4TestEngine implements TestEngine {

    /**
     * The logger of the class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Junit4TestEngine.class);

    /**
     * The bundle context of this bundle to be able to get the Junit test services.
     */
    private BundleContext bundleContext;

    /**
     * Constructor.
     * 
     * @param bundleContext
     *            The bundle context of this bundle to be able to get the Junit test services.
     */
    public Junit4TestEngine(final BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public List<TestClassResult> runTest(final ServiceReference<Object> reference, final boolean developmentMode) {
        LOGGER.info("Test OSGI Service will be run by JUnit: " + reference.toString());
        List<TestClassResult> result = new ArrayList<TestClassResult>();
        try {
            Object service = bundleContext.getService(reference);
            String[] klassNames = (String[]) reference.getProperty(Constants.OBJECTCLASS);
            if (klassNames == null) {
                LOGGER.error("Cannot load interface names for Junit service");
                return Collections.emptyList();
            }
            Filter developmentModeFilter = null;
            if (developmentMode) {
                developmentModeFilter = new DevelopmentModeFilter();
            }
            for (String klassName : klassNames) {

                try {
                    Class<?> klass = reference.getBundle().loadClass(klassName);

                    BlockJUnit4ObjectRunner runner = new BlockJUnit4ObjectRunner(klass, service);
                    if (developmentModeFilter != null) {
                        try {
                            runner.filter(developmentModeFilter);
                        } catch (NoTestsRemainException e) {
                            LOGGER.info("Skipping all methods from class " + klass + " due to development mode.");
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
                        TestCaseResult testCaseResult =
                                new TestCaseResult(flowTestCaseResult.getMethodName(),
                                        flowTestCaseResult.getStartTime(), flowTestCaseResult.getFinishTime(),
                                        flowTestCaseResult.getFailure());
                        testCaseResults.add(testCaseResult);
                    }

                    result.add(new TestClassResult(klassName, extendedResult.getRunCount(), extendedResult
                            .getErrorCount(), extendedResult.getFailureCount(), extendedResult.getIgnoreCount(),
                            extendedResult.getStartTime(), extendedResult.getFinishTime(), testCaseResults));
                } catch (InitializationError e) {
                    LOGGER.error("Could not initialize Junit runner", e);
                } catch (ClassNotFoundException e) {
                    Object testIdObject =
                            reference.getProperty(org.everit.osgi.dev.testrunner.Constants.SERVICE_PROPERTY_TEST_ID);
                    LOGGER.error("Could not load the class of the test: " + testIdObject, e);
                }
            }
        } finally {
            bundleContext.ungetService(reference);
        }

        return result;
    }
}
