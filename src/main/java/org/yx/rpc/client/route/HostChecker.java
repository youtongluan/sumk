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

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.main.SumkThreadPool;
import org.yx.rpc.Host;

public class HostChecker {

	private static HostChecker holder = new HostChecker();

	private HostChecker() {
		SumkThreadPool.scheduledExecutor.scheduleWithFixedDelay(new Checker(), 5,
				AppInfo.getInt("soa.hosts.check.period", 3), TimeUnit.SECONDS);
	}

	public static HostChecker get() {
		return holder;
	}

	private ConcurrentHashMap<Host, Long> downUrls = new ConcurrentHashMap<>();

	public boolean isDowned(Host url) {
		return downUrls.containsKey(url);
	}

	/**
	 * 将列表中不可用的url过滤掉
	 * 
	 * @param urls
	 * @return
	 */
	public List<Host> available(List<Host> urls) {
		List<Host> us = new ArrayList<Host>(urls);
		List<Host> avas = new ArrayList<Host>(us.size());
		for (Host u : us) {
			if (!downUrls.containsKey(u)) {
				avas.add(u);
			}
		}
		return avas;
	}

	public void addDownUrl(Host url) {
		downUrls.putIfAbsent(url, System.currentTimeMillis());
	}

	private class Checker implements Runnable {

		private int getTimeOut(int urlSize) {
			if (urlSize == 1) {
				return AppInfo.getInt("soa.socket.connecttimeout.1", 3000);
			} else if (urlSize == 2) {
				return AppInfo.getInt("soa.socket.connecttimeout.2", 2500);
			} else if (urlSize > 5) {
				return AppInfo.getInt("soa.socket.connecttimeout.5", 1000);
			}
			return AppInfo.getInt("soa.socket.connecttimeout.3", 2000);
		}

		@Override
		public void run() {
			if (downUrls.isEmpty()) {
				return;
			}
			Host[] urls = downUrls.keySet().toArray(new Host[0]);
			int timeout = getTimeOut(urls.length);
			for (Host url : urls) {
				try {
					Socket socket = new Socket();

					socket.connect(url.toInetSocketAddress(), timeout);
					if (socket.isConnected()) {
						socket.close();
						downUrls.remove(url);
					}
				} catch (UnknownHostException e) {
					Log.get("sumk.SYS").error(e.getMessage(), e);
				} catch (Exception e) {
				}
			}

		}

	}

}
