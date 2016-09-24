package org.yx.rpc.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

import org.yx.exception.SoaException;
import org.yx.rpc.server.Response;

public class RequestLocker {
	static Map<String, RespFuture> locks = new ConcurrentHashMap<>();

	public static RespFuture register(Req req) {
		RespFuture r = new RespFuture(req);
		locks.put(req.getSn(), r);
		return r;
	}

	public static void unLockAndSetResult(Response resp) {
		RespFuture r = locks.remove(resp.getSn());
		if (r == null) {
			return;
		}
		r.resp = resp;
		r.unpark();
	}
}

class RespFuture {
	Response resp = null;
	private Req req;
	private Thread thread;

	public RespFuture(Req req) {
		this.req = req;
		this.thread = Thread.currentThread();
	}

	void unpark() {
		LockSupport.unpark(thread);
	}

	ReqResp getResponse(long timeout) {
		long start = req.getStart() > 100000 ? req.getStart() : System.currentTimeMillis();
		long end = start + timeout;
		do {
			LockSupport.parkUntil(end);
		} while (resp == null && RequestLocker.locks.containsKey(req.getSn()) && end > System.currentTimeMillis());
		if (resp == null) {
			String msg = "timeout in " + timeout + "ms,sn=" + req.getSn();
			SoaException.throwException(142234, msg, new TimeoutException(msg));
		}
		return new ReqResp(req, resp);
	}
}
