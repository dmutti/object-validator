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
package com.github.dmutti.annotations.processor.impl;

import java.lang.annotation.*;
import java.lang.reflect.*;
import com.github.dmutti.annotations.processor.*;
import com.github.dmutti.object.validator.type.*;


public class NumberNotNegativeProcessor extends AnnotationProcessor {

    public NumberNotNegativeProcessor(final Object target, final Field field, final Annotation annotation) {
        super(target, field, annotation, new InstanceOfTypeValidator(Number.class, int.class, float.class, double.class, byte.class, long.class));
    }

    @Override
    protected boolean isValidValue() {
        Object obj = getObjectFieldFromTarget();

        if (null == obj) {
            return false;
        }

        return ((Number) obj).doubleValue() >= 0;
    }

    @Override
    protected String getErrorMessage() {
        return String.format(" deve ser maior ou igual a zero.");
    }
}
