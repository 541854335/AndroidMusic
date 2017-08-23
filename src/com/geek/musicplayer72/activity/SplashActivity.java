package com.geek.musicplayer72.activity;

import com.geek.musicplayer72.R;
import com.geek.musicplayer72.R.layout;
import com.geek.musicplayer72.R.menu;
import com.geek.musicplayer72.TestActivity;
import com.geek.musicplayer72.utils.ActivityUtil;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class SplashActivity extends FragmentActivity {
	
	Handler handler = new Handler();
	Runnable r = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			toMain();
			finish();
		}
	};
	
	public void onBackPressed() {
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		handler.postDelayed(r, 1000);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeCallbacks(r);
	}
	
	public void toMain(){
		ActivityUtil.toActivity(this, MainActivity.class);
	}
}
