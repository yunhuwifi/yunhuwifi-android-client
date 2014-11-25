package com.yunhuwifi.models;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Data;
import android.widget.Toast;

import com.yunhuwifi.util.FileUtil;
import com.yunhuwifi.vcard.ContactStruct;
import com.yunhuwifi.vcard.Contacts;
import com.yunhuwifi.vcard.VCardComposer;
import com.yunhuwifi.vcard.VCardException;
import com.yunhuwifi.vcard.VCardParser;
import com.yunhuwifi.vcard.VDataBuilder;
import com.yunhuwifi.vcard.VNode;
import com.yunhuwifi.vcard.ContactStruct.ContactMethod;
import com.yunhuwifi.vcard.ContactStruct.PhoneData;


public class ContactInfo {

    private String name;
    
    public static class PhoneInfo{
        public int type;
        public String number;
    }
    
    public static class EmailInfo{
        public int type;
        public String email;
    }
    
    private List<PhoneInfo> phoneList = new ArrayList<PhoneInfo>();
    private List<EmailInfo> email = new ArrayList<EmailInfo>(); 

    public ContactInfo(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    public ContactInfo setName(String name) {
        this.name = name;
        return this;
    }
    public List<PhoneInfo> getPhoneList() {
        return phoneList;
    }
    public ContactInfo setPhoneList(List<PhoneInfo> phoneList) {
        this.phoneList = phoneList;
        return this;
    }
    public List<EmailInfo> getEmail() {
        return email;
    }
    public ContactInfo setEmail(List<EmailInfo> email) {
        this.email = email;
        return this;
    }

    @Override
    public String toString() {
        return "{name: "+name+", number: "+phoneList+", email: "+email+"}";
    }
    
    public static class ContactHandler {

        private static ContactHandler instance_ = new ContactHandler();
        
        public static ContactHandler getInstance(){
            return instance_;
        }
        
        public Cursor queryContact(Activity context, String[] projection){
            Cursor cur = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
            return cur;
        }
        
        public List<ContactInfo> getContactInfo(Activity context){
            List<ContactInfo> infoList = new ArrayList<ContactInfo>();
            
            Cursor cur = queryContact(context, null);
            
            if(cur.moveToFirst()){
                do{
                    
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String displayName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    ContactInfo info = new ContactInfo(displayName);
                    
                    int phoneCount = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    
                    if(phoneCount>0){
                        
                        Cursor phonesCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id , null, null);
                        
                        if(phonesCursor.moveToFirst()) {
                            List<ContactInfo.PhoneInfo> phoneNumberList = new ArrayList<ContactInfo.PhoneInfo>();
                            do{
                                String phoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                int type = phonesCursor.getInt(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                                
                                ContactInfo.PhoneInfo phoneInfo = new ContactInfo.PhoneInfo();
                                phoneInfo.type=type;
                                phoneInfo.number=phoneNumber;
                                
                                phoneNumberList.add(phoneInfo);
                            }while(phonesCursor.moveToNext());
                            info.setPhoneList(phoneNumberList);
                        }
                    }
                    
                    Cursor emailCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID+"="+id, null, null);
                    
                    if(emailCur.moveToFirst()){
                        List<ContactInfo.EmailInfo> emailList = new ArrayList<ContactInfo.EmailInfo>();
                        do{
                            String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
                            int type = emailCur.getInt(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                            
                            ContactInfo.EmailInfo emailInfo=new ContactInfo.EmailInfo();
                            emailInfo.type=type;    
                            emailInfo.email=email;    
                            
                            emailList.add(emailInfo);
                        }while(emailCur.moveToNext());
                        
                        info.setEmail(emailList);
                    }
                    
                    infoList.add(info);
                }while(cur.moveToNext());
            }
            return infoList;
        }
        
        public void backupContacts(Activity context, List<ContactInfo> infos){
            
            try {
            	String path = FileUtil.getFilePath("contacts.vcf");
//                String path = Environment.getExternalStorageDirectory() + "/contacts.vcf";
                
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path),"UTF-8");
                
                VCardComposer composer = new VCardComposer();
                
                for (ContactInfo info : infos)
                {
                    ContactStruct contact = new ContactStruct();
                    contact.name = info.getName();
                    List<ContactInfo.PhoneInfo> numberList = info
                            .getPhoneList();
                    for (ContactInfo.PhoneInfo phoneInfo : numberList)
                    {
                        contact.addPhone(phoneInfo.type, phoneInfo.number,
                                null, true);
                    }
                    List<ContactInfo.EmailInfo> emailList = info.getEmail();
                    for (ContactInfo.EmailInfo emailInfo : emailList)
                    {
                        contact.addContactmethod(Contacts.KIND_EMAIL,
                                emailInfo.type, emailInfo.email, null, true);
                    }
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
            
            Toast.makeText(context, "备份成功！", Toast.LENGTH_SHORT).show();
        }
        
        
        public List<ContactInfo> restoreContacts() throws Exception {
            List<ContactInfo> contactInfoList = new ArrayList<ContactInfo>();
            VCardParser parse = new VCardParser();
            VDataBuilder builder = new VDataBuilder();
            String file = Environment.getExternalStorageDirectory() + "/contacts.vcf";
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            
            String vcardString = "";
            String line;
            while((line = reader.readLine()) != null) {
                vcardString += line + "\n";
            }
            reader.close();
            
            boolean parsed = parse.parse(vcardString, "UTF-8", builder);
            
            if(!parsed){
                throw new VCardException("Could not parse vCard file: "+ file);
            }
            
            List<VNode> pimContacts = builder.vNodeList;
            
            for (VNode contact : pimContacts) {

                ContactStruct contactStruct=ContactStruct.constructContactFromVNode(contact, 1);
                List<PhoneData> phoneDataList = contactStruct.phoneList;
                List<ContactInfo.PhoneInfo> phoneInfoList = new ArrayList<ContactInfo.PhoneInfo>();
                for(PhoneData phoneData : phoneDataList){
                    ContactInfo.PhoneInfo phoneInfo = new ContactInfo.PhoneInfo();
                    phoneInfo.number=phoneData.data;
                    phoneInfo.type=phoneData.type;
                    phoneInfoList.add(phoneInfo);
                }
                
                List<ContactMethod> emailList = contactStruct.contactmethodList;
                List<ContactInfo.EmailInfo> emailInfoList = new ArrayList<ContactInfo.EmailInfo>();
                if (null!=emailList)
                {
                    for (ContactMethod contactMethod : emailList)
                    {
                        if (Contacts.KIND_EMAIL == contactMethod.kind)
                        {
                            ContactInfo.EmailInfo emailInfo = new ContactInfo.EmailInfo();
                            emailInfo.email = contactMethod.data;
                            emailInfo.type = contactMethod.type;
                            emailInfoList.add(emailInfo);
                        }
                    }
                }
                ContactInfo info = new ContactInfo(contactStruct.name).setPhoneList(phoneInfoList).setEmail(emailInfoList);
                contactInfoList.add(info);
            }
          
            return contactInfoList;
        }

        
        public void addContacts(Activity context, ContactInfo info){
            ContentValues values = new ContentValues();
            Uri rawContactUri = context.getContentResolver().insert(RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);
            
            values.clear();
            values.put(Data.RAW_CONTACT_ID, rawContactId);
            values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
            values.put(StructuredName.GIVEN_NAME, info.getName());
            context.getContentResolver().insert(
                    android.provider.ContactsContract.Data.CONTENT_URI, values);
            
            List<ContactInfo.PhoneInfo> phoneList = info.getPhoneList();
            for (ContactInfo.PhoneInfo phoneInfo : phoneList) {
                values.clear();
                values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
                values.put(Phone.NUMBER, phoneInfo.number);
                values.put(Phone.TYPE, phoneInfo.type);
                context.getContentResolver().insert(
                        android.provider.ContactsContract.Data.CONTENT_URI, values);
            }
            
            List<ContactInfo.EmailInfo> emailList = info.getEmail();
            
            for (ContactInfo.EmailInfo email : emailList) {
                values.clear();
                values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
                values.put(Email.DATA, email.email);
                values.put(Email.TYPE, email.type);
                context.getContentResolver().insert(
                        android.provider.ContactsContract.Data.CONTENT_URI, values);
            }
            
        }
        
    }
}

