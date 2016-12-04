package org.yx.db.conn;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.yx.conf.DBConfig;
import org.yx.db.DBType;
import org.yx.sumk.batis.ResUtils;

public class DataSourceFactory {
	/**
	 * 创建数据库连接池，包含了读写的操作
	 * 
	 * @param db
	 *            数据库的名称
	 * @return 返回读和写2个key。如果数据源是可读写的，那么它会同时出现在读写2个池子里，但是读写是共用的
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Map<DBType, WeightedDataSourceRoute> create(String db) throws Exception {
		Map<String, Map<String, String>> hmap = parse(db);
		WeightedDataSourceRoute read = new WeightedDataSourceRoute();
		WeightedDataSourceRoute write = new WeightedDataSourceRoute();
		Map<DBType, WeightedDataSourceRoute> poolMap = new HashMap<>();
		poolMap.put(DBType.READONLY, read);
		poolMap.put(DBType.WRITE, write);
		for (String key : hmap.keySet()) {
			Map<String, String> p = hmap.get(key);
			DBConfig dc = new DBConfig();
			dc.setProperties(p);
			DataSourceWraper ds = dc.createDS(key);
			if (ds.getType().isWritable()) {
				ds.setDefaultReadOnly(false);
				WeightedDS w = new WeightedDS(ds);
				w.setWeight(dc.getWeight() > 0 ? dc.getWeight() : 1);
				write.addServer(w);
				if (ds.getType() == DBType.ANY) {
					WeightedDS r = new WeightedDS(ds);
					r.setWeight(dc.getRead_weight() > 0 ? dc.getRead_weight() : 1);
					read.addServer(r);
				}
			} else if (ds.getType().isReadable()) {
				ds.setDefaultReadOnly(true);
				WeightedDS r = new WeightedDS(ds);
				int w = dc.getRead_weight() > 0 ? dc.getRead_weight() : dc.getWeight();
				r.setWeight(w > 0 ? w : 1);
				read.addServer(r);
			}
		}
		return poolMap;
	}

	static Map<String, Map<String, String>> parse(String db) throws Exception {
		return ResUtils.parseIni(ResUtils.dbResource(db).dbIni());
	}

}
