package com.app.util;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.app.scanner.App;
import com.app.scanner.R;

/**
 * 浮动球
 * @author Ni Guijun
 *
 */
public class FloatViewUtils {

	private WindowManager manager;
	private ImageView floatView;
	
	public void createFloatView(Context context) {
		if (manager == null && floatView == null) {
			int w = 80;// 大小
			int pxValue=dp2px(context,w);
			final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
			// 获取的是WindowManagerImpl.CompatModeWrapper
			manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			// 设置window type
			params.type = LayoutParams.TYPE_PHONE;
			// 设置图片格式，效果为背景透明
			params.format = PixelFormat.RGBA_8888;
			// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
			params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|LayoutParams.FLAG_NOT_FOCUSABLE;
			// 调整悬浮窗显示的停靠位置为左侧置顶
			params.gravity = Gravity.LEFT | Gravity.TOP;
			// 以屏幕左上角为原点，设置x、y初始值，相对于gravity
			int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
			int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
			params.x = screenWidth - pxValue;
			params.y = screenHeight - pxValue;
			params.width = pxValue;
			params.height = pxValue;
	
			floatView=new ImageView(context);
			floatView.setVisibility(View.VISIBLE);
			floatView.setImageResource(R.drawable.ic_scan);
			manager.addView(floatView, params);
			floatView.setOnTouchListener(new OnTouchListener() {
				// 触屏监听
				float lastX, lastY;
				int oldOffsetX, oldOffsetY;
				int tag = 0;// 悬浮球 所需成员变量
		
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					final int action = event.getAction();
					float x = event.getX();
					float y = event.getY();
					if (tag == 0) {
						oldOffsetX = params.x; // 偏移量
						oldOffsetY = params.y; // 偏移量
					}
					if (action == MotionEvent.ACTION_DOWN) {
						lastX = x;
						lastY = y;
					} else if (action == MotionEvent.ACTION_MOVE) {
						params.x += (int) (x - lastX) / 3; // 减小偏移量,防止过度抖动
						params.y += (int) (y - lastY) / 3; // 减小偏移量,防止过度抖动
						tag = 1;
						manager.updateViewLayout(floatView, params);
					} else if (action == MotionEvent.ACTION_UP) {
						int newOffsetX = params.x;
						int newOffsetY = params.y;
						// 只要按钮一动位置不是很大,就认为是点击事件
						if (Math.abs(oldOffsetX - newOffsetX) <= 20 && Math.abs(oldOffsetY - newOffsetY) <= 20) {
							if(onFloatViewClickListener!=null){
								onFloatViewClickListener.onFloatViewClick();
							}
						} else {
							tag = 0;
						}
					}
					return true;
				}
			});
		}
	}

	public void setImageRes(int resID){
		if(floatView!=null){
			floatView.setImageResource(resID);
		}
	}
	
	public void removeFloatView() {
		if (manager != null && floatView != null) {
			manager.removeView(floatView);
			manager = null;
			floatView = null;
		}
	}
	
	public void toggle(Context context){
		boolean isScan=(Boolean)SPUtils.get(context, App.KEY_SCAN, true);
		if(isScan){
			createFloatView(context);
		}else{
			removeFloatView();
		}
	}
	
	private int dp2px(Context context,float dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal,context.getResources().getDisplayMetrics());
	}
	
	
	private OnFloatViewClickListener onFloatViewClickListener;
	
	public void setOnFloatViewClickListener(
			OnFloatViewClickListener onFloatViewClickListener) {
		this.onFloatViewClickListener = onFloatViewClickListener;
	}

	public interface OnFloatViewClickListener{
		public void onFloatViewClick();
	}
}
