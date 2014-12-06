package com.yunhuwifi.fragment;

import com.yunhuwifi.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class SkydriveFragment extends Fragment{
	
	private ListView lvskydrive;
	
	public View onCreateView(LayoutInflater inflater,ViewGroup group,Bundle bundle){
		View v=inflater.inflate(R.layout.fragment_skydrive, group, false);
		lvskydrive=(ListView) v.findViewById(R.id.lvskydrive);
		setHasOptionsMenu(true);
		return v;
	}
	
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater){
    	menuInflater.inflate(R.menu.menu, menu);
    	super.onCreateOptionsMenu(menu, menuInflater);
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case R.id.menunewfile:
    		break;
    	default :
    		break;
    	}
		return super.onOptionsItemSelected(item);
    	
    }
	
}
