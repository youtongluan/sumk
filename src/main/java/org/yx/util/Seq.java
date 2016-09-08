package org.yx.util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicIntegerArray;


public final class Seq {

	private static final long FROMMILS=1420041600000L;
	private static final int LOCAL_SEQ_INDEX=64;
	
	private AtomicIntegerArray localSeqs=new AtomicIntegerArray(LOCAL_SEQ_INDEX+1);
	private SeqCounter counter;
	public Seq(){
		try {
			for(int i=0;i<localSeqs.length();i++){
				localSeqs.set(i, ThreadLocalRandom.current().nextInt(256));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setCounter(SeqCounter counter) {
		this.counter = counter;
	}

	
	private int localHashIndex(String name){
		if(name==null|| name.isEmpty()){
			return LOCAL_SEQ_INDEX;
		}
		return name.hashCode() & (LOCAL_SEQ_INDEX-1);
	}

	
	private int localSeq(String name){
		int hash=localHashIndex(name);
		int num = localSeqs.incrementAndGet(hash);
		if(num>50000000){
			localSeqs.weakCompareAndSet(hash,num,ThreadLocalRandom.current().nextInt(100));
		}
		return num;
	}
	private static long shortNowMills(){
		return System.currentTimeMillis()-FROMMILS;
	}
	private static long fullTime(long time){
		return time+FROMMILS;
	}
	
	int sub(String name){
		if(counter!=null){
			try {
				int c=counter.count(name);
				if(c>-1){
					return c ;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int sub=(ThreadLocalRandom.current().nextInt(0x100)<<16);
		sub |= ((int)System.nanoTime()) & 0xFF00;
		return sub | (localSeq(name) & 0xFF);
		
	}
	
	public long next(String name){
		if(name!=null){
			name=name.trim();
			if(name.isEmpty()){
				name=null;
			}
		}
		long num=shortNowMills();
		num &= 0x7FFFFFFFFFL;
		num <<= 24;
		int sub=sub(name) & 0xFFFFFF ;
		return num|sub;
	}
	
	public long next(){
		return next(null);
	}
	
	public static long getDate(long seq){
		long num=seq & 0x7FFFFFFFFF000000L;
		num >>=24;
		return fullTime(num);
	}
	
	

}
