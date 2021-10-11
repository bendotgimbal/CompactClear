package compact.mobile;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.SimpleAdapter;


import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class DetailListPickup extends Activity {
	String locpk, username, urlphp,asigment;
	String waybill;
	Button keluar, refresh;
	ListView list;
	gps_tracker gps;
	JSONArray wblist = null;
	private DetailListAdapter DetailListAdapter;
	private static final String TANGGAL = "Tanggal";
	private static final String WAYBILL = "Waybill";
	private static final String JENIS = "Jenis";

	ArrayList<HashMap<String, String>> mylistData =
            new ArrayList<HashMap<String, String>>();
	String[] columnTags = new String[] {TANGGAL, WAYBILL};
	int[] columnIds = new int[] {R.id.tanggal, R.id.manifes};
	SessionManager session;
	//Integer booking = 1234;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detaillistpickup);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
 	        username = user.get(SessionManager.KEY_USER);
 	        urlphp = user.get(SessionManager.KEY_URL);
 	        locpk = user.get(SessionManager.KEY_LOCPK);
 	       DetailListAdapter = new DetailListAdapter(this);  
 	      DetailListAdapter.open();
 	     Bundle extras = getIntent().getExtras();
         asigment = extras.getString("asigment");
         //jenis = extras.getString("jenis");
         Log.d("Nomor Asigment", asigment);
 	      
 	      
 	      
 	     refreshdata();
 	     
 	     
 	    keluar=(Button)findViewById(R.id.btnKeluar);
        refresh=(Button)findViewById(R.id.btnRefresh);
        list = (ListView) findViewById(R.id.mylist);
        
        
        
        Cursor cur = DetailListAdapter.getSingleContact(asigment);
		Integer t = cur.getCount();
		Log.d("isi list", String.valueOf(t));
		if (cur != null)  {
			cur.moveToFirst();
			if (cur.getCount() > 0) {
				Log.d("Jumlah data on list", String.valueOf(t));
				do{
					String waybill = cur.getString(0);
					String tanggal = cur.getString(1);
					String jenis = cur.getString(2);
					Log.d("Isi", "Waybill = "+waybill+" tanggal = "+tanggal);
					HashMap<String, String> map = new HashMap<String, String>();
					 map.put("Waybill", waybill);
					 map.put("Tanggal", tanggal);
					 map.put("Jenis", jenis);
					 mylistData.add(map);

				}while (cur.moveToNext());
			}
		}
		SimpleAdapter arrayAdapter =
	              new SimpleAdapter(this, mylistData, R.layout.listviewasigment,
	                             columnTags , columnIds);
		list.setAdapter(arrayAdapter);	
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                @SuppressWarnings("unchecked")
                HashMap<String, String> o = (HashMap<String, String>) list.getItemAtPosition(position);
                
                waybill = o.get("Waybill");
               String jenis = o.get("Jenis");
                Log.d("tes waybill", waybill);
                if(jenis.equals("1")){
                	show_dialog2();
                }else if (jenis.equals("2")){
                	show_dialog3();
                }
                
			     Log.d("error 2", "manifes = " +waybill);
			     
            }
        });
		 refresh.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
					Intent a = new Intent (DetailListPickup.this,DetailListPickup.class);
					startActivity(a);
					
				}
			});
		 
     keluar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
    
	}
	public void refreshdata(){
		
		JSONParser jParser = new JSONParser();
		Log.d("error 2", "Userername = " +username+" Url = "+urlphp+" locpk = "+locpk);
        
		try {
			Log.d("error 3", "Userername = " +username+" Url = "+urlphp+" locpk = "+locpk);
			JSONObject json = jParser.getJSONFromUrl(urlphp + "details_list_pickup.php?asigment=" + asigment);
			String response = json.getString("response_code");
			DetailListAdapter.deleteContact(asigment);
			if (response.equals("0")){
				
			}else{
				wblist = json.getJSONArray("list");
				Log.d("error 5", "Userername = " +username+" Url = "+urlphp+"details_list_pickup.php?asigment=" + asigment);
				int cek = wblist.length();
				Log.d("error ", "isi list = " +String.valueOf(cek));
				
				for(int k = 0; k < wblist.length(); k++){
					 JSONObject ar = wblist.getJSONObject(k);
					 String tanggal = ar.getString("tanggal");
					 String awb = ar.getString("awb");
					 String jenis = ar.getString("jenis");
					 Log.d("WBLIST","awb : " + tanggal + ", tanggal : " + tanggal );
					 ContactDetailList listbooking = new ContactDetailList();
			 	       for(int t = 0; t < 5; t++){
			 	 	     	listbooking.setWaybill(awb);
			 	 	    	listbooking.setTanggal(tanggal);
			 	 	    	listbooking.setAsigment(asigment);
			 	 	    	listbooking.setJenis(jenis);
			 	 	    	DetailListAdapter.createContact(listbooking);
			 	       }
				}
			}


		} catch (JSONException e) {
    		//e.printStackTrace();
    		
    		Log.d("WBLIST","Error : " + e.toString());
    	}
		

	}
	public void update_data() {
		 Log.d("masuk klik 3", "cek url"+urlphp);
		   	ArrayList<NameValuePair> masukparam = new ArrayList<NameValuePair>();
			//LocPk S = LocPk.getInstance(); 
			//locpk  = S.getData(); 
			Log.d("cek LOCPK", locpk);
		Log.d("masuk klik 4", "cek url"+urlphp);
			//UserName S1 = UserName.getInstance(); 
			//username  = S1.getData(); 
			Log.d("cek username", username);
			 gps = new gps_tracker(DetailListPickup.this);
			 Log.d("masuk klik 5", "cek url"+urlphp); 

				        	
				String response = null;

					
						    ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
			       			masukparam1.add(new BasicNameValuePair("waybill", waybill));
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
			       			
			       			String response1;
			       			Log.d("Waybill", waybill);
			       			try {
			       				
			       				response1 = CustomHttpClient.executeHttpPost(urlphp +   "update.php", masukparam1);
			       				String res1 = response1.toString();
			       				res1 = res1.trim();
			       				res1 = res1.replaceAll("\\s+","");
			       				if (res1.equals("f")) {
			       				// Toast.makeText(DetailListPickup.getContext(), "Waybill tidak terdaftar !!", Toast.LENGTH_SHORT).show();
								 //enotifier.setText(masukparam + "::" + res);
			       					Log.d("PUP","Waybill sudah dilakukan PUP Scan !!");
			       					sukses("Waybill tidak terdaftar","AWB "+waybill);
			       				}
			       				else if (res1.equals("f2")){
			       				// Toast.makeText(theButton.getContext(), "Waybill sudah dilakukan PUP Scan !! ", Toast.LENGTH_SHORT).show();
								 //enotifier.setText(masukparam + "::" + res);
			       					Log.d("PUP","Waybill tidak terdaftar !!");
			       					sukses("Waybill sudah dilakukan PUP Scan","AWB "+waybill);
			       				}
			       				else if (res1.equals("0")) {
			       					//Toast.makeText(theButton.getContext(), "Scan PUP successed.", Toast.LENGTH_SHORT).show();
			       					Log.d("PUP","Terkirim ke server");
			       					sukses("Scan PUP successed","AWB "+waybill);
			       					//enotifier.setText("");
			           				//ewaybill.setText("");
			       				}
			       				else {
			       					//Toast.makeText(theButton.getContext(), "Scan PUP failed !!.", Toast.LENGTH_SHORT).show();
			       					Log.d("PUP","gagal simpan ke server");
			       					sukses("Scan PUP failed !!","AWB "+waybill);
			      		 			//savetolocal();
			      		 			//Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();
			       				}
			       					
			       				}
			      		 	catch (Exception e) {
			      		 		Log.d("Gagal", e.toString());
			      		 			//Toast.makeText(theButton.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
			      		 			//savetolocal();
			      		 			//Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();
			      		 		}
					 						      			

			 
		   }
    public void sukses(String msg,String awb){
    	
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(
    	        DetailListPickup.this);
    	
    	alertDialog.setCancelable(false);
    	 
    	// Setting Dialog Title
    	alertDialog.setTitle(awb);
    	 
    	// Setting Dialog Message
    	alertDialog.setMessage(msg);
    	 
    	// Setting Positive "Yes" Btn
    	alertDialog.setPositiveButton("OK",
    	        new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	            	 Intent a = new Intent (DetailListPickup.this,DetailListPickup.class);
                         a.putExtra("asigment", asigment);
                         startActivityForResult(a, 1);
                         finish();
    	            }
    	        });
    	
    	alertDialog.show();
    	
    }
    public void show_dialog2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailListPickup.this);
        builder.setTitle("Action ");
        builder.setItems(new CharSequence[]
                        {"Pickup", "Batal"},

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        

                        switch (which) {

                            case 0:
                               	update_data();
                                
                                break;
                            case 1:


                                break;



                        }

                    }

                });

        builder.create().show();

    }
    public void show_dialog3(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailListPickup.this);
        builder.setTitle("Action ");
        builder.setItems(new CharSequence[]
                        {"Pickup", "Batal"},

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        

                        switch (which) {

                            case 0:
                               	 Intent a = new Intent (DetailListPickup.this,multi_pup.class);
                                    a.putExtra("asigment", asigment);
                                    a.putExtra("po", waybill);
                                    startActivityForResult(a, 1);
                                    finish();
                                break;
                            case 1:


                                break;



                        }

                    }

                });

        builder.create().show();

    }
}
