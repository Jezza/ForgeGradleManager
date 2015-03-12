package me.jezza.fgpm.mod;

import com.google.common.base.Strings;
import me.jezza.fgpm.App;
import me.jezza.fgpm.gui.lib.IVersion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A version string must follow this:
 * \d+(\.\d+)*(\-(\w+))?
 */
public class Version implements Comparable<Version>, IVersion {

    private static boolean wasStripped = false;

    public static final Pattern VALID_NUMBERING = Pattern.compile("^\\d+(\\.\\d+)*");
    public static final Pattern VALID_RELEASE_STAGE = Pattern.compile("\\-(\\w+)$");

    private String numbering, releaseStage;
    private boolean canAlter;

    private Version(String numbering) {
        this.numbering = numbering;
    }

    public String getNumbering() {
        return numbering;
    }

    public String getReleaseStage() {
        return releaseStage;
    }

    public boolean canAlter() {
        return canAlter;
    }

    public void increment() {
        App.LOG.info("Increment...");
    }

    public void decrement() {
        App.LOG.info("Decrement...");
    }

    @Override
    public String toString() {
        StringBuilder versionString = new StringBuilder(numbering);

        if (!Strings.isNullOrEmpty(releaseStage)) {
            versionString.append("-");
            versionString.append(releaseStage);
        }

        return versionString.toString();
    }

    @Override
    public int compareTo(Version o) {
        String[] parts = numbering.split("\\.");
        String[] otherParts = o.numbering.split("\\.");

        for (int i = 0; i < parts.length && i < otherParts.length; i++) {
            int num1;
            int num2;

            try {
                num1 = Integer.parseInt(parts[i].replaceFirst("^0+(?!$)", ""));
            } catch (NumberFormatException ignored) {
                return -1;
            }

            try {
                num2 = Integer.parseInt(otherParts[i].replaceFirst("^0+(?!$)", ""));
            } catch (NumberFormatException ignored) {
                return 1;
            }

            if (num1 != num2)
                return Integer.compare(num1, num2);
        }
        return Integer.compare(parts.length, otherParts.length);
    }

    public static Version parse(String versionString) throws InvalidVersionException {
        if (Strings.isNullOrEmpty(versionString))
            throw new InvalidVersionException("No version parsed.");

        Matcher matcher = VALID_NUMBERING.matcher(versionString);
        if (!matcher.find())
            throw new InvalidVersionException("Incorrect numbering format; Must match: " + VALID_NUMBERING.pattern());
        Version version = new Version(matcher.group());

        matcher = VALID_RELEASE_STAGE.matcher(versionString);
        if (matcher.find()) {
            version.releaseStage = matcher.group(1);
            App.LOG.trace("Release stage found: " + version.releaseStage);
        } else
            App.LOG.trace("No release stage found.");

        wasStripped = version.toString().length() != versionString.length();
        version.canAlter = !wasStripped;
        return version;
    }

    public static boolean wasLastParseStripped() {
        return wasStripped;
    }
}
