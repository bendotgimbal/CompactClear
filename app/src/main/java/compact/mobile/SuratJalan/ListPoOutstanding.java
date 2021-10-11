package compact.mobile.SuratJalan;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import compact.mobile.JSONParser;
import compact.mobile.SessionManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import compact.mobile.R;
import compact.mobile.SuratJalan.DB.ListPoOutstandingDBAdapter;
import compact.mobile.SuratJalan.Scan.multiScanPoOutstanding;
import compact.mobile.SuratJalan.helper.ListPoOutstandingAdapter;
import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;

public class ListPoOutstanding extends Activity {
	
	Button BTN_keluar, BTN_refresh;

	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_NO_PO= "no_po";
	private static final String AR_KODE_CUST = "kode_customer";
	private static final String AR_NAMA_CUST = "nama_customer";
	private static final String AR_NAMA_CUST_SHORTEN = "sort_nama_customer";
	private static final String AR_STATUS = "status";
	private static final String AR_ALAMAT_PICKUP = "alamat";
	
	String STR_urlphp, STR_URL_API, STR_url_po_outstanding;
	String Str_sp_url_assigment;
	String STR_assigment, STR_po, STR_cust, STR_status, STR_alamat;
	String STR_no_assigment, STR_no_po, STR_AlamatCustomer, STR_NamaCustomer;
	String STR_data;
	String RespnseMessage, RespnseCode;
	public String Str_lo_Koneksi, Str_LinkListPOOutstanding;
	
	final ArrayList<HashMap<String, String>> daftar_list_po_outstanding = new ArrayList<HashMap<String, String>>();
	
	SessionManager session;
	ListPoOutstandingDBAdapter db;
	
	SharedPreferences myPrefs;
	SharedPreferences.Editor spEditor;
    SharedPreferences sp;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_po_outstanding);
        
        Koneksi Str_lo_Koneksi = new Koneksi();
        Str_LinkListPOOutstanding = Str_lo_Koneksi.ConnPOOutstanding();
        
        myPrefs = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE); 
//		this.context = context;
		spEditor = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE).edit();
        sp = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE);
        spEditor = myPrefs.edit();
        
        BTN_keluar=(Button)findViewById(R.id.btnKeluar);
        BTN_refresh=(Button)findViewById(R.id.btnRefresh);
        
        BTN_refresh.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				Intent a = new Intent(ListPoOutstanding.this, ListPoOutstanding.class);
				startActivity(a);

			}
		});
	 
		BTN_keluar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
        
        Intent in = getIntent();
     	////No Assigment
        STR_no_assigment = (myPrefs.getString("sp_no_assignment", ""));
//        STR_no_assigment = in.getStringExtra("AR_NO_ASSIGMENT");
        Log.d("Debug", "Halaman ListPoOutstanding " +"Intent From OtherAssigment >>> " + "Nomor Asigment " + STR_no_assigment);
        
        Str_sp_url_assigment = (myPrefs.getString("sp_url_assigment", ""));
        Log.d("Debug", "URL Shared Preferences"+" || URL Assigment = " + Str_sp_url_assigment);
        
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        STR_urlphp = user.get(SessionManager.KEY_URL);
        Log.e("Debug", "Halaman ListPoOutstanding " +"ISI >>> " + "URL = " + STR_urlphp);
        
