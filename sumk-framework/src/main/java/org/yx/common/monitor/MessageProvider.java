package org.yx.common.monitor;

public interface MessageProvider {

	Object get(String type, String key, Object param);
}
