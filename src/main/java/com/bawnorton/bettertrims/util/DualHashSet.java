package com.bawnorton.bettertrims.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class DualHashSet<A, B> implements DualSet<A, B> {
    private final Set<A> setA;
    private final Set<B> setB;

    private final Function<A, B> a2b;
    private final Function<B, A> b2a;

    public DualHashSet(Function<A, B> a2b, Function<B, A> b2a) {
        this.a2b = a2b;
        this.b2a = b2a;
        this.setA = new HashSet<>();
        this.setB = new HashSet<>();
    }

    public Set<A> getSetA() {
        return setA;
    }

    public Set<B> getSetB() {
        return setB;
    }

    public boolean addA(A a) {
        setA.add(a);
        return setB.add(a2b.apply(a));
    }

    public boolean addB(B b) {
        setB.add(b);
        return setA.add(b2a.apply(b));
    }

    public void addAllA(Set<A> set) {
        setA.addAll(set);
        set.forEach(element -> setB.add(a2b.apply(element)));
    }

    public void addAllB(Set<B> set) {
        setB.addAll(set);
        set.forEach(element -> setA.add(b2a.apply(element)));
    }

    public boolean containsA(A a) {
        return setA.contains(a);
    }

    public boolean containsB(B b) {
        return setB.contains(b);
    }

    public boolean removeA(A a) {
        setA.remove(a);
        return setB.remove(a2b.apply(a));
    }

    public boolean removeB(B b) {
        setB.remove(b);
        return setA.remove(b2a.apply(b));
    }

    public void clear() {
        setA.clear();
        setB.clear();
    }

    @Override
    public void forEachA(Consumer<A> aConsumer) {
        setA.forEach(aConsumer);
    }

    @Override
    public void forEachB(Consumer<B> bConsumer) {
        setB.forEach(bConsumer);
    }
}
