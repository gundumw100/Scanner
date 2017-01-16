package com.app.scanner;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.app.util.FloatViewUtils;
import com.app.util.Scanner;

/**
 * 扫描服务
 * 
 * 该Service为辅助功能服务，需要在设置->辅助功能，开启服务
 * 
 * 小米3需要在 设置->应用->选择要开启的软件，勾选开启悬浮窗
 * 小米5需要在 设置->应用管理->授权管理->应用权限管理->选择要开启的软件，勾选开启悬浮窗
 * 
 * @author Ni Guijun
 *
 */
public class ScannerFloatService extends FloatService implements FloatViewUtils.OnFloatViewClickListener{

	private Context context;
	private boolean isAppend=true;//追加粘贴or覆盖粘贴
	
	public final static String EVENT_SCAN="scan";//显示扫描按钮
	public final static String EVENT_SHAN="shan";//闪光灯
	public final static String EVENT_DING="ding";//定位灯
	public final static String EVENT_YI="yi";//一维码
	public final static String EVENT_ER="er";//二维码
	public final static String EVENT_ENTER="enter";//换行
	public final static String EVENT_VIBRATE="vibrate";//震动
	public final static String EVENT_STOP="stop";//
	public final static String EVENT_CONTINU="continu";//连扫
	public final static String EVENT_UPDATE_UI_CONTINU="ui_continu";//更新界面
	
	public static Scanner scanner;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
		floatViewUtils.setOnFloatViewClickListener(this);
		initScanner();
	}

	private String lastData;
	private void initScanner(){
		scanner=Scanner.getInstance();
		scanner.setOnScanResultListener(new Scanner.OnScanResultListener() {
			
			@Override
			public void onScanSuccess(String data) {
				// TODO Auto-generated method stub
				if(App.mode_continu==2){
					if(data.equals(lastData)){//去重
						return;
					}else{
						lastData=data;
					}
				}
				performPasteAction(data);
			}
		});
		scanner.init(context);
	}
	
	private void releaseScanner(){
		if(scanner!=null){
			scanner.release();
			scanner=null;
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		releaseScanner();
	}

	@Override
	public void onFloatViewClick() {
		// TODO Auto-generated method stub
//		if(isTest){
//			String data="abcdefg1234567890"+"\n";
////			data=isEnter?data+"\n":data;
//			performPasteAction(data);
//		}else{
			if(scanner==null){
				initScanner();
			}
			if(App.mode_continu==1||App.mode_continu==2){//连扫
				if(scanner.isRunning()){
					floatViewUtils.setImageRes(R.drawable.ic_scan);
					scanner.removeCallbacks();
					EventBus.getDefault().post(ScannerFloatService.EVENT_UPDATE_UI_CONTINU);
				}else{
					floatViewUtils.setImageRes(R.drawable.ic_stop);
					scanner.doContinuScan();
				}
			}else{
				scanner.doScan();
			}
//		}
	}

	private void performPasteAction(String data){
		AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
		if(rootInActiveWindow!=null){
			AccessibilityNodeInfo nodeInfo=rootInActiveWindow.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
			 if (nodeInfo != null) {
				 if(android.os.Build.VERSION.SDK_INT<18){
					 //http://stackoverflow.com/questions/23100695/accessibilitynodeinfo-send-text
					 String text="Not support! The Android API must >= 18";
					 showToast(text);
					 Log.i("tag", text);
					 
////					 final AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(AccessibilityEvent.obtain());
//					 final AccessibilityRecordCompat record = AccessibilityRecordCompat.obtain();
//					 if(record!=null){
//						 AccessibilityNodeInfoCompat source = record.getSource();
//						 source.performAction(AccessibilityNodeInfoCompat.ACTION_PASTE);
//					 }
				 }else if(android.os.Build.VERSION.SDK_INT<21){
					 ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
					 if(isAppend){
						 //追加粘贴
						 ClipData clip = ClipData.newPlainText("label",data );
						 clipboard.setPrimaryClip(clip);
						 nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
					 }else{
						 //覆盖粘贴
						 Bundle arguments = new Bundle();
						 arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,AccessibilityNodeInfo.MOVEMENT_GRANULARITY_WORD);
						 arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN, true);
						 nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, arguments);
						 ClipData clip = ClipData.newPlainText("label", data);
						 clipboard.setPrimaryClip(clip);
						 nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
					 }
				 }else{//API>=21
					 if(isAppend){
						 //追加粘贴
						 ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
						 ClipData clip = ClipData.newPlainText("label",data );
						 clipboard.setPrimaryClip(clip);
						 nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
					 }else{
						 //覆盖粘贴
						 Bundle bundle=new Bundle();
						 bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, data);
						 nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,bundle);
					 }
				 }
			 }else{
				 showToast("没有检测到输入框");
			 }
		}else{
			showToast("请在 设置->辅助功能 中开启Scanner");
		}
	}
	
	private void showToast(String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if(EVENT_STOP.equals(event)){
			releaseScanner();
		}else if(EVENT_SCAN.equals(event)){
			floatViewUtils.toggle(context);
		}else if(EVENT_CONTINU.equals(event)){
			scanner.removeCallbacks();
		}else{
			scanner.resetScanFlag();
		}
	}
	
}