package com.microsoft.bingclients.eduapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.microsoft.bingclients.eduapp.models.Constant;
import com.microsoft.bingclients.eduapp.models.SearchItem;
import com.microsoft.bingclients.eduapp.models.SearchSnippet;
import com.microsoft.bingclients.eduapp.utils.ImageDownloader;
import com.microsoft.bingclients.eduapp.utils.StringConverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

public class SearchHistoryFragment extends Fragment {
	
	private HistoryAdapter mHistoryAdapter;
	/**
     * Returns a new instance of this fragment for the given section
     * number.
     */
	public static SearchHistoryFragment newInstance() {
        return new SearchHistoryFragment();
    }

    public SearchHistoryFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.fragment_search_history, container, false);
    	setHasOptionsMenu(true);
    	
    	mHistoryAdapter = new HistoryAdapter((OverviewActivity) getActivity(), new ArrayList<SearchItem>());
    	ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.videoList);
    	listView.setAdapter(mHistoryAdapter);

    	return rootView;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	loadHistory();
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	inflater.inflate(R.menu.history, menu);
    	super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_clear) {
        	SharedPreferences history = getActivity().getSharedPreferences(Constant.HISTORY_SHARED_PREFERENCES, 0);
        	history.edit().clear().commit();

        	mHistoryAdapter.setList(new ArrayList<SearchItem>());
        	mHistoryAdapter.notifyDataSetChanged();
        	
        	((OverviewActivity) getActivity()).getTab()
					.setText(String.format(getString(R.string.search_history_tab), 0));
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    public void loadHistory() {
    	new LoadHistoryTask(mHistoryAdapter).execute(); 
    }
    
    private class LoadHistoryTask extends AsyncTask<Void, Void, ArrayList<SearchItem>> {
    	
    	private HistoryAdapter mHistoryAdapter;
    	
    	public LoadHistoryTask(HistoryAdapter historyAdapter) {
    		mHistoryAdapter = historyAdapter;
        }

    	@Override
    	protected ArrayList<SearchItem> doInBackground(Void... arg) {
    		ArrayList<SearchItem> searchItems = new ArrayList<SearchItem>();
        	SharedPreferences history = getActivity().getSharedPreferences(Constant.HISTORY_SHARED_PREFERENCES, 0);
        	Map<String, ?> items = history.getAll();
        	Iterator<String> iterator = items.keySet().iterator();
        	
        	while (iterator.hasNext()) {
        		String strItem = (String) items.get(iterator.next());
        		searchItems.add(StringConverter.convertStringToSearchItem(strItem));
        	}
    		
    		return searchItems;
    	}
    	
    	@Override
    	protected void onPostExecute(ArrayList<SearchItem> searchItems) {
    		mHistoryAdapter.setList(searchItems);
        	mHistoryAdapter.notifyDataSetChanged();
        	
        	((OverviewActivity) getActivity()).getTab()
					.setText(String.format(getString(R.string.search_history_tab), searchItems.size()));
    	}
    }
    
    private class HistoryAdapter extends BaseExpandableListAdapter {
    	
    	private OverviewActivity mActivity;

    	private ArrayList<SearchItem> mList;

    	private LayoutInflater mInflater;
    	
    	private ImageDownloader mImageDownloader; 

        public HistoryAdapter(OverviewActivity activity, ArrayList<SearchItem> list) {
        	mActivity = activity;
            mInflater = LayoutInflater.from(activity);
            mList = list;
            mImageDownloader = new ImageDownloader();
        }
        
        public void setList(ArrayList<SearchItem> list) {
        	mList = list;
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
            	
            	image.setOnClickListener(new PlayButtonListener(mActivity, mList.get(groupPosition), 0));
            	
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
                    
                    play.setOnClickListener(new PlayButtonListener(mActivity, mList.get(groupPosition), floatTime));
                    
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

    	private SearchItem mItem;
    	
    	private float mTime;
    	
    	private OverviewActivity mActivity;
    	
    	public PlayButtonListener(OverviewActivity activity, SearchItem item, float time) {
    		mActivity = activity;
    		mItem = item;
    		mTime = time;
    	}
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mActivity.hideSoftKeyboard();

			Intent intent = new Intent();
			intent.setClass(mActivity, VideoPlayerActivity.class);
			intent.putExtra(Constant.BUNDLE_STRING_ID, mItem.getId());
			intent.putExtra(Constant.BUNDLE_STRING_URL, mItem.getUrl());
			intent.putExtra(Constant.BUNDLE_FLOAT_TIME, mTime);
			mActivity.startActivity(intent);
		}
    }
}
