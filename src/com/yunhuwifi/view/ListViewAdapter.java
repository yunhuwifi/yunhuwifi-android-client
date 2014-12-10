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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import android.widget.Button;
import android.widget.TextView;

import com.yunhuwifi.activity.R;

public class ListViewAdapter extends BaseAdapter { 

	private List<ListViewItem> listData;
	private LayoutInflater inflater;
	public static boolean visable = false;
	public List<Boolean> flags;
	private int switchType,selectedPosition; 
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

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView==null){
					holder = new ListViewItemViewHolder();
					convertView = inflater.inflate(switchType, null);
					holder.stateView = (TextView) convertView
							.findViewById(R.id.tvDowningState);
					holder.descripeView=(TextView) convertView
							.findViewById(R.id.tvDescribe);
					holder.sizeView = (TextView) convertView
							.findViewById(R.id.tvDowningSize);
					holder.percentView = (TextView) convertView
							.findViewById(R.id.tvDowningPercent);
					holder.titleView = (TextView) convertView
							.findViewById(R.id.tvTitle);
					holder.bitmapView = (ImageView) convertView
							.findViewById(R.id.ivIcon);
					holder.checkview = (CheckBox) convertView
							.findViewById(R.id.cbitem);
					holder.pbView=(ProgressBar)convertView.findViewById(R.id.pbdowning);
					holder.operationview=convertView.findViewById(R.id.viewOperate);
					convertView.setTag(holder);
			 		
		}
		else{
			holder=(ListViewItemViewHolder) convertView.getTag();
		}
		
		
		ListViewItem item=listData.get(position);
		if(item.getState()==null){
			if(item.getSpeed()==null){
				if(item.getBitmap()!=null){
					holder.titleView.setText(item.getTitle());
					holder.bitmapView.setImageBitmap(item.getBitmap());
					if(position==selectedPosition){
						holder.operationview.setVisibility(View.VISIBLE);
					}else{
						holder.operationview.setVisibility(View.GONE);
					}
				}else{
					holder.titleView.setText(item.getTitle());//移除
					if(holder.operationview instanceof Button){
						holder.operationview.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Button b=(Button) v;
								if(b.getText()=="移除"){
									b.setText("移除中");
								}
							}
						});
					}else if(holder.operationview instanceof ToggleButton){
						holder.operationview.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								ToggleButton tb=(ToggleButton) v;
								tb.setChecked(false);
							}
						});
					}
				}
				
			}else{
				if(item.getBitmap()==null){
					holder.titleView.setText(item.getTitle());
					holder.descripeView.setText(item.getSpeed());
					if(visable){
						holder.checkview.setVisibility(View.VISIBLE);
						holder.operationview.setVisibility(View.GONE);
					}else{
						holder.checkview.setVisibility(View.GONE);
						holder.operationview.setVisibility(View.VISIBLE);
						
					}
				}else{
					holder.bitmapView.setImageBitmap(item.getBitmap());
					holder.titleView.setText(item.getTitle());
					holder.descripeView.setText(item.getSpeed());
					if(holder.operationview instanceof Button){
						holder.operationview.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Button b=(Button) v;
								if(b.getText()=="安装"){
									b.setText("安装中");
									b.setClickable(false);
									b.setBackgroundResource(R.drawable.installing);
								}
							}
						});
					}
				}
			}
		}else{
			holder.bitmapView.setImageBitmap((item.getBitmap())); 
			holder.titleView.setText(item.getTitle());
			holder.descripeView.setText(item.getSpeed());
			holder.sizeView.setText(item.getSize());
			holder.percentView.setText(item.getPrecent());
			holder.stateView.setText(item.getState());
			holder.pbView.setProgress(item.getProgress());
			holder.operationview.setOnClickListener(new MyLister(position));
		}
	
		handler.sendMessage(handler.obtainMessage(1, listData.size(), findCount()));
		return convertView;
		
		
	}
	public class ListViewItemViewHolder {
		ImageView bitmapView;
		TextView titleView;  
		TextView descripeView;
		TextView sizeView;
		TextView stateView;
		TextView percentView;
		TextView timeView;
		ProgressBar pbView;
		View operationview;
		CheckBox checkview; 
	}
	
	public class MyLister implements OnClickListener{
		
		private int po;
		public MyLister(int position){
			this.po=position;
		}
		
		@Override
		public void onClick(View v) {
			if(v.getId()==holder.operationview.getId()){
				holder.operationview.setBackgroundResource(R.drawable.continueicon);
				holder.stateView.setText("已暂停");
				holder.stateView.setTextColor(R.color.itemlefttoptext); 
				holder.percentView.setTextColor(R.color.itemlefttoptext);
				System.out.println("点击id"+v.getId()+holder.operationview.getId()+po);
			}
			
		}
		
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
