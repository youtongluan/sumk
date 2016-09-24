package org.yx.db;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.yx.conf.AppInfo;
import org.yx.conf.DBConfig;
import org.yx.conf.ResUtils;

public class DBFactory {
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
		HashMap<String, Properties> hmap = parse(db);
		WeightedDataSourceRoute read = new WeightedDataSourceRoute();
		WeightedDataSourceRoute write = new WeightedDataSourceRoute();
		Map<DBType, WeightedDataSourceRoute> poolMap = new HashMap<>();
		poolMap.put(DBType.READONLY, read);
		poolMap.put(DBType.WRITE, write);
		for (String key : hmap.keySet()) {
			Properties p = hmap.get(key);
			DBConfig dc = new DBConfig();
			dc.setProperties(p);
			DataSourceWraper ds = dc.createDS(key);
			if (ds.getType().isWritable()) {
				ds.setDefaultReadOnly(false);
				WeightedDS w = new WeightedDS(ds);
				w.setWeight(dc.getWeight() > 0 ? dc.getWeight() : 1);
				write.addServer(w);
				if (dc.getRead_weight() > 0) {
					WeightedDS r = new WeightedDS(ds);
					r.setWeight(dc.getRead_weight());
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

	static HashMap<String, Properties> parse(String db) throws IOException {
		String url = AppInfo.getDBRoot(ResUtils.CLASSPATH + "db/") + db + "/db.ini";
		return ResUtils.parseIni(ResUtils.getResAsInputStream(url));
	}

}
