package me.lokka30.treasury.api.economy.response;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import me.lokka30.treasury.api.common.response.FailureReason;
import me.lokka30.treasury.api.common.response.Subscriber;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EconomySubscriberTest {

    @Test
    void succeedWrappedFutureEconomy() {
        BiConsumer<Long, Throwable> callBack = Mockito.mock(BiConsumer.class);
        CompletableFuture<Long> future = EconomySubscriber.asFuture(this::econSubCoSucceed);
        future.whenComplete(callBack);

        Mockito.verify(callBack, Mockito.times(1))
                .accept(Mockito.eq(30L), Mockito.isNull());
    }

    @Test
    void failWrappedFutureEconomy() {
        BiConsumer<Long, Throwable> callBack = Mockito.mock(BiConsumer.class);
        CompletableFuture<Long> future = EconomySubscriber.asFuture(this::econSubConFail);
        future.whenComplete(callBack);

        Mockito.verify(callBack, Mockito.times(1))
                .accept(Mockito.isNull(), Mockito.argThat(throwable ->
                        throwable instanceof EconomyException &&
                                "dummy".equals(((EconomyException) throwable)
                                        .getReason()
                                        .getDescription())));

    }

    @Test
    void succeedWrappedFutureSubscriberWithExpliciteEconConsumer() {
        BiConsumer<Long, Throwable> callBack = Mockito.mock(BiConsumer.class);
        CompletableFuture<Long> future = EconomySubscriber.asFuture(this::econSubCoSucceedEcon);
        future.whenComplete(callBack);

        Mockito.verify(callBack, Mockito.times(1))
                .accept(Mockito.eq(30L), Mockito.isNull());
    }

    @Test
    void failWrappedFutureSubscriberEconConsumer() {
        BiConsumer<Long, Throwable> callBack = Mockito.mock(BiConsumer.class);
        CompletableFuture<Long> future = EconomySubscriber.asFuture(this::econSubConFailEcon);
        future.whenComplete(callBack);

        Mockito.verify(callBack, Mockito.times(1))
                .accept(Mockito.isNull(), Mockito.argThat(throwable ->
                        throwable instanceof EconomyException &&
                                "dummy".equals(((EconomyException) throwable)
                                        .getReason()
                                        .getDescription())));

    }

    private void econSubCoSucceed(Subscriber<Long, EconomyException> subscriber) {
        subscriber.succeed(30L);
    }

    private void econSubConFail(Subscriber<Long, EconomyException> subscriber) {
        subscriber.fail(new EconomyException(FailureReason.of("dummy")));
    }

    private void econSubCoSucceedEcon(EconomySubscriber<Long> subscriber) {
        subscriber.succeed(30L);
    }

    private void econSubConFailEcon(EconomySubscriber<Long> subscriber) {
        subscriber.fail(new EconomyException(FailureReason.of("dummy")));
    }
}
