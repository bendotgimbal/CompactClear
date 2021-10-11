package compact.mobile.SuratJalan.helper;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import compact.mobile.R;

public class ListDetailAWBOthersAdapter extends BaseAdapter {
	
	TextView mNoAWB, mStatus;
	
	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_NO_AWB = "no_awb";
	private static final String AR_STATUS = "status";
	private static final String AR_ALAMAT = "alamat";

	Context context;
	ArrayList<HashMap<String, String>> daftar_list_awb_others_detail;

	public ListDetailAWBOthersAdapter(Context context, ArrayList<HashMap<String, String>> daftar_list_awb_others_detail){
		this.context = context;
		this.daftar_list_awb_others_detail = daftar_list_awb_others_detail;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return daftar_list_awb_others_detail.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return daftar_list_awb_others_detail.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
            convertView = LayoutInflater.from(context).
                inflate(R.layout.list_awb_others_detail_adapter, parent, false);
        }
		
		// get current item to be displayed
        HashMap<String, String> item = (HashMap<String, String>) getItem(position);
        
     // get the TextView for item name and item description
        mNoAWB = (TextView) convertView.findViewById(R.id.isi_nomor_awb);
        mStatus = (TextView) convertView.findViewById(R.id.isi_status);
        
        mNoAWB.setText(item.get(AR_NO_AWB));
        mStatus.setText(item.get(AR_STATUS));
        
//        if(item.get(AR_STATUS).equals("1")){
//        	mStatus.setText("Closed");
//        }else{
//        	mStatus.setText("Opened");
//        }
        
//        if (item.get(AR_STATUS).equals("0")) {
//        	mStatus.setText("EDP");
//		} else if (item.get(AR_STATUS).equals("1")) {
//			mStatus.setText("PUP");
//		} else {
//			mStatus.setText("PDX");
//		}
		
		return convertView;
	}

}
