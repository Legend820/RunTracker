package com.bignerdranch.android.runtracker;

import android.content.Context;

public class RunLoader extends DataLoader<Run> {
	private long mRunId;
	public RunLoader(Context context,long runId) {
		super(context);
		mRunId=runId;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Run loadInBackground() {
		// TODO Auto-generated method stub
		return RunManager.get(getContext()).getRun(mRunId);
	}

}
