package com.dk.util.network;

import java.io.ByteArrayOutputStream;

import org.json.JSONObject;

public abstract interface ApiDownloadListener {
	public abstract void onApiDownloadSuccess(JSONObject data);
	public abstract void onApiDownloadProgress(int progress);
	public abstract void onApiDownloadError(int code);
	public abstract void onApiDownloadSuccessRaw(ByteArrayOutputStream st);
}