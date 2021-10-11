package compact.mobile;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import compact.mobile.config.Koneksi;
import compact.mobile.config.firebase.app.ConfigFirebase;
import compact.mobile.config.firebase.database.UserKurirFirebase;
import compact.mobile.config.firebase.util.NotificationUtils;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	//28-09-2017
		public String Str_lo_Koneksi, Str_login_user, Str_update_IDFirebase;
		String STR_url_login;
		String Str_username, Str_password;
		String Str_rspn_code, Str_rspn_lcpk, Str_rspn_muse_name;
		String RespnseMessage, RespnseCode, STR_response_code;
		String STR_ResponseCodeLogin, STR_ResponseMessageLogin, STR_ResponseCodeUpdateToken, STR_ResponseCodeMessageToken;
	String STR_responsecodeConnection, STR_responsemessageConnection, STR_path_cek_connection;
		Button login_user, BTN_login_user;

	private static final int LONG_DELAY = 3500; // 3.5 seconds
	private static final int SHORT_DELAY = 2000; // 2 seconds

	private static final String TAG = MainActivity.class.getSimpleName();
	private BroadcastReceiver mRegistrationBroadcastReceiver;
	private TextView txtRegId, txtMessage;
	private DatabaseReference mFirebaseDatabase;
	private FirebaseDatabase mFirebaseInstance;
	private FirebaseAnalytics mFirebaseAnalytics;
	private String userId;

	 String	HostUrl	= "";
	 String	IDKurir	= "";
	 String UN ,PW;
	 EditText un,pw;
    TextView error ;
	 Button login,keluar;
	 private HostAdapter hostdb;
	 private UserAdapter userdb;
	 private DBAdapter db;
	 String sdayNow,sdayNow2;
	 UserAdapter usdb;
	 JSONArray version = null;
	 private  ProgressDialog progressBar;
	private  ProgressDialog pDialog;
	 String sv = null;
//	 SessionManager session;
	 compact.mobile.config.SessionManager sessionMan;
    compact.mobile.config.SessionManager sessionManDetailStore;
	 LocPk locpk = LocPk.getInstance();
	UserName username = UserName.getInstance();
	urlgw url = urlgw.getInstance();
	tipekoneksi conn = tipekoneksi.getInstance();

	private static final int REQUEST_WRITE_STORAGE = 112;
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
	   mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

	   //Firebase Database
	   mFirebaseInstance = FirebaseDatabase.getInstance();
	   mFirebaseDatabase = mFirebaseInstance.getReference("users_mobile_agent");
	   mFirebaseInstance.getReference("app_title").setValue("User Kurir Firebase Mobile Agent");

	   boolean success=true;
	   if(success){
		   Log.d("Debug", "success to create directory");
		   Log.d("Debug", "Mainactivity - IF success");
		   ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
		   Log.d("Debug", "If");
		   String intStorageDirectory = getFilesDir().toString();
		   Log.d("Debug", "Dir Storage "+intStorageDirectory);
		   File mediaStorageDirFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		   File mediaStorageDir = new File(mediaStorageDirFolder+ File.separator + "Compact" + File.separator);
		   Log.d("Debug", "Media Storage "+mediaStorageDir);

		   if (!mediaStorageDir.exists()) {
			   if (!mediaStorageDir.mkdirs()) {
				   Log.d("Debug", "failed to create directory");
				   Log.d("Debug", "Mainactivity - IF IF");
			   } else {
				   Log.d("Debug", "success to create directory");
				   Log.d("Debug", "Mainactivity - IF ELSE");
			   }
		   }
	   }else{
		   Log.d("Debug", "failed to create directory");
		   Log.d("Debug", "Mainactivity - ELSE failed");
	   }

       Koneksi Str_lo_Koneksi = new Koneksi();
       Str_login_user = Str_lo_Koneksi.ConnLogin();
	   Str_update_IDFirebase = Str_lo_Koneksi.ConnAddIDFirebase();
	   STR_path_cek_connection = Str_lo_Koneksi.ConnCekConnection();

       sessionMan = new compact.mobile.config.SessionManager(getApplicationContext());
	   sessionManDetailStore = new compact.mobile.config.SessionManager(getApplicationContext());
		db = new DBAdapter(this);
		db.open();
		db.close();
		hostdb = new HostAdapter(this);
		userdb = new UserAdapter(this);

		hostdb.open();

		Cursor cur = hostdb.getAllContact();
		if (cur != null)  {
			Log.d("ada isinya","host " + HostUrl);
			cur.moveToFirst();
			if (cur.getCount() > 0) {
				HostUrl = cur.getString(cur.getColumnIndexOrThrow(HostAdapter.HOST));
			}
		}
		hostdb.close();
		Log.d("ambil data","host " + HostUrl);

		if (HostUrl.equals("")){
			Intent jx_hostgo = new Intent(MainActivity.this, SettingHost.class);
			startActivity(jx_hostgo);
			finish();
		}


		Calendar c = Calendar.getInstance();
		Integer dayNow = c.get(Calendar.DAY_OF_MONTH);

		dayNow = dayNow + 99;


		sdayNow = dayNow.toString();
		StringBuffer sb=new StringBuffer(sdayNow);
		sdayNow2 = sb.reverse().toString();

		Log.d("debug","today -> " + sdayNow + " <-> " + sdayNow2);

		un=(EditText)findViewById(R.id.user);
       pw=(EditText)findViewById(R.id.password);
