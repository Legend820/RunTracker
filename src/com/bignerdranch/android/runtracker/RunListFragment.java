package com.bignerdranch.android.runtracker;

import com.bignerdranch.android.runtracker.RunDatabaseHelper.RunCursor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class RunListFragment extends ListFragment {
	private static final int REQUEST_NEW_RUN=0;
	private RunCursor mCursor;
	public static  RunManager mRunManager;
	private boolean isFirst;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//开启右上角的菜单
		setHasOptionsMenu(true);
		isFirst=true;
		mRunManager = RunManager.get(getActivity());
		mCursor=RunManager.get(getActivity()).queryRuns();
		
		RunCursorAdapter adapter=new RunCursorAdapter(getActivity(),mCursor);
		setListAdapter(adapter);
	}
	public void onDestroy(){
		mCursor.close();
		super.onDestroy();
	}
	public void onPause(){
		super.onPause();
		isFirst=false;
	}
	//从详情页跳转过来时更新adapter内容
	public void onResume(){
		super.onResume();
		if(isFirst!=true){
		mCursor=RunManager.get(getActivity()).queryRuns();
		RunCursorAdapter adapter=new RunCursorAdapter(getActivity(),mCursor);
		setListAdapter(adapter);
		}
	}
	
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.run_list_options, menu);
	}
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			case R.id.menu_item_new_run:
			Intent i=new Intent(getActivity(),RunActivity.class);
			startActivityForResult(i,REQUEST_NEW_RUN);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}	
	}
	public void onActivityResult(int requestCode,int resultCode,Intent data){
		if(REQUEST_NEW_RUN==requestCode){
			mCursor.requery();
			((RunCursorAdapter)getListAdapter()).notifyDataSetChanged();
		}
	}
	
	public void onListItemClick(ListView l,View v,int position,long id){
		Intent i=new Intent(getActivity(),RunActivity.class);
		i.putExtra(RunActivity.EXTRA_RUN_ID, id);
		startActivity(i);
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
			
			 boolean trackingThisRun=mRunManager.isTrackingRun(run);

			 boolean started = mRunManager.isTrackingRun();
			TextView startDateTextView=(TextView)view;
			String dt= DateFormat.format("yyyy-MM-dd,hh:mm:ss,EEEE", run.getStartDate()).toString();
			//拼接字符串
			String cellText=context.getString(R.string.cell_text, dt);
			if(started&&trackingThisRun) startDateTextView.setTextColor(Color.RED);
			startDateTextView.setText(cellText);
		}
	
	}
	

}
