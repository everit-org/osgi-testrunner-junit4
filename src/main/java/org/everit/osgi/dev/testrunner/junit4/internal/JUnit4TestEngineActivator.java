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

import java.util.Hashtable;

import org.everit.osgi.dev.testrunner.TestRunnerConstants;
import org.everit.osgi.dev.testrunner.engine.TestEngine;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator of the bundle that registers the JUnit 4 test engine.
 */
public class JUnit4TestEngineActivator implements BundleActivator {

    /**
     * Service registration of JUnit 4 test engine.
     */
    private ServiceRegistration<TestEngine> junit4TestRunnerSR;

    @Override
    public void start(final BundleContext context) throws Exception {
        Junit4TestEngine junit4TestRunner = new Junit4TestEngine(context);
        Hashtable<String, Object> serviceProperties = new Hashtable<String, Object>();
        serviceProperties.put(TestRunnerConstants.SERVICE_PROPERTY_TESTRUNNER_ENGINE_TYPE, "junit4");
        junit4TestRunnerSR = context.registerService(TestEngine.class, junit4TestRunner, serviceProperties);
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        if (junit4TestRunnerSR != null) {
            junit4TestRunnerSR.unregister();
            junit4TestRunnerSR = null;
        }
    }

}
