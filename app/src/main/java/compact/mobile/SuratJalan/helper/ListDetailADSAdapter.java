package compact.mobile.SuratJalan.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import compact.mobile.config.AWBList;

public class ListDetailADSAdapter extends BaseAdapter {

	TextView mNoAssigment, mNoAWB, mNamaToko, mStatus, mService, mType;
	CheckBox CB_NoAWB;
	
	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_NO_AWB = "no_awb";
	private static final String AR_NAMA_TOKO = "nama_toko";
	private static final String AR_STATUS = "status";
	private static final String AR_ALAMAT = "alamat";
	private static final String AR_SERVICE = "service";
	private static final String AR_TYPE = "type";
	
	private List<AWBList> listAWB;
	private LayoutInflater inflator;
	
	Context context;
	ArrayList<HashMap<String, String>> daftar_list_ads_detail;
	
	HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();
	
	public ListDetailADSAdapter(Context context, ArrayList<HashMap<String, String>> daftar_list_ads_detail){
		this.context = context;
		this.daftar_list_ads_detail = daftar_list_ads_detail;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return daftar_list_ads_detail.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return daftar_list_ads_detail.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		if (convertView == null) {
//            convertView = LayoutInflater.from(context).
//                inflate(R.layout.list_ads_detail_adapter, parent, false);
//        }
//		
//		// get current item to be displayed
//        HashMap<String, String> item = (HashMap<String, String>) getItem(position);
//        
//     // get the TextView for item name and item description
//        mNoAWB = (TextView) convertView.findViewById(R.id.isi_no_awb);
//        mStatus = (TextView) convertView.findViewById(R.id.isi_status);
//        mType = (TextView) convertView.findViewById(R.id.isi_type);
//        
//        mNoAWB.setText(item.get(AR_NO_AWB));
//        mStatus.setText(item.get(AR_STATUS));
//        mType.setText(item.get(AR_TYPE));
//        
////        if(item.get(AR_STATUS).equals("1")){
////        	mStatus.setText("Closed");
////        }else{
////        	mStatus.setText("Opened");
////        }
//        
////        if (item.get(AR_STATUS).equals("0")) {
////        	mStatus.setText("EDP");
////		} else if (item.get(AR_STATUS).equals("1")) {
////			mStatus.setText("PUP");
////		} else {
////			mStatus.setText("PDX");
////		}
//        
//		return convertView;
//	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
            convertView = LayoutInflater.from(context).
                inflate(compact.mobile.R.layout.list_ads_detail_adapter, parent, false);
        }
		
		// get current item to be displayed
        HashMap<String, String> item = (HashMap<String, String>) getItem(position);
        
        // get the TextView for item name and item description
        mNoAWB = (TextView) convertView.findViewById(compact.mobile.R.id.isi_no_awb);
        mStatus = (TextView) convertView.findViewById(compact.mobile.R.id.isi_status);
        mService = (TextView) convertView.findViewById(compact.mobile.R.id.isi_service);
        mType = (TextView) convertView.findViewById(compact.mobile.R.id.isi_type);
        
        mNoAWB.setText(item.get(AR_NO_AWB));
        mStatus.setText(item.get(AR_STATUS));
        mService.setText(item.get(AR_SERVICE));
        mType.setText(item.get(AR_TYPE));
		
//		CheckBox check = (CheckBox) convertView.findViewById(R.id.selected);
//		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				// TODO Auto-generated method stub
//				if (isChecked) {
//					state.put(position, isChecked);
//				} else {
//					state.remove(position);
//				}
//			}
//		});
//		check.setChecked((state.get(position) == null ? false : true));
        
        CB_NoAWB = (CheckBox) convertView.findViewById(compact.mobile.R.id.selected);
        CB_NoAWB.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			if (((CheckBox) v).isChecked()) {
    				String hmeteran = "1";
    				CB_NoAWB.setChecked(true);
    			} else {
    				String hmeteran = "0";
    				CB_NoAWB.setChecked(false);
    			}
    		}
    	});
		return convertView;
	}
	
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		View view = convertView;
//		ViewHolder holder = null;
//		if (convertView == null) {
//			convertView = inflator.inflate(R.layout.list_ads_detail_adapter, parent, false);
//			holder = new ViewHolder();
//			holder.title = (TextView) convertView.findViewById(R.id.isi_no_awb);
//			holder.chk = (CheckBox) convertView.findViewById(R.id.selected);
//			holder.chk
//					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//						@Override
//						public void onCheckedChanged(CompoundButton view,
//								boolean isChecked) {
//							int getPosition = (Integer) view.getTag();
//							listAWB.get(getPosition).setSelected(view.isChecked());
//
//						}
//					});
//			convertView.setTag(holder);
//			convertView.setTag(R.id.isi_no_awb, holder.title);
//			convertView.setTag(R.id.selected, holder.chk);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		holder.chk.setTag(position);
//
//		holder.title.setText(listAWB.get(position).getName());
//		holder.chk.setChecked(listAWB.get(position).isSelected());
//
//		return convertView;
//	}
//
//	static class ViewHolder {
//		protected TextView title;
//		protected CheckBox chk;
//	}

}
