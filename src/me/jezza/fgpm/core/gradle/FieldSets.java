package me.jezza.fgpm.core.gradle;

import com.google.common.collect.Lists;
import me.jezza.fgpm.mod.ModState;
import me.jezza.fgpm.core.Utils;
import me.jezza.fgpm.core.managers.GradleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldSets {

    private List<String> fields;

    public FieldSets() {
        this.fields = Lists.newArrayList();
    }

    public FieldSets(String... fields) {
        this.fields = Lists.newArrayList(fields);
    }

    public void setFields(ModState modState) {
        fields.clear();
        fields.add(modState.version.toString());
        fields.add(modState.groupName);
        fields.add(modState.modID);
    }

    public String getWrittenState() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            sb.append(FieldPattern.class.getEnumConstants()[i].name());
            sb.append(" = \"");
            sb.append(fields.get(i));
            sb.append("\"");
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String[] getWrittenStateFormat() {
        return getWrittenState().split(System.lineSeparator());
    }

    public void writeIntoFile(List<String> file) {
        String[] formatted = getWrittenStateFormat();
        for (FieldPattern fieldPattern : FieldPattern.values())
            for (int i = 0; i < file.size(); i++) {
                String line = file.get(i);
                if (line.startsWith(fieldPattern.name())) {
                    file.set(i, formatted[fieldPattern.ordinal()]);
                    break;
                }
            }
    }

    public static FieldSets createFromFile(List<String> file) {
        List<String> fields = getFieldsFromFile(file);
        int fieldSize = FieldPattern.size();
        if (fields.size() != fieldSize)
            return generateDefaultFields();
        String[] tempFields = new String[fieldSize];
        for (int i = 0; i < fieldSize; i++)
            tempFields[i] = GradleUtils.getValueFromField(fields.get(i));
        return new FieldSets(tempFields);
    }

    public static FieldSets generateDefaultFields() {
        return new FieldSets("1.0.0", "com.example.mod", "modID");
    }

    public static boolean existsInFile(List<String> file) {
        return getFieldsFromFile(file).size() == FieldPattern.size();
    }

    private static List<String> getFieldsFromFile(List<String> file) {
        List<String> fieldList = new ArrayList<String>();
        String compiled = Utils.compileList(file);
        for (FieldPattern pattern : FieldPattern.values()) {
            Matcher matcher = pattern.matcher(compiled);
            if (matcher.find())
                fieldList.add(matcher.group(0));
        }
        return fieldList;
    }

    private static enum FieldPattern {
        version,
        group,
        archivesBaseName;

        private final Pattern pattern;

        private FieldPattern() {
            pattern = Pattern.compile(name() + " ?= ?[\"'].*?[\"']");
        }

        public Matcher matcher(String line) {
            return pattern.matcher(line);
        }

        public static int size() {
            return FieldPattern.values().length;
        }

    }
}
