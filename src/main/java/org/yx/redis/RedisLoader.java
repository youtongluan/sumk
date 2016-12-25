package org.yx.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.util.CollectionUtils;
import org.yx.util.SeqUtil;
import org.yx.util.StringUtils;

import redis.clients.jedis.JedisPoolConfig;

public class RedisLoader {
	private static JedisPoolConfig defaultConfig = null;

	public static JedisPoolConfig getDefaultConfig() {
		return defaultConfig;
	}

	public static void setDefaultConfig(JedisPoolConfig defaultConfig) {
		RedisLoader.defaultConfig = defaultConfig;
	}

	private static final String SLAVE_PRE = "slave.";

	public static void init() throws Exception {
		try {
			loadRedisByConfig();
			Redis counter = RedisPool.getRedisExactly(AppInfo.get("sumk.counter.name", "session"));
			if (counter != null) {
				SeqUtil.setCounter(new RedisCounter(counter));
			}
		} catch (Exception e) {
			Log.get(RedisLoader.class).error("failed to load redis pool from redis.properties");
			throw e;
		}
	}

	private static void loadRedisByConfig() throws IOException, Exception {
		String file = "redis.properties";
		InputStream in = RedisLoader.class.getClassLoader().getResourceAsStream(file);
		if (in == null) {
			Log.get("SYS").info("can not found redis property file:{}", file);
			return;
		}
		Map<String, String> p = CollectionUtils.loadMap(in);
		Log.get(RedisLoader.class).debug("config:{}", p);
		Set<String> keys = p.keySet();
		for (String kk : keys) {
			if (StringUtils.isEmpty(kk)) {
				continue;
			}
			String v = p.get(kk);
			if (kk.startsWith(SLAVE_PRE)) {
				createReadRedis(kk.substring(SLAVE_PRE.length()), v.split(","));
				continue;
			}
			Redis redis = create(v);
			String[] moduleKeys = kk.split(",");
			for (String key : moduleKeys) {
				key = key.toLowerCase();
				if (StringUtils.isEmpty(key)) {
					continue;
				}
				if (StringUtils.isEmpty(v)) {
					continue;
				}
				if (RedisConstants.DEFAULT.equals(key)) {
					RedisPool._defaultRedis = redis;
				} else {
					RedisPool.put(key, redis);
				}
			}
		}
	}

	private static void createReadRedis(String host, String[] redisParams) throws Exception {
		if (StringUtils.isEmpty(host) || redisParams.length == 0 || !host.contains(":")) {
			return;
		}
		List<RedisParamter> list = new ArrayList<>();
		for (String param : redisParams) {
			param = param.trim();
			if (StringUtils.isEmpty(param)) {
				continue;
			}
			list.add(createParam(param));
		}
		if (list.isEmpty()) {
			return;
		}
		RedisPool.attachRead(host, list.toArray(new RedisParamter[list.size()]));
	}

	private static RedisParamter createParam(String v) throws Exception {
		String[] params = v.split("#");
		String ip = params[0];
		RedisParamter param;
		if (ip.contains(":")) {
			String[] addr = ip.split(":");
			ip = addr[0];
			param = RedisParamter.create(ip, Integer.parseInt(addr[1]));
		} else {
			param = RedisParamter.create(ip);
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
		return param;
	}

	private static Redis create(String v) throws Exception {
		return RedisFactory.get(defaultConfig, createParam(v));
	}
}
