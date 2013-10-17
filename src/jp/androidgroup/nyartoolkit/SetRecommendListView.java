package jp.androidgroup.nyartoolkit;

import java.util.ArrayList;

import jp.androidgroup.nyartoolkit.HttpGetClient.AsyncTaskCallback;
import android.app.ActionBar.LayoutParams;
import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;


public class SetRecommendListView{
	
	String textName;
	String[] modelnames;
	ArrayList<String> Recomendmodelnames;
	
	SetRecommendListView(String textName){
		this.textName = textName;
	}
	
	public ArrayList<String> SetListViewContents(ArrayList<String> al){
		Recomendmodelnames = al;
		
		HttpGetClient hgc = new HttpGetClient(new AsyncTaskCallback() {
	        public void preExecute() {
	            //だいたいの場合ダイアログの表示などを行う
	        }
	        public void postExecute(String result) {
	            //AsyncTaskの結果を受け取り「，」で分割し配列に格納
	        	modelnames = result.split(",", 0);
	        	
	        	for(int i=0; i<5; i++){
	        		Recomendmodelnames.add(modelnames[i]);
//	        		System.out.println(modelnames[i]);
	        		System.out.println(Recomendmodelnames);
	        	}
	        	
	        }
		});
		hgc.execute(textName);
		return Recomendmodelnames;
		
//		adapter.addList(user);
//		//アダプタに対してデータが変更したことを知らせる
//		adapter.notifyDataSetChanged();
		
	}
	public ArrayList<String> SetListViewContents2(ArrayList<String> al){
		
		al.add("Ladybug");
		al.add("Ladybug");
		al.add("Ladybug");
		al.add("Ladybug");
		al.add("Ladybug");
		return al;
		
//		adapter.addList(user);
//		//アダプタに対してデータが変更したことを知らせる
//		adapter.notifyDataSetChanged();
		
	}
}
