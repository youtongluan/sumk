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
package org.yx.redis.v3;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.JedisPubSub;

public class RedisImpl extends AbstractRedis {

	public RedisImpl(JedisExecutor executor) {
		super(executor);
	}

	@Override
	public String get(String key) {
		return execAndRetry(jedis -> jedis.get(key));
	}

	@Override
	public String type(String key) {
		return execAndRetry(jedis -> jedis.type(key));
	}

	@Override
	public Long append(String key, String value) {
		return execAndRetry(jedis -> jedis.append(key, value));
	}

	@Override
	public Set<String> keys(String pattern) {
		return execAndRetry(jedis -> jedis.keys(pattern));
	}

	@Override
	public String set(String key, String value, String nxxx) {
		return execAndRetry(jedis -> jedis.set(key, value, Redis3Kit.createSetParams(nxxx, null, -1)));
	}

	@Override
	public String set(String key, String value, String nxxx, String expx, long time) {
		return execAndRetry(jedis -> jedis.set(key, value, Redis3Kit.createSetParams(nxxx, expx, time)));
	}

	@Override
	public String set(String key, String value, String expx, long time) {
		return execAndRetry(jedis -> jedis.set(key, value, Redis3Kit.createSetParams(null, expx, time)));
	}

	@Override
	public String set(String key, String value) {
		return execAndRetry(jedis -> jedis.set(key, value));
	}

	@Override
	public Boolean exists(String key) {
		return execAndRetry(jedis -> jedis.exists(key));
	}

	@Override
	public Long exists(String... keys) {
		return execAndRetry(jedis -> jedis.exists(keys));
	}

	@Override
	public String rename(String oldkey, String newkey) {
		return execAndRetry(jedis -> jedis.rename(oldkey, newkey));
	}

	@Override
	public Long sort(String key, String dstkey) {
		return execAndRetry(jedis -> jedis.sort(key, dstkey));
	}

	@Override
	public List<String> sort(String key) {
		return execAndRetry(jedis -> jedis.sort(key));
	}

	@Override
	public Long unlink(String... keys) {
		return execAndRetry(jedis -> jedis.unlink(keys));
	}

	@Override
	public Long unlink(String key) {
		return execAndRetry(jedis -> jedis.unlink(key));
	}

	@Override
	public List<String> brpop(int timeout, String key) {
		return execAndRetry(jedis -> jedis.brpop(timeout, key));
	}

	@Override
	public List<String> brpop(int timeout, String... keys) {
		return execAndRetry(jedis -> jedis.brpop(timeout, keys));
	}

	@Override
	public List<String> brpop(String... args) {
		return execAndRetry(jedis -> jedis.brpop(args));
	}

	@Override
	public byte[] dump(String key) {
		return execAndRetry(jedis -> jedis.dump(key));
	}

	@Override
	public Object eval(String script, List<String> keys, List<String> args) {
		return execAndRetry(jedis -> jedis.eval(script, keys, args));
	}

	@Override
	public Object eval(String script, int keyCount, String... params) {
		return execAndRetry(jedis -> jedis.eval(script, keyCount, params));
	}

	@Override
	public Object eval(String script, String sampleKey) {
		return execAndRetry(jedis -> jedis.eval(script));
	}

	@Override
	public Long sadd(String key, String... members) {
		return execAndRetry(jedis -> jedis.sadd(key, members));
	}

	@Override
	public Double hincrByFloat(String key, String field, double value) {
		return execAndRetry(jedis -> jedis.hincrByFloat(key, field, value));
	}

	@Override
	public List<String> hvals(String key) {
		return execAndRetry(jedis -> jedis.hvals(key));
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		return execAndRetry(jedis -> jedis.hmget(key, fields));
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		return execAndRetry(jedis -> jedis.hgetAll(key));
	}

	@Override
	public Long rpush(String key, String... strings) {
		return execAndRetry(jedis -> jedis.rpush(key, strings));
	}

	@Override
	public Long llen(String key) {
		return execAndRetry(jedis -> jedis.llen(key));
	}

	@Override
	public Long hlen(String key) {
		return execAndRetry(jedis -> jedis.hlen(key));
	}

	@Override
	public String lindex(String key, long index) {
		return execAndRetry(jedis -> jedis.lindex(key, index));
	}

