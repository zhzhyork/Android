package com.microsoft.bingclients.eduapp;

import java.util.ArrayList;

import com.microsoft.bingclients.eduapp.models.Constant;
import com.microsoft.bingclients.eduapp.views.CustomMediaController;
import com.microsoft.bingclients.eduapp.views.CustomScrollView;
import com.microsoft.bingclients.eduapp.views.CustomScrollView.ScrollViewListener;
import com.microsoft.bingclients.eduapp.views.CustomVideoView;
import com.microsoft.bingclients.eduapp.views.VideoPointerPanel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class VideoPlayerActivity extends Activity {
	
	private Context mContext;
	
	private ProgressDialog mProgressDialog;
	
	private CustomVideoView mVideoView;
	
	private CustomMediaController mMediaController;
	
	private LinearLayout mContainer;
	
	private VideoPointerPanel mPointerPanel;

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
        
        mVideoView = (CustomVideoView) findViewById(R.id.video);
        
        mVideoView.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer player) {
            	// TODO Auto-generated method stub
            	mProgressDialog.dismiss();
            	player.seekTo((int)(time * 1000));
            	player.start();
            	
            	/********************* For test use ******************/
            	float[] offset = {1000, 2000, 3000, 4000, 5000};
                mPointerPanel = new VideoPointerPanel(mContext, offset, player.getDuration());
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
        
        mMediaController = new CustomMediaController(this, mContainer);
        mMediaController.setAnchorView(mVideoView);
        mMediaController.setMediaPlayer(mVideoView);
		
        mVideoView.setMediaController(mMediaController);
        mVideoView.setKeepScreenOn(true);
        mVideoView.setVideoPath(url);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVideoView.getLayoutParams();
			params.gravity = Gravity.CENTER;
			mVideoView.setLayoutParams(params);
			mMediaController.hide();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVideoView.getLayoutParams();
			params.gravity = Gravity.NO_GRAVITY;
			mVideoView.setLayoutParams(params);
			mMediaController.show();
		}
	}
	
	private void setupComments() {
		/********************* For test use ******************/
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		ArrayList<String> l1 = new ArrayList<String>();
		l1.add("I like this part");
		l1.add("It's great");
		l1.add("Like it. Like it. Like it. Like it.");
		l1.add("hahahahaha");
		l1.add("I like this part, too");
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
}
