package org.takanolab.ar.log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientPost2 {
	public String doPost(String params )
	{
		//HTTP通信用URL
		String url = "http://www2.mkm.ic.kanagawa-it.ac.jp/~mkasahara/ARBrowser/Log/FileWright.php";
//		String url = "http://www2.mkm.ic.kanagawa-it.ac.jp/~mkasahara/ARBrowser/Log/FileWright.php?data=" + params;
	    try
	    {            
	        HttpPost method = new HttpPost( url );
	        
	        DefaultHttpClient client = new DefaultHttpClient();
	        
	        // POST データの設定
	        StringEntity paramEntity = new StringEntity( params );
	        paramEntity.setChunked( false );
	        paramEntity.setContentType( "application/x-www-form-urlencoded" );
	        method.setEntity( paramEntity );
	        
//	        System.out.println(params);
	        
	        HttpResponse response = client.execute( method );
	        int status = response.getStatusLine().getStatusCode();
	        if ( status != HttpStatus.SC_OK )
	            throw new Exception( "" );
	        
	        return EntityUtils.toString( response.getEntity(), "UTF-8" );
	    }
	    catch ( Exception e )
	    {
	        return null;
	    }
	}
}
