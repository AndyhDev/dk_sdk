package com.dk.util;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class PathSplitter {
	public List<String> getPathStrings(String path){
		return getPathStrings(new File(path));
	}
    public List<String> getPathStrings(File f){
        LinkedList<String> list = new LinkedList<String>();
        File p = f;
        while (p != null){
        	String name = p.getName();
        	if(name.isEmpty()){
        		name = "/";
        	}
            list.addFirst(name);
            p = p.getParentFile();
        }
        return list;
    }
    public List<File> getParentDirectorys(String path){
    	return getParentDirectorys(new File(path));
    }
    public List<File> getParentDirectorys(File f){
    	LinkedList<File> list = new LinkedList<File>();
        File p = f;
        while (p != null){
            list.addFirst(p);
            p = p.getParentFile();
        }
        return list;
    }
}

