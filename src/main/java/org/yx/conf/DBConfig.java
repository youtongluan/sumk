package org.yx.conf;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.yx.db.conn.DataSourceWraper;
import org.yx.log.Log;
import org.yx.util.Assert;
import org.yx.util.SimpleBeanUtil;

public class DBConfig {

	String type = "";
	int weight = 0;
	int read_weight = 0;
	Map<String, String> properties;

	public DBConfig() {
		properties = new HashMap<>();
		properties.put("driverClassName", "com.mysql.jdbc.Driver");
		properties.put("validationQuery", "select 1");
		properties.put("maxTotal", "30");
		properties.put("minIdle", "2");
		properties.put("maxIdle", "10");
		properties.put("maxWaitMillis", "10000");
		properties.put("testOnBorrow", "false");
		properties.put("testOnReturn", "false");
		properties.put("testWhileIdle", "true");
		properties.put("removeAbandonedOnBorrow", "false");
		properties.put("removeAbandonedOnMaintenance", "true");
		properties.put("removeAbandonedTimeout", "30");
		properties.put("logAbandoned", "true");
		properties.put("timeBetweenEvictionRunsMillis", "30000");
		properties.put("softMinEvictableIdleTimeMillis", "60000");

		properties.put("logExpiredConnections", "false");
		properties.put("poolPreparedStatements", "false");
		properties.put("defaultAutoCommit", "false");

	}

	public String getProperty(String name) {
		return this.properties.get(name);
	}

	public void setProperties(Map<String, String> p) throws IllegalAccessException, InvocationTargetException {
		Set<String> set = p.keySet();
		for (String key : set) {
			String v = p.get(key);
			if (v == null) {
				Log.get("sumk.db.config").debug("{} key的值是null，被忽略掉", key);
				continue;
			}
			switch (key) {
			case "type":
				this.type = v.toLowerCase();
				break;
			case "weight":
				this.weight = Integer.parseInt(v);
				break;
			case "read_weight":
				this.read_weight = Integer.parseInt(v);
				break;
			default:
				properties.put(key, v);
				break;
			}

		}
	}

	/**
	 * 
	 * @param name
	 * @return 返回读和写连个路由。如果一个数据源是可读写的，那么它同时存在两个路由中，但对象实体只有一个
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public DataSourceWraper createDS(String name) throws Exception {
		Assert.isTrue(this.valid(), "url,username,password,type should not be null");
		Assert.isTrue(type.matches("^(wr|read|write)$"), "db type should be one of(wr,read,write)");
		DataSourceWraper ds = new DataSourceWraper(name, type);
		SimpleBeanUtil.copyProperties(ds, this.properties);
		return ds;
	}

	public boolean valid() {
		return this.type != null && properties.get("url") != null && properties.get("username") != null
				&& properties.get("password") != null;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getRead_weight() {
		return read_weight;
	}

	public void setRead_weight(int read_weight) {
		this.read_weight = read_weight;
	}

	@Override
	public String toString() {
		return "DBConfig [type=" + type + ", weight=" + weight + ", read_weight=" + read_weight + ", properties="
				+ properties + "]";
	}

}