//       login=(Button)findViewById(R.id.submit2);
       login_user=(Button)findViewById(R.id.submit);
       BTN_login_user=(Button)findViewById(R.id.btn_login);
       keluar=(Button)findViewById(R.id.close);
       error=(TextView)findViewById(R.id.error);


	   txtRegId = (TextView) findViewById(R.id.txt_idFirebase);
	   txtMessage = (TextView) findViewById(R.id.txt_FirebaseMessage);
	   mRegistrationBroadcastReceiver = new BroadcastReceiver() {
		   @Override
		   public void onReceive(Context context, Intent intent) {

			   // checking for type intent filter
			   if (intent.getAction().equals(ConfigFirebase.REGISTRATION_COMPLETE)) {
				   // gcm successfully registered
				   // now subscribe to `global` topic to receive app wide notifications
				   FirebaseMessaging.getInstance().subscribeToTopic(ConfigFirebase.TOPIC_GLOBAL);

				   displayFirebaseRegId();
//				   new sendToken().execute();

			   } else if (intent.getAction().equals(ConfigFirebase.PUSH_NOTIFICATION)) {
				   // new push notification is received

				   String message = intent.getStringExtra("message");

				   Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

				   txtMessage.setText(message);
			   }
		   }
	   };
	   displayFirebaseRegId();

	   new android.os.Handler().postDelayed(new Runnable() {
		   @Override
		   public void run() {
			   new cekConnectionPerformanceFriebaseAsync().execute();
		   }
	   },500);


       keluar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
       usdb = new  UserAdapter(MainActivity.this);

