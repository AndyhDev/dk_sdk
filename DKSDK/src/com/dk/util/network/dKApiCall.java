package com.dk.util.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

public class dKApiCall implements RequestListener {
	@SuppressWarnings("unused")
	private final String TAG = "dKApiCall";
	private String command;
	private dKSession session;
	private Activity activity;
	private String url;
	private ApiCallListener listener;
	private Request request;
	private String userName;
	private String passw;
	
	public dKApiCall(Activity activity, dKSession session, String command){
		this.activity = activity;
		this.command = command;
		this.session = session;
		
		url = "https://dk-force.de/api/" + command + ".php" + session.getUrlGET();
		request = new Request(activity, url);
		request.setListener(this);
	}
	public dKApiCall(Activity activity, String command, String userName, String passw){
		this.activity = activity;
		this.command = command;
		this.session = null;
		this.userName = userName;
		this.passw = passw;
		
		request = new Request(activity);
		request.setListener(this);

	}
	public dKApiCall(dKSession session, String command){
		this.command = command;
		this.session = session;
		
		url = "https://dk-force.de/api/" + command + ".php" + session.getUrlGET();
		request = new Request(url);
		request.setListener(this);
	}
	public dKApiCall(String command, String userName, String passw){
		this.command = command;
		this.session = null;
		this.userName = userName;
		this.passw = passw;
		
		request = new Request();
		request.setListener(this);

	}
	public void call(){
		if(session == null){
			dKLogin login;
			if(activity != null){
				login = new dKLogin(activity, userName, passw);
			}else{
				login = new dKLogin(userName, passw);
			}
			login.setListener(new LoginListener() {
				@Override
				public void onLoginSuccess(dKSession session) {
					dKApiCall.this.session = session;
					url = "https://dk-force.de/api/" + command + ".php" + session.getUrlGET();
					request.setUrl(url);
					request.start();
				}
				
				@Override
				public void onLoginError(int code) {
					if(listener != null){
						listener.onApiCallError(dKSession.ERROR_UNKNOWN);
					}
				}
			});
			login.login();
		}else{
			request.start();
		}
	}
	public void addParam(String paramName, String paramValue){
		request.addParam(paramName, paramValue);
	}
	public dKSession getSession(){
		return session;
	}
	public String getCommand() {
		return command;
	}
	public String getUrl() {
		return url;
	}
	public void setListener(ApiCallListener listener){
		this.listener = listener;
	}

	@Override
	public void onRequestEnd(String msg) {
		Log.d("response", "res:" + msg);
		if(msg.startsWith("error:")){
			String code = msg.replace("error:", "").trim();
			int errorCode = dKSession.ERROR_UNKNOWN;
			
			try{ 
				errorCode = Integer.parseInt(code); 
		    }catch(NumberFormatException e){ 
		        errorCode = dKSession.ERROR_UNKNOWN;
		    }
			if(listener != null){
				listener.onApiCallError(errorCode);
			}
		}else{
			try {
				JSONObject data = new JSONObject(msg);
				listener.onApiCallSuccess(data);
			} catch (JSONException e) {
				e.printStackTrace();
				if(listener != null){
					listener.onApiCallError(dKSession.ERROR_RESPONSE);
				}
			}
		}
	}

	@Override
	public void onRequestError() {
		if(listener != null){
			listener.onApiCallError(dKSession.ERROR_UNKNOWN);
		}
	}
	public boolean getSyncModus() {
		return request.getSyncModus();
	}

	public void setSyncModus(boolean syncModus) {
		request.setSyncModus(syncModus);
	}
}
