package com.geek.musicplayer72.bean;

public class MusicPosition {
	int groupPostition;
	int chirldPostiton;
	
	//是否存在一个位置
	public boolean haveMusicPosition(){
		if(groupPostition>-1 && chirldPostiton >-1){
			return true;
		}
		return false;
	}
	
	public MusicPosition(){
		groupPostition = -1;
		chirldPostiton = -1;
	}
	public MusicPosition(int groupPostition,int chirdPostiton){
		this.groupPostition = groupPostition;
		this.chirldPostiton = chirdPostiton;
	}
	
	public int getGroupPostition() {
		return groupPostition;
	}
	public void setGroupPostition(int groupPostition) {
		this.groupPostition = groupPostition;
	}
	public int getChirdPostiton() {
		return chirldPostiton;
	}
	public void setChirldPostiton(int chirldPostiton) {
		this.chirldPostiton = chirldPostiton;
	}
	
	public boolean equals(MusicPosition mp) {
		// TODO Auto-generated method stub
		if(this.chirldPostiton == mp.getChirdPostiton() 
				&& this.groupPostition == mp.getGroupPostition()){
			return true;
		}
		
		return false;
	}
	
	
	
}
