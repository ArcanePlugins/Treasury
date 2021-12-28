/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;
import me.lokka30.treasury.api.economy.response.EconomyException;
import org.jetbrains.annotations.NotNull;

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
