package org.yx.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActStatis {

	private Map<String, Statis> actStatis = new ConcurrentHashMap<>();

	/**
	 * 对一次访问的统计
	 * 
	 * @param name
	 *            接口名
	 * @param time
	 *            接口消耗的时间
	 * @param success
	 *            接口访问是否成功
	 */
	public void visit(String name, long time, boolean success) {
		Statis statis = actStatis.get(name);
		if (statis == null) {
			statis = new Statis(name);
			Statis tmp = actStatis.putIfAbsent(name, statis);
			if (tmp != null) {
				statis = tmp;
			}
		}
		if (success) {
			statis.successVisit(time);
		} else {
			statis.failedVisit(time);
		}
	}

	public Map<String, Statis> getAndreset() {
		Map<String, Statis> tmp = this.actStatis;
		actStatis = new ConcurrentHashMap<>();
		return tmp;
	}

	public Map<String, Statis> getAll() {
		return this.actStatis;
	}
}
