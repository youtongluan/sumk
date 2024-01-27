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
package org.yx.db.conn;

import java.util.Objects;
import java.util.function.Consumer;

import org.yx.db.enums.TxHook;

class SqlSessionHook {
	private final TxHook type;
	private final Consumer<HookContext> action;

	public SqlSessionHook(TxHook type, Consumer<HookContext> action) {
		this.type = Objects.requireNonNull(type);
		this.action = Objects.requireNonNull(action);
	}

	public TxHook getType() {
		return type;
	}

	public Consumer<HookContext> getAction() {
		return action;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SqlSessionHook other = (SqlSessionHook) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
