package compact.mobile.setorcod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import compact.mobile.JSONParser;
import compact.mobile.Jx_ConnectionManager;
import compact.mobile.R;
import compact.mobile.SessionManager;
import compact.mobile.UserName;
import compact.mobile.urlgw;
import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;

public class ListAWBSetorCODAdd extends Activity {

	String locpk, username, urlphp;
	Button button1, button2, button3;
	TextView txt_jxlsc_tv;
	int total_nilai, total_awb, total_awb_row;
	JSONArray list_awb_cod = null;

	Jx_ConnectionManager jxcon = new Jx_ConnectionManager(this.getBaseContext());
	// if(jxcon.isNetOk(MainActivity.this.getApplicationContext())){
	
	ListAWBSetorCODAddAdapter listAWBSetorCODAdapter;
	
	private static final String AR_NO_AWB_SETOR_COD = "lowb_mswb_nomor";
	private static final String AR_TOTAL_AWB_SETOR_COD = "ttwb_cod";
	private static final String AR_TOTAL_AWB_SETOR_COD_CONVERT = "ttwb_cod_convert";
	
	final ArrayList<HashMap<String, String>> daftar_list_awb_setorCod = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, Object>> listAWBSetorCOD =new ArrayList<HashMap<String,Object>>();
	private ArrayAdapter<String> listAWBArrayAdapter;
	private ArrayAdapter<String> listNOMINALArrayAdapter;
	
	EditText ET_ListAWB, ET_ListNOMINAL, ET_TotalNOMINAL;
	TextView TV_TotalNominalConvert, TV_TotalNominalConvertFinal;
	ListView LV_ListAWB, LV_ListNOMINAL;
	
	String STR_urlphp, STR_url_setor_cod_BtnPlus, Str_lo_Koneksi, Str_LinkListAWBSetorCOD, Str_LinkInsertListAWBSetorCOD;
	String STR_username, STR_usernameSPref, STR_no_assigment, STR_no_assigmentSPref; 
	String STR_SetorCOD_Waybill, STR_SetorCOD_total_SetorWaybill;
	String STR_data;
	String STR_swaybill, STR_nominal, STR_total_nilai, STR_total_nilai_setor;
	String RespnseMessage, RespnseCode, STR_response_code;
	SessionManager session;
	
	SharedPreferences myPrefs;
    SharedPreferences.Editor spEditor;
    SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_awb_setor_cod_add);
		
		session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        STR_urlphp = user.get(SessionManager.KEY_URL);
        STR_username = user.get(SessionManager.KEY_USER);
        Log.d("Debug", "1. URL COD Button Plus >>> " + STR_urlphp);
        
        Koneksi Str_lo_Koneksi = new Koneksi();
        Str_LinkListAWBSetorCOD = Str_lo_Koneksi.ConnAwbCOD();
        Str_LinkInsertListAWBSetorCOD = Str_lo_Koneksi.ConnInsertCOD();
        
        myPrefs = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE); 
        spEditor = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE).edit();
        sp = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE);
        spEditor = myPrefs.edit();
        
        STR_usernameSPref = (myPrefs.getString("sp_username_setor_cod", ""));
        Log.d("Debug", "Halaman Add Setor COD " +"Intent From List Setor COD >>> " + "Username " + STR_usernameSPref);
//        STR_no_assigment = (myPrefs.getString("sp_assigment_setor_cod", ""));
        STR_no_assigment = (myPrefs.getString("sp_no_assignment", ""));
        Log.d("Debug", "Halaman Add Setor COD " +"Intent From List Setor COD >>> " + "Nomor Asigment " + STR_no_assigment);
        
        Intent in = getIntent();
        STR_no_assigment = in.getStringExtra("STR_no_assigment");
        Log.d("Debug", "Halaman ListAWBSetorCODADD " +"Intent From List Setor COD >>>" 
                + " Nomor Asigment " + STR_no_assigment);
        
        spEditor.putString(sharedpref.SP_ASSIGMENT_SETOR_SCOD, STR_no_assigment);
//        spEditor.putString(sharedpref.SP_ASSIGMENT_SETOR_SCOD, "sp_no_assignment");
        spEditor.commit();
        
