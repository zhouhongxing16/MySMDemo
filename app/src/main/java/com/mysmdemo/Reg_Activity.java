package com.mysmdemo;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Reg_Activity extends Activity{

	EditText etphone,password,repassword;
	ImageView back;
	Button btnext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);
		
		back = (ImageView) findViewById(R.id.zhuce_back);
		etphone = (EditText) findViewById(R.id.phone);
		password = (EditText) findViewById(R.id.password);
		repassword = (EditText) findViewById(R.id.repassword);
		btnext = (Button) findViewById(R.id.next);
		
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Reg_Activity.this,Login_Activity.class);
				 startActivity(intent);
			}
		});
		
		
		btnext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final String phone;
				final String realpassword;
				
				
				if(etphone.getText().toString().equals("")){
					Toast.makeText(Reg_Activity.this, "电话号码不能为空!",
							Toast.LENGTH_SHORT).show();
					
				}
				
				else if(password.getText().toString().equals("")||repassword.getText().toString().equals("")){
					Toast.makeText(Reg_Activity.this, "密码不能为空!",
							Toast.LENGTH_SHORT).show();
					
				}
				
				else if(!password.getText().toString().equals(repassword.getText().toString())){
					Toast.makeText(Reg_Activity.this, "两次密码不一致!",
							Toast.LENGTH_SHORT).show();
					
				}
				else{
					
					Intent intent = new Intent(Reg_Activity.this,Reg_Activity2.class);
					
					Bundle bundle =new Bundle();
					 bundle.putString("phone",etphone.getText().toString());
					 bundle.putString("password",repassword.getText().toString());
					 intent.putExtras(bundle);
					 
					 startActivity(intent);
				}
				
			}
		});
		
		
	}

	
	    
}
