package org.yx.db.listener;

import org.yx.bean.Bean;
import org.yx.db.annotation.CacheType;
import org.yx.db.event.UpdateEvent;
import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.PojoMetaHolder;
import org.yx.listener.SumkEvent;
import org.yx.log.Log;
import org.yx.redis.RecordReq;
import org.yx.util.GsonUtil;

@Bean
public class UpdateListener implements DBListener<UpdateEvent> {

	@Override
	public boolean accept(SumkEvent event) {
		return UpdateEvent.class.isInstance(event);
	}

	@Override
	public void listen(UpdateEvent event) {
		try {
			PojoMeta pm = PojoMetaHolder.getTableMeta(event.getTable());
			if (pm == null || pm.isNoCache()) {
				return;
			}
			Object src = event.getPojo();
			String id = pm.getRedisID(src, true);
			if (pm.cacheType() == CacheType.LIST) {
				RecordReq.del(pm, id);
				return;
			}
			if (event.isFullUpdate()) {
				RecordReq.set(pm, id, GsonUtil.toJson(src));
			} else {
				RecordReq.del(pm, id);
			}

		} catch (Exception e) {
			Log.printStack("db-listener", e);
		}
	}

	@Override
	public String[] getTags() {
		return null;
	}

}
