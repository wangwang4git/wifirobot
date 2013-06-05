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
	
	//������ư���
	private Button bt_v_up;
	private Button bt_v_down;
	private Button bt_h_left;
	private Button bt_h_right;

	public MjpegView sv_camera = null;
	// ����ģʽ
	int videoDisplayMode = -1;

	private WifiRobotControlClient mClient = null;
	private Thread mControlThread = null;
	private Handler mHandler = null;
	
	// ϵͳSensor������
	private SensorManager sensorManager = null;
	// ϵͳSensor������
	private AccelerometerOnTouchListener accelerometerListener = null;
	// ������Ӧ����
	private CheckBox mGravity = null;
	private boolean isOpenSensor = false;
	//����������������
	private CheckBox mCtrlModel = null;
	private TextView tv_ctrl_model = null;
	private boolean isButtonModel = true;

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
		
		//�������
		bt_v_up = (Button)findViewById(R.id.bt_v_up);
		bt_v_down = (Button)findViewById(R.id.bt_v_down);
		bt_h_left = (Button)findViewById(R.id.bt_h_left);
		bt_h_right = (Button)findViewById(R.id.bt_h_right);

		bt_advance.setOnTouchListener(new ClickEvent());
		bt_retreat.setOnTouchListener(new ClickEvent());
		bt_right.setOnTouchListener(new ClickEvent());
		bt_left.setOnTouchListener(new ClickEvent());

		sb_vertical.setOnSeekBarChangeListener(this.seekBarListener);
		sb_horizon.setOnSeekBarChangeListener(this.seekBarListener);

		sv_camera = (MjpegView) findViewById(R.id.sv);
		// �����Զ���˫���¼�������
		sv_camera.setOnTouchListener(new cameraOnTouchListener());
		String cameraURL = "http://" + System_data.videoAddr_store.trim() + ":"
				+ System_data.videoPort_store.trim() + "/?action=stream";
		MjpegInputStream mjpegInputStream = MjpegInputStream.read(cameraURL);
		if (null == mjpegInputStream) {
			Toast.makeText(this, "Video access failure", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Video access success", Toast.LENGTH_SHORT).show();
			sv_camera.setSource(MjpegInputStream.read(cameraURL));
			// ����ģʽ
			videoDisplayMode = MjpegView.SIZE_FULLSCREEN; // MjpegView.SIZE_BEST_FIT
			sv_camera.setDisplayMode(videoDisplayMode);
			sv_camera.showFps(true);
		}

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0: {
					Toast.makeText(getApplicationContext(), "Socket link success",
							Toast.LENGTH_SHORT).show();
					break;
				}
				case -1: {
					Toast.makeText(getApplicationContext(), "Socket link failure",
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
		
		// ��ȡϵͳ�Ĵ������������
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
					
					bt_v_up.setVisibility(View.INVISIBLE);
					bt_v_down.setVisibility(View.INVISIBLE);
					bt_h_left.setVisibility(View.INVISIBLE);
					bt_h_right.setVisibility(View.INVISIBLE);
					
					tv_ctrl_model.setVisibility(View.INVISIBLE);
					mCtrlModel.setVisibility(View.INVISIBLE);

					
				} else {
					isOpenSensor = false;

					bt_advance.setVisibility(View.VISIBLE);
					bt_retreat.setVisibility(View.VISIBLE);
					bt_right.setVisibility(View.VISIBLE);
					bt_left.setVisibility(View.VISIBLE);
					
					tv_ctrl_model.setVisibility(View.VISIBLE);
					mCtrlModel.setVisibility(View.VISIBLE);
					mCtrlModel.setChecked(isButtonModel);
					if(isButtonModel)
					{
						bt_v_up.setVisibility(View.VISIBLE);
						bt_v_down.setVisibility(View.VISIBLE);
						bt_h_left.setVisibility(View.VISIBLE);
						bt_h_right.setVisibility(View.VISIBLE);
						
						((TextView) findViewById(R.id.tv_vertical))
						.setVisibility(View.INVISIBLE);
				        ((TextView) findViewById(R.id.tv_horizon))
						.setVisibility(View.INVISIBLE);
			          	sb_vertical.setVisibility(View.INVISIBLE);
			           	sb_horizon.setVisibility(View.INVISIBLE);
					}
					else
					{
						bt_v_up.setVisibility(View.INVISIBLE);
						bt_v_down.setVisibility(View.INVISIBLE);
						bt_h_left.setVisibility(View.INVISIBLE);
						bt_h_right.setVisibility(View.INVISIBLE);
						
						((TextView) findViewById(R.id.tv_vertical))
						.setVisibility(View.VISIBLE);
				        ((TextView) findViewById(R.id.tv_horizon))
						.setVisibility(View.VISIBLE);
			          	sb_vertical.setVisibility(View.VISIBLE);
			           	sb_horizon.setVisibility(View.VISIBLE);
					}
					
					

				}
			}
		});
		
		//���ö���Ŀ���ģʽ���������߽�������
		mCtrlModel = (CheckBox)findViewById(R.id.cb_ctrl_model);
		tv_ctrl_model = (TextView)findViewById(R.id.tv_ctrl_model);
		sb_vertical.setVisibility(View.GONE);
		sb_horizon.setVisibility(View.GONE);
		((TextView)findViewById(R.id.tv_vertical)).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.tv_horizon)).setVisibility(View.INVISIBLE);
		mCtrlModel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					((TextView) findViewById(R.id.tv_vertical))
					.setVisibility(View.GONE);
			        ((TextView) findViewById(R.id.tv_horizon))
					.setVisibility(View.GONE);
		           	sb_vertical.setVisibility(View.GONE);
			        sb_horizon.setVisibility(View.GONE);
			        

					bt_v_up.setVisibility(View.VISIBLE);
					bt_v_down.setVisibility(View.VISIBLE);
					bt_h_left.setVisibility(View.VISIBLE);
					bt_h_right.setVisibility(View.VISIBLE);
			        
			        isButtonModel = true;
				}
				else
				{
					((TextView) findViewById(R.id.tv_vertical))
					.setVisibility(View.VISIBLE);
			        ((TextView) findViewById(R.id.tv_horizon))
					.setVisibility(View.VISIBLE);
		        	sb_vertical.setVisibility(View.VISIBLE);
			        sb_horizon.setVisibility(View.VISIBLE);
			        
					bt_v_up.setVisibility(View.GONE);
					bt_v_down.setVisibility(View.GONE);
					bt_h_left.setVisibility(View.GONE);
					bt_h_right.setVisibility(View.GONE);
			        
			        isButtonModel = false;
				}
				
			}
		});
		isOpenSensor = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Ϊϵͳ�ļ��ٶȴ�����ע�������
		sensorManager.registerListener(accelerometerListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		// ȡ��ע��
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
			// ���ȸı�ʱ����
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// ��ʼ�϶�ʱ��������onProgressChanged��������onStartTrackingTouch��ֹͣ�϶�ǰֻ����һ��
			// ��onProgressChangedֻҪ���϶����ͻ��ظ�����

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
			// �����϶�ʱ����

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
	 * ���ܣ� �Զ���˫���¼�������
	 */
	// �������Ĵ���
	private int count = 0;
	// ��һ�ε����ʱ�� long��
	private long firstClick = 0;
	// ���һ�ε����ʱ��
	private long lastClick = 0;
	// ˫�����ʱ��
	private int deltaTime = 500;

	private class cameraOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					// ����ڶ��ε�� �����һ�ε��ʱ����� ��ô���ڶ��ε����Ϊ��һ�ε��
					if (firstClick != 0
							&& System.currentTimeMillis() - firstClick > deltaTime) {
						count = 0;
					}
				count++;
				if (count == 1) {
					firstClick = System.currentTimeMillis();
				} else if (count == 2) {
					lastClick = System.currentTimeMillis();
					// ���ε��С��500ms Ҳ�����������
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

		// ���״̬
		private void clear() {
			count = 0;
			firstClick = 0;
			lastClick = 0;
		}
	}
	
	/*
	 * ���ܣ� �Զ���������Ӧ������
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
			// values[0] X�����ϵļ��ٶ�
			// values[1] Y�����ϵļ��ٶ�
			// values[2] Z�����ϵļ��ٶ�
		}
	}
	
}
