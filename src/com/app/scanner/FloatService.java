package com.app.scanner;

import org.simple.eventbus.EventBus;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.app.util.FloatViewUtils;
import com.app.util.SPUtils;

/**
 * 浮动按钮Service
 * 
 * 抽象类，需要实现onFloatViewClick(),用于点击浮动按钮后的具体事件
 * 该Service为辅助功能服务，需要在设置->辅助功能，开启服务
 * 
 * 小米3需要在 设置->应用->选择要开启的软件，勾选开启悬浮窗
 * 小米5需要在 设置->应用管理->授权管理->应用权限管理->选择要开启的软件，勾选开启悬浮窗
 * 
 * @author Ni Guijun
 *
 */
public class FloatService extends AccessibilityService {

	private Context context;
	//需要在ScannerBroadcastReceiver中方便调用toggle(),故做成static
	public static FloatViewUtils floatViewUtils;
//	private ScannerBroadcastReceiver receiver;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
		floatViewUtils=new FloatViewUtils();
		EventBus.getDefault().register(this);
		boolean isScan=(Boolean)SPUtils.get(context, App.KEY_SCAN, true);
		if(isScan){//设置成关闭状态就不用显示按钮
			floatViewUtils.createFloatView(context);
		}
		
		// 动态注册广播
//		receiver = new ScannerBroadcastReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(App.ACTION_NOTIFICATION);
//		registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		floatViewUtils.removeFloatView();
//		unregisterReceiver(receiver);
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		//空实现即可
//		Log.i("tag", "eventText=="+convertEventToText(event));
//		switch (event.getEventType()) {
//		case AccessibilityEvent.TYPE_VIEW_CLICKED:
//			AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(event);
//			 AccessibilityNodeInfoCompat source = record.getSource();
//			 source.performAction(AccessibilityNodeInfoCompat.ACTION_SET_TEXT);
//			 
//			break;
//		default:
//			break;
//			
//		}
		
	}
	
	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub
	}
	
	private String convertEventToText(AccessibilityEvent event){
		String eventText = "";
		switch (event.getEventType()) {
		case AccessibilityEvent.TYPE_VIEW_CLICKED:
			eventText = "TYPE_VIEW_CLICKED";
			break;
		case AccessibilityEvent.TYPE_VIEW_FOCUSED:
			eventText = "TYPE_VIEW_FOCUSED";
			break;
		case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
			eventText = "TYPE_VIEW_LONG_CLICKED";
			break;
		case AccessibilityEvent.TYPE_VIEW_SELECTED:
			eventText = "TYPE_VIEW_SELECTED";
			break;
		case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
			eventText = "TYPE_VIEW_TEXT_CHANGED";
			break;
		case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
			eventText = "TYPE_WINDOW_STATE_CHANGED";
			break;
		case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
			eventText = "TYPE_NOTIFICATION_STATE_CHANGED";
			break;
		case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
			eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_END";
			break;
		case AccessibilityEvent.TYPE_ANNOUNCEMENT:
			eventText = "TYPE_ANNOUNCEMENT";
			break;
		case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
			eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_START";
			break;
		case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
			eventText = "TYPE_VIEW_HOVER_ENTER";
			break;
		case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
			eventText = "TYPE_VIEW_HOVER_EXIT";
			break;
		case AccessibilityEvent.TYPE_VIEW_SCROLLED:
			eventText = "TYPE_VIEW_SCROLLED";
			break;
		case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
			eventText = "TYPE_VIEW_TEXT_SELECTION_CHANGED";
			break;
		case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
			eventText = "TYPE_WINDOW_CONTENT_CHANGED";
			break;
		default:
			eventText = "EventType="+event.getEventType();
			break;
			
		}
		return eventText;
	}

}