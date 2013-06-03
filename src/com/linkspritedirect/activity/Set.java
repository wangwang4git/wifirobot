package com.linkspritedirect.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.linkspritedirect.R;

public class Set extends Activity {
	public EditText et_controlAddr;
	public EditText et_controlPort;
	public EditText et_videoAddr;
	public EditText et_videoPort;
	
	public Button bt_save; 
	//public Button bt_cancel;
	
	public String controlAddr_temp;
	public String videoAddr_temp;
	public String controlPort_temp;
	public String videoPort_temp;
	
	private EditText et_up = null;
	private EditText et_down = null;
	private EditText et_left = null;
	private EditText et_right = null;
	private EditText et_stop = null;
	
	private EditText et_camera_h = null;
	private EditText et_camera_v = null;
	private CheckBox cb_h_number = null;
	private CheckBox cb_v_number = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_set);
		
		et_controlAddr=(EditText)findViewById(R.id.et_controlAddr);
		et_controlPort=(EditText)findViewById(R.id.et_controlPort);
		et_videoAddr=(EditText)findViewById(R.id.et_videoAddr);
		et_videoPort=(EditText)findViewById(R.id.et_videoPort);
		
		bt_save=(Button)findViewById(R.id.bt_save);
		//bt_cancel=(Button)findViewById(R.id.bt_cancel);
		
		et_controlAddr.setText(System_data.controlAddr_store);
		et_controlPort.setText(System_data.controlPort_store);
		et_videoAddr.setText(System_data.videoAddr_store);
		et_videoPort.setText(System_data.videoPort_store);
		
		bt_save.setOnClickListener(new bt_clickEvent());
		//bt_cancel.setOnClickListener(new bt_clickEvent());
	 
		
		et_up = (EditText) findViewById(R.id.et_up);
		et_down = (EditText) findViewById(R.id.et_down);
		et_left = (EditText) findViewById(R.id.et_left);
		et_right = (EditText) findViewById(R.id.et_right);
		et_stop = (EditText) findViewById(R.id.et_stop);
		
		et_camera_h = (EditText) findViewById(R.id.et_camera_h);
		et_camera_v = (EditText) findViewById(R.id.et_camera_v);
		
		et_up.setText(System_data.up_store);
		et_down.setText(System_data.down_store);
		et_left.setText(System_data.left_store);
		et_right.setText(System_data.right_store);
		et_stop.setText(System_data.stop_store);
		
		et_camera_h.setText(System_data.camera_h_store);
		et_camera_v.setText(System_data.camera_v_store);
		cb_h_number = (CheckBox) findViewById(R.id.cb_h_number);
		cb_v_number = (CheckBox) findViewById(R.id.cb_v_number);
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

				dialog_save();
				