//       login.setOnClickListener(new View.OnClickListener() {
//
//       	public void onClick(View v) {
//       		progressBar = new ProgressDialog(MainActivity.this);
//		    progressBar.setCancelable(false);
//		    progressBar.setMessage("Loading...");
//		    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);;
//
//
//
//       		if (un.getText().toString().equals(sdayNow) && pw.getText().toString().equals(sdayNow2))  {
//       			finish();
//       			if (HostUrl.equals("")) {
//       				url.setData("");
//       			}
//       			else {
//       				url.setData(HostUrl + "/android/");
//       			}
//       			username.setData(sdayNow);
//       			conn.setData("Offline");
//       			Intent a = new Intent (MainActivity.this,setup_menu.class);
//   				startActivity(a);
//   				return;
//       		}
//
//       		// MyFile file = new MyFile(v.getContext());
//
//
//               String urlphp ;
//               //String[] dataini;
//               urlphp = "";
//
//               Log.d("debug","host terbaca -> " + HostUrl);
//
//               if (HostUrl.equals("")) {
//                  //urlphp = "http://www.atri-xpress.co.id/android/";
//                  Log.d("debug","tidak ada host " + HostUrl);
//                  setUrl(v);
//	        		un.setText("");
//	        		pw.setText("");
//                  return;
//                 }
//               else
//               {
////                urlphp = HostUrl + "/android/";
//            	   urlphp = "http://" + HostUrl + "/android/";			//8-18-2017		Yosua
//
//            	   if (urlphp.equals("http://43.252.144.14:81/android/")) {
//						Log.i("Debug", "Halaman Multi Scan PO Outstanding "
//								+ "Replace URL Lokal Server");
//						STR_url_login = urlphp.replace(
//								"http://43.252.144.14:81/android/",
//								"http://43.252.144.14:81/clear_mobile");
//					} else if (urlphp
//							.equals("http://api-mobile.atex.co.id/android/")) {
//						Log.i("Debug", "Halaman Login "
//								+ "Replace URL Online Server");
//						STR_url_login = urlphp.replace(urlphp,
//								"http://api-mobile.atex.co.id/clear_mobile");
//					} else {
//						Toast.makeText(getApplicationContext(),
//								"Server Tidak Tersedia", 0).show();
//					}
//					STR_url_login = urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/clear_mobile");
//					Log.i("Debug", "Halaman Login " +"Test String Replace URL " + STR_url_login);
//
//               }
//               //urlphp = "http://192.168.6.44/android/";
//
//               /*try {
//               	urlphp = file.readini();
//               	dataini = urlphp.split("=");
//					urlphp = dataini[1].trim() ;
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//					error.setText("file konfigurasi tidak ada !!");
//				}*/
//
//               url.setData(urlphp);
//
//               ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//       		postParameters.add(new BasicNameValuePair("username", un.getText().toString()));
//       		postParameters.add(new BasicNameValuePair("password", pw.getText().toString()));
//
//       		String response = null;
//				UN =  un.getText().toString();
//				PW = pw.getText().toString();
//				error.setText("Loading ..");
//       		try {
//       			progressBar.show();
//       			response = CustomHttpClient.executeHttpPost(urlphp + "cek_user.php", postParameters);
//
//       			String res = response.toString();
//
//       			String hasil = res;
//       			String[] separated = hasil.split("--");
//       			String stat = separated[0].trim();
//       			String lcpk = separated[1].trim();
//       			String unik = separated[2].trim();
//
//       			//set id unik
//				IdUnik obidunik = IdUnik.getInstance();
//				obidunik.setData(unik.trim());
//
//				Log.d("ID UNIK", unik.trim());
//
//       			Log.d("cek user", "respon = " + res + " - " + response.toString());
//       			res = res.trim();
//       			res = res.replaceAll("\\s+","");
//       			error.setText(res);
//
//       			if (stat.trim().equals("1"))
//       			 {
//
//       				error.setText("Username dan Password benar");
//    				locpk.setData(lcpk.trim());
//
//    				Log.d("locpk", lcpk.trim());
//
//    				conn.setData("Online");
//
//    				username.setData(UN);
//
//    				UN = UN.toUpperCase();
//    				Log.d("user",UN);
//
//    				if (UN.equalsIgnoreCase("ADMIN")){
//    					Log.d("user","true " + UN);
//    					show_dialog(v);
//    				}
//    				else
//    				{
//    					usdb.open();
//    					Log.d("user","false " + UN);
//     					UserKurirFirebase usr = new UserKurirFirebase();
//		    			usr.setUsername(UN);
//		    			usr.setPassword(PW);
//		    			usr.setLocation(lcpk);
//		    			usdb.createContact(usr);
//		    			usdb.close();
//		    			String user = UN;
//
//		    			session.createLoginSession(user,urlphp,"online",lcpk);
//		    			Intent myIntent = new Intent (MainActivity.this, menu_utama.class);
//			       		startActivity(myIntent);
//
//			       		finish();
//
//    					//main_menu(v);
//    					//cek_pod(v);
//    				}
//
//       			 }
//       			 else {
//       				error.setText("Sorry!! Username or Password salah");
//       			 }
//
//       			}
//      		 	catch (Exception e) {
//      		 	    Toast.makeText(v.getContext(), "Koneksi Terputus", Toast.LENGTH_SHORT).show();
//      		 		error.setText("koneksi terputus, offline mode on." + e.toString());
//      		 		Log.d("debug","koneksi ngaco " + HostUrl);
//      		 		userdb.open();
//
//      		 		String pass,loc;
//	       		 	Cursor cur = userdb.getSingleContact(UN);
//	       			if (cur != null)  {
//	       				cur.moveToFirst();
//	       				if (cur.getCount() > 0) {
//
//	       					pass = cur.getString(cur.getColumnIndexOrThrow(UserAdapter.PASSWD));
//	       					//Log.d("debug","pass " + pass);
//
//	       					loc = cur.getString(cur.getColumnIndexOrThrow(UserAdapter.LOCATION));
//	       					if (pass.equals(pw.getText().toString()))
//	       					{
//	       						locpk.setData(loc);
//	       						username.setData(UN);
//
//	             				UN = UN.toUpperCase();
//	             				Log.d("user",UN);
//
//	             				conn.setData("Offline");
//
//	             				if (UN.equalsIgnoreCase("ADMIN")){
//	             					Log.d("user","true " + UN);
//	             					show_dialog(v);
//	             				}
//	             				else
//	             				{
//	             					username.setData(UN);
//	             					Log.d("user","false " + UN);
//	            			    	//cek_pod(v);
//	             					String user = UN;
//
//	             					session.createLoginSession(user,urlphp,"offline",loc);
//	             					Intent myIntent = new Intent (MainActivity.this, menu_utama.class);
//	            		       		startActivity(myIntent);
//
//	            		       		finish();
//	             					//new CheckVersion().execute();
//	             					//main_menu(v);
//	            			    }
//	       					}
//	       					else
//	       					{
//	       						LoginFail("Password Salah");
//	       						Log.d("data local","Password Salah");
//	       					}
//	       				}
//	       				else
//	       				{
//	       					LoginFail("UserKurirFirebase tidak ditemukan");
//	       					Log.d("data local","UserKurirFirebase tidak ditemukan");
//	       				}
//	       			}
//	       			else
//	       			{
//	       				LoginFail("UserKurirFirebase tidak ditemukan");
//	       				Log.d("data local","Tidak ada data");
//	       			}
//      		 		userdb.close();
//      		 		}
//      		 	//error.setText("");
//       		progressBar.dismiss();
//       		un.setText("");
//       		pw.setText("");
//       	}
//       });

       BTN_login_user.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(),"Test Button", Toast.LENGTH_LONG).show();
				String Str_IdKurir = un.getText().toString();
				String Str_PassKurir = pw.getText().toString();
				Log.i("Debug","Username = " + Str_IdKurir + " - Password = " + Str_PassKurir);

				if(TextUtils.isEmpty(Str_IdKurir)) {
					un.setError("Username tidak boleh kosong");
			        return;
			     }
			    else if (TextUtils.isEmpty(Str_PassKurir)) {
			    	pw.setError("Password tidak boleh kosong");
			        return;
			     }
			    else{
					new android.os.Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							new loginAsync().execute();
						}
					},500);

