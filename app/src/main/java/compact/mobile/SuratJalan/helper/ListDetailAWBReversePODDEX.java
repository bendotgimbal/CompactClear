package compact.mobile.SuratJalan.helper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import compact.mobile.R;

public class ListDetailAWBReversePODDEX extends BaseAdapter {
    private static final String AR_A = "date";
    private static final String AR_B = "time";
    private static final String AR_C= "awb_no";
    private static final String AR_D = "booking_code";
    private static final String AR_E= "total_setor";
    private static final String AR_F= "komisi_agen";
    private static final String AR_G = "status";

    Context context;
    ArrayList<HashMap<String, String>> detail_awb_reverse_pod;

    public ListDetailAWBReversePODDEX(Context context, ArrayList<HashMap<String, String>> detail_awb_reverse_pod){
        this.context = context;
        this.detail_awb_reverse_pod = detail_awb_reverse_pod;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return detail_awb_reverse_pod.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return detail_awb_reverse_pod.get(position);
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
            convertView = LayoutInflater.from(context).
//                inflate(R.layout.list_transaksi, parent, false);
        inflate(R.layout.list_detail_awb_reverse_poddex, null);

            // get the TextView for item name and item description
            holder.mDate = (TextView) convertView.findViewById(R.id.date);
            holder.mTime = (TextView) convertView.findViewById(R.id.time);
            holder.mSetor = (TextView) convertView.findViewById(R.id.rp_total_setor);
            holder.mAwb = (TextView) convertView.findViewById(R.id.awb_no);
            holder.mCode = (TextView) convertView.findViewById(R.id.booking_code);
            holder.mKomisi = (TextView) convertView.findViewById(R.id.rp_komisi_agen);
            holder.mStatus = (TextView) convertView.findViewById(R.id.status);
            holder.statusmerah = (ImageView) convertView.findViewById(R.id.status_merah);
            holder.statusbiru = (ImageView) convertView.findViewById(R.id.status_biru);
            holder.imInformation = (ImageView) convertView.findViewById(R.id.status_information);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        //sets the text for item name and item description from the current item object
        HashMap<String, String> item = (HashMap<String, String>) getItem(position);
        holder.mDate.setText(item.get(AR_A));
        holder.mTime.setText(item.get(AR_B));
        holder.mSetor.setText(item.get(AR_C));
        holder.mAwb.setText(item.get(AR_D));
        holder.mCode.setText(item.get(AR_E));
        holder.mKomisi.setText(item.get(AR_F));
        holder.mStatus.setText(item.get(AR_G));

        if(item.get(AR_G).equals("1")){
            holder.mStatus.setTextColor(Color.BLUE);
            holder.statusbiru.setVisibility(View.VISIBLE);
            holder.statusmerah.setVisibility(View.GONE);
            holder.imInformation.setVisibility(View.VISIBLE);

        }else{
            holder.mStatus.setTextColor(Color.RED);
            holder.statusbiru.setVisibility(View.GONE);
            holder.statusmerah.setVisibility(View.VISIBLE);
            holder.imInformation.setVisibility(View.GONE);
        }

        Picasso.with(holder.statusmerah.getContext()).load(R.drawable.bck_grnd_status_merah).into(holder.statusmerah);
        Picasso.with(holder.statusbiru.getContext()).load(R.drawable.bck_grnd_status_biru).into(holder.statusbiru);
        Picasso.with(holder.imInformation.getContext()).load(R.drawable.information).into(holder.imInformation);

        // returns the view for the current row
        return convertView;
    }

    static class ViewHolder{
        ImageView statusmerah, statusbiru, imInformation;
        TextView mDate, mTime, mSetor, mAwb, mCode, mKomisi, mStatus;
    }
}
