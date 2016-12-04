package org.yx.bean;

import java.util.ArrayList;
import java.util.List;

import org.yx.common.StartConstants;
import org.yx.listener.Listener;
import org.yx.listener.SumkEvent;
import org.yx.log.Log;
import org.yx.util.StringUtils;

public abstract class AbstractBeanListener implements Listener<BeanEvent> {

	protected List<String> packages = new ArrayList<String>();
	protected boolean valid = false;

	public AbstractBeanListener(String packs) {
		if (StringUtils.isEmpty(packs)) {
			return;
		}
		String[] ps = packs.split(",");
		for (String p : ps) {
			addPackage(p);
		}
		valid = this.packages.size() > 0;
	}

	public boolean addPackage(String p) {
		p = p.trim();
		if (p.isEmpty()) {
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
	public boolean accept(SumkEvent event) {
		if (!valid) {
			return false;
		}
		if (!BeanEvent.class.isInstance(event)) {
			return false;
		}
		String clzName = ((BeanEvent) event).clz().getName();
		if (clzName.startsWith(StartConstants.INNER_PACKAGE + ".")) {
			return true;
		}
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
