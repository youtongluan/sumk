package org.yx.db;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;
class WeightedDataSourceRoute {
	private volatile int currentIndex = -1;
	private volatile int currentWeight = 0;
	private int maxWeight; 
	private int gcdWeight; 
    private List<WeightedDS> serverList; 
    
    public Set<DataSource> allDataSource(){
    	Set<DataSource> list=new HashSet<>();
    	for(WeightedDS ds:this.serverList){
    		list.add(ds.getDs());
    	}
    	return list;
    }
    
    private static int gcd(int a, int b) {
		BigInteger b1 = new BigInteger(String.valueOf(a));
		BigInteger b2 = new BigInteger(String.valueOf(b));
		BigInteger gcd = b1.gcd(b2);
		return gcd.intValue();
	}

	
	private static int getGCDForServers(List<WeightedDS> serverList) {
		int w = 0;
		for (int i = 0, len = serverList.size(); i < len - 1; i++) {
			if (w == 0) {
				w = gcd(serverList.get(i).getWeight(),
						serverList.get(i + 1).getWeight());
			} else {
				w = gcd(w, serverList.get(i + 1).getWeight());
			}
		}
		return w;
	}
	
    
    private static int getMaxWeightForServers(List<WeightedDS> serverList) {
        int w = 0;
        for (WeightedDS s:serverList) {
            if (s.getWeight()>w) {
                w=s.getWeight();
            }
        }
        return w;
    }
    /**
     * 根据权重获取连接
     * @return
     */
    public DataSource datasource(){
    	WeightedDS sm=this.GetServer();
    	if(sm==null){
    		return null;
    	}
    	return sm.getDs();
    }
    
    
    private WeightedDS GetServer() {
    	List<WeightedDS> list=serverList;
    	int serverCount=list.size();
    	for(int i=0;i<serverCount*2;i++) {
            currentIndex = (currentIndex + 1) % serverCount;
            if (currentIndex == 0) {
                currentWeight = currentWeight - gcdWeight;
                if (currentWeight <= 0) {
                    currentWeight = maxWeight;
                    if (currentWeight == 0)
                        return null;
                }
            }
            
            int index=currentIndex%serverCount;
            if (list.get(index).getWeight() >= currentWeight) {



                return list.get(index);
            }
        }
        return null;
    }

    public void addServer(WeightedDS s) {
    	List<WeightedDS> list=new ArrayList<>(serverList);
    	list.add(s);
    	init(list);
	}
    
    public void removeServer(DataSource ds){
    	List<WeightedDS> list=new ArrayList<>(serverList);
    	for(WeightedDS s:list){
    		if(s.getDs().equals(ds)){
    			list.remove(s);
    			break;
    		}
    	}
    	init(list);
    }
    
	public WeightedDataSourceRoute(WeightedDS... servers) {
		List<WeightedDS> list=new ArrayList<>();
    	Arrays.stream(servers).forEach(s->list.add(s));
        init(list);
    }
	
	public WeightedDataSourceRoute(Collection<WeightedDS> servers) {
        init(servers);
    }
	
	private void init(Collection<WeightedDS> list){
		serverList=new ArrayList<WeightedDS>(list.size());
		serverList.addAll(list);
        maxWeight = getMaxWeightForServers(serverList);
        gcdWeight = getGCDForServers(serverList);
	}

}


