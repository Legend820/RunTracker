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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class RunFragment extends Fragment {
    private static final String TAG = "RunFragment";
    private static final String ARG_RUN_ID="RUN_ID";
    private RunManager mRunManager;
    private Run mRun;
    private Location mLastLocation;
    private Button mStartButton, mStopButton;
    private TextView mStartedTextView, mLatitudeTextView, 
        mLongitudeTextView, mAltitudeTextView, mDurationTextView;
    
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
        setRetainInstance(true);
        mRunManager = RunManager.get(getActivity());
        Bundle args=getArguments();
        if(args!=null){
        	long runId=args.getLong(ARG_RUN_ID,-1);
        	if(runId!=-1){
        		mRun=mRunManager.getRun(runId);
        		mLastLocation=mRunManager.getLastLocationForRun(runId);
        	}
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, container, false);
        
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
        if (mLastLocation != null) {
            durationSeconds = mRun.getDurationSeconds(mLastLocation.getTime());

            mLatitudeTextView.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeTextView.setText(Double.toString(mLastLocation.getLongitude()));
            mAltitudeTextView.setText(Double.toString(mLastLocation.getAltitude()));
            mDurationTextView.setText(Run.formatDuration(durationSeconds));
        }

        
        mStartButton.setEnabled(!started);
        //mStopButton.setEnabled(started);
        mStopButton.setEnabled(started||trackingThisRun);
    }
    
}
