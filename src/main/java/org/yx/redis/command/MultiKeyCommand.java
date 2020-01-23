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
import java.util.Set;

public interface MultiKeyCommand {
	Long del(String... keys);

	Long unlink(String... keys);

	Long exists(String... keys);

	List<String> blpop(int timeout, String... keys);

	List<String> brpop(int timeout, String... keys);

	List<String> blpop(String... args);

	List<String> brpop(String... args);

	Set<String> keys(String pattern);

	List<String> mget(String... keys);

	String mset(String... keysvalues);

	Long msetnx(String... keysvalues);

	String rename(String oldkey, String newkey);

	Long renamenx(String oldkey, String newkey);

	String rpoplpush(String srckey, String dstkey);

	Set<String> sdiff(String... keys);

	Long sdiffstore(String dstkey, String... keys);

	Set<String> sinter(String... keys);

	Long sinterstore(String dstkey, String... keys);

	Long smove(String srckey, String dstkey, String member);

	Long sort(String key, String dstkey);

	Set<String> sunion(String... keys);

	Long sunionstore(String dstkey, String... keys);

	String watch(String... keys);

	String unwatch();

	Long zinterstore(String dstkey, String... sets);

	Long zunionstore(String dstkey, String... sets);

	String brpoplpush(String source, String destination, int timeout);

	Long publish(String channel, String message);

	String randomKey();

	String pfmerge(String destkey, String... sourcekeys);

	long pfcount(String... keys);

	Long touch(String... keys);
}
