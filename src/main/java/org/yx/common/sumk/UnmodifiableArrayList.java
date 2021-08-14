/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.common.sumk;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public final class UnmodifiableArrayList<E> extends AbstractList<E> implements RandomAccess, Serializable {

	private static final long serialVersionUID = 1L;

	private final Object[] elements;

	public UnmodifiableArrayList(E[] array) {
		elements = Objects.requireNonNull(array);
	}

	public UnmodifiableArrayList(Collection<E> col) {

		this.elements = col.toArray();
	}

	@Override
	public int size() {
		return elements.length;
	}

	@Override
	public Object[] toArray() {
		return elements.clone();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		System.arraycopy(this.elements, 0, a, 0, size());
		return a;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		return (E) elements[index];
	}

	@Override
	public int indexOf(Object o) {
		Object[] a = this.elements;
		if (o == null) {
			for (int i = 0; i < a.length; i++)
				if (a[i] == null)
					return i;
		} else {
			for (int i = 0; i < a.length; i++)
				if (o.equals(a[i]))
					return i;
		}
		return -1;
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	@Override
	public Spliterator<E> spliterator() {
		return Spliterators.spliterator(elements, Spliterator.ORDERED);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void forEach(Consumer<? super E> action) {
		Objects.requireNonNull(action);
		for (Object e : elements) {
			action.accept((E) e);
		}
	}

	@Override
	public void sort(Comparator<? super E> c) {
		throw new UnsupportedOperationException();
	}

}
