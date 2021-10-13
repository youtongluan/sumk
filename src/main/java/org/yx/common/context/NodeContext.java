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
package org.yx.common.context;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.yx.asm.ParamPojo;

public abstract class NodeContext<T extends CalleeNode> {
	protected final T node;
	private ParamPojo argPojo;

	public NodeContext(T node) {
		this.node = Objects.requireNonNull(node);
	}

	public T node() {
		return node;
	}

	public ParamPojo getParamPojo() {
		return argPojo;
	}

	public void setParamPojo(ParamPojo argPojo) {
		this.argPojo = Objects.requireNonNull(argPojo);
	}

	/**
	 * @return 参数组成的map对象
	 */
	public Map<String, Object> getParams() {
		Object[] args = argPojo.params();
		int len = args.length;
		List<String> names = node.paramNames();
		Map<String, Object> map = new LinkedHashMap<>();
		for (int i = 0; i < len; i++) {
			String name = names.get(i);
			map.put(name, args[i]);
		}
		return map;
	}
}
