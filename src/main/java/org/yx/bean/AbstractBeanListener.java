package org.yx.bean;

import java.util.ArrayList;
import java.util.List;

import org.yx.listener.Listener;
import org.yx.log.Log;

public abstract class AbstractBeanListener implements Listener<BeanEvent> {

	
	protected List<String> packages = new ArrayList<String>();
	
	public AbstractBeanListener(String packs){
		if(packs==null){
			return;
		}
		String[] ps=packs.split(",");
		for(String p:ps){
			addPackage(p);
		}
	}

	public boolean addPackage(String p) {
		p=p.trim();
		if(p.isEmpty()){
			return false;
		}
		List<String> ps = this.packages;
		ps = new ArrayList<>(ps);
		String p2 = p + ".";
		if (!ps.contains(p2)) {
			ps.add(p2);
			this.packages = ps;
			Log.get(this.getClass()).trace("add package {}", p);
			return true;
		}
		return false;
	}

	@Override
	public boolean accept(BeanEvent event) {
		String clzName = event.getClassName();
		List<String> packs = this.packages;
		for (String pack : packs) {
			if (clzName.startsWith(pack)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String[] getTags() {
		return null;
	}

}
