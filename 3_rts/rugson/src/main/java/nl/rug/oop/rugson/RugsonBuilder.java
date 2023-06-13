package nl.rug.oop.rugson;

import java.util.Map;

import nl.rug.oop.rugson.converters.structure.TypeAdapter;

public class RugsonBuilder {
    private boolean prettyPrint;
    private Map<Class<?>, TypeAdapter<?>> typeAdapters;

    public RugsonBuilder setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }

    public RugsonBuilder addTypeAdapter(Class<?> clazz, TypeAdapter<?> typeAdapter) {
        typeAdapters.put(clazz, typeAdapter);
        return this;
    }

    public Rugson build() {
        return new Rugson(prettyPrint, typeAdapters);
    }
}
