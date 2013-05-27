package com.linkspritedirect.activity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.linkspritedirect.R;
import com.linkspritedirect.activity.Set.System_data;
import com.linkspritedirect.mjpgview.MjpegInputStream;
import com.linkspritedirect.mjpgview.MjpegView;
import com.linkspritedirect.socket.WifiRobotControlClient;

public class Monitor extends Activity {
	private static final String ADVANCE = "T";
	private static final String RETREAT = "B";
	private static final String RIGHT = "R";
	private static final String LEFT = "L";
	private static final String STOP = "0";
	private static final String VERTICAL = "V";
	private static final String HORIZON = "H";

	public Button bt_advance;
	public Button bt_retreat;
	public Button bt_right;
	public Button bt_left;
	public Button bt_stop;

	public SeekBar sb_vertical;
	public SeekBar sb_horizon;

	public MjpegView sv_camera = null;
	// ����ģʽ
	int videoDisplayMode = -1;

	private WifiRobotControlClient mClient = null;
	private Thread mControlThread = null;
	private Handler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_monitor);

		bt_advance = (Button) findViewById(R.id.bt_advance);
		bt_retreat = (Button) findViewById(R.id.bt_retreat);
		bt_right = (Button) findViewById(R.id.bt_right);
		bt_left = (Button) findViewById(R.id.bt_left);
		//bt_stop = (Button) findViewById(R.id.bt_stop);

		sb_vertical = (SeekBar) findViewById(R.id.sb_vertical);
		sb_vertical.setProgress(50);
		sb_horizon = (SeekBar) findViewById(R.id.sb_horizon);
		sb_horizon.setProgress(50);

		bt_advance.setOnTouchListener(new ClickEvent());
		bt_retreat.setOnTouchListener(new ClickEvent());
		bt_right.setOnTouchListener(new ClickEvent());
		bt_left.setOnTouchListener(new ClickEvent());
		//bt_stop.setOnTouchListener(new ClickEvent());

		sb_vertical.setOnSeekBarChangeListener(this.seekBarListener);
		sb_horizon.setOnSeekBarChangeListener(this.seekBarListener);

		sv_camera = (MjpegView) findViewById(R.id.sv);
		// �����Զ���˫���¼�������
		sv_camera.setOnTouchListener(new ButtonOnTouchListener());
		String cameraURL = "http://" + System_data.videoAddr_store.trim() + ":"
				+ System_data.videoPort_store.trim() + "/?action=stream";
		sv_camera.setSource(MjpegInputStream.read(cameraURL));
		// ����ģʽ
		videoDisplayMode = MjpegView.SIZE_FULLSCREEN; // MjpegView.SIZE_BEST_FIT
		sv_camera.setDisplayMode(videoDisplayMode);
		sv_camera.showFps(true);

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0: {
					Toast.makeText(getApplicationContext(), "Socket���ӳɹ�",
							Toast.LENGTH_SHORT).show();
					break;
				}
				case -1: {
					Toast.makeText(getApplicationContext(), "Socket����ʧ��",
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
/*			case R.id.bt_stop: {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN: {
					mClient.send(STOP);
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
			}*/
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

	private class ButtonOnTouchListener implements OnTouchListener {
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
}
