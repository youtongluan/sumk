package org.yx.db.sql;

import org.yx.db.annotation.SoftDelete;

public interface SoftDeleteParser {

	Object parseValue(Class<?> type, String value);

	SoftDeleteMeta parse(SoftDelete sd);

}