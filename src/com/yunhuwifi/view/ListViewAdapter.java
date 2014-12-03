package com.yunhuwifi.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yunhuwifi.activity.R;

public class ListViewAdapter extends BaseAdapter { 
	private List<ListViewItem> listData;
	private LayoutInflater inflater;
	public static boolean visable = false;
	public List<Boolean> flags;
	private int switchType; 
	private Context context;
	private ListViewItemViewHolder holder=null;
		private Handler handler;
	public ListViewAdapter(Context context, List<ListViewItem> listState, int switchType,Handler handler) {
		super();
		this.context=context;
		inflater = LayoutInflater.from(context);
		this.listData = listState;
		this.handler = handler;
		flags = new ArrayList<Boolean>();
		for (int i = 0; i < listData.size(); i++) {
			flags.add(false);
		}
		this.switchType=switchType;
		handler.sendMessage(handler.obtainMessage(1, listData.size(), findCount()));
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			switch(switchType){
			
			case 0:
					holder = new ListViewItemViewHolder();
					convertView = inflater.inflate(R.layout.listview_item_state, null);
				holder.titleView = (TextView) convertView
						.findViewById(R.id.tvState);
				holder.checkview = (CheckBox) convertView
						.findViewById(R.id.cbState);
				holder.timeView=(TextView) convertView.findViewById(R.id.tvStateTime);
				holder.operationview=convertView.findViewById(R.id.ivState);
				convertView.setTag(holder);
				
				break;
			
			case 1:
					holder = new ListViewItemViewHolder();
					convertView = inflater.inflate(R.layout.listview_item_download, null);
				holder.stateView = (TextView) convertView
						.findViewById(R.id.tvDowningState);
				holder.timeView = (TextView) convertView
						.findViewById(R.id.tvDowningtime);
				holder.sizeView = (TextView) convertView
						.findViewById(R.id.tvDowningSize);
				holder.percentView = (TextView) convertView
						.findViewById(R.id.tvDowningPercent);
				holder.titleView = (TextView) convertView
						.findViewById(R.id.tvDowningTitle);
				holder.bitmapView = (ImageView) convertView
						.findViewById(R.id.ivDowning);
				holder.checkview = (CheckBox) convertView
						.findViewById(R.id.cbdowning);
				holder.pbView=(ProgressBar)convertView.findViewById(R.id.pbdowning);
				holder.operationview=convertView.findViewById(R.id.btnDowning);
				convertView.setTag(holder);
				
				break;
			case 2:
					holder = new ListViewItemViewHolder();
					convertView = inflater.inflate(R.layout.listview_item_blacklist, null);
				holder.titleView = (TextView) convertView
						.findViewById(R.id.tvalineTitle);
				holder.bitmapView = (ImageView) convertView
						.findViewById(R.id.imgalineIcon);
				holder.checkview = (CheckBox) convertView
						.findViewById(R.id.cbaline);
				holder.operationview=convertView.findViewById(R.id.btnaline);
				convertView.setTag(holder);
				
				break;
			case 3:
					holder = new ListViewItemViewHolder();
					convertView = inflater.inflate(R.layout.listview_item_wifi, null);
					holder.titleView = (TextView) convertView
							.findViewById(R.id.tvWifiName);
					holder.bitmapView = (ImageView) convertView
							.findViewById(R.id.ivwifi);
					holder.operationview=convertView.findViewById(R.id.tgOpen);
					convertView.setTag(holder);
				
				break;
			case 4:
					holder = new ListViewItemViewHolder();
					convertView = inflater.inflate(R.layout.listview_item_device, null);
					holder.titleView = (TextView) convertView
							.findViewById(R.id.tvDeviceTitle);
					holder.descriptionView = (TextView) convertView
							.findViewById(R.id.tvDeviceIp);
					holder.bitmapView = (ImageView) convertView
							.findViewById(R.id.ivDevice);
					holder.operationview=convertView.findViewById(R.id.btnDevice);
					convertView.setTag(holder);
				
				break;
			case 5:
					holder = new ListViewItemViewHolder();
					convertView = inflater.inflate(R.layout.listview_item_file, null);
					holder.titleView = (TextView) convertView.findViewById(R.id.tvFileTitle);
					holder.sizeView = (TextView) convertView.findViewById(R.id.tvFileSize);
					holder.timeView = (TextView) convertView.findViewById(R.id.tvFileTime);
					holder.bitmapView = (ImageView) convertView.findViewById(R.id.ivfile);
					holder.checkview = (CheckBox) convertView.findViewById(R.id.cbFile);
					convertView.setTag(holder);
			
				break;
			
			case 6:
				
					holder = new ListViewItemViewHolder();
					convertView = inflater.inflate(R.layout.listview_item_notinstallapp, null);
					holder.titleView = (TextView) convertView
							.findViewById(R.id.tvInstall);
					holder.descriptionView = (TextView) convertView
							.findViewById(R.id.tvMsg);
					holder.bitmapView = (ImageView) convertView
							.findViewById(R.id.ivInstall);
					holder.operationview=convertView.findViewById(R.id.btnInstall);
					convertView.setTag(holder);
				break;
			
			case 7:
					holder = new ListViewItemViewHolder();
					convertView = inflater.inflate(R.layout.listview_item_set, null);
					holder.titleView = (TextView) convertView
							.findViewById(R.id.tvset);
					holder.bitmapView = (ImageView) convertView
							.findViewById(R.id.ivset);
					holder.operationview=convertView.findViewById(R.id.ivsetnext);
					convertView.setTag(holder);
				break;
			
			case 8:
					holder = new ListViewItemViewHolder();
					convertView = inflater.inflate(R.layout.listview_item_router, null);
					holder.titleView = (TextView) convertView
							.findViewById(R.id.tvRemark);
					holder.descriptionView = (TextView) convertView
							.findViewById(R.id.tvIPAddress);
					holder.bitmapView = (ImageView) convertView
							.findViewById(R.id.ivRouter);
					holder.operationview=convertView.findViewById(R.id.ivChecked);
					convertView.setTag(holder);
				break;
			
			}
	}else{
		holder = (ListViewItemViewHolder) convertView.getTag();
		
	}
		switch(switchType){
			case 0:
				ListViewItem item0 = listData.get(position);
				holder.titleView.setText(item0.getTitle());
				holder.timeView.setText(item0.getDate());
				if (visable) {
					holder.operationview.setVisibility(View.GONE);
				holder.checkview.setVisibility(View.VISIBLE);
				holder.checkview.setChecked(flags.get(position));

			} else {
				holder.checkview.setVisibility(View.GONE);
					holder.operationview.setVisibility(View.VISIBLE);
					
			}
				holder.checkview.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						CheckBox box = (CheckBox) v;
						flags.set(position, box.isChecked());
						notifyDataSetChanged();
					}
				});
			break;
			case 1:
				 
				ListViewItem item1 = listData.get(position);
