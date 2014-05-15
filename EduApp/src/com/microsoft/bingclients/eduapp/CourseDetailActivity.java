package com.microsoft.bingclients.eduapp;

import java.util.ArrayList;

import com.microsoft.bingclients.eduapp.models.SearchQuery;
import com.microsoft.bingclients.eduapp.models.SearchResults;
import com.microsoft.bingclients.eduapp.utils.RssReader;
import com.microsoft.bingclients.eduapp.models.Constant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CourseDetailActivity extends ActionBarActivity {
	
	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);
		
		Intent intent = getIntent();
		String title = intent.getStringExtra(Constant.BUNDLE_STRING_TITLE);
		
		/********************* For test use ******************/
		ArrayList<String> list = new ArrayList<String>();
		if ("CS101".equals(title)) {
			list.add("Pointers");
			list.add("Algorithms");
			list.add("Memory Addresses");
			list.add("Binary Files");
		} else if ("CSE341".equals(title)) {
			list.add("Pattern Matching");
			list.add("Decomposition");
			list.add("Delaying Evaluation");
			list.add("Dynamic Typing");
		}
		
		if (list != null) {
			ListView listView = (ListView) findViewById(R.id.list);
			listView.setAdapter(new CourseAdapter(this, list));
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
	
	private class CourseAdapter extends BaseAdapter {
		
		private ArrayList<String> mList;
		
		private CourseDetailActivity mActivity;

    	private LayoutInflater mInflater;

		public CourseAdapter(CourseDetailActivity activity, ArrayList<String> list) {
			mActivity = activity;
            mInflater = LayoutInflater.from(activity);
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
			convertView = mInflater.inflate(R.layout.list_item, null);
			
			TextView query = (TextView) convertView.findViewById(R.id.item);
			
			if (position < mList.size()) {
				final String text = mList.get(position);
				query.setText(text);
				
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						mActivity.search(text);
					}
					
				});
			}
			
			return convertView;
		}
	}
	
	public void search(String key) {
    	new SearchTask(this).execute(key); 
    }
    
    private class SearchTask extends AsyncTask<String, Void, SearchResults> {
    	
    	private ProgressDialog mProgressDialog;
    	
    	private Context mContext;
    	
    	public SearchTask(Context context) {
    		mContext = context;
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
    		
    		return RssReader.read(query);
    	}
    	
    	@Override
    	protected void onPostExecute(SearchResults videos) {
    		if (mProgressDialog.isShowing()) {
    			mProgressDialog.dismiss();
            }
    		
    		Intent intent = new Intent();
    		intent.setClass(mContext, ResultListActivity.class);
    		intent.putExtra(Constant.BUNDLE_STRING_VIDEO, videos);
    		mContext.startActivity(intent);
    	}
    }
}
