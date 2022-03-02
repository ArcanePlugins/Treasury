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
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a state of completion. This is used to block event execution until the
 * {@link EventSubscriber} finishes an async task.
 *
 * @author MrIvanPlays
 * @see EventSubscriber
 * @since v1.1.0
 */
public final class Completion {

    /**
     * Returns a completed {@code Completion}
     *
     * @return completed completion
     */
    @NotNull
    public static Completion completed() {
        return new Completion(true);
    }

    /**
     * Returns a completed {@code Completion} which is exceptionally completed with the specified
     * {@link Throwable} {@code error}
     *
     * @param error error to get a completed completion with
     * @return completed completion
     */
    @NotNull
    public static Completion completedExceptionally(@NotNull Throwable error) {
        return new Completion(error);
    }

    /**
     * Returns a completed {@code Completion} which is exceptionally completed with the specified
     * {@link Collection} of {@link Throwable Throwables} {@code errors}
     *
     * @param errors errors to get a completed completion with
     * @return completed completion
     */
    @NotNull
    public static Completion completedExceptionally(@NotNull Collection<@NotNull Throwable> errors) {
        return new Completion(errors);
    }

    /**
     * Returns a {@code Completion} which is a summary from all the specified {@code Completions}
     * {@code other}. <b>WARNING: This method blocks the thread it's being called onto. You may
     * want to use this asynchronously.</b>
     *
     * @param other completions to join
     * @return a joined completion
     */
    @NotNull
    public static Completion join(@NotNull Completion @NotNull ... other) {
        Objects.requireNonNull(other, "other");
        List<Throwable> errors = new ArrayList<>();
        for (Completion completion : other) {
            Objects.requireNonNull(completion, "completion");
            if (!completion.isCompleted()) {
                completion.waitCompletion();
            }
            if (!completion.getErrors().isEmpty()) {
                errors.addAll(completion.getErrors());
            }
        }
        return errors.isEmpty()
                ? Completion.completed()
                : Completion.completedExceptionally(errors);
    }

    private CountDownLatch latch = new CountDownLatch(1);
    private Collection<Throwable> errors;

    /**
     * Creates a new {@code Completion} which is not completed.
     */
    public Completion() {

    }

    private Completion(boolean completed) {
        if (completed) {
            this.latch.countDown();
        }
    }

    private Completion(@NotNull Collection<@NotNull Throwable> errors) {
        this.errors = Objects.requireNonNull(errors, "errors");
        this.latch.countDown();
    }

    private Completion(@NotNull Throwable error) {
        this(Collections.singletonList(Objects.requireNonNull(error, "error")));
    }

    /**
     * Returns whether this {@code Completion} is completed.
     *
     * @return completed or not
     * @since v1.1.2
     */
    public boolean isCompleted() {
        return latch.getCount() == 0;
    }

    /**
     * Completes this {@code Completion}.
     *
     * @throws IllegalStateException if this completion is already completed
     */
    public void complete() {
        if (latch.getCount() == 0) {
            throw new IllegalStateException("Completion already completed");
        }
        latch.countDown();
    }

    /**
     * Completes this {@code Completion} exceptionally with the specified {@link Throwable}
     * {@code error}
     *
     * @param error error to complete this completion with
     * @throws IllegalStateException if this completion is already completed
     */
    public void completeExceptionally(@NotNull Throwable error) {
        if (latch.getCount() == 0) {
            throw new IllegalStateException("Completion already completed");
        }
        Objects.requireNonNull(error, "error");
        this.errors = Collections.singletonList(error);
        latch.countDown();
    }

    /**
     * Completes this {@code Completion} exceptionally with the specified {@link Collection} of
     * {@link Throwable Throwables} {@code errors}
     *
     * @param errors errors to complete this completion with
     * @throws IllegalStateException if this completion is already completed
     */
    public void completeExceptionally(@NotNull Collection<@NotNull Throwable> errors) {
        if (latch.getCount() == 0) {
            throw new IllegalStateException("Completion already completed");
        }
        Objects.requireNonNull(errors, "errors");
        this.errors = errors;
        latch.countDown();
    }

    /**
     * Waits for this {@code Completion} to complete. <b>WARNING: This blocks the thread it is
     * called onto. In 99% of the use cases you wouldn't need to use this. If you really need
     * to use this method, it is highly recommended that you do it asynchronously.</b>
     */
    public void waitCompletion() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Returns the errors this {@code Completion} got completed with. If this {@code Completion}
     * isn't completed, or was completed without any errors, it returns and empty
     * {@link Collection}.
     *
     * @return errors or empty collection
     */
    @NotNull
    public Collection<@NotNull Throwable> getErrors() {
        return errors == null ? Collections.emptyList() : errors;
    }

    /**
     * Runs the specified {@link Consumer} {@code task} when this {@code Completion} completes.
     * <b>WARNING: This has the potential to block the thread it's called onto. In 99% of the use
     * cases you wouldn't need to use this. If you really need to use this method, it is highly
     * recommended that you do it asynchronously.</b>
     *
     * @param completedTask task to run
     */
    public void whenComplete(@Nullable Consumer<@NotNull Collection<@NotNull Throwable>> completedTask) {
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

}
