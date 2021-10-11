package compact.mobile.SuratJalan.helper;

import java.util.HashMap;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import compact.mobile.R;

public class ListAWBOthersAdapter extends BaseAdapter {
	
	TextView mNoAssigment, mNamaCust, mJumlah, mNoPasscode;
	private static final String AR_A = "asigment";
	private static final String AR_B = "kode_customer";
	private static final String AR_C = "nama_customer";
	private static final String AR_C_NAMA_CUST_SHORTEN = "sort_nama_customer";
	private static final String AR_D = "status";
	private static final String AR_E = "jumlah";
	private static final String AR_F = "no_pascode";
	
	Context context;
	ArrayList<HashMap<String, String>> daftar_list_awb_others;

	public ListAWBOthersAdapter(Context context, ArrayList<HashMap<String, String>> daftar_list_awb_others){
		this.context = context;
		this.daftar_list_awb_others = daftar_list_awb_others;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return daftar_list_awb_others.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return daftar_list_awb_others.get(position);
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
                inflate(R.layout.list_awb_others_adapter, parent, false);
        }
		
		// get current item to be displayed
        HashMap<String, String> item = (HashMap<String, String>) getItem(position);
        
     // get the TextView for item name and item description
        mNamaCust = (TextView) convertView.findViewById(R.id.isi_nama_customer);
        mJumlah = (TextView) convertView.findViewById(R.id.isi_jumlah);
        mNoPasscode = (TextView) convertView.findViewById(R.id.isi_no_passcode);
        
        mNamaCust.setText(item.get(AR_C_NAMA_CUST_SHORTEN));
//        mNamaCust.setText(item.get(AR_C));
        mJumlah.setText(item.get(AR_E));
        mNoPasscode.setText(item.get("Kosong"));
		return convertView;
	}

}