	@Override
	public Long lpush(String key, String... strings) {
		return execAndRetry(jedis -> jedis.lpush(key, strings));
	}

	@Override
	public Long lrem(String key, long count, String value) {
		return execAndRetry(jedis -> jedis.lrem(key, count, value));
	}

	@Override
	public String lpop(String key) {
		return execAndRetry(jedis -> jedis.lpop(key));
	}

	@Override
	public String lset(String key, long index, String value) {
		return execAndRetry(jedis -> jedis.lset(key, index, value));
	}

	@Override
	public String rpop(String key) {
		return execAndRetry(jedis -> jedis.rpop(key));
	}

	@Override
	public Long del(String... keys) {
		return execAndRetry(jedis -> jedis.del(keys));
	}

	@Override
	public Long del(String key) {
		return execAndRetry(jedis -> jedis.del(key));
	}

	@Override
	public String restore(String key, int ttl, byte[] serializedValue) {
		return execAndRetry(jedis -> jedis.restore(key, (long) ttl, serializedValue));
	}

	@Override
	public Long hdel(String key, String... fields) {
		return execAndRetry(jedis -> jedis.hdel(key, fields));
	}

	@Override
	public String getSet(String key, String value) {
		return execAndRetry(jedis -> jedis.getSet(key, value));
	}

	@Override
	public String ltrim(String key, long start, long stop) {
		return execAndRetry(jedis -> jedis.ltrim(key, start, stop));
	}

	@Override
	public String setex(String key, int seconds, String value) {
		return execAndRetry(jedis -> jedis.setex(key, (long) seconds, value));
	}

	@Override
	public List<String> lrange(String key, long start, long stop) {
		return execAndRetry(jedis -> jedis.lrange(key, start, stop));
	}

	@Override
	public Long hsetnx(String key, String field, String value) {
		return execAndRetry(jedis -> jedis.hsetnx(key, field, value));
	}

	@Override
	public String psetex(String key, long milliseconds, String value) {
		return execAndRetry(jedis -> jedis.psetex(key, milliseconds, value));
	}

	@Override
	public Long persist(String key) {
		return execAndRetry(jedis -> jedis.persist(key));
	}

	@Override
	public Set<String> hkeys(String key) {
		return execAndRetry(jedis -> jedis.hkeys(key));
	}

	@Override
	public Long setnx(String key, String value) {
		return execAndRetry(jedis -> jedis.setnx(key, value));
	}

	@Override
	public Long decrBy(String key, long decrement) {
		return execAndRetry(jedis -> jedis.decrBy(key, decrement));
	}

	@Override
	public Long decr(String key) {
		return execAndRetry(jedis -> jedis.decr(key));
	}

	@Override
	public Long hset(String key, String field, String value) {
		return execAndRetry(jedis -> jedis.hset(key, field, value));
	}

	@Override
	public Long hset(String key, Map<String, String> hash) {
		return execAndRetry(jedis -> jedis.hset(key, hash));
	}

	@Override
	public String hget(String key, String field) {
		return execAndRetry(jedis -> jedis.hget(key, field));
	}

	@Override
	public String hmset(String key, Map<String, String> hash) {
		return execAndRetry(jedis -> jedis.hmset(key, hash));
	}

	@Override
	public Boolean hexists(String key, String field) {
		return execAndRetry(jedis -> jedis.hexists(key, field));
	}

	@Override
	public Long incrBy(String key, long increment) {
		return execAndRetry(jedis -> jedis.incrBy(key, increment));
	}

	@Override
	public Double incrByFloat(String key, double increment) {
		return execAndRetry(jedis -> jedis.incrByFloat(key, increment));
	}

	@Override
	public Long incr(String key) {
		return execAndRetry(jedis -> jedis.incr(key));
	}

	@Override
	public Long expire(String key, int seconds) {
		return execAndRetry(jedis -> jedis.expire(key, (long) seconds));
	}

	@Override
	public Long pexpire(String key, long milliseconds) {
		return execAndRetry(jedis -> jedis.pexpire(key, milliseconds));
	}

	@Override
	public String substr(String key, int start, int end) {
		return execAndRetry(jedis -> jedis.substr(key, start, end));
	}

	@Override
	public Long hincrBy(String key, String field, long value) {
		return execAndRetry(jedis -> jedis.hincrBy(key, field, value));
	}

