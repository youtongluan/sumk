package org.yx.db.visit;

import java.util.Collection;
import java.util.List;

import org.yx.db.sql.PojoMeta;

/**
 * 本接口只负责实际的存取，实现类一般不需要null判断等
 */
public interface RecordAccess {

	String get(PojoMeta m, String id);

	List<String> getMultiValue(PojoMeta m, Collection<String> ids);

	void set(PojoMeta m, String id, String json);

	void del(PojoMeta m, String id);

	void delMulti(PojoMeta m, String[] ids);
}
