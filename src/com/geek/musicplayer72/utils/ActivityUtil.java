package com.geek.musicplayer72.utils;

import com.geek.musicplayer72.bean.MusicBean;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;

public class ActivityUtil implements AppConfig {
	
	public static void toActivity(Context ctx,Class clazz){
		Intent intent = new Intent();
		intent.setClass(ctx, clazz);
		ctx.startActivity(intent);
	}
	
	public static Intent getMuiscBroadcastIntent(int state){
		Intent intent = new Intent();
		intent.setAction(BROADCAST_MUISC);
		intent.putExtra(MUISC_STATE,state);
		return intent;
	}
	
	public static void sendMusicBroadCast(Context ctx,int state,MusicBean mb){
		Intent intent = getMuiscBroadcastIntent(state);
		
		//带上参数
		intent.putExtra(MUSIC_BEAN, mb);
		ctx.sendBroadcast(intent);
	}
}
