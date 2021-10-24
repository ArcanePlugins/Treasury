package me.lokka30.treasury.plugin.command.treasury.subcommand.migration;

import me.lokka30.treasury.api.economy.response.EconomyException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Phaser;
import java.util.function.Consumer;

final class FailureConsumer<T> extends PhasedSubscriber<T> {
    private final Consumer<EconomyException> consumer;

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
