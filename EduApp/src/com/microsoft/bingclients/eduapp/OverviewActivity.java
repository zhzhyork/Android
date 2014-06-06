package com.microsoft.bingclients.eduapp;

import com.microsoft.bingclients.eduapp.models.Constant;
import com.microsoft.bingclients.eduapp.models.SearchQuery;
import com.microsoft.bingclients.eduapp.models.SearchResults;
import com.microsoft.bingclients.eduapp.utils.RssReader;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;

public class OverviewActivity extends ActionBarActivity implements ActionBar.TabListener, OnQueryTextListener {
	
	private SearchView mSearchView;
	
	private ViewPager mViewPager;
	
	private MenuItem mSearchMenuItem;
	
	private Tab mTab;
	
	private CollectionPagerAdapter mCollectionPagerAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        
        mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

        });

        for (int i = 0; i < mCollectionPagerAdapter.getCount(); i ++) {
        	mTab = actionBar.newTab()
        				.setText(mCollectionPagerAdapter.getPageTitle(i))
        				.setTabListener(this);
            actionBar.addTab(mTab);
        }
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	if (mSearchMenuItem != null) {
    		mSearchMenuItem.collapseActionView();
    	}
    }
    
    public Tab getTab() {
    	return mTab;
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
        	Intent intent = new Intent();
    		intent.setClass(this, AuthenticationActivity.class);
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
		
		hideSoftKeyboard();
        
		search(text);
		
		return false;
	}
	
	public void hideSoftKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
	}
	
    @Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
		// TODO Auto-generated method stub
	}
	
    private class CollectionPagerAdapter extends FragmentPagerAdapter {

    	private final static int NUM_ITEMS = 2;

        public CollectionPagerAdapter(FragmentManager fm) {
        	super(fm);
        }

        @Override
        public Fragment getItem(int position) {
	        Fragment fragment = null;
	        
	        switch (position) {
	        case 0:
	        	fragment = CourseTableFragment.newInstance();
	        	break;
	        case 1:
	        	fragment = SearchHistoryFragment.newInstance();
	        	break;
        	default:
        		break;
	        }
	        
	        return fragment;
        }

        @Override
        public int getCount() {
	        return NUM_ITEMS;
	    }

        @Override
        public CharSequence getPageTitle(int position) {
	        String tabLabel = null;
	        
	        switch (position) {
	            case 0:
	                tabLabel = getString(R.string.course_table_tab);
	                break;
	            case 1:
	                tabLabel = String.format(getString(R.string.search_history_tab), 0);
	                break;
	            default:
	                break;
	        }

	        return tabLabel;
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
