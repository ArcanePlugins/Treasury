/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.misc;

/**
 * Represents the current API version.
 *
 * @author lokka30, MrIvanPlays
 * @since v1.0.0
 * @deprecated API versions are no longer used as of Treasury v1.1.0.
 */
@Deprecated
public enum EconomyAPIVersion {
    v1_0(new short[]{1, 0}),
    V1_1(new short[]{1, 1});

    /**
     * Returns the current economy api version.
     *
     * @return current version
     * @deprecated internal use only
     */
    @Deprecated
    public static EconomyAPIVersion getCurrentAPIVersion() {
        EconomyAPIVersion[] constants = EconomyAPIVersion.values();
        return constants[constants.length - 1];
    }

    private final short[] versionArray;

    EconomyAPIVersion(short[] versionArray) {
        if (versionArray == null || versionArray.length > 2) {
            throw new IllegalArgumentException(
                    "versionArray must not be null and must contain only 2 versions e.g 1, 0");
        }
        this.versionArray = versionArray;
    }

    /**
     * Returns the major api version as a short value for this {@link EconomyAPIVersion}
     *
     * @return major api version
     * @since v1.0
     */
    public short getMajorRevision() {
        return versionArray[0];
    }

    /**
     * Returns the minor api version as a short value for this {@link EconomyAPIVersion}
     *
     * @return minor api version
     * @since v1.0
     */
    public short getMinorRevision() {
        return versionArray[1];
    }

}