//        if (STR_urlphp.equals("http://43.252.144.14:81/android/")) {
//	    	Log.i("Debug", "Halaman ListPoOutstanding " +"Replace URL Lokal Server");
//	    	STR_URL_API = STR_urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/compact_mobile");
//		}
//	    else if (STR_urlphp.equals("http://api-mobile.atex.co.id/android/")) {
//	    	Log.i("Debug", "Halaman ListPoOutstanding " +"Replace URL Online Server");
//	    	STR_URL_API = STR_urlphp.replace(STR_urlphp, "http://compact.atex.co.id/compact_mobile");
//	    }
//	    else if (STR_urlphp.equals("http://apiatrex.alfatrex.id/android/")) {
//			Log.i("Debug", "Halaman MainActivity "+ "Replace URL Online Server New Compact Mobile - Second API");
//			STR_URL_API = STR_urlphp.replace(STR_urlphp,"http://apiatrex.alfatrex.id/compact_mobile");
//		}
//		else if (STR_urlphp.equals("http://api.alfatrex.id/android/")) {
//			Log.i("Debug", "Halaman MainActivity "+ "Replace URL Online Server New Compact Mobile");
//			STR_URL_API = STR_urlphp.replace(STR_urlphp,"http://api.alfatrex.id/compact_mobile");
//		}
//	    else {
//	    	Toast.makeText(getApplicationContext(), "Server Tidak Tersedia",0).show();
//	    }
//        
//        STR_URL_API = STR_urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/compact_mobile");
//		Log.i("Debug", "Halaman ListPoOutstanding " +"Test String Replace URL " + STR_URL_API);
		
