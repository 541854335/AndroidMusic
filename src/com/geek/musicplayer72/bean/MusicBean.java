package com.geek.musicplayer72.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicBean implements Parcelable {
	String musicName;
	String musicPath;
	String artist;
	String musicImageUrl;
	String musicLRCUrl;
	
	public String getMusicLRCUrl() {
		return musicLRCUrl;
	}
	public void setMusicLRCUrl(String musicLRCUrl) {
		this.musicLRCUrl = musicLRCUrl;
	}

	int flag; //0代表网络，1代表本地
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public static Parcelable.Creator<MusicBean> getCreator() {
		return CREATOR;
	}
	public String getMusicImageUrl() {
		return musicImageUrl;
	}
	public void setMusicImageUrl(String musicImageUrl) {
		this.musicImageUrl = musicImageUrl;
	}

	int song_id;
	int album_id;
	
	public int getSong_id() {
		return song_id;
	}
	public void setSong_id(int song_id) {
		this.song_id = song_id;
	}
	
	public int getAlbum_id() {
		return album_id;
	}
	public void setAlbum_id(int album_id) {
		this.album_id = album_id;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getMusicName() {
		return musicName;
	}
	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}
	public String getMusicPath() {
		return musicPath;
	}
	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}
	
	
	
	
	public int describeContents() {
        return 0;
    }
	//将数据写入
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(musicName);
        out.writeString(musicPath);
        out.writeString(artist);
        out.writeString(musicImageUrl);
        
        out.writeString(musicLRCUrl);
        
        out.writeInt(flag);
        out.writeInt(song_id);
        out.writeInt(album_id);
    }

    public static final Parcelable.Creator<MusicBean> CREATOR
            = new Parcelable.Creator<MusicBean>() {
        public MusicBean createFromParcel(Parcel in) {
            return new MusicBean(in);
        }

        public MusicBean[] newArray(int size) {
            return new MusicBean[size];
        }
    };
    
    //读取数据，有参数的构造方法
    private MusicBean(Parcel in) {
        
        musicName = in.readString();
    	musicPath = in.readString();
    	artist = in.readString();
    	musicImageUrl = in.readString();
    	
    	musicLRCUrl =in.readString();
    	
    	flag    = in.readInt();
    	song_id = in.readInt();
    	album_id = in.readInt();
     
    }
    
    public MusicBean(){};
}
