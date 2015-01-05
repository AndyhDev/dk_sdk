package com.dk.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dk.sdk.R;

public class Click extends Preference{
	private View layout;

	public Click(Context context, AttributeSet attrs) {
		super(context, attrs);

	}
	
	@Override
	protected View onCreateView(ViewGroup parent) {
		LayoutInflater li = (LayoutInflater)getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		layout = li.inflate(R.layout.click_preference, parent, false);
		return layout;
	}
	
}
