package jp.androidgroup.nyartoolkit;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HttpGetClient extends AsyncTask<String, String, String>  {
	
	private String recommendModelname = new String();
	private AsyncTaskCallback callback = null;
	String _url;
	
	public interface AsyncTaskCallback {
        void preExecute();
        void postExecute(String result);
    }

	public HttpGetClient(AsyncTaskCallback atc){
//		mActivity = activity;
		this.callback = atc;
	}
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.preExecute();
    }
	
	@Override
	protected synchronized String doInBackground(String... contents) {
		
		HttpURLConnection http = null;
		InputStream in = null;
		
		try {
			// URLにHTTP接続
			_url = "http://www2.mkm.ic.kanagawa-it.ac.jp/~mkasahara/User_profile/Ranking/" + contents[0] + ".txt";
			URL url = new URL(_url);

			// URLにHTTP接続
			http = (HttpURLConnection)url.openConnection();
			http.setRequestMethod("GET");
			http.connect();
			
			// データを取得
			in = http.getInputStream();
			
			//テキストデータを読み出す
			byte[] line = new byte[512];
			int size;
			while(true) {
				size = in.read(line);
				if(size <= 0)
					break;
				recommendModelname += new String(line);
//				System.out.println("recommendModelname = " + recommendModelname);
			}
//			// HTMLソースを表示
//			httpget_text.setText(recommendModelname);
		} catch(Exception e) {
//			recommendModelname.setText(e.toString());
		} finally {
			try{
				if(http != null)
					http.disconnect();
				if(in != null)
					in.close();
			} catch (Exception e) {}
		}
		return recommendModelname;
//		return result;
	}
	
	@Override
	protected void onPostExecute(String recommendModelname) {
		// TODO 自動生成されたメソッド・スタブ
		super.onPostExecute(recommendModelname);
//		mActivity.OnCallback(recommendModelname);
		callback.postExecute(recommendModelname);
	}
}