////		STR_url_po_outstanding = STR_URL_API + "/" +"po_outstanding"+"/"+ STR_no_assigment;
////		STR_url_po_outstanding = Str_sp_url_assigment + "/" +"po_outstanding"+"/"+ STR_no_assigment;
//        STR_url_po_outstanding = STR_urlphp + Str_LinkListPOOutstanding + STR_no_assigment;
//		Log.d("Debug","Halaman ListPoOutstanding " +"Test URL " + STR_url_po_outstanding);
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpGet request = new HttpGet(STR_url_po_outstanding);
//        Log.d("Debug","Halaman ListPoOutstanding " +"Test URL Assigment " + request);
//        try {
//        	HttpResponse response = client.execute(request);
//			HttpEntity entity = response.getEntity();
//			STR_data = EntityUtils.toString(entity);
//			System.out.println(response.getStatusLine());
//			Log.e("Debug", STR_data);
//			daftar_list_po_outstanding.clear();
//			HashMap<String, String> mapPoOutstanding;
//			try {
//				JSONObject jsonObject = new JSONObject(STR_data);
//				JSONArray ListObjPoOutstanding = jsonObject.getJSONArray("data");
//				RespnseMessage = jsonObject.getString("response_message");
//				RespnseCode = jsonObject.getString("response_code");
//				Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
//				for (int i = 0; i < ListObjPoOutstanding.length(); i++) {
//					JSONObject obj = ListObjPoOutstanding.getJSONObject(i);
//					STR_assigment = obj.getString("asigment");
//					STR_po = obj.getString("no_po");
//					STR_cust = obj.getString("nama_customer");
//					STR_status = obj.getString("status");
//					STR_alamat = obj.getString("alamat");
//
////					String shorten_StoreName = STR_cust.substring(0,4) + "...";
////					Log.d("Debug", "Shorten Store Name " + shorten_StoreName);
//
//					mapPoOutstanding = new HashMap<String, String>();
//					mapPoOutstanding.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
//					mapPoOutstanding.put(AR_NO_PO, obj.getString("no_po"));
//					mapPoOutstanding.put(AR_KODE_CUST, obj.getString("kode_customer"));
//					mapPoOutstanding.put(AR_NAMA_CUST, obj.getString("nama_customer"));
////					mapPoOutstanding.put(AR_NAMA_CUST_SHORTEN, shorten_StoreName);
//					mapPoOutstanding.put(AR_NAMA_CUST_SHORTEN, obj.getString("sort_nama_customer"));
//					mapPoOutstanding.put(AR_STATUS, obj.getString("status"));
//					mapPoOutstanding.put(AR_ALAMAT_PICKUP, obj.getString("alamat"));
//					daftar_list_po_outstanding.add(mapPoOutstanding);
//					Log.d("Debug", "Hashmap PO Outstanding " + daftar_list_po_outstanding);
//
////					PoOutstanding po_outstanding = new PoOutstanding();
////					po_outstanding.setAsigment(AR_NO_ASSIGMENT);
////					po_outstanding.setNoPo(AR_NO_PO);
////					po_outstanding.setKodeCustomer(AR_KODE_CUST);
////					po_outstanding.setNamaCustomer(AR_NAMA_CUST);
////					po_outstanding.setStatus(AR_STATUS);
////					db = new ListPoOutstandingDBAdapter(ListPoOutstanding.this);
////					db.open();
////					db.createContact(po_outstanding);
////					db.close();
////					Log.d("PUP","PO Outstanding to local database .. " + po_outstanding);
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				Log.d("Debug","Trace = ERROR ");
//			}
//        } catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//        this.adapter_listview_po_outstanding();

		//Update 02-10-2018
		JSONParseListPoOutstanding doItInBackGround = new JSONParseListPoOutstanding();
		doItInBackGround.execute();
	}

	//Update 02-10-2018
	private class JSONParseListPoOutstanding extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("Debug", "Lewat PreExecute");

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			STR_url_po_outstanding = STR_urlphp + Str_LinkListPOOutstanding + STR_no_assigment;
			Log.d("Debug","Halaman ListPoOutstanding " +"Test URL " + STR_url_po_outstanding);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(STR_url_po_outstanding);
			Log.d("Debug","Halaman ListPoOutstanding " +"Test URL Assigment " + request);

			try {
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_list_po_outstanding.clear();
				HashMap<String, String> mapPoOutstanding;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListObjPoOutstanding = jsonObject.getJSONArray("data");
					RespnseMessage = jsonObject.getString("response_message");
					RespnseCode = jsonObject.getString("response_code");
					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
					for (int i = 0; i < ListObjPoOutstanding.length(); i++) {
						JSONObject obj = ListObjPoOutstanding.getJSONObject(i);
						STR_assigment = obj.getString("asigment");
						STR_po = obj.getString("no_po");
						STR_cust = obj.getString("nama_customer");
						STR_status = obj.getString("status");
						STR_alamat = obj.getString("alamat");

						mapPoOutstanding = new HashMap<String, String>();
						mapPoOutstanding.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
						mapPoOutstanding.put(AR_NO_PO, obj.getString("no_po"));
						mapPoOutstanding.put(AR_KODE_CUST, obj.getString("kode_customer"));
						mapPoOutstanding.put(AR_NAMA_CUST, obj.getString("nama_customer"));
						mapPoOutstanding.put(AR_NAMA_CUST_SHORTEN, obj.getString("sort_nama_customer"));
						mapPoOutstanding.put(AR_STATUS, obj.getString("status"));
						mapPoOutstanding.put(AR_ALAMAT_PICKUP, obj.getString("alamat"));
						daftar_list_po_outstanding.add(mapPoOutstanding);
						Log.d("Debug", "Hashmap PO Outstanding " + daftar_list_po_outstanding);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			adapter_listview_po_outstanding();
			Log.d("Debug", "1.Test Sampai Sini ");

		}
	}
	
	public void adapter_listview_po_outstanding() {
		Log.e("Debug", "Listview PO Outstanding");
		ListPoOutstandingAdapter adapterPoOutstanding = new ListPoOutstandingAdapter(this, daftar_list_po_outstanding);
		ListView listViewPoOutstanding = (ListView) findViewById(R.id.listviews_po_outstanding);
		listViewPoOutstanding.setAdapter(adapterPoOutstanding);
		adapterPoOutstanding.notifyDataSetChanged();
		
		listViewPoOutstanding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),daftar_list_po_outstanding.get(position).get(AR_NO_PO).toString()+ " Selected", Toast.LENGTH_LONG).show();
				STR_no_assigment = daftar_list_po_outstanding.get(position).get(AR_NO_ASSIGMENT).toString();
				STR_no_po = daftar_list_po_outstanding.get(position).get(AR_NO_PO).toString();
				STR_NamaCustomer = daftar_list_po_outstanding.get(position).get(AR_NAMA_CUST).toString();
				STR_AlamatCustomer = daftar_list_po_outstanding.get(position).get(AR_ALAMAT_PICKUP).toString();
