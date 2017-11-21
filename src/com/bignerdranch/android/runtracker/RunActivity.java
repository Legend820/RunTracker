package com.bignerdranch.android.runtracker;

import android.support.v4.app.Fragment;

public class RunActivity extends singleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new RunFragment();
	}

	
}
