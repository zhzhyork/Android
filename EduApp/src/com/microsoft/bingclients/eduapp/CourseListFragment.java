package com.microsoft.bingclients.eduapp;

import java.util.ArrayList;

import com.microsoft.bingclients.eduapp.models.Constant;
import com.microsoft.bingclients.eduapp.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CourseListFragment extends Fragment {
	
	/**
     * Returns a new instance of this fragment for the given section
     * number.
     */
	public static CourseListFragment newInstance() {
        return new CourseListFragment();
    }

    public CourseListFragment() {
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_course_list, container, false);
		
		/********************* For test use ******************/
		ArrayList<String> list = new ArrayList<String>();
		list.add("CS101");
		list.add("CSE341");
		
		if (list != null) {
			ListView listView = (ListView) rootView.findViewById(R.id.list);
			listView.setAdapter(new CourseListAdapter(getActivity(), list));
			
			if (list.size() > 0) {
	        	TextView tv = (TextView) rootView.findViewById(R.id.empty);
	        	tv.setVisibility(View.INVISIBLE);
	        }
		}
		
		return rootView;
	}
	
	private class CourseListAdapter extends BaseAdapter {
		
		private ArrayList<String> mList;

    	private LayoutInflater mInflater;

		public CourseListAdapter(Context context, ArrayList<String> list) {
            mInflater = LayoutInflater.from(context);
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
			
			TextView title = (TextView) convertView.findViewById(R.id.item);
			
			if (position < mList.size()) {
				final String text = mList.get(position);
				title.setText(text);
				
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(getActivity(), CourseDetailActivity.class);
						intent.putExtra(Constant.BUNDLE_STRING_TITLE, text);
						startActivity(intent);
					}
					
				});
			}
			
			return convertView;
		}
	}
}
