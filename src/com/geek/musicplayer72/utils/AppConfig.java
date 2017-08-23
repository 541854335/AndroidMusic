package com.geek.musicplayer72.utils;

public interface AppConfig {
	public static final String BROADCAST_MUISC = "com.geek.broadcast.music";
	public static final String MUISC_STATE = "musicState";
	
	public static final String MUSIC_BEAN = "musicbean";
	
	public static final int MUISC_START = 1;//1�������¿�ʼ
	
	public static final int MUISC_PLAY  = 2;//2������
	
	public static final int MUISC_PUASE = 3;//3������ͣ
	
	public static final int MUISC_NEXT = 4;//4������һ��
	
	public static final int MUISC_TOP = 5;//5������һ��
	
	public static final int MUISC_UPDATE_UI = 98;
	
	public static final int MUISC_CLOSE = 99;//3����رշ���

	
	public static final int MUSIC_FROM_LOCAL = 0;
	public static final int MUSIC_FROM_WEB = 1;
}
