package com.dk.ui.widgets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.sdk.R;

public class FolderAdapter extends BaseAdapter implements OnClickListener{
	private LayoutInflater layInf;
	@SuppressWarnings("unused")
	private Context context;
	private File curDir;
	private List<File> dirs = new ArrayList<File>();
	private FolderSelectorListener listener;
	private SelectedFolderList selected;
	
	public FolderAdapter(Context c, File dir){
		context = c;
		layInf = LayoutInflater.from(c);
		curDir = dir;
		loadDir();
	}
	public void setDir(File dir){
		curDir = dir;
		loadDir();
	}
	private void loadDir() {
		if(curDir == null){
			return;
		}
		File[] files = curDir.listFiles();
		dirs = new ArrayList<File>();
		
		if(files != null){
	         for(File path:files){
        	 	if(path.isDirectory()){
        	 		dirs.add(path);
        	 	}
	         }
		}
	}
	@Override
	public int getCount() {
		return dirs.size();
	}

	@Override
	public Object getItem(int position) {
		return dirs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		File dir = dirs.get(position);
		if(view == null){
			view = (RelativeLayout)layInf.inflate(R.layout.folder_item, parent, false);
		}
		
		TextView text = (TextView)view.findViewById(R.id.text);
		text.setTag(dir);
		text.setText(dir.getName());
		text.setOnClickListener(this);
		
		CheckBox box = (CheckBox)view.findViewById(R.id.checkBox);
		box.setTag(dir);
		box.setOnClickListener(this);
		box.setChecked(false);
		if(selected != null){
			if(selected.isFolderIn(dir)){
				box.setChecked(true);
			}
		}
		
		
		return view;

	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.text){
			if(listener != null){
				listener.onFolderClicked((File) v.getTag());
			}
		}else if(id == R.id.checkBox){
			if(listener != null){
				CheckBox box = (CheckBox) v;
				listener.onFolderChecked((File) v.getTag(),box.isChecked());
			}
		}
	}
	public void setListener(FolderSelectorListener listener) {
		this.listener = listener;
	}
	public void setSelectedFolderList(SelectedFolderList selected) {
		this.selected = selected;
	}
	
	
}
