package com.example.theone.webconnect;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 通过action进行交互
 * @author 魏天武
 * @version 
 * @create_date 2014-10-29 上午9:38:56
 */
public class DownData {
	// 创建HttpClient对象
    public static HttpClient httpClient = new DefaultHttpClient();
    
    public Handler mHandler;//返回查询结果
    public String url;//访问数据的地址
    private Map<String,String> rawParams;//要进行输入的参数
 
    /**
     * 没有参数时调用
     * @param url
     * @param mHandler
     */
    public DownData(String url,Handler mHandler){
    	this.url=url;
    	this.mHandler=mHandler;
    	try {
			getRequest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("", "Exception:"+e.toString());
		}
    }
    
    /**
     * 有参数时进行调用
     * @param url
     * @param rawParams
     * @param mHandler
     */
    public DownData(String url, Map<String,String> rawParams,Handler mHandler){
    	this.url=url;
    	this.rawParams=rawParams;
    	this.mHandler=mHandler;
    	try {
			postRequest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("", "Exception:"+e.toString());
		}
    }
    /**
     * 
     * @param url
     *            发送请求的URL
     * @return 服务器响应字符串
     * @throws Exception
     */
    public void getRequest(){
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 创建HttpGet对象。
		        HttpGet get = new HttpGet(url);
		        // 发送GET请求
		        HttpResponse httpResponse;
				try {
					httpResponse = httpClient.execute(get);
					// 如果服务器成功地返回响应
			        if (httpResponse.getStatusLine().getStatusCode() == 200) {
			            // 获取服务器响应字符串
			            String result = EntityUtils.toString(httpResponse.getEntity());
			            successOrFail(1, result);
			        } else {
			            successOrFail(0, "服务器响应代码:"+httpResponse.getStatusLine().getStatusCode());
			        }
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					successOrFail(0, e.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					successOrFail(0, e.toString());
				}
			}
		});
    }
 
    /**
     * 
     * @param url
     *            发送请求的URL
     * @param params
     *            请求参数
     * @return 服务器响应字符串
     * @throws Exception
     */
    public void postRequest() {
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 创建HttpPost对象。
		        HttpPost post = new HttpPost(url);
		        // 如果传递参数个数比较多的话可以对传递的参数进行封装
		        List<NameValuePair> params = new ArrayList<NameValuePair>();
		        for (String key : rawParams.keySet()) {
		            // 封装请求参数
		            params.add(new BasicNameValuePair(key, rawParams.get(key)));
		        }	
		        // 设置请求参数
		        try {
					post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
					// 发送POST请求
			        HttpResponse httpResponse = httpClient.execute(post);
			        // 如果服务器成功地返回响应
			        if (httpResponse.getStatusLine().getStatusCode() == 200) {
			            // 获取服务器响应字符串
			            String result = EntityUtils.toString(httpResponse.getEntity());
			            successOrFail(1,result);
			        }
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					successOrFail(0,e.toString());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					successOrFail(0,e.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					successOrFail(0,e.toString());
				}
		    }
		});
    }
    
    /**
     * 数据加载成功或失败进行数据处理
     *
     *@param flag  1代表成功，0代表失败
     *@param result 结果参数，成功返回结果，失败返回失败消息
     */
    private void successOrFail(int flag,String result){
    	Message msg=new Message();
		Bundle data=new Bundle();
		msg.what=flag;//what=1 代表成功
        data.putString("result", result);
		msg.setData(data);
		mHandler.sendMessage(msg);
    }
    
}
