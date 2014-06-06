package com.microsoft.bingclients.eduapp;

import com.microsoft.bingclients.eduapp.models.Constant;
import com.microsoft.bingclients.eduapp.models.SearchQuery;
import com.microsoft.bingclients.eduapp.models.SearchResults;
import com.microsoft.bingclients.eduapp.utils.RssReader;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class AuthenticationActivity extends ActionBarActivity implements OnQueryTextListener {
	
	private SearchView mSearchView;
	
	private MenuItem mSearchMenuItem;
	
	private EditText mUsernameEditText;
	
	private EditText mPasswordEditText;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        
        mUsernameEditText = (EditText) findViewById(R.id.username);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        Button signinButton = (Button) findViewById(R.id.signin);
        signinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String username = mUsernameEditText.getText().toString();
				String password = mPasswordEditText.getText().toString();
				
				InputMethodManager imm = (InputMethodManager) getSystemService(
					      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mPasswordEditText.getWindowToken(), 0);
					
				signIn(username, password);
			}
        	
        });
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
    
    public void signIn(String username, String password) {
    	Intent intent = new Intent();
		intent.setClass(this, OverviewActivity.class);
		startActivity(intent);
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
