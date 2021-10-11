package compact.mobile;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import compact.mobile.SuratJalan.AlfadigitalWebViewActivity;
import compact.mobile.SuratJalan.DB.C_ADS;
import compact.mobile.SuratJalan.DB.C_AWBOthers;
import compact.mobile.SuratJalan.DB.C_DEX;
import compact.mobile.SuratJalan.DB.C_Finish_ADS;
import compact.mobile.SuratJalan.DB.C_Finish_AWBOthers;
import compact.mobile.SuratJalan.DB.C_Finish_PoOutstanding;
import compact.mobile.SuratJalan.DB.C_POD;
import compact.mobile.SuratJalan.DB.C_Pincode_ADS;
import compact.mobile.SuratJalan.DB.C_PoOutstanding;
import compact.mobile.SuratJalan.DB.ListADSDBAdapter;
import compact.mobile.SuratJalan.DB.ListAWBOthersDBAdapter;
import compact.mobile.SuratJalan.DB.ListDEXDBAdapter;
import compact.mobile.SuratJalan.DB.ListFinishADSDBAdapter;
import compact.mobile.SuratJalan.DB.ListFinishAWBOthersDBAdapter;
import compact.mobile.SuratJalan.DB.ListFinishPincodeADSDBAdapter;
import compact.mobile.SuratJalan.DB.ListFinishPoOutstandingDBAdapter;
import compact.mobile.SuratJalan.DB.ListPODDBAdapter;
import compact.mobile.SuratJalan.DB.ListPoOutstandingDBAdapter;
import compact.mobile.SuratJalan.ListAssigment;
import compact.mobile.config.Koneksi;
import compact.mobile.config.firebase.app.ConfigFirebase;
import compact.mobile.config.firebase.database.UserKurirFirebase;
import compact.mobile.config.sharedpref;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class menu_utama extends Activity {
	ListPoOutstandingDBAdapter dbListPoOutstanding;
	ListFinishPoOutstandingDBAdapter dbFinishPoOutstanding;
	ListADSDBAdapter dbListADS;
	ListFinishADSDBAdapter dbFinishADS;
	ListAWBOthersDBAdapter dbListAWBOthers;
	ListFinishAWBOthersDBAdapter dbFinishAWBOthers;
	ListPODDBAdapter dbListPOD;
	ListDEXDBAdapter dbListDEX;
	ListFinishPincodeADSDBAdapter dbFinishPincodeADS;
	String Str_urlphp_po, STR_url_po_outstanding, STR_url_finish_po_outstanding, STR_url_ads
	, STR_url_finish_ads, STR_url_awb_others, STR_url_finish_awb_others, STR_url_pod, STR_url_dex;
	String Str_url_po, Str_url_cekupdate;
	String RespnseMessage, RespnseCode, STR_response_code;
	String Str_sp_url_scan_PO, Str_sp_url_scan_ADS, Str_sp_url_scan_AWB_Others, Str_sp_url_scan_POD, Str_sp_url_scan_DEX
	, Str_sp_url_scan_finish_PO, Str_sp_url_scan_finish_ADS, Str_sp_url_scan_finish_AWB_Others;
	String Str_sp_url_assigment;
	String STR_url_cek_version;
	String Str_sp_cek_version;
	String STR_version, STR_status, Str_version2, Str_status2;
	TextView TV_version, TV_status, TV_version_now;
	String Str_check_connection, Str_offline_response;
	String STR_remove_IDFirebase, STR_ResponseCodeRemoveToken, STR_badgeCount;
	public String Str_lo_Koneksi,Str_LinkUpdate, STR_Linkpo_outstanding, STR_Linkfinish_po_outstanding, STR_Linkads,
	STR_Linkfinish_ads, STR_Linkawb_others, STR_Linkfinish_awb_others, STR_Linkpod, STR_Linkdex;
	public String Str_login_user;


	private Location location;
	// cek apakah GPS aktif ?
	boolean						isGPSEnable				= false;
	// cek network aktif ?
	boolean						isNetworkEnable			= false;
	// pencarian location ?
	boolean						canGetLocation			= false;

	private static final int PERMISSION_REQUEST_CODE = 200;
	TelephonyManager telephonyManager;
	LocationManager locationManager;

	private static final String TAG = menu_utama.class.getSimpleName();
	private DatabaseReference mFirebaseDatabase;
	private FirebaseDatabase mFirebaseInstance;
	private String userId;

	private static final int REQUEST_WRITE_STORAGE = 112;

//	//Shared Preferences scan dan finish
//	SharedPreferences myPrefs_url_scan_PO, myPrefs_url_scan_ADS, myPrefs_url_scan_AWB_Others, myPrefs_url_scan_POD, myPrefs_url_scan_DEX
//	, myPrefs_url_scan_finish_PO, myPrefs_url_scan_finish_ADS, myPrefs_url_scan_finish_AWB_Others;
//	SharedPreferences.Editor spEditor_url_scan_PO, spEditor_url_scan_ADS, spEditor_url_scan_AWB_Others, spEditor_url_scan_POD, spEditor_url_scan_DEX
//	, spEditor_url_scan_finish_PO, spEditor_url_scan_finish_ADS, spEditor_url_scan_finish_AWB_Others;
//    SharedPreferences sp_url_scan_PO, sp_url_scan_ADS, sp_url_scan_AWB_Others, sp_url_scan_POD, sp_url_scan_DEX
//    , sp_url_scan_finish_PO, sp_url_scan_finish_ADS, sp_url_scan_finish_AWB_Others;

	public String Str_UrlAccessKeyAWS, Str_LinkAccessKeyAWS;
	String STR_AWS_Key_Name, STR_AWS_Key_Code, STR_AWS_Secret_Name, STR_AWS_Secret_Code, STR_AWS_Path_Folder;

	SharedPreferences myPrefs;
	SharedPreferences.Editor spEditor;
    SharedPreferences sp;

	 Button getgps,wblist,multipup,pup,pod,dex,scd,logout,upload,pickup;
	 Button BTN_Assigment, BTN_Alfadigital, BTN_AlfadigitalDisable;
	 TextView showurl;
    ListView LV_kurir_store;
	 gps_tracker gps;
	 String urlphp, urlnya, username, lat_long,sv;
	 JSONArray version = null;
	 DBAdapter database;
	PosAdapter db,	posdb;
	 WaybillAdapter wbdb;
	 ImageAdapter idb;
	 TransAdapter tsdb;
	 UserAdapter usdb;
//	 SessionManager session;
//	 SessionManager sessionMan;
    compact.mobile.SessionManager sessionMan;
    compact.mobile.config.SessionManager sessionManDetailStore;
	 TimerTask mTimertask;
	 final Handler handler = new Handler();
	 String myimei  ;

	String STR_IDCourier, STR_CodeStore, STR_NameStore, STR_AddressStore, STR_LocStore, STR_Mapi, STR_Webview;
	String STR_BadgeCount, STR_BadgeCountResult, STR_WebviewURLResult;
	TextView TV_BadgeCount, TV_BadgeCountDisable;
	LinearLayout LIN_ListViewBadgeCount;
    private static final String KODE_KURIR = "muse_code";
	private static final String KODE_TOKO = "muse_code_store";
    private static final String NAMA_TOKO = "muse_nama_toko";
    private static final String ALAMAT_TOKO = "muse_alamat_toko";
    private static final String LATLONG_TOKO = "muse_latlong_toko";
    private static final String MAPI = "muse_mapi";
    private static final String WEBVIEW = "muse_webview";
    String[] columnTags = new String[] {KODE_KURIR, NAMA_TOKO, ALAMAT_TOKO, LATLONG_TOKO};
    int[] columnIds = new int[] {R.id.tv_nama_kurir, R.id.tv_nama_toko, R.id.tv_alamat_toko, R.id.tv_latlong_toko};
    ArrayList<HashMap<String, String>> ListDataStore = new ArrayList<HashMap<String, String>>();
	compact.mobile.config.SessionManager sessionDetailStore;

	 Timer t = new Timer();

	 ProgressDialog progressBar;
	FrameLayout fl_progress;
	 int progressBarStatus = 0;
	 Handler progressBarHandler = new Handler();
	 private HostAdapter hostdb;
	 String	HostUrl;
	 @TargetApi(3)
	public void onCreate(Bundle savedInstanceState) {

	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.menu_utama);

		 //Firebase Database
		 mFirebaseInstance = FirebaseDatabase.getInstance();
		 mFirebaseDatabase = mFirebaseInstance.getReference("users_mobile_agent");
		 mFirebaseInstance.getReference("app_title").setValue("User Kurir Firebase Mobile Agent");

	        gps = new gps_tracker(menu_utama.this);

	        String Str_check_connection_awb_others = null;
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
			if (networkinfo != null && networkinfo.isConnected()) {
				// aksi ketika ada koneksi internet
				Str_check_connection_awb_others = "Yes Signal";
				Log.d("Debug", "Koneksi Internet Tersedia");
			} else {
				// aksi ketika tidak ada koneksi internet
				Str_check_connection_awb_others = "No Signal";
				Log.d("Debug", "Koneksi Internet Tidak Tersedia");
			}

	        String Str_lat_long = Double.toString(gps.getLatitude()) + ", "+ Double.toString(gps.getLongitude());
	        Log.d("Debug","LatLong = " + Str_lat_long);

	        Koneksi Str_lo_Koneksi = new Koneksi();
	        Str_LinkUpdate = Str_lo_Koneksi.ConnCekUpdate();
	        STR_Linkpo_outstanding = Str_lo_Koneksi.ConnPOOutstandingScan();
	        STR_Linkfinish_po_outstanding = Str_lo_Koneksi.ConnPOOutstandingClose();
	        STR_Linkads = Str_lo_Koneksi.ConnADSScan();
	        STR_Linkfinish_ads = Str_lo_Koneksi.ConnADSFinishPincode();
	        STR_Linkawb_others = Str_lo_Koneksi.ConnAWBOthersScan();
	        STR_Linkfinish_awb_others = Str_lo_Koneksi.ConnAWBOthersClose();
	        STR_Linkpod = Str_lo_Koneksi.ConnPODScan();
	        STR_Linkdex = Str_lo_Koneksi.ConnDEXScan();
		 	STR_remove_IDFirebase = Str_lo_Koneksi.ConnRemoveIDFirebase();
		 Str_login_user = Str_lo_Koneksi.ConnLogin();
		 Str_LinkAccessKeyAWS = Str_lo_Koneksi.ConnAccessKeyAWS();
		 STR_badgeCount = Str_lo_Koneksi.ConnBadgeCount();

	        Log.d("Debug","Log Path URL"
	    	        + " 1. Str_LinkUpdate "+Str_LinkUpdate
	    	        + " 2. STR_Linkpo_outstanding "+STR_Linkpo_outstanding
	    	        + " 3. STR_Linkfinish_po_outstanding "+STR_Linkfinish_po_outstanding
	    	        + " 4. STR_Linkads "+STR_Linkads
	    	        + " 5. STR_Linkfinish_ads "+STR_Linkfinish_ads
	    	        + " 6. STR_Linkawb_others "+STR_Linkawb_others
	    	        + " 7. STR_Linkfinish_awb_others "+STR_Linkfinish_awb_others
	    	        + " 8. STR_Linkpod "+STR_Linkpod
	    	        + " 9. STR_Linkdex "+STR_Linkdex
					+ " 10. Str_LinkAccessKeyAWS "+Str_LinkAccessKeyAWS);

//	        myPrefs_url_scan_PO = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE);
//	        myPrefs_url_scan_ADS = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE);
//	        myPrefs_url_scan_AWB_Others = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE);
//	        myPrefs_url_scan_POD = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE);
//	        myPrefs_url_scan_DEX = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE);
//	        myPrefs_url_scan_finish_PO = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE);
//	        myPrefs_url_scan_finish_ADS = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE);
//	        myPrefs_url_scan_finish_AWB_Others = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE);
//	        
//	        spEditor_url_scan_PO = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_PO_OUTSTANDING, Context.MODE_PRIVATE).edit();
//	        spEditor_url_scan_ADS = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_ADS, Context.MODE_PRIVATE).edit();
//	        spEditor_url_scan_AWB_Others = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_AWB_OTHERS, Context.MODE_PRIVATE).edit();
//	        spEditor_url_scan_POD = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_POD, Context.MODE_PRIVATE).edit();
//	        spEditor_url_scan_DEX = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_DEX, Context.MODE_PRIVATE).edit();
//	        spEditor_url_scan_finish_PO = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_FINISH_PO_OUTSTANDING, Context.MODE_PRIVATE).edit();
//	        spEditor_url_scan_finish_ADS = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_FINISH_ADS, Context.MODE_PRIVATE).edit();
//	        spEditor_url_scan_finish_AWB_Others = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_FINISH_AWB_OTHERS, Context.MODE_PRIVATE).edit();
//	        
//	        sp_url_scan_PO = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_PO_OUTSTANDING, Context.MODE_PRIVATE);
//	        sp_url_scan_ADS = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_ADS, Context.MODE_PRIVATE);
//	        sp_url_scan_AWB_Others = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_AWB_OTHERS, Context.MODE_PRIVATE);
//	        sp_url_scan_POD = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_POD, Context.MODE_PRIVATE);
//	        sp_url_scan_DEX = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_DEX, Context.MODE_PRIVATE);
//	        sp_url_scan_finish_PO = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_FINISH_PO_OUTSTANDING, Context.MODE_PRIVATE);
//	        sp_url_scan_finish_ADS = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_FINISH_ADS, Context.MODE_PRIVATE);
//	        sp_url_scan_finish_AWB_Others = getApplicationContext().getSharedPreferences(sharedpref.SP_URL_FINISH_AWB_OTHERS, Context.MODE_PRIVATE);
//	        
//	        spEditor_url_scan_PO = myPrefs_url_scan_PO.edit();
//	        spEditor_url_scan_ADS = myPrefs_url_scan_ADS.edit();
//	        spEditor_url_scan_AWB_Others = myPrefs_url_scan_AWB_Others.edit();
//	        spEditor_url_scan_POD = myPrefs_url_scan_POD.edit();
//	        spEditor_url_scan_DEX = myPrefs_url_scan_DEX.edit();
//	        spEditor_url_scan_finish_PO = myPrefs_url_scan_finish_PO.edit();
//	        spEditor_url_scan_finish_ADS = myPrefs_url_scan_finish_ADS.edit();
//	        spEditor_url_scan_finish_AWB_Others = myPrefs_url_scan_finish_AWB_Others.edit();

	        myPrefs = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE);
	        spEditor = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE).edit();
	        sp = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE);
	        spEditor = myPrefs.edit();

	        Log.d("debug","masuk session = " );
	        sessionMan = new SessionManager(getApplicationContext());
		 sessionDetailStore = new compact.mobile.config.SessionManager(getApplicationContext());
   		        Log.d("debug","5" );
   		     TV_version_now = (TextView) findViewById(R.id.txt_version_now);
   		        wblist=(Button)findViewById(R.id.wblist);
   		        pup=(Button)findViewById(R.id.btnpup);
   		      //  pickup=(Button)findViewById(R.id.btnpickup);
   		        pod=(Button)findViewById(R.id.btnpod);
   		        dex=(Button)findViewById(R.id.btndex);
   		        scd=(Button)findViewById(R.id.btnscd);
   		        logout=(Button)findViewById(R.id.btnlogout);
   		        upload=(Button)findViewById(R.id.btnupload);
   		        showurl=(TextView)findViewById(R.id.showurl);
   		     BTN_Assigment=(Button)findViewById(R.id.btnAssignment);
         BTN_Alfadigital=(Button)findViewById(R.id.btnAlfadigital);
   		     //   multipup=(Button)findViewById(R.id.btnpupmulti);
   		      //  getgps = (Button)findViewById(R.id.btngps);
         LV_kurir_store = (ListView) findViewById(R.id.lv_kurir);
		 TV_BadgeCount=(TextView)findViewById(R.id.tv_badge_notification);
		 BTN_AlfadigitalDisable=(Button)findViewById(R.id.btnAlfadigital_disable);
//		 BTN_AlfadigitalDisable.setEnabled(false);
		 TV_BadgeCountDisable=(TextView)findViewById(R.id.tv_badge_notification_disable);
		 fl_progress = (FrameLayout) findViewById(R.id.fl_progress);
		 LIN_ListViewBadgeCount = (LinearLayout) findViewById(R.id.lin_lv);


//   		        getgps.setOnClickListener(new View.OnClickListener() {
//   					public void onClick(View v) {
//   						gps = new gps_tracker(menu_utama.this);
//   						if(gps.canGetLocation()){
//   						
//   							//showgps.setText(Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude()));
//   							String CURRENT_LOCATION = Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude());
//   							Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//   							Uri.parse("http://maps.google.co.id/maps?q="+ CURRENT_LOCATION ));
//   							startActivity(intent);
//   						}
//   						else
//   						{
//   							gps.showSettingsAlert();
//   						}
//   						
//   					}
//   				});
//   		        
   		        wblist.setOnClickListener(new View.OnClickListener() {
   					public void onClick(View v) {
   						 Intent a = new Intent (menu_utama.this,wblist.class);
   						 startActivity(a);
   					}
   				});

//   		        multipup.setOnClickListener(new View.OnClickListener() {
//   					public void onClick(View v) {
//   						 Intent a = new Intent (menu_utama.this,multi_pup.class);
//   						 startActivity(a);
//
//   					}
//   				});


   		        pup.setOnClickListener(new View.OnClickListener() {
   					public void onClick(View v) {
   						 Intent a = new Intent (menu_utama.this,Pickup.class);
   						 startActivity(a);

   					}
   				});


   		        pod.setOnClickListener(new View.OnClickListener() {
   					public void onClick(View v) {
   						 noWaybill obnomor = noWaybill.getInstance();
   					     obnomor.setData("");
   						 Intent a = new Intent (menu_utama.this,cekpod.class);
   						 startActivity(a);

   					}
   				});

