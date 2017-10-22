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
package org.yx.bean;

import java.util.ArrayList;
import java.util.List;

import org.yx.common.StartConstants;
import org.yx.listener.Listener;
import org.yx.listener.SumkEvent;
import org.yx.log.Log;
import org.yx.util.StringUtil;

public abstract class AbstractBeanListener implements Listener<BeanEvent> {

	protected List<String> packages = new ArrayList<String>(128);
	protected boolean valid = false;

	public AbstractBeanListener(String packs) {
		if (StringUtil.isEmpty(packs)) {
			return;
		}
		String[] ps = packs.replace('，', ',').split(",");
		for (String p : ps) {
			addPackage(p);
		}
		valid = this.packages.size() > 0;
	}

	public boolean addPackage(String p) {
		p = p.trim();
		if (p.isEmpty()) {
			return false;
		}
		List<String> ps = this.packages;
		ps = new ArrayList<>(ps);
		String p2 = p + ".";
		if (!ps.contains(p2)) {
			ps.add(p2);
			this.packages = ps;
			Log.get("sumk.bean").trace("add package {}", p);
			return true;
		}
		return false;
	}

	@Override
	public boolean accept(SumkEvent event) {
		if (!valid) {
			return false;
		}
		if (!BeanEvent.class.isInstance(event)) {
			return false;
		}
		String clzName = ((BeanEvent) event).clz().getName();
		if (clzName.startsWith(StartConstants.INNER_PACKAGE + ".")) {
			return true;
		}
		List<String> packs = this.packages;
		for (String pack : packs) {
			if (clzName.startsWith(pack)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String[] getTags() {
		return null;
	}

}
