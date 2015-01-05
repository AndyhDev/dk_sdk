package com.dk.util;

import android.annotation.SuppressLint;

public class FileSize {
	@SuppressLint("DefaultLocale")
	public static String humanReadableSize(long bytes) {
	    int unit = 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = ("KMGTPE").charAt(exp-1) + "";
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
