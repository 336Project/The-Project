package com.example.theone.webaccess.impl;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.example.theone.util.JsonUtil;
import com.example.theone.util.LogUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/***
 * 接口访问类
 * 
 * @Create_date 2014-07-09
 * 
 */
public class AsyncAccess extends BaseAccess {
	public static final String BASE_URL="";
	public static final String GET_DATA_FALSE="获取数据失败";
	public static final int CONN_TIMES=1;
	
	private final String RESULT_KEY = "result";
	private final String RESULT_DATA_NULL = "暂无相关数据";
	
	private Handler mHadnler;
	private ExecutorService mExcutorService;// 线程池
	/**
	 *  构造函数，无线程池(优先使用这个)
	 * @param clazz 目标类
	 * @param listener 回调监听器
	 */
	public AsyncAccess(final Class<?> clazz, final WebServiceAccessListener listener) {
		mHadnler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				if (msg.what == 1) {
					String strResult = msg.getData().getString(RESULT_KEY);
					if(!"{}".equals(strResult)){
						LogUtil.v(getClass(), strResult);
						if(clazz!=null){
							Object objResult = JsonUtil.jsonToBean(strResult, clazz);
							listener.getAccessResult(true, objResult, null);
						}else{
							listener.getAccessResult(true, strResult, null);
						}
					}else{
						listener.getAccessResult(false, null, RESULT_DATA_NULL);
					}
				} else {
					listener.getAccessResult(false, null, GET_DATA_FALSE);
				}
				return false;
			}
		});
	}
	/***
	 * 构造函数,使用线程池
	 * @param excutorService 线程池
	 * @param clazz 目标类
	 * @param listener 回调监听器
	 */
	public AsyncAccess(ExecutorService excutorService, final Class<?> clazz, final WebServiceAccessListener listener) {
		mExcutorService = excutorService;
		mHadnler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				if (msg.what == 1) {
					String strResult = msg.getData().getString(RESULT_KEY);
					if(!"{}".equals(strResult)){
						LogUtil.v(getClass(), strResult);
						if(clazz!=null){
							Object objResult = JsonUtil.jsonToBean(strResult, clazz);
							listener.getAccessResult(true, objResult, null);
						}else{
							listener.getAccessResult(true, strResult, null);
						}
					}else{
						listener.getAccessResult(false, null, RESULT_DATA_NULL);
					}
				} else {
					listener.getAccessResult(false, null, GET_DATA_FALSE);
				}
				return false;
			}
		});
	}
	/***
	 * 构造函数，无线程池，无目标类泛型
	 * @param listener 回调监听器
	 */
	public AsyncAccess(final WebServiceAccessListener listener) {
		mHadnler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				if (msg.what == 1) {
					String strResult = msg.getData().getString(RESULT_KEY);
					listener.getAccessResult(true, strResult, null);
				} else {
					listener.getAccessResult(false, null, GET_DATA_FALSE);
				}
				return false;
			}
		});
	}
	/***
	 * 构造函数，使用线程池，无目标类泛型
	 * @param excutorService 线程池
	 * @param listener 回调监听器
	 */
	public AsyncAccess(ExecutorService excutorService, final WebServiceAccessListener listener) {
		mExcutorService = excutorService;
		mHadnler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				if (msg.what == 1) {
					String strResult = msg.getData().getString(RESULT_KEY);
					listener.getAccessResult(true, strResult, null);
				} else {
					listener.getAccessResult(false, null, GET_DATA_FALSE);
				}
				return false;
			}
		});
	}
	/***
	 * 访问接口获取数据
	 * 
	 * @param accessUrl 访问地址
	 * @param MethodName 方法名
	 * @param params	参数
	 */
	public void accessToGetResult(final String accessUrl,final Map<String, String> params) {
		
		if(mExcutorService!=null){
			mExcutorService.execute(new Runnable() {

				@Override
				public void run() {
					int connTimes = CONN_TIMES;
					while(connTimes>=0){
						String strResult = postRequest(accessUrl, params);
						if (!TextUtils.isEmpty(strResult)) {
							Bundle bundle = new Bundle();
							bundle.putString(RESULT_KEY, strResult);
							Message msg = new Message();
							msg.setData(bundle);
							msg.what = 1;
							mHadnler.sendMessage(msg);
							break;
						} else{
							connTimes--;
						}
						if(connTimes<0){
							mHadnler.sendEmptyMessage(0);
						}
					}
				}
			});
		}else{
			accessToGetResultByThread(accessUrl, params);
		}
	}
	/***
	 * 访问接口，无线程池
	 * 
	 * @param accessUrl 访问地址
	 * @param MethodName 方法名
	 * @param params	参数
	 */
	public void accessToGetResultByThread(final String accessUrl, final Map<String, String> params) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int connTimes = CONN_TIMES;
				while(connTimes>=0){
					String strResult = postRequest(accessUrl, params);
					if (!TextUtils.isEmpty(strResult)) {
						Bundle bundle = new Bundle();
						bundle.putString(RESULT_KEY, strResult);
						Message msg = new Message();
						msg.setData(bundle);
						msg.what = 1;
						mHadnler.sendMessage(msg);
						break;
					} else{
						connTimes--;
					}
					if(connTimes<0){
						mHadnler.sendEmptyMessage(0);
					}
				}
			}
		}).start();
	}
}
