package compact.mobile.SuratJalan;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import compact.mobile.DBAdapter;
import compact.mobile.JSONParser;
import compact.mobile.SessionManager;
import compact.mobile.SuratJalan.DB.C_DEX_List;
import compact.mobile.SuratJalan.DB.C_POD_List;
import compact.mobile.SuratJalan.DB.ListDEXChooseDBAdapter;
import compact.mobile.SuratJalan.DB.ListPODChooseDBAdapter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.TextView;
import android.widget.Toast;
import compact.mobile.R;
import compact.mobile.SuratJalan.Scan.multiScanDEX;
import compact.mobile.SuratJalan.Scan.multiScanPOD;
import compact.mobile.SuratJalan.Scan.testingScan.barcodeScanner;
import compact.mobile.SuratJalan.Scan.testingScan.testScan;
import compact.mobile.SuratJalan.helper.ListPOD_DEXAdapter;
import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;

public class ListPOD_DEX extends Activity {
	
	Button BTN_keluar, BTN_pod, BTN_dex, BTN_refresh;
	TextView TV_nominal_cod_replace, TV_nominal_cod_replace_final;
	
	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_NO_AWB = "no_awb";
	private static final String AR_STATUS = "status";
	private static final String AR_ALAMAT = "alamat";
	private static final String AR_NILAI_COD = "nilai_cod";
	private static final String AR_SERVICE = "service";
	private static final String AR_NILAI_COD_REPLACE = "nilai_cod_replace";

	private static final String AR_DEX_LIST_KODE = "mrem_kode";
	private static final String AR_DEX_LIST_KETERANGAN = "mrem_keterangan";

	private static final String AR_POD_LIST_KODE = "mrem_kode";
	private static final String AR_POD_LIST_KETERANGAN = "mrem_keterangan";

	private static final String AR_VALIDATION_CODE = "validation_kode";
	private static final String AR_VALIDATION_STATUS = "validation_status";
    private static final String AR_KETERANGAN_NTC= "keterangan_ntc";

	String STR_urlphp, STR_URL_API, STR_url_pod_dex, STR_url_dexlist, STR_url_podlist;
	String Str_sp_url_assigment;
	String STR_assigment, STR_no_awb, STR_status, STR_service, STR_alamat, STR_alamat_DialogMessage;
	String STR_no_assigment, STR_spref_assigment;
	String STR_validation_code, STR_validation_status, STR_spref_validation_code, STR_arraylist_validation_code, STR_arraylist_spref_validation_code, STR_keterangan_ntc;
	String STR_nilai_cod, STR_nilai_cod_replace;
	String STR_DexList_Kode, STR_DexList_Keterangan;
	String STR_PodList_Kode, STR_PodList_Keterangan;
	String STR_data;
	String RespnseMessage, RespnseCode;
	public String Str_lo_Koneksi, Str_LinkListPODDEX, Str_DEXList, Str_PODList;
	SessionManager session;
    FrameLayout fl_progress;
	
	SharedPreferences myPrefs;
	SharedPreferences.Editor spEditor;
    SharedPreferences sp;

	ListDEXChooseDBAdapter dbDEXList;
	ListPODChooseDBAdapter dbPODList;
	
	final ArrayList<HashMap<String, String>> daftar_list_pod_dex = new ArrayList<HashMap<String, String>>();
	final ArrayList<HashMap<String, String>> daftar_dex_list = new ArrayList<HashMap<String, String>>();
	final ArrayList<HashMap<String, String>> daftar_pod_list = new ArrayList<HashMap<String, String>>();
	final ArrayList<HashMap<String, String>> daftar_validation_code_list = new ArrayList<HashMap<String, String>>();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pod_dex);
        
        Koneksi Str_lo_Koneksi = new Koneksi();
        Str_LinkListPODDEX = Str_lo_Koneksi.ConnPODDEX();
		Str_DEXList = Str_lo_Koneksi.ConnDEXList();
		Str_PODList = Str_lo_Koneksi.ConnPODList();
        
        myPrefs = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE); 
//		this.context = context;
		spEditor = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE).edit();
        sp = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE);
        spEditor = myPrefs.edit();

        BTN_pod=(Button)findViewById(R.id.btnPod);
        BTN_dex=(Button)findViewById(R.id.btnDex);
        BTN_refresh=(Button)findViewById(R.id.btnRefresh);
        BTN_keluar=(Button)findViewById(R.id.btnKeluar);
        fl_progress = (FrameLayout) findViewById(R.id.fl_progress);
        
        BTN_refresh.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				finish();
//				Intent a = new Intent(ListPOD_DEX.this, ListPOD_DEX.class);
//				startActivity(a);
				JSONParseListPODDEX doItInBackGround = new JSONParseListPODDEX();
				doItInBackGround.execute();
			}
		});
        
        BTN_keluar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
        
        BTN_pod.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				finish();
//				Intent a = new Intent(ListPOD_DEX.this, cekpod.class);
				Intent a = new Intent(ListPOD_DEX.this, multiScanPOD.class);
//				Intent a = new Intent(ListPOD_DEX.this, barcodeScanner.class);
//				Intent a = new Intent(ListPOD_DEX.this, testScan.class);
				a.putExtra("asigment", STR_spref_assigment);
				a.putExtra("daftar_validation_code_list", daftar_validation_code_list);
				startActivity(a);

			}
		});
        
        BTN_dex.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				finish();
				Log.d("Debug","Button DEX | DEX List " +" = " + daftar_dex_list);
