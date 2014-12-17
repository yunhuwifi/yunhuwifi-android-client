package com.foxrouter.api;

import com.yunhuwifi.RouterContext;
import com.yunhuwifi.handlers.JsonCallBack;

public class RouterModuleFile extends RouterModule{

	public RouterModuleFile(RouterContext context) {
		super(context);
		super.setModule("file");
	}
	
	public void list(JsonCallBack<? extends Object> callback,String path){
		
		super.execute("list", new Object[]{path}, callback);
		
	}
	
	public void remame(String src,String dst,JsonCallBack<? extends Object> callback ){
		
		super.execute("rename", new Object[]{src,dst},callback);
		
	}
	
	public void copy(String src,String dst,JsonCallBack<? extends Object> callback){
		super.execute("copy", new Object[]{src,dst},callback);
		
	}
	
	public void mkdir(String path,JsonCallBack<? extends Object> callback){
		super.execute("mkdir", new Object[]{path}, callback);
		
	}
	
	public void rmdir(String path,JsonCallBack<? extends Object> callback){
		
		super.execute("rmdir", new Object[]{path}, callback);
		
	}
	
	public void delete(String src,JsonCallBack<? extends Object> callback){
		super.execute("delete", new Object[]{src}, callback);
		
	}
	
	public void move(String src ,String dst,JsonCallBack<? extends Object> callback){
		super.execute("move", new Object[]{src, dst},callback);
	}

}
