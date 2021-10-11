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
import compact.mobile.R;
import compact.mobile.SessionManager;
import compact.mobile.multi_pup;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import compact.mobile.SuratJalan.Scan.multiScanAWBOthers;
import compact.mobile.SuratJalan.helper.ListDetailAWBOthersAdapter;
import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;

public class ListDetailAWBOthers extends Activity {
	//09-08-2017
	Button BTN_keluar, BTN_refresh, BTN_scan, BTN_finish;
	
	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_NO_AWB = "no_awb";
	private static final String AR_STATUS = "status";
	private static final String AR_ALAMAT = "alamat";
	
	String STR_urlphp, STR_URL_API, STR_url_awb_others_detail;
	String Str_sp_url_assigment;
	String STR_assigment, STR_no_awb, STR_status_assigment, STR_status, STR_alamat;
	String STR_no_assigment, STR_kode_cust;
	String STR_data;
	String RespnseMessage, RespnseCode;
	public String Str_lo_Koneksi, Str_LinkDetailAWBOthers;
	
	SessionManager session;
    FrameLayout fl_progress;
	
	SharedPreferences myPrefs;
	SharedPreferences.Editor spEditor;
    SharedPreferences sp;
	
	final ArrayList<HashMap<String, String>> daftar_list_awb_others_detail = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> daftar_validation_waybill_list = new ArrayList<HashMap<String, String>>();

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(compact.mobile.R.layout.activity_list_awb_others_detail);
        
        Koneksi Str_lo_Koneksi = new Koneksi();
        Str_LinkDetailAWBOthers = Str_lo_Koneksi.ConnAWBOthersDetail();
        
        myPrefs = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE); 
//		this.context = context;
		spEditor = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE).edit();
        sp = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE);
        spEditor = myPrefs.edit();
        
        BTN_scan=(Button)findViewById(compact.mobile.R.id.btnScan);
        BTN_keluar=(Button)findViewById(compact.mobile.R.id.btnKeluar);
        BTN_refresh=(Button)findViewById(compact.mobile.R.id.btnRefresh);
        BTN_finish=(Button)findViewById(compact.mobile.R.id.btnFinish);
        fl_progress = (FrameLayout) findViewById(R.id.fl_progress);
        
        BTN_refresh.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				Intent a = new Intent(ListDetailAWBOthers.this, ListDetailAWBOthers.class);
				startActivity(a);

			}
		});
	 
		BTN_keluar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		BTN_scan.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String STR_assigment_in_button = STR_no_assigment;
				String STR_awb_in_button = STR_no_awb;
				String STR_status_in_button = STR_status;
				String STR_status_assigment_in_button = STR_status_assigment;
				Log.d("Debug","Isi Di Button Scan"
				+" No.Assigment = "+STR_assigment
				+" No.AWB = "+STR_awb_in_button
				+" Status = "+STR_status_in_button
				+" Status Assigment = "+STR_status_assigment_in_button);
				
//				if (STR_status_assigment_in_button.equals("0")) {
//					Log.i("Debug", "Status Open");
////					show_dialog1();
					Intent a = new Intent(ListDetailAWBOthers.this, multiScanAWBOthers.class);
					a.putExtra("asigment", STR_no_assigment);
//					a.putExtra("po", STR_assigment);
					a.putExtra("awb", STR_no_awb);
					a.putExtra("kode_toko", STR_kode_cust);
                a.putExtra("daftar_validation_waybill_list", daftar_validation_waybill_list);
					startActivityForResult(a, 1);
//				} else if (STR_status_assigment_in_button.equals("1")) {
//					Log.i("Debug", "Status Closed");
////					show_dialog2();
//				}
			}
		});
        
        Intent in = getIntent();
     	////No Assigment
