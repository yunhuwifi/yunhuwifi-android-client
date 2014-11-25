package com.yunhuwifi.service;
import java.io.File;
import java.io.FileOutputStream;
import org.xmlpull.v1.XmlSerializer;

import com.yunhuwifi.models.SmsField;
import com.yunhuwifi.util.FileUtil;
 
 
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;
 
public class ExportSmsXml {
    Context context;
    public static final String SMS_URI_ALL = "content://sms/";
    private FileOutputStream outStream = null;
    private XmlSerializer serializer;
 
    public ExportSmsXml(Context context) {
        this.context = context;
    }
 
    public void xmlStart() {
    	String path = FileUtil.getFilePath("SMSBackup");
//        String path = Environment.getExternalStorageDirectory().getPath() + "/SMSBackup";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(path, "message.xml");
        try {
            outStream = new FileOutputStream(file2);
            serializer = Xml.newSerializer();
            serializer.setOutput(outStream, "UTF-8");
            serializer.startDocument("UTF-8", true);
            serializer.startTag(null, "sms");
             
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public boolean createXml() throws Exception {
 
        this.xmlStart();
        Cursor cursor = null;
        try {
            ContentResolver conResolver = context.getContentResolver();
            String[] projection = new String[] { SmsField.ADDRESS, SmsField.PERSON, SmsField.DATE, SmsField.PROTOCOL,
                                                SmsField.READ, SmsField.STATUS, SmsField.TYPE, SmsField.REPLY_PATH_PRESENT,
                                                SmsField.BODY,SmsField.LOCKED,SmsField.ERROR_CODE, SmsField.SEEN }; // type=1是收件箱，==2是发件箱;read=0表示未读，read=1表示读过，seen=0表示未读，seen=1表示读过
            Uri uri = Uri.parse(SMS_URI_ALL);
            cursor = conResolver.query(uri, projection, null, null, "_id asc");
            if (cursor.moveToFirst()) {
                String address;
                String person;
                String date;
                String protocol;
                String read;
                String status;
                String type;
                String reply_path_present;
                String body;
                String locked;
                String error_code;
                String seen;
                do {
                    address = cursor.getString(cursor.getColumnIndex(SmsField.ADDRESS));
                    if (address == null) {
                        address = "";
                    }
                    person = cursor.getString(cursor.getColumnIndex(SmsField.PERSON));
                    if (person == null) {
                        person = "";
                    }
                    date = cursor.getString(cursor.getColumnIndex(SmsField.DATE));
                    if (date == null) {
                        date = "";
                    }
                    protocol = cursor.getString(cursor.getColumnIndex(SmsField.PROTOCOL));
                    if (protocol == null) {
                        protocol = "";
                    }
                    read = cursor.getString(cursor.getColumnIndex(SmsField.READ));
                    if (read == null) {
                        read = "";
                    }
                    status = cursor.getString(cursor.getColumnIndex(SmsField.STATUS));
                    if (status == null) {
                        status = "";
                    }
                    type = cursor.getString(cursor.getColumnIndex(SmsField.TYPE));
                    if (type == null) {
                        type = "";
                    }
                    reply_path_present = cursor.getString(cursor.getColumnIndex(SmsField.REPLY_PATH_PRESENT));
                    if (reply_path_present == null) {
                        reply_path_present = "";
                    }
                    body = cursor.getString(cursor.getColumnIndex(SmsField.BODY));
                    if (body == null) {
                        body = "";
                    }
                    locked = cursor.getString(cursor.getColumnIndex(SmsField.LOCKED));
                    if (locked == null) {
                        locked = "";
                    }
                    error_code = cursor.getString(cursor.getColumnIndex(SmsField.ERROR_CODE));
                    if (error_code == null) {
                        error_code = "";
                    }
                    seen = cursor.getString(cursor.getColumnIndex(SmsField.SEEN));
                    if (seen == null) {
                        seen = "";
                    }
                    serializer.startTag(null, "item");
                    serializer.attribute(null, SmsField.ADDRESS, address);
                    serializer.attribute(null, SmsField.PERSON, person);
                    serializer.attribute(null, SmsField.DATE, date);
                    serializer.attribute(null, SmsField.PROTOCOL, protocol);
                    serializer.attribute(null, SmsField.READ, read);
                    serializer.attribute(null, SmsField.STATUS, status);
                    serializer.attribute(null, SmsField.TYPE, type);
                    serializer.attribute(null, SmsField.REPLY_PATH_PRESENT, reply_path_present);
                    serializer.attribute(null, SmsField.BODY, body);
                    serializer.attribute(null, SmsField.LOCKED, locked);
                    serializer.attribute(null, SmsField.ERROR_CODE, error_code);
                    serializer.attribute(null, SmsField.SEEN, seen);
                    serializer.endTag(null, "item");
 
                } while (cursor.moveToNext());
            } else {
            	Toast.makeText(context, "备份失败",Toast.LENGTH_SHORT);
                return false;
            }
 
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            Log.d("SQLiteException:", ex.getMessage());
        }finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        serializer.endTag(null, "sms");
        serializer.endDocument();
        outStream.flush();
        outStream.close();
        Toast.makeText(context, "备份成功", Toast.LENGTH_SHORT);
        return true;
    }
}
