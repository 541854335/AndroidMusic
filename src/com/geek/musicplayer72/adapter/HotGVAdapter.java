package com.geek.musicplayer72.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.geek.musicplayer72.R;
import com.geek.musicplayer72.bean.MusicBean;
import com.geek.musicplayer72.http.HttpUtil;
import com.geek.musicplayer72.manager.MusicManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HotGVAdapter extends BaseAdapter{
	
	List<MusicBean> mList;
	Context mContext;
	public HotGVAdapter(List<MusicBean> list,Context ctx){
		mList = list;
		mContext = ctx;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.gv_hot_item, null);
		}
		
		final MusicBean mb = mList.get(position);
		
		TextView tv_detail = (TextView) convertView.findViewById(R.id.tv_music_detail);
	     ImageView iv = (ImageView) convertView.findViewById(R.id.iv_music_icon);
		
		
		//从网路下载图片到内存中
		HttpUtil.loadImage(MusicManager.SEVER_HOST+mb.getMusicImageUrl(), iv);

		tv_detail.setText(mb.getArtist());
		
		return convertView;
	}

}
