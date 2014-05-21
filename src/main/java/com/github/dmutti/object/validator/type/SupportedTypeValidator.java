/*
 *    Copyright 1996-2014 UOL Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.github.dmutti.object.validator.type;

import java.lang.reflect.*;
import java.util.*;


public abstract class SupportedTypeValidator {

    private final List<Class<?>> supportedClasses = new ArrayList<Class<?>>();

    public SupportedTypeValidator(Class<?>... classes) {
        this.supportedClasses.addAll(Arrays.asList(classes));
    }

    public String getTypeSupportedList() {
        StringBuffer result = new StringBuffer(100);
        for (int i = 0, j = supportedClasses.size(); i < j; i++) {
            result.append(supportedClasses.get(i).getName());
            if (i+1 < j) {
                result.append(", ");
            }
        }
        return result.toString();
    }

    public abstract boolean isTypeSupported(Field field);

    protected final List<Class<?>> getSupportedClasses() {
        return this.supportedClasses;
    }
}
