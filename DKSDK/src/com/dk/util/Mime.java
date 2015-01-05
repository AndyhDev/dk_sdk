package com.dk.util;

import java.io.File;

import android.net.Uri;
import android.webkit.MimeTypeMap;

public class Mime {
	public static String getMime(File f){
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(f).toString());
		if (extension != null) {
		    MimeTypeMap mime = MimeTypeMap.getSingleton();
		    type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}
	public static String getMime(Uri uri){
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
		if (extension != null) {
		    MimeTypeMap mime = MimeTypeMap.getSingleton();
		    type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}
	public static String getMime(String path){
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(path)).toString());
		if (extension != null) {
		    MimeTypeMap mime = MimeTypeMap.getSingleton();
		    type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}
	public static String getMime(File f, String notExists){
		String mime = getMime(f);
		if(mime == null){
			return notExists;
		}
		return mime;
	}
	public static String getMime(Uri uri, String notExists){
		String mime = getMime(uri);
		if(mime == null){
			return notExists;
		}
		return mime;
	}
	public static String getMime(String path, String notExists){
		String mime = getMime(path);
		if(mime == null){
			return notExists;
		}
		return mime;
	}
}
