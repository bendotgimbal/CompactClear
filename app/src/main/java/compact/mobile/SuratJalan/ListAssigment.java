package compact.mobile.SuratJalan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import compact.mobile.JSONParser;
import compact.mobile.MainActivity;
import compact.mobile.R;
import compact.mobile.gps_tracker;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import compact.mobile.SessionManager;
import compact.mobile.SuratJalan.helper.ListAssigmentAdapter;
import compact.mobile.config.GpsService;
import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;
import compact.mobile.imei;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class ListAssigment extends Activity {
	
	Button BTN_keluar, BTN_refresh;
	
	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_TANGGAL = "tanggal";
	private static final String AR_AWB = "awb";
	private static final String AR_STATUS = "status";
	private static final String AR_JENIS = "jenis";

    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int PERMISSION_REQUEST_CODE = 200;
    TelephonyManager telephonyManager;
    LocationManager locationManager;
    String myimei  ;
	
	String STR_locpk, STR_username, STR_urlphp;
	String STR_URL_API, STR_url_assigment;
	String Str_sp_url_assigment;
	String STR_assigment, STR_tanggal, STR_awb, STR_status, STR_jenis;
	String STR_no_assigment;
	String STR_data;
	String RespnseMessage, RespnseCode;
	
	public String Str_lo_Koneksi, Str_LinkListAssigment;
	
	final ArrayList<HashMap<String, String>> daftar_list_assigment = new ArrayList<HashMap<String, String>>();

    SessionManager sessionMan;
	SessionManager session;
	gps_tracker gps;

//	GoogleMap googleMap;
	GpsService gpsServices;
	
	private Location locationPhone;
	
	// cek apakah GPS aktif ?
		boolean isGPSEnable = false;
		// cek network aktif ?
		boolean isNetworkEnable = false;
		// cek network Pasti aktif ?
		boolean canGetLocation = false;
	
	SharedPreferences myPrefs;
	SharedPreferences.Editor spEditor;
    SharedPreferences sp;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_assigment);

        sessionMan = new SessionManager(getApplicationContext());
        if( sessionMan.isLoggedIn()==false){
            Log.d("Debug","session false " );
            Intent a = new Intent(ListAssigment.this, MainActivity.class);
            startActivity(a);
            finish();
        }else {
            Log.d("Debug", "session true ");
            HashMap<String, String> user = sessionMan.getUserDetails();
            String str_username = user.get(SessionManager.KEY_USER);
            Log.d("Debug", "Session Username " + str_username);

            if (checkPermissions()) {
                Log.d("Debug", "if checkPermissions" + " || startApplication");
                startApplication();
                boolean success = true;
                if (success) {
                    Log.d("Debug", "success to create directory");
                    Log.d("Debug", "ListAssigment - 1a success");
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                    Log.d("Debug", "ListAssigment - If");
                    String intStorageDirectory = getFilesDir().toString();
                    Log.d("Debug", "Dir Storage " + intStorageDirectory);
                    File mediaStorageDirFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File mediaStorageDir = new File(mediaStorageDirFolder + File.separator + "Compact" + File.separator);
                    Log.d("Debug", "Media Storage " + mediaStorageDir);

					gps = new gps_tracker(ListAssigment.this);
					gpsServices = new GpsService(ListAssigment.this);

					String Str_check_connection_awb_others = null;
					ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
					if (networkinfo != null && networkinfo.isConnected()) {
						// aksi ketika ada koneksi internet
						Str_check_connection_awb_others = "Yes Signal";
						Log.d("Debug", "Halaman ListAssigment"
								+ " Koneksi Internet Tersedia");
					} else {
						// aksi ketika tidak ada koneksi internet
						Str_check_connection_awb_others = "No Signal";
						Log.d("Debug", "Halaman ListAssigment"
								+ " Koneksi Internet Tidak Tersedia");
					}

					String Str_lat_long = Double.toString(gps.getLatitude()) + ", "
							+ Double.toString(gps.getLongitude());
					Log.d("Debug", "Halaman ListAssigment" + " LatLong = " + Str_lat_long);

					Location location = new Location("");
					double lng = location.getLongitude();
					double lat = location.getLatitude();
					Log.d("Debug", "Halaman ListAssigment " + "Lat = " + lat +" || "+"Lang = " + lng);

					if (gpsServices.canGetLocation())
					{
						String stringLatitude = String.valueOf(gps.getLatitude());
						String stringLongitude = String.valueOf(gps.getLongitude());
						Log.d("Debug","Log Lokasi AutoNetwork 1 "+stringLatitude + ", " + stringLongitude);

						// ambil latitude dan longitude
						double latitude = gps.getLatitude();
						double longitude = gps.getLongitude();
						Log.d("Debug","Log Lokasi AutoNetwork "+latitude + ", " + longitude);
					}

					LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					// Creating a criteria object to retrieve provider
					Criteria criteria = new Criteria();
					// Getting the name of the best provider
					String provider = locationManager.getBestProvider(criteria, true);
					// Getting Current Location
					Location locationPhone = locationManager.getLastKnownLocation(provider);

					// cek GPS status
					isGPSEnable = locationManager
							.isProviderEnabled(LocationManager.GPS_PROVIDER);
					// cek status koneksi
					isNetworkEnable = locationManager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

					if (location == null && !isGPSEnable) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, 200000, 100, (LocationListener) this);
						locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//					MapUpdateLocation();
						Toast.makeText(this, "Cek Pertama Running, GPS Aktif",
								Toast.LENGTH_SHORT).show();
					} else if (location == null && !isNetworkEnable) {
						locationManager.requestLocationUpdates(
								LocationManager.NETWORK_PROVIDER, 200000, 100, (LocationListener) this);
						locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//					MapUpdateLocation();
						Toast.makeText(this, "Cek Pertama Running, Koneksi Internet Aktif",
								Toast.LENGTH_SHORT).show();
					}

                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            Log.d("Debug", "failed to create directory");
                            Log.d("Debug", "ListAssigment - 1a");
                        } else {
                            Log.d("Debug", "success to create directory");
                            Log.d("Debug", "ListAssigment - 1b");
                        }
                    }
                } else {
                    Log.d("Debug", "failed to create directory");
                    Log.d("Debug", "ListAssigment - 1b failed");
                }
            } else {
                Log.d("Debug", "else checkPermissions" + " || setPermissions");
                setPermissions();
                boolean success = true;
                if (success) {
                    Log.d("Debug", "success to create directory");
                    Log.d("Debug", "ListAssigment - 2a success");
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                    Log.d("Debug", "Else");
                    String intStorageDirectory = getFilesDir().toString();
                    Log.d("Debug", "Dir Storage " + intStorageDirectory);
                    File mediaStorageDirFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File mediaStorageDir = new File(mediaStorageDirFolder + File.separator + "Compact" + File.separator);
                    Log.d("Debug", "Media Storage " + mediaStorageDir);

                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            Log.d("Debug", "failed to create directory");
                            Log.d("Debug", "ListAssigment - 2a");
                        } else {
                            Log.d("Debug", "success to create directory");
                            Log.d("Debug", "ListAssigment - 2b");
                        }
                    }
                } else {
                    Log.d("Debug", "failed to create directory");
                    Log.d("Debug", "ListAssigment - 2b failed");
                }
            }
        }
        
