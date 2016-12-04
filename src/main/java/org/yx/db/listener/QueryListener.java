package org.yx.db.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yx.bean.Bean;
import org.yx.db.annotation.CacheType;
import org.yx.db.event.QueryEvent;
import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.PojoMetaHolder;
import org.yx.listener.SumkEvent;
import org.yx.log.Log;
import org.yx.redis.RecordReq;
import org.yx.util.GsonUtil;

@Bean
public class QueryListener implements DBListener<QueryEvent> {

	@Override
	public boolean accept(SumkEvent event) {
		return QueryEvent.class.isInstance(event);
	}

	@Override
	public void listen(QueryEvent event) {
		try {
			PojoMeta pm = PojoMetaHolder.getTableMeta(event.getTable());
			if (pm == null || pm.isNoCache() || event.getResult() == null) {
				return;
			}
			List<Map<String, Object>> in = event.getIn();
			if (in == null || in.size() != 1) {
				return;
			}

			Map<String, Object> where = in.get(0);
			if (!pm.isOnlyRedisID(where)) {
				return;
			}
			String id = pm.getRedisID(where, false);
			if (id == null) {
				return;
			}

			List<Object> list = new ArrayList<>();
			for (Object obj : event.getResult()) {
				if (id.equals(pm.getRedisID(obj, false))) {
					list.add(obj);
				}
			}
			if (list.isEmpty()) {
				return;
			}

			if (pm.cacheType() == CacheType.LIST) {
				RecordReq.set(pm, id, GsonUtil.toJson(list));
				return;
			}
			if (list.size() != 1 || list.get(0) == null) {
				return;
			}
			RecordReq.set(pm, id, GsonUtil.toJson(list.get(0)));
		} catch (Exception e) {
			Log.printStack("db-listener", e);
		}
	}

	@Override
	public String[] getTags() {
		return null;
	}

}
