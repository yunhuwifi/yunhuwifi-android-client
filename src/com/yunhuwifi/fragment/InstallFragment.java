
package com.yunhuwifi.fragment;

import java.util.ArrayList;
import java.util.List;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.activity.PluginRunActivity;
import com.yunhuwifi.models.InstallItem;
import com.yunhuwifi.view.InstallItemAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class InstallFragment extends Fragment {
	private GridView gv_menu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_installapp, container,
				false);
		findView(view);
		init();
		return view;
	}

	private void findView(View v) {
		gv_menu = (GridView) v.findViewById(R.id.gv_menu);
		this.gv_menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						PluginRunActivity.class);

				startActivity(intent);
			}

		});
	}

	private void init() {
		List<InstallItem> menus = new ArrayList<InstallItem>();
		menus.add(new InstallItem(R.drawable.launcher, "海量手机资源", "电子书 音乐 壁纸 铃声"));
		menus.add(new InstallItem(R.drawable.launcher, "优酷", "可离线下载"));
		menus.add(new InstallItem(R.drawable.launcher, "迅雷", "uyuf"));
		menus.add(new InstallItem(R.drawable.launcher, "智能唤醒", "utf"));
		menus.add(new InstallItem(R.drawable.launcher, "文件管理", "utf"));
		menus.add(new InstallItem(R.drawable.launcher, "云加速", "futf"));
		menus.add(new InstallItem(R.drawable.launcher, "BT资源", "utf"));
		menus.add(new InstallItem(R.drawable.launcher, "远程控制", "cuttc"));
		menus.add(new InstallItem(R.drawable.launcher, "智能唤醒", "utfc"));
		menus.add(new InstallItem(R.drawable.launcher, "文件管理", "tuf"));
		// 计算margin
		int margin = (int) (getResources().getDisplayMetrics().density * 14 * 13 / 9);
		InstallItemAdapter adapter = new InstallItemAdapter(getActivity(),
				menus, margin);
		gv_menu.setAdapter(adapter);

	}
}