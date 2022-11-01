/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.concurrent.Phaser;
import org.jetbrains.annotations.NotNull;

// FIXME: Jikoo
abstract class PhasedSubscriber<T> /* implements EconomySubscriber<T> */ {

    /*
    private final @NotNull Phaser phaser;

    PhasedSubscriber(@NotNull Phaser phaser) {
        this.phaser = phaser;
        this.phaser.register();
    }

    @Override
    public final void succeed(@NotNull T t) {
        phaseAccept(t);
        phaser.arriveAndDeregister();
    }

    public abstract void phaseAccept(@NotNull T t);

    @Override
    public final void fail(@NotNull EconomyException exception) {
        phaseFail(exception);
        phaser.arriveAndDeregister();
    }

    public abstract void phaseFail(@NotNull EconomyException exception);
     */

}
