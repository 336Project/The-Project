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
 * ͨ��action���н���
 * @author κ����
 * @version 
 * @create_date 2014-10-29 ����9:38:56
 */
public class DownData {
	// ����HttpClient����
    public static HttpClient httpClient = new DefaultHttpClient();
    
    public Handler mHandler;//���ز�ѯ���
    public String url;//�������ݵĵ�ַ
    private Map<String,String> rawParams;//Ҫ��������Ĳ���
 
    /**
     * û�в���ʱ����
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
     * �в���ʱ���е���
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
     *            ���������URL
     * @return ��������Ӧ�ַ���
     * @throws Exception
     */
    public void getRequest(){
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// ����HttpGet����
		        HttpGet get = new HttpGet(url);
		        // ����GET����
		        HttpResponse httpResponse;
				try {
					httpResponse = httpClient.execute(get);
					// ����������ɹ��ط�����Ӧ
			        if (httpResponse.getStatusLine().getStatusCode() == 200) {
			            // ��ȡ��������Ӧ�ַ���
			            String result = EntityUtils.toString(httpResponse.getEntity());
			            successOrFail(1, result);
			        } else {
			            successOrFail(0, "��������Ӧ����:"+httpResponse.getStatusLine().getStatusCode());
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
     *            ���������URL
     * @param params
     *            �������
     * @return ��������Ӧ�ַ���
     * @throws Exception
     */
    public void postRequest() {
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// ����HttpPost����
		        HttpPost post = new HttpPost(url);
		        // ������ݲ��������Ƚ϶�Ļ����ԶԴ��ݵĲ������з�װ
		        List<NameValuePair> params = new ArrayList<NameValuePair>();
		        for (String key : rawParams.keySet()) {
		            // ��װ�������
		            params.add(new BasicNameValuePair(key, rawParams.get(key)));
		        }	
		        // �����������
		        try {
					post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
					// ����POST����
			        HttpResponse httpResponse = httpClient.execute(post);
			        // ����������ɹ��ط�����Ӧ
			        if (httpResponse.getStatusLine().getStatusCode() == 200) {
			            // ��ȡ��������Ӧ�ַ���
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
     * ���ݼ��سɹ���ʧ�ܽ������ݴ���
     *
     *@param flag  1����ɹ���0����ʧ��
     *@param result ����������ɹ����ؽ����ʧ�ܷ���ʧ����Ϣ
     */
    private void successOrFail(int flag,String result){
    	Message msg=new Message();
		Bundle data=new Bundle();
		msg.what=flag;//what=1 ����ɹ�
        data.putString("result", result);
		msg.setData(data);
		mHandler.sendMessage(msg);
    }
    
}
