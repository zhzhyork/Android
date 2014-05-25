package com.microsoft.bingclients.eduapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class AuthenticationFragment extends Fragment implements OnClickListener {

	private EditText mUsernameEditText;
	
	private EditText mPasswordEditText;
	
	/**
     * Returns a new instance of this fragment for the given section
     * number.
     */
	public static AuthenticationFragment newInstance() {
        return new AuthenticationFragment();
    }

    public AuthenticationFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_auth, container, false);
        
        mUsernameEditText = (EditText) rootView.findViewById(R.id.username);
        mPasswordEditText = (EditText) rootView.findViewById(R.id.password);
        Button signinButton = (Button) rootView.findViewById(R.id.signin);
        signinButton.setOnClickListener(this);
        
        return rootView;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String username = mUsernameEditText.getText().toString();
		String password = mPasswordEditText.getText().toString();
		
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mPasswordEditText.getWindowToken(), 0);
			
		((MainActivity) getActivity()).signIn(username, password);
	}
}
