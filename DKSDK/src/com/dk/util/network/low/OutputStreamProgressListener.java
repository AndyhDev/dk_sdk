package com.dk.util.network.low;

public abstract interface OutputStreamProgressListener {
	public abstract void onWrite(Long sended);
}

