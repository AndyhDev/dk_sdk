package com.dk.ui.widgets;


import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dk.sdk.R;
import com.dk.style.dKColor;
import com.dk.util.PathSplitter;

public class FolderSelector extends Activity implements OnClickListener, FolderSelectorListener{
	private static final String TAG = "FolderSelector";
	
	private ListView folderList;
	private LinearLayout folderStruct;
	private FolderAdapter adapter;
	private PathSplitter pathSplitter = new PathSplitter();
	private SelectedFolderList selected = new SelectedFolderList();
	
	private File curDir;
	public static final String dirKey = "dirKey";
	public static final String action = "FolderSelectorAction";
	public static final String selectedKey = "selectedKey";
	
	private int folderStructButtonId = 4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.folder_selector);
		
		getActionBar().setIcon(R.drawable.folder);
		getActionBar().setBackgroundDrawable(new ColorDrawable(dKColor.ORANGE));
		adapter = new FolderAdapter(this, null);
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
			Intent intent = getIntent();
			if(intent != null){
				String dir = intent.getStringExtra(dirKey);
				if(dir != null){
					curDir = new File(dir);
				}else{
					curDir = new File("/");
					SelectedFolderList sel = intent.getParcelableExtra(selectedKey);
					if(sel != null){
						selected = sel;
						adapter.setSelectedFolderList(selected);
					}
				}
			}else{
				curDir = new File("/");
			}
		}
		loadDir(curDir);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.folder_selector, menu);
		
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_ok){
			Intent data = new Intent();
			data.setAction(action);
			data.putExtra(selectedKey, selected);
			
			setResult(RESULT_OK, data);
			finish();
		}
		return super.onOptionsItemSelected(item);
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
	
}
