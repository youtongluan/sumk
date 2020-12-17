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
package org.yx.util.kit;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Objects;
import java.util.function.BiFunction;

import org.yx.common.date.TimeUtil;
import org.yx.exception.SumkException;
import org.yx.util.StreamUtil;

public class TypeConverter {

	private static BiFunction<Object, Class<?>, Object> customConverter = (value, type) -> value;

	public static BiFunction<Object, Class<?>, Object> getCustomConverter() {
		return customConverter;
	}

	public static void setCustomConverter(BiFunction<Object, Class<?>, Object> customConverter) {
		TypeConverter.customConverter = Objects.requireNonNull(customConverter);
	}

	@SuppressWarnings("unchecked")
	public static <T> T convert(Object value, Class<T> type) throws Exception {
		if (value == null || type == value.getClass()) {
			return (T) value;
		}
		if (TimeUtil.isGenericDate(type)) {
			return TimeUtil.toType(value, type, false);
		}
		if (type.isInstance(value)) {
			return type.cast(value);
		}

		if (Number.class.isAssignableFrom(type) && Number.class.isInstance(value)) {
			T t = (T) toType((Number) value, type, false);
			if (t != null) {
				return t;
			}
		}
		if (type == byte[].class && Blob.class.isInstance(value)) {
			Blob v = (Blob) value;
			return (T) StreamUtil.readAllBytes(v.getBinaryStream(), true);
		}
		if (type == String.class && Clob.class.isInstance(value)) {
			Clob v = (Clob) value;
			return (T) StreamUtil.readAll(v.getCharacterStream(), true);
		}
		return (T) customConverter.apply(value, type);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Number> T toType(Number v, Class<?> type, boolean failIfNotSupport) {
		if (v.getClass() == type) {
			return (T) v;
		}
		if (Integer.class == type || int.class == type) {
			return (T) Integer.valueOf(v.intValue());
		}
		if (Long.class == type || long.class == type) {
			return (T) Long.valueOf(v.longValue());
		}
		if (Double.class == type || double.class == type) {
			return (T) Double.valueOf(v.doubleValue());
		}

		if (Byte.class == type || byte.class == type) {
			return (T) Byte.valueOf(v.byteValue());
		}
		if (Short.class == type || short.class == type) {
			return (T) Short.valueOf(v.shortValue());
		}
		if (Float.class == type || float.class == type) {
			return (T) Float.valueOf(v.floatValue());
		}

		if (BigDecimal.class == type) {
			if (Integer.class.isInstance(v) || Long.class.isInstance(v) || Short.class.isInstance(v)
					|| Byte.class.isInstance(v)) {
				return (T) new BigDecimal(v.longValue());
			}
			return (T) new BigDecimal(v.doubleValue());
		}
		if (failIfNotSupport || !Number.class.isAssignableFrom(type)) {
			throw new SumkException(927816546, type.getClass().getName() + "is not valid Number type");
		}
		return null;
	}
}
