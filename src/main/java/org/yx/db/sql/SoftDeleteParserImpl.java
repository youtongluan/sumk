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

import java.util.List;

import org.yx.annotation.db.SoftDelete;
import org.yx.db.enums.ValidRecord;
import org.yx.exception.SumkException;

public class SoftDeleteParserImpl implements SoftDeleteParser {

	public Object parseValue(Class<?> type, String value) {
		if (type == String.class) {
			return value;
		}
		if (Integer.class == type || int.class == type) {
			return Integer.valueOf(value);
		}
		if (Byte.class == type || byte.class == type) {
			return Byte.valueOf(value);
		}
		if (Short.class == type || short.class == type) {
			return Short.valueOf(value);
		}
		if (Long.class == type || long.class == type) {
			return Long.valueOf(value);
		}
		if (Double.class == type || double.class == type) {
			return Double.valueOf(value);
		}
		if (Float.class == type || float.class == type) {
			return Float.valueOf(value);
		}
		throw new SumkException(234267, type.getName() + " is not supported by soft delete");
	}

	@Override
	public SoftDeleteMeta parse(SoftDelete sd, List<ColumnMeta> fieldMetas) {
		if (sd == null) {
			return null;
		}
		String name = sd.value();
		boolean provided = false;
		for (ColumnMeta f : fieldMetas) {
			if (name.equalsIgnoreCase(f.getFieldName())) {
				provided = true;
				break;
			}
		}
		if (sd.type() == Boolean.class || sd.type() == boolean.class) {
			return new SoftDeleteMeta(sd.value(), Boolean.TRUE, Boolean.FALSE, true, provided);
		}
		boolean equal = sd.whatIsValid() == ValidRecord.EQUAL_VALID;
		return new SoftDeleteMeta(sd.value(), parseValue(sd.type(), sd.validValue()),
				parseValue(sd.type(), sd.inValidValue()), equal, provided);
	}

}
