/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.misc;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class to help with {@link CompletableFuture completable futures}.
 *
 * @author MrIvanPlays, A248
 * @since 1.2.1
 */
public class FutureHelper {

    /**
     * Joins and filters futures. All the futures are mapped to return a collective future of the
     * same data type whilst simultaneously filtering them via the specified {@code filter}
     * {@link Function}.
     *
     * @param filter filter
     * @param futures futures
     * @param <T> type parameter
     * @return joined and filtered future
     */
    @NotNull
    public static <T> CompletableFuture<Collection<T>> joinAndFilter(
            @NotNull Function<T, CompletableFuture<TriState>> filter,
            @NotNull Collection<CompletableFuture<T>> futures
    ) {

        Objects.requireNonNull(filter, "filter");
        Objects.requireNonNull(futures, "futures");

        List<CompletableFuture<Map.Entry<T, TriState>>> filteredFutures =
                new ArrayList<>(futures.size());
        for (CompletableFuture<T> future : futures) {
            filteredFutures.add(future.thenCompose((a) -> filter.apply(a).thenApply(allowed -> new AbstractMap.SimpleImmutableEntry<>(a, allowed))));
        }
        return CompletableFuture.allOf(
                filteredFutures.toArray(new CompletableFuture[0])
        ).thenApply((ignore) -> {
            List<T> results = new ArrayList<>(filteredFutures.size());
            for (CompletableFuture<Map.Entry<T, TriState>> filteredFuture : filteredFutures) {
                // By now, all futures are complete -- join() will not block
                Map.Entry<T, TriState> entry = filteredFuture.join();
                if (entry.getValue() == TriState.TRUE) {
                    results.add(entry.getKey());
                }
            }
            return results;
        });
    }

    /**
     * A helper method to create exceptionally completed {@link CompletableFuture futures}.
     * <p>
     * This is needed because Treasury API compiles on Java 8.
     *
     * @param error the error to complete with
     * @return completable future which has been completed exceptionally
     * @param <T> type parameter
     */
    @NotNull
    public static <T> CompletableFuture<T> failedFuture(@NotNull Throwable error) {
        CompletableFuture<T> ret = new CompletableFuture<>();
        ret.completeExceptionally(error);
        return ret;
    }

}
