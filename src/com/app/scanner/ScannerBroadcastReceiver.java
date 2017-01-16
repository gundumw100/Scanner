package com.app.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.util.NotificationUtils;
import com.app.util.SPUtils;

/**
 * 广播，控制显示隐藏扫描按钮，并通知Notification更新
 * @author Ni Guijun
 *
 */
public class ScannerBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if(App.ACTION_NOTIFICATION.equals(action)){
			if(FloatService.floatViewUtils!=null){
				boolean isScan=intent.getBooleanExtra(App.KEY_NOTIFICATION_CLICK, true);
				SPUtils.put(context, App.KEY_SCAN, !isScan);
				//不能直接使用EventBus，否则Notification会消失
	//			EventBus.getDefault().post(ScannerFloatService.EVENT_SCAN);
				FloatService.floatViewUtils.toggle(context);//
				//通知更新Notification
				NotificationUtils.showCustomNotification(context);
			}
		}
	}
}
