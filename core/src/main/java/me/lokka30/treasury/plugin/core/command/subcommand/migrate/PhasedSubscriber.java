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
