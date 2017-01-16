package com.app.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.app.util.SPUtils;

/**
 * 开机检测
 * @author Ni Guijun
 *
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

	static final String ACTION = "android.intent.action.BOOT_COMPLETED";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		Log.i("tag","action="+action);
		if (ACTION.equals(action)) {
			 Toast.makeText(context,"系统开机",Toast.LENGTH_LONG).show();
			// 重启系统时置为0，表示不马上发通知
//			SPUtils.put(context, App.KEY_NOTIFICATION_SAFE, 0);
			
//			Intent i = new Intent(context, MainActivity.class);
//			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(i);
			   
		}

	}

}