//        gps = new gps_tracker(ListAssigment.this);
//        gpsServices = new GpsService(ListAssigment.this);
//
//		String Str_check_connection_awb_others = null;
//		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
//		if (networkinfo != null && networkinfo.isConnected()) {
//			// aksi ketika ada koneksi internet
//			Str_check_connection_awb_others = "Yes Signal";
//			Log.d("Debug", "Halaman ListAssigment"
//					+ " Koneksi Internet Tersedia");
//		} else {
//			// aksi ketika tidak ada koneksi internet
//			Str_check_connection_awb_others = "No Signal";
//			Log.d("Debug", "Halaman ListAssigment"
//					+ " Koneksi Internet Tidak Tersedia");
//		}
//
//		String Str_lat_long = Double.toString(gps.getLatitude()) + ", "
//				+ Double.toString(gps.getLongitude());
//		Log.d("Debug", "Halaman ListAssigment" + " LatLong = " + Str_lat_long);
//
//		Location location = new Location("");
//		double lng = location.getLongitude();
//		double lat = location.getLatitude();
//		Log.d("Debug", "Halaman ListAssigment " + "Lat = " + lat +" || "+"Lang = " + lng);
//
//		if (gpsServices.canGetLocation())
//		{
//			String stringLatitude = String.valueOf(gps.getLatitude());
//			String stringLongitude = String.valueOf(gps.getLongitude());
//			Log.d("Debug","Log Lokasi AutoNetwork 1 "+stringLatitude + ", " + stringLongitude);
//
//			// ambil latitude dan longitude
//			double latitude = gps.getLatitude();
//			double longitude = gps.getLongitude();
//			Log.d("Debug","Log Lokasi AutoNetwork "+latitude + ", " + longitude);
//		}
//
//		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		// Creating a criteria object to retrieve provider
//		Criteria criteria = new Criteria();
//		// Getting the name of the best provider
//		String provider = locationManager.getBestProvider(criteria, true);
//		// Getting Current Location
//		Location locationPhone = locationManager.getLastKnownLocation(provider);
//
//		// cek GPS status
//				isGPSEnable = locationManager
//						.isProviderEnabled(LocationManager.GPS_PROVIDER);
//				// cek status koneksi
//				isNetworkEnable = locationManager
//						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//				if (location == null && !isGPSEnable) {
//					locationManager.requestLocationUpdates(
//							LocationManager.GPS_PROVIDER, 200000, 100, (LocationListener) this);
//					locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
////					MapUpdateLocation();
//					Toast.makeText(this, "Cek Pertama Running, GPS Aktif",
//							Toast.LENGTH_SHORT).show();
//				} else if (location == null && !isNetworkEnable) {
//					locationManager.requestLocationUpdates(
//							LocationManager.NETWORK_PROVIDER, 200000, 100, (LocationListener) this);
//					locationManager
//							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
////					MapUpdateLocation();
//					Toast.makeText(this, "Cek Pertama Running, Koneksi Internet Aktif",
//							Toast.LENGTH_SHORT).show();
//				}
        
        Koneksi Str_lo_Koneksi = new Koneksi();
        Str_LinkListAssigment = Str_lo_Koneksi.ConnAssigment();
        
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
				Intent a = new Intent(ListAssigment.this, ListAssigment.class);
				startActivity(a);

			}
		});
	 
		BTN_keluar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		Str_sp_url_assigment = (myPrefs.getString("sp_url_assigment", ""));
        Log.d("Debug", "URL Shared Preferences"+" || URL Assigment = " + Str_sp_url_assigment);
        
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        STR_username = user.get(SessionManager.KEY_USER);
        STR_urlphp = user.get(SessionManager.KEY_URL);
        STR_locpk = user.get(SessionManager.KEY_LOCPK);
	    Log.e("Debug", "Halaman ListAssigment " +"ISI >>> "
	    	    + "Username = " + STR_username
	    	    + " URL = " + STR_urlphp
	    	    + " Entah = " + STR_locpk);
	    
	    spEditor.putString(sharedpref.SP_USERNAME, STR_username);
        spEditor.commit();
	    
