/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FireCompletion<T> {

    private CountDownLatch latch = new CountDownLatch(1);
    private final Executor async;

    @Nullable
    private T result;
    @Nullable
    private Collection<@NotNull Throwable> errors;

    public FireCompletion(@NotNull Class<?> event) {
        this.async = EventExecutorTracker.INSTANCE.getExecutor(Objects.requireNonNull(event,
                "event"
        ));
    }

    public void complete(@NotNull T result) {
        if (latch.getCount() == 0) {
            throw new IllegalStateException("FireCompletion already completed");
        }
        this.result = Objects.requireNonNull(result, "result");
        latch.countDown();
    }

    public void completeExceptionally(@NotNull Collection<@NotNull Throwable> errors) {
        if (latch.getCount() == 0) {
            throw new IllegalStateException("FireCompletion already completed");
        }
        this.errors = Objects.requireNonNull(errors, "errors");
        latch.countDown();
    }

    public void whenCompleteBlocking(
            @Nullable BiConsumer<@Nullable T, @NotNull Collection<@NotNull Throwable>> completedTask
    ) {
        if (completedTask != null) {
            if (latch.getCount() == 0) {
                completedTask.accept(result, errors == null ? Collections.emptyList() : errors);
            } else {
                try {
                    latch.await();
                    completedTask.accept(result, errors == null ? Collections.emptyList() : errors);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void whenCompleteAsync(@Nullable BiConsumer<@Nullable T, @NotNull Collection<@NotNull Throwable>> completedTask) {
        if (completedTask != null) {
            async.execute(() -> whenCompleteBlocking(completedTask));
        }
    }

}
