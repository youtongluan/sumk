package org.yx.sumk.batis;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.managed.ManagedTransaction;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.util.Assert;

/**
 * 负责创建SqlSession，但不负责SqlSession的事务等操作。<BR>
 * reload用于重新加载。<BR>
 * 在无法确认是只读的情况下，就使用写库.
 */
public class SqlSessionFactory {

	static Log logger = Log.get(SqlSessionFactory.class);

	private static Configuration configuration;
	private static Map<String, SqlSessionFactory> factoryMap = new ConcurrentHashMap<>();

	private String db;

	private SqlSessionFactory(String dbName) {
		this.db = dbName;
	}

	public static SqlSessionFactory get(String dbName) {
		try {
			Assert.hasText(dbName, "db name can not be empty");
			dbName = dbName.trim();
			SqlSessionFactory factory = factoryMap.get(dbName);
			if (factory != null) {
				return factory;
			}
			synchronized (SqlSessionFactory.class) {
				factory = factoryMap.get(dbName);
				if (factory != null) {
					return factory;
				}
				factory = new SqlSessionFactory(dbName);
				factory.init();
				factoryMap.put(dbName, factory);
			}
			return factory;
		} catch (Exception e) {
			Log.printStack(e);
			SumkException.throwException(100234325, "create factory failed");
			return null;
		}
	}

	void destroy() {

	}

	public static void reload(String dbName) throws Exception {
		Assert.hasText(dbName, "db name can not be empty");
		dbName = dbName.trim();
		SqlSessionFactory factory = factoryMap.get(dbName);
		if (factory == null) {
			return;
		}
		factory = new SqlSessionFactory(dbName);
		factory.init();
		SqlSessionFactory old = factoryMap.put(dbName, factory);
		old.destroy();
	}

	public SqlSession session(Connection conn) {

		Transaction transaction = new ManagedTransaction(conn, false);
		SimpleExecutor excutor = new SimpleExecutor(configuration, transaction);
		return new DefaultSqlSession(configuration, excutor);
	}

	void init() throws Exception {
		configuration = new Configuration();
		Map<String, InputStream> sqls = MybatisSqlXmlUtils.openInputs(db);
		Set<Map.Entry<String, InputStream>> entries = sqls.entrySet();
		for (Map.Entry<String, InputStream> entry : entries) {
			XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(entry.getValue(), configuration, entry.getKey(),
					configuration.getSqlFragments());
			xmlMapperBuilder.parse();
		}
	}

}
