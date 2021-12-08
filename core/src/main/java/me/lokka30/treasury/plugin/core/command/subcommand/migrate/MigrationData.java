/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.migrate;

import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.plugin.core.ProviderEconomy;
import me.lokka30.treasury.plugin.core.debug.DebugCategory;
import me.lokka30.treasury.plugin.core.debug.DebugHandler;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * A {@code Record}-style data container for economy migration.
 */
class MigrationData {

    private final @NotNull ProviderEconomy from;
    private final @NotNull ProviderEconomy to;
    private final boolean debugEnabled;
    private final @NotNull QuickTimer timer = new QuickTimer();
    private final @NotNull Map<Currency, Currency> migratedCurrencies = new ConcurrentHashMap<>();
    private final @NotNull Collection<String> nonMigratedCurrencies = new ConcurrentLinkedQueue<>();
    private final @NotNull AtomicInteger playerAccountsProcessed = new AtomicInteger();
    private final @NotNull AtomicInteger bankAccountsProcessed = new AtomicInteger();

    MigrationData(
            @NotNull ProviderEconomy from,
            @NotNull ProviderEconomy to,
            boolean debugEnabled) {
        this.from = from;
        this.to = to;
        this.debugEnabled = debugEnabled;
    }

    void debug(Supplier<String> supplier) {
        if (debugEnabled) {
            DebugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, supplier.get());
        }
    }

    @NotNull ProviderEconomy from() {
        return from;
    }

    @NotNull ProviderEconomy to() {
        return to;
    }

    @NotNull QuickTimer timer() {
        return timer;
    }

    @NotNull Map<Currency, Currency> migratedCurrencies() {
        return migratedCurrencies;
    }

    @NotNull Collection<String> nonMigratedCurrencies() {
        return nonMigratedCurrencies;
    }

    @NotNull AtomicInteger playerAccountsProcessed() {
        return playerAccountsProcessed;
    }

    @NotNull AtomicInteger bankAccountsProcessed() {
        return bankAccountsProcessed;
    }

}
