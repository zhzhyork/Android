package com.microsoft.bingclients.eduapp;

import java.util.ArrayList;

import com.microsoft.bingclients.eduapp.R;
import com.microsoft.bingclients.eduapp.VideoPlayerActivity;
import com.microsoft.bingclients.eduapp.models.SearchItem;
import com.microsoft.bingclients.eduapp.models.SearchSnippet;
import com.microsoft.bingclients.eduapp.utils.ImageDownloader;
import com.microsoft.bingclients.eduapp.models.Constant;
import com.microsoft.bingclients.eduapp.models.SearchResults;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultListActivity extends ActionBarActivity {

	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_list);
		
		SearchResults videos = (SearchResults) getIntent().getExtras()
				.getParcelable(Constant.BUNDLE_STRING_VIDEO);
		
		if (videos != null) {
			ExpandableListView resultListView = (ExpandableListView) findViewById(R.id.videoList);
			resultListView.setAdapter(new ResultAdapter(this, videos.getItems()));
            
            if (videos.getItems() != null && videos.getItems().size() > 0) {
            	TextView tv = (TextView) findViewById(R.id.empty);
            	tv.setVisibility(View.INVISIBLE);
            }
        }
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logout) {
        	Intent intent = new Intent(Constant.ACTION_SIGN_OUT);
    		startActivity(intent);
        	finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private class ResultAdapter extends BaseExpandableListAdapter {
    	
    	private Context mContext;

    	private ArrayList<SearchItem> mList;

    	private LayoutInflater mInflater;
    	
    	private ImageDownloader mImageDownloader; 

        public ResultAdapter(Context context, ArrayList<SearchItem> list) {
        	mContext = context;
            mInflater = LayoutInflater.from(context);
            mList = list;
            mImageDownloader = new ImageDownloader();
        }

		@Override
		public Object getChild(int groupPosition, int childPosititon) {
			// TODO Auto-generated method stub
			return mList.get(groupPosition).getDescription();
		}

		@Override
		public long getChildId(int groupPosition, int childPosititon) {
			// TODO Auto-generated method stub
			return childPosititon;
		}

		@Override
		public View getChildView(int groupPosition, int childPosititon, boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
            convertView = mInflater.inflate(R.layout.list_video_item, null);
            TextView description = (TextView) convertView.findViewById(R.id.description);

            if (groupPosition < mList.size()) {
            	description.setText(Html.fromHtml(mList.get(groupPosition).getDescription()));
            }

            return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return mList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
            convertView = mInflater.inflate(R.layout.list_video_group, null);
			
            LinearLayout group = (LinearLayout) convertView.findViewById(R.id.group);
			ImageView image = (ImageView) convertView.findViewById(R.id.image);
			TextView duration = (TextView) convertView.findViewById(R.id.duration);
            TextView title = (TextView) convertView.findViewById(R.id.title);     

            if (groupPosition < mList.size()) {
            	mImageDownloader.download(mList.get(groupPosition).getImage(), image);
            	duration.setText(mList.get(groupPosition).getDuration());
            	title.setText(mList.get(groupPosition).getTitle());
            	
            	image.setOnClickListener(new PlayButtonListener(mContext, mList.get(groupPosition).getUrl(), 0));
            	
            	for (int i = 0; i < mList.get(groupPosition).getSnippets().size(); i ++) {
            		SearchSnippet snippet = mList.get(groupPosition).getSnippets().get(i);
            		float floatTime = Float.parseFloat(snippet.getTime());
            		int intTime = (int) floatTime;
            		
            		View snippetView = mInflater.inflate(R.layout.list_snippet, null);
            		TextView span = (TextView) snippetView.findViewById(R.id.span);
                    Button play = (Button) snippetView.findViewById(R.id.play);
                    
                    span.setText("... " + snippet.getSpan() + " ...");
                    play.setText("Play from: " + String.format("%02d:%02d:%02d", intTime/3600, (intTime%3600)/60, (intTime%60)));
                    play.setFocusable(false);
                    
                    play.setOnClickListener(new PlayButtonListener(mContext, mList.get(groupPosition).getUrl(), floatTime));
                    
                    group.addView(snippetView);
            	}
            }

            return convertView;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
    }
    
    private class PlayButtonListener implements OnClickListener {

    	private String mUrl;
    	
    	private float mTime;
    	
    	private Context mContext;
    	
    	public PlayButtonListener(Context context, String url, float time) {
    		mContext = context;
    		mUrl = url;
    		mTime = time;
    	}
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(mContext, VideoPlayerActivity.class);
			intent.putExtra(Constant.BUNDLE_STRING_URL, mUrl);
			intent.putExtra(Constant.BUNDLE_FLOAT_TIME, mTime);
			mContext.startActivity(intent);
		}
    }
}
