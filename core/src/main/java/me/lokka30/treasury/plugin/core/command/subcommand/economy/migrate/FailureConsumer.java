/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.concurrent.Phaser;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

// FIXME: Jikoo
final class FailureConsumer<T> extends PhasedSubscriber<T> {

    /*
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
     */

}
