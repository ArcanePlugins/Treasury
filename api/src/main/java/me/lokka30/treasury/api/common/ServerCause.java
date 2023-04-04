/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common;

import org.jetbrains.annotations.NotNull;

class ServerCause implements Cause<String> {

    static ServerCause INSTANCE = new ServerCause();

    private static boolean instanceCreated = false;

    private ServerCause() {
        if (instanceCreated) {
            throw new IllegalStateException("Server cause already created!");
        }
        instanceCreated = true;
    }

    @Override
    public @NotNull String identifier() {
        return "Server";
    }

    @Override
    public boolean equals(@NotNull Cause<?> other) {
        if (other instanceof ServerCause) {
            return true;
        }
        if (other.identifier() instanceof String) {
            return ((String) other.identifier()).equalsIgnoreCase("Server");
        }
        return false;
    }

}
