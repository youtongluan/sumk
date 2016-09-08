package org.yx.redis;

import org.yx.exception.SystemException;
import org.yx.log.Log;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;


public class Redis {

	Redis(JedisPool pool, int tryCount) {
		super();
		this.pool = pool;
		this.tryCount = tryCount;
	}
	
	private int tryCount;
	private JedisPool pool;
	
	Jedis jedis(){
		return pool.getResource();
	}
	
	public int getTryCount() {
		return tryCount;
	}

	/**
	 * 获取原生的jedis对象，用于执行一些特别的操作，或者批量操作
	 * @param callback
	 * @return
	 */
	public <T> T exec(RedisCallBack<T> callback) {
        return new RedisTemplate(this).execute(callback);
    }
	
	private void handleRedisException(Exception e1){
		 if(e1!=null){
		    throw new SystemException(12342422,e1.getMessage(),e1);
		 }
	}
	
	

	public java.lang.String get(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.get(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.get").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.get").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String type(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.type(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.type").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.type").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long append(java.lang.String key,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.append(key,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.append").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.append").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> keys(java.lang.String pattern) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.keys(pattern);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.keys").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.keys").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String set(java.lang.String key,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.set(key,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.set").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.set").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String set(java.lang.String key,java.lang.String value,java.lang.String nxxx) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.set(key,value,nxxx);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.set").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.set").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String set(java.lang.String key,java.lang.String value,java.lang.String nxxx,java.lang.String expx,int time) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.set(key,value,nxxx,expx,time);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.set").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.set").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String set(java.lang.String key,java.lang.String value,java.lang.String nxxx,java.lang.String expx,long time) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.set(key,value,nxxx,expx,time);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.set").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.set").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Boolean exists(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.exists(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.exists").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.exists").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long exists(java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.exists(keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.exists").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.exists").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String rename(java.lang.String oldkey,java.lang.String newkey) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.rename(oldkey,newkey);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.rename").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.rename").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> sort(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sort(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sort").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sort").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> sort(java.lang.String key,redis.clients.jedis.SortingParams sortingParameters) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sort(key,sortingParameters);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sort").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sort").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long sort(java.lang.String key,redis.clients.jedis.SortingParams sortingParameters,java.lang.String dstkey) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sort(key,sortingParameters,dstkey);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sort").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sort").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long sort(java.lang.String key,java.lang.String dstkey) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sort(key,dstkey);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sort").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sort").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public redis.clients.jedis.ScanResult<java.lang.String> scan(java.lang.String cursor) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.scan(cursor);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.scan").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.scan").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public redis.clients.jedis.ScanResult<java.lang.String> scan(java.lang.String cursor,redis.clients.jedis.ScanParams params) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.scan(cursor,params);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.scan").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.scan").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> brpop(int timeout,java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.brpop(timeout,key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.brpop").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.brpop").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> brpop(java.lang.String... args) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.brpop(args);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.brpop").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.brpop").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> brpop(int timeout,java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.brpop(timeout,keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.brpop").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.brpop").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public byte[] dump(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.dump(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.dump").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.dump").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public void subscribe(redis.clients.jedis.JedisPubSub jedisPubSub,java.lang.String... channels) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		 jedis.subscribe(jedisPubSub,channels);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.subscribe").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.subscribe").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterInfo() {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterInfo();
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterInfo").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterInfo").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterForget(java.lang.String nodeId) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterForget(nodeId);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterForget").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterForget").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long clusterKeySlot(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterKeySlot(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterKeySlot").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterKeySlot").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterReplicate(java.lang.String nodeId) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterReplicate(nodeId);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterReplicate").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterReplicate").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> clusterSlaves(java.lang.String nodeId) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterSlaves(nodeId);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterSlaves").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterSlaves").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterFailover() {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterFailover();
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterFailover").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterFailover").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.Object> clusterSlots() {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterSlots();
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterSlots").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterSlots").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String asking() {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.asking();
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.asking").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.asking").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> pubsubChannels(java.lang.String pattern) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.pubsubChannels(pattern);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.pubsubChannels").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.pubsubChannels").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long pubsubNumPat() {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.pubsubNumPat();
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.pubsubNumPat").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.pubsubNumPat").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Map<java.lang.String, java.lang.String> pubsubNumSub(java.lang.String... channels) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.pubsubNumSub(channels);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.pubsubNumSub").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.pubsubNumSub").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long pfadd(java.lang.String key,java.lang.String... elements) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.pfadd(key,elements);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.pfadd").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.pfadd").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public long pfcount(java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.pfcount(keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.pfcount").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.pfcount").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public long pfcount(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.pfcount(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.pfcount").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.pfcount").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String pfmerge(java.lang.String destkey,java.lang.String... sourcekeys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.pfmerge(destkey,sourcekeys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.pfmerge").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.pfmerge").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long geoadd(java.lang.String key,double longitude,double latitude,java.lang.String member) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.geoadd(key,longitude,latitude,member);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.geoadd").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.geoadd").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long geoadd(java.lang.String key,java.util.Map<java.lang.String, redis.clients.jedis.GeoCoordinate> memberCoordinateMap) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.geoadd(key,memberCoordinateMap);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.geoadd").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.geoadd").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Double geodist(java.lang.String key,java.lang.String member1,java.lang.String member2,redis.clients.jedis.GeoUnit unit) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.geodist(key,member1,member2,unit);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.geodist").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.geodist").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Double geodist(java.lang.String key,java.lang.String member1,java.lang.String member2) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.geodist(key,member1,member2);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.geodist").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.geodist").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> geohash(java.lang.String key,java.lang.String... members) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.geohash(key,members);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.geohash").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.geohash").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<redis.clients.jedis.GeoCoordinate> geopos(java.lang.String key,java.lang.String... members) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.geopos(key,members);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.geopos").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.geopos").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(java.lang.String key,double longitude,double latitude,double radius,redis.clients.jedis.GeoUnit unit) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.georadius(key,longitude,latitude,radius,unit);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.georadius").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.georadius").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadius(java.lang.String key,double longitude,double latitude,double radius,redis.clients.jedis.GeoUnit unit,redis.clients.jedis.params.geo.GeoRadiusParam param) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.georadius(key,longitude,latitude,radius,unit,param);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.georadius").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.georadius").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.Long> bitfield(java.lang.String key,java.lang.String... arguments) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.bitfield(key,arguments);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.bitfield").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.bitfield").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long del(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.del(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.del").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.del").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long del(java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.del(keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.del").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.del").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long renamenx(java.lang.String oldkey,java.lang.String newkey) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.renamenx(oldkey,newkey);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.renamenx").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.renamenx").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String randomKey() {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.randomKey();
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.randomKey").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.randomKey").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long expire(java.lang.String key,int seconds) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.expire(key,seconds);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.expire").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.expire").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long expireAt(java.lang.String key,long unixTime) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.expireAt(key,unixTime);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.expireAt").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.expireAt").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long ttl(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.ttl(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.ttl").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.ttl").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long move(java.lang.String key,int dbIndex) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.move(key,dbIndex);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.move").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.move").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String getSet(java.lang.String key,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.getSet(key,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.getSet").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.getSet").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> mget(java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.mget(keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.mget").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.mget").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long setnx(java.lang.String key,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.setnx(key,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.setnx").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.setnx").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String setex(java.lang.String key,int seconds,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.setex(key,seconds,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.setex").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.setex").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String mset(java.lang.String... keysvalues) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.mset(keysvalues);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.mset").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.mset").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long msetnx(java.lang.String... keysvalues) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.msetnx(keysvalues);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.msetnx").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.msetnx").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long decrBy(java.lang.String key,long integer) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.decrBy(key,integer);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.decrBy").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.decrBy").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long decr(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.decr(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.decr").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.decr").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long incrBy(java.lang.String key,long integer) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.incrBy(key,integer);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.incrBy").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.incrBy").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Double incrByFloat(java.lang.String key,double value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.incrByFloat(key,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.incrByFloat").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.incrByFloat").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long incr(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.incr(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.incr").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.incr").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long hset(java.lang.String key,java.lang.String field,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hset(key,field,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hset").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hset").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String hget(java.lang.String key,java.lang.String field) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hget(key,field);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hget").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hget").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long hsetnx(java.lang.String key,java.lang.String field,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hsetnx(key,field,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hsetnx").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hsetnx").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String hmset(java.lang.String key,java.util.Map<java.lang.String, java.lang.String> hash) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hmset(key,hash);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hmset").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hmset").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> hmget(java.lang.String key,java.lang.String... fields) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hmget(key,fields);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hmget").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hmget").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long hincrBy(java.lang.String key,java.lang.String field,long value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hincrBy(key,field,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hincrBy").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hincrBy").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Double hincrByFloat(java.lang.String key,java.lang.String field,double value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hincrByFloat(key,field,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hincrByFloat").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hincrByFloat").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Boolean hexists(java.lang.String key,java.lang.String field) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hexists(key,field);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hexists").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hexists").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long hdel(java.lang.String key,java.lang.String... fields) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hdel(key,fields);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hdel").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hdel").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long hlen(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hlen(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hlen").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hlen").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> hkeys(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hkeys(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hkeys").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hkeys").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> hvals(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hvals(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hvals").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hvals").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Map<java.lang.String, java.lang.String> hgetAll(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hgetAll(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hgetAll").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hgetAll").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long rpush(java.lang.String key,java.lang.String... strings) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.rpush(key,strings);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.rpush").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.rpush").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long lpush(java.lang.String key,java.lang.String... strings) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.lpush(key,strings);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.lpush").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.lpush").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long llen(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.llen(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.llen").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.llen").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> lrange(java.lang.String key,long start,long end) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.lrange(key,start,end);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.lrange").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.lrange").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String ltrim(java.lang.String key,long start,long end) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.ltrim(key,start,end);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.ltrim").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.ltrim").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String lindex(java.lang.String key,long index) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.lindex(key,index);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.lindex").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.lindex").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String lset(java.lang.String key,long index,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.lset(key,index,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.lset").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.lset").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long lrem(java.lang.String key,long count,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.lrem(key,count,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.lrem").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.lrem").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String lpop(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.lpop(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.lpop").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.lpop").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String rpop(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.rpop(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.rpop").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.rpop").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String rpoplpush(java.lang.String srckey,java.lang.String dstkey) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.rpoplpush(srckey,dstkey);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.rpoplpush").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.rpoplpush").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long sadd(java.lang.String key,java.lang.String... members) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sadd(key,members);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sadd").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sadd").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> smembers(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.smembers(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.smembers").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.smembers").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long srem(java.lang.String key,java.lang.String... members) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.srem(key,members);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.srem").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.srem").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String spop(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.spop(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.spop").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.spop").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> spop(java.lang.String key,long count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.spop(key,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.spop").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.spop").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long smove(java.lang.String srckey,java.lang.String dstkey,java.lang.String member) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.smove(srckey,dstkey,member);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.smove").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.smove").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long scard(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.scard(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.scard").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.scard").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Boolean sismember(java.lang.String key,java.lang.String member) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sismember(key,member);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sismember").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sismember").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> sinter(java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sinter(keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sinter").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sinter").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long sinterstore(java.lang.String dstkey,java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sinterstore(dstkey,keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sinterstore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sinterstore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> sunion(java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sunion(keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sunion").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sunion").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long sunionstore(java.lang.String dstkey,java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sunionstore(dstkey,keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sunionstore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sunionstore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> sdiff(java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sdiff(keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sdiff").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sdiff").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long sdiffstore(java.lang.String dstkey,java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sdiffstore(dstkey,keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sdiffstore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sdiffstore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> srandmember(java.lang.String key,int count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.srandmember(key,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.srandmember").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.srandmember").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String srandmember(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.srandmember(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.srandmember").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.srandmember").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zadd(java.lang.String key,java.util.Map<java.lang.String, java.lang.Double> scoreMembers,redis.clients.jedis.params.sortedset.ZAddParams params) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zadd(key,scoreMembers,params);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zadd").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zadd").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zadd(java.lang.String key,java.util.Map<java.lang.String, java.lang.Double> scoreMembers) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zadd(key,scoreMembers);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zadd").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zadd").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zadd(java.lang.String key,double score,java.lang.String member) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zadd(key,score,member);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zadd").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zadd").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zadd(java.lang.String key,double score,java.lang.String member,redis.clients.jedis.params.sortedset.ZAddParams params) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zadd(key,score,member,params);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zadd").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zadd").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrange(java.lang.String key,long start,long end) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrange(key,start,end);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrange").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrange").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zrem(java.lang.String key,java.lang.String... members) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrem(key,members);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrem").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrem").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Double zincrby(java.lang.String key,double score,java.lang.String member,redis.clients.jedis.params.sortedset.ZIncrByParams params) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zincrby(key,score,member,params);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zincrby").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zincrby").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Double zincrby(java.lang.String key,double score,java.lang.String member) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zincrby(key,score,member);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zincrby").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zincrby").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zrank(java.lang.String key,java.lang.String member) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrank(key,member);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrank").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrank").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zrevrank(java.lang.String key,java.lang.String member) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrank(key,member);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrank").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrank").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrevrange(java.lang.String key,long start,long end) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrange(key,start,end);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrange").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrange").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<redis.clients.jedis.Tuple> zrangeWithScores(java.lang.String key,long start,long end) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrangeWithScores(key,start,end);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrangeWithScores").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrangeWithScores").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zcard(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zcard(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zcard").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zcard").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Double zscore(java.lang.String key,java.lang.String member) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zscore(key,member);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zscore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zscore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String watch(java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.watch(keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.watch").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.watch").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> blpop(int timeout,java.lang.String... keys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.blpop(timeout,keys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.blpop").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.blpop").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> blpop(java.lang.String... args) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.blpop(args);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.blpop").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.blpop").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> blpop(int timeout,java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.blpop(timeout,key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.blpop").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.blpop").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zcount(java.lang.String key,double min,double max) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zcount(key,min,max);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zcount").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zcount").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zcount(java.lang.String key,java.lang.String min,java.lang.String max) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zcount(key,min,max);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zcount").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zcount").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key,java.lang.String min,java.lang.String max) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrangeByScore(key,min,max);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrangeByScore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrangeByScore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key,double min,double max) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrangeByScore(key,min,max);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrangeByScore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrangeByScore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key,java.lang.String min,java.lang.String max,int offset,int count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrangeByScore(key,min,max,offset,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrangeByScore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrangeByScore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrangeByScore(java.lang.String key,double min,double max,int offset,int count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrangeByScore(key,min,max,offset,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrangeByScore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrangeByScore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key,java.lang.String max,java.lang.String min) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrangeByScore(key,max,min);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrangeByScore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrangeByScore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key,java.lang.String max,java.lang.String min,int offset,int count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrangeByScore(key,max,min,offset,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrangeByScore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrangeByScore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key,double max,double min,int offset,int count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrangeByScore(key,max,min,offset,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrangeByScore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrangeByScore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrevrangeByScore(java.lang.String key,double max,double min) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrangeByScore(key,max,min);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrangeByScore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrangeByScore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zremrangeByRank(java.lang.String key,long start,long end) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zremrangeByRank(key,start,end);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zremrangeByRank").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zremrangeByRank").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zremrangeByScore(java.lang.String key,java.lang.String start,java.lang.String end) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zremrangeByScore(key,start,end);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zremrangeByScore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zremrangeByScore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zremrangeByScore(java.lang.String key,double start,double end) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zremrangeByScore(key,start,end);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zremrangeByScore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zremrangeByScore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zunionstore(java.lang.String dstkey,java.lang.String... sets) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zunionstore(dstkey,sets);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zunionstore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zunionstore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zunionstore(java.lang.String dstkey,redis.clients.jedis.ZParams params,java.lang.String... sets) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zunionstore(dstkey,params,sets);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zunionstore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zunionstore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zinterstore(java.lang.String dstkey,redis.clients.jedis.ZParams params,java.lang.String... sets) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zinterstore(dstkey,params,sets);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zinterstore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zinterstore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zinterstore(java.lang.String dstkey,java.lang.String... sets) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zinterstore(dstkey,sets);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zinterstore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zinterstore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zlexcount(java.lang.String key,java.lang.String min,java.lang.String max) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zlexcount(key,min,max);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zlexcount").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zlexcount").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrangeByLex(java.lang.String key,java.lang.String min,java.lang.String max) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrangeByLex(key,min,max);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrangeByLex").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrangeByLex").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrangeByLex(java.lang.String key,java.lang.String min,java.lang.String max,int offset,int count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrangeByLex(key,min,max,offset,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrangeByLex").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrangeByLex").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrevrangeByLex(java.lang.String key,java.lang.String max,java.lang.String min) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrangeByLex(key,max,min);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrangeByLex").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrangeByLex").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<java.lang.String> zrevrangeByLex(java.lang.String key,java.lang.String max,java.lang.String min,int offset,int count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrangeByLex(key,max,min,offset,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrangeByLex").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrangeByLex").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long zremrangeByLex(java.lang.String key,java.lang.String min,java.lang.String max) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zremrangeByLex(key,min,max);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zremrangeByLex").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zremrangeByLex").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long strlen(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.strlen(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.strlen").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.strlen").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long lpushx(java.lang.String key,java.lang.String... string) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.lpushx(key,string);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.lpushx").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.lpushx").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long persist(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.persist(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.persist").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.persist").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long rpushx(java.lang.String key,java.lang.String... string) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.rpushx(key,string);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.rpushx").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.rpushx").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String echo(java.lang.String string) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.echo(string);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.echo").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.echo").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long linsert(java.lang.String key,redis.clients.jedis.BinaryClient.LIST_POSITION where,java.lang.String pivot,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.linsert(key,where,pivot,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.linsert").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.linsert").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String brpoplpush(java.lang.String source,java.lang.String destination,int timeout) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.brpoplpush(source,destination,timeout);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.brpoplpush").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.brpoplpush").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Boolean setbit(java.lang.String key,long offset,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.setbit(key,offset,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.setbit").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.setbit").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Boolean setbit(java.lang.String key,long offset,boolean value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.setbit(key,offset,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.setbit").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.setbit").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Boolean getbit(java.lang.String key,long offset) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.getbit(key,offset);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.getbit").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.getbit").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long setrange(java.lang.String key,long offset,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.setrange(key,offset,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.setrange").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.setrange").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String getrange(java.lang.String key,long startOffset,long endOffset) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.getrange(key,startOffset,endOffset);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.getrange").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.getrange").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long bitpos(java.lang.String key,boolean value,redis.clients.jedis.BitPosParams params) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.bitpos(key,value,params);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.bitpos").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.bitpos").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long bitpos(java.lang.String key,boolean value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.bitpos(key,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.bitpos").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.bitpos").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> configGet(java.lang.String pattern) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.configGet(pattern);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.configGet").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.configGet").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String configSet(java.lang.String parameter,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.configSet(parameter,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.configSet").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.configSet").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long publish(java.lang.String channel,java.lang.String message) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.publish(channel,message);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.publish").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.publish").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public void psubscribe(redis.clients.jedis.JedisPubSub jedisPubSub,java.lang.String... patterns) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		 jedis.psubscribe(jedisPubSub,patterns);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.psubscribe").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.psubscribe").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Object evalsha(java.lang.String sha1,int keyCount,java.lang.String... params) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.evalsha(sha1,keyCount,params);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.evalsha").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.evalsha").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Object evalsha(java.lang.String script) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.evalsha(script);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.evalsha").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.evalsha").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Object evalsha(java.lang.String sha1,java.util.List<java.lang.String> keys,java.util.List<java.lang.String> args) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.evalsha(sha1,keys,args);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.evalsha").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.evalsha").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.Boolean> scriptExists(java.lang.String... sha1) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.scriptExists(sha1);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.scriptExists").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.scriptExists").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Boolean scriptExists(java.lang.String sha1) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.scriptExists(sha1);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.scriptExists").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.scriptExists").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String scriptLoad(java.lang.String script) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.scriptLoad(script);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.scriptLoad").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.scriptLoad").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<redis.clients.util.Slowlog> slowlogGet(long entries) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.slowlogGet(entries);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.slowlogGet").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.slowlogGet").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<redis.clients.util.Slowlog> slowlogGet() {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.slowlogGet();
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.slowlogGet").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.slowlogGet").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long objectRefcount(java.lang.String string) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.objectRefcount(string);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.objectRefcount").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.objectRefcount").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String objectEncoding(java.lang.String string) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.objectEncoding(string);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.objectEncoding").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.objectEncoding").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long objectIdletime(java.lang.String string) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.objectIdletime(string);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.objectIdletime").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.objectIdletime").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long bitcount(java.lang.String key,long start,long end) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.bitcount(key,start,end);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.bitcount").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.bitcount").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long bitcount(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.bitcount(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.bitcount").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.bitcount").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long bitop(redis.clients.jedis.BitOP op,java.lang.String destKey,java.lang.String... srcKeys) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.bitop(op,destKey,srcKeys);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.bitop").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.bitop").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.util.Map<java.lang.String, java.lang.String>> sentinelMasters() {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sentinelMasters();
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sentinelMasters").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sentinelMasters").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long sentinelReset(java.lang.String pattern) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sentinelReset(pattern);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sentinelReset").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sentinelReset").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.util.Map<java.lang.String, java.lang.String>> sentinelSlaves(java.lang.String masterName) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sentinelSlaves(masterName);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sentinelSlaves").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sentinelSlaves").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String sentinelFailover(java.lang.String masterName) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sentinelFailover(masterName);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sentinelFailover").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sentinelFailover").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String sentinelMonitor(java.lang.String masterName,java.lang.String ip,int port,int quorum) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sentinelMonitor(masterName,ip,port,quorum);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sentinelMonitor").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sentinelMonitor").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String sentinelRemove(java.lang.String masterName) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sentinelRemove(masterName);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sentinelRemove").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sentinelRemove").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String sentinelSet(java.lang.String masterName,java.util.Map<java.lang.String, java.lang.String> parameterMap) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sentinelSet(masterName,parameterMap);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sentinelSet").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sentinelSet").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String restore(java.lang.String key,int ttl,byte... serializedValue) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.restore(key,ttl,serializedValue);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.restore").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.restore").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long pexpire(java.lang.String key,long milliseconds) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.pexpire(key,milliseconds);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.pexpire").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.pexpire").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long pexpireAt(java.lang.String key,long millisecondsTimestamp) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.pexpireAt(key,millisecondsTimestamp);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.pexpireAt").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.pexpireAt").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long pttl(java.lang.String key) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.pttl(key);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.pttl").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.pttl").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String psetex(java.lang.String key,long milliseconds,java.lang.String value) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.psetex(key,milliseconds,value);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.psetex").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.psetex").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clientKill(java.lang.String client) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clientKill(client);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clientKill").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clientKill").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clientSetname(java.lang.String name) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clientSetname(name);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clientSetname").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clientSetname").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String migrate(java.lang.String host,int port,java.lang.String key,int destinationDb,int timeout) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.migrate(host,port,key,destinationDb,timeout);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.migrate").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.migrate").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<java.lang.String, java.lang.String>> hscan(java.lang.String key,java.lang.String cursor,redis.clients.jedis.ScanParams params) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hscan(key,cursor,params);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hscan").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hscan").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public redis.clients.jedis.ScanResult<java.util.Map.Entry<java.lang.String, java.lang.String>> hscan(java.lang.String key,java.lang.String cursor) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.hscan(key,cursor);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.hscan").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.hscan").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public redis.clients.jedis.ScanResult<java.lang.String> sscan(java.lang.String key,java.lang.String cursor) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sscan(key,cursor);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sscan").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sscan").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public redis.clients.jedis.ScanResult<java.lang.String> sscan(java.lang.String key,java.lang.String cursor,redis.clients.jedis.ScanParams params) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sscan(key,cursor,params);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sscan").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sscan").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(java.lang.String key,java.lang.String cursor,redis.clients.jedis.ScanParams params) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zscan(key,cursor,params);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zscan").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zscan").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public redis.clients.jedis.ScanResult<redis.clients.jedis.Tuple> zscan(java.lang.String key,java.lang.String cursor) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zscan(key,cursor);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zscan").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zscan").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterNodes() {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterNodes();
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterNodes").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterNodes").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String readonly() {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.readonly();
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.readonly").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.readonly").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterMeet(java.lang.String ip,int port) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterMeet(ip,port);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterMeet").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterMeet").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterReset(redis.clients.jedis.JedisCluster.Reset resetType) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterReset(resetType);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterReset").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterReset").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterAddSlots(int... slots) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterAddSlots(slots);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterAddSlots").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterAddSlots").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterDelSlots(int... slots) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterDelSlots(slots);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterDelSlots").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterDelSlots").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(java.lang.String key,java.lang.String member,double radius,redis.clients.jedis.GeoUnit unit) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.georadiusByMember(key,member,radius,unit);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.georadiusByMember").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.georadiusByMember").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<redis.clients.jedis.GeoRadiusResponse> georadiusByMember(java.lang.String key,java.lang.String member,double radius,redis.clients.jedis.GeoUnit unit,redis.clients.jedis.params.geo.GeoRadiusParam param) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.georadiusByMember(key,member,radius,unit,param);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.georadiusByMember").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.georadiusByMember").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterSaveConfig() {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterSaveConfig();
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterSaveConfig").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterSaveConfig").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Long clusterCountKeysInSlot(int slot) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterCountKeysInSlot(slot);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterCountKeysInSlot").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterCountKeysInSlot").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterFlushSlots() {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterFlushSlots();
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterFlushSlots").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterFlushSlots").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterSetSlotStable(int slot) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterSetSlotStable(slot);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterSetSlotStable").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterSetSlotStable").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterSetSlotImporting(int slot,java.lang.String nodeId) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterSetSlotImporting(slot,nodeId);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterSetSlotImporting").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterSetSlotImporting").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterSetSlotMigrating(int slot,java.lang.String nodeId) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterSetSlotMigrating(slot,nodeId);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterSetSlotMigrating").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterSetSlotMigrating").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String clusterSetSlotNode(int slot,java.lang.String nodeId) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterSetSlotNode(slot,nodeId);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterSetSlotNode").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterSetSlotNode").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> clusterGetKeysInSlot(int slot,int count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.clusterGetKeysInSlot(slot,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.clusterGetKeysInSlot").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.clusterGetKeysInSlot").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.List<java.lang.String> sentinelGetMasterAddrByName(java.lang.String masterName) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.sentinelGetMasterAddrByName(masterName);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.sentinelGetMasterAddrByName").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.sentinelGetMasterAddrByName").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key,java.lang.String max,java.lang.String min,int offset,int count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrangeByScoreWithScores(key,max,min,offset,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrangeByScoreWithScores").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrangeByScoreWithScores").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key,double max,double min) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrangeByScoreWithScores(key,max,min);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrangeByScoreWithScores").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrangeByScoreWithScores").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key,double max,double min,int offset,int count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrangeByScoreWithScores(key,max,min,offset,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrangeByScoreWithScores").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrangeByScoreWithScores").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeByScoreWithScores(java.lang.String key,java.lang.String max,java.lang.String min) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrangeByScoreWithScores(key,max,min);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrangeByScoreWithScores").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrangeByScoreWithScores").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key,double min,double max,int offset,int count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrangeByScoreWithScores(key,min,max,offset,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrangeByScoreWithScores").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrangeByScoreWithScores").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key,java.lang.String min,java.lang.String max) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrangeByScoreWithScores(key,min,max);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrangeByScoreWithScores").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrangeByScoreWithScores").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key,double min,double max) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrangeByScoreWithScores(key,min,max);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrangeByScoreWithScores").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrangeByScoreWithScores").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<redis.clients.jedis.Tuple> zrangeByScoreWithScores(java.lang.String key,java.lang.String min,java.lang.String max,int offset,int count) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrangeByScoreWithScores(key,min,max,offset,count);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrangeByScoreWithScores").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrangeByScoreWithScores").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.util.Set<redis.clients.jedis.Tuple> zrevrangeWithScores(java.lang.String key,long start,long end) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.zrevrangeWithScores(key,start,end);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.zrevrangeWithScores").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.zrevrangeWithScores").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.String substr(java.lang.String key,int start,int end) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.substr(key,start,end);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.substr").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.substr").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Object eval(java.lang.String script,java.util.List<java.lang.String> keys,java.util.List<java.lang.String> args) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.eval(script,keys,args);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.eval").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.eval").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Object eval(java.lang.String script) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.eval(script);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.eval").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.eval").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}
	public java.lang.Object eval(java.lang.String script,int keyCount,java.lang.String... params) {
		Jedis jedis = null;
		Exception e1=null;
	    for(int i=0;i<tryCount;i++){
	    	try{
	    		jedis = pool.getResource();
	    		return jedis.eval(script,keyCount,params);
	    	}catch(Exception e){
	    		if(JedisConnectionException.class.isInstance(e)||
	    				(e.getCause()!=null && JedisConnectionException.class.isInstance(e.getCause())) ){
	    			Log.get("Redis.eval").error("redis connection failed！"+e.getMessage(),e);
	        		e1=e;
	        		continue;
	    		}
	    		Log.get("Redis.eval").error("redis execute error！"+e.getMessage(),e);
	    		SystemException.throwException(12342411,e.getMessage(),e);
	    	}finally{
	    		if(jedis!=null){
	    			jedis.close();
	    		}
	    	}
	    }
	    handleRedisException(e1);
	    throw new SystemException(12342423,"未知redis异常");
	}

}
