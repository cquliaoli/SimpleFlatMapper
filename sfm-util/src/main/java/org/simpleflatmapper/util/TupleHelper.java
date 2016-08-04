package org.simpleflatmapper.util;

import java.lang.reflect.Type;

public class TupleHelper {

    public static boolean isTuple(Type type) {
        return isSfmTuple(type) || isJoolTuple(type);
    }

    public static boolean isSfmTuple(Type type) {
        String className = TypeHelper.toClass(type).getName();
        return className.startsWith("org.simpleflatmapper.core.tuples.Tuple")
                && !className.endsWith("Tuples");
    }

    public static boolean isJoolTuple(Type type) {
        Class<?> clazz = TypeHelper.toClass(type);
        while(clazz != null) {
            for(Class<?> i : clazz.getInterfaces()) {
                if ("org.jooq.lambda.tuple.Tuple".equals(i.getName())) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

}
