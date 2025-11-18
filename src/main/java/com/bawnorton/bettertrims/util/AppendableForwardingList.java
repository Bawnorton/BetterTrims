package com.bawnorton.bettertrims.util;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class AppendableForwardingList<T> extends AbstractList<T> {
	private final List<T> original;
	private final List<T> additionalElements;

	public AppendableForwardingList(List<T> original) {
		this.original = original;
		this.additionalElements = new ArrayList<>();
	}

	@Override
	public T get(int index) {
		int originalSize = original.size();
		if (index < originalSize) {
			return original.get(index);
		} else if (index - originalSize < additionalElements.size()) {
			return additionalElements.get(index - originalSize);
		} else {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
		}
	}

	@Override
	public int size() {
		return original.size() + additionalElements.size();
	}

	@Override
	public void add(int index, T element) {
		if (index != size()) {
			throw new UnsupportedOperationException("Can only append at end");
		}
		additionalElements.add(element);
		modCount++;
	}

	@Override
	public boolean add(T element) {
		additionalElements.add(element);
		modCount++;
		return true;
	}

	@Override
	public T set(int index, T element) {
		int originalSize = original.size();
		if (index < originalSize) {
			throw new UnsupportedOperationException("Cannot modify original list");
		}
		return additionalElements.set(index - originalSize, element);
	}

	@Override
	public T remove(int index) {
		int originalSize = original.size();
		if (index < originalSize) {
			throw new UnsupportedOperationException("Cannot modify original list");
		}
		modCount++;
		return additionalElements.remove(index - originalSize);
	}

	@Override
	public boolean remove(Object o) {
		boolean removed = additionalElements.remove(o);
		if (removed) modCount++;
		return removed;
	}

	@Override
	public void clear() {
		if (!additionalElements.isEmpty()) {
			additionalElements.clear();
			modCount++;
		}
	}

	@Override
	public @NotNull List<T> subList(int fromIndex, int toIndex) {
		int totalSize = size();
		if (fromIndex < 0 || toIndex > totalSize || fromIndex > toIndex) {
			throw new IndexOutOfBoundsException("fromIndex=" + fromIndex + ", toIndex=" + toIndex + ", size=" + totalSize);
		}

		int originalSize = original.size();
		List<T> result = new ArrayList<>(toIndex - fromIndex);

		if (toIndex <= originalSize) {
			result.addAll(original.subList(fromIndex, toIndex));
		} else if (fromIndex >= originalSize) {
			result.addAll(additionalElements.subList(fromIndex - originalSize, toIndex - originalSize));
		} else {
			result.addAll(original.subList(fromIndex, originalSize));
			result.addAll(additionalElements.subList(0, toIndex - originalSize));
		}

		return result;
	}

	@Override
	public @NotNull Iterator<T> iterator() {
		return new Iterator<>() {
			private final Iterator<T> origIt = original.iterator();
			private final Iterator<T> addIt = additionalElements.iterator();

			@Override
			public boolean hasNext() {
				return origIt.hasNext() || addIt.hasNext();
			}

			@Override
			public T next() {
				if (origIt.hasNext()) return origIt.next();
				if (addIt.hasNext()) return addIt.next();
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Removal via iterator not supported");
			}
		};
	}
}