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
package com.github.dmutti.object.inspector;

import java.lang.reflect.*;
import java.math.*;
import java.util.*;
import java.util.Map.Entry;
import com.github.dmutti.annotations.utils.*;


public abstract class AbstractObjectInspector {

    private IdentityHashMap<Object, Class<?>> map;
    private static final List<Class<?>> immutable = Arrays.asList(new Class<?>[] {String.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Character.class, Boolean.class, BigDecimal.class, BigInteger.class});

    public Map<Object, Class<?>> getObjectsToInspect(Object node) throws RuntimeException {
        map = new IdentityHashMap<Object, Class<?>>();

        try {
            inspect(node);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    private final void inspect(Object node) throws Exception {
        if (!isInspectableNode(node) && !isIgnoredNode(node)) {
            return;
        }

        map.put(node, node.getClass());
        for (Object obj : getFieldsToInspect(node, node.getClass())) {
            inspect(obj);
        }
    }

    private boolean isInspectableNode(Object node) {
        return node != null &&
               !map.containsKey(node) &&
               !node.getClass().isPrimitive() &&
               !immutable.contains(node.getClass());
    }

    private List<Object> getFieldsToInspect(Object node, Class<?> clazz) throws Exception {

        List<Object> fieldsToInspect = new ArrayList<Object>();

        for (Field field : ValidatorHelper.getAllFieldsFromClass(clazz)) {
            field.setAccessible(true);

            Object inspectedObjectFieldValue = field.get(node);
            if (null == inspectedObjectFieldValue || field.getType().isPrimitive()) {
                continue;
            }

            if (!isCollection(field) && !isArray(field) && !isMap(field)) {
                fieldsToInspect.add(inspectedObjectFieldValue);
            } else if (isCollection(field)) {
                for (Object listMember : (Collection<?>) inspectedObjectFieldValue) {

                    if (isInspectableNode(listMember)) {
                        fieldsToInspect.add(listMember);
                    }
                }
            } else if (isMap(field)) {
                for (Entry<?, ?> mapEntry : ((Map<?,?>) inspectedObjectFieldValue).entrySet()) {

                    if (isInspectableNode(mapEntry.getKey())) {
                        fieldsToInspect.add(mapEntry.getKey());
                    }

                    if (isInspectableNode(mapEntry.getValue())) {
                        fieldsToInspect.add(mapEntry.getValue());
                    }
                }
            } else if (isArray(field)) {
                int arraySize = Array.getLength(inspectedObjectFieldValue);
                for (int i = 0; i < arraySize; i++) {
                    Object arrayMember = Array.get(inspectedObjectFieldValue, i);
                    if (isInspectableNode(arrayMember)) {
                        fieldsToInspect.add(arrayMember);
                    }
                }
            }
        }
        return fieldsToInspect;
    }

    /** Hook */
    protected boolean isIgnoredNode(Object node) {
        return false;
    }

    private boolean isMap(Field field) {
        return Map.class.isAssignableFrom(field.getType());
    }

    private boolean isArray(Field field) {
        return field.getType().isArray();
    }

    private boolean isCollection(Field field) {
        return Collection.class.isAssignableFrom(field.getType());
    }
}