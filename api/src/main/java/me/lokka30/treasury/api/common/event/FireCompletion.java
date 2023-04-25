/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
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

/**
 * Represents a {@link Completion} which is used when an event is fired.
 *
 * @param <T> event type
 * @author MrIvanPlays
 * @since v1.1.0
 */
public final class FireCompletion<T> {

    private CountDownLatch latch = new CountDownLatch(1);
    private final Executor async;

    @Nullable
    private T result;
    @Nullable
    private Collection<@NotNull Throwable> errors;

    public FireCompletion() {
        this.async = ExecutorHolder.INSTANCE.getExecutor();
    }

    /**
     * Successfully completes this completion with the specified result.
     *
     * @param result result to complete with
     * @throws IllegalStateException if this completion got already completed
     */
    public void complete(@NotNull T result) {
        if (latch.getCount() == 0) {
            throw new IllegalStateException("FireCompletion already completed");
        }
        this.result = Objects.requireNonNull(result, "result");
        latch.countDown();
    }

    /**
     * Completes this completion exceptionally with the specified {@link Collection} of
     * {@link Throwable Throwables} {@code errors}
     *
     * @param errors the errors to complete with
     * @throws IllegalStateException if this completion got already completed
     */
    public void completeExceptionally(@NotNull Collection<@NotNull Throwable> errors) {
        if (latch.getCount() == 0) {
            throw new IllegalStateException("FireCompletion already completed");
        }
        this.errors = Objects.requireNonNull(errors, "errors");
        latch.countDown();
    }

    /**
     * Waits for this completion to complete and prints all the errors if this completion got
     * completed exceptionally. <b>WARNING: This blocks the thread it is called onto. If you need
     * to use this method, it is highly recommended that you do it asynchronously.</b>
     */
    public void waitCompletion() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (errors != null && !errors.isEmpty()) {
            for (Throwable e : errors) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Runs the specified {@link BiConsumer} task when this completion completes. <b>WARNING:
     * This method blocks the thread it's being called onto. If you don't want to block the
     * thread this gets called onto, please use {@link #whenCompleteAsync(BiConsumer)}</b>
     *
     * @param completedTask task to run
     */
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

    /**
     * Runs the specified {@link BiConsumer} {@code task} asynchronously when this completion
     * completes.
     *
     * @param completedTask task to run
     */
    public void whenCompleteAsync(@Nullable BiConsumer<@Nullable T, @NotNull Collection<@NotNull Throwable>> completedTask) {
        if (completedTask != null) {
            async.execute(() -> whenCompleteBlocking(completedTask));
        }
    }

}
