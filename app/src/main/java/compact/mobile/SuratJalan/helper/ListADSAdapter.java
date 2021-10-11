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

public class ListADSAdapter extends BaseAdapter {
	
	TextView mNoAssigment, mNamaToko, mStatusAssigment, mJml, mNoPasscode;
	
	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_KODE_TOKO = "kode_toko";
	private static final String AR_NAMA_TOKO = "nama_toko";
	private static final String AR_NAMA_COMPANY = "company";
	private static final String AR_NAMA_TOKO_SHORTEN = "sort_nama_toko";
	private static final String AR_STATUS = "status";
	private static final String AR_JUMLAH = "jumlah";
	private static final String AR_NO_PASCODE = "passcode";
	private static final String AR_ALAMAT = "alamat";
	
	Context context;
	ArrayList<HashMap<String, String>> daftar_list_ads;

	public ListADSAdapter(Context context, ArrayList<HashMap<String, String>> daftar_list_ads){
		this.context = context;
		this.daftar_list_ads = daftar_list_ads;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return daftar_list_ads.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return daftar_list_ads.get(position);
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
                inflate(R.layout.list_ads_adapter, parent, false);
        }
		
		// get current item to be displayed
        HashMap<String, String> item = (HashMap<String, String>) getItem(position);
        
     // get the TextView for item name and item description
        mNoAssigment = (TextView) convertView.findViewById(R.id.isi_nomor_assigment);
        mNamaToko = (TextView) convertView.findViewById(R.id.isi_nama_toko);
        mJml = (TextView) convertView.findViewById(R.id.isi_jml);
        mNoPasscode = (TextView) convertView.findViewById(R.id.isi_no_pass_code);
        mStatusAssigment = (TextView) convertView.findViewById(R.id.isi_status_awb);
        
        mNoAssigment.setText(item.get(AR_NO_ASSIGMENT));
        mNamaToko.setText(item.get(AR_NAMA_TOKO_SHORTEN));
//        mNamaToko.setText(item.get(AR_NAMA_TOKO));
        mJml.setText(item.get(AR_JUMLAH));
        mNoPasscode.setText(item.get(AR_NO_PASCODE));
        mStatusAssigment.setText(item.get(AR_STATUS));
        
        if (item.get(AR_STATUS).equals("0")) {
        	mStatusAssigment.setText("Open");
		} else {
			mStatusAssigment.setText("Close");
		}
		
		return convertView;
	}

}
