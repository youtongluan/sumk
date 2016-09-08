package org.yx.db;

public class AbstractCachable implements Cachable {

	private boolean cacheEnable;
	@Override
	public boolean isCacheEnable() {
		return cacheEnable;
	}

	@Override
	public void setCacheEnable(boolean cache) {
		this.cacheEnable=cache;
	}

	@Override
	public String getModule() {
		return null;
	}

}
