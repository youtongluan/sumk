package org.yx.redis;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.springframework.util.StringUtils;
import org.yx.log.Log;

public class RedisLoader {
	public static void init() throws Exception {
		Properties p = new Properties();
		try {
			InputStream in = ClassLoader.getSystemResourceAsStream("redis.properties");
			if (in == null) {
				return;
			}
			p.load(in);
			Log.get(RedisLoader.class).debug(p);
			Set<Object> keys = p.keySet();
			for (Object o : keys) {
				if (o == null || "".equals(o)) {
					continue;
				}
				String key = (String) o;
				String v = p.getProperty(key);
				v = v.trim();
				if (StringUtils.isEmpty(v)) {
					continue;
				}
				String[] params = v.split("#");
				String ip = params[0];
				RedisParamter param = RedisParamter.create();
				if (ip.contains(":")) {
					String[] addr = ip.split(":");
					ip = addr[0];
					param.setPort(Integer.parseInt(addr[1]));
				}
				if (params.length > 1 && !StringUtils.isEmpty(params[1])) {
					param.setDb(Integer.parseInt(params[1]));
				}
				if (params.length > 2 && !StringUtils.isEmpty(params[2])) {
					param.setPassword(params[2]);
				}
				if (params.length > 3 && !StringUtils.isEmpty(params[3])) {
					param.setTimeout(Integer.parseInt(params[3]));
				}
				if (params.length > 4 && !StringUtils.isEmpty(params[4])) {
					param.setTryCount(Integer.parseInt(params[4]));
				}
				Log.get(RedisLoader.class).trace("{} -- {} {}", key, ip, param);
				Redis redis = RedisPool.get(null, ip, param);
				if ("default".equals(key)) {
					RedisRep._defaultRedis = redis;
				} else {
					RedisRep.put(key, redis);
				}
			}
		} catch (Exception e) {
			Log.get(RedisLoader.class).error("failed to load redis pool from redis.properties");
			throw e;
		}
	}
}
