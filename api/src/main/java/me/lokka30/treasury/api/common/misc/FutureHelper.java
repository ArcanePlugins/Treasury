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
     * Does exactly what {@link #mapJoinFilter(Function, Function, Collection)} does except that
     * it doesn't map data types.
     *
     * @param filter filter
     * @param futures futures
     * @param <T> type parameter
     * @return future with collection
     */
    @NotNull
    public static <T> CompletableFuture<Collection<T>> joinAndFilter(
            @NotNull Function<T, CompletableFuture<TriState>> filter,
            @NotNull Collection<CompletableFuture<T>> futures
    ) {
        return mapJoinFilter(filter, Function.identity(), futures);
    }

    /**
     * Maps, joins, and filters futures.
     * Maps all the futures holding data type A to return a collection of data type B
     * via the specified {@code mapper} {@link Function} and filters them via the specified
     * filter function.
     *
     * @param filter  filter
     * @param mapper  mapper
     * @param futures futures of data type A
     * @param <A>     type parameter A
     * @param <B>     type parameter B
     * @return future with collection of data type B
     */
    @NotNull
    public static <A, B> CompletableFuture<Collection<B>> mapJoinFilter(
            @NotNull Function<A, CompletableFuture<TriState>> filter,
            @NotNull Function<A, B> mapper,
            @NotNull Collection<CompletableFuture<A>> futures
    ) {
        Objects.requireNonNull(filter, "filter");
        Objects.requireNonNull(mapper, "mapper");
        Objects.requireNonNull(futures, "futures");

        List<CompletableFuture<Map.Entry<A, TriState>>> filteredFutures =
                new ArrayList<>(futures.size());
        for (CompletableFuture<A> future : futures) {
            filteredFutures.add(future.thenCompose((a) -> filter
                    .apply(a)
                    .thenApply((allowed) -> new AbstractMap.SimpleImmutableEntry<>(a, allowed))));
        }
        return CompletableFuture.allOf(
                filteredFutures.toArray(new CompletableFuture[0])
        ).thenApply((ignore) -> {
            List<B> results = new ArrayList<>(filteredFutures.size());
            for (CompletableFuture<Map.Entry<A, TriState>> filteredFuture : filteredFutures) {
                // By now, all futures are complete -- join() will not block
                Map.Entry<A, TriState> entry = filteredFuture.join();
                if (entry.getValue() == TriState.TRUE) {
                    results.add(mapper.apply(entry.getKey()));
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
