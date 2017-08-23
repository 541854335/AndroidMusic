package com.geek.musicplayer72.bean;

import java.util.ArrayList;
import java.util.List;

public class MusicGroup {
	String name;//БъЬт
	List<MusicBean> children = new ArrayList<MusicBean>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<MusicBean> getChildren() {
		return children;
	}
	public void setChildren(List<MusicBean> children) {
		this.children = children;
	}
	
	
}
