package compact.mobile;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;

public class sip extends Activity {
	 String urlphp;
	TextView enotifier;
	EditText ewaybill;
	Button submit,keluar;
	gps_tracker gps;
	public String locpk,username, mswb_pk, origin, destination, tppenerima;
	
	WaybillAdapter	db;
	
	 public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.sip);
	       urlgw url = urlgw.getInstance(); 
	       urlphp = url.getData(); 
	       ewaybill=(EditText)findViewById(R.id.waybill);
	       enotifier=(TextView)findViewById(R.id.notifier);
	   
	       
	       submit=(Button)findViewById(R.id.btnsubmit);
	       keluar=(Button)findViewById(R.id.btnKeluar);

	       ewaybill.setOnKeyListener(new OnKeyListener() {
	    	    public boolean onKey(View v, int keyCode, KeyEvent event) {
	    	        // If the event is a key-down event on the "enter" button
	    	        //if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
	    	         //   (keyCode == KeyEvent.KEYCODE_ENTER)) {
	    	    	if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
	    	    			   keyCode == EditorInfo.IME_ACTION_DONE ||
	    	    			   event.getAction() == KeyEvent.ACTION_UP &&
	    	    			   event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
	    	          // Perform action on key press
	    	    		if (ewaybill.getText().toString().trim().length() != 0) {
	    	    			update_data(v);
	    	    		}
	    	          return true;
	    	        }
	    	        return false;
	    	    }
	    	});
	       submit.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					if (ewaybill.getText().toString().trim().length() == 0) {
	       			Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
	       			ewaybill.requestFocus();
	       		} else {
	       			
	       			String[] sWB;
	       			String sTemp1, sTemp2;
	       			if (ewaybill.getText().toString().trim().indexOf("-") > 0)
	       			{
	       				sWB = ewaybill.getText().toString().trim().split("-");
	       				sTemp1 = (sWB[0]+"0000").substring(0,4);
	       				sTemp2 = "000000000000"+sWB[1]; 
	       				sTemp2 = sTemp2.substring(sTemp2.length() - 9);
	       				ewaybill.setText(sTemp1 + sTemp2);
	       				Log.d("SIP","waybill : "+ewaybill.getText().toString().trim());
	       			}
	       			
	       			
	       			update_data(v);
	       		}
				}
			});
	       
	             
	       keluar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					 finish();
					// Intent a = new Intent (pup.this,main_menu.class);
					// startActivity(a);
				}
			});
	       
	       
	 }	
	 
	 @TargetApi(3)
	public void update_data(View theButton) {
		   	ArrayList<NameValuePair> masukparam = new ArrayList<NameValuePair>();
			LocPk S = LocPk.getInstance(); 
			locpk  = S.getData(); 

			UserName S1 = UserName.getInstance(); 
			username  = S1.getData(); 
			 gps = new gps_tracker(sip.this);
			 
			masukparam.add(new BasicNameValuePair("waybill", ewaybill.getText().toString()));
			masukparam.add(new BasicNameValuePair("locpk", locpk));
			masukparam.add(new BasicNameValuePair("kurir", username));
			masukparam.add(new BasicNameValuePair("tipe", "sip"));
				        	
				String response = null;
				try {
					response = CustomHttpClient.executeHttpPost(urlphp + "cek_waybill.php", masukparam);
					String res = response.toString();
					Log.d("cek waybill", "respon = " + res + " - " + response.toString());
					res = res.trim();
					res = res.replaceAll("\\s+","");
					if (res.equals("0"))
					 {
						 Toast.makeText(theButton.getContext(), "Waybill tidak terdaftar !! ["+res+"]", Toast.LENGTH_SHORT).show();
						 //enotifier.setText(masukparam + "::" + res);
						
					 }
					else if (res.equals("1")) {
						 Toast.makeText(theButton.getContext(), "Waybill sudah dilakukan SIP Scan !! ["+res+"]", Toast.LENGTH_SHORT).show();
						 //enotifier.setText(masukparam + "::" + res);
					}
					 else {
						    ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
			       			masukparam1.add(new BasicNameValuePair("waybill", ewaybill.getText().toString()));
			       			masukparam1.add(new BasicNameValuePair("penerima", ""));
			       			masukparam1.add(new BasicNameValuePair("telp", ""));
			       			masukparam1.add(new BasicNameValuePair("kota", ""));
			       			masukparam1.add(new BasicNameValuePair("tujuan", ""));
			       			masukparam1.add(new BasicNameValuePair("mswb_pk", res));
			       			masukparam1.add(new BasicNameValuePair("locpk", locpk));
			       			masukparam1.add(new BasicNameValuePair("username", username));
			       			masukparam1.add(new BasicNameValuePair("tiperem", ""));
			       			masukparam1.add(new BasicNameValuePair("tipe", "4"));
			       			masukparam1.add(new BasicNameValuePair("keterangan", ""));
			       			masukparam1.add(new BasicNameValuePair("lat_long",Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude())));
			       			masukparam1.add(new BasicNameValuePair("waktu",""));
			       			
			       			String response1 = null;
			       			try {
			       				response1 = CustomHttpClient.executeHttpPost(urlphp +   "update.php", masukparam1);
			       				String res1 = response1.toString();
			       				res1 = res1.trim();
			       				res1 = res1.replaceAll("\\s+","");
			       				if (res1.equals("0")) {
			       					Toast.makeText(theButton.getContext(), "Scan SIP successed.", Toast.LENGTH_SHORT).show();
			       					Log.d("SIP","Terkirim ke server");
			       					enotifier.setText("");
			           				ewaybill.setText("");
			       				}
			       				else {
			       					Toast.makeText(theButton.getContext(), "Scan SIP failed !!.", Toast.LENGTH_SHORT).show();
			       					Log.d("SIP","gagal simpan ke server");
			      		 			savetolocal();
			      		 			Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();
			       				}
			       					
			       				}
			      		 	catch (Exception e) {
			      		 			//Toast.makeText(theButton.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
			      		 			savetolocal();
			      		 			Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();
			      		 		}
					 	}					      			
				}
			 	catch (Exception e) {
				 		savetolocal();
				 		Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();

				 		// ewaybill.setText(e.toString());
			 		}
		   	
		   }
	 
	 @TargetApi(3)
	private void savetolocal(){
		 
		 Log.d("SIP","prepare for SIP -> local" );
  		Waybill waybill = new Waybill();
 		Time now = new Time(Time.getCurrentTimezone());
 		now.setToNow();
 		String sTgl =  Integer.toString(now.year) + "-" + ("00" + Integer.toString(now.month + 1)).substring(("00" + Integer.toString(now.month + 1)).length() - 2) + "-" + ("00" + Integer.toString(now.monthDay)).substring(("00" + Integer.toString(now.monthDay)).length() - 2) + " " + 
 				("00" + Integer.toString(now.hour)).substring(("00" + Integer.toString(now.hour)).length() - 2)  + ":" + ("00" + Integer.toString(now.minute)).substring(("00" + Integer.toString(now.minute)).length() - 2) + ":" + ("00" + Integer.toString(now.second)).substring(("00" + Integer.toString(now.second)).length() - 2);
			waybill.setWaybill(ewaybill.getText().toString());
			waybill.setPenerima("");
			waybill.setKota("");
			waybill.setTujuan("");
			waybill.setMswb_pk("");
			waybill.setLocpk(locpk);
			waybill.setUser(username);
			waybill.setTiperem("");
			waybill.setTelp("");
			waybill.setTipe("4");
			waybill.setKeterangan("");
			waybill.setLat_Long(Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude()));
			waybill.setWaktu(sTgl);
			waybill.setStatus("0");
			
			Log.d("SIP","add SIP -> local" );
			db = new WaybillAdapter(sip.this);
			db.open();
			db.createContact(waybill);
			db.close();
			Log.d("SIP","SIP add to local database ..");
			enotifier.setText("");
			ewaybill.setText("");
	 }
	 
	
	 public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		    if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		        //tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
		        ewaybill.setText(intent.getStringExtra("SCAN_RESULT"));
		      } else if (resultCode == RESULT_CANCELED) {

		      }
		    }
		  }
}
