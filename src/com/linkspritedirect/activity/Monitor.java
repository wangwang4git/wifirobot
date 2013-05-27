package com.linkspritedirect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.linkspritedirect.R;
import com.linkspritedirect.activity.Set.System_data;
import com.linkspritedirect.mjpgview.MjpegInputStream;
import com.linkspritedirect.mjpgview.MjpegView;
import com.linkspritedirect.socket.WifiRobotControlClient;

public class Monitor extends Activity {
	private String ADVANCE = null;
	private String RETREAT = null;
	private String LEFT = null;
	private String RIGHT = null;
	private String STOP = null;
	
	private String HORIZON = null;
	private String VERTICAL = null;

	public Button bt_advance;
	public Button bt_retreat;
	public Button bt_right;
	public Button bt_left;

	public SeekBar sb_vertical;
	public SeekBar sb_horizon;

	public MjpegView sv_camera = null;
	// 缩放模式
	int videoDisplayMode = -1;

	private WifiRobotControlClient mClient = null;
	private Thread mControlThread = null;
	private Handler mHandler = null;
	
	// 系统Sensor管理器
	private SensorManager sensorManager = null;
	// 系统Sensor监听器
	private AccelerometerOnTouchListener accelerometerListener = null;
	// 重力感应开启
	private CheckBox mGravity = null;
	private boolean isOpenSensor = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_monitor);

		ADVANCE = System_data.up_store;
		RETREAT = System_data.down_store;
		LEFT = System_data.left_store;
		RIGHT = System_data.right_store;
		STOP = System_data.stop_store;
		
		HORIZON = System_data.camera_h_store;
		VERTICAL = System_data.camera_v_store;
		
		bt_advance = (Button) findViewById(R.id.bt_advance);
		bt_retreat = (Button) findViewById(R.id.bt_retreat);
		bt_right = (Button) findViewById(R.id.bt_right);
		bt_left = (Button) findViewById(R.id.bt_left);

		sb_vertical = (SeekBar) findViewById(R.id.sb_vertical);
		sb_vertical.setProgress(50);
		sb_horizon = (SeekBar) findViewById(R.id.sb_horizon);
		sb_horizon.setProgress(50);

		bt_advance.setOnTouchListener(new ClickEvent());
		bt_retreat.setOnTouchListener(new ClickEvent());
		bt_right.setOnTouchListener(new ClickEvent());
		bt_left.setOnTouchListener(new ClickEvent());

		sb_vertical.setOnSeekBarChangeListener(this.seekBarListener);
		sb_horizon.setOnSeekBarChangeListener(this.seekBarListener);

		sv_camera = (MjpegView) findViewById(R.id.sv);
		// 设置自定义双击事件监听器
		sv_camera.setOnTouchListener(new cameraOnTouchListener());
		String cameraURL = "http://" + System_data.videoAddr_store.trim() + ":"
				+ System_data.videoPort_store.trim() + "/?action=stream";
		sv_camera.setSource(MjpegInputStream.read(cameraURL));
		// 缩放模式
		videoDisplayMode = MjpegView.SIZE_FULLSCREEN; // MjpegView.SIZE_BEST_FIT
		sv_camera.setDisplayMode(videoDisplayMode);
		sv_camera.showFps(true);

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0: {
					Toast.makeText(getApplicationContext(), "Socket连接成功",
							Toast.LENGTH_SHORT).show();
					break;
				}
				case -1: {
					Toast.makeText(getApplicationContext(), "Socket连接失败",
							Toast.LENGTH_SHORT).show();
					break;
				}
				default: {
					break;
				}
				}

				super.handleMessage(msg);
			}
		};

		mClient = new WifiRobotControlClient(mHandler,
				System_data.controlAddr_store.trim(),
				Integer.parseInt(System_data.controlPort_store.trim()));
		mControlThread = new Thread(mClient);
		mControlThread.start();
		
		// 获取系统的传感器管理服务
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometerListener = new AccelerometerOnTouchListener();
		mGravity = (CheckBox) findViewById(R.id.cb_gravity);
		mGravity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					isOpenSensor = true;

					bt_advance.setVisibility(View.INVISIBLE);
					bt_retreat.setVisibility(View.INVISIBLE);
					bt_right.setVisibility(View.INVISIBLE);
					bt_left.setVisibility(View.INVISIBLE);

					((TextView) findViewById(R.id.tv_vertical))
							.setVisibility(View.INVISIBLE);
					((TextView) findViewById(R.id.tv_horizon))
							.setVisibility(View.INVISIBLE);
					sb_vertical.setVisibility(View.INVISIBLE);
					sb_horizon.setVisibility(View.INVISIBLE);
				} else {
					isOpenSensor = false;

					bt_advance.setVisibility(View.VISIBLE);
					bt_retreat.setVisibility(View.VISIBLE);
					bt_right.setVisibility(View.VISIBLE);
					bt_left.setVisibility(View.VISIBLE);

					((TextView) findViewById(R.id.tv_vertical))
							.setVisibility(View.VISIBLE);
					((TextView) findViewById(R.id.tv_horizon))
							.setVisibility(View.VISIBLE);
					sb_vertical.setVisibility(View.VISIBLE);
					sb_horizon.setVisibility(View.VISIBLE);
				}
			}
		});
		isOpenSensor = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 为系统的加速度传感器注册监听器
		sensorManager.registerListener(accelerometerListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		// 取消注册
		sensorManager.unregisterListener(accelerometerListener);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		mClient.destroy();
		
		sv_camera.stopPlayback();
	}

	class ClickEvent implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
			case R.id.bt_advance: {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN: {
					mClient.send(ADVANCE);
					break;
				}
				case MotionEvent.ACTION_UP: {
					mClient.send(STOP);
					break;
				}
				default: {
					break;
				}
				}
				break;
			}
			case R.id.bt_retreat: {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN: {
					mClient.send(RETREAT);
					break;
				}
				case MotionEvent.ACTION_UP: {
					mClient.send(STOP);
					break;
				}
				default: {
					break;
				}
				}
				break;
			}
			case R.id.bt_right: {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN: {
					mClient.send(RIGHT);
					break;
				}
				case MotionEvent.ACTION_UP: {
					mClient.send(STOP);
					break;
				}
				default: {
					break;
				}
				}
				break;
			}
			case R.id.bt_left: {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN: {
					mClient.send(LEFT);
					break;
				}
				case MotionEvent.ACTION_UP: {
					mClient.send(STOP);
					break;
				}
				default: {
					break;
				}
				}
				break;
			}
			}
			return false;
		}
	}

	private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// 进度改变时触发
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// 开始拖动时触发，与onProgressChanged区别在于onStartTrackingTouch在停止拖动前只触发一次
			// 而onProgressChanged只要在拖动，就会重复触发

			switch (seekBar.getId()) {
			case R.id.sb_vertical: {
				mClient.send(VERTICAL);
				break;
			}
			case R.id.sb_horizon: {
				mClient.send(HORIZON);
				break;
			}
			default: {
				break;
			}
			}
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// 结束拖动时触发

			switch (seekBar.getId()) {
			case R.id.sb_vertical: {
				mClient.send(VERTICAL);
				break;
			}
			case R.id.sb_horizon: {
				mClient.send(HORIZON);
				break;
			}
			default: {
				break;
			}
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent back_intent = new Intent();
			back_intent.setClass(Monitor.this, Introduction.class);
			startActivity(back_intent);
			Monitor.this.finish();
		}
		return false;
	}
	
	/**
	 * 功能： 自定义双击事件监听器
	 */
	// 计算点击的次数
	private int count = 0;
	// 第一次点击的时间 long型
	private long firstClick = 0;
	// 最后一次点击的时间
	private long lastClick = 0;
	// 双击间隔时间
	private int deltaTime = 500;

	private class cameraOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					// 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
					if (firstClick != 0
							&& System.currentTimeMillis() - firstClick > deltaTime) {
						count = 0;
					}
				count++;
				if (count == 1) {
					firstClick = System.currentTimeMillis();
				} else if (count == 2) {
					lastClick = System.currentTimeMillis();
					// 两次点击小于500ms 也就是连续点击
					if (lastClick - firstClick < deltaTime) {
						// MjpegView.SIZE_FULLSCREEN
						// MjpegView.SIZE_BEST_FIT
						if (videoDisplayMode == MjpegView.SIZE_FULLSCREEN) {
							videoDisplayMode = MjpegView.SIZE_BEST_FIT;
							sv_camera.setDisplayMode(videoDisplayMode);
						} else {
							videoDisplayMode = MjpegView.SIZE_FULLSCREEN;
							sv_camera.setDisplayMode(videoDisplayMode);
						}
					}
					clear();
				}
			}
			return false;
		}

		// 清空状态
		private void clear() {
			count = 0;
			firstClick = 0;
			lastClick = 0;
		}
	}
	
	/*
	 * 功能： 自定义重力感应监听器
	 */
	private class AccelerometerOnTouchListener implements SensorEventListener {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			// float[] values = event.values;
			// values[0] X方向上的加速度
			// values[1] Y方向上的加速度
			// values[2] Z方向上的加速度
		}
	}
	
}
