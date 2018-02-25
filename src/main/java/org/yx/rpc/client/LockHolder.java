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
package org.yx.rpc.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.yx.exception.CodeException;
import org.yx.log.Log;
import org.yx.main.SumkThreadPool;
import org.yx.rpc.server.Response;

public final class LockHolder {
	private static final ConcurrentHashMap<String, RpcLocker> locks = new ConcurrentHashMap<>();

	static final LockTimeoutMonitor monitor = new LockTimeoutMonitor();
	static {
		SumkThreadPool.scheduledExecutor.scheduleWithFixedDelay(monitor, 1000, 500, TimeUnit.MILLISECONDS);
	}

	static void register(RpcLocker r, long endTime) {
		Req req = r.req;
		monitor.add(req.getSn(), endTime);
		if (locks.putIfAbsent(req.getSn(), r) != null) {
			throw new CodeException(-111111111, req.getSn() + " duplicate!!!!!!!!!!!!!!!!!!!!!");
		}
	}

	static void unLockAndSetResult(Response resp) {
		RpcLocker r = locks.remove(resp.sn());
		if (r == null) {
			return;
		}
		RpcResult result = new RpcResult(resp.json(), resp.exception());
		r.wakeup(result);
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

	private static class LockTimeoutMonitor implements Runnable {

		private static final DelayQueue<DelayedObject> QUEUE = new DelayQueue<DelayedObject>();

		@Override
		public void run() {
			try {
				DelayedObject delay;
				while ((delay = QUEUE.poll()) != null) {
					RpcLocker locker = LockHolder.remove(delay.sn);
					if (locker != null) {

						locker.wakeup(RpcResult.timeout(locker.req));
					}
				}
			} catch (Exception e) {
				Log.printStack(e);
				return;
			}

		}

		void add(String reqId, long endTime) {
			QUEUE.add(new DelayedObject(reqId, endTime));
		}
	}

	private static class DelayedObject implements Delayed {

		private long endTime;
		final String sn;

		public DelayedObject(String sn, long endTime) {
			this.endTime = endTime;
			this.sn = sn;
		}

		public long getDelay(TimeUnit unit) {
			return unit.convert(endTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}

		public int compareTo(Delayed other) {
			long d = other instanceof DelayedObject ? this.endTime - DelayedObject.class.cast(other).endTime
					: this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
			return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
		}
	}

}
