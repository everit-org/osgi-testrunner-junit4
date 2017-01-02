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
    Junit4TestEngine junit4TestRunner = new Junit4TestEngine();
    Hashtable<String, Object> serviceProperties = new Hashtable<String, Object>();
    serviceProperties.put(TestRunnerConstants.SERVICE_PROPERTY_TESTRUNNER_ENGINE, "junit4");
    junit4TestRunnerSR =
        context.registerService(TestEngine.class, junit4TestRunner, serviceProperties);
  }

  @Override
  public void stop(final BundleContext context) throws Exception {
    if (junit4TestRunnerSR != null) {
      junit4TestRunnerSR.unregister();
      junit4TestRunnerSR = null;
    }
  }

}
