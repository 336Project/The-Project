package com.example.theone.webaccess.impl;


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

/**
 * 数据加载基类(核心下载类，进行下载操作)
 * @author 魏天武
 * @version 
 * @create_date 2014-10-29 下午2:59:37
 */
public class BaseAccess {
	// 创建HttpClient对象
    public static HttpClient httpClient = new DefaultHttpClient();
	/**
	 * 接口访问监听
	 * @author 魏天武
	 * @version 
	 * @create_date 2014-10-29 下午3:00:25
	 */
	public interface WebServiceAccessListener {
		/***
		 * 访问接口获得返回结果的回调函数
		 * @param isAccessSuccess 访问接口是否成功，仅作访问是否成功判断，数据操作是否成功看返回的数据具体处理
		 * @param objResult 结果数据，访问接口失败则为空
		 * @param msg 消息,请求失败才会有内容
		 */
		public void getAccessResult(boolean isAccessSuccess,Object objResult,String msg);
	}

    /**
     * 访问web获取json数据
     *
     *@param url
     *@param rawParams
     *@return
     */
    public static String postRequest(String url, Map<String, String> rawParams){
        // 创建HttpPost对象。
    	try {
    		HttpResponse httpResponse;
    		if(rawParams==null){//没有参数的时候直接请求
    			HttpGet get = new HttpGet(url);
    	        // 发送GET请求
    	        httpResponse = httpClient.execute(get);
    		}else{//有参数时，进行传入参数请求
    			HttpPost post = new HttpPost(url);
    	        // 如果传递参数个数比较多的话可以对传递的参数进行封装
    	        List<NameValuePair> params = new ArrayList<NameValuePair>();
    	        for (String key : rawParams.keySet()) {
    	            // 封装请求参数
    	            params.add(new BasicNameValuePair(key, rawParams.get(key)));
    	        }
    	        // 设置请求参数
				post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				// 发送POST请求
		        httpResponse = httpClient.execute(post);
		        // 如果服务器成功地返回响应
    		}
        
	        if (httpResponse.getStatusLine().getStatusCode() == 200) {
	            // 获取服务器响应字符串
	            String result = EntityUtils.toString(httpResponse.getEntity());
	            return result;
	        }
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
}
