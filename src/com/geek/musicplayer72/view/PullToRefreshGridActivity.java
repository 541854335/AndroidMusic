package com.geek.musicplayer72.view;

import java.util.LinkedList;

import com.geek.musicplayer72.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class PullToRefreshGridActivity extends Activity {
	private LinkedList<String> mListItems;
	private PullToRefreshGridView mPullRefreshListView;
	private ArrayAdapter<String> mAdapter;

	private int mItemCount = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pulltorefreshgrid);
		// 得到控件
		mPullRefreshListView =  (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);

		// 初始化数据和数据源
		initDatas();

		mAdapter = new ArrayAdapter<String>(this, R.layout.weather_textview,
				R.id.id_grid_item_text, mListItems);
		mPullRefreshListView.setAdapter(mAdapter);
		
	}

	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
											// the list here.
		new GetDataTask().execute();
	}

	private void initDatas() {
		mListItems = new LinkedList<String>();

		for (int i = 0; i < mItemCount; i++) {
			mListItems.add(i + "");
		}
	}

	private class GetDataTask extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			 mListItems.add("" + mItemCount++);  
	            mAdapter.notifyDataSetChanged();  
	            // Call onRefreshComplete when the list has been refreshed.  
	            mPullRefreshListView.onRefreshComplete();  
		}
	}
}