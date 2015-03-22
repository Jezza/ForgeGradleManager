package me.jezza.fgpm.core.gradle.reader;

import me.jezza.fgpm.core.exceptions.GradleSyntaxException;
import me.jezza.fgpm.core.gradle.builders.ComponentBuilder;
import me.jezza.fgpm.core.gradle.lib.ElementTypes;

import static me.jezza.fgpm.core.gradle.lib.ElementTypes.*;

public class GradleNavigator {

    private CharBuffer dataBuffer;
    private IndexBuffer elementBuffer;
    private int elementIndex;

    private int startIndex;
    private ComponentBuilder builder;

    public GradleNavigator(CharBuffer dataBuffer, IndexBuffer elementBuffer) {
        this(dataBuffer, elementBuffer, 0);
    }

    public GradleNavigator(CharBuffer dataBuffer, IndexBuffer elementBuffer, int elementIndex) {
        this.dataBuffer = dataBuffer;
        this.elementBuffer = elementBuffer;
        this.elementIndex = elementIndex;
        this.startIndex = elementIndex;
        builder = new ComponentBuilder(this);
    }

    public boolean hasNext() {
        return elementIndex < this.elementBuffer.count - 1;
    }

    public boolean hasPrevious() {
        return elementIndex > 0;
    }

    public void next() {
        if (!hasNext())
            throw new IndexOutOfBoundsException("No element found: " + elementIndex);
        elementIndex++;
    }

    public void previous() {
        if (!hasPrevious())
            throw new IndexOutOfBoundsException("No element found: " + elementIndex);
        elementIndex--;
    }

    public void resetPosition() {
        elementIndex = startIndex;
    }

    public int position() {
        return elementBuffer.position[elementIndex];
    }

    public int length() {
        return elementBuffer.length[elementIndex];
    }

    public byte type() {
        return elementBuffer.type[elementIndex];
    }

    public String cursorPosition() {
        int line = 1;
        int index = 0;

        for (int i = 0; i < position(); i++) {
            if (dataBuffer.data[i] == GradleReader.NEW_LINE) {
                line++;
                index = 0;
            } else
                index++;
        }

        return String.format("Line #%s, Char #%s", line, index);
    }

    public boolean isEqual(String target) {
        int pos = position();
        for (int j = 0; j < length(); j++)
            if (target.charAt(j) != dataBuffer.data[pos + j])
                return false;
        return true;
    }

    /**
     * @return the number of components that reside on the same depth inside of the method.
     */
    public int countMethodComponents() {
        int tempIndex = elementIndex + 1;
        int count = 0;
        int depth = 0;
        for (; ; tempIndex++) {
            if (tempIndex >= elementBuffer.type.length)
                throw new GradleSyntaxException("Something went parsing methods...");

            byte type = elementBuffer.type[tempIndex];
            if (depth == 0 && type == METHOD_END)
                break;

            switch (type) {
                case FUNCTION_START:
                case METHOD_START:
                    depth++;
                    break;
                case FUNCTION_END:
                case METHOD_END:
                    depth--;
            }
            if (depth == 0)
                count++;
        }
        return count;
    }

    public int countMethodComponentsNoDepth() {
        int tempIndex = elementIndex + 1;
        int count = 0;
        int depth = 0;
        for (; ; tempIndex++) {
            if (tempIndex >= elementBuffer.type.length)
                throw new GradleSyntaxException("Something went parsing methods...");

            byte type = elementBuffer.type[tempIndex];
            if (depth == 0 && type == METHOD_END)
                break;

            switch (type) {
                case FUNCTION_START:
                case METHOD_START:
                    depth++;
                    break;
                case FUNCTION_END:
                case METHOD_END:
                    depth--;
            }
            count++;
        }
        return count;
    }

    /**
     * @return the number of components that reside inside of a function call.
     */
    public int countFunctionComponents() {
        int count = 0;
        int tempIndex = elementIndex + 1;
        byte type = elementBuffer.type[tempIndex];
        while (type != FUNCTION_END) {
            switch (type) {
                case COLON:
                case COMMA:
                    type = elementBuffer.type[++tempIndex];
                    continue;
            }
            count++;
            type = elementBuffer.type[++tempIndex];
        }
        return count;
    }

    public String typeString() {
        switch (type()) {
            case ElementTypes.NAMESPACE:
                return "NAMESPACE";
            case ElementTypes.METHOD_START:
                return "METHOD_START";
            case ElementTypes.METHOD_END:
                return "METHOD_END";
            case ElementTypes.FUNCTION_START:
                return "FUNCTION_START";
            case ElementTypes.FUNCTION_END:
                return "FUNCTION_END";
            case ElementTypes.EQUAL:
                return "EQUAL";
            case ElementTypes.COLON:
                return "COLON";
            case ElementTypes.STRING:
                return "STRING";
            case ElementTypes.COMMA:
                return "COMMA";
            case ElementTypes.LAMBDA:
                return "LAMBDA";
            case ElementTypes.CONCAT:
                return "CONCAT";
        }
        return "UNDEFINED";
    }

    public String asString() {
        int pos = position();
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < length(); j++)
            builder.append(dataBuffer.data[pos + j]);
        return builder.toString();
    }

    public String asNamespace() {
        int pos = position();
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < length(); j++) {
            char c = dataBuffer.data[pos + j];
            if (!Character.isAlphabetic(c))
                return builder.toString();
            builder.append(c);
        }
        return builder.toString();
    }

    public boolean asBoolean() {
        return type() == NAMESPACE && Boolean.parseBoolean(asString());
    }

    public ComponentBuilder builder() {
        return builder;
    }
}
