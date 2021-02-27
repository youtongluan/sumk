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
package org.yx.listener;

import java.util.List;
import java.util.Objects;

import org.yx.exception.SumkException;
import org.yx.util.CollectionUtil;

public class ListenerGroupImpl<T extends SumkListener> implements ListenerGroup<T> {

	private SumkListener[] listeners = new SumkListener[0];

	@Override
	public void listen(SumkEvent event) {
		for (SumkListener lin : listeners) {
			lin.listen(event);
		}
	}

	@Override
	public void setListener(T[] listeners) {
		for (SumkListener lis : Objects.requireNonNull(listeners)) {
			if (lis == null) {
				throw new SumkException(2453451, "监听器不能为null");
			}
		}
		this.listeners = listeners;

	}

	@Override
	public List<SumkListener> getListeners() {
		return CollectionUtil.unmodifyList(this.listeners);
	}
}