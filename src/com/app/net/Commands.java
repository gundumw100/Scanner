package com.app.net;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;

/**
 * 
 * @author pythoner
 *
 */
public final class Commands {

	public static final String IP = "http://139.196.53.136:8080";
//	public static final String BASE_URL = "http://www.jianve.com:8081/handler/user_handler.ashx";
	public static final String BASE_URL = "http://139.196.53.136:8081/handler/user_handler.ashx";

	private Commands() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}


	/**
	 * 检查版本接口
	 * 
	 * @param context
	 * @param device_type
	 * @param version_id
	 * @param onSuccessListener
	 */
	public static void doCommandCheckVersion(Context context, String device_type, String version_id,
			Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setDevice_type(device_type);
		pars.setVersion_id(version_id);
		doCommand(context, "CheckVersion", pars, onSuccessListener);
	}

	//////////////////////////////////////////////
	// base
	//////////////////////////////////////////////
	/**
	 * 统一调用VolleyHelper执行连接命令，有loading
	 * 
	 * @param context
	 * @param map
	 * @param onSuccessListener
	 */
//	private static void doCommand(Context context, Map<String, String> map, Listener<JSONObject> onSuccessListener) {
//		VolleyHelper.execPostRequest(context, BASE_URL, map, onSuccessListener);
//	}

	/**
	 * 统一调用VolleyHelper执行连接命令，可控制loading有无
	 * 
	 * @param context
	 * @param map
	 * @param onSuccessListener
	 * @param showProgress
	 */
	private static void doCommand(Context context, Map<String, String> map, Listener<JSONObject> onSuccessListener,
			boolean showProgress) {
		VolleyHelper.execPostRequest(context, BASE_URL, map, onSuccessListener, showProgress);
	}

	/**
	 * 将对象序列化为JSON字符串
	 * 
	 * @param object
	 * @return JSON字符串
	 */
	private static String toJSon(Object object) {
		return new Gson().toJson(object);// 使用Gson
	}

	public static void doCommand(Context context, String actionName, Pars pars,
			Listener<JSONObject> onSuccessListener,boolean showProgress) {
		Message message = new Message();
		message.setPars(pars);
		message.setActionName(actionName);
//		 Log.i("tag", toJSon(message));
		Map<String, String> map = new HashMap<String, String>();
		map.put("inData", toJSon(message));
		doCommand(context, map, onSuccessListener,showProgress);
	}

	public static void doCommand(Context context, String actionName, Pars pars,
			Listener<JSONObject> onSuccessListener) {
		doCommand(context, actionName, pars, onSuccessListener, true);
	}

}
