/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
