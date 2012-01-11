package cn.lzu.edu.webdav;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class NasStorageLocalList extends Activity {
    public static final int UPLOAD_TOAST = 1;
	
	private String ROOT = "http://202.201.1.135:30080/mnt/li/lzu1/s1/";
	private Sardine sardine = null;
	
	Resources localResources = null;
	
	private final int CREATE_FOLDER = 1;
	private int mPresentClick = 0;

	private int mPictures[];
	
	private String tag = "xiao";
	private String NasRootPath;
	
	private ListView mFileDirList;
	
	private File mPresentFile;
	private List<File> list = null;
	private ArrayList<HashMap<String, Object>> recordItem;
	private File[] localFiles;
	
	BroadcastReceiver mExternalStorageReceiver;
	static UpdateUiHandler mUpdateUiHandler;
	private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.server_files);
        initVariable();
        createDirectory();    
        listFile();
        localResources = this.getResources();
    }
    
    public void initVariable(){
    	mFileDirList = (ListView)findViewById(R.id.mServerList);
    	mPictures = new int[]{R.drawable.back, R.drawable.dir, R.drawable.doc};
    	mUpdateUiHandler = new UpdateUiHandler();
    	sardine = SardineFactory.begin("lzu", "nopasswd");
    }
    
    private Handler handler = new Handler() {               
        public void handleMessage(Message message) {
            progressDialog.dismiss(); // 关闭进度条
            listFile();
            if(message.what == 1) {
            	Toast.makeText(NasStorageLocalList.this, localResources.getString(R.string.upload_toast), Toast.LENGTH_SHORT).show();
            }
        }
	};
    
    public void createDirectory(){
    	File localPath = android.os.Environment.getExternalStorageDirectory();
    	
    	if(localPath.exists()){
    		Log.i(tag, "localPath.exists");
    		NasRootPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/NasStorage".trim();
    		localPath = new File(NasRootPath);
    		mPresentFile = localPath;
    		Log.i(tag, "path = " + android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/NasStorage");
    		if(!localPath.exists()){
    			Log.i(tag, "mkdir");
    			localPath.mkdirs();
    		}
    	}
    }
    
    public void listFile(){
    	File localPath = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath());
    	localFiles = localPath.listFiles();
    	setTitle(localPath.getAbsolutePath());
    	fillFile(localFiles);
    }
    
    public void fillFile(File[] paramFiles){
    	SimpleAdapter adapter = null;
		recordItem = new ArrayList<HashMap<String, Object>>();
		list = new ArrayList<File>();
		for(File f: paramFiles){
			if(Invalid(f) == 0){
				list.add(f);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("picture", mPictures[1]);
				map.put("name", f.getName());
				recordItem.add(map);
			}
			if(Invalid(f) == 1){
				list.add(f);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("picture", mPictures[2]);
				map.put("name", f.getName());
				recordItem.add(map);
			}
		}
		adapter = new SimpleAdapter(this, recordItem, R.layout.local_item, new String[]{"picture", "name"}, new int[]{R.id.local_picture, R.id.local_text});
		mFileDirList.setAdapter(adapter);
		mFileDirList.setOnItemClickListener(new FileChooserListener());
		mFileDirList.setOnItemLongClickListener(new LongClickListener());
    }
    
    private int Invalid(File f){
		if(f.isDirectory()){
			return 0;
		}
		else{
			return 1;
		}
	}
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	
    	menu.add(0, CREATE_FOLDER, 0, R.string.create_folder);
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case CREATE_FOLDER:
    		createDialog().show();
    		break;
    	}
    	
    	return true;
    }
    
    protected void onDestroy(){
    	Log.i(tag, "onDestroy");
    	super.onDestroy();
    }
    
    private class FileChooserListener implements OnItemClickListener{
		
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			File file = list.get(arg2);
			mPresentFile = file;
			if(file.isDirectory()){
				setTitle(file.getAbsolutePath());
				File[] files = file.listFiles();
				fillFile(files);
			}
		}
	}
    public Dialog createDialog(){
	    AlertDialog.Builder builder = new Builder(this);
		final View layout = View.inflate(this, R.layout.create_new_folder, null);
		final EditText localFileName = (EditText)layout.findViewById(R.id.folder_name);
		
		builder.setTitle(this.getResources().getString(R.string.create_folder));
		builder.setView(layout);
		builder.setPositiveButton(localResources.getString(R.string.ok), new OnClickListener(){
	
		
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				File localFile = mPresentFile;
				String localString = localFileName.getText().toString().trim();
				if(localString == "" || localString.equals("")){
					localString = localResources.getString(R.string.create_folder);
				}
	    		localFile = new File(localFile.getAbsolutePath() + "/" + localString);
	    		if(!localFile.exists()){
	    			localFile.mkdir();
	    		}
	    		else{
	    			Toast.makeText(NasStorageLocalList.this, localResources.getString(R.string.rename), Toast.LENGTH_LONG).show();
	    		}	
	    		setTitle(mPresentFile.getAbsolutePath());
	    		fillFile(mPresentFile.listFiles());
				dialog.dismiss();
			}
		});
		
		builder.setNegativeButton(localResources.getString(R.string.cancel), new OnClickListener(){
	
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();
			}
		});
		return builder.create();
    }
    
    class LongClickListener implements OnItemLongClickListener{

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			mPresentClick = arg2;
			createFunctionDialog().show();
			return false;
		}
    	
    }
    
    public Dialog createFunctionDialog(){
	    AlertDialog.Builder builder = new Builder(this);
	    String []localArray = {localResources.getString(R.string.upload_file), localResources.getString(R.string.delete)};
	    builder.setItems(localArray, new OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case 0:
					progressDialog = ProgressDialog.show(NasStorageLocalList.this, localResources.getString(R.string.upload_dialog_tile), localResources.getString(R.string.dialog_mess));
					new UpLoadThread().run();
					break;
				case 1:
					localFiles[mPresentClick].delete();
					listFile();
					break;
				}
			}
	    	
	    });
		return builder.create();
    }
    
    class UpLoadThread implements Runnable{

		public void run() {
			uploadFile();
			Message msg_uploadedData = new Message();
			msg_uploadedData.what = UPLOAD_TOAST;
       	 	handler.sendMessageDelayed(msg_uploadedData, 500);
		}
    	
    }
    
    public void uploadFile() {
    	InputStream fis;
		File t = localFiles[mPresentClick];
		try {
			fis = new FileInputStream(t);
			sardine.put(ROOT + t.getName(), fis);
			fis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	public class UpdateUiHandler extends Handler{
		public void handleMessage(final Message msg) {  
			NasStorageLocalList.this.setTitle("已上传" + msg.what + "%");
		}
	}

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param actionUrl
	 * @param params
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public static String post(String actionUrl, Map<String, String> params,
			Map<String, File> files, long length) throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

		Log.i("xiao", "HttpURLConnection");

		conn.setReadTimeout(5 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		Log.i("xiao", "before DataOutputStream ");

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// 发送文件数据
		if (files != null) {
			int i = 0;
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\"file"
						+ (i++) + "\"; filename=\"" + file.getKey() + "\""
						+ LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				int percent;
				Log.i("xiao", "length = " + length);
				int number = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
					number += len;
					percent = (int)(number * 100 / length);
					Log.i("xiao", "len = " + len);
					Log.i("xiao", "percent = " + percent);
					Message localMessage = new Message();
					localMessage.what = percent;
					mUpdateUiHandler.sendMessage(localMessage);
				}

				is.close();
				outStream.write(LINEND.getBytes());
			}
		}
		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();

		// 得到响应码
		int res = conn.getResponseCode();
		InputStream in = null;
		if (res == 200) {
			in = conn.getInputStream();
			int ch;
			StringBuilder sb2 = new StringBuilder();
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
		}
		return in == null ? null : in.toString();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		Intent localIntent = new Intent();
    		localIntent.setClass(NasStorageLocalList.this, SardineActivity.class);
    		NasStorageLocalList.this.startActivity(localIntent);
    		NasStorageLocalList.this.finish();
    	}
    	return false;
    }
}