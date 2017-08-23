package com.geek.musicplayer72.utils;

public interface AppConfig {
	public static final String BROADCAST_MUISC = "com.geek.broadcast.music";
	public static final String MUISC_STATE = "musicState";
	
	public static final String MUSIC_BEAN = "musicbean";
	
	public static final int MUISC_START = 1;//1代表重新开始
	
	public static final int MUISC_PLAY  = 2;//2代表播放
	
	public static final int MUISC_PUASE = 3;//3代表暂停
	
	public static final int MUISC_NEXT = 4;//4代表下一首
	
	public static final int MUISC_TOP = 5;//5代表上一首
	
	public static final int MUISC_UPDATE_UI = 98;
	
	public static final int MUISC_CLOSE = 99;//3代表关闭服务

	
	public static final int MUSIC_FROM_LOCAL = 0;
	public static final int MUSIC_FROM_WEB = 1;
}
