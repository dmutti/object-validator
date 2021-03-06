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


public class InstanceOfTypeValidator extends SupportedTypeValidator {

    public InstanceOfTypeValidator(Class<?>... classes) {
        super(classes);
    }

    @Override
    public boolean isTypeSupported(Field field) {

        for (Class<?> clazz : getSupportedClasses()) {
            if (clazz.isAssignableFrom(field.getType())) {
                return true;
            }
        }
        return false;
    }
}
