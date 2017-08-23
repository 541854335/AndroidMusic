package com.geek.musicplayer72.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.geek.musicplayer72.R;
import com.geek.musicplayer72.adapter.HotGVAdapter;
import com.geek.musicplayer72.bean.MusicBean;
import com.geek.musicplayer72.bean.MusicPosition;
import com.geek.musicplayer72.fragment.listener.MuiscFragmentInfo;
import com.geek.musicplayer72.http.IHttpCallBack;
import com.geek.musicplayer72.manager.MusicManager;
import com.geek.musicplayer72.utils.AppConfig;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class DiscoverFragment extends Fragment implements AppConfig {
	GridView gv_hot;
	PullToRefreshGridView mPullRefreshListView;
	View rootView;
	HotGVAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.frag_discover, null);
		/* gv_hot = (GridView) rootView.findViewById(R.id.gv_hot); */
		musifFragmentInfo = (MuiscFragmentInfo) getActivity();
		mPullRefreshListView = (PullToRefreshGridView) rootView
				.findViewById(R.id.gv_hot);

		initGridView();

		return rootView;
	}

	MuiscFragmentInfo musifFragmentInfo;

	private void initGridView() {

		MusicManager.getMusicFromWeb(new IHttpCallBack() {

			@Override
			public void onSuccess(String result) {
				// 1,获取数据
				List<MusicBean> list = new ArrayList<MusicBean>();

				// 将result转换成List

				try {
					JSONArray array = new JSONArray(result);
					for (int i = 0; i < array.length(); i++) {
						JSONObject jobject = array.getJSONObject(i);

						MusicBean musicBean = new MusicBean();

						musicBean.setSong_id((int) jobject
								.getLong(MusicManager.COL_ID));
						musicBean.setMusicName(jobject
								.getString(MusicManager.COL_MUISCNAME));
						musicBean.setArtist(jobject
								.getString(MusicManager.COL_ARTIST));
						musicBean.setMusicPath(jobject
								.getString(MusicManager.COL_MUSICURL));
						musicBean.setMusicImageUrl(jobject
								.getString(MusicManager.COL_IMAGEURL));
						musicBean.setMusicLRCUrl(jobject
								.getString(MusicManager.COL_MUISCLRCURL));

						// 设置属性是从网络获取的
						musicBean.setFlag(MUSIC_FROM_WEB);

						list.add(musicBean);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 获取网络的数据
				MusicManager.musicListFromWeb = list;

				// 2，创建适配器
				adapter = new HotGVAdapter(list, getActivity());

				// 3，绑定适配器
				mPullRefreshListView.setAdapter(adapter);

				// 4,实现点击事件
				mPullRefreshListView
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								HotGVAdapter gvAdapter = (HotGVAdapter) parent
										.getAdapter();
								MusicBean mb = (MusicBean) gvAdapter
										.getItem(position);
								// 设置音乐来源
								mb.setFlag(MUSIC_FROM_WEB);
								mb.setMusicPath(mb.getMusicPath());
								// 保存当前位置

								// 修改Activity下面音乐的信息
								if (musifFragmentInfo != null) {
									// MainActivity activity = (MainActivity)
									// getActivity();

									musifFragmentInfo.onMusicSelected(mb,
											new MusicPosition(0, position));
								}
							}
						});
				mPullRefreshListView
						.setOnRefreshListener(new OnRefreshListener<GridView>() {

							@Override
							public void onRefresh(
									 PullToRefreshBase<GridView> refreshView) {
				
								new AsyncTask<Void, Void, Void>() {
									protected Void doInBackground(
											Void... params) {
										try {
											Thread.sleep(1000);
										} catch (Exception e) {
											e.printStackTrace();
										}

										return null;
									}

									@Override
									protected void onPostExecute(Void result) {
										adapter.notifyDataSetChanged();
										mPullRefreshListView
												.onRefreshComplete();
										initGridView();
									}
								}.execute();

							}

						});
			}
		});

	}

}
