package me.jezza.fgpm.core.managers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GradleUtils {

    private static final Pattern valuePattern = Pattern.compile("[\"'].*?[\"']");

    private GradleUtils() {
    }

    public static String getValueFromField(String line) {
        Matcher matcher = valuePattern.matcher(line);
        if (!matcher.find())
            return "";
        String value = matcher.group(0);
        return value.substring(1, value.length() - 1);
    }

    /**
     * public static void main(String[] args) {
     * }
     */
    public static String formatMethod(String method) {
        method = method.replaceAll(System.lineSeparator(), "");
        method = method.replaceAll("\n", "");
        StringBuilder formatted = new StringBuilder();

        int indentDepth = 4;

        int depth = 0;
        boolean opening = true;
        for (int i = 0; i < method.length(); i++) {
            char c = method.charAt(i);
            int k = i + 1;
            char peek = k >= method.length() ? c : method.charAt(k);

            switch (c) {
                case '{':
                    formatted.append(c);
                    formatted.append(System.lineSeparator());
                    depth++;
                    if (depth > 0)
                        for (int j = 0; j < depth * indentDepth; j++)
                            formatted.append(" ");
                    continue;
                case '}':
                    depth--;
                    formatted.append(System.lineSeparator());
                    if (depth > 0)
                        for (int j = 0; j < depth * indentDepth; j++)
                            formatted.append(" ");
                    formatted.append(c);
                    if (peek == '}')
                        continue;
                    formatted.append(System.lineSeparator());
                    if (depth > 0)
                        for (int j = 0; j < depth * indentDepth; j++)
                            formatted.append(" ");
                    continue;
                case '"':
                    if (opening) {
                        formatted.append(c);
                        opening = false;
                        continue;
                    }
                    formatted.append(c);
                    formatted.append(System.lineSeparator());
                    opening = true;
                    continue;
                default:
                    formatted.append(c);
            }
        }
        return formatted.toString();
    }

}
