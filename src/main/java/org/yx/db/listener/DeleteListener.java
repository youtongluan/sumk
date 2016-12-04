package org.yx.db.listener;

import org.yx.bean.Bean;
import org.yx.db.event.DeleteEvent;
import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.PojoMetaHolder;
import org.yx.listener.SumkEvent;
import org.yx.log.Log;
import org.yx.redis.RecordReq;

@Bean
public class DeleteListener implements DBListener<DeleteEvent> {

	@Override
	public boolean accept(SumkEvent event) {
		return DeleteEvent.class.isInstance(event);
	}

	@Override
	public void listen(DeleteEvent event) {
		try {
			PojoMeta pm = PojoMetaHolder.getTableMeta(event.getTable());
			if (pm == null || pm.isNoCache()) {
				return;
			}
			Object src = event.getWhere();
			String id = pm.getRedisID(src, true);
			RecordReq.del(pm, id);
		} catch (Exception e) {
			Log.printStack("db-listener", e);
		}
	}

	@Override
	public String[] getTags() {
		return null;
	}

}
