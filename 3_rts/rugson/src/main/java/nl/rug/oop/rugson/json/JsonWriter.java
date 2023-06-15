package nl.rug.oop.rugson.json;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;

/**
 * This class is used to convert a List of JsonValues to a JSON String.
 */
@RequiredArgsConstructor
public class JsonWriter {
    private final boolean prettyPrint;

    private int indent = 0;

    /**
     * Converts a List of JsonValues to a JSON String.
     * 
     * @param values - the list of JsonValues
     * @return - the String representation of the JsonValues
     */
    public String print(List<JsonValue> values) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            JsonValue current = values.get(i), previous = null, next = null;
            if (i > 0) {
                previous = values.get(i - 1);
            }
            if (i < values.size() - 1) {
                next = values.get(i + 1);
            }

            printJsonValue(builder, previous, current, next);
        }

        return builder.toString();
    }

    private void printJsonValue(StringBuilder builder, JsonValue previous, JsonValue current, JsonValue next) {
        switch (current.getType()) {
            case START_OBJECT:
                printStartObject(builder, current);
                break;
            case END_OBJECT:
                printEndObject(builder, current, next);
                break;
            case START_ARRAY:
                printStartArray(builder, current);
                break;
            case END_ARRAY:
                printEndArray(builder, current, next);
                break;
            case NAME:
                printName(builder, current);
                break;
            case BOOLEAN:
            case NULL:
            case NUMBER:
                printPrimitive(builder, previous, current, next);
                break;
            case STRING:
                printString(builder, previous, current, next);
                break;
            case EOF:
                break;
        }
    }

    private void printStartObject(StringBuilder builder, JsonValue current) {
        indent(builder);
        builder.append("{");
        incrementIndent();
        newLine(builder);
    }

    private void printEndObject(StringBuilder builder, JsonValue current, JsonValue next) {
        decrementIndent();
        newLine(builder);
        indent(builder);
        builder.append("}");
        addCommaIfNeeded(builder, next.getType());
    }

    private void printStartArray(StringBuilder builder, JsonValue current) {
        indent(builder);
        builder.append("[");
        incrementIndent();
        newLine(builder);
    }

    private void printEndArray(StringBuilder builder, JsonValue current, JsonValue next) {
        decrementIndent();
        newLine(builder);
        indent(builder);
        builder.append("]");
        addCommaIfNeeded(builder, next.getType());
    }

    private void printName(StringBuilder builder, JsonValue current) {
        indent(builder);
        builder.append(current.getValue() + ":");
        if (prettyPrint) {
            builder.append(" ");
        }
    }

    private void printPrimitive(StringBuilder builder, JsonValue previous, JsonValue current, JsonValue next) {
        if (!previous.getType().equals(JsonToken.NAME)) {
            indent(builder);
        }

        builder.append(current.getValue());
        addCommaIfNeeded(builder, next.getType());
    }

    private void printString(StringBuilder builder, JsonValue previous, JsonValue current, JsonValue next) {
        if (!previous.getType().equals(JsonToken.NAME)) {
            indent(builder);
        }

        String stringValue = (String) current.getValue();
        builder.append("\"" + escape(stringValue) + "\"");
        addCommaIfNeeded(builder, next.getType());
    }

    private String escape(String s) {
        if (s == null) {
            return null;
        }

        // List of characters obtained from
        // https://stackoverflow.com/questions/2406121/how-do-i-escape-a-string-in-java
        return s.replace("\\", "\\\\")
                .replace("\b", "\\b")
                .replace("\t", "\\t")
                .replace("\f", "\\f")
                .replace("\'", "\\'")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\"", "\\\"");
    }

    private void addCommaIfNeeded(StringBuilder builder, JsonToken nextToken) {
        Set<JsonToken> tokens = Set.of(JsonToken.START_ARRAY, JsonToken.START_OBJECT, JsonToken.NAME,
                JsonToken.BOOLEAN, JsonToken.NULL, JsonToken.NUMBER, JsonToken.STRING);
        if (tokens.contains(nextToken)) {
            builder.append(",");
            if (prettyPrint) {
                builder.append("\n");
            }
        }
    }

    private void indent(StringBuilder builder) {
        if (prettyPrint) {
            for (int i = 0; i < indent; i++) {
                builder.append("    ");
            }
        }
    }

    private void incrementIndent() {
        if (prettyPrint) {
            indent++;
        }
    }

    private void decrementIndent() {
        if (prettyPrint) {
            indent--;
        }
    }

    private void newLine(StringBuilder builder) {
        if (prettyPrint) {
            builder.append("\n");
        }
    }

}
