package com.exercise.employment.countryemployment.utils.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static void setField(Object object, Field field, Object value) throws IllegalAccessException {
        makeAccessible(field);
        field.set(object, value);
    }

    public static void executeOnAllClassFields(Class<?> clazz, FieldDefCommand fieldCommand) {
        if (clazz == null || fieldCommand == null)
            return;
        while (!clazz.equals(Object.class)) {
            for (Field field : clazz.getDeclaredFields()) {
                makeAccessible(field);
                fieldCommand.execute(field);
            }
            clazz = clazz.getSuperclass();
        }
    }


    public static void executeOnClassFieldsByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass, FieldDefCommand fieldCommand) {
        executeOnAllClassFields(clazz, fieldDef -> {
            if (fieldDef.isAnnotationPresent(annotationClass))
                fieldCommand.execute(fieldDef);
        });
    }

    @FunctionalInterface
    public interface FieldDefCommand {
        public void execute(Field fieldDef);
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

}


