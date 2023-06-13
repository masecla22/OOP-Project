package nl.rug.oop.rugson.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import lombok.NonNull;

/**
 * This class is responsible for reading JSON from an input stream.
 * It will convert the JSON string into a list of JsonValues.
 */
public class JsonReader {
    @NonNull
    private PushbackInputStream inputStream;

    private Stack<Character> bracketStack = new Stack<>();

    private List<JsonValue> values = new ArrayList<>();

    private JsonValue lastValue;

    /**
     * Creates a new JsonReader.
     * 
     * @param stream - the input stream to read from
     */
    public JsonReader(InputStream stream) {
        // We need to be able to push back characters to the stream, so we use
        // PushbackInputStream
        this.inputStream = new PushbackInputStream(stream);
    }

    /**
     * Reads the next value from the input stream.
     * 
     * @return - the next value
     * @throws IOException - if the input stream throws an exception
     */
    public JsonValue nextValue() throws IOException {
        if (lastValue != null && lastValue.getType().equals(JsonToken.EOF)) {
            return null;
        }

        if (values.isEmpty()) {
            return ingestNextValue();
        } else {
            return values.remove(0);
        }
    }

    /**
     * Peeks at the next value from the input stream.
     * 
     * @return - the next value
     * @throws IOException - if the input stream throws an exception
     */
    public JsonValue peek() throws IOException {
        if (values.isEmpty()) {
            JsonValue value = ingestNextValue();
            values.add(value);
            return value;
        } else {
            return values.get(0);
        }
    }

