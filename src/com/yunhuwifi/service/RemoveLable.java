package com.yunhuwifi.service;

import cn.sharesdk.framework.authorize.AuthorizeAdapter;

public class RemoveLable extends AuthorizeAdapter{
	
	public void onCreate(){
		hideShareSDKLogo();
	}
}
