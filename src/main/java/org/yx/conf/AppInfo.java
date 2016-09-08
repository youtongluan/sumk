package org.yx.conf;

import java.io.InputStream;

import org.springframework.util.StringUtils;
import org.yx.log.Log;
import org.yx.rpc.NetworkUtil;

public class AppInfo{
	private static String appId="UNKOWN";

	public static final PropertiesInfo info=new PropertiesInfo("app.properties"){

		@Override
		public void deal(InputStream in) throws Exception {
			super.deal(in);
			String id=get("appId");
			if(id!=null){
				AppInfo.appId=id;
			}
		}
		
	};
	
	public static String getZKUrl(){
		return info.get("zkurl");
	}
	
	public static String getIp(){
		String ip = info.get("ip");
		if(ip!=null){
			return ip;
		}
		try {
			return NetworkUtil.getLocalIP();
		} catch (Exception e) {
			Log.get("SYS.9").error(e);
		}
		return "0.0.0.0";
	}
	
	public static String getAppId() {
		return appId;
	}

	public static String getDBRoot(String defalutRoot){
		String root=info.get("dbRoot");
		if(!StringUtils.isEmpty(root)){
			return "file://"+root+"/";
		}
		return defalutRoot;
	}

	public static String get(String name) {
		return info.get(name);
	}
	
}
