package jp.androidgroup.nyartoolkit;

import java.util.ArrayList;
import android.app.ActionBar.LayoutParams;
import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;


public class SetRecommendListView{
	
	public ArrayList<String> SetListViewContents(ArrayList<String> al){
		
		al.add("Papilio Maackii");
		al.add("Papilio Maackii");
		al.add("Papilio Maackii");
		al.add("Papilio Maackii");
		al.add("Papilio Maackii");
		return al;
		
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
