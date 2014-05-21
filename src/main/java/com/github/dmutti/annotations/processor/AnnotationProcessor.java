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
package com.github.dmutti.annotations.processor;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import com.github.dmutti.annotations.utils.*;
import com.github.dmutti.object.validator.type.*;


public abstract class AnnotationProcessor {

    private final Object target;
    private final Field field;
    private final Annotation annotation;
    private final SupportedTypeValidator typeValidator;

    public AnnotationProcessor(Object target, Field field, Annotation annotation, SupportedTypeValidator typeValidator) {
        this.target = target;
        this.field = field;
        this.annotation = annotation;
        this.typeValidator = typeValidator;
    }

    public final String validate() throws IllegalArgumentException {

        if (!isFieldTypeSupported()) {
            return String.format("[@%s] so pode ser utilizada em atributos do tipo [%s].", annotation.annotationType().getName(), typeValidator.getTypeSupportedList());
        }

        if (isCurrentAnnotationIncompatibleWithOthers()) {
            return String.format("[@%s] nao pode ser utilizada com as seguintes anotacoes [%s].", annotation.annotationType().getName(), ValidatorHelper.prettyPrintClassNames(getIncompatibleAnnotations(), ","));
        }

        if (!isValidValue()) {
            return String.format("[%s.%s]", field.getDeclaringClass().getName(), field.getName()) + getErrorMessage();
        }

        return null;
    }

    protected abstract boolean isValidValue();
    protected abstract String getErrorMessage();

    protected final boolean isCurrentAnnotationIncompatibleWithOthers() {
        for (Annotation ann : getField().getAnnotations()) {
            if (getIncompatibleAnnotations().contains(ann.annotationType())) {
                return true;
            }
        }
        return false;
    }

    protected List<Class<?>> getIncompatibleAnnotations() {
        return Collections.EMPTY_LIST;
    }


    protected final Object getObjectFieldFromTarget() {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected final String getObjectFieldFromTargetAsString() {
        Object o = getObjectFieldFromTarget();
        return (null == o) ? null : (String) o;
    }

    protected final String getObjectFieldFromTargetAsDefaultString() {
        Object o = getObjectFieldFromTarget();
        return (null == o) ? "" : (String) o;
    }

    protected final boolean isFieldTypeSupported() {
        return typeValidator.isTypeSupported(getField());
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass());
    }

    protected Object getTarget() {
        return this.target;
    }

    protected Field getField() {
        return this.field;
    }

    protected Annotation getAnnotation() {
        return this.annotation;
    }
}
