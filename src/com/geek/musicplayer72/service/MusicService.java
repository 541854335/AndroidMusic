package com.geek.musicplayer72.service;

import java.io.IOException;
import java.util.List;

import com.geek.musicplayer72.R;
import com.geek.musicplayer72.activity.MusicActivity;
import com.geek.musicplayer72.bean.MusicBean;
import com.geek.musicplayer72.bean.MusicGroup;
import com.geek.musicplayer72.bean.MusicPosition;
import com.geek.musicplayer72.http.IHttpImageListerner;
import com.geek.musicplayer72.manager.MusicManager;
import com.geek.musicplayer72.utils.ActivityUtil;
import com.geek.musicplayer72.utils.AppConfig;
import com.geek.musicplayer72.utils.BitmapUtil;
import com.geek.musicplayer72.widget.MusicAppWidget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class MusicService extends Service implements AppConfig {

	MediaPlayer mp;
	MusicBean mCurrentMusicBean;
	NotificationManager nm;
	AppWidgetManager appWidgetManager;

	public MediaPlayer getMediaPlayer() {
		return mp;
	}

	public MusicBean getmCurrentMusicBean() {
		return mCurrentMusicBean;
	}

	public void seekTo(int progress) {
		mp.seekTo(progress);
	}

	public int getCurrentPosition() {
		return mp.getCurrentPosition();
	}

	public int getDuration() {
		return mp.getDuration();
	}

	public boolean isPlaying() {
		return mp.isPlaying();
	}

	public void setmCurrentMusicBean(MusicBean mCurrentMusicBean) {
		this.mCurrentMusicBean = mCurrentMusicBean;
	}

	@Override
	public IBinder onBind(Intent intent) {

		return new MyBinder();
	}

	public class MyBinder extends Binder {
		public MusicService getService() {
			return MusicService.this;
		}
	}

	// 在service第一次被创建的时候调用
	MusicBroadcastReceiver musicReceiver;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mp = new MediaPlayer();

		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		appWidgetManager = AppWidgetManager.getInstance(this);

		//

		// 注册广播
		musicReceiver = new MusicBroadcastReceiver();

		registerReceiver(musicReceiver, new IntentFilter(BROADCAST_MUISC));

	}

	public void setOnCompletionListener(OnCompletionListener listener) {
		mp.setOnCompletionListener(listener);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mp != null) {
			mp.release();
		}

		// 解除注册
		if (musicReceiver != null) {
			unregisterReceiver(musicReceiver);
		}
	}

	class MusicBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int state = intent.getIntExtra(MUISC_STATE, -1);
			MusicBean mb = intent.getParcelableExtra(MUSIC_BEAN);

			switch (state) {
			case MUISC_START:
				startMusic(mb);

				break;
			case MUISC_PLAY:

				mp.start();
				sendNotification(mCurrentMusicBean);
				// 刷新界面
				ActivityUtil.sendMusicBroadCast(MusicService.this,
						MUISC_UPDATE_UI, mCurrentMusicBean);

				break;
			case MUISC_PUASE:
				mp.pause();
				sendNotification(mCurrentMusicBean);
				// 刷新界面
				ActivityUtil.sendMusicBroadCast(MusicService.this,
						MUISC_UPDATE_UI, mCurrentMusicBean);

				break;
			case MUISC_CLOSE:

				nm.cancel(NOTIFACTION_ID);
				break;
			case MUISC_NEXT:
				nextMusic();
				break;
			case MUISC_TOP:
				TopMusic();
				break;
			}
		  
		}

		}

	

	public void startMusic(MusicBean mb, MusicPosition position) {
		MusicManager.mCurrentPosition = position;
		startMusic(mb);
	}

	public void startMusicFromWeb(MusicBean mb) {
		mCurrentMusicBean = mb;
		try {
			// 重置mediaplayer的状态
			mp.reset();
			mp.setDataSource(MusicManager.SEVER_HOST + mb.getMusicPath());
			// 准备音乐
			// mp.prepare();//同步
			mp.prepareAsync();// 异步

			mp.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mp.start();
					// 关闭notification
					// nm.cancel(NOTIFACTION_ID);
					// 发出通知
					sendNotification(mCurrentMusicBean);
					// 更新Ui
					ActivityUtil.sendMusicBroadCast(MusicService.this,
							MUISC_UPDATE_UI, mCurrentMusicBean);

				}
			});

			// mp.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void startMusic(MusicBean mb) {

		// 音乐结束后的状态
		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {

				// 重复放播
				// startMusic(mCurrentMusicBean);

				// 发送一条广播播放了下一首
				ActivityUtil.sendMusicBroadCast(MusicService.this, MUISC_NEXT,
						nextMusic());
			}
		});

		mCurrentMusicBean = mb;
		try {
			// 重置mediaplayer的状态
			mp.reset();

			mp.setDataSource(mb.getMusicPath());
			// 准备音乐
			mp.prepare();// 同步
			mp.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 关闭notification
		// nm.cancel(NOTIFACTION_ID);
		sendNotification(mCurrentMusicBean);
	}

	private void sendNotification(MusicBean mb) {
		// 创建一个notification
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);

		final Notification notification = builder
				.setSmallIcon(R.drawable.ic_launcher)
				.setPriority(Notification.PRIORITY_MAX) // 最高级
				.setOngoing(true) // 设置一直存在
				.setTicker("正在播放音乐...").build();

		final RemoteViews rv = new RemoteViews(getPackageName(),// 获取当前App的报名
				R.layout.notification_layout);
		// 显示文字
		rv.setTextViewText(R.id.tv_music_name, mb.getMusicName());
		rv.setTextViewText(R.id.tv_singer, mb.getArtist());

		// 关闭notification
		rv.setOnClickPendingIntent(R.id.iv_close,
				getBroadcastPengIntent(MUISC_CLOSE, MUISC_CLOSE));

		// 设置显示的图片
		if (mb.getFlag() == MUSIC_FROM_LOCAL) {
			rv.setImageViewBitmap(
					R.id.iv_music_icon,
					BitmapUtil.getArtwork(this, mb.getSong_id(),
							mb.getAlbum_id(), true, false));

			MusicManager.loadWebImage(mb.getMusicImageUrl(), null,
					new IHttpImageListerner() {

						@Override
						public void onLoadImage(Bitmap bitmap) {
							// TODO Auto-generated method stub

							// 发出
							nm.notify(NOTIFACTION_ID, notification);
							ComponentName provider = new ComponentName(
									getApplicationContext(),
									MusicAppWidget.class);
							// 更新widget
							appWidgetManager.updateAppWidget(provider, rv);
						}
					});
		} else {
			MusicManager.loadWebImage(mb.getMusicImageUrl(), null,
					new IHttpImageListerner() {

						@Override
						public void onLoadImage(Bitmap bitmap) {
							// TODO Auto-generated method stub
							rv.setImageViewBitmap(R.id.iv_music_icon, bitmap);

							// 发出
							nm.notify(NOTIFACTION_ID, notification);
							ComponentName provider = new ComponentName(
									getApplicationContext(),
									MusicAppWidget.class);
							// 更新widget
							appWidgetManager.updateAppWidget(provider, rv);
						}
					});
		}
		//
		if (mp.isPlaying()) {
			rv.setImageViewResource(R.id.iv_btn_play, R.drawable.note_btn_pause);
			rv.setOnClickPendingIntent(R.id.iv_btn_play,
					getBroadcastPengIntent(MUISC_PUASE, MUISC_PUASE));

		} else {
			rv.setImageViewResource(R.id.iv_btn_play, R.drawable.note_btn_play);
			rv.setOnClickPendingIntent(R.id.iv_btn_play,
					getBroadcastPengIntent(MUISC_PLAY, MUISC_PLAY));

		}

		rv.setOnClickPendingIntent(R.id.iv_btn_next,
				getBroadcastPengIntent(MUISC_NEXT, MUISC_NEXT));
		rv.setOnClickPendingIntent(R.id.iv_btn_pre,
				getBroadcastPengIntent(MUISC_TOP, MUISC_TOP));

		// 设置大型的内容
		notification.bigContentView = rv;

	}

	private PendingIntent getBroadcastPengIntent(int broadcaststate,
			int resquest// 请求编号
	) {
		Intent intent = ActivityUtil.getMuiscBroadcastIntent(broadcaststate);

		PendingIntent pi = PendingIntent.getBroadcast(this, resquest, // 请求的编号
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// ActivityUtil.sendMusicBroadCast(ctx, state, mb);
		// rv.setOnClickPendingIntent(R.id.iv_close, pi);
		return pi;
	}

	public static final int NOTIFACTION_ID = 5213;

	public MusicBean nextMusic() {

		MusicBean mb;
		if (mCurrentMusicBean.getFlag() == MUSIC_FROM_LOCAL) {
			// 获取当前的列表
			List<MusicGroup> list = MusicManager
					.getPinYinOrderList(getContentResolver());

			MusicPosition musicPosition = MusicManager.mCurrentPosition;

			int currentGroup = musicPosition.getGroupPostition();
			int currentChird = musicPosition.getChirdPostiton();
			int nextGroup = currentGroup;
			int nextChild = currentChird;

			if (list.get(currentGroup).getChildren().size() == currentChird + 1) {
				// 判断是否最后一条

				if (list.size() == currentGroup + 1) {
					nextGroup = 0;

				} else {
					nextGroup++;
				}
				nextChild = 0;

			} else {
				nextChild++;
			}

			// 更新选中的位置
			MusicManager.setCurrentMusicPosition(nextGroup, nextChild);

			mb = list.get(nextGroup).getChildren().get(nextChild);

			startMusic(mb);
		} else {
			int pos = MusicManager.mCurrentPostionFromWeb.getChirdPostiton();
			pos++;
			if (pos >= MusicManager.musicListFromWeb.size()) {
				pos = 0;
			}
			mb = MusicManager.musicListFromWeb.get(pos);

			MusicManager.mCurrentPostionFromWeb.setChirldPostiton(pos);

			startMusicFromWeb(mb);
		}

		sendNotification(mCurrentMusicBean);
		// 刷新界面
		ActivityUtil.sendMusicBroadCast(MusicService.this, MUISC_UPDATE_UI,
				mCurrentMusicBean);

		return mb;
	}

	public MusicBean TopMusic() {

		MusicBean mb;
		if (mCurrentMusicBean.getFlag() == MUSIC_FROM_LOCAL) {
			// 获取当前的列表
			List<MusicGroup> list = MusicManager
					.getPinYinOrderList(getContentResolver());

			MusicPosition musicPosition = MusicManager.mCurrentPosition;

			int currentGroup = musicPosition.getGroupPostition();
			int currentChird = musicPosition.getChirdPostiton();
			int nextGroup = currentGroup;
			int nextChild = currentChird;

			if (0 == currentChird) {
				// 判断是否第一条

				if (0 == currentGroup) {
					nextGroup = list.size() - 1;

				} else {
					nextGroup--;
				}
				nextChild = list.get(nextGroup).getChildren().size() - 1;

			} else {
				nextChild--;
			}

			// 更新选中的位置
			MusicManager.setCurrentMusicPosition(nextGroup, nextChild);

			mb = list.get(nextGroup).getChildren().get(nextChild);

			startMusic(mb);
		} else {
			int pos = MusicManager.mCurrentPostionFromWeb.getChirdPostiton();
			pos--;
			if (pos >= MusicManager.musicListFromWeb.size()) {
				pos = 0;
			} else if (pos < 0) {
				pos = MusicManager.musicListFromWeb.size() - 1;
			}

			mb = MusicManager.musicListFromWeb.get(pos);

			MusicManager.mCurrentPostionFromWeb.setChirldPostiton(pos);

			startMusicFromWeb(mb);
		}

		sendNotification(mCurrentMusicBean);
		// 刷新界面
		ActivityUtil.sendMusicBroadCast(MusicService.this, MUISC_UPDATE_UI,
				mCurrentMusicBean);

		return mb;
	}
}