//   		        pickup.setOnClickListener(new View.OnClickListener() {
//   					public void onClick(View v) {
//   						 Intent a = new Intent (menu_utama.this,ListPickupBooking.class);
//   						 startActivity(a);
//
//   				}
//   				});
   		        dex.setOnClickListener(new View.OnClickListener() {
   					public void onClick(View v) {
   						 noWaybill obnomor = noWaybill.getInstance();
   					     obnomor.setData("");

   	 					 Intent a = new Intent (menu_utama.this,cekdex.class);
   						 startActivity(a);

   					}
   				});


   		        Log.d("debug","6" );
   		        upload.setOnClickListener(new View.OnClickListener() {
   					public void onClick(View v) {
   						 //upload_gps();
   						 //upload_waybill();
   						upload_all_data_assigment();
//   						 upload_data();				//proses yang lama
   						// tarik_data();
   					}
   				});

   		        scd.setOnClickListener(new View.OnClickListener() {
   					public void onClick(View v) {
   						 Intent a = new Intent (menu_utama.this,Setorcod.class);
   						 startActivity(a);

   					}
   				});

   		     BTN_Assigment.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						 Intent a = new Intent (menu_utama.this,ListAssigment.class);
						 startActivity(a);

					}
				});

		 BTN_Alfadigital.setOnClickListener(new View.OnClickListener() {
			 public void onClick(View v) {
				 Intent a = new Intent (menu_utama.this, AlfadigitalWebViewActivity.class);
				 a.putExtra("code_store", STR_CodeStore);
				 a.putExtra("muse_webview", STR_WebviewURLResult);
				 Log.d("Debug", "Intent From Menu Utama"+" || Code Store = "+STR_CodeStore+" || Webview URL = "+STR_WebviewURLResult);
				 startActivity(a);

			 }
		 });

		 BTN_AlfadigitalDisable.setOnClickListener(new View.OnClickListener() {
			 public void onClick(View v) {
				 Toast.makeText(getApplicationContext(),"Maaf, Oder Kosong", Toast.LENGTH_LONG).show();
			 }
		 });


   		        logout.setOnClickListener(new View.OnClickListener() {
   					public void onClick(View v) {
						sessionMan.logoutUser();
//                        sessionManDetailStore.logoutUser();         //agak bermasalah
//                        sessionMan.logoutUser2();
//                        sessionManDetailStore.logoutUser2();
   			            finish();
						new menu_utama.logoutAsync().execute();
						String Str_IdKurir = username;
						Log.i("Debug","Logout "+"Username = " + Str_IdKurir );
//						if(TextUtils.isEmpty(Str_IdKurir)) {
//							Toast.makeText(getApplicationContext(),"Username tidak boleh kosong", Toast.LENGTH_LONG).show();
//							return;
//						}
//						else {
//							new android.os.Handler().postDelayed(new Runnable() {
//								@Override
//								public void run() {
//									new logoutAsync().execute();
//								}
//							}, 500);
//						}
   					}
   				});
   		        if( sessionMan.isLoggedIn()==false){
					Log.d("Debug","session false " );
   		            Intent a = new Intent(menu_utama.this, MainActivity.class);
   		            startActivity(a);
   		            finish();
   		        }else{


	   		     Log.d("Debug","session true " );
	   		         HashMap<String, String> user = sessionMan.getUserDetails();
	   		        username = user.get(SessionManager.KEY_USER);
	   		        urlphp = user.get(SessionManager.KEY_URL);
	   		        String status = user.get(SessionManager.KEY_STATUS);
	   		     String locpk = user.get(SessionManager.KEY_LOCPK);
					Log.d("Debug","Session Status " + status );

					HashMap<String, String> userDetailStore = sessionDetailStore.getUserDetailsStore();
					STR_IDCourier = userDetailStore.get(sessionDetailStore.KEY_PREF_ID_KURIR);
					STR_CodeStore = userDetailStore.get(sessionDetailStore.KEY_PREF_KODE_TOKO);
					STR_NameStore = userDetailStore.get(sessionDetailStore.KEY_PREF_NAMA_TOKO);
					STR_AddressStore = userDetailStore.get(sessionDetailStore.KEY_PREF_ALAMAT_TOKO);
					STR_LocStore = userDetailStore.get(sessionDetailStore.KEY_PREF_LATLONG_TOKO);
                    STR_Mapi = userDetailStore.get(sessionDetailStore.KEY_PREF_MAPI);
                    STR_Webview = userDetailStore.get(sessionDetailStore.KEY_PREF_WEBVIEW);
					Log.d("Debug","Session Detail Store Courier" + " || " +STR_IDCourier+ " || " +STR_CodeStore+ " || " +STR_NameStore+ " || " +STR_AddressStore+ " || " +STR_LocStore+ " || " +STR_Mapi+ " || " +STR_Webview );

					ListDataStore.clear();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(KODE_KURIR, STR_IDCourier);
					map.put(KODE_TOKO, STR_CodeStore);
					map.put(NAMA_TOKO, STR_NameStore);
					map.put(ALAMAT_TOKO, STR_AddressStore);
					map.put(LATLONG_TOKO, STR_LocStore);
                    map.put(MAPI, STR_Mapi);
                    map.put(WEBVIEW, STR_Webview);
					ListDataStore.add(map);
					Log.d("Debug", "Hashmap Courier Detail Store " + ListDataStore);

					final SimpleAdapter arrayAdapter =
							new SimpleAdapter(this, ListDataStore, R.layout.list_kurir_store,
									columnTags , columnIds);
					LV_kurir_store.setAdapter(arrayAdapter);

					HashMap<String, String> UserIdFirebase = sessionMan.getUserIDFirebaseDetails();
					String Str_IdKurir = UserIdFirebase.get(sessionMan.KEY_ID_KURIR);
					String Str_IdFirebase = UserIdFirebase.get(sessionMan.KEY_ID_FIREBASE);
					SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
					Date date = new Date();
					String Str_date_time = dateformat.format(date);
					Log.d("Debug","Session ID Firebase" + " || ID Kurir : " + Str_IdKurir + " || ID Firebase : " + Str_IdFirebase + " || Date Now : " + Str_date_time);
					SharedPreferences pref = getApplicationContext().getSharedPreferences(ConfigFirebase.SHARED_PREF, 0);
					String regId = pref.getString("regId", null);
					Log.d("Debug","Session ID Firebase Ke-2" + " || ID Kurir : " + username + " || ID Firebase : " + regId);

//					createUser(username, regId, Str_date_time);
//					createUser(Str_IdKurir, Str_IdFirebase);

//					new updateFirebaseUserAsync().execute();

//	   		        cek_versi();
	   		     new CekUpdate().execute();
//	   		        imei imgw = imei.getInstance();
//	   		        String IMEI_Number;
//
////	   		        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
////	   		        IMEI_Number = tm.getDeviceId();
//					TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//					IMEI_Number = tm.getDeviceId().toString();
//
//	   		   	 	if (tm != null)
//	   		   	 		IMEI_Number = tm.getDeviceId();
//	   		   	 	if (IMEI_Number == null || IMEI_Number.length() == 0)
//	   		   	 		IMEI_Number = Build.FINGERPRINT;
//
//	   		        imgw.setData(IMEI_Number);
//
//	   	   	  		//username  = session.getUserDetails();
//	   	   	  		Log.d("debug","username = " + username );
//	   	   	  		Log.d("debug","lockpk = " + locpk );
//
//	   	   	  		imei im = imei.getInstance();
//	   	   	  		myimei  = im.getData();
//	   	   	  		Log.d("debug","imei = " + myimei );

					new AccessAWSS3().execute();

//					if (checkPermissions()) {
//						Log.d("Debug","if checkPermissions"+" || startApplication");
//						startApplication();
////						File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Image Resize Compact");
////
////						if (!mediaStorageDir.exists()) {
////							if (!mediaStorageDir.mkdirs()) {
////								Log.d("=====", "failed to create directory");
////							}
////						}
//
//////						File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Image Compact");
////						File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact" + File.separator);
////						boolean success = true;
////						if (!mediaStorageDir.exists()) {
////							success = mediaStorageDir.mkdirs();
////						}
////						if (success) {
////							// Do something on success
////							Log.d("Debug", "success to create directory");
////						} else {
////							// Do something else on failure
////							Log.d("Debug", "failed to create directory");
////						}
//
////						Boolean hasPermission = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
////						if (!hasPermission) {
////							ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
////                            Log.d("Debug", "If If");
////							boolean success=true;
////							String intStorageDirectory = getFilesDir().toString();
////							Log.d("Debug", "Dir Storage "+intStorageDirectory);
////							File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact" + File.separator);
//////							File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+ "Image Compact");
////							Log.d("Debug", "Media Storage "+mediaStorageDir);
////
////							if (!mediaStorageDir.exists()) {
////								if (!mediaStorageDir.mkdirs()) {
////									Log.d("Debug", "failed to create directory");
////									Log.d("Debug", "1a");
////								} else {
////									Log.d("Debug", "success to create directory");
////									Log.d("Debug", "1b");
////								}
////							}
////
////							if(success){
////								Log.d("Debug", "success to create directory");
////								Log.d("Debug", "1a success");
////							}else{
////								Log.d("Debug", "failed to create directory");
////								Log.d("Debug", "1b failed");
////							}
////						}else {
////                            Log.d("Debug", "If Else");
////							boolean success=true;
////							String intStorageDirectory = getFilesDir().toString();
////							Log.d("Debug", "Dir Storage "+intStorageDirectory);
//////							File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Image Compact");
////							File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact" + File.separator);
//////							File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+ "Image Compact");
////							Log.d("Debug", "Media Storage "+mediaStorageDir);
////
////							if (!mediaStorageDir.exists()) {
////								if (!mediaStorageDir.mkdirs()) {
////									Log.d("Debug", "failed to create directory");
////									Log.d("Debug", "2a");
////								} else {
////									Log.d("Debug", "success to create directory");
////									Log.d("Debug", "2b");
////								}
////							}
////
////							if(success){
////								Log.d("Debug", "success to create directory");
////								Log.d("Debug", "2a success");
////							}else{
////								Log.d("Debug", "failed to create directory");
////								Log.d("Debug", "2b failed");
////							}
////						}
//
////						ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
////						Log.d("Debug", "If");
////						boolean success=true;
////						String intStorageDirectory = getFilesDir().toString();
////						Log.d("Debug", "Dir Storage "+intStorageDirectory);
//////						File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact" + File.separator);
////						File mediaStorageDirFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
////						File mediaStorageDir = new File(mediaStorageDirFolder+ File.separator + "Image Compact" + File.separator);
//////							File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+ "Image Compact");
////						Log.d("Debug", "Media Storage "+mediaStorageDir);
////
////						if (!mediaStorageDir.exists()) {
////							if (!mediaStorageDir.mkdirs()) {
////								Log.d("Debug", "failed to create directory");
////								Log.d("Debug", "1a");
////							} else {
////								Log.d("Debug", "success to create directory");
////								Log.d("Debug", "1b");
////							}
////						}
////
////						if(success){
////							Log.d("Debug", "success to create directory");
////							Log.d("Debug", "1a success");
////						}else{
////							Log.d("Debug", "failed to create directory");
////							Log.d("Debug", "1b failed");
////						}
//
//						boolean success=true;
//						if(success){
//							Log.d("Debug", "success to create directory");
//							Log.d("Debug", "1a success");
//							ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
//							Log.d("Debug", "If");
//							String intStorageDirectory = getFilesDir().toString();
//							Log.d("Debug", "Dir Storage "+intStorageDirectory);
////						File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact" + File.separator);
//							File mediaStorageDirFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//							File mediaStorageDir = new File(mediaStorageDirFolder+ File.separator + "Image Compact" + File.separator);
////							File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+ "Image Compact");
//							Log.d("Debug", "Media Storage "+mediaStorageDir);
//
//							if (!mediaStorageDir.exists()) {
//								if (!mediaStorageDir.mkdirs()) {
//									Log.d("Debug", "failed to create directory");
//									Log.d("Debug", "1a");
//								} else {
//									Log.d("Debug", "success to create directory");
//									Log.d("Debug", "1b");
//								}
//							}
//						}else{
//							Log.d("Debug", "failed to create directory");
//							Log.d("Debug", "1b failed");
//						}
//					} else {
//						Log.d("Debug","else checkPermissions"+" || setPermissions");
//						setPermissions();
////						File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Image Resize Compact");
////
////						if (!mediaStorageDir.exists()) {
////							if (!mediaStorageDir.mkdirs()) {
////								Log.d("=====", "failed to create directory");
////							}
////						}
//
//////						File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Image Compact");
////						File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact" + File.separator);
////						boolean success = true;
////						if (!mediaStorageDir.exists()) {
////							success = mediaStorageDir.mkdirs();
////						}
////						if (success) {
////							// Do something on success
////							Log.d("Debug", "success to create directory");
////						} else {
////							// Do something else on failure
////							Log.d("Debug", "failed to create directory");
////						}
//
////						Boolean hasPermission = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
////						if (!hasPermission) {
////							ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
////                            Log.d("Debug", "Else If");
////							boolean success=true;
////							String intStorageDirectory = getFilesDir().toString();
////							Log.d("Debug", "Dir Storage "+intStorageDirectory);
////							File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact" + File.separator);
//////							File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+ "Image Compact");
////							Log.d("Debug", "Media Storage "+mediaStorageDir);
////
////							if (!mediaStorageDir.exists()) {
////								if (!mediaStorageDir.mkdirs()) {
////									Log.d("Debug", "failed to create directory");
////									Log.d("Debug", "3a");
////								} else {
////									Log.d("Debug", "success to create directory");
////									Log.d("Debug", "3b");
////								}
////							}
////
////							if(success){
////								Log.d("Debug", "success to create directory");
////								Log.d("Debug", "3a success");
////							}else{
////								Log.d("Debug", "failed to create directory");
////								Log.d("Debug", "3b failed");
////							}
////						}else {
////                            Log.d("Debug", "Else Else");
////							boolean success=true;
////							String intStorageDirectory = getFilesDir().toString();
////							Log.d("Debug", "Dir Storage "+intStorageDirectory);
//////							File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Image Compact");
////							File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact" + File.separator);
//////							File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+ "Image Compact");
////							Log.d("Debug", "Media Storage "+mediaStorageDir);
////
////							if (!mediaStorageDir.exists()) {
////								if (!mediaStorageDir.mkdirs()) {
////									Log.d("Debug", "failed to create directory");
////									Log.d("Debug", "4a");
////								} else {
////									Log.d("Debug", "success to create directory");
////									Log.d("Debug", "4b");
////								}
////							}
////
////							if(success){
////								Log.d("Debug", "success to create directory");
////								Log.d("Debug", "4a success");
////							}else{
////								Log.d("Debug", "failed to create directory");
////								Log.d("Debug", "4b failed");
////							}
////						}
//
////						ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
////						Log.d("Debug", "Else");
////						boolean success=true;
////						String intStorageDirectory = getFilesDir().toString();
////						Log.d("Debug", "Dir Storage "+intStorageDirectory);
//////						File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact" + File.separator);
////						File mediaStorageDirFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
////						File mediaStorageDir = new File(mediaStorageDirFolder+ File.separator + "Image Compact" + File.separator);
//////							File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+ "Image Compact");
////						Log.d("Debug", "Media Storage "+mediaStorageDir);
////
////						if (!mediaStorageDir.exists()) {
////							if (!mediaStorageDir.mkdirs()) {
////								Log.d("Debug", "failed to create directory");
////								Log.d("Debug", "2a");
////							} else {
////								Log.d("Debug", "success to create directory");
////								Log.d("Debug", "2b");
////							}
////						}
////
////						if(success){
////							Log.d("Debug", "success to create directory");
////							Log.d("Debug", "2a success");
////						}else{
////							Log.d("Debug", "failed to create directory");
////							Log.d("Debug", "2b failed");
////						}
//
//						boolean success=true;
//						if(success){
//							Log.d("Debug", "success to create directory");
//							Log.d("Debug", "2a success");
//							ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
//							Log.d("Debug", "Else");
//							String intStorageDirectory = getFilesDir().toString();
//							Log.d("Debug", "Dir Storage "+intStorageDirectory);
////						File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact" + File.separator);
//							File mediaStorageDirFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//							File mediaStorageDir = new File(mediaStorageDirFolder+ File.separator + "Image Compact" + File.separator);
////							File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+ "Image Compact");
//							Log.d("Debug", "Media Storage "+mediaStorageDir);
//
//							if (!mediaStorageDir.exists()) {
//								if (!mediaStorageDir.mkdirs()) {
//									Log.d("Debug", "failed to create directory");
//									Log.d("Debug", "2a");
//								} else {
//									Log.d("Debug", "success to create directory");
//									Log.d("Debug", "2b");
//								}
//							}
//						}else{
//							Log.d("Debug", "failed to create directory");
//							Log.d("Debug", "2b failed");
//						}
//					}


	   	   	  		if (urlphp != null){

//	   		 			urlnya = urlphp.replace("/android/", "");
	   		 			Log.d("debug","URL = " + urlnya );

	   		 		}
	   	   	  		showurl.setText(urlnya);
	   	   	  		Log.d("debug","url" +urlphp );
	   	   	  TextView TV_URL=(TextView)findViewById(R.id.showurl);
		   	   TV_URL.setText(urlphp);

//	   	   	  if (urlphp.equals("http://43.252.144.14:81/android/")) {
//					Log.i("Debug", "Halaman Syncronize "+ "Replace URL Lokal Server");
//					Str_urlphp_po = urlphp.replace("http://43.252.144.14:81/android/","http://43.252.144.14:81/compact_mobile");
////				} else if (urlphp.equals("http://api-mobile.atex.co.id/android/")) {
//	   	   } else if (urlphp.equals("http://compact.atex.co.id/android/")) {
//					Log.i("Debug", "Halaman Syncronize "+ "Replace URL Online Server");
////					Str_urlphp_po = urlphp.replace(urlphp,"http://api-mobile.atex.co.id/clear_mobile");
//					Str_urlphp_po = urlphp.replace(urlphp,"http://compact.atex.co.id/compact_mobile");
//				}
//	   	else if (urlphp.equals("http://apiatrex.alfatrex.id/android/")) {
//			Log.i("Debug", "Halaman Syncronize "+ "Replace URL Online Server New Compact Mobile - Second API");
//			Str_urlphp_po = urlphp.replace(urlphp,"http://apiatrex.alfatrex.id/compact_mobile");
//		}
//	   	else if (urlphp.equals("http://api.alfatrex.id/android/")) {
//			Log.i("Debug", "Halaman Syncronize "+ "Replace URL Online Server New Compact Mobile");
//			Str_urlphp_po = urlphp.replace(urlphp,"http://api.alfatrex.id/compact_mobile");
//		}
//				else {
//					Log.i("Debug", "Halaman Syncronize "+ "Server Tidak Tersedia");
//			    	Toast.makeText(getApplicationContext(), "Server Tidak Tersedia",0).show();
//			    }
//
////	   	   Str_urlphp_po = urlphp.replace("http://43.252.144.14:81/android/","http://43.252.144.14:81/clear_mobile");
//	   	   Log.i("Debug", "Halaman Syncronize PO Outstanding " + "Replace URL "+ Str_urlphp_po);
//
//			STR_url_po_outstanding = Str_urlphp_po + "/" +"scan_pup_all";
//			Log.d("Debug","Halaman Syncronize - List PO Outstanding " +"Test URL " + STR_url_po_outstanding);
//
//			STR_url_finish_po_outstanding = Str_urlphp_po + "/" +"closing_po";
//			Log.d("Debug","Halaman Syncronize - Finish List PO Outstanding " +"Test URL " + STR_url_finish_po_outstanding);
//
////			STR_url_ads = Str_urlphp_po + "/" +"scan_awb_all";
//			STR_url_ads = Str_urlphp_po + "/" +"scan_awb_ads";
//			Log.d("Debug","Halaman Syncronize - List ADS " +"Test URL " + STR_url_ads);
//
//			STR_url_finish_ads = Str_urlphp_po + "/" +"closing_ads";
//			Log.d("Debug","Halaman Syncronize - Finish List ADS " +"Test URL " + STR_url_finish_ads);
//
//			STR_url_awb_others = Str_urlphp_po + "/" +"scan_awb_all";
//			Log.d("Debug","Halaman Syncronize - List AWB Others " +"Test URL " + STR_url_awb_others);
//
//			STR_url_finish_awb_others = Str_urlphp_po + "/" +"closing_other";
//			Log.d("Debug","Halaman Syncronize - Finish AWB Others " +"Test URL " + STR_url_finish_awb_others);
//
//			STR_url_pod = Str_urlphp_po + "/" +"scan_pod";
//			Log.d("Debug","Halaman Syncronize - POD " +"Test URL " + STR_url_pod);
//
//			STR_url_dex = Str_urlphp_po + "/" +"scan_dex";
//			Log.d("Debug","Halaman Syncronize - DEX " +"Test URL " + STR_url_dex);
//
//			STR_url_cek_version = Str_urlphp_po + "/" +"cekversi";
//			Log.d("Debug","Halaman Syncronize - Cek Version " +"Test URL " + STR_url_cek_version);
//
//			spEditor.putString(sharedpref.SP_URL_PO_OUTSTANDING, STR_url_po_outstanding);
//			spEditor.commit();
//			spEditor.putString(sharedpref.SP_URL_ADS, STR_url_ads);
//			spEditor.commit();
//			spEditor.putString(sharedpref.SP_URL_AWB_OTHERS, STR_url_awb_others);
//			spEditor.commit();
//			spEditor.putString(sharedpref.SP_URL_POD, STR_url_pod);
//			spEditor.commit();
//			spEditor.putString(sharedpref.SP_URL_DEX, STR_url_dex);
//			spEditor.commit();
//			spEditor.putString(sharedpref.SP_URL_FINISH_PO_OUTSTANDING, STR_url_finish_po_outstanding);
//			spEditor.commit();
//			spEditor.putString(sharedpref.SP_URL_FINISH_ADS, STR_url_finish_ads);
//			spEditor.commit();
//			spEditor.putString(sharedpref.SP_URL_FINISH_AWB_OTHERS, STR_url_finish_awb_others);
//			spEditor.commit();
//			spEditor.putString(sharedpref.SP_URL_CHECK_VERSION, STR_url_cek_version);
//			spEditor.commit();
//			spEditor.putString(sharedpref.SP_URL_ASSIGMENT, Str_urlphp_po);
//			spEditor.commit();
//
//			Str_sp_url_scan_PO = (myPrefs.getString("sp_po_outstanding", ""));
//			Str_sp_url_scan_ADS = (myPrefs.getString("sp_ads", ""));
//			Str_sp_url_scan_AWB_Others = (myPrefs.getString("sp_awb_others", ""));
//			Str_sp_url_scan_POD = (myPrefs.getString("sp_pod", ""));
//			Str_sp_url_scan_DEX = (myPrefs.getString("sp_dex", ""));
//			Str_sp_url_scan_finish_PO = (myPrefs.getString("sp_finish_po_outstanding", ""));
//			Str_sp_url_scan_finish_ADS = (myPrefs.getString("sp_finish_ads", ""));
//			Str_sp_url_scan_finish_AWB_Others = (myPrefs.getString("sp_finish_awb_others", ""));
//			Str_sp_cek_version = (myPrefs.getString("sp_check_version", ""));
//			Str_sp_url_assigment = (myPrefs.getString("sp_url_assigment", ""));
//			Log.d("Debug","Halaman Syncronize - All URL"
//					+" || Scan PO = " + Str_sp_url_scan_PO
//					+" || Scan ADS = " + Str_sp_url_scan_ADS
//					+" || Scan AWB Others = " + Str_sp_url_scan_AWB_Others
//					+" || Scan POD = " + Str_sp_url_scan_POD
//					+" || Scan DEX = " + Str_sp_url_scan_DEX
//					+" || Finish PO = " + Str_sp_url_scan_finish_PO
//					+" || Finish ADS = " + Str_sp_url_scan_finish_ADS
//					+" || Finish AWB Others = " + Str_sp_url_scan_finish_AWB_Others
//					+" || Check Version = " + Str_sp_cek_version
//					+" || URL Assigment = " + Str_sp_url_assigment);

		   	Str_urlphp_po = urlphp;
	        Log.i("Debug", "Test Base URL "+ Str_urlphp_po);

	   		        mTimertask = new TimerTask(){
	   	 	        	  public void run() {
	   		 	        		 runOnUiThread(new Runnable() {
	   		 	        		    public void run() {
	   		 	        		    	Log.d("debug","gps" );
	   		 	        		    	gps_tracker mygps;
	   		 	        		    	mygps = new gps_tracker(menu_utama.this);
	   		 	        		    	lat_long = Double.toString(mygps.getLatitude()) + ", " + Double.toString(mygps.getLongitude());

//	   		 	        		    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//	   		 	        		    // cek GPS status
//	   		 	        		    isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//	   		 	        		    // cek status koneksi
//	   		 	        		    isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//	   		 	        		if (location == null && !isGPSEnable) {
//									Log.d("Debug", "GPS Aktif ");
//								} else if (location == null && !isNetworkEnable) {
//									Log.d("Debug", "Koneksi Internet Aktif ");
//								} else if (location != null) {
//									Log.d("Debug", "GPS Atau Koneksi Internet Tidak Aktif ");
//								} else {
//									Log.d("Debug", "Tidak Ada Jaringan GPS Atau Koneksi Internet");
//								}

	   		 	        		    }
	   		 	        		});

	   		 	        		Log.d("debug","2" );
	   				   			  ArrayList<NameValuePair> parampos = new ArrayList<NameValuePair>();
	   				   			  parampos.add(new BasicNameValuePair("muse_kode", username));
	   				   			  parampos.add(new BasicNameValuePair("lat_long",lat_long));
	   				   			  parampos.add(new BasicNameValuePair("imei",myimei));
	   				   			  parampos.add(new BasicNameValuePair("time",""));
	   				   			  String response = null;
	   				     			try {
	   				     				Log.d("debug","3" );
	   				     				response = CustomHttpClient.executeHttpPost(urlphp +"update_pos.php", parampos);
	   				     				String res = response.toString();
	   				     				res = res.trim();
	   				     				res = res.replaceAll("\\s+","");
	   				     				}
	   				    		 	catch (Exception e) {
										DBAdapter dbListPosisi = new DBAdapter(getApplicationContext());
										List<String> labelsPosisi = dbListPosisi.getPosisi();
										Log.i("Debug", "Cek Array List Posisi " + labelsPosisi);
										boolean retval_finishPincodeADS = labelsPosisi.isEmpty();
										if (retval_finishPincodeADS == true) {
											Log.i("Debug", "Array List Posisi Kosong");

											Log.d("GPS","prepare for GPS -> local" + " || List Kosong");
	   					 	        		Posisi pos = new Posisi();
	   					 	        		Time now = new Time(Time.getCurrentTimezone());
	   					 	        		now.setToNow();
	   					 	        		String sTgl =  Integer.toString(now.year) + "-" + ("00" + Integer.toString(now.month + 1)).substring(("00" + Integer.toString(now.month + 1)).length() - 2) + "-" + ("00" + Integer.toString(now.monthDay)).substring(("00" + Integer.toString(now.monthDay)).length() - 2) + " " +
	   					 	        				("00" + Integer.toString(now.hour)).substring(("00" + Integer.toString(now.hour)).length() - 2)  + ":" + ("00" + Integer.toString(now.minute)).substring(("00" + Integer.toString(now.minute)).length() - 2) + ":" + ("00" + Integer.toString(now.second)).substring(("00" + Integer.toString(now.second)).length() - 2);
	   					 	        		pos.setImei(myimei);
	   					 					pos.setUser(username);
	   					 					pos.setLat_Long(lat_long);
	   					 					pos.setTime(sTgl);
	   					 					pos.setStatus("0");
	   					 					Log.d("GPS","time now : " + sTgl );
	   					 					db = new PosAdapter(menu_utama.this);
	   					 					db.open();
	   					 					db.createPosition(pos);
	   					 					db.close();
	   					 					Log.d("GPS","GPS add to local database ..");
										}else {
											Log.i("Debug", "Array List Posisi Isi");

											Log.d("GPS","prepare for GPS -> local" + " || List Isi");
											Posisi pos = new Posisi();
											Time now = new Time(Time.getCurrentTimezone());
											now.setToNow();
											String sTgl =  Integer.toString(now.year) + "-" + ("00" + Integer.toString(now.month + 1)).substring(("00" + Integer.toString(now.month + 1)).length() - 2) + "-" + ("00" + Integer.toString(now.monthDay)).substring(("00" + Integer.toString(now.monthDay)).length() - 2) + " " +
													("00" + Integer.toString(now.hour)).substring(("00" + Integer.toString(now.hour)).length() - 2)  + ":" + ("00" + Integer.toString(now.minute)).substring(("00" + Integer.toString(now.minute)).length() - 2) + ":" + ("00" + Integer.toString(now.second)).substring(("00" + Integer.toString(now.second)).length() - 2);
											pos.setImei(myimei);
											pos.setUser(username);
											pos.setLat_Long(lat_long);
											pos.setTime(sTgl);
											pos.setStatus("0");
											Log.d("GPS","time now : " + sTgl );
											db = new PosAdapter(menu_utama.this);
											db.open();
											db.updatePosition(pos);
											db.close();
											Log.d("GPS","GPS add to local database ..");
										}
	   				    		 	}
	   	 	        	  }
	   		        };

	   		        Log.d("debug","4" );
	   		        t.schedule(mTimertask,300000,300000);




   		        }

	 }

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("Debug", "BadgeCount onResume");
//		new menu_utama.badgecountAsync().execute();
		if(STR_CodeStore == null || STR_CodeStore == "" || STR_CodeStore.isEmpty()){
			Log.d("Debug", "Kode Store Kosong");
			TV_BadgeCountDisable.setText("0");
//			TV_BadgeCountDisable.setVisibility(View.VISIBLE);
//			BTN_AlfadigitalDisable.setVisibility(View.VISIBLE);
			LIN_ListViewBadgeCount.setVisibility(View.GONE);
			TV_BadgeCount.setVisibility(View.GONE);
			BTN_Alfadigital.setVisibility(View.GONE);
		}else{
			Log.d("Debug", "Kode Store Isi");
			new menu_utama.badgecountAsync().execute();
		}
	}

	public class logoutAsync extends AsyncTask<String, String, String> {

//		ProgressDialog pDialog;
		String Str_IdKurir = username;
		String responsecode, responsemessage;

		protected void onPreExecute() {
//			pDialog.setMessage("Loading...");
//			pDialog.setIndeterminate(false);
//			pDialog.setCancelable(false);
		}

		protected String doInBackground(String... params) {
			HttpURLConnection connection;
			OutputStreamWriter request = null;
			URL url = null;
			String URI = null;
			String response = null;
			String parameters = "kode_kurir="+Str_IdKurir;

			try
			{
				String urlphp;
				urlphp = "";
				Log.d("debug", "Host Server -> " + Str_urlphp_po);
				url = new URL(Str_urlphp_po+STR_remove_IDFirebase);
				Log.d("Debug","Test URL Update Token " + url);

				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestMethod("POST");

				request = new OutputStreamWriter(connection.getOutputStream());
				//Log.d("Debug","request = " + request);
				request.write(parameters);
				request.flush();
				request.close();
				String line = "";

				//Get data from server
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				while ((line = reader.readLine()) != null)
				{
					sb.append(line + "\n");
					sb.append("response_code");
					sb.append("response_message");
					Log.d("Debug","TraceLine = " + line);
				}

				JSONObject jsonObject = new JSONObject(sb.toString());
				responsecode = jsonObject.getString("response_code");
				responsemessage = jsonObject.getString("response_message");

			}
			catch(IOException e)
			{
				Log.d("Debug","Trace = ERROR ");
				Log.d("Debug","response_code  " + responsecode);

			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
//			if (!responsecode.equals("1")) {
//			if (responsecode == "1") {
//				sessionMan.logoutUser();
//				finish();
//				Toast.makeText(getApplicationContext(),responsemessage, Toast.LENGTH_SHORT).show();
//			}
//			else {
//				Intent a = new Intent(menu_utama.this, menu_utama.class);
//				startActivity(a);
//				finish();
//				Toast.makeText(getApplicationContext(),responsemessage, Toast.LENGTH_SHORT).show();
//			}
		}
	}

	public class badgecountAsync extends AsyncTask<String, String, JSONObject> {

		//		ProgressDialog pDialog;
		String Str_IdKurir = username;
		String responsecode, responsemessage;

		protected void onPreExecute() {
			super.onPreExecute();
			fl_progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			STR_BadgeCount = urlphp + STR_badgeCount + STR_CodeStore;
			Log.d("Debug","Halaman Badge Count " + STR_BadgeCount);
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(STR_BadgeCount);
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				String data = EntityUtils.toString(entity);
				Log.e("Debug", "Parsing Json Location " + data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONArray countryListObj = jsonObject.getJSONArray("data");
					String RespnseMessage = jsonObject.getString("response_message");
					String RespnseCode = jsonObject.getString("response_code");
					for (int i = 0; i < countryListObj.length(); i++) {
						JSONObject obj = countryListObj.getJSONObject(i);
						STR_BadgeCountResult = obj.getString("badge_count");
						STR_WebviewURLResult = obj.getString("muse_webview");
						Log.d("Debug", "Hasil Json"+ " || Badget Count " + STR_BadgeCountResult + " || URL Webview " + STR_WebviewURLResult);
					}
//                    String ResponseCount = jsonObject.getString("count");
//                    STR_BadgeCountResult = ResponseCount;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ClientProtocolException e) {
				Log.d("HTTPCLIENT", e.getLocalizedMessage());
			} catch (IOException e) {
				Log.d("HTTPCLIENT", e.getLocalizedMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			Log.d("Debug", "Post Execute");
			TV_BadgeCount = (TextView) findViewById(R.id.tv_badge_notification);
			TV_BadgeCount.setText(STR_BadgeCountResult);
			String strResultBadgeCount = TV_BadgeCount.getText().toString();
			Log.d("Debug", "Badget Count " + strResultBadgeCount);
			fl_progress.setVisibility(View.GONE);
		}
	}

	private void upload_gps(){

			posdb = new PosAdapter(menu_utama.this);
			Log.d("Progress Bar","Prepare..");
			progressBar = new ProgressDialog(this);
			progressBar.setCancelable(true);
			progressBar.setMessage("Upload GPS database ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.setProgress(0);
			progressBar.setMax(100);
			progressBarStatus = 0;
			 if (urlphp.equals("")){
				 Intent a = new Intent (menu_utama.this,SettingHost.class);
				 startActivity(a);
				 return;
			 }

			 Log.d("Progress Bar","opening database");
			 posdb.open();
			 Cursor cur = posdb.getIdlePosition("0");
			 Log.d("Progress Bar","database open");
 			 if (cur != null)  {
 				Log.d("Progress Bar","database ready");
 				cur.moveToFirst();
 				Log.d("Progress Bar","went to first record");
 				 if (cur.getCount() > 0) {
 					Log.d("Progress Bar","data exists");
 					Log.d("Progress Bar","show progressbar");
 					 progressBar.show();
 					 do {

						String muse_kode = cur.getString(cur.getColumnIndexOrThrow(PosAdapter.USER));
						String imei = cur.getString(cur.getColumnIndexOrThrow(PosAdapter.IMEI));
						String waktu = cur.getString(cur.getColumnIndexOrThrow(PosAdapter.TIME));
						String latlong = cur.getString(cur.getColumnIndexOrThrow(PosAdapter.LAT_LONG));
						//String status = cur.getString(cur.getColumnIndexOrThrow(PosAdapter.STATUS));

						ArrayList<NameValuePair> parampos = new ArrayList<NameValuePair>();
						parampos.add(new BasicNameValuePair("muse_kode", muse_kode));
						parampos.add(new BasicNameValuePair("lat_long",latlong));
						parampos.add(new BasicNameValuePair("imei",imei));
						parampos.add(new BasicNameValuePair("time",waktu));

						String response = null;
						try {

							response = CustomHttpClient.executeHttpPost(urlphp +"update_pos.php", parampos);
							String res = response.toString();
							res = res.trim();
							Log.d("WAYBILL","Terkirim ke server =>" + res);
							res = res.replaceAll("\\s+","");

							Posisi pos = new Posisi();
		 	        		pos.setImei(imei);
		 					pos.setUser(muse_kode);
		 					pos.setLat_Long(latlong);
		 					pos.setTime(waktu);
		 					pos.setStatus("1");
		 					Log.d("GPS","update status, imei = " + imei + " waktu = " + waktu);
		 					posdb.updateStatus(pos);
							}
					 	catch (Exception e) {
					 		Toast.makeText(this.getBaseContext(), "Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
					 		Log.d("GPS","Connectioan Fail");
					 		break;
					 	}

					} while (cur.moveToNext());
 					Log.d("Progress Bar","dismiss");
 					progressBar.dismiss();
				}
 				 else
 				 {
 					Log.d("Progress Bar","data is null");
 					Toast.makeText(this.getBaseContext(), "Data GPS is null ...", Toast.LENGTH_SHORT).show();
 				 }
			 }
 			posdb.close();
	 }

	private void upload_waybill(){

		wbdb = new WaybillAdapter(menu_utama.this);
		Log.d("Progress Bar","Prepare..");
		progressBar = new ProgressDialog(this);
		progressBar.setCancelable(true);
		progressBar.setMessage("Upload transaction database ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBarStatus = 0;
		 if (urlphp.equals("")){
			 Intent a = new Intent (menu_utama.this,SettingHost.class);
			 startActivity(a);
			 return;
		 }

		 Log.d("Progress Bar","opening database");
		 wbdb.open();
		 Cursor cur = wbdb.getIdleWaybill("0");
		 Log.d("Progress Bar","database open");
			 if (cur != null)  {
				Log.d("Progress Bar","database ready");
				cur.moveToFirst();
				Log.d("Progress Bar","went to first record");
				 if (cur.getCount() > 0) {
					Log.d("Progress Bar","data exists");
					Log.d("Progress Bar","show progressbar");
					 progressBar.show();
					 do {

					String waybill = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.WAYBILL));
					String penerima = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.PENERIMA));
					String kota = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.KOTA));
					String tujuan = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.TUJUAN));
					String mswb_pk = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.MSWB_PK));
					String locpk = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.LOCPK));
					String username = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.USERNAME));
					String tiperem = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.TIPEREM));
					String telp = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.TELP));
					String tipe = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.TIPE));
					String keterangan = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.KETERANGAN));
					String lat_long = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.LAT_LONG));
					String waktu = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.WAKTU));
					String po = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.PO));
					//String status = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.STATUS));

					 ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
		       			masukparam1.add(new BasicNameValuePair("waybill", waybill));
		       			masukparam1.add(new BasicNameValuePair("penerima", penerima));
		       			masukparam1.add(new BasicNameValuePair("telp",telp));
		       			masukparam1.add(new BasicNameValuePair("kota", kota));
		       			masukparam1.add(new BasicNameValuePair("tujuan", tujuan));
		       			masukparam1.add(new BasicNameValuePair("mswb_pk", mswb_pk));
		       			masukparam1.add(new BasicNameValuePair("locpk", locpk));
		       			masukparam1.add(new BasicNameValuePair("username", username));
		       			masukparam1.add(new BasicNameValuePair("tiperem", tiperem));
		       			masukparam1.add(new BasicNameValuePair("tipe", tipe));
		       			masukparam1.add(new BasicNameValuePair("keterangan", keterangan));
		       			masukparam1.add(new BasicNameValuePair("lat_long",lat_long));
		       			masukparam1.add(new BasicNameValuePair("waktu",waktu));
		       			masukparam1.add(new BasicNameValuePair("po",po));
		       			String response1 = null;
		       			try {
		       				response1 = CustomHttpClient.executeHttpPost(urlphp +   "update.php", masukparam1);
		       				String res1 = response1.toString();
		       				res1 = res1.trim();
		       				Log.d("WAYBILL","Terkirim ke server =>" + res1);
		       				res1 = res1.replaceAll("\\s+","");
		       				Log.d("WAYBILL","Terkirim ke server");

		       				Waybill wb = new Waybill();
		       				wb.setWaybill(waybill);
		       				wb.setPenerima(penerima);
		       				wb.setKota(kota);
		       				wb.setTujuan(tujuan);
		       				wb.setMswb_pk(mswb_pk);
		       				wb.setLocpk(locpk);
		       				wb.setUser(username);
		       				wb.setTiperem(tiperem);
		       				wb.setTelp(telp);
		       				wb.setTipe(tipe);
		       				wb.setKeterangan(keterangan);
		       				wb.setLat_Long(lat_long);
		       				wb.setWaktu(waktu);
		       				wb.setStatus("1");

		       				Log.d("WAYBILL","update status, waybill = " + waybill + " waktu = " + waktu);
		 					wbdb.updateContact(wb);

		       				}
		      		 	catch (Exception e) {
		      		 			//Toast.makeText(theButton.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
		      		 			Toast.makeText(this.getBaseContext(), "Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
		      		 			Log.d("WAYBILL","Connectioan Fail");
		      		 			break;
		      		 		}

				} while (cur.moveToNext());
					 Log.d("Progress Bar","dismiss");
	 					progressBar.dismiss();
			}
				 else
				 {
					Log.d("Progress Bar","data is null");
					Toast.makeText(this.getBaseContext(), "Data Waybill is null ...", Toast.LENGTH_SHORT).show();
				 }
		 }
			wbdb.close();
 }

	private void upload_data(){

		posdb = new PosAdapter(menu_utama.this);
		Log.d("Progress Bar","Prepare data user ..");
		progressBar = new ProgressDialog(this);
		progressBar.setCancelable(true);
		progressBar.setMessage("Upload Data Local");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		Log.d("Progress Bar","Url .."+urlphp);
		 if (urlphp.equals("")){
			 Intent a = new Intent (menu_utama.this,SettingHost.class);
			 startActivity(a);
			 return;
		 }

		 Log.d("Progress Bar","opening database user ");
		 posdb.open();
		 Cursor cur = posdb.getIdlePosition("0");
		 Log.d("Progress Bar","database user open");
			 if (cur != null)  {
				Log.d("Progress Bar","database user ready");
				cur.moveToFirst();
				Log.d("Progress Bar","went to first record user");
				 if (cur.getCount() > 0) {
					Log.d("Progress Bar","data exists");
					Log.d("Progress Bar","show progressbar");
					 progressBar.show();
					 do {

					String muse_kode = cur.getString(cur.getColumnIndexOrThrow(PosAdapter.USER));
					String imei = cur.getString(cur.getColumnIndexOrThrow(PosAdapter.IMEI));
					String waktu = cur.getString(cur.getColumnIndexOrThrow(PosAdapter.TIME));
					String latlong = cur.getString(cur.getColumnIndexOrThrow(PosAdapter.LAT_LONG));
					//String status = cur.getString(cur.getColumnIndexOrThrow(PosAdapter.STATUS));

					ArrayList<NameValuePair> parampos = new ArrayList<NameValuePair>();
					parampos.add(new BasicNameValuePair("muse_kode", muse_kode));
					parampos.add(new BasicNameValuePair("lat_long",latlong));
					parampos.add(new BasicNameValuePair("imei",imei));
					parampos.add(new BasicNameValuePair("time",waktu));

					String response = null;
					try {

						response = CustomHttpClient.executeHttpPost(urlphp +"update_pos.php", parampos);
						String res = response.toString();
						res = res.trim();
						Log.d("WAYBILL","data user Terkirim ke server =>" + res);
						res = res.replaceAll("\\s+","");

						Posisi pos = new Posisi();
	 	        		pos.setImei(imei);
	 					pos.setUser(muse_kode);
	 					pos.setLat_Long(latlong);
	 					pos.setTime(waktu);
	 					pos.setStatus("1");
	 					Log.d("GPS","update status, imei = " + imei + " waktu = " + waktu);
	 					posdb.updateStatus(pos);
						}
				 	catch (Exception e) {
				 		Toast.makeText(this.getBaseContext(), "Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
				 		Log.d("GPS","Connectioan Fail");
				 		break;
				 	}

				} while (cur.moveToNext());
					Log.d("Progress Bar","dismiss");
					progressBar.dismiss();
			}
				 else
				 {
					Log.d("Progress Bar","data is null");
					Toast.makeText(this.getBaseContext(), "Data GPS is null ...", Toast.LENGTH_SHORT).show();
				 }
		 }
		posdb.close();

		wbdb = new WaybillAdapter(menu_utama.this);
		Log.d("Progress Bar","Prepare data waybill..");

		Log.d("Progress Bar","opening database waybill");
		 wbdb.open();
		 Cursor dcur = wbdb.getIdleWaybill("0");
		 Log.d("Progress Bar","database waybill open");
			 if (dcur != null)  {
				Log.d("Progress Bar","database waybill ready");
				dcur.moveToFirst();
				Log.d("Progress Bar","went to first record waybill");
				 if (dcur.getCount() > 0) {
					Log.d("Progress Bar","data exists");
					Log.d("Progress Bar","show progressbar");
					 progressBar.show();
					 do {

					String waybill = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.WAYBILL));
					String penerima = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.PENERIMA));
					String kota = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.KOTA));
					String tujuan = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.TUJUAN));
					String mswb_pk = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.MSWB_PK));
					String locpk = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.LOCPK));
					String username = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.USERNAME));
					String tiperem = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.TIPEREM));
					String telp = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.TELP));
					String tipe = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.TIPE));
					String keterangan = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.KETERANGAN));
					String lat_long = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.LAT_LONG));
					String waktu = dcur.getString(dcur.getColumnIndexOrThrow(WaybillAdapter.WAKTU));
					//String status = cur.getString(cur.getColumnIndexOrThrow(WaybillAdapter.STATUS));

					 ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
		       			masukparam1.add(new BasicNameValuePair("waybill", waybill));
		       			masukparam1.add(new BasicNameValuePair("penerima", penerima));
		       			masukparam1.add(new BasicNameValuePair("telp",telp));
		       			masukparam1.add(new BasicNameValuePair("kota", kota));
		       			masukparam1.add(new BasicNameValuePair("tujuan", tujuan));
		       			masukparam1.add(new BasicNameValuePair("mswb_pk", mswb_pk));
		       			masukparam1.add(new BasicNameValuePair("locpk", locpk));
		       			masukparam1.add(new BasicNameValuePair("username", username));
		       			masukparam1.add(new BasicNameValuePair("tiperem", tiperem));
		       			masukparam1.add(new BasicNameValuePair("tipe", tipe));
		       			masukparam1.add(new BasicNameValuePair("keterangan", keterangan));
		       			masukparam1.add(new BasicNameValuePair("lat_long",lat_long));
		       			masukparam1.add(new BasicNameValuePair("waktu",waktu));
		       			String response1 = null;
		       			Log.d("cek parameter", "waybill =>" + waybill + ", penerima => " + penerima + ", kota => " + kota +
	        					"tujuan => "+tujuan + ", mswb_pk => " +mswb_pk + ",locpk => "+locpk + ",username =>" + username +", tiperem => "+tiperem + ", telp => " + telp
	        					+ ",tipe => 7 , keterangan => " + keterangan + ",lat_long => " +lat_long +
	        					", waktu=>  ");
		       			try {
		       				response1 = CustomHttpClient.executeHttpPost(urlphp +   "update.php", masukparam1);
		       				String res1 = response1.toString();
		       				res1 = res1.trim();
		       				Log.d("WAYBILL","Terkirim ke server =>" + res1);
		       				res1 = res1.replaceAll("\\s+","");
		       				Log.d("WAYBILL","Terkirim ke server");

		       				Waybill wb = new Waybill();
		       				wb.setWaybill(waybill);
		       				wb.setPenerima(penerima);
		       				wb.setKota(kota);
		       				wb.setTujuan(tujuan);
		       				wb.setMswb_pk(mswb_pk);
		       				wb.setLocpk(locpk);
		       				wb.setUser(username);
		       				wb.setTiperem(tiperem);
		       				wb.setTelp(telp);
		       				wb.setTipe(tipe);
		       				wb.setKeterangan(keterangan);
		       				wb.setLat_Long(lat_long);
		       				wb.setWaktu(waktu);
		       				wb.setStatus("1");

		       				Log.d("WAYBILL","update status, waybill = " + waybill + " waktu = " + waktu);
		 					wbdb.updateContact(wb);

		       				}
		      		 	catch (Exception e) {
		      		 			//Toast.makeText(theButton.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
		      		 			Toast.makeText(this.getBaseContext(), "Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
		      		 			Log.d("WAYBILL","Connectioan Fail");
		      		 			break;
		      		 		}

				} while (dcur.moveToNext());
					 Log.d("Progress Bar","dismiss");
	 					progressBar.dismiss();
			}
				 else
				 {
					Log.d("Progress Bar","data is null");
					Toast.makeText(this.getBaseContext(), "Data Waybill is null ...", Toast.LENGTH_SHORT).show();
				 }
		 }
			wbdb.close();
		//akhir waybill

		// ambil data signature & photo

			idb = new ImageAdapter(menu_utama.this);
			Log.d("Progress Bar","opening database user ");
			 idb.open();
			 Cursor icur = idb.getIdlePosition("0");
			 Log.d("Progress Bar","database image open");
				 if (icur != null)  {
					Log.d("Progress Bar","database image ready");
					icur.moveToFirst();
					Log.d("Progress Bar","went to first record user");
					 if (icur.getCount() > 0) {
						Log.d("Progress Bar","data exists");
						Log.d("Progress Bar","show progressbar");
						 progressBar.show();
						 do {
							String name = icur.getString(icur.getColumnIndexOrThrow(ImageAdapter.NAME));
							String image = icur.getString(icur.getColumnIndexOrThrow(ImageAdapter.IMAGE));
							String penerima = icur.getString(icur.getColumnIndexOrThrow(ImageAdapter.PENERIMA));
							//String status = cur.getString(cur.getColumnIndexOrThrow(PosAdapter.STATUS));
							Log.d("id", name);
							ArrayList<NameValuePair> iparampos = new ArrayList<NameValuePair>();
							iparampos.add(new BasicNameValuePair("image",image));
							iparampos.add(new BasicNameValuePair("name",name));
							iparampos.add(new BasicNameValuePair("penerima",penerima));

					        String response = null;
					        try{
					            response = CustomHttpClient.executeHttpPost(urlphp + "upload_image.php", iparampos);
									String res = response.toString();
									res = res.trim();
									res = res.replaceAll("\\s+","");
									//String the_string_response = convertResponseToString(response);
					            //Toast.makeText(menu_utama.this, "Response " + res, Toast.LENGTH_LONG).show();
									gambar gbr = new gambar();
				 	        		gbr.setImage(image);
				 					gbr.setName(name);
				 					gbr.setPenerima(penerima);
				 					gbr.setStatus("1");
				 					Log.d("IMAGE","update status, name = " + name + " penerima = " + penerima);
				 					idb.updateStatus(gbr);
					        }catch(Exception e){
					              Toast.makeText(menu_utama.this, "Fail to connect server, using local -> " +e.toString() + " -> " + e.getMessage(), Toast.LENGTH_LONG).show();
					              Log.d("IMAGE","Connectioan Fail");
			      		 			break;
					        }

						} while (cur.moveToNext());
							Log.d("Progress Bar","dismiss");
							progressBar.dismiss();
					}
					 else
					 {
						Log.d("Progress Bar","data is null");
						Toast.makeText(this.getBaseContext(), "Data Image is null ...", Toast.LENGTH_SHORT).show();
					 }
			 }
			idb.close();

	}

	public void cek_versi(){
		JSONParser jParser = new JSONParser();
		String versinya = "sc-1.0.5";

	       Log.d("cek versi", versinya);
	       Log.d("cek url", urlphp);
	       try {

				JSONObject json = jParser.getJSONFromUrl(urlphp + "cek_versi.php");
				version = json.getJSONArray("version");

				for(int i = 0; i < version.length(); i++){
					JSONObject ar = version.getJSONObject(i);
					String versi = ar.getString("versi");
					String status = ar.getString("status");

					if (versinya.equals(versi)) {

						sv = "ok";

					} else {

						if (status.equals("Y")){

							sv = "y";

						} else {

							sv = "n";

						}

					}

				}
				if (sv.equals("y")){
					versionY();
				}else if (sv.equals("n")){
					versionN();
				}else{
					versionOK();
				}
			} catch (Exception e) {

			}
	}

	private class CekUpdate extends AsyncTask<String, String, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			Log.i("Debug", "Lewatin Cek Update ");
			JSONParser jParser = new JSONParser();
