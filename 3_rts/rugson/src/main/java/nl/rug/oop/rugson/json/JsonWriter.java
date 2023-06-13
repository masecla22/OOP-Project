package nl.rug.oop.rugson.json;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonWriter {
    private final boolean prettyPrint;

    private int indent = 0;

    public String print(List<JsonValue> values) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            JsonValue value = values.get(i);
            if (value.getType().equals(JsonToken.START_OBJECT)) {
                indent(builder);
                builder.append("{");
                newLine(builder);
                incrementIndent();
            } else if (value.getType().equals(JsonToken.END_OBJECT)) {
                decrementIndent();
                newLine(builder);
                indent(builder);
                builder.append("}");
                addCommaIfNeeded(builder, values.get(i + 1).getType());
            } else if (value.getType().equals(JsonToken.START_ARRAY)) {
                builder.append("[");
                incrementIndent();
                newLine(builder);
            } else if (value.getType().equals(JsonToken.END_ARRAY)) {
                decrementIndent();
                newLine(builder);
                indent(builder);
                builder.append("]");
                addCommaIfNeeded(builder, values.get(i + 1).getType());
            } else if (value.getType().equals(JsonToken.NAME)) {
                indent(builder);
                builder.append(value.getValue() + ":");
                if (prettyPrint) {
                    builder.append(" ");
                }
            } else if (value.getType().equals(JsonToken.BOOLEAN) || value.getType().equals(JsonToken.NULL)
                    || value.getType().equals(JsonToken.NUMBER)) {
                if (!values.get(i - 1).getType().equals(JsonToken.NAME))
                    indent(builder);
                builder.append(value.getValue());
                addCommaIfNeeded(builder, values.get(i + 1).getType());
            } else if (value.getType().equals(JsonToken.STRING)) {
                if (!values.get(i - 1).getType().equals(JsonToken.NAME))
                    indent(builder);

                String stringValue = (String) value.getValue();
                builder.append("\"" + escape(stringValue) + "\"");
                addCommaIfNeeded(builder, values.get(i + 1).getType());
            }
        }

        return builder.toString();
    }

    private String escape(String s) {
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
