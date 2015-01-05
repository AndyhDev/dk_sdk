package com.dk.ui.widgets;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dk.util.PathSplitter;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcel;
import android.os.Parcelable;

public class SelectedFolderList implements Parcelable {
	public static final String folderKey = "folderKey";
	
	private List<String> folders = new ArrayList<String>();
	private PathSplitter splitter = new PathSplitter();
	
	public SelectedFolderList(){
    }
	
	public void add(File folder){
		folders.add(folder.getAbsolutePath());
	}
	public void add(String path){
		folders.add(path);
	}
	public File get(int index){
		return new File(folders.get(index));
	}
	public int size(){
		return folders.size();
	}
	public boolean isFolderIn(File folder){
		return folders.contains(folder.getAbsolutePath());
	}
	public boolean isFileOK(File file){
		boolean result = false;
		List<File> parents = splitter.getParentDirectorys(file);
		for(int i = 0; i < parents.size(); i++){
			if(isFolderIn(parents.get(i))){
				result = true;
				break;
			}
		}
		return result;
	}
	public void removeFolder(File folder){
		folders.remove(folder.getAbsolutePath());
	}
	public void removeFolder(String path){
		folders.remove(path);
	}
	public void writeToSettings(String key, SharedPreferences settings){
		Editor editor = settings.edit();
		Set<String> set = new HashSet<String>();
		set.addAll(folders);
		editor.putStringSet(key, set);
		editor.commit();
	}
	public void readFromSettings(String key, SharedPreferences settings){
		Set<String> keys = settings.getStringSet(key, null);
		if(keys != null){
			folders = new ArrayList<String>(keys);
		}
	}
	public void addList(SelectedFolderList list){
		for(int i = 0; i < list.size(); i++){
			File f = list.get(i);
			if(!isFolderIn(f)){
				add(f);
			}
		}
	}
    protected SelectedFolderList(Parcel in) {
        if (in.readByte() == 0x01) {
            folders = new ArrayList<String>();
            in.readList(folders, String.class.getClassLoader());
        } else {
            folders = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (folders == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(folders);
        }
    }

    public static final Parcelable.Creator<SelectedFolderList> CREATOR = new Parcelable.Creator<SelectedFolderList>() {
        @Override
        public SelectedFolderList createFromParcel(Parcel in) {
            return new SelectedFolderList(in);
        }

        @Override
        public SelectedFolderList[] newArray(int size) {
            return new SelectedFolderList[size];
        }
    };
}