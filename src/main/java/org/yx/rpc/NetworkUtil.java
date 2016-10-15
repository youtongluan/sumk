package org.yx.rpc;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.yx.log.Log;

public class NetworkUtil {

	private static String localIp = null;

	private static boolean isValid(InetAddress ia) {
		if (ia instanceof Inet6Address) {
			return false;
		}
		return ia.isSiteLocalAddress();

	}

	public static void setLocalIp(String ip) {
		try {
			ip = ip.trim();
			InetAddress ia = InetAddress.getByName(ip);
			if (Inet4Address.class.isInstance(ia)) {
				localIp = ip;
			}
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

	public static String getLocalIP() throws Exception {
		if (localIp != null) {
			return localIp;
		}
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
					localIp = ia.getHostAddress();
					return localIp;
				}
			}
		}
		localIp = InetAddress.getLocalHost().getHostAddress().toString();
		return localIp;
	}

	public static List<String> getLocalIPList() {
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
						ipList.add(ia.getHostAddress());
					}

				}
			}
		} catch (SocketException e) {
			Log.printStack(e);
		}
		return ipList;
	}

	/**
	 * 网卡是否有效
	 * 
	 * @param ni
	 * @return
	 * @throws SocketException
	 */
	private static boolean isUp(NetworkInterface ni) throws SocketException {
		return ni.getName().startsWith("eth") && (!ni.isVirtual()) && ni.isUp();
	}
}
