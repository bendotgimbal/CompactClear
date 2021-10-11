package compact.mobile;
import android.app.Activity;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class ListAsigmentKurir extends Activity {
	String locpk, username, urlphp;
	Button keluar, refresh;
	ListView list;

	JSONArray wblist = null;
	private AsigmentKurirAdapter asigmentAdapter;
	private static final String TANGGAL = "Tanggal";
	private static final String MANIFES = "Manifes";

	ArrayList<HashMap<String, String>> mylistData =
            new ArrayList<HashMap<String, String>>();
	String[] columnTags = new String[] {TANGGAL, MANIFES};
	int[] columnIds = new int[] {R.id.tanggal, R.id.manifes};
	SessionManager session;
	Integer booking = 1234;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_asigment_kurir);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
 	        username = user.get(SessionManager.KEY_USER);
 	        urlphp = user.get(SessionManager.KEY_URL);
 	        locpk = user.get(SessionManager.KEY_LOCPK);
 	       asigmentAdapter = new AsigmentKurirAdapter(this);  
 	      asigmentAdapter.open();
 	     refreshdata();
 	    keluar=(Button)findViewById(R.id.btnKeluar);
        refresh=(Button)findViewById(R.id.btnRefresh);
        list = (ListView) findViewById(R.id.mylist);
        
        Cursor cur = asigmentAdapter.getAllContact();
		Integer t = cur.getCount();
		Log.d("isi list", String.valueOf(t));
		if (cur != null)  {
			cur.moveToFirst();
			if (cur.getCount() > 0) {
				Log.d("Jumlah data on list", String.valueOf(t));
				do{
					//Log.d("tes", cur.getString(2));
					Integer l = cur.getInt(2);
					Log.d("tssasas", String.valueOf(l));
					String manifes = cur.getString(0);
					String tanggal = cur.getString(1);
					String jenis = cur.getString(2);
					String po = cur.getString(3);
					
					HashMap<String, String> map = new HashMap<String, String>();
					 map.put("Manifes", manifes);
					 map.put("Tanggal", tanggal);
					 map.put("Jenis", jenis);
					 map.put("Po", po);
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
                
                String asigment = o.get("Manifes");
                String jenis = o.get("Jenis");
                String po = o.get("Po");
              
                	 Log.d("jenis", jenis);
                	 Intent a = new Intent (ListAsigmentKurir.this,DetailListPickup.class);
                	 a.putExtra("jenis", jenis);
                     a.putExtra("asigment", asigment);
                     startActivityForResult(a, 1);
                     finish();
                
              
               
			    Log.d("error 2", "asigment = " +asigment);
			     
			     
            }
        });
		 refresh.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
					Intent a = new Intent (ListAsigmentKurir.this,ListAsigmentKurir.class);
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
			JSONObject json = jParser.getJSONFromUrl(urlphp + "list_pickup_po.php?kurir=" + username);
			String response = json.getString("response_code");
			asigmentAdapter.deleteAllContact();
			if (response.equals("0")){
				
			}else{
				wblist = json.getJSONArray("list");
				Log.d("error 5", "Userername = " +username+" Url = "+urlphp+" list_pickup_po.php?kurir=" + username);
				int cek = wblist.length();
				Log.d("error ", "isi list = " +String.valueOf(cek));
				
				for(int k = 0; k < wblist.length(); k++){
					 JSONObject ar = wblist.getJSONObject(k);
					 String tanggal = ar.getString("tanggal");
					 String manifes = ar.getString("asigment");
					 String jenis = ar.getString("jenis");
					 String po = ar.getString("awb");
					 Log.d("WBLIST","Manifes : " + manifes + ", tanggal : " + tanggal+" " +jenis+" " + po);
					 ContactAsigmentKurir listbooking = new ContactAsigmentKurir();
			 	       for(int t = 0; t < 5; t++){
			 	 	     	listbooking.setManifes(manifes);
			 	 	    	listbooking.setTanggal(tanggal);
			 	 	    	listbooking.setJenis(jenis);
			 	 	    	listbooking.setPo(po);
			 	 	    	asigmentAdapter.createContact(listbooking);
			 	 	    	Log.d("insert", manifes);
			 	       }
				}
			}


		} catch (JSONException e) {
    		//e.printStackTrace();
    		
    		Log.d("WBLIST","Error : " + e.toString());
    	}
		

	}
}
