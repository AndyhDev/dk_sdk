package com.dk.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.dk.style.dKColor;
import com.dk.util.UiHelper;

public class ProgressBar extends View {
	private final String TAG = "ProgressBar";
	private int width = UiHelper.getPx(getContext(), 100);
	private int height = UiHelper.getPx(getContext(), 20);
	private int radius = UiHelper.getPx(getContext(), 6);
	private int border = UiHelper.getPx(getContext(), 1);;
	
	private Paint paintFill;
	private Paint paintBorder;
	private RectF rectFill;
	private RectF rectBorder;
	
	private int borderColor = dKColor.DARKER_GRAY;
	private int fillColor = dKColor.GREEN;
	private int progress = 0;
	
	public ProgressBar(Context context) {
        super(context);
        init();
    }
	
    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init(){
    	setWillNotDraw(false);
    	paintFill = new Paint();
    	paintBorder = new Paint();
    	
    	paintFill.setAntiAlias(true);
    	paintBorder.setAntiAlias(true);
    	
    	paintFill.setStyle(Paint.Style.FILL);
    	paintBorder.setStyle(Paint.Style.STROKE);
    	paintBorder.setStrokeWidth(border);
    	
    	paintFill.setColor(fillColor);
    	paintBorder.setColor(borderColor);
    }
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.d(TAG, "onSizeChanged(int " + w + ", int" + h + ", int " + oldw + ", int " + oldh + ")");
		width = w;
		height = h;
		radius = Math.min(h, w) / 3;
		
		rectBorder = new RectF(border, border, width - border, height - border);
		if(progress == 0){
			rectFill = new RectF(0, 0, 0, 0);
		}else{
			int fill = (int) ((float)(width - border - border) / (float)100 * progress);
			Log.d(TAG, "fill:" + fill);
			rectFill = new RectF(border, border, fill, height - border);
		}
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(TAG, "onDraw");

	    canvas.drawRoundRect(rectFill, radius, radius, paintFill);

	    canvas.drawRoundRect(rectBorder, radius, radius, paintBorder);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Log.d(TAG, "onSaveInstanceState");
		return super.onSaveInstanceState();
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Log.d(TAG, "onRestoreInstanceState");
		super.onRestoreInstanceState(state);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int desiredWidth = UiHelper.getPx(getContext(), 100);
	    int desiredHeight = UiHelper.getPx(getContext(), 20);

	    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

	    int width;
	    int height;

	    if (widthMode == MeasureSpec.EXACTLY) {
	        width = widthSize;
	    } else if (widthMode == MeasureSpec.AT_MOST) {
	        width = Math.min(desiredWidth, widthSize);
	    } else {
	        width = desiredWidth;
	    }

	    if (heightMode == MeasureSpec.EXACTLY) {
	        height = heightSize;
	    } else if (heightMode == MeasureSpec.AT_MOST) {
	        height = Math.min(desiredHeight, heightSize);
	    } else {
	        height = desiredHeight;
	    }
	    
	    setMeasuredDimension(width, height);
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		paintBorder.setColor(borderColor);
	}

	public int getFillColor() {
		return fillColor;
	}

	public void setFillColor(int fillColor) {
		this.fillColor = fillColor;
		paintFill.setColor(fillColor);
	}
	
	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		if(progress == 0){
			rectFill = new RectF(0, 0, 0, 0);
		}else{
			int fill = (int) ((float)(width - border - border) / (float)100 * progress);
			Log.d(TAG, "fill:" + fill);
			rectFill = new RectF(border, border, fill + border, height - border);
		}
		invalidate();
	}
}
