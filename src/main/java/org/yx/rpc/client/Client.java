package org.yx.rpc.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.conf.AppInfo;
import org.yx.exception.SoaException;
import org.yx.log.Log;
import org.yx.rpc.RpcUtils;
import org.yx.rpc.SourceSn;
import org.yx.rpc.client.route.ZkRouteParser;
import org.yx.util.GsonUtil;
import org.yx.util.UUIDSeed;

public final class Client {
	private static String getAppId(){
		return AppInfo.getAppId();
	}
	private static Map<String,Long> TimeoutMap=new ConcurrentHashMap<>();
	private static long DEFAULT_TIMEOUT=Long.getLong("RPC.TIMEOUT",30000);
	public static void init(){
		String zkUrl=AppInfo.getZKUrl();
		Log.get("SYS.2").info("zkUrl:{}",zkUrl);
		new ZkRouteParser().parse(zkUrl);;
	}
	private static Req createReq(String method){
		Req req=new Req();
		req.setStart(System.currentTimeMillis());
		String sn=UUIDSeed.random();
		req.setSn(sn);
		String sn0=SourceSn.getSn0();
		if(sn0!=null){
			req.setSn0(sn);
		}
		req.setMethod(method);
		req.setSrc(getAppId());
		return req;
	}
	
	private static String call0(Req req){
		ReqResp reqResp;
		try {
			reqResp = ReqSender.send(req, TimeoutMap.getOrDefault(RpcUtils.getAppId(req.getMethod()), DEFAULT_TIMEOUT));
			if(reqResp.getResp().getException()!=null){
				throw reqResp.getResp().getException();
			}
			return reqResp.getResp().getJson();
		} catch (Throwable e) {
			Log.get("SYS.3").error(e);
			SoaException.throwException(1022,e.getMessage(),e);
			return null;
		}
	}
	public static String call(String method,Object... args){
		Req req=createReq(method);
		String[] params=new String[args.length];
		for(int i=0;i<args.length;i++){
			params[i]=GsonUtil.toJson(args[i]);
		}
		req.setParams(params);
		return call0(req);
	}
	
	/**
	 * 
	 * @param method
	 * @param arg 用json序列化的参数对象
	 * @return
	 */
	public static String callInJson(String method,String arg){
		Req req=createReq(method);
		req.setArgs(arg);
		return call0(req);
	}
}
