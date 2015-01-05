package com.dk.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.dk.sdk.R;
import com.dk.style.dKColor;
import com.dk.ui.widgets.RoundedView;

public class ColorPicker extends Preference{
	private Integer color = null;
	
	private String key = null;
	private View layout;
	private RoundedView colorPrev;
	private int fallbackDefaultValue = dKColor.RED;
	private SharedPreferences prefs;

	private Editor editor;

	public ColorPicker(Context context, AttributeSet attrs) {
		super(context, attrs);

		
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        key = getKey();
        if(key != null){
        	color = prefs.getInt(key, fallbackDefaultValue);
        }
	}
	@Override
	protected void onSetInitialValue(boolean restore, Object defaultValue) {
	    color = (restore ? getPersistedInt(fallbackDefaultValue) : (Integer) defaultValue);
	    setColor(color);
	}
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
	   return a.getInteger(index, fallbackDefaultValue);
	}
	
	@Override
	protected View onCreateView(ViewGroup parent) {
		LayoutInflater li = (LayoutInflater)getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		layout = li.inflate(R.layout.color_picker_preference, parent, false);
		
		colorPrev = (RoundedView)layout.findViewById(R.id.color_prev);
		if(color != null){
			colorPrev.setBgColor(color);
		}
		return layout;
	}
	
	public void setColor(Integer color){
		if(color != null){
			if(colorPrev != null){
				colorPrev.setBgColor(color);
			}
			this.color = color;
		}
	}
	public int getColor(){
		return color;
	}
	private void save(int color){
		if(key != null){
			editor.putInt(key, color);
			editor.commit();
			setColor(color);
		}
	}
	@Override
	protected void onClick() {
		super.onClick();
		ColorPickerDialog dlg = new ColorPickerDialog(getContext(), color);
		dlg.show();
		
		dlg.setListener(new OnColorListener() {
			@Override
			public void onColor(int color) {
				save(color);
			}
		});

	}
	
	public interface OnColorListener{
		public abstract void onColor(int color);
	}
	
	class ColorPickerDialog extends Dialog implements android.view.View.OnClickListener{
		private Button ok;
		private int curColor;
		private com.dk.ui.widgets.ColorPicker picker;
		private Button cancel;
		
		private OnColorListener listener;
		
		public ColorPickerDialog(Context context, int color) {
			super(context);
			this.curColor = color;
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		    requestWindowFeature(Window.FEATURE_NO_TITLE);
		    setContentView(R.layout.color_picker_dialog);
		    
		    picker = (com.dk.ui.widgets.ColorPicker)findViewById(R.id.colorPicker);
		    picker.setColor(curColor);
		    ok = (Button) findViewById(R.id.ok);
		    ok.setOnClickListener(this);
		    
		    cancel = (Button) findViewById(R.id.cancel);
		    cancel.setOnClickListener(this);


		}
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if(id == R.id.ok){
				if(listener != null){
					listener.onColor(picker.getColor());
				}
			}
			dismiss();
		}
		
		public void setListener(OnColorListener listener){
			this.listener = listener;
		}
	}
}
