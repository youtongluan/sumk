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
package org.yx.common.route;

import java.util.Objects;

public abstract class AbstractWeightedServer<T> implements WeightedServer<T> {

	protected final T source;

	private int weight = 1;

	public AbstractWeightedServer(T source) {
		this.source = Objects.requireNonNull(source);
	}

	@Override
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight > 0 ? weight : 0;
	}

	@Override
	public T getSource() {
		return this.source;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + weight;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractWeightedServer<?> other = (AbstractWeightedServer<?>) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

	@Override
	public int compareTo(WeightedServer<T> o) {
		return Integer.compare(o.getWeight(), this.getWeight());
	}

	@Override
	public String toString() {
		return "WeightedSource [source=" + source + ", weight=" + weight + "]";
	}

}
