package org.yx.rpc.client.route;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.util.StringUtils;
import org.yx.rpc.Url;
import org.yx.rpc.ZKRouteData;
import org.yx.rpc.ZkClientHolder;
import org.yx.rpc.ZkData;
import org.yx.util.GsonUtil;

public class ZkRouteParser {
	/**
	 * 根据zk的数据，初始化RouteHolder
	 * @param zkUrl
	 */
	public void parse(String zkUrl){
		Map<Url,ZkData> datas=new HashMap<>();
		ZkClient zk=ZkClientHolder.getZkClient(zkUrl);
		
		List<String> paths=zk.getChildren(ZkClientHolder.SOA_ROOT);
		for(String path:paths){
			Url url=Url.create(path);
			String json=zk.readData(ZkClientHolder.SOA_ROOT+"/"+path);
			ZKRouteData data=GsonUtil.fromJson(json, ZKRouteData.class);
			String methods=data.getMethods();
			if(StringUtils.isEmpty(methods)){
				continue;
			}
			ZkData d=new ZkData();
			d.setMethods(methods);
			d.setWeight(data.getWeight());
			datas.put(url, d);
		}
		parseRoute(datas);
	}
	private void parseRoute(Map<Url,ZkData> datas){
		Map<String, Set<ServerMachine>> map=new HashMap<>();
		for(Url url :datas.keySet()){
			Map<String, ServerMachine> ms=this.createServerMachine(url, datas.get(url));
			for(String m:ms.keySet()){
				Set<ServerMachine> server=map.get(m);
				if(server==null){
					server=new HashSet<>();
					map.put(m, server);
				}
				server.add(ms.get(m));
			}
		}
		Map<String,WeightedRoute> routes=new HashMap<>();
		for(String method:map.keySet()){
			routes.put(method, new WeightedRoute(map.get(method)));
		}
		RouteHolder.init(routes,datas);
	}
	/**
	 * 返回方法和路由的键值对，如果方法为空，返回空map。
	 * @param url
	 * @param data
	 * @return
	 */
	public Map<String, ServerMachine> createServerMachine(Url url,ZkData data){
		Map<String,ServerMachine> servers=new HashMap<>();
		int weight=data.getWeight()==null?5:data.getWeight();
		String method=data.getMethods();
		if(method==null||method.isEmpty()){
			return servers;
		}
		for(String m:method.split(",")){
			ServerMachine server=new ServerMachine(url,weight);
			servers.put(m,server);
		}
		return servers;
	}
}
