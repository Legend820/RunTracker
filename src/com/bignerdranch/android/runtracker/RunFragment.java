package com.bignerdranch.android.runtracker;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class RunFragment extends Fragment {
    private static final String TAG = "RunFragment";
    private static final String ARG_RUN_ID="RUN_ID";
    private static final int LOAD_RUN=0;
    private static final int LOAD_LOCATION=1;
    private RunManager mRunManager;
    private Run mRun;
    private Location mLastLocation;
    private Button mStartButton, mStopButton;
    private TextView mStartedTextView, mLatitudeTextView, 
        mLongitudeTextView, mAltitudeTextView, mDurationTextView;
    
    private class LocationLoaderCallbacks implements LoaderCallbacks<Location>{

		@Override
		public Loader<Location> onCreateLoader(int id, Bundle args) {
			// TODO Auto-generated method stub
			return new LastLocationLoader(getActivity(),args.getLong(ARG_RUN_ID));
		}

		@Override
		public void onLoadFinished(Loader<Location> loader, Location location) {
			// TODO Auto-generated method stub
			mLastLocation=location;
			updateUI();
		}

		@Override
		public void onLoaderReset(Loader<Location> loader) {
			// TODO Auto-generated method stub
			
		}
    }
    
    
    private class RunLoaderCallbacks implements LoaderCallbacks<Run>{

		@Override
		public Loader<Run> onCreateLoader(int id, Bundle args) {
			// TODO Auto-generated method stub
			return new RunLoader(getActivity(),args.getLong(ARG_RUN_ID));
		}

		@Override
		public void onLoadFinished(Loader<Run> loader, Run run) {
			// TODO Auto-generated method stub
			mRun=run;
			updateUI();
		}

		@Override
		public void onLoaderReset(Loader<Run> loader) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
    
    private BroadcastReceiver mLocationReceiver = new LocationReceiver() {
        protected void onLocationReceived(Context context, Location loc) {
        	if(!mRunManager.isTrackingRun(mRun))
        		return;
        	
            mLastLocation = loc;
            if (isVisible()) 
                updateUI();
        }
        
        protected void onProviderEnabledChanged(boolean enabled) {
            int toastText = enabled ? R.string.gps_enabled : R.string.gps_disable;
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
        }
        
    };
    
    public static RunFragment newInstance(long runId){
    	Bundle args=new Bundle();
    	args.putLong(ARG_RUN_ID, runId);
    	RunFragment rf=new RunFragment();
    	rf.setArguments(args);
		return rf;
    	
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //在配置变化的时候将这个fragment保存下来
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mRunManager = RunManager.get(getActivity());
        Bundle args=getArguments();
        if(args!=null){
        	long runId=args.getLong(ARG_RUN_ID,-1);
        	if(runId!=-1){
//        		mRun=mRunManager.getRun(runId);
//       		mLastLocation=mRunManager.getLastLocationForRun(runId);
        		LoaderManager lm=getLoaderManager();
        		lm.initLoader(LOAD_RUN, args, new RunLoaderCallbacks());
        		lm.initLoader(LOAD_LOCATION, args, new LocationLoaderCallbacks());
        	}
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    		case android.R.id.home:
    			if(NavUtils.getParentActivityName(getActivity())!=null){
    				NavUtils.navigateUpFromSameTask(getActivity());
    			}
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
		
    	
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, container, false);
        if(NavUtils.getParentActivityName(getActivity())!=null){
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mStartedTextView = (TextView)view.findViewById(R.id.run_startedTextView);
        mLatitudeTextView = (TextView)view.findViewById(R.id.run_latitudeTextView);
        mLongitudeTextView = (TextView)view.findViewById(R.id.run_longitudeTextView);
        mAltitudeTextView = (TextView)view.findViewById(R.id.run_altitudeTextView);
        mDurationTextView = (TextView)view.findViewById(R.id.run_durationTextView);
        
        mStartButton = (Button)view.findViewById(R.id.run_startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mRunManager.startLocationUpdates();
//                mRun = new Run();
              //  mRun=mRunManager.startNewRun();
            	if(mRun==null){
            		mRun=mRunManager.startNewRun();
            	}else{
            		mRunManager.startTrackingRun(mRun);
            	}
            	updateUI();
            }
        });
        
        mStopButton = (Button)view.findViewById(R.id.run_stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mRunManager.stopLocationUpdates();
            	mRunManager.stopRun();
            	updateUI();
            }
        });
        
        updateUI();
        
        return view;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mLocationReceiver, 
                new IntentFilter(RunManager.ACTION_LOCATION));
    }
    
    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }
    
    private void updateUI() {
        boolean started = mRunManager.isTrackingRun();
        boolean trackingThisRun=mRunManager.isTrackingRun(mRun);
        
        if (mRun != null){
        	Date mTime=mRun.getStartDate();
        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mStartedTextView.setText(sdf.format(mTime));
        }
        int durationSeconds = 0;
        mDurationTextView.setText(Run.formatDuration(durationSeconds));
        if (mRun!=null&&mLastLocation != null) {
            durationSeconds = mRun.getDurationSeconds(mLastLocation.getTime());

            mLatitudeTextView.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeTextView.setText(Double.toString(mLastLocation.getLongitude()));
            mAltitudeTextView.setText(Double.toString(mLastLocation.getAltitude()));
            mDurationTextView.setText(Run.formatDuration(durationSeconds));
        }

        
        mStartButton.setEnabled(!started);
        //mStopButton.setEnabled(started);
        mStopButton.setEnabled(started&&trackingThisRun);
    }
    
}