//			String versinya = "sc-1.0.5";

			HashMap<String, String> user = sessionMan.getUserDetails();
			urlphp = user.get(SessionManager.KEY_URL);
			Log.i("Debug", "URL doInBackground " + urlphp);

//			if (urlphp.equals("http://43.252.144.14:81/android/")) {
//				Log.i("Debug", "Halaman Syncronize "
//						+ "Replace URL Lokal Server");
//				Str_url_po = urlphp.replace(
//						"http://43.252.144.14:81/android/",
//						"http://43.252.144.14:81/clear_mobile");
//			} else if (urlphp.equals("http://api-mobile.atex.co.id/android/")) {
//				Log.i("Debug", "Halaman Syncronize "
//						+ "Replace URL Online Server");
//				Str_url_po = urlphp.replace(urlphp,
//						"http://api-mobile.atex.co.id/clear_mobile");
//			}
//			else if (urlphp.equals("http://apiatrex.alfatrex.id/android/")) {
//				Log.i("Debug", "Halaman Syncronize "
//						+ "Replace URL Online Server New Compact Mobile - Second API");
//				Str_url_po = urlphp.replace(urlphp,
//						"http://apiatrex.alfatrex.id/compact_mobile");
//			}
//			else if (urlphp.equals("http://api.alfatrex.id/android/")) {
//				Log.i("Debug", "Halaman Syncronize "
//						+ "Replace URL Online Server New Compact Mobile");
//				Str_url_po = urlphp.replace(urlphp,
//						"http://api.alfatrex.id/compact_mobile");
//			}
//			else {
//				Log.i("Debug", "Halaman Syncronize " + "Server Tidak Tersedia");
//				Toast.makeText(getApplicationContext(),
//						"Server Tidak Tersedia", 0).show();
//			}

