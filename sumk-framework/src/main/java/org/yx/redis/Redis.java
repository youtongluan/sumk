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

import org.yx.redis.command.BinaryJedisCommand;
import org.yx.redis.command.JedisCommand;
import org.yx.redis.command.MultiKeyCommand;
import org.yx.redis.command.ScriptingCommand;

public interface Redis extends BinaryJedisCommand, JedisCommand, MultiKeyCommand, ScriptingCommand {

	/**
	 * @return redis的主机地址，如果存在多个，就用逗号分隔
	 */
	String hosts();

	/**
	 * 返回的config只是用来查看，不要去修改它。对它的修改没有意义
	 * 
	 * @return 构造redis对象所使用的配置
	 */
	RedisConfig getRedisConfig();

	void shutdownPool();

	RedisType redisType();

	/**
	 * 如果发生了异常，返回null代替抛出异常。 一般而言它只对普通redis起作用，对于redis集群，该方法不一定有作用。
	 * 通过isMuted()可以判断是否生效
	 * 
	 * @return Redis对象，不为null
	 */
	Redis mute();

	boolean isMuted();
}