//				Intent a = new Intent(ListPOD_DEX.this, cekdex.class);
				Intent a = new Intent(ListPOD_DEX.this, multiScanDEX.class);
//				a.putExtra("asigment", STR_no_assigment);
				a.putExtra("asigment", STR_spref_assigment);
				a.putExtra("dex_list", daftar_dex_list);
				a.putExtra("daftar_validation_code_list", daftar_validation_code_list);
				startActivity(a);

			}
		});
        
        Intent in = getIntent();
     	////No Assigment
        STR_no_assigment = in.getStringExtra("AR_NO_ASSIGMENT");
        Log.d("Debug", "Halaman ListPOD_DEX " +"Intent From OtherAssigment >>> " + "Nomor Asigment " + STR_no_assigment);
        
        STR_spref_assigment = (myPrefs.getString("sp_spref_assigment_all", ""));
        Log.d("Debug", "Halaman ListPOD_DEX Sharedpref >>> " + "Nomor Asigment " + STR_spref_assigment);
        
        Str_sp_url_assigment = (myPrefs.getString("sp_url_assigment", ""));
        Log.d("Debug", "URL Shared Preferences"+" || URL Assigment = " + Str_sp_url_assigment);
        
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        STR_urlphp = user.get(SessionManager.KEY_URL);
        Log.e("Debug", "Halaman ListPOD_DEX " +"ISI >>> " + "URL = " + STR_urlphp);
        
