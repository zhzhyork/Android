package com.microsoft.bingclients.eduapp;

import java.util.ArrayList;

import com.microsoft.bingclients.eduapp.models.Constant;
import com.microsoft.bingclients.eduapp.views.CustomScrollView;
import com.microsoft.bingclients.eduapp.views.CustomScrollView.ScrollViewListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayerActivity extends Activity {
	
	private Context mContext;
	
	private ProgressDialog mProgressDialog;
	
	private VideoView mVideoView;
	
	private MediaController mMediaController;
	
	private LinearLayout mContainer;
	
	private PointerPanel mPointerPanel;

	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_player);
		mContext = this;
		
		mProgressDialog = ProgressDialog.show(this, "", "Loading video...", true);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				mProgressDialog.dismiss();
				finish();
			}
			
		});
		
		Intent intent = getIntent();
		
		String url = intent.getStringExtra(Constant.BUNDLE_STRING_URL);
        final float time = intent.getFloatExtra(Constant.BUNDLE_FLOAT_TIME, 0);
        
        mVideoView = (VideoView) findViewById(R.id.video);
        
        mVideoView.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer player) {
            	// TODO Auto-generated method stub
            	mProgressDialog.dismiss();
            	player.seekTo((int)(time * 1000));
            	player.start();
            	
            	/********************* For test use ******************/
            	float[] offset = {1000, 2000, 3000, 4000, 5000};
                mPointerPanel = new PointerPanel(mContext, offset, player.getDuration());
                mContainer.addView(mPointerPanel);
                
                mMediaController.show();
            }

        });
        
        mVideoView.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer player, int what, int extra) {
				// TODO Auto-generated method stub
				mProgressDialog.dismiss();
				finish();
				return false;
			}
        	
        });
        
        mVideoView.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				finish();
			}
        	
        });
        
        setupComments();
        
        mMediaController = new VideoController(this, true); 
        mMediaController.setAnchorView(mVideoView);
        mMediaController.setMediaPlayer(mVideoView);
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
		
        mVideoView.setMediaController(mMediaController);
        mVideoView.setMinimumWidth(dm.widthPixels);
        mVideoView.setMinimumHeight(dm.heightPixels);
        mVideoView.setKeepScreenOn(true);
        mVideoView.setVideoPath(url);
	}
	
	private void setupComments() {
		/********************* For test use ******************/
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		ArrayList<String> l1 = new ArrayList<String>();
		l1.add("hey00");
		l1.add("hey01");
		l1.add("hey02");
		l1.add("hey03");
		l1.add("hey04");
		l1.add("hey05");
		list.add(l1);
		ArrayList<String> l2 = new ArrayList<String>();
		l2.add("hey10");
		l2.add("hey11");
		l2.add("hey12");
		l2.add("hey13");
		l2.add("hey14");
		l2.add("hey15");
		list.add(l2);
		ArrayList<String> l3 = new ArrayList<String>();
		l3.add("hey20");
		l3.add("hey21");
		l3.add("hey22");
		l3.add("hey23");
		l3.add("hey24");
		l3.add("hey25");
		list.add(l3);
		ArrayList<String> l4 = new ArrayList<String>();
		l4.add("hey30");
		l4.add("hey31");
		l4.add("hey32");
		l4.add("hey33");
		l4.add("hey34");
		l4.add("hey35");
		list.add(l4);
		ArrayList<String> l5 = new ArrayList<String>();
		l5.add("hey40");
		l5.add("hey41");
		l5.add("hey42");
		l5.add("hey43");
		l5.add("hey44");
		l5.add("hey45");
		list.add(l5);
        
		mContainer = (LinearLayout) findViewById(R.id.container);
        final LinearLayout gallery = (LinearLayout) findViewById(R.id.gallery);
        
        for (int i = 0; i < list.size(); i ++) {
        	View view = getLayoutInflater().inflate(R.layout.video_comment, null);
        	ListView listView = (ListView) view.findViewById(R.id.comments);
        	listView.setAdapter(new CommentAdapter(this, list.get(i)));
        	
        	gallery.addView(view);
        }
        
        CustomScrollView scrollView = (CustomScrollView) findViewById(R.id.scrollView);
        scrollView.setScrollViewListener(new ScrollViewListener() {

			@Override
			public void onScrollChanged(CustomScrollView scrollView, int x,
					int y, int oldx, int oldy) {
				// TODO Auto-generated method stub
				mPointerPanel.setScroll(x);
				mPointerPanel.invalidate();
			}
        	
        });
        
        Button addButton = (Button) findViewById(R.id.add);
        addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final int position = mVideoView.getCurrentPosition();
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Leave your comment");
				
				final EditText et = new EditText(mContext);
				builder.setView(et);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						ArrayList<String> list = new ArrayList<String>();
						list.add(et.getText().toString());
						
						View view = LayoutInflater.from(mContext).inflate(R.layout.video_comment, null);
			        	ListView listView = (ListView) view.findViewById(R.id.comments);
			        	listView.setAdapter(new CommentAdapter(mContext, list));
			        	
			        	gallery.addView(view);
			        	
			        	mPointerPanel.addOffset(position);
			        	mPointerPanel.invalidate();
					}
					
				});
				
				builder.setNegativeButton("Cancel", null);
				builder.show();
			}
        	
        });
	}
	
	private class CommentAdapter extends BaseAdapter {
		
		private Context mContext;
		
		private ArrayList<String> mList;

		public CommentAdapter(Context context, ArrayList<String> list) {
			mContext = context;
            mList = list;
        }
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (position < mList.size()) {
				TextView tv = new TextView(mContext);
				tv.setText(mList.get(position));
				tv.setTextColor(mContext.getResources().getColor(android.R.color.black));
				convertView = tv;
			}
			
			return convertView;
		}
	}
	
	private class VideoController extends MediaController {

		public VideoController(Context context, boolean useFastForward) {
			super(context, useFastForward);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void show() {
			super.show();
			
			mContainer.setVisibility(View.VISIBLE);
		}
		
		@Override
	    public void hide() {
	        super.hide();
	        
	        mContainer.setVisibility(View.INVISIBLE);
	    }
	}
	
	private class PointerPanel extends View {
		
		private Context mContext;
		
		private Paint mPaint;
		
		private float[] mOffset;
		
		private int mDuration;
		
		private int mScroll;

        public PointerPanel(Context context, float[] offset, int duration) {
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
}
