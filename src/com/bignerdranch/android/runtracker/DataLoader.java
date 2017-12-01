package com.bignerdranch.android.runtracker;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class DataLoader<D> extends AsyncTaskLoader<D> {

	private D mData;
	public DataLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	//检查数据是否已加载，如果加载就立即发送出去，如果没有加载就调用超类forcelod()方法来获取数据
	protected void onStartLoading(){
		if(mData!=null){
			deliverResult(mData);
		}else{
			forceLoad();
		}
	}
	//先将数据存储起来，然后如果loader已启动就调 用超类将数据发送出去
	public void deliverResult(D data){
		mData=data;
		if(isStarted()){
			super.deliverResult(data);
		}
	}

}
