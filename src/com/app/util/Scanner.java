package com.app.util;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.app.scanner.App;
import com.zte.scanzbar.ScanBarCodePro;

/**
 * 扫描枪
 * @author Ni Guijun
 *
 */
public class Scanner {

	private Context context;
	private ScanBarCodePro scanbar;
	
	private boolean isScan;
	private int isShan,isDing;
	private boolean isYi,isEr;
	private boolean isEnter;
	private boolean isVibrate;
	
	public static Scanner scanner;
	public static Scanner getInstance(){
		if(scanner==null){
			scanner=new Scanner();
		}
		return scanner;
	}
	
	public void init(final Context context){
		if(scanbar==null){
			this.context=context;
			try{
				scanbar = new ScanBarCodePro(context); //debug
			}catch(Exception e){
				Log.i("tag","初始化ScanBarCodePro异常");
			}
			if(scanbar!=null){
				setScanFlag();
				scanbar.setCallBackListener(new ScanBarCodePro.OnListener(){
		        	@Override            
		        	public void execute(String resultData){
		        		if(!TextUtils.isEmpty(resultData)){
		        			if(isVibrate){
		        				doVibrate(context);
		        			}
		                	CameraSound.getInstance(context).play(CameraSound.SHUTTER_CLICK_TONE3, 0);
		            		String data=resultData;
		            		data=isEnter?data+"\n":data;
		            		if(onScanResultListener!=null){
		            			onScanResultListener.onScanSuccess(data);
		            		}
		        		}
//		        		else{
//		                    showToast("Scan Failed");
//		        		}
		        		if(isRunning){//连扫
		        			handler.post(runnable);
		        		}
		        	}         
		        });
			}
		}
	}
	
	private OnScanResultListener onScanResultListener;
	
	public void setOnScanResultListener(OnScanResultListener onScanResultListener) {
		this.onScanResultListener = onScanResultListener;
	}

	public interface OnScanResultListener{
		void onScanSuccess(String data);
	}
	
	public void release() {
		if(scanbar!=null){
			scanbar.DeInitScanBarCode();
			scanbar=null;
		}
		CameraSound.release();
	}
	
	public void resetScanFlag(){
		setScanFlag();
	}
	
	public void setScanFlag(){
		try{
			if(scanbar!=null){
				isScan=(Boolean)SPUtils.get(context, App.KEY_SCAN, true);
				isShan=(Integer)SPUtils.get(context, App.KEY_SHAN, 0);
				isDing=(Integer)SPUtils.get(context, App.KEY_DING, 0);
				isYi=(Boolean)SPUtils.get(context, App.KEY_YI, true);
				isEr=(Boolean)SPUtils.get(context, App.KEY_ER, true);
				isEnter=(Boolean)SPUtils.get(context, App.KEY_ENTER, true);
				isVibrate=(Boolean)SPUtils.get(context, App.KEY_VIBRATE, true);
				
				//set Scan decode Type
				if(isShan==2){//off
					scanbar.SetFlashLight(0);//0 关闭； 1常亮
				}else if(isShan==1){//on
					scanbar.SetFlashLight(1);
				}else if(isShan==0){
					scanbar.SetFlashLight(2);
				}
				
				if(isDing==0||isDing==2){//off
					scanbar.SetPositionLight (0);//0 关闭； 1常亮
				}else{
					scanbar.SetPositionLight (1);
				}
				
				if(isYi){
					scanbar.SetScanBarCodeType(1);//0 条码扫描识别开启； 1 条码扫描识别关闭
				}else{
					scanbar.SetScanBarCodeType(0);
				}
				if(isEr){
					scanbar.SetScanQRCodeType(1);//0 二维码扫描识别开启； 1 二维码扫描识别关闭
				}else{
					scanbar.SetScanQRCodeType(0);
				}
//				if(isVibrate){//扫描成功手机震动提示
//					scanbar.SetVibrateStats();
//				}else{
//					scanbar.SetVibrateStats();
//				}
			}
		}catch(Exception e){
			Log.i("tag","setScanFlag Exception");
		}
	}
	
	private void doVibrate(Context context) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(100);
	}
	
	private long exitTime = 0;
	private boolean running = false;

	private void tick() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				running = true;
				while (running) {
					if (exitTime >= 10) {
						if(scanbar!=null){
							scanbar.SetPositionLight (0);//0 关闭； 1常亮
						}
						running = false;
					}
					exitTime++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}
	
	public void doScan() {
		if(scanbar!=null){
			if(isDing==0){//AUTO自动关闭定位灯
				scanbar.SetPositionLight(1);//打开定位灯
				exitTime = 0;
				if (!running) {
					tick();
				}
			}
			scanbar.DoScanBar();
		}
	}
	
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			doScan();
		}
	};

	//连扫
	public void doContinuScan() {
		if(!isRunning){
			isRunning=true;
			handler.post(runnable);
		}
	}

	public void removeCallbacks(){
		isRunning=false;
		handler.removeCallbacks(runnable);
	}
	
	private boolean isRunning=false;
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
}
