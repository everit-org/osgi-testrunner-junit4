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

import org.everit.osgi.dev.testrunner.TestDuringDevelopment;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

/**
 * A filter that allows to run functions annotated with {@link TestDuringDevelopment}.
 */
public class DevelopmentModeFilter extends Filter {

  @Override
  public String describe() {
    return "In case the development mode flag is true, a method annotated with @Test will"
        + " run only if the method or the class is annotated with @TestDuringDevelopment";
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
