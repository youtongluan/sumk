package org.yx.util.secury;

import java.security.MessageDigest;

/**
 *
 * @author Administrator
 */
public class MD5Utils {

	public static String encrypt(byte[] data) throws Exception {
		MessageDigest md = MessageDigest.getInstance("md5");
		md.update(data);
		byte[] md5Result = md.digest();
		return parseByte2HexStr(md5Result);
	}

	private static String parseByte2HexStr(byte buf[]) {
		StringBuilder sb = new StringBuilder(32);
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
}
