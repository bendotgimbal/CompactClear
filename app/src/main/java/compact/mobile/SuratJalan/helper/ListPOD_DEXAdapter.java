package compact.mobile.SuratJalan.helper;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import compact.mobile.R;
import compact.mobile.SuratJalan.ListPOD_DEX;

public class ListPOD_DEXAdapter extends BaseAdapter {
	
	TextView mNoAssigment, mNoAWB, mStatus, mService, mNominalCOD, mInfo;
    ImageView imInfo;
	
	private static final String AR_A = "asigment";
	private static final String AR_B = "no_awb";
	private static final String AR_C = "status";
	private static final String AR_D = "alamat";
	private static final String AR_E = "nilai_cod";
	private static final String AR_F = "service";
	private static final String AR_E_REPLACE = "nilai_cod_replace";
	private static final String AR_G = "keterangan_ntc";
	
	Context context;
	ArrayList<HashMap<String, String>> daftar_list_pod_dex;

	public ListPOD_DEXAdapter(Context context, ArrayList<HashMap<String, String>> daftar_list_pod_dex){
		this.context = context;
		this.daftar_list_pod_dex = daftar_list_pod_dex;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return daftar_list_pod_dex.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return daftar_list_pod_dex.get(position);
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
                inflate(R.layout.list_pod_dex_adapter, parent, false);
        }
		
		// get current item to be displayed
        final HashMap<String, String> item = (HashMap<String, String>) getItem(position);
        
     // get the TextView for item name and item description
        mNoAWB = (TextView) convertView.findViewById(R.id.isi_nomor_awb);
        mStatus = (TextView) convertView.findViewById(R.id.isi_status);
        mService = (TextView) convertView.findViewById(R.id.isi_service);
        mNominalCOD = (TextView) convertView.findViewById(R.id.isi_nilai_cod);
//		mInfo = (TextView) convertView.findViewById(R.id.tv_info);
        imInfo = (ImageView) convertView.findViewById(R.id.im_info);

//		mInfo.setVisibility((item.get(AR_G) == null || item.get(AR_G).isEmpty()) ? View.GONE : View.VISIBLE);
//		mInfo.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				((ListPOD_DEX) context).dipencet(item.get(AR_G));
//			}
//		});

//        imInfo.setVisibility((item.get(AR_G) == null || item.get(AR_G).isEmpty()) ? View.GONE : View.VISIBLE);
//        imInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((ListPOD_DEX) context).dipencet(item.get(AR_G));
//            }
//        });
        
        mNoAWB.setText(item.get(AR_B));
        mStatus.setText(item.get(AR_C));
        mService.setText(item.get(AR_F));
        mNominalCOD.setText(item.get(AR_E_REPLACE));
//		mInfo.setText(item.get(AR_G));
        
//        if (item.get(AR_C).equals("0")) {
//        	mStatus.setText("Open");
//		} else if (item.get(AR_C).equals("1")) {
//			mStatus.setText("POD");
//		} else {
//			mStatus.setText("DEX");
//		}

//        if(item.get(AR_G) == null || item.get(AR_G).isEmpty()){
//            imInfo.setVisibility(View.GONE);
//        }else{
//            imInfo.setVisibility(View.VISIBLE);
//        }
//        imInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((ListPOD_DEX) context).dipencet(item.get(AR_G));
//            }
//        });

		if (item.get(AR_G).equals("")) {
			imInfo.setVisibility(View.GONE);
		}else{
			imInfo.setVisibility(View.VISIBLE);
			imInfo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((ListPOD_DEX) context).dipencet(item.get(AR_G));
				}
			});
		}
        notifyDataSetChanged();
		return convertView;
	}

}
