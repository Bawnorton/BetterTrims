package com.bawnorton.bettertrims.util;

import java.util.Set;
import java.util.function.Consumer;

public interface DualSet<A, B> {
    Set<A> getSetA();

    Set<B> getSetB();

    boolean addA(A a);

    boolean addB(B b);

    void addAllA(Set<A> aSet);

    void addAllB(Set<B> bSet);

    boolean containsA(A a);

    boolean containsB(B b);

    boolean removeA(A a);

    boolean removeB(B b);

    void clear();

    void forEachA(Consumer<A> aConsumer);

    void forEachB(Consumer<B> bConsumer);
}
