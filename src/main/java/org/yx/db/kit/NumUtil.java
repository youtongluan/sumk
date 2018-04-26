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

import org.yx.exception.SumkException;

public class NumUtil {

	@SuppressWarnings("unchecked")
	public static <T extends Number> T toType(Number v, Class<?> type, boolean failIfNotSupport) {
		if (v.getClass() == type) {
			return (T) v;
		}
		if (Byte.class == type) {
			return (T) Byte.valueOf(v.byteValue());
		}

		if (Short.class == type) {
			return (T) Short.valueOf(v.shortValue());
		}

		if (Integer.class == type) {
			return (T) Integer.valueOf(v.intValue());
		}

		if (Long.class == type) {
			return (T) Long.valueOf(v.longValue());
		}

		if (Float.class == type) {
			return (T) Float.valueOf(v.floatValue());
		}

		if (Double.class == type) {
			return (T) Double.valueOf(v.doubleValue());
		}
		if (failIfNotSupport || !Number.class.isAssignableFrom(type)) {
			throw new SumkException(927816546, type.getClass().getName() + "is not valid Number type");
		}
		return (T) v;
	}
}