//        if (STR_urlphp.equals("http://43.252.144.14:81/android/")) {
//	    	Log.i("Debug", "Halaman ListADS " +"Replace URL Lokal Server");
//	    	STR_URL_API = STR_urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/compact_mobile");
//		}
//	    else if (STR_urlphp.equals("http://api-mobile.atex.co.id/android/")) {
//	    	Log.i("Debug", "Halaman ListADS " +"Replace URL Online Server");
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
//		Log.i("Debug", "Halaman List POD/DEX " +"Test String Replace URL " + STR_URL_API);

		Log.d("Debug","Lewat Cek Spinner Data");
		// database handler
		DBAdapter dbListDEX = new DBAdapter(getApplicationContext());
		List<String> lablesDEX = dbListDEX.getAllLabelsDEX();
		Log.i("Debug", "Cek Array List DEX " + lablesDEX);

		boolean retval_dex = lablesDEX.isEmpty();
		if (retval_dex == true){
			Log.i("Debug", "Array List DEX Kosong");

//		//		Di Matikan Sementara Per-Tanggal 02/07/2018
//		STR_url_dexlist = STR_urlphp + Str_DEXList;
//		Log.d("Debug","DEX List " +"Test URL " + STR_url_dexlist);
//		DefaultHttpClient clientDEXList = new DefaultHttpClient();
//		HttpGet requestDEXList = new HttpGet(STR_url_dexlist);
//		Log.d("Debug","Halaman List POD/DEX  " +" | DEX List " + requestDEXList);
//		try {
//			HttpResponse response = clientDEXList.execute(requestDEXList);
//			HttpEntity entity = response.getEntity();
//			STR_data = EntityUtils.toString(entity);
//			System.out.println(response.getStatusLine());
//			Log.e("Debug", STR_data);
//			daftar_dex_list.clear();
//			HashMap<String, String> mapDexList;
//			try {
//				JSONObject jsonObject = new JSONObject(STR_data);
//				JSONArray ListObjDex = jsonObject.getJSONArray("data");
//				RespnseMessage = jsonObject.getString("response_message");
//				RespnseCode = jsonObject.getString("response_code");
//				Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
//				for (int i = 0; i < ListObjDex.length(); i++) {
//					JSONObject obj = ListObjDex.getJSONObject(i);
//					STR_DexList_Kode = obj.getString("mrem_kode");
//					STR_DexList_Keterangan = obj.getString("mrem_keterangan");
//
//					mapDexList = new HashMap<String, String>();
//					mapDexList.put(AR_DEX_LIST_KODE, obj.getString("mrem_kode"));
//					mapDexList.put(AR_DEX_LIST_KETERANGAN, obj.getString("mrem_keterangan"));
//					daftar_dex_list.add(mapDexList);
//					Log.d("Debug", "Hashmap DEX List " + daftar_dex_list);
//
//					C_DEX_List dex_ListChoose = new C_DEX_List();
//					String Str_DexKodeList = STR_DexList_Kode;
//					String Str_DexKeteranganList = STR_DexList_Keterangan;
//					Log.d("Debug", "Cek Save Data DEX List"
//							+ " DEX Kode = " + Str_DexKodeList
//							+ " DEX Keterangan = " + Str_DexKeteranganList);
//
//					dex_ListChoose.setDEX_Kode(Str_DexKodeList);
//					dex_ListChoose.setDEX_Keterangan(Str_DexKeteranganList);
//					Log.d("Debug","add DEX List -> local" );
//					dbDEXList = new ListDEXChooseDBAdapter(ListPOD_DEX.this);
//					dbDEXList.open();
//					dbDEXList.createContact(dex_ListChoose);
//					dbDEXList.close();
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
//
//		Log.d("Debug","DEX List " +" = " + daftar_dex_list);

			//Update 02-10-2018
			JSONParseListDEXTrue doItInBackGround = new JSONParseListDEXTrue();
			doItInBackGround.execute();

		}
		else {
//			Log.i("Debug", "Array List Isi DEX");
			Log.i("Debug", "Array List UPDATE Isi DEX");

//			//		Di Matikan Sementara Per-Tanggal 02/07/2018
//			STR_url_dexlist = STR_urlphp + Str_DEXList;
//			Log.d("Debug","DEX List " +"Test URL " + STR_url_dexlist);
//			DefaultHttpClient clientDEXList = new DefaultHttpClient();
//			HttpGet requestDEXList = new HttpGet(STR_url_dexlist);
//			Log.d("Debug","Halaman List POD/DEX  " +" | UPDATE DEX List " + requestDEXList);
//			try {
//				HttpResponse response = clientDEXList.execute(requestDEXList);
//				HttpEntity entity = response.getEntity();
//				STR_data = EntityUtils.toString(entity);
//				System.out.println(response.getStatusLine());
//				Log.e("Debug", STR_data);
//				daftar_dex_list.clear();
//				HashMap<String, String> mapDexList;
//				try {
//					JSONObject jsonObject = new JSONObject(STR_data);
//					JSONArray ListObjDex = jsonObject.getJSONArray("data");
//					RespnseMessage = jsonObject.getString("response_message");
//					RespnseCode = jsonObject.getString("response_code");
//					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
//					for (int i = 0; i < ListObjDex.length(); i++) {
//						JSONObject obj = ListObjDex.getJSONObject(i);
//						STR_DexList_Kode = obj.getString("mrem_kode");
//						STR_DexList_Keterangan = obj.getString("mrem_keterangan");
//
//						mapDexList = new HashMap<String, String>();
//						mapDexList.put(AR_DEX_LIST_KODE, obj.getString("mrem_kode"));
//						mapDexList.put(AR_DEX_LIST_KETERANGAN, obj.getString("mrem_keterangan"));
//						daftar_dex_list.add(mapDexList);
//						Log.d("Debug", "Hashmap UPDATE DEX List " + daftar_dex_list);
//
//						C_DEX_List dex_ListChoose = new C_DEX_List();
//						String Str_DexKodeList = STR_DexList_Kode;
//						String Str_DexKeteranganList = STR_DexList_Keterangan;
//						Log.d("Debug", "Cek UPDATE Save Data DEX List" + " DEX Kode = " + Str_DexKodeList + " DEX Keterangan = " + Str_DexKeteranganList);
//
//						dex_ListChoose.setDEX_Kode(Str_DexKodeList);
//						dex_ListChoose.setDEX_Keterangan(Str_DexKeteranganList);
//						Log.d("Debug","UPDATE DEX List -> local" );
//						dbDEXList = new ListDEXChooseDBAdapter(ListPOD_DEX.this);
//						dbDEXList.open();
//						dbDEXList.updateContact(dex_ListChoose);
//						dbDEXList.close();
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					Log.d("Debug","Trace = ERROR ");
//				}
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			Log.d("Debug","UPDATE DEX List " +" = " + daftar_dex_list);

			//Update 02-10-2018
			JSONParseListDEXFalse doItInBackGround = new JSONParseListDEXFalse();
			doItInBackGround.execute();
		}

		DBAdapter dbListPOD = new DBAdapter(getApplicationContext());
		List<String> lablesPOD = dbListPOD.getAllLabelsPOD();
		Log.i("Debug", "Cek Array List POD " + lablesPOD);

		boolean retval_pod = lablesPOD.isEmpty();
		if (retval_pod == true){
			Log.i("Debug", "Array List POD Kosong");

//			//		Di Matikan Sementara Per-Tanggal 02/07/2018
//			STR_url_podlist = STR_urlphp + Str_PODList;
//			Log.d("Debug","POD List " +"Test URL " + STR_url_podlist);
//			DefaultHttpClient clientPODList = new DefaultHttpClient();
//			HttpGet requestPODList = new HttpGet(STR_url_podlist);
//			Log.d("Debug","Halaman List POD/DEX  " +" | POD List " + requestPODList);
//			try {
//				HttpResponse response = clientPODList.execute(requestPODList);
//				HttpEntity entity = response.getEntity();
//				STR_data = EntityUtils.toString(entity);
//				System.out.println(response.getStatusLine());
//				Log.e("Debug", STR_data);
//				daftar_pod_list.clear();
//				HashMap<String, String> mapPodList;
//				try {
//					JSONObject jsonObject = new JSONObject(STR_data);
//					JSONArray ListObjPod = jsonObject.getJSONArray("data");
//					RespnseMessage = jsonObject.getString("response_message");
//					RespnseCode = jsonObject.getString("response_code");
//					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
//					for (int i = 0; i < ListObjPod.length(); i++) {
//						JSONObject obj = ListObjPod.getJSONObject(i);
//						STR_PodList_Kode = obj.getString("mrem_kode");
//						STR_PodList_Keterangan = obj.getString("mrem_keterangan");
//
//						mapPodList = new HashMap<String, String>();
//						mapPodList.put(AR_POD_LIST_KODE, obj.getString("mrem_kode"));
//						mapPodList.put(AR_POD_LIST_KETERANGAN, obj.getString("mrem_keterangan"));
//						daftar_pod_list.add(mapPodList);
//						Log.d("Debug", "Hashmap POD List " + daftar_pod_list);
//
//						C_POD_List pod_ListChoose = new C_POD_List();
//						String Str_PodKodeList = STR_PodList_Kode;
//						String Str_PodKeteranganList = STR_PodList_Keterangan;
//						Log.d("Debug", "Cek Save Data POD List"
//								+ " POD Kode = " + Str_PodKodeList
//								+ " POD Keterangan = " + Str_PodKeteranganList);
//
//						pod_ListChoose.setPOD_Kode(Str_PodKodeList);
//						pod_ListChoose.setPOD_Keterangan(Str_PodKeteranganList);
//						Log.d("Debug","add POD List -> local" );
//						dbPODList = new ListPODChooseDBAdapter(ListPOD_DEX.this);
//						dbPODList.open();
//						dbPODList.createContact(pod_ListChoose);
//						dbPODList.close();
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					Log.d("Debug","Trace = ERROR ");
//				}
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			Log.d("Debug","POD List " +" = " + daftar_pod_list);

			//Update 02-10-2018
			JSONParseListPODTrue doItInBackGround = new JSONParseListPODTrue();
			doItInBackGround.execute();

		}
		else {
//			Log.i("Debug", "Array List Isi POD");
			Log.i("Debug", "Array List UPDATE Isi POD");

//			STR_url_podlist = STR_urlphp + Str_PODList;
//			Log.d("Debug","POD List UPDATE " +"Test URL " + STR_url_podlist);
//			DefaultHttpClient clientPODList = new DefaultHttpClient();
//			HttpGet requestPODList = new HttpGet(STR_url_podlist);
//			Log.d("Debug","Halaman List POD/DEX  " +" | UPDATE POD List " + requestPODList);
//			try {
//				HttpResponse response = clientPODList.execute(requestPODList);
//				HttpEntity entity = response.getEntity();
//				STR_data = EntityUtils.toString(entity);
//				System.out.println(response.getStatusLine());
//				Log.e("Debug", STR_data);
//				daftar_pod_list.clear();
//				HashMap<String, String> mapPodList;
//				try {
//					JSONObject jsonObject = new JSONObject(STR_data);
//					JSONArray ListObjPod = jsonObject.getJSONArray("data");
//					RespnseMessage = jsonObject.getString("response_message");
//					RespnseCode = jsonObject.getString("response_code");
//					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
//					for (int i = 0; i < ListObjPod.length(); i++) {
//						JSONObject obj = ListObjPod.getJSONObject(i);
//						STR_PodList_Kode = obj.getString("mrem_kode");
//						STR_PodList_Keterangan = obj.getString("mrem_keterangan");
//
//						mapPodList = new HashMap<String, String>();
//						mapPodList.put(AR_POD_LIST_KODE, obj.getString("mrem_kode"));
//						mapPodList.put(AR_POD_LIST_KETERANGAN, obj.getString("mrem_keterangan"));
//						daftar_pod_list.add(mapPodList);
//						Log.d("Debug", "Hashmap UPDATE POD List " + daftar_pod_list);
//
//						C_POD_List pod_ListChoose = new C_POD_List();
//						String Str_PodKodeList = STR_PodList_Kode;
//						String Str_PodKeteranganList = STR_PodList_Keterangan;
//						Log.d("Debug", "Cek UPDATE Save Data POD List" + " POD Kode = " + Str_PodKodeList + " POD Keterangan = " + Str_PodKeteranganList);
//
//						pod_ListChoose.setPOD_Kode(Str_PodKodeList);
//						pod_ListChoose.setPOD_Keterangan(Str_PodKeteranganList);
//						Log.d("Debug","UPDATE POD List -> local" );
//						dbPODList = new ListPODChooseDBAdapter(ListPOD_DEX.this);
//						dbPODList.open();
//						dbPODList.updateContact(pod_ListChoose);
//						dbPODList.close();
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					Log.d("Debug","Trace = ERROR ");
//				}
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			Log.d("Debug","UPDATE POD List " +" = " + daftar_pod_list);

			//Update 02-10-2018
			JSONParseListPODFalse doItInBackGround = new JSONParseListPODFalse();
			doItInBackGround.execute();
		}
		
