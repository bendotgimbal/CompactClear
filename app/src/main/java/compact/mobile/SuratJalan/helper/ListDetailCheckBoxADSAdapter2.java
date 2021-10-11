package compact.mobile.SuratJalan.helper;

import java.util.ArrayList;
import java.util.HashMap;

import compact.mobile.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ListDetailCheckBoxADSAdapter2 extends BaseAdapter {
	
	Context context;
	ArrayList<HashMap<String, Object>> listDataDetailAWB;	
	public HashMap<Integer, Boolean> ListAWBDetail = new HashMap<Integer, Boolean>();		

	//	public CheckboxAdapter(Context context,	ArrayList<HashMap<String, Object>> listData) {
	public ListDetailCheckBoxADSAdapter2(Context context,	ArrayList<HashMap<String, Object>> listDataDetailAWB) {
		this.context = context;
		this.listDataDetailAWB = listDataDetailAWB;	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listDataDetailAWB.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listDataDetailAWB.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	
		LayoutInflater mInflater = LayoutInflater.from(context);
		convertView = mInflater.inflate(R.layout.item2, null);
//		ImageView image = (ImageView) convertView.findViewById(R.id.friend_image);
//		image.setBackgroundResource((Integer) listDataDetailAWB.get(position).get("friend_image"));
		TextView mNoAWB = (TextView) convertView.findViewById(R.id.isi_no_awb);
		mNoAWB.setText((String) listDataDetailAWB.get(position).get("AR_NO_AWB"));
		TextView mService = (TextView) convertView.findViewById(R.id.isi_service);
		mService.setText((String) listDataDetailAWB.get(position).get("AR_SERVICE"));
		TextView mStatus = (TextView) convertView.findViewById(R.id.isi_status);
		mStatus.setText((String) listDataDetailAWB.get(position).get("AR_STATUS"));
		TextView mType = (TextView) convertView.findViewById(R.id.isi_type);
		mType.setText((String) listDataDetailAWB.get(position).get("AR_TYPE"));
		CheckBox check = (CheckBox) convertView.findViewById(R.id.selected);		
		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					ListAWBDetail.put(position, isChecked);					
				} else {
					ListAWBDetail.remove(position);				
				}
			}
		});
		check.setChecked((ListAWBDetail.get(position) == null ? false : true));
		return convertView;
	}
}
