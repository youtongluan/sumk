package org.yx.rpc;

import java.util.HashSet;
import java.util.Set;

/**
 * zk中保存的节点信息
 * @author youtl
 *
 */
public class ZkData {
	private String methods;
	private Integer weight;
	private Long timeout;
	private Integer clientCount;
	public String getMethods() {
		return methods;
	}
	public void setMethods(String methods) {
		this.methods = methods;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Long getTimeout() {
		return timeout;
	}
	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}
	public Integer getClientCount() {
		return clientCount;
	}
	public void setClientCount(Integer clientCount) {
		this.clientCount = clientCount;
	}
	public Set<String> methodSet(){
		String[] ms=this.methods.split(",");
		Set<String> set=new HashSet<String>();
		for(String m:ms){
			set.add(m);
		}
		return set;
	}
}