////		STR_url_pod_dex = STR_URL_API + "/" +"poddex"+"/"+ STR_no_assigment;
////		STR_url_pod_dex = Str_sp_url_assigment + "/" +"poddex"+"/"+ STR_no_assigment;
////        STR_url_pod_dex = STR_urlphp + Str_LinkListPODDEX + STR_no_assigment;
//        STR_url_pod_dex = STR_urlphp + Str_LinkListPODDEX + STR_spref_assigment;
//		Log.d("Debug","Halaman List POD/DEX " +"Test Isi Assigment " + STR_spref_assigment);
//		Log.d("Debug","Halaman List POD/DEX " +"Test URL " + STR_url_pod_dex);
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpGet request = new HttpGet(STR_url_pod_dex);
//        Log.d("Debug","Halaman List POD/DEX  " +"Test URL Assigment " + request);
//        try {
//        	HttpResponse response = client.execute(request);
//			HttpEntity entity = response.getEntity();
//			STR_data = EntityUtils.toString(entity);
//			System.out.println(response.getStatusLine());
//			Log.e("Debug", STR_data);
//			daftar_list_pod_dex.clear();
//			HashMap<String, String> mapPodDex;
//			try {
//				JSONObject jsonObject = new JSONObject(STR_data);
//				JSONArray ListObjPodDex = jsonObject.getJSONArray("data");
//				RespnseMessage = jsonObject.getString("response_message");
//				RespnseCode = jsonObject.getString("response_code");
//				Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
//				for (int i = 0; i < ListObjPodDex.length(); i++) {
//					JSONObject obj = ListObjPodDex.getJSONObject(i);
//					STR_assigment = obj.getString("asigment");
//					STR_no_awb = obj.getString("no_awb");
//					STR_status = obj.getString("status");
//					STR_alamat = obj.getString("alamat");
//					STR_nilai_cod = obj.getString("nilai_cod");
//					STR_service = obj.getString("service");
//
//					//convert mata uang - cod
//					DecimalFormat formatter = new DecimalFormat("#,##0.00");
//					TV_nominal_cod_replace = (TextView) findViewById(R.id.nominal_cod_pod_dex_replace);
//					TV_nominal_cod_replace.setText(STR_nilai_cod);
//					String strCOD = TV_nominal_cod_replace.getText().toString().replaceAll("[Rp,.]", "");
//					Locale localsetor = new Locale("id", "id");
//					String replaceablesetor = String.format("[Rp,.\\s]",NumberFormat.getCurrencyInstance().getCurrency().getSymbol(localsetor));
//					String strCODReplace = TV_nominal_cod_replace.getText().toString().replaceAll(replaceablesetor,"");
//					Log.d("Debug", "Replace Nilai COD " + strCODReplace);
//					double parsedsetor;
//					try {
//	    				parsedsetor = Double.parseDouble(strCOD);
//	    			} catch (NumberFormatException e) {
//	    				parsedsetor = 0.00;
//	    			}
//					parsedsetor = Double.parseDouble(strCODReplace);
//	                String formattedsetor = formatter.format((parsedsetor));
//	                String replacesetor = String.format("[Rp\\s]",NumberFormat.getCurrencyInstance().getCurrency().getSymbol(localsetor));
//	                String strCODReplace2 = formattedsetor.replaceAll(replacesetor, "");
//	                Log.d("Debug","Replace Nilai COD 2 " + strCODReplace2);
//	                TV_nominal_cod_replace_final = (TextView) findViewById(R.id.nominal_cod_pod_dex_replace_final);
//	                TV_nominal_cod_replace_final.setText(strCODReplace2);
//
//					mapPodDex = new HashMap<String, String>();
//					mapPodDex.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
//					mapPodDex.put(AR_NO_AWB, obj.getString("no_awb"));
//					mapPodDex.put(AR_STATUS, obj.getString("status"));
//					mapPodDex.put(AR_ALAMAT, obj.getString("alamat"));
//					mapPodDex.put(AR_NILAI_COD, obj.getString("nilai_cod"));
//					mapPodDex.put(AR_SERVICE, obj.getString("service"));
//					mapPodDex.put(AR_NILAI_COD_REPLACE, strCODReplace2);
//					daftar_list_pod_dex.add(mapPodDex);
//					Log.d("Debug", "Hashmap ADS " + daftar_list_pod_dex);
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
//	}
//        this.adapter_listview_pod_dex();

		//Update 02-10-2018