//				holder.bitmapView.setImageBitmap((item.getBitmap()));
				holder.titleView.setText(item1.getTitle());
//				holder.operationview.setBackgroundResource((item1.getOperationView()));
//				holder.sizeView.setText(item1.getSize());
//				holder.percentView.setText(item1.getPrecent());
//				holder.stateView.setText(item1.getState());
//				holder.timeView.setText(item1.getDate());
				holder.operationview.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Button btn=(Button) v;
						btn.setBackgroundResource(R.drawable.stopicon);
					}
				});
				break;
			case 2:
				 
				ListViewItem item2 = listData.get(position);
				holder.bitmapView.setImageBitmap((item2.getBitmap()));
				holder.titleView.setText(item2.getTitle());
				
				break;
			case 3:
				ListViewItem item3 = listData.get(position);
//				holder.bitmapView.setImageBitmap((item.getBitmap()));
				holder.titleView.setText(item3.getTitle());
				holder.operationview.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ToggleButton toggleButton=(ToggleButton) v;
						toggleButton.setChecked(false);
						
					}
				});
				break;
			case 4:
				ListViewItem item4 = listData.get(position); 
//				holder.bitmapView.setImageBitmap((item.getBitmap()));
				holder.titleView.setText(item4.getTitle());
				holder.descriptionView.setText(item4.getSpeed());
				break;
			case 5:
				ListViewItem item5 = listData.get(position);
				holder.titleView.setText(item5.getTitle());
				/*holder.bitmapView.setImageBitmap((item5.getBitmap()));
				holder.sizeView.setText(item5.getSize());
				holder.timeView.setText(item5.getSpeed());*/
				break;
			case 6:
				ListViewItem item6 = listData.get(position); 
//				holder.bitmapView.setImageBitmap((item6.getBitmap()));
				holder.titleView.setText(item6.getTitle());
				holder.descriptionView.setText(item6.getSpeed());
				holder.operationview.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Button installing=(Button) v;
						installing.setText("安装中");
						installing.setBackgroundResource(R.drawable.installing);
						installing.setClickable(false);
					}
				});
				break;
			case 7:
				ListViewItem item7 = listData.get(position);
				holder.bitmapView.setImageBitmap((item7.getBitmap()));
				holder.titleView.setText(item7.getTitle());
				break;
			case 8:
				ListViewItem item8 = listData.get(position);
				holder.bitmapView.setImageBitmap((item8.getBitmap()));
				holder.titleView.setText(item8.getTitle());
				holder.descriptionView.setText(item8.getSize());
				holder.timeView.setText(item8.getSpeed());
				break;
			
		}

		handler.sendMessage(handler.obtainMessage(1, listData.size(), findCount()));
		return convertView;
	}
	public class ListViewItemViewHolder {
		ImageView bitmapView;
		TextView titleView; 
		TextView descriptionView;
		TextView sizeView;
		TextView stateView;
		TextView percentView;
		TextView timeView;
		ProgressBar pbView;
		View operationview;
		CheckBox checkview; 
	}
	private int findCount() {
		int count = 0;
		for (int i = 0; i < flags.size(); i++) {
			if (flags.get(i))
				count++;
		}
		return count;
	}


}
