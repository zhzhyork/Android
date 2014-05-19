package com.microsoft.bingclients.eduapp.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;

public class CustomMediaController extends MediaController {
	
	private LinearLayout mContainer;
	
	public CustomMediaController(Context context) {
        super(context, true);
        // TODO Auto-generated constructor stub
    }

	public CustomMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
	
	public CustomMediaController(Context context, boolean useFastForward) {
		super(context, useFastForward);
		// TODO Auto-generated constructor stub
	}
	
	public CustomMediaController(Context context, LinearLayout container) {
		super(context, true);
		mContainer = container;
	}

	@Override
	public void show() {
		super.show(0);
		
		mContainer.setVisibility(View.VISIBLE);
	}
	
	@Override
    public void hide() {
		super.hide();
		
		mContainer.setVisibility(View.INVISIBLE);
    }
	
	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            ((Activity) getContext()).finish();

        return super.dispatchKeyEvent(event);
    }
}