/*				Intent intent=new Intent();
				intent.setClass(Set.this, Introduction.class);
				startActivity(intent);
				Set.this.finish();*/
				
			}
			break;
			
			//case R.id.bt_cancel:
			//{
				

				//dialog_cancel();
				/*
				Intent intent=new Intent();
				intent.setClass(Set.this, Introduction.class);
				startActivity(intent);
				Set.this.finish();
				*/
				
			//}
			//break;
			
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
		
		public static String up_store = "w";
		public static String down_store = "s";
		public static String left_store = "a";
		public static String right_store = "d";
		public static String stop_store = "0";

		public static String camera_h_store = "h";
		public static boolean camera_h_num_store = false;
		public static String camera_v_store = "v";
		public static boolean camera_v_num_store = false;
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
	
	public void dialog_save()
	{
		AlertDialog.Builder builder=new Builder(Set.this);
		builder.setMessage("Are you sure to save£¿");
		builder.setTitle("Tips");
		builder.setPositiveButton("sure",new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
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
				
				System_data.up_store = et_up.getText().toString();
				System_data.down_store = et_down.getText().toString();
				System_data.left_store = et_left.getText().toString();
				System_data.right_store = et_right.getText().toString();
				System_data.stop_store = et_stop.getText().toString();

				System_data.camera_h_store = et_camera_h.getText().toString();
				System_data.camera_v_store = et_camera_v.getText().toString();
				System_data.camera_h_num_store =  cb_h_number.isChecked();
				System_data.camera_v_num_store =  cb_v_number.isChecked();

				et_up.setText(System_data.up_store);
				et_down.setText(System_data.down_store);
				et_left.setText(System_data.left_store);
				et_right.setText(System_data.right_store);
				et_stop.setText(System_data.stop_store);

				et_camera_h.setText(System_data.camera_h_store);
				et_camera_v.setText(System_data.camera_v_store);
				cb_h_number.setChecked(System_data.camera_h_num_store);
				cb_v_number.setChecked(System_data.camera_v_num_store);
				
				Intent intent=new Intent();
				intent.setClass(Set.this, Introduction.class);
				startActivity(intent);
				Set.this.finish();
			}
			
		});
		
		builder.setNegativeButton("cancel", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				et_controlAddr.setText(System_data.controlAddr_store);
				et_controlPort.setText(System_data.controlPort_store);
				et_videoAddr.setText(System_data.videoAddr_store);
				et_videoPort.setText(System_data.videoPort_store);
				
				et_up.setText(System_data.up_store);
				et_down.setText(System_data.down_store);
				et_left.setText(System_data.left_store);
				et_right.setText(System_data.right_store);
				et_stop.setText(System_data.stop_store);
				
				et_camera_h.setText(System_data.camera_h_store);
				et_camera_v.setText(System_data.camera_v_store);
				cb_h_number.setChecked(System_data.camera_h_num_store);
				cb_v_number.setChecked(System_data.camera_v_num_store);
				
				Intent intent=new Intent();
				intent.setClass(Set.this, Introduction.class);
				startActivity(intent);
				Set.this.finish();
			}

			
		});
		
		builder.create().show();
		
	}
	
	public void dialog_cancel()
	{
		AlertDialog.Builder builder=new Builder(Set.this);
		builder.setMessage("Are you sure to cancel£¿");
		builder.setTitle("Tips");
		builder.setPositiveButton("sure",new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				et_controlAddr.setText(System_data.controlAddr_store);
				et_controlPort.setText(System_data.controlPort_store);
				et_videoAddr.setText(System_data.videoAddr_store);
				et_videoPort.setText(System_data.videoPort_store);
				
				et_up.setText(System_data.up_store);
				et_down.setText(System_data.down_store);
				et_left.setText(System_data.left_store);
				et_right.setText(System_data.right_store);
				et_stop.setText(System_data.stop_store);
				
				et_camera_h.setText(System_data.camera_h_store);
				et_camera_v.setText(System_data.camera_v_store);
				cb_h_number.setChecked(System_data.camera_h_num_store);
				cb_v_number.setChecked(System_data.camera_v_num_store);
			}
			
		});
		
		builder.setNegativeButton("cancel", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			
				
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
				
				System_data.up_store = et_up.getText().toString();
				System_data.down_store = et_down.getText().toString();
				System_data.left_store = et_left.getText().toString();
				System_data.right_store = et_right.getText().toString();
				System_data.stop_store = et_stop.getText().toString();

				System_data.camera_h_store = et_camera_h.getText().toString();
				System_data.camera_v_store = et_camera_v.getText().toString();
				System_data.camera_h_num_store =  cb_h_number.isChecked();
				System_data.camera_v_num_store =  cb_v_number.isChecked();

				et_up.setText(System_data.up_store);
				et_down.setText(System_data.down_store);
				et_left.setText(System_data.left_store);
				et_right.setText(System_data.right_store);
				et_stop.setText(System_data.stop_store);

				et_camera_h.setText(System_data.camera_h_store);
				et_camera_v.setText(System_data.camera_v_store);
				cb_h_number.setChecked(System_data.camera_h_num_store);
				cb_v_number.setChecked(System_data.camera_v_num_store);
			}

			
		});
		
		builder.create().show();
		
	}
	
}
