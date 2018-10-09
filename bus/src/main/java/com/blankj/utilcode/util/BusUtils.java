package com.blankj.utilcode.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class BusUtils {

    private static final NULL NULL = new NULL();

    public static <T> T post(String name, Object... objects) {
        if (name == null || name.length() == 0) return null;
        Object result = injectMethod(name, objects);
        if (!result.equals(NULL)) {
            //noinspection unchecked
            return (T) result;
        }
        return null;
    }

    private static Object injectMethod(String name, Object[] objects) {
        return NULL;
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.CLASS)
    public @interface Bus {

    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.CLASS)
    public @interface Subscribe {
        String name() default "";
    }

    private static class NULL {
    }
}