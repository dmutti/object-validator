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
import com.github.dmutti.annotations.strings.*;
import com.github.dmutti.object.validator.type.*;


public class StringLengthProcessor extends AnnotationProcessor {

    private final int min;
    private final int max;

    public StringLengthProcessor(final Object target, final Field field, final Annotation annotation) {
        super(target, field, annotation, new InstanceOfTypeValidator(String.class));
        min = ((StringLength) getAnnotation()).min();
        max = ((StringLength) getAnnotation()).max();

        if (min < 0 || max < 0) {
            throw new IllegalArgumentException(String.format("Valores invalidos de minimo [%d] e maximo [%d].", min, max));
        }
    }

    @Override
    protected boolean isValidValue() {
        String value = getObjectFieldFromTargetAsDefaultString();

        if (min >= 0 && max > 0) {
            return value.length() >= min && value.length() <= max;
        }

        if (min > 0) {
            return value.length() >= min;
        }

        if (max > 0) {
            return value.length() <= max;
        }

        return false;
    }

    @Override
    protected String getErrorMessage() {

        String msg = " deve ter ";

        if (min > 0) {
            msg += String.format("tamanho minimo igual a [%d]", min);
        }

        if (max > 0 && min > 0) {
            msg += " e ";
        }

        if (max > 0) {
            msg += String.format("tamanho maximo igual a [%d]", max);
        }

        msg += ".";
        return msg;
    }
}
