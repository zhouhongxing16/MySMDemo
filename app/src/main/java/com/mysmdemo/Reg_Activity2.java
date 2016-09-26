package com.mysmdemo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

public class Reg_Activity2 extends Activity implements OnClickListener, Callback{
	
	private static final int TIME_OUT = 10 * 1000;
	private static String APPKEY = "12690babb2698";

	private boolean ready;
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "811c9834953638740c8345a0a47a5a4e";

	private TextView et_phonenumber;

	private EditText et_number;

	private Button bt_sendsms;

	private Button bt_postmessage;

	//private String phonenumber;
	
	private TimeCount time;
	
	//private TextView phonenum;
	String phone;
	String password;
	ImageView back;
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg)
		{
			
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Toast.makeText(Reg_Activity2.this, "注册成功!",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Reg_Activity2.this,
						Login_Activity.class);
				startActivity(intent);
				break;
			case 2:
				Toast.makeText(Reg_Activity2.this, "注册失败，请重试！",
						Toast.LENGTH_SHORT).show();
			default:
				break;
			}
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg_commit);
		Bundle bundle = this.getIntent().getExtras();
		
		phone = bundle.getString("phone");
		password = bundle.getString("password");
		//phonenum = (TextView) findViewById(R.id.tx_phonenumber);
		SMSSDK.initSDK(this, APPKEY, APPSECRET,false);
		
		back = (ImageView) findViewById(R.id.zhuce3_back);
		bt_sendsms = (Button) findViewById(R.id.bt_sendsms);
		bt_postmessage = (Button) findViewById(R.id.bt_postmessage);
		et_phonenumber = (TextView) findViewById(R.id.et_phonenumber);
		et_number = (EditText) findViewById(R.id.et_number);
		
		et_phonenumber.setText(phone);
		//phonenum.setText(phone);
		bt_sendsms.setOnClickListener(this);
		bt_postmessage.setOnClickListener(this);
		
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Reg_Activity2.this,Reg_Activity.class);
				 startActivity(intent);
			}
		});
		initSDK();

		
	}
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			bt_sendsms.setText("重新发送验证码");
			bt_sendsms.setClickable(true);
			bt_sendsms.setTextSize(12);

		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			bt_sendsms.setClickable(false);
			bt_sendsms.setText(millisUntilFinished / 1000 + "秒后获取");
			bt_sendsms.setTextSize(18);
		}
	}

	private void initSDK() {
		try {
			
			final Handler handler = new Handler(this);
			EventHandler eventHandler = new EventHandler() {
				public void afterEvent(int event, int result, Object data) {
					Message msg = new Message();
					msg.arg1 = event;
					msg.arg2 = result;
					msg.obj = data;
					handler.sendMessage(msg);
				}
			};
	
			SMSSDK.registerEventHandler(eventHandler); // 注册短信回调
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_sendsms:
			/*phonenumber = et_phonenumber.getText().toString().trim();
			if (!TextUtils.isEmpty(phonenumber)) {*/
				SMSSDK.getVerificationCode("86", phone);//获取短信
				//SMSSDK.getVoiceVerifyCode("86", phone);
				
				time = new TimeCount(10000, 1000);
				time.start();
		
			/*}else {
				Toast.makeText(Reg_Activity2.this, "电话号码不能为空", 0).show();
			
			}*/
			break;
	case R.id.bt_postmessage:
			
		String number = et_number.getText().toString().trim();
		if (!TextUtils.isEmpty(number)) {
			SMSSDK.submitVerificationCode("86", phone,number);//验证短信
			
			
			
		}else {
			Toast.makeText(Reg_Activity2.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
			break;
		default:
			break;
		}

	}

	protected void onDestroy() {
	
			// 销毁回调监听接口
			SMSSDK.unregisterAllEventHandler();
			super.onDestroy();

	}

	@Override
	public boolean handleMessage(Message msg) {

		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		
		
		 if (result == SMSSDK.RESULT_COMPLETE) {
				System.out.println("--------result"+event);
             //回调完成
             if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
             //提交验证码成功
            	 //Toast.makeText(Reg_Activity2.this, "注册成功", Toast.LENGTH_SHORT).show();
            	 new Thread() {
						public void run() {
							 Regist(phone, password);
							
						}
					}.start();
            	
             }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
             //获取验证码成功
            	 Toast.makeText(Reg_Activity2.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
             }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
             //返回支持发送验证码的国家列表
             } 
           }else{                                                                 

//				((Throwable) data).printStackTrace();
//				Toast.makeText(MainActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//					Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
				int status = 0;	
					try {
						((Throwable) data).printStackTrace();
						Throwable throwable = (Throwable) data;

						JSONObject object = new JSONObject(throwable.getMessage());
						String des = object.optString("detail");
						status = object.optInt("status");
						if (!TextUtils.isEmpty(des)) {
							Toast.makeText(Reg_Activity2.this, des, Toast.LENGTH_SHORT).show();
							return false;
						}
					} catch (Exception e) {
						SMSLog.getInstance().w(e);
					}
			
       }
		return false;
	}
	
	
	private void Regist(final String phone, final String password) {
		String msg =null;
		
			String flag = null;
				// 要访问的HttpServlet
				String urlStr = "http://182.254.223.113:8080/FinalServer/registServlet?";
				// 要传递的数据
				String query = "phone=" + phone + "&password="
						+ password;
				urlStr += query;

				try {
					URL url = new URL(urlStr);
					// 获得连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setReadTimeout(TIME_OUT);
					conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.setRequestMethod("POST");
					conn.setUseCaches(false);
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("Charset", "UTF-8");
					conn.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded");
					conn.connect();
					InputStream input = conn.getInputStream();

					InputStreamReader reader = new InputStreamReader(input,
							"UTF-8");
					BufferedReader bfreader = new BufferedReader(reader);
					msg = bfreader.readLine();
					flag = msg;
					bfreader.close();
					reader.close();
					conn.disconnect();
				} catch (Exception e) {

				}
				
				praseFlag(msg);
				
				}
	
	private void praseFlag(String flag){
		if (flag.equals("success")) {
			Log.e("tag", flag);
			handler.sendEmptyMessage(1);
			
		}
		else 
		{
			System.out.println(13333);
			handler.sendEmptyMessage(2);
			
		}
	}
	
}
