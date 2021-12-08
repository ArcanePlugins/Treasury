/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.migrate;

import me.lokka30.treasury.api.economy.response.EconomyException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Phaser;
import java.util.function.Consumer;

final class FailureConsumer<T> extends PhasedSubscriber<T> {
    private final @NotNull Consumer<EconomyException> consumer;

    FailureConsumer(@NotNull Phaser phaser, @NotNull Consumer<EconomyException> consumer) {
        super(phaser);
        this.consumer = consumer;
    }

    @Override
    public void phaseAccept(@NotNull T t) {
        // Do nothing.
    }

    @Override
    public void phaseFail(@NotNull EconomyException exception) {
        consumer.accept(exception);
    }

}
