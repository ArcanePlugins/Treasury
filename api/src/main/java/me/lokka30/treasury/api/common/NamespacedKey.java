/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common;

import java.util.Locale;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * @throws IllegalArgumentException if the inputted namespace or key contain any spaces or if
     *                                  the namespace is "treasury". The
     *                                  "treasury" namespace is reserved for the Treasury plugin.
     */
    @NotNull
    public static NamespacedKey of(@NotNull String namespace, @NotNull String key) {
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(key, "key");
        validateNotTreasuryNamespace(namespace);
        validateWhitespaces(namespace, "Namespaces cannot contain spaces");
        validateWhitespaces(key, "Keys cannot contain spaces");
        return new NamespacedKey(namespace, key);
    }

    /**
     * Creates a new {@code NamespacedKey} via the specified {@code namespacedKey} {@link String}
     * , formatted in a way of such: "(namespace):(key)"
     *
     * @param namespacedKey namespaced key
     * @return new namespaced key
     * @throws IllegalArgumentException if the inputted string is not correctly formatted, if the
     *                                  inputted string contains any whitespaces, or if
     *                                  the namespace is "treasury". The "treasury" namespace is
     *                                  reserved for the Treasury plugin.
     */
    @NotNull
    public static NamespacedKey fromString(@NotNull String namespacedKey) {
        Objects.requireNonNull(namespacedKey, "namespacedKey");
        int columnCharOccurrences = 0;
        for (char c : namespacedKey.toCharArray()) {
            if (Character.isWhitespace(c)) {
                throw new IllegalArgumentException("A namespaced key string cannot contain spaces");
            }
            if (c == ':') {
                columnCharOccurrences++;
                if (columnCharOccurrences > 1) {
                    break;
                }
            }
        }
        if (columnCharOccurrences > 1 || columnCharOccurrences == 0) {
            throw new IllegalArgumentException("namespacedKey string should only contain a single colon (':') character");
        }
        String[] parts = namespacedKey.split(":");
        String namespace = parts[0], key = parts[1];

        validateNotTreasuryNamespace(namespace);

        return new NamespacedKey(namespace, key);
    }

    private static void validateNotTreasuryNamespace(@NotNull String namespace) {
        String callerClassName = getCallerClassName();
        if (!callerClassName.contains("me.lokka30.treasury")) {
            if (namespace.toLowerCase(Locale.ROOT).contains("treasury")) {
                throw new IllegalArgumentException(
                        "The 'treasury' namespace is reserved for Treasury plugin!");
            }
        }
    }

    @Nullable
    private static String getCallerClassName() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i = 1; i < elements.length; i++) {
            StackTraceElement element = elements[i];
            if (!element.getClassName().equals(NamespacedKey.class.getName()) && element
                    .getClassName()
                    .indexOf("java.lang.Thread") != 0) {
                if (callerClassName == null) {
                    callerClassName = element.getClassName();
                } else if (!callerClassName.equals(element.getClassName())) {
                    return callerClassName;
                }
            }
        }
        return null;
    }

    private static void validateWhitespaces(@NotNull String val, @NotNull String error) {
        for (char c : val.toCharArray()) {
            if (Character.isWhitespace(c)) {
                throw new IllegalArgumentException(error);
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
