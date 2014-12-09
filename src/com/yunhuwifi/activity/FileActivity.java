package com.yunhuwifi.activity;

import java.io.File;
import java.util.ArrayList;

import com.yunhuwifi.view.FileAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FileActivity extends HeaderActivity {

	private ListView lvfile;
	private static final String ROOT_PATH = "/";
	private ArrayList<String> names = null;
	private ArrayList<String> paths = null;
	private View view;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_file);
		lvfile = (ListView) findViewById(R.id.lvfile);
		this.setHeaderText("存储管理");
		this.setLeftImageVisible(true);
		this.setRightImageVisible(true);
		this.ivRight.setImageResource(R.drawable.downicon);
		this.ivLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		this.ivRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(FileActivity.this,
						DownloadActivity.class));
			}
		});
		showFileDir(ROOT_PATH);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			menuBox();
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * public boolean OnCreateOptionsMenu(Menu menu){
	 * super.onCreateOptionsMenu(menu);
	 * getMenuInflater().inflate(R.menu.menu,menu); return true;
	 * 
	 * }
	 * 
	 * public boolean onOptionsItemSelected(MenuItem item){
	 * switch(item.getItemId()){ case R.id.menunewfile: popupNewFile(); break;
	 * case R.id.menuupload:
	 * 
	 * break; } return super.onOptionsItemSelected(item);
	 * 
	 * }
	 */

	private void menuBox() {
		final Dialog dlg = new Dialog(this);
		View dlgView = View.inflate(this, R.layout.menu_box, null);
		View btnnewfile = dlgView.findViewById(R.id.btnnewfile);
		btnnewfile.setTag(dlg);
		btnnewfile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popupNewFile();
				dlg.dismiss();
			}
		});
		View btnupload = dlgView.findViewById(R.id.btnupload);
		btnupload.setTag(dlg);
		btnupload.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				uploadBox();
				dlg.dismiss();
			}

		});
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(dlgView);
		WindowManager.LayoutParams params = dlg.getWindow()
				.getAttributes();
		dlg.getWindow().setGravity(Gravity.BOTTOM);
		params.y =2;
		dlg.getWindow().setAttributes(params);
		dlg.show();

	}

	private void uploadBox() {
		final Dialog dlg = new Dialog(this);
		View dlgView = View.inflate(this, R.layout.upload_box, null);
		View btnpicture = dlgView.findViewById(R.id.btnpicture);
		btnpicture.setTag(dlg);
		btnpicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popupNewFile();
				dlg.dismiss();
			}
		});
		View btnmusic = dlgView.findViewById(R.id.btnmusic);
		btnmusic.setTag(dlg);
		btnmusic.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dlg.dismiss();
			}

		});
		View btnvideo = dlgView.findViewById(R.id.btnvideo);
		btnvideo.setTag(dlg);
		btnvideo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dlg.dismiss();
			}

		});
		View btnother = dlgView.findViewById(R.id.btnother);
		btnother.setTag(dlg);
		btnother.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dlg.dismiss();
			}

		});
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(dlgView);
		WindowManager.LayoutParams params = dlg.getWindow()
				.getAttributes();
		dlg.getWindow().setGravity(Gravity.BOTTOM);
		params.y = 20;
		dlg.getWindow().setAttributes(params);
		dlg.show();

	}

	private void popupNewFile() {
		final Dialog dlg = new Dialog(this);
		View dlgView = View.inflate(this, R.layout.dns_box, null);
		View ok = dlgView.findViewById(R.id.ok);
		ok.setTag(dlg);
		final TextView tvdialogtitle = (TextView) dlgView
				.findViewById(R.id.dialogtitle);
		tvdialogtitle.setText("新建文件夹");
		final EditText first = (EditText) dlgView.findViewById(R.id.first);
		final EditText backup = (EditText) dlgView.findViewById(R.id.backup);
		backup.setVisibility(View.GONE);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String ftxt = first.getText().toString();
				dlg.dismiss();
			}
		});
		View cancel = dlgView.findViewById(R.id.cancel);
		cancel.setTag(dlg);
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dlg.dismiss();
			}

		});
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(dlgView);
		dlg.show();
	}

	private void showFileDir(String path) {
		names = new ArrayList<String>();
		paths = new ArrayList<String>();
		File file = new File(path);
		File[] files = file.listFiles();

		if (!ROOT_PATH.equals(path)) {
			names.add("@1");
			paths.add(ROOT_PATH);

			names.add("@2");
			paths.add(file.getParent());
		}
		for (File f : files) {
			names.add(f.getName());
			paths.add(f.getPath());
		}
		lvfile.setAdapter((new FileAdapter(FileActivity.this, names, paths)));
		lvfile.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String path = paths.get(position);
				File file = new File(path);
				if (file.exists() && file.canRead()) {
					if (file.isDirectory()) {
						showFileDir(path);
					} else {
						fileHandle(file);
					}
				} else {
					Resources res = getResources();
					new AlertDialog.Builder(FileActivity.this)
							.setTitle("Message")
							.setMessage("没有权限")
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									}).show();
				}

			}
		});
	}

	private void openFile(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		String type = getMIMEType(file);
		intent.setDataAndType(Uri.fromFile(file), type);
		startActivity(intent);
	}

	private String getMIMEType(File file) {
		String type = "";
		String name = file.getName();
		String end = name.substring(name.lastIndexOf(".") + 1, name.length())
				.toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("mp4") || end.equals("3gp")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("png") || end.equals("jpeg")
				|| end.equals("bmp") || end.equals("gif")) {
			type = "image";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}

	private void fileHandle(final File file) {

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					openFile(file);
				} else if (which == 1) {
					LayoutInflater factory = LayoutInflater
							.from(FileActivity.this);
					view = factory.inflate(R.layout.rename_box, null);
					editText = (EditText) view.findViewById(R.id.editText);
					editText.setText(file.getName());

					android.content.DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String modifyName = editText.getText().toString();
							final String fpath = file.getParentFile().getPath();
							final File newFile = new File(fpath + "/"
									+ modifyName);
							if (newFile.exists()) {
								if (!modifyName.equals(file.getName())) {
									new AlertDialog.Builder(FileActivity.this)
											.setTitle("注意!")
											.setMessage("文件名已存在，是否覆盖？")
											.setPositiveButton(
													"确定",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															if (file.renameTo(newFile)) {
																showFileDir(fpath);
																showToast(
																		"重命名成功！",
																		Toast.LENGTH_SHORT);
															} else {
																showToast(
																		"重命名失败！",
																		Toast.LENGTH_SHORT);
															}
														}
													})
											.setNegativeButton(
													"取消",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {

														}
													}).show();
								}
							} else {
								if (file.renameTo(newFile)) {
									showFileDir(fpath);
									showToast("重命名成功！", Toast.LENGTH_SHORT);
								} else {
									showToast("重命名失败！", Toast.LENGTH_SHORT);
								}
							}
						}
					};
					AlertDialog renameDialog = new AlertDialog.Builder(
							FileActivity.this).create();
					renameDialog.setView(view);
					renameDialog.setButton("确定", listener2);
					renameDialog.setButton2("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
					renameDialog.show();
				} else {
					new AlertDialog.Builder(FileActivity.this)
							.setTitle("注意!")
							.setMessage("确定要删除此文件吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (file.delete()) {
												// 更新文件列表
												showFileDir(file.getParent());
												showToast("删除成功！",
														Toast.LENGTH_SHORT);
											} else {
												showToast("删除失败！",
														Toast.LENGTH_SHORT);
											}

										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									}).show();
				}
			}
		};
		String[] menu = { "打开", "重命名", "删除", "复制", "粘贴", "详情" };
		new AlertDialog.Builder(FileActivity.this).setTitle("请选择要进行的操作!")
				.setItems(menu, listener)
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();

	}

	private void popupBind() {
		final Dialog dlg = new Dialog(this);
		View dlgView = View.inflate(this, R.layout.confirm_box, null);
		View oklink = dlgView.findViewById(R.id.ok);
		oklink.setTag(dlg);
		TextView dialogtitle = (TextView) dlgView
				.findViewById(R.id.dialogtitle);
		dialogtitle.setText("提示");
		TextView dialogmessage = (TextView) dlgView
				.findViewById(R.id.dialogmessage);
		dialogmessage.setText("您需要设置账户才能使用百度网盘！");

		oklink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(FileActivity.this,
						BaiDuLoginActivity.class));
				dlg.dismiss();
			}
		});
		View cancellink = dlgView.findViewById(R.id.cancel);
		cancellink.setTag(dlg);
		cancellink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});

		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(dlgView);
		dlg.show();

	}
}
