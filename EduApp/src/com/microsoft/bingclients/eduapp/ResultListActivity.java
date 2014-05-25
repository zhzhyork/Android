package com.microsoft.bingclients.eduapp;

import java.util.ArrayList;

import com.microsoft.bingclients.eduapp.R;
import com.microsoft.bingclients.eduapp.VideoPlayerActivity;
import com.microsoft.bingclients.eduapp.models.SearchItem;
import com.microsoft.bingclients.eduapp.models.SearchQuery;
import com.microsoft.bingclients.eduapp.models.SearchSnippet;
import com.microsoft.bingclients.eduapp.utils.ImageDownloader;
import com.microsoft.bingclients.eduapp.utils.RssReader;
import com.microsoft.bingclients.eduapp.models.Constant;
import com.microsoft.bingclients.eduapp.models.SearchResults;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultListActivity extends ActionBarActivity implements OnQueryTextListener{
	
	private SearchView mSearchView;
	
	private ExpandableListView mResultListView;
	
	private ResultAdapter mResultAdapter;
	
	private View mFooterView;
	
	private boolean mIsLoading;
	
	private boolean mHasMore;
	
	private String mKeyword;
	
	private ArrayList<SearchItem> mSearchItems;

	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_list);
		
		Intent intent = getIntent();
		SearchResults videos = (SearchResults) intent.getExtras()
				.getParcelable(Constant.BUNDLE_STRING_VIDEO);
		mKeyword = intent.getStringExtra(Constant.BUNDLE_STRING_KEYWORD);
		
		if (videos != null) {
			mSearchItems = videos.getItems();
			mHasMore = mSearchItems.size() == 10;
			mResultAdapter = new ResultAdapter(this, mSearchItems);
			
			mResultListView = (ExpandableListView) findViewById(R.id.videoList);
			
			mFooterView = getLayoutInflater().inflate(R.layout.list_footer, null);
			mFooterView.setVisibility(View.GONE);
	        mIsLoading = false;
	        mResultListView.addFooterView(mFooterView, null, false);
			
			mResultListView.setAdapter(mResultAdapter);
            
            if (videos.getItems() != null && videos.getItems().size() > 0) {
            	TextView tv = (TextView) findViewById(R.id.empty);
            	tv.setVisibility(View.INVISIBLE);
            }
            
            mResultListView.setOnScrollListener(new OnScrollListener() {
            	
                @Override
                public void onScrollStateChanged(AbsListView view, 
                        int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, 
                        int visibleItemCount, int totalItemCount) {
                	if (mIsLoading || !mHasMore) {
                        return;
                    }

                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    	mFooterView.setVisibility(View.VISIBLE);
                        mIsLoading = true;
                        search(mKeyword, mResultListView.getCount() + 1, false);
                    }
                }

            });
        }
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
        
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setOnQueryTextListener(this);
        
        return super.onCreateOptionsMenu(menu);
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

	@Override
	public boolean onQueryTextChange(String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String text) {
		// TODO Auto-generated method stub
		mSearchView.setIconified(true);
		mSearchView.clearFocus();
		
		InputMethodManager imm = (InputMethodManager) getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
        
		mKeyword = text;
		search(text, 1, true);
		
		return false;
	}
	
	public void updateResult(SearchResults videos, boolean isNew) {
		mFooterView.setVisibility(View.GONE);
        mIsLoading = false;
        
        mHasMore = videos.getItems().size() == 10;
        
		if (isNew) {
			mSearchItems = videos.getItems();
			mResultListView.setSelection(0);
		} else {
			mSearchItems.addAll(videos.getItems());
		}
		
		mResultAdapter.setList(mSearchItems);
		mResultAdapter.notifyDataSetChanged();
	}
	
	public void search(String key, int start, boolean isNew) {
		String[] keys = {key, String.valueOf(start), String.valueOf(isNew)};
    	new SearchTask(this, isNew).execute(keys); 
    }
    
    private class SearchTask extends AsyncTask<String, Void, SearchResults> {
    	
    	private ProgressDialog mProgressDialog;
    	
    	private ResultListActivity mActivity;
    	
    	private boolean mIsNew;
    	
    	public SearchTask(Context context, boolean isNew) {
    		mIsNew = isNew;
    		mActivity = (ResultListActivity) context;
    		
    		if (isNew) {
    			mProgressDialog = new ProgressDialog(context);
    		}
        }

    	@Override
    	protected void onPreExecute() {
    		if (mProgressDialog != null) {
    			mProgressDialog.setMessage("Searching...");
        		mProgressDialog.show();
    		}
    	}

    	@Override
    	protected SearchResults doInBackground(String... key) {
    		SearchQuery query = new SearchQuery();
    		query.setText(key[0]);
    		query.setStart(Integer.parseInt(key[1]));
    		
    		return RssReader.read(query);
    	}
    	
    	@Override
    	protected void onPostExecute(SearchResults videos) {
    		if (mProgressDialog != null && mProgressDialog.isShowing()) {
    			mProgressDialog.dismiss();
            }
    		
    		mActivity.updateResult(videos, mIsNew);
    	}
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
            	
            	image.setOnClickListener(new PlayButtonListener(mContext, mList.get(groupPosition).getId(), 
            			mList.get(groupPosition).getUrl(), 0));
            	
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
                    
                    play.setOnClickListener(new PlayButtonListener(mContext, mList.get(groupPosition).getId(), 
                    		mList.get(groupPosition).getUrl(), floatTime));
                    
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

    	private String mId;
    	
    	private String mUrl;
    	
    	private float mTime;
    	
    	private Context mContext;
    	
    	public PlayButtonListener(Context context, String id, String url, float time) {
    		mContext = context;
    		mId = id;
    		mUrl = url;
    		mTime = time;
    	}
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(mContext, VideoPlayerActivity.class);
			intent.putExtra(Constant.BUNDLE_STRING_ID, mId);
			intent.putExtra(Constant.BUNDLE_STRING_URL, mUrl);
			intent.putExtra(Constant.BUNDLE_FLOAT_TIME, mTime);
			mContext.startActivity(intent);
		}
    }
}
