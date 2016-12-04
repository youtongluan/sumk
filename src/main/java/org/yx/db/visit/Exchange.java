package org.yx.db.visit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yx.db.sql.PojoMeta;
import org.yx.log.Log;
import org.yx.redis.RecordReq;
import org.yx.redis.RedisPool;

public class Exchange {
	/**
	 * 移除掉已经从数据库中获取的条件
	 */
	private List<Map<String, Object>> leftIn;

	private List<String> data;

	private List<Map<String, Object>> canToRedis;

	/**
	 * @param leftIn
	 *            它的引用有可能发生变化，但它的内容是不会变的
	 */
	public Exchange(List<Map<String, Object>> leftIn) {
		this.leftIn = leftIn;
	}

	/**
	 * 如果没有从redis中查到任何数据，leftIn==in
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getLeftIn() {
		return leftIn;
	}

	public List<String> getData() {
		return data;
	}

	public List<Map<String, Object>> getCanToRedis() {
		return canToRedis;
	}

	/**
	 * @param pm
	 * @throws Exception
	 */
	public void findFromCache(PojoMeta pm) {
		List<Map<String, Object>> origin = this.leftIn;
		if (origin == null || origin.isEmpty()||RedisPool.defaultRedis()==null) {
			return;
		}
		try {
			boolean[] onlyRedis = new boolean[origin.size()];
			List<String> redisList = new ArrayList<>();
			for (int i = 0; i < onlyRedis.length; i++) {
				Map<String, Object> map = origin.get(i);
				if (pm.isOnlyRedisID(map)) {
					redisList.add(pm.getRedisID(map, false));
					onlyRedis[i] = true;
				}
			}
			List<String> fromRedis = RecordReq.getMultiValue(pm, redisList);
			if (fromRedis == null || fromRedis.isEmpty()) {
				return;
			}

			this.leftIn = new ArrayList<>();
			int k = 0;
			this.data = new ArrayList<>();
			for (int i = 0; i < onlyRedis.length; i++) {
				Map<String, Object> conditon = origin.get(i);

				if (!onlyRedis[i]) {
					this.leftIn.add(conditon);
					continue;
				}

				String value = fromRedis.get(k);
				k++;

				if (value != null && value.length() > 0) {
					this.data.add(value);
					continue;
				}

				if (this.canToRedis == null) {
					this.canToRedis = new ArrayList<>();
				}
				this.canToRedis.add(conditon);
				this.leftIn.add(conditon);

			}
		} catch (Exception e) {
			this.leftIn = origin;
			this.data = null;
			this.canToRedis = null;
			Log.printStack("sumk.sql", e);
		}
	}

}