//		JSONParseListPODDEX doItInBackGround = new JSONParseListPODDEX();
//		doItInBackGround.execute();
	}

    @Override
    protected void onResume() {
        super.onResume();
        JSONParseListPODDEX doItInBackGround = new JSONParseListPODDEX();
        doItInBackGround.execute();
    }

	//Update 02-10-2018
	private class JSONParseListDEXTrue extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("Debug", "Lewat PreExecute");

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			STR_url_dexlist = STR_urlphp + Str_DEXList;
			Log.d("Debug","DEX List " +"Test URL " + STR_url_dexlist);
			DefaultHttpClient clientDEXList = new DefaultHttpClient();
			HttpGet requestDEXList = new HttpGet(STR_url_dexlist);
			Log.d("Debug","Halaman List POD/DEX  " +" | DEX List " + requestDEXList);

			try {
				HttpResponse response = clientDEXList.execute(requestDEXList);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_dex_list.clear();
				HashMap<String, String> mapDexList;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListObjDex = jsonObject.getJSONArray("data");
					RespnseMessage = jsonObject.getString("response_message");
					RespnseCode = jsonObject.getString("response_code");
					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
					for (int i = 0; i < ListObjDex.length(); i++) {
						JSONObject obj = ListObjDex.getJSONObject(i);
						STR_DexList_Kode = obj.getString("mrem_kode");
						STR_DexList_Keterangan = obj.getString("mrem_keterangan");

						mapDexList = new HashMap<String, String>();
						mapDexList.put(AR_DEX_LIST_KODE, obj.getString("mrem_kode"));
						mapDexList.put(AR_DEX_LIST_KETERANGAN, obj.getString("mrem_keterangan"));
						daftar_dex_list.add(mapDexList);
						Log.d("Debug", "Hashmap DEX List " + daftar_dex_list);

						C_DEX_List dex_ListChoose = new C_DEX_List();
						String Str_DexKodeList = STR_DexList_Kode;
						String Str_DexKeteranganList = STR_DexList_Keterangan;
						Log.d("Debug", "Cek Save Data DEX List"
								+ " DEX Kode = " + Str_DexKodeList
								+ " DEX Keterangan = " + Str_DexKeteranganList);

						dex_ListChoose.setDEX_Kode(Str_DexKodeList);
						dex_ListChoose.setDEX_Keterangan(Str_DexKeteranganList);
						Log.d("Debug","add DEX List -> local" );
						dbDEXList = new ListDEXChooseDBAdapter(ListPOD_DEX.this);
						dbDEXList.open();
						dbDEXList.createContact(dex_ListChoose);
						dbDEXList.close();
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
			Log.d("Debug","DEX List " +" = " + daftar_dex_list);
		}
	}

	//Update 02-10-2018
	private class JSONParseListDEXFalse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("Debug", "Lewat PreExecute");

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			STR_url_dexlist = STR_urlphp + Str_DEXList;
			Log.d("Debug","DEX List " +"Test URL " + STR_url_dexlist);
			DefaultHttpClient clientDEXList = new DefaultHttpClient();
			HttpGet requestDEXList = new HttpGet(STR_url_dexlist);
			Log.d("Debug","Halaman List POD/DEX  " +" | UPDATE DEX List " + requestDEXList);

			try {
				HttpResponse response = clientDEXList.execute(requestDEXList);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_dex_list.clear();
				HashMap<String, String> mapDexList;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListObjDex = jsonObject.getJSONArray("data");
					RespnseMessage = jsonObject.getString("response_message");
					RespnseCode = jsonObject.getString("response_code");
					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
					for (int i = 0; i < ListObjDex.length(); i++) {
						JSONObject obj = ListObjDex.getJSONObject(i);
						STR_DexList_Kode = obj.getString("mrem_kode");
						STR_DexList_Keterangan = obj.getString("mrem_keterangan");

						mapDexList = new HashMap<String, String>();
						mapDexList.put(AR_DEX_LIST_KODE, obj.getString("mrem_kode"));
						mapDexList.put(AR_DEX_LIST_KETERANGAN, obj.getString("mrem_keterangan"));
						daftar_dex_list.add(mapDexList);
						Log.d("Debug", "Hashmap UPDATE DEX List " + daftar_dex_list);

						C_DEX_List dex_ListChoose = new C_DEX_List();
						String Str_DexKodeList = STR_DexList_Kode;
						String Str_DexKeteranganList = STR_DexList_Keterangan;
						Log.d("Debug", "Cek UPDATE Save Data DEX List" + " DEX Kode = " + Str_DexKodeList + " DEX Keterangan = " + Str_DexKeteranganList);

						dex_ListChoose.setDEX_Kode(Str_DexKodeList);
						dex_ListChoose.setDEX_Keterangan(Str_DexKeteranganList);
						Log.d("Debug","UPDATE DEX List -> local" );
						dbDEXList = new ListDEXChooseDBAdapter(ListPOD_DEX.this);
						dbDEXList.open();
						dbDEXList.updateContact(dex_ListChoose);
						dbDEXList.close();
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
			Log.d("Debug","UPDATE DEX List " +" = " + daftar_dex_list);
		}
	}

	//Update 02-10-2018
	private class JSONParseListPODTrue extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("Debug", "Lewat PreExecute");

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			STR_url_podlist = STR_urlphp + Str_PODList;
			Log.d("Debug","POD List " +"Test URL " + STR_url_podlist);
			DefaultHttpClient clientPODList = new DefaultHttpClient();
			HttpGet requestPODList = new HttpGet(STR_url_podlist);
			Log.d("Debug","Halaman List POD/DEX  " +" | POD List " + requestPODList);

			try {
				HttpResponse response = clientPODList.execute(requestPODList);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_pod_list.clear();
				HashMap<String, String> mapPodList;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListObjPod = jsonObject.getJSONArray("data");
					RespnseMessage = jsonObject.getString("response_message");
					RespnseCode = jsonObject.getString("response_code");
					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
					for (int i = 0; i < ListObjPod.length(); i++) {
						JSONObject obj = ListObjPod.getJSONObject(i);
						STR_PodList_Kode = obj.getString("mrem_kode");
						STR_PodList_Keterangan = obj.getString("mrem_keterangan");

						mapPodList = new HashMap<String, String>();
						mapPodList.put(AR_POD_LIST_KODE, obj.getString("mrem_kode"));
						mapPodList.put(AR_POD_LIST_KETERANGAN, obj.getString("mrem_keterangan"));
						daftar_pod_list.add(mapPodList);
						Log.d("Debug", "Hashmap POD List " + daftar_pod_list);

						C_POD_List pod_ListChoose = new C_POD_List();
						String Str_PodKodeList = STR_PodList_Kode;
						String Str_PodKeteranganList = STR_PodList_Keterangan;
						Log.d("Debug", "Cek Save Data POD List"
								+ " POD Kode = " + Str_PodKodeList
								+ " POD Keterangan = " + Str_PodKeteranganList);

						pod_ListChoose.setPOD_Kode(Str_PodKodeList);
						pod_ListChoose.setPOD_Keterangan(Str_PodKeteranganList);
						Log.d("Debug","add POD List -> local" );
						dbPODList = new ListPODChooseDBAdapter(ListPOD_DEX.this);
						dbPODList.open();
						dbPODList.createContact(pod_ListChoose);
						dbPODList.close();
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
			Log.d("Debug","POD List " +" = " + daftar_pod_list);
		}
	}

	//Update 02-10-2018
	private class JSONParseListPODFalse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("Debug", "Lewat PreExecute");

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			STR_url_podlist = STR_urlphp + Str_PODList;
			Log.d("Debug","POD List UPDATE " +"Test URL " + STR_url_podlist);
			DefaultHttpClient clientPODList = new DefaultHttpClient();
			HttpGet requestPODList = new HttpGet(STR_url_podlist);
			Log.d("Debug","Halaman List POD/DEX  " +" | UPDATE POD List " + requestPODList);

			try {
				HttpResponse response = clientPODList.execute(requestPODList);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_pod_list.clear();
				HashMap<String, String> mapPodList;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListObjPod = jsonObject.getJSONArray("data");
					RespnseMessage = jsonObject.getString("response_message");
					RespnseCode = jsonObject.getString("response_code");
					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
					for (int i = 0; i < ListObjPod.length(); i++) {
						JSONObject obj = ListObjPod.getJSONObject(i);
						STR_PodList_Kode = obj.getString("mrem_kode");
						STR_PodList_Keterangan = obj.getString("mrem_keterangan");

						mapPodList = new HashMap<String, String>();
						mapPodList.put(AR_POD_LIST_KODE, obj.getString("mrem_kode"));
						mapPodList.put(AR_POD_LIST_KETERANGAN, obj.getString("mrem_keterangan"));
						daftar_pod_list.add(mapPodList);
						Log.d("Debug", "Hashmap UPDATE POD List " + daftar_pod_list);

						C_POD_List pod_ListChoose = new C_POD_List();
						String Str_PodKodeList = STR_PodList_Kode;
						String Str_PodKeteranganList = STR_PodList_Keterangan;
						Log.d("Debug", "Cek UPDATE Save Data POD List" + " POD Kode = " + Str_PodKodeList + " POD Keterangan = " + Str_PodKeteranganList);

						pod_ListChoose.setPOD_Kode(Str_PodKodeList);
						pod_ListChoose.setPOD_Keterangan(Str_PodKeteranganList);
						Log.d("Debug","UPDATE POD List -> local" );
						dbPODList = new ListPODChooseDBAdapter(ListPOD_DEX.this);
						dbPODList.open();
						dbPODList.updateContact(pod_ListChoose);
						dbPODList.close();
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
			Log.d("Debug","UPDATE POD List " +" = " + daftar_pod_list);
		}
	}

	//Update 02-10-2018
	private class JSONParseListPODDEX extends AsyncTask<String, String, JSONObject> {
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
			STR_url_pod_dex = STR_urlphp + Str_LinkListPODDEX + STR_spref_assigment;
			Log.d("Debug","Halaman List POD/DEX " +"Test Isi Assigment " + STR_spref_assigment);
			Log.d("Debug","Halaman List POD/DEX " +"Test URL " + STR_url_pod_dex);

//			STR_url_pod_dex = STR_url_pod_dex.replace("http", "https");
//			STR_url_pod_dex = STR_url_pod_dex.replace("stg", "stgs");
//			STR_url_pod_dex = STR_url_pod_dex.replace(".api", "");

			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(STR_url_pod_dex);
			request.setHeader("Cache-Control", "no-cache");
			request.setHeader("Connection", "keep-alive");
			Log.d("Debug","Halaman List POD/DEX  " +"Test URL Assigment " + request);

			try {
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_list_pod_dex.clear();
                daftar_validation_code_list.clear();
				HashMap<String, String> mapPodDex;
				HashMap<String, String> mapValidationCode;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListObjPodDex = jsonObject.getJSONArray("data");
					RespnseMessage = jsonObject.getString("response_message");
					RespnseCode = jsonObject.getString("response_code");
					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
					for (int i = 0; i < ListObjPodDex.length(); i++) {
						JSONObject obj = ListObjPodDex.getJSONObject(i);
						STR_assigment = obj.getString("asigment");
						STR_no_awb = obj.getString("no_awb");
						STR_status = obj.getString("status");
						STR_alamat = obj.getString("alamat");
						STR_nilai_cod = obj.getString("nilai_cod");
						STR_service = obj.getString("service");
						STR_validation_code = obj.getString("validation_kode");
						STR_validation_status = obj.getString("validation_status");
                        STR_keterangan_ntc = obj.getString("keterangan_ntc");

						spEditor.putString(sharedpref.SP_VALIDATION_CODE, STR_validation_code);
						spEditor.commit();

						STR_spref_validation_code = (myPrefs.getString("sp_validation_code", ""));
						Log.d("Debug", "Sharedpref >>> " + "Validation Code " + STR_spref_validation_code);

						//convert mata uang - cod
						DecimalFormat formatter = new DecimalFormat("#,##0.00");
						TV_nominal_cod_replace = (TextView) findViewById(R.id.nominal_cod_pod_dex_replace);
						TV_nominal_cod_replace.setText(STR_nilai_cod);
						String strCOD = TV_nominal_cod_replace.getText().toString().replaceAll("[Rp,.]", "");
						Locale localsetor = new Locale("id", "id");
						String replaceablesetor = String.format("[Rp,.\\s]",NumberFormat.getCurrencyInstance().getCurrency().getSymbol(localsetor));
						String strCODReplace = TV_nominal_cod_replace.getText().toString().replaceAll(replaceablesetor,"");
						Log.d("Debug", "Replace Nilai COD " + strCODReplace);
						double parsedsetor;
						try {
							parsedsetor = Double.parseDouble(strCOD);
						} catch (NumberFormatException e) {
							parsedsetor = 0.00;
						}
						parsedsetor = Double.parseDouble(strCODReplace);
						String formattedsetor = formatter.format((parsedsetor));
						String replacesetor = String.format("[Rp\\s]",NumberFormat.getCurrencyInstance().getCurrency().getSymbol(localsetor));
						String strCODReplace2 = formattedsetor.replaceAll(replacesetor, "");
						Log.d("Debug","Replace Nilai COD 2 " + strCODReplace2);
						TV_nominal_cod_replace_final = (TextView) findViewById(R.id.nominal_cod_pod_dex_replace_final);
						TV_nominal_cod_replace_final.setText(strCODReplace2);

						mapPodDex = new HashMap<String, String>();
						mapPodDex.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
						mapPodDex.put(AR_NO_AWB, obj.getString("no_awb"));
						mapPodDex.put(AR_STATUS, obj.getString("status"));
						mapPodDex.put(AR_ALAMAT, obj.getString("alamat"));
						mapPodDex.put(AR_NILAI_COD, obj.getString("nilai_cod"));
						mapPodDex.put(AR_SERVICE, obj.getString("service"));
						mapPodDex.put(AR_NILAI_COD_REPLACE, strCODReplace2);
                        mapPodDex.put(AR_KETERANGAN_NTC, obj.getString("keterangan_ntc"));
//                        String strKeteranganNTC = "";
//                        if (STR_keterangan_ntc != null){
//                            strKeteranganNTC = "1";
//                        }else{
//                            strKeteranganNTC = "0";
//                        }
//                        mapPodDex.put(AR_KETERANGAN_NTC, strKeteranganNTC);
						daftar_list_pod_dex.add(mapPodDex);
						Log.d("Debug", "Hashmap POD " + daftar_list_pod_dex);

						mapValidationCode = new HashMap<String, String>();
						mapValidationCode.put(AR_NO_AWB, obj.getString("no_awb"));
						mapValidationCode.put(AR_VALIDATION_CODE, obj.getString("validation_kode"));
						mapValidationCode.put(AR_VALIDATION_STATUS, obj.getString("validation_status"));
						daftar_validation_code_list.add(mapValidationCode);
						Log.d("Debug", "Hashmap Validation Code " + daftar_validation_code_list);
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
			adapter_listview_pod_dex();
			Log.d("Debug", "1.Test Sampai Sini ");
            fl_progress.setVisibility(View.GONE);
		}
	}
	
	public void adapter_listview_pod_dex() {
		Log.e("Debug", "Listview POD / DEX");
		ListPOD_DEXAdapter adapterPOD_DEX = new ListPOD_DEXAdapter(this,daftar_list_pod_dex);
		ListView listViewPOD_DEX = (ListView) findViewById(R.id.listviews_pod_dex);
		listViewPOD_DEX.setAdapter(adapterPOD_DEX);
		adapterPOD_DEX.notifyDataSetChanged();
		
		listViewPOD_DEX.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),daftar_list_pod_dex.get(position).get(AR_NO_AWB).toString()+ " Selected", Toast.LENGTH_LONG).show();
