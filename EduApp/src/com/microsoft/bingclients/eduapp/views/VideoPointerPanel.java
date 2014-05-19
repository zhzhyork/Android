package com.microsoft.bingclients.eduapp.views;

import com.microsoft.bingclients.eduapp.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class VideoPointerPanel extends View {
	
	private Context mContext;

	private Paint mPaint;
	
	private float[] mOffset;
	
	private int mDuration;
	
	private int mScroll;
	
	public VideoPointerPanel(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

    public VideoPointerPanel(Context context, float[] offset, int duration) {
        super(context);
        mContext = context;
        mOffset = offset;
        mDuration = duration;
        
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
    }
    
    public void setScroll(int scroll) {
    	mScroll = scroll;
    }
    
    public void addOffset(float offset) {
    	float[] newOffset = new float[mOffset.length + 1];
    	for (int i = 0; i < mOffset.length; i ++) {
    		newOffset[i] = mOffset[i];
    	}
    	
    	newOffset[mOffset.length] = offset;
    	mOffset = newOffset;
    }
    
    @Override
    public void onDraw(Canvas canvas) {
    	float width = ((View) getParent()).getWidth();
    	float margin = mContext.getResources().getDimension(R.dimen.video_pointer_margin);
    	float height = mContext.getResources().getDimension(R.dimen.video_pointer_height);
    	float blockWidth = mContext.getResources().getDimension(R.dimen.video_block_width);
    	
    	for (int i = 0; i < mOffset.length; i ++) {
    		canvas.drawLine(blockWidth * (i + (float) 0.5) - mScroll, (float) 0.0, 
    				margin + mOffset[i] / mDuration * (width - 2 * margin), height, mPaint);
    	}
    }
}
