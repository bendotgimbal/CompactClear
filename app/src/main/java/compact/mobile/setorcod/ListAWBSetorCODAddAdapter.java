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

public class ListAWBSetorCODAddAdapter extends BaseAdapter {
	
	TextView mNoAWBSetorCOD, mTotalNoAWBSetorCOD;
	
	private static final String AR_NO_AWB_SETOR_COD = "lowb_mswb_nomor";
	private static final String AR_TOTAL_AWB_SETOR_COD = "ttwb_cod";
	private static final String AR_TOTAL_AWB_SETOR_COD_CONVERT = "ttwb_cod_convert";

	protected static final HashMap<Integer, Boolean> ListAWBSetorCOD = null;

//	protected static final HashMap<Integer, Boolean> ListAWBSetorCOD = null;

	Context context;
	ArrayList<HashMap<String, String>> daftar_list_awb_setorCod;
//	ArrayList<HashMap<String, Object>> list_awb_setorCod;
//	public HashMap<Integer, Boolean> listAWBSetorCOD = new HashMap<Integer, Boolean>();
//	
//	public ListAWBSetorCODAdapter(Context context, ArrayList<HashMap<String, String>> daftar_list_awb_setorCod, ArrayList<HashMap<String, Object>> list_awb_setorCod){
//		this.context = context;
//		this.daftar_list_awb_setorCod = daftar_list_awb_setorCod;
//		this.list_awb_setorCod = list_awb_setorCod;
//		
//	}

	public ListAWBSetorCODAddAdapter(Context context, ArrayList<HashMap<String, String>> daftar_list_awb_setorCod){
		this.context = context;
		this.daftar_list_awb_setorCod = daftar_list_awb_setorCod;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return daftar_list_awb_setorCod.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return daftar_list_awb_setorCod.get(position);
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
                inflate(R.layout.list_awb_setor_cod_adapter, parent, false);
        }
		
		// get current item to be displayed
        HashMap<String, String> item = (HashMap<String, String>) getItem(position);
        
     // get the TextView for item name and item description
        mNoAWBSetorCOD = (TextView) convertView.findViewById(R.id.isi_nomor_awb);
        mTotalNoAWBSetorCOD = (TextView) convertView.findViewById(R.id.isi_total_awb_setor_cod);
        
        mNoAWBSetorCOD.setText(item.get(AR_NO_AWB_SETOR_COD));
        mTotalNoAWBSetorCOD.setText(item.get(AR_TOTAL_AWB_SETOR_COD_CONVERT));
		
		return convertView;
	}

}
