package com.dk.util.network;

import java.io.File;

import android.app.Activity;
import android.util.Log;

import com.dk.util.network.low.Post;
import com.dk.util.network.low.PostListener;

public class Upload implements PostListener {
	private static final String TAG = "Upload";
	
	private Post post;
	private String url; 
	private UploadListener listener;
	//private Long totalSize = (long) 0;


	private Activity activity;
	private boolean syncModus = false;
	
	public Upload(Activity activity, String url){
		this.activity = activity;
		this.url = url;
		post = new Post(url);
	}
	public Upload(String url){
		this.url = url;
		post = new Post(url);
	}
	public void addFile(String name, String filePath){
	    post.addFile(name, new File(filePath));
	}
	public void addParam(String paramName, String paramValue){
		post.addParam(paramName, paramValue);
	}
	public void setListener(UploadListener listener){
		this.listener = listener;
	}
	public long getSize(){
		return post.getSize();
	}
	
	public String start(){
		post.setListener(this);
		Log.d(TAG, "upload 31");
		if(syncModus){
			Log.d(TAG, "upload 32");
			return doUp();
		}else{
			Log.d(TAG, "upload 33");
			new Thread(new Runnable(){
				public void run() {
					doUp();
				}
			}).start();
		}
		Log.d(TAG, "upload 35");
		return null;
	}
	private String doUp(){
		String response = post.run();
		Log.d(TAG, "upload 34 " + response);
		
		if(listener != null){
			if(post.getStatusCode() == 200){
				final String body = response;
				if(activity == null){
					listener.onUploadEnd(body);
				}else{
					activity.runOnUiThread(new Runnable(){
						@Override
						public void run() {
							listener.onUploadEnd(body);
						}
					});
				}
			}else{
				if(activity == null){
					listener.onUploadError();
				}else{
					activity.runOnUiThread(new Runnable(){
						@Override
						public void run() {
							listener.onUploadError();
						}
					});
				}
			}
		}
		return response;
	} 
	public void cancel(){
		post.cancel();
	}
	public String getUrl() {
		return url;
	}
	public boolean getSyncModus() {
		return syncModus;
	}
	public void setSyncModus(boolean syncModus) {
		this.syncModus = syncModus;
	}
	@Override
	public void onProgress(final int progress) {
		if(listener != null){
			if(activity == null){
				listener.onUploadProgress(progress);
			}else{
				activity.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						listener.onUploadProgress(progress);
					}
				});
			}
		}
	}
}
