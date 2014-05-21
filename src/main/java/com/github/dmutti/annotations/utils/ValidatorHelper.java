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
package com.github.dmutti.annotations.utils;

import java.lang.reflect.*;
import java.util.*;


public class ValidatorHelper {

    public static Stack<Class<?>> getClassHierarchyFrom(Class<?> clazz) {
        Stack<Class<?>> result = new Stack<Class<?>>();
        Class<?> target = clazz;

        while (target.getSuperclass() != null) {
            result.add(target);
            target = target.getSuperclass();
        }

        for (Class<?> iface : clazz.getInterfaces()) {
            result.add(iface);
        }
        return result;
    }

    public static Collection<Field> getAllFieldsFromClass(Class<?> clazz) {

        Stack<Class<?>> classHierarchy = getClassHierarchyFrom(clazz);
        Set<Field> fields = new LinkedHashSet<Field>();

        while (!classHierarchy.empty()) {
            Class<?> currentClass = classHierarchy.pop();
            for (Field f : currentClass.getDeclaredFields()) {
                fields.add(f);
            }

            for (Field f : currentClass.getFields()) {
                fields.add(f);
            }
        }

        return fields;
    }

    public static String prettyPrintErrors(Collection<String> errors, String separator) {
        return separator + prettyPrintList(errors, separator);
    }

    public static String prettyPrintClassNames(Collection<Class<?>> classes, String separator) {
        Collection<String> classNames = new ArrayList<String>();
        for (Class<?> clazz : classes) {
            classNames.add(clazz.getName());
        }
        return prettyPrintList(classNames, separator);
    }

    public static String prettyPrintList(Collection<?> objects, String separator) {
        if (null == objects || objects.isEmpty()) {
            return "";
        }

        StringBuffer result = new StringBuffer(400);
        int curr = 0;
        for (Object mensagem : objects) {
            result.append(mensagem);
            if (++curr < objects.size()) {
                result.append(separator);
            }
        }
        return result.toString();
    }
}
