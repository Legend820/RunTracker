package com.bignerdranch.android.runtracker;

import android.content.Context;
import android.location.Location;

public class LastLocationLoader extends DataLoader<Location> {
	private long mRunId;
	public LastLocationLoader(Context context,long runId) {
		super(context);
		mRunId=runId;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Location loadInBackground() {
		// TODO Auto-generated method stub
		return RunManager.get(getContext()).getLastLocationForRun(mRunId);
	}

}
