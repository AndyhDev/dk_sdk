package com.dk.util.network;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DownloadImage implements DownloadListener {
	private Activity activity;
	private String url;
	private Download download;
	private DownloadImageListener listener;
	
	public DownloadImage(Activity activity, String url){
		this.activity = activity;
		this.url = url;
		download = new Download(this.activity, url);
	}
	public DownloadImage(String url){
		this.url = url;
		download = new Download(url);
	}
	
	public void start(){
		download.setListener(this);
		download.start();
	}
	public String getUrl(){
		return url;
	}
	public void setListener(DownloadImageListener listener){
		this.listener = listener;
	}
	@Override
	public void onDownloadProgress(int progress) {
		if(listener != null){
			listener.onDownloadImageProgress(progress);
		}
		
	}
	@Override
	public void onDownloadEnd(DownloadResult result) {
		if(listener != null){
			byte[] data = result.getSt().toByteArray();
			Bitmap bmp;
			try{
				bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
			}catch (Exception e){
				e.printStackTrace();
				listener.onDownloadImageError();
				return;
			}
			listener.onDownloadImageEnd(bmp);
		}
	}
	@Override
	public void onDownloadError() {
		if(listener != null){
			listener.onDownloadImageError();
		}
	}
}
