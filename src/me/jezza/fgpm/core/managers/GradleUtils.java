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

    public static String formatMethod(String method) {
        String[] lineSplit = method.split(System.lineSeparator());
        StringBuilder formatted = new StringBuilder();

        int depth = 0;
        boolean openingBrace = false;
        for (String str : lineSplit) {
            if (openingBrace)
                depth++;
            openingBrace = str.contains("{");
            if (str.contains("}"))
                depth--;
            if (depth > 0)
                for (int i = 0; i < depth * 4; i++)
                    formatted.append(" ");
            formatted.append(str);
            formatted.append(System.lineSeparator());
        }

        return formatted.toString();
    }

}
