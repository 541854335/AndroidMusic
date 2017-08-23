package com.geek.musicplayer72.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.geek.musicplayer72.R;
import com.geek.musicplayer72.activity.MainActivity;
import com.geek.musicplayer72.bean.MusicBean;
import com.geek.musicplayer72.bean.MusicGroup;
import com.geek.musicplayer72.bean.MusicPosition;
import com.geek.musicplayer72.fragment.listener.MuiscFragmentInfo;
import com.geek.musicplayer72.manager.MusicManager;
import com.geek.musicplayer72.utils.ActivityUtil;
import com.geek.musicplayer72.utils.AppConfig;
import com.geek.musicplayer72.utils.TextUtil;
import com.geek.musicplayer72.view.AssortView;
import com.geek.musicplayer72.view.AssortView.OnTouchAssortListener;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MusicFragment extends Fragment implements AppConfig  {
	ExpandableListView elv_music;
	List<MusicGroup> musicGroupList;
	AssortView av_pinying;
	View rootView;
	PopupWindow popupWindow;
	
	
	MuiscFragmentInfo musifFragmentInfo;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.frag_music, null);
		
		//赋值
		musifFragmentInfo = (MuiscFragmentInfo) getActivity();
		
	    elv_music = (ExpandableListView) rootView.findViewById(R.id.elv_music);
	    av_pinying = (AssortView) rootView.findViewById(R.id.av_pinying);
		
	    initPopupWindow();
	    
	    //设置控件的点击事件
	    av_pinying.setOnTouchAssortListener(new OnTouchAssortListener() {
			
	    	//抬起的时候
			@Override
			public void onTouchAssortUP() {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
			//选中的选中
			@Override
			public void onTouchAssortListener(String s) {
				//修改内容
				tv_letter.setText(s);
				//显示对话框
				popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
				//选择被选中的位置
				for (int i = 0; i < musicGroupList.size(); i++) {
					if(s.equals(musicGroupList.get(i).getName())){
						//设置group显示的位置
						elv_music.setSelectedGroup(i);
						break;
					}
				}
				
			}
		});
	    
	    elv_music.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				//找到点击的音乐的实体类
				MusicBean mb = musicGroupList.get(groupPosition).getChildren().get(childPosition);
				
				//保存选中的
				
				//发送一条广播
				//ActivityUtil.sendMusicBroadCast(getActivity(), MUISC_START, mb);
				
				
				//修改Activity下面音乐的信息
				if(musifFragmentInfo != null){
					//MainActivity activity = (MainActivity) getActivity();
					musifFragmentInfo.onMusicSelected(mb,new MusicPosition(groupPosition,childPosition));
				}
				return true;
			}
		});
	    
	    
		initMusicList();
		
		return rootView;
	}
	TextView tv_letter;
	//初始化弹出对话框
	private void initPopupWindow() {
		popupWindow = new PopupWindow(getActivity());
	    tv_letter = new TextView(getActivity());
		//设置popupWindows的大小
		popupWindow.setWidth(100);
		popupWindow.setHeight(100);
		
		tv_letter.setTextSize(30);
		tv_letter.setGravity(Gravity.CENTER);
		tv_letter.setTextColor(Color.WHITE);
		popupWindow.setContentView(tv_letter);
	}

	
	private void initMusicList() {
		//1,数据源
		musicGroupList = MusicManager.getPinYinOrderList(getActivity().getContentResolver());
		
		//2,适配器
		elv_music.setAdapter(new BaseExpandableListAdapter() {
			
			
			@Override
			public boolean isChildSelectable(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				
				if(convertView == null){
					convertView = getActivity().getLayoutInflater().inflate(R.layout.elv_music_group, null);
					
				}
				TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				 
				//设置当前group消费事件
				convertView.setClickable(true);
				
				MusicGroup mg = musicGroupList.get(groupPosition);
				
				tv_name.setText(mg.getName());
				
				
				return convertView;
			}
			
			@Override
			public long getGroupId(int groupPosition) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getGroupCount() {
				// TODO Auto-generated method stub
				return musicGroupList.size();
			}
			
			@Override
			public Object getGroup(int groupPosition) {
				// TODO Auto-generated method stub
				return musicGroupList.get(groupPosition);
			}
			
			
			
			@Override
			public int getChildrenCount(int groupPosition) {
				// TODO Auto-generated method stub
				return musicGroupList.get(groupPosition).getChildren().size();
			}
			
			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if(convertView == null){
					convertView = getActivity().getLayoutInflater().inflate(R.layout.elv_music_child, null);
					
				}
				
				
				TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				TextView tv_artist = (TextView) convertView.findViewById(R.id.tv_artist);
				
				MusicBean mb = musicGroupList.get(groupPosition).getChildren().get(childPosition);
				
				tv_name.setText(mb.getMusicName());
				tv_artist.setText(mb.getArtist());
				
				return convertView;
			}
			
			@Override
			public long getChildId(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getChild(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return null;
			}
		
		});
		
		
		//3，展开所有的子元素
		for (int i = 0; i <musicGroupList.size() ; i++) {
			elv_music.expandGroup(i);
		}
		
		
		//elv_music.setSelectedGroup(groupPosition);
	}
}
