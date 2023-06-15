package nl.rug.oop.rugson;

import java.util.HashMap;
import java.util.Map;

import nl.rug.oop.rugson.converters.structure.TypeAdapter;

/**
 * This class is used to customize the properties of a Rugson instance.
 */
public class RugsonBuilder {
    private boolean prettyPrint;
    private Map<Class<?>, TypeAdapter<?>> typeAdapters = new HashMap<>();

    /**
     * Toggles pretty printing of JSON output.
     * 
     * @param prettyPrint - whether to pretty print or not
     * @return - this builder
     */
    public RugsonBuilder setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }

    /**
     * Adds a type adapter to the Rugson instance.
     * 
     * @param clazz       - the class to add the type adapter for
     * @param typeAdapter - the type adapter to add
     * @return - this builder
     */
    public RugsonBuilder addTypeAdapter(Class<?> clazz, TypeAdapter<?> typeAdapter) {
        typeAdapters.put(clazz, typeAdapter);
        return this;
    }

    /**
     * Builds a Rugson instance with the properties set in this builder.
     * 
     * @return - the Rugson instance
     */
    public Rugson build() {
        return new Rugson(prettyPrint, typeAdapters);
    }
}
