/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Completion {

    static Completion completed(@NotNull Executor async) {
        return new Completion(true, async);
    }

    static Completion completed(@NotNull Class<?> eventClass) {
        return new Completion(true, eventClass);
    }

    static Completion completedExceptionally(
            @NotNull Throwable error, @NotNull Class<?> eventClass
    ) {
        return new Completion(error, eventClass);
    }

    @NotNull
    static Completion join(
            @NotNull Class<?> eventClass, @NotNull Completion @NotNull ... other
    ) {
        Objects.requireNonNull(eventClass, "eventClass");
        Objects.requireNonNull(other, "other");
        List<Throwable> errors = new ArrayList<>();
        for (Completion completion : other) {
            Objects.requireNonNull(completion, "completion");
            completion.waitCompletion();
            if (!completion.getErrors().isEmpty()) {
                errors.addAll(completion.getErrors());
            }
        }
        return errors.isEmpty() ? Completion.completed(eventClass) : new Completion(errors,
                eventClass
        );
    }

    private CountDownLatch latch = new CountDownLatch(1);
    private final Executor async;
    private Collection<Throwable> errors;

    Completion(@NotNull Executor async) {
        this.async = Objects.requireNonNull(async, "async");
    }

    private Completion(boolean completed, @NotNull Executor async) {
        this.async = Objects.requireNonNull(async, "async");
        if (completed) {
            this.latch.countDown();
        }
    }

    private Completion(boolean completed, @NotNull Class<?> eventClass) {
        this.async = EventExecutorTracker.INSTANCE.getExecutor(Objects.requireNonNull(eventClass,
                "eventClass"
        ));
        if (completed) {
            this.latch.countDown();
        }
    }

    private Completion(
            @NotNull Collection<@NotNull Throwable> errors, @NotNull Class<?> eventClass
    ) {
        this.errors = Objects.requireNonNull(errors, "errors");
        this.async = EventExecutorTracker.INSTANCE.getExecutor(Objects.requireNonNull(eventClass,
                "eventClass"
        ));
        this.latch.countDown();
    }

    private Completion(@NotNull Throwable error, @NotNull Class<?> eventClass) {
        this(Collections.singletonList(Objects.requireNonNull(error, "error")), eventClass);
    }

    public void complete() {
        if (latch.getCount() == 0) {
            throw new IllegalStateException("Completion already completed");
        }
        latch.countDown();
    }

    public void completeExceptionally(@NotNull Throwable error) {
        if (latch.getCount() == 0) {
            throw new IllegalStateException("Completion already completed");
        }
        Objects.requireNonNull(error, "error");
        this.errors = Collections.singletonList(error);
        latch.countDown();
    }

    public void completeExceptionally(@NotNull Collection<@NotNull Throwable> errors) {
        if (latch.getCount() == 0) {
            throw new IllegalStateException("Completion already completed");
        }
        Objects.requireNonNull(errors, "errors");
        this.errors = errors;
        latch.countDown();
    }

    public void waitCompletion() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @NotNull
    public Collection<@NotNull Throwable> getErrors() {
        return errors == null ? Collections.emptyList() : errors;
    }

    public void whenCompleteBlocking(@Nullable Consumer<@NotNull Collection<@NotNull Throwable>> completedTask) {
        if (completedTask != null) {
            if (latch.getCount() == 0) {
                completedTask.accept(getErrors());
            } else {
                try {
                    latch.await();
                    completedTask.accept(getErrors());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void whenCompleteAsync(@Nullable Consumer<@NotNull Collection<@NotNull Throwable>> completedTask) {
        if (completedTask != null) {
            async.execute(() -> {
                if (latch.getCount() == 0) {
                    completedTask.accept(getErrors());
                } else {
                    try {
                        latch.await();
                        completedTask.accept(getErrors());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
    }

}
