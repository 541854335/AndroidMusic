package com.geek.musicplayer72.test;

import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;

import com.geek.musicplayer72.manager.MusicManager;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.test.AndroidTestCase;

public class TestDataCase extends AndroidTestCase{
//	public void atestMusicData(){
//		//·¢ËÍ¹ã²¥Ë¢ÐÂ¸èÇú
//		getContext().sendBroadcast(new Intent(
//				Intent.ACTION_MEDIA_MOUNTED,
//				Uri.parse("file://"
//						+ Environment
//								.getExternalStorageDirectory()
//								.getAbsolutePath())));
//		
//		ContentResolver cr = getContext().getContentResolver();
//		List list = MusicManager.getListFromLocal(cr);
//	
//		System.out.println(list.size());
//	}
//	
//	public void testPinying(){
//		String[] pinyins = PinyinHelper.toHanyuPinyinStringArray('G');
//		
//		
//		char first = pinyins[0].charAt(0);
//		
//		int pos = first-97;
//		
//		String f = String.valueOf(first);
//		System.out.println(f);
//	}
	public void testNum(){
		String a="00";
		double d = Double.parseDouble(a);
		int i = Integer.parseInt(a);
		System.out.println(d+"  "+ i);
	}
}
