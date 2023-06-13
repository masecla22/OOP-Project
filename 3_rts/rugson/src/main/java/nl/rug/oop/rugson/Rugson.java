package nl.rug.oop.rugson;

import nl.rug.oop.rugson.fields.DefaultJavaNamingStrategy;
import nl.rug.oop.rugson.fields.FieldNamingStrategy;

public class Rugson {
    private FieldNamingStrategy fieldNamingStrategy;

    public Rugson() {
        this.fieldNamingStrategy = new DefaultJavaNamingStrategy();
    }
}
