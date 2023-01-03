/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common;

import com.mrivanplays.methodcaller.MethodCallerAccessor;
import java.util.Locale;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a namespaced key, used for unique identification of any so-called "non-player"
 * object the Treasury APIs have.
 *
 * @author MrIvanPlays
 * @since 2.0.0
 */
public final class NamespacedKey {

    /**
     * Creates a new {@code NamespacedKey}.
     *
     * @param namespace namespace
     * @param key       key
     * @return new namespaced key
     * @throws IllegalArgumentException if the namespace is "treasury". The
     *                                  "treasury" namespace is reserved for the Treasury plugin.
     */
    @NotNull
    public static NamespacedKey of(@NotNull String namespace, @NotNull String key) {
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(key, "key");
        validateNotTreasuryNamespace(namespace);
        return new NamespacedKey(namespace, key);
    }

    /**
     * Creates a new {@code NamespacedKey} via the specified {@code namespacedKey} {@link String}
     * , formatted in a way of such: "(namespace):(key)"
     *
     * @param namespacedKey namespaced key
     * @return new namespaced key
     * @throws IllegalArgumentException if the inputted string is not correctly formatted or if
     *                                  the namespace is "treasury". The "treasury" namespace is reserved for the Treasury plugin.
     */
    @NotNull
    public static NamespacedKey fromString(@NotNull String namespacedKey) {
        Objects.requireNonNull(namespacedKey, "namespacedKey");
        if (namespacedKey.indexOf(':') == -1) {
            throw new IllegalArgumentException("namespacedKey string should contain a ':'");
        }
        String[] parts = namespacedKey.split(":");
        String namespace = parts[0], key = parts[1];

        validateNotTreasuryNamespace(namespace);

        return new NamespacedKey(namespace, key);
    }

    private static void validateNotTreasuryNamespace(@NotNull String namespace) {
        Class<?> caller = MethodCallerAccessor.create().getCallerClass();
        if (!caller.getPackage().getName().contains("me.lokka30.treasury")) {
            if (namespace.toLowerCase(Locale.ROOT).contains("treasury")) {
                throw new IllegalArgumentException(
                        "The 'treasury' namespace is reserved for Treasury plugin!");
            }
        }
    }

    private final String namespace, key;

    private NamespacedKey(@NotNull String namespace, @NotNull String key) {
        this.namespace = namespace;
        this.key = key;
    }

    /**
     * Returns the namespace this {@code NamespacedKey} holds.
     *
     * @return namespace
     */
    @NotNull
    public String getNamespace() {
        return this.namespace;
    }

    /**
     * Returns the key this {@code NamespacedKey} holds.
     *
     * @return key
     */
    @NotNull
    public String getKey() {
        return this.key;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NamespacedKey that = (NamespacedKey) o;

        if (!getNamespace().equals(that.getNamespace())) {
            return false;
        }
        return getKey().equals(that.getKey());
    }

    @Override
    public int hashCode() {
        int result = getNamespace().hashCode();
        result = 31 * result + getKey().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.namespace + ":" + this.key;
    }

}
