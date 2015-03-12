package me.jezza.fgpm.core.gradle.reader;

import me.jezza.fgpm.core.exceptions.GradleSyntaxException;
import me.jezza.fgpm.core.exceptions.ParseException;
import me.jezza.fgpm.core.gradle.builders.ComponentBuilder;
import me.jezza.fgpm.core.gradle.lib.IGradleComponent;
import org.apache.commons.io.IOUtils;

import java.io.*;

import static me.jezza.fgpm.core.gradle.lib.ElementTypes.*;

public class GradleReader {

    public static final char NEW_LINE = '\n';

    private int position = 0;
    private int elementIndex = 0;

    private CharBuffer dataBuffer;
    private IndexBuffer elementBuffer;
    private Reader in;

    public GradleReader(String string) {
        this(new StringReader(string));
    }

    public GradleReader(File file) throws FileNotFoundException {
        this(new FileReader(file));
    }

    public GradleReader(Reader in) {
        if (in == null)
            throw new NullPointerException("Reader is null");
        this.in = in;
        this.dataBuffer = new CharBuffer();
    }

    public IGradleComponent[] read() {
        String input;
        try {
            input = IOUtils.toString(in);
        } catch (IOException e) {
            throw new ParseException("Failed to parse file!", e);
        }

        dataBuffer.data = input.toCharArray();
        dataBuffer.length = dataBuffer.data.length;

        elementBuffer = new IndexBuffer(dataBuffer.length, true);

        position = 0;
        for (; position < dataBuffer.length; position++) {
            switch (dataBuffer.data[position]) {
                case 'a':
                case 'A':
                case 'b':
                case 'B':
                case 'c':
                case 'C':
                case 'd':
                case 'D':
                case 'e':
                case 'E':
                case 'f':
                case 'F':
                case 'g':
                case 'G':
                case 'h':
                case 'H':
                case 'i':
                case 'I':
                case 'j':
                case 'J':
                case 'k':
                case 'K':
                case 'l':
                case 'L':
                case 'm':
                case 'M':
                case 'n':
                case 'N':
                case 'o':
                case 'O':
                case 'p':
                case 'P':
                case 'q':
                case 'Q':
                case 'r':
                case 'R':
                case 's':
                case 'S':
                case 't':
                case 'T':
                case 'u':
                case 'U':
                case 'v':
                case 'V':
                case 'w':
                case 'W':
                case 'x':
                case 'X':
                case 'y':
                case 'Y':
                case 'z':
                case 'Z':
                    consumeTextBlock();
                    elementIndex++;
                    break;
                case '}':
                    setElementDataLength1(elementIndex++, METHOD_END, position);
                    break;
                case ')':
                    setElementDataLength1(elementIndex++, FUNCTION_END, position);
                    break;
                case '-':
                    consumeLambda();
                    break;
                case '\'':
                case '"':
                    consumeString();
                    elementIndex++;
                    break;
                case '/':
                    skipComment();
                    break;
            }
        }
        elementBuffer.count = elementIndex;

        GradleNavigator navigator = new GradleNavigator(dataBuffer, elementBuffer, -1);
        ComponentBuilder builder = navigator.builder();

//        App.LOG.info(elementIndex);
//        while (navigator.hasNext()) {
//            navigator.next();
//            App.LOG.info(navigator.asString());
//        }

//        IGradleComponent sourceSets = builder.findComponent("sourceSets");

//        IGradleComponent test = builder.find("sourceSets");
//        test.isNamespace();
//        test.isMethod();
//            test.isLambda();
//        test.isFunction();
//        test.isString();
//            test.isField();
//            test.isApplier();

//        EQUAL = 6;
//        COLON = 7;
//        STRING = 8;
//        COMMA = 9;
//        LAMBDA = 10;
//        CONCAT = 11;
        return builder.build();
    }

    private void consumeTextBlock() {
        int tempPos = position;
        boolean endOfBlock = false;
        while (!endOfBlock) {
//            ++tempPos;
//            App.LOG.warn(dataBuffer.data[tempPos]);
//            switch (dataBuffer.data[tempPos]) {
            switch (dataBuffer.data[++tempPos]) {
                case NEW_LINE:
                    consumeNamespace(tempPos);
                    endOfBlock = true;
                    continue;
                case '{':
                    consumeMethod(tempPos);
                    endOfBlock = true;
                    break;
                case '=':
                    consumeField(tempPos);
                    endOfBlock = true;
                    break;
                case '\'':
                case '"':
                    consumeNamespace(tempPos);
                    endOfBlock = true;
                    tempPos--;
                    break;
                case '(':
                    consumeFunction(tempPos);
                    endOfBlock = true;
                    break;
                case ')':
                    consumeFunctionEnd(tempPos);
                    endOfBlock = true;
                    break;
                case ',':
                    consumeComma(tempPos);
                    endOfBlock = true;
                    break;
                case ':':
                    consumeColon(tempPos);
                    endOfBlock = true;
                    break;
            }
        }
        this.position = tempPos;
    }