	@Override
	public Long ttl(String key) {
		return execAndRetry(jedis -> jedis.ttl(key));
	}

	@Override
	public Long pttl(String key) {
		return execAndRetry(jedis -> jedis.pttl(key));
	}

	@Override
	public Long touch(String key) {
		return execAndRetry(jedis -> jedis.touch(key));
	}

	@Override
	public Long touch(String... keys) {
		return execAndRetry(jedis -> jedis.touch(keys));
	}

	@Override
	public Boolean setbit(String key, long offset, boolean value) {
		return execAndRetry(jedis -> jedis.setbit(key, offset, value));
	}

	@Override
	public Long expireAt(String key, long unixTime) {
		return execAndRetry(jedis -> jedis.expireAt(key, unixTime));
	}

	@Override
	public Long pexpireAt(String key, long millisecondsTimestamp) {
		return execAndRetry(jedis -> jedis.pexpireAt(key, millisecondsTimestamp));
	}

	@Override
	public Boolean getbit(String key, long offset) {
		return execAndRetry(jedis -> jedis.getbit(key, offset));
	}

	@Override
	public Long setrange(String key, long offset, String value) {
		return execAndRetry(jedis -> jedis.setrange(key, offset, value));
	}

	@Override
	public String getrange(String key, long startOffset, long endOffset) {
		return execAndRetry(jedis -> jedis.getrange(key, startOffset, endOffset));
	}

	@Override
	public Long msetnx(String... keysvalues) {
		return execAndRetry(jedis -> jedis.msetnx(keysvalues));
	}

	@Override
	public Long zrevrank(String key, String member) {
		return execAndRetry(jedis -> jedis.zrevrank(key, member));
	}

	@Override
	public Double zincrby(String key, double increment, String member) {
		return execAndRetry(jedis -> jedis.zincrby(key, increment, member));
	}

	@Override
	public Long zrank(String key, String member) {
		return execAndRetry(jedis -> jedis.zrank(key, member));
	}

	@Override
	public Long rpushx(String key, String... string) {
		return execAndRetry(jedis -> jedis.rpushx(key, string));
	}

	@Override
	public List<String> srandmember(String key, int count) {
		return execAndRetry(jedis -> jedis.srandmember(key, count));
	}

	@Override
	public String srandmember(String key) {
		return execAndRetry(jedis -> jedis.srandmember(key));
	}

	@Override
	public Long pfadd(String key, String... elements) {
		return execAndRetry(jedis -> jedis.pfadd(key, elements));
	}

	@Override
	public long pfcount(String... keys) {
		return execAndRetry(jedis -> jedis.pfcount(keys));
	}

	@Override
	public long pfcount(String key) {
		return execAndRetry(jedis -> jedis.pfcount(key));
	}

	@Override
	public Double geodist(String key, String member1, String member2) {
		return execAndRetry(jedis -> jedis.geodist(key, member1, member2));
	}

	@Override
	public Long zrem(String key, String... members) {
		return execAndRetry(jedis -> jedis.zrem(key, members));
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max) {
		return execAndRetry(jedis -> jedis.zrangeByScore(key, min, max));
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max) {
		return execAndRetry(jedis -> jedis.zrangeByScore(key, min, max));
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		return execAndRetry(jedis -> jedis.zrangeByScore(key, min, max, offset, count));
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
		return execAndRetry(jedis -> jedis.zrangeByScore(key, min, max, offset, count));
	}

	@Override
	public List<String> geohash(String key, String... members) {
		return execAndRetry(jedis -> jedis.geohash(key, members));
	}

	@Override
	public List<String> mget(String... keys) {
		return execAndRetry(jedis -> jedis.mget(keys));
	}

	@Override
	public Long zcard(String key) {
		return execAndRetry(jedis -> jedis.zcard(key));
	}

	@Override
	public String rpoplpush(String srckey, String dstkey) {
		return execAndRetry(jedis -> jedis.rpoplpush(srckey, dstkey));
	}

	@Override
	public Long smove(String srckey, String dstkey, String member) {
		return execAndRetry(jedis -> jedis.smove(srckey, dstkey, member));
	}

