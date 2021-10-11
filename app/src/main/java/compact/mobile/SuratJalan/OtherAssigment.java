package compact.mobile.SuratJalan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import compact.mobile.R;
import compact.mobile.setorcod.ListSetorCOD;

public class OtherAssigment extends Activity {

	TextView TV_no_assigment;
	Button BTN_po_outstanding, BTN_ads, BTN_awb_others, BTN_pod_or_dex, BTN_setor_cod;
	String STR_no_assigment;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_assigment);
//		Bundle extras = getIntent().getExtras();
//		no_assigment = extras.getString("asigment");
//        Log.d("Nomor Asigment", no_assigment);
        
        Intent in = getIntent();
     	////No Assigment
        STR_no_assigment = in.getStringExtra("AR_NO_ASSIGMENT");
        Log.d("Debug", "Intent From Assigment >>> " + "Nomor Asigment " + STR_no_assigment);
        
        BTN_po_outstanding=(Button)findViewById(R.id.btnpo_outstanding);
        BTN_ads=(Button)findViewById(R.id.btnads);
        BTN_awb_others=(Button)findViewById(R.id.wblist_others);
        BTN_pod_or_dex=(Button)findViewById(R.id.btnpod_ord_dex);
        BTN_setor_cod=(Button)findViewById(R.id.btnscd);
        
        TV_no_assigment = (TextView) findViewById(R.id.no_assigment);
        TV_no_assigment.setText(STR_no_assigment);
        
        BTN_po_outstanding.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				finish();
				Intent a = new Intent(OtherAssigment.this, ListPoOutstanding.class);
				a.putExtra("AR_NO_ASSIGMENT", STR_no_assigment);
				Log.d("Debug", "To PO Outstanding >>> " +"No.Assigment = " +STR_no_assigment);
				startActivity(a);

			}
		});
        
        BTN_ads.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				finish();
				Intent a = new Intent(OtherAssigment.this, ListADS.class);
				a.putExtra("AR_NO_ASSIGMENT", STR_no_assigment);
				Log.d("Debug", "To ADS >>> " +"No.Assigment = " +STR_no_assigment);
				startActivity(a);

			}
		});
        
        BTN_awb_others.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				finish();
				Intent a = new Intent(OtherAssigment.this, ListAWBOthers.class);
				a.putExtra("AR_NO_ASSIGMENT", STR_no_assigment);
				Log.d("Debug", "To AWB Others >>> " +"No.Assigment = " +STR_no_assigment);
				startActivity(a);

			}
		});
        
        BTN_pod_or_dex.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				finish();
				Intent a = new Intent(OtherAssigment.this, ListPOD_DEX.class);
				a.putExtra("AR_NO_ASSIGMENT", STR_no_assigment);
				Log.d("Debug", "To POD / DEX >>> " +"No.Assigment = " +STR_no_assigment);
				startActivity(a);

			}
		});
        
        BTN_setor_cod.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				finish();
//				Intent a = new Intent(OtherAssigment.this, Setorcod.class);
				Intent a = new Intent(OtherAssigment.this, ListSetorCOD.class);
				a.putExtra("AR_NO_ASSIGMENT", STR_no_assigment);
				Log.d("Debug", "To Setor COD >>> " +"No.Assigment = " +STR_no_assigment);
				startActivity(a);

			}
		});
	}
	
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
}
