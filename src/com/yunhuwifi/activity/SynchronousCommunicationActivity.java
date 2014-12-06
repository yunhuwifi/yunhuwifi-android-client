package com.yunhuwifi.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.models.CallLogs;
import com.yunhuwifi.models.ContactInfo;
import com.yunhuwifi.models.RouterSet;
import com.yunhuwifi.service.ExportSmsXml;
import com.yunhuwifi.util.FileUtil;
import com.yunhuwifi.util.ImageTools;
import com.yunhuwifi.vcard.ContactStruct;
import com.yunhuwifi.vcard.VCardComposer;
import com.yunhuwifi.vcard.VCardException;
import com.yunhuwifi.view.ListViewAdapter;
import com.yunhuwifi.view.ListViewItem;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class SynchronousCommunicationActivity extends HeaderActivity  {
	private final int SET=3;
	private static final int SCALE = 5;// 照片缩小比例
	private ListView lvCommunications;
	private List<ListViewItem> data = new ArrayList<ListViewItem>();
	private ListViewAdapter adapter;
	private ContentResolver mContentResolver;
	private ArrayList<CallLogs> mCallLogListData = new ArrayList<CallLogs>();
	private LinkedHashMap<String, CallLogs> mCallLogMapData = new LinkedHashMap<String, CallLogs>();
	Handler hand = new Handler() ;
	public void handleMessage(android.os.Message msg) {
		if (msg.what == 1) {
			Log.d("tag","共" + msg.arg1 + "条，已选" + msg.arg2 + "条");
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_synchronous_communication);
		this.setHeaderText("同步通讯");
		this.setLeftImageVisible(true);
		this.setRightImageVisible(false);
		this.ivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		lvCommunications = (ListView) findViewById(R.id.lvCommunications);
		initdata();
	}

	private void initdata() {
		RouterSet item1=new RouterSet();
		RouterSet item2=new RouterSet();
		RouterSet item3=new RouterSet();
		RouterSet item4=new RouterSet();
		item1.setIconRes(BitmapFactory.decodeResource(this.getResources(), R.drawable.addressbook));
		item1.setMsg("通讯录");
//		item1.setViewOpreate(R.drawable.nextbg);
		item2.setIconRes(BitmapFactory.decodeResource(this.getResources(), R.drawable.message));
		item2.setMsg("短信");
//		item2.setViewOpreate(R.drawable.nextbg);
		item3.setIconRes(BitmapFactory.decodeResource(this.getResources(), R.drawable.photo));
		item3.setMsg("相册");
//		item3.setViewOpreate(R.drawable.nextbg);
		item4.setIconRes(BitmapFactory.decodeResource(this.getResources(), R.drawable.callhistory));
		item4.setMsg("通话记录");
//		item4.setViewOpreate(R.drawable.nextbg);
		data.add(item1);
		data.add(item2);
		data.add(item3);
		data.add(item4);
		
		adapter = new ListViewAdapter(SynchronousCommunicationActivity.this,data,SET,hand);
		lvCommunications.setAdapter(adapter);
		lvCommunications.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					exportContacts();
					break;
				case 1:
					ExportSmsXml export = new ExportSmsXml(
							getApplicationContext());

					try {
						if (export.createXml()) {
							showToast("短信备份成功", Toast.LENGTH_SHORT);
						} else {
							showToast("短信备份失败", Toast.LENGTH_SHORT);
						}
					} catch (Exception e) {
						e.printStackTrace();
						showToast("短信备份出错", Toast.LENGTH_SHORT);
					}
					break;
				case 2:
					showPicker(SynchronousCommunicationActivity.this);
					break;
				case 3:
					try {
						List<CallLogs> listLog = getCallLogsInfo();
						backupCallLog(SynchronousCommunicationActivity.this,
								listLog);
						showToast("通话记录导出成功", Toast.LENGTH_SHORT);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						showToast("通话记录导出出错", Toast.LENGTH_SHORT);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						showToast("通话记录导出失败", Toast.LENGTH_SHORT);
					}
					break;
				}

			}
		});
	}

	protected List<CallLogs> getCallLogsInfo()
			throws UnsupportedEncodingException, FileNotFoundException {
		mContentResolver = this.getContentResolver();

		Cursor mCursor = mContentResolver.query(CallLog.Calls.CONTENT_URI,
				new String[] { "_id", "date", "duration", "name", "number",
						"type" }, null, null, "date desc");
		mCallLogMapData.clear();
		while (mCursor.moveToNext()) {

			CallLogs nowCallLogs = new CallLogs();

			nowCallLogs.setId(mCursor.getInt(0));

			nowCallLogs.setDate(mCursor.getLong(1));
			Date nowdate = new Date(0, 0, 0);
			
			Date today = new Date(nowdate.getYear(), nowdate.getMonth(),
					nowdate.getDay());
			if (nowdate.getTime() - (60 * 1000) < nowCallLogs.getDate()) {
				
				nowCallLogs.setDateFormat("刚刚");
			} else if (nowdate.getTime() - (60 * 1000 * 60) < nowCallLogs
					.getDate()) {
				
				nowCallLogs.setDateFormat("n分钟");
			} else if (nowdate.getTime() - (60 * 1000 * 60 * 24) < nowCallLogs
					.getDate()) {
			
				nowCallLogs.setDateFormat("今天");
			} else if (nowdate.getTime() - (60 * 1000 * 60 * 24 * 2) < nowCallLogs
					.getDate()) {
			
				nowCallLogs.setDateFormat("昨天");
			} else {
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
				nowCallLogs.setDateFormat(sdf.format(new Date(nowCallLogs
						.getDate())));
			}

			nowCallLogs.setDuration(mCursor.getLong(2));
			nowCallLogs.setName(mCursor.getString(3));
			nowCallLogs.setTelNumber(mCursor.getString(4));
			nowCallLogs.setType(mCursor.getInt(5));

			mCallLogListData.add(nowCallLogs);
			CallLogs mapCallLog = mCallLogMapData.get(nowCallLogs
					.getTelNumber());

			if (null != mapCallLog) {
				int callCount = mapCallLog.getCount() + 1;
				mapCallLog.setCount(callCount);
			} else {
				nowCallLogs.setCount(1);
				mCallLogMapData.put(nowCallLogs.getTelNumber(), nowCallLogs);
			}

		}
		mCallLogListData = new ArrayList<CallLogs>(mCallLogMapData.values());
		mCallLogListData.toString();
		return mCallLogListData;

	}


	public void backupCallLog(Activity context, List<CallLogs> infos) {

		try {

			String path = FileUtil.getFilePath("calllogs.vcf");

			OutputStreamWriter writer = new OutputStreamWriter( 
					FileUtil.getFileOutputStream(path, false), "UTF-8");

			VCardComposer composer = new VCardComposer();

			for (CallLogs info : infos) {
				ContactStruct contact = new ContactStruct();
				contact.name = info.getName() == null ? "" : info.getName();
				contact.addPhone(info.getType(), info.getTelNumber(), null,
						true);
				String vcardString = composer.createVCard(contact,
						VCardComposer.VERSION_VCARD30_INT);
				writer.write(vcardString);
				writer.write("\n");

				writer.flush();
			}
			writer.close();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (VCardException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	ContactInfo.ContactHandler handler = ContactInfo.ContactHandler
			.getInstance();

	public void exportContacts() {
		List<ContactInfo> _infoList = handler.getContactInfo(this);
		handler.backupContacts(this, _infoList); // 备份联系人信息
	}

	public void importContacts() {
		try {
			List<ContactInfo> infoList = handler.restoreContacts();
			Toast.makeText(this, "获取成功！", Toast.LENGTH_SHORT).show();
			for (ContactInfo contactInfo : infoList) {
				handler.addContacts(this, contactInfo);
			}

			Toast.makeText(this, "导入联系人信息成功!", Toast.LENGTH_LONG);

		} catch (Exception e) {
			Toast.makeText(this, "导入联系人信息失败!+ e.printStackTrace()",
					Toast.LENGTH_SHORT).show();
		}

	}


	public void showPicker(Context context) {
		final Dialog dlg = new Dialog(this);
		View dlgView = View.inflate(this, R.layout.photo_box, null);
		View photograph = dlgView.findViewById(R.id.photograph);
		photograph.setTag(dlg);
		photograph.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				Uri imageUri = Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), "image.jpg"));
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(openCameraIntent, 0);
				dlg.dismiss();
			}
		});
		View photo = dlgView.findViewById(R.id.photo);
		photo.setTag(dlg);
		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
				openAlbumIntent.setType("image/*");
				startActivityForResult(openAlbumIntent, 1);
				dlg.dismiss();
			}
		});
		View cancelphoto = dlgView.findViewById(R.id.cancelphoto);
		cancelphoto.setTag(dlg);
		cancelphoto.setOnClickListener(new OnClickListener() {

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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 0:
				Bitmap bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory() + "/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
						bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				bitmap.recycle();

				ImageTools.savePhotoToSDCard(newBitmap, Environment
						.getExternalStorageDirectory().getAbsolutePath(),
						String.valueOf(System.currentTimeMillis()));
				break;

			case 1:
				ContentResolver resolver = getContentResolver();
				Uri originalUri = data.getData();
				try {
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					if (photo != null) {
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / SCALE, photo.getHeight()
										/ SCALE);
						photo.recycle();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}

	}
}
