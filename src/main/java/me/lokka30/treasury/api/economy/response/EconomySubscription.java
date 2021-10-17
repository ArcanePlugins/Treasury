package me.lokka30.treasury.api.economy.response;

import org.jetbrains.annotations.NotNull;

public interface EconomySubscription<T> {

    void accept(@NotNull T t);

    void error(@NotNull EconomyException exception);

}
