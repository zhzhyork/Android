package com.microsoft.bingclients.eduapp;

import com.microsoft.bingclients.eduapp.models.Constant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends Activity {
	
	private ProgressDialog mProgressDialog;

	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_player);
		
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
        
        VideoView video = (VideoView) findViewById(R.id.video);
        
        video.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer player) {
            	// TODO Auto-generated method stub
            	mProgressDialog.dismiss();
            	player.seekTo((int)(time * 1000));
            	player.start();
            }

        });
        
        video.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer player, int what, int extra) {
				// TODO Auto-generated method stub
				mProgressDialog.dismiss();
				finish();
				return false;
			}
        	
        });
        
        video.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				finish();
			}
        	
        });
        
        MediaController mediaController = new MediaController(this); 
        mediaController.setAnchorView(video);
        mediaController.setMediaPlayer(video);
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
		
        video.setMediaController(mediaController);
        video.setMinimumWidth(dm.widthPixels);
        video.setMinimumHeight(dm.heightPixels);
        video.setKeepScreenOn(true);
        video.setVideoPath(url);
	}

}
