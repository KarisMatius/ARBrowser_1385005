//package jp.androidgroup.nyartoolkit;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.TextView;
//
//public class MyExpandableListAdapter extends BaseExpandableListAdapter {
//	 
//	 
//	  private LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	 
//	  @Override
//	  public Object getChild(int groupPosition, int childPosition) {
//	    // TODO 自動生成されたメソッド・スタブ
//	    return allChildList.get(groupPosition).get(childPosition);
//	  }
//	 
//	  @Override
//	  public long getChildId(int groupPosition, int childPosition) {
//	    // TODO 自動生成されたメソッド・スタブ
//	    return childPosition;
//	  }
//	 
//	  @Override
//	  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//	    // TODO 自動生成されたメソッド・スタブ
//	 
//	    if (convertView == null) {
//	      convertView = inflater.inflate(R.layout.test_explist_cell_c, parent, false);
//	    }
//	    TextView cont_tv = (TextView)convertView.findViewById(R.id.cont_tv);
//	 
//	    cont_tv.setText(allChildList.get(groupPosition).get(childPosition).get(sum_st).toString());
//	    return convertView;
//	  }
//	 
//	  @Override
//	  public int getChildrenCount(int groupPosition) {
//	    // TODO 自動生成されたメソッド・スタブ
//	    return allChildList.get(groupPosition).size();
//	  }
//	 
//	  @Override
//	  public Object getGroup(int groupPosition) {
//	    // TODO 自動生成されたメソッド・スタブ
//	    return parentList.get(groupPosition);
//	  }
//	 
//	  @Override
//	  public int getGroupCount() {
//	    // TODO 自動生成されたメソッド・スタブ
//	    return parentList.size();
//	  }
//	 
//	  @Override
//	  public long getGroupId(int groupPosition) {
//	    // TODO 自動生成されたメソッド・スタブ
//	    return groupPosition;
//	  }
//	 
//	  @Override
//	  public boolean hasStableIds() {
//	    // TODO 自動生成されたメソッド・スタブ
//	    return true;
//	  }
//	 
//	  @Override
//	  public boolean isChildSelectable(int groupPosition, int childPosition) {
//	    // TODO 自動生成されたメソッド・スタブ
//	    return true;
//	  }
//
//	@Override
//	public View getChildView(int groupPosition, int childPosition,
//			boolean isLastChild, View convertView, ViewGroup parent) {
//		// TODO 自動生成されたメソッド・スタブ
//		return null;
//	}
//
//	@Override
//	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//		// TODO 自動生成されたメソッド・スタブ
//		 if (convertView == null) {
//		      convertView = inflater.inflate(R.layout.test_explist_cell_p, parent, false);
//		    }
//		    TextView group_tv = (TextView)convertView.findViewById(R.id.group_tv);
//		    group_tv.setText(parentList.get(groupPosition).get(ttl_st).toString());
//		    return convertView;
//	}
//	 
//	}
