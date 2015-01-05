package com.dk.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dk.style.dKColor;
import com.dk.util.UiHelper;

public class ColorPicker extends View{
	private int width = UiHelper.getPx(getContext(), 100);
	private int height = UiHelper.getPx(getContext(), 100);
	private int step = width/4;
	private int color;
	
	private Paint paint;
	private Paint actPaint;
	private RectF out;
	
	private final int[] colors = new int[] {
			0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
			0xFFFFFF00, 0xFFFF0000
	};
	private Paint oldPaint;
	private RectF inner;
	
	public ColorPicker(Context context) {
		super(context);
		init();
	}
	public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
	
	private void init() {
		setWillNotDraw(false);
    	
    	
    	paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(new SweepGradient(0, 0, colors, null));
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(step);
    	
		actPaint = new Paint();
		actPaint.setAntiAlias(true);
		actPaint.setStyle(Paint.Style.FILL);
		actPaint.setColor(dKColor.DARK_BLUE);
		
		oldPaint = new Paint();
		oldPaint.setAntiAlias(true);
		oldPaint.setStyle(Paint.Style.FILL);
		oldPaint.setColor(dKColor.DARK_BLUE);
		
		int r = width/2 - step/2 - UiHelper.getPx(getContext(), 10);
		out = new RectF(-r, -r, r, r);
		
		r = step/2;
		inner = new RectF(-r, -r, r, r);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w;
		height = h;
		step = width/4;
		
		int r = width/2 - step/2 - UiHelper.getPx(getContext(), 10);
		out = new RectF(-r, -r, r, r);
		paint.setStrokeWidth(step);
		
		r = step/2;
		inner = new RectF(-r, -r, r, r);
		
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

    	paint.setColor(dKColor.DARK_ORANGE);
    	canvas.translate(width/2, height/2);

		canvas.drawOval(out, paint);
	    
		//canvas.drawCircle(0, 0, step/2, actPaint);
		
		
		canvas.drawArc(inner, -90, 180, true, actPaint);
		canvas.drawArc(inner, 90, 180, true, oldPaint);
	}
	
	private int ave(int s, int d, float p) {
		return s + Math.round(p * (d - s));
	}
	
	private int interpColor(int colors[], float unit) {
		if (unit <= 0) {
			return colors[0];
		}

		if (unit >= 1) {
			return colors[colors.length - 1];
		}

		float p = unit * (colors.length - 1);
		int i = (int) p;
		p -= i;

		int c0 = colors[i];
		int c1 = colors[i + 1];
		int a = ave(Color.alpha(c0), Color.alpha(c1), p);
		int r = ave(Color.red(c0), Color.red(c1), p);
		int g = ave(Color.green(c0), Color.green(c1), p);
		int b = ave(Color.blue(c0), Color.blue(c1), p);

		return Color.argb(a, r, g, b);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX() - getRootView().getWidth()/2;
		float y = event.getY() - width/2;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			float angle = (float) java.lang.Math.atan2(y, x);
			// need to turn angle [-PI ... PI] into unit [0....1]
			float unit = (float)(angle / (2 * Math.PI));

			if (unit < 0) {
				unit += 1;
			}

			color = interpColor(colors, unit);
			actPaint.setColor(color);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			//ColorDialogPreference.this.color = centerPaint.getColor();
			break;
		}

		return true;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int desiredWidth = UiHelper.getPx(getContext(), 100);
	    int desiredHeight = UiHelper.getPx(getContext(), 100);

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
	    width = Math.max(width, height);
	    height = width;
	    
	    //setMeasuredDimension(width, height);
	    super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        );
	}
	public int getColor(){
		return color;
	}
	public void setColor(int color) {
		this.color = color;
		actPaint.setColor(color);
		oldPaint.setColor(color);
	}

}
