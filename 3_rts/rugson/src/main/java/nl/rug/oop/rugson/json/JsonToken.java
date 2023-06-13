package nl.rug.oop.rugson.json;

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

    EOF;
}
