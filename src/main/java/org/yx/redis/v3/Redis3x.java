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

import java.util.function.Function;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * jedis2.x 额外增加的方法
 */
public interface Redis3x {
	/**
	 * 执行redis的批量操作或jedis的原生操作。<BR>
	 * 注意:<B>如果是集群情况下，要保证所有的操作确实在key所对应的节点上才行，所以这个方法在集群环境里要慎重使用</B>
	 * 
	 * @param <T>
	 *            返回值类型
	 * @param sampleKey
	 *            只有在集群情况下才有意义，用于寻找真正的jedis节点
	 * @param callback
	 *            批处理的代码
	 * @return 返回的是callback的返回值
	 */
	<T> T execute(String sampleKey, Function<Jedis, T> callback);

	Long publish(String channel, String message);

	void subscribe(JedisPubSub jedisPubSub, String... channels);

	void psubscribe(final JedisPubSub jedisPubSub, final String... patterns);
}