//	    if (STR_urlphp.equals("http://43.252.144.14:81/android/")) {
//	    	Log.i("Debug", "Halaman ListAssigment " +"Replace URL Lokal Server");
//	    	STR_URL_API = STR_urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/compact_mobile");
//		}
//	    else if (STR_urlphp.equals("http://compact.atex.co.id/android/")) {
//	    	Log.i("Debug", "Halaman ListAssigment " +"Replace URL Online Server");
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
//	    STR_URL_API = STR_urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/compact_mobile");
//		Log.i("Debug", "Halaman ListAssigment " +"Test String Replace URL " + STR_URL_API);
		
////		STR_url_assigment = STR_URL_API + "/" +"assigment"+"/"+ STR_username;
////		STR_url_assigment = Str_sp_url_assigment + "/" +"assigment"+"/"+ STR_username;
//	    STR_url_assigment = STR_urlphp + Str_LinkListAssigment+STR_username;
//		Log.d("Debug","Halaman ListAssigment " +"Test URL " + STR_url_assigment);
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpGet request = new HttpGet(STR_url_assigment);
//        Log.d("Debug","Halaman ListAssigment " +"Test URL Assigment " + request);
//        try {
//        	HttpResponse response = client.execute(request);
//			HttpEntity entity = response.getEntity();
//			STR_data = EntityUtils.toString(entity);
//			System.out.println(response.getStatusLine());
//			Log.e("Debug", STR_data);
//			daftar_list_assigment.clear();
//			HashMap<String, String> mapAssigment;
//			try {
//				JSONObject jsonObject = new JSONObject(STR_data);
//				JSONArray ListObjAssigment = jsonObject.getJSONArray("data");
//				RespnseMessage = jsonObject.getString("response_message");
//				RespnseCode = jsonObject.getString("response_code");
//				Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
//				for (int i = 0; i < ListObjAssigment.length(); i++) {
//					JSONObject obj = ListObjAssigment.getJSONObject(i);
//					STR_assigment = obj.getString("asigment");
//					STR_tanggal = obj.getString("tanggal");
//					STR_awb = obj.getString("awb");
//					STR_status = obj.getString("status");
////					jenis = obj.getString("jenis");
//
//					mapAssigment = new HashMap<String, String>();
//					mapAssigment.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
//					mapAssigment.put(AR_TANGGAL, obj.getString("tanggal"));
//					mapAssigment.put(AR_AWB, obj.getString("awb"));
//					mapAssigment.put(AR_STATUS, obj.getString("status"));
////					mapAssigment.put(AR_JENIS, obj.getString("jenis"));
//					daftar_list_assigment.add(mapAssigment);
//					Log.d("Debug", "Hashmap Assigment " + daftar_list_assigment);
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
//        this.adapter_listview_assigment();

		//Update 02-10-2018
		JSONParseListAssigment doItInBackGround = new JSONParseListAssigment();
		doItInBackGround.execute();
	}

	//Update 02-10-2018
	private class JSONParseListAssigment extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("Debug", "Lewat PreExecute");

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			STR_url_assigment = STR_urlphp + Str_LinkListAssigment+STR_username;
			Log.d("Debug","Halaman ListAssigment " +"Test URL " + STR_url_assigment);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(STR_url_assigment);
			Log.d("Debug","Halaman ListAssigment " +"Test URL Assigment " + request);

			try {
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_list_assigment.clear();
				HashMap<String, String> mapAssigment;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListObjAssigment = jsonObject.getJSONArray("data");
					RespnseMessage = jsonObject.getString("response_message");
					RespnseCode = jsonObject.getString("response_code");
					Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
					for (int i = 0; i < ListObjAssigment.length(); i++) {
						JSONObject obj = ListObjAssigment.getJSONObject(i);
						STR_assigment = obj.getString("asigment");
						STR_tanggal = obj.getString("tanggal");
						STR_awb = obj.getString("awb");
						STR_status = obj.getString("status");

						mapAssigment = new HashMap<String, String>();
						mapAssigment.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
						mapAssigment.put(AR_TANGGAL, obj.getString("tanggal"));
						mapAssigment.put(AR_AWB, obj.getString("awb"));
						mapAssigment.put(AR_STATUS, obj.getString("status"));
						daftar_list_assigment.add(mapAssigment);
						Log.d("Debug", "Hashmap Assigment " + daftar_list_assigment);
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
			adapter_listview_assigment();
			Log.d("Debug", "1.Test Sampai Sini ");

		}
	}

	public void adapter_listview_assigment() {
		Log.e("Debug", "Listview Nomor Assigment");
		ListAssigmentAdapter adapterAssigment = new ListAssigmentAdapter(this, daftar_list_assigment);
		ListView listViewAssigment = (ListView) findViewById(R.id.listviews_assigment);
		listViewAssigment.setAdapter(adapterAssigment);
		adapterAssigment.notifyDataSetChanged();
		
		listViewAssigment.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),daftar_list_assigment.get(position).get(AR_NO_ASSIGMENT).toString()+ " Selected", Toast.LENGTH_LONG).show();
				STR_no_assigment = daftar_list_assigment.get(position).get(AR_NO_ASSIGMENT).toString();
				spEditor.putString(sharedpref.SP_NO_SPJ, STR_no_assigment);
	            spEditor.commit();
	            spEditor.putString(sharedpref.SP_ASSIGMENT_ALL, STR_no_assigment);
		        spEditor.commit();
