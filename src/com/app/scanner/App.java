package com.app.scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;

import com.app.model.Config;
import com.app.model.User;
import com.app.net.RequestManager;
import com.widget.iconify.Iconify;
import com.widget.iconify.font.FontAwesomeModule;

/**
 * 启动类，初始化参数等
 * 
 * @author pythoner
 * 
 */
public class App extends Application{
	public static final String TAG = "tag";
	public static boolean IS_LOG = true;// 是否打印请求返回后的解密数据，发布的时候false
    
	public static boolean needsCatchCrash = true;//捕获异常，开发阶段不需要（打印异常于控制台），发布版本需要
	public static boolean hasNetwork = true;
	public static DisplayMetrics dm;
	public static Resources res;//
	public static Config config = new Config();
	public static User user = new User();
	
	public static final String KEY_SCAN="IS_ON";
	public static final String KEY_SHAN="IS_SHAN";
	public static final String KEY_DING="IS_DING";
	public static final String KEY_YI="IS_YI";
	public static final String KEY_ER="IS_ER";
	public static final String KEY_ENTER="IS_ENTER";
	public static final String KEY_VIBRATE="IS_VIBRATE";
	
	public static final String ACTION_NOTIFICATION="com.app.scanner.ACTION_NOTIFICATION";
	public static final String KEY_NOTIFICATION_CLICK="KEY_NOTIFICATION_CLICK";
	
	public static int mode_continu=0;//
	
	@Override
	public void onCreate() {
		super.onCreate();
		if(needsCatchCrash){
			initCrash();
		}
		initConfig();
		initIconify();
		initVolley();
	}

	private void initCrash(){
		//如果设置了错误捕获，需要在子工程的manifest.xml中添加:
		//<activity android:process=":error_activity" android:name="com.base.app.DefaultErrorActivity" android:screenOrientation="portrait"/>
        //<activity android:process=":error_activity" android:name="com.base.app.CustomErrorActivity" android:screenOrientation="portrait"/>
		//自定义错误显示界面，如果没有设置指定界面，则显示默认的DefaultErrorActivity界面
//      CrashHelper.setErrorActivityClass(CustomErrorActivity.class);
		CrashHelper.install(this);
	}
	
	private void initVolley() {
		RequestManager.init(this);
	}
	
	private void initIconify(){
		Iconify
        .with(new FontAwesomeModule());
//		.with(new EntypoModule())
//        .with(new TypiconsModule())
//        .with(new MaterialModule())
//        .with(new MeteoconsModule())
//        .with(new WeathericonsModule())
//        .with(new SimpleLineIconsModule())
//        .with(new IoniconsModule());
	}
	
	private void initConfig() {
		res = getResources();
		dm = res.getDisplayMetrics();
		// 初始化系统信息
		config.setTotalMemory(this.getTotalMemory());
		config.setAvailMemory(this.getAvailMemory());
		config.setDensityDpi(dm.densityDpi);
		config.setScreenWidth(dm.widthPixels);
		config.setScreenHeight(dm.heightPixels);
		config.setStatusBarHeight(getStatusBarHeight());
		config.setModel(android.os.Build.MODEL);
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		config.setDeviceId(tm.getDeviceId());
		config.setSdk(android.os.Build.VERSION.SDK_INT);
		config.setRelease(android.os.Build.VERSION.RELEASE);
		String androidID = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);
		config.setAndroidID(androidID);
		try {
			PackageInfo packInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			config.setVersionCode(packInfo.versionCode);
			config.setVersionName(packInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, config.toString());
	}
	
	private String getAvailMemory() {// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		return Formatter.formatFileSize(getBaseContext(), mi.availMem);// 将获取的内存大小规格化
	}

	private String getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			arrayOfString = str2.split("\\s+");
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();
		} catch (IOException e) {

		}
		return Formatter.formatFileSize(getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @return
	 */
	private int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height","dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	
}
