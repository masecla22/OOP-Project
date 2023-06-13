package nl.rug.oop.rugson.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import lombok.NonNull;

public class JsonReader {
    @NonNull
    private PushbackInputStream inputStream;

    private Stack<Character> bracketStack = new Stack<>();

    private List<JsonValue> values = new ArrayList<>();

    private JsonValue lastValue;

    public JsonReader(InputStream stream) {
        this.inputStream = new PushbackInputStream(stream);
    }

    private void discardWhiteSpaceAndComma() throws IOException {
        char initialChar = (char) -1;
        do {
            initialChar = (char) inputStream.read();
        } while (Character.isWhitespace(initialChar) || initialChar == ',');
        inputStream.unread(initialChar);
    }

    public JsonValue nextValue() throws IOException {
        if (lastValue != null && lastValue.getType().equals(JsonToken.EOF))
            return null;

        if (values.isEmpty()) {
            return ingestNextValue();
        } else {
            return values.remove(0);
        }
    }

    public JsonValue peek() throws IOException {
        if (values.isEmpty()) {
            JsonValue value = ingestNextValue();
            values.add(value);
            return value;
        } else {
            return values.get(0);
        }
    }

    private char nextNonWhiteSpace() throws IOException {
        char c = (char) -1;
        do {
            c = (char) inputStream.read();
        } while (Character.isWhitespace(c));
        return c;
    }

    private String nextFieldName(char initialChar) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(initialChar);
        while (Character.isLetterOrDigit(initialChar = (char) inputStream.read())) {
            builder.append(initialChar);
        }

        // Discard all whitespace and the :
        do {
            initialChar = (char) inputStream.read();
        } while (Character.isWhitespace(initialChar) || initialChar == ':');
        this.inputStream.unread(initialChar);

