package compact.mobile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;
import compact.mobile.setorcod.ListAWBSetorCODAdd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;

public class Setorcod extends Activity {
	TextView txt_sts_jxlsc;
	String locpk, username, urlphp;
	Integer cek_list = 1;
	JSONArray list_scd = null;
	SetorcodAdapter sql_scd_table;
	ImageButton imb_plus_jxlsc;

	Jx_ConnectionManager jxcon = new Jx_ConnectionManager(this.getBaseContext());
	// if(jxcon.isNetOk(MainActivity.this.getApplicationContext())){

	private LinkedHashMap<String, GroupInfo> subjects = new LinkedHashMap<String, GroupInfo>();
	private ArrayList<GroupInfo> deptList = new ArrayList<GroupInfo>();
	private CustomAdapter listAdapter;
	private ExpandableListView simpleExpandableListView;
	
	final ArrayList<HashMap<String, String>> daftar_list_setorCod = new ArrayList<HashMap<String, String>>();
	
	String STR_urlphp, STR_url_setor_cod, Str_lo_Koneksi, Str_LinkListSetorCOD;
	String STR_username, STR_no_assigment;
	String STR_data;
	SessionManager session;
	
	SharedPreferences myPrefs;
    SharedPreferences.Editor spEditor;
    SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setor_cod);
		
		session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        STR_urlphp = user.get(SessionManager.KEY_URL);
        STR_username = user.get(SessionManager.KEY_USER);
        Log.d("Debug", "1. URL Setor COD >>> " + STR_urlphp);
        Log.d("Debug", "Username Setor COD >>> " + STR_username);
        
        Koneksi Str_lo_Koneksi = new Koneksi();
        Str_LinkListSetorCOD = Str_lo_Koneksi.ConnSetorCOD();
        
        Intent in = getIntent();
        STR_no_assigment = in.getStringExtra("AR_NO_ASSIGMENT");
        Log.d("Debug", "Halaman List Setor COD " +"Intent From Others Asigment >>>" 
                + " Nomor Asigment " + STR_no_assigment);
        
        myPrefs = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE); 
        spEditor = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE).edit();
        sp = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE);
        spEditor = myPrefs.edit();
        
//        STR_username = (myPrefs.getString("sp_username_setor_cod", ""));
//        Log.d("Debug", "Halaman Add Setor COD " +"Intent From List Setor COD >>> " + "Username " + STR_username);
//        STR_no_assigment = (myPrefs.getString("sp_assigment_setor_cod", ""));
//        Log.d("Debug", "Halaman Add Setor COD " +"Intent From List Setor COD >>> " + "Nomor Asigment " + STR_no_assigment);

		imb_plus_jxlsc = (ImageButton) findViewById(R.id.imb_plus_jxlsc);

		urlgw url = urlgw.getInstance();
//		urlphp = url.getData();
//		urlphp = STR_urlphp+"/android/";
		urlphp = STR_urlphp+Str_LinkListSetorCOD;
		Log.d("Debug", "2. URL Setor COD >>> " + urlphp);
		UserName S1 = UserName.getInstance();
		username = S1.getData();

