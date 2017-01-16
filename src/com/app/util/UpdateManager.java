package com.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.app.scanner.R;
import com.app.widget.ArrowDownloadButton;

/**
 * 用法: 
 * mUpdateManager = new UpdateManager(context);
 * mUpdateManager.setOnUpdateListener(new OnUpdateListener() {
 * 
 * @Override public void onUpdate(boolean isUpdate) { // TODO Auto-generated
 *           method stub } });
 * 
 *           mUpdateManager.setUpdateParms(apkUrl, versionCode,forceUpdate,...);
 *           mUpdateManager.doUpdate();
 * @author Administrator
 * 
 */
/**
 * 软件更新自动检测
 * @author Administrator
 *
 */
public class UpdateManager {
	private static final int DOWNLOADING = 1;//下载中
	private static final int DOWNLOADED = 2;//下载完成
	private String mSavePath;
	private int progress;
	private boolean cancelUpdate = false;
	private Context mContext;
//	private ProgressBar mProgress;
//	private NumberProgressBar mProgress;
	private ArrowDownloadButton mProgress;
	private Dialog mDownloadDialog;
	private static String mApkUrl;
	private static String mIntroduction;
	private static String mVersionName;
	private static int mServiceCode;
	private static int mforceUpdate;// 是否强制升级 1-是 0-否
	private String mPackageNameString = "app";
	private OnUpdateListener mOnUpdateListener;

	public interface OnUpdateListener {
		public void onUpdate(boolean isUpdate);
	}

	public UpdateManager(Context context) {
		this.mContext = context;
	}

	public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
		mOnUpdateListener = onUpdateListener;
	}

	/**
	 * 
	 * @param apkUrl apk下载地址
	 * @param serviceCode 新版本号(int)
	 * @param forceUpdate 是否强制升级 1-是 0-否
	 * @param introduction 升级说明
	 * @param versionName 新版本号(String)
	 */
	public void setUpdateParms(String apkUrl, int serviceCode, int forceUpdate,String introduction,String versionName) {
		mApkUrl = apkUrl;
		String[] strs = mApkUrl.split("/");
		String last = strs[strs.length - 1];
		int index = last.indexOf(".");
		mPackageNameString = last.substring(0, index);
		mServiceCode = serviceCode;
		mforceUpdate = forceUpdate;
		mIntroduction=introduction;
		mVersionName=versionName;
	}

	/**
	 * 
	 * 开始检测更新(setUpdateParms后直接调用该方法)
	 */
	public void doUpdate() {
		if (isNeedUpdate()) {
			showNoticeDialog();
		} else {
			if (mOnUpdateListener != null) {
				mOnUpdateListener.onUpdate(false);
			}
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOADING:// 正在下载, 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case DOWNLOADED:// 安装文件
				mProgress.setProgress(100);
//				mProgress.reset();
				mDownloadDialog.dismiss();
				if (mOnUpdateListener != null) {
					mOnUpdateListener.onUpdate(true);
				}
				installApk();
				break;
			default:
				break;
			}
		};
	};

	private boolean isNeedUpdate() {
		int versionCode = getVersionCode(mContext);
		Log.i("tag", "新版本："+mServiceCode+"；使用中的版本:"+versionCode);
		// if (mServiceCode.compareTo(versionCode) > 0)
		if (mServiceCode > versionCode) {
			return true;
		}
		return false;
	}

	private int getVersionCode(Context context) {
		int versionCode = 0;

		try {
			versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	private void showNoticeDialog() {
		if(((Activity)mContext).isFinishing()){
			return;
		}
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_update_title);
//		builder.setMessage(R.string.soft_update_content);
		if(mIntroduction==null){
			mIntroduction="";
		}else{
			mIntroduction=mIntroduction.replace(";", "\n");
		}
		builder.setMessage("新版本"+mVersionName+"\n"+mIntroduction);
		builder.setPositiveButton(R.string.soft_update_immediately,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						showDownloadDialog();
					}
				});

		if (mforceUpdate == 0) {// 不强制更新
			builder.setNegativeButton(R.string.soft_update_later,
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							if (mOnUpdateListener != null) {
								mOnUpdateListener.onUpdate(false);
							}
						}
					});
		}
		Dialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		View v = LayoutInflater.from(mContext).inflate(R.layout.softupdate_progress, null);
//		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
//		mProgress = (NumberProgressBar)v.findViewById(R.id.update_progress);
//		mProgress.setOnProgressBarListener(new com.mb.bgfitting.view.NumberProgressBar.OnProgressBarListener() {
//			
//			@Override
//			public void onProgressChange(int current, int max) {
//				// TODO Auto-generated method stub
//				if(current == max) {
//					mProgress.setProgress(0);
//		        }
//			}
//		});
		mProgress = (ArrowDownloadButton)v.findViewById(R.id.update_progress);
		mProgress.startAnimating();
		builder.setView(v);
		builder.setNegativeButton(R.string.soft_update_cancel,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						cancelUpdate = true;
						if (mOnUpdateListener != null) {
							mOnUpdateListener.onUpdate(false);
						}
						dialog.dismiss();
					}
				});
		mDownloadDialog = builder.create();
		mDownloadDialog.setCancelable(false);
		mDownloadDialog.setCanceledOnTouchOutside(false);
		mDownloadDialog.show();
		downloadApk();
	}

	private void downloadApk() {
		new DownloadApkThread().start();
	}

	private class DownloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				String sdpath = getBaseSavePath(mContext) + "/";
				mSavePath = sdpath + "download";
				URL url = new URL(mApkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(mSavePath);
				if (!file.exists()) {
					file.mkdir();
				}
				File apkFile = new File(mSavePath, mPackageNameString);
				FileOutputStream fos = new FileOutputStream(apkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					mHandler.sendEmptyMessage(DOWNLOADING);
					if (numread <= 0) {
						mHandler.sendEmptyMessage(DOWNLOADED);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!cancelUpdate);
				fos.close();
				is.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private void installApk() {
		File apkfile = new File(mSavePath, mPackageNameString);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
	}

	public static String getBaseSavePath(Context context) {
		String sdStatus = Environment.getExternalStorageState();
		if (sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		} else {
			return context.getFilesDir().getAbsolutePath();
		}
	}

}
