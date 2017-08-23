package com.geek.musicplayer72.activity;

import com.geek.musicplayer72.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class BaseBarActivity extends FragmentActivity{
	
	FrameLayout fl_content;
	FrameLayout fl_actionbar;
	
	LinearLayout ll_back;
	LinearLayout ll_titleView;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		super.setContentView(R.layout.activity_base);
		fl_content = (FrameLayout) findViewById(R.id.fl_content);
		fl_actionbar = (FrameLayout) findViewById(R.id.fl_actionbar);
		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		ll_titleView = (LinearLayout) findViewById(R.id.ll_title);
		ll_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	public void setActionBarTitleView(View view){
		ll_titleView.removeAllViews();
		ll_titleView.addView(view);
	}
	
	
	public void setActionBarBackGroudColor(int color){
		fl_actionbar.setBackgroundColor(color);
	}
	
	public void setActionBar(int layoutResID){
		fl_actionbar.removeAllViews();
		View view = getLayoutInflater().inflate(layoutResID, null);
		fl_actionbar.addView(view);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		//1，将之前的内容移除
		fl_content.removeAllViews();
		//2,创建新的内容部分
		View view = getLayoutInflater().inflate(layoutResID, null);
		fl_content.addView(view);
	}
	
	
}