////				Intent i = new Intent(getApplicationContext(), OtherAssigment.class);
////				Intent i = new Intent (ListPoOutstanding.this, ScanAWB.class);
////				i.putExtra("AR_NO_ASSIGMENT", STR_no_assigment);
////				Log.d("Debug", "To Scan AWB >>> " +"No.Assigment = " +STR_no_assigment);
////				Intent i = new Intent (ListPoOutstanding.this, multi_pup.class);
//				Intent i = new Intent(ListPoOutstanding.this, multiScanPoOutstanding.class);
//				i.putExtra("asigment", STR_no_assigment);
//				i.putExtra("po", STR_no_po);
//				Log.d("Debug", "To Scan AWB >>> " 
//				+"No.Assigment = " +STR_no_assigment
//				+" No.PO = " +STR_no_po);
//				startActivity(i);
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) daftar_list_po_outstanding.get(position);
				String status = o.get("status");
				// Log.d("tes waybill", waybill);
				if (status.equals("0")) {
					Log.i("Debug", "Status Open");
//					show_dialog1();
					show_dialog3();
					STR_NamaCustomer = daftar_list_po_outstanding.get(position).get(AR_NAMA_CUST).toString();
					STR_AlamatCustomer = daftar_list_po_outstanding.get(position).get(AR_ALAMAT_PICKUP).toString();
				} else if (status.equals("1")) {
					Log.i("Debug", "Status Closed");
					show_dialog2();
				}
			}
});
		
	}
	
	public void show_dialog1() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ListPoOutstanding.this);
		builder.setTitle("Action ");
		builder.setItems(new CharSequence[] { "Pickup", "Batal" },

		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {
				case 0:
//					Intent a = new Intent(ListDetailADS.this, multi_pup.class);
					Intent i = new Intent(ListPoOutstanding.this, multiScanPoOutstanding.class);
					i.putExtra("asigment", STR_no_assigment);
					i.putExtra("po", STR_no_po);
					startActivityForResult(i, 1);
//					finish();
					break;
				case 1:

					break;
				}
			}
		});
		builder.create().show();
	}
	
	public void show_dialog2() {
		Toast.makeText(getApplicationContext(),"No.PO ini sudah selesai di proses" + STR_no_po, Toast.LENGTH_LONG).show();
//		AlertDialog.Builder builder = new AlertDialog.Builder(ListPoOutstanding.this);
//        builder.setTitle("Action ");
//        builder.setItems(new CharSequence[]
//                        {"Pickup", "Batal"},
//
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
////                               	update_data();
//                                break;
//                            case 1:
//                                break;
//                        }
//                    }
//                });
//        builder.create().show();
	}
	
	public void show_dialog3() {
		String STR_AlamatPickup = "Alamat Pickup";
		String STR_NamaCustomer_result = STR_NamaCustomer;
		String STR_alamat_result = STR_AlamatCustomer;
		String STR_alamat_replace = STR_alamat_result.replace("<br>"," ");
		Log.d("Debug", "Replace <br> --> " + STR_alamat_replace);
		AlertDialog.Builder builder = new AlertDialog.Builder(ListPoOutstanding.this);
//		builder.setTitle(STR_AlamatPickup);
		builder.setTitle(STR_NamaCustomer_result);
		builder.setMessage(STR_alamat_replace);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {   
			public void onClick(DialogInterface dialog, int which) {   
//				 Toast.makeText(getApplicationContext(),"Clicked OK!", Toast.LENGTH_SHORT).show();
//			  return; 
				Intent i = new Intent(ListPoOutstanding.this, multiScanPoOutstanding.class);
				i.putExtra("asigment", STR_no_assigment);
				i.putExtra("po", STR_no_po);
				startActivityForResult(i, 1);
				} 
			});
		
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {   
                Toast.makeText(getApplicationContext(),"Clicked Cancel!", Toast.LENGTH_SHORT).show();
              return;   
            	}
            });
		builder.create().show();
	}
	
	public void onBackPressed() {
	      super.onBackPressed();
	      finish();
	  }
	
}
