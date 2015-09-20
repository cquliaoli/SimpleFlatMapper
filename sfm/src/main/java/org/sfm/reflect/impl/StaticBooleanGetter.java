package org.sfm.reflect.impl;

import org.sfm.reflect.Getter;
import org.sfm.reflect.primitive.BooleanGetter;

public class StaticBooleanGetter<T> implements BooleanGetter, Getter<T, Boolean> {
    private final boolean value;

    public StaticBooleanGetter(boolean value) {
        this.value = value;
    }

    @Override
    public boolean getBoolean(Object target) throws Exception {
        return value;
    }

    @Override
    public Boolean get(T target) throws Exception {
        return value;
    }
}
