package compact.mobile;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

//import android.widget.ArrayAdapter;

public class wblist extends Activity  {
	String locpk, username, urlphp;
	Button keluar, refresh;
	ListView list;

	JSONArray wblist = null;
	 
	private static final String WAYBILL = "Nomor";
	private static final String PENERIMA = "Penerima";
	private static final String ALAMAT = "Alamat";
	private static final String LATLONG = "Latlong";
	ArrayList<HashMap<String, String>> mylistData =
            new ArrayList<HashMap<String, String>>();
	String[] columnTags = new String[] {WAYBILL, PENERIMA, ALAMAT,LATLONG};
	int[] columnIds = new int[] {R.id.nomor, R.id.penerima, R.id.alamat, R.id.latlong};
	SessionManager session;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wblist);
        
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
 	        username = user.get(SessionManager.KEY_USER);
 	        urlphp = user.get(SessionManager.KEY_URL);
 	        locpk = user.get(SessionManager.KEY_LOCPK);
 	        

        keluar=(Button)findViewById(R.id.btnKeluar);
        refresh=(Button)findViewById(R.id.btnRefresh);
        list = (ListView) findViewById(R.id.mylist);
        Log.d("error", "Userername = " +username+" Url = "+urlphp+" locpk = "+locpk);
        refreshdata();
        
		list.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				String kode = ((TextView) arg1.findViewById(R.id.nomor)).getText().toString();
				String coordinat = ((TextView) arg1.findViewById(R.id.latlong)).getText().toString();
				String alamat = ((TextView) arg1.findViewById(R.id.alamat)).getText().toString();
				 noWaybill obnomor = noWaybill.getInstance();
			     obnomor.setData(kode);
			     
			     if (coordinat.equals("0,0"))
			     {
			    	 coordinat = alamat.replace(" ","+");
			     }
			     show_dialog2(arg1,kode,coordinat);
				//show_dialog(arg1);
				//Intent in = new Intent(wblist.this, Detailwb.class);
				//in.putExtra(AR_NO, kode);
				//startActivity(in);
			}
		});
		
		 refresh.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
					Intent a = new Intent (wblist.this,wblist.class);
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
			JSONObject json = jParser.getJSONFromUrl(urlphp + "wblistcourier.php?kurir=" + username + "&locpk=" + locpk);
			Log.d("error 4", urlphp + "wblistcourier.php?kurir=" + username + "&locpk=" + locpk);
			wblist = json.getJSONArray("wblist");
			Log.d("error 5", "Userername = " +username+" Url = "+urlphp+" locpk = "+locpk);
		
			for(int i = 0; i < wblist.length(); i++){
				 JSONObject ar = wblist.getJSONObject(i);
				 String nomor = ar.getString("nomor");
				 String penerima = ar.getString("penerima");
				 String alamat = ar.getString("alamat");
				 String latlong = ar.getString("latlong");
				 Log.d("WBLIST","Waybill : " + nomor + ", Penerima : " + penerima + ", Latlong : " + latlong + ",  Alamat : " + alamat);
				 HashMap<String, String> map = new HashMap<String, String>();
				 map.put(WAYBILL, nomor);
				 map.put(PENERIMA, penerima);
				 map.put(ALAMAT, alamat);
				 map.put(LATLONG, latlong);
				 mylistData.add(map);
			}
		} catch (JSONException e) {
    		//e.printStackTrace();
    		
    		Log.d("WBLIST","Error : " + e.toString());
    	}
		
		SimpleAdapter arrayAdapter =
	               new SimpleAdapter(this, mylistData, R.layout.listviewrow,
	                             columnTags , columnIds);
		list.setAdapter(arrayAdapter);		
	}
	
	public void adapter_listview() {
			 
		//ListAdapter adapter = new SimpleAdapter(wblist.this,mylistData,
		//		R.layout.listviewrow,
		//		new String[] { AR_NO, AR_STS }, new int[] {
		//		R.id.nomor, R.id.status});
		// setListAdapter(adapter);
		// ListView lv = getListView();
		 //lv.setOnItemClickListener(new OnItemClickListener() {
	
		// public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		// String kode = ((TextView) view.findViewById(R.id.nomor)).getText().toString();
		 
		// Intent in = new Intent(AksesServerActivity.this, DetailAksesServer.class);
		// in.putExtra(AR_ID, kode);
		// startActivity(in);
		 
		 //}
		// });
	}

	
	public void show_dialog2(View theButton, String AWB, final String coordinat){
		 AlertDialog.Builder builder = new AlertDialog.Builder(wblist.this);
		    builder.setTitle("Action for waybill "+ AWB);
		    builder.setItems(new CharSequence[]
		            {"Search GPS", "POD", "DEX", "Cancel"},
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int which) {
		                    // The 'which' argument contains the index position
		                    // of the selected item
		                    switch (which) {
		                        case 0:
		                        	//String CURRENT_LOCATION = "-6.360556, 106.833461";
		                        	//String CURRENT_LOCATION = coordinat;
		    						//Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.co.id/maps?q="+ coordinat ));
		                        	Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
		    								Uri.parse("http://maps.google.co.id/maps?saddr=Current+Location&daddr"+ coordinat));
		    						startActivity(intent);
		                            break;
		                        case 1:
		        	   				startActivity(new Intent (wblist.this,cekpod.class));
		                            break;
		                        case 2:
		                        	
		        	   				startActivity(new Intent (wblist.this,cekdex.class));
		                            break;
		                        case 3:
		                        	dialog.dismiss();
		                            break;
		                    }
		                }
		            });
		    builder.create().show();
	}
	
	public void show_dialog(View theButton){
		
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
		        wblist.this);
		 
		// Setting Dialog Title
		alertDialog2.setTitle("Action for this Waybill");
		 
		// Setting Dialog Message
		alertDialog2.setMessage("Please select ?");
		 
		// Setting Icon to Dialog
		//alertDialog2.setIcon(R.drawable.delete);
		 
		// Setting Positive "Yes" Btn
		alertDialog2.setPositiveButton("DEX",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {

		            Intent a = new Intent (wblist.this,cekdex.class);
	   				startActivity(a);
		            }
		        });
		// Setting Negative "NO" Btn
		alertDialog2.setNegativeButton("POD",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
	   				Intent a = new Intent (wblist.this,cekpod.class);
	   				startActivity(a);
		            }
		        });
		alertDialog2.setNeutralButton("Cancel",
				new DialogInterface.OnClickListener() {
			 		public void onClick(DialogInterface dialog, int which) {
	              //  finish();
	               // Intent a = new Intent (wblist.this,cekpod.class);
	                //startActivity(a);
			 			dialog.dismiss();
			 		}
				});
		alertDialog2.show();
	}	
	
 }
