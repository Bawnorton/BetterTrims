package com.bawnorton.bettertrims.util;

import java.util.Set;
import java.util.function.Consumer;

public class SynchronizedDualSet<A, B> implements DualSet<A, B> {
    private final DualSet<A, B> delegate;
    private final Object mutex;

    private SynchronizedDualSet(DualSet<A, B> dualSet) {
        this.delegate = dualSet;
        this.mutex = new Object();
    }

    public static <A, B> SynchronizedDualSet<A, B> of(DualSet<A, B> dualSet) {
        return new SynchronizedDualSet<>(dualSet);
    }

    public Set<A> getSetA() {
        synchronized (mutex) {
            return delegate.getSetA();
        }
    }

    public Set<B> getSetB() {
        synchronized (mutex) {
            return delegate.getSetB();
        }
    }

    public boolean addA(A a) {
        synchronized (mutex) {
            return delegate.addA(a);
        }
    }

    @Override
    public boolean addB(B b) {
        synchronized (mutex) {
            return delegate.addB(b);
        }
    }

    @Override
    public void addAllA(Set<A> as) {
        synchronized (mutex) {
            delegate.addAllA(as);
        }
    }

    @Override
    public void addAllB(Set<B> bs) {
        synchronized (mutex) {
            delegate.addAllB(bs);
        }
    }

    @Override
    public boolean containsA(A a) {
        synchronized (mutex) {
            return delegate.containsA(a);
        }
    }

    @Override
    public boolean containsB(B b) {
        synchronized (mutex) {
            return delegate.containsB(b);
        }
    }

    @Override
    public boolean removeA(A a) {
        synchronized (mutex) {
            return delegate.removeA(a);
        }
    }

    @Override
    public boolean removeB(B b) {
        synchronized (mutex) {
            return delegate.removeB(b);
        }
    }

    @Override
    public void clear() {
        synchronized (mutex) {
            delegate.clear();
        }
    }

    @Override
    public void forEachA(Consumer<A> aConsumer) {
        synchronized (mutex) {
            delegate.forEachA(aConsumer);
        }
    }

    @Override
    public void forEachB(Consumer<B> bConsumer) {
        synchronized (mutex) {
            delegate.forEachB(bConsumer);
        }
    }
}
