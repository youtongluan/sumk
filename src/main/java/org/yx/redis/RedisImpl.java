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

import redis.clients.jedis.JedisPoolConfig;

public class RedisImpl extends Redis {

	public RedisImpl(JedisPoolConfig config, RedisParamter p) {
		super(config, p);
	}

	public java.lang.String get(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.get(key);
		});
	}

	public java.lang.String type(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.type(key);
		});
	}

	public java.lang.Long append(java.lang.String key, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.append(key, value);
		});
	}

	public java.util.Set<java.lang.String> keys(java.lang.String pattern) {
		return this.executeAndRetry(jedis -> {
			return jedis.keys(pattern);
		});
	}

	public java.lang.String set(java.lang.String key, java.lang.String value, java.lang.String nxxx) {
		return this.executeAndRetry(jedis -> {
			return jedis.set(key, value, nxxx);
		});
	}

	public java.lang.String set(java.lang.String key, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.set(key, value);
		});
	}

	public java.lang.String set(java.lang.String key, java.lang.String value, java.lang.String expx, long time) {
		return this.executeAndRetry(jedis -> {
			return jedis.set(key, value, expx, time);
		});
	}

	public java.lang.String set(java.lang.String key, java.lang.String value, java.lang.String nxxx,
			java.lang.String expx, long time) {
		return this.executeAndRetry(jedis -> {
			return jedis.set(key, value, nxxx, expx, time);
		});
	}

	public java.lang.Long exists(java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.exists(keys);
		});
	}

	public java.lang.Boolean exists(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.exists(key);
		});
	}

	public java.lang.String rename(java.lang.String oldkey, java.lang.String newkey) {
		return this.executeAndRetry(jedis -> {
			return jedis.rename(oldkey, newkey);
		});
	}

	public java.util.List<java.lang.String> sort(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.sort(key);
		});
	}

	public java.util.List<java.lang.String> sort(java.lang.String key,
			redis.clients.jedis.SortingParams sortingParameters) {
		return this.executeAndRetry(jedis -> {
			return jedis.sort(key, sortingParameters);
		});
	}

	public java.lang.Long sort(java.lang.String key, redis.clients.jedis.SortingParams sortingParameters,
			java.lang.String dstkey) {
		return this.executeAndRetry(jedis -> {
			return jedis.sort(key, sortingParameters, dstkey);
		});
	}

	public java.lang.Long sort(java.lang.String key, java.lang.String dstkey) {
		return this.executeAndRetry(jedis -> {
			return jedis.sort(key, dstkey);
		});
	}

	public java.lang.Long unlink(java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.unlink(keys);
		});
	}

	public java.lang.Long unlink(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.unlink(key);
		});
	}

	public redis.clients.jedis.ScanResult<java.lang.String> scan(java.lang.String cursor) {
		return this.executeAndRetry(jedis -> {
			return jedis.scan(cursor);
		});
	}

	@SuppressWarnings("deprecation")
	public redis.clients.jedis.ScanResult<java.lang.String> scan(int cursor) {
		return this.executeAndRetry(jedis -> {
			return jedis.scan(cursor);
		});
	}

	public redis.clients.jedis.ScanResult<java.lang.String> scan(java.lang.String cursor,
			redis.clients.jedis.ScanParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.scan(cursor, params);
		});
	}

	@SuppressWarnings("deprecation")
	public java.util.List<java.lang.String> brpop(java.lang.String arg) {
		return this.executeAndRetry(jedis -> {
			return jedis.brpop(arg);
		});
	}

	public java.util.List<java.lang.String> brpop(int timeout, java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.brpop(timeout, keys);
		});
	}

	public java.util.List<java.lang.String> brpop(int timeout, java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.brpop(timeout, key);
		});
	}

	public java.util.List<java.lang.String> brpop(java.lang.String... args) {
		return this.executeAndRetry(jedis -> {
			return jedis.brpop(args);
		});
	}

	public byte[] dump(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.dump(key);
		});
	}

	public java.lang.Object eval(java.lang.String script, int keyCount, java.lang.String... params) {
		return this.executeAndRetry(jedis -> {
			return jedis.eval(script, keyCount, params);
		});
	}

	public java.lang.Object eval(java.lang.String script) {
		return this.executeAndRetry(jedis -> {
			return jedis.eval(script);
		});
	}

	public java.lang.Object eval(java.lang.String script, java.util.List<java.lang.String> keys,
			java.util.List<java.lang.String> args) {
		return this.executeAndRetry(jedis -> {
			return jedis.eval(script, keys, args);
		});
	}

	public void subscribe(redis.clients.jedis.JedisPubSub jedisPubSub, java.lang.String... channels) {
		this.executeAndRetry(jedis -> {
			jedis.subscribe(jedisPubSub, channels);
			return null;
		});
	}

	public java.lang.Long zinterstore(java.lang.String dstkey, redis.clients.jedis.ZParams params,
			java.lang.String... sets) {
		return this.executeAndRetry(jedis -> {
			return jedis.zinterstore(dstkey, params, sets);
		});
	}

	public java.lang.Long zinterstore(java.lang.String dstkey, java.lang.String... sets) {
		return this.executeAndRetry(jedis -> {
			return jedis.zinterstore(dstkey, sets);
		});
	}

	public java.lang.Long publish(java.lang.String channel, java.lang.String message) {
		return this.executeAndRetry(jedis -> {
			return jedis.publish(channel, message);
		});
	}

	public void psubscribe(redis.clients.jedis.JedisPubSub jedisPubSub, java.lang.String... patterns) {
		this.executeAndRetry(jedis -> {
			jedis.psubscribe(jedisPubSub, patterns);
			return null;
		});
	}

	public java.lang.String pfmerge(java.lang.String destkey, java.lang.String... sourcekeys) {
		return this.executeAndRetry(jedis -> {
			return jedis.pfmerge(destkey, sourcekeys);
		});
	}

	public java.lang.Long bitop(redis.clients.jedis.BitOP op, java.lang.String destKey, java.lang.String... srcKeys) {
		return this.executeAndRetry(jedis -> {
			return jedis.bitop(op, destKey, srcKeys);
		});
	}

	public java.lang.Long zunionstore(java.lang.String dstkey, redis.clients.jedis.ZParams params,
			java.lang.String... sets) {
		return this.executeAndRetry(jedis -> {
			return jedis.zunionstore(dstkey, params, sets);
		});
	}

	public java.lang.Long zunionstore(java.lang.String dstkey, java.lang.String... sets) {
		return this.executeAndRetry(jedis -> {
			return jedis.zunionstore(dstkey, sets);
		});
	}

	public java.lang.String brpoplpush(java.lang.String source, java.lang.String destination, int timeout) {
		return this.executeAndRetry(jedis -> {
			return jedis.brpoplpush(source, destination, timeout);
		});
	}

	public java.lang.String randomKey() {
		return this.executeAndRetry(jedis -> {
			return jedis.randomKey();
		});
	}

	public java.lang.String watch(java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.watch(keys);
		});
	}

	public java.lang.String scriptLoad(java.lang.String script) {
		return this.executeAndRetry(jedis -> {
			return jedis.scriptLoad(script);
		});
	}

	public java.lang.Object evalsha(java.lang.String sha1, int keyCount, java.lang.String... params) {
		return this.executeAndRetry(jedis -> {
			return jedis.evalsha(sha1, keyCount, params);
		});
	}

	public java.lang.Object evalsha(java.lang.String sha1, java.util.List<java.lang.String> keys,
			java.util.List<java.lang.String> args) {
		return this.executeAndRetry(jedis -> {
			return jedis.evalsha(sha1, keys, args);
		});
	}

	public java.lang.Object evalsha(java.lang.String sha1) {
		return this.executeAndRetry(jedis -> {
			return jedis.evalsha(sha1);
		});
	}

	public java.lang.Boolean scriptExists(java.lang.String sha1) {
		return this.executeAndRetry(jedis -> {
			return jedis.scriptExists(sha1);
		});
	}

	public java.util.List<java.lang.Boolean> scriptExists(java.lang.String... sha1) {
		return this.executeAndRetry(jedis -> {
			return jedis.scriptExists(sha1);
		});
	}

	public java.util.List<java.lang.String> hvals(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.hvals(key);
		});
	}

	public java.lang.Long lrem(java.lang.String key, long count, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.lrem(key, count, value);
		});
	}

	public java.lang.Long pttl(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.pttl(key);
		});
	}

	public java.util.Set<java.lang.String> hkeys(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.hkeys(key);
		});
	}

	public java.lang.String hget(java.lang.String key, java.lang.String field) {
		return this.executeAndRetry(jedis -> {
			return jedis.hget(key, field);
		});
	}

	public java.lang.String setex(java.lang.String key, int seconds, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.setex(key, seconds, value);
		});
	}

	public java.lang.Long hdel(java.lang.String key, java.lang.String... fields) {
		return this.executeAndRetry(jedis -> {
			return jedis.hdel(key, fields);
		});
	}

	public java.lang.String ltrim(java.lang.String key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.ltrim(key, start, stop);
		});
	}

	public java.lang.String restore(java.lang.String key, int ttl, byte[] serializedValue) {
		return this.executeAndRetry(jedis -> {
			return jedis.restore(key, ttl, serializedValue);
		});
	}

	public java.lang.Long touch(java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.touch(keys);
		});
	}

	public java.lang.Long touch(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.touch(key);
		});
	}

	public java.lang.Long hsetnx(java.lang.String key, java.lang.String field, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.hsetnx(key, field, value);
		});
	}

	public java.lang.String lpop(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.lpop(key);
		});
	}

	public java.lang.String psetex(java.lang.String key, long milliseconds, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.psetex(key, milliseconds, value);
		});
	}

	public java.lang.Long pexpireAt(java.lang.String key, long millisecondsTimestamp) {
		return this.executeAndRetry(jedis -> {
			return jedis.pexpireAt(key, millisecondsTimestamp);
		});
	}

	public java.lang.String hmset(java.lang.String key, java.util.Map<java.lang.String, java.lang.String> hash) {
		return this.executeAndRetry(jedis -> {
			return jedis.hmset(key, hash);
		});
	}

	public java.lang.Long lpush(java.lang.String key, java.lang.String... strings) {
		return this.executeAndRetry(jedis -> {
			return jedis.lpush(key, strings);
		});
	}

	public java.lang.String rpop(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.rpop(key);
		});
	}

	public java.lang.Long sadd(java.lang.String key, java.lang.String... members) {
		return this.executeAndRetry(jedis -> {
			return jedis.sadd(key, members);
		});
	}

	public java.util.Set<java.lang.String> smembers(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.smembers(key);
		});
	}

	public java.lang.Long srem(java.lang.String key, java.lang.String... members) {
		return this.executeAndRetry(jedis -> {
			return jedis.srem(key, members);
		});
	}

	public java.util.Set<java.lang.String> spop(java.lang.String key, long count) {
		return this.executeAndRetry(jedis -> {
			return jedis.spop(key, count);
		});
	}

	public java.lang.String spop(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.spop(key);
		});
	}

	public java.lang.Double hincrByFloat(java.lang.String key, java.lang.String field, double value) {
		return this.executeAndRetry(jedis -> {
			return jedis.hincrByFloat(key, field, value);
		});
	}

	public java.lang.Long setrange(java.lang.String key, long offset, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.setrange(key, offset, value);
		});
	}

	public java.lang.Long setnx(java.lang.String key, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.setnx(key, value);
		});
	}

	public java.lang.Boolean getbit(java.lang.String key, long offset) {
		return this.executeAndRetry(jedis -> {
			return jedis.getbit(key, offset);
		});
	}

	public java.lang.Long hset(java.lang.String key, java.lang.String field, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.hset(key, field, value);
		});
	}

	public java.lang.Long hset(java.lang.String key, java.util.Map<java.lang.String, java.lang.String> hash) {
		return this.executeAndRetry(jedis -> {
			return jedis.hset(key, hash);
		});
	}

	public java.lang.Long decr(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.decr(key);
		});
	}

	public java.lang.String substr(java.lang.String key, int start, int end) {
		return this.executeAndRetry(jedis -> {
			return jedis.substr(key, start, end);
		});
	}

	public java.lang.String getrange(java.lang.String key, long startOffset, long endOffset) {
		return this.executeAndRetry(jedis -> {
			return jedis.getrange(key, startOffset, endOffset);
		});
	}

	public java.util.List<java.lang.String> hmget(java.lang.String key, java.lang.String... fields) {
		return this.executeAndRetry(jedis -> {
			return jedis.hmget(key, fields);
		});
	}

	public java.lang.Boolean setbit(java.lang.String key, long offset, boolean value) {
		return this.executeAndRetry(jedis -> {
			return jedis.setbit(key, offset, value);
		});
	}

	public java.lang.Boolean setbit(java.lang.String key, long offset, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.setbit(key, offset, value);
		});
	}

	public java.lang.Long pexpire(java.lang.String key, long milliseconds) {
		return this.executeAndRetry(jedis -> {
			return jedis.pexpire(key, milliseconds);
		});
	}

	public java.lang.Long ttl(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.ttl(key);
		});
	}

	public java.lang.Long hincrBy(java.lang.String key, java.lang.String field, long value) {
		return this.executeAndRetry(jedis -> {
			return jedis.hincrBy(key, field, value);
		});
	}

	public java.lang.Boolean hexists(java.lang.String key, java.lang.String field) {
		return this.executeAndRetry(jedis -> {
			return jedis.hexists(key, field);
		});
	}

	public java.lang.Long hlen(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.hlen(key);
		});
	}

	public java.util.Map<java.lang.String, java.lang.String> hgetAll(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.hgetAll(key);
		});
	}

	public java.lang.Long persist(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.persist(key);
		});
	}

	public java.lang.Long decrBy(java.lang.String key, long decrement) {
		return this.executeAndRetry(jedis -> {
			return jedis.decrBy(key, decrement);
		});
	}

	public java.lang.Long rpush(java.lang.String key, java.lang.String... strings) {
		return this.executeAndRetry(jedis -> {
			return jedis.rpush(key, strings);
		});
	}

	public java.lang.String getSet(java.lang.String key, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.getSet(key, value);
		});
	}

	public java.lang.Long llen(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.llen(key);
		});
	}

	public java.util.List<java.lang.String> lrange(java.lang.String key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.lrange(key, start, stop);
		});
	}

	public java.lang.String lindex(java.lang.String key, long index) {
		return this.executeAndRetry(jedis -> {
			return jedis.lindex(key, index);
		});
	}

	public java.lang.String lset(java.lang.String key, long index, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.lset(key, index, value);
		});
	}

	public java.lang.Double incrByFloat(java.lang.String key, double increment) {
		return this.executeAndRetry(jedis -> {
			return jedis.incrByFloat(key, increment);
		});
	}

	public java.lang.Long expireAt(java.lang.String key, long unixTime) {
		return this.executeAndRetry(jedis -> {
			return jedis.expireAt(key, unixTime);
		});
	}

	public java.lang.Long incrBy(java.lang.String key, long increment) {
		return this.executeAndRetry(jedis -> {
			return jedis.incrBy(key, increment);
		});
	}

	public java.lang.Long incr(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.incr(key);
		});
	}

	public java.lang.Long expire(java.lang.String key, int seconds) {
		return this.executeAndRetry(jedis -> {
			return jedis.expire(key, seconds);
		});
	}

	public long pfcount(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.pfcount(key);
		});
	}

	public long pfcount(java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.pfcount(keys);
		});
	}

	public java.util.List<redis.clients.jedis.GeoCoordinate> geopos(java.lang.String key, java.lang.String... members) {
		return this.executeAndRetry(jedis -> {
			return jedis.geopos(key, members);
		});
	}

	public java.lang.Long bitpos(java.lang.String key, boolean value, redis.clients.jedis.BitPosParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.bitpos(key, value, params);
		});
	}

	public java.lang.Long bitpos(java.lang.String key, boolean value) {
		return this.executeAndRetry(jedis -> {
			return jedis.bitpos(key, value);
		});
	}

	public java.lang.Long zremrangeByRank(java.lang.String key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.zremrangeByRank(key, start, stop);
		});
	}

	@SuppressWarnings("deprecation")
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<java.lang.String, java.lang.String>> hscan(
			java.lang.String key, int cursor) {
		return this.executeAndRetry(jedis -> {
			return jedis.hscan(key, cursor);
		});
	}

	public redis.clients.jedis.ScanResult<java.util.Map.Entry<java.lang.String, java.lang.String>> hscan(
			java.lang.String key, java.lang.String cursor, redis.clients.jedis.ScanParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.hscan(key, cursor, params);
		});
	}

	public redis.clients.jedis.ScanResult<java.util.Map.Entry<java.lang.String, java.lang.String>> hscan(
			java.lang.String key, java.lang.String cursor) {
		return this.executeAndRetry(jedis -> {
			return jedis.hscan(key, cursor);
		});
	}

	public java.lang.Boolean sismember(java.lang.String key, java.lang.String member) {
		return this.executeAndRetry(jedis -> {
			return jedis.sismember(key, member);
		});
	}

	public java.lang.Long strlen(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.strlen(key);
		});
	}

	public java.util.Set<java.lang.String> zrevrange(java.lang.String key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrange(key, start, stop);
		});
	}

	public java.lang.Long move(java.lang.String key, int dbIndex) {
		return this.executeAndRetry(jedis -> {
			return jedis.move(key, dbIndex);
		});
	}

	public java.lang.Long zrevrank(java.lang.String key, java.lang.String member) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrank(key, member);
		});
	}

	public java.util.Set<java.lang.String> zrangeByLex(java.lang.String key, java.lang.String min, java.lang.String max,
			int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByLex(key, min, max, offset, count);
		});
	}

	public java.util.Set<java.lang.String> zrangeByLex(java.lang.String key, java.lang.String min,
			java.lang.String max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByLex(key, min, max);
		});
	}

	public java.lang.String mset(java.lang.String... keysvalues) {
		return this.executeAndRetry(jedis -> {
			return jedis.mset(keysvalues);
		});
	}

	public java.lang.String rpoplpush(java.lang.String srckey, java.lang.String dstkey) {
		return this.executeAndRetry(jedis -> {
			return jedis.rpoplpush(srckey, dstkey);
		});
	}

	public java.util.Set<java.lang.String> sdiff(java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.sdiff(keys);
		});
	}

	public java.lang.Long rpushx(java.lang.String key, java.lang.String... string) {
		return this.executeAndRetry(jedis -> {
			return jedis.rpushx(key, string);
		});
	}

	public java.util.List<java.lang.Long> bitfield(java.lang.String key, java.lang.String... arguments) {
		return this.executeAndRetry(jedis -> {
			return jedis.bitfield(key, arguments);
		});
	}

	public java.lang.Long sdiffstore(java.lang.String dstkey, java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.sdiffstore(dstkey, keys);
		});
	}

	public java.util.Set<java.lang.String> sinter(java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.sinter(keys);
		});
	}

	public java.lang.Long sinterstore(java.lang.String dstkey, java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.sinterstore(dstkey, keys);
		});
	}

	public java.lang.Long smove(java.lang.String srckey, java.lang.String dstkey, java.lang.String member) {
		return this.executeAndRetry(jedis -> {
			return jedis.smove(srckey, dstkey, member);
		});
	}

	public java.util.Set<java.lang.String> sunion(java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.sunion(keys);
		});
	}

	public java.util.Set<java.lang.String> zrevrangeByLex(java.lang.String key, java.lang.String max,
			java.lang.String min, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByLex(key, max, min, offset, count);
		});
	}

	public java.util.Set<java.lang.String> zrevrangeByLex(java.lang.String key, java.lang.String max,
			java.lang.String min) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByLex(key, max, min);
		});
	}

	public java.lang.Long sunionstore(java.lang.String dstkey, java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.sunionstore(dstkey, keys);
		});
	}

	public java.lang.Long del(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.del(key);
		});
	}

	public java.lang.Long del(java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.del(keys);
		});
	}

	public java.lang.Long linsert(java.lang.String key, redis.clients.jedis.ListPosition where, java.lang.String pivot,
			java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.linsert(key, where, pivot, value);
		});
	}

	@SuppressWarnings("deprecation")
	public java.lang.Long linsert(java.lang.String key, redis.clients.jedis.BinaryClient.LIST_POSITION where,
			java.lang.String pivot, java.lang.String value) {
		return this.executeAndRetry(jedis -> {
			return jedis.linsert(key, where, pivot, value);
		});
	}

	public java.lang.Double zincrby(java.lang.String key, double increment, java.lang.String member,
			redis.clients.jedis.params.sortedset.ZIncrByParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.zincrby(key, increment, member, params);
		});
	}

	public java.lang.Double zincrby(java.lang.String key, double increment, java.lang.String member) {
		return this.executeAndRetry(jedis -> {
			return jedis.zincrby(key, increment, member);
		});
	}

	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key, double min, double max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScore(key, min, max);
		});
	}

	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key, java.lang.String min,
			java.lang.String max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScore(key, min, max);
		});
	}

	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key, java.lang.String min,
			java.lang.String max, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScore(key, min, max, offset, count);
		});
	}

	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key, double min, double max, int offset,
			int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScore(key, min, max, offset, count);
		});
	}

	public java.lang.Long zadd(java.lang.String key, java.util.Map<java.lang.String, java.lang.Double> scoreMembers,
			redis.clients.jedis.params.sortedset.ZAddParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.zadd(key, scoreMembers, params);
		});
	}

	public java.lang.Long zadd(java.lang.String key, double score, java.lang.String member,
			redis.clients.jedis.params.sortedset.ZAddParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.zadd(key, score, member, params);
		});
	}

	public java.lang.Long zadd(java.lang.String key, double score, java.lang.String member) {
		return this.executeAndRetry(jedis -> {
			return jedis.zadd(key, score, member);
		});
	}

	public java.lang.Long zadd(java.lang.String key, java.util.Map<java.lang.String, java.lang.Double> scoreMembers) {
		return this.executeAndRetry(jedis -> {
			return jedis.zadd(key, scoreMembers);
		});
	}

	public java.lang.Long zrem(java.lang.String key, java.lang.String... members) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrem(key, members);
		});
	}

	public java.lang.Long geoadd(java.lang.String key, double longitude, double latitude, java.lang.String member) {
		return this.executeAndRetry(jedis -> {
			return jedis.geoadd(key, longitude, latitude, member);
		});
	}

	public java.lang.Long geoadd(java.lang.String key,
			java.util.Map<java.lang.String, redis.clients.jedis.GeoCoordinate> memberCoordinateMap) {
		return this.executeAndRetry(jedis -> {
			return jedis.geoadd(key, memberCoordinateMap);
		});
	}

	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key, java.lang.String max,
			java.lang.String min, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScore(key, max, min, offset, count);
		});
	}

	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key, double max, double min) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScore(key, max, min);
		});
	}

	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key, double max, double min, int offset,
			int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScore(key, max, min, offset, count);
		});
	}

	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key, java.lang.String max,
			java.lang.String min) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScore(key, max, min);
		});
	}

	public java.util.List<java.lang.String> srandmember(java.lang.String key, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.srandmember(key, count);
		});
	}

	public java.lang.String srandmember(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.srandmember(key);
		});
	}

	public java.lang.Long zrank(java.lang.String key, java.lang.String member) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrank(key, member);
		});
	}

	public java.lang.Long scard(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.scard(key);
		});
	}

	public java.lang.Long zcount(java.lang.String key, java.lang.String min, java.lang.String max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zcount(key, min, max);
		});
	}

	public java.lang.Long zcount(java.lang.String key, double min, double max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zcount(key, min, max);
		});
	}

	public java.lang.Long zremrangeByLex(java.lang.String key, java.lang.String min, java.lang.String max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zremrangeByLex(key, min, max);
		});
	}

	public java.util.Set<java.lang.String> zrange(java.lang.String key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrange(key, start, stop);
		});
	}

	public java.lang.Long lpushx(java.lang.String key, java.lang.String... string) {
		return this.executeAndRetry(jedis -> {
			return jedis.lpushx(key, string);
		});
	}

	@SuppressWarnings("deprecation")
	public java.util.List<java.lang.String> blpop(java.lang.String arg) {
		return this.executeAndRetry(jedis -> {
			return jedis.blpop(arg);
		});
	}

	public java.util.List<java.lang.String> blpop(java.lang.String... args) {
		return this.executeAndRetry(jedis -> {
			return jedis.blpop(args);
		});
	}

	public java.util.List<java.lang.String> blpop(int timeout, java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.blpop(timeout, keys);
		});
	}

	public java.util.List<java.lang.String> blpop(int timeout, java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.blpop(timeout, key);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(java.lang.String key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadius(key, longitude, latitude, radius, unit);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(java.lang.String key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadius(key, longitude, latitude, radius, unit, param);
		});
	}

	@SuppressWarnings("deprecation")
	public redis.clients.jedis.ScanResult<java.lang.String> sscan(java.lang.String key, int cursor) {
		return this.executeAndRetry(jedis -> {
			return jedis.sscan(key, cursor);
		});
	}

	public redis.clients.jedis.ScanResult<java.lang.String> sscan(java.lang.String key, java.lang.String cursor,
			redis.clients.jedis.ScanParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.sscan(key, cursor, params);
		});
	}

	public redis.clients.jedis.ScanResult<java.lang.String> sscan(java.lang.String key, java.lang.String cursor) {
		return this.executeAndRetry(jedis -> {
			return jedis.sscan(key, cursor);
		});
	}

	@SuppressWarnings("deprecation")
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(java.lang.String key, int cursor) {
		return this.executeAndRetry(jedis -> {
			return jedis.zscan(key, cursor);
		});
	}

	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(java.lang.String key,
			java.lang.String cursor) {
		return this.executeAndRetry(jedis -> {
			return jedis.zscan(key, cursor);
		});
	}

	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(java.lang.String key,
			java.lang.String cursor, redis.clients.jedis.ScanParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.zscan(key, cursor, params);
		});
	}

	public java.lang.Long zcard(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.zcard(key);
		});
	}

	public java.util.List<java.lang.String> geohash(java.lang.String key, java.lang.String... members) {
		return this.executeAndRetry(jedis -> {
			return jedis.geohash(key, members);
		});
	}

	public java.lang.Long zlexcount(java.lang.String key, java.lang.String min, java.lang.String max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zlexcount(key, min, max);
		});
	}

	public java.lang.Long hstrlen(java.lang.String key, java.lang.String field) {
		return this.executeAndRetry(jedis -> {
			return jedis.hstrlen(key, field);
		});
	}

	public java.lang.Long msetnx(java.lang.String... keysvalues) {
		return this.executeAndRetry(jedis -> {
			return jedis.msetnx(keysvalues);
		});
	}

	public java.util.List<java.lang.String> mget(java.lang.String... keys) {
		return this.executeAndRetry(jedis -> {
			return jedis.mget(keys);
		});
	}

	public java.lang.Long renamenx(java.lang.String oldkey, java.lang.String newkey) {
		return this.executeAndRetry(jedis -> {
			return jedis.renamenx(oldkey, newkey);
		});
	}

	public java.lang.Long zremrangeByScore(java.lang.String key, java.lang.String min, java.lang.String max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zremrangeByScore(key, min, max);
		});
	}

	public java.lang.Long zremrangeByScore(java.lang.String key, double min, double max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zremrangeByScore(key, min, max);
		});
	}

	public java.lang.Long bitcount(java.lang.String key) {
		return this.executeAndRetry(jedis -> {
			return jedis.bitcount(key);
		});
	}

	public java.lang.Long bitcount(java.lang.String key, long start, long end) {
		return this.executeAndRetry(jedis -> {
			return jedis.bitcount(key, start, end);
		});
	}

	public java.lang.Double geodist(java.lang.String key, java.lang.String member1, java.lang.String member2,
			redis.clients.jedis.GeoUnit unit) {
		return this.executeAndRetry(jedis -> {
			return jedis.geodist(key, member1, member2, unit);
		});
	}

	public java.lang.Double geodist(java.lang.String key, java.lang.String member1, java.lang.String member2) {
		return this.executeAndRetry(jedis -> {
			return jedis.geodist(key, member1, member2);
		});
	}

	public java.lang.Long pfadd(java.lang.String key, java.lang.String... elements) {
		return this.executeAndRetry(jedis -> {
			return jedis.pfadd(key, elements);
		});
	}

	public java.lang.Double zscore(java.lang.String key, java.lang.String member) {
		return this.executeAndRetry(jedis -> {
			return jedis.zscore(key, member);
		});
	}

	public java.lang.String echo(java.lang.String string) {
		return this.executeAndRetry(jedis -> {
			return jedis.echo(string);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrangeWithScores(java.lang.String key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeWithScores(key, start, stop);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeWithScores(java.lang.String key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeWithScores(key, start, stop);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMemberReadonly(java.lang.String key,
			java.lang.String member, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadiusByMemberReadonly(key, member, radius, unit, param);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMemberReadonly(java.lang.String key,
			java.lang.String member, double radius, redis.clients.jedis.GeoUnit unit) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadiusByMemberReadonly(key, member, radius, unit);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusReadonly(java.lang.String key,
			double longitude, double latitude, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadiusReadonly(key, longitude, latitude, radius, unit, param);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusReadonly(java.lang.String key,
			double longitude, double latitude, double radius, redis.clients.jedis.GeoUnit unit) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadiusReadonly(key, longitude, latitude, radius, unit);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key,
			java.lang.String max, java.lang.String min) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScoreWithScores(key, max, min);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key, double max,
			double min) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScoreWithScores(key, max, min);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key, double max,
			double min, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key,
			java.lang.String max, java.lang.String min, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key, double min,
			double max, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key, java.lang.String min,
			java.lang.String max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScoreWithScores(key, min, max);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key, double min,
			double max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScoreWithScores(key, min, max);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key, java.lang.String min,
			java.lang.String max, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(java.lang.String key,
			java.lang.String member, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadiusByMember(key, member, radius, unit, param);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(java.lang.String key,
			java.lang.String member, double radius, redis.clients.jedis.GeoUnit unit) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadiusByMember(key, member, radius, unit);
		});
	}

	public byte[] get(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.get(key);
		});
	}

	public java.lang.String type(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.type(key);
		});
	}

	public java.lang.Long append(byte[] key, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.append(key, value);
		});
	}

	public java.lang.String set(byte[] key, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.set(key, value);
		});
	}

	public java.lang.String set(byte[] key, byte[] value, byte[] nxxx) {
		return this.executeAndRetry(jedis -> {
			return jedis.set(key, value, nxxx);
		});
	}

	public java.lang.String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
		return this.executeAndRetry(jedis -> {
			return jedis.set(key, value, nxxx, expx, time);
		});
	}

	public java.lang.Boolean exists(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.exists(key);
		});
	}

	public java.util.List<byte[]> sort(byte[] key, redis.clients.jedis.SortingParams sortingParameters) {
		return this.executeAndRetry(jedis -> {
			return jedis.sort(key, sortingParameters);
		});
	}

	public java.util.List<byte[]> sort(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.sort(key);
		});
	}

	public java.lang.Long unlink(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.unlink(key);
		});
	}

	@SuppressWarnings("deprecation")
	public java.util.List<byte[]> brpop(byte[] arg) {
		return this.executeAndRetry(jedis -> {
			return jedis.brpop(arg);
		});
	}

	public byte[] dump(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.dump(key);
		});
	}

	public java.lang.String unwatch() {
		return this.executeAndRetry(jedis -> {
			return jedis.unwatch();
		});
	}

	public java.util.List<byte[]> hvals(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.hvals(key);
		});
	}

	public java.lang.Long lrem(byte[] key, long count, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.lrem(key, count, value);
		});
	}

	public java.lang.Long pttl(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.pttl(key);
		});
	}

	public java.util.Set<byte[]> hkeys(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.hkeys(key);
		});
	}

	public byte[] hget(byte[] key, byte[] field) {
		return this.executeAndRetry(jedis -> {
			return jedis.hget(key, field);
		});
	}

	public java.lang.String setex(byte[] key, int seconds, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.setex(key, seconds, value);
		});
	}

	public java.lang.Long hdel(byte[] key, byte[]... fields) {
		return this.executeAndRetry(jedis -> {
			return jedis.hdel(key, fields);
		});
	}

	public java.lang.String ltrim(byte[] key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.ltrim(key, start, stop);
		});
	}

	public java.lang.String restoreReplace(byte[] key, int ttl, byte[] serializedValue) {
		return this.executeAndRetry(jedis -> {
			return jedis.restoreReplace(key, ttl, serializedValue);
		});
	}

	public java.lang.String restore(byte[] key, int ttl, byte[] serializedValue) {
		return this.executeAndRetry(jedis -> {
			return jedis.restore(key, ttl, serializedValue);
		});
	}

	public java.lang.Long touch(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.touch(key);
		});
	}

	public java.lang.Long hsetnx(byte[] key, byte[] field, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.hsetnx(key, field, value);
		});
	}

	public byte[] lpop(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.lpop(key);
		});
	}

	public java.lang.String psetex(byte[] key, long milliseconds, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.psetex(key, milliseconds, value);
		});
	}

	public java.lang.Long pexpireAt(byte[] key, long millisecondsTimestamp) {
		return this.executeAndRetry(jedis -> {
			return jedis.pexpireAt(key, millisecondsTimestamp);
		});
	}

	public java.lang.String hmset(byte[] key, java.util.Map<byte[], byte[]> hash) {
		return this.executeAndRetry(jedis -> {
			return jedis.hmset(key, hash);
		});
	}

	public java.lang.Long lpush(byte[] key, byte[]... strings) {
		return this.executeAndRetry(jedis -> {
			return jedis.lpush(key, strings);
		});
	}

	public byte[] rpop(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.rpop(key);
		});
	}

	public java.lang.Long sadd(byte[] key, byte[]... members) {
		return this.executeAndRetry(jedis -> {
			return jedis.sadd(key, members);
		});
	}

	public java.util.Set<byte[]> smembers(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.smembers(key);
		});
	}

	public java.lang.Long srem(byte[] key, byte[]... member) {
		return this.executeAndRetry(jedis -> {
			return jedis.srem(key, member);
		});
	}

	public byte[] spop(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.spop(key);
		});
	}

	public java.util.Set<byte[]> spop(byte[] key, long count) {
		return this.executeAndRetry(jedis -> {
			return jedis.spop(key, count);
		});
	}

	public java.lang.Double hincrByFloat(byte[] key, byte[] field, double value) {
		return this.executeAndRetry(jedis -> {
			return jedis.hincrByFloat(key, field, value);
		});
	}

	public java.lang.Long setrange(byte[] key, long offset, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.setrange(key, offset, value);
		});
	}

	public java.lang.Long setnx(byte[] key, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.setnx(key, value);
		});
	}

	public java.lang.Boolean getbit(byte[] key, long offset) {
		return this.executeAndRetry(jedis -> {
			return jedis.getbit(key, offset);
		});
	}

	public java.lang.Long hset(byte[] key, java.util.Map<byte[], byte[]> hash) {
		return this.executeAndRetry(jedis -> {
			return jedis.hset(key, hash);
		});
	}

	public java.lang.Long hset(byte[] key, byte[] field, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.hset(key, field, value);
		});
	}

	public java.lang.Long decr(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.decr(key);
		});
	}

	public byte[] substr(byte[] key, int start, int end) {
		return this.executeAndRetry(jedis -> {
			return jedis.substr(key, start, end);
		});
	}

	public byte[] getrange(byte[] key, long startOffset, long endOffset) {
		return this.executeAndRetry(jedis -> {
			return jedis.getrange(key, startOffset, endOffset);
		});
	}

	public java.util.List<byte[]> hmget(byte[] key, byte[]... fields) {
		return this.executeAndRetry(jedis -> {
			return jedis.hmget(key, fields);
		});
	}

	public java.lang.Boolean setbit(byte[] key, long offset, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.setbit(key, offset, value);
		});
	}

	public java.lang.Boolean setbit(byte[] key, long offset, boolean value) {
		return this.executeAndRetry(jedis -> {
			return jedis.setbit(key, offset, value);
		});
	}

	public java.lang.Long pexpire(byte[] key, long milliseconds) {
		return this.executeAndRetry(jedis -> {
			return jedis.pexpire(key, milliseconds);
		});
	}

	public java.lang.Long ttl(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.ttl(key);
		});
	}

	public java.lang.Long hincrBy(byte[] key, byte[] field, long value) {
		return this.executeAndRetry(jedis -> {
			return jedis.hincrBy(key, field, value);
		});
	}

	public java.lang.Boolean hexists(byte[] key, byte[] field) {
		return this.executeAndRetry(jedis -> {
			return jedis.hexists(key, field);
		});
	}

	public java.lang.Long hlen(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.hlen(key);
		});
	}

	public java.util.Map<byte[], byte[]> hgetAll(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.hgetAll(key);
		});
	}

	public java.lang.Long persist(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.persist(key);
		});
	}

	public java.lang.Long decrBy(byte[] key, long decrement) {
		return this.executeAndRetry(jedis -> {
			return jedis.decrBy(key, decrement);
		});
	}

	public java.lang.Long rpush(byte[] key, byte[]... strings) {
		return this.executeAndRetry(jedis -> {
			return jedis.rpush(key, strings);
		});
	}

	public byte[] getSet(byte[] key, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.getSet(key, value);
		});
	}

	public java.lang.Long llen(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.llen(key);
		});
	}

	public java.util.List<byte[]> lrange(byte[] key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.lrange(key, start, stop);
		});
	}

	public byte[] lindex(byte[] key, long index) {
		return this.executeAndRetry(jedis -> {
			return jedis.lindex(key, index);
		});
	}

	public java.lang.String lset(byte[] key, long index, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.lset(key, index, value);
		});
	}

	public java.lang.Double incrByFloat(byte[] key, double increment) {
		return this.executeAndRetry(jedis -> {
			return jedis.incrByFloat(key, increment);
		});
	}

	public java.lang.Long expireAt(byte[] key, long unixTime) {
		return this.executeAndRetry(jedis -> {
			return jedis.expireAt(key, unixTime);
		});
	}

	public java.lang.Long incrBy(byte[] key, long increment) {
		return this.executeAndRetry(jedis -> {
			return jedis.incrBy(key, increment);
		});
	}

	public java.lang.Long incr(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.incr(key);
		});
	}

	public java.lang.Long expire(byte[] key, int seconds) {
		return this.executeAndRetry(jedis -> {
			return jedis.expire(key, seconds);
		});
	}

	public long pfcount(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.pfcount(key);
		});
	}

	public java.util.List<redis.clients.jedis.GeoCoordinate> geopos(byte[] key, byte[]... members) {
		return this.executeAndRetry(jedis -> {
			return jedis.geopos(key, members);
		});
	}

	public java.lang.Long zremrangeByRank(byte[] key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.zremrangeByRank(key, start, stop);
		});
	}

	public redis.clients.jedis.ScanResult<java.util.Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
		return this.executeAndRetry(jedis -> {
			return jedis.hscan(key, cursor);
		});
	}

	public redis.clients.jedis.ScanResult<java.util.Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor,
			redis.clients.jedis.ScanParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.hscan(key, cursor, params);
		});
	}

	public java.lang.Boolean sismember(byte[] key, byte[] member) {
		return this.executeAndRetry(jedis -> {
			return jedis.sismember(key, member);
		});
	}

	public java.lang.Long strlen(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.strlen(key);
		});
	}

	public java.util.Set<byte[]> zrevrange(byte[] key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrange(key, start, stop);
		});
	}

	public java.lang.Long move(byte[] key, int dbIndex) {
		return this.executeAndRetry(jedis -> {
			return jedis.move(key, dbIndex);
		});
	}

	public java.lang.Long zrevrank(byte[] key, byte[] member) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrank(key, member);
		});
	}

	public java.util.Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByLex(key, min, max);
		});
	}

	public java.util.Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByLex(key, min, max, offset, count);
		});
	}

	public java.lang.Long rpushx(byte[] key, byte[]... string) {
		return this.executeAndRetry(jedis -> {
			return jedis.rpushx(key, string);
		});
	}

	public java.util.List<java.lang.Long> bitfield(byte[] key, byte[]... arguments) {
		return this.executeAndRetry(jedis -> {
			return jedis.bitfield(key, arguments);
		});
	}

	public java.util.Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByLex(key, max, min, offset, count);
		});
	}

	public java.util.Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByLex(key, max, min);
		});
	}

	public java.lang.Long del(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.del(key);
		});
	}

	public java.lang.Long linsert(byte[] key, redis.clients.jedis.ListPosition where, byte[] pivot, byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.linsert(key, where, pivot, value);
		});
	}

	@SuppressWarnings("deprecation")
	public java.lang.Long linsert(byte[] key, redis.clients.jedis.BinaryClient.LIST_POSITION where, byte[] pivot,
			byte[] value) {
		return this.executeAndRetry(jedis -> {
			return jedis.linsert(key, where, pivot, value);
		});
	}

	public java.lang.Double zincrby(byte[] key, double increment, byte[] member,
			redis.clients.jedis.params.sortedset.ZIncrByParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.zincrby(key, increment, member, params);
		});
	}

	public java.lang.Double zincrby(byte[] key, double increment, byte[] member) {
		return this.executeAndRetry(jedis -> {
			return jedis.zincrby(key, increment, member);
		});
	}

	public java.util.Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScore(key, min, max, offset, count);
		});
	}

	public java.util.Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScore(key, min, max);
		});
	}

	public java.util.Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScore(key, min, max);
		});
	}

	public java.util.Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScore(key, min, max, offset, count);
		});
	}

	public java.lang.Long zadd(byte[] key, double score, byte[] member) {
		return this.executeAndRetry(jedis -> {
			return jedis.zadd(key, score, member);
		});
	}

	public java.lang.Long zadd(byte[] key, double score, byte[] member,
			redis.clients.jedis.params.sortedset.ZAddParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.zadd(key, score, member, params);
		});
	}

	public java.lang.Long zadd(byte[] key, java.util.Map<byte[], java.lang.Double> scoreMembers) {
		return this.executeAndRetry(jedis -> {
			return jedis.zadd(key, scoreMembers);
		});
	}

	public java.lang.Long zadd(byte[] key, java.util.Map<byte[], java.lang.Double> scoreMembers,
			redis.clients.jedis.params.sortedset.ZAddParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.zadd(key, scoreMembers, params);
		});
	}

	public java.lang.Long zrem(byte[] key, byte[]... members) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrem(key, members);
		});
	}

	public java.lang.Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
		return this.executeAndRetry(jedis -> {
			return jedis.geoadd(key, longitude, latitude, member);
		});
	}

	public java.lang.Long geoadd(byte[] key,
			java.util.Map<byte[], redis.clients.jedis.GeoCoordinate> memberCoordinateMap) {
		return this.executeAndRetry(jedis -> {
			return jedis.geoadd(key, memberCoordinateMap);
		});
	}

	public java.util.Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScore(key, max, min);
		});
	}

	public java.util.Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScore(key, max, min, offset, count);
		});
	}

	public java.util.Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScore(key, max, min, offset, count);
		});
	}

	public java.util.Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScore(key, max, min);
		});
	}

	public byte[] srandmember(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.srandmember(key);
		});
	}

	public java.util.List<byte[]> srandmember(byte[] key, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.srandmember(key, count);
		});
	}

	public java.lang.Long zrank(byte[] key, byte[] member) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrank(key, member);
		});
	}

	public java.lang.Long scard(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.scard(key);
		});
	}

	public java.lang.Long zcount(byte[] key, double min, double max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zcount(key, min, max);
		});
	}

	public java.lang.Long zcount(byte[] key, byte[] min, byte[] max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zcount(key, min, max);
		});
	}

	public java.lang.Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zremrangeByLex(key, min, max);
		});
	}

	public java.util.Set<byte[]> zrange(byte[] key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrange(key, start, stop);
		});
	}

	public java.lang.Long lpushx(byte[] key, byte[]... string) {
		return this.executeAndRetry(jedis -> {
			return jedis.lpushx(key, string);
		});
	}

	@SuppressWarnings("deprecation")
	public java.util.List<byte[]> blpop(byte[] arg) {
		return this.executeAndRetry(jedis -> {
			return jedis.blpop(arg);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(byte[] key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadius(key, longitude, latitude, radius, unit);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(byte[] key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadius(key, longitude, latitude, radius, unit, param);
		});
	}

	public redis.clients.jedis.ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
		return this.executeAndRetry(jedis -> {
			return jedis.sscan(key, cursor);
		});
	}

	public redis.clients.jedis.ScanResult<byte[]> sscan(byte[] key, byte[] cursor,
			redis.clients.jedis.ScanParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.sscan(key, cursor, params);
		});
	}

	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(byte[] key, byte[] cursor,
			redis.clients.jedis.ScanParams params) {
		return this.executeAndRetry(jedis -> {
			return jedis.zscan(key, cursor, params);
		});
	}

	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(byte[] key, byte[] cursor) {
		return this.executeAndRetry(jedis -> {
			return jedis.zscan(key, cursor);
		});
	}

	public java.lang.Long zcard(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.zcard(key);
		});
	}

	public java.util.List<byte[]> geohash(byte[] key, byte[]... members) {
		return this.executeAndRetry(jedis -> {
			return jedis.geohash(key, members);
		});
	}

	public java.lang.Long zlexcount(byte[] key, byte[] min, byte[] max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zlexcount(key, min, max);
		});
	}

	public java.lang.Long hstrlen(byte[] key, byte[] field) {
		return this.executeAndRetry(jedis -> {
			return jedis.hstrlen(key, field);
		});
	}

	public java.lang.Long zremrangeByScore(byte[] key, byte[] min, byte[] max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zremrangeByScore(key, min, max);
		});
	}

	public java.lang.Long zremrangeByScore(byte[] key, double min, double max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zremrangeByScore(key, min, max);
		});
	}

	public java.lang.Long bitcount(byte[] key) {
		return this.executeAndRetry(jedis -> {
			return jedis.bitcount(key);
		});
	}

	public java.lang.Long bitcount(byte[] key, long start, long end) {
		return this.executeAndRetry(jedis -> {
			return jedis.bitcount(key, start, end);
		});
	}

	public java.lang.Double geodist(byte[] key, byte[] member1, byte[] member2) {
		return this.executeAndRetry(jedis -> {
			return jedis.geodist(key, member1, member2);
		});
	}

	public java.lang.Double geodist(byte[] key, byte[] member1, byte[] member2, redis.clients.jedis.GeoUnit unit) {
		return this.executeAndRetry(jedis -> {
			return jedis.geodist(key, member1, member2, unit);
		});
	}

	public java.lang.Long pfadd(byte[] key, byte[]... elements) {
		return this.executeAndRetry(jedis -> {
			return jedis.pfadd(key, elements);
		});
	}

	public java.lang.Double zscore(byte[] key, byte[] member) {
		return this.executeAndRetry(jedis -> {
			return jedis.zscore(key, member);
		});
	}

	public byte[] echo(byte[] string) {
		return this.executeAndRetry(jedis -> {
			return jedis.echo(string);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrangeWithScores(byte[] key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeWithScores(key, start, stop);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeWithScores(byte[] key, long start, long stop) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeWithScores(key, start, stop);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member,
			double radius, redis.clients.jedis.GeoUnit unit, redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadiusByMemberReadonly(key, member, radius, unit, param);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member,
			double radius, redis.clients.jedis.GeoUnit unit) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadiusByMemberReadonly(key, member, radius, unit);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadiusReadonly(key, longitude, latitude, radius, unit, param);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadiusReadonly(key, longitude, latitude, radius, unit);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min,
			int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScoreWithScores(key, max, min);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min,
			int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrevrangeByScoreWithScores(key, max, min);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max,
			int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(byte[] key, double min, double max,
			int offset, int count) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScoreWithScores(key, min, max);
		});
	}

	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
		return this.executeAndRetry(jedis -> {
			return jedis.zrangeByScoreWithScores(key, min, max);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member,
			double radius, redis.clients.jedis.GeoUnit unit) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadiusByMember(key, member, radius, unit);
		});
	}

	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member,
			double radius, redis.clients.jedis.GeoUnit unit, redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return this.executeAndRetry(jedis -> {
			return jedis.georadiusByMember(key, member, radius, unit, param);
		});
	}

}
