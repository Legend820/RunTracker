package com.bignerdranch.android.runtracker;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class DataLoader<D> extends AsyncTaskLoader<D> {

	private D mData;
	public DataLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	//��������Ƿ��Ѽ��أ�������ؾ��������ͳ�ȥ�����û�м��ؾ͵��ó���forcelod()��������ȡ����
	protected void onStartLoading(){
		if(mData!=null){
			deliverResult(mData);
		}else{
			forceLoad();
		}
	}
	//�Ƚ����ݴ洢������Ȼ�����loader�������͵� �ó��ཫ���ݷ��ͳ�ȥ
	public void deliverResult(D data){
		mData=data;
		if(isStarted()){
			super.deliverResult(data);
		}
	}

}
