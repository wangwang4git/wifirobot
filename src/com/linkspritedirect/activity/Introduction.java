package com.linkspritedirect.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.linkspritedirect.R;
import com.linkspritedirect.activity.Set.System_data;

public class Introduction extends Activity {
	public Button bt_start;
	public static final int MENU_SET_ID=Menu.FIRST;
	public static final int MENU_ABOUT_ID=Menu.FIRST+1;
    public ImageButton ib_start;

	//上次输入数据的保存
	private SharedPreferences sharedPreferenecs;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_introduction);
		
		//用户数据保存功能的初始化
		sharedPreferenecs = this.getSharedPreferences("user", MODE_WORLD_READABLE);
		//读取用户上次输入的信息
		userMsgReader();
		
		ib_start =  (ImageButton)findViewById(R.id.ib_start);
		
		ib_start.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Introduction.this,Monitor.class);
				startActivity(intent);
				Introduction.this.finish();
			}
			
		});
	}

	private void userMsgReader()
	{
		System_data.videoAddr_store = sharedPreferenecs.getString("et_video_addr", System_data.videoAddr_store);
		System_data.videoPort_store = sharedPreferenecs.getString("et_video_port", System_data.videoPort_store);
		System_data.controlAddr_store = sharedPreferenecs.getString("et_ctrl_addr", System_data.controlAddr_store);
		System_data.controlPort_store = sharedPreferenecs.getString("et_ctrl_port", System_data.controlPort_store);
		
		System_data.left_store = sharedPreferenecs.getString("left_store", System_data.left_store);
		System_data.right_store = sharedPreferenecs.getString("right_store",System_data.right_store);
		System_data.up_store = sharedPreferenecs.getString("up_store",System_data.up_store);
		System_data.down_store = sharedPreferenecs.getString("down_store",System_data.down_store);
		System_data.stop_store = sharedPreferenecs.getString("stop_store",System_data.stop_store);
		
		System_data.camera_h_left = sharedPreferenecs.getString("et_h_left",System_data.camera_h_left);
		System_data.camera_h_right = sharedPreferenecs.getString("et_v_right",System_data.camera_h_right);
		System_data.camera_v_up = sharedPreferenecs.getString("et_v_up", System_data.camera_v_up);
		System_data.camera_v_down = sharedPreferenecs.getString("et_v_down", System_data.camera_v_down);
	
		System_data.gravity = sharedPreferenecs.getInt("et_gravity", System_data.gravity);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.introduction, menu);
		menu.add(0, MENU_SET_ID, 0, "Setting");
		menu.add(0,MENU_ABOUT_ID,0,"About");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case MENU_SET_ID:
		{
			Intent intent=new Intent();
			intent.setClass(Introduction.this,Set.class);
			startActivity(intent);
			Introduction.this.finish();
		}
		break;
		
		case MENU_ABOUT_ID:
		{
			Intent intent = new Intent();
			intent.setClass(Introduction.this, about.class);
			startActivity(intent);
			Introduction.this.finish();
		}
		break;
		
		default:
			break;
		
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			dialog();
		}
		return false;
	}
	
	public void dialog()
	{
		AlertDialog.Builder builder=new Builder(Introduction.this);
		builder.setMessage("Are you sure to quit?");
		builder.setTitle("Tips");
		builder.setPositiveButton("sure", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				android.os.Process.killProcess(android.os.Process.myPid());
				
			}
		});
		
		builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		builder.create().show();
	}
}
