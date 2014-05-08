package com.microsoft.bingclients.bevmo;

import java.util.ArrayList;

import com.microsoft.bingclients.bevmo.models.Constant;
import com.microsoft.bingclients.bevmo.models.SearchItem;
import com.microsoft.bingclients.bevmo.models.SearchResults;
import com.microsoft.bingclients.bevmo.models.SearchSnippet;
import com.microsoft.bingclients.bevmo.utils.ImageDownloader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultListFragment extends Fragment {
	
	private ExpandableListView mResultListView;
	
	private SearchResults mVideos;

	/**
     * Returns a new instance of this fragment for the given section
     * number.
     */
	public static ResultListFragment newInstance() {
        return new ResultListFragment();
    }

    public ResultListFragment() {
    	mVideos = new SearchResults();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        
        if (mVideos != null) {
        	mResultListView = (ExpandableListView) rootView.findViewById(R.id.list);
            mResultListView.setAdapter(new ResultAdapter(getActivity(), mVideos.getItems()));
            
            if (mVideos.getItems() != null && mVideos.getItems().size() > 0) {
            	TextView tv = (TextView) rootView.findViewById(R.id.empty);
            	tv.setVisibility(View.INVISIBLE);
            }
        }
        
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mVideos = ((MainActivity) activity).getVideos();
    }
    
    private class ResultAdapter extends BaseExpandableListAdapter {

    	private ArrayList<SearchItem> mList;

    	private LayoutInflater mInflater;
    	
    	private ImageDownloader mImageDownloader; 

        public ResultAdapter(Context context, ArrayList<SearchItem> list) {
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
            convertView = mInflater.inflate(R.layout.list_item, null);
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
            convertView = mInflater.inflate(R.layout.list_group, null);
			
            LinearLayout group = (LinearLayout) convertView.findViewById(R.id.group);
			ImageView image = (ImageView) convertView.findViewById(R.id.image);
			TextView duration = (TextView) convertView.findViewById(R.id.duration);
            TextView title = (TextView) convertView.findViewById(R.id.title);     

            if (groupPosition < mList.size()) {
            	mImageDownloader.download(mList.get(groupPosition).getImage(), image);
            	duration.setText(mList.get(groupPosition).getDuration());
            	title.setText(mList.get(groupPosition).getTitle());
            	
            	image.setOnClickListener(new PlayButtonListener(mList.get(groupPosition).getUrl(), 0));
            	
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
                    
                    play.setOnClickListener(new PlayButtonListener(mList.get(groupPosition).getUrl(), floatTime));
                    
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
    	
    	public PlayButtonListener(String url, float time) {
    		mUrl = url;
    		mTime = time;
    	}
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getActivity(), VideoPlayerActivity.class);
			intent.putExtra(Constant.BUNDLE_STRING_URL, mUrl);
			intent.putExtra(Constant.BUNDLE_FLOAT_TIME, mTime);
			startActivity(intent);
		}
    }
}
