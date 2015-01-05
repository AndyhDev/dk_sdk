package com.dk.util.network;

import android.graphics.Bitmap;

public abstract interface DownloadImageListener {
	public abstract void onDownloadImageProgress(int progress);
	public abstract void onDownloadImageEnd(Bitmap bitmap);
	public abstract void onDownloadImageError();
}