//        STR_no_assigment = in.getStringExtra("AR_NO_ASSIGMENT");
//        STR_kode_cust = in.getStringExtra("AR_KODE_CUST");
//        STR_status_assigment = in.getStringExtra("AR_STATUS");
//        Log.d("Debug", "Halaman ListDetailAWBOthers " +"Intent From ListAWBOthers >>>" 
//        + " Nomor Asigment " + STR_no_assigment
//        + " Kode Customer " + STR_kode_cust
//        + " Status Assigment " + STR_status_assigment);
        
        STR_no_assigment = (myPrefs.getString("sp_no_assignment", ""));
        Log.d("Debug", "Halaman ListDetailAWBOthers " +"Intent From ListAWBOthers >>> " + "Nomor Asigment " + STR_no_assigment);
        STR_kode_cust = (myPrefs.getString("sp_kode_cust", ""));
        Log.d("Debug", "Halaman ListDetailAWBOthers " +"Intent From ListAWBOthers >>> " + "Kode Customer " + STR_kode_cust);
        STR_status_assigment = (myPrefs.getString("sp_status_assigment", ""));
        Log.d("Debug", "Halaman ListDetailAWBOthers " +"Intent From ListAWBOthers >>> " + "Status Asigment " + STR_status_assigment);
        
        Str_sp_url_assigment = (myPrefs.getString("sp_url_assigment", ""));
        Log.d("Debug", "URL Shared Preferences"+" || URL Assigment = " + Str_sp_url_assigment);
        
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        STR_urlphp = user.get(SessionManager.KEY_URL);
        Log.e("Debug", "Halaman ListDetailAWBOthers " +"ISI >>> " + "URL = " + STR_urlphp);
        
//        if (STR_urlphp.equals("http://43.252.144.14:81/android/")) {
//	    	Log.i("Debug", "Halaman ListDetailAWBOthers " +"Replace URL Lokal Server");
//	    	STR_URL_API = STR_urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/compact_mobile");
//		}
//	    else if (STR_urlphp.equals("http://api-mobile.atex.co.id/android/")) {
//	    	Log.i("Debug", "Halaman ListDetailAWBOthers " +"Replace URL Online Server");
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
//		Log.i("Debug", "Halaman ListDetailAWBOthers " +"Test String Replace URL " + STR_URL_API);
		