//					new android.os.Handler().postDelayed(new Runnable() {
//						@Override
//						public void run() {
//							new sendToken().execute();
//						}
//					},500);
				}
			}
		});

       login_user.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				progressBar = new ProgressDialog(MainActivity.this);
				progressBar.setCancelable(false);
				progressBar.setMessage("Loading...");
				progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

				if (un.getText().toString().equals(sdayNow)
						&& pw.getText().toString().equals(sdayNow2)) {
					finish();
					if (HostUrl.equals("")) {
						url.setData("");
					} else {
						url.setData(HostUrl + "/android/");
					}
					username.setData(sdayNow);
					conn.setData("Offline");
					Intent a = new Intent(MainActivity.this, setup_menu.class);
					startActivity(a);
					return;
				}

				String urlphp;
				urlphp = "";

				Log.d("debug", "host terbaca -> " + HostUrl);

				if (HostUrl.equals("")) {
					Log.d("debug", "tidak ada host " + HostUrl);
					setUrl(v);
					un.setText("");
					pw.setText("");
					return;
				} else {
					urlphp = "http://" + HostUrl + "/android/";
					Log.d("Debug", "Host URL " + urlphp);

//					if (urlphp.equals("http://43.252.144.14:81/android/")) {
//						Log.i("Debug", "Halaman Multi Scan PO Outstanding "
//								+ "Replace URL Lokal Server");
//						STR_url_login = urlphp.replace(
//								"http://43.252.144.14:81/android/",
//								"http://43.252.144.14:81/clear_mobile/login");
//					} else if (urlphp
//							.equals("http://api-mobile.atex.co.id/android/")) {
//						Log.i("Debug", "Halaman Login "
//								+ "Replace URL Online Server");
//						STR_url_login = urlphp.replace(urlphp,
//								"http://api-mobile.atex.co.id/clear_mobile/login");
//					} else {
//						Toast.makeText(getApplicationContext(),
//								"Server Tidak Tersedia", 0).show();
//					}
//					STR_url_login = urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/clear_mobile/login");
//					Log.i("Debug", "Halaman Login " +"Test String Replace URL " + STR_url_login);

					if (urlphp.equals("http://43.252.144.14:81/android/")) {
						Log.i("Debug", "Halaman MainActivity "+ "Replace URL Lokal Server");
						STR_url_login = urlphp.replace("http://43.252.144.14:81/android/","http://43.252.144.14:81/compact_mobile");
					} else if (urlphp.equals("http://compact.atex.co.id/android/")) {
						Log.i("Debug", "Halaman MainActivity "+ "Replace URL Online Server");
						STR_url_login = urlphp.replace(urlphp,"http://compact.atex.co.id/compact_mobile");
					}
					else if (urlphp.equals("http://apiatrex.alfatrex.id/android/")) {
						Log.i("Debug", "Halaman MainActivity "+ "Replace URL Online Server New Compact Mobile - Second API");
						STR_url_login = urlphp.replace(urlphp,"http://apiatrex.alfatrex.id/compact_mobile");
					}
					else if (urlphp.equals("http://api.alfatrex.id/android/")) {
						Log.i("Debug", "Halaman MainActivity "+ "Replace URL Online Server New Compact Mobile");
						STR_url_login = urlphp.replace(urlphp,"http://api.alfatrex.id/compact_mobile");
					}
					else {
						Toast.makeText(getApplicationContext(),"Server Tidak Tersedia", Toast.LENGTH_LONG).show();
					}
//					STR_url_login = urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/clear_mobile/login");
					Log.i("Debug", "Halaman Login " +"Test String Replace URL " + STR_url_login);

				}
				url.setData(urlphp);

				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("username", un
						.getText().toString()));
				postParameters.add(new BasicNameValuePair("password", pw
						.getText().toString()));

				String response = null;
				UN = un.getText().toString();
				PW = pw.getText().toString();
				error.setText("Loading ..");

				Str_username = UN;
				Str_password = PW;
				Log.d("Debug", "Isi Login"
				+ " Username " + Str_username
				+ " Password " + Str_password);

				try {
					progressBar.show();
//					cekLogin(Str_username, Str_password);
					update_login();
				} catch (Exception e) {
					Toast.makeText(v.getContext(), "Koneksi Terputus",
							Toast.LENGTH_SHORT).show();
					error.setText("koneksi terputus, offline mode on."
							+ e.toString());
					Log.d("debug", "koneksi ngaco " + HostUrl);
					userdb.open();

					String pass, loc;
					Cursor cur = userdb.getSingleContact(UN);
					if (cur != null) {
						cur.moveToFirst();
						if (cur.getCount() > 0) {

							pass = cur.getString(cur
									.getColumnIndexOrThrow(UserAdapter.PASSWD));
							// Log.d("debug","pass " + pass);

							loc = cur.getString(cur
									.getColumnIndexOrThrow(UserAdapter.LOCATION));
							if (pass.equals(pw.getText().toString())) {
								locpk.setData(loc);
								username.setData(UN);
								UN = UN.toUpperCase();
								Log.d("user", UN);

								conn.setData("Offline");

								if (UN.equalsIgnoreCase("ADMIN")) {
									Log.d("user", "true " + UN);
									show_dialog(v);
								} else {
									username.setData(UN);
									Log.d("user", "false " + UN);
									// cek_pod(v);
									String user = UN;

									sessionMan.createLoginSession(user, urlphp,
											"offline", loc);
									Intent myIntent = new Intent(
											MainActivity.this, menu_utama.class);
									startActivity(myIntent);

									finish();
								}
							} else {
								LoginFail("Password Salah");
								Log.d("data local", "Password Salah");
							}
						} else {
							LoginFail("UserKurirFirebase tidak ditemukan");
							Log.d("data local", "UserKurirFirebase tidak ditemukan");
						}
					} else {
						LoginFail("UserKurirFirebase tidak ditemukan");
						Log.d("data local", "Tidak ada data");
					}
					userdb.close();
				}
				// error.setText("");
				progressBar.dismiss();
				un.setText("");
				pw.setText("");
			}
		});

       }

	private void displayFirebaseRegId() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(ConfigFirebase.SHARED_PREF, 0);
		String regId = pref.getString("regId", null);

		Log.e(TAG, "Firebase reg id: " + regId);

		if (!TextUtils.isEmpty(regId))
			txtRegId.setText("Firebase Reg Id: " + regId);
		else
			txtRegId.setText("Firebase Reg Id is not received yet!");
	}

	@Override
	protected void onResume() {
		super.onResume();

		// register GCM registration complete receiver
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(ConfigFirebase.REGISTRATION_COMPLETE));

		// register new push message receiver
		// by doing this, the activity will be notified each time a new message arrives
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(ConfigFirebase.PUSH_NOTIFICATION));

		// clear the notification area when the app is opened
		NotificationUtils.clearNotifications(getApplicationContext());
	}

   public class loginAsync extends AsyncTask<String, String, String>
	{
	   ProgressDialog pDialog;
	   String Str_IdKurir = un.getText().toString();
	   String Str_PassKurir = pw.getText().toString();

	   String responsecode, responsemessage;
	   String SessionIdKurir, SessionLocPk, SessionNameKurir;
        String SessionPrefIdKurir, SessionPrefCodeStore, SessionPrefNameStore, SessionPrefAddressStore, SessionPrefLocStore, SessionPrefMapi, SessionPrefWebview;

		SharedPreferences pref = getApplicationContext().getSharedPreferences(ConfigFirebase.SHARED_PREF, 0);
		String regId = pref.getString("regId", null);

	   protected void onPreExecute(){
			super.onPreExecute();

			pDialog = new ProgressDialog(MainActivity.this);
	        pDialog.setMessage("Loading...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        pDialog.show();

//		   checkFirebaseDB();
	   }

	   @Override
		protected String doInBackground(String... params) {
		   HttpURLConnection connection;
	       OutputStreamWriter request = null;
	       URL url = null;
           String URI = null;
           String response = null;
           String parameters = "username="+Str_IdKurir+"&password="+Str_PassKurir+"&id_firebase="+regId;
		   Log.d("Debug", "Parameter Login -> " + parameters);

           try
           {
        	   String urlphp;
        	   urlphp = "";
        	   Log.d("Debug", "Host Server -> " + HostUrl);
//        	   url = new URL("http://"+HostUrl+Str_login_user);
			   url = new URL("https://"+HostUrl+Str_login_user);
        	   Log.d("Debug","Test URL Login " + url);

//        	   String urlphpRulesLama = "http://" + HostUrl + "/android/";
			   String urlphpRulesLama = "https://" + HostUrl + "/android/";
        	   Log.d("Debug", "Host URL Rules Lama " + urlphpRulesLama);

        	   	if (urlphpRulesLama.equals("http://43.252.144.14:81/android/")) {
					Log.i("Debug", "Halaman MainActivity "+ "Replace URL Lokal Server");
					STR_url_login = urlphpRulesLama.replace("http://43.252.144.14:81/android/","http://43.252.144.14:81/compact_mobile");
				} else if (urlphpRulesLama.equals("http://compact.atex.co.id/android/")) {
					Log.i("Debug", "Halaman MainActivity "+ "Replace URL Online Server");
					STR_url_login = urlphp.replace(urlphpRulesLama,"http://compact.atex.co.id/compact_mobile");
				}
				else if (urlphpRulesLama.equals("http://apiatrex.alfatrex.id/android/")) {
					Log.i("Debug", "Halaman MainActivity "+ "Replace URL Online Server New Compact Mobile - Second API");
					STR_url_login = urlphp.replace(urlphpRulesLama,"http://apiatrex.alfatrex.id/compact_mobile");
				}
				else if (urlphpRulesLama.equals("http://api.alfatrex.id/android/")) {
					Log.i("Debug", "Halaman MainActivity "+ "Replace URL Online Server New Compact Mobile");
					STR_url_login = urlphpRulesLama.replace(urlphpRulesLama,"http://api.alfatrex.id/compact_mobile");
				}
				else if (urlphpRulesLama.equals("http://apicompact.alfatrex.id/android/")) {
					Log.i("Debug", "Halaman MainActivity "+ "Replace URL Production Https");
					STR_url_login = urlphpRulesLama.replace(urlphpRulesLama,"http://apiscompact.alfatrex.id/compact_mobile");
				}
				else if (urlphpRulesLama.equals("http://stg.apicompact.alfatrex.id/android/")) {
					Log.i("Debug", "Halaman MainActivity "+ "Replace URL Staging Https");
					STR_url_login = urlphpRulesLama.replace(urlphpRulesLama,"http://stgscompact.alfatrex.id/compact_mobile");
				}
                else if (urlphpRulesLama.equals("http://apiscompact.alfatrex.id/android/")) {
                    Log.i("Debug", "Halaman MainActivity "+ "Replace Production Http To Https");
                    STR_url_login = urlphpRulesLama.replace(urlphpRulesLama,"https://apiscompact.alfatrex.id/compact_mobile");
                }
                else if (urlphpRulesLama.equals("http://stgscompact.alfatrex.id/android/")) {
                    Log.i("Debug", "Halaman MainActivity "+ "Replace Staging Http To Https");
                    STR_url_login = urlphpRulesLama.replace(urlphpRulesLama,"https://stgscompact.alfatrex.id/compact_mobile");
                }
                else if (urlphpRulesLama.equals("https://apiscompact.alfatrex.id/android/")) {
                    Log.i("Debug", "Halaman MainActivity "+ "Replace Production - Https Valid");
                    STR_url_login = urlphpRulesLama.replace(urlphpRulesLama,"https://apiscompact.alfatrex.id/compact_mobile");
                }
                else if (urlphpRulesLama.equals("https://stgscompact.alfatrex.id/android/")) {
                    Log.i("Debug", "Halaman MainActivity "+ "Replace Staging - Https Valid");
                    STR_url_login = urlphpRulesLama.replace(urlphpRulesLama,"https://stgscompact.alfatrex.id/compact_mobile");
                }
				else {
					Log.i("Debug", "Halaman MainActivity "+ "Server Tidak Tersedia");
//					Toast.makeText(getApplicationContext(),"Server Tidak Tersedia", 0).show();
				}
//				STR_url_login = urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/clear_mobile/login");
				Log.i("Debug", "Halaman Login " +"Test String Replace URL " + STR_url_login);

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

//               JSONObject json = new JSONObject(sb.toString()).getJSONObject("data");
//               SessionIdKurir = json.getString("muse_code");
//               SessionLocPk = json.getString("muse_mloc_pk");
//               SessionNameKurir = json.getString("muse_name");
//               session.createLoginSessionNew(SessionIdKurir,SessionLocPk,SessionNameKurir);
//
//               isr.close();
//               reader.close();

               JSONObject jsonObjectLogin = new JSONObject(sb.toString());
               JSONArray ListObjData = jsonObjectLogin.getJSONArray("data");
               RespnseMessage = jsonObjectLogin.getString("response_message");
               RespnseCode = jsonObjectLogin.getString("response_code");
               for (int i = 0; i < ListObjData.length(); i++) {
            	   JSONObject obj = ListObjData.getJSONObject(i);
					String STR_CourierCode = obj.getString("muse_code");
					String STR_CourierLocPk = obj.getString("muse_mloc_pk");
					String STR_CourierName = obj.getString("muse_name");
					SessionIdKurir = obj.getString("muse_code");
					SessionLocPk = obj.getString("muse_mloc_pk");
					SessionNameKurir = obj.getString("muse_name");
					sessionMan.createLoginSessionNew(SessionIdKurir,SessionLocPk,SessionNameKurir);

                   SessionPrefIdKurir = obj.getString("muse_code");
				   SessionPrefCodeStore = obj.getString("muse_code_store");
                   SessionPrefNameStore = obj.getString("muse_nama_toko");
                   SessionPrefAddressStore = obj.getString("muse_alamat_toko");
                   SessionPrefLocStore = obj.getString("muse_latlong_toko");
				   SessionPrefMapi = obj.getString("muse_mapi");
				   SessionPrefWebview = obj.getString("muse_webview");
//                   sessionManDetailStore.createKurirStore(SessionPrefIdKurir,SessionPrefNameStore,SessionPrefAddressStore,SessionPrefLocStore);
				   sessionManDetailStore.createKurirStore(SessionPrefIdKurir,SessionPrefCodeStore,SessionPrefNameStore,SessionPrefAddressStore,SessionPrefLocStore,SessionPrefMapi,SessionPrefWebview);

					isr.close();
					reader.close();
               }
           }
           catch(IOException e)
           {
               Log.d("Debug","Trace = ERROR ");
               Log.d("Debug","response_code  " + STR_ResponseCodeLogin);

           }
           catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
           }

		   return null;
	   }

	   @Override
     protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
           super.onPostExecute(result);

           if(isFinishing()){
               return;
           }
           pDialog.dismiss();

           String Response_doInBackground = SessionNameKurir;
           Log.d("Debug","QWERTY = " + Response_doInBackground);

           Log.d("Debug","ResponseCode PostExecute = " + responsecode +" || "+ "ResponseMessage = "+responsemessage);
           Log.d("Debug","JsonObject PostExecute " +" IDKurir = "+ SessionIdKurir +" || "+ "NamaKurir = "+SessionNameKurir +" || "+ "LockPk = "+SessionLocPk);

           if (responsecode == null) {
           	Toast.makeText(MainActivity.this, "Login gagal", Toast.LENGTH_LONG).show();
           	return;
					 }

			if (!responsecode.equals("1")) {
//           if (RespnseCode == "1") {
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.toast_custom_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
				final Toast toast = new Toast(getApplicationContext());
        		TextView tv = (TextView) layout.findViewById(R.id.toast);
        		tv.setText(responsemessage);
                tv.setHeight(100);
                tv.setWidth(200);
                //Set toast gravity to bottom
        		toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        		//Set toast duration
//        		toast.setDuration(5);
				toast.setDuration(Toast.LENGTH_LONG);

        		//Set the custom layout to Toast
        		toast.setView(layout);
        		//Display toast
        		toast.show();
        		new CountDownTimer(1000, 1000){
        		    public void onTick(long millisUntilFinished) {toast.show();}
        		    public void onFinish() {toast.show();}
        		}.start();
        		un.setText("");
        		pw.setText("");
//        		Toast.makeText(getApplicationContext(), responsemessage, 0).show();
			} else {
				String user = UN;
				String IDKurir = Str_IdKurir;
//				String urlphp2 = "http://" + HostUrl + "/android/";
//				String urlphp2 = "http://" + HostUrl;
				String urlphp2 = "https://" + HostUrl;
				Log.d("Debug", "Host URL onPostExecute "+urlphp2);
				sessionMan.createLoginSession(IDKurir, urlphp2, "online", SessionLocPk);
				sessionManDetailStore.createKurirStore(SessionPrefIdKurir,SessionPrefCodeStore,SessionPrefNameStore,SessionPrefAddressStore,SessionPrefLocStore,SessionPrefMapi,SessionPrefWebview);
				Toast.makeText(getApplicationContext(),"Selamat Datang kembali Kurir "+ SessionNameKurir, Toast.LENGTH_LONG).show();
				Intent a = new Intent(MainActivity.this, menu_utama.class);
				startActivity(a);
                finish();
			}
	   }
	}

	public class sendToken extends AsyncTask<String, String, String> {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(ConfigFirebase.SHARED_PREF, 0);
		String regId = pref.getString("regId", null);

		ProgressDialog pDialog;
		String Str_IdKurir = un.getText().toString();
		String responsecode, responsemessage;

		protected void onPreExecute() {
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
		}

		protected String doInBackground(String... params) {
			HttpURLConnection connection;
			OutputStreamWriter request = null;
			URL url = null;
			String URI = null;
			String response = null;
			String parameters = "kode_kurir="+Str_IdKurir+"id_firebase="+regId;

			try
			{
				String urlphp;
				urlphp = "";
				Log.d("debug", "Host Server -> " + HostUrl);
//				url = new URL("http://"+HostUrl+Str_update_IDFirebase);
				url = new URL("https://"+HostUrl+Str_update_IDFirebase);
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
				Log.d("Debug","response_code  " + STR_ResponseCodeUpdateToken);

			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (!responsecode.equals("1")) {
				Toast.makeText(getApplicationContext(),responsemessage, Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(getApplicationContext(),responsemessage, Toast.LENGTH_SHORT).show();
			}
		}
	}

   public void update_login() {
		Log.d("Debug", "Update Data Login");

		Str_username = UN;
		Str_password = PW;
		Log.d("Debug", "Isi Login" + " Username " + Str_username + " Password "
				+ Str_password);

		String response = null;

		ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
		masukparam1.add(new BasicNameValuePair("username", Str_username));
		masukparam1.add(new BasicNameValuePair("password", Str_password));

		String response1;

		try {
			Log.d("Debug", "Update Data Login "+"Lewat Try");
			cekLogin(Str_username, Str_password);
			Log.d("Debug","Trace String");
//			savetoLocalDBLogin(Str_username, Str_password);
		} catch (Exception e) {
			Log.d("Debug", "Update Data Login "+"Catch");
			Log.d("Gagal", e.toString());
			// TODO Auto-generated catch block

			String user = UN;
//			String urlphp2 = "http://" + HostUrl + "/android/";
			String urlphp2 = "https://" + HostUrl + "/android/";
			Log.d("Debug", "Host URL 2 "+urlphp2);
			sessionMan.createLoginSession(user, urlphp2, "online", Str_rspn_lcpk);
			Intent myIntent = new Intent(MainActivity.this, menu_utama.class);
			startActivity(myIntent);
			finish();

			e.printStackTrace();
			displayExceptionMessage(e.getMessage());
//			savetoLocalDBLogin(Str_username, Str_password);
		}
	}

//   private void savetoLocalDBLogin(String Str_username, String Str_password) {
//		Log.d("Debug", "SaveLocalDBLogin");
//		Str_username = UN;
//		Str_password = PW;
//		Log.d("Debug", "Isi Login" + " Username " + Str_username + " Password "
//				+ Str_password);
//
//		UserKurirFirebase usr = new UserKurirFirebase();
//		usr.setUsername(Str_username);
//		usr.setPassword(Str_password);
//		usr.setLocation(Str_lcpk);
//		usdb.createContact(usr);
//		usdb.close();
//	}

   private void displayExceptionMessage(String message) {
	// TODO Auto-generated method stub

}

public void cekLogin(String Str_username, String Str_password)
			throws JSONException {
		HttpURLConnection connection;
		OutputStreamWriter request = null;
		URL url = null;
		String URI = null;
		String response = null;
		String parameters = "username=" + Str_username + "&password="
				+ Str_password;
		Log.d("Debug", "Parameters " + parameters);
		try {
//			url = new URL(STR_url_login);
			String url_login = STR_url_login +"/login";
			url = new URL(url_login);
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
			JSONArray ListObjLogin = json.getJSONArray("data");
			Log.d("Debug", "Trace Customer = " + json);
			RespnseCode = json.getString("response_code");
			RespnseMessage = json.getString("response_message");
			Log.d("Debug", "Response Code = " + RespnseCode);
			Log.d("Debug", "Response Message  = " + RespnseMessage);
			for (int i = 0; i < ListObjLogin.length(); i++) {
				JSONObject obj = ListObjLogin.getJSONObject(i);
//			JSONObject json_data = new JSONObject(sb.toString()).getJSONObject("data");
				Str_rspn_code = obj.getString("muse_code");
				Str_rspn_lcpk = obj.getString("muse_mloc_pk");
				Str_rspn_muse_name = obj.getString("muse_name");

				User usr = new User();
				usr.setUsername(Str_username);
				usr.setPassword(Str_password);
				usr.setLocation(Str_rspn_lcpk);
				usdb.createContact(usr);
				usdb.close();
			}
			if (RespnseCode.toString().trim().length() == 1) {
				Toast.makeText(getApplicationContext(),"Login Berhasil", Toast.LENGTH_LONG).show();
//				String user = UN;
//				session.createLoginSession(user, STR_url_login, "online",
//						Str_rspn_lcpk);
//				Intent myIntent = new Intent(MainActivity.this,
//						menu_utama.class);
//				startActivity(myIntent);

				finish();
			} else {
				Toast.makeText(getApplicationContext(),"Login Gagal, Mohon Periksa Kembali", Toast.LENGTH_LONG).show();
			}
		} catch (IOException e) {
			// Error
			Log.d("Debug", "Trace = ERROR ");
		}
	}

  public void LoginFail(String e){
	   Toast.makeText(this.getBaseContext(), e, Toast.LENGTH_SHORT).show();
	   error.setText("Login fail ..." + e);
  }

  public void setUrl (View theButton)
    {
      finish();
      Intent a = new Intent (MainActivity.this,SettingHost.class);
	   startActivity(a);
    }

   public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main, menu);
       return true;
   }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// respond to menu item selection
		switch (item.getItemId()) {
		case R.id.sethost:
			Intent a = new Intent(MainActivity.this, SettingHost.class);
			startActivity(a);
			finish();

			return true;

		case R.id.close:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

   public void show_dialog(View theButton){

	AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
	        MainActivity.this);

	// Setting Dialog Title
	alertDialog2.setTitle("Login Succes");

	// Setting Dialog Message
	alertDialog2.setMessage("Please select ?");

	// Setting Icon to Dialog
	//alertDialog2.setIcon(R.drawable.delete);

	// Setting Positive "Yes" Btn
	alertDialog2.setPositiveButton("Setup",
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                finish();
   				Intent a = new Intent (MainActivity.this,setup_menu.class);
   				startActivity(a);
	            }
	        });
	// Setting Negative "NO" Btn
	alertDialog2.setNegativeButton("Main Menu",
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                finish();
   				Intent a = new Intent (MainActivity.this,menu_utama.class);
   				startActivity(a);
	            }
	        });
	alertDialog2.show();
}

	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
		super.onPause();
	}

