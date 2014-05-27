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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

public class CourseDetailActivity extends ActionBarActivity implements OnQueryTextListener {
	
	private SearchView mSearchView;
	
	private MenuItem mSearchMenuItem;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);
		
		Intent intent = getIntent();
		String title = intent.getStringExtra(Constant.BUNDLE_STRING_TITLE);
		
		ArrayList<Course> courses = JsonParser.parseCourses(getAssets());
		
		for (int i = 0; i < courses.size(); i ++) {
			if (courses.get(i).getName().equals(title)) {
				Course course = courses.get(i);
				TextView name = (TextView) findViewById(R.id.name);
				name.setText(course.getName() + ": " + course.getTitle());
				
				TextView classes = (TextView) findViewById(R.id.classes);
				ArrayList<String> classList = course.getClassList();
				String className = classList.get(0);
				for (int j = 1; j < classList.size(); j ++) {
					className += ", " + classList.get(j);
				}
				classes.setText("Class: " + className);
				
				Spinner spinner = (Spinner) findViewById(R.id.spinner);
				spinner.setAdapter(new SpinnerAdapter(this, course.getWords()));
				
				ExpandableListView listView = (ExpandableListView) findViewById(R.id.list);
				listView.setAdapter(new CourseAdapter(this, course));
				break;
			}
		}
	}
	
	@Override
    public void onResume() {
    	super.onResume();
    	if (mSearchMenuItem != null) {
    		mSearchMenuItem.collapseActionView();
    	}
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();
        mSearchView.setFocusable(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					mSearchMenuItem.collapseActionView();
                }
			}
        });
        
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
		mSearchMenuItem.collapseActionView();
		
		InputMethodManager imm = (InputMethodManager) getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
        
		search(text);
		
		return false;
	}
	
	private class CourseAdapter extends BaseExpandableListAdapter {
		
		private Course mCourse;

    	private LayoutInflater mInflater;

		public CourseAdapter(Context context, Course course) {
            mInflater = LayoutInflater.from(context);
            mCourse = course;
        }
		
		@Override
		public Object getChild(int groupPosition, int childPosititon) {
			// TODO Auto-generated method stub
			return mCourse.getValues().get(groupPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosititon) {
			// TODO Auto-generated method stub
			return childPosititon;
		}

		@Override
		public View getChildView(int groupPosition, int childPosititon, boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = mInflater.inflate(R.layout.list_course_item, null);
			TextView value = (TextView) convertView.findViewById(R.id.value);
			value.setText(mCourse.getValues().get(groupPosition));
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
			return mCourse.getKeys().get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return mCourse.getKeys().size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
            convertView = mInflater.inflate(R.layout.list_course_group, null);
			TextView value = (TextView) convertView.findViewById(R.id.key);
			value.setText(mCourse.getKeys().get(groupPosition));
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
	
	private class SpinnerAdapter extends BaseAdapter {
		
		private CourseDetailActivity mActivity;
		
		private ArrayList<String> mList;
		
		public SpinnerAdapter(Context context, ArrayList<String> list) {
			mActivity = (CourseDetailActivity) context;
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
			TextView tv = new TextView(mActivity);
			final String word = mList.get(position);
			tv.setTextSize(mActivity.getResources().getDimension(R.dimen.table_group_text_size));
			tv.setPadding(10, 10, 10, 10);
			tv.setText(word);
			
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					mActivity.search(word);
				}
				
			});
			
			convertView = tv;
			return convertView;
		}
	}

	public void search(String key) {
    	new SearchTask(this).execute(key); 
    }
    
    private class SearchTask extends AsyncTask<String, Void, SearchResults> {
    	
    	private ProgressDialog mProgressDialog;
    	
    	private Context mContext;
    	
    	private String mKeyword;
    	
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
    		query.setStart(1);
    		
    		mKeyword = key[0];
    		
    		return RssReader.read(query);
    	}
    	
    	@Override
    	protected void onPostExecute(SearchResults videos) {
    		if (mProgressDialog.isShowing()) {
    			mProgressDialog.dismiss();
            }
    		
    		Intent intent = new Intent();
    		intent.setClass(mContext, ResultListActivity.class);
    		intent.putExtra(Constant.BUNDLE_STRING_KEYWORD, mKeyword);
    		intent.putExtra(Constant.BUNDLE_STRING_VIDEO, videos);
    		mContext.startActivity(intent);
    	}
    }
}
