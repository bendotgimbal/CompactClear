package compact.mobile;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;


public class multi_pup extends Activity {
	 String urlphp,po;
	TextView enotifier,nopo;
	EditText ewaybill;
	Button add,submit,keluar,scan;
	gps_tracker gps;
	ListView mainListView;
	private  ProgressDialog progressBar;
	private ArrayAdapter<String> listAdapter ; 
	public String locpk,username, mswb_pk, origin, destination, tppenerima;
	SessionManager session;
	WaybillAdapter	db;
	
	 public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.multi_pup);
	        session = new SessionManager(getApplicationContext());
	        HashMap<String, String> user = session.getUserDetails();
	 	        username = user.get(SessionManager.KEY_USER);
	 	        urlphp = user.get(SessionManager.KEY_URL);
	 	        locpk = user.get(SessionManager.KEY_LOCPK);

	       ewaybill=(EditText)findViewById(R.id.waybill);
	       enotifier=(TextView)findViewById(R.id.notifier);
	       mainListView = (ListView) findViewById( R.id.mainListView );   
	       scan=(Button)findViewById(R.id.btnscan);
	       HandleClick hc = new HandleClick();
	       scan.setOnClickListener(hc);
	       add=(Button)findViewById(R.id.btnadd);
	       submit=(Button)findViewById(R.id.btnsubmit);
	       keluar=(Button)findViewById(R.id.btnKeluar);
	       Bundle extras = getIntent().getExtras();
	       po = extras.getString("po");
	        nopo = (TextView)findViewById(R.id.po);
	        nopo.setText("Nomor Pickup Order : "+po);
	       ArrayList<String> wbList = new ArrayList<String>(); 
	       
	       listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow,wbList); 
	       
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
	    	        	add_list();
	    	    		}
	    	        	
	    	          return true;
	    	        }
	    	        return false;
	    	    }
	    	});
	       
	       add.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
				if (ewaybill.getText().toString().trim().length() == 0) {
	       			//Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
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
	       				Log.d("add waybill","waybill : "+ewaybill.getText().toString().trim());
	       			}
	       			
	       			add_list();
	       			
	       		}
				}
			});
	       
	       
	       submit.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					progressBar = new ProgressDialog(multi_pup.this);
	    		    progressBar.setCancelable(false);
	    		    progressBar.setMessage("Loading...");
	    		    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);;
	    		    progressBar.show();
						if (mainListView.getCount() == 0) {
			       			Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
			       		} else {
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
	 
	 public void add_list(){
		 
		 int x = listAdapter.getPosition(ewaybill.getText().toString().trim()) ;
			
			Log.d("add waybill","cek posisi " + ewaybill.getText().toString().trim() + " => " + x);
			
			if (x < 0 )
			{	
			listAdapter.add(ewaybill.getText().toString().trim());
			Log.d("add waybill", "=> " + ewaybill.getText().toString().trim() );
			mainListView.setAdapter( listAdapter );
			}
			
			//ewaybill.setFocusableInTouchMode(true);
			ewaybill.requestFocus();
			ewaybill.setText("");
		 
	 }
	 
	 @TargetApi(3)
	public void update_data(View theButton) {
		   int i,a;
 
			String swaybill = "";

			gps = new gps_tracker(multi_pup.this);
			i = this.mainListView.getCount();
			for (a = 0; a<i; a++)
			{
				if(a<i-1){
					swaybill += "'"+this.mainListView.getAdapter().getItem(a)+ "',"; }
				else{
					swaybill += "'"+this.mainListView.getAdapter().getItem(a)+ "'";
				}
			}

			Log.d("get waybill", swaybill);
			
				try {
						    ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
						    masukparam1.add(new BasicNameValuePair("po",po));
			       			masukparam1.add(new BasicNameValuePair("waybill",swaybill));
			       			masukparam1.add(new BasicNameValuePair("penerima", ""));
			       			masukparam1.add(new BasicNameValuePair("telp", ""));
			       			masukparam1.add(new BasicNameValuePair("kota", ""));
			       			masukparam1.add(new BasicNameValuePair("tujuan", ""));
			       			masukparam1.add(new BasicNameValuePair("mswb_pk", ""));
			       			masukparam1.add(new BasicNameValuePair("locpk", locpk));
			       			masukparam1.add(new BasicNameValuePair("username", username));
			       			masukparam1.add(new BasicNameValuePair("tiperem", ""));
			       			masukparam1.add(new BasicNameValuePair("tipe", "2"));
			       			masukparam1.add(new BasicNameValuePair("keterangan", ""));
			       			masukparam1.add(new BasicNameValuePair("lat_long",Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude())));
			       			masukparam1.add(new BasicNameValuePair("waktu",""));
			       			Log.d("waybill" ,swaybill );
			       			String response1 = null;
			       			try {
			       				response1 = CustomHttpClient.executeHttpPost(urlphp +   "update.php", masukparam1);
			       				Log.d("respone" , response1 );
			       				String res1 = response1.toString();
			       				res1 = res1.trim();
			       				res1 = res1.replaceAll("\\s+","");
			       			
			       				if (res1.equals("0")) {
			       					//Toast.makeText(theButton.getContext(), "Scan PUP successed.", Toast.LENGTH_SHORT).show();
			       					
			       					Log.d("PUP","Terkirim ke server");
			    	       			listAdapter.clear();
			    	       			mainListView.setAdapter( listAdapter );
			    	       			sukses("Scan PUP successed","Pickup Order "+po);
			    	       			
			       				}
			       				else {
			       					Toast.makeText(theButton.getContext(), "Scan PUP failed !!.", Toast.LENGTH_SHORT).show();
			       					Log.d("PUP","gagal simpan ke server");
			      		 			savetolocal(swaybill);
			      		 			Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();
			       				}
			       				
			       			}
			      		 	catch (Exception e) {
			      		 			savetolocal(swaybill);
			      		 			Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();
			      		 		}
				}
			 	catch (Exception e) {
				 		savetolocal(swaybill);
				 		Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();

				 		
			 		}
		   	
		   }
	 
	 @TargetApi(3)
	    public void sukses(String msg,String po){
	    	
	    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(
	    	        multi_pup.this);
	    	
	    	alertDialog.setCancelable(false);
	    	 
	    	// Setting Dialog Title
	    	alertDialog.setTitle(po);
	    	 
	    	// Setting Dialog Message
	    	alertDialog.setMessage(msg);
	    	 
	    	// Setting Positive "Yes" Btn
	    	alertDialog.setPositiveButton("OK",
	    	        new DialogInterface.OnClickListener() {
	    	            public void onClick(DialogInterface dialog, int which) {
	    	            	finish();
	    	            }
	    	        });
	    	
	    	alertDialog.show();
	    	
	    }
	private void savetolocal(String sWB){
		 
		 Log.d("PUP","prepare for PUP -> local" );
  		Waybill waybill = new Waybill();
 		Time now = new Time(Time.getCurrentTimezone());
 		now.setToNow();
 		String sTgl =  Integer.toString(now.year) + "-" + ("00" + Integer.toString(now.month + 1)).substring(("00" + Integer.toString(now.month + 1)).length() - 2) + "-" + ("00" + Integer.toString(now.monthDay)).substring(("00" + Integer.toString(now.monthDay)).length() - 2) + " " + 
 				("00" + Integer.toString(now.hour)).substring(("00" + Integer.toString(now.hour)).length() - 2)  + ":" + ("00" + Integer.toString(now.minute)).substring(("00" + Integer.toString(now.minute)).length() - 2) + ":" + ("00" + Integer.toString(now.second)).substring(("00" + Integer.toString(now.second)).length() - 2);
			waybill.setWaybill(sWB);
			waybill.setPenerima("");
			waybill.setKota("");
			waybill.setTujuan("");
			waybill.setMswb_pk("");
			waybill.setLocpk(locpk);
			waybill.setUser(username);
			waybill.setTiperem("");
			waybill.setTelp("");
			waybill.setTipe("2");
			waybill.setKeterangan("");
			waybill.setLat_Long(Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude()));
			waybill.setWaktu(sTgl);
			waybill.setStatus("0");
			waybill.setPo(po);
			Log.d("PUP","add PUP -> local" );
			db = new WaybillAdapter(multi_pup.this);
			db.open();
			db.createContact(waybill);
			db.close();
			Log.d("PUP","PUP add to local database ..");
			listAdapter.clear();
   			mainListView.setAdapter( listAdapter );
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
