package me.jezza.fgpm.core.managers.gradle;

import me.jezza.fgpm.core.ModState;
import me.jezza.fgpm.core.Utils;
import me.jezza.fgpm.core.managers.GradleUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceSets {
    private static final Pattern pattern = Pattern.compile("sourceSets\\s*\\{\\s*main\\s*\\{\\s*java\\s*\\{\\s*srcDir \".*\"\\s*}\\s*resources\\s*\\{\\s*srcDir \".*\"\\s*}\\s*}\\s*}");
    private static final Pattern srcPattern = Pattern.compile("srcDir \".*?\"");

    private String javaDirectory = "";
    private String resourceDirectory = "";

    public SourceSets() {
    }

    public SourceSets(String javaDirectory, String resourceDirectory) {
        this.javaDirectory = javaDirectory.replaceAll("\\\\", "/");
        this.resourceDirectory = resourceDirectory.replaceAll("\\\\", "/");
    }

    public void setDirectories(ModState modState) {
        javaDirectory = modState.getCommonFolder().replaceAll("\\\\", "/");
        resourceDirectory = modState.getResourceFolder().replaceAll("\\\\", "/");
    }

    public String getWrittenState() {
        StringBuilder sb = new StringBuilder("sourceSets {" + System.lineSeparator());
        sb.append("main {" + System.lineSeparator());
        sb.append("java {" + System.lineSeparator());
        sb.append("srcDir \"" + javaDirectory + "\"" + System.lineSeparator());
        sb.append("}" + System.lineSeparator());
        sb.append("resources {" + System.lineSeparator());
        sb.append("srcDir \"" + resourceDirectory + "\"" + System.lineSeparator());
        sb.append("}" + System.lineSeparator());
        sb.append("}" + System.lineSeparator());
        sb.append("}" + System.lineSeparator());
        sb.append(" " + System.lineSeparator());
        return GradleUtils.formatMethod(sb.toString());
    }

    public String[] getWrittenStateFormat() {
        return getWrittenState().split(System.lineSeparator());
    }

    public void writeIntoFile(List<String> file) {
        String[] formatted = getWrittenStateFormat();

        if (!existsInFile(file)) {
            int position = -1;
            int index = 0;
            for (String line : file) {
                if (line.trim().contains("archivesBaseName")) {
                    position = index + 2;
                    break;
                }
                index++;
            }

            for (int i = 0; i < formatted.length; i++) {
                if (position < 0)
                    file.add(formatted[i]);
                else
                    file.add(i + position, formatted[i]);
            }
            return;
        }

        int startingPoint = -1;
        int endingPoint = -1;

        String compiled = Utils.compileList(file);
        Matcher matcher = pattern.matcher(compiled);
        matcher.find();

        int num = 0;
        int index = 0;
        for (int i = 0; i < file.size(); i++) {
            num += file.get(i).length();
            if (startingPoint == -1 && num >= matcher.start())
                startingPoint = index + 2;
            if (endingPoint == -1 && num >= matcher.end())
                endingPoint = index + 2;
            index++;
        }
        file.subList(startingPoint, endingPoint).clear();
        for (int i = 0; i < formatted.length; i++)
            file.add(startingPoint + i, formatted[i]);
    }

    public static SourceSets createFromFile(List<String> file) {
        String sourceSets = getSourceSetsFromFile(file);
        if (sourceSets.equals(""))
            return generateDefaultSourceSets();
        Matcher matcher = srcPattern.matcher(sourceSets);
        String javaDirectory = "";
        String resourceDirectory = "";
        if (matcher.find())
            javaDirectory = matcher.group();
        if (matcher.find())
            resourceDirectory = matcher.group();

        javaDirectory = GradleUtils.getValueFromField(javaDirectory);
        resourceDirectory = GradleUtils.getValueFromField(resourceDirectory);
        if (javaDirectory.equals("") || resourceDirectory.equals(""))
            return generateDefaultSourceSets();
        return new SourceSets(javaDirectory, resourceDirectory);
    }

    public static SourceSets generateDefaultSourceSets() {
        return new SourceSets("./main/java/", "./main/resources/");
    }

    public static boolean existsInFile(List<String> file) {
        return !getSourceSetsFromFile(file).equals("");
    }

    private static String getSourceSetsFromFile(List<String> file) {
        String compiled = Utils.compileList(file);
        Matcher matcher = pattern.matcher(compiled);
        return matcher.find() ? matcher.group(0) : "";
    }

}
