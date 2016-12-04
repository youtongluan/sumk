package org.yx.conf;

import java.nio.charset.Charset;

import org.yx.rpc.codec.Protocols;

public class Profile {
	public final static Charset CHARSET_DEFAULT = Charset.forName("UTF-8");
	public final static int version = 0x160;

	public static long feature() {
		long v = version;
		v <<= 32;
		v |= Protocols.profile();
		return v;
	}

	public static String featureInHex() {
		return Long.toHexString(feature());
	}

}
