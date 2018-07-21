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
package org.yx.redis;

import org.yx.exception.SumkException;
import org.yx.log.Log;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

public class RedisImpl extends Redis {

	public RedisImpl(JedisPoolConfig config, RedisParamter p) {
		super(config, p);
	}

	@Override
	public java.lang.String get(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.get(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("get - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String type(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.type(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("type - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long append(java.lang.String key, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.append(key, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("append - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> keys(java.lang.String pattern) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.keys(pattern);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("keys - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String set(java.lang.String key, java.lang.String value, java.lang.String nxxx) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.set(key, value, nxxx);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("set - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String set(java.lang.String key, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.set(key, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("set - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String set(java.lang.String key, java.lang.String value, java.lang.String nxxx,
			java.lang.String expx, long time) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.set(key, value, nxxx, expx, time);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("set - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long exists(java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.exists(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("exists - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean exists(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.exists(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("exists - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String rename(java.lang.String oldkey, java.lang.String newkey) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.rename(oldkey, newkey);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("rename - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> sort(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sort(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sort - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long sort(java.lang.String key, java.lang.String dstkey) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sort(key, dstkey);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sort - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long sort(java.lang.String key, redis.clients.jedis.SortingParams sortingParameters,
			java.lang.String dstkey) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sort(key, sortingParameters, dstkey);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sort - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> sort(java.lang.String key,
			redis.clients.jedis.SortingParams sortingParameters) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sort(key, sortingParameters);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sort - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<java.lang.String> scan(java.lang.String cursor,
			redis.clients.jedis.ScanParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.scan(cursor, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("scan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<java.lang.String> scan(java.lang.String cursor) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.scan(cursor);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("scan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	@Deprecated
	public redis.clients.jedis.ScanResult<java.lang.String> scan(int cursor) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.scan(cursor);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("scan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	@Deprecated
	public java.util.List<java.lang.String> brpop(java.lang.String arg) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.brpop(arg);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("brpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> brpop(int timeout, java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.brpop(timeout, key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("brpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> brpop(java.lang.String... args) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.brpop(args);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("brpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> brpop(int timeout, java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.brpop(timeout, keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("brpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long hset(java.lang.String key, java.lang.String field, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hset(key, field, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hset - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String setex(java.lang.String key, int seconds, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.setex(key, seconds, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("setex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String hget(java.lang.String key, java.lang.String field) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hget(key, field);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hget - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long persist(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.persist(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("persist - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long pexpire(java.lang.String key, long milliseconds) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pexpire(key, milliseconds);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pexpire - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long expire(java.lang.String key, int seconds) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.expire(key, seconds);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("expire - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long expireAt(java.lang.String key, long unixTime) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.expireAt(key, unixTime);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("expireAt - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long pexpireAt(java.lang.String key, long millisecondsTimestamp) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pexpireAt(key, millisecondsTimestamp);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pexpireAt - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean setbit(java.lang.String key, long offset, boolean value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.setbit(key, offset, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("setbit - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean setbit(java.lang.String key, long offset, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.setbit(key, offset, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("setbit - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long ttl(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.ttl(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("ttl - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean getbit(java.lang.String key, long offset) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.getbit(key, offset);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("getbit - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long setrange(java.lang.String key, long offset, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.setrange(key, offset, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("setrange - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String getrange(java.lang.String key, long startOffset, long endOffset) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.getrange(key, startOffset, endOffset);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("getrange - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String getSet(java.lang.String key, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.getSet(key, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("getSet - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long decrBy(java.lang.String key, long integer) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.decrBy(key, integer);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("decrBy - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long setnx(java.lang.String key, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.setnx(key, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("setnx - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long incrBy(java.lang.String key, long integer) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.incrBy(key, integer);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("incrBy - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long decr(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.decr(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("decr - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long incr(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.incr(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("incr - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double incrByFloat(java.lang.String key, double value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.incrByFloat(key, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("incrByFloat - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long hsetnx(java.lang.String key, java.lang.String field, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hsetnx(key, field, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hsetnx - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String substr(java.lang.String key, int start, int end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.substr(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("substr - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> hmget(java.lang.String key, java.lang.String... fields) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hmget(key, fields);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hmget - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String hmset(java.lang.String key, java.util.Map<java.lang.String, java.lang.String> hash) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hmset(key, hash);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hmset - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long hincrBy(java.lang.String key, java.lang.String field, long value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hincrBy(key, field, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hincrBy - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean hexists(java.lang.String key, java.lang.String field) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hexists(key, field);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hexists - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double hincrByFloat(java.lang.String key, java.lang.String field, double value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hincrByFloat(key, field, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hincrByFloat - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long hlen(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hlen(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hlen - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long hdel(java.lang.String key, java.lang.String... fields) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hdel(key, fields);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hdel - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> hkeys(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hkeys(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hkeys - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Map<java.lang.String, java.lang.String> hgetAll(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hgetAll(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hgetAll - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> hvals(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hvals(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hvals - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long rpush(java.lang.String key, java.lang.String... strings) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.rpush(key, strings);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("rpush - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long llen(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.llen(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("llen - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long lpush(java.lang.String key, java.lang.String... strings) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lpush(key, strings);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lpush - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> lrange(java.lang.String key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lrange(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lrange - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String lindex(java.lang.String key, long index) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lindex(key, index);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lindex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String ltrim(java.lang.String key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.ltrim(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("ltrim - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String lset(java.lang.String key, long index, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lset(key, index, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lset - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long lrem(java.lang.String key, long count, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lrem(key, count, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lrem - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String lpop(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lpop(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long sadd(java.lang.String key, java.lang.String... members) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sadd(key, members);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String rpop(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.rpop(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("rpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> smembers(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.smembers(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("smembers - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long srem(java.lang.String key, java.lang.String... members) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.srem(key, members);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("srem - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long scard(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.scard(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("scard - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String spop(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.spop(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("spop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> spop(java.lang.String key, long count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.spop(key, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("spop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean sismember(java.lang.String key, java.lang.String member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sismember(key, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sismember - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long strlen(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.strlen(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("strlen - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> srandmember(java.lang.String key, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.srandmember(key, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("srandmember - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String srandmember(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.srandmember(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("srandmember - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zadd(java.lang.String key, double score, java.lang.String member,
			redis.clients.jedis.params.sortedset.ZAddParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zadd(key, score, member, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zadd(java.lang.String key, java.util.Map<java.lang.String, java.lang.Double> scoreMembers) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zadd(key, scoreMembers);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zadd(java.lang.String key, double score, java.lang.String member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zadd(key, score, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zadd(java.lang.String key, java.util.Map<java.lang.String, java.lang.Double> scoreMembers,
			redis.clients.jedis.params.sortedset.ZAddParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zadd(key, scoreMembers, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrange(java.lang.String key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrange(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrange - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zrem(java.lang.String key, java.lang.String... members) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrem(key, members);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrem - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zrank(java.lang.String key, java.lang.String member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrank(key, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrank - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double zincrby(java.lang.String key, double score, java.lang.String member,
			redis.clients.jedis.params.sortedset.ZIncrByParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zincrby(key, score, member, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zincrby - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double zincrby(java.lang.String key, double score, java.lang.String member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zincrby(key, score, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zincrby - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrevrange(java.lang.String key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrange(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrange - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zrevrank(java.lang.String key, java.lang.String member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrank(key, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrank - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeWithScores(java.lang.String key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeWithScores(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zcard(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zcard(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zcard - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double zscore(java.lang.String key, java.lang.String member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zscore(key, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zscore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zcount(java.lang.String key, double min, double max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zcount(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zcount(java.lang.String key, java.lang.String min, java.lang.String max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zcount(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key, java.lang.String max,
			java.lang.String min) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScore(key, max, min);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key, double max, double min) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScore(key, max, min);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key, double max, double min, int offset,
			int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScore(key, max, min, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key, java.lang.String max,
			java.lang.String min, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScore(key, max, min, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key, double min, double max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScore(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key, java.lang.String min,
			java.lang.String max, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScore(key, min, max, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key, double min, double max, int offset,
			int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScore(key, min, max, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key, java.lang.String min,
			java.lang.String max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScore(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zremrangeByRank(java.lang.String key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zremrangeByRank(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zremrangeByRank - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zremrangeByScore(java.lang.String key, double start, double end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zremrangeByScore(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zremrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zremrangeByScore(java.lang.String key, java.lang.String start, java.lang.String end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zremrangeByScore(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zremrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zlexcount(java.lang.String key, java.lang.String min, java.lang.String max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zlexcount(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zlexcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrevrangeByLex(java.lang.String key, java.lang.String max,
			java.lang.String min) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByLex(key, max, min);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByLex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrevrangeByLex(java.lang.String key, java.lang.String max,
			java.lang.String min, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByLex(key, max, min, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByLex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrangeByLex(java.lang.String key, java.lang.String min, java.lang.String max,
			int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByLex(key, min, max, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByLex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> zrangeByLex(java.lang.String key, java.lang.String min,
			java.lang.String max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByLex(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByLex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long linsert(java.lang.String key, redis.clients.jedis.BinaryClient.LIST_POSITION where,
			java.lang.String pivot, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.linsert(key, where, pivot, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("linsert - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zremrangeByLex(java.lang.String key, java.lang.String min, java.lang.String max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zremrangeByLex(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zremrangeByLex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long lpushx(java.lang.String key, java.lang.String... string) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lpushx(key, string);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lpushx - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	@Deprecated
	public java.util.List<java.lang.String> blpop(java.lang.String arg) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.blpop(arg);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("blpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> blpop(int timeout, java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.blpop(timeout, key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("blpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> blpop(int timeout, java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.blpop(timeout, keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("blpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> blpop(java.lang.String... args) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.blpop(args);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("blpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long rpushx(java.lang.String key, java.lang.String... string) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.rpushx(key, string);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("rpushx - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String echo(java.lang.String string) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.echo(string);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("echo - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long del(java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.del(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("del - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long del(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.del(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("del - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long bitcount(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.bitcount(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("bitcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long bitcount(java.lang.String key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.bitcount(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("bitcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long move(java.lang.String key, int dbIndex) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.move(key, dbIndex);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("move - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long pfadd(java.lang.String key, java.lang.String... elements) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pfadd(key, elements);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pfadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long geoadd(java.lang.String key,
			java.util.Map<java.lang.String, redis.clients.jedis.GeoCoordinate> memberCoordinateMap) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.geoadd(key, memberCoordinateMap);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("geoadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long geoadd(java.lang.String key, double longitude, double latitude, java.lang.String member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.geoadd(key, longitude, latitude, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("geoadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public long pfcount(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pfcount(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pfcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public long pfcount(java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pfcount(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pfcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> geohash(java.lang.String key, java.lang.String... members) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.geohash(key, members);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("geohash - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double geodist(java.lang.String key, java.lang.String member1, java.lang.String member2,
			redis.clients.jedis.GeoUnit unit) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.geodist(key, member1, member2, unit);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("geodist - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double geodist(java.lang.String key, java.lang.String member1, java.lang.String member2) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.geodist(key, member1, member2);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("geodist - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(java.lang.String key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.georadius(key, longitude, latitude, radius, unit, param);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("georadius - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(java.lang.String key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.georadius(key, longitude, latitude, radius, unit);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("georadius - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoCoordinate> geopos(java.lang.String key, java.lang.String... members) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.geopos(key, members);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("geopos - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<java.lang.String> sscan(java.lang.String key, java.lang.String cursor,
			redis.clients.jedis.ScanParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sscan(key, cursor, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	@Deprecated
	public redis.clients.jedis.ScanResult<java.lang.String> sscan(java.lang.String key, int cursor) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sscan(key, cursor);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<java.lang.String> sscan(java.lang.String key, java.lang.String cursor) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sscan(key, cursor);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<java.lang.String, java.lang.String>> hscan(
			java.lang.String key, java.lang.String cursor) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hscan(key, cursor);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	@Deprecated
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<java.lang.String, java.lang.String>> hscan(
			java.lang.String key, int cursor) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hscan(key, cursor);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<java.lang.String, java.lang.String>> hscan(
			java.lang.String key, java.lang.String cursor, redis.clients.jedis.ScanParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hscan(key, cursor, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.Long> bitfield(java.lang.String key, java.lang.String... arguments) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.bitfield(key, arguments);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("bitfield - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	@Deprecated
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(java.lang.String key, int cursor) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zscan(key, cursor);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(java.lang.String key,
			java.lang.String cursor) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zscan(key, cursor);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(java.lang.String key,
			java.lang.String cursor, redis.clients.jedis.ScanParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zscan(key, cursor, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.String> mget(java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.mget(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("mget - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long msetnx(java.lang.String... keysvalues) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.msetnx(keysvalues);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("msetnx - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String mset(java.lang.String... keysvalues) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.mset(keysvalues);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("mset - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String rpoplpush(java.lang.String srckey, java.lang.String dstkey) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.rpoplpush(srckey, dstkey);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("rpoplpush - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long renamenx(java.lang.String oldkey, java.lang.String newkey) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.renamenx(oldkey, newkey);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("renamenx - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> sdiff(java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sdiff(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sdiff - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long sdiffstore(java.lang.String dstkey, java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sdiffstore(dstkey, keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sdiffstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long sinterstore(java.lang.String dstkey, java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sinterstore(dstkey, keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sinterstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> sinter(java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sinter(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sinter - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<java.lang.String> sunion(java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sunion(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sunion - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long smove(java.lang.String srckey, java.lang.String dstkey, java.lang.String member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.smove(srckey, dstkey, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("smove - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String watch(java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.watch(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("watch - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long sunionstore(java.lang.String dstkey, java.lang.String... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sunionstore(dstkey, keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sunionstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zinterstore(java.lang.String dstkey, java.lang.String... sets) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zinterstore(dstkey, sets);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zinterstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zinterstore(java.lang.String dstkey, redis.clients.jedis.ZParams params,
			java.lang.String... sets) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zinterstore(dstkey, params, sets);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zinterstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zunionstore(java.lang.String dstkey, java.lang.String... sets) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zunionstore(dstkey, sets);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zunionstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zunionstore(java.lang.String dstkey, redis.clients.jedis.ZParams params,
			java.lang.String... sets) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zunionstore(dstkey, params, sets);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zunionstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String brpoplpush(java.lang.String source, java.lang.String destination, int timeout) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.brpoplpush(source, destination, timeout);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("brpoplpush - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public void psubscribe(redis.clients.jedis.JedisPubSub jedisPubSub, java.lang.String... patterns) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				jedis.psubscribe(jedisPubSub, patterns);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("psubscribe - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long publish(java.lang.String channel, java.lang.String message) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.publish(channel, message);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("publish - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long bitop(redis.clients.jedis.BitOP op, java.lang.String destKey, java.lang.String... srcKeys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.bitop(op, destKey, srcKeys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("bitop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String pfmerge(java.lang.String destkey, java.lang.String... sourcekeys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pfmerge(destkey, sourcekeys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pfmerge - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean scriptExists(java.lang.String sha1) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.scriptExists(sha1);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("scriptExists - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.Boolean> scriptExists(java.lang.String... sha1) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.scriptExists(sha1);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("scriptExists - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object evalsha(java.lang.String script) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.evalsha(script);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("evalsha - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object evalsha(java.lang.String sha1, int keyCount, java.lang.String... params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.evalsha(sha1, keyCount, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("evalsha - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object evalsha(java.lang.String sha1, java.util.List<java.lang.String> keys,
			java.util.List<java.lang.String> args) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.evalsha(sha1, keys, args);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("evalsha - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String scriptLoad(java.lang.String script) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.scriptLoad(script);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("scriptLoad - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long pttl(java.lang.String key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pttl(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pttl - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String psetex(java.lang.String key, long milliseconds, java.lang.String value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.psetex(key, milliseconds, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("psetex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long bitpos(java.lang.String key, boolean value, redis.clients.jedis.BitPosParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.bitpos(key, value, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("bitpos - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long bitpos(java.lang.String key, boolean value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.bitpos(key, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("bitpos - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String randomKey() {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.randomKey();
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("randomKey - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object eval(java.lang.String script) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.eval(script);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("eval - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object eval(java.lang.String script, int keyCount, java.lang.String... params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.eval(script, keyCount, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("eval - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object eval(java.lang.String script, java.util.List<java.lang.String> keys,
			java.util.List<java.lang.String> args) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.eval(script, keys, args);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("eval - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeWithScores(java.lang.String key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeWithScores(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key, java.lang.String min,
			java.lang.String max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScoreWithScores(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key, java.lang.String min,
			java.lang.String max, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key, double min,
			double max, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key, double min,
			double max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScoreWithScores(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key, double max,
			double min, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key, double max,
			double min) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScoreWithScores(key, max, min);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key,
			java.lang.String max, java.lang.String min) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScoreWithScores(key, max, min);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key,
			java.lang.String max, java.lang.String min, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(java.lang.String key,
			java.lang.String member, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.georadiusByMember(key, member, radius, unit, param);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("georadiusByMember - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(java.lang.String key,
			java.lang.String member, double radius, redis.clients.jedis.GeoUnit unit) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.georadiusByMember(key, member, radius, unit);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("georadiusByMember - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public void subscribe(redis.clients.jedis.JedisPubSub jedisPubSub, java.lang.String... channels) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				jedis.subscribe(jedisPubSub, channels);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("subscribe - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] get(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.get(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("get - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String type(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.type(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("type - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long append(byte[] key, byte[] value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.append(key, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("append - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> keys(byte[] pattern) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.keys(pattern);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("keys - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String set(byte[] key, byte[] value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.set(key, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("set - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String set(byte[] key, byte[] value, byte[] nxxx) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.set(key, value, nxxx);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("set - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.set(key, value, nxxx, expx, time);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("set - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long exists(byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.exists(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("exists - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean exists(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.exists(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("exists - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String rename(byte[] oldkey, byte[] newkey) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.rename(oldkey, newkey);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("rename - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long sort(byte[] key, byte[] dstkey) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sort(key, dstkey);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sort - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long sort(byte[] key, redis.clients.jedis.SortingParams sortingParameters, byte[] dstkey) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sort(key, sortingParameters, dstkey);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sort - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> sort(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sort(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sort - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> sort(byte[] key, redis.clients.jedis.SortingParams sortingParameters) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sort(key, sortingParameters);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sort - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	@Deprecated
	public java.util.List<byte[]> brpop(byte[] arg) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.brpop(arg);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("brpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> brpop(int timeout, byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.brpop(timeout, keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("brpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> brpop(byte[]... args) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.brpop(args);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("brpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long hset(byte[] key, byte[] field, byte[] value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hset(key, field, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hset - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String setex(byte[] key, int seconds, byte[] value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.setex(key, seconds, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("setex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] hget(byte[] key, byte[] field) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hget(key, field);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hget - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long persist(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.persist(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("persist - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long pexpire(byte[] key, long milliseconds) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pexpire(key, milliseconds);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pexpire - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long expire(byte[] key, int seconds) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.expire(key, seconds);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("expire - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long expireAt(byte[] key, long unixTime) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.expireAt(key, unixTime);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("expireAt - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long pexpireAt(byte[] key, long millisecondsTimestamp) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pexpireAt(key, millisecondsTimestamp);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pexpireAt - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean setbit(byte[] key, long offset, byte[] value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.setbit(key, offset, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("setbit - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean setbit(byte[] key, long offset, boolean value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.setbit(key, offset, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("setbit - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long ttl(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.ttl(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("ttl - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean getbit(byte[] key, long offset) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.getbit(key, offset);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("getbit - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long setrange(byte[] key, long offset, byte[] value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.setrange(key, offset, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("setrange - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] getrange(byte[] key, long startOffset, long endOffset) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.getrange(key, startOffset, endOffset);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("getrange - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] getSet(byte[] key, byte[] value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.getSet(key, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("getSet - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long decrBy(byte[] key, long integer) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.decrBy(key, integer);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("decrBy - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long setnx(byte[] key, byte[] value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.setnx(key, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("setnx - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long incrBy(byte[] key, long integer) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.incrBy(key, integer);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("incrBy - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long decr(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.decr(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("decr - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long incr(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.incr(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("incr - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double incrByFloat(byte[] key, double integer) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.incrByFloat(key, integer);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("incrByFloat - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long hsetnx(byte[] key, byte[] field, byte[] value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hsetnx(key, field, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hsetnx - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] substr(byte[] key, int start, int end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.substr(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("substr - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> hmget(byte[] key, byte[]... fields) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hmget(key, fields);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hmget - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String hmset(byte[] key, java.util.Map<byte[], byte[]> hash) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hmset(key, hash);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hmset - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long hincrBy(byte[] key, byte[] field, long value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hincrBy(key, field, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hincrBy - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean hexists(byte[] key, byte[] field) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hexists(key, field);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hexists - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double hincrByFloat(byte[] key, byte[] field, double value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hincrByFloat(key, field, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hincrByFloat - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long hlen(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hlen(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hlen - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long hdel(byte[] key, byte[]... fields) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hdel(key, fields);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hdel - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> hkeys(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hkeys(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hkeys - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Map<byte[], byte[]> hgetAll(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hgetAll(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hgetAll - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> hvals(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hvals(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hvals - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long rpush(byte[] key, byte[]... strings) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.rpush(key, strings);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("rpush - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long llen(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.llen(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("llen - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long lpush(byte[] key, byte[]... strings) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lpush(key, strings);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lpush - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> lrange(byte[] key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lrange(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lrange - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] lindex(byte[] key, long index) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lindex(key, index);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lindex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String ltrim(byte[] key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.ltrim(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("ltrim - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String lset(byte[] key, long index, byte[] value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lset(key, index, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lset - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long lrem(byte[] key, long count, byte[] value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lrem(key, count, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lrem - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] lpop(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lpop(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long sadd(byte[] key, byte[]... members) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sadd(key, members);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] rpop(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.rpop(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("rpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> smembers(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.smembers(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("smembers - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long srem(byte[] key, byte[]... member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.srem(key, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("srem - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long scard(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.scard(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("scard - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] spop(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.spop(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("spop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> spop(byte[] key, long count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.spop(key, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("spop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Boolean sismember(byte[] key, byte[] member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sismember(key, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sismember - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long strlen(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.strlen(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("strlen - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> srandmember(byte[] key, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.srandmember(key, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("srandmember - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] srandmember(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.srandmember(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("srandmember - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zadd(byte[] key, double score, byte[] member,
			redis.clients.jedis.params.sortedset.ZAddParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zadd(key, score, member, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zadd(byte[] key, java.util.Map<byte[], java.lang.Double> scoreMembers,
			redis.clients.jedis.params.sortedset.ZAddParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zadd(key, scoreMembers, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zadd(byte[] key, java.util.Map<byte[], java.lang.Double> scoreMembers) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zadd(key, scoreMembers);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zadd(byte[] key, double score, byte[] member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zadd(key, score, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrange(byte[] key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrange(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrange - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zrem(byte[] key, byte[]... members) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrem(key, members);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrem - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zrank(byte[] key, byte[] member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrank(key, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrank - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double zincrby(byte[] key, double score, byte[] member,
			redis.clients.jedis.params.sortedset.ZIncrByParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zincrby(key, score, member, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zincrby - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double zincrby(byte[] key, double score, byte[] member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zincrby(key, score, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zincrby - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrevrange(byte[] key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrange(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrange - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zrevrank(byte[] key, byte[] member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrank(key, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrank - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeWithScores(byte[] key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeWithScores(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zcard(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zcard(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zcard - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double zscore(byte[] key, byte[] member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zscore(key, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zscore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zcount(byte[] key, byte[] min, byte[] max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zcount(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zcount(byte[] key, double min, double max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zcount(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScore(key, max, min);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScore(key, max, min, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScore(key, max, min);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScore(key, max, min, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScore(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScore(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScore(key, min, max, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScore(key, min, max, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zremrangeByRank(byte[] key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zremrangeByRank(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zremrangeByRank - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zremrangeByScore(byte[] key, double start, double end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zremrangeByScore(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zremrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zremrangeByScore(byte[] key, byte[] start, byte[] end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zremrangeByScore(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zremrangeByScore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zlexcount(byte[] key, byte[] min, byte[] max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zlexcount(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zlexcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByLex(key, max, min);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByLex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByLex(key, max, min, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByLex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByLex(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByLex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByLex(key, min, max, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByLex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long linsert(byte[] key, redis.clients.jedis.BinaryClient.LIST_POSITION where, byte[] pivot,
			byte[] value) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.linsert(key, where, pivot, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("linsert - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zremrangeByLex(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zremrangeByLex - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long lpushx(byte[] key, byte[]... string) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.lpushx(key, string);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("lpushx - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	@Deprecated
	public java.util.List<byte[]> blpop(byte[] arg) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.blpop(arg);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("blpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> blpop(byte[]... args) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.blpop(args);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("blpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> blpop(int timeout, byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.blpop(timeout, keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("blpop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long rpushx(byte[] key, byte[]... string) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.rpushx(key, string);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("rpushx - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] echo(byte[] string) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.echo(string);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("echo - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long del(byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.del(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("del - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long del(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.del(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("del - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long bitcount(byte[] key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.bitcount(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("bitcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long bitcount(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.bitcount(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("bitcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long move(byte[] key, int dbIndex) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.move(key, dbIndex);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("move - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long pfadd(byte[] key, byte[]... elements) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pfadd(key, elements);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pfadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long geoadd(byte[] key,
			java.util.Map<byte[], redis.clients.jedis.GeoCoordinate> memberCoordinateMap) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.geoadd(key, memberCoordinateMap);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("geoadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.geoadd(key, longitude, latitude, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("geoadd - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long pfcount(byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pfcount(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pfcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public long pfcount(byte[] key) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pfcount(key);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pfcount - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> geohash(byte[] key, byte[]... members) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.geohash(key, members);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("geohash - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double geodist(byte[] key, byte[] member1, byte[] member2) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.geodist(key, member1, member2);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("geodist - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Double geodist(byte[] key, byte[] member1, byte[] member2, redis.clients.jedis.GeoUnit unit) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.geodist(key, member1, member2, unit);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("geodist - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(byte[] key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.georadius(key, longitude, latitude, radius, unit, param);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("georadius - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(byte[] key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.georadius(key, longitude, latitude, radius, unit);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("georadius - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoCoordinate> geopos(byte[] key, byte[]... members) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.geopos(key, members);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("geopos - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<byte[]> sscan(byte[] key, byte[] cursor,
			redis.clients.jedis.ScanParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sscan(key, cursor, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sscan(key, cursor);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor,
			redis.clients.jedis.ScanParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hscan(key, cursor, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.hscan(key, cursor);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("hscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> bitfield(byte[] key, byte[]... arguments) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.bitfield(key, arguments);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("bitfield - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(byte[] key, byte[] cursor,
			redis.clients.jedis.ScanParams params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zscan(key, cursor, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(byte[] key, byte[] cursor) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zscan(key, cursor);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zscan - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<byte[]> mget(byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.mget(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("mget - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long msetnx(byte[]... keysvalues) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.msetnx(keysvalues);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("msetnx - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String mset(byte[]... keysvalues) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.mset(keysvalues);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("mset - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.rpoplpush(srckey, dstkey);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("rpoplpush - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long renamenx(byte[] oldkey, byte[] newkey) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.renamenx(oldkey, newkey);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("renamenx - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> sdiff(byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sdiff(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sdiff - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long sdiffstore(byte[] dstkey, byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sdiffstore(dstkey, keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sdiffstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long sinterstore(byte[] dstkey, byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sinterstore(dstkey, keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sinterstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> sinter(byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sinter(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sinter - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<byte[]> sunion(byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sunion(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sunion - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.smove(srckey, dstkey, member);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("smove - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String watch(byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.watch(keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("watch - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long sunionstore(byte[] dstkey, byte[]... keys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sunionstore(dstkey, keys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("sunionstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zinterstore(byte[] dstkey, redis.clients.jedis.ZParams params, byte[]... sets) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zinterstore(dstkey, params, sets);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zinterstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zinterstore(byte[] dstkey, byte[]... sets) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zinterstore(dstkey, sets);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zinterstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String unwatch() {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.unwatch();
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("unwatch - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zunionstore(byte[] dstkey, byte[]... sets) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zunionstore(dstkey, sets);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zunionstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long zunionstore(byte[] dstkey, redis.clients.jedis.ZParams params, byte[]... sets) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zunionstore(dstkey, params, sets);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zunionstore - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.brpoplpush(source, destination, timeout);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("brpoplpush - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public void psubscribe(redis.clients.jedis.BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				jedis.psubscribe(jedisPubSub, patterns);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("psubscribe - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long publish(byte[] channel, byte[] message) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.publish(channel, message);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("publish - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Long bitop(redis.clients.jedis.BitOP op, byte[] destKey, byte[]... srcKeys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.bitop(op, destKey, srcKeys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("bitop - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] randomBinaryKey() {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.randomBinaryKey();
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("randomBinaryKey - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String pfmerge(byte[] destkey, byte[]... sourcekeys) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.pfmerge(destkey, sourcekeys);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("pfmerge - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<java.lang.Long> scriptExists(byte[]... sha1) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.scriptExists(sha1);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("scriptExists - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.evalsha(sha1, keyCount, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("evalsha - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object evalsha(byte[] sha1, java.util.List<byte[]> keys, java.util.List<byte[]> args) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.evalsha(sha1, keys, args);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("evalsha - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object evalsha(byte[] sha1) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.evalsha(sha1);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("evalsha - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String scriptFlush() {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.scriptFlush();
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("scriptFlush - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public byte[] scriptLoad(byte[] script) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.scriptLoad(script);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("scriptLoad - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.String scriptKill() {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.scriptKill();
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("scriptKill - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object eval(byte[] script, int keyCount, byte[]... params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.eval(script, keyCount, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("eval - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object eval(byte[] script) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.eval(script);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("eval - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object eval(byte[] script, java.util.List<byte[]> keys, java.util.List<byte[]> args) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.eval(script, keys, args);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("eval - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.lang.Object eval(byte[] script, byte[] keyCount, byte[]... params) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.eval(script, keyCount, params);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("eval - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeWithScores(byte[] key, long start, long end) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeWithScores(key, start, end);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(byte[] key, double min, double max,
			int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScoreWithScores(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScoreWithScores(key, min, max);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max,
			int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min,
			int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScoreWithScores(key, max, min);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min,
			int offset, int count) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.zrevrangeByScoreWithScores(key, max, min);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("zrevrangeByScoreWithScores - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member,
			double radius, redis.clients.jedis.GeoUnit unit) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.georadiusByMember(key, member, radius, unit);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("georadiusByMember - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member,
			double radius, redis.clients.jedis.GeoUnit unit, redis.clients.jedis.params.geo.GeoRadiusParam param) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.georadiusByMember(key, member, radius, unit, param);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("georadiusByMember - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public void subscribe(redis.clients.jedis.BinaryJedisPubSub jedisPubSub, byte[]... channels) {
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				jedis.subscribe(jedisPubSub, channels);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error(this.hosts + " - redis connection failed，idle=" + pool.getNumIdle()
							+ ",active=" + pool.getNumActive(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("subscribe - redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

}
