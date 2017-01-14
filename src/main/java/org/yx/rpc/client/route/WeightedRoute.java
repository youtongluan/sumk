/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.yx.rpc.client.route;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.yx.rpc.Host;

public class WeightedRoute {
	private volatile int currentIndex = -1;
	private volatile int currentWeight = 0;
	private int maxWeight;
	private int gcdWeight;
	private List<ServerMachine> serverList;

	private static int gcd(int a, int b) {
		BigInteger b1 = new BigInteger(String.valueOf(a));
		BigInteger b2 = new BigInteger(String.valueOf(b));
		BigInteger gcd = b1.gcd(b2);
		return gcd.intValue();
	}

	private static int getGCDForServers(List<ServerMachine> serverList) {
		int w = 0;
		for (int i = 0, len = serverList.size(); i < len - 1; i++) {
			if (w == 0) {
				w = gcd(serverList.get(i).weight, serverList.get(i + 1).weight);
			} else {
				w = gcd(w, serverList.get(i + 1).weight);
			}
		}
		return w;
	}

	private static int getMaxWeightForServers(List<ServerMachine> serverList) {
		int w = 0;
		for (ServerMachine s : serverList) {
			if (s.getWeight() > w) {
				w = s.getWeight();
			}
		}
		return w;
	}

	public Host getUrl() {
		ServerMachine sm = this.GetServer();
		if (sm == null) {
			return null;
		}
		return sm.getUrl();
	}

	private ServerMachine GetServer() {
		List<ServerMachine> list = serverList;
		int serverCount = list.size();
		for (int i = 0; i < serverCount * 2; i++) {
			currentIndex = (currentIndex + 1) % serverCount;
			if (currentIndex == 0) {
				currentWeight = currentWeight - gcdWeight;
				if (currentWeight <= 0) {
					currentWeight = maxWeight;
					if (currentWeight == 0)
						return null;
				}
			}

			int index = currentIndex % serverCount;
			if (list.get(index).weight >= currentWeight) {
				if (HostChecker.instance().isDowned(list.get(index).getUrl())) {
					continue;
				}
				return list.get(index);
			}
		}
		return null;
	}

	public void addServer(ServerMachine s) {
		List<ServerMachine> list = new ArrayList<>(serverList);
		list.add(s);
		init(list);
	}

	public void removeServer(Host url) {
		List<ServerMachine> list = new ArrayList<>(serverList);
		for (ServerMachine s : list) {
			if (s.getUrl().equals(url)) {
				list.remove(s);
				break;
			}
		}
		init(list);
	}

	public WeightedRoute(ServerMachine... servers) {
		List<ServerMachine> list = new ArrayList<>();
		Arrays.stream(servers).forEach(s -> list.add(s));
		init(list);
	}

	public WeightedRoute(Collection<ServerMachine> servers) {
		init(servers);
	}

	private void init(Collection<ServerMachine> list) {
		serverList = new ArrayList<ServerMachine>(list.size());
		serverList.addAll(list);
		maxWeight = getMaxWeightForServers(serverList);
		gcdWeight = getGCDForServers(serverList);
	}

}
