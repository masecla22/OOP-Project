package nl.rug.oop.rugson.fields;

import java.lang.reflect.Field;

public class UppercaseNamingStrategy implements FieldNamingStrategy {

    @Override
    public Field getClassField(Class<?> clazz, String jsonName) throws NoSuchFieldException, SecurityException {
        String name = jsonName.substring(0, 1).toLowerCase() + jsonName.substring(1);
        return clazz.getDeclaredField(name);
    }

    @Override
    public String getJsonName(Field field) {
        String name = field.getName();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

}
