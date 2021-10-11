package compact.mobile.SuratJalan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import compact.mobile.R;

public class ScanAWB extends Activity {
	
	TextView TV_no_assigment;
	String STR_no_assigment;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_awb);
        
        Intent in = getIntent();
        STR_no_assigment = in.getStringExtra("AR_NO_ASSIGMENT");
        Log.d("Debug", "Intent From List PO Outstanding >>> " + "Nomor Asigment " + STR_no_assigment);
        
        TV_no_assigment = (TextView) findViewById(R.id.no_assigment);
        TV_no_assigment.setText(STR_no_assigment);
	}

}