////		getDataScd();
//		getDataSetorCOD();

		//Update 02-10-2018
		JSONParseListgetDataSetorCOD doItInBackGround = new JSONParseListgetDataSetorCOD();
		doItInBackGround.execute();
		Log.d("cek list",cek_list.toString());
		if(cek_list != 1){
			getAllDataLite();
		}
		

		// get reference of the ExpandableListView
		simpleExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
		// create the adapter by passing your ArrayList data
		listAdapter = new CustomAdapter(Setorcod.this, deptList);
		// attach the adapter to the expandable list view
		simpleExpandableListView.setAdapter(listAdapter);

		imb_plus_jxlsc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent ax = new Intent(Setorcod.this, Lsc_Plus.class);
				Intent ax = new Intent(Setorcod.this, ListAWBSetorCODAdd.class);
				ax.putExtra("STR_no_assigment", STR_no_assigment);
                ax.putExtra("STR_username", STR_username);
                Log.d("Debug", "To Add Setor COD >>> " +"No.Assigment = " +STR_no_assigment);
                Log.d("Debug", "To Add Setor COD >>> " +"Username = " +STR_username);
                
                spEditor.putString(sharedpref.SP_ASSIGMENT_SETOR_SCOD, STR_no_assigment);
				spEditor.putString(sharedpref.SP_USERNAME_SETOR_SCOD, STR_username);
	            spEditor.commit();
				startActivity(ax);
				finish();
			}
		});
	}

	private void getAllDataLite() {
		sql_scd_table = new SetorcodAdapter(Setorcod.this);
		sql_scd_table.open();
		Cursor cur = sql_scd_table.getAllContact();
		if (cur != null) {
			cur.moveToFirst();
			do {
				Log.d("Jx_ListSetorCod", ":  getAllDataLite : "
						+ cur.getString(cur.getColumnIndexOrThrow(sql_scd_table.BSC_KODE_SETORAN)));
				// addProduct(get_rp[0],get_rp[1]);
				String[] spResponse = cur.getString(cur.getColumnIndexOrThrow(sql_scd_table.BSC_LIST_WAYBILL))
						.split("-");
				String sts_setoran = "";
				if (cur.getString(cur.getColumnIndexOrThrow(sql_scd_table.BSC_STATUS)).equals("1")) {
					sts_setoran = "Sudah Terkonfirmasi";
				} else {
					sts_setoran = "Belum Terkonfirmasi";
				}

				for (String chSpResponse : spResponse) {
					// Log.d("Jx_ListSetorCod",": getAllDataLite : " +
					// chSpResponse);
					addProduct(cur.getString(cur.getColumnIndexOrThrow(sql_scd_table.BSC_KODE_SETORAN))
							+ "-Nilai Setoran : "
							+ cur.getString(cur.getColumnIndexOrThrow(sql_scd_table.BSC_TOTAL_NILAI_COD))
							+ "-Status Setoran : " + sts_setoran, chSpResponse);
				}
			} while (cur.moveToNext());

		} else {
			Toast.makeText(getBaseContext(), "Data List Kosong", Toast.LENGTH_LONG).show();
		}

		sql_scd_table.close();
	}

	private void getDataScd() {
		if (jxcon.isNetOk(Setorcod.this.getApplicationContext())) {
			Toast.makeText(getBaseContext(), "Online Status", Toast.LENGTH_LONG).show();
			JSONParser jParser = new JSONParser();

			try {
				JSONObject json = jParser.getJSONFromUrl(urlphp + "atex_get_list_scd.php?kode_kurir=" + username);
				Log.d("Debug", "json get list Setor COD >>> " + json);
				list_scd = json.getJSONArray("list_scd");
				Log.d("list_scd", "Panjang Store List : " + list_scd.length());
				
				if(list_scd.length() == 0){
					cek_list = 1;
				}else{
					cek_list = 0;
				}

				sql_scd_table = new SetorcodAdapter(Setorcod.this);
				sql_scd_table.open();
				sql_scd_table.deleteAllContact();
				sql_scd_table.insertData(list_scd);
				sql_scd_table.close();

			} catch (Exception e) {
				Log.d("LISTCUSTOMER", "Error : " + e.toString());
				Toast.makeText(getBaseContext(), "Error Pengambilan data Online", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getBaseContext(), "Offline Status", Toast.LENGTH_LONG).show();
		}
	}
	
	private void getDataSetorCOD() {
		STR_url_setor_cod = urlphp + STR_username;
		Log.d("Debug","Halaman Setor COD" +" Test URL " + STR_url_setor_cod);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(STR_url_setor_cod);
		Log.d("Debug","Halaman List Setor COD" +" Test URL Assigment " + request);
		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			STR_data = EntityUtils.toString(entity);
			System.out.println(response.getStatusLine());
			Log.e("Debug", STR_data);
			daftar_list_setorCod.clear();
			HashMap<String, String> mapListSetorCOD;
			try {
				JSONObject jsonObject = new JSONObject(STR_data);
				JSONArray ListSetorCOD = jsonObject.getJSONArray("list_scd");
				Log.e("Debug", "Response API " + ListSetorCOD);
				for (int i = 0; i < ListSetorCOD.length(); i++) {
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("Debug", "Trace = ERROR ");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Update 02-10-2018
	private class JSONParseListgetDataSetorCOD extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("Debug", "Lewat PreExecute");

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			STR_url_setor_cod = urlphp + STR_username;
			Log.d("Debug","Halaman Setor COD" +" Test URL " + STR_url_setor_cod);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(STR_url_setor_cod);
			Log.d("Debug","Halaman List Setor COD" +" Test URL Assigment " + request);

			try {
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_list_setorCod.clear();
				HashMap<String, String> mapListSetorCOD;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListSetorCOD = jsonObject.getJSONArray("list_scd");
					Log.e("Debug", "Response API " + ListSetorCOD);
					for (int i = 0; i < ListSetorCOD.length(); i++) {

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
			Log.d("Debug", "1.Test Sampai Sini ");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.setor_cod, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// respond to menu item selection
		switch (item.getItemId()) {
		case R.id.refresh:
			Intent a = new Intent(Setorcod.this, Setorcod.class);
			startActivity(a);
			finish();

			return true;

		case R.id.back:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private int addProduct(String department, String product) {

		int groupPosition = 0;
		// check the hash map if the group already exists
		GroupInfo headerInfo = subjects.get(department);
		// add the group if doesn't exists
		if (headerInfo == null) {
			headerInfo = new GroupInfo();
			headerInfo.setName(department);
			subjects.put(department, headerInfo);
			deptList.add(headerInfo);
		}

		// get the children for the group
		ArrayList<ChildInfo> productList = headerInfo.getProductList();
		// create a new child and add that to the group
		ChildInfo detailInfo = new ChildInfo();
		detailInfo.setSequence("");
		detailInfo.setName(product);
		productList.add(detailInfo);
		headerInfo.setProductList(productList);

		// find the group position inside the list
		groupPosition = deptList.indexOf(headerInfo);
		return groupPosition;
	}
}
