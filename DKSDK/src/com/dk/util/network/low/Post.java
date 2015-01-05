package com.dk.util.network.low;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class Post {
	private HttpURLConnection conn;
	private OutputStreamProgress dos;
	
	private String lineEnd = "\r\n";
	private String twoHyphens = "--";
	private String boundary = "**********" + System.currentTimeMillis();
	private String url;
	private HashMap<String, String> params = new HashMap<String, String>();
	private HashMap<String, File> files = new HashMap<String, File>();
	private long size = 0L;
	//private String charset = "UTF-8";
	private PostListener listener;
	private OutputStreamProgressListener outListener;
	private int lastProgress = -1;
	private int statusCode = 0;
	private static final int MAX_RETRIES = 6;
	private volatile boolean stop = false;
	private Lock lock = new ReentrantLock();
	private boolean stoped = false;
	
	public Post(){
	}
	public Post(String url){
		this.url = url;
	}
	public boolean addParam(String name, String value){
		params.put(name, value);
		return true;

	}
	public static String getMime(File f){
		String type = "application/octet-stream";
		String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(f).toString());
		if (extension != null) {
		    MimeTypeMap mime = MimeTypeMap.getSingleton();
		    type = mime.getMimeTypeFromExtension(extension);
		}
		if(type == null){
			return "application/octet-stream";
		}
		return type;
	}
	public boolean addFile(String name, File f){
		if(f.exists()){
			String mime = getMime(f);
			String header = twoHyphens + boundary + lineEnd;
			header += "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + name + "\"" + lineEnd;
			header += "Content-Type: " + mime + lineEnd;
			header += "Content-Transfer-Encoding: binary" + lineEnd + lineEnd;
			files.put(header, f);
			size += f.length();
			size += header.length();
		}
		return false;
	}
	private String getParams(){
		if(params.size() > 0){
			String en = "";
			Iterator<Entry<String, String>> it = params.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
			    en += twoHyphens + boundary + lineEnd;
				en += "Content-Disposition: form-data; name=\"" + pairs.getKey() + "\"" + lineEnd + lineEnd;
				en += pairs.getValue() + lineEnd;
			}
			size += en.length();
			return en;
		}else{
			return "";
		}
	}
	public void cancel(){
		lock.lock();
		stop = true;
		lock.unlock();
	}
	public String run(){
		int trys = 0;
		while(trys < MAX_RETRIES){
		    try {
				conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setChunkedStreamingMode(0);
				conn.setDoInput(true);
		        conn.setDoOutput(true);
		        conn.setUseCaches(false);
		        conn.setRequestMethod("POST");
		        conn.setRequestProperty("Connection", "Keep-Alive");
		        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
		        
		        dos = new OutputStreamProgress(conn.getOutputStream());
		        outListener = new OutputStreamProgressListener() {
					@Override
					public void onWrite(Long sended) {
						lock.lock();
						if(stop){
							lock.unlock();
							stoped = true;
							conn.disconnect();
						}else{
							lock.unlock();
						}
						if(listener != null){
							int progress = (int)((float) 100 / size * sended);
							if(progress != lastProgress){
								lastProgress = progress;
								listener.onProgress(progress);
							}
						}
					}
				};
				dos.setListener(outListener);
		        dos.write(getParams().getBytes());
		        dos.flush();
		        
		        Iterator<Entry<String, File>> it = files.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, File> pairs = (Map.Entry<String, File>)it.next();
					dos.write(pairs.getKey().getBytes());
					
					 FileInputStream inputStream = new FileInputStream(pairs.getValue());
				     byte[] buffer = new byte[1024];
				     int bytesRead = -1;
				     while ((bytesRead = inputStream.read(buffer)) != -1) {
				    	 dos.write(buffer, 0, bytesRead);
				     }
				     
				     inputStream.close();
				     dos.write(lineEnd.getBytes());
				     dos.flush();
				     it.remove();
				}
		        dos.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes());
				
				dos.flush();
		        dos.close();
		        statusCode = conn.getResponseCode();
		        if(statusCode == HttpURLConnection.HTTP_OK){
		        	BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		        	String response = "";
		        	String line = null;
		            while ((line = reader.readLine()) != null) {
		                response += line + lineEnd;
		            }
		            reader.close();
		            conn.disconnect();
		            return response;
		        }
		        
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				Log.e("ERROR", "hallo");
				e.printStackTrace();
			} finally {
				if(conn != null){
	                conn.disconnect();
				}
	        }
		    trys++;
		}
	    return "";
		
	}
	
	public String getLineEnd() {
		return lineEnd;
	}
	public void setLineEnd(String lineEnd) {
		this.lineEnd = lineEnd;
	}
	public String getTwoHyphens() {
		return twoHyphens;
	}
	public void setTwoHyphens(String twoHyphens) {
		this.twoHyphens = twoHyphens;
	}
	public String getBoundary() {
		return boundary;
	}
	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getSize(){
		return size;
	}
	public void setListener(PostListener listener) {
		this.listener = listener;
	}
	public int getStatusCode(){
		return statusCode;
	}
	public boolean isStoped() {
		return stoped;
	}
}
