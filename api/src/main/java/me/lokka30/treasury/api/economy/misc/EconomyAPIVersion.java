/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.api.economy.misc;

/**
 * Represents the current API version.
 *
 * @author lokka30, MrIvanPlays
 * @since v1.0
 */
public enum EconomyAPIVersion {
    v1_0(new short[] {1, 0})
    ;

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
            throw new IllegalArgumentException("versionArray must not be null and must contain only 2 versions e.g 1, 0");
        }
        this.versionArray = versionArray;
    }

    /**
     * Returns the major api version as a short value for this {@link EconomyAPIVersion}
     *
     * @return major api version
     * @author MrIvanPlays
     * @since v1.0
     */
    public short getMajorRevision() {
        return versionArray[0];
    }

    /**
     * Returns the minor api version as a short value for this {@link EconomyAPIVersion}
     *
     * @return minor api version
     * @author MrIvanPlays
     * @since v1.0
     */
    public short getMinorRevision() {
        return versionArray[1];
    }

}
