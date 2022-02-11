package me.lokka30.treasury.api.common.response;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import me.lokka30.treasury.api.economy.response.EconomyException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SubscriberTest {

    @Test
    void succeedWrappedFutureSubscriber() {
        BiConsumer<Long, Throwable> callBack = Mockito.mock(BiConsumer.class);
        CompletableFuture<Long> future = Subscriber.asFuture(this::econSubCoSucceed);
        future.whenComplete(callBack);

        Mockito.verify(callBack, Mockito.times(1))
                .accept(Mockito.eq(30L), Mockito.isNull());
    }

    @Test
    void failWrappedFutureSubscriber() {
        BiConsumer<Long, Throwable> callBack = Mockito.mock(BiConsumer.class);
        CompletableFuture<Long> future = Subscriber.asFuture(this::econSubConFail);
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

}
