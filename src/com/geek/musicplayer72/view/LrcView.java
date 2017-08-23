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
	// 歌词类列表
	private List<LrcContent> mLrcList;
	// 用于存放每一句歌词
	private LrcContent mLrcContent;
	int mCurrentPosition = 10;//存放当前
	
	Paint mPaint;// 非高亮部分
	int darkSize = 25;
	int darkColor = Color.WHITE;
	
	Paint mPathPaint;
	
	int lightSize = 25;
	int lightColor = Color.YELLOW;
	
	private float mX = 0;
	private float mY = 50;
	
	private float off_y= 30;//歌词之间的间隔
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
				//已经是最后一句了
				//Toast.makeText(context, text, duration)
			}else if(mCurrentPosition == mLrcList.size() - 2){
				if(mp.getCurrentPosition() >= mLrcList.get(mCurrentPosition+1).getLrc_time()){
					mCurrentPosition++;
				}
			}else if(mp.getCurrentPosition() >= mLrcList.get(mCurrentPosition+1).getLrc_time()
					&& mp.getCurrentPosition() < mLrcList.get(mCurrentPosition+2).getLrc_time()
					){
				mCurrentPosition++;
			}else{//说明时间不对，需要修正
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
		
		// 非高亮部分
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(darkSize);
		mPaint.setColor(darkColor);
		mPaint.setTypeface(Typeface.SERIF);
		mPaint.setTextAlign(Paint.Align.CENTER);
		// 高亮部分 当前歌词
		mPathPaint = new Paint();
		mPathPaint.setAntiAlias(true);
		mPathPaint.setTextSize(lightSize);
		mPathPaint.setColor(lightColor);
		mPathPaint.setTypeface(Typeface.SANS_SERIF);
		mPathPaint.setTextAlign(Paint.Align.CENTER);
		
		
		
		
		
		setText("没有找到歌词");
	}
	
	public void updateIndex(){//更新时间

		//如果已经大于倒数第二句那么就是最后一句,并且不需要继续查询了
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
		/////绘制当前播放歌词
		canvas.drawText(mLrcList.get(mCurrentPosition).getLrc(),
				mX, 
				mY, mPathPaint);
		/////绘制上面的歌词
		for(int i= mCurrentPosition-1;i>0;i--){
			canvas.drawText(mLrcList.get(i).getLrc(),
					mX, 
					mY - (mCurrentPosition - i) *(darkSize+off_y), mPaint);
		}
		/////绘制下面的歌词
		for(int i= mCurrentPosition+1;i<mLrcList.size();i++){
			canvas.drawText(mLrcList.get(i).getLrc(),
					mX, 
					mY + (i-mCurrentPosition)*(darkSize+off_y), mPaint);
		}
		
		
	}
	
	public void setLRCPath(String host , String path) {
		String result;
		if(path == null || path.equals("")){
			result ="没有找到歌词...";
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
	 * 读取歌词文件的内容
	 */
	private String readLRC(final String song_path) {
		// 歌词文件与mp3文件在同一目录，并且名字要相同
		//File f = new File(song_path);
		//修改成网络的
		final StringBuilder stringBuilder = new StringBuilder();
		
		
		
		if (song_path==null ||song_path.equals("")) {
			stringBuilder.append("未找到歌词文件");
			return stringBuilder.toString();
		}else{
			stringBuilder.append("正在寻找歌词...");
		}
		
		
		AsyncTask task = new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				try {
					URL url = new URL(song_path);
					
					InputStream fis = url.openStream();
					// 以utf-8方式解析文字
					InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
					BufferedReader br = new BufferedReader(isr);
					String s = "";
					while ((s = br.readLine()) != null) {
						// 替换字符
						s = s.replace("[", "");
						s = s.replace("]", "@");
						// 分离"@"字符
						String splitLrc_data[] = s.split("@");
						if (splitLrc_data.length > 1) {
							mLrcContent.setLrc(splitLrc_data[1]);
							// 处理歌词取得歌曲时间
							int LrcTime = TimeStr(splitLrc_data[0]);
							// 设置歌词时间
							mLrcContent.setLrc_time(LrcTime);
							// 添加进列表数组
							mLrcList.add(mLrcContent);
							// 创建对象
							mLrcContent = new LrcContent();
						}
					}
					br.close();
					isr.close();
					fis.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					stringBuilder.append("未找到歌词文件");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					stringBuilder.append("木有读取到歌词啊！");
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
	 * 解析歌曲时间处理类
	 */
	private int TimeStr(String timeStr) {
		
	
		
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");
		// 取得时间信息存放到数组中
		String timeData[] = timeStr.split("@");

		// 分离出分、秒并转换为整型
//		if(timeData[0].equals("00")){
//			
//			Integer.parseInt(timeData[0]);
//		};
		
		int minute = Integer.parseInt(timeData[0].substring(1));
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);

		// 计算上一行与下一行的时间转换为毫秒数
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		return currentTime;
	}


	/**
	 * 获得歌词和时间并返回的类
	 */
	public class LrcContent {
		private String Lrc;
		private int Lrc_time;

		// 获得歌词
		public String getLrc() {
			return Lrc;
		}

		// 设置歌词
		public void setLrc(String lrc) {
			Lrc = lrc;
		}

		// 获得歌词对应的时间
		public int getLrc_time() {
			return Lrc_time;
		}

		// 设置歌词对应的时间
		public void setLrc_time(int lrc_time) {
			Lrc_time = lrc_time;
		}
	}
}
