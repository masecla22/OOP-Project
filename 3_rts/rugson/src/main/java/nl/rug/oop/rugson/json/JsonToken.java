package nl.rug.oop.rugson.json;

/**
 * This enum represents the different types of JSON tokens.
 */
public enum JsonToken {
    START_ARRAY,
    END_ARRAY,

    NAME,
    START_OBJECT,
    END_OBJECT,

    BOOLEAN,
    NULL,

    NUMBER,
    STRING,
    EOF
}