//        STR_no_assigmentSPref = (myPrefs.getString("sp_assigment_setor_cod", ""));
        STR_no_assigmentSPref = (myPrefs.getString("sp_no_assignment", ""));
        Log.d("Debug", "Share Pref Halaman ADD AWB Setor COD >>> " + "Nomor Asigment " + STR_no_assigment);
        
        ET_ListAWB = (EditText) findViewById(R.id.add_ET_awb_list);
        ET_ListNOMINAL = (EditText) findViewById(R.id.add_ET_nominal_list);
		LV_ListAWB = (ListView) findViewById(R.id.add_LV_awb_list);
		LV_ListNOMINAL = (ListView) findViewById(R.id.add_LV_nominal_list);
		
//		ET_TotalNOMINAL = (EditText) findViewById(R.id.add_ET_total_nominal_cod);
//		TV_TotalNominalConvert = (TextView) findViewById(R.id.TV_total_nominal_cod_convert);
		
		ArrayList<String> wbList = new ArrayList<String>();
		listAWBArrayAdapter = new ArrayAdapter<String>(this, R.layout.simplerow_awb, wbList);
		
		ArrayList<String> nomList = new ArrayList<String>();
		listNOMINALArrayAdapter = new ArrayAdapter<String>(this, R.layout.simplerow_nominal, nomList);

		txt_jxlsc_tv = (TextView) findViewById(R.id.txt_jxlsc_tv);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button3.setEnabled(false);
		txt_jxlsc_tv.setText("Rp. \u00A0 0");
		total_nilai = 0;
		total_awb = 0;

		urlgw url = urlgw.getInstance();
