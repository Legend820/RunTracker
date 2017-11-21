package com.bignerdranch.android.runtracker;

import android.support.v4.app.Fragment;

public class RunListActivity extends singleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new RunListFragment();
	}

}
