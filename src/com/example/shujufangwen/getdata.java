package com.example.shujufangwen;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class getdata {
	
	public static void getweatherdatafromsql(Context context){
		
	}
	
	public static void getweatherdatafrominter(final Handler handler, final String ip) {
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					HttpPost hPost = new HttpPost("http://"+ip+":8888");

					JSONObject jsonRequest = new JSONObject();
					jsonRequest.put("action", "get");
					jsonRequest.put("object", "agriculture");

					StringEntity se = new StringEntity(jsonRequest.toString());
					hPost.setEntity(se);
					HttpClient hcClient = new DefaultHttpClient();
					HttpResponse hRequest = hcClient.execute(hPost);
					int i = hRequest.getStatusLine().getStatusCode();
					if (i == 200) {
						InputStream isInputStream = hRequest.getEntity()
								.getContent();
						ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
						int len;
						byte[] buf = new byte[1024];
						while ((len = isInputStream.read(buf)) != -1) {
							baOutputStream.write(buf, 0, len);
						}
						isInputStream.close();
						final String string = baOutputStream.toString();
						baOutputStream.close();
						msg.what = 0;
						msg.obj = string;
					}else{
						msg.what = 1;
						msg.obj = i;
					}
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = 2;
				}
				handler.sendMessage(msg);
			};
		}.start();
	}
}
