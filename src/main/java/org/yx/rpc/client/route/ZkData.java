package org.yx.rpc.client.route;

import java.util.ArrayList;
import java.util.Collection;

import org.yx.util.StringUtils;

/**
 * zk中保存的节点信息
 * 
 * @author 游夏
 *
 */
public class ZkData {
	private Collection<IntfInfo> intfs = new ArrayList<IntfInfo>();
	int weight;
	long timeout;
	int clientCount;

	public Collection<IntfInfo> getIntfs() {
		return intfs;
	}

	/**
	 * 要先设置weight等属性
	 * 
	 * @param intf
	 */

	public void addIntf(IntfInfo intf) {
		this.intfs.add(intf);
	}

	void setWeight(String w) {
		if (StringUtils.isEmpty(w)) {
			return;
		}
		this.weight = Integer.parseInt(w);
	}

}
