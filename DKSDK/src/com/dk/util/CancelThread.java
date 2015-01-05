package com.dk.util;

import java.util.concurrent.atomic.AtomicInteger;

public class CancelThread extends Thread{
	private final AtomicInteger test = new AtomicInteger(0);
	
	public synchronized void cancel(){
		test.set(1);
	}
	public synchronized boolean getCanceled(){
		if(test.get() == 0){
			return false;
		}
		return true;
	}
}
