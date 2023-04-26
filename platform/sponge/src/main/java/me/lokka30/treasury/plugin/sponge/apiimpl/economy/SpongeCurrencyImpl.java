/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.apiimpl.economy;

import java.math.BigDecimal;
import java.util.Locale;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.service.economy.Currency;

public class SpongeCurrencyImpl implements Currency {

    private me.lokka30.treasury.api.economy.currency.Currency wrapper;

    public SpongeCurrencyImpl(@NotNull me.lokka30.treasury.api.economy.currency.Currency wrapper) {
        this.wrapper = wrapper;
    }

    public me.lokka30.treasury.api.economy.currency.Currency getDelegate() {
        return this.wrapper;
    }

    @Override
    public Component displayName() {
        return Component.text(wrapper.getDisplayName(BigDecimal.ONE, Locale.ENGLISH));
    }

    @Override
    public Component pluralDisplayName() {
        return Component.text(wrapper.getDisplayName(BigDecimal.valueOf(2), Locale.ENGLISH));
    }

    @Override
    public Component symbol() {
        return Component.text(wrapper.getSymbol());
    }

    @Override
    public Component format(final BigDecimal amount, final int numFractionDigits) {
        return Component.text(wrapper.format(amount, Locale.ENGLISH, numFractionDigits));
    }

    @Override
    public int defaultFractionDigits() {
        return wrapper.getPrecision();
    }

    @Override
    public boolean isDefault() {
        return wrapper.isPrimary();
    }

}