    private void consumeString() {
        int tempPos = position;
        boolean endOfStringFound = false;
        while (!endOfStringFound) {
            switch (dataBuffer.data[++tempPos]) {
                case '\'':
                case '"':
                    // TODO: Make sure escaping works correctly.
                    endOfStringFound = dataBuffer.data[tempPos - 1] != '\\';
                    break;
            }
        }
        setElementData(elementIndex, STRING, position, tempPos - position + 1);
        this.position = tempPos;
    }

    private void consumeLambda() {
        if (position + 1 > dataBuffer.length)
            throwException(position);

        position += 1;
        if (dataBuffer.data[position] != '>')
            throwException(position);

        setElementData(elementIndex++, LAMBDA, position - 1, 2);
    }

    private void skipComment() {
        if (position + 1 > dataBuffer.length) {
            position = dataBuffer.length;
            return;
        }
        position += 1;

        switch (dataBuffer.data[position]) {
            case '*':
                skipBlockComment();
                break;
            case '/':
                skipLineComment();
                break;
            default:
                throwException(position);
        }
    }

    private void skipBlockComment() {
        int tempPos = position;
        boolean endOfCommentFound = false;
        while (!endOfCommentFound) {
            switch (dataBuffer.data[++tempPos]) {
                case '*':
                    endOfCommentFound = dataBuffer.data[tempPos + 1] == '/';
                    break;
            }
        }
        this.position = tempPos;
    }

    private void skipLineComment() {
        int tempPos = position;
        boolean endOfCommentFound = false;
        while (!endOfCommentFound) {
            switch (dataBuffer.data[++tempPos]) {
                case NEW_LINE:
                    endOfCommentFound = true;
                    break;
            }
        }
        this.position = tempPos;
    }

    private void consumeNamespace(int position) {
        setElementData(elementIndex, NAMESPACE, this.position, position - this.position);
    }

    private void consumeMethod(int position) {
        setElementData(elementIndex, METHOD_START, this.position, position - this.position + 1);
    }

    private void consumeFunction(int position) {
        setElementData(elementIndex, FUNCTION_START, this.position, position - this.position + 1);
    }

    private void consumeFunctionEnd(int position) {
        consumeNamespace(position);
        setElementDataLength1(++elementIndex, FUNCTION_END, position);
    }

    private void consumeColon(int position) {
        setElementData(elementIndex, COLON, this.position, position - this.position + 1);
    }

    private void consumeField(int position) {
        setElementData(elementIndex, EQUAL, this.position, position - this.position + 1);
    }

    private void consumeComma(int position) {
        setElementData(elementIndex, COMMA, this.position, position - this.position + 1);
    }

    private void setElementDataLength1(int index, byte type, int position) {
        elementBuffer.type[index] = type;
        elementBuffer.position[index] = position;
        elementBuffer.length[index] = 1;
    }

    private void setElementData(int index, byte type, int position, int length) {
        this.elementBuffer.type[index] = type;
        this.elementBuffer.position[index] = position;
        this.elementBuffer.length[index] = length;
    }

    private void throwException(int position) {
        throw new GradleSyntaxException("Invalid syntax! Current index position: " + getPositionString(position));
    }

    private void throwException(int position, String message) {
        throw new GradleSyntaxException("Invalid syntax! Current index position: " + getPositionString(position));
    }

    private void throwException(int position, String message, Throwable throwable) {
        throw new GradleSyntaxException(message + " Current index position: " + getPositionString(position));
    }

    private String getPositionString() {
        return getPositionString(position);
    }

    private String getPositionString(int position) {
        int line = 1;
        int index = 0;

        for (int i = 0; i < position; i++) {
            if (dataBuffer.data[i] == NEW_LINE) {
                line++;
                index = 0;
            } else
                index++;
        }

        return String.format("Line #%s, Char #%s", line, index);
    }
}
