package com.geek.musicplayer72.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek.musicplayer72.manager.MusicManager;
import com.geek.musicplayer72.utils.StreamUtil;

public class HttpUtil {
	
	public static String get(String urlpath){
		//1,创建一个Url
		try {
			URL url = new URL(urlpath);
			//2,打开一个链接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//3,打开流
			InputStream in = conn.getInputStream();
			
			String result = StreamUtil.toString(in);
			
			return result;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void loadImage(final String imageUrl,final ImageView iv,final IHttpImageListerner listerner){
		
		AsyncTask task = new AsyncTask() {
			
			//工作线程
			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				URL url;
				Bitmap bitmap = null;
				try {
					url = new URL(imageUrl);
					InputStream is = url.openStream();
					
					bitmap = BitmapFactory.decodeStream(is);
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				
				return bitmap;
			}
			
			//更新ui方法
			@Override
			protected void onPostExecute(Object result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				Bitmap bm = (Bitmap)result;
				if(iv != null){
					iv.setImageBitmap(bm);
				}
				if(listerner != null){
					listerner.onLoadImage(bm);
				}
			}
		};
	//执行下载的任务
	task.execute();
	}

	
	public static void loadImage(final String imageUrl,final ImageView iv){
		loadImage(imageUrl,iv,null);
	}
}
