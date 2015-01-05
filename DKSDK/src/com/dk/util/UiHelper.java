package com.dk.util;

import android.content.Context;

public class UiHelper {
	public static int getPx(Context context, int dp){
	    float density = context.getResources().getDisplayMetrics().density;
	    return Math.round((float)dp * density);
	}
}
