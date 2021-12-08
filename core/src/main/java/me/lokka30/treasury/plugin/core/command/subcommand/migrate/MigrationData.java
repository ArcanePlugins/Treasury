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
