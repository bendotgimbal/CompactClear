package compact.mobile.SuratJalan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import compact.mobile.R;
import compact.mobile.SuratJalan.helper.ListAWBOthersAdapter;
import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;

public class ListAWBOthers extends Activity {
	
	Button BTN_keluar, BTN_refresh;
	
	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_KODE_CUST = "kode_customer";
	private static final String AR_NAMA_CUST = "nama_customer";
	private static final String AR_NAMA_CUST_SHORTEN = "sort_nama_customer";
	private static final String AR_STATUS = "status";
	private static final String AR_JUMLAH = "jumlah";
	
	String STR_urlphp, STR_URL_API, STR_url_awb_others;
	String Str_sp_url_assigment;
	String STR_assigment, STR_kode_cust, Str_cust_kode, STR_nama_cust, STR_status, STR_jumlah, STR_status_assigment;
	String STR_no_assigment;
	String STR_data;
	String RespnseMessage, RespnseCode;
	public String Str_lo_Koneksi, Str_LinkListAWBOthers;
	
	SessionManager session;
	
	SharedPreferences myPrefs;
	SharedPreferences.Editor spEditor;
    SharedPreferences sp;
	
	final ArrayList<HashMap<String, String>> daftar_list_awb_others = new ArrayList<HashMap<String, String>>();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_awb_others);
        
        Koneksi Str_lo_Koneksi = new Koneksi();
        Str_LinkListAWBOthers = Str_lo_Koneksi.ConnAWBOthers();
        
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
				Intent a = new Intent(ListAWBOthers.this, ListAWBOthers.class);
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
        Log.d("Debug", "Halaman ListAWBOthers " +"Intent From OtherAssigment >>> " + "Nomor Asigment " + STR_no_assigment);
        
        Str_sp_url_assigment = (myPrefs.getString("sp_url_assigment", ""));
        Log.d("Debug", "URL Shared Preferences"+" || URL Assigment = " + Str_sp_url_assigment);
        
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        STR_urlphp = user.get(SessionManager.KEY_URL);
        Log.e("Debug", "Halaman ListAWBOthers " +"ISI >>> " + "URL = " + STR_urlphp);
        
//        if (STR_urlphp.equals("http://43.252.144.14:81/android/")) {
//	    	Log.i("Debug", "Halaman ListADS " +"Replace URL Lokal Server");
//	    	STR_URL_API = STR_urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/compact_mobile");
//		}
//	    else if (STR_urlphp.equals("http://api-mobile.atex.co.id/android/")) {
//	    	Log.i("Debug", "Halaman ListAWBOthers " +"Replace URL Online Server");
//	    	STR_URL_API = STR_urlphp.replace(STR_urlphp, "http://api-mobile.atex.co.id/compact_mobile");
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
//		Log.i("Debug", "Halaman ListAWBOthers " +"Test String Replace URL " + STR_URL_API);
		
