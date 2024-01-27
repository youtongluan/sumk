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
package org.yx.db.kit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.yx.common.util.S;
import org.yx.db.SDB;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.util.Loader;

public final class SDBuilder {
	private String name;
	private Map<String, Object> param;

	public SDBuilder name(String name) {
		this.name = name;
		return this;
	}

	@SuppressWarnings("unchecked")
	public SDBuilder param(Object p) {
		if (p == null) {
			this.param = null;
			return this;
		}
		if (p instanceof Map) {
			this.param = (Map<String, Object>) p;
			return this;
		}
		this.param = S.bean().beanToMap(p, false);
		return this;
	}

	public <T> List<T> list(Class<T> clz) {
		List<Map<String, Object>> list = SDB.list(name, param);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<T> retList = new ArrayList<>();
		for (Map<String, Object> ret : list) {
			retList.add(toBean(ret, clz));
		}
		return retList;
	}

	public <T> T queryOne(Class<T> clz) {
		Map<String, Object> ret = SDB.queryOne(name, param);
		return toBean(ret, clz);
	}

	private <T> T toBean(Map<String, Object> ret, Class<T> clz) {
		if (ret == null) {
			return null;
		}
		return S.bean().fillBeanIgnoreCaseAndUnderLine(ret, newInstance(clz));
	}

	private <T> T newInstance(Class<T> clz) {
		try {
			return Loader.newInstance(clz);
		} catch (Exception e) {
			Logs.db().error(e.toString(), e);
			throw new SumkException(234125435, "创建" + clz.getName() + "的实例失败，可能是它没有无参的构造函数");
		}
	}
}