	@Override
	public Object evalsha(String sha1, int keyCount, String... params) {
		return execAndRetry(jedis -> jedis.evalsha(sha1, keyCount, params));
	}

	@Override
	public Object evalsha(String sha1, String sampleKey) {
		return execAndRetry(jedis -> jedis.evalsha(sha1));
	}

	@Override
	public Object evalsha(String sha1, List<String> keys, List<String> args) {
		return execAndRetry(jedis -> jedis.evalsha(sha1, keys, args));
	}

	@Override
	public List<Long> bitfield(String key, String... arguments) {
		return execAndRetry(jedis -> jedis.bitfield(key, arguments));
	}

	@Override
	public Long sunionstore(String dstkey, String... keys) {
		return execAndRetry(jedis -> jedis.sunionstore(dstkey, keys));
	}

	@Override
	public Long lpushx(String key, String... string) {
		return execAndRetry(jedis -> jedis.lpushx(key, string));
	}

	@Override
	public String echo(String string) {
		return execAndRetry(jedis -> jedis.echo(string));
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		return execAndRetry(jedis -> jedis.zrevrangeByScore(key, max, min));
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		return execAndRetry(jedis -> jedis.zrevrangeByScore(key, max, min, offset, count));
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
		return execAndRetry(jedis -> jedis.zrevrangeByScore(key, max, min, offset, count));
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min) {
		return execAndRetry(jedis -> jedis.zrevrangeByScore(key, max, min));
	}

	@Override
	public Long srem(String key, String... members) {
		return execAndRetry(jedis -> jedis.srem(key, members));
	}

	@Override
	public Long zcount(String key, String min, String max) {
		return execAndRetry(jedis -> jedis.zcount(key, min, max));
	}

	@Override
	public Long zcount(String key, double min, double max) {
		return execAndRetry(jedis -> jedis.zcount(key, min, max));
	}

