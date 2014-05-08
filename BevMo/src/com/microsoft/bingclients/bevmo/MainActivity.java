package com.microsoft.bingclients.bevmo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.microsoft.bingclients.bevmo.models.Channel;
import com.microsoft.bingclients.bevmo.models.Constant;
import com.microsoft.bingclients.bevmo.models.SearchQuery;
import com.microsoft.bingclients.bevmo.models.SearchResults;
import com.microsoft.bingclients.bevmo.utils.RssReader;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends ActionBarActivity
        implements ChannelPickerFragment.NavigationDrawerCallbacks, OnQueryTextListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private ChannelPickerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    
    private String mIndex;
    
    private SearchView mSearchView;
    
    private ArrayList<Channel> mChannels;
    
    private SearchResults mVideos;
      
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (ChannelPickerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, WelcomeFragment.newInstance(position))
                .commit();
    }

    public void onSectionAttached(int number) {
    	mTitle = mChannels.get(number).getName();
    	mIndex = mChannels.get(number).getIndex();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            
            mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            mSearchView.setOnQueryTextListener(this);
            
            restoreActionBar();
            return true;
        }
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        
		search(text);
		
		return false;
	}
    
    public void setChannels(ArrayList<Channel> channels) {
    	mChannels = channels;
    }
    
    public Channel getChannel(int number) {
    	return mChannels.get(number);
    }
    
    public void updateVideos(SearchResults videos) {
    	mVideos = videos;
    	
    	FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, ResultListFragment.newInstance())
                .commit();
    }
    
    public SearchResults getVideos() {
    	return mVideos;
    }
    
    public void search(String key) {
    	new SearchTask(this).execute(key); 
    }
    
    private class SearchTask extends AsyncTask<String, Void, SearchResults> {
    	
    	private ProgressDialog mProgressDialog;
    	
    	private MainActivity mActivity;
    	
    	public SearchTask(MainActivity activity) {
    		mActivity = activity;
    		mProgressDialog = new ProgressDialog(activity);
        }

    	@Override
    	protected void onPreExecute() {
    		mProgressDialog.setMessage("Searching...");
    		mProgressDialog.show();

    	}

    	@Override
    	protected SearchResults doInBackground(String... key) {
    		try {
    			StringBuffer data = new StringBuffer("");
    			FileInputStream inputStream = openFileInput(Constant.HISTORY_FILE_NAME);
    			byte[] dataArray = new byte[1024];
    		    int n;
    		    while ((n = inputStream.read(dataArray)) != -1) {
    		    	data.append(new String(dataArray, 0, n));
    		    }
    		    inputStream.close();
    		    
    		    if (!data.toString().startsWith(key[0] + ";") && !data.toString().contains(";" + key[0] + ";")) {
    		    	String[] items = data.toString().split(";");
        		    if (items != null && items.length >= Constant.HISTORY_MAX_COUNT) {
        		    	data.delete(0, data.length());
        		    	for (int i = items.length - Constant.HISTORY_MAX_COUNT + 1; i < items.length; i ++) {
        		    		data.append(items[i] + ";");
        		    	}
        		    }

        			FileOutputStream outputStream = openFileOutput(Constant.HISTORY_FILE_NAME, Context.MODE_PRIVATE);
        			String output = data.insert(0, key[0] + ";").toString();
        			outputStream.write(output.getBytes());
        			outputStream.close();
    		    }
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		SearchQuery query = new SearchQuery();
    		query.setText(key[0]);
    		query.setIndex(mIndex);
    		
    		return RssReader.read(query);
    	}
    	
    	@Override
    	protected void onPostExecute(SearchResults videos) {
    		mActivity.updateVideos(videos);
    		
    		if (mProgressDialog.isShowing()) {
    			mProgressDialog.dismiss();
            }
    	}
    }
}
