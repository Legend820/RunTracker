package com.bignerdranch.android.runtracker;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

public abstract class SQLiteCursorLoader extends AsyncTaskLoader<Cursor> {
	
	private Cursor mCursor;
	public SQLiteCursorLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	protected abstract Cursor loadCursor();
	
	public Cursor loadInBackground(){
		Cursor cursor=loadCursor();
		if(cursor!=null){
			cursor.getCount();
		}
		return cursor;
		
	}
	//发送数据结果
	public void deliverResult(Cursor data){
		Cursor oldCursor=mCursor;
		mCursor=data;
		if(isStarted()){
			super.deliverResult(data);
		}
		if(oldCursor!=null&&oldCursor!=data&&!oldCursor.isClosed()){
			oldCursor.close();
		}
	}
	//检查数据是否已加载，如果加载就立即发送出去，如果没有加载就调用超类forcelod()方法来获取数据
	protected void onStartLoading(){
		if(mCursor!=null){
			deliverResult(mCursor);
		}
		if(takeContentChanged()||mCursor==null){
			forceLoad();
		}
	}
	
	protected void onStopLoading(){
		cancelLoad();
	}
	public void onCanceled(Cursor cursor){
		if(cursor!=null&&!cursor.isClosed()){
			cursor.close();
		}
	}
	
	protected void onReset(){
		super.onReset();
		onStopLoading();
		if(mCursor!=null&&!mCursor.isClosed()){
			mCursor.close();
		}
		mCursor=null;
	}
	
	
	
	
	

}
