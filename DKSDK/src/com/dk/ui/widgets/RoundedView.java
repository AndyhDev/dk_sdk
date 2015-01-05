package com.dk.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.dk.style.dKColor;
import com.dk.util.UiHelper;

public class RoundedView extends View{
	private int width = UiHelper.getPx(getContext(), 100);
	private int height = UiHelper.getPx(getContext(), 40);
	private Paint paint;
	private int bgColor = dKColor.BLUE;
	private RectF rect;
	private int r;

	public RoundedView(Context context) {
        super(context);
        init();
    }
	
    public RoundedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init(){
    	setWillNotDraw(false);
    	paint = new Paint();
		paint.setAntiAlias(true);
    	paint.setStyle(Paint.Style.FILL);
    	paint.setColor(bgColor);
    }
    
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w;
		height = h;
		rect = new RectF(0, 0, width, height);
		r = (int) (Math.min(height, width)*0.2);
		invalidate();
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
    
    public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
		paint.setColor(bgColor);
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
    	canvas.drawRoundRect(rect, r, r, paint);
	    
	    
	}

}
