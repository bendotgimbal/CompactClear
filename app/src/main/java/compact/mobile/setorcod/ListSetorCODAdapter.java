package compact.mobile.setorcod;

import java.util.ArrayList;
import java.util.HashMap;

import compact.mobile.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListSetorCODAdapter extends BaseAdapter {
	
	TextView mNoAWBSetorCOD, mKodeSetorCOD, mTanggalAWBSetorCOD, mStatusAWBSetorCOD, mTotalNoAWBSetorCOD;
	
	private static final String AR_KODE_SETORAN = "bsc_kode_setoran";
	private static final String AR_NO_AWB_SETOR_COD = "airwaybill";
	private static final String AR_DATE_BOOKING = "bsc_tanggal_booking";
	private static final String AR_TIME_BOOKING = "bsc_time_booking";
	private static final String AR_TOTAL_AWB_SETOR_COD = "bsc_total_nilai_cod";
	private static final String AR_TOTAL_AWB_SETOR_COD_CONVERT = "bsc_total_nilai_cod_convert";
	private static final String AR_STATUS = "bsc_status";
	
	Context context;
	ArrayList<HashMap<String, String>> daftar_list_setor_Cod;

	public ListSetorCODAdapter(Context context,
			ArrayList<HashMap<String, String>> daftar_list_setor_Cod) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.daftar_list_setor_Cod = daftar_list_setor_Cod;
	}

	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		return 0;
		return daftar_list_setor_Cod.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
//		return null;
		return daftar_list_setor_Cod.get(position);
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
                inflate(R.layout.list_setor_cod_adapter, parent, false);
        }
		
		// get current item to be displayed
        HashMap<String, String> item = (HashMap<String, String>) getItem(position);
        
     // get the TextView for item name and item description
        mNoAWBSetorCOD = (TextView) convertView.findViewById(R.id.isi_nomor_awb);
        mKodeSetorCOD = (TextView) convertView.findViewById(R.id.isi_kode_setor);
        mTanggalAWBSetorCOD = (TextView) convertView.findViewById(R.id.isi_tanggal_setor);
        mStatusAWBSetorCOD = (TextView) convertView.findViewById(R.id.isi_status_setor);
        mTotalNoAWBSetorCOD = (TextView) convertView.findViewById(R.id.isi_total_awb_setor_cod);
        
        mNoAWBSetorCOD.setText(item.get(AR_NO_AWB_SETOR_COD));
        mKodeSetorCOD.setText(item.get(AR_KODE_SETORAN));
        mTanggalAWBSetorCOD.setText(item.get(AR_DATE_BOOKING));
//        mStatusAWBSetorCOD.setText(item.get(AR_STATUS));
        mTotalNoAWBSetorCOD.setText(item.get(AR_TOTAL_AWB_SETOR_COD_CONVERT));
        
        if (item.get(AR_STATUS).equals("1")) {
        	mStatusAWBSetorCOD.setText("PAID");
		} else {
			mStatusAWBSetorCOD.setText("UNPAID");
		}
        
//		return null;
        return convertView;
	}

}
