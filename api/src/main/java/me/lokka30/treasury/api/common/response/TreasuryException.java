/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.response;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

/**
 * Represents a {@link RuntimeException}, indicating failure, without a stacktrace.
 *
 * @author yannicklamprecht, MrIvanPlays
 * @since 1.1.0 but heavily modified in 2.0.0
 */
public final class TreasuryException extends RuntimeException {

    private final Function<Locale, String> messageSupplier;

    /**
     * Create a new {@code TreasuryException}
     *
     * @param message a message, representing a reason why failure occurred
     */
    public TreasuryException(@NotNull String message) {
        super(message, null, false, false);
        this.messageSupplier = ($) -> message;
    }

    /**
     * Create a new {@code TreasuryException} with a {@link Function} that provides a localized
     * message.
     * <p>{@link Locale#ENGLISH} message must be always provided.
     *
     * @param messageSupplier supplier of localized messages
     */
    public TreasuryException(@NotNull Function<Locale, String> messageSupplier) {
        super(verifyEnglishMessage(messageSupplier), null, false, false);
        this.messageSupplier = messageSupplier;
    }

    @NotNull
    private static String verifyEnglishMessage(@NotNull Function<Locale, String> messageSupplier) {
        return Objects.requireNonNull(
                messageSupplier.apply(Locale.ENGLISH),
                "No Locale#ENGLISH message provided when creating a TreasuryException with a message supplier."
        );
    }

    /**
     * Get a localized message.
     *
     * @param locale the locale for which a message is needed
     * @return if the {@code TreasuryException} object this method is called from is created via
     *         {@link #TreasuryException(Function)}, the return value might be null for the
     *         specified locale. Otherwise, if it is created via
     *         {@link #TreasuryException(String)}, then it will always return the specified
     *         non-null message without the specified locale affecting it.
     */
    @UnknownNullability
    public String getMessage(@NotNull Locale locale) {
        return this.messageSupplier.apply(locale);
    }

}
