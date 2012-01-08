package cn.lzu.edu.webdav;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SardineActivity extends Activity {
	private String ROOT = "http://202.201.1.135:30080/mnt/li/lzu1/s1/";
	private String TAG = "Sardine";
	
	private ListView mList;
	
	private ArrayAdapter<String> mAdapter; 
	
	private List<String> mListData;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mList = (ListView)findViewById(R.id.mNasList);
        initListData();
        
        mAdapter = new ArrayAdapter<String>(this, R.layout.item, mListData);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new ClickListListener());
        setTitle("NasStorage Demo");
	}
	
	 private void initListData(){
	    	Resources localResource = this.getResources();
	    	mListData = new ArrayList<String>();
	    	mListData.add(localResource.getString(R.string.nas_file));
	    	mListData.add(localResource.getString(R.string.local_file));
	    }
	    
	    class ClickListListener implements OnItemClickListener{

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent localIntent = new Intent();
				switch(arg2){
				case 0:
					localIntent.setClass(SardineActivity.this, NasStorageNasList.class);
					break;
				case 1:
//					localIntent.setClass(SardineActivity.this, NasStorageLocalList.class);
					break;
				}
				SardineActivity.this.startActivity(localIntent);
				SardineActivity.this.finish();
			}
	    	
	    }
}
		/*Sardine sardine = SardineFactory.begin("lzu", "nopasswd");
		List<DavResource> resources;
		try {
			resources = sardine.list(ROOT + "nihao/");
			for (DavResource res : resources) {
				System.out.println("Res: " + res.getName());
			}
			sardine.copy(ROOT + "aliii", ROOT + "justtest/aliii");
			 保存文件到sd卡，并命名为test.txt
			 * InputStream fis = sardine.get(ROOT + "hist.log");
			FileOutputStream fos=new FileOutputStream( "/mnt/sdcard/test.txt");
			byte[] buffer = new byte[1444];
			int byteread=0;
			while((byteread=fis.read(buffer))!=-1) { 
			      fos.write(buffer,0,byteread); 
			  }   
			fis.close();
			fos.close();
			InputStream fis = new FileInputStream(new File(
			"/mnt/sdcard/HttpHost.java"));
			sardine.put(ROOT + "nihao/HttpHost.java", fis);
		} catch (IOException e) {
			Log.i("xiao", "====================================");
			Log.i("xiao", "Error = " + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Serializer serializer = new Persister();

		// deserializer
		try {
			InputStream source = getAssets().open("ms.xml");
			MS multistatus = serializer.read(MS.class, source, false);
			Log.i(TAG, "Read multistatus " + multistatus.toString());
			for (Response res : multistatus.getResponse()) {
				Log.i(TAG, "Response " + res.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// serializer
		Example example = new Example("Example message", 123);
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			serializer.write(example, out);
			Log.i(TAG, "Out " + out.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
*/