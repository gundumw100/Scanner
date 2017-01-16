package com.app.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.RemoteViews;

import com.app.scanner.App;
import com.app.scanner.MainActivity;
import com.app.scanner.R;
/**
 * 
 * @author Ni Guijun
 *
 */
public class NotificationUtils {

	private static final int NOTIFY_ID = 0;
	public static void showCustomNotification(Context context) {
		//重启系统时报异常
		//android.app.RemoteServiceException: Bad notification posted from package *: Couldn't create icon: StatusBarIcon
		//http://stackoverflow.com/questions/25317659/how-to-fix-android-app-remoteserviceexception-bad-notification-posted-from-pac
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(context, MainActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// FLAG_ONE_SHOT
		intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder = new Builder(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notify);
        
        boolean isScan=(Boolean)SPUtils.get(context, App.KEY_SCAN, true);
        remoteViews.setTextViewText(R.id.btn_scan,isScan?"隐藏扫描键": "显示扫描键");
        //点击事件处理
        Intent actionIntent = new Intent(App.ACTION_NOTIFICATION);
        actionIntent.putExtra(App.KEY_NOTIFICATION_CLICK, isScan);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_scan, pendingIntent);
        mBuilder.setContent(remoteViews)
                .setContentIntent(contentIntent)
                .setTicker("扫描精灵")
//                .setOngoing(true)
//                .setPriority(Notification.PRIORITY_DEFAULT)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(false)
//                .setSmallIcon(R.drawable.scanner_small)
                //http://stackoverflow.com/questions/24968556/how-to-fix-this-bug-android-app-remoteserviceexception-bad-notification-post
                .setSmallIcon(context.getApplicationInfo().icon)//采用quick fallback image
				.setDefaults(Notification.DEFAULT_ALL);
        
        Notification notify = mBuilder.build();
        notify.flags = Notification.FLAG_NO_CLEAR;//|Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(NOTIFY_ID, notify);
	}
	
	public static void showNotification(Context context) {
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);// FLAG_ONE_SHOT,FLAG_ACTIVITY_NEW_TASK,
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
//		Bitmap largeIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();
		Notification notification = new Notification.Builder(context)
				.setContentTitle("扫描精灵")
				.setContentText("点击打开设置")
				.setContentIntent(contentIntent)
//				.setSmallIcon(R.drawable.scanner_small)
				.setSmallIcon(context.getApplicationInfo().icon)//采用quick fallback image
//				.setLargeIcon(largeIcon)
				.setAutoCancel(false)
				.setWhen(System.currentTimeMillis())
				.setDefaults(Notification.DEFAULT_ALL)
				.getNotification();
		notification.flags |= Notification.FLAG_NO_CLEAR;
		notificationManager.notify(NOTIFY_ID, notification);
	}
	
}
