package me.lokka30.treasury.plugin.bukkit.command.treasury.subcommand.migrate;

import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Phaser;

abstract class PhasedSubscriber<T> implements EconomySubscriber<T> {
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

}
