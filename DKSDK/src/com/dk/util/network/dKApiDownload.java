package com.dk.util.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

public class dKApiDownload implements DownloadListener {
	private Activity activity;
	private dKSession session;
	private Download down;
	private ApiDownloadListener listener;
	private String fileName;
	private String filePath;
	private boolean getRawData = false;
	
	public dKApiDownload(Activity activity, dKSession session){
		this.activity = activity;
		this.session = session;
		
		down = new Download(activity, "https://dk-force.de/api/download.php" + session.getUrlGET());
		down.setListener(this);
	}
	public dKApiDownload(dKSession session){
		this.session = session;
		
		down = new Download("https://dk-force.de/api/download.php" + session.getUrlGET());
		down.setListener(this);
	}
	public dKApiDownload(Activity activity, dKSession session, String command){
		this.activity = activity;
		this.session = session;
		
		down = new Download(activity, "https://dk-force.de/api/" + command + ".php" + session.getUrlGET());
		down.setListener(this);
	}
	public dKApiDownload(dKSession session, String command){
		this.session = session;
		
		down = new Download("https://dk-force.de/api/" + command + ".php" + session.getUrlGET());
		down.setListener(this);
	}
	public void download(){
		down.start();
	}
	public void stop(){
		down.stop();
	}
	public Activity getActivity() {
		return activity;
	}

	public dKSession getSession() {
		return session;
	}
	
	public void addParam(String paramName, String paramValue){
		down.addParam(paramName, paramValue);
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public void setListener(ApiDownloadListener listener){
		this.listener = listener;
	}
	
	public boolean getSyncModus() {
		return down.getSyncModus();
	}
	public void setSyncModus(boolean syncModus) {
		down.setSyncModus(syncModus);
	}
	@Override
	public void onDownloadProgress(int progress) {
		if(listener != null){
			if(progress > 100){
				progress = 100;
			}
			listener.onApiDownloadProgress(progress);
		}
	}
	@Override
	public void onDownloadEnd(DownloadResult result) {
		try {
			if(listener != null){
				if(getRawData){
					listener.onApiDownloadSuccessRaw(result.getSt());
				}else{
					JSONObject data = new JSONObject(result.getSt().toString());
					listener.onApiDownloadSuccess(data);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			if(listener != null){
				listener.onApiDownloadError(dKSession.ERROR_RESPONSE);
			}
		}
	}
	@Override
	public void onDownloadError() {
		if(listener != null){
			listener.onApiDownloadError(dKSession.ERROR_UNKNOWN);
		}
	}
	public boolean getGetRawData() {
		return getRawData;
	}
	public void setGetRawData(boolean getRawData) {
		this.getRawData = getRawData;
	}
}
