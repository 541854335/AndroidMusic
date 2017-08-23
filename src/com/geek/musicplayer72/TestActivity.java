package com.geek.musicplayer72;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.geek.musicplayer72.activity.BaseBarActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class TestActivity extends BaseBarActivity {
	TextView tv_content;
	WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		tv_content = (TextView) findViewById(R.id.tv_content);
		webView = (WebView) findViewById(R.id.webView);
		
		
		/*webView.loadUrl(loginpath);*/
	}
	
	//创建一个handler
	Handler handler = new Handler(){
		//处理消息的方法，会在Activity的ui线程中
		public void handleMessage(Message msg) {
			//获取消息中的内容
			String content = (String) msg.obj;
			
			//显示内容
			tv_content.setText(Html.fromHtml(content));
		};
	};
	
	
	public void sendHttp(View v){
		Thread thread = new Thread(){
			public void run() {
				sendHttp();//是一个阻塞方法
			};
		};
		thread.start();
	}
	String loginpath = "http://192.168.16.111:8080/CMS72/html/login.html";
	public void sendHttp(){
		//1,创建一个URL地址
				URL url;
				try {
					url = new URL(loginpath);
					//2，打开连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					
					//3,设置Http请求的头的内容，默认是get请求
					//conn.setRequestMethod(method);
					
					//4,获取一个流
					  //需要在发送body中写入内容的话
					//	conn.getOutputStream();
					
					//5获取返回的内容
					//获取返回的头信息
					//conn.getHeaderField(pos);  
					//获取返回的body信息
					InputStream is = conn.getInputStream(); 
					
					//使用字符流
					StringBuffer sb = new StringBuffer();
					
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String line;
					
					//readline是阻塞方法
					 while( (line = br.readLine())!=null){
						sb.append(line);
					 }
				    Log.d("http", sb.toString());
					is.close();
					
					//显示内容
					tv_content.setText(sb.toString());
					
					//使用Handler将数据方式到主线程
					Message msg = new Message();
					//将数据放入到消息中
					msg.obj = sb.toString();
					//发送消息
					handler.sendMessage(msg);
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
}
