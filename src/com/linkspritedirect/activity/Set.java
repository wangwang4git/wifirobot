package com.linkspritedirect.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	//视频传输控制地址和端口
	public EditText et_controlAddr;
	public EditText et_controlPort;
	public EditText et_videoAddr;
	public EditText et_videoPort;
	
	//运动方位控制设置编辑器
	private EditText et_up = null;
	private EditText et_down = null;
	private EditText et_left = null;
	private EditText et_right = null;
	private EditText et_stop = null;
	
	//舵机转动方位控制编辑器
	private EditText et_h_left = null;
	private EditText et_h_right = null;
	private EditText et_v_up = null;
	private EditText et_v_down = null;
	
	// 重力感应时间间隔
	private EditText et_gravity = null;
	
	//保存按钮
	public Button bt_save; 
	
	//上次输入数据的保存
	private SharedPreferences sharedPreferenecs;
	private Editor editor;
	
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
		
		et_up = (EditText) findViewById(R.id.et_up);
		et_down = (EditText) findViewById(R.id.et_down);
		et_left = (EditText) findViewById(R.id.et_left);
		et_right = (EditText) findViewById(R.id.et_right);
		et_stop = (EditText) findViewById(R.id.et_stop);
		
		et_h_left = (EditText) findViewById(R.id.et_h_left);
		et_h_right = (EditText) findViewById(R.id.et_h_right);
		et_v_up = (EditText) findViewById(R.id.et_v_up);
		et_v_down = (EditText) findViewById(R.id.et_v_down);
		
		et_gravity = (EditText) findViewById(R.id.et_gravity);
		
		bt_save=(Button)findViewById(R.id.bt_save);
		
		//用户数据保存功能的初始化
		sharedPreferenecs = this.getSharedPreferences("user", MODE_WORLD_READABLE);
		editor = sharedPreferenecs.edit();
		
		//初始化话设置界面的各项数据
		et_controlAddr.setText(System_data.controlAddr_store);
		et_controlPort.setText(System_data.controlPort_store);
		et_videoAddr.setText(System_data.videoAddr_store);
		et_videoPort.setText(System_data.videoPort_store);
		
		et_up.setText(System_data.up_store);
		et_down.setText(System_data.down_store);
		et_left.setText(System_data.left_store);
		et_right.setText(System_data.right_store);
		et_stop.setText(System_data.stop_store);
		
		et_h_left.setText(System_data.camera_h_left);
		et_h_right.setText(System_data.camera_h_right);
		et_v_up.setText(System_data.camera_v_up);
		et_v_down.setText(System_data.camera_v_down);
		
		et_gravity.setText(System_data.gravity + "");
		
		//保存按钮的功能实现
		bt_save.setOnClickListener(new bt_clickEvent());


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
				break;
			}
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

		public static String camera_h_left = "h";
		public static String camera_h_right = "r";

		public static String camera_v_up = "v";
		public static String camera_v_down = "d";
		
		public static int gravity = 100;
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
		builder.setMessage("Are you sure to save？");
		builder.setTitle("Tips");
		builder.setPositiveButton("sure",new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				System_data.controlAddr_store=et_controlAddr.getText().toString();
				System_data.controlPort_store=et_controlPort.getText().toString();
				System_data.videoAddr_store=et_videoAddr.getText().toString();
				System_data.videoPort_store=et_videoPort.getText().toString();
				
				
				et_controlAddr.setText(System_data.controlAddr_store);
				et_controlPort.setText(System_data.controlPort_store);
				et_videoAddr.setText(System_data.videoAddr_store);
				et_videoPort.setText(System_data.videoPort_store);
				
				System_data.up_store = et_up.getText().toString();
				System_data.down_store = et_down.getText().toString();
				System_data.left_store = et_left.getText().toString();
				System_data.right_store = et_right.getText().toString();
				System_data.stop_store = et_stop.getText().toString();

				System_data.camera_h_left = et_h_left.getText().toString();
				System_data.camera_h_right = et_h_right.getText().toString();
				System_data.camera_v_up = et_v_up.getText().toString();
				System_data.camera_v_down = et_v_down.getText().toString();


				et_up.setText(System_data.up_store);
				et_down.setText(System_data.down_store);
				et_left.setText(System_data.left_store);
				et_right.setText(System_data.right_store);
				et_stop.setText(System_data.stop_store);

				et_h_left.setText(System_data.camera_h_left);
				et_h_right.setText(System_data.camera_h_right);
				et_v_up.setText(System_data.camera_v_up);
				et_v_down.setText(System_data.camera_v_down);
				
				try {
					System_data.gravity = Integer.parseInt(et_gravity
							.getText().toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				et_gravity.setText(System_data.gravity + "");
				
				
				//用户数据保存
				userMsgWriter();

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
				
				et_h_left.setText(System_data.camera_h_left);
				et_h_right.setText(System_data.camera_h_right);
				et_v_up.setText(System_data.camera_v_up);
				et_v_down.setText(System_data.camera_v_down);
				
				et_gravity.setText(System_data.gravity + "");
				
				Intent intent=new Intent();
				intent.setClass(Set.this, Introduction.class);
				startActivity(intent);
				Set.this.finish();
			}
			
		});
		
		builder.create().show();
		
	}
	
	private void userMsgWriter()
	{
		//将数据存入xml文件
		editor.putString("up_store",System_data.up_store);
		editor.putString("down_store",System_data.down_store);
		editor.putString("right_store",System_data.right_store);
		editor.putString("left_store",System_data.left_store);
		editor.putString("stop_store",System_data.stop_store);
		
		editor.putString("et_h_left",System_data.camera_h_left);
		editor.putString("et_h_right",System_data.camera_h_right);
		editor.putString("et_v_up",System_data.camera_v_up);
		editor.putString("et_v_down",System_data.camera_v_down);
		
		editor.putString("et_ctrl_addr", System_data.controlAddr_store);
		editor.putString("et_ctrl_port", System_data.controlPort_store);
		editor.putString("et_video_addr", System_data.videoAddr_store);
		editor.putString("et_video_port", System_data.videoPort_store);
		
		editor.putInt("et_gravity", System_data.gravity);
		
		editor.commit();
	}
	
}
