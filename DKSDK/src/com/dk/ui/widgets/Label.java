package com.dk.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.dk.style.dKColor;
import com.dk.util.UiHelper;

public class Label extends View implements OnTouchListener {
	private final String TAG = "ColorButton";
	
	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_CENTER = 2;
	
	private int width = UiHelper.getPx(getContext(), 100);
	private int height = UiHelper.getPx(getContext(), 40);
	
	private int bgColor = dKColor.RED;
	private int textColor = dKColor.LIGHT_GRAY;
	private Rect in;
	private boolean hover = false;
	private int align = ALIGN_LEFT; 
	private String text = "dK";
	private Rect bg = new Rect(0, 0, width, height);;
	private Paint paint;
	private Paint textPaint;

	public Label(Context context) {
        super(context);
        init();
    }
	
    public Label(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init(){
    	setWillNotDraw(false);
    	setOnTouchListener(this);
    	
    	paint = new Paint();
		paint.setAntiAlias(true);
    	paint.setStyle(Paint.Style.FILL);
    	
    	textPaint = new Paint();
	    textPaint.setAntiAlias(true);
    }
    
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w;
		height = h;
		bg = new Rect(0, 0, width, height);
		invalidate();
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
	
	public void setText(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	public int getTextAlign() {
		return align;
	}

	public void setTextAlign(int align) {
		this.align = align;
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
    
    @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

    	paint.setColor(bgColor);
	    canvas.drawRect(bg, paint);
	    
	    textPaint.setColor(textColor);
	    textPaint.setTextSize((float)(height*0.7));
	    
	    int xPos = 0;
	    if(align == ALIGN_LEFT){
	    	textPaint.setTextAlign(Align.LEFT);
	    	xPos = 0;
	    }else if(align == ALIGN_RIGHT){
		    textPaint.setTextAlign(Align.LEFT);
		    xPos = (int) (canvas.getWidth() - textPaint.measureText(text));
	    }else if(align == ALIGN_CENTER){
	    	textPaint.setTextAlign(Align.CENTER);
	    	xPos = (canvas.getWidth() / 2);
	    }
	   
	    int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
	    canvas.drawText(text, xPos, yPos, textPaint);
	}
    
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int eventaction = event.getAction();

	    switch (eventaction) {
	        case MotionEvent.ACTION_DOWN:
	        	in = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
	        	hover = true;
	            break;

	        case MotionEvent.ACTION_MOVE:
	        	if((in.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))){
	        		hover = true;
	        	}else{
	        		hover = false;
	        	}
	            break;
	        case MotionEvent.ACTION_UP:
	        	if(hover){
		        	Log.d(TAG, "click");
		        	v.performClick();
	        	}
	            break;
	    }
	    return true;
	}
	@Override
	public boolean performClick() {
		super.performClick();
		
	    return true;
	}

}
