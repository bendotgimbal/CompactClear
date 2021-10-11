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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import compact.mobile.R;
import compact.mobile.SuratJalan.helper.ListADSAdapter;
import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;

public class ListADS extends Activity {
	
	TextView TXT_nama_toko_convert;
	Button BTN_keluar, BTN_refresh;
	
//	private ListADSDBAdapter adsDB;

	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_KODE_TOKO = "kode_toko";
	private static final String AR_NAMA_TOKO = "nama_toko";
	private static final String AR_NAMA_COMPANY = "company";
	private static final String AR_NAMA_TOKO_SHORTEN = "sort_nama_toko";
	private static final String AR_STATUS = "status";
	private static final String AR_JUMLAH = "jumlah";
	private static final String AR_NO_PASCODE = "passcode";
	private static final String AR_ALAMAT = "alamat";
	
	String STR_urlphp, STR_URL_API, STR_url_ads;
	String Str_sp_url_assigment;
	String STR_assigment, STR_kode_toko, STR_code_toko, STR_nama_toko, STR_nama_company, STR_jml, STR_status,
	STR_convert_nama_toko, STR_convert_nama_toko_hasil, STR_status_assigment, STR_passcode, STR_passcode_toko;
	String STR_no_assigment, STR_AlamatCustomer, STR_AlamatCust, STR_NamaCustomer;
	String STR_data;
	String RespnseMessage, RespnseCode;
	public String Str_lo_Koneksi, Str_LinkListADS;
	
	final ArrayList<HashMap<String, String>> daftar_list_ads = new ArrayList<HashMap<String, String>>();
	
	SessionManager session;
//	ListADSDBAdapter	db;
	
	SharedPreferences myPrefs;
	SharedPreferences.Editor spEditor;
    SharedPreferences sp;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ads);
        
        Koneksi Str_lo_Koneksi = new Koneksi();
        Str_LinkListADS = Str_lo_Koneksi.ConnADS();
        
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
				Intent a = new Intent(ListADS.this, ListADS.class);
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
        Log.d("Debug", "Halaman ListADS " +"Intent From OtherAssigment >>> " + "Nomor Asigment " + STR_no_assigment);
        
        Str_sp_url_assigment = (myPrefs.getString("sp_url_assigment", ""));
        Log.d("Debug", "URL Shared Preferences"+" || URL Assigment = " + Str_sp_url_assigment);
        
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        STR_urlphp = user.get(SessionManager.KEY_URL);
        Log.e("Debug", "Halaman ListADS " +"ISI >>> " + "URL = " + STR_urlphp);
        
//        if (STR_urlphp.equals("http://43.252.144.14:81/android/")) {
//	    	Log.i("Debug", "Halaman ListADS " +"Replace URL Lokal Server");
//	    	STR_URL_API = STR_urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/compact_mobile");
//		}
////	    else if (STR_urlphp.equals("http://api-mobile.atex.co.id/android/")) {
//        else if (STR_urlphp.equals("http://compact.atex.co.id/android/")) {
//	    	Log.i("Debug", "Halaman ListADS " +"Replace URL Online Server");
//	    	STR_URL_API = STR_urlphp.replace(STR_urlphp, "http://compact.atex.co.id/compact_mobile");
//	    }
//        else if (STR_urlphp.equals("http://apiatrex.alfatrex.id/android/")) {
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
//		Log.i("Debug", "Halaman ListADS " +"Test String Replace URL " + STR_URL_API);
		
