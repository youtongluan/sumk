package org.yx.main;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;
import org.yx.bean.BeanFactoryListener;
import org.yx.bean.BeanPublisher;
import org.yx.conf.AppInfo;
import org.yx.http.start.HttpBeanListener;
import org.yx.http.start.HttpStarter;
import org.yx.rpc.server.start.SOABeanListener;
import org.yx.rpc.server.start.SOAStarter;

public class Bootstrap {
	public static void main(String[] args) {
		try {
			BeanPublisher.addListener(new BeanFactoryListener(AppInfo.get("ioc")));
			boolean soaServer=false,httpServer=false;
			String soa=AppInfo.get("soa");
			if(soa!=null&&soa.length()>0){
				BeanPublisher.addListener(new SOABeanListener(soa));
				soaServer=!Boolean.getBoolean("nosoa");
			}
			String http=AppInfo.get("http");
			if(http!=null&&http.length()>0){
				BeanPublisher.addListener(new HttpBeanListener(http));
				httpServer=!Boolean.getBoolean("nohttp");
			}
			BeanPublisher.publishBeans(allPackage(soa,http,AppInfo.get("ioc")));
			if(soaServer){
				SOAStarter.start();
			}
			if(httpServer){
				HttpStarter.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	/**
	 * 将逗号分隔符的字符串，拆分为无逗号的字符串
	 * @param ps 每个字符串都可能含有,
	 * @return
	 */
	private static String[] allPackage(String... ps) {
		List<String> list=new ArrayList<String>();
		for(String p:ps){
			if(StringUtils.isEmpty(p)){
				continue;
			}
			p=p.replace('，', ',');
			String[] ss=p.split(",");
			for(String s:ss){
				s=s.trim();
				if(s.isEmpty()){
					continue;
				}
				list.add(s);
			}
		}
		return list.toArray(new String[list.size()]);
	}
}
