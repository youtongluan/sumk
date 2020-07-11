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
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class UnmodifiableArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Serializable {

	private static final long serialVersionUID = 1L;

	private final E[] elements;

	public UnmodifiableArrayList(E[] array) {
		elements = Objects.requireNonNull(array);
	}

	@SuppressWarnings("unchecked")
	public UnmodifiableArrayList(Collection<E> col, Class<E> clz) {
		this.elements = (E[]) Array.newInstance(clz, col.size());
		int i = 0;
		for (E e : col) {
			this.elements[i++] = e;
		}
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
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		int size = size();
		if (a.length < size)
			return Arrays.copyOf(this.elements, size, (Class<? extends T[]>) a.getClass());
		System.arraycopy(this.elements, 0, a, 0, size);
		if (a.length > size)
			a[size] = null;
		return a;
	}

	@Override
	public E get(int index) {
		return elements[index];
	}

	@Override
	public int indexOf(Object o) {
		E[] a = this.elements;
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

	@Override
	public void forEach(Consumer<? super E> action) {
		Objects.requireNonNull(action);
		for (E e : elements) {
			action.accept(e);
		}
	}

	@Override
	public void sort(Comparator<? super E> c) {
		throw new UnsupportedOperationException();
	}

}
