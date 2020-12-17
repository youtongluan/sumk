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
package org.yx.conf;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Predicate;

import org.yx.common.matcher.BooleanMatcher;
import org.yx.common.matcher.Matchers;
import org.yx.log.RawLog;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public final class LocalhostUtil {

	private static String localIp = null;
	private static String[] localIps = null;
	private static Predicate<String> matcher = defaultMatcher();

	private static Predicate<String> defaultMatcher() {

		Predicate<String> m = Matchers.createWildcardMatcher("10.*,172.*,16.*,192.*,168.*", 1);
		Predicate<String> not1 = Matchers.createWildcardMatcher("*.1", 1).negate();
		return m.and(not1);
	}

	private static boolean isValid(InetAddress ia) {
		if (Inet6Address.class.isInstance(ia) && AppInfo.getBoolean("sumk.local.ipv6.disable", false)) {
			return false;
		}
		return !ia.isAnyLocalAddress() && !ia.isLoopbackAddress();
	}

	private static String getHostAddress(InetAddress ia) {
		String address = ia.getHostAddress();
		int index = address.indexOf('%');
		if (index > 0 && AppInfo.getBoolean("sumk.ipv6.pure", true)) {
			address = address.substring(0, index);
		}
		return address;
	}

	public static synchronized boolean setLocalIp(String ip) {
		if (ip == null) {
			ip = "";
		}
		try {
			ip = StringUtil.toLatin(ip).trim();
			if (ip.contains(Matchers.WILDCARD) || ip.contains(",")) {
				matcher = Matchers.createWildcardMatcher(ip, 1);
				resetLocalIp();
				return true;
			}
			matcher = defaultMatcher();
			if (ip.length() > 0) {
				localIp = ip;
			} else {
				resetLocalIp();
			}
			return true;
		} catch (Exception e) {
			RawLog.error("sumk.conf", e.getMessage(), e);
			return false;
		}
	}

	public static String getLocalIP() {
		if (localIp != null) {// localIp不会从非null变为null
			return localIp;
		}
		resetLocalIp();
		return localIp;
	}

	private synchronized static void resetLocalIp() {
		try {
			String ip = getLocalIP0();
			if (ip != null && ip.length() > 0) {
				localIp = ip;
				return;
			}
		} catch (Exception e) {
			RawLog.error("sumk.conf", e.getMessage(), e);
		}
		if (localIp == null) {
			localIp = "0.0.0.0";
		}
	}

	private static String getLocalIP0() throws Exception {
		String[] ips = getLocalIps();
		Predicate<String> matcher = LocalhostUtil.matcher;
		if (matcher == null) {
			matcher = BooleanMatcher.TRUE;
		}
		for (String ip : ips) {
			if (matcher.test(ip)) {
				return ip;
			}
		}
		if (ips.length > 0) {
			RawLog.warn("sumk.conf", "没有合适ip，使用第一个ip，列表为:" + Arrays.toString(ips));
			return ips[0];
		}
		RawLog.warn("sumk.conf", "找不到任何ip，使用0.0.0.0");
		return "0.0.0.0";
	}

	private static synchronized String[] getLocalIps() {
		if (localIps != null) {
			return localIps;
		}
		List<String> ipList = new ArrayList<String>();
		try {
			Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) e1.nextElement();
				if (!isUp(ni)) {
					continue;
				}
				Enumeration<?> e2 = ni.getInetAddresses();
				while (e2.hasMoreElements()) {
					InetAddress ia = (InetAddress) e2.nextElement();
					if (isValid(ia)) {
						ipList.add(getHostAddress(ia));
					}

				}
			}
		} catch (SocketException e) {
			RawLog.error("sumk.conf", e.getMessage(), e);
		}
		if (ipList.isEmpty()) {
			return new String[0];
		}
		localIps = ipList.toArray(new String[ipList.size()]);
		return localIps;
	}

	/**
	 * 网卡是否有效
	 * 
	 * @param ni
	 * @return
	 * @throws SocketException
	 */
	private static boolean isUp(NetworkInterface ni) throws SocketException {
		return (!ni.isVirtual()) && ni.isUp() && (!ni.isLoopback());
	}

	public static List<String> getLocalIPList() {
		return CollectionUtil.unmodifyList(getLocalIps());
	}
}
