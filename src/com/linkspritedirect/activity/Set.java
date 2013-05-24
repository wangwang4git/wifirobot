package com.linkspritedirect.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.linkspritedirect.R;

public class Set extends Activity {
	public EditText et_controlAddr;
	public EditText et_controlPort;
	public EditText et_videoAddr;
	public EditText et_videoPort;
	
	public Button bt_save; 
	public Button bt_cancel;
	
	public String controlAddr_temp;
	public String videoAddr_temp;
	public String controlPort_temp;
	public String videoPort_temp;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.set);
		
		et_controlAddr=(EditText)findViewById(R.id.et_controlAddr);
		et_controlPort=(EditText)findViewById(R.id.et_controlPort);
		et_videoAddr=(EditText)findViewById(R.id.et_videoAddr);
		et_videoPort=(EditText)findViewById(R.id.et_videoPort);
		
		bt_save=(Button)findViewById(R.id.bt_save);
		bt_cancel=(Button)findViewById(R.id.bt_cancel);
		
		et_controlAddr.setText(System_data.controlAddr_store);
		et_controlPort.setText(System_data.controlPort_store);
		et_videoAddr.setText(System_data.videoAddr_store);
		et_videoPort.setText(System_data.videoPort_store);
		
		bt_save.setOnClickListener(new bt_clickEvent());
		bt_cancel.setOnClickListener(new bt_clickEvent());
	 
		
	}
	
	

	
	class bt_clickEvent implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.bt_save:
			{
				controlAddr_temp=et_controlAddr.getText().toString();
				controlPort_temp=et_controlPort.getText().toString();
				videoAddr_temp=et_videoAddr.getText().toString();
				videoPort_temp=et_videoPort.getText().toString();
				
				System_data.controlAddr_store=controlAddr_temp;
				System_data.controlPort_store=controlPort_temp;
				System_data.videoAddr_store=videoAddr_temp;
				System_data.videoPort_store=videoPort_temp;
				
				
				et_controlAddr.setText(System_data.controlAddr_store);
				et_controlPort.setText(System_data.controlPort_store);
				et_videoAddr.setText(System_data.videoAddr_store);
				et_videoPort.setText(System_data.videoPort_store);
				
				
				Intent intent=new Intent();
				intent.setClass(Set.this, Introduction.class);
				startActivity(intent);
				Set.this.finish();
				
			}
			break;
			
			case R.id.bt_cancel:
			{
				
				et_controlAddr.setText(System_data.controlAddr_store);
				et_controlPort.setText(System_data.controlPort_store);
				et_videoAddr.setText(System_data.videoAddr_store);
				et_videoPort.setText(System_data.videoPort_store);
				
				/*
				Intent intent=new Intent();
				intent.setClass(Set.this, Introduction.class);
				startActivity(intent);
				Set.this.finish();
				*/
				
			}
			break;
			
			default:
				break;
			}
		}
		
	}
	
	static class System_data
	{
		public static String controlAddr_store="192.168.3.141";
		public static String controlPort_store="5000";
		public static String videoAddr_store="10.10.10.101";
		public static String videoPort_store="8090";
	}
	
	
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			Intent back_intent=new Intent();
			back_intent.setClass(Set.this, Introduction.class);
			startActivity(back_intent);
			Set.this.finish();
		}
		return false;
	}
	
}