	@Override
	public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
		return execAndRetry(jedis -> jedis.zrangeByLex(key, min, max, offset, count));
	}

	@Override
	public Set<String> zrangeByLex(String key, String min, String max) {
		return execAndRetry(jedis -> jedis.zrangeByLex(key, min, max));
	}

	@Override
	public Set<String> sdiff(String... keys) {
		return execAndRetry(jedis -> jedis.sdiff(keys));
	}

	@Override
	public Set<String> sunion(String... keys) {
		return execAndRetry(jedis -> jedis.sunion(keys));
	}

	@Override
	public Long scard(String key) {
		return execAndRetry(jedis -> jedis.scard(key));
	}

	@Override
	public Long renamenx(String oldkey, String newkey) {
		return execAndRetry(jedis -> jedis.renamenx(oldkey, newkey));
	}

	@Override
	public Long zadd(String key, double score, String member) {
		return execAndRetry(jedis -> jedis.zadd(key, score, member));
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		return execAndRetry(jedis -> jedis.zadd(key, scoreMembers));
	}

	@Override
	public Double zscore(String key, String member) {
		return execAndRetry(jedis -> jedis.zscore(key, member));
	}

	@Override
	public Long zinterstore(String dstkey, String... sets) {
		return execAndRetry(jedis -> jedis.zinterstore(dstkey, sets));
	}

	@Override
	public Long zunionstore(String dstkey, String... sets) {
		return execAndRetry(jedis -> jedis.zunionstore(dstkey, sets));
	}

	@Override
	public Long sinterstore(String dstkey, String... keys) {
		return execAndRetry(jedis -> jedis.sinterstore(dstkey, keys));
	}

	@Override
	public String randomKey() {
		return execAndRetry(jedis -> jedis.randomKey());
	}

	@Override
	public Long strlen(String key) {
		return execAndRetry(jedis -> jedis.strlen(key));
	}

	@Override
	public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
		return execAndRetry(jedis -> jedis.zrevrangeByLex(key, max, min, offset, count));
	}

	@Override
	public Set<String> zrevrangeByLex(String key, String max, String min) {
		return execAndRetry(jedis -> jedis.zrevrangeByLex(key, max, min));
	}

	@Override
	public String pfmerge(String destkey, String... sourcekeys) {
		return execAndRetry(jedis -> jedis.pfmerge(destkey, sourcekeys));
	}

	@Override
	public Boolean sismember(String key, String member) {
		return execAndRetry(jedis -> jedis.sismember(key, member));
	}

	@Override
	public Set<String> zrevrange(String key, long start, long stop) {
		return execAndRetry(jedis -> jedis.zrevrange(key, start, stop));
	}

	@Override
	public String watch(String... keys) {
		return execAndRetry(jedis -> jedis.watch(keys));
	}

	@Override
	public Long publish(String channel, String message) {
		return execAndRetry(jedis -> jedis.publish(channel, message));
	}

	@Override
	public Long bitcount(String key) {
		return execAndRetry(jedis -> jedis.bitcount(key));
	}

	@Override
	public Long bitcount(String key, long start, long end) {
		return execAndRetry(jedis -> jedis.bitcount(key, start, end));
	}

	@Override
	public Long zremrangeByLex(String key, String min, String max) {
		return execAndRetry(jedis -> jedis.zremrangeByLex(key, min, max));
	}

	@Override
	public Long sdiffstore(String dstkey, String... keys) {
		return execAndRetry(jedis -> jedis.sdiffstore(dstkey, keys));
	}

	@Override
	public Long move(String key, int dbIndex) {
		return execAndRetry(jedis -> jedis.move(key, dbIndex));
	}

	@Override
	public Long geoadd(String key, double longitude, double latitude, String member) {
		return execAndRetry(jedis -> jedis.geoadd(key, longitude, latitude, member));
	}

	@Override
	public Set<String> smembers(String key) {
		return execAndRetry(jedis -> jedis.smembers(key));
	}

	@Override
	public Set<String> zrange(String key, long start, long stop) {
		return execAndRetry(jedis -> jedis.zrange(key, start, stop));
	}

	@Override
	public Long zlexcount(String key, String min, String max) {
		return execAndRetry(jedis -> jedis.zlexcount(key, min, max));
	}

	@Override
	public Long hstrlen(String key, String field) {
		return execAndRetry(jedis -> jedis.hstrlen(key, field));
	}

	@Override
	public Long zremrangeByScore(String key, double min, double max) {
		return execAndRetry(jedis -> jedis.zremrangeByScore(key, min, max));
	}

	@Override
	public Long zremrangeByScore(String key, String min, String max) {
		return execAndRetry(jedis -> jedis.zremrangeByScore(key, min, max));
	}

	@Override
	public String brpoplpush(String source, String destination, int timeout) {
		return execAndRetry(jedis -> jedis.brpoplpush(source, destination, timeout));
	}

	@Override
	public Long zremrangeByRank(String key, long start, long stop) {
		return execAndRetry(jedis -> jedis.zremrangeByRank(key, start, stop));
	}

	@Override
	public Set<String> sinter(String... keys) {
		return execAndRetry(jedis -> jedis.sinter(keys));
	}

	@Override
	public String scriptLoad(String script) {
		return execAndRetry(jedis -> jedis.scriptLoad(script));
	}

	@Override
	public Set<String> spop(String key, long count) {
		return execAndRetry(jedis -> jedis.spop(key, count));
	}

	@Override
	public String spop(String key) {
		return execAndRetry(jedis -> jedis.spop(key));
	}

	@Override
	public List<String> blpop(int timeout, String... keys) {
		return execAndRetry(jedis -> jedis.blpop(timeout, keys));
	}

	@Override
	public List<String> blpop(int timeout, String key) {
		return execAndRetry(jedis -> jedis.blpop(timeout, key));
	}

	@Override
	public List<String> blpop(String... args) {
		return execAndRetry(jedis -> jedis.blpop(args));
	}

	@Override
	public String mset(String... keysvalues) {
		return execAndRetry(jedis -> jedis.mset(keysvalues));
	}

	@Override
	public byte[] get(byte[] key) {
		return execAndRetry(jedis -> jedis.get(key));
	}

	@Override
	public String type(byte[] key) {
		return execAndRetry(jedis -> jedis.type(key));
	}

	@Override
	public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
		return execAndRetry(
				jedis -> jedis.set(key, value, Redis3Kit.createSetParams(new String(nxxx, StandardCharsets.UTF_8),
						new String(expx, StandardCharsets.UTF_8), time)));
	}

	@Override
	public String set(byte[] key, byte[] value) {
		return execAndRetry(jedis -> jedis.set(key, value));
	}

	@Override
	public Boolean exists(byte[] key) {
		return execAndRetry(jedis -> jedis.exists(key));
	}

	@Override
	public List<byte[]> hvals(byte[] key) {
		return execAndRetry(jedis -> jedis.hvals(key));
	}

	@Override
	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		return execAndRetry(jedis -> jedis.hmget(key, fields));
	}

	@Override
	public Map<byte[], byte[]> hgetAll(byte[] key) {
		return execAndRetry(jedis -> jedis.hgetAll(key));
	}

	@Override
	public Long rpush(byte[] key, byte[]... strings) {
		return execAndRetry(jedis -> jedis.rpush(key, strings));
	}

	@Override
	public Long llen(byte[] key) {
		return execAndRetry(jedis -> jedis.llen(key));
	}

	@Override
	public byte[] lindex(byte[] key, long index) {
		return execAndRetry(jedis -> jedis.lindex(key, index));
	}

	@Override
	public Long lpush(byte[] key, byte[]... strings) {
		return execAndRetry(jedis -> jedis.lpush(key, strings));
	}

	@Override
	public Long lrem(byte[] key, long count, byte[] value) {
		return execAndRetry(jedis -> jedis.lrem(key, count, value));
	}

	@Override
	public byte[] lpop(byte[] key) {
		return execAndRetry(jedis -> jedis.lpop(key));
	}

	@Override
	public String lset(byte[] key, long index, byte[] value) {
		return execAndRetry(jedis -> jedis.lset(key, index, value));
	}

	@Override
	public byte[] rpop(byte[] key) {
		return execAndRetry(jedis -> jedis.rpop(key));
	}

	@Override
	public Long del(byte[] key) {
		return execAndRetry(jedis -> jedis.del(key));
	}

	@Override
	public Long hdel(byte[] key, byte[]... fields) {
		return execAndRetry(jedis -> jedis.hdel(key, fields));
	}

	@Override
	public String ltrim(byte[] key, long start, long stop) {
		return execAndRetry(jedis -> jedis.ltrim(key, start, stop));
	}

	@Override
	public List<byte[]> lrange(byte[] key, long start, long stop) {
		return execAndRetry(jedis -> jedis.lrange(key, start, stop));
	}

	@Override
	public Long hsetnx(byte[] key, byte[] field, byte[] value) {
		return execAndRetry(jedis -> jedis.hsetnx(key, field, value));
	}

	@Override
	public Set<byte[]> hkeys(byte[] key) {
		return execAndRetry(jedis -> jedis.hkeys(key));
	}

	@Override
	public Long hset(byte[] key, Map<byte[], byte[]> hash) {
		return execAndRetry(jedis -> jedis.hset(key, hash));
	}

	@Override
	public Long hset(byte[] key, byte[] field, byte[] value) {
		return execAndRetry(jedis -> jedis.hset(key, field, value));
	}

	@Override
	public byte[] hget(byte[] key, byte[] field) {
		return execAndRetry(jedis -> jedis.hget(key, field));
	}

	@Override
	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		return execAndRetry(jedis -> jedis.hmset(key, hash));
	}

	@Override
	public Boolean hexists(byte[] key, byte[] field) {
		return execAndRetry(jedis -> jedis.hexists(key, field));
	}

	@Override
	public Long pexpire(byte[] key, long milliseconds) {
		return execAndRetry(jedis -> jedis.pexpire(key, milliseconds));
	}

	@Override
	public Long pttl(byte[] key) {
		return execAndRetry(jedis -> jedis.pttl(key));
	}

	@Override
	public Long setrange(byte[] key, long offset, byte[] value) {
		return execAndRetry(jedis -> jedis.setrange(key, offset, value));
	}

	@Override
	public byte[] getrange(byte[] key, long startOffset, long endOffset) {
		return execAndRetry(jedis -> jedis.getrange(key, startOffset, endOffset));
	}

	@Override
	public String unwatch() {
		return execAndRetry(jedis -> jedis.unwatch());
	}

	@Override
	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		execAndRetry(jedis -> {
			jedis.subscribe(jedisPubSub, channels);
			return "OK";
		});
	}

	@Override
	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		execAndRetry(jedis -> {
			jedis.psubscribe(jedisPubSub, patterns);
			return "OK";
		});
	}

}
