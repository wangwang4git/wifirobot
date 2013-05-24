package com.linkspritedirect.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.linkspritedirect.R;

public class Introduction extends Activity {
	public Button bt_start;
	public Button bt_setting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_introduction);
		bt_start=(Button)findViewById(R.id.bt_start);
		bt_setting=(Button)findViewById(R.id.bt_set);
		//btn_listener ClickEvent=new btn_listener();
		bt_start.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Introduction.this,Monitor.class);
				startActivity(intent);
				Introduction.this.finish();
				
			}
			
		});
		bt_setting.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Introduction.this,Set.class);
				startActivity(intent);
				Introduction.this.finish();
			}
			
		});
		



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.introduction, menu);
		return true;
	}

}
