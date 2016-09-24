package org.yx.rpc;

public final class RpcUtils {
	/**
	 * 返回appId
	 * 
	 * @param method
	 *            appId.clz.method
	 * @return
	 */
	public static String getAppId(String method) {
		int k = method.indexOf(".");
		return k > 0 ? method.substring(0, k) : method;
	}
}
