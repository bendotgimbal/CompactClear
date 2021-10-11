package compact.mobile.SuratJalan.helper;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class ListDetailADSAdapter3 extends BaseAdapter{
	
	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_NO_AWB = "no_awb";
	private static final String AR_NAMA_TOKO = "nama_toko";
	private static final String AR_STATUS = "status";
	private static final String AR_ALAMAT = "alamat";
	private static final String AR_SERVICE = "service";
	private static final String AR_TYPE = "type";
	private static final String AR_CHECKBOX = "checkbox";
	
	Context context;
	ArrayList<HashMap<String, String>> daftar_list_ads_detail2;
	public HashMap<Integer, Boolean> awbList = new HashMap<Integer, Boolean>();
	
	public ListDetailADSAdapter3(Context context, ArrayList<HashMap<String, String>> daftar_list_ads_detail2){
		this.context = context;
		this.daftar_list_ads_detail2 = daftar_list_ads_detail2;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return daftar_list_ads_detail2.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return daftar_list_ads_detail2.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// inflate the layout for each list row
		View view = convertView;
        ViewHolder holder = null;
        if (convertView == null) {
        	holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(compact.mobile.R.layout.list_ads_detail_adapter, null);
	
        // get current item to be displayed
//        HashMap<String, String> item = (HashMap<String, String>) getItem(position);
        
        // get the TextView for item name and item description
        holder.mNoAWB = (TextView) convertView.findViewById(compact.mobile.R.id.isi_no_awb);
        holder.mStatus = (TextView) convertView.findViewById(compact.mobile.R.id.isi_status);
        holder.mService = (TextView) convertView.findViewById(compact.mobile.R.id.isi_service);
        holder.mType = (TextView) convertView.findViewById(compact.mobile.R.id.isi_type);
        holder.mcheckText = (TextView) convertView.findViewById(compact.mobile.R.id.txt_selected);
        holder.mcheck = (CheckBox) convertView.findViewById(compact.mobile.R.id.selected);
        holder.checked = (ImageView) convertView.findViewById(compact.mobile.R.id.checked);
        holder.unchecked = (ImageView) convertView.findViewById(compact.mobile.R.id.unchecked);
		convertView.setTag(holder);
    }else{
        holder = (ViewHolder)view.getTag();
    }
      
        //sets the text for item name and item description from the current item object
        HashMap<String, String> item = (HashMap<String, String>) getItem(position);
        holder.mNoAWB.setText(item.get("AR_NO_AWB"));
        holder.mStatus.setText(item.get("AR_STATUS"));
        holder.mService.setText(item.get("AR_SERVICE"));
        holder.mType.setText(item.get("AR_TYPE"));
        holder.mcheckText.setText(item.get("AR_CHECKBOX"));
		
        if(item.get("AR_CHECKBOX").equals("y")){
			holder.mcheck.setChecked(true);
//			holder.checked.setVisibility(View.VISIBLE);
//			holder.unchecked.setVisibility(View.GONE);
			
		}else{
			holder.mcheck.setChecked(false);
//			holder.checked.setVisibility(View.GONE);
//			holder.unchecked.setVisibility(View.VISIBLE);
		}
		
        Picasso.with(holder.checked.getContext()).load(compact.mobile.R.drawable.checked_icon).into(holder.checked);
		Picasso.with(holder.unchecked.getContext()).load(compact.mobile.R.drawable.unchecked_icon).into(holder.unchecked);

        // returns the view for the current row
		return convertView;
	}
	
	static class ViewHolder{
		ImageView checked, unchecked;
        TextView mNoAWB, mStatus, mService, mType, mcheckText;
        CheckBox mcheck;
    }

	public void updateData(ListDetailADSAdapter2 adapterDetailADS) {
		// TODO Auto-generated method stub
		
	}
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}