////		STR_url_ads = STR_URL_API + "/" +"tokoads"+"/"+ STR_no_assigment;
////		STR_url_ads = Str_sp_url_assigment + "/" +"tokoads"+"/"+ STR_no_assigment;
//        STR_url_ads = STR_urlphp + Str_LinkListADS + STR_no_assigment;
//		Log.d("Debug","Halaman ListADS " +"Test URL " + STR_url_ads);
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpGet request = new HttpGet(STR_url_ads);
//        Log.d("Debug","Halaman ListADS " +"Test URL Assigment " + request);
//        try {
//			HttpResponse response = client.execute(request);
//			HttpEntity entity = response.getEntity();
//			STR_data = EntityUtils.toString(entity);
//			System.out.println(response.getStatusLine());
//			Log.e("Debug", STR_data);
//			daftar_list_ads.clear();
//			HashMap<String, String> mapADS;
//			try {
//				JSONObject jsonObject = new JSONObject(STR_data);
//				JSONArray ListObjADS = jsonObject.getJSONArray("data");
//				RespnseMessage = jsonObject.getString("response_message");
//				RespnseCode = jsonObject.getString("response_code");
//				Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
////				ADS listads = new ADS();
////				adsDB = new ListADSDBAdapter(this);
////				adsDB.open();
////				ADS ads = new ADS();
//				for (int i = 0; i < ListObjADS.length(); i++) {
//					JSONObject obj = ListObjADS.getJSONObject(i);
//					STR_assigment = obj.getString("asigment");
//					STR_kode_toko = obj.getString("kode_toko");
//					STR_nama_toko = obj.getString("nama_toko");
//					STR_nama_company = obj.getString("company");
//					STR_status = obj.getString("status");
//					STR_jml = obj.getString("jumlah");
//					STR_passcode = obj.getString("passcode");
//					STR_AlamatCust = obj.getString("alamat");
//
////					String shorten_StoreName = STR_nama_toko.substring(0,3) + "...";
////					Log.d("Debug", "Shorten Store Name " + shorten_StoreName);
//
////					String shorten_StoreName = STR_nama_toko.replace(" ", "");
////					shorten_StoreName = shorten_StoreName.trim();
////					Log.d("Debug", "Shorten Store Name " + shorten_StoreName);
//
////					TXT_nama_toko_convert = (TextView) findViewById(R.id.txt_status_convert);
////					TXT_nama_toko_convert.setText(STR_nama_toko);
////					STR_convert_nama_toko = TXT_nama_toko_convert.getText().toString();
////					Log.d("Debug","Test Convert Nama Toko 1 " + STR_convert_nama_toko);
////
////					if (TXT_nama_toko_convert.getText().toString().equals("0")) {
////						TXT_nama_toko_convert.setText("Nama Kosong");
////					} else {
////						Log.d("Debug","Alamat Tersedia >>> " + STR_convert_nama_toko);
////					}
////
////					Log.d("Debug","Test Convert Nama Toko 2 " + STR_convert_nama_toko);
////
////					STR_convert_nama_toko_hasil = TXT_nama_toko_convert.getText().toString();
////					Log.d("Debug","Test Convert Nama Toko 3 " + STR_convert_nama_toko_hasil);
//
//					mapADS = new HashMap<String, String>();
//					mapADS.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
//					mapADS.put(AR_KODE_TOKO, obj.getString("kode_toko"));
////					mapADS.put(AR_NAMA_TOKO, STR_convert_nama_toko_hasil);
//					mapADS.put(AR_NAMA_TOKO, obj.getString("nama_toko"));
////					mapADS.put(AR_NAMA_TOKO_SHORTEN, shorten_StoreName);
//					mapADS.put(AR_NAMA_COMPANY, obj.getString("company"));
//					mapADS.put(AR_NAMA_TOKO_SHORTEN, obj.getString("sort_nama_toko"));
//					mapADS.put(AR_STATUS, obj.getString("status"));
//					mapADS.put(AR_JUMLAH, obj.getString("jumlah"));
//					mapADS.put(AR_NO_PASCODE, obj.getString("passcode"));
//					mapADS.put(AR_ALAMAT, obj.getString("alamat"));
//					daftar_list_ads.add(mapADS);
//					Log.d("Debug", "Hashmap ADS " + daftar_list_ads);
//
//////					ADS ads = new ADS();
////					ads.setAsigment(AR_NO_ASSIGMENT);
////					ads.setNamaToko(AR_NAMA_TOKO);
////					ads.setStatus(AR_STATUS);
////					ads.setNoAWB(AR_JUMLAH);
//////					db = new ListADSDBAdapter(ListADS.this);
//////					db.open();
//////					db.createContact(listads);
//////					db.close();
////					adsDB.createContact(ads);
////					Log.d("PUP","ADS to local database .. " + adsDB);
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				Log.d("Debug","Trace = ERROR ");
//			}
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//        this.adapter_listview_ads();

		//Update 02-10-2018
		JSONParseListADS doItInBackGround = new JSONParseListADS();
		doItInBackGround.execute();
	}

	//Update 02-10-2018
	private class JSONParseListADS extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("Debug", "Lewat PreExecute");

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			STR_url_ads = STR_urlphp + Str_LinkListADS + STR_no_assigment;
			Log.d("Debug","Halaman ListADS " +"Test URL " + STR_url_ads);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(STR_url_ads);
			Log.d("Debug","Halaman ListADS " +"Test URL Assigment " + request);

			try {
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_list_ads.clear();
				HashMap<String, String> mapADS;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListObjADS = jsonObject.getJSONArray("data");
					RespnseMessage = jsonObject.getString("response_message");
					RespnseCode = jsonObject.getString("response_code");
					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
					for (int i = 0; i < ListObjADS.length(); i++) {
						JSONObject obj = ListObjADS.getJSONObject(i);
						STR_assigment = obj.getString("asigment");
						STR_kode_toko = obj.getString("kode_toko");
						STR_nama_toko = obj.getString("nama_toko");
						STR_nama_company = obj.getString("company");
						STR_status = obj.getString("status");
						STR_jml = obj.getString("jumlah");
						STR_passcode = obj.getString("passcode");
						STR_AlamatCust = obj.getString("alamat");

						mapADS = new HashMap<String, String>();
						mapADS.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
						mapADS.put(AR_KODE_TOKO, obj.getString("kode_toko"));
						mapADS.put(AR_NAMA_TOKO, obj.getString("nama_toko"));
						mapADS.put(AR_NAMA_COMPANY, obj.getString("company"));
						mapADS.put(AR_NAMA_TOKO_SHORTEN, obj.getString("sort_nama_toko"));
						mapADS.put(AR_STATUS, obj.getString("status"));
						mapADS.put(AR_JUMLAH, obj.getString("jumlah"));
						mapADS.put(AR_NO_PASCODE, obj.getString("passcode"));
						mapADS.put(AR_ALAMAT, obj.getString("alamat"));
						daftar_list_ads.add(mapADS);
						Log.d("Debug", "Hashmap ADS " + daftar_list_ads);
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
			adapter_listview_ads();
			Log.d("Debug", "1.Test Sampai Sini ");

		}
	}
	
	public void adapter_listview_ads() {
		Log.e("Debug", "Listview ADS");
		ListADSAdapter adapterADS= new ListADSAdapter(this, daftar_list_ads);
		ListView listViewADS = (ListView) findViewById(R.id.listviews_ads);
		listViewADS.setAdapter(adapterADS);
		adapterADS.notifyDataSetChanged();
		
		listViewADS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),daftar_list_ads.get(position).get(AR_NAMA_TOKO).toString()+ " Selected", Toast.LENGTH_LONG).show();
				STR_no_assigment = daftar_list_ads.get(position).get(AR_NO_ASSIGMENT).toString();
				STR_status_assigment = daftar_list_ads.get(position).get(AR_STATUS).toString();
				STR_code_toko = daftar_list_ads.get(position).get(AR_KODE_TOKO).toString();
				STR_NamaCustomer = daftar_list_ads.get(position).get(AR_NAMA_TOKO).toString();
				STR_AlamatCustomer = daftar_list_ads.get(position).get(AR_ALAMAT).toString();
				STR_passcode_toko = daftar_list_ads.get(position).get(AR_NO_PASCODE).toString();
