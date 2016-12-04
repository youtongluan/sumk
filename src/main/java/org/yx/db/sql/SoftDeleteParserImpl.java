package org.yx.db.sql;

import org.yx.bean.Bean;
import org.yx.db.annotation.SoftDelete;
import org.yx.exception.SumkException;

@Bean
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
		return new SoftDeleteMeta(sd.value(), parseValue(sd.columnType(), sd.validValue()),
				parseValue(sd.columnType(), sd.inValidValue()), sd.columnType());
	}
}
