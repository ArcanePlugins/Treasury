package me.lokka30.treasury.plugin.bukkit.command.treasury.subcommand.migrate;

import me.lokka30.treasury.api.economy.response.EconomyException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;

class PhasedFutureSubscriber<T> extends PhasedSubscriber<T> {
    private final @NotNull CompletableFuture<T> future;

    PhasedFutureSubscriber(@NotNull Phaser phaser, @NotNull CompletableFuture<T> future) {
        super(phaser);
        this.future = future;
    }

    @Override
    public void phaseAccept(@NotNull T t) {
        future.complete(t);
    }

    @Override
    public void phaseFail(@NotNull EconomyException exception) {
        future.completeExceptionally(exception);
    }

}