    /**
     * Reads all values from the input stream until the end.
     * 
     * @return - the list of values
     */
    public List<JsonValue> ingestEverything() {
        List<JsonValue> result = new ArrayList<JsonValue>();
        JsonValue nextValue;
        try {
            while ((nextValue = ingestNextValue()) != null) {
                result.add(nextValue);
                if (nextValue.getType().equals(JsonToken.EOF)) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private void discardWhiteSpaceAndComma() throws IOException {
        char initialChar = (char) -1;
        do {
            initialChar = (char) inputStream.read();
        } while (Character.isWhitespace(initialChar) || initialChar == ',');
        inputStream.unread(initialChar);
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
        while (true) {
            initialChar = (char) inputStream.read();
            if (Character.isLetterOrDigit(initialChar)) {
                builder.append(initialChar);
            } else {
                break;
            }
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
                throwInvalidCharacter(c);
            }
        }

        discardWhiteSpaceAndComma();
    }

    private String nextNumber(char initialCharacter) throws IOException {
        StringBuilder builder = new StringBuilder().append(initialCharacter);

        Set<Character> validChars = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-', '+', 'e', 'E');
        boolean hasDecimalPoint = false, hasExponent = false;

        char lastCharacter = 0;
        while (true) {
            initialCharacter = (char) inputStream.read();
            if (!validChars.contains(initialCharacter)) {
                break;
            }

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
                    throwInvalidCharacter(c);
                }
            }

            // Discard all whitespace and the ,
            discardWhiteSpaceAndComma();
            return true;
        } else if (initialCharacter == 'f') {
            char[] expected = new char[] { 'a', 'l', 's', 'e' };
            for (char c : expected) {
                if (c != (char) inputStream.read()) {
                    throwInvalidCharacter(c);
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
        if (lastValue != null && lastValue.getType().equals(JsonToken.EOF)) {
            return null;
        }
        if (this.inputStream == null) {
            JsonValue result = new JsonValue(JsonToken.EOF, null);
            lastValue = result;
            return result;
        }

        char c = nextNonWhiteSpace();

        JsonValue result = null;
        if (c == '}') {
            result = handleClosingBracket();
        } else if (Character.isLetter(c)) {
            result = handleLetter(c);
        } else if (c == '{') {
            result = handleOpeningBracket(c);
        } else if (c == '[') {
            result = handleOpeningBrace(c);
        } else if (c == ']') {
            result = handleClosingBrace();
        } else if (Character.isDigit(c) || c == '-') {
            result = handleDigitOrMinus(c);
        } else if (c == '"') {
            result = handleQuote(c);
        } else {
            throwInvalidCharacter(c);
        }

        return result;
    }

    private void throwInvalidCharacter(char c) throws IOException {
        throw new IOException("Unexpected character " + c);
    }

    private JsonValue handleQuote(char c) throws IOException {
        if (lastValue != null) {
            if (!lastValue.getType().equals(JsonToken.NAME) &&
                    !lastValue.getType().equals(JsonToken.END_OBJECT) &&
                    !lastValue.getType().equals(JsonToken.END_ARRAY) &&
                    !lastValue.getType().equals(JsonToken.START_ARRAY) &&
                    !lastValue.getType().equals(JsonToken.STRING) &&
                    !lastValue.getType().equals(JsonToken.NULL) &&
                    !lastValue.getType().equals(JsonToken.BOOLEAN) &&
                    !lastValue.getType().equals(JsonToken.NUMBER)) {
                throwInvalidCharacter(c);
            }
        }

        String string = nextString();
        JsonValue result = new JsonValue(JsonToken.STRING, string);
        lastValue = result;
        return result;
    }

    private JsonValue handleDigitOrMinus(char c) throws IOException {
        if (lastValue != null) {
            if (!lastValue.getType().equals(JsonToken.NAME) &&
                    !lastValue.getType().equals(JsonToken.NUMBER) &&
                    !lastValue.getType().equals(JsonToken.END_OBJECT) &&
                    !lastValue.getType().equals(JsonToken.END_ARRAY) &&
                    !lastValue.getType().equals(JsonToken.START_ARRAY)) {
                throwInvalidCharacter(c);
            }
        }

        String number = nextNumber(c);
        JsonValue result = new JsonValue(JsonToken.NUMBER, number);
        lastValue = result;
        return result;
    }

    private JsonValue handleClosingBrace() throws IOException {
        if (this.bracketStack.size() == 0) {
            throwInvalidCharacter(']');
        }
        char lastBracket = this.bracketStack.pop();

        if (lastBracket != '[') {
            throwInvalidCharacter(lastBracket);
        }

        if (this.bracketStack.size() == 0) {
            this.inputStream = null;
        } else {
            discardWhiteSpaceAndComma();
        }

        JsonValue result = new JsonValue(JsonToken.END_ARRAY, null);
        lastValue = result;
        return result;
    }

    private JsonValue handleOpeningBrace(char c) {
        bracketStack.push(c);
        JsonValue result = new JsonValue(JsonToken.START_ARRAY, null);
        lastValue = result;
        return result;
    }

    private JsonValue handleOpeningBracket(char c) throws IOException {
        if (lastValue != null) {
            if (!lastValue.getType().equals(JsonToken.NAME) &&
                    !lastValue.getType().equals(JsonToken.END_OBJECT) &&
                    !lastValue.getType().equals(JsonToken.END_ARRAY) &&
                    !lastValue.getType().equals(JsonToken.START_ARRAY)) {
                throwInvalidCharacter('{');
            }
        }

        this.bracketStack.push(c);
        JsonValue result = new JsonValue(JsonToken.START_OBJECT, null);
        lastValue = result;
        return result;
    }

    private JsonValue handleLetter(char c) throws IOException {
        if (lastValue == null) {
            throwInvalidCharacter(c);
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
                throwInvalidCharacter(c);
            }
        }

        String fieldName = nextFieldName(c);
        JsonValue result = new JsonValue(JsonToken.NAME, fieldName);
        lastValue = result;
        return result;
    }

    private JsonValue handleClosingBracket() throws IOException {
        if (this.bracketStack.size() == 0) {
            throwInvalidCharacter('}');
        }

        char lastBracket = this.bracketStack.pop();
        if (lastBracket != '{') {
            throwInvalidCharacter(lastBracket);
        }

        if (this.bracketStack.size() == 0) {
            this.inputStream = null;
        } else {
            discardWhiteSpaceAndComma();
        }

        JsonValue result = new JsonValue(JsonToken.END_OBJECT, null);
        lastValue = result;
        return result;
    }
}
