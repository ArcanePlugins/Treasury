/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.misc;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Represents a sorted list. Sorting algorithm's O(log2n) which is not that much efficient, with
 * O(n) moves. Not the fastest implementation, but it suits our needs.
 *
 * @param <T> type
 * @author MrIvanPlays
 */
public class SortedList<T extends Comparable<T>> extends LinkedList<T> {

    private int binarySearch(ListIterator<T> iterator, T key) {
        int low = 0;
        int high = iterator.previousIndex();
        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = get(iterator, mid);
            int cmp = midVal.compareTo(key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1); // key not found
    }

    private T get(ListIterator<T> iterator, int index) {
        T obj;
        int pos = iterator.nextIndex();
        if (pos <= index) {
            do {
                obj = iterator.next();
            } while (pos++ < index);
        } else {
            do {
                obj = iterator.previous();
            } while (--pos > index);
        }
        return obj;
    }

    private void move(ListIterator<T> iterator, int index) {
        int pos = iterator.nextIndex();
        if (pos == index) {
            return;
        }
        if (pos < index) {
            do {
                iterator.next();
            } while (++pos < index);
        } else {
            do {
                iterator.previous();
            } while (--pos > index);
        }
    }

    @Override
    public boolean add(T t) {
        ListIterator<T> iterator = listIterator(size());
        int idx = binarySearch(iterator, t);
        if (idx < 0) {
            idx = ~idx;
        }
        move(iterator, idx);
        iterator.add(t);
        return true;
    }

}
