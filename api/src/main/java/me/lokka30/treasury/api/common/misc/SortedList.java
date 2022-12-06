/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.misc;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a sorted list.
 * <p>This implementation of a sorted list is backed by a {@link LinkedList}, with a custom sorting
 * algorithm added, since {@link java.util.List#sort(Comparator)} proves inefficient when dealing
 * with bigger lists.
 * <p>Benchmarked with this code using the JMH library:
 * <pre>
 * &#64;BenchmarkMode(Mode.AverageTime)
 * &#64;OutputTimeUnit(TimeUnit.NANOSECONDS)
 * &#64;Fork(value = 3)
 * &#64;State(Scope.Benchmark)
 * public class SortedListBenchmark {
 *
 *   &#64;Benchmark
 *   public static void sortedList(Blackhole blackhole) {
 *     SortedList<Integer> list = new SortedList<>();
 *     list.add(3);
 *     list.add(5);
 *     list.add(2);
 *     list.add(6);
 *     blackhole.consume(list);
 *   }
 *
 * }
 * </pre>
 * we've got results of 33.039 ± 3.311 ns/op .
 *
 * @param <T> type
 * @author <a href="mailto:ivan@mrivanplays.com">Ivan Pekov</a>
 */
public class SortedList<T extends Comparable<T>> extends LinkedList<T> {

    @Override
    public boolean add(@NotNull T t) {
        int index = 0;
        while (index < size() && t.compareTo(get(index)) > 0) {
            index++;
        }

        super.add(index, t);
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> add) {
        if (add.isEmpty()) {
            return false;
        }
        for (T val : add) {
            add(val);
        }
        return true;
    }

}
