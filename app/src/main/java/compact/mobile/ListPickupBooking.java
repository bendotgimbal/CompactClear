package compact.mobile;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

//import android.widget.ArrayAdapter;

public class ListPickupBooking extends Activity  {
	String locpk, username, urlphp;
	Button keluar, refresh;
	ListView list;

	JSONArray wblist = null;
	private BookingAdapter bookingadapter;
	private static final String BOOKING = "Booking";
	private static final String NAMA = "Nama";
	private static final String ALAMAT = "Alamat";
	private static final String LATLONG = "Latlong";
	ArrayList<HashMap<String, String>> mylistData =
            new ArrayList<HashMap<String, String>>();
	String[] columnTags = new String[] {BOOKING, NAMA, ALAMAT};
	int[] columnIds = new int[] {R.id.nomor, R.id.nama, R.id.alamat};
	SessionManager session;
	Integer booking = 1234;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickup_list);
        
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
 	        username = user.get(SessionManager.KEY_USER);
 	        urlphp = user.get(SessionManager.KEY_URL);
 	        locpk = user.get(SessionManager.KEY_LOCPK);
 	       bookingadapter = new BookingAdapter(this);  
 	       bookingadapter.open();
 	       refreshdata();

       keluar=(Button)findViewById(R.id.btnKeluar);
        refresh=(Button)findViewById(R.id.btnRefresh);
        list = (ListView) findViewById(R.id.mylist);
        Log.d("error", "Userername = " +username+" Url = "+urlphp+" locpk = "+locpk);
		Cursor cur = bookingadapter.getAllContact();
		Integer t = cur.getCount();
		Log.d("isi list", String.valueOf(t));
		if (cur != null)  {
			cur.moveToFirst();
			if (cur.getCount() > 0) {
				Log.d("Jumlah data on list", String.valueOf(t));
				do{
					String booking = cur.getString(0);
					String nama = cur.getString(1);
					String alamat = cur.getString(2);
					String latlang = cur.getString(3);
					HashMap<String, String> map = new HashMap<String, String>();
					 map.put("Booking", booking);
					 map.put("Nama", nama);
					 map.put("Alamat", alamat);
					 map.put("Latlong", latlang);
					 mylistData.add(map);

				}while (cur.moveToNext());
			}
		}
		SimpleAdapter arrayAdapter =
	              new SimpleAdapter(this, mylistData, R.layout.listviewbok,
	                             columnTags , columnIds);


		list.setAdapter(arrayAdapter);	
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                @SuppressWarnings("unchecked")
                HashMap<String, String> o = (HashMap<String, String>) list.getItemAtPosition(position);
                
                String coordinat = o.get("Latlong");
				String alamat = o.get("Alamat");
			    String booking = o.get("Booking");
			     if (coordinat.equals("0,0"))
			     {
			    	 coordinat = alamat.replace(" ","+");
			    }
			     Log.d("error 2", "coordinat = " +coordinat+" alamat = "+alamat+" booking = "+booking);
			     show_dialog2(coordinat,booking);
            }
        });
		
		 refresh.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
					Intent a = new Intent (ListPickupBooking.this,ListPickupBooking.class);
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
			JSONObject json = jParser.getJSONFromUrl(urlphp + "list_pickup_booking.php?kurir=" + username);
			String response = json.getString("code");
			bookingadapter.deleteAllContact();
			if (response.equals("0")){
				
			}else{
				wblist = json.getJSONArray("list_booking");
				Log.d("error 5", "Userername = " +username+" Url = "+urlphp+" list_pickup_booking.php?kurir=" + username);
				int cek = wblist.length();
				Log.d("error ", "isi list = " +String.valueOf(cek));
				
				for(int k = 0; k < wblist.length(); k++){
					 JSONObject ar = wblist.getJSONObject(k);
					 String booking = ar.getString("booking");
					 String nama = ar.getString("nama");
					 String alamat = ar.getString("alamat");
					 String latlang = ar.getString("latlang");
					 Log.d("WBLIST","Waybill : " + booking + ", Penerima : " + nama + ", Latlong : " + latlang + ",  Alamat : " + alamat);
					 ContactListBooking listbooking = new ContactListBooking();
			 	       for(int t = 0; t < 5; t++){
			 	 	     	listbooking.setBooking(booking);
			 	 	    	listbooking.setNama(nama);
			 	 	    	listbooking.setAlamat(alamat);
			 	 	    	listbooking.setLatlang(latlang);
			 	 	        bookingadapter.createContact(listbooking);
			 	       }
				}
			}


		} catch (JSONException e) {
    		//e.printStackTrace();
    		
    		Log.d("WBLIST","Error : " + e.toString());
    	}
		

	}
	
	
    public void show_dialog2(final String coordinat, final String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pickup : "+id);
        builder.setItems(new CharSequence[]
                        {"Cari Lokasi Alamat", "Pickup", "Batal"},

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Dialog"," lokasi " + coordinat);

                        switch (which) {

                            case 0:


                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.co.id/maps?q=" + coordinat));

                                //Intent intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://maps.google.co.id/maps?saddr=Current+Location&daddr"+ coordinat));

                                startActivity(intent);

                                break;
                            case 1:

                                Intent a = new Intent(ListPickupBooking.this, PickupBooking.class);
                                 a.putExtra("booking", id);
                                 startActivityForResult(a, 1);
                                finish();
                                break;

                            case 2:

                                dialog.dismiss();

                                break;


                        }

                    }

                });

        builder.create().show();

    }
	
	
	
 }
