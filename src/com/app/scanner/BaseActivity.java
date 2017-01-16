package com.app.scanner;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.AnimRes;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author pythoner
 *
 */
public abstract class BaseActivity extends FragmentActivity{

	public static final String tag = "tags";
	protected Context context;
	public App app;
	private TextView btn_left, btn_right;
	private TextView tv_title;
	private View actionView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 初始化view组件，通常写在实现类的onCreate()中
	 */
	public abstract void initViews();

	/**
	 * 初始化view中内容，通常写在实现类的onResume()中
	 */
	// public abstract void initValues();
	/**
	 * 刷新view中内容，通常写在网络请求成功后的回调函数中
	 */
	public abstract void updateViews(Object obj);

	/**
	 * findViewById的简便写法，直接一个$方法搞定，类似JQuery语法
	 * @param viewID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <T extends View> T $(int viewID) {
	    return (T) findViewById(viewID);
	}
	
	/**
	 * 左右两边分别带一个按钮的通用的ActionBar
	 * 
	 * @param leftBtn
	 * @param drawableForLeftBtn
	 * @param title
	 * @param rightBtn
	 * @param drawableForRightBtn
	 */
	public void initActionBar(String leftBtn, Drawable drawableForLeftBtn,
			String title, String rightBtn, Drawable drawableForRightBtn) {
		
		actionView =findViewById(R.id.actionbar);
		tv_title = (TextView) findViewById(R.id.tv_title);
		if (tv_title != null) {
			tv_title.setText(title);
		}
		btn_left = (TextView) findViewById(R.id.btn_left);
		if (btn_left != null) {
			if(leftBtn==null&&drawableForLeftBtn==null){
				btn_left.setVisibility(View.GONE);
			}else{
				btn_left.setVisibility(View.VISIBLE);
				btn_left.setText(leftBtn==null?"":leftBtn);
				if (drawableForLeftBtn != null) {
					drawableForLeftBtn.setBounds(0, 0,
							drawableForLeftBtn.getMinimumWidth(),
							drawableForLeftBtn.getMinimumHeight());
					btn_left.setCompoundDrawables(drawableForLeftBtn, null, null,
							null);
				}
	
				btn_left.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						doLeftButtonClick(v);
					}
				});
			}
		}

		btn_right = (TextView) findViewById(R.id.btn_right);
		if (btn_right != null) {
			if(rightBtn==null&&drawableForRightBtn==null){
				btn_right.setVisibility(View.GONE);
			}else{
				btn_right.setVisibility(View.VISIBLE);
				btn_right.setText(rightBtn==null?"":rightBtn);
	
				if (drawableForRightBtn != null) {
					drawableForRightBtn.setBounds(0, 0,
							drawableForRightBtn.getMinimumWidth(),
							drawableForRightBtn.getMinimumHeight());
					btn_right.setCompoundDrawables(null, null, drawableForRightBtn,
							null);
				}
	
				btn_right.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						doRightButtonClick(v);
					}
				});
			}
		}
		
	}

	public View getActionView() {
//		actionView.setVisibility(View.VISIBLE);
		return actionView;
	}
	public TextView getRightButton() {
		btn_right.setVisibility(View.VISIBLE);
		return btn_right;
	}

	public TextView getLeftButton() {
		btn_left.setVisibility(View.VISIBLE);
		return btn_left;
	}

	/**
	 * 用于重设ActionBar中的标题
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		tv_title.setText(text);
	}

	/**
	 * 按下右上角执行该方法，需要override该方法
	 */
	public void doRightButtonClick(View v) {

	}

	/**
	 * 按下左上角执行该方法，默认关闭该activity，必要时需要override该方法
	 */
	public void doLeftButtonClick(View v) {
		finish();
	}

	/**
	 * 执行动画
	 * 
	 * @param context
	 * @param view
	 * @param animId
	 */
	public void doAnimation(Context context, View view, @AnimRes int animId) {
		Animation animation = AnimationUtils.loadAnimation(context, animId);
		view.startAnimation(animation);
	}
	
	/**
	 * ActionBar,默认有返回键
	 * 
	 * @param title
	 * @param rightBtn
	 * @param drawableForRightBtn
	 */
	public void initActionBar(String title, String rightBtn, Drawable drawableForRightBtn) {
		initActionBar(null, App.res.getDrawable(R.drawable.left), title, rightBtn, drawableForRightBtn);
	}

	////////////////////////////////////////////////////////////////
	// 
	////////////////////////////////////////////////////////////////
	/**
	 * 判断字符创是否为空或null
	 * @param text
	 * @return
	 */
	public boolean isEmpty(String text){
		return TextUtils.isEmpty(text);
	}
	
	/**
	 * dp转px
	 * 
	 * @param context
	 * @param val
	 * @return
	 */
	public int dp2px(float dpVal)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, context.getResources().getDisplayMetrics());
	}

	/**
	 * px转dp
	 * 
	 * @param context
	 * @param pxVal
	 * @return
	 */
	public float px2dp(float pxVal)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (pxVal / scale);
	}
	////////////////////////////////////////////////////////////////
	// FloatView相关
	////////////////////////////////////////////////////////////////
	private WindowManager wm;
	private ImageView view;// 浮动按钮

	/**
	 * 添加悬浮View
	 * 
	 * @param marginBottom 悬浮View与屏幕底部的距离
	 */
	protected void createFloatView(int marginBottom) {
		int w = 80;// 大小
		int pxValue=dp2px(w);
//		wm = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		view=new ImageView(context);
		view.setVisibility(View.VISIBLE);
		view.setImageResource(R.drawable.ic_scan);
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.type = LayoutParams.TYPE_BASE_APPLICATION;// 所有程序窗口的“基地”窗口，其他应用程序窗口都显示在它上面。
//		params.type = LayoutParams.TYPE_PHONE;
		params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
		params.format = PixelFormat.TRANSLUCENT;// 不设置这个弹出框的透明遮罩显示为黑色
		params.width = pxValue;
		params.height = pxValue;
		params.gravity = Gravity.TOP | Gravity.LEFT;
		int screenWidth = getResources().getDisplayMetrics().widthPixels;
		int screenHeight = getResources().getDisplayMetrics().heightPixels;
		params.x = screenWidth - pxValue;
		params.y = screenHeight - pxValue - marginBottom;
		view.setOnTouchListener(new OnTouchListener() {
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
					wm.updateViewLayout(view, params);
				} else if (action == MotionEvent.ACTION_UP) {
					int newOffsetX = params.x;
					int newOffsetY = params.y;
					// 只要按钮一动位置不是很大,就认为是点击事件
					if (Math.abs(oldOffsetX - newOffsetX) <= 20 && Math.abs(oldOffsetY - newOffsetY) <= 20) {
						onFloatViewClick();
					} else {
						tag = 0;
					}
				}
				return true;
			}
		});
		wm.addView(view, params);
	}

	/**
	 * 点击浮动按钮触发事件，需要override该方法
	 */
	protected void onFloatViewClick() {
		
	}

	/**
	 * 将悬浮View从WindowManager中移除，需要与createFloatView()成对出现
	 */
	public void removeFloatView() {
		if (wm != null && view != null) {
			wm.removeViewImmediate(view);
			// wm.removeView(view);//不要调用这个，WindowLeaked
			view = null;
			wm = null;
		}
	}

	/**
	 * 隐藏悬浮View
	 */
	public void hideFloatView() {
		if (wm != null && view != null && view.isShown()) {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示悬浮View
	 */
	public void showFloatView() {
		if (wm != null && view != null && !view.isShown()) {
			view.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 判断List是否是空列表
	 * 
	 * @param list
	 * @return
	 */
	public boolean isEmptyList(List<?> list) {
		return list == null || list.size() == 0;
	}

	public String nullToEmpty(String text) {
		return text == null ? "" : text;
	}
	////////////////////////////////////////////////////////////////////////////
	// 
	/////////////////////////////////////////////////////////////////////////////
	
	protected Dialog dialog;

	/**
	 * 网络连接时显示Loading
	 */
	public void showProgress() {
		if (dialog == null&&!isFinishing()) {
			dialog = new Dialog(this, R.style.Theme_TransparentDialog);
			// dialog.setContentView(R.layout.progress_dialog);
			dialog.setContentView(new ProgressBar(getApplicationContext()));
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
	}

	public void closeProgress() {
		if (dialog != null&&!isFinishing()) {
			dialog.cancel();
			dialog = null;
		}
	}
	
	private static Toast toast;

	public void showToast(String text) {
		if (null == toast) {
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		} else {
			toast.setText(text);
		}
		toast.show();
	}

	public void showToast(@StringRes int resId) {
		if (null == toast) {
			toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
		} else {
			toast.setText(resId);
		}
		toast.show();
	}
	////////////////////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////////////////////
	/**
	 * 从url中截获11位商品号
	 * 
	 * @param url
	 * @return
	 */
	public String getProdID(String url) {
		// http://shop.banggo.com/product/24409219130true
		if (!TextUtils.isEmpty(url)) {
			int index = url.lastIndexOf("/") + 1;
			String temp = url.substring(index);
			return temp.replace("true", "").trim();
		}
		return null;
	}

	/**
	 * 扫描商品号到TextView
	 * 
	 * @param data
	 * @param tv
	 */
	public void setProdIDToTextView(String data, TextView tv) {
		String prodClsID = getProdID(data);
		if (TextUtils.isEmpty(prodClsID)) {
			showToast(R.string.alert_no_barcode_found);
		} else {
			tv.setText(prodClsID);
		}
	}
	////////////////////////////////////////////////////////////////////////////
	// Thimfone 机型扫描相关
	/////////////////////////////////////////////////////////////////////////////
	/**
	 * 扫描商品后的Handler，需要覆盖onScanProductHandleMessage(id)
	 */
	public Handler resultHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String message = (String) msg.obj;
			if(message.equalsIgnoreCase("time out")){
				showToast(R.string.alert_no_barcode_found);
				return;
			}
			String prodID = getProdID(message);
			if (TextUtils.isEmpty(prodID)) {
				showToast(R.string.alert_no_barcode_found);
			}else{
				onScanProductHandleMessage(prodID);
			}
		}
	};
	
	/**
	 * 扫描到商品码后需要执行的事件，需要重写该函数
	 */
	public void onScanProductHandleMessage(String prodID){
		
	}
	
	// //////////////////////////////////////////////////////////////
	// 网络数据相关
	// //////////////////////////////////////////////////////////////
	/**
	 * 网络请求返回成功与否,若不成功默认显示Toast
	 * 
	 * @param response
	 * @return
	 */
	public boolean isSuccess(JSONObject response) {
		return isSuccess(response, true);
	}

	public boolean isSuccess(JSONObject response, boolean showToast) {
		closeProgress();
		Boolean isSuccess = response.optBoolean("Result");
		if (isSuccess) {
			return true;
		} else {
			if (showToast) {
				String msg = response.optString("Message");
				// String msg = response.optString("ErrMessage");
				showToast(msg);
			}
			return false;
		}
	}

	public <T> T mapperToObject(JSONObject response, Class<T> clazz) {
		String result = null;
		result = response.toString();
		// Log.i("tag", "result="+result);
		if (isEmpty(result)) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		// 反序列化时，忽略不匹配或者Bean不存在的字段
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		try {
			return mapper.readValue(result, clazz);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
