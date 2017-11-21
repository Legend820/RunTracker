package com.bignerdranch.android.runtracker;

import com.bignerdranch.android.runtracker.RunDatabaseHelper.RunCursor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class RunListFragment extends ListFragment {
	private RunCursor mCursor;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mCursor=RunManager.get(getActivity()).queryRuns();
		RunCursorAdapter adapter=new RunCursorAdapter(getActivity(),mCursor);
		setListAdapter(adapter);
	}
	public void onDestroy(){
		mCursor.close();
		super.onDestroy();
	}

	private static class RunCursorAdapter extends CursorAdapter{
		private RunCursor mRunCursor;
		public RunCursorAdapter(Context context, RunCursor cursor) {
			super(context,cursor,0);
			mRunCursor=cursor;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater.inflate(android.R.layout.simple_list_item_1, parent,false);
		}
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			Run run=mRunCursor.getRun();
			TextView startDateTextView=(TextView)view;
			String cellText=context.getString(R.string.cell_text, run.getStartDate());
			startDateTextView.setText(cellText);
		}


		
	}
}
