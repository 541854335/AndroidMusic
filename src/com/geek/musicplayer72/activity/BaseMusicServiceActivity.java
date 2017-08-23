package com.geek.musicplayer72.activity;

import com.geek.musicplayer72.bean.MusicBean;
import com.geek.musicplayer72.service.MusicService;
import com.geek.musicplayer72.utils.AppConfig;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

public class BaseMusicServiceActivity extends BaseBarActivity implements
		AppConfig {
	
	ActivityReceiver musicReceiver;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		// Æô¶¯service
		Intent intent = new Intent();
		intent.setClass(this, MusicService.class);
		startService(intent);
		// °ó¶¨Service
		bindService();

		// ×¢²á
		// ×¢²á¹ã²¥
		musicReceiver = new ActivityReceiver();

		registerReceiver(musicReceiver, new IntentFilter(BROADCAST_MUISC));
	}
	

	MusicService musicService;
	ServiceConnection serviceConn;

	public void onActivityServiceConnected() {

	}

	private void bindService() {
		serviceConn = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				musicService = ((MusicService.MyBinder) service).getService();
				onActivityServiceConnected();
			}
		};

		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(this, MusicService.class);
		bindService(intent, serviceConn, Service.BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (serviceConn != null) {
			unbindService(serviceConn);
		}
		unregisterReceiver(musicReceiver);
	}

	class ActivityReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int state = intent.getIntExtra(MUISC_STATE, -1);
			MusicBean mb = intent.getParcelableExtra(MUSIC_BEAN);

			switch (state) {
			case MUISC_UPDATE_UI:
				onUpdateMusicState(mb);
				break;

			default:
				break;
			}
		}

	}

	public void onUpdateMusicState(MusicBean mb) {
	}
}
