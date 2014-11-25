package com.yunhuwifi.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.os.Environment;

public class FileUtil {

	private final static String ROOT_PATH = "/yunhu/router";

	public static String getFilePath(String filename) {
		String rootPath = Environment.getExternalStorageDirectory() + ROOT_PATH;
		if (filename.startsWith("/"))
			return rootPath + filename;
		else
			return rootPath + "/" + filename;
	}

	public static FileOutputStream getFileOutputStream(String filename,
			boolean append) throws FileNotFoundException {
		File file = new File(filename);
		String path = file.getParent();
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return new FileOutputStream(filename, append);
	}
}
