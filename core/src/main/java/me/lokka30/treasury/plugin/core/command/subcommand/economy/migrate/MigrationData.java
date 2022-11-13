/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.core.debug.DebugCategory;
import me.lokka30.treasury.plugin.core.debug.DebugHandler;
import me.lokka30.treasury.plugin.core.utils.QuickTimer;
import org.jetbrains.annotations.NotNull;

/**
 * A {@code Record}-style data container for economy migration.
 */
class MigrationData {

    private final @NotNull Service<EconomyProvider> from;
    private final @NotNull Service<EconomyProvider> to;
    private final boolean debugEnabled;
    private final @NotNull QuickTimer timer = new QuickTimer();
    private final @NotNull Collection<String> nonMigratedCurrencies = new ConcurrentLinkedQueue<>();
    private final @NotNull AtomicInteger playerAccountsProcessed = new AtomicInteger();
    private final @NotNull AtomicInteger nonPlayerAccountsProcessed = new AtomicInteger();

    MigrationData(
            @NotNull Service<EconomyProvider> from,
            @NotNull Service<EconomyProvider> to,
            boolean debugEnabled
    ) {
        this.from = from;
        this.to = to;
        this.debugEnabled = debugEnabled;
    }

    void debug(Supplier<String> supplier) {
        if (debugEnabled) {
            DebugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND, supplier.get());
        }
    }

    @NotNull Service<EconomyProvider> from() {
        return from;
    }

    @NotNull Service<EconomyProvider> to() {
        return to;
    }

    @NotNull QuickTimer timer() {
        return timer;
    }

    @NotNull Collection<String> nonMigratedCurrencies() {
        return nonMigratedCurrencies;
    }

    @NotNull AtomicInteger playerAccountsProcessed() {
        return playerAccountsProcessed;
    }

    @NotNull AtomicInteger nonPlayerAccountsProcessed() {
        return nonPlayerAccountsProcessed;
    }

}
