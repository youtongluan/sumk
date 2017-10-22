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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

import org.yx.exception.SoaException;
import org.yx.log.Log;
import org.yx.rpc.RpcCode;
import org.yx.rpc.server.Response;

public class RequestLocker {
	static Map<String, RespFuture> locks = new ConcurrentHashMap<>();

	public static RespFuture register(Req req) {
		RespFuture r = new RespFuture(req);
		locks.put(req.getSn(), r);
		return r;
	}

	public static void unLockAndSetResult(Response resp) {
		Log.get("sumk.SOA").trace("unlock:{}" + resp.getSn());
		RespFuture r = locks.remove(resp.getSn());
		if (r == null) {
			return;
		}
		r.resp = resp;
		r.unpark();
	}
}

class RespFuture {

	Response resp;

	final Req req;
	private final Thread thread;

	RespFuture(Req req) {
		this.req = req;
		this.thread = Thread.currentThread();
	}

	void unpark() {
		LockSupport.unpark(thread);
	}

	ReqResp getResponse(long timeout) {
		long start = req.getStart() > 100000 ? req.getStart() : System.currentTimeMillis();
		long end = start + timeout;
		while (resp == null && RequestLocker.locks.containsKey(req.getSn()) && end > System.currentTimeMillis()) {
			LockSupport.parkUntil(end);
		}
		if (resp == null) {
			String msg = "timeout in " + timeout + "ms,sn=" + req.getSn();
			throw new SoaException(RpcCode.TIMEOUT, msg, new TimeoutException(msg));
		}
		return new ReqResp(req, resp);
	}
}
