package compact.mobile;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
//import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

//import android.widget.ArrayAdapter;

public class PickupBooking extends Activity  {
	String locpk, username, urlphp,kode_booking;
	Button keluar, refresh,scan,submit;
	ListView list;
	Integer harga,biaya,total,diskon,premi,panjang,lebar,tinggi,berat,volume,beratkena;
	
	TextView booking,nama,alamat,text_berat,isipaket,text_total,text_volum,text_beratkena;
	EditText ep,el,et;
	SessionManager session;
	JSONArray deails_booking = null;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickup_booking);
       // scan=(Button)findViewById(R.id.btnscan);
	       HandleClick hc = new HandleClick();
	      // scan.setOnClickListener(hc);
	       submit=(Button)findViewById(R.id.btnsubmit);
	       keluar=(Button)findViewById(R.id.btnKeluar);
	       ep=(EditText)findViewById(R.id.panjang);
	       el=(EditText)findViewById(R.id.lebar);
	       et=(EditText)findViewById(R.id.tinggi);
	       
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
 	       username = user.get(SessionManager.KEY_USER);
 	       urlphp = user.get(SessionManager.KEY_URL);
 	       locpk = user.get(SessionManager.KEY_LOCPK);
 	       booking = (TextView) findViewById(R.id.booking); 
 	      
 	      nama = (TextView) findViewById(R.id.nama);
 	      alamat = (TextView) findViewById(R.id.alamat);
 	     text_berat = (TextView) findViewById(R.id.berat);
 	      isipaket = (TextView) findViewById(R.id.isipaket);
 	     text_total = (TextView) findViewById(R.id.total);
 	    text_volum = (TextView) findViewById(R.id.volume);
 	   text_beratkena = (TextView) findViewById(R.id.berat_kena);
 	     Bundle extras = getIntent().getExtras();
         kode_booking = extras.getString("booking");
         booking.setText(": "+kode_booking);
         ambildata();
	     keluar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					 finish();
					 Intent a = new Intent (PickupBooking.this,ListPickupBooking.class);
					 startActivity(a);
				}
			});
	       submit.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					new Pickup().execute();
				}
			});
         ep.addTextChangedListener(new TextWatcher() {

             public void afterTextChanged(Editable s) {
            	 
            	 
            	 String spanjang = ep.getText().toString();
            	 if(spanjang.equals("")){
            		 
            	 }else{
            		// 
            		 panjang = Integer.valueOf(ep.getText().toString());
            		 lebar = Integer.valueOf(el.getText().toString());
            		 tinggi = Integer.valueOf(et.getText().toString());
            		 Integer kubik = (panjang*lebar*tinggi)/1000000;
            		 if (kubik > 0.05) 
            		    {
            			 Toast.makeText(getApplicationContext(), "Maximum kubikasi yang kami terima adalah 0.05m3 per paket. Harap dipacking ulang", Toast.LENGTH_LONG).show();
            			 ep.setText("1");
            		    }else{
            		    	volume = (panjang * lebar * tinggi)/6000;
            		    	volume = Math.round(volume);
            		    	if(volume < 1){
            		    		volume = 1;
            		    	}
            		    	if(volume > berat){
            		    		beratkena = volume;
            		    	}else{
            		    		beratkena = berat;
            		    	}
            		    	total = (harga * beratkena) + premi - diskon;
            		    	text_volum.setText(": "+String.valueOf(volume)+ " Kg");
            		    	text_beratkena.setText(": "+String.valueOf(beratkena)+" Kg");
            		    	
            		    	NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);           
           				    String ntotal = rupiahFormat.format(Double.parseDouble(String.valueOf(total)));
           				    
            		    	text_total.setText(": Rp "+ntotal);
            		    }
            	 }
             }

             public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

             public void onTextChanged(CharSequence s, int start, int before, int count) {}
          });
         el.addTextChangedListener(new TextWatcher() {

             public void afterTextChanged(Editable s) {
            	 
            	 
            	 String slebar = el.getText().toString();
            	 if(slebar.equals("")){
            		 
            	 }else{
            		// 
            		 panjang = Integer.valueOf(ep.getText().toString());
            		 lebar = Integer.valueOf(el.getText().toString());
            		 tinggi = Integer.valueOf(et.getText().toString());
            		 Integer kubik = (panjang*lebar*tinggi)/1000000;
            		 if (kubik > 0.05) 
            		    {
            			 Toast.makeText(getApplicationContext(), "Maximum kubikasi yang kami terima adalah 0.05m3 per paket. Harap dipacking ulang", Toast.LENGTH_LONG).show();
            			 el.setText("1");
            		    }else{
            		    	volume = (panjang * lebar * tinggi)/6000;
            		    	volume = Math.round(volume);
            		    	if(volume < 1){
            		    		volume = 1;
            		    	}
            		    	if(volume > berat){
            		    		beratkena = volume;
            		    	}else{
            		    		beratkena = berat;
            		    	}
            		    	total = (harga * beratkena) + premi - diskon;
            		    	text_volum.setText(": "+String.valueOf(volume)+ " Kg");
            		    	text_beratkena.setText(": "+String.valueOf(beratkena)+" Kg");
            		    	
            		    	NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);           
           				    String ntotal = rupiahFormat.format(Double.parseDouble(String.valueOf(total)));
           				    
            		    	text_total.setText(": Rp "+ntotal);
            		    }
            	 }
             }

             public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

             public void onTextChanged(CharSequence s, int start, int before, int count) {}
          });
         et.addTextChangedListener(new TextWatcher() {

             public void afterTextChanged(Editable s) {
            	 
            	 
            	 String stinggi = et.getText().toString();
            	 if(stinggi.equals("")){
            		 
            	 }else{
            		// 
            		 panjang = Integer.valueOf(ep.getText().toString());
            		 lebar = Integer.valueOf(el.getText().toString());
            		 tinggi = Integer.valueOf(et.getText().toString());
            		 Integer kubik = (panjang*lebar*tinggi)/1000000;
            		 if (kubik > 0.05) 
            		    {
            			 Toast.makeText(getApplicationContext(), "Maximum kubikasi yang kami terima adalah 0.05m3 per paket. Harap dipacking ulang", Toast.LENGTH_LONG).show();
            			 et.setText("1");
            		    }else{
            		    	volume = (panjang * lebar * tinggi)/6000;
            		    	volume = Math.round(volume);
            		    	if(volume < 1){
            		    		volume = 1;
            		    	}
            		    	if(volume > berat){
            		    		beratkena = volume;
            		    	}else{
            		    		beratkena = berat;
            		    	}
            		    	total = (harga * beratkena) + premi - diskon;
            		    	text_volum.setText(": "+String.valueOf(volume)+ " Kg");
            		    	text_beratkena.setText(": "+String.valueOf(beratkena)+" Kg");
            		    	
            		    	NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);           
           				    String ntotal = rupiahFormat.format(Double.parseDouble(String.valueOf(total)));
           				    
            		    	text_total.setText(": Rp "+ntotal);
            		    }
            	 }
             }

             public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

             public void onTextChanged(CharSequence s, int start, int before, int count) {}
          });
	}
	public void ambildata(){
		
		JSONParser jParser = new JSONParser();
		Log.d("error 2", "Userername = " +username+" Url = "+urlphp+" locpk = "+locpk);
        
		try {
			Log.d("error 3", "Userername = " +username+" Url = "+urlphp+" locpk = "+locpk);
			JSONObject json = jParser.getJSONFromUrl(urlphp + "detail_booking.php?booking=" + kode_booking);
			deails_booking = json.getJSONArray("detail_booking");
			Log.d("error 5", "Userername = " +username+" Url = "+urlphp+" detail_booking.php?booking=" + kode_booking);
			int cek = deails_booking.length();
			Log.d("error ", "isi list = " +String.valueOf(cek));
			for(int k = 0; k < deails_booking.length(); k++){
				 JSONObject ar = deails_booking.getJSONObject(k);
				 String snama = ar.getString("nama");
				 String salamat = ar.getString("alamat");
				 String sberat = ar.getString("berat");
				 String sisipaket = ar.getString("isipaket");
				 String stotal = ar.getString("total");
				 berat = Integer.valueOf(ar.getString("berat"));
				 harga = Integer.valueOf(ar.getString("harga"));
				 panjang = Integer.valueOf(ar.getString("panjang"));
				 lebar = Integer.valueOf(ar.getString("lebar"));
				 tinggi = Integer.valueOf(ar.getString("tinggi"));
				 total = Integer.valueOf(ar.getString("total"));
				 diskon = Integer.valueOf(ar.getString("diskon_amount"));
				 premi = Integer.valueOf(ar.getString("nilaipremi"));
				 volume = Integer.valueOf(ar.getString("volume"));
				 beratkena = Integer.valueOf(ar.getString("beratkena"));
				 
				 
				 Log.d("Booking","Nama : " + snama + ", alamat : " + salamat + ", berat : " + sberat + ",  total : " + stotal);
				 Log.d("Booking","panjang : " + String.valueOf(panjang) + ", lebar : " + String.valueOf(lebar) + ", tinggi : " + String.valueOf(tinggi) + ",  diskon : " + String.valueOf(diskon));
				 Log.d("Booking","volume : " + String.valueOf(volume) + ", beratkena : " + String.valueOf(beratkena) + ", harga : " + String.valueOf(harga) + ",  berat : " + String.valueOf(berat));
				 nama.setText(": "+snama);
				 alamat.setText(": "+salamat);
				 text_berat.setText(": "+sberat);
				 isipaket.setText(": "+sisipaket);
				 
				 ep.setText(String.valueOf(panjang));
				 el.setText(String.valueOf(lebar));
				 et.setText(String.valueOf(tinggi));
				 
				 text_volum.setText(": "+ar.getString("volume")+" Kg");
				 text_beratkena.setText(": "+ ar.getString("beratkena")+" Kg");
				 
				 NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);           
				 String ntotal = rupiahFormat.format(Double.parseDouble(stotal));

				 text_total.setText(": Rp "+ntotal);
				 
			}

		} catch (JSONException e) {
    		//e.printStackTrace();
    		
    		Log.d("WBLIST","Error : " + e.toString());
    	}
		

	}

    public class Pickup extends AsyncTask<String, String, String>
    {
        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
        ProgressDialog pDialog;
        String success = null;
        String massage = null;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(PickupBooking.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            
        }
       
        @Override
        protected String doInBackground(String... arg0) {
            JSONParser jParser = new JSONParser();
            ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
   			masukparam1.add(new BasicNameValuePair("booking", kode_booking));
   			masukparam1.add(new BasicNameValuePair("berat", String.valueOf(beratkena)));
   			masukparam1.add(new BasicNameValuePair("panjang", String.valueOf(panjang)));
   			masukparam1.add(new BasicNameValuePair("lebar", String.valueOf(lebar)));
   			masukparam1.add(new BasicNameValuePair("tinggi", String.valueOf(tinggi)));
   			masukparam1.add(new BasicNameValuePair("total", String.valueOf(total)));
   			masukparam1.add(new BasicNameValuePair("volume", String.valueOf(volume)));
   			//masukparam1.add(new BasicNameValuePair("awb", ewaybill.getText().toString()));
   			
   			
   			
   			String response;
   			try {

   				response = CustomHttpClient.executeHttpPost(urlphp +   "pickup_booking.php", masukparam1);
   				String res1 = response.toString();
   				String[] separated = res1.split(",");
   				String res = separated[0]; 
   				massage = separated[1]; 
   				Log.d("error 2", "Result = " +res1+" Url = "+urlphp+" pickup_booking.php");
   				res1 = res.trim();
   				if (res1.equals("0")) {
   					success = "0";
   				}else{
   					success = "1";
   				}

   					
   				}
  		 	catch (Exception e) {
  		 		success = "5";
  		 	}


            return null;

        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();

            if (success.equals("0")) {
                Toast.makeText(getApplicationContext(), massage, Toast.LENGTH_LONG).show();
                Intent a = new Intent(PickupBooking.this, ListPickupBooking.class);
                startActivity(a);
                finish();
            }else if (success.equals("1")){
                Toast.makeText(getApplicationContext(), massage, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Tidak Konek Ke Server !!", Toast.LENGTH_LONG).show();
            }


        }

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
	       // ewaybill.setText(intent.getStringExtra("SCAN_RESULT"));
	      } else if (resultCode == RESULT_CANCELED) {

	      }
	    }
	  }
	
 }