//			Str_url_cekupdate = Str_url_po + "/" +"cekversi";
//			String strurl = Str_url_cekupdate;
//			Log.i("Debug", "Test URL " + strurl);
//			DefaultHttpClient client = new DefaultHttpClient();
//			HttpGet request = new HttpGet(strurl);
			Str_url_cekupdate = urlphp + Str_LinkUpdate;
			Log.d("Debug","Halaman Syncronize - Cek Version " +"Test URL " + Str_url_cekupdate);

			try {
				String strurl = Str_url_cekupdate;
				Log.i("Debug", "Test URL " + strurl);
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(strurl);
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				String data = EntityUtils.toString(entity);
				Log.e("Debug", "Parsing Json Location " + data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONArray countryListObj = jsonObject.getJSONArray("data");
					String RespnseMessage = jsonObject.getString("response_message");
					String RespnseCode = jsonObject.getString("response_code");
					for (int i = 0; i < countryListObj.length(); i++) {
						JSONObject obj = countryListObj.getJSONObject(i);
						STR_version = obj.getString("versi");
						STR_status = obj.getString("status");
						Log.d("Debug", "Hasil Json"
								+ " || Version " + STR_version
								+ " || Url " + STR_status);

//						if (versinya.equals(versi)) {
//							sv = "ok";
//						} else {
//							if (status.equals("Y")){
//								sv = "y";
//							} else {
//								sv = "n";
//							}
//						}

//						TV_version = (TextView) findViewById(R.id.txt_version);
//						TV_version.setText(versi);
//						Str_version = TV_version.getText().toString();
//						TV_status = (TextView) findViewById(R.id.txt_status);
//						TV_status.setText(status);
//						Str_status = TV_status.getText().toString();
//						Log.d("Debug", "String"
//						+" Version " + Str_version
//						+" Status " + Str_status);
					}
//					if (sv.equals("y")){
//						versionY();
//					}else if (sv.equals("n")){
//						versionN();
//					}else{
//						versionOK();
//					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ClientProtocolException e) {
				Log.d("HTTPCLIENT", e.getLocalizedMessage());
			} catch (IOException e) {
				Log.d("HTTPCLIENT", e.getLocalizedMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			Log.d("Debug", "1.Test Sampai Sini ");
			TV_version = (TextView) findViewById(R.id.txt_version);
			TV_version.setText(STR_version);
			Str_version2 = TV_version.getText().toString();
			TV_status = (TextView) findViewById(R.id.txt_status);
			TV_status.setText(STR_status);
			Str_status2 = TV_status.getText().toString();
			Log.d("Debug", "String"
					+" Version " + Str_version2
					+" Status " + Str_status2);

			String Version = sv;
			Log.d("Debug", "Isi Version " + Version);

			String versinya = "sc-1.2.3";
//			Log.d("Debug", "Version " + versinya);
//			TV_version = (TextView) findViewById(R.id.txt_version);
//			Str_version = TV_version.getText().toString();
			TV_version_now = (TextView) findViewById(R.id.txt_version_now);
			TV_version_now.setText(Str_version2);
			TV_status = (TextView) findViewById(R.id.txt_status);
//			Str_status = TV_status.getText().toString();
			Log.d("Debug", "Post Execute" +" String"
			+" Version " + Str_version2
			+" Status " + Str_status2);

			if (versinya.equals(Str_version2)) {
				sv = "ok";
			} else {
				if (Str_status2.equals("Y")) {
					sv = "y";
				} else {
					sv = "n";
				}
			}

			if (sv.equals("y")){
				versionY();
			}else if (sv.equals("n")){
				versionN();
			}else{
				versionOK();
			}

		}
	}

	private class AccessAWSS3 extends AsyncTask<String, String, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			Log.i("Debug", "Lewatin Access AWS S3 ");
			JSONParser jParser = new JSONParser();

			HashMap<String, String> user = sessionMan.getUserDetails();
			urlphp = user.get(SessionManager.KEY_URL);
			Log.i("Debug", "URL doInBackground " + urlphp);
			Str_UrlAccessKeyAWS = urlphp + Str_LinkAccessKeyAWS;
			Log.d("Debug","URL - GET AWS S3 Access Key - " +"Test URL " + Str_UrlAccessKeyAWS);

			try {
				String strurl = Str_UrlAccessKeyAWS;
				Log.i("Debug", "Test URL " + strurl);
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(strurl);
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				String data = EntityUtils.toString(entity);
				Log.e("Debug", "Parsing Json Location " + data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONArray countryListObj = jsonObject.getJSONArray("data");
					String RespnseMessage = jsonObject.getString("response_message");
					String RespnseCode = jsonObject.getString("response_code");
					for (int i = 0; i < countryListObj.length(); i++) {
						JSONObject obj = countryListObj.getJSONObject(i);
//						STR_AWS_Key_Name = obj.getString("aws_key_name");
						STR_AWS_Key_Code = obj.getString("aws_key_code");
//						STR_AWS_Secret_Name = obj.getString("aws_secret_name");
						STR_AWS_Secret_Code = obj.getString("aws_secret_code");
						STR_AWS_Path_Folder = obj.getString("aws_path_folder");
						Log.d("Debug", "Hasil Json" + " || AWS Key Code " + STR_AWS_Key_Code + " || AWS Secret Code " + STR_AWS_Secret_Code+ " || AWS Path Folder " + STR_AWS_Path_Folder);

						sessionMan.createAWSSession(STR_AWS_Key_Code,STR_AWS_Secret_Code,STR_AWS_Path_Folder);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					toastOnUiThread("Can't create AWS Session");
				}
			} catch (ClientProtocolException e) {
				Log.d("HTTPCLIENT", e.getLocalizedMessage());
				toastOnUiThread("Can't create AWS Session");

			} catch (IOException e) {
				Log.d("HTTPCLIENT", e.getLocalizedMessage());
				toastOnUiThread("Can't create AWS Session");
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			Log.d("Debug", "1.Test Sampai Sini ");
		}
	}

	private void toastOnUiThread(final String message) {
		runOnUiThread(new Runnable() {
			@Override public void run() {
				Toast.makeText(menu_utama.this, message, Toast.LENGTH_LONG).show();
			}
		});
	}

	public void versionOK(){

	}

	public void versionY() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
		        menu_utama.this);

		alertDialog.setCancelable(false);

		// Setting Dialog Title
		alertDialog.setTitle("Update Versi");

		// Setting Dialog Message
		alertDialog.setMessage("Versi terbaru aplikasi tersedia");

		// Setting Icon to Dialog
		//alertDialog.setIcon(R.drawable.delete);

		// Setting Positive
		alertDialog.setPositiveButton("Download",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	// TODO Auto-generated method stub

		            //	Intent myIntent = new Intent();
					//	myIntent.setAction(Intent.ACTION_VIEW);
					//	myIntent.addCategory(Intent.CATEGORY_BROWSABLE);
					 //	myIntent.setData(Uri.parse("http://www.atex.co.id/downloads/SimpleClear.apk"));

						//startActivityForResult(myIntent, 0);


////						Uri webpage = Uri.parse("http://www.atex.co.id/downloads/SimpleClear.apk");
////		            	Uri webpage = Uri.parse("http://43.252.144.14:81/compact_mobile/CompactClear.apk");
//		            	Uri webpage = Uri.parse(urlphp+"/apk_compact/CompactClear.apk");
//		            	Log.d("Debug", "URL Download Lokal " + webpage);
//					    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//					    if (intent.resolveActivity(getPackageManager()) != null) {
//					        startActivity(intent);
//					    }

						final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
						if (intent.resolveActivity(getPackageManager()) != null) {
							startActivity(intent);
						}


		            	dialog.dismiss();
		            	finish();
		            }
		        });

		alertDialog.show();

	}

	public void versionN(){

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
		        menu_utama.this);

		alertDialog.setCancelable(false);

		// Setting Dialog Title
		alertDialog.setTitle("Update Versi");

		// Setting Dialog Message
		alertDialog.setMessage("Versi terbaru aplikasi tersedia. Download aplikasi ?");

		// Setting Icon to Dialog
		//alertDialog.setIcon(R.drawable.delete);

		// Setting Positive
		alertDialog.setPositiveButton("Download",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	// TODO Auto-generated method stub

		            	//Intent myIntent = new Intent();
					//	myIntent.setAction(Intent.ACTION_VIEW);
					//	myIntent.addCategory(Intent.CATEGORY_BROWSABLE);
					 	//myIntent.setData(Uri.parse("http://www.atex.co.id/downloads/SimpleClear.apk"));

						//startActivityForResult(myIntent, 0);

//						 Uri webpage = Uri.parse("http://www.atex.co.id/downloads/SimpleClear.apk");
		            	Uri webpage = Uri.parse(urlphp+"/apk_compact/CompactClear.apk");
		            	Log.d("Debug", "URL Download Productions " + webpage);
						    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
						    if (intent.resolveActivity(getPackageManager()) != null) {
						        startActivity(intent);
						    }


		            	dialog.dismiss();
		            	finish();
		            }
		        });



		alertDialog.show();

	}
	/*
	@TargetApi(3)
	
	 private void tarik_data(){
		 Log.d("Progress Bar","Prepare.."); 
			progressBar = new ProgressDialog(this);
			progressBar.setCancelable(true);
			progressBar.setMessage("Downloading database ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			 if (urlphp.equals("")){
				 Intent a = new Intent (menu_utama.this,SettingHost.class);
				 startActivity(a);
				 return;
			 }
			 
			usdb = new  UserAdapter(menu_utama.this); 
			usdb.open();
			 //	ambil data user
			ArrayList<NameValuePair> aParameters = new ArrayList<NameValuePair>();
			String respon = null;
		 	try {
		 			Log.d("Progress Bar","getting user data");
		 			respon = CustomHttpClient.executeHttpPost(urlphp + "get_data_user.php",aParameters);
		 			Log.e("hasil respon", respon);
		 			if (respon == null) {
		 				Log.e("hasil respon", "data tidak ada");
		 				Toast.makeText(this.getBaseContext(), "Data user tidak ada !!", Toast.LENGTH_SHORT).show();
		     			//return;
		 			}
		 			else
		 			{	
			 			usdb.deleteAllContact();
			 			Log.d("Progress Bar","prepare insert data user");
			 			try
			 			{
			 				Log.d("Progress Bar","show progressbar");
			 				progressBar.show();
			 				final String res = respon;
			 				new Thread(new Runnable() {
			 					  public void run() {
			 						  JSONObject json= null;
			 						  JSONArray JA = null;
					 		 		  try {
											JA = new JSONArray(res);
											double j = JA.length();
											for(int i=0;i<j;i++)
									    	{
												json=JA.getJSONObject(i);
												UserKurirFirebase usr = new UserKurirFirebase();
								    			usr.setUsername(json.getString("kode"));
								    			usr.setPassword(json.getString("passwd"));
								    			usr.setLocation(json.getString("lokasi"));
								    			usdb.createContact(usr);
								    			Log.e("input data", json.getString("kode") + ", " + json.getString("passwd") + "," + json.getString("lokasi"));
								    			progressBarStatus = (int) ((i/j)*100);
								    			Log.e("Progress bar","status -> (" + i +" / " + j + ") * 100 = "+ (int) ((i/j)*100) +" = " +progressBarStatus ); 
								    		}
											Log.d("Progress Bar","dismiss");
						 					progressBar.dismiss();
									   } catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
									   }	  
					 		 		   usdb.close();
					 		 		   Log.e("input data", "Successfull !!");
			 				  }	
				    		}).start();
			 				
			 			}
			 			catch(Exception e)
			 			{
	
			 			Log.e("Fail 3", e.toString());
			 			Toast.makeText(this.getBaseContext(), "Error = " + e.toString(), Toast.LENGTH_SHORT).show();
			 			usdb.close();
			 			}
		 			}
		 	}
			catch (Exception e) {
			 			Log.e("Fail 4", e.toString());
			 			Toast.makeText(this.getBaseContext(), "Error = " + e.toString(), Toast.LENGTH_SHORT).show();
			 			
			 			usdb.close();
			}
		 		
		 	//end ambil data user
		 	
		   //ambil data waybill
			tsdb = new  TransAdapter(menu_utama.this); 
			tsdb.open();
		 		Log.d("Progress Bar","Near launch");
				 
			   	 long yourDateMillis = System.currentTimeMillis() - (14 * 24 * 60 * 60 * 1000);
			   	 Time yourDate = new Time();
			   	 yourDate.set(yourDateMillis);
			   	 String sTgl = yourDate.format("%Y-%m-%d");
			   	 Log.d("Tanggal","set date to : " + sTgl);
			   	 
				 ArrayList<NameValuePair> newParameters = new ArrayList<NameValuePair>();
				 newParameters.add(new BasicNameValuePair("tgl", sTgl));
				    respon = null;
			 		try {
			 			Log.d("Progress Bar","get data waybill");
			 			respon  = CustomHttpClient.executeHttpPost(urlphp + "get_data.php",newParameters);
			 			Log.e("hasil respon", respon );
			 			if (respon  == null) {
			 				Toast.makeText(this.getBaseContext(), "Data Waybill tidak ada !!", Toast.LENGTH_SHORT).show();
			     			//return;
			 			}
			 			else
			 			{	
				 			tsdb.deleteAllContact();
				 			
				 			Log.d("Progress Bar","prepare insert data waybill");
				 			try
				 			{
				 				Log.d("Progress Bar","show progressbar");
				 				progressBar.show();
				 				final String res = respon ;
				 				new Thread(new Runnable() {
				 					  public void run() {
				 						  JSONObject json= null;
				 						  JSONArray JA = null;
						 		 		  try {
												JA = new JSONArray(res);
												double j = JA.length();
												for(int i=0;i<j;i++)
										    	{
													json=JA.getJSONObject(i);
													TransWB wb = new TransWB();
									    			wb.setMswb_pk(json.getString("mswb_pk"));
									    			wb.setMswb_no(json.getString("mswb_no"));
									    			wb.setCustomer(json.getString("customer"));
									    			wb.setShipper(json.getString("shipper"));
									    			wb.setConsignee(json.getString("consignee"));
									    			wb.setOrg(json.getString("org"));
									    			wb.setOrigin(json.getString("origin"));
									    			wb.setDest(json.getString("dest"));
									    			wb.setDestination(json.getString("destination"));
									    			wb.setContent(json.getString("content"));
									    			wb.setQTY(json.getString("qty"));
									    			wb.setKG(json.getString("kg"));
									    			wb.setStatus("0");
									    			tsdb.createContact(wb);
									    			Log.e("input data", json.getString("mswb_pk") + ", " + json.getString("mswb_no") );
									    			progressBarStatus = (int) ((i/j)*100);
									    			Log.e("Progress bar","status -> (" + i +" / " + j + ") * 100 = "+ (int) ((i/j)*100) +" = " +progressBarStatus ); 
									    		}
												Log.d("Progress Bar","dismiss");
							 					progressBar.dismiss();
									    		
										   } catch (JSONException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
										   }	  
						 		 		   tsdb.close();
						 		 		   Log.e("input data", "Successfull !!");
						 					
				 				  }	
					    		}).start();
				 				
				 			}
				 			catch(Exception e)
				 			{
	
					 			Log.e("Fail 3", e.toString());
					 			Toast.makeText(this.getBaseContext(), "Error = " + e.toString(), Toast.LENGTH_SHORT).show();
					 			tsdb.close();
				 			}
				 		}
			 			}
					 	catch (Exception e) {
				 			Log.e("Fail 4", e.toString());
				 			Toast.makeText(this.getBaseContext(), "Error = " + e.toString(), Toast.LENGTH_SHORT).show();
				 			tsdb.close();
					 	}		 		
		 //akhir ambil data waybill
		 		
	 }	
	 */

	private void upload_all_data_assigment() {
		Log.d("Debug", "Lewat Update All Assigment");

		Log.d("Debug","Cek Path URL For Upload"
    	        + " || 1. Str_LinkUpdate = "+Str_LinkUpdate
    	        + " || 2. STR_Linkpo_outstanding = "+STR_Linkpo_outstanding
    	        + " || 3. STR_Linkfinish_po_outstanding = "+STR_Linkfinish_po_outstanding
    	        + " || 4. STR_Linkads = "+STR_Linkads
    	        + " || 5. STR_Linkfinish_ads = "+STR_Linkfinish_ads
    	        + " || 6. STR_Linkawb_others = "+STR_Linkawb_others
    	        + " || 7. STR_Linkfinish_awb_others = "+STR_Linkfinish_awb_others
    	        + " || 8. STR_Linkpod = "+STR_Linkpod
    	        + " || 9. STR_Linkdex = "+STR_Linkdex);

//		gps_tracker mygps;
//	    mygps = new gps_tracker(menu_utama.this);
//	    lat_long = Double.toString(mygps.getLatitude()) + ", " + Double.toString(mygps.getLongitude());
//	    	
//	    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//	    // cek GPS status
//	    isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//	    // cek status koneksi
//	    isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//	    
//	    if (location == null && !isGPSEnable) {
//	    	locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//			Log.d("Debug", "GPS Aktif ");
//		} else if (location == null && !isNetworkEnable) {
//			locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//			Log.d("Debug", "Koneksi Internet Aktif ");
//		} else if (location != null) {
//			Log.d("Debug", "GPS Atau Koneksi Internet Tidak Aktif ");
//		} else {
//			Log.d("Debug", "Jaringan GPS Atau Koneksi Internet Aktif");
//		}
//	    
//	    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//	    NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
//	    if (networkinfo != null && networkinfo.isConnected()) {
//			// aksi ketika ada koneksi internet
//	    	Log.d("Debug", "Koneksi Internet Aktif ");
//		} else {
//			// aksi ketika tidak ada koneksi internet
//			Log.d("Debug", "Koneksi Internet Tidak Aktif ");
//		}

		STR_url_po_outstanding = Str_urlphp_po+Str_LinkUpdate;
		STR_url_finish_po_outstanding = Str_urlphp_po+STR_Linkpo_outstanding;
		STR_url_ads = Str_urlphp_po+STR_Linkfinish_po_outstanding;
		STR_url_finish_ads = Str_urlphp_po+STR_Linkads;
		STR_url_awb_others = Str_urlphp_po+STR_Linkfinish_ads;
		STR_url_finish_awb_others = Str_urlphp_po+STR_Linkfinish_awb_others;
		STR_url_pod = Str_urlphp_po+STR_Linkpod;
		STR_url_dex = Str_urlphp_po+STR_Linkdex;
		STR_url_cek_version = Str_urlphp_po+Str_LinkUpdate;

		Log.d("Debug","Final URL For Upload"
    	        + " || 1. Str_LinkUpdate = "+STR_url_cek_version
    	        + " || 2. STR_Linkpo_outstanding = "+STR_url_po_outstanding
    	        + " || 3. STR_Linkfinish_po_outstanding = "+STR_url_finish_po_outstanding
    	        + " || 4. STR_Linkads = "+STR_url_ads
    	        + " || 5. STR_Linkfinish_ads = "+STR_url_finish_ads
    	        + " || 6. STR_Linkawb_others = "+STR_url_awb_others
    	        + " || 7. STR_Linkfinish_awb_others = "+STR_url_finish_awb_others
    	        + " || 8. STR_Linkpod = "+STR_url_pod
    	        + " || 9. STR_Linkdex = "+STR_url_dex);

		dbListPoOutstanding = new ListPoOutstandingDBAdapter(menu_utama.this);
		Log.d("Progress Bar", "Prepare Data PO Outstanding ..");
		progressBar = new ProgressDialog(this);
		progressBar.setCancelable(true);
		progressBar.setMessage("Upload Data Local All Assigment");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		Log.d("Progress Bar", "Url .." + Str_urlphp_po);
		if (Str_urlphp_po.equals("")) {
			Intent a = new Intent(menu_utama.this, SettingHost.class);
			startActivity(a);
			return;
		}
		Log.d("Progress Bar", "Opening Database PO Outstanding");
		dbListPoOutstanding.open();
//		Cursor curListPoOutstanding = dbListPoOutstanding.getIdlePoOutstanding("0");
		Cursor curListPoOutstanding = dbListPoOutstanding.getAllContact();
		Log.d("Progress Bar", "Database PO Outstanding Open");
		if (curListPoOutstanding != null) {
			Log.d("Progress Bar", "Database PO Outstanding Ready");
			curListPoOutstanding.moveToFirst();
			Log.d("Progress Bar", "went to first record PO Outstanding");
			if (curListPoOutstanding.getCount() > 0) {
				Log.d("Progress Bar", "Data PO Outstanding Exists");
				Log.d("Progress Bar", "Show Progressbar");
				progressBar.show();
				do {
					String Str_po_outstanding_waybill = curListPoOutstanding.getString(curListPoOutstanding.getColumnIndexOrThrow(ListPoOutstandingDBAdapter.DB_WAYBILL));
					String Str_po_outstanding_locpk = curListPoOutstanding.getString(curListPoOutstanding.getColumnIndexOrThrow(ListPoOutstandingDBAdapter.DB_LOCPK));
					String Str_po_outstanding_lat_long = curListPoOutstanding.getString(curListPoOutstanding.getColumnIndexOrThrow(ListPoOutstandingDBAdapter.DB_LAT_LONG));
					String Str_po_outstanding_username = curListPoOutstanding.getString(curListPoOutstanding.getColumnIndexOrThrow(ListPoOutstandingDBAdapter.DB_USERNAME));
					String Str_po_outstanding_po = curListPoOutstanding.getString(curListPoOutstanding.getColumnIndexOrThrow(ListPoOutstandingDBAdapter.DB_PO));
					String Str_po_outstanding_assigment = curListPoOutstanding.getString(curListPoOutstanding.getColumnIndexOrThrow(ListPoOutstandingDBAdapter.DB_ASSIGMENT));
					String Str_po_outstanding_image = curListPoOutstanding.getString(curListPoOutstanding.getColumnIndexOrThrow(ListPoOutstandingDBAdapter.DB_IMAGE));
					String Str_po_outstanding_kota = curListPoOutstanding.getString(curListPoOutstanding.getColumnIndexOrThrow(ListPoOutstandingDBAdapter.DB_KOTA));
					String Str_po_outstanding_waktu = curListPoOutstanding.getString(curListPoOutstanding.getColumnIndexOrThrow(ListPoOutstandingDBAdapter.DB_WAKTU));

					ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
					masukparam1.add(new BasicNameValuePair("waybill", Str_po_outstanding_waybill));
					masukparam1.add(new BasicNameValuePair("locpk", Str_po_outstanding_locpk));
					masukparam1.add(new BasicNameValuePair("lat_long", Str_po_outstanding_lat_long));
					masukparam1.add(new BasicNameValuePair("username", Str_po_outstanding_username));
					masukparam1.add(new BasicNameValuePair("po", Str_po_outstanding_po));
					masukparam1.add(new BasicNameValuePair("assigment", Str_po_outstanding_assigment));
					masukparam1.add(new BasicNameValuePair("image", Str_po_outstanding_image));
					masukparam1.add(new BasicNameValuePair("kota", Str_po_outstanding_kota));
					masukparam1.add(new BasicNameValuePair("waktu", Str_po_outstanding_waktu));

					String response1 = null;

					try {
						cekPostPoOutstanding(Str_po_outstanding_waybill, Str_po_outstanding_locpk, Str_po_outstanding_lat_long
								, Str_po_outstanding_username, Str_po_outstanding_po, Str_po_outstanding_assigment, Str_po_outstanding_image
								, Str_po_outstanding_kota, Str_po_outstanding_waktu);
					} catch (Exception e) {
      		 			//Toast.makeText(theButton.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
      		 			Toast.makeText(this.getBaseContext(), "PO Outstanding Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
      		 			Log.d("Debug","Connectioan Fail To PO Outstanding Database");
      		 			break;
      		 		}
				} while (curListPoOutstanding.moveToNext());
				Log.d("Progress Bar", "Dismiss");
				progressBar.dismiss();
			} else {
				Log.d("Progress Bar", "Data PO Outstanding Is Null");
				Toast.makeText(this.getBaseContext(), "Local Database PO Outstanding Kosong ...", Toast.LENGTH_SHORT).show();
			}
		}
		dbListPoOutstanding.close();

		dbFinishPoOutstanding = new ListFinishPoOutstandingDBAdapter(menu_utama.this);
		Log.d("Progress Bar", "Prepare Data Finish PO Outstanding ..");
		Log.d("Progress Bar", "Opening Database Finish PO Outstanding");
		dbFinishPoOutstanding.open();
//		Cursor curFinishPoOutstanding = dbFinishPoOutstanding.getIdleFinishPoOutstanding("0");
		Cursor curFinishPoOutstanding = dbFinishPoOutstanding.getAllContact();
		Log.d("Progress Bar", "Database Finish PO Outstanding Open");
		if (curFinishPoOutstanding != null) {
			Log.d("Progress Bar", "Database Finish PO Outstanding Ready");
			curFinishPoOutstanding.moveToFirst();
			Log.d("Progress Bar", "went to first record Finish PO Outstanding");
			if (curFinishPoOutstanding.getCount() > 0) {
				Log.d("Progress Bar", "Data Finish PO Outstanding Exists");
				Log.d("Progress Bar", "Show Progressbar");
				progressBar.show();
				do {
					String Str_finish_po_outstandingpo = curFinishPoOutstanding.getString(curFinishPoOutstanding.getColumnIndexOrThrow(ListFinishPoOutstandingDBAdapter.DB_PO));
					String Str_finish_po_outstandingassigment = curFinishPoOutstanding.getString(curFinishPoOutstanding.getColumnIndexOrThrow(ListFinishPoOutstandingDBAdapter.DB_NO_ASSIGMENT));

					ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
					masukparam1.add(new BasicNameValuePair("po", Str_finish_po_outstandingpo));
					masukparam1.add(new BasicNameValuePair("assigment", Str_finish_po_outstandingassigment));

					String response1 = null;

					try {
						cekPostFinishPO(Str_finish_po_outstandingpo, Str_finish_po_outstandingassigment);
					} catch (Exception e) {
						// Toast.makeText(theButton.getContext(), e.toString(),
						// Toast.LENGTH_SHORT).show();
						Toast.makeText(this.getBaseContext(), "Finish PO Outstanding Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
						Log.d("Debug","Connectioan Fail To Finish PO Outstanding Database");
						break;
					}
				} while (curFinishPoOutstanding.moveToNext());
				Log.d("Progress Bar", "Dismiss");
				progressBar.dismiss();
			} else {
				Log.d("Progress Bar", "Data Finish PO Outstanding Is Null");
				Toast.makeText(this.getBaseContext(), "Local Database Finish PO Outstanding Kosong ...", Toast.LENGTH_SHORT).show();
			}
		}
		dbFinishPoOutstanding.close();

		dbListADS = new ListADSDBAdapter(menu_utama.this);
		Log.d("Progress Bar", "Prepare Data ADS ..");
		Log.d("Progress Bar", "Opening Database ADS");
		dbListADS.open();
//		Cursor curListADS = dbListADS.getIdleADS("0");
		Cursor curListADS = dbListADS.getAllContact();
		Log.d("Progress Bar", "Database ADS Open");
		if (curListADS != null) {
			Log.d("Progress Bar", "Database ADS Ready");
			curListADS.moveToFirst();
			Log.d("Progress Bar", "went to first record ADS");
			if (curListADS.getCount() > 0) {
				Log.d("Progress Bar", "Data Finish ADS Exists");
				Log.d("Progress Bar", "Show Progressbar");
				progressBar.show();
				do {
					String Str_ads_waybill = curListADS.getString(curListADS.getColumnIndexOrThrow(ListADSDBAdapter.DB_WAYBILL));
					String Str_ads_locpk = curListADS.getString(curListADS.getColumnIndexOrThrow(ListADSDBAdapter.DB_LOCPK));
					String Str_ads_lat_long = curListADS.getString(curListADS.getColumnIndexOrThrow(ListADSDBAdapter.DB_LAT_LONG));
					String Str_ads_username = curListADS.getString(curListADS.getColumnIndexOrThrow(ListADSDBAdapter.DB_USERNAME));
					String Str_ads_assigment = curListADS.getString(curListADS.getColumnIndexOrThrow(ListADSDBAdapter.DB_ASSIGMENT));
//					String Str_ads_image = curListADS.getString(curListADS.getColumnIndexOrThrow(ListADSDBAdapter.DB_IMAGE));
					String Str_ads_waktu = curListADS.getString(curListADS.getColumnIndexOrThrow(ListADSDBAdapter.DB_WAKTU));
					String Str_ads_customer = curListADS.getString(curListADS.getColumnIndexOrThrow(ListADSDBAdapter.DB_CUSTOMER));

					ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
					masukparam1.add(new BasicNameValuePair("waybill", Str_ads_waybill));
					masukparam1.add(new BasicNameValuePair("locpk", Str_ads_locpk));
					masukparam1.add(new BasicNameValuePair("lat_long", Str_ads_lat_long));
					masukparam1.add(new BasicNameValuePair("username", Str_ads_username));
					masukparam1.add(new BasicNameValuePair("assigment", Str_ads_assigment));
//					masukparam1.add(new BasicNameValuePair("image", Str_ads_image));
					masukparam1.add(new BasicNameValuePair("waktu", Str_ads_waktu));
					masukparam1.add(new BasicNameValuePair("customer", Str_ads_customer));

					String response1 = null;

					try {
//						cekPostADS(Str_ads_waybill, Str_ads_locpk, Str_ads_lat_long, Str_ads_username, Str_ads_assigment, Str_ads_image, Str_ads_waktu, Str_ads_customer);
						cekPostADS(Str_ads_waybill, Str_ads_locpk, Str_ads_lat_long, Str_ads_username, Str_ads_assigment, Str_ads_waktu, Str_ads_customer);
					} catch (Exception e) {
						// Toast.makeText(theButton.getContext(), e.toString(),
						// Toast.LENGTH_SHORT).show();
						Toast.makeText(this.getBaseContext(), "ADS Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
						Log.d("Debug","Connectioan Fail To ADS Database");
						break;
					}
				} while (curListADS.moveToNext());
				Log.d("Progress Bar", "Dismiss");
				progressBar.dismiss();
			} else {
				Log.d("Progress Bar", "Data ADS Is Null");
				Toast.makeText(this.getBaseContext(), "Local Database ADS Kosong ...", Toast.LENGTH_SHORT).show();
			}
		}
		dbListADS.close();

		dbFinishADS = new ListFinishADSDBAdapter(menu_utama.this);
		Log.d("Progress Bar", "Prepare Data Finish ADS ..");
		Log.d("Progress Bar", "Opening Database Finish ADS");
		dbFinishADS.open();
//		Cursor curFinishADS = dbFinishADS.getIdleFinishADS("0");
		Cursor curFinishADS = dbFinishADS.getAllContact();
		Log.d("Progress Bar", "Database Finish ADS Open");
		if (curFinishADS != null) {
			Log.d("Progress Bar", "Database Finish ADS Ready");
			curFinishADS.moveToFirst();
			Log.d("Progress Bar", "went to first record Finish ADS");
			if (curFinishADS.getCount() > 0) {
				Log.d("Progress Bar", "Data Finish ADS Exists");
				Log.d("Progress Bar", "Show Progressbar");
				progressBar.show();
				do {
					String Str_ads_finish_waktu = curFinishADS.getString(curFinishADS.getColumnIndexOrThrow(ListFinishADSDBAdapter.DB_WAKTU));
					String Str_ads_finish_assigment = curFinishADS.getString(curFinishADS.getColumnIndexOrThrow(ListFinishADSDBAdapter.DB_NO_ASSIGMENT));
					String Str_ads_finish_customer = curFinishADS.getString(curFinishADS.getColumnIndexOrThrow(ListFinishADSDBAdapter.DB_CUSTOMER));
					String Str_ads_finish_username = curFinishADS.getString(curFinishADS.getColumnIndexOrThrow(ListFinishADSDBAdapter.DB_USERNAME));
					String Str_ads_finish_locpk = curFinishADS.getString(curFinishADS.getColumnIndexOrThrow(ListFinishADSDBAdapter.DB_LOCPK));

					ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
					masukparam1.add(new BasicNameValuePair("waktu", Str_ads_finish_waktu));
					masukparam1.add(new BasicNameValuePair("assigment", Str_ads_finish_assigment));
					masukparam1.add(new BasicNameValuePair("customer", Str_ads_finish_customer));
					masukparam1.add(new BasicNameValuePair("username", Str_ads_finish_username));
					masukparam1.add(new BasicNameValuePair("locpk", Str_ads_finish_locpk));

					String response1 = null;

					try {
						cekPostFinishADS(Str_ads_finish_waktu, Str_ads_finish_assigment, Str_ads_finish_customer, Str_ads_finish_username, Str_ads_finish_locpk);
					} catch (Exception e) {
						// Toast.makeText(theButton.getContext(), e.toString(),
						// Toast.LENGTH_SHORT).show();
						Toast.makeText(this.getBaseContext(), "Finish ADS Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
						Log.d("Debug","Connectioan Fail To Finish ADS Database");
						break;
					}
				} while (curFinishADS.moveToNext());
				Log.d("Progress Bar", "Dismiss");
				progressBar.dismiss();
			} else {
				Log.d("Progress Bar", "Data Finish ADS Is Null");
				Toast.makeText(this.getBaseContext(), "Local Database Finish ADS Kosong ...", Toast.LENGTH_SHORT).show();
			}
		}
		dbFinishADS.close();

		dbFinishPincodeADS = new ListFinishPincodeADSDBAdapter(menu_utama.this);
		Log.d("Progress Bar", "Prepare Data Finish Pincode ADS ..");
		Log.d("Progress Bar", "Opening Database Finish Pincode ADS");
		dbFinishPincodeADS.open();
		Cursor curFinishPincodeADS = dbFinishPincodeADS.getAllContact();
		Log.d("Progress Bar", "Database Finish Pincode ADS Open");
		if (curFinishPincodeADS != null) {
			Log.d("Progress Bar", "Database Finish Pincode ADS Ready");
			curFinishPincodeADS.moveToFirst();
			Log.d("Progress Bar", "went to first record Finish Pincode ADS");
			if (curFinishPincodeADS.getCount() > 0) {
				Log.d("Progress Bar", "Data Finish Pincode ADS Exists");
				Log.d("Progress Bar", "Show Progressbar");
				progressBar.show();
				do {
					String Str_ads_finish_pincode_assigment = curFinishPincodeADS.getString(curFinishADS.getColumnIndexOrThrow(ListFinishPincodeADSDBAdapter.DB_NO_ASSIGMENT));
					String Str_ads_finish_pincode_customer = curFinishPincodeADS.getString(curFinishPincodeADS.getColumnIndexOrThrow(ListFinishPincodeADSDBAdapter.DB_CUSTOMER));
					String Str_ads_finish_pincode_username = curFinishPincodeADS.getString(curFinishPincodeADS.getColumnIndexOrThrow(ListFinishPincodeADSDBAdapter.DB_USERNAME));
					String Str_ads_finish_pincode_locpk = curFinishPincodeADS.getString(curFinishPincodeADS.getColumnIndexOrThrow(ListFinishPincodeADSDBAdapter.DB_LOCPK));
					String Str_ads_finish_pincode_pincode = curFinishPincodeADS.getString(curFinishPincodeADS.getColumnIndexOrThrow(ListFinishPincodeADSDBAdapter.DB_PINCODE));
					String Str_ads_finish_pincode_lat_long = curFinishPincodeADS.getString(curFinishPincodeADS.getColumnIndexOrThrow(ListFinishPincodeADSDBAdapter.DB_LAT_LONG));
					String Str_ads_finish_pincode_waktu = curFinishPincodeADS.getString(curFinishPincodeADS.getColumnIndexOrThrow(ListFinishPincodeADSDBAdapter.DB_WAKTU));
					String Str_ads_finish_pincode_waybill = curFinishPincodeADS.getString(curFinishPincodeADS.getColumnIndexOrThrow(ListFinishPincodeADSDBAdapter.DB_WAYBILL));
					String Str_ads_finish_pincode_kota = curFinishPincodeADS.getString(curFinishPincodeADS.getColumnIndexOrThrow(ListFinishPincodeADSDBAdapter.DB_KOTA));

					ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
					masukparam1.add(new BasicNameValuePair("assigment", Str_ads_finish_pincode_assigment));
					masukparam1.add(new BasicNameValuePair("customer", Str_ads_finish_pincode_customer));
					masukparam1.add(new BasicNameValuePair("username", Str_ads_finish_pincode_username));
					masukparam1.add(new BasicNameValuePair("locpk", Str_ads_finish_pincode_locpk));
					masukparam1.add(new BasicNameValuePair("pincode", Str_ads_finish_pincode_pincode));
					masukparam1.add(new BasicNameValuePair("lat_long", Str_ads_finish_pincode_lat_long));
					masukparam1.add(new BasicNameValuePair("waktu", Str_ads_finish_pincode_waktu));
					masukparam1.add(new BasicNameValuePair("waybill", Str_ads_finish_pincode_waybill));
					masukparam1.add(new BasicNameValuePair("kota", Str_ads_finish_pincode_kota));

					String response1 = null;

					try {
						cekPostFinishPincodeADS(Str_ads_finish_pincode_assigment, Str_ads_finish_pincode_customer
								, Str_ads_finish_pincode_username, Str_ads_finish_pincode_locpk, Str_ads_finish_pincode_pincode
								, Str_ads_finish_pincode_lat_long, Str_ads_finish_pincode_waktu, Str_ads_finish_pincode_waybill, Str_ads_finish_pincode_kota);
					} catch (Exception e) {
						Toast.makeText(this.getBaseContext(), "Finish ADS Pincode Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
						Log.d("Debug","Connectioan Fail To Finish Pincode ADS Database");
						break;
					}
				} while (curFinishPincodeADS.moveToNext());
				Log.d("Progress Bar", "Dismiss");
				progressBar.dismiss();
			} else {
				Log.d("Progress Bar", "Data Finish Pincode ADS Is Null");
				Toast.makeText(this.getBaseContext(), "Local Database Finish Pincode ADS Kosong ...", Toast.LENGTH_SHORT).show();
			}
		}
		dbFinishPincodeADS.close();

		dbListAWBOthers = new ListAWBOthersDBAdapter(menu_utama.this);
		Log.d("Progress Bar", "Prepare Data AWB Others ..");
		Log.d("Progress Bar", "Opening Database AWB Others");
		dbListAWBOthers.open();
//		Cursor curListAWBOthers = dbListAWBOthers.getIdleAWBOthers("0");
		Cursor curListAWBOthers = dbListAWBOthers.getAllContact();
		Log.d("Progress Bar", "Database AWB Others Open");
		if (curListAWBOthers != null) {
			Log.d("Progress Bar", "Database AWB Others Ready");
			curListAWBOthers.moveToFirst();
			Log.d("Progress Bar", "went to first record AWB Others");
			if (curListAWBOthers.getCount() > 0) {
				Log.d("Progress Bar", "Data AWB Others Exists");
				Log.d("Progress Bar", "Show Progressbar");
				progressBar.show();
				do {
					String Str_awb_others_waybill = curListAWBOthers.getString(curListAWBOthers.getColumnIndexOrThrow(ListAWBOthersDBAdapter.DB_WAYBILL));
					String Str_awb_others_locpk = curListAWBOthers.getString(curListAWBOthers.getColumnIndexOrThrow(ListAWBOthersDBAdapter.DB_LOCPK));
					String Str_awb_others_lat_long = curListAWBOthers.getString(curListAWBOthers.getColumnIndexOrThrow(ListAWBOthersDBAdapter.DB_LAT_LONG));
					String Str_awb_others_username = curListAWBOthers.getString(curListAWBOthers.getColumnIndexOrThrow(ListAWBOthersDBAdapter.DB_USERNAME));
					String Str_awb_others_assigment = curListAWBOthers.getString(curListAWBOthers.getColumnIndexOrThrow(ListAWBOthersDBAdapter.DB_ASSIGMENT));
					String Str_awb_others_image = curListAWBOthers.getString(curListAWBOthers.getColumnIndexOrThrow(ListAWBOthersDBAdapter.DB_IMAGE));
					String Str_awb_others_waktu = curListAWBOthers.getString(curListAWBOthers.getColumnIndexOrThrow(ListAWBOthersDBAdapter.DB_WAKTU));

					ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
					masukparam1.add(new BasicNameValuePair("waybill", Str_awb_others_waybill));
					masukparam1.add(new BasicNameValuePair("locpk", Str_awb_others_locpk));
					masukparam1.add(new BasicNameValuePair("lat_long", Str_awb_others_lat_long));
					masukparam1.add(new BasicNameValuePair("username", Str_awb_others_username));
					masukparam1.add(new BasicNameValuePair("assigment", Str_awb_others_assigment));
					masukparam1.add(new BasicNameValuePair("image", Str_awb_others_image));
					masukparam1.add(new BasicNameValuePair("waktu", Str_awb_others_waktu));

					String response1 = null;

					try {
						cekPostAWBOthers(Str_awb_others_waybill, Str_awb_others_locpk, Str_awb_others_lat_long, Str_awb_others_username, Str_awb_others_assigment, Str_awb_others_image, Str_awb_others_waktu);
					} catch (Exception e) {
						// Toast.makeText(theButton.getContext(), e.toString(),
						// Toast.LENGTH_SHORT).show();
						Toast.makeText(this.getBaseContext(), "Finish ADS Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
						Log.d("Debug","Connectioan Fail To AWB Others Database");
						break;
					}
				} while (curListAWBOthers.moveToNext());
				Log.d("Progress Bar", "Dismiss");
				progressBar.dismiss();
			} else {
				Log.d("Progress Bar", "Data AWB Others Is Null");
				Toast.makeText(this.getBaseContext(), "Local Database AWB Others Kosong ...", Toast.LENGTH_SHORT).show();
			}
		}
		dbListAWBOthers.close();

		dbFinishAWBOthers = new ListFinishAWBOthersDBAdapter(menu_utama.this);
		Log.d("Progress Bar", "Prepare Data Finish AWB Others ..");
		Log.d("Progress Bar", "Opening Database Finish AWB Others");
		dbFinishAWBOthers.open();
//		Cursor curFinishListAWBOthers = dbFinishAWBOthers.getIdleFinishAWBOthers("0");
		Cursor curFinishListAWBOthers = dbFinishAWBOthers.getAllContact();
		Log.d("Progress Bar", "Database Finish AWB Others Open");
		if (curFinishListAWBOthers != null) {
			Log.d("Progress Bar", "Database Finish AWB Others Ready");
			curFinishListAWBOthers.moveToFirst();
			Log.d("Progress Bar", "went to first record Finish AWB Others");
			if (curFinishListAWBOthers.getCount() > 0) {
				Log.d("Progress Bar", "Data Finish AWB Others Exists");
				Log.d("Progress Bar", "Show Progressbar");
				progressBar.show();
				do {
					String Str_awb_others_finish_waktu = curFinishListAWBOthers.getString(curFinishListAWBOthers.getColumnIndexOrThrow(ListFinishAWBOthersDBAdapter.DB_WAKTU));
					String Str_awb_others_finish_assigment = curFinishListAWBOthers.getString(curFinishListAWBOthers.getColumnIndexOrThrow(ListFinishAWBOthersDBAdapter.DB_NO_ASSIGMENT));
					String Str_awb_others_finish_customer = curFinishListAWBOthers.getString(curFinishListAWBOthers.getColumnIndexOrThrow(ListFinishAWBOthersDBAdapter.DB_CUSTOMER));
					String Str_awb_others_finish_username = curFinishListAWBOthers.getString(curFinishListAWBOthers.getColumnIndexOrThrow(ListFinishAWBOthersDBAdapter.DB_USERNAME));
					String Str_awb_others_finish_locpk = curFinishListAWBOthers.getString(curFinishListAWBOthers.getColumnIndexOrThrow(ListFinishAWBOthersDBAdapter.DB_LOCPK));

					ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
					masukparam1.add(new BasicNameValuePair("waybill", Str_awb_others_finish_waktu));
					masukparam1.add(new BasicNameValuePair("locpk", Str_awb_others_finish_assigment));
					masukparam1.add(new BasicNameValuePair("lat_long", Str_awb_others_finish_customer));
					masukparam1.add(new BasicNameValuePair("username", Str_awb_others_finish_username));
					masukparam1.add(new BasicNameValuePair("assigment", Str_awb_others_finish_locpk));

					String response1 = null;

					try {
						cekPostFinishAWBOthers(Str_awb_others_finish_waktu, Str_awb_others_finish_assigment, Str_awb_others_finish_customer, Str_awb_others_finish_username, Str_awb_others_finish_locpk);
					} catch (Exception e) {
						// Toast.makeText(theButton.getContext(), e.toString(),
						// Toast.LENGTH_SHORT).show();
						Toast.makeText(this.getBaseContext(), "Finish AWB Others Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
						Log.d("Debug","Connectioan Fail To Finish AWB Others Database");
						break;
					}
				} while (curFinishListAWBOthers.moveToNext());
				Log.d("Progress Bar", "Dismiss");
				progressBar.dismiss();
			} else {
				Log.d("Progress Bar", "Data Finish AWB Others Is Null");
				Toast.makeText(this.getBaseContext(), "Local Database Finish AWB Others Kosong ...", Toast.LENGTH_SHORT).show();
			}
		}
		dbFinishAWBOthers.close();

		dbListPOD = new ListPODDBAdapter(menu_utama.this);
		Log.d("Progress Bar", "Prepare Data POD ..");
		Log.d("Progress Bar", "Opening Database POD");
		dbListPOD.open();
//		Cursor curListPOD = dbListPOD.getIdlePOD("0");
		Cursor curListPOD = dbListPOD.getAllContact();
		Log.d("Progress Bar", "Database POD Open");
		if (curListPOD != null) {
			Log.d("Progress Bar", "Database POD Ready");
			curListPOD.moveToFirst();
			Log.d("Progress Bar", "went to first record POD");
			if (curListPOD.getCount() > 0) {
				Log.d("Progress Bar", "Data POD Exists");
				Log.d("Progress Bar", "Show Progressbar");
				progressBar.show();
				do {
					String Str_pod_waybill = curListPOD.getString(curListPOD.getColumnIndexOrThrow(ListPODDBAdapter.DB_WAYBILL));
					String Str_pod_locpk = curListPOD.getString(curListPOD.getColumnIndexOrThrow(ListPODDBAdapter.DB_LOCPK));
					String Str_pod_username = curListPOD.getString(curListPOD.getColumnIndexOrThrow(ListPODDBAdapter.DB_USERNAME));
					String Str_pod_lat_long = curListPOD.getString(curListPOD.getColumnIndexOrThrow(ListPODDBAdapter.DB_LAT_LONG));
					String Str_pod_image = curListPOD.getString(curListPOD.getColumnIndexOrThrow(ListPODDBAdapter.DB_IMAGE));
//					String Str_pod_kota = curListPOD.getString(curListPOD.getColumnIndexOrThrow(ListPODDBAdapter.DB_KOTA));
					String Str_pod_assigment = curListPOD.getString(curListPOD.getColumnIndexOrThrow(ListPODDBAdapter.DB_ASSIGMENT));
					String Str_pod_keterangan = curListPOD.getString(curListPOD.getColumnIndexOrThrow(ListPODDBAdapter.DB_KETERANGAN));
					String Str_pod_penerima = curListPOD.getString(curListPOD.getColumnIndexOrThrow(ListPODDBAdapter.DB_PENERIMA));
					String Str_pod_tiperem = curListPOD.getString(curListPOD.getColumnIndexOrThrow(ListPODDBAdapter.DB_TIPEREM));
					String Str_pod_tlp = curListPOD.getString(curListPOD.getColumnIndexOrThrow(ListPODDBAdapter.DB_TLP));
					String Str_pod_waktu = curListPOD.getString(curListPOD.getColumnIndexOrThrow(ListPODDBAdapter.DB_WAKTU));

					ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
					masukparam1.add(new BasicNameValuePair("waybill", Str_pod_waybill));
					masukparam1.add(new BasicNameValuePair("locpk", Str_pod_locpk));
					masukparam1.add(new BasicNameValuePair("username", Str_pod_username));
					masukparam1.add(new BasicNameValuePair("lat_long", Str_pod_lat_long));
					masukparam1.add(new BasicNameValuePair("image", Str_pod_image));
//					masukparam1.add(new BasicNameValuePair("kota", Str_pod_kota));
					masukparam1.add(new BasicNameValuePair("assigment", Str_pod_assigment));
					masukparam1.add(new BasicNameValuePair("keterangan", Str_pod_keterangan));
					masukparam1.add(new BasicNameValuePair("penerima", Str_pod_penerima));
					masukparam1.add(new BasicNameValuePair("tiperem", Str_pod_tiperem));
					masukparam1.add(new BasicNameValuePair("telp", Str_pod_tlp));
					masukparam1.add(new BasicNameValuePair("waktu", Str_pod_waktu));

					String response1 = null;

					try {
						cekPostPOD(Str_pod_waybill, Str_pod_locpk, Str_pod_username, Str_pod_lat_long, Str_pod_image, Str_pod_assigment
						, Str_pod_keterangan, Str_pod_penerima, Str_pod_tiperem, Str_pod_tlp, Str_pod_waktu);
					} catch (Exception e) {
						// Toast.makeText(theButton.getContext(), e.toString(),
						// Toast.LENGTH_SHORT).show();
						Toast.makeText(this.getBaseContext(), "POD Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
						Log.d("Debug","Connectioan Fail To POD Database");
						break;
					}
				} while (curListPOD.moveToNext());
				Log.d("Progress Bar", "Dismiss");
				progressBar.dismiss();
			} else {
				Log.d("Progress Bar", "Data POD Is Null");
				Toast.makeText(this.getBaseContext(), "Local Database POD Kosong ...", Toast.LENGTH_SHORT).show();
			}
		}
		dbListPOD.close();

		dbListDEX = new ListDEXDBAdapter(menu_utama.this);
		Log.d("Progress Bar", "Prepare Data DEX ..");
		Log.d("Progress Bar", "Opening Database DEX");
		dbListDEX.open();
//		Cursor curListDEX = dbListDEX.getIdleDEX("0");
		Cursor curListDEX = dbListDEX.getAllContact();
		Log.d("Progress Bar", "Database DEX Open");
		if (curListDEX != null) {
			Log.d("Progress Bar", "Database DEX Ready");
			curListDEX.moveToFirst();
			Log.d("Progress Bar", "went to first record DEX");
			if (curListDEX.getCount() > 0) {
				Log.d("Progress Bar", "Data DEX Exists");
				Log.d("Progress Bar", "Show Progressbar");
				progressBar.show();
				do {
					String Str_dex_waybill = curListDEX.getString(curListDEX.getColumnIndexOrThrow(ListDEXDBAdapter.DB_WAYBILL));
					String Str_dex_locpk = curListDEX.getString(curListDEX.getColumnIndexOrThrow(ListDEXDBAdapter.DB_LOCPK));
					String Str_dex_username = curListDEX.getString(curListDEX.getColumnIndexOrThrow(ListDEXDBAdapter.DB_USERNAME));
					String Str_dex_lat_long = curListDEX.getString(curListDEX.getColumnIndexOrThrow(ListDEXDBAdapter.DB_LAT_LONG));
					String Str_dex_image = curListDEX.getString(curListDEX.getColumnIndexOrThrow(ListDEXDBAdapter.DB_IMAGE));
//					String Str_dex_kota = curListDEX.getString(curListDEX.getColumnIndexOrThrow(ListDEXDBAdapter.DB_KOTA));
					String Str_dex_assigment = curListDEX.getString(curListDEX.getColumnIndexOrThrow(ListDEXDBAdapter.DB_ASSIGMENT));
					String Str_dex_keterangan = curListDEX.getString(curListDEX.getColumnIndexOrThrow(ListDEXDBAdapter.DB_KETERANGAN));
//					String Str_dex_penerima = curListDEX.getString(curListDEX.getColumnIndexOrThrow(ListDEXDBAdapter.DB_PENERIMA));
					String Str_dex_tiperem = curListDEX.getString(curListDEX.getColumnIndexOrThrow(ListDEXDBAdapter.DB_TIPEREM));
//					String Str_dex_tlp = curListDEX.getString(curListDEX.getColumnIndexOrThrow(ListDEXDBAdapter.DB_TLP));
					String Str_dex_waktu = curListDEX.getString(curListDEX.getColumnIndexOrThrow(ListDEXDBAdapter.DB_WAKTU));

					ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
					masukparam1.add(new BasicNameValuePair("waybill", Str_dex_waybill));
					masukparam1.add(new BasicNameValuePair("locpk", Str_dex_locpk));
					masukparam1.add(new BasicNameValuePair("username", Str_dex_username));
					masukparam1.add(new BasicNameValuePair("lat_long", Str_dex_lat_long));
					masukparam1.add(new BasicNameValuePair("image", Str_dex_image));
//					masukparam1.add(new BasicNameValuePair("kota", Str_dex_kota));
					masukparam1.add(new BasicNameValuePair("assigment", Str_dex_assigment));
					masukparam1.add(new BasicNameValuePair("keterangan", Str_dex_keterangan));
//					masukparam1.add(new BasicNameValuePair("penerima", Str_dex_penerima));
					masukparam1.add(new BasicNameValuePair("tiperem", Str_dex_tiperem));
//					masukparam1.add(new BasicNameValuePair("telp", Str_dex_tlp));
					masukparam1.add(new BasicNameValuePair("waktu", Str_dex_waktu));

					String response1 = null;

					try {
//						cekPostDEX(Str_dex_waybill, Str_dex_locpk, Str_dex_username, Str_dex_lat_long, Str_dex_image, Str_dex_assigment
//						, Str_dex_keterangan, Str_dex_penerima, Str_dex_tiperem, Str_dex_tlp);
						cekPostDEX(Str_dex_waybill, Str_dex_locpk, Str_dex_username, Str_dex_lat_long, Str_dex_image, Str_dex_assigment
								, Str_dex_keterangan, Str_dex_tiperem, Str_dex_waktu);
					} catch (Exception e) {
						// Toast.makeText(theButton.getContext(), e.toString(),
						// Toast.LENGTH_SHORT).show();
						Toast.makeText(this.getBaseContext(), "DEX Connection Fail !" + e.toString(), Toast.LENGTH_SHORT).show();
						Log.d("Debug","Connectioan Fail To DEX Database");
						break;
					}
				} while (curListDEX.moveToNext());
				Log.d("Progress Bar", "Dismiss");
				progressBar.dismiss();
			} else {
				Log.d("Progress Bar", "Data DEX Is Null");
				Toast.makeText(this.getBaseContext(), "Local Database DEX Kosong ...", Toast.LENGTH_SHORT).show();
			}
		}
		dbListDEX.close();
	}

	public void cekPostPoOutstanding(String Str_waybill, String Str_locpk,
			String Str_lat_long, String Str_username, String Str_po,
			String Str_assigment, String Str_image, String Str_kota,
			String Str_waktu) throws JSONException {
		HttpURLConnection connection;
		OutputStreamWriter request = null;
		URL url = null;
		String URI = null;
		String response = null;

		C_PoOutstanding po_outstanding = new C_PoOutstanding();
		po_outstanding.setWaybill(Str_waybill);
		po_outstanding.setLocpk(Str_locpk);
		po_outstanding.setLat_Long(Str_lat_long);
		po_outstanding.setUsername(Str_username);
		po_outstanding.setPo(Str_po);
		po_outstanding.setAssigment(Str_assigment);
		po_outstanding.setImage(Str_image);
		po_outstanding.setKota(Str_kota);
		po_outstanding.setWaktu(Str_waktu);
		dbListPoOutstanding.updateContact(po_outstanding);

		String parameters = "waybill=" + Str_waybill
				+ "&locpk=" + Str_locpk
				+ "&username=" + Str_username
				+ "&lat_long=" + Str_lat_long
				+ "&po=" + Str_po
				+ "&image=" + Str_image
				+ "&kota=" + Str_kota
				+ "&asigment=" + Str_assigment
				+ "&waktu=" + Str_waktu;
		Log.d("Debug", "Parameters " + parameters);
		try {
			url = new URL(STR_url_po_outstanding);
			Log.d("Debug", "Test URL " + url);

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
			Log.d("Debug", "Trace Customer = " + json);
			RespnseCode = json.getString("response_code");
			RespnseMessage = json.getString("response_message");
			Log.d("Debug", "Response Code = " + RespnseCode);
			Log.d("Debug", "Response Message  = " + RespnseMessage);

			if (RespnseCode.toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local PO Outstanding, Gagal Di Proses.", Toast.LENGTH_SHORT).show();
       		}
			else {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local PO Outstanding, Berhasil Di Proses.", Toast.LENGTH_SHORT).show();
			}

		} catch (IOException e) {
			// Error
			Log.d("Debug", "Trace = ERROR ");
		}
	}

	public void cekPostFinishPO(String Str_po, String Str_assigment) throws JSONException {
		HttpURLConnection connection;
		OutputStreamWriter request = null;
		URL url = null;
		String URI = null;
		String response = null;

		C_Finish_PoOutstanding finish_po_outstanding = new C_Finish_PoOutstanding();
		finish_po_outstanding.setPo(Str_po);
		finish_po_outstanding.setAssigment(Str_assigment);
		dbFinishPoOutstanding.updateContact(finish_po_outstanding);

		String parameters = "po=" + Str_po
				+ "&asigment=" + Str_assigment;
		Log.d("Debug", "Parameters " + parameters);
		try {
			url = new URL(STR_url_finish_po_outstanding);
			Log.d("Debug", "Test URL " + url);

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
			Log.d("Debug", "Trace Customer = " + json);
			RespnseCode = json.getString("response_code");
			RespnseMessage = json.getString("response_message");
			Log.d("Debug", "Response Code = " + RespnseCode);
			Log.d("Debug", "Response Message  = " + RespnseMessage);

			if (RespnseCode.toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local Finish PO Outstanding, Gagal Di Proses.", Toast.LENGTH_SHORT).show();
       		}
			else {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local Finish PO Outstanding, Berhasil Di Proses.", Toast.LENGTH_SHORT).show();
			}

		} catch (IOException e) {
			// Error
			Log.d("Debug", "Trace = ERROR ");
		}
	}

	public void cekPostADS(String Str_ads_waybill, String Str_ads_locpk,
			String Str_ads_lat_long, String Str_ads_username,
			String Str_ads_assigment, String Str_ads_waktu, String Str_ads_customer)
			throws JSONException {
		HttpURLConnection connection;
		OutputStreamWriter request = null;
		URL url = null;
		String URI = null;
		String response = null;

		C_ADS ads = new C_ADS();
		ads.setWaybill(Str_ads_waybill);
		ads.setLocpk(Str_ads_locpk);
		ads.setLat_Long(Str_ads_lat_long);
		ads.setUsername(Str_ads_username);
		ads.setAssigment(Str_ads_assigment);
//		ads.setImage(Str_ads_image);
		ads.setWaktu(Str_ads_waktu);
		ads.setCustomer(Str_ads_customer);
		dbListADS.updateContact(ads);

		String parameters = "waybill=" + Str_ads_waybill
				+ "&locpk=" + Str_ads_locpk
				+ "&username=" + Str_ads_username
				+ "&lat_long=" + Str_ads_lat_long
//				+ "&image=" + Str_ads_image
				+ "&asigment=" + Str_ads_assigment
				+ "&waktu=" + Str_ads_waktu
				+ "&customer=" + Str_ads_customer;
		Log.d("Debug", "Parameters " + parameters);
		try {
			url = new URL(STR_url_ads);
			Log.d("Debug", "Test URL " + url);

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
			InputStreamReader isr = new InputStreamReader(
					connection.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				sb.append("respone_code");
				sb.append("respone_message");
				Log.d("Debug", "TraceLine = " + line);
			}
			JSONObject json = new JSONObject(sb.toString());
			Log.d("Debug", "Trace Customer = " + json);
			RespnseCode = json.getString("response_code");
			RespnseMessage = json.getString("response_message");
			Log.d("Debug", "Response Code = " + RespnseCode);
			Log.d("Debug", "Response Message  = " + RespnseMessage);

			if (RespnseCode.toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local ADS, Gagal Di Proses.", Toast.LENGTH_SHORT).show();
       		}
			else {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local ADS, Berhasil Di Proses.", Toast.LENGTH_SHORT).show();
			}

		} catch (IOException e) {
			// Error
			Log.d("Debug", "Trace = ERROR ");
		}
	}

	public void cekPostFinishADS(String Str_ads_finish_waktu,
			String Str_ads_finish_assigment, String Str_ads_finish_customer,
			String Str_ads_finish_username, String Str_ads_finish_locpk)
			throws JSONException {
		HttpURLConnection connection;
		OutputStreamWriter request = null;
		URL url = null;
		String URI = null;
		String response = null;

		C_Finish_ADS finish_ads = new C_Finish_ADS();
		finish_ads.setWaktu(Str_ads_finish_waktu);
		finish_ads.setAssigment(Str_ads_finish_assigment);
		finish_ads.setCustomer(Str_ads_finish_customer);
		finish_ads.setUsername(Str_ads_finish_username);
		finish_ads.setLocpk(Str_ads_finish_locpk);
		dbFinishADS.updateContact(finish_ads);

		String parameters = "waktu=" + Str_ads_finish_waktu
				+ "&asigment=" + Str_ads_finish_assigment
				+ "&customer=" + Str_ads_finish_customer
				+ "&username=" + Str_ads_finish_username
				// + "&kota=" + Str_kota
				+ "&locpk=" + Str_ads_finish_locpk;
		Log.d("Debug", "Parameters " + parameters);
		try {
			url = new URL(STR_url_finish_ads);
			Log.d("Debug", "Test URL " + url);

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
			InputStreamReader isr = new InputStreamReader(
					connection.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				sb.append("respone_code");
				sb.append("respone_message");
				Log.d("Debug", "TraceLine = " + line);
			}
			JSONObject json = new JSONObject(sb.toString());
			Log.d("Debug", "Trace Customer = " + json);
			RespnseCode = json.getString("response_code");
			RespnseMessage = json.getString("response_message");
			Log.d("Debug", "Response Code = " + RespnseCode);
			Log.d("Debug", "Response Message  = " + RespnseMessage);

			if (RespnseCode.toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local Finish ADS, Gagal Di Proses.", Toast.LENGTH_SHORT).show();
       		}
			else {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local Finish ADS, Berhasil Di Proses.", Toast.LENGTH_SHORT).show();
			}

		} catch (IOException e) {
			// Error
			Log.d("Debug", "Trace = ERROR ");
		}
	}

	public void cekPostFinishPincodeADS(String Str_ads_finish_pincode_assigment, String Str_ads_finish_pincode_customer,
			String Str_ads_finish_pincode_username, String Str_ads_finish_pincode_locpk, String Str_ads_finish_pincode_pincode,
			String Str_ads_finish_pincode_lat_long, String Str_ads_finish_pincode_waktu, String Str_ads_finish_pincode_waybill,
			String Str_ads_finish_pincode_kota)
			throws JSONException {
		HttpURLConnection connection;
		OutputStreamWriter request = null;
		URL url = null;
		String URI = null;
		String response = null;

		C_Pincode_ADS finish_pincode_ads = new C_Pincode_ADS();
		finish_pincode_ads.setAssigment(Str_ads_finish_pincode_assigment);
		finish_pincode_ads.setCustomer(Str_ads_finish_pincode_customer);
		finish_pincode_ads.setUsername(Str_ads_finish_pincode_username);
		finish_pincode_ads.setLocpk(Str_ads_finish_pincode_locpk);
		finish_pincode_ads.setPincode(Str_ads_finish_pincode_pincode);
		finish_pincode_ads.setLat_Long(Str_ads_finish_pincode_lat_long);
		finish_pincode_ads.setWaktu(Str_ads_finish_pincode_waktu);
		finish_pincode_ads.setWaybill(Str_ads_finish_pincode_waybill);
		finish_pincode_ads.setKota(Str_ads_finish_pincode_kota);
		dbFinishPincodeADS.updateContact(finish_pincode_ads);

		String parameters = "asigment=" + Str_ads_finish_pincode_assigment
				+ "&customer=" + Str_ads_finish_pincode_customer
				+ "&username=" + Str_ads_finish_pincode_username
				+ "&locpk=" + Str_ads_finish_pincode_locpk
				+ "&pincode=" + Str_ads_finish_pincode_pincode
				+ "&lat_long=" + Str_ads_finish_pincode_lat_long
				+ "&waktu=" + Str_ads_finish_pincode_waktu
				+ "&waybill=" + Str_ads_finish_pincode_waktu
				+ "&kota=" + Str_ads_finish_pincode_waktu;
		Log.d("Debug", "Parameters " + parameters);
		try {
			url = new URL(STR_url_finish_ads);
			Log.d("Debug", "Test URL " + url);

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
			InputStreamReader isr = new InputStreamReader(
					connection.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				sb.append("respone_code");
				sb.append("respone_message");
				Log.d("Debug", "TraceLine = " + line);
			}
			JSONObject json = new JSONObject(sb.toString());
			Log.d("Debug", "Trace Customer = " + json);
			RespnseCode = json.getString("response_code");
			RespnseMessage = json.getString("response_message");
			Log.d("Debug", "Response Code = " + RespnseCode);
			Log.d("Debug", "Response Message  = " + RespnseMessage);

			if (RespnseCode.toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local Finish Pincode ADS, Gagal Di Proses.", Toast.LENGTH_SHORT).show();
       		}
			else {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local Finish Pincode ADS, Berhasil Di Proses.", Toast.LENGTH_SHORT).show();
			}

		} catch (IOException e) {
			// Error
			Log.d("Debug", "Trace = ERROR ");
		}
	}

	public void cekPostAWBOthers(String Str_awb_others_waybill, String Str_awb_others_locpk,
			String Str_awb_others_lat_long, String Str_awb_others_username,
			String Str_awb_others_assigment, String Str_awb_others_image, String Str_awb_others_waktu)
			throws JSONException {
		HttpURLConnection connection;
		OutputStreamWriter request = null;
		URL url = null;
		String URI = null;
		String response = null;

		C_AWBOthers awb_others = new C_AWBOthers();
		awb_others.setWaybill(Str_awb_others_waybill);
		awb_others.setLocpk(Str_awb_others_locpk);
		awb_others.setLat_Long(Str_awb_others_lat_long);
		awb_others.setUsername(Str_awb_others_username);
		awb_others.setAssigment(Str_awb_others_assigment);
		awb_others.setImage(Str_awb_others_image);
		awb_others.setWaktu(Str_awb_others_waktu);
		dbListAWBOthers.updateContact(awb_others);

		String parameters = "waybill=" + Str_awb_others_waybill + "&locpk="
				+ Str_awb_others_locpk + "&username=" + Str_awb_others_username
				+ "&lat_long=" + Str_awb_others_lat_long + "&image="
				+ Str_awb_others_assigment + "&asigment=" + Str_awb_others_image + "&waktu="
				+ Str_awb_others_waktu;
		Log.d("Debug", "Parameters " + parameters);
		try {
			url = new URL(STR_url_awb_others);
			Log.d("Debug", "Test URL " + url);

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
			InputStreamReader isr = new InputStreamReader(
					connection.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				sb.append("respone_code");
				sb.append("respone_message");
				Log.d("Debug", "TraceLine = " + line);
			}
			JSONObject json = new JSONObject(sb.toString());
			Log.d("Debug", "Trace Customer = " + json);
			RespnseCode = json.getString("response_code");
			RespnseMessage = json.getString("response_message");
			Log.d("Debug", "Response Code = " + RespnseCode);
			Log.d("Debug", "Response Message  = " + RespnseMessage);

			if (RespnseCode.toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local AWB Others, Gagal Di Proses.", Toast.LENGTH_SHORT).show();
       		}
			else {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local AWB Others, Berhasil Di Proses.", Toast.LENGTH_SHORT).show();
			}

		} catch (IOException e) {
			// Error
			Log.d("Debug", "Trace = ERROR ");
		}
	}

	public void cekPostFinishAWBOthers(String Str_awb_others_finish_waktu,
			String Str_awb_others_finish_assigment, String Str_awb_others_finish_customer,
			String Str_awb_others_finish_username, String Str_awb_others_finish_locpk)
			throws JSONException {
		HttpURLConnection connection;
		OutputStreamWriter request = null;
		URL url = null;
		String URI = null;
		String response = null;

		C_Finish_AWBOthers finish_awb_others = new C_Finish_AWBOthers();
		finish_awb_others.setWaktu(Str_awb_others_finish_waktu);
		finish_awb_others.setAssigment(Str_awb_others_finish_assigment);
		finish_awb_others.setCustomer(Str_awb_others_finish_customer);
		finish_awb_others.setUsername(Str_awb_others_finish_username);
		finish_awb_others.setLocpk(Str_awb_others_finish_locpk);
		dbFinishAWBOthers.updateContact(finish_awb_others);

		String parameters = "waktu=" + Str_awb_others_finish_waktu + "&asigment="
				+ Str_awb_others_finish_assigment + "&customer=" + Str_awb_others_finish_customer + "&username="
				+ Str_awb_others_finish_username
				// + "&kota=" + Str_kota
				+ "&locpk=" + Str_awb_others_finish_locpk;
		Log.d("Debug", "Parameters " + parameters);
		try {
			url = new URL(STR_url_finish_awb_others);
			Log.d("Debug", "Test URL " + url);

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
			InputStreamReader isr = new InputStreamReader(
					connection.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				sb.append("respone_code");
				sb.append("respone_message");
				Log.d("Debug", "TraceLine = " + line);
			}
			JSONObject json = new JSONObject(sb.toString());
			Log.d("Debug", "Trace Customer = " + json);
			RespnseCode = json.getString("response_code");
			RespnseMessage = json.getString("response_message");
			Log.d("Debug", "Response Code = " + RespnseCode);
			Log.d("Debug", "Response Message  = " + RespnseMessage);

			if (RespnseCode.toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local Finish AWB Others, Gagal Di Proses.", Toast.LENGTH_SHORT).show();
       		}
			else {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local Finish AWB Others, Berhasil Di Proses.", Toast.LENGTH_SHORT).show();
			}

		} catch (IOException e) {
			// Error
			Log.d("Debug", "Trace = ERROR ");
		}
	}

	public void cekPostPOD(String Str_pod_waybill,
			String Str_pod_locpk, String Str_pod_username,
			String Str_pod_lat_long, String Str_pod_image, String Str_pod_assigment,
			String Str_pod_keterangan, String Str_pod_penerima, String Str_pod_tiperem, String Str_pod_tlp, String Str_pod_waktu)
			throws JSONException {
		HttpURLConnection connection;
		OutputStreamWriter request = null;
		URL url = null;
		String URI = null;
		String response = null;

		C_POD pod = new C_POD();
		pod.setWaybill(Str_pod_waybill);
		pod.setLocpk(Str_pod_locpk);
		pod.setUsername(Str_pod_username);
		pod.setLat_Long(Str_pod_lat_long);
		pod.setImage(Str_pod_image);
//		pod.setKota(Str_pod_kota);
		pod.setAssigment(Str_pod_assigment);
		pod.setKeterangan(Str_pod_keterangan);
		pod.setPenerima(Str_pod_penerima);
		pod.setTiperem(Str_pod_tiperem);
		pod.setTelp(Str_pod_tlp);
		pod.setWaktu(Str_pod_waktu);
		dbListPOD.updateContact(pod);

		String parameters = "waybill=" + Str_pod_waybill
				+ "&locpk=" + Str_pod_locpk
				+ "&username=" + Str_pod_username
				+ "&lat_long=" + Str_pod_lat_long
				+ "&image=" + Str_pod_image
//				+ "&kota=" + Str_pod_kota
				+ "&asigment=" + Str_pod_assigment
				+ "&keterangan=" + Str_pod_keterangan
				+ "&penerima=" + Str_pod_penerima
				+ "&tiperem=" + Str_pod_tiperem
				+ "&telp=" + Str_pod_tlp
				+ "&waktu=" + Str_pod_waktu;
		Log.d("Debug", "Parameters " + parameters);
		try {
			url = new URL(STR_url_pod);
			Log.d("Debug", "Test URL " + url);

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
			InputStreamReader isr = new InputStreamReader(
					connection.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				sb.append("respone_code");
				sb.append("respone_message");
				Log.d("Debug", "TraceLine = " + line);
			}
			JSONObject json = new JSONObject(sb.toString());
			Log.d("Debug", "Trace Customer = " + json);
			RespnseCode = json.getString("response_code");
			RespnseMessage = json.getString("response_message");
			Log.d("Debug", "Response Code = " + RespnseCode);
			Log.d("Debug", "Response Message  = " + RespnseMessage);

			if (RespnseCode.toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local POD, Gagal Di Proses.", Toast.LENGTH_SHORT).show();
       		}
			else {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local POD, Berhasil Di Proses.", Toast.LENGTH_SHORT).show();
			}

		} catch (IOException e) {
			// Error
			Log.d("Debug", "Trace = ERROR ");
		}
	}

	public void cekPostDEX(String Str_dex_waybill,
			String Str_dex_locpk, String Str_dex_username,
			String Str_dex_lat_long, String Str_dex_image, String Str_dex_assigment,
			String Str_dex_keterangan, String Str_dex_tiperem, String Str_dex_waktu)
			throws JSONException {
		HttpURLConnection connection;
		OutputStreamWriter request = null;
		URL url = null;
		String URI = null;
		String response = null;

		C_DEX dex = new C_DEX();
		dex.setWaybill(Str_dex_waybill);
		dex.setLocpk(Str_dex_locpk);
		dex.setUsername(Str_dex_username);
		dex.setLat_Long(Str_dex_lat_long);
		dex.setImage(Str_dex_image);
//		dex.setKota(Str_dex_kota);
		dex.setAssigment(Str_dex_assigment);
		dex.setKeterangan(Str_dex_keterangan);
//		dex.setPenerima(Str_dex_penerima);
		dex.setTiperem(Str_dex_tiperem);
//		dex.setTelp(Str_dex_tlp);
		dex.setWaktu(Str_dex_waktu);
		dbListDEX.updateContact(dex);

		String parameters = "waybill=" + Str_dex_waybill
				+ "&locpk=" + Str_dex_locpk
				+ "&username=" + Str_dex_username
				+ "&lat_long=" + Str_dex_lat_long
				+ "&image=" + Str_dex_image
//				+ "&kota=" + Str_dex_kota
				+ "&asigment=" + Str_dex_assigment
				+ "&keterangan=" + Str_dex_keterangan
//				+ "&penerima=" + Str_dex_penerima
				+ "&tiperem=" + Str_dex_tiperem
//				+ "&telp=" + Str_dex_tlp;
				+ "&waktu=" + Str_dex_waktu;
		Log.d("Debug", "Parameters " + parameters);
		try {
			url = new URL(STR_url_dex);
			Log.d("Debug", "Test URL " + url);

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
			InputStreamReader isr = new InputStreamReader(
					connection.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				sb.append("respone_code");
				sb.append("respone_message");
				Log.d("Debug", "TraceLine = " + line);
			}
			JSONObject json = new JSONObject(sb.toString());
			Log.d("Debug", "Trace Customer = " + json);
			RespnseCode = json.getString("response_code");
			RespnseMessage = json.getString("response_message");
			Log.d("Debug", "Response Code = " + RespnseCode);
			Log.d("Debug", "Response Message  = " + RespnseMessage);

			if (RespnseCode.toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local DEX, Gagal Di Proses.", Toast.LENGTH_SHORT).show();
       		}
			else {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Local DEX, Berhasil Di Proses.", Toast.LENGTH_SHORT).show();
			}

		} catch (IOException e) {
			// Error
			Log.d("Debug", "Trace = ERROR ");
		}
	}

	private boolean checkPermissions() {
		int read_phone_state = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
		int access_fine_location = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
		int write_esternal_storage= ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
		int read_esternal_storage= ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

		return read_phone_state == PackageManager.PERMISSION_GRANTED && access_fine_location == PackageManager.PERMISSION_GRANTED && write_esternal_storage == PackageManager.PERMISSION_GRANTED && read_esternal_storage == PackageManager.PERMISSION_GRANTED;
//		return read_phone_state == PackageManager.PERMISSION_GRANTED && access_fine_location == PackageManager.PERMISSION_GRANTED && read_esternal_storage == PackageManager.PERMISSION_GRANTED;
	}

	private void requestPermission() {

		ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE, ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

	}

	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode != PERMISSION_REQUEST_CODE ) {
			return;
		}
		boolean isGranted = true;
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				isGranted = false;
				break;
			}
		}

		if (isGranted) {
			startApplication();
		} else {
			Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();

		}
	}

	private void setPermissions() {
		ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE, ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//		ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
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
		@SuppressLint("MissingPermission") Location locationPhone = locationManager.getLastKnownLocation(provider);
	}

//	private void createUser(String Str_IdKurir, String regId, String Str_date_time) {
//		Log.i("Debug","Create Firebase DB in menu_utama");
//		// TODO
//		// In real apps this userId should be fetched
//		// by implementing firebase auth
//		if (TextUtils.isEmpty(userId)) {
//			userId = mFirebaseDatabase.push().getKey();
//		}
//
//		UserKurirFirebase user_kurir_firebase = new UserKurirFirebase(Str_IdKurir, Str_IdKurir, regId, Str_date_time);
//
//		mFirebaseDatabase.child(username).setValue(user_kurir_firebase);
//
//		addUserChangeListener(username);
//	}

//	private void addUserChangeListener(String Str_IdKurir) {
//		mFirebaseDatabase.child(Str_IdKurir).addValueEventListener(new ValueEventListener() {
//			@Override
//			public void onDataChange(DataSnapshot dataSnapshot) {
//				UserKurirFirebase user_kurir_firebase = dataSnapshot.getValue(UserKurirFirebase.class);
//
//				// Check for null
//				if (user_kurir_firebase == null) {
//					Log.e(TAG, "User data is null!");
//					return;
//				}
//
//				Log.e(TAG, "User data is changed in menu_utama! " + user_kurir_firebase.Str_IdKurir + ", " + user_kurir_firebase.Str_PassKurir + ", " + user_kurir_firebase.Str_regId);
//
////				// clear edit text
////				un.setText("");
////				pw.setText("");
//
////					toggleButton();
//			}
//
//			@Override
//			public void onCancelled(DatabaseError error) {
//				// Failed to read value
//				Log.e(TAG, "Failed to read user", error.toException());
//			}
//		});
//	}

//	public class updateFirebaseUserAsync extends AsyncTask<String, String, String>
//	{
//		ProgressDialog pDialog;
//		String responsecode, responsemessage;
//		String SessionIdKurir, SessionLocPk, SessionNameKurir;
//		HashMap<String, String> UserIdFirebase = sessionMan.getUserIDFirebaseDetails();
//		String Str_IdKurir = UserIdFirebase.get(sessionMan.KEY_ID_KURIR);
//		String Str_IdFirebase = UserIdFirebase.get(sessionMan.KEY_ID_FIREBASE);
//
//		HashMap<String, String> user = sessionMan.getUserDetails();
//		String username = user.get(SessionManager.KEY_USER);
//		String urlphp = user.get(SessionManager.KEY_URL);
//
//		SharedPreferences pref = getApplicationContext().getSharedPreferences(ConfigFirebase.SHARED_PREF, 0);
//		String regId = pref.getString("regId", null);
//
//		protected void onPreExecute(){
//			super.onPreExecute();
//
//			pDialog = new ProgressDialog(menu_utama.this);
//			pDialog.setMessage("Loading...");
//			pDialog.setIndeterminate(false);
//			pDialog.setCancelable(false);
//			pDialog.show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			HttpURLConnection connection;
//			OutputStreamWriter request = null;
//			URL url = null;
//			String URI = null;
//			String response = null;
//			String parameters = "username="+username+"&password="+username+"&id_firebase="+regId;
//			Log.d("Debug", "Parameter Update Firebase -> " + parameters);
//
//			try
//			{
////				String urlphp;
////				urlphp = "";
//				Log.d("debug", "Host Server -> " + urlphp);
//				url = new URL(urlphp+Str_login_user);
//				Log.d("Debug","Test URL Login " + url);
//
//				connection = (HttpURLConnection) url.openConnection();
//				connection.setDoOutput(true);
//				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//				connection.setRequestMethod("POST");
//
//				request = new OutputStreamWriter(connection.getOutputStream());
//				//Log.d("Debug","request = " + request);
//				request.write(parameters);
//				request.flush();
//				request.close();
//				String line = "";
//
//				//Get data from server
//				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
//				BufferedReader reader = new BufferedReader(isr);
//				StringBuilder sb = new StringBuilder();
//				while ((line = reader.readLine()) != null)
//				{
//					sb.append(line + "\n");
//					sb.append("response_code");
//					sb.append("response_message");
//					Log.d("Debug","TraceLine = " + line);
//				}
//
//				JSONObject jsonObject = new JSONObject(sb.toString());
//				responsecode = jsonObject.getString("response_code");
//				responsemessage = jsonObject.getString("response_message");
//
//				JSONObject jsonObjectLogin = new JSONObject(sb.toString());
//				JSONArray ListObjData = jsonObjectLogin.getJSONArray("data");
//				RespnseMessage = jsonObjectLogin.getString("response_message");
//				RespnseCode = jsonObjectLogin.getString("response_code");
//				for (int i = 0; i < ListObjData.length(); i++) {
//					JSONObject obj = ListObjData.getJSONObject(i);
//					String STR_CourierCode = obj.getString("muse_code");
//					String STR_CourierLocPk = obj.getString("muse_mloc_pk");
//					String STR_CourierName = obj.getString("muse_name");
//					SessionIdKurir = obj.getString("muse_code");
//					SessionLocPk = obj.getString("muse_mloc_pk");
//					SessionNameKurir = obj.getString("muse_name");
//
//					isr.close();
//					reader.close();
//				}
//			}
//			catch(IOException e)
//			{
//				Log.d("Debug","Trace = ERROR ");
//				Log.d("Debug","response_code  " + RespnseCode);
//
//			}
//			catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			pDialog.dismiss();
//
//			Log.d("Debug","ResponseCode PostExecute = " + responsecode +" || "+ "ResponseMessage = "+responsemessage);
//			Log.d("Debug","JsonObject PostExecute " +" IDKurir = "+ SessionIdKurir +" || "+ "NamaKurir = "+SessionNameKurir +" || "+ "LockPk = "+SessionLocPk);
//
//			if (!responsecode.equals("1")) {
//				LayoutInflater inflater = getLayoutInflater();
//				View layout = inflater.inflate(R.layout.toast_custom_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
//				final Toast toast = new Toast(getApplicationContext());
//				TextView tv = (TextView) layout.findViewById(R.id.toast);
//				tv.setText(responsemessage);
//				tv.setHeight(100);
//				tv.setWidth(200);
//				//Set toast gravity to bottom
//				toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
//				//Set toast duration
//				toast.setDuration(Toast.LENGTH_LONG);
//
//				//Set the custom layout to Toast
//				toast.setView(layout);
//				//Display toast
//				toast.show();
//				new CountDownTimer(1000, 1000){
//					public void onTick(long millisUntilFinished) {toast.show();}
//					public void onFinish() {toast.show();}
//				}.start();
////				un.setText("");
////				pw.setText("");
//			} else {
//				Toast.makeText(getApplicationContext(),"Data Kurir "+ SessionNameKurir +" Telah Di Update", Toast.LENGTH_LONG).show();
//			}
//		}
//	}

}
