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
	
	//����һ��handler
	Handler handler = new Handler(){
		//������Ϣ�ķ���������Activity��ui�߳���
		public void handleMessage(Message msg) {
			//��ȡ��Ϣ�е�����
			String content = (String) msg.obj;
			
			//��ʾ����
			tv_content.setText(Html.fromHtml(content));
		};
	};
	
	
	public void sendHttp(View v){
		Thread thread = new Thread(){
			public void run() {
				sendHttp();//��һ����������
			};
		};
		thread.start();
	}
	String loginpath = "http://192.168.16.111:8080/CMS72/html/login.html";
	public void sendHttp(){
		//1,����һ��URL��ַ
				URL url;
				try {
					url = new URL(loginpath);
					//2��������
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					
					//3,����Http�����ͷ�����ݣ�Ĭ����get����
					//conn.setRequestMethod(method);
					
					//4,��ȡһ����
					  //��Ҫ�ڷ���body��д�����ݵĻ�
					//	conn.getOutputStream();
					
					//5��ȡ���ص�����
					//��ȡ���ص�ͷ��Ϣ
					//conn.getHeaderField(pos);  
					//��ȡ���ص�body��Ϣ
					InputStream is = conn.getInputStream(); 
					
					//ʹ���ַ���
					StringBuffer sb = new StringBuffer();
					
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String line;
					
					//readline����������
					 while( (line = br.readLine())!=null){
						sb.append(line);
					 }
				    Log.d("http", sb.toString());
					is.close();
					
					//��ʾ����
					tv_content.setText(sb.toString());
					
					//ʹ��Handler�����ݷ�ʽ�����߳�
					Message msg = new Message();
					//�����ݷ��뵽��Ϣ��
					msg.obj = sb.toString();
					//������Ϣ
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