        return builder.toString();
    }

    private void nextNull() throws IOException {
        // Discard 3 characters
        // (the 'u', the 'l', and the 'l')
        char[] expected = new char[] { 'u', 'l', 'l' };

        for (char c : expected) {
            if (c != (char) inputStream.read()) {
                throw new IOException("Unexpected character " + c);
            }
        }

        discardWhiteSpaceAndComma();
    }

    private String nextNumber(char initialCharacter) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(initialCharacter);

        Set<Character> validChars = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-', '+', 'e', 'E');
        boolean hasDecimalPoint = false;
        boolean hasExponent = false;

        char lastCharacter = 0;
        while (validChars.contains(initialCharacter = (char) inputStream.read())) {
            if (initialCharacter == '.') {
                if (hasDecimalPoint) {
                    throw new IOException("Invalid number " + builder.toString());
                } else {
                    hasDecimalPoint = true;
                }
            } else if (initialCharacter == 'e' || initialCharacter == 'E') {
                if (hasExponent) {
                    throw new IOException("Invalid number " + builder.toString());
                } else {
                    hasExponent = true;
                }
            } else if (initialCharacter == '+' || initialCharacter == '-') {
                if ((lastCharacter != 'e' && lastCharacter != 'E') || lastCharacter == 0) {
                    throw new IOException("Invalid number " + builder.toString());
                }
            }

            lastCharacter = initialCharacter;
            builder.append(initialCharacter);
        }

        this.inputStream.unread(initialCharacter);
        discardWhiteSpaceAndComma();

        return builder.toString();
    }

    private boolean nextBoolean(char initialCharacter) throws IOException {
        if (initialCharacter == 't') {
            char[] expected = new char[] { 'r', 'u', 'e' };
            for (char c : expected) {
                if (c != (char) inputStream.read()) {
                    throw new IOException("Unexpected character " + c);
                }
            }

            // Discard all whitespace and the ,
            discardWhiteSpaceAndComma();
            return true;
        } else if (initialCharacter == 'f') {
            char[] expected = new char[] { 'a', 'l', 's', 'e' };
            for (char c : expected) {
                if (c != (char) inputStream.read()) {
                    throw new IOException("Unexpected character " + c);
                }
            }

            // Discard all whitespace and the ,
            discardWhiteSpaceAndComma();
            return false;
        } else {
            throw new IOException("Unexpected character " + initialCharacter);
        }
    }

    private String nextString() throws IOException {
        StringBuilder builder = new StringBuilder();

        char initialCharacter = (char) inputStream.read();
        do {
            if (initialCharacter == '\\') {
                char nextCharacter = (char) inputStream.read();
                if (nextCharacter == 'u') {
                    char[] hexChars = new char[4];
                    for (int i = 0; i < 4; i++) {
                        hexChars[i] = (char) inputStream.read();
                    }
                    String hexString = new String(hexChars);
                    int hexValue = Integer.parseInt(hexString, 16);
                    builder.append((char) hexValue);
                } else {
                    builder.append(nextCharacter);
                }
            } else {
                builder.append(initialCharacter);
            }
            initialCharacter = (char) inputStream.read();
        } while (initialCharacter != '"');

        discardWhiteSpaceAndComma();
        return builder.toString();
    }

    private JsonValue ingestNextValue() throws IOException {
        if (lastValue != null && lastValue.getType().equals(JsonToken.EOF))
            return null;
        if (this.inputStream == null) {
            JsonValue result = new JsonValue(JsonToken.EOF, null);
            lastValue = result;
            return result;
        }

        char c = nextNonWhiteSpace();

        if (c == '}') {
            if (this.bracketStack.size() == 0)
                throw new IOException("Unexpected character }");
            char lastBracket = this.bracketStack.pop();
            if (lastBracket != '{')
                throw new IOException("Unexpected character } (expected " + lastBracket + ")");

            if (this.bracketStack.size() == 0)
                this.inputStream = null;
            else
                discardWhiteSpaceAndComma();
            JsonValue result = new JsonValue(JsonToken.END_OBJECT, null);
            lastValue = result;
            return result;
        } else if (Character.isLetter(c)) {
            if (lastValue == null) {
                throw new IOException("Unexpected character " + c);
            }

            if (lastValue.getType().equals(JsonToken.NAME)) {
                if (c == 'n') {
                    nextNull();
                    JsonValue result = new JsonValue(JsonToken.NULL, null);
                    lastValue = result;
                    return result;
                } else if (c == 't' || c == 'f') {
                    boolean value = nextBoolean(c);
                    JsonValue result = new JsonValue(JsonToken.BOOLEAN, value);
                    lastValue = result;
                    return result;
                } else {
                    throw new IOException("Unexpected character " + c);
                }
            }

            String fieldName = nextFieldName(c);
            JsonValue result = new JsonValue(JsonToken.NAME, fieldName);
            lastValue = result;
            return result;
        } else if (c == '{') {
            if (lastValue != null) {
                if (!lastValue.getType().equals(JsonToken.NAME) &&
                        !lastValue.getType().equals(JsonToken.END_OBJECT) &&
                        !lastValue.getType().equals(JsonToken.END_ARRAY) &&
                        !lastValue.getType().equals(JsonToken.START_ARRAY)) {
                    throw new IOException("Unexpected character {");
                }
            }

            this.bracketStack.push(c);
            JsonValue result = new JsonValue(JsonToken.START_OBJECT, null);
            lastValue = result;
            return result;
        } else if (c == '[') {
            bracketStack.push(c);
            JsonValue result = new JsonValue(JsonToken.START_ARRAY, null);
            lastValue = result;
            return result;
        } else if (c == ']') {
            if (this.bracketStack.size() == 0)
                throw new IOException("Unexpected character ]");
            char lastBracket = this.bracketStack.pop();

            if (lastBracket != '[')
                throw new IOException("Unexpected character ]");

            if (this.bracketStack.size() == 0)
                this.inputStream = null;
            else
                discardWhiteSpaceAndComma();

            JsonValue result = new JsonValue(JsonToken.END_ARRAY, null);
            lastValue = result;
            return result;
        } else if (Character.isDigit(c) || c == '-') {
            if (lastValue != null) {
                if (!lastValue.getType().equals(JsonToken.NAME) &&
                        !lastValue.getType().equals(JsonToken.NUMBER) &&
                        !lastValue.getType().equals(JsonToken.END_OBJECT) &&
                        !lastValue.getType().equals(JsonToken.END_ARRAY) &&
                        !lastValue.getType().equals(JsonToken.START_ARRAY)) {
                    throw new IOException("Unexpected character " + c);
                }
            }

            String number = nextNumber(c);
            JsonValue result = new JsonValue(JsonToken.NUMBER, number);
            lastValue = result;
            return result;
        } else if (c == '"') {
            if (lastValue != null) {
                if (!lastValue.getType().equals(JsonToken.NAME) &&
                        !lastValue.getType().equals(JsonToken.END_OBJECT) &&
                        !lastValue.getType().equals(JsonToken.END_ARRAY)) {
                    throw new IOException("Unexpected character " + c);
                }
            }

            String string = nextString();
            JsonValue result = new JsonValue(JsonToken.STRING, string);
            lastValue = result;
            return result;
        } else {
            throw new IOException("Unexpected character " + c);
        }
    }
}