////		STR_url_awb_others = STR_URL_API + "/" +"pickup_other"+"/"+ STR_no_assigment;
////		STR_url_awb_others = Str_sp_url_assigment + "/" +"pickup_other"+"/"+ STR_no_assigment;
//        STR_url_awb_others = STR_urlphp + Str_LinkListAWBOthers + STR_no_assigment;
//		Log.d("Debug","Halaman ListAWBOthers" +"Test URL " + STR_url_awb_others);
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpGet request = new HttpGet(STR_url_awb_others);
//        Log.d("Debug","Halaman ListAWBOthers " +"Test URL Assigment " + request);
//        try {
//        	HttpResponse response = client.execute(request);
//			HttpEntity entity = response.getEntity();
//			STR_data = EntityUtils.toString(entity);
//			System.out.println(response.getStatusLine());
//			Log.e("Debug", STR_data);
//			daftar_list_awb_others.clear();
//			HashMap<String, String> mapAWBOthers;
//			try {
//				JSONObject jsonObject = new JSONObject(STR_data);
//				JSONArray ListObjAWBOthers = jsonObject.getJSONArray("data");
//				RespnseMessage = jsonObject.getString("response_message");
//				RespnseCode = jsonObject.getString("response_code");
//				Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
//				for (int i = 0; i < ListObjAWBOthers.length(); i++) {
//					JSONObject obj = ListObjAWBOthers.getJSONObject(i);
//					STR_assigment = obj.getString("asigment");
//					STR_kode_cust = obj.getString("kode_customer");
//					STR_nama_cust = obj.getString("nama_customer");
//					STR_status = obj.getString("status");
//					STR_jumlah = obj.getString("jumlah");
////					String shorten_CustName = STR_nama_cust.substring(0,3) + "...";
////					Log.d("Debug", "Shorten Customer Name " + shorten_CustName);
//
//					mapAWBOthers = new HashMap<String, String>();
//					mapAWBOthers.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
//					mapAWBOthers.put(AR_KODE_CUST, obj.getString("kode_customer"));
//					mapAWBOthers.put(AR_NAMA_CUST, obj.getString("nama_customer"));
////					mapAWBOthers.put(AR_NAMA_CUST_SHORTEN, shorten_CustName);
//					mapAWBOthers.put(AR_NAMA_CUST_SHORTEN, obj.getString("sort_nama_customer"));
//					mapAWBOthers.put(AR_STATUS, obj.getString("status"));
//					mapAWBOthers.put(AR_JUMLAH, obj.getString("jumlah"));
//					daftar_list_awb_others.add(mapAWBOthers);
//					Log.d("Debug", "Hashmap AWB Others " + daftar_list_awb_others);
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				Log.d("Debug", "Trace = ERROR ");
//			}
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		this.adapter_listview_awb_others();

		//Update 02-10-2018
		JSONParseListAWBOthers doItInBackGround = new JSONParseListAWBOthers();
		doItInBackGround.execute();
        
	}

	//Update 02-10-2018
	private class JSONParseListAWBOthers extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("Debug", "Lewat PreExecute");

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			STR_url_awb_others = STR_urlphp + Str_LinkListAWBOthers + STR_no_assigment;
			Log.d("Debug","Halaman ListAWBOthers" +"Test URL " + STR_url_awb_others);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(STR_url_awb_others);
			Log.d("Debug","Halaman ListAWBOthers " +"Test URL Assigment " + request);

			try {
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_list_awb_others.clear();
				HashMap<String, String> mapAWBOthers;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListObjAWBOthers = jsonObject.getJSONArray("data");
					RespnseMessage = jsonObject.getString("response_message");
					RespnseCode = jsonObject.getString("response_code");
					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
					for (int i = 0; i < ListObjAWBOthers.length(); i++) {
						JSONObject obj = ListObjAWBOthers.getJSONObject(i);
						STR_assigment = obj.getString("asigment");
						STR_kode_cust = obj.getString("kode_customer");
						STR_nama_cust = obj.getString("nama_customer");
						STR_status = obj.getString("status");
						STR_jumlah = obj.getString("jumlah");
//					String shorten_CustName = STR_nama_cust.substring(0,3) + "...";
//					Log.d("Debug", "Shorten Customer Name " + shorten_CustName);

						mapAWBOthers = new HashMap<String, String>();
						mapAWBOthers.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
						mapAWBOthers.put(AR_KODE_CUST, obj.getString("kode_customer"));
						mapAWBOthers.put(AR_NAMA_CUST, obj.getString("nama_customer"));
//					mapAWBOthers.put(AR_NAMA_CUST_SHORTEN, shorten_CustName);
						mapAWBOthers.put(AR_NAMA_CUST_SHORTEN, obj.getString("sort_nama_customer"));
						mapAWBOthers.put(AR_STATUS, obj.getString("status"));
						mapAWBOthers.put(AR_JUMLAH, obj.getString("jumlah"));
						daftar_list_awb_others.add(mapAWBOthers);
						Log.d("Debug", "Hashmap AWB Others " + daftar_list_awb_others);
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
			adapter_listview_awb_others();
			Log.d("Debug", "1.Test Sampai Sini ");

		}
	}
	
	public void adapter_listview_awb_others() {
		Log.e("Debug", "Listview AWB Others");
		ListAWBOthersAdapter adapterAWBOthers = new ListAWBOthersAdapter(this,daftar_list_awb_others);
		ListView listViewAWBOthers = (ListView) findViewById(R.id.listviews_awb_others);
		listViewAWBOthers.setAdapter(adapterAWBOthers);
		adapterAWBOthers.notifyDataSetChanged();
		
		listViewAWBOthers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Toast.makeText(getApplicationContext(),daftar_list_awb_others.get(position).get(AR_NAMA_CUST).toString()+ " Selected", Toast.LENGTH_LONG).show();
            	STR_no_assigment = daftar_list_awb_others.get(position).get(AR_NO_ASSIGMENT).toString();
            	STR_status_assigment = daftar_list_awb_others.get(position).get(AR_STATUS).toString();
            	Str_cust_kode = daftar_list_awb_others.get(position).get(AR_KODE_CUST).toString();
				Intent i = new Intent (ListAWBOthers.this, ListDetailAWBOthers.class);
				i.putExtra("AR_NO_ASSIGMENT", STR_no_assigment);
				i.putExtra("AR_KODE_CUST", Str_cust_kode);
				i.putExtra("AR_STATUS", STR_status_assigment);
				Log.d("Debug", "Item Click Status = " +STR_status_assigment);
				Log.d("Debug", "To Detail AWB Others >>> " +"Isi = " +i);
				
				spEditor.putString(sharedpref.SP_NO_SPJ, STR_no_assigment);
				spEditor.putString(sharedpref.SP_STATUS_ASSIGMENT, STR_status_assigment);
				spEditor.putString(sharedpref.SP_KODE_CUST, Str_cust_kode);
	            spEditor.commit();
				startActivity(i);
            }
        });
		
	}

}
