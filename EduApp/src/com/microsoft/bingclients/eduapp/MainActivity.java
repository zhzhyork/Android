package com.microsoft.bingclients.eduapp;

import com.microsoft.bingclients.eduapp.models.Constant;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
        	signOut();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	
    	if (Constant.ACTION_SIGN_OUT.equals(intent.getAction())) {
    		signOut();
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
        	signOut();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    public void signIn(String username, String password) {
    	getSupportFragmentManager().beginTransaction()
        		.replace(R.id.container, CourseListFragment.newInstance())
        		.commit();
    }
    
    private void signOut() {
    	getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, AuthenticationFragment.newInstance())
				.commit();
    }
}