////		STR_url_awb_others_detail = STR_URL_API + "/" +"details_pickup_other"+"/"+ STR_kode_cust+"/"+STR_no_assigment;
////		STR_url_awb_others_detail = Str_sp_url_assigment + "/" +"details_pickup_other"+"/"+ STR_kode_cust+"/"+STR_no_assigment;
//        STR_url_awb_others_detail = STR_urlphp + Str_LinkDetailAWBOthers + STR_kode_cust+"/"+STR_no_assigment;
//		Log.d("Debug","Halaman ListDetailAWBOthers" +"Test URL " + STR_url_awb_others_detail);
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpGet request = new HttpGet(STR_url_awb_others_detail);
//        Log.d("Debug","Halaman ListDetailAWBOthers " +"Test URL Assigment " + request);
//        try {
//        	HttpResponse response = client.execute(request);
//			HttpEntity entity = response.getEntity();
//			STR_data = EntityUtils.toString(entity);
//			System.out.println(response.getStatusLine());
//			Log.e("Debug", STR_data);
//			daftar_list_awb_others_detail.clear();
//			HashMap<String, String> mapDetailAWBOthers;
//			try {
//				JSONObject jsonObject = new JSONObject(STR_data);
//				JSONArray ListObjDetailAWBOthers = jsonObject.getJSONArray("data");
//				RespnseMessage = jsonObject.getString("response_message");
//				RespnseCode = jsonObject.getString("response_code");
//				Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
//				for (int i = 0; i < ListObjDetailAWBOthers.length(); i++) {
//					JSONObject obj = ListObjDetailAWBOthers.getJSONObject(i);
//					STR_assigment = obj.getString("asigment");
//					STR_no_awb = obj.getString("no_awb");
//					STR_status = obj.getString("status");
//					STR_alamat = obj.getString("alamat");
//
//					mapDetailAWBOthers = new HashMap<String, String>();
//					mapDetailAWBOthers.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
//					mapDetailAWBOthers.put(AR_NO_AWB, obj.getString("no_awb"));
//					mapDetailAWBOthers.put(AR_STATUS, obj.getString("status"));
//					mapDetailAWBOthers.put(AR_ALAMAT, obj.getString("alamat"));
//					daftar_list_awb_others_detail.add(mapDetailAWBOthers);
//					Log.d("Debug", "Hashmap AWB Others " + daftar_list_awb_others_detail);
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
//        this.adapter_listview_awb_others_detail();

		//Update 02-10-2018
		JSONParseListDetailAWBOthers doItInBackGround = new JSONParseListDetailAWBOthers();
		doItInBackGround.execute();
	}

	//Update 02-10-2018
	private class JSONParseListDetailAWBOthers extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("Debug", "Lewat PreExecute");
            fl_progress.setVisibility(View.VISIBLE);

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			STR_url_awb_others_detail = STR_urlphp + Str_LinkDetailAWBOthers + STR_kode_cust+"/"+STR_no_assigment;
			Log.d("Debug","Halaman ListDetailAWBOthers" +"Test URL " + STR_url_awb_others_detail);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(STR_url_awb_others_detail);
			Log.d("Debug","Halaman ListDetailAWBOthers " +"Test URL Assigment " + request);

			try {
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_list_awb_others_detail.clear();
                daftar_validation_waybill_list.clear();
				HashMap<String, String> mapDetailAWBOthers;
                HashMap<String, String> mapValidationWaybill;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListObjDetailAWBOthers = jsonObject.getJSONArray("data");
					RespnseMessage = jsonObject.getString("response_message");
					RespnseCode = jsonObject.getString("response_code");
					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
					for (int i = 0; i < ListObjDetailAWBOthers.length(); i++) {
						JSONObject obj = ListObjDetailAWBOthers.getJSONObject(i);
						STR_assigment = obj.getString("asigment");
						STR_no_awb = obj.getString("no_awb");
						STR_status = obj.getString("status");
						STR_alamat = obj.getString("alamat");

						mapDetailAWBOthers = new HashMap<String, String>();
						mapDetailAWBOthers.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
						mapDetailAWBOthers.put(AR_NO_AWB, obj.getString("no_awb"));
						mapDetailAWBOthers.put(AR_STATUS, obj.getString("status"));
						mapDetailAWBOthers.put(AR_ALAMAT, obj.getString("alamat"));
						daftar_list_awb_others_detail.add(mapDetailAWBOthers);
						Log.d("Debug", "Hashmap AWB Others " + daftar_list_awb_others_detail);

                        mapValidationWaybill = new HashMap<String, String>();
                        mapValidationWaybill.put(AR_NO_AWB, obj.getString("no_awb"));
                        daftar_validation_waybill_list.add(mapValidationWaybill);
                        Log.d("Debug", "Hashmap Validation Waybill " + daftar_validation_waybill_list);
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
			adapter_listview_awb_others_detail();
			Log.d("Debug", "1.Test Sampai Sini ");
            fl_progress.setVisibility(View.GONE);
		}
	}
	
	public void adapter_listview_awb_others_detail() {
		Log.e("Debug", "Listview AWB Others Detail");
		ListDetailAWBOthersAdapter adapterDetailAWBOthers = new ListDetailAWBOthersAdapter(this,daftar_list_awb_others_detail);
		ListView listViewDetailAWBOthers = (ListView) findViewById(compact.mobile.R.id.listviews_ads_detail);
		listViewDetailAWBOthers.setAdapter(adapterDetailAWBOthers);
		adapterDetailAWBOthers.notifyDataSetChanged();
		
		listViewDetailAWBOthers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            	Toast.makeText(getApplicationContext(),daftar_list_awb_others_detail.get(position).get(AR_NO_AWB).toString()+ " Selected", Toast.LENGTH_LONG).show();
                @SuppressWarnings("unchecked")
                HashMap<String, String> o = (HashMap<String, String>) daftar_list_awb_others_detail.get(position);
//                waybill = o.get("Waybill");
                show_dialog_detail();
//               String status = o.get("status");
////                Log.d("tes waybill", waybill);
//                if(status.equals("0")){
////                	show_dialog1();
////                	show_dialog2();
//                	show_dialog_detail();
//                }else if (status.equals("1")){
////                	show_dialog2();
//                	show_dialog_detail();
//                }
////			     Log.d("error 2", "manifes = " +waybill);
            }
        });
		
	}
	
	public void OnNextScan(View view, int position) {
		Log.d("Debug", "Next Scan");
		HashMap<String, String> o = (HashMap<String, String>) daftar_list_awb_others_detail.get(position);
		String status = o.get("status");
		// Log.d("tes waybill", waybill);
		if (status.equals("0")) {
			show_dialog1();
		} else if (status.equals("1")) {
			show_dialog2();
		}
	}
	
	public void show_dialog1() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailAWBOthers.this);
		builder.setTitle("Action ");
		builder.setItems(new CharSequence[] { "Pickup", "Batal" },

		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {
				case 0:
					Intent a = new Intent(ListDetailAWBOthers.this, multi_pup.class);
					a.putExtra("asigment", STR_assigment);
					a.putExtra("po", AR_NO_AWB);
					startActivityForResult(a, 1);
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

	}
	
	public void show_dialog_detail() {
		String STR_AlamatPickup = "Alamat Pickup";
		String STR_alamat_result = STR_alamat;
		String STR_alamat_replace = STR_alamat_result.replace("<br>"," ");
		Log.d("Debug", "Replace <br> --> " + STR_alamat_replace);
		AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailAWBOthers.this);
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
	
}
