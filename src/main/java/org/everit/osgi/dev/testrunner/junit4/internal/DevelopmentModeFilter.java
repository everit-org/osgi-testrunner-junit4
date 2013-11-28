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

import org.everit.osgi.dev.testrunner.TestDuringDevelopment;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

/**
 * A filter that allows to run functions annotated with {@link TestDuringDevelopment}.
 */
public class DevelopmentModeFilter extends Filter {

    @Override
    public String describe() {
        return "In case the development mode flag is true, a method annotated with @Test will run only if the "
                + "@TestMethodInDevelopmentMode is present or the class is annotated with @TestClassInDevelopmentMode";
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
