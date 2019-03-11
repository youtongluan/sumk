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
package org.yx.db.sql;

import org.yx.annotation.db.SoftDelete;
import org.yx.exception.SumkException;

public class SoftDeleteParserImpl implements SoftDeleteParser {

	public Object parseValue(Class<?> type, String value) {
		if (type == String.class) {
			return value;
		}
		if (Integer.class == type) {
			return Integer.valueOf(value);
		}
		if (Byte.class == type) {
			return Byte.valueOf(value);
		}
		if (Short.class == type) {
			return Short.valueOf(value);
		}
		if (Long.class == type) {
			return Long.valueOf(value);
		}
		if (Double.class == type) {
			return Double.valueOf(value);
		}
		if (Float.class == type) {
			return Float.valueOf(value);
		}
		throw new SumkException(234267, type.getName() + " is not supported by soft delete");
	}

	@Override
	public SoftDeleteMeta parse(SoftDelete sd) {
		if (sd == null) {
			return null;
		}
		if (sd.columnType() == Boolean.class) {
			return new SoftDeleteMeta(sd.value(), Boolean.TRUE, Boolean.FALSE, sd.columnType());
		}
		return new SoftDeleteMeta(sd.value(), parseValue(sd.columnType(), sd.validValue()),
				parseValue(sd.columnType(), sd.inValidValue()), sd.columnType());
	}
}
