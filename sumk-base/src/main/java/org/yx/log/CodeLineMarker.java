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
package org.yx.log;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.slf4j.Marker;

public class CodeLineMarker implements Marker {

	private static final long serialVersionUID = 1L;

	private final String packageName;

	public CodeLineMarker(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public String getName() {
		return packageName;
	}

	@Override
	public void add(Marker reference) {

	}

	@Override
	public boolean remove(Marker reference) {
		return false;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public boolean hasReferences() {
		return false;
	}

	@Override
	public Iterator<Marker> iterator() {
		return new Iterator<Marker>() {

			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public Marker next() {
				throw new NoSuchElementException();
			}

		};
	}

	@Override
	public boolean contains(Marker other) {
		return false;
	}

	@Override
	public boolean contains(String name) {
		return false;
	}

}