//				Toast.makeText(getApplicationContext(),daftar_list_pod_dex.get(position).get(AR_ALAMAT).toString()+ " Selected", Toast.LENGTH_LONG).show();
				HashMap<String, String> o = (HashMap<String, String>) daftar_list_pod_dex.get(position);
				STR_alamat_DialogMessage = daftar_list_pod_dex.get(position).get(AR_ALAMAT).toString();
				show_dialog_detail(STR_alamat_DialogMessage);
			}
		});
	}

	public void dipencet(String info) {
//        Toast.makeText(getApplicationContext(),info, Toast.LENGTH_LONG).show();
        Log.e("Debug", "Show Dialog NTC = "+info);

        if (info == "null" && info.equals("null")){
            Log.e("Debug", "Show Dialog NTC null");
            Toast.makeText(getApplicationContext(),"Kosong", Toast.LENGTH_LONG).show();
        }else {
            Log.e("Debug", "Show Dialog NTC not null");
            Toast.makeText(getApplicationContext(),info, Toast.LENGTH_LONG).show();
            show_dialog_detail_NTC(info);
        }
    }
	
//	public void show_dialog_detail() {
//		String STR_AlamatPickup = "Alamat POD / DEX";
//		String STR_alamat_result = STR_alamat;
//		Log.d("Debug", "Alamat --> " + STR_alamat);
//		String STR_alamat_replace = STR_alamat_result.replace("<br>"," ");
//		Log.d("Debug", "Replace <br> --> " + STR_alamat_replace);
//		AlertDialog.Builder builder = new AlertDialog.Builder(ListPOD_DEX.this);
//		builder.setTitle(STR_AlamatPickup);
//		builder.setMessage(STR_alamat_replace);
//		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(),"Clicked Cancel!", Toast.LENGTH_SHORT).show();
//              return;
//            	}
//            });
//		builder.create().show();
//	}

	public void show_dialog_detail(String STR_alamat_DialogMessage) {
		String STR_AlamatPickup = "Alamat POD / DEX";
		String STR_alamat_result = STR_alamat_DialogMessage;
		Log.d("Debug", "Alamat --> " + STR_alamat_DialogMessage);
		String STR_alamat_replace = STR_alamat_result.replace("<br>"," ");
		Log.d("Debug", "Replace <br> --> " + STR_alamat_replace);
		AlertDialog.Builder builder = new AlertDialog.Builder(ListPOD_DEX.this);
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

    public void show_dialog_detail_NTC(String info) {
        String STR_InformasiNTC = "Informasi AWB";
//        String STR_NTC = STR_keterangan_ntc;
        String STR_NTC = info;
		Log.d("Debug", "NTC --> " + info);
        String STR_NTC_replace = STR_NTC.replace("<br>"," ");
        Log.d("Debug", "Replace <br> --> " + STR_NTC_replace);
        AlertDialog.Builder builder = new AlertDialog.Builder(ListPOD_DEX.this);
        builder.setTitle(STR_InformasiNTC);
        builder.setMessage(STR_NTC_replace);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Clicked Cancel!", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        builder.create().show();
    }

    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }
	
}