//				Intent i = new Intent(getApplicationContext(), OtherAssigment.class);
				Intent i = new Intent (ListAssigment.this, OtherAssigment.class);
				i.putExtra("AR_NO_ASSIGMENT", STR_no_assigment);
				Log.d("Debug", "To Others Assigment >>> " +"No.Assigment = " +STR_no_assigment);
				startActivity(i);
//				startActivityForResult(i, 1);
//                finish();
			}
		});
		
	}

    private boolean checkPermissions() {
        int read_phone_state = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int access_fine_location = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int write_esternal_storage= ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int read_esternal_storage= ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int camera= ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return read_phone_state == PackageManager.PERMISSION_GRANTED && access_fine_location == PackageManager.PERMISSION_GRANTED && write_esternal_storage == PackageManager.PERMISSION_GRANTED && read_esternal_storage == PackageManager.PERMISSION_GRANTED && camera == PackageManager.PERMISSION_GRANTED;
    }

    private void setPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE, ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @SuppressLint("MissingPermission") public void startApplication() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String deviceId = telephonyManager.getDeviceId();
        imei imgw = imei.getInstance();

        if (telephonyManager != null)
            deviceId = telephonyManager.getDeviceId();
        if (deviceId == null || deviceId.length() == 0)
            deviceId = Build.FINGERPRINT;

        imgw.setData(deviceId);
        imei im = imei.getInstance();
        myimei  = im.getData();
        Log.d("debug","imei = " + myimei );

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location locationPhone = locationManager.getLastKnownLocation(provider);
    }
	
}
