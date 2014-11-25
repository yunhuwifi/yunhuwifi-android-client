package com.yunhuwifi.fragment;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppPagerAdapter extends FragmentPagerAdapter {

	private Fragment[] fragments;

	public AppPagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new Fragment[2];
		fragments[0] = new InstallFragment();
		fragments[1] = new NotinstallFragment();
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