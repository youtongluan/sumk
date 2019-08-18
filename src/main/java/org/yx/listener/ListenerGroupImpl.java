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

import java.util.ArrayList;
import java.util.List;

import org.yx.log.Log;

public class ListenerGroupImpl<T extends SumkEvent> implements ListenerGroup<T> {

	protected List<Listener<T>> listeners = new ArrayList<>();

	@Override
	public Listener<T> removeListener(Listener<T> listener) {
		List<Listener<T>> lis = this.listeners;
		lis = new ArrayList<>(lis);
		Listener<T> l = null;
		for (int i = lis.size() - 1; i >= 0; i--) {
			if (lis.get(i).equals(listener)) {
				l = lis.remove(i);
				break;
			}
		}
		if (l != null) {
			this.listeners = lis;
		}
		return l;
	}

	@Override
	public boolean addListener(Listener<T> listener) {
		List<Listener<T>> lis = this.listeners;
		lis = new ArrayList<>(lis);
		if (!lis.contains(listener)) {
			lis.add(listener);
			this.listeners = lis;
			Log.get("sumk.SYS").trace("add listener {}", listener.toString());
			return true;
		}
		return false;
	}

	@Override
	public void listen(T event) {
		List<Listener<T>> lis = this.listeners;
		for (Listener<T> lin : lis) {
			if (lin.accept(event)) {
				lin.listen(event);
			}
		}
	}

	@Override
	public int size() {
		return this.listeners.size();
	}

}