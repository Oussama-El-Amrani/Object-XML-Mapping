package org.example.dataMapper.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SetterOfField {
    public static void setAttribute(Field field, Object object, Object newValue) {
        Method setterOfAttribute = null;
        try {
            setterOfAttribute = object.getClass()
                    .getMethod(
                            "set" +
                                    field.getName().substring(0, 1)
                                            .toUpperCase() +
                                    field.getName()
                                            .substring(1),
                            Class.forName(field.getType().getName())
                    );
            try {
                System.out.println(newValue.getClass().getName() + ' '+ newValue);
                if (field.getType().getName().equals("java.lang.Integer"))
                    // TODO
                    setterOfAttribute.invoke(object,Integer.parseInt((String)newValue));
                else
                    setterOfAttribute.invoke(object,newValue);
            } catch (IllegalAccessException  e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
