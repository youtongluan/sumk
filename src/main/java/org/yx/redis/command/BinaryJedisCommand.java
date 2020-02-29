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
package org.yx.redis.command;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BinaryJedisCommand {
	String set(byte[] key, byte[] value);

	String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time);

	byte[] get(byte[] key);

	Boolean exists(byte[] key);

	String type(byte[] key);

	Long expire(byte[] key, int seconds);

	Long pexpire(byte[] key, long milliseconds);

	Long pttl(byte[] key);

	Long touch(byte[] key);

	Boolean setbit(byte[] key, long offset, boolean value);

	Boolean setbit(byte[] key, long offset, byte[] value);

	Boolean getbit(byte[] key, long offset);

	Long setrange(byte[] key, long offset, byte[] value);

	byte[] getrange(byte[] key, long startOffset, long endOffset);

	Long hset(byte[] key, byte[] field, byte[] value);

	Long hset(byte[] key, Map<byte[], byte[]> hash);

	byte[] hget(byte[] key, byte[] field);

	Long hsetnx(byte[] key, byte[] field, byte[] value);

	String hmset(byte[] key, Map<byte[], byte[]> hash);

	List<byte[]> hmget(byte[] key, byte[]... fields);

	Boolean hexists(byte[] key, byte[] field);

	Long hdel(byte[] key, byte[]... field);

	Long hlen(byte[] key);

	Set<byte[]> hkeys(byte[] key);

	List<byte[]> hvals(byte[] key);

	Map<byte[], byte[]> hgetAll(byte[] key);

	Long rpush(byte[] key, byte[]... args);

	Long lpush(byte[] key, byte[]... args);

	Long llen(byte[] key);

	List<byte[]> lrange(byte[] key, long start, long stop);

	String ltrim(byte[] key, long start, long stop);

	byte[] lindex(byte[] key, long index);

	String lset(byte[] key, long index, byte[] value);

	Long lrem(byte[] key, long count, byte[] value);

	byte[] lpop(byte[] key);

	byte[] rpop(byte[] key);

	Long del(byte[] key);
}
