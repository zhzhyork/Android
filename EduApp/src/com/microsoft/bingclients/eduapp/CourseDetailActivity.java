package com.microsoft.bingclients.eduapp;

import java.util.ArrayList;

import com.microsoft.bingclients.eduapp.models.Course;
import com.microsoft.bingclients.eduapp.models.SearchQuery;
import com.microsoft.bingclients.eduapp.models.SearchResults;
import com.microsoft.bingclients.eduapp.utils.JsonParser;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CourseDetailActivity extends ActionBarActivity {
	
	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);
		
		Intent intent = getIntent();
		String title = intent.getStringExtra(Constant.BUNDLE_STRING_TITLE);
		
		ArrayList<Course> courses = JsonParser.parseCourses(getAssets());
		
		for (int i = 0; i < courses.size(); i ++) {
			if (courses.get(i).getName().equals(title)) {
				ListView listView = (ListView) findViewById(R.id.list);
				listView.setAdapter(new CourseAdapter(this, courses.get(i)));
				break;
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
	
	private class CourseAdapter extends BaseAdapter {
		
		private Course mCourse;
		
		private CourseDetailActivity mActivity;

    	private LayoutInflater mInflater;

		public CourseAdapter(CourseDetailActivity activity, Course course) {
			mActivity = activity;
            mInflater = LayoutInflater.from(activity);
            mCourse = course;
        }
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCourse.getKeys().size() + 4;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			switch (position) {
			case 0:
				return mCourse.getName();
			case 1:
				return mCourse.getTitle();
			case 2:
				return mCourse.getClassList();
			case 3:
				return mCourse.getWords();
			default:
				return mCourse.getKeys().get(position - 4);
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView key;
			TextView value;
			
			switch (position) {
			case 0:
				convertView = mInflater.inflate(R.layout.course_detail_item, null);
				key = (TextView) convertView.findViewById(R.id.key);
				value = (TextView) convertView.findViewById(R.id.value);
				
				key.setText("Name");
				value.setText(mCourse.getName());
				break;
			case 1:
				convertView = mInflater.inflate(R.layout.course_detail_item, null);
				key = (TextView) convertView.findViewById(R.id.key);
				value = (TextView) convertView.findViewById(R.id.value);
				
				key.setText("Title");
				value.setText(mCourse.getTitle());
				break;
			case 2:
				convertView = mInflater.inflate(R.layout.course_detail_item, null);
				key = (TextView) convertView.findViewById(R.id.key);
				value = (TextView) convertView.findViewById(R.id.value);
				
				key.setText("Class");
				ArrayList<String> classes = mCourse.getClassList();
				String className = classes.get(0);
				for (int i = 1; i < classes.size(); i ++) {
					className += ", " + classes.get(i);
				}
				value.setText(className);
				break;
			case 3:
				convertView = mInflater.inflate(R.layout.course_keyword_item, null);
				key = (TextView) convertView.findViewById(R.id.key);
				LinearLayout words = (LinearLayout) convertView.findViewById(R.id.words);
				
				key.setText("Key Words");
				for (int j = 0; j < mCourse.getWords().size(); j ++) {
					View view = mInflater.inflate(R.layout.course_keyword_button, null);
					Button button = (Button) view.findViewById(R.id.button);
					final String word = mCourse.getWords().get(j);
					button.setText(word);
					
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							// TODO Auto-generated method stub
							mActivity.search(word);
						}
						
					});
					
					words.addView(view);
				}
				break;
			default:
				convertView = mInflater.inflate(R.layout.course_detail_item, null);
				key = (TextView) convertView.findViewById(R.id.key);
				value = (TextView) convertView.findViewById(R.id.value);
				
				key.setText(mCourse.getKeys().get(position - 4));
				value.setText(mCourse.getValues().get(position - 4));
				break;
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
