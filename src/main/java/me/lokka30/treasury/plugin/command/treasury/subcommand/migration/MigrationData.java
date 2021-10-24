package me.lokka30.treasury.plugin.command.treasury.subcommand.migration;

import me.lokka30.microlib.maths.QuickTimer;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.plugin.Treasury;
import me.lokka30.treasury.plugin.debug.DebugCategory;
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

    private final @NotNull Treasury treasury;
    private final @NotNull EconomyProvider from;
    private final @NotNull EconomyProvider to;
    private final boolean debugEnabled;
    private final @NotNull QuickTimer timer = new QuickTimer();
    private final @NotNull Map<Currency, Currency> migratedCurrencies = new ConcurrentHashMap<>();
    private final @NotNull Collection<String> nonMigratedCurrencies = new ConcurrentLinkedQueue<>();
    private final @NotNull AtomicInteger playerAccountsProcessed = new AtomicInteger();
    private final @NotNull AtomicInteger bankAccountsProcessed = new AtomicInteger();

    MigrationData(
            @NotNull Treasury treasury,
            @NotNull EconomyProvider from,
            @NotNull EconomyProvider to,
            boolean debugEnabled) {
        this.treasury = treasury;
        this.from = from;
        this.to = to;
        this.debugEnabled = debugEnabled;
    }

    void debug(Supplier<String> supplier) {
        if (debugEnabled) {
            treasury.debugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, supplier.get());
        }
    }

    @NotNull EconomyProvider from() {
        return from;
    }

    @NotNull EconomyProvider to() {
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
