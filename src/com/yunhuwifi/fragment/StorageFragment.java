package com.yunhuwifi.fragment;

import java.io.File;
import java.util.ArrayList;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.view.FileAdapter;

public class StorageFragment extends Fragment {
	 private static final String ROOT_PATH = "/";
	    private ArrayList<String> names = null;
	    private ArrayList<String> paths = null;
	    private View view;
	    private EditText editText;
	    private ListView lvstorage;
	    
	    
	    @Override
	    public View onCreateView(LayoutInflater layoutInflater,ViewGroup container,Bundle savedInstanceState){
	    	View v=layoutInflater.inflate(R.layout.fragment_storage, container, false);
	    	lvstorage=(ListView) v.findViewById(R.id.lvstorage);
	    	showFileDir(ROOT_PATH);
	    	setHasOptionsMenu(true);
			return v;
	    }
	    
	    
	    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater){
	    	menuInflater.inflate(R.menu.menu, menu);
	    	super.onCreateOptionsMenu(menu, menuInflater);
	    }
	    public boolean onOptionsItemSelected(MenuItem item){
	    	switch(item.getItemId()){
	    	case R.id.menunewfile:
	    		popupNewFile();
	    		break;
	    	default :
	    		break;
	    	}
			return false;
	    	
	    }
	
	    private void popupNewFile() {
			final Dialog dlg = new Dialog(getActivity());
			View dlgView = View.inflate(getActivity(),R.layout.dns_box,null);
			View ok = dlgView.findViewById(R.id.ok); 
			ok.setTag(dlg); 
			final TextView tvdialogtitle=(TextView)dlgView.findViewById(R.id.dialogtitle);  
			tvdialogtitle.setText("新建文件夹");
			final EditText first=(EditText)dlgView.findViewById(R.id.first);  
			final EditText backup = (EditText)dlgView.findViewById(R.id.backup); 
			backup.setVisibility(View.GONE);
			ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String ftxt=first.getText().toString();
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
	    
	    private void showFileDir(String path){
	        names = new ArrayList<String>();
	        paths = new ArrayList<String>();
	        File file = new File(path);
	        File[] files = file.listFiles();
	        
	        if (!ROOT_PATH.equals(path)){
	            names.add("@1");
	            paths.add(ROOT_PATH);
	            
	            names.add("@2");
	            paths.add(file.getParent());
	        }
	        for (File f : files){
	            names.add(f.getName());
	            paths.add(f.getPath());
	        }
	        lvstorage.setAdapter((new FileAdapter(getActivity(),names, paths)));
	        lvstorage.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					 String path = paths.get(position);
				        File file = new File(path);
				        if (file.exists() && file.canRead()){
				            if (file.isDirectory()){
				                showFileDir(path);
				            }
				            else{
				                fileHandle(file);
				            }
				        }
				        else{
				            Resources res = getResources();
				            new AlertDialog.Builder(getActivity()).setTitle("Message")
				            .setMessage("没有权限")
				            .setPositiveButton("OK",new OnClickListener() {
				                @Override
				                public void onClick(DialogInterface dialog, int which) {
				                    
				                }
				            }).show();
				        }
					
				}
			});
	    }

	    private void fileHandle(final File file){
	        OnClickListener listener = new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                if (which == 0){
	                    openFile(file);
	                }
	                else if(which == 1){
	                    LayoutInflater factory = LayoutInflater.from(getActivity());
	                    view = factory.inflate(R.layout.rename_box, null);
	                    editText = (EditText)view.findViewById(R.id.editText);
	                    editText.setText(file.getName());
	                    
	                    OnClickListener listener2 = new DialogInterface.OnClickListener() {
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            String modifyName = editText.getText().toString();
	                            final String fpath = file.getParentFile().getPath();
	                            final File newFile = new File(fpath + "/" + modifyName);
	                            if (newFile.exists()){
	                                if (!modifyName.equals(file.getName())){
	                                    new AlertDialog.Builder(getActivity())
	                                    .setTitle("注意!")
	                                    .setMessage("文件名已存在，是否覆盖？")
	                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                                        @Override
	                                        public void onClick(DialogInterface dialog, int which) {
	                                            if (file.renameTo(newFile)){
	                                                showFileDir(fpath);
	                                                displayToast("重命名成功！");
	                                            }
	                                            else{
	                                                displayToast("重命名失败！");
	                                            }
	                                        }
	                                    })
	                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                                        @Override
	                                        public void onClick(DialogInterface dialog, int which) {
	                                            
	                                        }
	                                    })
	                                    .show();
	                                }
	                            }
	                            else{
	                                if (file.renameTo(newFile)){
	                                    showFileDir(fpath);
	                                    displayToast("重命名成功！");
	                                }
	                                else{
	                                    displayToast("重命名失败！");
	                                }
	                            }
	                        }
	                    };
	                    AlertDialog renameDialog = new AlertDialog.Builder(getActivity()).create();
	                    renameDialog.setView(view);
	                    renameDialog.setButton("确定", listener2);
	                    renameDialog.setButton2("取消", new DialogInterface.OnClickListener() {
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            
	                        }
	                    });
	                    renameDialog.show();
	                }
	                else{
	                    new AlertDialog.Builder(getActivity())
	                    .setTitle("注意!")
	                    .setMessage("确定要删除此文件吗？")
	                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            if(file.delete()){
	                                //更新文件列表
	                                showFileDir(file.getParent());
	                                displayToast("删除成功！");
	                            }
	                            else{
	                                displayToast("删除失败！");
	                            }
	                        }
	                    })
	                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            
	                        }
	                    }).show();
	                }
	            }
	        };
	        String[] menu = {"打开","重命名","删除","复制","粘贴","详情"};
	        new AlertDialog.Builder(getActivity())
	        .setTitle("请选择要进行的操作!")
	        .setItems(menu, listener)
	        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                
	            }
	        }).show();
	    }
	    private void openFile(File file){
	        Intent intent = new Intent();
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent.setAction(android.content.Intent.ACTION_VIEW);
	        
	        String type = getMIMEType(file);
	        intent.setDataAndType(Uri.fromFile(file), type);
	        startActivity(intent);
	    }
	    private String getMIMEType(File file){
	        String type = "";
	        String name = file.getName();
	        String end = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
	        if (end.equals("m4a") || end.equals("mp3") || end.equals("wav")){
	            type = "audio";
	        }
	        else if(end.equals("mp4") || end.equals("3gp")) {
	            type = "video";
	        }
	        else if (end.equals("jpg") || end.equals("png") || end.equals("jpeg") || end.equals("bmp") || end.equals("gif")){
	            type = "image";
	        }
	        else {
	            type = "*";
	        }
	        type += "/*";
	        return type;
	    }
	    private void displayToast(String message){
	        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	    }
}
