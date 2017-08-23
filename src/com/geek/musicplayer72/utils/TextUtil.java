package com.geek.musicplayer72.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

public class TextUtil {
//	public static String getFirstUpperPinYin(char ch){
//		String[] pinyins = PinyinHelper.toHanyuPinyinStringArray('邓');
//		char first = pinyins[0].charAt(0);
//		String f = String.valueOf(first);
//	
//		return f.toLowerCase();
//	}
	
	public static char getFirstAscPinYin(char ch){
		String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(ch);
		
		if(pinyins == null){//说明是英文状态
			return String.valueOf(ch).toLowerCase().charAt(0);
		}
		
		char first = pinyins[0].charAt(0);
	
		return first;
	}
	
	public static String getTime(long millSecond){
		 long second = millSecond/1000%60;
		 long minute = millSecond/1000/60;
		 
		 return String.format("%02d:%02d",minute,second);
	}
}
