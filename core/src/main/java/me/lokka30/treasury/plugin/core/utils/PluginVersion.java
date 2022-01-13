package me.lokka30.treasury.plugin.core.utils;

import java.util.Objects;
import java.util.regex.Pattern;
import me.lokka30.treasury.plugin.core.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * A version object, representing Treasury plugin's version.
 *
 * @author MrIvanPlays
 */
public final class PluginVersion {

    private static final Pattern VERSION_VALIDATOR = Pattern.compile(
            "\\d+\\.\\d+\\.\\d+-[0-9A-Fa-f]{7}-(SNAPSHOT|RELEASE)");

    private final short majorRevision, minorRevision, patchRevision;
    private final boolean developmentVersion, release;
    private final String stringVersion, commit;

    public PluginVersion(@NotNull String input, @NotNull Logger logger) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(logger, "logger");

        if (!VERSION_VALIDATOR.matcher(input).matches()) {
            throw new IllegalArgumentException(
                    "Your current Treasury version is not formatted correctly! Please inform a Treasury maintainer as soon as possible.");
        }

        this.stringVersion = input;

        String[] parts = input.split("-");

        this.commit = parts[1];

        this.developmentVersion = parts[2].equalsIgnoreCase("SNAPSHOT");
        this.release = parts[2].equalsIgnoreCase("RELEASE");

        String[] versionNoParts = parts[0].split("\\.");
        this.majorRevision = Short.parseShort(versionNoParts[0]);
        this.minorRevision = Short.parseShort(versionNoParts[1]);
        this.patchRevision = Short.parseShort(versionNoParts[2]);
    }

    /**
     * Get the major revision of this plugin version.
     *
     * @return major version
     */
    public short getMajorRevision() {
        return majorRevision;
    }

    /**
     * Get the minor revision of this plugin version.
     *
     * @return minor version
     */
    public short getMinorRevision() {
        return minorRevision;
    }

    /**
     * Get the patch revision of this plugin version.
     *
     * @return patch version
     */
    public short getPatchRevision() {
        return patchRevision;
    }

    /**
     * Returns whether this plugin version is a development version.
     *
     * @return development version or not
     */
    public boolean isDevelopmentVersion() {
        return developmentVersion;
    }

    /**
     * Returns whether this plugin version is a release version.
     *
     * @return release version or not
     */
    public boolean isReleaseVersion() {
        return release;
    }

    /**
     * Returns the abbreviated commit id, stored inside the version.
     *
     * @return commit id
     */
    public String getCommit() {
        return commit;
    }

    /**
     * Compares the specified {@link PluginVersion} {@code other} to this plugin version.
     *
     * @param other version to compare to
     * @return comparison result
     */
    public ComparisonResult compare(PluginVersion other) {
        if (other.getMajorRevision() > majorRevision) {
            return ComparisonResult.NEWER;
        } else if (other.getMajorRevision() < majorRevision) {
            return ComparisonResult.OLDER;
        } else {
            if (other.getMinorRevision() > minorRevision) {
                return ComparisonResult.NEWER;
            } else if (other.getMinorRevision() < minorRevision) {
                return ComparisonResult.OLDER;
            } else {
                if (other.getPatchRevision() > patchRevision) {
                    return ComparisonResult.NEWER;
                } else if (other.getPatchRevision() < patchRevision) {
                    return ComparisonResult.OLDER;
                } else {
                    // do not compare development versions - we do other stuff inside the update
                    // checker
                    return ComparisonResult.EQUAL;
                }
            }
        }
    }

    /**
     * Represents a result of comparing two {@link PluginVersion PluginVersions}.
     *
     * @author MrIvanPlays
     */
    public enum ComparisonResult {

        /**
         * The version which was given to compare to is older than the version comparing.
         */
        OLDER,

        /**
         * The version which was given to compare to is newer than the version comparing.
         */
        NEWER,

        /**
         * The version which was given to compare to is the same as the version comparing.
         */
        EQUAL,

        /**
         * Special case comparison result constant, which means the version which was given to
         * compare was something we couldn't understand somehow.
         */
        UNKNOWN
    }

    @Override
    public String toString() {
        return stringVersion;
    }

}
