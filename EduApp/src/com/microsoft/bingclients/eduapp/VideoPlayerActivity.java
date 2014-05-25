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
import android.view.inputmethod.InputMethodManager;
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
		
		Intent intent = getIntent();
		// String id = intent.getStringExtra(Constant.BUNDLE_STRING_ID);
		String url = intent.getStringExtra(Constant.BUNDLE_STRING_URL);
        final float time = intent.getFloatExtra(Constant.BUNDLE_FLOAT_TIME, 0);
		
		mProgressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				mProgressDialog.dismiss();
				finish();
			}
			
		});
		
		mVideoView = (CustomVideoView) findViewById(R.id.video);
        
        mVideoView.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer player) {
            	// TODO Auto-generated method stub
            	mProgressDialog.dismiss();
            	player.seekTo((int)(time * 1000));
            	player.start();
            	
            	/********************* For test use ******************/
            	int duration = player.getDuration();
            	float[] offset = {(float) (duration * 0.01),
            					  (float) (duration * 0.1),
            					  (float) (duration * 0.5),
            					  (float) (duration * 0.85)};
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
		l1.add("User1:\n\nI like this part.\n\n5-2-2013");
		l1.add("User2:\n\nIt's great.\n\n5-4-2013");
		l1.add("User3:\n\nLike it. Like it. Like it. Like it.\n\n7-21-2013");
		l1.add("User4:\n\nI like this part, too.\n\n1-10-2014");
		list.add(l1);
		ArrayList<String> l2 = new ArrayList<String>();
		l2.add("User1:\n\nI don't like this part.\n\n5-5-2013");
		l2.add("User2:\n\nMe neither.\n\n8-8-2013");
		l2.add("User3:\n\nWhat is it about?\n\n2-11-2014");
		l2.add("User4:\n\nI Skipped it.\n\n4-30-2014");
		list.add(l2);
		ArrayList<String> l3 = new ArrayList<String>();
		l3.add("User1:\n\nProfessor recommandation.\n\n5-9-2013");
		l3.add("User2:\n\nThis is really helpful!\n\n12-1-2013");
		l3.add("User3:\n\nThanks for sharing.\n\n2-11-2014");
		l3.add("User4:\n\n:)\n\n5-12-2014");
		list.add(l3);
		ArrayList<String> l4 = new ArrayList<String>();
		l4.add("User1:\n\nNice!\n\n11-25-2013");
		l4.add("User2:\n\nI am confused. Anyone else?\n\n1-3-2014");
		l4.add("User3:\n\nMe too. Who has reference to this?\n\n3-19-2014");
		l4.add("User4:\n\nhttp://something.com\n\n5-20-2014");
		list.add(l4);
        
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
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
						
						ArrayList<String> list = new ArrayList<String>();
						String comment = et.getText().toString();
						comment = "Your Id:\n\n" + comment + "\n\n5-25-2014";
						list.add(comment);
						
						View view = LayoutInflater.from(mContext).inflate(R.layout.video_comment, null);
			        	ListView listView = (ListView) view.findViewById(R.id.comments);
			        	listView.setAdapter(new CommentAdapter(mContext, list));
			        	
			        	gallery.addView(view);
			        	
			        	mPointerPanel.addOffset(position);
			        	mPointerPanel.invalidate();
					}
					
				});
				
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
					}
					
				});
				
				builder.show();
			}
        	
        });
	}
	
	private class CommentAdapter extends BaseAdapter {
		
		private ArrayList<String> mList;
		
		private LayoutInflater mInflater;

		public CommentAdapter(Context context, ArrayList<String> list) {
			mInflater = LayoutInflater.from(context);
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
			convertView = mInflater.inflate(R.layout.video_comment_item, null);
			
			if (position < mList.size()) {
				TextView tv = (TextView) convertView.findViewById(R.id.text);
				tv.setText(mList.get(position));
			}
			
			return convertView;
		}
	}
}
