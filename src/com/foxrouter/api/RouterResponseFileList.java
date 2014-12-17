package com.foxrouter.api;

import java.util.List;

import com.foxrouter.api.model.RouterFile;

public class RouterResponseFileList extends RouterResponse{
	
	private List<RouterFile> result;

	public List<RouterFile> getResult() {
		return result;
	}

	public void setResult(List<RouterFile> result) {
		this.result = result;
	}

}
