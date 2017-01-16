package com.app.scanner;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.VersionInfo;
import com.app.model.response.CheckVersionResponse;
import com.app.net.Commands;
import com.app.util.NotificationUtils;
import com.app.util.SPUtils;
import com.app.util.UpdateManager;
import com.app.util.UpdateManager.OnUpdateListener;
import com.widget.expandablelayout.ExpandableLayout;

/**
 * 
 * @author Ni Guijun
 *
 */
public class MainActivity extends BaseActivity implements OnClickListener{

	private CheckBox cb_ball;
	private RadioGroup rg_continu,rg_shan,rg_ding,rg_yi,rg_er,rg_enter,rg_vibrate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		updateViews(null);
		doCommandCheckVersion(false);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//update the float Text
		boolean isScan=(Boolean)SPUtils.get(context, App.KEY_SCAN, true);
		cb_ball.setText(isScan?"隐藏":"显示");
		cb_ball.setChecked(isScan);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void doCommandCheckVersion(final boolean showTip){
		String device_type = "SCAN";
		String version_id = String.valueOf(App.config.getVersionCode());
		Commands.doCommandCheckVersion(context, device_type, version_id, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				 Log.i("tag", response.toString());
				 if (isSuccess(response)) {
						CheckVersionResponse obj = mapperToObject(response, CheckVersionResponse.class);
						final VersionInfo bean = obj.getVersion_Info();
						if (bean != null) {
							UpdateManager updateManager = new UpdateManager(context);
							updateManager.setOnUpdateListener(new OnUpdateListener() {

								@Override
								public void onUpdate(boolean isUpdate) {
									// TODO Auto-generated method
									if (isUpdate) {
									}
								}
							});
							int forceupdate = bean.getForceupdate() ? 1 : 0;
							updateManager.setUpdateParms(bean.getFile_url(), Integer.parseInt(bean.getVersion()),
									forceupdate, bean.getIntroduction(), bean.getVersion_name());
							updateManager.doUpdate();
						}else{
							if(showTip){
								showToast("已经是最新版本");
							}
						}

					}
			}
		});
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		TextView tv_version = $(R.id.tv_version);
		tv_version.setText("扫描精灵V"+App.config.getVersionName());
		
		final ExpandableLayout expandableLayout = $(R.id.expandableLayout);
		CheckBox btn_expand=$(R.id.btn_expand);
		btn_expand.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton btn, boolean checked) {
				// TODO Auto-generated method stub
				expandableLayout.toggle();
			}
		});
		
		$(R.id.btn_hide).setOnClickListener(this);
		$(R.id.btn_update).setOnClickListener(this);
		$(R.id.btn_default).setOnClickListener(this);
		$(R.id.btn_setting).setOnClickListener(this);
		$(R.id.btn_exit).setOnClickListener(this);
		
		cb_ball = $(R.id.cb_ball);
		rg_continu = $(R.id.rg_continu);
		rg_shan = $(R.id.rg_shan);
		rg_ding = $(R.id.rg_ding);
		rg_yi = $(R.id.rg_yi);
		rg_er = $(R.id.rg_er);
		rg_enter = $(R.id.rg_enter);
		rg_vibrate = $(R.id.rg_vibrate);
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		int isShan=(Integer)SPUtils.get(context, App.KEY_SHAN, 0);
		int isDing=(Integer)SPUtils.get(context, App.KEY_DING, 0);
		boolean isYi=(Boolean)SPUtils.get(context, App.KEY_YI, true);
		boolean isEr=(Boolean)SPUtils.get(context, App.KEY_ER, true);
		boolean isEnter=(Boolean)SPUtils.get(context, App.KEY_ENTER, true);
		boolean isVibrate=(Boolean)SPUtils.get(context, App.KEY_VIBRATE, false);
		
		cb_ball.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				// TODO Auto-generated method stub
				cb_ball.setText(checked?"隐藏":"显示");
				SPUtils.put(context, App.KEY_SCAN, checked);
				EventBus.getDefault().post(ScannerFloatService.EVENT_SCAN);
			}
		});
		
		int[] continus={R.id.rb_continu_off,R.id.rb_continu_normal,R.id.rb_continu_detection};
		rg_continu.check(continus[0]);
		rg_continu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup rg, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_continu_off:
					App.mode_continu=0;
					break;
				case R.id.rb_continu_normal:
					App.mode_continu=1;
					break;
				case R.id.rb_continu_detection:
					App.mode_continu=2;
					break;
				}
				EventBus.getDefault().post(ScannerFloatService.EVENT_CONTINU);
			}
		});
		
		int[] shans={R.id.rb_shan_auto,R.id.rb_shan_on,R.id.rb_shan_off};
		rg_shan.check(shans[isShan]);
		rg_shan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup rg, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_shan_auto:
					SPUtils.put(context, App.KEY_SHAN, 0);
					break;
				case R.id.rb_shan_on:
					SPUtils.put(context, App.KEY_SHAN, 1);
					break;
				case R.id.rb_shan_off:
					SPUtils.put(context, App.KEY_SHAN, 2);
					break;
				}
				EventBus.getDefault().post(ScannerFloatService.EVENT_SHAN);
			}
		});
		//
		int[] dings={R.id.rb_ding_auto,R.id.rb_ding_on,R.id.rb_ding_off};
		rg_ding.check(dings[isDing]);
		rg_ding.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup rg, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_ding_auto:
					SPUtils.put(context, App.KEY_DING, 0);
					break;
				case R.id.rb_ding_on:
					SPUtils.put(context, App.KEY_DING, 1);
					break;
				case R.id.rb_ding_off:
					SPUtils.put(context, App.KEY_DING, 2);
					break;
				}
				EventBus.getDefault().post(ScannerFloatService.EVENT_DING);
			}
		});
		//
		rg_yi.check(isYi?R.id.rb_yi_on:R.id.rb_yi_off);
		rg_yi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup rg, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_yi_on:
					SPUtils.put(context, App.KEY_YI, true);
					break;
				case R.id.rb_yi_off:
					SPUtils.put(context, App.KEY_YI, false);
					break;
				}
				EventBus.getDefault().post(ScannerFloatService.EVENT_YI);
			}
		});
		//
		rg_er.check(isEr?R.id.rb_er_on:R.id.rb_er_off);
		rg_er.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup rg, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_er_on:
					SPUtils.put(context, App.KEY_ER, true);
					break;
				case R.id.rb_er_off:
					SPUtils.put(context, App.KEY_ER, false);
					break;
				}
				EventBus.getDefault().post(ScannerFloatService.EVENT_ER);
			}
		});
		
		rg_enter.check(isEnter?R.id.rb_enter_on:R.id.rb_enter_off);
		rg_enter.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup rg, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_enter_on:
					SPUtils.put(context, App.KEY_ENTER, true);
					break;
				case R.id.rb_enter_off:
					SPUtils.put(context, App.KEY_ENTER, false);
					break;
				}
				EventBus.getDefault().post(ScannerFloatService.EVENT_ENTER);
			}
		});
		//
		rg_vibrate.check(isVibrate?R.id.rb_vibrate_on:R.id.rb_vibrate_off);
		rg_vibrate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup rg, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_vibrate_on:
					SPUtils.put(context, App.KEY_VIBRATE, true);
					break;
				case R.id.rb_vibrate_off:
					SPUtils.put(context, App.KEY_VIBRATE, false);
					break;
				}
				EventBus.getDefault().post(ScannerFloatService.EVENT_VIBRATE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=null;
		switch (v.getId()) {
		case R.id.btn_hide:
			moveTaskToBack();
			break;
		case R.id.btn_exit:
			doExit();
			break;
		case R.id.btn_update:
			doCommandCheckVersion(true);
			break;
		case R.id.btn_default:
			doDefault();
			break;
		case R.id.btn_setting:
			intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
			startActivity(intent);
			break;
		}
	}

	private void doExit(){
		EventBus.getDefault().post(ScannerFloatService.EVENT_STOP);
		SPUtils.put(context, App.KEY_SCAN, false);
		EventBus.getDefault().post(ScannerFloatService.EVENT_SCAN);
		finish();
	}
	
	private void doDefault(){
		SPUtils.put(context, App.KEY_SCAN, true);
		SPUtils.put(context, App.KEY_SHAN, 0);
		SPUtils.put(context, App.KEY_DING, 0);
		SPUtils.put(context, App.KEY_YI, true);
		SPUtils.put(context, App.KEY_ER, true);
		SPUtils.put(context, App.KEY_ENTER, true);
		SPUtils.put(context, App.KEY_VIBRATE, false);
		
		cb_ball.setChecked(true);
		rg_continu.check(R.id.rb_continu_off);//
		rg_shan.check(R.id.rb_shan_auto);
		rg_ding.check(R.id.rb_ding_auto);
		rg_yi.check(R.id.rb_yi_on);
		rg_er.check(R.id.rb_er_on);
		rg_enter.check(R.id.rb_enter_on);
		rg_vibrate.check(R.id.rb_vibrate_off);
	}
	
	private long exitTime = 0;
	private void exitApp() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			doExit();
			System.exit(0);
		}
	}
	@Override
	public void onBackPressed() {
		exitApp();
	}
	
	private void moveTaskToBack(){
		NotificationUtils.showCustomNotification(context);
		moveTaskToBack(true);
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if(ScannerFloatService.EVENT_UPDATE_UI_CONTINU.equals(event)){
			rg_continu.check(R.id.rb_continu_off);//
		}
	}
	
}
