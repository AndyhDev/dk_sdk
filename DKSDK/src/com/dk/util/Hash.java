package com.dk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class Hash {
public static String getMd5(String content){
		MessageDigest digester;
		try {
			digester = MessageDigest.getInstance( "MD5" );
		
			final byte[] prehashBytes= content.getBytes( "iso-8859-1" );
			
			digester.update( prehashBytes );
	
			final byte[] hash = digester.digest( );
	
			final StringBuffer hexString = new StringBuffer();
	
			for (int i = 0; i < hash.length; i++) {
	            if ((0xff & hash[i]) < 0x10) {
	                hexString.append("0"
	                        + Integer.toHexString((0xFF & hash[i])));
	            } else {
	                hexString.append(Integer.toHexString(0xFF & hash[i]));
	            }
	        }
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}
	public static String getMd5FromFile(String path){
		return getMd5FromFile(new File(path));
	}
	
	public static String getMd5FromFile(File f){
		FileInputStream fis;
		try {
			fis = new FileInputStream(f);
			String md5 = new String(Hex.encodeHex(DigestUtils.md5(fis)));
			fis.close();
			return md5;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
