package com.geek.musicplayer72.manager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.geek.musicplayer72.bean.MusicBean;
import com.geek.musicplayer72.bean.MusicGroup;
import com.geek.musicplayer72.bean.MusicPosition;
import com.geek.musicplayer72.http.HttpUtil;
import com.geek.musicplayer72.http.IHttpCallBack;
import com.geek.musicplayer72.http.IHttpImageListerner;
import com.geek.musicplayer72.utils.TextUtil;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class MusicManager {
	
	public static final String COL_ID = "id";
	public static final String COL_MUISCNAME = "musicname";
	public static final String COL_ARTIST = "artist";
	public static final String COL_DETAIL = "detail";
	public static final String COL_IMAGEURL = "imageurl";
	public static final String COL_MUSICURL = "musicurl";
	public static final String COL_MUISCLRCURL = "musiclrcurl";
	
	
	public static final String[] MUISC_ATTRS = new String[] {
			// 歌曲名
			MediaStore.Audio.Media.DISPLAY_NAME,
			// 专辑名
			MediaStore.Audio.Media.ALBUM,
			// 歌手名
			MediaStore.Audio.Media.ARTIST,
			// 歌曲时间
			MediaStore.Audio.Media.DURATION,
			// 歌曲大小
			MediaStore.Audio.Media.SIZE,
			// 歌曲ID
			MediaStore.Audio.Media._ID,
			// 歌曲路径
			MediaStore.Audio.Media.DATA, 
			// 专辑id
			MediaStore.Audio.Media.ALBUM_ID,
		};

	public static String LOG_TAG = "MusicManager";

	static String[] Letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	
	//本地音乐列表
	private static List<MusicGroup> musicGroupList;
	
	//网络的数据
	public static List<MusicBean> musicListFromWeb;
	
	public static MusicPosition mCurrentPostionFromWeb = new MusicPosition();
	public static MusicPosition mCurrentPosition = new MusicPosition();
	
	public static void setCurrentMusicPosition(int groupPosition,int chirldPostiton){
		
		mCurrentPosition.setChirldPostiton(chirldPostiton);
		mCurrentPosition.setGroupPostition(groupPosition);
	}
	
	
	public static List<MusicGroup> getPinYinOrderList(ContentResolver cr) {

		if (musicGroupList == null) {
			musicGroupList = new ArrayList<MusicGroup>();
			List<MusicBean> musicList = MusicManager.getListFromLocal(cr);

			// 创建大写字母的集合
			musicGroupList = new ArrayList<MusicGroup>();
			for (int i = 0; i < Letters.length; i++) {
				MusicGroup mg = new MusicGroup();
				mg.setName(Letters[i]);
				musicGroupList.add(mg);
			}

			for (int i = 0; i < musicList.size(); i++) {
				MusicBean mb = musicList.get(i);
				// 获取首先字母
				char letter = TextUtil.getFirstAscPinYin(mb.getMusicName()
						.charAt(0));

				// 找到首字对应的group
				MusicGroup mg = musicGroupList.get(letter - 97);
				// 将数据添加到group
				mg.getChildren().add(mb);

			}
			// 将没有child的group移除
			for (int i = 0; i < musicGroupList.size(); i++) {
				if (musicGroupList.get(i).getChildren().size() == 0) {
					musicGroupList.remove(i);
					i--;
				}
			}
		}

		return musicGroupList;
	}

	public static List<MusicBean> getListFromLocal(ContentResolver cr) {
		Cursor c = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,// 获取sdcard上音频文件
				MUISC_ATTRS, // 查询的字段
				null, null, null);
		List<MusicBean> list = new ArrayList<MusicBean>();
		// 遍历获取值
		while (c.moveToNext()) {
			String musicName = c.getString(0);
			String artist = c.getString(2);
			int songid = c.getInt(5);
			String musicPath = c.getString(6);
			int album_id =  c.getInt(7);
			
			
			MusicBean mb = new MusicBean();
			mb.setMusicName(musicName);
			mb.setMusicPath(musicPath);
			mb.setArtist(artist);
			mb.setSong_id(songid);
			mb.setAlbum_id(album_id);
			
			list.add(mb);
			Log.d(LOG_TAG, "音乐：" + musicName + ",地址:" + musicPath);
		}
		return list;
	}
	public static final String SEVER_HOST = "http://192.168.56.1:8080";
	public static final String URL_GETHOT_LIST =SEVER_HOST+"/CMS72/html/music!toJsonList";
		
	
	public static void loadWebImage(String url,ImageView iv){
		HttpUtil.loadImage(SEVER_HOST+url, iv);
	}
	public static void loadWebImage(String url,ImageView iv,IHttpImageListerner listerner){
		HttpUtil.loadImage(SEVER_HOST+url, iv,listerner);
	}
	
	
	public static void getMusicFromWeb(final IHttpCallBack callback){
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				//更新界面
				callback.onSuccess((String)msg.obj);
			}
		};
		final List<MusicBean> list = new ArrayList<MusicBean>();
		Thread thread = new Thread(){
			public void run() {
				String result= HttpUtil.get(URL_GETHOT_LIST);
				
				Message msg = new Message();
				msg.obj =result;
				handler.sendMessage(msg);
			};
		};
		thread.start();
		
	}
}
