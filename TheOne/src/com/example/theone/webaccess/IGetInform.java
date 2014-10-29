package com.example.theone.webaccess;

/**
 * 
 * @author 魏天武
 * @version 
 * @create_date 2014-10-29 下午3:23:41
 */
public interface IGetInform {

	/**获取数据的方法*/ 
	public final String METHOD_PUSH_MESSAGE_INFO = "appTabletUpdate";
	
	/**
	 * 获取数据
	 *
	 *@param IMEI
	 *@param user_id
	 *@param channel_id
	 *@param app_name
	 */
	public void getInform(String IMEI,String user_id,String channel_id,String app_name);
}
