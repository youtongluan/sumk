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

import java.util.function.Function;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * 这个工具类只支持jedis-2.x，对redis服务端的版本号不做要求
 */
public class RedisUtil {
	/**
	 * 这个方法只在redis cluster中使用。
	 * 
	 * @param r
	 *            redis对象，通过RedisPool.get(XX)获取
	 * @return JedisCluster对象
	 */
	public static JedisCluster getCluster(Redis r) {
		return Redis2Cluster.class.cast(r);
	}

	/**
	 * 本方法只在普通redis或者sentinel模式下使用， 它允许开发者对jedis对象进行批处理等其它redis没有提供的操作
	 * 
	 * @param <T>
	 *            参数类型
	 * @param r
	 *            redis对象，通过RedisPool.get(XX)获取
	 * @param callback
	 *            回调函数
	 * @return 回调函数的返回值
	 */
	public static <T> T execute(Redis r, Function<Jedis, T> callback) {
		Redis2 r2 = (Redis2) r;
		return r2.execAndRetry(callback);
	}
}
