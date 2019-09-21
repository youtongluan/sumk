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

	@Override
	public java.lang.String get(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.get(key));
	}

	@Override
	public java.lang.String type(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.type(key));
	}

	@Override
	public java.lang.Long append(java.lang.String key, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.append(key, value));
	}

	@Override
	public java.util.Set<java.lang.String> keys(java.lang.String pattern) {
		return executeAndRetry(jedis -> jedis.keys(pattern));
	}

	@Override
	public java.lang.String set(java.lang.String key, java.lang.String value, java.lang.String nxxx) {
		return executeAndRetry(jedis -> jedis.set(key, value, nxxx));
	}

	@Override
	public java.lang.String set(java.lang.String key, java.lang.String value, java.lang.String expx, long time) {
		return executeAndRetry(jedis -> jedis.set(key, value, expx, time));
	}

	@Override
	public java.lang.String set(java.lang.String key, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.set(key, value));
	}

	@Override
	public java.lang.String set(java.lang.String key, java.lang.String value, java.lang.String nxxx,
			java.lang.String expx, long time) {
		return executeAndRetry(jedis -> jedis.set(key, value, nxxx, expx, time));
	}

	@Override
	public java.lang.Boolean exists(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.exists(key));
	}

	@Override
	public java.lang.Long exists(java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.exists(keys));
	}

	@Override
	public java.lang.String rename(java.lang.String oldkey, java.lang.String newkey) {
		return executeAndRetry(jedis -> jedis.rename(oldkey, newkey));
	}

	@Override
	public java.lang.Long sort(java.lang.String key, java.lang.String dstkey) {
		return executeAndRetry(jedis -> jedis.sort(key, dstkey));
	}

	@Override
	public java.util.List<java.lang.String> sort(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.sort(key));
	}

	@Override
	public java.util.List<java.lang.String> sort(java.lang.String key,
			redis.clients.jedis.SortingParams sortingParameters) {
		return executeAndRetry(jedis -> jedis.sort(key, sortingParameters));
	}

	@Override
	public java.lang.Long sort(java.lang.String key, redis.clients.jedis.SortingParams sortingParameters,
			java.lang.String dstkey) {
		return executeAndRetry(jedis -> jedis.sort(key, sortingParameters, dstkey));
	}

	@Override
	public java.lang.Long unlink(java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.unlink(keys));
	}

	@Override
	public java.lang.Long unlink(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.unlink(key));
	}

	@Override
	public redis.clients.jedis.ScanResult<java.lang.String> scan(java.lang.String cursor,
			redis.clients.jedis.ScanParams params) {
		return executeAndRetry(jedis -> jedis.scan(cursor, params));
	}

	@Override
	public redis.clients.jedis.ScanResult<java.lang.String> scan(java.lang.String cursor) {
		return executeAndRetry(jedis -> jedis.scan(cursor));
	}

	@SuppressWarnings("deprecation")
	@Override
	public redis.clients.jedis.ScanResult<java.lang.String> scan(int cursor) {
		return executeAndRetry(jedis -> jedis.scan(cursor));
	}

	@Override
	public java.util.List<java.lang.String> brpop(int timeout, java.lang.String key) {
		return executeAndRetry(jedis -> jedis.brpop(timeout, key));
	}

	@Override
	public java.util.List<java.lang.String> brpop(int timeout, java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.brpop(timeout, keys));
	}

	@SuppressWarnings("deprecation")
	@Override
	public java.util.List<java.lang.String> brpop(java.lang.String arg) {
		return executeAndRetry(jedis -> jedis.brpop(arg));
	}

	@Override
	public java.util.List<java.lang.String> brpop(java.lang.String... args) {
		return executeAndRetry(jedis -> jedis.brpop(args));
	}

	@Override
	public byte[] dump(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.dump(key));
	}

	@Override
	public java.lang.Object eval(java.lang.String script, java.util.List<java.lang.String> keys,
			java.util.List<java.lang.String> args) {
		return executeAndRetry(jedis -> jedis.eval(script, keys, args));
	}

	@Override
	public java.lang.Object eval(java.lang.String script, int keyCount, java.lang.String... params) {
		return executeAndRetry(jedis -> jedis.eval(script, keyCount, params));
	}

	@Override
	public java.lang.Object eval(java.lang.String script) {
		return executeAndRetry(jedis -> jedis.eval(script));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMemberReadonly(java.lang.String key,
			java.lang.String member, double radius, redis.clients.jedis.GeoUnit unit) {
		return executeAndRetry(jedis -> jedis.georadiusByMemberReadonly(key, member, radius, unit));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMemberReadonly(java.lang.String key,
			java.lang.String member, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return executeAndRetry(jedis -> jedis.georadiusByMemberReadonly(key, member, radius, unit, param));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(java.lang.String key,
			java.lang.String member, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return executeAndRetry(jedis -> jedis.georadiusByMember(key, member, radius, unit, param));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(java.lang.String key,
			java.lang.String member, double radius, redis.clients.jedis.GeoUnit unit) {
		return executeAndRetry(jedis -> jedis.georadiusByMember(key, member, radius, unit));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key, java.lang.String min,
			java.lang.String max) {
		return executeAndRetry(jedis -> jedis.zrangeByScoreWithScores(key, min, max));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key, double min,
			double max, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrangeByScoreWithScores(key, min, max, offset, count));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key, java.lang.String min,
			java.lang.String max, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrangeByScoreWithScores(key, min, max, offset, count));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key, double min,
			double max) {
		return executeAndRetry(jedis -> jedis.zrangeByScoreWithScores(key, min, max));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeWithScores(java.lang.String key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.zrevrangeWithScores(key, start, stop));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key,
			java.lang.String max, java.lang.String min) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScoreWithScores(key, max, min));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key, double max,
			double min) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScoreWithScores(key, max, min));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key, double max,
			double min, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScoreWithScores(key, max, min, offset, count));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key,
			java.lang.String max, java.lang.String min, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScoreWithScores(key, max, min, offset, count));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusReadonly(java.lang.String key,
			double longitude, double latitude, double radius, redis.clients.jedis.GeoUnit unit) {
		return executeAndRetry(jedis -> jedis.georadiusReadonly(key, longitude, latitude, radius, unit));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusReadonly(java.lang.String key,
			double longitude, double latitude, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return executeAndRetry(jedis -> jedis.georadiusReadonly(key, longitude, latitude, radius, unit, param));
	}

	@Override
	public java.lang.Long hlen(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.hlen(key));
	}

	@Override
	public java.util.List<java.lang.String> hvals(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.hvals(key));
	}

	@Override
	public java.lang.Boolean setbit(java.lang.String key, long offset, boolean value) {
		return executeAndRetry(jedis -> jedis.setbit(key, offset, value));
	}

	@Override
	public java.lang.Boolean setbit(java.lang.String key, long offset, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.setbit(key, offset, value));
	}

	@Override
	public java.util.Map<java.lang.String, java.lang.String> hgetAll(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.hgetAll(key));
	}

	@Override
	public java.lang.Long expire(java.lang.String key, int seconds) {
		return executeAndRetry(jedis -> jedis.expire(key, seconds));
	}

	@Override
	public java.lang.Long rpush(java.lang.String key, java.lang.String... strings) {
		return executeAndRetry(jedis -> jedis.rpush(key, strings));
	}

	@Override
	public java.lang.Long ttl(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.ttl(key));
	}

	@Override
	public java.lang.Long lpush(java.lang.String key, java.lang.String... strings) {
		return executeAndRetry(jedis -> jedis.lpush(key, strings));
	}

	@Override
	public java.lang.Long pttl(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.pttl(key));
	}

	@Override
	public java.lang.String setex(java.lang.String key, int seconds, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.setex(key, seconds, value));
	}

	@Override
	public java.lang.Long decr(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.decr(key));
	}

	@Override
	public java.lang.Long pexpireAt(java.lang.String key, long millisecondsTimestamp) {
		return executeAndRetry(jedis -> jedis.pexpireAt(key, millisecondsTimestamp));
	}

	@Override
	public java.lang.Long setnx(java.lang.String key, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.setnx(key, value));
	}

	@Override
	public java.lang.Long expireAt(java.lang.String key, long unixTime) {
		return executeAndRetry(jedis -> jedis.expireAt(key, unixTime));
	}

	@Override
	public java.lang.Long incr(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.incr(key));
	}

	@Override
	public java.lang.Long touch(java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.touch(keys));
	}

	@Override
	public java.lang.Long touch(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.touch(key));
	}

	@Override
	public java.lang.String psetex(java.lang.String key, long milliseconds, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.psetex(key, milliseconds, value));
	}

	@Override
	public java.lang.Long decrBy(java.lang.String key, long decrement) {
		return executeAndRetry(jedis -> jedis.decrBy(key, decrement));
	}

	@Override
	public java.lang.String restore(java.lang.String key, int ttl, byte[] serializedValue) {
		return executeAndRetry(jedis -> jedis.restore(key, ttl, serializedValue));
	}

	@Override
	public java.lang.Double incrByFloat(java.lang.String key, double increment) {
		return executeAndRetry(jedis -> jedis.incrByFloat(key, increment));
	}

	@Override
	public java.lang.Long hset(java.lang.String key, java.lang.String field, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.hset(key, field, value));
	}

	@Override
	public java.lang.Long hset(java.lang.String key, java.util.Map<java.lang.String, java.lang.String> hash) {
		return executeAndRetry(jedis -> jedis.hset(key, hash));
	}

	@Override
	public java.lang.String hget(java.lang.String key, java.lang.String field) {
		return executeAndRetry(jedis -> jedis.hget(key, field));
	}

	@Override
	public java.lang.Double hincrByFloat(java.lang.String key, java.lang.String field, double value) {
		return executeAndRetry(jedis -> jedis.hincrByFloat(key, field, value));
	}

	@Override
	public java.lang.Boolean hexists(java.lang.String key, java.lang.String field) {
		return executeAndRetry(jedis -> jedis.hexists(key, field));
	}

	@Override
	public java.util.Set<java.lang.String> hkeys(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.hkeys(key));
	}

	@Override
	public java.lang.String getSet(java.lang.String key, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.getSet(key, value));
	}

	@Override
	public java.lang.Long setrange(java.lang.String key, long offset, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.setrange(key, offset, value));
	}

	@Override
	public java.lang.Long hdel(java.lang.String key, java.lang.String... fields) {
		return executeAndRetry(jedis -> jedis.hdel(key, fields));
	}

	@Override
	public java.lang.Long pexpire(java.lang.String key, long milliseconds) {
		return executeAndRetry(jedis -> jedis.pexpire(key, milliseconds));
	}

	@Override
	public java.lang.Boolean getbit(java.lang.String key, long offset) {
		return executeAndRetry(jedis -> jedis.getbit(key, offset));
	}

	@Override
	public java.lang.Long persist(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.persist(key));
	}

	@Override
	public java.lang.Long incrBy(java.lang.String key, long increment) {
		return executeAndRetry(jedis -> jedis.incrBy(key, increment));
	}

	@Override
	public java.lang.String substr(java.lang.String key, int start, int end) {
		return executeAndRetry(jedis -> jedis.substr(key, start, end));
	}

	@Override
	public java.util.List<java.lang.String> hmget(java.lang.String key, java.lang.String... fields) {
		return executeAndRetry(jedis -> jedis.hmget(key, fields));
	}

	@Override
	public java.lang.Long hsetnx(java.lang.String key, java.lang.String field, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.hsetnx(key, field, value));
	}

	@Override
	public java.lang.String getrange(java.lang.String key, long startOffset, long endOffset) {
		return executeAndRetry(jedis -> jedis.getrange(key, startOffset, endOffset));
	}

	@Override
	public java.lang.String hmset(java.lang.String key, java.util.Map<java.lang.String, java.lang.String> hash) {
		return executeAndRetry(jedis -> jedis.hmset(key, hash));
	}

	@Override
	public java.lang.Long hincrBy(java.lang.String key, java.lang.String field, long value) {
		return executeAndRetry(jedis -> jedis.hincrBy(key, field, value));
	}

	@Override
	public java.lang.Boolean sismember(java.lang.String key, java.lang.String member) {
		return executeAndRetry(jedis -> jedis.sismember(key, member));
	}

	@Override
	public java.lang.String srandmember(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.srandmember(key));
	}

	@Override
	public java.util.List<java.lang.String> srandmember(java.lang.String key, int count) {
		return executeAndRetry(jedis -> jedis.srandmember(key, count));
	}

	@Override
	public java.lang.Long scard(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.scard(key));
	}

	@Override
	public java.util.Set<java.lang.String> smembers(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.smembers(key));
	}

	@Override
	public java.lang.Long strlen(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.strlen(key));
	}

	@Override
	public java.lang.String lindex(java.lang.String key, long index) {
		return executeAndRetry(jedis -> jedis.lindex(key, index));
	}

	@Override
	public java.lang.Long zadd(java.lang.String key, java.util.Map<java.lang.String, java.lang.Double> scoreMembers,
			redis.clients.jedis.params.sortedset.ZAddParams params) {
		return executeAndRetry(jedis -> jedis.zadd(key, scoreMembers, params));
	}

	@Override
	public java.lang.Long zadd(java.lang.String key, double score, java.lang.String member) {
		return executeAndRetry(jedis -> jedis.zadd(key, score, member));
	}

	@Override
	public java.lang.Long zadd(java.lang.String key, double score, java.lang.String member,
			redis.clients.jedis.params.sortedset.ZAddParams params) {
		return executeAndRetry(jedis -> jedis.zadd(key, score, member, params));
	}

	@Override
	public java.lang.Long zadd(java.lang.String key, java.util.Map<java.lang.String, java.lang.Double> scoreMembers) {
		return executeAndRetry(jedis -> jedis.zadd(key, scoreMembers));
	}

	@Override
	public java.util.Set<java.lang.String> zrange(java.lang.String key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.zrange(key, start, stop));
	}

	@Override
	public java.lang.String rpop(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.rpop(key));
	}

	@Override
	public java.lang.String ltrim(java.lang.String key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.ltrim(key, start, stop));
	}

	@Override
	public java.util.List<java.lang.String> lrange(java.lang.String key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.lrange(key, start, stop));
	}

	@Override
	public java.lang.String lpop(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.lpop(key));
	}

	@Override
	public java.lang.String lset(java.lang.String key, long index, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.lset(key, index, value));
	}

	@Override
	public java.lang.Long sadd(java.lang.String key, java.lang.String... members) {
		return executeAndRetry(jedis -> jedis.sadd(key, members));
	}

	@Override
	public java.lang.Long srem(java.lang.String key, java.lang.String... members) {
		return executeAndRetry(jedis -> jedis.srem(key, members));
	}

	@Override
	public java.lang.Long lrem(java.lang.String key, long count, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.lrem(key, count, value));
	}

	@Override
	public java.lang.Long llen(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.llen(key));
	}

	@Override
	public java.util.Set<java.lang.String> spop(java.lang.String key, long count) {
		return executeAndRetry(jedis -> jedis.spop(key, count));
	}

	@Override
	public java.lang.String spop(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.spop(key));
	}

	@SuppressWarnings("deprecation")
	@Override
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<java.lang.String, java.lang.String>> hscan(
			java.lang.String key, int cursor) {
		return executeAndRetry(jedis -> jedis.hscan(key, cursor));
	}

	@Override
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<java.lang.String, java.lang.String>> hscan(
			java.lang.String key, java.lang.String cursor, redis.clients.jedis.ScanParams params) {
		return executeAndRetry(jedis -> jedis.hscan(key, cursor, params));
	}

	@Override
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<java.lang.String, java.lang.String>> hscan(
			java.lang.String key, java.lang.String cursor) {
		return executeAndRetry(jedis -> jedis.hscan(key, cursor));
	}

	@Override
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(java.lang.String key,
			java.lang.String cursor) {
		return executeAndRetry(jedis -> jedis.zscan(key, cursor));
	}

	@SuppressWarnings("deprecation")
	@Override
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(java.lang.String key, int cursor) {
		return executeAndRetry(jedis -> jedis.zscan(key, cursor));
	}

	@Override
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(java.lang.String key,
			java.lang.String cursor, redis.clients.jedis.ScanParams params) {
		return executeAndRetry(jedis -> jedis.zscan(key, cursor, params));
	}

	@Override
	public java.lang.Double zscore(java.lang.String key, java.lang.String member) {
		return executeAndRetry(jedis -> jedis.zscore(key, member));
	}

	@Override
	public java.lang.Long zremrangeByRank(java.lang.String key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.zremrangeByRank(key, start, stop));
	}

	@Override
	public java.lang.Long lpushx(java.lang.String key, java.lang.String... string) {
		return executeAndRetry(jedis -> jedis.lpushx(key, string));
	}

	@Override
	public java.lang.Long rpushx(java.lang.String key, java.lang.String... string) {
		return executeAndRetry(jedis -> jedis.rpushx(key, string));
	}

	@Override
	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key, java.lang.String min,
			java.lang.String max, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrangeByScore(key, min, max, offset, count));
	}

	@Override
	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key, double min, double max) {
		return executeAndRetry(jedis -> jedis.zrangeByScore(key, min, max));
	}

	@Override
	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key, java.lang.String min,
			java.lang.String max) {
		return executeAndRetry(jedis -> jedis.zrangeByScore(key, min, max));
	}

	@Override
	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key, double min, double max, int offset,
			int count) {
		return executeAndRetry(jedis -> jedis.zrangeByScore(key, min, max, offset, count));
	}

	@Override
	public java.lang.Long move(java.lang.String key, int dbIndex) {
		return executeAndRetry(jedis -> jedis.move(key, dbIndex));
	}

	@Override
	public long pfcount(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.pfcount(key));
	}

	@Override
	public long pfcount(java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.pfcount(keys));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(java.lang.String key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit) {
		return executeAndRetry(jedis -> jedis.georadius(key, longitude, latitude, radius, unit));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(java.lang.String key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return executeAndRetry(jedis -> jedis.georadius(key, longitude, latitude, radius, unit, param));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeWithScores(java.lang.String key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.zrangeWithScores(key, start, stop));
	}

	@Override
	public java.lang.Long zcard(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.zcard(key));
	}

	@Override
	public java.util.Set<java.lang.String> zrevrange(java.lang.String key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.zrevrange(key, start, stop));
	}

	@Override
	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key, java.lang.String max,
			java.lang.String min, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScore(key, max, min, offset, count));
	}

	@Override
	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key, double max, double min, int offset,
			int count) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScore(key, max, min, offset, count));
	}

	@Override
	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key, java.lang.String max,
			java.lang.String min) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScore(key, max, min));
	}

	@Override
	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key, double max, double min) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScore(key, max, min));
	}

	@Override
	public java.lang.Long zremrangeByScore(java.lang.String key, java.lang.String min, java.lang.String max) {
		return executeAndRetry(jedis -> jedis.zremrangeByScore(key, min, max));
	}

	@Override
	public java.lang.Long zremrangeByScore(java.lang.String key, double min, double max) {
		return executeAndRetry(jedis -> jedis.zremrangeByScore(key, min, max));
	}

	@Override
	public java.lang.String echo(java.lang.String string) {
		return executeAndRetry(jedis -> jedis.echo(string));
	}

	@Override
	public redis.clients.jedis.ScanResult<java.lang.String> sscan(java.lang.String key, java.lang.String cursor) {
		return executeAndRetry(jedis -> jedis.sscan(key, cursor));
	}

	@Override
	public redis.clients.jedis.ScanResult<java.lang.String> sscan(java.lang.String key, java.lang.String cursor,
			redis.clients.jedis.ScanParams params) {
		return executeAndRetry(jedis -> jedis.sscan(key, cursor, params));
	}

	@SuppressWarnings("deprecation")
	@Override
	public redis.clients.jedis.ScanResult<java.lang.String> sscan(java.lang.String key, int cursor) {
		return executeAndRetry(jedis -> jedis.sscan(key, cursor));
	}

	@Override
	public java.util.List<java.lang.Long> bitfield(java.lang.String key, java.lang.String... arguments) {
		return executeAndRetry(jedis -> jedis.bitfield(key, arguments));
	}

	@Override
	public java.lang.Long hstrlen(java.lang.String key, java.lang.String field) {
		return executeAndRetry(jedis -> jedis.hstrlen(key, field));
	}

	@Override
	public java.lang.String mset(java.lang.String... keysvalues) {
		return executeAndRetry(jedis -> jedis.mset(keysvalues));
	}

	@Override
	public java.lang.Double geodist(java.lang.String key, java.lang.String member1, java.lang.String member2,
			redis.clients.jedis.GeoUnit unit) {
		return executeAndRetry(jedis -> jedis.geodist(key, member1, member2, unit));
	}

	@Override
	public java.lang.Double geodist(java.lang.String key, java.lang.String member1, java.lang.String member2) {
		return executeAndRetry(jedis -> jedis.geodist(key, member1, member2));
	}

	@Override
	public java.lang.Long renamenx(java.lang.String oldkey, java.lang.String newkey) {
		return executeAndRetry(jedis -> jedis.renamenx(oldkey, newkey));
	}

	@SuppressWarnings("deprecation")
	@Override
	public java.lang.Long linsert(java.lang.String key, redis.clients.jedis.BinaryClient.LIST_POSITION where,
			java.lang.String pivot, java.lang.String value) {
		return executeAndRetry(jedis -> jedis.linsert(key, where, pivot, value));
	}

	@Override
	public java.lang.Long linsert(java.lang.String key, redis.clients.jedis.ListPosition where, java.lang.String pivot,
			java.lang.String value) {
		return executeAndRetry(jedis -> jedis.linsert(key, where, pivot, value));
	}

	@Override
	public java.lang.String rpoplpush(java.lang.String srckey, java.lang.String dstkey) {
		return executeAndRetry(jedis -> jedis.rpoplpush(srckey, dstkey));
	}

	@Override
	public java.util.List<java.lang.String> geohash(java.lang.String key, java.lang.String... members) {
		return executeAndRetry(jedis -> jedis.geohash(key, members));
	}

	@Override
	public java.util.Set<java.lang.String> sinter(java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.sinter(keys));
	}

	@Override
	public java.lang.Long zremrangeByLex(java.lang.String key, java.lang.String min, java.lang.String max) {
		return executeAndRetry(jedis -> jedis.zremrangeByLex(key, min, max));
	}

	@Override
	public java.lang.Long zlexcount(java.lang.String key, java.lang.String min, java.lang.String max) {
		return executeAndRetry(jedis -> jedis.zlexcount(key, min, max));
	}

	@Override
	public java.lang.Long bitpos(java.lang.String key, boolean value) {
		return executeAndRetry(jedis -> jedis.bitpos(key, value));
	}

	@Override
	public java.lang.Long bitpos(java.lang.String key, boolean value, redis.clients.jedis.BitPosParams params) {
		return executeAndRetry(jedis -> jedis.bitpos(key, value, params));
	}

	@Override
	public java.lang.Long sinterstore(java.lang.String dstkey, java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.sinterstore(dstkey, keys));
	}

	@Override
	public java.lang.Long smove(java.lang.String srckey, java.lang.String dstkey, java.lang.String member) {
		return executeAndRetry(jedis -> jedis.smove(srckey, dstkey, member));
	}

	@Override
	public java.util.Set<java.lang.String> sunion(java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.sunion(keys));
	}

	@Override
	public java.lang.Long del(java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.del(keys));
	}

	@Override
	public java.lang.Long del(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.del(key));
	}

	@Override
	public java.util.List<java.lang.String> mget(java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.mget(keys));
	}

	@Override
	public java.lang.Double zincrby(java.lang.String key, double increment, java.lang.String member,
			redis.clients.jedis.params.sortedset.ZIncrByParams params) {
		return executeAndRetry(jedis -> jedis.zincrby(key, increment, member, params));
	}

	@Override
	public java.lang.Double zincrby(java.lang.String key, double increment, java.lang.String member) {
		return executeAndRetry(jedis -> jedis.zincrby(key, increment, member));
	}

	@Override
	public java.lang.Long bitcount(java.lang.String key, long start, long end) {
		return executeAndRetry(jedis -> jedis.bitcount(key, start, end));
	}

	@Override
	public java.lang.Long bitcount(java.lang.String key) {
		return executeAndRetry(jedis -> jedis.bitcount(key));
	}

	@Override
	public java.lang.Long geoadd(java.lang.String key, double longitude, double latitude, java.lang.String member) {
		return executeAndRetry(jedis -> jedis.geoadd(key, longitude, latitude, member));
	}

	@Override
	public java.lang.Long geoadd(java.lang.String key,
			java.util.Map<java.lang.String, redis.clients.jedis.GeoCoordinate> memberCoordinateMap) {
		return executeAndRetry(jedis -> jedis.geoadd(key, memberCoordinateMap));
	}

	@Override
	public java.lang.Long sdiffstore(java.lang.String dstkey, java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.sdiffstore(dstkey, keys));
	}

	@Override
	public java.lang.String watch(java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.watch(keys));
	}

	@Override
	public java.lang.Long sunionstore(java.lang.String dstkey, java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.sunionstore(dstkey, keys));
	}

	@Override
	public java.lang.Long pfadd(java.lang.String key, java.lang.String... elements) {
		return executeAndRetry(jedis -> jedis.pfadd(key, elements));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoCoordinate> geopos(java.lang.String key, java.lang.String... members) {
		return executeAndRetry(jedis -> jedis.geopos(key, members));
	}

	@Override
	public java.lang.Long zrank(java.lang.String key, java.lang.String member) {
		return executeAndRetry(jedis -> jedis.zrank(key, member));
	}

	@Override
	public java.lang.Long zinterstore(java.lang.String dstkey, redis.clients.jedis.ZParams params,
			java.lang.String... sets) {
		return executeAndRetry(jedis -> jedis.zinterstore(dstkey, params, sets));
	}

	@Override
	public java.lang.Long zinterstore(java.lang.String dstkey, java.lang.String... sets) {
		return executeAndRetry(jedis -> jedis.zinterstore(dstkey, sets));
	}

	@Override
	public java.lang.Long publish(java.lang.String channel, java.lang.String message) {
		return executeAndRetry(jedis -> jedis.publish(channel, message));
	}

	@Override
	public java.util.Set<java.lang.String> zrevrangeByLex(java.lang.String key, java.lang.String max,
			java.lang.String min) {
		return executeAndRetry(jedis -> jedis.zrevrangeByLex(key, max, min));
	}

	@Override
	public java.util.Set<java.lang.String> zrevrangeByLex(java.lang.String key, java.lang.String max,
			java.lang.String min, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrevrangeByLex(key, max, min, offset, count));
	}

	@Override
	public java.lang.String randomKey() {
		return executeAndRetry(jedis -> jedis.randomKey());
	}

	@Override
	public java.lang.Long bitop(redis.clients.jedis.BitOP op, java.lang.String destKey, java.lang.String... srcKeys) {
		return executeAndRetry(jedis -> jedis.bitop(op, destKey, srcKeys));
	}

	@Override
	public java.util.Set<java.lang.String> sdiff(java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.sdiff(keys));
	}

	@Override
	public java.lang.Long zrem(java.lang.String key, java.lang.String... members) {
		return executeAndRetry(jedis -> jedis.zrem(key, members));
	}

	@Override
	public java.util.List<java.lang.String> blpop(int timeout, java.lang.String... keys) {
		return executeAndRetry(jedis -> jedis.blpop(timeout, keys));
	}

	@SuppressWarnings("deprecation")
	@Override
	public java.util.List<java.lang.String> blpop(java.lang.String arg) {
		return executeAndRetry(jedis -> jedis.blpop(arg));
	}

	@Override
	public java.util.List<java.lang.String> blpop(int timeout, java.lang.String key) {
		return executeAndRetry(jedis -> jedis.blpop(timeout, key));
	}

	@Override
	public java.util.List<java.lang.String> blpop(java.lang.String... args) {
		return executeAndRetry(jedis -> jedis.blpop(args));
	}

	@Override
	public java.lang.Long msetnx(java.lang.String... keysvalues) {
		return executeAndRetry(jedis -> jedis.msetnx(keysvalues));
	}

	@Override
	public java.lang.String brpoplpush(java.lang.String source, java.lang.String destination, int timeout) {
		return executeAndRetry(jedis -> jedis.brpoplpush(source, destination, timeout));
	}

	@Override
	public java.util.Set<java.lang.String> zrangeByLex(java.lang.String key, java.lang.String min, java.lang.String max,
			int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrangeByLex(key, min, max, offset, count));
	}

	@Override
	public java.util.Set<java.lang.String> zrangeByLex(java.lang.String key, java.lang.String min,
			java.lang.String max) {
		return executeAndRetry(jedis -> jedis.zrangeByLex(key, min, max));
	}

	@Override
	public java.lang.Long zrevrank(java.lang.String key, java.lang.String member) {
		return executeAndRetry(jedis -> jedis.zrevrank(key, member));
	}

	@Override
	public java.lang.Long zunionstore(java.lang.String dstkey, java.lang.String... sets) {
		return executeAndRetry(jedis -> jedis.zunionstore(dstkey, sets));
	}

	@Override
	public java.lang.Long zunionstore(java.lang.String dstkey, redis.clients.jedis.ZParams params,
			java.lang.String... sets) {
		return executeAndRetry(jedis -> jedis.zunionstore(dstkey, params, sets));
	}

	@Override
	public java.lang.Long zcount(java.lang.String key, java.lang.String min, java.lang.String max) {
		return executeAndRetry(jedis -> jedis.zcount(key, min, max));
	}

	@Override
	public java.lang.Long zcount(java.lang.String key, double min, double max) {
		return executeAndRetry(jedis -> jedis.zcount(key, min, max));
	}

	@Override
	public java.lang.String pfmerge(java.lang.String destkey, java.lang.String... sourcekeys) {
		return executeAndRetry(jedis -> jedis.pfmerge(destkey, sourcekeys));
	}

	@Override
	public void psubscribe(redis.clients.jedis.JedisPubSub jedisPubSub, java.lang.String... patterns) {
		this.executeAndRetry(jedis -> {
			jedis.psubscribe(jedisPubSub, patterns);
			return null;
		});
	}

	@Override
	public void subscribe(redis.clients.jedis.JedisPubSub jedisPubSub, java.lang.String... channels) {
		this.executeAndRetry(jedis -> {
			jedis.subscribe(jedisPubSub, channels);
			return null;
		});
	}

	@Override
	public java.lang.Object evalsha(java.lang.String sha1, int keyCount, java.lang.String... params) {
		return executeAndRetry(jedis -> jedis.evalsha(sha1, keyCount, params));
	}

	@Override
	public java.lang.Object evalsha(java.lang.String sha1) {
		return executeAndRetry(jedis -> jedis.evalsha(sha1));
	}

	@Override
	public java.lang.Object evalsha(java.lang.String sha1, java.util.List<java.lang.String> keys,
			java.util.List<java.lang.String> args) {
		return executeAndRetry(jedis -> jedis.evalsha(sha1, keys, args));
	}

	@Override
	public java.lang.String scriptLoad(java.lang.String script) {
		return executeAndRetry(jedis -> jedis.scriptLoad(script));
	}

	@Override
	public java.lang.Boolean scriptExists(java.lang.String sha1) {
		return executeAndRetry(jedis -> jedis.scriptExists(sha1));
	}

	@Override
	public java.util.List<java.lang.Boolean> scriptExists(java.lang.String... sha1) {
		return executeAndRetry(jedis -> jedis.scriptExists(sha1));
	}

	@Override
	public byte[] get(byte[] key) {
		return executeAndRetry(jedis -> jedis.get(key));
	}

	@Override
	public java.lang.String type(byte[] key) {
		return executeAndRetry(jedis -> jedis.type(key));
	}

	@Override
	public java.lang.Long append(byte[] key, byte[] value) {
		return executeAndRetry(jedis -> jedis.append(key, value));
	}

	@Override
	public java.lang.String set(byte[] key, byte[] value) {
		return executeAndRetry(jedis -> jedis.set(key, value));
	}

	@Override
	public java.lang.String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
		return executeAndRetry(jedis -> jedis.set(key, value, nxxx, expx, time));
	}

	@Override
	public java.lang.String set(byte[] key, byte[] value, byte[] nxxx) {
		return executeAndRetry(jedis -> jedis.set(key, value, nxxx));
	}

	@Override
	public java.lang.Boolean exists(byte[] key) {
		return executeAndRetry(jedis -> jedis.exists(key));
	}

	@Override
	public java.util.List<byte[]> sort(byte[] key, redis.clients.jedis.SortingParams sortingParameters) {
		return executeAndRetry(jedis -> jedis.sort(key, sortingParameters));
	}

	@Override
	public java.util.List<byte[]> sort(byte[] key) {
		return executeAndRetry(jedis -> jedis.sort(key));
	}

	@Override
	public java.lang.Long unlink(byte[] key) {
		return executeAndRetry(jedis -> jedis.unlink(key));
	}

	@SuppressWarnings("deprecation")
	@Override
	public java.util.List<byte[]> brpop(byte[] arg) {
		return executeAndRetry(jedis -> jedis.brpop(arg));
	}

	@Override
	public byte[] dump(byte[] key) {
		return executeAndRetry(jedis -> jedis.dump(key));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member,
			double radius, redis.clients.jedis.GeoUnit unit, redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return executeAndRetry(jedis -> jedis.georadiusByMemberReadonly(key, member, radius, unit, param));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member,
			double radius, redis.clients.jedis.GeoUnit unit) {
		return executeAndRetry(jedis -> jedis.georadiusByMemberReadonly(key, member, radius, unit));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member,
			double radius, redis.clients.jedis.GeoUnit unit) {
		return executeAndRetry(jedis -> jedis.georadiusByMember(key, member, radius, unit));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member,
			double radius, redis.clients.jedis.GeoUnit unit, redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return executeAndRetry(jedis -> jedis.georadiusByMember(key, member, radius, unit, param));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		return executeAndRetry(jedis -> jedis.zrangeByScoreWithScores(key, min, max));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max,
			int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrangeByScoreWithScores(key, min, max, offset, count));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(byte[] key, double min, double max,
			int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrangeByScoreWithScores(key, min, max, offset, count));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
		return executeAndRetry(jedis -> jedis.zrangeByScoreWithScores(key, min, max));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeWithScores(byte[] key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.zrevrangeWithScores(key, start, stop));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min,
			int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScoreWithScores(key, max, min, offset, count));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScoreWithScores(key, max, min));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min,
			int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScoreWithScores(key, max, min, offset, count));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScoreWithScores(key, max, min));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit) {
		return executeAndRetry(jedis -> jedis.georadiusReadonly(key, longitude, latitude, radius, unit));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return executeAndRetry(jedis -> jedis.georadiusReadonly(key, longitude, latitude, radius, unit, param));
	}

	@Override
	public java.lang.Long hlen(byte[] key) {
		return executeAndRetry(jedis -> jedis.hlen(key));
	}

	@Override
	public java.util.List<byte[]> hvals(byte[] key) {
		return executeAndRetry(jedis -> jedis.hvals(key));
	}

	@Override
	public java.lang.Boolean setbit(byte[] key, long offset, byte[] value) {
		return executeAndRetry(jedis -> jedis.setbit(key, offset, value));
	}

	@Override
	public java.lang.Boolean setbit(byte[] key, long offset, boolean value) {
		return executeAndRetry(jedis -> jedis.setbit(key, offset, value));
	}

	@Override
	public java.util.Map<byte[], byte[]> hgetAll(byte[] key) {
		return executeAndRetry(jedis -> jedis.hgetAll(key));
	}

	@Override
	public java.lang.Long expire(byte[] key, int seconds) {
		return executeAndRetry(jedis -> jedis.expire(key, seconds));
	}

	@Override
	public java.lang.Long rpush(byte[] key, byte[]... strings) {
		return executeAndRetry(jedis -> jedis.rpush(key, strings));
	}

	@Override
	public java.lang.Long ttl(byte[] key) {
		return executeAndRetry(jedis -> jedis.ttl(key));
	}

	@Override
	public java.lang.Long lpush(byte[] key, byte[]... strings) {
		return executeAndRetry(jedis -> jedis.lpush(key, strings));
	}

	@Override
	public java.lang.Long pttl(byte[] key) {
		return executeAndRetry(jedis -> jedis.pttl(key));
	}

	@Override
	public java.lang.String setex(byte[] key, int seconds, byte[] value) {
		return executeAndRetry(jedis -> jedis.setex(key, seconds, value));
	}

	@Override
	public java.lang.Long decr(byte[] key) {
		return executeAndRetry(jedis -> jedis.decr(key));
	}

	@Override
	public java.lang.Long pexpireAt(byte[] key, long millisecondsTimestamp) {
		return executeAndRetry(jedis -> jedis.pexpireAt(key, millisecondsTimestamp));
	}

	@Override
	public java.lang.Long setnx(byte[] key, byte[] value) {
		return executeAndRetry(jedis -> jedis.setnx(key, value));
	}

	@Override
	public java.lang.Long expireAt(byte[] key, long unixTime) {
		return executeAndRetry(jedis -> jedis.expireAt(key, unixTime));
	}

	@Override
	public java.lang.Long incr(byte[] key) {
		return executeAndRetry(jedis -> jedis.incr(key));
	}

	@Override
	public java.lang.Long touch(byte[] key) {
		return executeAndRetry(jedis -> jedis.touch(key));
	}

	@Override
	public java.lang.String psetex(byte[] key, long milliseconds, byte[] value) {
		return executeAndRetry(jedis -> jedis.psetex(key, milliseconds, value));
	}

	@Override
	public java.lang.Long decrBy(byte[] key, long decrement) {
		return executeAndRetry(jedis -> jedis.decrBy(key, decrement));
	}

	@Override
	public java.lang.String restore(byte[] key, int ttl, byte[] serializedValue) {
		return executeAndRetry(jedis -> jedis.restore(key, ttl, serializedValue));
	}

	@Override
	public java.lang.Double incrByFloat(byte[] key, double increment) {
		return executeAndRetry(jedis -> jedis.incrByFloat(key, increment));
	}

	@Override
	public java.lang.Long hset(byte[] key, java.util.Map<byte[], byte[]> hash) {
		return executeAndRetry(jedis -> jedis.hset(key, hash));
	}

	@Override
	public java.lang.Long hset(byte[] key, byte[] field, byte[] value) {
		return executeAndRetry(jedis -> jedis.hset(key, field, value));
	}

	@Override
	public byte[] hget(byte[] key, byte[] field) {
		return executeAndRetry(jedis -> jedis.hget(key, field));
	}

	@Override
	public java.lang.Double hincrByFloat(byte[] key, byte[] field, double value) {
		return executeAndRetry(jedis -> jedis.hincrByFloat(key, field, value));
	}

	@Override
	public java.lang.Boolean hexists(byte[] key, byte[] field) {
		return executeAndRetry(jedis -> jedis.hexists(key, field));
	}

	@Override
	public java.util.Set<byte[]> hkeys(byte[] key) {
		return executeAndRetry(jedis -> jedis.hkeys(key));
	}

	@Override
	public byte[] getSet(byte[] key, byte[] value) {
		return executeAndRetry(jedis -> jedis.getSet(key, value));
	}

	@Override
	public java.lang.String restoreReplace(byte[] key, int ttl, byte[] serializedValue) {
		return executeAndRetry(jedis -> jedis.restoreReplace(key, ttl, serializedValue));
	}

	@Override
	public java.lang.Long setrange(byte[] key, long offset, byte[] value) {
		return executeAndRetry(jedis -> jedis.setrange(key, offset, value));
	}

	@Override
	public java.lang.Long hdel(byte[] key, byte[]... fields) {
		return executeAndRetry(jedis -> jedis.hdel(key, fields));
	}

	@Override
	public java.lang.Long pexpire(byte[] key, long milliseconds) {
		return executeAndRetry(jedis -> jedis.pexpire(key, milliseconds));
	}

	@Override
	public java.lang.Boolean getbit(byte[] key, long offset) {
		return executeAndRetry(jedis -> jedis.getbit(key, offset));
	}

	@Override
	public java.lang.Long persist(byte[] key) {
		return executeAndRetry(jedis -> jedis.persist(key));
	}

	@Override
	public java.lang.Long incrBy(byte[] key, long increment) {
		return executeAndRetry(jedis -> jedis.incrBy(key, increment));
	}

	@Override
	public byte[] substr(byte[] key, int start, int end) {
		return executeAndRetry(jedis -> jedis.substr(key, start, end));
	}

	@Override
	public java.util.List<byte[]> hmget(byte[] key, byte[]... fields) {
		return executeAndRetry(jedis -> jedis.hmget(key, fields));
	}

	@Override
	public java.lang.Long hsetnx(byte[] key, byte[] field, byte[] value) {
		return executeAndRetry(jedis -> jedis.hsetnx(key, field, value));
	}

	@Override
	public byte[] getrange(byte[] key, long startOffset, long endOffset) {
		return executeAndRetry(jedis -> jedis.getrange(key, startOffset, endOffset));
	}

	@Override
	public java.lang.String hmset(byte[] key, java.util.Map<byte[], byte[]> hash) {
		return executeAndRetry(jedis -> jedis.hmset(key, hash));
	}

	@Override
	public java.lang.Long hincrBy(byte[] key, byte[] field, long value) {
		return executeAndRetry(jedis -> jedis.hincrBy(key, field, value));
	}

	@Override
	public java.lang.Boolean sismember(byte[] key, byte[] member) {
		return executeAndRetry(jedis -> jedis.sismember(key, member));
	}

	@Override
	public java.util.List<byte[]> srandmember(byte[] key, int count) {
		return executeAndRetry(jedis -> jedis.srandmember(key, count));
	}

	@Override
	public byte[] srandmember(byte[] key) {
		return executeAndRetry(jedis -> jedis.srandmember(key));
	}

	@Override
	public java.lang.Long scard(byte[] key) {
		return executeAndRetry(jedis -> jedis.scard(key));
	}

	@Override
	public java.util.Set<byte[]> smembers(byte[] key) {
		return executeAndRetry(jedis -> jedis.smembers(key));
	}

	@Override
	public java.lang.Long strlen(byte[] key) {
		return executeAndRetry(jedis -> jedis.strlen(key));
	}

	@Override
	public byte[] lindex(byte[] key, long index) {
		return executeAndRetry(jedis -> jedis.lindex(key, index));
	}

	@Override
	public java.lang.Long zadd(byte[] key, double score, byte[] member) {
		return executeAndRetry(jedis -> jedis.zadd(key, score, member));
	}

	@Override
	public java.lang.Long zadd(byte[] key, java.util.Map<byte[], java.lang.Double> scoreMembers) {
		return executeAndRetry(jedis -> jedis.zadd(key, scoreMembers));
	}

	@Override
	public java.lang.Long zadd(byte[] key, double score, byte[] member,
			redis.clients.jedis.params.sortedset.ZAddParams params) {
		return executeAndRetry(jedis -> jedis.zadd(key, score, member, params));
	}

	@Override
	public java.lang.Long zadd(byte[] key, java.util.Map<byte[], java.lang.Double> scoreMembers,
			redis.clients.jedis.params.sortedset.ZAddParams params) {
		return executeAndRetry(jedis -> jedis.zadd(key, scoreMembers, params));
	}

	@Override
	public java.util.Set<byte[]> zrange(byte[] key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.zrange(key, start, stop));
	}

	@Override
	public byte[] rpop(byte[] key) {
		return executeAndRetry(jedis -> jedis.rpop(key));
	}

	@Override
	public java.lang.String ltrim(byte[] key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.ltrim(key, start, stop));
	}

	@Override
	public java.util.List<byte[]> lrange(byte[] key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.lrange(key, start, stop));
	}

	@Override
	public byte[] lpop(byte[] key) {
		return executeAndRetry(jedis -> jedis.lpop(key));
	}

	@Override
	public java.lang.String lset(byte[] key, long index, byte[] value) {
		return executeAndRetry(jedis -> jedis.lset(key, index, value));
	}

	@Override
	public java.lang.Long sadd(byte[] key, byte[]... members) {
		return executeAndRetry(jedis -> jedis.sadd(key, members));
	}

	@Override
	public java.lang.Long srem(byte[] key, byte[]... member) {
		return executeAndRetry(jedis -> jedis.srem(key, member));
	}

	@Override
	public java.lang.Long lrem(byte[] key, long count, byte[] value) {
		return executeAndRetry(jedis -> jedis.lrem(key, count, value));
	}

	@Override
	public java.lang.Long llen(byte[] key) {
		return executeAndRetry(jedis -> jedis.llen(key));
	}

	@Override
	public byte[] spop(byte[] key) {
		return executeAndRetry(jedis -> jedis.spop(key));
	}

	@Override
	public java.util.Set<byte[]> spop(byte[] key, long count) {
		return executeAndRetry(jedis -> jedis.spop(key, count));
	}

	@Override
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
		return executeAndRetry(jedis -> jedis.hscan(key, cursor));
	}

	@Override
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor,
			redis.clients.jedis.ScanParams params) {
		return executeAndRetry(jedis -> jedis.hscan(key, cursor, params));
	}

	@Override
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(byte[] key, byte[] cursor,
			redis.clients.jedis.ScanParams params) {
		return executeAndRetry(jedis -> jedis.zscan(key, cursor, params));
	}

	@Override
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(byte[] key, byte[] cursor) {
		return executeAndRetry(jedis -> jedis.zscan(key, cursor));
	}

	@Override
	public java.lang.Double zscore(byte[] key, byte[] member) {
		return executeAndRetry(jedis -> jedis.zscore(key, member));
	}

	@Override
	public java.lang.Long zremrangeByRank(byte[] key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.zremrangeByRank(key, start, stop));
	}

	@Override
	public java.lang.Long lpushx(byte[] key, byte[]... string) {
		return executeAndRetry(jedis -> jedis.lpushx(key, string));
	}

	@Override
	public java.lang.Long rpushx(byte[] key, byte[]... string) {
		return executeAndRetry(jedis -> jedis.rpushx(key, string));
	}

	@Override
	public java.util.Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrangeByScore(key, min, max, offset, count));
	}

	@Override
	public java.util.Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		return executeAndRetry(jedis -> jedis.zrangeByScore(key, min, max));
	}

	@Override
	public java.util.Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
		return executeAndRetry(jedis -> jedis.zrangeByScore(key, min, max));
	}

	@Override
	public java.util.Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrangeByScore(key, min, max, offset, count));
	}

	@Override
	public java.lang.Long move(byte[] key, int dbIndex) {
		return executeAndRetry(jedis -> jedis.move(key, dbIndex));
	}

	@Override
	public long pfcount(byte[] key) {
		return executeAndRetry(jedis -> jedis.pfcount(key));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(byte[] key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit) {
		return executeAndRetry(jedis -> jedis.georadius(key, longitude, latitude, radius, unit));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(byte[] key, double longitude,
			double latitude, double radius, redis.clients.jedis.GeoUnit unit,
			redis.clients.jedis.params.geo.GeoRadiusParam param) {
		return executeAndRetry(jedis -> jedis.georadius(key, longitude, latitude, radius, unit, param));
	}

	@Override
	public java.util.Set<redis.clients.jedis.Tuple> zrangeWithScores(byte[] key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.zrangeWithScores(key, start, stop));
	}

	@Override
	public java.lang.Long zcard(byte[] key) {
		return executeAndRetry(jedis -> jedis.zcard(key));
	}

	@Override
	public java.util.Set<byte[]> zrevrange(byte[] key, long start, long stop) {
		return executeAndRetry(jedis -> jedis.zrevrange(key, start, stop));
	}

	@Override
	public java.util.Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScore(key, max, min, offset, count));
	}

	@Override
	public java.util.Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScore(key, max, min));
	}

	@Override
	public java.util.Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScore(key, max, min, offset, count));
	}

	@Override
	public java.util.Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
		return executeAndRetry(jedis -> jedis.zrevrangeByScore(key, max, min));
	}

	@Override
	public java.lang.Long zremrangeByScore(byte[] key, double min, double max) {
		return executeAndRetry(jedis -> jedis.zremrangeByScore(key, min, max));
	}

	@Override
	public java.lang.Long zremrangeByScore(byte[] key, byte[] min, byte[] max) {
		return executeAndRetry(jedis -> jedis.zremrangeByScore(key, min, max));
	}

	@Override
	public byte[] echo(byte[] string) {
		return executeAndRetry(jedis -> jedis.echo(string));
	}

	@Override
	public redis.clients.jedis.ScanResult<byte[]> sscan(byte[] key, byte[] cursor,
			redis.clients.jedis.ScanParams params) {
		return executeAndRetry(jedis -> jedis.sscan(key, cursor, params));
	}

	@Override
	public redis.clients.jedis.ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
		return executeAndRetry(jedis -> jedis.sscan(key, cursor));
	}

	@Override
	public java.util.List<java.lang.Long> bitfield(byte[] key, byte[]... arguments) {
		return executeAndRetry(jedis -> jedis.bitfield(key, arguments));
	}

	@Override
	public java.lang.Long hstrlen(byte[] key, byte[] field) {
		return executeAndRetry(jedis -> jedis.hstrlen(key, field));
	}

	@Override
	public java.lang.Double geodist(byte[] key, byte[] member1, byte[] member2) {
		return executeAndRetry(jedis -> jedis.geodist(key, member1, member2));
	}

	@Override
	public java.lang.Double geodist(byte[] key, byte[] member1, byte[] member2, redis.clients.jedis.GeoUnit unit) {
		return executeAndRetry(jedis -> jedis.geodist(key, member1, member2, unit));
	}

	@SuppressWarnings("deprecation")
	@Override
	public java.lang.Long linsert(byte[] key, redis.clients.jedis.BinaryClient.LIST_POSITION where, byte[] pivot,
			byte[] value) {
		return executeAndRetry(jedis -> jedis.linsert(key, where, pivot, value));
	}

	@Override
	public java.lang.Long linsert(byte[] key, redis.clients.jedis.ListPosition where, byte[] pivot, byte[] value) {
		return executeAndRetry(jedis -> jedis.linsert(key, where, pivot, value));
	}

	@Override
	public java.util.List<byte[]> geohash(byte[] key, byte[]... members) {
		return executeAndRetry(jedis -> jedis.geohash(key, members));
	}

	@Override
	public java.lang.Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
		return executeAndRetry(jedis -> jedis.zremrangeByLex(key, min, max));
	}

	@Override
	public java.lang.Long zlexcount(byte[] key, byte[] min, byte[] max) {
		return executeAndRetry(jedis -> jedis.zlexcount(key, min, max));
	}

	@Override
	public java.lang.Long del(byte[] key) {
		return executeAndRetry(jedis -> jedis.del(key));
	}

	@Override
	public java.lang.Double zincrby(byte[] key, double increment, byte[] member) {
		return executeAndRetry(jedis -> jedis.zincrby(key, increment, member));
	}

	@Override
	public java.lang.Double zincrby(byte[] key, double increment, byte[] member,
			redis.clients.jedis.params.sortedset.ZIncrByParams params) {
		return executeAndRetry(jedis -> jedis.zincrby(key, increment, member, params));
	}

	@Override
	public java.lang.Long bitcount(byte[] key, long start, long end) {
		return executeAndRetry(jedis -> jedis.bitcount(key, start, end));
	}

	@Override
	public java.lang.Long bitcount(byte[] key) {
		return executeAndRetry(jedis -> jedis.bitcount(key));
	}

	@Override
	public java.lang.Long geoadd(byte[] key,
			java.util.Map<byte[], redis.clients.jedis.GeoCoordinate> memberCoordinateMap) {
		return executeAndRetry(jedis -> jedis.geoadd(key, memberCoordinateMap));
	}

	@Override
	public java.lang.Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
		return executeAndRetry(jedis -> jedis.geoadd(key, longitude, latitude, member));
	}

	@Override
	public java.lang.String unwatch() {
		return executeAndRetry(jedis -> jedis.unwatch());
	}

	@Override
	public java.lang.Long pfadd(byte[] key, byte[]... elements) {
		return executeAndRetry(jedis -> jedis.pfadd(key, elements));
	}

	@Override
	public java.util.List<redis.clients.jedis.GeoCoordinate> geopos(byte[] key, byte[]... members) {
		return executeAndRetry(jedis -> jedis.geopos(key, members));
	}

	@Override
	public java.lang.Long zrank(byte[] key, byte[] member) {
		return executeAndRetry(jedis -> jedis.zrank(key, member));
	}

	@Override
	public java.util.Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
		return executeAndRetry(jedis -> jedis.zrevrangeByLex(key, max, min));
	}

	@Override
	public java.util.Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrevrangeByLex(key, max, min, offset, count));
	}

	@Override
	public java.lang.Long zrem(byte[] key, byte[]... members) {
		return executeAndRetry(jedis -> jedis.zrem(key, members));
	}

	@SuppressWarnings("deprecation")
	@Override
	public java.util.List<byte[]> blpop(byte[] arg) {
		return executeAndRetry(jedis -> jedis.blpop(arg));
	}

	@Override
	public java.util.Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
		return executeAndRetry(jedis -> jedis.zrangeByLex(key, min, max, offset, count));
	}

	@Override
	public java.util.Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
		return executeAndRetry(jedis -> jedis.zrangeByLex(key, min, max));
	}

	@Override
	public java.lang.Long zrevrank(byte[] key, byte[] member) {
		return executeAndRetry(jedis -> jedis.zrevrank(key, member));
	}

	@Override
	public java.lang.Long zcount(byte[] key, double min, double max) {
		return executeAndRetry(jedis -> jedis.zcount(key, min, max));
	}

	@Override
	public java.lang.Long zcount(byte[] key, byte[] min, byte[] max) {
		return executeAndRetry(jedis -> jedis.zcount(key, min, max));
	}

}
