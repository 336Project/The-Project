package com.example.theone.webaccess.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.util.Log;

import com.example.theone.util.JsonUtil;
import com.example.theone.util.LogUtil;
import com.example.theone.webaccess.IGetInform;

public class GetInform extends AsyncAccess implements IGetInform{

	public final String URL=BASE_URL+METHOD_PUSH_MESSAGE_INFO;
	
	public GetInform(Class<?> clazz, WebServiceAccessListener listener) {
		super(clazz, listener);
	}

	@Override
	public void getInform(String IMEI, String user_id, String channel_id,
			String app_name) {
		// TODO Auto-generated method stub
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("IMEI", IMEI);
		map.put("user_id", user_id);
		map.put("channel_id", channel_id);
		map.put("app_name", app_name);
		String strMap=JsonUtil.objectToJson(map);
		LogUtil.v(getClass(), strMap);
		Map<String, String> params=new HashMap<String, String>();
		params.put("map",strMap);
		accessToGetResult(URL,params);
	}

}
