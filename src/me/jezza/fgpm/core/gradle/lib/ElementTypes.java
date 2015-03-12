package me.jezza.fgpm.core.gradle.lib;

public class ElementTypes {

    public static final byte NAMESPACE = 1;
    public static final byte METHOD_START = 2;
    public static final byte METHOD_END = 3;
    public static final byte FUNCTION_START = 4;
    public static final byte FUNCTION_END = 5;
    public static final byte EQUAL = 6;
    public static final byte COLON = 7;
    public static final byte STRING = 8;
    public static final byte COMMA = 9;
    public static final byte LAMBDA = 10;
    public static final byte CONCAT = 11;
    /**
     * NAMESPACE: just a block of letters, no defined meaning yet.
     * METHOD_NAME: It wasn't correctly formatted, so it managed to find a connecting brace. Also functions as METHOD_START
     * METHOD_START: Start of a method body.
     * METHOD_END: End of a method body.
     * FUNCTION_START: Start of a function call.
     * FUNCTION_END: End of a function call.
     * EQUAL: equals sign.
     * COLON: colon.
     * STRING: NAMESPACE within quotes. ["'] Either or.
     * COMMA: comma.
     */

}
