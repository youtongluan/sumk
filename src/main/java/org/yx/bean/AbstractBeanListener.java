/**
 * Copyright (C) 2016 - 2030 youtongluan.
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

import java.util.HashSet;
import java.util.Set;

import org.yx.listener.SumkEvent;
import org.yx.util.StringUtil;

public abstract class AbstractBeanListener implements BeanEventListener {

	protected final String[] packages;
	protected boolean valid;

	public AbstractBeanListener(String packs) {
		Set<String> pks = new HashSet<>();
		String[] ps = packs == null ? new String[0] : StringUtil.toLatin(packs).split(",");
		for (String p : ps) {
			p = p.trim();
			if (p.isEmpty()) {
				continue;
			}
			if (!p.endsWith(".")) {
				p += ".";
			}
			pks.add(p);
		}
		valid = pks.size() > 0;
		pks.add("org.yx.");
		this.packages = pks.toArray(new String[pks.size()]);
	}

	@Override
	public void listen(SumkEvent event) {
		if (this.accept(event)) {
			this.onListen((BeanEvent) event);
		}

	}

	protected abstract void onListen(BeanEvent cast);

	private boolean accept(SumkEvent event) {
		if (!valid) {
			return false;
		}
		if (!(event instanceof BeanEvent)) {
			return false;
		}
		String clzName = ((BeanEvent) event).clz().getName();
		for (String pack : packages) {
			if (clzName.startsWith(pack)) {
				return true;
			}
		}
		return false;
	}

}
