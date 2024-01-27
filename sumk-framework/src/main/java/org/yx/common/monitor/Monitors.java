package org.yx.common.monitor;

import java.util.ArrayList;
import java.util.List;

public class Monitors {
	private static final List<MessageProvider> providers = new ArrayList<>(5);
	public static final String BLANK = "  ";

	public static synchronized void add(MessageProvider provider) {
		if (!providers.contains(provider)) {
			providers.add(provider);
		}
	}

	public static Object getMessage(String type, String key, Object param) {
		for (MessageProvider p : providers) {
			Object msg = p.get(type, key, param);
			if (msg != null) {
				return msg;
			}
		}
		return null;
	}
}