//	public void checkFirebaseDB() {
//		String Str_IdKurir = un.getText().toString();
//		String Str_PassKurir = pw.getText().toString();
//		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
//		Date date = new Date();
//		String Str_date_time = dateformat.format(date);
//		final UserKurirFirebase user_kurir_firebase = (UserKurirFirebase) getIntent().getSerializableExtra("data");
//		SharedPreferences pref = getApplicationContext().getSharedPreferences(ConfigFirebase.SHARED_PREF, 0);
//		String Str_regId = pref.getString("regId", null);
//		Log.i("Debug","Check Firebase DB" + " Username = " + Str_IdKurir + " - Password = " + Str_PassKurir + " - IDFirebase = " + Str_regId + " - Date Now = " + Str_date_time);
//
//		if (TextUtils.isEmpty(userId)) {
//			createUser(Str_IdKurir, Str_PassKurir, Str_regId, Str_date_time);
//		} else {
//			updateUser(user_kurir_firebase);
//		}
//
//	}
//
//	private void createUser(String Str_IdKurir, String Str_PassKurir, String regId, String Str_date_time) {
//		// TODO
//		// In real apps this userId should be fetched
//		// by implementing firebase auth
//		if (TextUtils.isEmpty(userId)) {
//			userId = mFirebaseDatabase.push().getKey();
//		}
//
//		UserKurirFirebase user_kurir_firebase = new UserKurirFirebase(Str_IdKurir, Str_PassKurir, regId, Str_date_time);
//
//		mFirebaseDatabase.child(Str_IdKurir).setValue(user_kurir_firebase);
//
//		addUserChangeListener(Str_IdKurir);
//	}
//
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
//				Log.e(TAG, "User data is changed!" + user_kurir_firebase.Str_IdKurir + ", " + user_kurir_firebase.Str_PassKurir + ", " + user_kurir_firebase.Str_regId+ ", " + user_kurir_firebase.Str_date_time);
//
//				// clear edit text
//				un.setText("");
//				pw.setText("");
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
//
//	private void updateUser(UserKurirFirebase user_kurir_firebase) {
//       if(user_kurir_firebase == null){
//           return;
//       }
//		Log.i("Debug","Lewat Update Firebase Kurir");
//		/**
//		 * Baris kode yang digunakan untuk mengupdate data barang
//		 * yang sudah dimasukkan di Firebase Realtime Database
//		 */
//		mFirebaseDatabase.child("users_mobile_agent") //akses parent index, ibaratnya seperti nama tabel
//				.child(user_kurir_firebase.getKey()) //select barang berdasarkan key
//				.setValue(user_kurir_firebase) //set value barang yang baru
//				.addOnSuccessListener(this, new OnSuccessListener<Void>() {
//					@Override
//					public void onSuccess(Void aVoid) {
//					}
//				});
//	}

	public class cekConnectionPerformanceFriebaseAsync extends AsyncTask<String, String, String>
	{
//		ProgressDialog pDialog;

		protected void onPreExecute() {
			super.onPreExecute();
			Log.e("Debug", "Cek Performance");

			if (MainActivity.this.isFinishing()) {
				Log.e("Debug", "Cek Activity Status");
				return;
			}
//			pDialog = new ProgressDialog(MainActivity.this);
//			pDialog.setMessage("Loading...");
//			pDialog.setIndeterminate(false);
//			pDialog.setCancelable(false);
//			pDialog.show();

		}
		@Override
		protected String doInBackground(String... params) {
			HttpURLConnection connection;
			OutputStreamWriter request = null;
			URL url = null;
			String URI = null;
//            String response = null;
//            String parameters = "username="+Str_IdKurir+"&password="+Str_md5PassKurir+"&id_firebase="+regId;
//            Log.d("Debug", "Parameter Login -> " + parameters);

			try
			{
				String Str_urlphp;
				Str_urlphp = "";
				Log.d("debug", "Host Server -> " + HostUrl);
//				url = new URL("http://"+HostUrl+STR_path_cek_connection);
				url = new URL("https://"+HostUrl+STR_path_cek_connection);
				Log.d("Debug","Test URL Cek Connection " + url);

				Trace myTrace = FirebasePerformance.getInstance().newTrace(HostUrl);
				myTrace.start();

				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestMethod("POST");

				request = new OutputStreamWriter(connection.getOutputStream());
				//Log.d("Debug","request = " + request);
//                request.write(parameters);
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
				STR_responsecodeConnection = jsonObject.getString("response_code");
				STR_responsemessageConnection = jsonObject.getString("response_message");

				connection.disconnect();
				myTrace.stop();

				if(HostUrl != null){

				}else{

				}
			}
			catch(IOException e){
				Log.d("Debug","Trace = ERROR ");

			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			pDialog.dismiss();
		}
	}

}
