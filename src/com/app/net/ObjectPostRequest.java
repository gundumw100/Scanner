package com.app.net;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * 
 * @author pythoner
 * 
 */
public class ObjectPostRequest extends Request<JSONObject> {

	private Map<String, String> mMap;
	private Listener<JSONObject> mListener;

	public ObjectPostRequest(String url, Map<String, String> map,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(Request.Method.POST, url, errorListener);
		mListener = listener;
		mMap = map;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mMap;
	}

	// 传送
	@Override
	protected void deliverResponse(JSONObject response) {
		if (mListener != null) {
			mListener.onResponse(response);
		}
	}

	// 此处因为response返回值是json数据,和JsonObjectRequest类一样即可
	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		String data=null;
		try {
			data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(data), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
//			IOUtil.writeToSDCard("data.txt","UnsupportedEncodingException-->\n"+data);
			return Response.error(new ParseError(e));
		} catch (JSONException e) {
//			IOUtil.writeToSDCard("data.txt","JSONException-->\n"+data);
			return Response.error(new ParseError(e));
		}
	}
	
}
