package com.yunhuwifi.models;

import android.graphics.Bitmap;

import com.yunhuwifi.view.ListViewItem;

public class RouterDownload implements ListViewItem{

	public Bitmap getDownloadBitmap() {
		return downloadBitmap;
	}
	public void setDownloadBitmap(Bitmap downloadBitmap) {
		this.downloadBitmap = downloadBitmap;
	}
	public String getDownloadtime() {
		return downloadtime;
	}
	public void setDownloadtime(String downloadtime) {
		this.downloadtime = downloadtime;
	}
	public String getDownloadpercent() {
		return downloadpercent;
	}
	public void setDownloadpercent(String downlaodpercent) {
		this.downloadpercent = downlaodpercent; 
	}
	public String getDownloadsize() {
		return downloadsize;
	}
	public void setDownloadsize(String downloadsize) {
		this.downloadsize = downloadsize;
	}
	public String getDownloadstate() {
		return downloadstate;
	}
	public void setDownloadstate(String downloadstate) {
		this.downloadstate = downloadstate;
	}
	public String getDownloadtitle() {
		return downloadtitle;
	}
	public void setDownloadtitle(String downloadtitle) {
		this.downloadtitle = downloadtitle;
	}
	public String getDownloadspeed() {
		return downloadspeed;
	}
	public void setDownloadspeed(String downloadspeed) {
		this.downloadspeed = downloadspeed;
	}
	public int getDownloadprogress() {
		return downloadprogress; 
	}
	public void setDownloadprogress(int downloadprpgress) {
		this.downloadprogress = downloadprpgress;
	}
	private Bitmap downloadBitmap;
	private String downloadtime; 
	private String downloadpercent;
	private String downloadsize;
	private String downloadstate;
	private String downloadtitle;
	private String downloadspeed; 
	private int downloadprogress;
	@Override
	public Bitmap getBitmap() {
	
		return getDownloadBitmap();
	}
	@Override
	public String getTitle() {
	
		return getDownloadtitle();
	}
	@Override
	public String getSpeed() {
	
		return getDownloadspeed();
	}
	@Override
	public String getDate() {
	
		return getDownloadtime();
	}
	@Override
	public String getState() {
	
		return getDownloadstate();
	}
	@Override
	public String getSize() {
	
		return getDownloadsize();
	}
	@Override
	public String getPrecent() {
	
		return getDownloadpercent();
	}
	@Override
	public int getOperationView() {
	
		return 0;
	}
	@Override
	public int getProgress() {
	
		return getDownloadprogress();
	}


}
