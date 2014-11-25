package com.yunhuwifi.cloud.api.models;

public class ResponseRegister {
	// / <summary>
	// / 返回用户 ID，表示用户注册成功
	// / </summary>
	public final static int Success = 0;
	// / <summary>
	// / 用户名不合法
	// / </summary>
	public final static int UserNameIllegal = -1;
	// / <summary>
	// / 包含不允许注册的词语
	// / </summary>
	public final static int ContainsInvalidWords = -2;
	// / <summary>
	// / 用户名已经存在
	// / </summary>
	public final static int UserNameExists = -3;
	// / <summary>
	// / Email 格式有误
	// / </summary>
	public final static int IncorrectEmailFormat = -4;
	// / <summary>
	// / Email 不允许注册
	// / </summary>
	public final static int EmailNotAllowed = -5;
	// / <summary>
	// / 该 Email 已经被注册
	// / </summary>
	public final static int EmailHasBeenRegistered = -6;
	// / <summary>
	// / 手机号码格式错误
	// / </summary>
	public final static int IncorrectPhoneFormat = -7;
	// / <summary>
	// / 手机号码已存在
	// / </summary>
	public final static int PhoneExists = -8;
	// / <summary>
	// / 没有填写用户名邮箱手机号码
	// / </summary>
	public final static int RegisterNameInvalid = -10;

	private int result;
	private int uid;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
}
