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

import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Class that extends the {@link BlockJUnit4ClassRunner} functionality to make it possible to run
 * tests on created objects and getting the annotations from the specified interface the object
 * implements. In integration tests it may be necessary that we want to set up an object and add
 * value to the properties of the object before we run tests on it.
 *
 */
public class BlockJUnit4ObjectRunner extends BlockJUnit4ClassRunner {

  /**
   * The object which has the test functions.
   */
  private final Object testObject;

  /**
   * Constructor of the class.
   *
   * @param klass
   *          The interface or parent class of the testObject that contains the annotated functions.
   * @param testObject
   *          The object that has the test functions to run.
   * @throws InitializationError
   *           if there was any error during the initialization of this class.
   */
  public BlockJUnit4ObjectRunner(final Class<?> klass, final Object testObject)
      throws InitializationError {
    super(klass);
    this.testObject = testObject;
  }

  /**
   * Simply giving back the testObject we got via the constructor.
   *
   * @return The value of the testObject property.
   */
  @Override
  protected Object createTest() {
    return testObject;
  }

  public Object getTestObject() {
    return testObject;
  }

  /**
   * Avoiding the validation of the constructor the test object is already instantiated and
   * interfaces do not have constructor at all.
   *
   * @param errors
   *          Not used.
   */
  @Override
  protected void validateConstructor(final List<Throwable> errors) {
    // In case of objects we do not want to validate constructors so nothing has to be done
  }
}
