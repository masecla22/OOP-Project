package nl.rug.oop.rugson.fields;

import java.lang.reflect.Field;

public class DefaultJavaNamingStrategy implements FieldNamingStrategy {

    @Override
    public Field getClassField(Class<?> clazz, String jsonName) throws NoSuchFieldException, SecurityException {
        return clazz.getDeclaredField(jsonName);
    }

    @Override
    public String getJsonName(Field field) {
        return field.getName();
    }

}
