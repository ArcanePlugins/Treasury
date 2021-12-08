/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.misc;

@SuppressWarnings("unused")
public enum EconomyAPIVersion {

    VERSION_1(1);

    private final int number;
    EconomyAPIVersion(final int number) { this.number = number; }

    public int getNumber() { return number; }

}
