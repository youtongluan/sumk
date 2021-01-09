/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.sumk.batis;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.managed.ManagedTransaction;
import org.yx.bean.IOC;
import org.yx.conf.AppInfo;
import org.yx.conf.LocalMultiResourceLoaderSupplier;
import org.yx.conf.MultiResourceLoader;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.util.StringUtil;

public class SqlSessionFactory {

	private static final ConcurrentMap<String, SqlSessionFactory> factoryMap = new ConcurrentHashMap<>();

	private Configuration configuration;
	private String db;

	private SqlSessionFactory() {

	}

	private static SqlSessionFactory create(String dbName) throws Exception {
		SqlSessionFactory sessionFactory = new SqlSessionFactory();
		sessionFactory.db = dbName;
		List<ConfigurationFactory> confFactorys = IOC.getBeans(ConfigurationFactory.class);
		if (confFactorys != null && confFactorys.size() > 0) {
			for (ConfigurationFactory f : confFactorys) {
				Configuration conf = f.create(dbName);
				if (conf != null) {
					sessionFactory.configuration = conf;
					return sessionFactory.sqlParse();
				}
			}
		}
		Configuration conf = new Configuration();
		conf.setDefaultExecutorType(ExecutorType.SIMPLE);
		conf.setCacheEnabled(false);
		sessionFactory.configuration = conf;
		return sessionFactory.sqlParse();
	}

	private static Supplier<MultiResourceLoader> resourceLoader = new LocalMultiResourceLoaderSupplier(
			AppInfo.get("sumk.db.mybatis.path", AppInfo.CLASSPATH_URL_PREFIX + "batis"));

	public static void setResourceLoader(Supplier<MultiResourceLoader> resourceLoader) {
		SqlSessionFactory.resourceLoader = Objects.requireNonNull(resourceLoader);
	}

	public static Supplier<MultiResourceLoader> getResourceLoader() {
		return resourceLoader;
	}

	public static SqlSessionFactory get(String dbName) {
		SqlSessionFactory factory = factoryMap.get(dbName);
		if (factory != null) {
			return factory;
		}
		try {
			factory = factoryMap.computeIfAbsent(dbName, name -> {
				Logs.db().info("mybatis创建{}的SqlSessionFactory", name);
				try {
					return SqlSessionFactory.create(name);
				} catch (Exception e) {
					Logs.db().error("创建" + name + "的SqlSessionFactory失败", e);
					return null;
				}
			});
			if (factory != null) {
				return factory;
			}
			return factoryMap.get(dbName);
		} catch (Exception e) {
			Log.printStack("sumk.sql.error", e);
			throw new SumkException(100234325, dbName + " create SqlSessionFactory failed");
		}
	}

	void destroy() {
	}

	public static void reload(String dbName) throws Exception {
		dbName = StringUtil.requireNotEmpty(dbName).trim();
		SqlSessionFactory factory = factoryMap.get(dbName);
		if (factory == null) {
			return;
		}
		factory = SqlSessionFactory.create(dbName);
		SqlSessionFactory old = factoryMap.put(dbName, factory);
		old.destroy();
	}

	public SqlSession openSession(Connection conn) {

		Transaction transaction = new ManagedTransaction(conn, false);
		Executor executor = configuration.newExecutor(transaction);
		return new DefaultSqlSession(configuration, executor);
	}

	SqlSessionFactory sqlParse() throws Exception {
		Map<String, byte[]> sqls = resourceLoader.get().openResources(db);
		Set<Map.Entry<String, byte[]>> entries = sqls.entrySet();
		for (Map.Entry<String, byte[]> entry : entries) {
			byte[] bs = entry.getValue();
			XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(new ByteArrayInputStream(bs), configuration,
					entry.getKey(), configuration.getSqlFragments());
			xmlMapperBuilder.parse();
		}
		return this;
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

}
