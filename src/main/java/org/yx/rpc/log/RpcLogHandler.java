package org.yx.rpc.log;

/**
 * 这个是在客户端解析的,最好用异步方式处理日志。 线程变量要通过Req对象来获取，而不能取当前线程的
 */
public interface RpcLogHandler {
	void handle(RpcLog log);
}