//		urlphp = url.getData();
//		urlphp = STR_urlphp+"/android/";
		urlphp = STR_urlphp+Str_LinkListAWBSetorCOD;
		Log.d("Debug", "2. URL COD Button Plus >>> " + urlphp);
		UserName S1 = UserName.getInstance();
		username = S1.getData();

		if (jxcon.isNetOk(ListAWBSetorCODAdd.this.getApplicationContext())) {
			Toast.makeText(getBaseContext(), "Online Status", Toast.LENGTH_SHORT).show();
//			displayListView();
			displayListViewButtonPlus();
			NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
			String currency = format.format(total_nilai).replaceAll("[$]", "Rp. \u00A0 ");

			txt_jxlsc_tv = (TextView) findViewById(R.id.txt_jxlsc_tv);
//			txt_jxlsc_tv.setText(currency + "  ( " + Integer.toString(total_awb + 1) + " awb)");
			txt_jxlsc_tv.setText(currency + "  ( " + Integer.toString(total_awb + 0) + " awb)");
		} else {
			Toast.makeText(getBaseContext(), "Offline Status", Toast.LENGTH_LONG).show();
			new android.os.Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					finish();
//					Intent ax = new Intent(ListAWBSetorCODAdd.this, Setorcod.class);
					Intent ax = new Intent(ListAWBSetorCODAdd.this, ListSetorCOD.class);
					startActivity(ax);
				}
			}, 2000);
		}

		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				Intent ax = new Intent(ListAWBSetorCODAdd.this, ListSetorCOD.class);
				startActivity(ax);
			}
		});
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent a = new Intent(ListAWBSetorCODAdd.this, ListAWBSetorCODAdd.class);
				startActivity(a);
				finish();
			}
		});
		
		ListView listViewAWBSetorCOD = (ListView) findViewById(R.id.listView1);
		listAWBSetorCODAdapter = new ListAWBSetorCODAddAdapter(this,daftar_list_awb_setorCod);
		listViewAWBSetorCOD.setAdapter(listAWBSetorCODAdapter);
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				HashMap<Integer, Boolean> ListAWBSetorCOD =listAWBSetorCODAdapter.ListAWBSetorCOD;
				for (int i = 0; i < listAWBSetorCODAdapter.getCount(); i++) {
//					System.out.println(listAWBSetorCODAdapter.get(i));
					Log.d("Debug", "Array List AWB " + daftar_list_awb_setorCod.get(i));
					
					HashMap<String, Object> map=(HashMap<String, Object>) listAWBSetorCODAdapter.getItem(i);
					String STR_NoAWB=map.get("lowb_mswb_nomor").toString();
					String STR_NominalSetor=map.get("ttwb_cod").toString();
					
					Log.d("Debug", "List Setor COD"
							+" 1. Waybill COD " + STR_NoAWB
							+" 2. Nilai COD " + STR_NominalSetor);
					
//					number_nominal_setor = TextUtils.isEmpty(STR_NominalSetor) ? 0 : Integer.valueOf(STR_NominalSetor);
//					Log.e("Debug", "Convert Null Nominal "+ number_nominal_setor);
//					Log.e("Debug", "Total Count " + listAWBSetorCODAdapter.getCount());
//					
//					tmp_nominal = number_nominal_setor * listAWBSetorCODAdapter.getCount();
//					Log.e("Debug", "Total " + tmp_nominal);
					
//					total_nilai = total_nilai + Integer.parseInt(STR_NominalSetor);
//					Log.e("Debug", "Total Nilai COD " + total_nilai);
					
					ET_ListAWB.setText(STR_NoAWB);
				    String STR_GetTextAWBList = ET_ListAWB.getText().toString();
				    Log.d("Debug","AWB " + STR_GetTextAWBList);
				    
				    ET_ListNOMINAL.setText(STR_NominalSetor);
				    String STR_GetTextNOMINALList = ET_ListNOMINAL.getText().toString();
				    Log.d("Debug","NOMINAL " + STR_GetTextNOMINALList);
				    
					    if (ET_ListAWB.getText().toString().trim().length() == 0) {
			       			Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
	//	            		ET_ListAWB.requestFocus();
			       		}
					    
					    else if (ET_ListNOMINAL.getText().toString().trim().length() == 0) {
			       			Toast.makeText(v.getContext(), "Nominal tidak boleh kosong.", Toast.LENGTH_SHORT).show();
	//	            		ET_ListNOMINAL.requestFocus();
			       		}
		            	else {
		            		add_list();
		            		Log.d("Debug","Lewat Button Finish");
		            	}
				}
				Toast.makeText(getApplicationContext(), "Data Sedang DI Proses", Toast.LENGTH_LONG).show();
                Log.w("DEBUG","!!! WAYBILL LAGI ");
				getListViewAWBSetorCOD();

				if (jxcon.isNetOk(ListAWBSetorCODAdd.this.getApplicationContext())) {
//					getListView();
					getListViewAWBSetorCOD();
					new android.os.Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							finish();
//							Intent ax = new Intent(ListAWBSetorCODAdd.this, Setorcod.class);
							Intent ax = new Intent(ListAWBSetorCODAdd.this, ListSetorCOD.class);
							startActivity(ax);
						}
					}, 3000);
				} else {
					Toast.makeText(getBaseContext(), "Offline Status", Toast.LENGTH_LONG).show();
					new android.os.Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							finish();
//							Intent ax = new Intent(ListAWBSetorCODAdd.this, Setorcod.class);
							Intent ax = new Intent(ListAWBSetorCODAdd.this, ListSetorCOD.class);
							startActivity(ax);
						}
					}, 3000);
				}

			}
		});

	}
	
	public void add_list(){
		Log.d("Debug","Lewat Add List");
		int x = listAWBArrayAdapter.getPosition(ET_ListAWB.getText().toString().trim());
		int y = listNOMINALArrayAdapter.getPosition(ET_ListNOMINAL.getText().toString().trim());
		
		Log.d("add waybill","cek posisi " + ET_ListAWB.getText().toString().trim() + " => " + x);
		Log.d("add nominal","cek posisi " + ET_ListNOMINAL.getText().toString().trim() + " => " + y);
		
//		if (x < 0 && y < 0) {
		if (y < 0) {
			listAWBArrayAdapter.add(ET_ListAWB.getText().toString().trim());
			Log.d("add waybill", "=> " + ET_ListAWB.getText().toString().trim() );
			LV_ListAWB.setAdapter(listAWBArrayAdapter);
			
			listNOMINALArrayAdapter.add(ET_ListNOMINAL.getText().toString().trim());
			Log.d("add nominal", "=> " + ET_ListNOMINAL.getText().toString().trim() );
			LV_ListNOMINAL.setAdapter(listNOMINALArrayAdapter);
			Toast.makeText(getBaseContext(), "You add "+ET_ListAWB.getText().toString().trim(), Toast.LENGTH_SHORT).show();
			
//			if (y == 0) {
//				listAWBArrayAdapter.add(ET_ListAWB.getText().toString().trim());
//				Log.d("add waybill", "=> " + ET_ListAWB.getText().toString().trim() );
//				LV_ListAWB.setAdapter(listAWBArrayAdapter);
//				
//				listNOMINALArrayAdapter.add(ET_ListNOMINAL.getText().toString().trim());
//				Log.d("add nominal", "=> " + ET_ListNOMINAL.getText().toString().trim() );
//				LV_ListNOMINAL.setAdapter(listNOMINALArrayAdapter);
//				Toast.makeText(getBaseContext(), "You add "+ET_ListAWB.getText().toString().trim(), Toast.LENGTH_SHORT).show();
//			}
//			else {
//				Toast.makeText(getBaseContext(), "You not add "+ET_ListAWB.getText().toString().trim(), Toast.LENGTH_SHORT).show();
//			}
		}
		else if (y == y) {
			listAWBArrayAdapter.add(ET_ListAWB.getText().toString().trim());
			Log.d("add waybill", "=> " + ET_ListAWB.getText().toString().trim() );
			LV_ListAWB.setAdapter(listAWBArrayAdapter);
			
			listNOMINALArrayAdapter.add(ET_ListNOMINAL.getText().toString().trim());
			Log.d("add nominal", "=> " + ET_ListNOMINAL.getText().toString().trim() );
			LV_ListNOMINAL.setAdapter(listNOMINALArrayAdapter);
			Toast.makeText(getBaseContext(), "You add "+ET_ListAWB.getText().toString().trim(), Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(getBaseContext(), "You not add "+ET_ListAWB.getText().toString().trim(), Toast.LENGTH_SHORT).show();
		}
		ET_ListAWB.setText("");
		ET_ListNOMINAL.setText("");
		update_data();
	}
	
	public void update_data() {
		Log.d("Debug","Lewat Update List");
		int i1,a1;
		int i2,a2;
		
		String STRwaybill = "";
		String STRnominal = "";
		
		i1 = this.LV_ListAWB.getCount();
		Log.d("Debug","Isi AWB = " + i1);
		
		i2 = this.LV_ListNOMINAL.getCount();
		Log.d("Debug","Isi Nominal = " + i2);
		
		for (a1 = 0; a1 < i1; a1++) {
			if(a1<i1-1){
				STRwaybill += ""+this.LV_ListAWB.getAdapter().getItem(a1)+ "-"; }
			else{
				STRwaybill += ""+this.LV_ListAWB.getAdapter().getItem(a1)+ "";
			}
		}
		
		Log.w("get waybill", STRwaybill);
		STR_swaybill = STRwaybill;
		Log.w("Debug","log waybill " + STR_swaybill);
		
		for (a2 = 0; a2 < i2; a2++) {
			if(a2<i2-1){
				STRnominal += ""+this.LV_ListNOMINAL.getAdapter().getItem(a2)+ "-"; }
			else{
				STRnominal += ""+this.LV_ListNOMINAL.getAdapter().getItem(a2)+ "";
			}
		}
		
		Log.w("get nominal", STRnominal);
		STR_nominal = STRnominal;
		Log.w("Debug","log nominal " + STR_nominal);
	}

//	private void getListView() {
//		String awb_list = "";
//		String ncod_list = "";
//		ArrayList<Lsc_Plus_Adapter> jx_LpaList = dataAdapter.daftar_list_awb_setorCod;
//		for (int i = 0; i < jx_LpaList.size(); i++) {
//			Lsc_Plus_Adapter jx_Lpa = jx_LpaList.get(i);
//			if (i != 0) {
//				awb_list = awb_list + "-";
//				ncod_list = ncod_list + "-";
//			}
//			awb_list = awb_list + jx_Lpa.getAirWaybill();
//			ncod_list = ncod_list + jx_Lpa.getNilai_Setoran();
//			
//			Log.d("Debug", "List Button Setor COD"
//					+" 1. Waybill COD " + awb_list
//					+" 2. Nilai COD " + ncod_list);
//		}
//		// Toast.makeText(getBaseContext(), "Online Status :
//		// "+awb_list,Toast.LENGTH_SHORT).show();
//
//		if (jxcon.isNetOk(ListAWBSetorCODAdd.this.getApplicationContext())) {
//			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//			postParameters.add(new BasicNameValuePair("awb_list", awb_list));
//			postParameters.add(new BasicNameValuePair("ncod_list", ncod_list));
//			postParameters.add(new BasicNameValuePair("kode_kurir", username));
//			postParameters.add(new BasicNameValuePair("total_nilai", Integer.toString(total_nilai)));
//			String response = null;
//
//			try {
//				response = CustomHttpClient.executeHttpPost(urlphp + "atex_insert_scod.php", postParameters);
//				Log.d("Debug", "response COD Button Plus >>> " + response);
//				String res = response.toString().replace(" ", "");
//				res = res.trim();
//				res = res.replaceAll("\\s+", "");
//
//				Toast.makeText(getBaseContext(), res, Toast.LENGTH_LONG).show();
//
//			} catch (Exception e) {
//				Log.d("LISTCUSTOMER", "Error : " + e.toString());
//				Toast.makeText(getBaseContext(), "Error Insert data Setoran COD", Toast.LENGTH_LONG).show();
//			}
//		} else {
//			Toast.makeText(getBaseContext(), "Offline Status", Toast.LENGTH_LONG).show();
//			new android.os.Handler().postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					finish();
//					Intent ax = new Intent(ListAWBSetorCODAdd.this, Setorcod.class);
//					startActivity(ax);
//				}
//			}, 2000);
//		}
//	}
	
	private void getListViewAWBSetorCOD() {
		Log.d("Debug","Get List AWB Setor COD");
		
		STR_total_nilai_setor = String.valueOf(total_nilai);
		
		Log.d("Debug", "Get List"
				+" 1. Waybill COD " + STR_swaybill
				+" 2. Nilai COD " + STR_nominal
				+" 3. Total Nilai COD " + STR_total_nilai_setor);
		
		String awb_list = "";
		String ncod_list = "";
		Log.d("Debug", "Array List AWB Setor COD " + daftar_list_awb_setorCod);
		HashMap<String, String> mapListAwbSetorCOD = new HashMap<String, String>();
		Log.d("Debug", "Hashmap List AWB Setor COD " + mapListAwbSetorCOD);
		
		for (int i = 0; i < mapListAwbSetorCOD.size(); i++) {
			String STR_mapListAWBSetorCOD = mapListAwbSetorCOD.get(i);
			Log.d("Debug", "String Looping " + STR_mapListAWBSetorCOD);
			if (i != 0) {
				awb_list = awb_list + "-";
				ncod_list = ncod_list + "-";
				
				Log.d("Debug", "List Button Setor COD"
						+" 1. Waybill COD " + awb_list
						+" 2. Nilai COD " + ncod_list);
			}
		}
		
		String STR_awbList = STR_swaybill;
		String STR_nominalList = STR_nominal;
		String STR_usernameKurir = STR_usernameSPref;
		String STR_totalNilaiSetor = STR_total_nilai_setor;
		Log.w("Debug", "Cek Data List" 
        		+ " || 1.List AWB = " + STR_awbList
        		+ " || 2.List Nominal = " + STR_nominalList
        		+ " || 3.Username = " + STR_usernameKurir
        		+ " || 4.Total Nilai Setor = " + STR_totalNilaiSetor);
		
		if (jxcon.isNetOk(ListAWBSetorCODAdd.this.getApplicationContext())) {
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//			postParameters.add(new BasicNameValuePair("awb_list", awb_list));
//			postParameters.add(new BasicNameValuePair("ncod_list", ncod_list));
			postParameters.add(new BasicNameValuePair("awb_list", STR_swaybill));
			postParameters.add(new BasicNameValuePair("ncod_list", STR_nominal));
//			postParameters.add(new BasicNameValuePair("kode_kurir", username));
//			postParameters.add(new BasicNameValuePair("total_nilai", Integer.toString(total_nilai)));
			postParameters.add(new BasicNameValuePair("kode_kurir", STR_usernameSPref));
			postParameters.add(new BasicNameValuePair("total_nilai", STR_total_nilai_setor));
			String response = null;
			Log.d("Debug", "Hashmap Parameter " + postParameters);

			try {
//				response = CustomHttpClient.executeHttpPost(urlphp + "atex_insert_scod.php", postParameters);
//				String URL_InsertListAWBSetorCOD = STR_urlphp+Str_LinkInsertListAWBSetorCOD;
//				Log.d("Debug", "URL Link Insert List Setor COD >>> " + URL_InsertListAWBSetorCOD);
//				response = CustomHttpClient.executeHttpPost(URL_InsertListAWBSetorCOD, postParameters);
//				Log.d("Debug", "response COD Button Plus >>> " + response);
//				String res = response.toString().replace(" ", "");
//				res = res.trim();
//				res = res.replaceAll("\\s+", "");
//				Toast.makeText(getBaseContext(), res, Toast.LENGTH_LONG).show();
				
				Log.d("Debug", "Try Update Data Setor COD ");
				cekSetorCOD(STR_awbList, STR_nominalList, STR_usernameKurir, STR_totalNilaiSetor);

			} catch (Exception e) {
				Log.d("LISTCUSTOMER", "Error : " + e.toString());
				Toast.makeText(getBaseContext(), "Error Insert data Setoran COD", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getBaseContext(), "Offline Status", Toast.LENGTH_LONG).show();
			new android.os.Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					finish();
//					Intent ax = new Intent(ListAWBSetorCODAdd.this, Setorcod.class);
					Intent ax = new Intent(ListAWBSetorCODAdd.this, ListSetorCOD.class);
					startActivity(ax);
				}
			}, 2000);
		}
	}
	
	private void displayListViewButtonPlus() {
////		STR_url_setor_cod_BtnPlus = urlphp + STR_no_assigment + "/" + STR_username;
//		STR_url_setor_cod_BtnPlus = urlphp + "/" + STR_no_assigmentSPref + "/" + STR_usernameSPref;
//		Log.d("Debug","Halaman Button Plus Setor COD" +" Test URL " + STR_url_setor_cod_BtnPlus);
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpGet request = new HttpGet(STR_url_setor_cod_BtnPlus);
//		Log.d("Debug","Halaman List Button Plus Setor COD" +" Test URL Assigment " + request);
//		try {
//			HttpResponse response = client.execute(request);
//			HttpEntity entity = response.getEntity();
//			STR_data = EntityUtils.toString(entity);
//			System.out.println(response.getStatusLine());
//			Log.e("Debug", STR_data);
//			daftar_list_awb_setorCod.clear();
//			HashMap<String, String> mapListAwbSetorCOD;
//			try {
//				JSONObject jsonObject = new JSONObject(STR_data);
//				JSONArray ListButtonPlusSetorCOD = jsonObject.getJSONArray("list_awb_cod");
//				Log.e("Debug", "Response API " + ListButtonPlusSetorCOD);
//				for (int i = 0; i < ListButtonPlusSetorCOD.length(); i++) {
//					JSONObject obj = ListButtonPlusSetorCOD.getJSONObject(i);
//					STR_SetorCOD_Waybill = obj.getString("lowb_mswb_nomor");
//					STR_SetorCOD_total_SetorWaybill = obj.getString("ttwb_cod");
//
//					total_nilai = total_nilai + Integer.parseInt(STR_SetorCOD_total_SetorWaybill);
//					Log.e("Debug", "Total Nilai COD " + total_nilai);
//
//					total_awb = ListButtonPlusSetorCOD.length();
//					Log.e("Debug", "Total AWB " + total_awb);
//
////					total_awb_row = total_awb_row + Integer.parseInt(STR_SetorCOD_Waybill);
////					Log.e("Debug", "Total Row AWB " + total_awb_row);
//
//					if (ListButtonPlusSetorCOD.length() == 0) {
//						Log.e("Debug", "Disable Button Proses");
//						button3.setEnabled(false);
//						Toast.makeText(getBaseContext(),"List Airwaybill kosong...", Toast.LENGTH_SHORT).show();
//						total_awb = -1;
//					}
//					else {
//						Log.e("Debug", "Enable Button Proses");
//						button3.setEnabled(true);
//
//					//convert mata uang - List Add Setor COD
//					DecimalFormat formatter = new DecimalFormat("#,##0.00");
//					TV_TotalNominalConvert = (TextView) findViewById(R.id.nominalAddSetorReplace);
//					TV_TotalNominalConvert.setText(STR_SetorCOD_total_SetorWaybill);
//					String strListAddSetorCOD = TV_TotalNominalConvert.getText().toString().replaceAll("[Rp,.]", "");
//					Locale localsetor = new Locale("id", "id");
//					String replaceablesetor = String.format("[Rp,.\\s]",NumberFormat.getCurrencyInstance().getCurrency().getSymbol(localsetor));
//					String strListAddSetorCODReplace = TV_TotalNominalConvert.getText().toString().replaceAll(replaceablesetor,"");
//					Log.d("Debug", "Replace Nilai List Add Setor COD " + strListAddSetorCODReplace);
//					double parsedsetor;
//					try {
//	    				parsedsetor = Double.parseDouble(strListAddSetorCOD);
//	    			} catch (NumberFormatException e) {
//	    				parsedsetor = 0.00;
//	    			}
//					parsedsetor = Double.parseDouble(strListAddSetorCODReplace);
//	                String formattedsetor = formatter.format((parsedsetor));
//	                String replacesetor = String.format("[Rp\\s]",NumberFormat.getCurrencyInstance().getCurrency().getSymbol(localsetor));
//	                String strListAddSetorCODReplace2 = formattedsetor.replaceAll(replacesetor, "");
//	                Log.d("Debug","Replace Nilai List Add Setor COD 2 " + strListAddSetorCODReplace2);
//	                TV_TotalNominalConvertFinal = (TextView) findViewById(R.id.nominalAddSetorReplace_Final);
//	                TV_TotalNominalConvertFinal.setText(strListAddSetorCODReplace2);
//
//					mapListAwbSetorCOD = new HashMap<String, String>();
//					mapListAwbSetorCOD.put(AR_NO_AWB_SETOR_COD, obj.getString("lowb_mswb_nomor"));
//					mapListAwbSetorCOD.put(AR_TOTAL_AWB_SETOR_COD, obj.getString("ttwb_cod"));
//					mapListAwbSetorCOD.put(AR_TOTAL_AWB_SETOR_COD_CONVERT, strListAddSetorCODReplace2);
//					daftar_list_awb_setorCod.add(mapListAwbSetorCOD);
//					Log.d("Debug", "Hashmap List AWB Setor COD " + daftar_list_awb_setorCod);
//
//					HashMap<String, Object> mapAWBSetorCOD=new HashMap<String, Object>();
//					mapListAwbSetorCOD.put(AR_NO_AWB_SETOR_COD, obj.getString("lowb_mswb_nomor"));
//					mapListAwbSetorCOD.put(AR_TOTAL_AWB_SETOR_COD, obj.getString("ttwb_cod"));
//					listAWBSetorCOD.add(mapAWBSetorCOD);
//					Log.d("Debug", "Hashmap AWB Setor COD " + listAWBSetorCOD);
//
//					}
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
//		this.adapter_listview_awb_setor_cod();

		//Update 02-10-2018
		JSONParseListAWBSetorCODAdd doItInBackGround = new JSONParseListAWBSetorCODAdd();
		doItInBackGround.execute();
	}

	//Update 02-10-2018
	private class JSONParseListAWBSetorCODAdd extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("Debug", "Lewat PreExecute");

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			STR_url_setor_cod_BtnPlus = urlphp + "/" + STR_no_assigmentSPref + "/" + STR_usernameSPref;
			Log.d("Debug","Halaman Button Plus Setor COD" +" Test URL " + STR_url_setor_cod_BtnPlus);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(STR_url_setor_cod_BtnPlus);
			Log.d("Debug","Halaman List Button Plus Setor COD" +" Test URL Assigment " + request);

			try {
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_list_awb_setorCod.clear();
				HashMap<String, String> mapListAwbSetorCOD;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListButtonPlusSetorCOD = jsonObject.getJSONArray("list_awb_cod");
					Log.e("Debug", "Response API " + ListButtonPlusSetorCOD);
					for (int i = 0; i < ListButtonPlusSetorCOD.length(); i++) {
						JSONObject obj = ListButtonPlusSetorCOD.getJSONObject(i);
						STR_SetorCOD_Waybill = obj.getString("lowb_mswb_nomor");
						STR_SetorCOD_total_SetorWaybill = obj.getString("ttwb_cod");

						total_nilai = total_nilai + Integer.parseInt(STR_SetorCOD_total_SetorWaybill);
						Log.e("Debug", "Total Nilai COD " + total_nilai);

						total_awb = ListButtonPlusSetorCOD.length();
						Log.e("Debug", "Total AWB " + total_awb);

						if (ListButtonPlusSetorCOD.length() == 0) {
							Log.e("Debug", "Disable Button Proses");
							button3.setEnabled(false);
							Toast.makeText(getBaseContext(),"List Airwaybill kosong...", Toast.LENGTH_SHORT).show();
							total_awb = -1;
						}
						else {
							Log.e("Debug", "Enable Button Proses");
							button3.setEnabled(true);

							//convert mata uang - List Add Setor COD
							DecimalFormat formatter = new DecimalFormat("#,##0.00");
							TV_TotalNominalConvert = (TextView) findViewById(R.id.nominalAddSetorReplace);
							TV_TotalNominalConvert.setText(STR_SetorCOD_total_SetorWaybill);
							String strListAddSetorCOD = TV_TotalNominalConvert.getText().toString().replaceAll("[Rp,.]", "");
							Locale localsetor = new Locale("id", "id");
							String replaceablesetor = String.format("[Rp,.\\s]",NumberFormat.getCurrencyInstance().getCurrency().getSymbol(localsetor));
							String strListAddSetorCODReplace = TV_TotalNominalConvert.getText().toString().replaceAll(replaceablesetor,"");
							Log.d("Debug", "Replace Nilai List Add Setor COD " + strListAddSetorCODReplace);
							double parsedsetor;
							try {
								parsedsetor = Double.parseDouble(strListAddSetorCOD);
							} catch (NumberFormatException e) {
								parsedsetor = 0.00;
							}
							parsedsetor = Double.parseDouble(strListAddSetorCODReplace);
							String formattedsetor = formatter.format((parsedsetor));
							String replacesetor = String.format("[Rp\\s]",NumberFormat.getCurrencyInstance().getCurrency().getSymbol(localsetor));
							String strListAddSetorCODReplace2 = formattedsetor.replaceAll(replacesetor, "");
							Log.d("Debug","Replace Nilai List Add Setor COD 2 " + strListAddSetorCODReplace2);
							TV_TotalNominalConvertFinal = (TextView) findViewById(R.id.nominalAddSetorReplace_Final);
							TV_TotalNominalConvertFinal.setText(strListAddSetorCODReplace2);

							mapListAwbSetorCOD = new HashMap<String, String>();
							mapListAwbSetorCOD.put(AR_NO_AWB_SETOR_COD, obj.getString("lowb_mswb_nomor"));
							mapListAwbSetorCOD.put(AR_TOTAL_AWB_SETOR_COD, obj.getString("ttwb_cod"));
							mapListAwbSetorCOD.put(AR_TOTAL_AWB_SETOR_COD_CONVERT, strListAddSetorCODReplace2);
							daftar_list_awb_setorCod.add(mapListAwbSetorCOD);
							Log.d("Debug", "Hashmap List AWB Setor COD " + daftar_list_awb_setorCod);

							HashMap<String, Object> mapAWBSetorCOD=new HashMap<String, Object>();
							mapListAwbSetorCOD.put(AR_NO_AWB_SETOR_COD, obj.getString("lowb_mswb_nomor"));
							mapListAwbSetorCOD.put(AR_TOTAL_AWB_SETOR_COD, obj.getString("ttwb_cod"));
							listAWBSetorCOD.add(mapAWBSetorCOD);
							Log.d("Debug", "Hashmap AWB Setor COD " + listAWBSetorCOD);

						}
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
			adapter_listview_awb_setor_cod();
			Log.d("Debug", "1.Test Sampai Sini ");
		}
	}
	
	public void adapter_listview_awb_setor_cod() {
		Log.e("Debug", "Listview AWB Setor COD Add");
		ListAWBSetorCODAddAdapter adapterListAWBSetorCOD = new ListAWBSetorCODAddAdapter(this,daftar_list_awb_setorCod);
		ListView listViewAWBSetorCOD = (ListView) findViewById(R.id.listView1);
		listViewAWBSetorCOD.setAdapter(adapterListAWBSetorCOD);
		adapterListAWBSetorCOD.notifyDataSetChanged();
		
		listViewAWBSetorCOD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
			}
		});
	}
	
	public void cekSetorCOD(String STR_awbList, String STR_nominalList, String STR_usernameKurir, String STR_totalNilaiSetor) throws JSONException {
		Log.d("Debug", "Lewat Cek Setor COD");
		
		HttpURLConnection connection;
		OutputStreamWriter request = null;
		URL url = null;
		String URI = null;
		String response = null;
		String parameters = "awb_list=" + STR_awbList 
				+ "&ncod_list=" + STR_nominalList
				+ "&kode_kurir=" + STR_usernameKurir
				+ "&total_nilai=" + STR_totalNilaiSetor;
		Log.d("Debug", "Parameters " + parameters);
		try {
			url = new URL(STR_urlphp+Str_LinkInsertListAWBSetorCOD);
			Log.d("Debug", "Test URL Finish Pincode " + url);
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestMethod("POST");
			
			request = new OutputStreamWriter(connection.getOutputStream());
			// Log.d("Debug","request = " + request);
			request.write(parameters);
			request.flush();
			request.close();
			String line = "";
			
			// Get data from server
			InputStreamReader isr = new InputStreamReader(connection.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();
			
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				sb.append("respone_code");
				sb.append("respone_message");
				Log.d("Debug", "TraceLine = " + line);
			}
			JSONObject json = new JSONObject(sb.toString());
			Log.d("Debug", "Trace Setor COD = " + json);
			RespnseCode = json.getString("response_code");
			RespnseMessage = json.getString("response_message");
			Log.d("Debug", "Response Code = " + RespnseCode);
			Log.d("Debug", "Response Message  = " + RespnseMessage);
			
			if (RespnseCode.equals("1")) {
				Log.i("Debug", RespnseMessage);
			} else {
				Log.i("Debug", RespnseMessage);
			}
		} catch (IOException e) {
			// Error
			Log.d("Debug", "Trace = ERROR ");
		}
	}
}