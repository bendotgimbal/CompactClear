package compact.mobile;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;


public class pup extends Activity {
	 String urlphp;
	TextView enotifier;
	EditText ewaybill;
	 SessionManager session;
	Button submit,keluar,scan;
	gps_tracker gps;
	private  ProgressDialog progressBar;
	public String locpk,username, mswb_pk, origin, destination, tppenerima;
	
	WaybillAdapter	db;
	
	 public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.pup);
	       session = new SessionManager(getApplicationContext());
	       HashMap<String, String> user = session.getUserDetails();
		        username = user.get(SessionManager.KEY_USER);
		        urlphp = user.get(SessionManager.KEY_URL);
		        locpk = user.get(SessionManager.KEY_LOCPK);
		        
	       ewaybill=(EditText)findViewById(R.id.waybill);
	       enotifier=(TextView)findViewById(R.id.notifier);
	       scan=(Button)findViewById(R.id.btnscan);
	       HandleClick hc = new HandleClick();
	       scan.setOnClickListener(hc);
	       submit=(Button)findViewById(R.id.btnsubmit);
	       keluar=(Button)findViewById(R.id.btnKeluar);
			 progressBar = new ProgressDialog(pup.this);
			    progressBar.setCancelable(false);
			    progressBar.setMessage("Loading...");
			    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);;
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
					Log.d("masuk klik", "cek url"+urlphp);
					    progressBar.show();
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
	       				Log.d("PUP","waybill : "+ewaybill.getText().toString().trim());
	       			}
	       			
	       			Log.d("masuk klik 2", "cek url"+urlphp);
	       			update_data(v);
	       		}
					progressBar.dismiss();	
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
		 Log.d("masuk klik 3", "cek url"+urlphp);
		   	ArrayList<NameValuePair> masukparam = new ArrayList<NameValuePair>();
			//LocPk S = LocPk.getInstance(); 
			//locpk  = S.getData(); 
			Log.d("cek LOCPK", locpk);
		Log.d("masuk klik 4", "cek url"+urlphp);
			//UserName S1 = UserName.getInstance(); 
			//username  = S1.getData(); 
			Log.d("cek username", username);
			 gps = new gps_tracker(pup.this);
			 Log.d("masuk klik 5", "cek url"+urlphp); 

				        	
				String response = null;

					
						    ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
			       			masukparam1.add(new BasicNameValuePair("waybill", ewaybill.getText().toString()));
			       			masukparam1.add(new BasicNameValuePair("penerima", ""));
			       			masukparam1.add(new BasicNameValuePair("telp", ""));
			       			masukparam1.add(new BasicNameValuePair("kota", ""));
			       			masukparam1.add(new BasicNameValuePair("tujuan", ""));
			       		//	masukparam1.add(new BasicNameValuePair("mswb_pk", res));
			       			masukparam1.add(new BasicNameValuePair("locpk", locpk));
			       			masukparam1.add(new BasicNameValuePair("username", username));
			       			masukparam1.add(new BasicNameValuePair("tiperem", ""));
			       			masukparam1.add(new BasicNameValuePair("tipe", "1"));
			       			masukparam1.add(new BasicNameValuePair("keterangan", ""));
			       			masukparam1.add(new BasicNameValuePair("lat_long",Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude())));
			       			masukparam1.add(new BasicNameValuePair("waktu",""));
			       			
			       			String response1 = null;
			       			try {
			       				response1 = CustomHttpClient.executeHttpPost(urlphp +   "update.php", masukparam1);
			       				String res1 = response1.toString();
			       				res1 = res1.trim();
			       				res1 = res1.replaceAll("\\s+","");
			       				if (res1.equals("f")) {
			       				 Toast.makeText(theButton.getContext(), "Waybill tidak terdaftar !!", Toast.LENGTH_SHORT).show();
								 //enotifier.setText(masukparam + "::" + res);
			       				}
			       				if (res1.equals("f2")){
			       				 Toast.makeText(theButton.getContext(), "Waybill sudah dilakukan PUP Scan !! ", Toast.LENGTH_SHORT).show();
								 //enotifier.setText(masukparam + "::" + res);
			       				}
			       				if (res1.equals("0")) {
			       					Toast.makeText(theButton.getContext(), "Scan PUP successed.", Toast.LENGTH_SHORT).show();
			       					Log.d("PUP","Terkirim ke server");
			       					enotifier.setText("");
			           				ewaybill.setText("");
			       				}
			       				else {
			       					Toast.makeText(theButton.getContext(), "Scan PUP failed !!.", Toast.LENGTH_SHORT).show();
			       					//Log.d("PUP","gagal simpan ke server");
			      		 			//savetolocal();
			      		 			//Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();
			       				}
			       					
			       				}
			      		 	catch (Exception e) {
			      		 			//Toast.makeText(theButton.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
			      		 			savetolocal();
			      		 			Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();
			      		 		}
					 						      			

			 
		   }
	 
	 @TargetApi(3)
	private void savetolocal(){
		 
		 Log.d("PUP","prepare for PUP -> local" );
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
			waybill.setTipe("1");
			waybill.setKeterangan("");
			waybill.setLat_Long(Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude()));
			waybill.setWaktu(sTgl);
			waybill.setStatus("0");
			waybill.setPo("");
			Log.d("PUP","add PUP -> local" );
			db = new WaybillAdapter(pup.this);
			db.open();
			db.createContact(waybill);
			db.close();
			Log.d("PUP","PUP add to local database ..");
			enotifier.setText("");
			ewaybill.setText("");
	 }
	 
		private class HandleClick implements OnClickListener{
			public void onClick(View arg0){
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				switch (arg0.getId()){
					case R.id.btnscan:
						intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF");
						break;
				}
				startActivityForResult(intent, 0);
			}
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
