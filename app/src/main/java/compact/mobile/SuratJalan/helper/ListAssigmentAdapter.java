package compact.mobile.SuratJalan.helper;

import java.util.HashMap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.content.Context;
import java.util.ArrayList;
import compact.mobile.R;

public class ListAssigmentAdapter extends BaseAdapter {
	
	TextView mNoAssigment, mStatusAssigment, mTanggalStatusAssigment;
	
	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_TANGGAL = "tanggal";
	private static final String AR_AWB = "awb";
	private static final String AR_STATUS = "status";
	private static final String AR_JENIS = "jenis";
	
	Context context;
	ArrayList<HashMap<String, String>> daftar_list_assigment;
	
	public ListAssigmentAdapter(Context context, ArrayList<HashMap<String, String>> daftar_list_assigment){
		this.context = context;
		this.daftar_list_assigment = daftar_list_assigment;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return daftar_list_assigment.size();
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return daftar_list_assigment.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                inflate(R.layout.list_assigment_adapter, parent, false);
        }
	
        // get current item to be displayed
        HashMap<String, String> item = (HashMap<String, String>) getItem(position);
        
        
        // get the TextView for item name and item description
        mNoAssigment = (TextView) convertView.findViewById(R.id.isi_nomor_assigment);
        mTanggalStatusAssigment = (TextView) convertView.findViewById(R.id.tanggal_status_assigment);
      
        mNoAssigment.setText(item.get(AR_NO_ASSIGMENT));
        mTanggalStatusAssigment.setText(item.get(AR_TANGGAL));
    		

        // returns the view for the current row
		return convertView;
	}
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}