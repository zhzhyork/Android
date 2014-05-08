package com.microsoft.bingclients.bevmo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.microsoft.bingclients.bevmo.models.Channel;
import com.microsoft.bingclients.bevmo.models.Constant;
import com.microsoft.bingclients.bevmo.models.Channel.Video;
import com.microsoft.bingclients.bevmo.utils.ImageDownloader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
        
        if (mChannel.getRecommendations().size() > 0) {
        	ListView RecommendationListView = (ListView) rootView.findViewById(R.id.recommendations);
        	RecommendationListView.setAdapter(new RecommendationAdapter(getActivity(), mChannel.getRecommendations()));
        }
        
        new LoadHistoryTask(getActivity(), (ListView) rootView.findViewById(R.id.history)).execute(Constant.HISTORY_FILE_NAME); 
        
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        int number = getArguments().getInt(ARG_SECTION_NUMBER);
        ((MainActivity) activity).onSectionAttached(number);
        mChannel = ((MainActivity) activity).getChannel(number);
    }
    
    private class LoadHistoryTask extends AsyncTask<String, Void, ArrayList<String>> {
    	
    	private Activity mActivity;
    	
    	private ListView mListView;
    	
    	public LoadHistoryTask(Activity activity, ListView listView) {
    		mActivity = activity;
    		mListView = listView;
        }
    	
    	@Override
    	protected ArrayList<String> doInBackground(String... fileName) {
    		ArrayList<String> history = new ArrayList<String>();
    		
    		try {
    			StringBuffer data = new StringBuffer("");
    			FileInputStream inputStream = mActivity.openFileInput(fileName[0]);
    		    byte[] dataArray = new byte[1024];
    		    int n;
    		    while ((n = inputStream.read(dataArray)) != -1) {
    		    	data.append(new String(dataArray, 0, n));
    		    }
    		    inputStream.close();
    		    
    		    if (data.length() > 0) {
    		    	String[] items = data.toString().split(";");
        		    if (items != null) {
        		    	for (int i = 0; i < items.length; i ++) {
        		    		history.add(items[i]);
        		    	}
        		    }
    		    }
    		} catch (FileNotFoundException e) {
				try {
					FileOutputStream outputStream;
					outputStream = mActivity.openFileOutput(fileName[0], Context.MODE_PRIVATE);
					outputStream.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		return history;
    	}
    	
    	@Override
    	protected void onPostExecute(ArrayList<String> history) {
    		mListView.setAdapter(new HistoryAdapter((MainActivity) getActivity(), history));
    	}
    }
    
    private class RecommendationAdapter extends BaseAdapter {

    	private ArrayList<Video> mList;

    	private LayoutInflater mInflater;
    	
    	private ImageDownloader mImageDownloader; 

        public RecommendationAdapter(Context context, ArrayList<Video> list) {
            mInflater = LayoutInflater.from(context);
            mList = list;
            mImageDownloader = new ImageDownloader();
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
			convertView = mInflater.inflate(R.layout.list_group, null);

			ImageView image = (ImageView) convertView.findViewById(R.id.image);
			TextView duration = (TextView) convertView.findViewById(R.id.duration);
            TextView title = (TextView) convertView.findViewById(R.id.title); 

            if (position < mList.size()) {
            	mImageDownloader.download(mList.get(position).image, image);
            	duration.setText(mList.get(position).duration);
            	title.setText(mList.get(position).title);
            	
            	image.setOnClickListener(new PlayVideoListener(mList.get(position).url));
            }

            return convertView;
		}
    }
    
    private class HistoryAdapter extends BaseAdapter {

    	private ArrayList<String> mList;

    	private MainActivity mActivity;
    	
    	private LayoutInflater mInflater;

        public HistoryAdapter(MainActivity activity, ArrayList<String> list) {
        	mActivity = activity;
            mList = list;
            mInflater = LayoutInflater.from(activity);
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
			convertView = mInflater.inflate(R.layout.list_history, null);

			if (position < mList.size()) {
            	final String text = mList.get(position);
            	TextView tv = (TextView) convertView.findViewById(R.id.text);
            	tv.setText(text);
            	
            	convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						mActivity.search(text);
					}
            		
            	});
            }

            return convertView;
		}
    }
    
    private class PlayVideoListener implements OnClickListener {

    	private String mUrl;
    	
    	public PlayVideoListener(String url) {
    		mUrl = url;
    	}
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getActivity(), VideoPlayerActivity.class);
			intent.putExtra(Constant.BUNDLE_STRING_URL, mUrl);
			startActivity(intent);
		}
    }
}