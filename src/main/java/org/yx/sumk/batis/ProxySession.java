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

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.yx.db.DBType;
import org.yx.db.conn.ConnectionPool;
import org.yx.exception.SumkException;

public class ProxySession implements SqlSession {

	@Override
	public Connection getConnection() {
		throw new SumkException(8675, "getConnection not support");
	}

	protected SqlSession readSession() {
		ConnectionPool context = ConnectionPool.get();
		return SqlSessionFactory.get(context.getDbName()).session(context.connection(DBType.READ));
	}

	protected SqlSession writeSession() {
		ConnectionPool context = ConnectionPool.get();
		return SqlSessionFactory.get(context.getDbName()).session(context.connection(DBType.WRITE));
	}

	@Override
	public <T> T selectOne(String statement) {
		return this.readSession().selectOne(statement);
	}

	@Override
	public <T> T selectOne(String statement, Object parameter) {
		return this.readSession().selectOne(statement, parameter);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		return this.readSession().selectMap(statement, mapKey);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return this.readSession().selectMap(statement, parameter, mapKey);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return this.readSession().selectMap(statement, parameter, mapKey, rowBounds);
	}

	@Override
	public <E> List<E> selectList(String statement) {
		return this.readSession().selectList(statement);
	}

	@Override
	public <E> List<E> selectList(String statement, Object parameter) {
		return this.readSession().selectList(statement, parameter);
	}

	@Override
	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		return this.readSession().selectList(statement, parameter, rowBounds);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void select(String statement, Object parameter, ResultHandler handler) {
		this.readSession().select(statement, parameter, handler);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void select(String statement, ResultHandler handler) {
		this.readSession().select(statement, handler);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		this.readSession().select(statement, parameter, rowBounds, handler);
	}

	@Override
	public int insert(String statement) {
		return this.writeSession().insert(statement);
	}

	@Override
	public int insert(String statement, Object parameter) {
		return this.writeSession().insert(statement, parameter);
	}

	@Override
	public int update(String statement) {
		return this.writeSession().update(statement);
	}

	@Override
	public int update(String statement, Object parameter) {
		return this.writeSession().update(statement, parameter);
	}

	@Override
	public int delete(String statement) {
		return this.writeSession().delete(statement);
	}

	@Override
	public int delete(String statement, Object parameter) {
		return this.writeSession().delete(statement, parameter);
	}

	@Override
	public void commit() {
	}

	@Override
	public void commit(boolean force) {
	}

	@Override
	public void rollback() {
	}

	@Override
	public void rollback(boolean force) {
	}

	@Override
	public List<BatchResult> flushStatements() {
		return this.writeSession().flushStatements();
	}

	@Override
	public void close() {
	}

	@Override
	public Configuration getConfiguration() {
		return this.writeSession().getConfiguration();
	}

	@Override
	public <T> T getMapper(Class<T> type) {
		return this.writeSession().getMapper(type);
	}

	@Override
	public void clearCache() {
		this.writeSession().clearCache();
	}
}
