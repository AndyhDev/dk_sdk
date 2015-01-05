package com.dk.ui.widgets;


import java.io.File;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dk.sdk.R;
import com.dk.util.PathSplitter;

public class FolderSelectorDlg extends Dialog implements OnClickListener, FolderSelectorListener{
	private static final String TAG = "FolderSelector";
	
	private ListView folderList;
	private LinearLayout folderStruct;
	private FolderAdapter adapter;
	private PathSplitter pathSplitter = new PathSplitter();
	private SelectedFolderList selected = new SelectedFolderList();
	private ImageView icon;
	private ImageButton okBnt;
	
	private onOkListener listener;
	
	private File curDir;
	public static final String dirKey = "dirKey";
	public static final String action = "FolderSelectorAction";
	public static final String selectedKey = "selectedKey";
	
	private int folderStructButtonId = 4;

	private Bitmap iconBmp;
	
	public FolderSelectorDlg(Context context, SelectedFolderList list, Bitmap icon) {
		super(context);
		selected = list;
		iconBmp = icon;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.folder_selector_dlg);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		
		setCancelable(false);
		
		icon = (ImageView) findViewById(R.id.imageView1);
		icon.setImageBitmap(iconBmp);
		
		okBnt = (ImageButton) findViewById(R.id.imageButton1);
		okBnt.setOnClickListener(this);
		
		adapter = new FolderAdapter(getContext(), null);
		adapter.setSelectedFolderList(selected);
		adapter.setListener(this);
		
		folderList = (ListView) findViewById(R.id.folder);
		folderList.setAdapter(adapter);
		folderStruct = (LinearLayout) findViewById(R.id.folderStruct);
		
		if(savedInstanceState != null){
			String dir = savedInstanceState.getString(dirKey);
			if(dir != null){
				curDir = new File(dir);
			}else{
				curDir = new File("/");
			}
		}else{
			curDir = new File("/");
		}
		loadDir(curDir);
	}
	
	private void loadDir(File dir) {
		curDir = dir;
		adapter.setDir(dir);
		adapter.notifyDataSetChanged();
		makeDirStruct();
	}

	private void makeDirStruct() {
		folderStruct.removeAllViews();
		List<String> segments = pathSplitter.getPathStrings(curDir);
		File tmpPath = new File("/");
		for(int i = 0; i < segments.size(); i++){
			String name = segments.get(i);
			Log.d(TAG, "name: " + name);
			tmpPath = new File(tmpPath, name);
			//Button b = new Button(this, null,  R.style.ButtonOrange);
			//b.setBackgroundResource(R.style.ButtonOrange);
			Button b = (Button) getLayoutInflater().inflate(R.layout.orange_button, folderStruct, false);
			b.setText(name);
			b.setId(folderStructButtonId);
			b.setTag(tmpPath);
			b.setOnClickListener(this);
			folderStruct.addView(b);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        File pDir = curDir.getParentFile();
	        if(pDir != null){
	        	loadDir(pDir);
	        	return true;
	        }
	        
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == folderStructButtonId){
			File tmpFile = (File) v.getTag();
			loadDir(tmpFile);
		}else if(id == R.id.imageButton1){
			if(listener != null){
				listener.onOk(selected);
			}
			dismiss();
		}
	}

	@Override
	public void onFolderClicked(File folder) {
		loadDir(folder);
	}

	@Override
	public void onFolderChecked(File folder, boolean checked) {
		if(checked){
			if(!selected.isFolderIn(folder)){
				selected.add(folder);
			}
		}else{
			Log.d(TAG, "removeFolder: " + folder.getName());
			selected.removeFolder(folder);
		}
		Log.d(TAG, "selectedSize: " + selected.size());
		adapter.setSelectedFolderList(selected);
	}

	public void setListener(onOkListener listener) {
		this.listener = listener;
	}
	
}
