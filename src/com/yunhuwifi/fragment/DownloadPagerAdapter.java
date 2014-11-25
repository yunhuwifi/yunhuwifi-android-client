package com.yunhuwifi.fragment;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DownloadPagerAdapter extends FragmentPagerAdapter {

	private Fragment[] fragments;

	public DownloadPagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new Fragment[2];
		fragments[0] = new DownloadedFragment();
		fragments[1] = new DownloadsFragment();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments[position];
	}

	@Override
	public int getCount() {
		return fragments.length;
	}
}