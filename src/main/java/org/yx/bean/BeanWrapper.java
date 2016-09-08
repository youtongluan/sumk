package org.yx.bean;

public class BeanWrapper {
	private Object Bean;
	
	private boolean soa;
	
	private boolean web;
	public Object getBean() {
		return Bean;
	}
	public void setBean(Object bean) {
		Bean = bean;
	}
	public boolean isSoa() {
		return soa;
	}
	public void setSoa(boolean soa) {
		this.soa = soa;
	}
	public boolean isWeb() {
		return web;
	}
	public void setWeb(boolean web) {
		this.web = web;
	}
	
	
}
