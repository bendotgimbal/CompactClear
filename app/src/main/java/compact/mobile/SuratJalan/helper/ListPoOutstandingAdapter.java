package compact.mobile.SuratJalan.helper;

import java.util.HashMap;

import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import compact.mobile.R;

import java.util.ArrayList;

public class ListPoOutstandingAdapter extends BaseAdapter {
	
	TextView mNoAssigment, mNoPO, mStatusAssigment, mCust;

	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_NO_PO= "no_po";
	private static final String AR_KODE_CUST = "kode_customer";
	private static final String AR_NAMA_CUST = "nama_customer";
	private static final String AR_NAMA_CUST_SHORTEN = "sort_nama_customer";
	private static final String AR_STATUS = "status";
	private static final String AR_ALAMAT_PICKUP = "alamat";
	
	Context context;
	ArrayList<HashMap<String, String>> daftar_list_po_outstanding;
	
	public ListPoOutstandingAdapter(Context context, ArrayList<HashMap<String, String>> daftar_list_po_outstanding){
		this.context = context;
		this.daftar_list_po_outstanding = daftar_list_po_outstanding;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return daftar_list_po_outstanding.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return daftar_list_po_outstanding.get(position);
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
                inflate(R.layout.list_po_outstanding_adapter, parent, false);
        }
		
		// get current item to be displayed
        HashMap<String, String> item = (HashMap<String, String>) getItem(position);
        
     // get the TextView for item name and item description
        mNoAssigment = (TextView) convertView.findViewById(R.id.isi_nomor_assigment);
        mNoPO = (TextView) convertView.findViewById(R.id.isi_nomor_po);
        mStatusAssigment = (TextView) convertView.findViewById(R.id.status_po_outstanding);
        mCust = (TextView) convertView.findViewById(R.id.cust_po_outstanding);
        
        mNoAssigment.setText(item.get(AR_NO_ASSIGMENT));
        mNoPO.setText(item.get(AR_NO_PO));
        mStatusAssigment.setText(item.get(AR_STATUS));
//        mCust.setText(item.get(AR_NAMA_CUST));
        mCust.setText(item.get(AR_NAMA_CUST_SHORTEN));
        
        if(item.get(AR_STATUS).equals("1")){
        	mStatusAssigment.setText("Closed");
        }else{
        	mStatusAssigment.setText("Opened");
        }
		
		return convertView;
	}
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
