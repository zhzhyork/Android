package com.microsoft.bingclients.eduapp;

import java.util.ArrayList;

import com.microsoft.bingclients.eduapp.models.Constant;
import com.microsoft.bingclients.eduapp.models.Course;
import com.microsoft.bingclients.eduapp.utils.JsonParser;
import com.microsoft.bingclients.eduapp.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class CourseTableFragment extends Fragment {
	
	/**
     * Returns a new instance of this fragment for the given section
     * number.
     */
	public static CourseTableFragment newInstance() {
        return new CourseTableFragment();
    }

    public CourseTableFragment() {
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_course_table, container, false);
		
		ArrayList<Course> courses = JsonParser.generateTableFromCourses(
				JsonParser.parseCourses(getActivity().getAssets()));
		
		if (courses != null) {
			GridView gridView = (GridView) rootView.findViewById(R.id.table);
			gridView.setAdapter(new CourseTableAdapter(getActivity(), courses));
		}
		
		return rootView;
	}
	
	private class CourseTableAdapter extends BaseAdapter {
		
		private Context mContext;
		
		private ArrayList<Course> mCourses;

    	private LayoutInflater mInflater;

		public CourseTableAdapter(Context context, ArrayList<Course> courses) {
			mContext = context;
            mInflater = LayoutInflater.from(context);
            mCourses = courses;
        }
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCourses.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mCourses.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = mInflater.inflate(R.layout.course_table_item, null);
			
			int height = (int) (mContext.getResources().getDisplayMetrics().heightPixels / 5 
	                - mContext.getResources().getDimension(R.dimen.table_column_height_offset));
			AbsListView.LayoutParams param = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height);
	        convertView.setLayoutParams(param);
			
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView title = (TextView) convertView.findViewById(R.id.title);
			
			if (position >= mCourses.size()) {
				return null;
			}
			
			switch (position) {
			case 0:
				break;
			case 1:
				name.setText("Sun");
				break;
			case 2:
				name.setText("Mon");
				break;
			case 3:
				name.setText("Tue");
				break;
			case 4:
				name.setText("Wed");
				break;
			case 5:
				name.setText("Thu");
				break;
			case 6:
				name.setText("Fri");
				break;
			case 7:
				name.setText("Sat");
				break;
			case 8:
				name.setText("1");
				break;
			case 16:
				name.setText("2");
				break;
			case 24:
				name.setText("3");
				break;
			case 32:
				name.setText("4");
				break;
			default:
				final String strName = mCourses.get(position).getName();
				name.setText(strName);
				title.setText(mCourses.get(position).getTitle());

				if (strName != null && !strName.isEmpty()) {
					convertView.setBackground(mContext.getResources().
							getDrawable(R.xml.course_table_item_bg));
					
					convertView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.setClass(getActivity(), CourseDetailActivity.class);
							intent.putExtra(Constant.BUNDLE_STRING_TITLE, strName);
							startActivity(intent);
						}
						
					});
				} else {
					convertView.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				}
				
				break;
			}
			
			return convertView;
		}
	}
}
