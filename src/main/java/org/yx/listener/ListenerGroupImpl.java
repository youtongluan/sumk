package org.yx.listener;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.yx.log.Log;

public class ListenerGroupImpl<T extends EventObject> implements ListenerGroup<T> {
	
	private List<Listener<T>> listeners = new ArrayList<>();
	
	@Override
	public Listener<T> removeListener(Listener<T> listener) {
		List<Listener<T>> lis=this.listeners;
		lis=new ArrayList<>(lis);
		Listener<T> l=null;
		for(int i=lis.size()-1;i>=0;i--){
			if(lis.get(i).equals(listener)){
				l=lis.remove(i);
				break;
			}
		}
		if(l!=null){
			this.listeners=lis;
		}
		return l;
	}

	@Override
	public boolean addListener(Listener<T> listener) {
		List<Listener<T>> lis=this.listeners;
		lis=new ArrayList<>(lis);
		if (!lis.contains(listener)) {
			lis.add(listener);
			this.listeners=lis;
			Log.get(this.getClass()).trace("add listener {}", listener.toString());
			return true;
		}
		return false;
	}

	
	@Override
	public void listen(T event) {
		List<Listener<T>> lis=this.listeners;
		for (Listener<T> lin : lis) {
			if (lin.accept(event)) {
				lin.listen(event);
			}
		}
	}

	@Override
	public int size() {
		return this.listeners.size();
	}


}