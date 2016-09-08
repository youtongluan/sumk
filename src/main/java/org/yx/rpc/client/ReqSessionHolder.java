package org.yx.rpc.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.rpc.Url;

public class ReqSessionHolder {
	private static Map<Url,Object> map=new ConcurrentHashMap<>();
	public static void addClient(Url url,ReqSession s){
		map.putIfAbsent(url, s);
	}
	
	
	
	public static ReqSession getSession(Url url){
		Object obj=map.get(url);
		if(obj==null){
			ReqSession ses=createSession(url);
			Object ses0=map.putIfAbsent(url, ses);
			if(ses0!=null){
				ses.close();
				return _getSession(ses0);
			}
			return ses;
		}
		return _getSession(obj);
	}

	@SuppressWarnings("unchecked")
	public static ReqSession _getSession(Object obj){
		if(ReqSession.class.isInstance(obj)){
			return (ReqSession) obj;
		}
		
		return ((List<ReqSession>)obj).get(0);
	}

	private static ReqSession createSession(Url url) {
		return new ReqSession(url.getIp(), url.getPort());
	}
}
