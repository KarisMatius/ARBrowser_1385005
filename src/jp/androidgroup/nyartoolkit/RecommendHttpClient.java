package jp.androidgroup.nyartoolkit;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class RecommendHttpClient extends AsyncTask<String, Void, Integer> {
	@Override
	protected synchronized Integer doInBackground(String... contents) {
		//HTTP通信用URL
		String url = "http://www2.mkm.ic.kanagawa-it.ac.jp/~mkasahara/User_profile/Ranking/GetRecommendModelname.php?data=" + contents[0];
		
		HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        System.out.println(url);
        HttpResponse res = null;
        try {
            res = httpclient.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return null;
	}
	
	@Override
    protected void onPreExecute() {
//        Log.i("AsyncTask", "onPreExecute");
    }
}
