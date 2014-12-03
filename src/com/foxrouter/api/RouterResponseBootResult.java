package com.foxrouter.api;

public class RouterResponseBootResult extends RouterResponse{
	private boolean isResult;
	private boolean isBootResult;
	public boolean isResult() {
		return isResult;
	}
	public void setResult(boolean isResult) {
		this.isResult = isResult;
	}
	public boolean isBootResult() {
		return isBootResult;
	}
	public void setBootResult(boolean isBootResult) {
		this.isBootResult = isBootResult;
	}

}
