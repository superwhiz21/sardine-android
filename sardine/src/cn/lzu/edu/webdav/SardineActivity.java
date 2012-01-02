package cn.lzu.edu.webdav;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SardineActivity extends Activity {
	private String ROOT = "http://202.201.1.135:30080/mnt/li/lzu1/s1/";
	private String TAG = "Sardine";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Log.i("xiao", "Resource ===  before begin" );
		Sardine sardine = SardineFactory.begin("lzu", "nopasswd");
		Log.i("xiao", "Resource ===  after begin" );
		List<DavResource> resources;
		try {
			Log.i("xiao", "before list");
			resources = sardine.list(ROOT);
			Log.i("xiao", "Resource ===  after list" );
			for (DavResource res : resources) {
				Log.i("xiao", "Resource ===  " + res);
				System.out.println("Res: " + res);
			}
			/*InputStream fis = new FileInputStream(new File(
			"/mnt/sdcard/HelloWorld1.txt"));*/
			// "/mnt/sdcard/iLauncher/cn.lzu.edu.androidcontrol.png"));
			//sardine.put(ROOT + "HelloWorld1.txt", fis);
		} catch (IOException e) {
			Log.i("xiao", "====================================");
			Log.i("xiao", "Error = " + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*Serializer serializer = new Persister();

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
		}*/

	}
}