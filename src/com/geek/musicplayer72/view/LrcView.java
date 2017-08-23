package com.geek.musicplayer72.view;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class LrcView extends TextView {
	// ������б�
	private List<LrcContent> mLrcList;
	// ���ڴ��ÿһ����
	private LrcContent mLrcContent;
	int mCurrentPosition = 10;//��ŵ�ǰ
	
	Paint mPaint;// �Ǹ�������
	int darkSize = 25;
	int darkColor = Color.WHITE;
	
	Paint mPathPaint;
	
	int lightSize = 25;
	int lightColor = Color.YELLOW;
	
	private float mX = 0;
	private float mY = 50;
	
	private float off_y= 30;//���֮��ļ��
	MediaPlayer mp;
	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	
	}
	
	Handler handler = new Handler();
	Runnable r = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			if(mCurrentPosition == mLrcList.size() -1){
				//�Ѿ������һ����
				//Toast.makeText(context, text, duration)
			}else if(mCurrentPosition == mLrcList.size() - 2){
				if(mp.getCurrentPosition() >= mLrcList.get(mCurrentPosition+1).getLrc_time()){
					mCurrentPosition++;
				}
			}else if(mp.getCurrentPosition() >= mLrcList.get(mCurrentPosition+1).getLrc_time()
					&& mp.getCurrentPosition() < mLrcList.get(mCurrentPosition+2).getLrc_time()
					){
				mCurrentPosition++;
			}else{//˵��ʱ�䲻�ԣ���Ҫ����
				updateIndex();
			}
			
			
			
			invalidate();
			handler.postDelayed(r, 100);
		}
	}; 
	public void setMediaPlayer(MediaPlayer m){
		mp = m;
	}
	public void start(){
		handler.sendEmptyMessageDelayed(0, 100);
	}
	public void stop(){
		handler.removeMessages(0);
	}
	private void init() {
		mCurrentPosition = 0;
		mLrcContent = new LrcContent();
		mLrcList = new ArrayList<LrcContent>();
		this.setGravity(Gravity.CENTER);
		
		// �Ǹ�������
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(darkSize);
		mPaint.setColor(darkColor);
		mPaint.setTypeface(Typeface.SERIF);
		mPaint.setTextAlign(Paint.Align.CENTER);
		// �������� ��ǰ���
		mPathPaint = new Paint();
		mPathPaint.setAntiAlias(true);
		mPathPaint.setTextSize(lightSize);
		mPathPaint.setColor(lightColor);
		mPathPaint.setTypeface(Typeface.SANS_SERIF);
		mPathPaint.setTextAlign(Paint.Align.CENTER);
		
		
		
		
		
		setText("û���ҵ����");
	}
	
	public void updateIndex(){//����ʱ��

		//����Ѿ����ڵ����ڶ�����ô�������һ��,���Ҳ���Ҫ������ѯ��
		if(mp.getCurrentPosition() > mLrcList.get(mLrcList.size()-2).getLrc_time()){
			mCurrentPosition = mLrcList.size()-1;
			return;
		}
		for(int i = 0;i<mLrcList.size();i++)
		{
			if(mp.getCurrentPosition() >= mLrcList.get(i).getLrc_time()
					&& mp.getCurrentPosition() < mLrcList.get(i+1).getLrc_time()
					){
				mCurrentPosition = i;
			}
		}
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		mX = w*0.5f;
		mY = h*0.5f;
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		
		if(mLrcList.size() == 0){
			super.onDraw(canvas);
			return;
		}
		/////���Ƶ�ǰ���Ÿ��
		canvas.drawText(mLrcList.get(mCurrentPosition).getLrc(),
				mX, 
				mY, mPathPaint);
		/////��������ĸ��
		for(int i= mCurrentPosition-1;i>0;i--){
			canvas.drawText(mLrcList.get(i).getLrc(),
					mX, 
					mY - (mCurrentPosition - i) *(darkSize+off_y), mPaint);
		}
		/////��������ĸ��
		for(int i= mCurrentPosition+1;i<mLrcList.size();i++){
			canvas.drawText(mLrcList.get(i).getLrc(),
					mX, 
					mY + (i-mCurrentPosition)*(darkSize+off_y), mPaint);
		}
		
		
	}
	
	public void setLRCPath(String host , String path) {
		String result;
		if(path == null || path.equals("")){
			result ="û���ҵ����...";
		}else{
			result = readLRC(host+path);
		}
		
		
		if(mLrcList.size() == 0){
			setTextSize(lightSize);
			setTextColor(lightColor);
			setText(result);
			invalidate();
		}
	}

	/**
	 * ��ȡ����ļ�������
	 */
	private String readLRC(final String song_path) {
		// ����ļ���mp3�ļ���ͬһĿ¼����������Ҫ��ͬ
		//File f = new File(song_path);
		//�޸ĳ������
		final StringBuilder stringBuilder = new StringBuilder();
		
		
		
		if (song_path==null ||song_path.equals("")) {
			stringBuilder.append("δ�ҵ�����ļ�");
			return stringBuilder.toString();
		}else{
			stringBuilder.append("����Ѱ�Ҹ��...");
		}
		
		
		AsyncTask task = new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				try {
					URL url = new URL(song_path);
					
					InputStream fis = url.openStream();
					// ��utf-8��ʽ��������
					InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
					BufferedReader br = new BufferedReader(isr);
					String s = "";
					while ((s = br.readLine()) != null) {
						// �滻�ַ�
						s = s.replace("[", "");
						s = s.replace("]", "@");
						// ����"@"�ַ�
						String splitLrc_data[] = s.split("@");
						if (splitLrc_data.length > 1) {
							mLrcContent.setLrc(splitLrc_data[1]);
							// ������ȡ�ø���ʱ��
							int LrcTime = TimeStr(splitLrc_data[0]);
							// ���ø��ʱ��
							mLrcContent.setLrc_time(LrcTime);
							// ��ӽ��б�����
							mLrcList.add(mLrcContent);
							// ��������
							mLrcContent = new LrcContent();
						}
					}
					br.close();
					isr.close();
					fis.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					stringBuilder.append("δ�ҵ�����ļ�");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					stringBuilder.append("ľ�ж�ȡ����ʰ���");
				}
				
				return stringBuilder.toString();
			}
			@Override
			protected void onPostExecute(Object result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				if(mLrcList.size() == 0){
					setTextSize(lightSize);
					setTextColor(lightColor);
					setText((String)result);
					invalidate();
				}else{

					handler.post(r);
				}
			}
		};
		
		task.execute();
		
		return stringBuilder.toString();
	}

	/**
	 * ��������ʱ�䴦����
	 */
	private int TimeStr(String timeStr) {
		
	
		
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");
		// ȡ��ʱ����Ϣ��ŵ�������
		String timeData[] = timeStr.split("@");

		// ������֡��벢ת��Ϊ����
//		if(timeData[0].equals("00")){
//			
//			Integer.parseInt(timeData[0]);
//		};
		
		int minute = Integer.parseInt(timeData[0].substring(1));
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);

		// ������һ������һ�е�ʱ��ת��Ϊ������
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		return currentTime;
	}


	/**
	 * ��ø�ʺ�ʱ�䲢���ص���
	 */
	public class LrcContent {
		private String Lrc;
		private int Lrc_time;

		// ��ø��
		public String getLrc() {
			return Lrc;
		}

		// ���ø��
		public void setLrc(String lrc) {
			Lrc = lrc;
		}

		// ��ø�ʶ�Ӧ��ʱ��
		public int getLrc_time() {
			return Lrc_time;
		}

		// ���ø�ʶ�Ӧ��ʱ��
		public void setLrc_time(int lrc_time) {
			Lrc_time = lrc_time;
		}
	}
}
