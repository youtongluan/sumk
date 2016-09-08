package org.yx.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.yx.exception.TooManyBeanException;
import org.yx.log.Log;



public class BeanPool {
	private Map<String,Object> map=new ConcurrentHashMap<>();
	static String getBeanName(Class<?> clz){
		String name= StringUtils.uncapitalize(clz.getSimpleName());
		if(name.endsWith("Impl")){
			name=name.substring(0, name.length()-4);
		}
		return name;
	}
	private final Object getBean(Object v){
		BeanWrapper w=(BeanWrapper)v;
		return w.getBean();
	}
	/**
	 * 获取pool中所有的bean
	 * @return
	 */
	Collection<Object> allBeans(){
		List<Object> list=new ArrayList<>();
		Collection<Object> vs=map.values();
		for(Object v:vs){
			if(!v.getClass().isArray()){
				list.add(getBean(v));
				continue;
			}
			Object[] objs=(Object[])v;
			for(Object o:objs){
				list.add(getBean(o));
			}
		}
		return list;
	}
	
	/**
	 * 添加bean，一个类的实例，不能多次出现在一个name中
	 * @param bean
	 * @param name
	 */
	public void put(String name,Object bean){
		Assert.notNull(bean);
		Class<?> clz=bean.getClass();
		if(name==null || name.isEmpty()){
			name=getBeanName(clz);
		}
		name=name.trim();
		BeanWrapper w=new BeanWrapper();
		w.setBean(bean);
		
		Object oldWrapper=map.putIfAbsent(name, w);
		if(oldWrapper==null){
			return;
		}
		synchronized(this){
			if(!oldWrapper.getClass().isArray()){
				Object old=this.getBean(oldWrapper);
				if(bean.getClass()==old.getClass()){
					Log.get(this.getClass(),"put").info("{}={} duplicate",name,bean.getClass().getName());
					return;
				}
				map.put(name, new BeanWrapper[]{(BeanWrapper) oldWrapper,w});
				return;
			}
			BeanWrapper[] objs=(BeanWrapper[])oldWrapper;
			for(BeanWrapper o:objs){
				if(bean.getClass()==o.getBean().getClass()){
					Log.get(this.getClass(),"put").info("{}={} duplicate",name,bean.getClass().getName());
					return;
				}
			}
			BeanWrapper[] beans=new BeanWrapper[objs.length+1];
			System.arraycopy(objs, 0, beans, 0, objs.length);
			beans[beans.length-1]=w;
			map.put(name, beans);
		}
		
	}
	/**
	 * name和clz都有可能为null，但不能同时为null
	 * @param name
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(String name,Class<?> clz){
		if(name==null || name.length()==0){
			name=getBeanName(clz);
		}
		if(clz==Object.class){
			clz=null;
		}
		Object bw=map.get(name);
		if(bw==null){
			return null;
		}
		if(!bw.getClass().isArray()){
			Object obj=this.getBean(bw);
			if(clz==null || clz.isInstance(obj)){
				return (T) obj;
			}
			throw new ClassCastException(name+"的类型不对。实际是"+obj.getClass().getName()+",不能与"+clz.getName()+"兼容");
		}
		if(clz==null){
			throw new TooManyBeanException(name+"存在多个实例");
		}
		BeanWrapper[] objs=(BeanWrapper[])bw;
		
		for(BeanWrapper o:objs){
			if(clz==o.getBean().getClass()){
				return (T) o.getBean();
			}
		}
		
		Object bean=null;
		for(BeanWrapper w:objs){
			Object o=w.getBean();
			if(clz.isInstance(o)){
				if(bean!=null){
					Log.get(this.getClass(),"getBean").error(name+"存在多个实例:"+o.getClass().getName()+","+bean.getClass().getName());
					throw new TooManyBeanException(name+"存在多个"+clz.getName()+"实例");
				}
				bean=o;
			}
		}
		return (T) bean;
		
	}
	@Override
	public String toString() {
		Iterator<Entry<String,Object>> i = map.entrySet().iterator();
        if (! i.hasNext())
            return "empty bean";

        StringBuilder sb = new StringBuilder();
        for (;;) {
            Entry<String,Object> e = i.next();
            String key = e.getKey();
            Object value = e.getValue();
            sb.append(key);
            sb.append(':');
            if(value.getClass().isArray()){
            	sb.append("[");
            	Object[] objs=(Object[])value;
        		
        		for(int k=0;k<objs.length;k++){
        			if(k>0){
        				sb.append(",");
        			}
        			Object o=objs[k];
        			sb.append(getBean(o).getClass().getName());
        		}
        		sb.append("]");
            }else{
            	sb.append(getBean(value).getClass().getName());
            }
           
            if (! i.hasNext())
                return sb.toString();
            sb.append(',').append(' ');
        }
	}
	
}
