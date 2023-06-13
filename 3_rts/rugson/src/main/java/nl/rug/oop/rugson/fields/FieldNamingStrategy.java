package nl.rug.oop.rugson.fields;

import java.lang.reflect.Field;

public interface FieldNamingStrategy {
    String getJsonName(Field field);

    Field getClassField(Class<?> clazz, String jsonName) throws NoSuchFieldException, SecurityException;
}
