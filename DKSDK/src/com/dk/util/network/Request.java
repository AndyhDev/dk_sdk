package com.dk.util.network;

import android.app.Activity;

import com.dk.util.network.low.Post;

public class Request {
	@SuppressWarnings("unused")
	private static final String TAG = "Request";
	private Activity activity;
	private String url;
	private Post post;
	private RequestListener listener;
	private boolean syncModus = false;
	
	public Request(Activity activity, String url){
		this.activity = activity;
		this.url = url;
		post = new Post();
	}
	
	public Request(Activity activity){
		this.activity = activity;
		post = new Post();
	}
	public Request(String url){
		this.url = url;
		post = new Post();
	}
	
	public Request(){
		post = new Post();
	}
	
	public void addParam(String paramName, String paramValue){
		post.addParam(paramName, paramValue);
	}
	
	public void start(){
		post.setUrl(url);
		if(syncModus){
			doRequest();
		}else{
			new Thread(new Runnable(){
				public void run() {
					doRequest();
				}
			}).start();
		}
	}
	
	private void doRequest(){
		String response = post.run();
		//Log.d(TAG, "response:" + response);
		if(post.getStatusCode() == 200){
			postEnd(response);
		}else{
			postError();
		}
	}
	
	private void postError(){
		if(listener != null){
			if(activity == null){
				listener.onRequestError();
				return;
			}
			activity.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					listener.onRequestError();
				}
			});
		}
	}
	private void postEnd(final String result){
		if(listener != null){
			if(activity == null){
				listener.onRequestEnd(result);
				return;
			}
			activity.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					listener.onRequestEnd(result);
				}
			});
		}
	}
	public void setListener(RequestListener listener){
		this.listener = listener;
	}
	
	public String getUrl(){
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean getSyncModus() {
		return syncModus;
	}

	public void setSyncModus(boolean syncModus) {
		this.syncModus = syncModus;
	}
	
}
