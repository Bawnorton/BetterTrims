package com.bawnorton.bettertrims.util;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class IterHelper {
    @SafeVarargs
    public static <T> Iterable<T> combine(Iterable<T>... iterables) {
        return () -> new Iterator<>() {
            private int index = 0;
            private Iterator<T> currentIterator = iterables[0].iterator();

            @Override
            public boolean hasNext() {
                if (currentIterator.hasNext()) return true;
                if (index + 1 < iterables.length) {
                    index++;
                    currentIterator = iterables[index].iterator();
                    return hasNext();
                }
                return false;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return currentIterator.next();
            }
        };
    }

    public static <T> List<T> toList(Iterable<T> iterable) {
        ArrayList<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
}
