package com.mysmdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Login_Activity extends Activity {
	private static final int TIME_OUT = 10 * 1000;
	Button btlogin, btregist;
	EditText etusername;
	EditText etuserpassword;
	TextView zhuce;
	ImageView back;
	boolean flag;
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg)
		{
			
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				/*Intent intent = new Intent(Login_Activity.this,
						MainActivity.class);
				startActivity(intent);*/
				
				break;
			case 2:
				Toast.makeText(Login_Activity.this, "用户名或密码错误!",
						Toast.LENGTH_SHORT).show();
			default:
				break;
			}
			
		}
	};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViewById(R.id.login);
		zhuce = (TextView) findViewById(R.id.zhuce);
		btlogin = (Button) findViewById(R.id.login);
		back = (ImageView) findViewById(R.id.login_back);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	
		zhuce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Login_Activity.this,
						Reg_Activity.class);
				startActivity(intent);
			}
		});
		btlogin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				etusername = (EditText) findViewById(R.id.username);
				etuserpassword = (EditText) findViewById(R.id.password);

				final String username = etusername.getText().toString();
				final String password = etuserpassword.getText().toString();
				if (username.equals("")) {
					Toast.makeText(Login_Activity.this, "用户名不能为空!",
							Toast.LENGTH_SHORT).show();
				}
				if (username.equals("")) {
					Toast.makeText(Login_Activity.this, "密码不能为空!",
							Toast.LENGTH_SHORT).show();
				} else {
					new Thread() {
						public void run() {
							login(username, password);
							
						}
					}.start();
					
				}
			}
		});

	}

	private void login(final String username, final String password) {
		String msg = null;

		String flag = null;
		// 要访问的HttpServlet
		String urlStr = "http://182.254.223.113:8080/FinalServer/loginServlet?";
		// 要传递的数据
		String query = "username=" + username + "&password=" + password;
		urlStr += query;

		try {
			URL url = new URL(urlStr);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

			InputStreamReader reader = new InputStreamReader(input, "UTF-8");
			BufferedReader bfreader = new BufferedReader(reader);
			msg = bfreader.readLine();
			
			
			
			bfreader.close();
			reader.close();
			conn.disconnect();
			
			
		} catch (Exception e) {

		}

		praseFlag(msg);
		
		
	}
	
	
		
	
	private void praseFlag(String flag){
		if (flag.equals("success")) {
			Log.i("tag", flag);
			handler.sendEmptyMessage(1);
			
		} else {
			System.out.println(13333);
			handler.sendEmptyMessage(2);
			
		}
		
		
	}

}
