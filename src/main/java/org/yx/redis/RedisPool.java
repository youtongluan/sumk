package org.yx.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.log.Log;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

	private static final Map<String,Redis> map=new ConcurrentHashMap<String,Redis>();
	
	private static String toMapKey(String host,int port,int db){
		return host+"_"+port+"#"+db;
	}
	
	
	/**
	 * 默认连接0库
	 * @param host
	 * @param port
	 * @return
	 */
	public static Redis get(String host, int port) throws Exception{
		RedisParamter p=RedisParamter.create(port);
		return get(null,host, p);
	}

	public static Redis get(String host, int port, int timeout) throws Exception {
		RedisParamter p=RedisParamter.create(port);
		p.setTimeout(timeout);
		return get(null,host, p);
	}
	

	/**
	 * 
	 * @param config 连接池配置. null的话就会采用默认设置
	 * @param host redis 的ip地址
	 * @param p 自定义的连接参数. null表示采用默认配置
	 * @return
	 */
	public static Redis get(JedisPoolConfig config,String host, RedisParamter p) throws Exception{
		if(p==null){
			p=RedisParamter.create();
		}
		return redis(config, host.trim(), p.getPort(), p.getTimeout(), p.getPassword(),
				p.getDb(),p.getTryCount());
	}

	private static Redis redis(JedisPoolConfig config, String host, int port,
			int timeout, String password, int db, int tryCount) throws Exception {
		if(tryCount<1 || tryCount>10){
			throw new Exception("tryCount必须介于0和10之间");
		}
		String key=toMapKey(host,port,db);
		Redis redis=map.get(key);
		if(redis==null){
			synchronized(RedisPool.class){
				if(map.containsKey(key)){
					return map.get(key);
				}
				JedisPool pool=create(config, host, port, timeout, password,
						db);
				redis=new Redis(pool,tryCount);
				map.put(key, redis);
			}
		}
		return redis;
	}

	private static JedisPool create(JedisPoolConfig config, String host, int port, int timeout, String password,
		    int database) {
		if(config==null){
			config=defaultPoolConfig();
		}
		Log.get(RedisPool.class,"create").info("create redis pool,host={},port={},db={}",host,port,database);
		return new JedisPool(config, host, port, timeout, password,
				database);
		
	}
	
	private static JedisPoolConfig defaultPoolConfig(){
		JedisPoolConfig config=new JedisPoolConfig();
		config.setMinIdle(1);
		config.setMaxIdle(20);
		config.setMaxTotal(100);
		config.setTestWhileIdle(true);
		config.setTimeBetweenEvictionRunsMillis(5*60000);
		config.setNumTestsPerEvictionRun(3);
		return config;
	}


}