//				Intent i = new Intent (ListADS.this, ListDetailADS.class);
//				i.putExtra("AR_NO_ASSIGMENT", STR_no_assigment);
//				i.putExtra("AR_KODE_TOKO", STR_code_toko);
//				i.putExtra("AR_STATUS", STR_status_assigment);
//				Log.d("Debug", "Item Click Status = " +STR_status_assigment);
////				Log.d("Debug", "To Scan Detail ADS >>> " +"No.Assigment = " +STR_no_assigment);
//				Log.d("Debug", "To Scan Detail ADS >>> " +"Isi = " +i);
//				startActivity(i);
				
				HashMap<String, String> o = (HashMap<String, String>) daftar_list_ads.get(position);
				String status = o.get("status");
				if (status.equals("1")) {
					show_dialog_detail();
//					show_dialog_detail2();
				} else if (status.equals("0")) {
//					show_dialog2();
					show_dialog_detail2();
					STR_NamaCustomer = daftar_list_ads.get(position).get(AR_NAMA_TOKO).toString();
					STR_AlamatCustomer = daftar_list_ads.get(position).get(AR_ALAMAT).toString();
					STR_passcode_toko = daftar_list_ads.get(position).get(AR_NO_PASCODE).toString();
//					Intent i = new Intent (ListADS.this, ListDetailADS.class);
//					i.putExtra("AR_NO_ASSIGMENT", STR_no_assigment);
//					i.putExtra("AR_KODE_TOKO", STR_code_toko);
//					i.putExtra("AR_NAMA_TOKO", STR_NamaCustomer);
//					i.putExtra("AR_STATUS", STR_status_assigment);
//					Log.d("Debug", "Item Click Status = " +STR_status_assigment);
////					Log.d("Debug", "To Scan Detail ADS >>> " +"No.Assigment = " +STR_no_assigment);
//					Log.d("Debug", "To Scan Detail ADS >>> " +"Isi = " +i);
//					startActivity(i);
				} else{
//					show_dialog_detail2();
					show_dialog_detail();
				}
			}
		});
	}
	
	public void show_dialog_detail() {
		String STR_AlamatPickup = "Alamat Pickup";
		String STR_alamat_result = "Toko Ini Sudah Di proses";
		String STR_alamat_replace = STR_alamat_result.replace("<br>"," ");
		Log.d("Debug", "Replace <br> --> " + STR_alamat_replace);
		AlertDialog.Builder builder = new AlertDialog.Builder(ListADS.this);
		builder.setTitle(STR_AlamatPickup);
		builder.setMessage(STR_alamat_replace);
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {   
                Toast.makeText(getApplicationContext(),"Clicked Cancel!", Toast.LENGTH_SHORT).show();
              return;   
            	}
            });
		builder.create().show();
	}
	
	public void show_dialog_detail2() {
		String STR_AlamatPickup = "Toko Pickup";
		String STR_NamaCustomer_result = STR_NamaCustomer;
		String STR_alamat_result = STR_AlamatCustomer;
		String STR_KodeToko = STR_code_toko;
		String STR_Company = STR_nama_company;
		String STR_alamat_replace = STR_alamat_result.replace("<br>"," ");
		Log.d("Debug", "Replace <br> --> " + STR_alamat_replace);
		AlertDialog.Builder builder = new AlertDialog.Builder(ListADS.this);
		builder.setTitle(STR_NamaCustomer_result+" - "+STR_Company+"-"+STR_KodeToko);
//		builder.setTitle(STR_NamaCustomer_result);
		builder.setMessage(STR_alamat_replace);
//		builder.setMessage(STR_NamaCustomer_result);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {   
			public void onClick(DialogInterface dialog, int which) {   
//				 Toast.makeText(getApplicationContext(),"Clicked OK!", Toast.LENGTH_SHORT).show();
//			  return; 
				Intent i = new Intent(ListADS.this, ListDetailADS.class);
				i.putExtra("AR_NO_ASSIGMENT", STR_no_assigment);
				i.putExtra("AR_KODE_TOKO", STR_code_toko);
				i.putExtra("AR_NAMA_TOKO", STR_NamaCustomer);
				i.putExtra("AR_STATUS", STR_status_assigment);
				i.putExtra("AR_NO_PASCODE", STR_passcode_toko);
				Log.d("Debug", "Item Click Status = " +STR_status_assigment);
				Log.d("Debug", "To Scan Detail ADS >>> " +"Isi = " +i);
				startActivity(i);
//				startActivityForResult(i, 1);
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
	
}
