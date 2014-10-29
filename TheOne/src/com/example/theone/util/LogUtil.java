package com.example.theone.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class LogUtil {
	/**
	 * 是否是开发状态，正式发布要置为false
	 */
	public static final boolean DEBUG=false;
	public static void v(Class<?> clzz,String msg){
		if(DEBUG){
			Log.v(clzz.getSimpleName(), msg);
		}
	}
	
	public static void e(Class<?> clzz,String msg){
		if(DEBUG){
			Log.e(clzz.getSimpleName(), msg);
		}
	}
	
	/**
	 * 
	 * @author 李晓伟
	 * 2014-8-22 下午3:52:00
	 * @param msg
	 * @TODO 打印日志到文件SDCard/hczd/cache/log.txt
	 */
	@SuppressLint("SimpleDateFormat")
	public static void printLog(final String msg){
		if(!DEBUG) return;
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		        String time = formatter.format(new Date());  
		        StringBuffer sb = new StringBuffer(); 
		        sb.append(time+"\n");
		        sb.append(msg+"\n");
		        try {  
		            String fileName = "log.txt";
		            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
		                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"hczd/cache/";  
		                File dir = new File(path);  
		                if (!dir.exists()) {  
		                    dir.mkdirs();  
		                }
		                File file=new File(path+fileName);
		                if(!file.exists()){
		                	file.createNewFile();
		                }
		                FileOutputStream fos = new FileOutputStream(path+fileName, true); 
		                fos.write(sb.toString().getBytes());  
		                fos.close();  
		            }  
		  
		        } catch (Exception e) {  
		        }  
				return null;
			}
		}.execute();
		
	}
	
}
