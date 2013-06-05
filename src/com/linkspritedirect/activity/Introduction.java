package com.linkspritedirect.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
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

public class Introduction extends Activity {
	public Button bt_start;
	public static final int MENU_SET_ID=Menu.FIRST;
	public static final int MENU_ABOUT_ID=Menu.FIRST+1;
    public ImageButton ib_start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_introduction);
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
