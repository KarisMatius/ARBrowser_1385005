package org.takanolab.ar.log;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class HttpClientPost extends AsyncTask<String, Void, Integer> {
	
	@Override
	protected Integer doInBackground(String... contents) {
		//HTTP通信用URL
		String url = "http://www2.mkm.ic.kanagawa-it.ac.jp/~mkasahara/User_profile/Log/FileWright.php?data=" + contents[0];
		
		HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        
//        ArrayList <NameValuePair> params = new ArrayList <NameValuePair>();
//        params.add( new BasicNameValuePair("content", contents[0]));
        
        System.out.println(contents[0]);
        
        HttpResponse res = null;
        try {
//        	post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            res = httpclient.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return null;
	}
	
	@Override
    protected void onPreExecute() {
        Log.i("AsyncTask", "onPreExecute");
    }
}
