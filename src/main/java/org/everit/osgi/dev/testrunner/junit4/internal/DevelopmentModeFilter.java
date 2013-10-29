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

import org.everit.osgi.dev.testrunner.TestClassInDevelopmentMode;
import org.everit.osgi.dev.testrunner.TestMethodInDevelopmentMode;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

public class DevelopmentModeFilter extends Filter {

    @Override
    public boolean shouldRun(Description description) {
        boolean result = false;
        TestMethodInDevelopmentMode testMethodInDevelopmentModeAnnotation =
                description.getAnnotation(TestMethodInDevelopmentMode.class);

        if (testMethodInDevelopmentModeAnnotation != null) {
            result = true;
        } else {
            Class<?> testClass = description.getTestClass();
            TestClassInDevelopmentMode testClassInDevelopmentModeAnnotation =
                    testClass.getAnnotation(TestClassInDevelopmentMode.class);
            
            if (testClassInDevelopmentModeAnnotation != null) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public String describe() {
        return "In case the development mode flag is true, a method annotated with @Test will run only if the "
                + "@TestMethodInDevelopmentMode is present or the class is annotated with @TestClassInDevelopmentMode";
    }

}
