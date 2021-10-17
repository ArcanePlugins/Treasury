package me.lokka30.treasury.api.economy.response;

import org.jetbrains.annotations.NotNull;

public interface EconomyPublisher<T> {

    void subscribe(@NotNull EconomySubscription<T> subscription);

}
