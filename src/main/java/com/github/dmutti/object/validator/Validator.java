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
package com.github.dmutti.object.validator;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;
import com.github.dmutti.annotations.processor.*;
import com.github.dmutti.annotations.utils.*;
import com.github.dmutti.object.inspector.*;


public class Validator {

    Map<Object, Class<?>> objectsToValidate;

    public void validate(final Object target) {

        if (null == target) {
            throw new NullPointerException();
        }
        objectsToValidate =  new ObjectInspector().getObjectsToInspect(target);
        Collection<String> errorMessages = new TreeSet<String>();

        for (Entry<Object, Class<?>> objectClassEntry : objectsToValidate.entrySet()) {
            Map<Field, Set<Annotation>> map = getAnnotatedFields(objectClassEntry.getValue());

            for(Entry<Field, Set<Annotation>> fieldAnnotations : map.entrySet()) {
                validateFields(errorMessages, objectClassEntry.getKey(), fieldAnnotations);
            }
        }

        if (!errorMessages.isEmpty()) {
            throw new IllegalStateException(ValidatorHelper.prettyPrintErrors(errorMessages, Constants.lineSeparator));
        }
    }

    private void validateFields(Collection<String> errorMessages, Object target, Entry<Field, Set<Annotation>> entry) {

        for (Annotation ann : entry.getValue()) {
            AnnotationProcessor annotationValidator = AnnotationProcessorFactory.getValidatorFor(target, entry.getKey(), ann);

            String result = annotationValidator.validate();
            if (StringUtils.isNotBlank(result)) {
                errorMessages.add(result);
            }
        }
    }

    private Map<Field, Set<Annotation>> getAnnotatedFields(Class<?> clazz) {
        Map<Field, Set<Annotation>> result = new IdentityHashMap<Field, Set<Annotation>>();

        for (Field f : ValidatorHelper.getAllFieldsFromClass(clazz)) {
            getFieldAnnotationMap(result, f);
        }
        return result;
    }

    private void getFieldAnnotationMap(Map<Field, Set<Annotation>> result, Field f) {

        Set<Annotation> ret = new LinkedHashSet<Annotation>();
        f.setAccessible(true);

        for (Annotation ann : f.getAnnotations()) {
            if (ann.annotationType().toString().contains(Constants.annotationsBasePackageName)) {
                ret.add(ann);
            }
        }

        if (!ret.isEmpty()) {
            result.put(f, ret);
        }
    }
}
