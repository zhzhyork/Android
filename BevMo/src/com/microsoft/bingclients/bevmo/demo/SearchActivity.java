package com.microsoft.bingclients.bevmo.demo;

import java.util.ArrayList;

import com.microsoft.bingclients.bevmo.R;
import com.microsoft.bingclients.bevmo.VideoPlayerActivity;
import com.microsoft.bingclients.bevmo.models.Constant;
import com.microsoft.bingclients.bevmo.models.SearchItem;
import com.microsoft.bingclients.bevmo.models.SearchQuery;
import com.microsoft.bingclients.bevmo.models.SearchResults;
import com.microsoft.bingclients.bevmo.models.SearchSnippet;
import com.microsoft.bingclients.bevmo.utils.ImageDownloader;
import com.microsoft.bingclients.bevmo.utils.RssReader;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends ActionBarActivity {
	
	private LinearLayout mSearchContainer;
	
	private EditText mSearchBox;
	
	private ResultAdapter mResultAdapter;
	
	private ListView mResultListView;
	
	TranslateAnimation mSearchBoxAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
        mSearchContainer = (LinearLayout) findViewById(R.id.container);
        mSearchBox = (EditText) findViewById(R.id.key);
        mResultListView = (ListView) findViewById(R.id.list);
        Button searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String key = mSearchBox.getText().toString();
				
				InputMethodManager imm = (InputMethodManager) getSystemService(
					      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mSearchBox.getWindowToken(), 0);
					
				search(key);
			}
        	
        });
        
        mResultAdapter = new ResultAdapter(this, new ArrayList<SearchItem>());
        mResultListView.setAdapter(mResultAdapter);
        
        final float amountToMoveDown = 15 - this.getResources().getDimension(R.dimen.search_box_margin_top);
        mSearchBoxAnimation = new TranslateAnimation(0, 0, 0, amountToMoveDown);
        mSearchBoxAnimation.setDuration(200);

        mSearchBoxAnimation.setAnimationListener(new TranslateAnimation.AnimationListener() {

	    	@Override
	    	public void onAnimationStart(Animation animation) { }
	
	    	@Override
	    	public void onAnimationRepeat(Animation animation) { }
	
	    	@Override
	    	public void onAnimationEnd(Animation animation) 
	    	{
	    		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSearchContainer.getLayoutParams();
	    	    params.topMargin += amountToMoveDown;
	    	    mSearchContainer.setLayoutParams(params);
	    	}
	    	
    	});
    }
    
    public void showResult(SearchResults videos) {
    	if (mSearchBoxAnimation != null) {
    		mSearchContainer.startAnimation(mSearchBoxAnimation);
    		mSearchBoxAnimation = null;
    	}

    	mResultAdapter.setList(videos.getItems());
		mResultAdapter.notifyDataSetChanged();
    }

    public void search(String key) {
    	new SearchTask(this).execute(key); 
    }
    
    private class SearchTask extends AsyncTask<String, Void, SearchResults> {
    	
    	private ProgressDialog mProgressDialog;
    	
    	private SearchActivity mActivity;
    	
    	private boolean mIsFakeDemo;
    	
    	public SearchTask(Context context) {
    		mActivity = (SearchActivity) context;
    		mProgressDialog = new ProgressDialog(context);
        }

    	@Override
    	protected void onPreExecute() {
    		mProgressDialog.setMessage("Searching...");
    		mProgressDialog.show();

    	}

    	@Override
    	protected SearchResults doInBackground(String... key) {
    		SearchQuery query = new SearchQuery();
    		query.setText(key[0]);
    		query.setIndex("MSNVideo");
    		
    		if ("Natasha".equalsIgnoreCase(key[0])) {
    			mIsFakeDemo = true;
    		}
    		
    		return RssReader.read(query);
    	}
    	
    	@Override
    	protected void onPostExecute(SearchResults videos) {
    		if (mProgressDialog.isShowing()) {
    			mProgressDialog.dismiss();
            }
    		
    		if (mIsFakeDemo) {
    			SearchSnippet fakeSnippet = new SearchSnippet();
    			fakeSnippet.setSpan("How old is Natasha");
    			fakeSnippet.setTime("96");
    			SearchItem fakeItem = new SearchItem();
    			fakeItem.setDuration("00:02:23");
    			fakeItem.setImage("https://eidm1q.dm1.livefilestore.com/y2miUQHsS4tOL7aojeNJZM1LCh4OFSEII6KQk63v19kJuCMT4TknPbAYqPsilDlkOZG1Jzq4HFG_uBTkSjEJu0KSiEDgXjMIz8j-qd-cWyYLdhow02JM4tf0vCk4xhRSuop/image.jpg");
    			fakeItem.setTitle("Conversational Q&A");
    			fakeItem.setUrl("https://eidm1q.dm1.livefilestore.com/y2mn4OcmOcwqia8JQ2wnfhV6sygliNV920AyAaNMGtckKsDfWgB7a6_Gqx3B1TBKe33VIZNrR5Se3f2A-gtMew52DCWu588OmhSt_6Ca-8iTPJuiHb4iEUl64bLAXWB2MRW/00115.mp4");
    			fakeItem.addSnippet(fakeSnippet);
    			SearchResults fakeResult = new SearchResults();
    			fakeResult.addItem(fakeItem);
    			mActivity.showResult(fakeResult);
    		} else {
    			mActivity.showResult(videos);
    		}
    	}
    }
    
    private class ResultAdapter extends BaseAdapter {

    	private ArrayList<SearchItem> mList;

    	private Context mContext;
    	
    	private ImageDownloader mImageDownloader; 

        public ResultAdapter(Context context, ArrayList<SearchItem> list) {
        	mContext = context;
            mList = list;
            mImageDownloader = new ImageDownloader();
        }
        
        public void setList(ArrayList<SearchItem> list) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.demo_list_group, null);
			
            LinearLayout group = (LinearLayout) convertView.findViewById(R.id.group);
			ImageView image = (ImageView) convertView.findViewById(R.id.image);
			TextView duration = (TextView) convertView.findViewById(R.id.duration);
            TextView title = (TextView) convertView.findViewById(R.id.title);     

            if (position < mList.size()) {
            	mImageDownloader.download(mList.get(position).getImage(), image);
            	duration.setText(mList.get(position).getDuration());
            	title.setText(mList.get(position).getTitle());
            	
            	image.setOnClickListener(new PlayButtonListener(mContext, mList.get(position).getUrl(), 0));
            	
            	for (int i = 0; i < mList.get(position).getSnippets().size(); i ++) {
            		SearchSnippet snippet = mList.get(position).getSnippets().get(i);
            		float floatTime = Float.parseFloat(snippet.getTime());
            		int intTime = (int) floatTime;
            		
            		View snippetView = LayoutInflater.from(mContext).inflate(R.layout.demo_list_snippet, null);
            		TextView span = (TextView) snippetView.findViewById(R.id.span);
                    Button play = (Button) snippetView.findViewById(R.id.play);
                    
                    span.setText("... " + snippet.getSpan() + " ...");
                    play.setText("Play from: " + String.format("%02d:%02d:%02d", intTime/3600, (intTime%3600)/60, (intTime%60)));
                    play.setFocusable(false);
                    
                    play.setOnClickListener(new PlayButtonListener(mContext, mList.get(position).getUrl(), floatTime));
                    
                    group.addView(snippetView);
            	}
            }

            return convertView;
		}
    }
    
    private class PlayButtonListener implements OnClickListener {

    	private Context mContext;
    	private String mUrl;
    	
    	private float mTime;
    	
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
			startActivity(intent);
		}
    }
}
