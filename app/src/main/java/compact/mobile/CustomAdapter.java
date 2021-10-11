package compact.mobile;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

@SuppressLint({ "InflateParams", "DefaultLocale" })
public class CustomAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<GroupInfo> deptList;
	private ArrayList<GroupInfo> originalList = new ArrayList<GroupInfo>();

	public CustomAdapter(Context context, ArrayList<GroupInfo> deptList) {
		this.context = context;
		this.deptList = deptList;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ChildInfo> productList = deptList.get(groupPosition).getProductList();
		return productList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

		ChildInfo detailInfo = (ChildInfo) getChild(groupPosition, childPosition);
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.jx_scd_child, null);
		}

		TextView sequence = (TextView) view.findViewById(R.id.sequence);
		sequence.setText(detailInfo.getSequence().trim() + ". ");
		TextView childItem = (TextView) view.findViewById(R.id.childItem);
		childItem.setText(detailInfo.getName().trim());

		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		ArrayList<ChildInfo> productList = deptList.get(groupPosition).getProductList();
		return productList.size();

	}

	@Override
	public Object getGroup(int groupPosition) {
		return deptList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return deptList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {

		GroupInfo headerInfo = (GroupInfo) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.jx_scd_group, null);
		}
		String[] spResponse = headerInfo.getName().trim().split("-");

		TextView heading = (TextView) view.findViewById(R.id.heading);
		TextView heading2 = (TextView) view.findViewById(R.id.heading2);
		TextView heading3 = (TextView) view.findViewById(R.id.heading3);

		heading.setText(spResponse[0]);
		heading2.setText(spResponse[1]);
		heading3.setText(spResponse[2]);

		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@SuppressLint("NewApi")
	public void filterData(String jx_query) {
		if (originalList.size() < 1) {
			originalList.addAll(deptList);
			Log.d("JX LVS SEARCH", "OriginalList Masukkin => " + originalList.size());
		}

		deptList.clear();
		Log.d("JX LVS SEARCH", "Originalnya => " + originalList.size());
		Log.d("JX LVS SEARCH", "detList     => " + deptList.size());
		jx_query = jx_query.toLowerCase();

		if (jx_query.isEmpty()) {

			deptList.addAll(originalList);
			Log.d("JX LVS SEARCH", "EMPTY NIH");

		} else {

			for (GroupInfo groupInfo : originalList) {
				ArrayList<ChildInfo> childInfoList = groupInfo.getProductList();
				if (groupInfo.getName().toLowerCase().contains(jx_query)) {
					Log.d("JX LVS SEARCH", "GRUOP MASUK LIST SEARCH ");
					deptList.add(groupInfo);
				} else {
					for (ChildInfo childInfo : childInfoList) {
						// Log.d("JX LVS SEARCH", "CHILD MASUK LIST SEARCH
						// "+childInfo.getName().toLowerCase());
						if (childInfo.getName().toLowerCase().contains(jx_query)) {

							// deptList.add(nGroupInfo);
							if (deptList.contains(groupInfo)) {
								Log.d("JX LVS SEARCH", "GROUP MASUK " + groupInfo.getName());
							} else {
								Log.d("JX LVS SEARCH", "GROUP KAGAK IN " + groupInfo.getName());
								deptList.add(groupInfo);
							}
						}
					}
				}

			}

		}

		notifyDataSetChanged();
	}

	public void deleteList() {
		deptList.clear();
		notifyDataSetChanged();
	}
}
