package com.geek.musicplayer72.activity;

import java.util.List;

import com.geek.musicplayer72.R;
import com.geek.musicplayer72.R.layout;
import com.geek.musicplayer72.R.menu;
import com.geek.musicplayer72.bean.MusicBean;
import com.geek.musicplayer72.bean.MusicGroup;
import com.geek.musicplayer72.bean.MusicPosition;
import com.geek.musicplayer72.fragment.DiscoverFragment;
import com.geek.musicplayer72.fragment.MusicFragment;
import com.geek.musicplayer72.fragment.listener.MuiscFragmentInfo;
import com.geek.musicplayer72.http.HttpUtil;
import com.geek.musicplayer72.manager.MusicManager;
import com.geek.musicplayer72.service.MusicService;
import com.geek.musicplayer72.utils.ActivityUtil;
import com.geek.musicplayer72.utils.AppConfig;
import com.geek.musicplayer72.utils.BitmapUtil;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends BaseMusicServiceActivity implements MuiscFragmentInfo{
	ViewPager vp;
	RadioGroup rg_main;
	TextView tv_muisc_name,tv_music_artist;
	CheckBox cb_music_playOrPause;
	LinearLayout ll_music_info;
	LinearLayout ll_no_music;
	ImageView iv_next;
	ImageView iv_music_icon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		setActionBar(R.layout.actionbar_main);
		
		
		initViews();
		
		initViewPager();
		
		
		
		initEvent();
		
		
	}
	@Override
	public void onActivityServiceConnected() {
		// TODO Auto-generated method stub
		super.onActivityServiceConnected();
		MusicBean mb = musicService.getmCurrentMusicBean();
		setMusicInfo(mb);
		
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		cb_music_playOrPause.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					ActivityUtil.sendMusicBroadCast(MainActivity.this, MUISC_PLAY, musicService.getmCurrentMusicBean());
				}else{
					ActivityUtil.sendMusicBroadCast(MainActivity.this, MUISC_PUASE,  musicService.getmCurrentMusicBean());
				}
			}
		});
		
		iv_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//ActivityUtil.sendMusicBroadCast(MainActivity.this, MUISC_START, mb);
				//刷新界面
				setMusicInfo(musicService.nextMusic());			
			
			}
		});
		
		ll_music_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				toMusicActivity();
			}
		});
	}
	
	private void toMusicActivity(){
		ActivityUtil.toActivity(MainActivity.this, MusicActivity.class);
		overridePendingTransition(
				R.anim.right_in, //进入的动画效果
				R.anim.left_out); //出去的动画效果
	}
	
	public void setMusicInfo(MusicBean mb){
		if(mb!=null){
			ll_no_music.setVisibility(View.GONE);
			ll_music_info.setVisibility(View.VISIBLE);
		}else{
			ll_no_music.setVisibility(View.VISIBLE);
			ll_music_info.setVisibility(View.GONE);
			return;
		}

		tv_muisc_name.setText(mb.getMusicName());
		tv_music_artist.setText(mb.getArtist());
		tv_muisc_name.requestFocus();
		
		if(musicService.isPlaying()){
			cb_music_playOrPause.setChecked(true);
		}else{
			cb_music_playOrPause.setChecked(false);
		}
		
		
		if(mb.getFlag()==MUSIC_FROM_LOCAL){
			Bitmap bm = BitmapUtil.getArtwork(
					this, 
					mb.getSong_id(), 
					mb.getAlbum_id(), 
					true, //支持使用默认图标
					true);//使用缩略图
			iv_music_icon.setImageBitmap(bm);
		}else{
			MusicManager.loadWebImage(mb.getMusicImageUrl(),iv_music_icon );
		}
	}
	
	
	private void initViews() {
		// TODO Auto-generated method stub
		tv_muisc_name = (TextView) findViewById(R.id.tv_music_name);
		cb_music_playOrPause = (CheckBox) findViewById(R.id.cb_music_play_pause);
		ll_music_info = (LinearLayout) findViewById(R.id.ll_muiscinfo);
		ll_no_music  = (LinearLayout) findViewById(R.id.ll_nomusic);
		iv_next     = (ImageView) findViewById(R.id.iv_next);
		tv_music_artist=(TextView) findViewById(R.id.tv_music_artist);
		iv_music_icon = (ImageView) findViewById(R.id.iv_music_icon);
	}

	private void initViewPager() {
		vp = (ViewPager) findViewById(R.id.vp_main);
		rg_main = (RadioGroup) findViewById(R.id.rg_main);
		vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 3;
			}
			
			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub
				Fragment fragment = null ;
	
				switch(position){
				case  0:
					fragment = new DiscoverFragment();
					break;
				case 1:
					fragment = new MusicFragment();
					break;
				case  2:
					fragment = new DiscoverFragment();
					break;
				}
				
				return fragment;
			}
		});
		
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				RadioButton rb = (RadioButton) rg_main.getChildAt(arg0);
				rb.setChecked(true);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		rg_main.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				for (int i = 0; i < rg_main.getChildCount(); i++) {
					View child = rg_main.getChildAt(i);
					if(child.getId() == checkedId){
						vp.setCurrentItem(i);
						break;
					}
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	//Fragment的音乐被选中的时候
	@Override
	public void onMusicSelected(MusicBean mb,MusicPosition musicPosition) {
		// TODO Auto-generated method stub
		//判断音乐选中的位置是否是同一个位置
		if(mb.getFlag() == MUSIC_FROM_LOCAL){
			MusicPosition currentPosition = MusicManager.mCurrentPosition;
			
			if(currentPosition.equals(musicPosition) 
					&& currentPosition.haveMusicPosition()){
				//相等的情况
				toMusicActivity();
				return;
			}
			//播放音乐
			musicService.startMusic(mb,musicPosition);
			setMusicInfo(mb);
			//判断music的状态给cb进行赋值
			cb_music_playOrPause.setChecked(true);
		}else{
			//判断位置是否等于当前位置
			MusicPosition currentPosition = MusicManager.mCurrentPostionFromWeb;
			
			if(currentPosition.equals(musicPosition) 
					&& currentPosition.haveMusicPosition()){
				//相等的情况
				toMusicActivity();
				return;
			}
			//设置当前被选中的位置
			MusicManager.mCurrentPostionFromWeb = musicPosition;
			
			musicService.startMusicFromWeb(mb);
		}
		
		
	}
	
	@Override
	public void onUpdateMusicState(MusicBean mb) {
		// TODO Auto-generated method stub
		super.onUpdateMusicState(mb);
		setMusicInfo(mb);
	}
}
