package org.example.dataMapper.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GetterOfFiled {
        public static Object getAttributeValue(Field field, Object object) {
        try {
            Method getterOfAttribute = object.getClass()
                    .getMethod(
                            "get" +
                                    field.getName().substring(0, 1)
                                            .toUpperCase() +
                                    field.getName()
                                            .substring(1)
                    );
            Object value = getterOfAttribute.invoke(object);

            if (field.getType().getName().equals("java.lang.Integer"))
                return String.valueOf(value);
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() + " infound getter ");
        }
    }
}
