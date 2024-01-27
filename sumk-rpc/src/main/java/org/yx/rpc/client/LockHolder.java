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
package org.yx.rpc.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.rpc.server.Response;
import org.yx.util.Task;

public final class LockHolder {
	private static final ConcurrentMap<String, RpcLocker> locks = new ConcurrentHashMap<>();

	static final LockTimeoutMonitor monitor = new LockTimeoutMonitor();
	static {
		Task.scheduleAtFixedRate(monitor, 1000, 500);
	}

	static void register(RpcLocker r, long endTime) {
		Req req = r.req;
		if (locks.putIfAbsent(req.getSn(), r) != null) {

			throw new SumkException(-111111111, req.getSn() + " duplicate!!!!!!!!!!!!!!!!!!!!!");
		}
		monitor.add(req.getSn(), endTime);
	}

	static void unLockAndSetResult(Response resp) {
		RpcLocker r = locks.remove(resp.sn());
		if (r == null) {
			Log.get("sumk.rpc.client").debug("{} has been removed.maybe is timeout.result:{}", resp.sn(), resp.json());
			return;
		}
		RpcResult result = new RpcResult(resp.json(), resp.exception());
		r.wakeupAndLog(result);
	}

	static RpcLocker remove(String sn) {
		return locks.remove(sn);
	}

	static boolean containsKey(String sn) {
		return locks.containsKey(sn);
	}

	public static int lockSize() {
		return locks.size();
	}

	private static final class LockTimeoutMonitor implements Runnable {

		private static final DelayQueue<DelayedObject> QUEUE = new DelayQueue<>();

		@Override
		public void run() {
			try {
				DelayedObject delay;
				while ((delay = QUEUE.poll()) != null) {
					RpcLocker locker = LockHolder.remove(delay.sn);
					if (locker != null) {
						locker.wakeupAndLog(RpcResult.timeout(locker.req));
					}
				}
			} catch (Exception e) {
				Log.printStack("sumk.error", e);
				return;
			}

		}

		void add(String reqId, long endTime) {
			QUEUE.add(new DelayedObject(reqId, endTime));
		}
	}

	private static final class DelayedObject implements Delayed {

		private final long endTime;
		final String sn;

		public DelayedObject(String sn, long endTime) {
			this.endTime = endTime;
			this.sn = sn;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(endTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}

		@Override
		public int compareTo(Delayed other) {
			long d = other instanceof DelayedObject ? this.endTime - ((DelayedObject) other).endTime
					: this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
			return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
		}
	}

}
