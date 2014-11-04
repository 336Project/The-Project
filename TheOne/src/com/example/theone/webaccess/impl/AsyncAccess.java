package com.example.theone.webaccess.impl;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.example.theone.util.JsonUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

/***
 * 搜索相关接口访问类（在线程中进行耗时操作，将结果通过listenter进行返回）
 * 
 * @author ZhongY
 * @Create_date 2014-07-09
 * 
 */
public class AsyncAccess extends BaseAccess {
	public final String HOST = "http://10.1.1.230:8080";
	
	public static final String BASE_URL="http://10.1.1.22:8080/hczd-sys/services/";
	public static final String BASE_TMS_URL="http://10.1.1.154:8088/hczd-tms/services/";
	public static final String BASE_VEH_URL="http://10.1.1.17:8080/hczd-sys/services/";
	public static final String GET_DATA_FALSE="获取数据失败";
	public static final int CONN_TIMES=1;
	public final String SOURCE="APP";
	
	private final String TAG = "AAB_Access";
	private final String RESULT_KEY = "result";
	private final String RESULT_DATA_NULL = "暂无相关数据";
	
	private Handler mHadnler;
	private ExecutorService mExcutorService;// 线程池
	/**
	 *  构造函数，无线程池
	 * @param clazz 目标类
	 * @param listener 回调监听器
	 */
	public AsyncAccess(final Class<?> clazz, final WebServiceAccessListener listener) {
		mHadnler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				if (msg.what == 1) {
					String strResult = msg.getData().getString(RESULT_KEY);
					Log.i(TAG, strResult);
					if(!"{}".equals(strResult)){
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
					Log.i(TAG, strResult);
					if(!"{}".equals(strResult)){
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
					Log.i(TAG, strResult);
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
				// TODO Auto-generated method stub
				if (msg.what == 1) {
					String strResult = msg.getData().getString(RESULT_KEY);
					Log.i(TAG, strResult);
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
					Log.e("", ""+strResult);
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
