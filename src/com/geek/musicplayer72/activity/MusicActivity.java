package com.geek.musicplayer72.activity;

import com.geek.musicplayer72.R;
import com.geek.musicplayer72.R.layout;
import com.geek.musicplayer72.R.menu;
import com.geek.musicplayer72.bean.MusicBean;
import com.geek.musicplayer72.http.IHttpImageListerner;
import com.geek.musicplayer72.manager.MusicManager;
import com.geek.musicplayer72.utils.ActivityUtil;
import com.geek.musicplayer72.utils.BitmapUtil;
import com.geek.musicplayer72.utils.TextUtil;
import com.geek.musicplayer72.view.CircleImageView;
import com.geek.musicplayer72.view.LrcView;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MusicActivity extends BaseMusicServiceActivity {
	ImageView iv_backgroud,iv_next,iv_music_pre;
	RelativeLayout fl_music_disc;
	CircleImageView iv_disc_icon;
	TextView tv_music_current;
	TextView tv_music_duration;
	SeekBar sb_music;
	RelativeLayout rl_disc_view;
	LrcView lrc_music;
	CheckBox cb_music_play_pause;
	Handler handler = new Handler();
	
	Runnable r = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			tv_music_current.setText(TextUtil.getTime(musicService.getCurrentPosition()));
			sb_music.setProgress(musicService.getCurrentPosition());
			
			handler.postDelayed(r, 1000);
		}
	};
	
	public void changeDiscOrLrc(View v){
		if(lrc_music.getVisibility() == View.GONE){
			lrc_music.setVisibility(View.VISIBLE);
			rl_disc_view.setVisibility(View.INVISIBLE);
		}else{
			lrc_music.setVisibility(View.GONE);
			rl_disc_view.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);
		//Activity
		setActionBarBackGroudColor(Color.GRAY);
		
		initViews();
		initEvent();
		
		
	}
	private void initEvent() {
		cb_music_play_pause.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					ActivityUtil.sendMusicBroadCast(MusicActivity.this, MUISC_PLAY, musicService.getmCurrentMusicBean());
				}else{
					ActivityUtil.sendMusicBroadCast(MusicActivity.this, MUISC_PUASE,  musicService.getmCurrentMusicBean());
				}
			}
		});
		iv_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				musicService.nextMusic();
			}
		});
		iv_music_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				musicService.TopMusic();
			}
		});
	}

	private void initLRC() {
		// TODO Auto-generated method stub
		MusicBean mb = musicService.getmCurrentMusicBean();
		
		
		if(mb.getFlag() == MUSIC_FROM_WEB){
			lrc_music.setLRCPath(MusicManager.SEVER_HOST,mb.getMusicLRCUrl());
			lrc_music.setMediaPlayer(musicService.getMediaPlayer());
		}
		if(musicService.isPlaying()){
			cb_music_play_pause.setChecked(true);
		}else{
			cb_music_play_pause.setChecked(false);
		}
	}

	private void startAnim(){
		//fl_music_disc.set
		fl_music_disc.post(new Runnable() {	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				RotateAnimation ra = new RotateAnimation(
						0, 
						360, 
						fl_music_disc.getWidth()/2,//锚点 
						fl_music_disc.getHeight()/2);//
				//持续时间
				ra.setDuration(12000);
				
				//设置运动的轨迹运动
				ra.setInterpolator(new LinearInterpolator());
				
				//重复的次数
				ra.setRepeatCount(RotateAnimation.INFINITE);
				//重复的模式
				ra.setRepeatMode(RotateAnimation.RESTART);
				fl_music_disc.startAnimation(ra);
			}
		});

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startAnim();
	}
	private void initViews() {
		// TODO Auto-generated method stub
		fl_music_disc = (RelativeLayout) findViewById(R.id.fl_music_disc);
		iv_disc_icon =  (CircleImageView) findViewById(R.id.iv_music_disc_icon);
		
		tv_music_current = (TextView) findViewById(R.id.tv_current_time);
		tv_music_duration = (TextView) findViewById(R.id.tv_duration_time);
	
		sb_music = (SeekBar) findViewById(R.id.sb_music);
		
		lrc_music = (LrcView) findViewById(R.id.lrc_music);
		rl_disc_view = (RelativeLayout) findViewById(R.id.rl_disc_view);
		cb_music_play_pause=(CheckBox) findViewById(R.id.cb_music_play_pause);
		iv_next =(ImageView) findViewById(R.id.iv_next);
		iv_music_pre=(ImageView) findViewById(R.id.iv_music_pre);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music, menu);
		return true;
	}
	TextView tv_title;
	@Override
	public void onActivityServiceConnected() {
		// TODO Auto-generated method stub
		super.onActivityServiceConnected();
		
		
		tv_title = new TextView(this);
		MusicBean mb = musicService.getmCurrentMusicBean();
		
		tv_title.setTextSize(15);
		tv_title.setTextColor(Color.WHITE);
		setActionBarTitleView(tv_title);
		
		iv_backgroud = (ImageView) findViewById(R.id.iv_background);
			
		//启动handler中的线程
		handler.postDelayed(r, 1000);
		
		sb_music.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser){
					//用户主动拖动的时候
					musicService.seekTo(progress);
				}
			}
		});
		
		refreshMusicUi(mb);
		
		initLRC();
	}
	
	public void refreshMusicUi(final MusicBean mb){
		tv_title.setText(mb.getMusicName());
		
		
		fl_music_disc.post(new Runnable() {
			
			@Override
			public void run() {
				if(mb.getFlag() == MUSIC_FROM_LOCAL){
					Bitmap bitmap = BitmapUtil.getArtwork(MusicActivity.this,
							mb.getSong_id(), 
							mb.getAlbum_id(), 
							true, true);
					// TODO Auto-generated method stub
					Bitmap disBm = Bitmap.createScaledBitmap(
							bitmap, 
							fl_music_disc.getWidth()/5*4, 
							fl_music_disc.getHeight()/5*4, true);
					//设置唱片图片
					iv_disc_icon.setImageBitmap(disBm);
					//设置背景图片
					iv_backgroud.setImageBitmap(BitmapUtil.doBlur(bitmap,
							10,
							false));
				}else{
					MusicManager.loadWebImage(mb.getMusicImageUrl(), null,new IHttpImageListerner() {
						
						@Override
						public void onLoadImage(Bitmap bitmap) {
							// TODO Auto-generated method stub
							
							Bitmap disBm = Bitmap.createScaledBitmap(
									bitmap, 
									fl_music_disc.getWidth()/5*4, 
									fl_music_disc.getHeight()/5*4, true);
							//设置唱片图片
							iv_disc_icon.setImageBitmap(disBm);
							
							iv_backgroud.setImageBitmap(BitmapUtil.doBlur(bitmap,
									10,
									false));
							
						}
					});
				}
			}
		});
		
		
		
		
		
		tv_music_duration.setText(TextUtil.getTime(musicService.getDuration()));
		
		sb_music.setMax(musicService.getDuration());
		
	}
	
	@Override
	public void onUpdateMusicState(MusicBean mb) {
		// TODO Auto-generated method stub
		super.onUpdateMusicState(mb);
		
		refreshMusicUi(mb);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeCallbacks(r);
	}
}
