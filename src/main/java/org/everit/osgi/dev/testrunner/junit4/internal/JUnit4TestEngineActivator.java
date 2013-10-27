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

import java.util.Hashtable;

import org.everit.osgi.dev.testrunner.Constants;
import org.everit.osgi.dev.testrunner.engine.TestEngine;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class JUnit4TestEngineActivator implements BundleActivator {

    private ServiceRegistration<TestEngine> junit4TestRunnerSR;

    @Override
    public void start(BundleContext context) throws Exception {
        Junit4TestRunner junit4TestRunner = new Junit4TestRunner(context);
        Hashtable<String, Object> serviceProperties = new Hashtable<String, Object>();
        serviceProperties.put(Constants.SERVICE_PROPERTY_TESTRUNNER_ENGINE_TYPE, "junit4");
        junit4TestRunnerSR = context.registerService(TestEngine.class, junit4TestRunner, serviceProperties);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (junit4TestRunnerSR != null) {
            junit4TestRunnerSR.unregister();
            junit4TestRunnerSR = null;
        }
    }

}
