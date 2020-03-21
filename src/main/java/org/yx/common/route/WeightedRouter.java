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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WeightedRouter<T> implements Router<T> {

	protected int currentIndex = -1;

	protected int currentWeight;

	protected final int MAX_WEIGHT;

	protected final int GCD_WEIGHT;

	protected final WeightedServer<T>[] SERVERS;

	protected final int SERVER_COUNT;

	public WeightedRouter(Collection<WeightedServer<T>> servers) {
		@SuppressWarnings("unchecked")
		WeightedServer<T>[] ws = servers.toArray(new WeightedServer[servers.size()]);
		Arrays.sort(ws, (a, b) -> b.getWeight() - a.getWeight());
		SERVERS = ws;
		SERVER_COUNT = SERVERS.length;
		MAX_WEIGHT = getMaxWeightForServers();
		GCD_WEIGHT = getGCDForServers();
		this.currentWeight = this.MAX_WEIGHT;
	}

	private BigInteger gcd(BigInteger a, BigInteger b) {
		return a.gcd(b);
	}

	protected int getGCDForServers() {
		List<BigInteger> list = new ArrayList<>(SERVERS.length);
		for (WeightedServer<T> sm : SERVERS) {
			int weight = sm.getWeight();
			if (weight <= 0) {
				continue;
			}
			list.add(new BigInteger(String.valueOf(weight)));
		}
		BigInteger w = new BigInteger("0");
		for (int i = 0, len = list.size(); i < len - 1; i++) {
			if (w.intValue() == 0) {
				w = gcd(list.get(i), list.get(i + 1));
			} else {
				w = gcd(w, list.get(i + 1));
			}
		}
		int gcd = w.intValue();
		return gcd > 0 ? gcd : 1;
	}

	protected int getMaxWeightForServers() {
		int w = 0;
		for (WeightedServer<T> s : SERVERS) {
			if (s.getWeight() > w) {
				w = s.getWeight();
			}
		}
		return w;
	}

	@Override
	public List<T> allSources() {
		List<T> list = new ArrayList<>(this.SERVER_COUNT);
		for (WeightedServer<T> s : this.SERVERS) {
			list.add(s.getSource());
		}
		return list;
	}

	@Override
	public List<T> aliveSources() {
		List<T> list = new ArrayList<>(this.SERVER_COUNT);
		for (WeightedServer<T> s : this.SERVERS) {
			if (!s.isEnable()) {
				continue;
			}
			list.add(s.getSource());
		}
		return list;
	}

	@Override
	public T select() {
		int index = 0;

		for (int i = 0; i < SERVER_COUNT; i++) {
			index = (currentIndex + 1) % SERVER_COUNT;
			currentIndex = index;

			if (index == 0) {
				int tempWeight = currentWeight - GCD_WEIGHT;

				currentWeight = tempWeight < 1 ? MAX_WEIGHT : tempWeight;
			}

			WeightedServer<T> server = SERVERS[index];
			if (server.getWeight() >= currentWeight) {
				if (!server.isEnable()) {
					continue;
				}
				return server.getSource();
			}
		}

		for (int i = 0; i < SERVER_COUNT; i++) {
			WeightedServer<T> w = SERVERS[i % SERVER_COUNT];
			if (w.isEnable()) {
				return w.getSource();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "[MAX_WEIGHT=" + MAX_WEIGHT + ", GCD_WEIGHT=" + GCD_WEIGHT + ", SERVER_COUNT=" + SERVER_COUNT + "]";
	}

}
