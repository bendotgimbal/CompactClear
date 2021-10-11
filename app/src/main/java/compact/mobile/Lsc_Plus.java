package compact.mobile;

import java.io.IOException;
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

import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;
import compact.mobile.setorcod.ListAWBSetorCODAddAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Lsc_Plus extends Activity {

	String locpk, username, urlphp;
	Button button1, button2, button3;
	TextView txt_jxlsc_tv;
	int total_nilai, total_awb;
	MyCustomAdapter dataAdapter = null;
	JSONArray list_awb_cod = null;

	Jx_ConnectionManager jxcon = new Jx_ConnectionManager(this.getBaseContext());
	// if(jxcon.isNetOk(MainActivity.this.getApplicationContext())){
	
	private static final String AR_NO_AWB_SETOR_COD = "no_awb";
	private static final String AR_TOTAL_AWB_SETOR_COD = "ttwb_cod";
	
	final ArrayList<HashMap<String, String>> daftar_list_awb_setorCod = new ArrayList<HashMap<String, String>>();
	
	String STR_urlphp, STR_url_setor_cod_BtnPlus, Str_lo_Koneksi, Str_LinkListAWBSetorCOD;
	String STR_username, STR_no_assigment; 
	String STR_SetorCOD_Waybill, STR_SetorCOD_total_SetorWaybill;
	String STR_data;
	SessionManager session;
	
	SharedPreferences myPrefs;
    SharedPreferences.Editor spEditor;
    SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lsc_plus);
		
		session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        STR_urlphp = user.get(SessionManager.KEY_URL);
        Log.d("Debug", "1. URL COD Button Plus >>> " + STR_urlphp);
        
        Koneksi Str_lo_Koneksi = new Koneksi();
        Str_LinkListAWBSetorCOD = Str_lo_Koneksi.ConnAwbCOD();
        
        myPrefs = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE); 
        spEditor = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE).edit();
        sp = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE);
        spEditor = myPrefs.edit();
        
        STR_username = (myPrefs.getString("sp_username_setor_cod", ""));
        Log.d("Debug", "Halaman Add Setor COD " +"Intent From List Setor COD >>> " + "Username " + STR_username);
        STR_no_assigment = (myPrefs.getString("sp_assigment_setor_cod", ""));
        Log.d("Debug", "Halaman Add Setor COD " +"Intent From List Setor COD >>> " + "Nomor Asigment " + STR_no_assigment);

		txt_jxlsc_tv = (TextView) findViewById(R.id.txt_jxlsc_tv);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
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

		if (jxcon.isNetOk(Lsc_Plus.this.getApplicationContext())) {
			Toast.makeText(getBaseContext(), "Online Status", Toast.LENGTH_SHORT).show();
//			displayListView();
			displayListViewButtonPlus();
			NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
			String currency = format.format(total_nilai).replaceAll("[$]", "Rp. \u00A0 ");

			txt_jxlsc_tv = (TextView) findViewById(R.id.txt_jxlsc_tv);
			txt_jxlsc_tv.setText(currency + "  ( " + Integer.toString(total_awb + 1) + " awb)");
		} else {
			Toast.makeText(getBaseContext(), "Offline Status", Toast.LENGTH_LONG).show();
			new android.os.Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					finish();
					Intent ax = new Intent(Lsc_Plus.this, Setorcod.class);
					startActivity(ax);
				}
			}, 2000);
		}

		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				Intent ax = new Intent(Lsc_Plus.this, Setorcod.class);
				startActivity(ax);
			}
		});
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent a = new Intent(Lsc_Plus.this, Lsc_Plus.class);
				startActivity(a);
				finish();
			}
		});
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (jxcon.isNetOk(Lsc_Plus.this.getApplicationContext())) {
					getListView();
					new android.os.Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							finish();
							Intent ax = new Intent(Lsc_Plus.this, Setorcod.class);
							startActivity(ax);
						}
					}, 3000);
				} else {
					Toast.makeText(getBaseContext(), "Offline Status", Toast.LENGTH_LONG).show();
					new android.os.Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							finish();
							Intent ax = new Intent(Lsc_Plus.this, Setorcod.class);
							startActivity(ax);
						}
					}, 3000);
				}

			}
		});

	}

	private void getListView() {
		String awb_list = "";
		String ncod_list = "";
		ArrayList<Lsc_Plus_Adapter> jx_LpaList = dataAdapter.countryList;
		for (int i = 0; i < jx_LpaList.size(); i++) {
			Lsc_Plus_Adapter jx_Lpa = jx_LpaList.get(i);
			if (i != 0) {
				awb_list = awb_list + "-";
				ncod_list = ncod_list + "-";
			}
			awb_list = awb_list + jx_Lpa.getAirWaybill();
			ncod_list = ncod_list + jx_Lpa.getNilai_Setoran();
			
			Log.d("Debug", "List Button Setor COD"
					+" 1. Waybill COD " + awb_list
					+" 2. Nilai COD " + ncod_list);
		}
		// Toast.makeText(getBaseContext(), "Online Status :
		// "+awb_list,Toast.LENGTH_SHORT).show();

		if (jxcon.isNetOk(Lsc_Plus.this.getApplicationContext())) {
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("awb_list", awb_list));
			postParameters.add(new BasicNameValuePair("ncod_list", ncod_list));
			postParameters.add(new BasicNameValuePair("kode_kurir", username));
			postParameters.add(new BasicNameValuePair("total_nilai", Integer.toString(total_nilai)));
			String response = null;

			try {
				response = CustomHttpClient.executeHttpPost(urlphp + "atex_insert_scod.php", postParameters);
				Log.d("Debug", "response COD Button Plus >>> " + response);
				String res = response.toString().replace(" ", "");
				res = res.trim();
				res = res.replaceAll("\\s+", "");

				Toast.makeText(getBaseContext(), res, Toast.LENGTH_LONG).show();

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
					Intent ax = new Intent(Lsc_Plus.this, Setorcod.class);
					startActivity(ax);
				}
			}, 2000);
		}
	}

	private void displayListView() {
		JSONParser jParser = new JSONParser();
		Lsc_Plus_Adapter country;
		ArrayList<Lsc_Plus_Adapter> countryList = new ArrayList<Lsc_Plus_Adapter>();

		try {
			JSONObject json = jParser.getJSONFromUrl(urlphp + "atex_get_awb_cod.php?kode_kurir=" + username);
			Log.d("Debug", "json get awb Setor COD >>> " + json);
			list_awb_cod = json.getJSONArray("list_awb_cod");
			if (list_awb_cod.length() == 0) {
				button3.setEnabled(false);
				Toast.makeText(getBaseContext(), "List Airwaybill kosong...", Toast.LENGTH_SHORT).show();
				total_awb = -1;
			} else {
				for (int i = 0; i < list_awb_cod.length(); i++) {
					JSONObject ar = list_awb_cod.getJSONObject(i);

					country = new Lsc_Plus_Adapter(ar.getString("lowb_mswb_nomor"), ar.getString("ttwb_cod"), false);
					countryList.add(country);

					total_nilai = total_nilai + Integer.parseInt(ar.getString("ttwb_cod"));
					total_awb = i;
				}
			}

		} catch (Exception e) {
			Log.d("LISTCUSTOMER", "Error : " + e.toString());
			Toast.makeText(getBaseContext(), "Error Pengambilan data Online", Toast.LENGTH_LONG).show();
			new android.os.Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					finish();
					Intent ax = new Intent(Lsc_Plus.this, Setorcod.class);
					startActivity(ax);
				}
			}, 10000);
		}
		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.lsc_plus_child, countryList);
		ListView listView = (ListView) findViewById(R.id.listView1);

		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

	}
	
	private void displayListViewButtonPlus() {
		STR_url_setor_cod_BtnPlus = urlphp + STR_no_assigment + "/" + STR_username;
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
					
					mapListAwbSetorCOD = new HashMap<String, String>();
					mapListAwbSetorCOD.put(AR_NO_AWB_SETOR_COD, obj.getString("lowb_mswb_nomor"));
					mapListAwbSetorCOD.put(AR_TOTAL_AWB_SETOR_COD, obj.getString("ttwb_cod"));
					daftar_list_awb_setorCod.add(mapListAwbSetorCOD);
					Log.d("Debug", "Hashmap List AWB Setor COD " + daftar_list_awb_setorCod);
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
		this.adapter_listview_awb_setor_cod();
	}

	private class MyCustomAdapter extends ArrayAdapter<Lsc_Plus_Adapter> {

		private ArrayList<Lsc_Plus_Adapter> countryList;

		public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Lsc_Plus_Adapter> countryList) {
			super(context, textViewResourceId, countryList);
			this.countryList = new ArrayList<Lsc_Plus_Adapter>();
			this.countryList.addAll(countryList);
		}

		private class ViewHolder {
			TextView code;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.lsc_plus_child, null);

				holder = new ViewHolder();
				holder.code = (TextView) convertView.findViewById(R.id.code);
				// holder.name = (CheckBox)
				// convertView.findViewById(R.id.checkBox1);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Lsc_Plus_Adapter jx_Lsc_Plus_Adapter = countryList.get(position);
			holder.code.setText(jx_Lsc_Plus_Adapter.getAirWaybill() + " \u00A0\u00A0\u00A0\u00A0 ( Nilai Setoran : "
					+ jx_Lsc_Plus_Adapter.getNilai_Setoran() + " )");
			// holder.name.setText(jx_Lsc_Plus_Adapter.getAirWaybill());
			// holder.name.setChecked(jx_Lsc_Plus_Adapter.isSelected());
			// holder.name.setTag(jx_Lsc_Plus_Adapter);

			return convertView;

		}

	}
	
	public void adapter_listview_awb_setor_cod() {
		Log.e("Debug", "Listview AWB Setor COD");
		ListAWBSetorCODAddAdapter adapterListAWBSetorCOD = new ListAWBSetorCODAddAdapter(this,daftar_list_awb_setorCod);
		ListView listViewAWBSetorCOD = (ListView) findViewById(R.id.listView1);
		listViewAWBSetorCOD.setAdapter(adapterListAWBSetorCOD);
		adapterListAWBSetorCOD.notifyDataSetChanged();
		
		listViewAWBSetorCOD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
			}
		});
	}
}
