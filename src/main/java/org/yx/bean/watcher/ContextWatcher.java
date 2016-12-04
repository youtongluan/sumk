package org.yx.bean.watcher;

public interface ContextWatcher extends IOCWatcher {

	/**
	 * IOC初始化完成后调用
	 */
	void contextInitialized();
}
