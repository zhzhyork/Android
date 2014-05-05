package com.microsoft.bingclients.bevmo;

import com.microsoft.bingclients.bevmo.models.Channel;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    private Channel mChannel;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WelcomeFragment newInstance(int sectionNumber) {
    	WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WelcomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        
        if (!mChannel.getTitle().isEmpty()) {
        	TextView title = (TextView) rootView.findViewById(R.id.title);
        	title.setText(mChannel.getTitle());
        }
        
        if (!mChannel.getDescription().isEmpty()) {
        	TextView description = (TextView) rootView.findViewById(R.id.description);
        	description.setText(mChannel.getDescription());
        }
        
        if (!mChannel.getUpdate().isEmpty()) {
        	TextView update = (TextView) rootView.findViewById(R.id.update);
            update.setText("Last update: " + mChannel.getUpdate());
        }
        
        if (mChannel.getPower().size() > 0) {
        	String power = "Powered by " + mChannel.getPower().get(0).name;
        	for (int i = 1; i < mChannel.getPower().size(); i ++) {
            	power += " and " + mChannel.getPower().get(i).name;
        	}
        	power += ".";
        	
        	TextView powerTv = (TextView) rootView.findViewById(R.id.power);
            powerTv.setText(power);
        }
        
        LinearLayout query = (LinearLayout) rootView.findViewById(R.id.query);
        for (int j = 0; j < mChannel.getQuery().size(); j ++) {
        	TextView tv = new TextView(getActivity());
        	tv.setText(mChannel.getQuery().get(j));
        	tv.setTextColor(getResources().getColor(R.color.welcome_content_color));
        	tv.setTextSize(getResources().getDimension(R.dimen.welcome_query_text_size));
        	tv.setPadding(0, 0, 14, 0);
        	query.addView(tv);
    	}
        
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        int number = getArguments().getInt(ARG_SECTION_NUMBER);
        ((MainActivity) activity).onSectionAttached(number);
        mChannel = ((MainActivity) activity).getChannel(number);
    }
}