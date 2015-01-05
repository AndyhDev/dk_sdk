package com.dk.util.network;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.util.Log;

public class Download {
	private String url; 
	private HttpClient httpClient;
	private HttpPost request;
	private DownloadListener listener;
	private MultipartEntityBuilder params;
	private Activity activity;
	private int lastPost = -1;
	private boolean syncModus = false;
	private boolean active = true;
	
	public Download(Activity activity, String url){
		this.activity = activity;
		this.url = url;
		httpClient = new DefaultHttpClient();
		request = new HttpPost(url);
		params = MultipartEntityBuilder.create();
		params.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	}
	public Download(String url){
		this.url = url;
		httpClient = new DefaultHttpClient();
		request = new HttpPost(url);
		params = MultipartEntityBuilder.create();
		params.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	}
	public void addParam(String paramName, String paramValue){
		params.addTextBody(paramName, paramValue);
	}
	public void start(){
		request.setEntity(params.build());
		
		if(syncModus){
			doDown();
		}else{
			new Thread(new Runnable(){
				public void run() {
					doDown();
				}
			}).start();
		}
	}
	public void stop(){
		active = false;
	}
	private void doDown(){
		try {
			HttpResponse response = httpClient.execute(request);
			if(response.getStatusLine().getStatusCode() != 200){
				postError();
				return;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				long size = entity.getContentLength();
				Log.d("contentSize", "contentSize:" + size);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				BufferedInputStream bis = new BufferedInputStream(entity.getContent());
				long readed = 0;
				byte[] b = new byte[4096];
				
				while (bis.read(b) != -1){
					out.write(b);
					readed += b.length;
					postProgress(size, readed);
					if(!active){
						postError();
						return;
					}
				}

				bis.close();
				out.close();
				postEnd(new DownloadResult(out));
			}else{
				postError();
			}
		} catch (ClientProtocolException e) {
			postError();
			e.printStackTrace();
		} catch (IOException e) {
			postError();
			e.printStackTrace();
		}
	}
	private void postProgress(long totalSize, long readed){
		int progress = (int) (((float) readed / (float) totalSize) * 100);
		if(progress > 100){
			progress = 100;
		}
		final int pr = progress;
		if(pr != lastPost){
			if(listener != null){
				if(activity == null){
					lastPost = pr;
					listener.onDownloadProgress(pr);
				}else{
					activity.runOnUiThread(new Runnable(){
						@Override
						public void run() {
							lastPost = pr;
							listener.onDownloadProgress(pr);
						}
					});
				}
			}
		}
	}
	private void postError(){
		if(listener != null){
			if(activity == null){
				listener.onDownloadError();
			}else{
				activity.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						listener.onDownloadError();
					}
				});
			}
		}
	}
	private void postEnd(final DownloadResult result){
		if(listener != null){
			if(activity == null){
				listener.onDownloadEnd(result);
			}else{
				activity.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						listener.onDownloadEnd(result);
					}
				});
			}
		}
	}
	public void setListener(DownloadListener listener){
		this.listener = listener;
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
}
