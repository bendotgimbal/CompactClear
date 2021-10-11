package compact.mobile.SuratJalan.Scan;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import compact.mobile.Base64;
import compact.mobile.BuildConfig;
import compact.mobile.SessionManager;
import compact.mobile.SuratJalan.Scan.testingScan.MyFileContentProvider;
import compact.mobile.gps_tracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import compact.mobile.R;
import compact.mobile.SuratJalan.DB.C_Finish_PoOutstanding;
import compact.mobile.SuratJalan.DB.C_PoOutstanding;
import compact.mobile.SuratJalan.DB.ListFinishPoOutstandingDBAdapter;
import compact.mobile.SuratJalan.DB.ListPoOutstandingDBAdapter;
import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;
import compact.mobile.service.AWSUploadService;
import id.zelory.compressor.Compressor;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

//public class multiScanPoOutstanding extends Activity {
public class multiScanPoOutstanding extends Activity implements View.OnClickListener {
	//09-08-2017
	private static final int CAMERA_REQUEST = 1888;
	public String STR_foto="";
	public String STR_replace="";
	TextView TV_no_assigment, TV_no_awb, TV_response_code, TV_PersentaseUpload;
	EditText ET_waybill;
	Button BTN_foto, BTN_submit;
	ImageView IV_foto;
	String STR_date_time, STR_lat_long;
	String STR_no_assigment, STR_no_awb;
	String STR_URL_API, STR_url_po_outstanding, STR_url_po_outstanding_finishing;
	String Str_sp_url_scan_PO, Str_sp_url_scan_finish_PO;
	String STR_ewaybill, STR_waybill, STR_username, STR_locpk, STR_no_po;
	String RespnseMessage, RespnseCode, RespnseMessageFinishPO, RespnseCodeFinishPO, RespnseDebug, STR_response_code;
	String STR_FileNamePhoto, STR_FullFileNamePhoto, STR_DateTime, STR_Date, STR_Time;
	String STR_CheckConnectionPOFinish = null;
    String STR_StatusResponseCodePO;
	public String Str_lo_Koneksi, Str_LinkScanPOOutstanding, Str_LinkClosePOOutstanding;
	ProgressBar PB_PersentaseUpload;
	FrameLayout fl_progress;
	
	 String urlphp,po;
	TextView enotifier,nopo;
	EditText ewaybill;
	Button finish,submit,keluar,scan;
	gps_tracker gps;
	ListView mainListView;
	private ListPoOutstandingDBAdapter PoOutstandingDBAdapter;
	private ListFinishPoOutstandingDBAdapter finish_po_outstanding;
	private  ProgressDialog progressBar;
	private ArrayAdapter<String> listAdapter ; 
	public String locpk,username, mswb_pk, origin, destination, tppenerima;
	SessionManager session;
//	WaybillAdapter	db;
	ListPoOutstandingDBAdapter	db;
	ListFinishPoOutstandingDBAdapter	dbFinish;
	
	SharedPreferences myPrefs;
	SharedPreferences.Editor spEditor;
    SharedPreferences sp;

	private File actualImage;
	private File compressedImage;
	static Bitmap compressedImageBitmap;
	static Bitmap Photo;
	byte[] imageInByte;
	static int heightPict,widthPict,qualityPict;
	static int check_size;

	public String height_data="240";
	public String width_data="240";
	public String quality_data="80";
	static int height,width,quality;

	private static final float maxHeight = 720.0f;
	private static final float maxWidth = 1280.0f;

	private static final int REQUEST_WRITE_STORAGE = 112;

	//AWS S3
//	String STR_AWS_Key;
//	String STR_AWS_Secret;
	private AmazonS3Client s3Client;
	private BasicAWSCredentials credentials;
	AmazonS3Client s3;
	TransferUtility transferUtility;
	TransferObserver observer;
	String STR_AWS_Key_Code, STR_AWS_Secret_Code, STR_AWS_Path_Folder;

	public String STR_URLScanSignature, Str_LinkScanSignature;
	String STR_penerima, STR_tipe_penerima;

	private NotificationManagerCompat notificationManager;

	//qr code scanner object
	private IntentIntegrator qrScan;

	Uri photoUri = null;
	File photoFile = null;

    private static final int PERMISSION_REQUEST_CODE = 200;
	
	 public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.multi_scan_po_outstanding);
	       
	       Koneksi Str_lo_Koneksi = new Koneksi();
	       Str_LinkScanPOOutstanding = Str_lo_Koneksi.ConnPOOutstandingScan();
	       Str_LinkClosePOOutstanding = Str_lo_Koneksi.ConnPOOutstandingClose();
		 Str_LinkScanSignature = Str_lo_Koneksi.ConnScanSignature();

		 fl_progress = (FrameLayout) findViewById(R.id.fl_progress);
	       
	       myPrefs = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE); 
	       spEditor = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE).edit();
	       sp = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE);
	       spEditor = myPrefs.edit();

		 notificationManager = NotificationManagerCompat.from(this);
	       
	        session = new SessionManager(getApplicationContext());
	        HashMap<String, String> user = session.getUserDetails();
	 	        username = user.get(SessionManager.KEY_USER);
	 	        urlphp = user.get(SessionManager.KEY_URL);
	 	        locpk = user.get(SessionManager.KEY_LOCPK);

		 session = new SessionManager(getApplicationContext());
		 HashMap<String, String> awss3 = session.getAwsDetails();
		 STR_AWS_Key_Code = awss3.get(SessionManager.KEY_AWS_CODE);
		 STR_AWS_Secret_Code = awss3.get(SessionManager.KEY_AWS_SECRET_CODE);
		 STR_AWS_Path_Folder = awss3.get(SessionManager.KEY_AWS_PATH_FOLDER);
		 Log.d("Debug", "AWS S3"+" || Key Code " + STR_AWS_Key_Code+" || Secret Code " + STR_AWS_Secret_Code+" || Path Folder " + STR_AWS_Path_Folder);

	 	       PoOutstandingDBAdapter = new ListPoOutstandingDBAdapter(this);
	 	      PoOutstandingDBAdapter.open();
	 	        
//	 	       Log.e("Debug", "Halaman Multi Scan PO Outstanding " +"ISI >>> " + "URL = " + urlphp);
//		 	      if (urlphp.equals("http://43.252.144.14:81/android/")) {
//		 		    	Log.i("Debug", "Halaman Multi Scan PO Outstanding " +"Replace URL Lokal Server");
//		 		    	STR_URL_API = urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/compact_mobile");
//		 			}
//		 	     else if (urlphp.equals("http://api-mobile.atex.co.id/android/")) {
//		 	    	Log.i("Debug", "Halaman Multi Scan PO Outstanding " +"Replace URL Online Server");
//		 	    	STR_URL_API = urlphp.replace(urlphp, "http://api-mobile.atex.co.id/compact_mobile");
//		 	    }
//		 	    else {
//			    	Toast.makeText(getApplicationContext(), "Server Tidak Tersedia",0).show();
//			    }
//		        
//		        STR_URL_API = urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/compact_mobile");
//				Log.i("Debug", "Halaman Multi Scan PO Outstanding " +"Test String Replace URL " + STR_URL_API);
				
//				STR_url_po_outstanding = STR_URL_API + "/" +"scan_pup_all";
	 	     STR_url_po_outstanding = urlphp + Str_LinkScanPOOutstanding;
				Log.d("Debug","Halaman Multi Scan PO Outstanding " +"Test URL " + STR_url_po_outstanding);
				
//				STR_url_po_outstanding_finishing = STR_URL_API + "/" +"closing_po";
				STR_url_po_outstanding_finishing = urlphp + Str_LinkClosePOOutstanding;
				Log.d("Debug","URL PO Outstanding Finishing" +"Test URL " + STR_url_po_outstanding_finishing);
				
				Str_sp_url_scan_PO = (myPrefs.getString("sp_po_outstanding", ""));
				Str_sp_url_scan_finish_PO = (myPrefs.getString("sp_finish_po_outstanding", ""));
				Log.d("Debug", "URL Shared Preferences" 
				+ " || URL Scan PO = " + Str_sp_url_scan_PO
				+ " || URL Finish PO = " + Str_sp_url_scan_finish_PO);
	 	        
	 	       IV_foto = (ImageView) findViewById(R.id.image);
	 			BTN_foto = (Button) findViewById(R.id.btnFoto);
	 			
	 			BTN_foto.setOnClickListener(new View.OnClickListener() {
	 				public void onClick(View v) {
//	 					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
//	 	                startActivityForResult(cameraIntent, CAMERA_REQUEST);

						if (checkPermissions()) {
							Log.d("Debug", "if checkPermissions" + " || startApplication");
							boolean success = true;
							if (success) {
								Log.d("Debug", "if-if success to create directory");
								ActivityCompat.requestPermissions(multiScanPoOutstanding.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA}, REQUEST_WRITE_STORAGE);
								String intStorageDirectory = getFilesDir().toString();
								Log.d("Debug", "Dir Storage " + intStorageDirectory);
								File mediaStorageDirFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
								File mediaStorageDir = new File(mediaStorageDirFolder + File.separator + "Compact" + File.separator);
								Log.d("Debug", "Media Storage " + mediaStorageDir);

								if (!mediaStorageDir.exists()) {
									if (!mediaStorageDir.mkdirs()) {
										Log.d("Debug", "if-if-if-if failed to create directory");
									} else {
										Log.d("Debug", "if-if-if-else success to create directory");
									}
								}
							} else {
								Log.d("Debug", "if-if-else failed to create directory");
							}
						}else {
							Log.d("Debug", "else checkPermissions" + " || setPermissions");
							setPermissions();
							boolean success=true;
							if(success){
								Log.d("Debug", "else-if success to create directory");
								ActivityCompat.requestPermissions(multiScanPoOutstanding.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA},REQUEST_WRITE_STORAGE);
								String intStorageDirectory = getFilesDir().toString();
								Log.d("Debug", "Dir Storage "+intStorageDirectory);
								File mediaStorageDirFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
								File mediaStorageDir = new File(mediaStorageDirFolder+ File.separator + "Compact" + File.separator);
								Log.d("Debug", "Media Storage "+mediaStorageDir);

								if (!mediaStorageDir.exists()) {
									if (!mediaStorageDir.mkdirs()) {
										Log.d("Debug", "else-if-if-if failed to create directory");
									} else {
										Log.d("Debug", "else-if-if-else success to create directory");
									}
								}
							}else{
								Log.d("Debug", "else-if-else failed to create directory");
							}
						}

						if (ET_waybill.getText().toString().isEmpty()) {
							Toast.makeText(multiScanPoOutstanding.this, "Input/Scan Waybill terlebih dahulu", Toast.LENGTH_LONG).show();
							return;
						}

						Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

						if (cameraIntent.resolveActivity(getPackageManager()) == null) {
							Toast.makeText(multiScanPoOutstanding.this, "Camera not found", Toast.LENGTH_LONG).show();
							return;
						}

						try {
							photoFile = createImageFile();
						} catch (IOException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "Create file failed", Toast.LENGTH_LONG).show();
						}

						if (photoFile != null) {
							photoUri = FileProvider.getUriForFile(multiScanPoOutstanding.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
						}

						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
						startActivityForResult(cameraIntent, CAMERA_REQUEST);
	 				}
	 			});

//	       ewaybill=(EditText)findViewById(R.id.waybill);
		 ET_waybill = (EditText) findViewById(compact.mobile.R.id.waybill);
	       enotifier=(TextView)findViewById(R.id.notifier);
	       mainListView = (ListView) findViewById( R.id.mainListView );   
	       scan=(Button)findViewById(R.id.btnscan);
//	       HandleClick hc = new HandleClick();
//	       scan.setOnClickListener(hc);
		 //intializing scan object
		 qrScan = new IntentIntegrator(this);
		 //attaching onclick listener
		 scan.setOnClickListener(this);
	       finish=(Button)findViewById(R.id.btnFinish);
	       submit=(Button)findViewById(R.id.btnSubmit);
	       keluar=(Button)findViewById(R.id.btnKeluar);

		 PB_PersentaseUpload = (ProgressBar) findViewById(R.id.progressBar1);
		 TV_PersentaseUpload = (TextView) findViewById(R.id.txt_progress);

	       Bundle extras = getIntent().getExtras();
	       po = extras.getString("po");
	        nopo = (TextView)findViewById(R.id.po);
//	        nopo.setText("Nomor Pickup Order : "+po);
	        nopo.setText("No.Assigment : "+po);
	        
	        STR_no_awb = extras.getString("awb");
	        STR_no_assigment = extras.getString("asigment");
	        Log.d("Debug", "Intent From List PO Outstanding >>>" 
	        +" No.AWB = "+ STR_no_awb
	        +" No.Assigment = "+ STR_no_assigment);
	        
	        Time now = new Time(Time.getCurrentTimezone());
			 now.setToNow();
			 String sTgl =  Integer.toString(now.year) + "-" + ("00" + Integer.toString(now.month + 1)).substring(("00" + Integer.toString(now.month + 1)).length() - 2) + "-" + ("00" + Integer.toString(now.monthDay)).substring(("00" + Integer.toString(now.monthDay)).length() - 2) + " " + 
						("00" + Integer.toString(now.hour)).substring(("00" + Integer.toString(now.hour)).length() - 2)  + ":" + ("00" + Integer.toString(now.minute)).substring(("00" + Integer.toString(now.minute)).length() - 2) + ":" + ("00" + Integer.toString(now.second)).substring(("00" + Integer.toString(now.second)).length() - 2);
			 
			 STR_date_time = sTgl;
			 Log.d("Debug", "Cek Tanggal " + STR_date_time);

		 STR_Date = ("00" + Integer.toString(now.monthDay)).substring(("00" + Integer.toString(now.monthDay)).length() - 2) +("00" + Integer.toString(now.month + 1)).substring(("00" + Integer.toString(now.month + 1)).length() - 2) +Integer.toString(now.year);
		 STR_Time = ("00" + Integer.toString(now.hour)).substring(("00" + Integer.toString(now.hour)).length() - 2)  + ("00" + Integer.toString(now.minute)).substring(("00" + Integer.toString(now.minute)).length() - 2) + ("00" + Integer.toString(now.second)).substring(("00" + Integer.toString(now.second)).length() - 2);
		 STR_DateTime = STR_Date+"-"+STR_Time;
		 Log.d("Debug", "Cek " + "|| Date = "+STR_Date+ "|| Time = "+STR_Time);
		 Log.d("Debug", "Cek Date Time " + STR_DateTime);
	        
	       ArrayList<String> wbList = new ArrayList<String>(); 
	       
	       listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow,wbList); 
	       
//	       ewaybill.setOnKeyListener(new OnKeyListener() {
//	    	    public boolean onKey(View v, int keyCode, KeyEvent event) {
//	    	        // If the event is a key-down event on the "enter" button
//	    	        //if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
//	    	         //   (keyCode == KeyEvent.KEYCODE_ENTER)) {
//	    	    	if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
//	    	    			   keyCode == EditorInfo.IME_ACTION_DONE ||
//	    	    			   event.getAction() == KeyEvent.ACTION_UP &&
//	    	    			   event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//	    	          // Perform action on key press
//	    	    		if (ewaybill.getText().toString().trim().length() != 0) {
//	    	        	add_list();
//	    	    		}
//	    	        	
//	    	          return true;
//	    	        }
//	    	        return false;
//	    	    }
//	    	});
	       
//	       add.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//				if (ewaybill.getText().toString().trim().length() == 0) {
//	       			//Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
//	       			ewaybill.requestFocus();
//	       		} else {
//	       			
//	       			String[] sWB;
//	       			String sTemp1, sTemp2;
//	       			if (ewaybill.getText().toString().trim().indexOf("-") > 0)
//	       			{
//	       				sWB = ewaybill.getText().toString().trim().split("-");
//	       				sTemp1 = (sWB[0]+"0000").substring(0,4);
//	       				sTemp2 = "000000000000"+sWB[1]; 
//	       				sTemp2 = sTemp2.substring(sTemp2.length() - 9);
//	       				ewaybill.setText(sTemp1 + sTemp2);
//	       				Log.d("add waybill","waybill : "+ewaybill.getText().toString().trim());
//	       			}
//	       			
//	       			add_list();
//	       			
//	       		}
//				}
//			});
	       
	       
	       submit.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					progressBar = new ProgressDialog(multiScanPoOutstanding.this);
	    		    progressBar.setCancelable(false);
	    		    progressBar.setMessage("Loading...");
	    		    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);;
	    		    progressBar.show();
	    		    
	    		    ET_waybill = (EditText) findViewById(R.id.waybill);
	    		    STR_waybill = ET_waybill.getText().toString();
	    		    Log.d("Debug","Cek Isi >>> "+ " Waybill = "+STR_waybill);
//						if (mainListView.getCount() == 0) {
//			       			Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
//			       		}
	    		    if (ET_waybill.getText().toString().trim().length() == 0) {
		       			Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
		       		}
//						else if(tfoto.equals("")){
//		        			Toast.makeText(v.getContext(), "Upload Foto ADS", Toast.LENGTH_SHORT).show();
//		        		}
					else if(photoFile == null || !photoFile.exists() || photoFile.length() == 0){
						Toast.makeText(v.getContext(), "Foto PoOutstanding tidak boleh kosong", Toast.LENGTH_SHORT).show();
					}
			       		else {
//			       			update_data(v);
//			       			savetoDB();
			       			update_data_po();
//			       			try {
//////			       				PoOutstanding listpo_outstanding = new PoOutstanding();
////			       				PoOutstanding po_outstanding = new PoOutstanding();
////			       				db = new ListPoOutstandingDBAdapter(multiScanPoOutstanding.this);
////			       				for(int t = 0; t < 5; t++){
////			       					po_outstanding.setWaybill(STR_waybill);
////			       					po_outstanding.setLocpk(locpk);
////			       					po_outstanding.setUsername(username);
////			       					po_outstanding.setPo(po);
////			       					po_outstanding.setImage(STR_foto);
//////			       					db.open();
//////				       				db.createContact(po_outstanding);
//////				       				db.close();
////			       					ListPoOutstandingDBAdapter.createContact(po_outstanding);
////					 	       }
//								cekPostPoOutstanding(STR_waybill, username, locpk, po, STR_foto);
//								Log.d("Debug","Trace String");
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//								displayExceptionMessage(e.getMessage());
//							}
			       		}
					progressBar.dismiss();
				}
			});
	       
	       finish.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					progressBar = new ProgressDialog(multiScanPoOutstanding.this);
	    		    progressBar.setCancelable(false);
	    		    progressBar.setMessage("Loading...");
	    		    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);;
	    		    progressBar.show();
	    		    
	    		    String Str_po = po;
					String Str_assigment = STR_no_assigment;
					Log.e("Debug", "Isi Tombol Finish >>>"
					+" No.PO = "+Str_po
					+" Mo.Assigment = "+Str_assigment);
					
					if (Str_po.toString().trim().length() == 0) {
		       			Toast.makeText(v.getContext(), "Periksa Kembali Data Terpilih.", Toast.LENGTH_SHORT).show();
		       		}
					else {
						update_data_finish_po();
//						try {
//							cekPostFinishPO(Str_po, Str_assigment);
//							Log.d("Debug","Trace String");
//						}
//						catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//							displayExceptionMessage(e.getMessage());
//						}
					}
					progressBar.dismiss();
				}
	       });
	       
	             
	       keluar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					 finish();
					 deleteCompactDir(true);
					// Intent a = new Intent (pup.this,main_menu.class);
					// startActivity(a);
				}
			});
	       
	 }
	 
	 protected void displayExceptionMessage(String message) {
			// TODO Auto-generated method stub

		}
	 
//	 public void add_list(){
//		 
//		 int x = listAdapter.getPosition(ewaybill.getText().toString().trim()) ;
//			
//			Log.d("add waybill","cek posisi " + ewaybill.getText().toString().trim() + " => " + x);
//			
//			if (x < 0 )
//			{	
//			listAdapter.add(ewaybill.getText().toString().trim());
//			Log.d("add waybill", "=> " + ewaybill.getText().toString().trim() );
//			mainListView.setAdapter( listAdapter );
//			
//			int i,a;
//			String STR_waybill = "";
//
//			i = this.mainListView.getCount();
//			for (a = 0; a<i; a++)
//			{
//				if(a<i-1){
//					STR_waybill += "'"+this.mainListView.getAdapter().getItem(a)+ "',"; }
//				else{
//					STR_waybill += "'"+this.mainListView.getAdapter().getItem(a)+ "'";
//				}
//			}
//
//			Log.d("add list waybill", STR_waybill);
//			Toast.makeText(getApplicationContext(), "No." + STR_waybill +" Sudah Di Tambahkan",0).show();
////			STR_ewaybill = ewaybill.getText().toString().trim();
//			STR_ewaybill = STR_waybill;
//			Log.d("Debug", "String Trim AWB => " + STR_ewaybill);
//			
//			}
//			
//			//ewaybill.setFocusableInTouchMode(true);
//			ewaybill.requestFocus();
//			ewaybill.setText("");
//		 
//	 }

    private void compressAndLoadPhoto() {
        try {
            photoFile = new Compressor(this)
                    .setMaxWidth(1366)
                    .setMaxHeight(1366)
                    .setQuality(75)
                    .compressToFile(photoFile);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to compress image", Toast.LENGTH_LONG).show();
            return;
        }

        Glide.with(this)
                .load(photoFile)
                .into(IV_foto);

		fl_progress.setVisibility(View.GONE);
    }
	 
	 public void update_data_po() {
			Log.d("Debug", "Update Data PO");
			
			gps = new gps_tracker(multiScanPoOutstanding.this);
			
			String Str_check_connection_po = null;
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
			if (networkinfo != null && networkinfo.isConnected()) {
				// aksi ketika ada koneksi internet
				Str_check_connection_po = "Yes Signal";
				Log.d("Debug", "Koneksi Internet Tersedia");
			} else {
				// aksi ketika tidak ada koneksi internet
				Str_check_connection_po = "No Signal";
				Log.d("Debug", "Koneksi Internet Tidak Tersedia");
			}
			
			Log.d("Debug", "Check Connection " + Str_check_connection_po);
			
			ArrayList<NameValuePair> masukparam = new ArrayList<NameValuePair>();
			String Str_waybill = STR_waybill;
			String Str_locpk = locpk;
			String Str_username = username;
			String Str_po = po;
			String Str_foto = STR_replace;
			String Str_assigment = STR_no_assigment;
			String Str_lat_long = Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude());
			Log.d("Debug", "Cek Data" + " Waybill = " + Str_waybill + " Locpk = "
					+ Str_locpk + " LatLong = " + Str_lat_long + " Username = "
					+ Str_username + " PO = " + Str_po + " Assigment = "
					+ Str_assigment + " Foto = " + Str_foto);
			
			String response = null;

			ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
			masukparam1.add(new BasicNameValuePair("waybill", Str_waybill));
			masukparam1.add(new BasicNameValuePair("locpk", locpk));
//			masukparam1.add(new BasicNameValuePair("lat_long", Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude())));
			masukparam1.add(new BasicNameValuePair("lat_long", Str_lat_long));
			masukparam1.add(new BasicNameValuePair("username", Str_username));
			masukparam1.add(new BasicNameValuePair("po", Str_po));
			masukparam1.add(new BasicNameValuePair("assigment", Str_assigment));
			masukparam1.add(new BasicNameValuePair("foto", STR_replace));
			masukparam1.add(new BasicNameValuePair("kota", Str_locpk));
			masukparam1.add(new BasicNameValuePair("waktu", "sTgl"));

			String response1;

			try {
				Log.d("Debug", "Update Data PO "+"Lewat Try");
//				cekPostPoOutstanding(STR_waybill, username, locpk, po, STR_foto);
//				cekPostPoOutstanding(STR_waybill, locpk, username, Str_lat_long, Str_po, STR_foto, Str_locpk, Str_assigment, STR_date_time, Str_check_connection_po);
				new android.os.Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						new cekPostPoOutstandingAsync().execute();
					}
				},500);
				new android.os.Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						new cekPostSignatureAsync().execute();
					}
				},500);
				UploadImageToAWS();
				Log.d("Debug","Trace String");
				savetoLocalDB(STR_waybill);
				reset();
			} catch (Exception e) {
				Log.d("Debug", "Update Data PO "+"Catch");
				Log.d("Gagal", e.toString());
				// TODO Auto-generated catch block
				e.printStackTrace();
				displayExceptionMessage(e.getMessage());
				savetoLocalDB(STR_waybill);
				reset();
			}
		}
	 
	 private void savetoLocalDB(String STR_waybill) {
		 Log.d("Debug","SaveLocalDB");
		 C_PoOutstanding po_outstanding = new C_PoOutstanding();
		 ET_waybill = (EditText) findViewById(R.id.waybill);
		 STR_waybill = ET_waybill.getText().toString();
		 
		 String Str_waybill = STR_waybill;
		 String Str_locpk = locpk;
		 String Str_username = username;
		 String Str_po = po;
		 String Str_foto = STR_replace;
		 String Str_assigment = STR_no_assigment;
		 Log.d("Debug","Isi SaveDB >>>" 
				 + " Waybill= "+Str_waybill
				 + " Lockpk= "+Str_locpk
				 + " Username= "+Str_username
				 + " PO= "+Str_po
				 + " Foto= "+Str_foto
				 + " No.Assigment= "+Str_assigment);
		 
//		 String Lat = "0";
//		 String Lang = "0";
//		 STR_lat_long = Lat + "," + Lang;
//		 Log.d("Debug", "Latlong " + STR_lat_long);
		 
//		 Time now = new Time(Time.getCurrentTimezone());
//		 now.setToNow();
//		 String sTgl =  Integer.toString(now.year) + "-" + ("00" + Integer.toString(now.month + 1)).substring(("00" + Integer.toString(now.month + 1)).length() - 2) + "-" + ("00" + Integer.toString(now.monthDay)).substring(("00" + Integer.toString(now.monthDay)).length() - 2) + " " + 
//					("00" + Integer.toString(now.hour)).substring(("00" + Integer.toString(now.hour)).length() - 2)  + ":" + ("00" + Integer.toString(now.minute)).substring(("00" + Integer.toString(now.minute)).length() - 2) + ":" + ("00" + Integer.toString(now.second)).substring(("00" + Integer.toString(now.second)).length() - 2);
//		 
//		 STR_date_time = sTgl;
//		 Log.d("Debug", "Cek Tanggal " + STR_date_time);
		 
		 STR_lat_long = Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude());
		 Log.d("Debug", "Latlong " + STR_lat_long);
		 
		 po_outstanding.setWaybill(Str_waybill);
		 po_outstanding.setLocpk(Str_locpk);
		 po_outstanding.setLat_Long(STR_lat_long);
//		 po_outstanding.setLat_Long(Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude()));
		 po_outstanding.setUsername(Str_username);
		 po_outstanding.setPo(Str_po);
		 po_outstanding.setAssigment(Str_assigment);
		 po_outstanding.setImage(Str_foto);
		 po_outstanding.setKota(Str_locpk);
		 po_outstanding.setWaktu(STR_date_time);
		 Log.d("Debug","add PO -> local" );
		 db = new ListPoOutstandingDBAdapter(multiScanPoOutstanding.this);
		 db.open();
		 db.createContact(po_outstanding);
		 db.close();
		 Log.d("PUP","PO add to local database ..");
		 listAdapter.clear();
		 mainListView.setAdapter(listAdapter);
		 
		}
	 
	 public void cekPostPoOutstanding(String STR_waybill, String locpk, String username
			 , String Str_lat_long, String Str_po, String STR_foto, String Str_locpk, String Str_assigment, String STR_date_time, String Str_check_connection_po) throws JSONException {
		 
		 Log.d("Debug", "Lewat Post PO Outstanding");	
		 	String Str_2nd_check_connection_po = Str_check_connection_po;
			Log.d("Debug", "2nd Response Check Connection  = " + Str_2nd_check_connection_po);
			if (Str_2nd_check_connection_po.equals("No Signal")) {
				Toast.makeText(getApplicationContext(), "Lost Signal...!! Data Disimpan Sementara Di Lokal Database", Toast.LENGTH_SHORT).show();
				show_dialog4();
			} else {
		 
			HttpURLConnection connection;
			OutputStreamWriter request = null;
			URL url = null;
			String URI = null;
			String response = null;
			String parameters = "waybill=" + STR_waybill
					+ "&locpk=" + locpk
					+ "&username=" + username
					+ "&lat_long=" + Str_lat_long
					+ "&po=" + Str_po
					+ "&image=" + STR_replace
					+ "&kota=" + Str_locpk
					+ "&asigment=" + Str_assigment
					+ "&waktu=" + STR_date_time;
			Log.d("Debug", "Parameters " + parameters);
			try {
				/* url = new URL("http://192.168.6.12/mobile_agen/booking"); */
				url = new URL(STR_url_po_outstanding);
//				url = new URL(Str_sp_url_scan_PO);
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
				
				TV_response_code = (TextView)findViewById(R.id.txt_Response_Code);
				TV_response_code.setText(RespnseCode);
				STR_response_code = TV_response_code.getText().toString();
				
//				if (TV_response_code.equals("0")) {
				if (RespnseCode.toString().trim().length() == 0) {
//					Toast.makeText(getApplicationContext(), "Mohon Periksa Kembali",0).show();
					Toast.makeText(getApplicationContext(), RespnseMessage + " Memproses Data, Mohon Di Coba Kembali.", Toast.LENGTH_SHORT).show();
				} else {
//					Toast.makeText(getApplicationContext(), "AWB Berhasil Di Proses",0).show();
					Toast.makeText(getApplicationContext(), RespnseMessage + " Memproses Data.", Toast.LENGTH_SHORT).show();
//					Intent a = new Intent(multiScanPoOutstanding.this, ListPoOutstanding.class);
//					startActivity(a);
				}
				
			} catch (IOException e) {
				// Error
				Log.d("Debug", "Trace = ERROR ");
			}
			}
		}

	public class cekPostPoOutstandingAsync extends AsyncTask<String, String, String>
	{
		ProgressDialog pDialog;
		String Str_status;
		String Str_NoAssigment = STR_no_assigment;
		String Str_username = username;
		String Str_locpk = locpk;
		String Str_lat_long = Double.toString(gps.getLatitude()) + ", "
				+ Double.toString(gps.getLongitude());
		String STR_lat_long = Str_lat_long;
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String STR_date_time = dateformat.format(date);
		String Str_waktu = STR_date_time;
		String STR_kota = locpk;
		String Str_po = po;

		protected void onPreExecute(){
			super.onPreExecute();

			pDialog = new ProgressDialog(multiScanPoOutstanding.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			HttpURLConnection connection;
			OutputStreamWriter request = null;
			URL url = null;
			String URI = null;
			String response = null;
			String parameters = "waybill=" + STR_waybill
					+ "&locpk=" + locpk
					+ "&username=" + username
					+ "&lat_long=" + STR_lat_long
					+ "&po=" + Str_po
					+ "&image=" + STR_replace
					+ "&kota=" + STR_kota
					+ "&asigment=" + Str_NoAssigment
					+ "&waktu=" + Str_waktu;
			Log.d("Debug", "Parameters " + parameters);

			try
			{
				String urlphp;
				urlphp = "";
				Log.d("debug", "Host Server -> " + STR_url_po_outstanding);
				url = new URL(STR_url_po_outstanding);
				Log.d("Debug","Test URL Login " + url);

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

				JSONObject json = new JSONObject(sb.toString());
				Log.d("Debug", "Trace Customer = " + json);
				RespnseCode = json.getString("response_code");
				RespnseMessage = json.getString("response_message");
				Log.d("Debug", "Response Code = " + RespnseCode);
				Log.d("Debug", "Response Message  = " + RespnseMessage);

//				TV_response_code = (TextView)findViewById(R.id.txt_Response_Code);
//				TV_response_code.setText(RespnseCode);
//				STR_response_code = TV_response_code.getText().toString();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						TV_response_code = findViewById(compact.mobile.R.id.txt_Response_Code);
						TV_response_code.setText(RespnseCode);
						STR_response_code = TV_response_code.getText().toString();
					}
				});
			}
			catch(IOException e)
			{
				Log.d("Debug","Trace = ERROR ");
//				Log.d("Debug","response_code  " + STR_ResponseCodeLogin);

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
			pDialog.dismiss();
			Log.d("Debug", "1.Test Sampai Sini ");
			if (RespnseCode.toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Memproses Data, Mohon Di Coba Kembali.", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), RespnseMessage + " Memproses Data.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	 
	 public void update_data_finish_po() {
			Log.d("Debug", "Update Data Finish PO");

			gps = new gps_tracker(multiScanPoOutstanding.this);
			
//			String Str_check_connection_po_finish = null;
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
			if (networkinfo != null && networkinfo.isConnected()) {
				// aksi ketika ada koneksi internet
//				Str_check_connection_po_finish = "Yes Signal";
				STR_CheckConnectionPOFinish = "Yes Signal";
				Log.d("Debug", "Koneksi Internet Tersedia");
			} else {
				// aksi ketika tidak ada koneksi internet
//				Str_check_connection_po_finish = "No Signal";
				STR_CheckConnectionPOFinish = "No Signal";
				Log.d("Debug", "Koneksi Internet Tidak Tersedia");
			}
			
//			Log.d("Debug", "Check Connection " + Str_check_connection_po_finish);
		 Log.d("Debug", "Check Connection " + STR_CheckConnectionPOFinish);

			ArrayList<NameValuePair> masukparam = new ArrayList<NameValuePair>();
			String Str_po = po;
			String Str_assigment = STR_no_assigment;
			Log.d("Debug", "Cek Update Data PO Finish" 
					+ " PO = " + Str_po
					+ " Assigment = " + Str_assigment);
			
			String response = null;
			
			ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
			masukparam1.add(new BasicNameValuePair("po", Str_po));
			masukparam1.add(new BasicNameValuePair("assigment", Str_assigment));
			
			String response1;
			
			try {
				Log.d("Debug", "Update Data Finish PO "+"Lewat Try");
//				cekPostFinishPO(Str_po, STR_no_assigment, Str_check_connection_po_finish);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new cekPostFinishPO().execute();
                    }
                },500);
				Log.d("Debug","Trace String");
				savetoLocalDBFinish(STR_no_assigment);
				reset();
			} catch (Exception e) {
				Log.d("Debug", "Update Data Finish PO "+"Catch");
				Log.d("Gagal", e.toString());
				// TODO Auto-generated catch block
				e.printStackTrace();
				displayExceptionMessage(e.getMessage());
				savetoLocalDBFinish(STR_no_assigment);
				reset();
			}
		}
	 
//	 public void cekPostFinishPO(String Str_po, String STR_no_assigment, String Str_check_connection_po_finish)throws JSONException {
//
//		 Log.d("Debug", "Lewat Post PO Outstanding Finish");
//		 	String Str_2nd_check_connection_po_finish = Str_check_connection_po_finish;
//			Log.d("Debug", "2nd Response Check Connection  = " + Str_2nd_check_connection_po_finish);
//			if (Str_2nd_check_connection_po_finish.equals("No Signal")) {
//				Toast.makeText(getApplicationContext(), "Lost Signal...!! Data Disimpan Sementara Di Lokal Database", Toast.LENGTH_SHORT).show();
//				show_dialog5();
//			} else {
//
//			HttpURLConnection connection;
//			OutputStreamWriter request = null;
//			URL url = null;
//			String URI = null;
//			String response = null;
//			String parameters = "po=" + Str_po + "&asigment=" + STR_no_assigment;
//			Log.d("Debug", "Parameters " + parameters);
//			try {
//				url = new URL(STR_url_po_outstanding_finishing);
////				url = new URL(Str_sp_url_scan_finish_PO);
//				Log.d("Debug", "Test URL " + url);
//
//				connection = (HttpURLConnection) url.openConnection();
//				connection.setDoOutput(true);
//				connection.setRequestProperty("Content-Type",
//						"application/x-www-form-urlencoded");
//				connection.setRequestMethod("POST");
//
//				request = new OutputStreamWriter(connection.getOutputStream());
//				// Log.d("Debug","request = " + request);
//				request.write(parameters);
//				request.flush();
//				request.close();
//				String line = "";
//
//				// Get data from server
//				InputStreamReader isr = new InputStreamReader(
//						connection.getInputStream());
//				BufferedReader reader = new BufferedReader(isr);
//				StringBuilder sb = new StringBuilder();
//
//				while ((line = reader.readLine()) != null) {
//					sb.append(line + "\n");
//					sb.append("respone_code");
//					sb.append("respone_message");
//					Log.d("Debug", "TraceLine = " + line);
//				}
//
//				JSONObject json = new JSONObject(sb.toString());
//				Log.d("Debug", "Trace Customer = " + json);
//				RespnseCode = json.getString("response_code");
//				RespnseMessage = json.getString("response_message");
//				Log.d("Debug", "Response Code = " + RespnseCode);
//				Log.d("Debug", "Response Message  = " + RespnseMessage);
//
//				TV_response_code = (TextView) findViewById(R.id.txt_Response_Code);
//				TV_response_code.setText(RespnseCode);
//				STR_response_code = TV_response_code.getText().toString();
//
//				String Str_status = STR_response_code;
//				Log.d("Debug", "String Response Code = " + Str_status);
//
////				if (TV_response_code.equals("0")) {
////					Toast.makeText(getApplicationContext(),"Mohon Periksa Kembali", 0).show();
////				}
////				else if (TV_response_code.equals("2")) {
////					Toast.makeText(getApplicationContext(),"PO Berhasil Di Proses Sebagian", 0).show();
////					keluar.setOnClickListener(new View.OnClickListener() {
////						public void onClick(View v) {
////							 finish();
////							// Intent a = new Intent (pup.this,main_menu.class);
////							// startActivity(a);
////						}
////					});
////				}
////				else {
////					Toast.makeText(getApplicationContext(),"PO Berhasil Di Proses", 0).show();
//////					Intent a = new Intent(multiScanPoOutstanding.this,ListPoOutstanding.class);
//////					startActivity(a);
////					keluar.setOnClickListener(new View.OnClickListener() {
////						public void onClick(View v) {
////							 finish();
////							// Intent a = new Intent (pup.this,main_menu.class);
////							// startActivity(a);
////						}
////					});
////				}
//
//				if (Str_status.equals("1")) {
////					Log.i("Debug", "Status Success");
//					Log.i("Debug", RespnseMessage);
//					show_dialog1();
//				} else if (Str_status.equals("2")) {
////					Log.i("Debug", "Status Success Sebagian");
//					Log.i("Debug", RespnseMessage);
//					show_dialog2();
//				} else {
////					Log.i("Debug", "Status Gagal");
//					Log.i("Debug", RespnseMessage);
//					show_dialog3();
//				}
//
//			} catch (IOException e) {
//				// Error
//				Log.d("Debug", "Trace = ERROR ");
//			}
//			}
//		}

    public class cekPostFinishPO extends AsyncTask<String, String, String>
    {
		ProgressDialog pDialog;
        protected void onPreExecute() {
            super.onPreExecute();

			pDialog = new ProgressDialog(multiScanPoOutstanding.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection;
            OutputStreamWriter request = null;
            URL url = null;
            String URI = null;
            String response = null;
			Log.d("Debug", "2nd Response Check Connection  = " + STR_CheckConnectionPOFinish);
			String parameters = "po=" + po + "&asigment=" + STR_no_assigment;
			Log.d("Debug", "Parameters " + parameters);
            try
            {
                Log.d("Debug", "URL = " + STR_url_po_outstanding_finishing);
                url = new URL(STR_url_po_outstanding_finishing);
                Log.d("Debug","Test URL Closed PO Outstanding " + url);

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

                JSONObject json = new JSONObject(sb.toString());
                Log.d("Debug", "Trace Json = " + json);
                RespnseCodeFinishPO = json.getString("response_code");
                RespnseMessageFinishPO = json.getString("response_message");
                Log.d("Debug", "Response Code = " + RespnseCodeFinishPO +" || "+"Response Message  = " + RespnseMessageFinishPO);
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
			pDialog.dismiss();

            TV_response_code = (TextView) findViewById(R.id.txt_Response_Code);
            TV_response_code.setText(RespnseCodeFinishPO);
            STR_StatusResponseCodePO = TV_response_code.getText().toString();
            Log.d("Debug", "String Response Code = " + STR_StatusResponseCodePO);

            if (STR_StatusResponseCodePO.equals("1")) {
                Log.i("Debug", RespnseMessageFinishPO);
                show_dialog1();
            } else if (STR_StatusResponseCodePO.equals("2")) {
                Log.i("Debug", RespnseMessageFinishPO);
                show_dialog2();
            } else {
                Log.i("Debug", RespnseMessageFinishPO);
                show_dialog3();
            }
        }
    }

	public class cekPostSignatureAsync extends AsyncTask<String, String, String>
	{
		ProgressDialog pDialog;
		String Str_status;
		String Str_assigment = STR_no_assigment;
		String STR_typescan = "PUP";
		String STR_filename = STR_FullFileNamePhoto;
		String STR_penerima = " ";
		String STR_tipe_penerima = " ";

		protected void onPreExecute(){
			super.onPreExecute();

			pDialog = new ProgressDialog(multiScanPoOutstanding.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			Log.d("Debug", "Post Scan PoOutstanding Signature");
		}

		@Override
		protected String doInBackground(String... params) {
			HttpURLConnection connection;
			OutputStreamWriter request = null;
			URL url = null;
			String URI = null;
			String response = null;
//			String fileName = photoFile.getName();
//			if(fileName.contains("-")){
//				fileName = fileName.substring(0, fileName.indexOf("-"));
//			}
			String fileName = "";
			if(photoFile != null){
				fileName = photoFile.getName();
				if(fileName.contains("-")){
					fileName = fileName.substring(0, fileName.indexOf("-"));
				}
			}
			String parameters = "waybill=" + STR_waybill
					+ "&username=" + username
					+ "&penerima=" + STR_penerima
					+ "&tiperem=" + STR_tipe_penerima
//					+ "&filename=" + fileName+".jpg"
					+ "&asigment=" + Str_assigment
					+ "&typescan=" + STR_typescan;

			if(photoFile != null){
				parameters += "&filename=" + fileName+".jpg";
			}
			Log.d("Debug", "Parameters " + parameters);

			try
			{
				STR_URLScanSignature = urlphp + Str_LinkScanSignature;
				Log.d("debug", "Host Server -> " + STR_URLScanSignature);
				url = new URL(STR_URLScanSignature);
				Log.d("Debug","URL Scan Signature " + url);

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

				JSONObject json = new JSONObject(sb.toString());
				Log.d("Debug", "Trace Json = " + json);
				RespnseCode = json.getString("response_code");
				RespnseMessage = json.getString("response_message");
				Log.d("Debug", "Response Code = " + RespnseCode +" || "+"Response Message  = " + RespnseMessage +" || "+"Response Debug  = " + RespnseDebug);
			}
			catch(IOException e)
			{
				Log.d("Debug","Trace = ERROR ");
//				Log.d("Debug","response_code  " + STR_ResponseCodeLogin);

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
			pDialog.dismiss();
			TV_response_code = (TextView)findViewById(R.id.txt_Response_Code);
			TV_response_code.setText(RespnseCode);
			STR_response_code = TV_response_code.getText().toString();

			Str_status = STR_response_code;
			Log.d("Debug", "String Response Code = " + Str_status);
			Log.d("Debug", "1.Test Sampai Sini ");
			if (Str_status.equals("1")) {
				Toast.makeText(getApplicationContext(), RespnseMessage, Toast.LENGTH_SHORT).show();
				Log.i("Debug", "Status Success");
//				show_dialog1();
			} else if (Str_status.equals("2")) {
				Toast.makeText(getApplicationContext(), RespnseMessage, Toast.LENGTH_SHORT).show();
				Log.i("Debug", "Status Success Sebagian");
//				show_dialog2();
			} else {
				Toast.makeText(getApplicationContext(), RespnseMessage, Toast.LENGTH_SHORT).show();
				Log.i("Debug", "Status Gagal");
//				show_dialog3();
			}
		}
	}
	 
	 private void savetoLocalDBFinish(String STR_no_assigment) {
		 Log.d("Debug","SaveLocalDBFinish PO");
		 C_Finish_PoOutstanding finish_po_outstanding = new C_Finish_PoOutstanding();
		 
		 String Str_po = po;
		 String Str_assigment = STR_no_assigment;
		 Log.d("Debug", "Isi SaveDB PO >>>" 
		    		+ " PO = " + Str_po 
		    		+ " Assigment = " + Str_assigment);
		 
		 finish_po_outstanding.setPo(po);
		 finish_po_outstanding.setAssigment(Str_assigment);
		 Log.d("Debug","add PO Finish -> local" );
		 dbFinish = new ListFinishPoOutstandingDBAdapter(multiScanPoOutstanding.this);
		 dbFinish.open();
		 dbFinish.createContact(finish_po_outstanding);
		 dbFinish.close();
		 Log.d("PUP","AWB add to local database ..");
		 listAdapter.clear();
		 mainListView.setAdapter(listAdapter);
	 }
	 
	 public void show_dialog1() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanPoOutstanding.this);
			builder.setTitle("No.PO Berhasil Di Finish");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }
	 
	 public void show_dialog2() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanPoOutstanding.this);
			builder.setTitle("No.PO Berhasil Di Finish Sebagian");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }
	 
	 public void show_dialog3() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanPoOutstanding.this);
			builder.setTitle("No.PO Gagal Di Finish, Mohon Ulangi Kembali");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }
	 
	 public void show_dialog4() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanPoOutstanding.this);
			builder.setTitle("No.PO Gagal Di Submit Ke Server, Data Di Simpan Sementara Di Lokal Database.");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }
	 
	 public void show_dialog5() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanPoOutstanding.this);
			builder.setTitle("No.PO Gagal Di Finish Ke Server, Data Di Simpan Sementara Di Lokal Database.");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }
	 
//	 @TargetApi(3)
//	public void update_data(View theButton) {
//		   int i,a;
//
//			String swaybill = "";
//
//			gps = new gps_tracker(multiScanPoOutstanding.this);
//			i = this.mainListView.getCount();
//			for (a = 0; a<i; a++)
//			{
//				if(a<i-1){
//					swaybill += "'"+this.mainListView.getAdapter().getItem(a)+ "',"; }
//				else{
//					swaybill += "'"+this.mainListView.getAdapter().getItem(a)+ "'";
//				}
//			}
//
//			Log.d("get waybill", swaybill);
//			
//				try {
//						    ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
////						    masukparam1.add(new BasicNameValuePair("po",po));
//						    masukparam1.add(new BasicNameValuePair("po",STR_no_assigment));
//			       			masukparam1.add(new BasicNameValuePair("waybill",swaybill));
//			       			masukparam1.add(new BasicNameValuePair("penerima", ""));
//			       			masukparam1.add(new BasicNameValuePair("telp", ""));
//			       			masukparam1.add(new BasicNameValuePair("kota", ""));
//			       			masukparam1.add(new BasicNameValuePair("tujuan", ""));
//			       			masukparam1.add(new BasicNameValuePair("mswb_pk", ""));
//			       			masukparam1.add(new BasicNameValuePair("locpk", locpk));
//			       			masukparam1.add(new BasicNameValuePair("username", username));
//			       			masukparam1.add(new BasicNameValuePair("tiperem", ""));
//			       			masukparam1.add(new BasicNameValuePair("tipe", "2"));
////			       			masukparam1.add(new BasicNameValuePair("tipe", "8"));
//			       			masukparam1.add(new BasicNameValuePair("keterangan", ""));
//			       			masukparam1.add(new BasicNameValuePair("lat_long",Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude())));
//			       			masukparam1.add(new BasicNameValuePair("waktu",""));
////			       			masukparam1.add(new BasicNameValuePair("foto",tfoto));
//			       			Log.d("waybill" ,swaybill );
//			       			String response1 = null;
//			       			try {
//			       				response1 = CustomHttpClient.executeHttpPost(urlphp +   "update2.php", masukparam1);
//			       				Log.d("respone" , response1 );
//			       				String res1 = response1.toString();
//			       				res1 = res1.trim();
//			       				res1 = res1.replaceAll("\\s+","");
//			       			
//			       				if (res1.equals("0")) {
//			       					//Toast.makeText(theButton.getContext(), "Scan PUP successed.", Toast.LENGTH_SHORT).show();
//			       					
//			       					Log.d("PUP","Terkirim ke server");
//			    	       			listAdapter.clear();
//			    	       			mainListView.setAdapter( listAdapter );
////			    	       			sukses("Scan PUP successed","Pickup Order "+po);
//			    	       			sukses("Scan PUP successed","Pickup Order "+STR_no_assigment);
//			    	       			
//			       				}
//			       				else {
//			       					Toast.makeText(theButton.getContext(), "Scan PUP failed !!.", Toast.LENGTH_SHORT).show();
//			       					Log.d("PUP","gagal simpan ke server");
////			      		 			savetolocal(swaybill);
//			      		 			Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();
//			       				}
//			       				
//			       			}
//			      		 	catch (Exception e) {
////			      		 			savetolocal(swaybill);
//			      		 			Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();
//			      		 		}
//				}
//			 	catch (Exception e) {
////				 		savetolocal(swaybill);
//				 		Toast.makeText(theButton.getContext(), "Fail connect to server, local database used", Toast.LENGTH_SHORT).show();
//
//				 		
//			 		}
//		   	
//		   }
	 
//	 @TargetApi(3)
//	    public void sukses(String msg,String po){
//	    	
//	    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//	    			multiScanPoOutstanding.this);
//	    	
//	    	alertDialog.setCancelable(false);
//	    	 
//	    	// Setting Dialog Title
//	    	alertDialog.setTitle(po);
//	    	 
//	    	// Setting Dialog Message
//	    	alertDialog.setMessage(msg);
//	    	 
//	    	// Setting Positive "Yes" Btn
//	    	alertDialog.setPositiveButton("OK",
//	    	        new DialogInterface.OnClickListener() {
//	    	            public void onClick(DialogInterface dialog, int which) {
//	    	            	finish();
//	    	            }
//	    	        });
//	    	
//	    	alertDialog.show();
//	    	
//	    }
	 
//	private void savetolocal(String sWB){
//		 
//		 Log.d("PUP","prepare for PUP -> local" );
//		Waybill waybill = new Waybill();
//		Time now = new Time(Time.getCurrentTimezone());
//		now.setToNow();
//		String sTgl =  Integer.toString(now.year) + "-" + ("00" + Integer.toString(now.month + 1)).substring(("00" + Integer.toString(now.month + 1)).length() - 2) + "-" + ("00" + Integer.toString(now.monthDay)).substring(("00" + Integer.toString(now.monthDay)).length() - 2) + " " + 
//				("00" + Integer.toString(now.hour)).substring(("00" + Integer.toString(now.hour)).length() - 2)  + ":" + ("00" + Integer.toString(now.minute)).substring(("00" + Integer.toString(now.minute)).length() - 2) + ":" + ("00" + Integer.toString(now.second)).substring(("00" + Integer.toString(now.second)).length() - 2);
//			waybill.setWaybill(sWB);
//			waybill.setPenerima("");
//			waybill.setKota("");
//			waybill.setTujuan("");
//			waybill.setMswb_pk("");
//			waybill.setLocpk(locpk);
//			waybill.setUser(username);
//			waybill.setTiperem("");
//			waybill.setTelp("");
//			waybill.setTipe("2");
////			waybill.setTipe("8");
//			waybill.setKeterangan("");
//			waybill.setLat_Long(Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude()));
//			waybill.setWaktu(sTgl);
//			waybill.setStatus("0");
////			waybill.setPo(po);
//			waybill.setPo(STR_no_assigment);
//			Log.d("PUP","add PUP -> local" );
//			db = new WaybillAdapter(multiScanPoOutstanding.this);
//			db.open();
//			db.createContact(waybill);
//			db.close();
//			Log.d("PUP","PUP add to local database ..");
//			listAdapter.clear();
// 			mainListView.setAdapter( listAdapter );
//	 }
	 
		private class HandleClick implements OnClickListener{
			public void onClick(View arg0){
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				switch (arg0.getId()){
					case R.id.btnscan:
						intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF");
						break;
				}
				startActivityForResult(intent, 0);
			}
		}
//	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		    if (requestCode == 0) {
//		      if (resultCode == RESULT_OK) {
//		        //tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
//		        ewaybill.setText(data.getStringExtra("SCAN_RESULT"));
//		      } else if (resultCode == RESULT_CANCELED) {
//
//		      }
//		    }
//		    else{
//	        	if (resultCode == RESULT_OK) {
//	        	Bitmap photo = (Bitmap) data.getExtras().get("data");
//	            IV_foto.setImageBitmap(photo);
//
//	            ByteArrayOutputStream bao = new ByteArrayOutputStream();
//
//	            photo.compress(Bitmap.CompressFormat.JPEG, 100, bao);
//	            byte [] byte_arr = bao.toByteArray();
////	            String image_encode = Base64.encodeBytes(byte_arr);
//					String image_encode = "data:image/jpg;base64,"+Base64.encodeBytes(byte_arr);
//					String base64Image = image_encode.split(",")[1];
//	            Integer pnj = image_encode.trim().length();
//		        Log.d("panjang foto", String.valueOf(pnj));
//					int width = photo.getWidth();
//					int height = photo.getHeight();
//					Log.d("Debug", "Scale Foto = " + String.valueOf(width)+ " px : " + String.valueOf(height) + " px");
//					Log.d("Debug", String.format("Size Foto: %s", getReadableFileSize(byte_arr.length)));
//	    		//set image hasil encode
//////		        STR_foto = image_encode;
////					STR_foto = base64Image;
////	            Log.d("Debug","Hasil Encode Photo = "+STR_foto);
////					STR_replace= STR_foto.replaceAll("[+]", ".");
////					Log.d("Debug","Replace Encode Photo = "+STR_replace);
//
//					Bitmap photo2 = (Bitmap) data.getExtras().get("data");
//					ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
//					photo2.compress(Bitmap.CompressFormat.JPEG, 100, bao2);
////                final Bitmap myImage2 = photo2;
//					byte [] byte_arr2 = bao2.toByteArray();
//					int width2 = photo2.getWidth();
//					int height2 = photo2.getHeight();
//					Log.d("Debug", "Ukuran From Camera Original = " + String.valueOf(width2)+ " px : " + String.valueOf(height2) + " px");
//					Log.d("Debug", String.format("Size Image From Camera Original: %s", getReadableFileSize(byte_arr2.length)));
//
//					final Bitmap photo3 = (Bitmap) data.getExtras().get("data");
//					ByteArrayOutputStream baoOriginal = new ByteArrayOutputStream();
//					photo3.compress(Bitmap.CompressFormat.JPEG, 100, baoOriginal);
//					byte [] byte_arrOriginal = baoOriginal.toByteArray();
//					int widthOriginal = photo3.getWidth();
//					int heightOriginal = photo3.getHeight();
//					Log.d("Debug", "Ukuran From Camera Original 2 = " + String.valueOf(widthOriginal)+ " px : " + String.valueOf(heightOriginal) + " px");
//					Log.d("Debug", String.format("Size Image From Camera Original 2 : %s", getReadableFileSize(byte_arrOriginal.length)));
//					Photo = photo3;
//
////					if(height_data.trim().length() <= 0 && width_data.trim().length() > 0){
////						if(quality_data.trim().length() <= 0){
////							Toast.makeText(getApplicationContext(), "Please insert quality", Toast.LENGTH_SHORT).show();
////						}else {
////							check_size = 1;
////							widthPict = Integer.parseInt(width_data);
////							qualityPict = Integer.parseInt(quality_data);
////							if(qualityPict > 0 && qualityPict <= 100){
////								ResizeImage();
////							}else{
////								Toast.makeText(getApplicationContext(), "Wrong quality input.try again !", Toast.LENGTH_SHORT).show();
////							}
////						}
////					}
////					else if(height_data.trim().length() > 0 && width_data.trim().length() <= 0){
////						Toast.makeText(getApplicationContext(), "Please insert width", Toast.LENGTH_SHORT).show();
////					}else{
////						if(quality_data.trim().length() <= 0){
////							Toast.makeText(getApplicationContext(), "Please insert quality", Toast.LENGTH_SHORT).show();
////						}else {
////							check_size = 0;
////							heightPict = Integer.parseInt(height_data);
////							widthPict = Integer.parseInt(width_data);
////							qualityPict = Integer.parseInt(quality_data);
////							if(qualityPict > 0 && qualityPict <= 100){
////								ResizeImage();
////							}else{
////								Toast.makeText(getApplicationContext(), "Wrong quality input.try again !", Toast.LENGTH_SHORT).show();
////							}
////						}
////
////					}
//
//					imageInByte = baoOriginal.toByteArray();
//					Log.d("Debug", String.format("Size Image : %s", getReadableFileSize(imageInByte.length)));
//					Log.d("Debug", String.format("Size Image 1 : %s", getReadableFileSize(image_encode.length())));
//					STR_foto = base64Image;
//					Log.d("Debug","Hasil Encode Photo If = "+STR_foto);
//					STR_replace= STR_foto.replaceAll("[+]", ".");
//					Log.d("Debug","Replace Encode Re-Image Photo = "+STR_replace);
//	        	}
//	        }
//		  }

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		// Check which request we're responding to
//		if (requestCode == 0) {
//			// Make sure the request was successful
//			if (resultCode == RESULT_OK) {
//				ET_waybill.setText(data.getStringExtra("SCAN_RESULT"));
//			} else if (resultCode == RESULT_CANCELED) {
//
//			}
//
//		}else{
//			if (resultCode == RESULT_OK) {
//				File out = new File(getFilesDir(), "newImage.jpg");
//				if (!out.exists()) {
//					Toast.makeText(getBaseContext(),
//							"Error while capturing image", Toast.LENGTH_LONG)
//							.show();
//					return;
//				}
//				Bitmap photo = BitmapFactory.decodeFile(out.getAbsolutePath());
//				IV_foto.setImageBitmap(photo);
//				ByteArrayOutputStream bao = new ByteArrayOutputStream();
//				photo.compress(Bitmap.CompressFormat.JPEG, 100, bao);
//				byte[] byte_arr = bao.toByteArray();
//				String image_encode = "data:image/png;base64," + Base64.encodeBytes(byte_arr);
//				String base64Image = image_encode.split(",")[1];
//				Integer pnj = image_encode.trim().length();
//				Log.d("panjang foto", String.valueOf(pnj));
//				int width = photo.getWidth();
//				int height = photo.getHeight();
//				Log.d("Debug", "Scale Foto = " + String.valueOf(width)+ " px : " + String.valueOf(height) + " px");
//				Log.d("Debug", String.format("Size image from camera: %s", getReadableFileSize(image_encode.length())));
//
////				STR_foto = base64Image;
////				STR_replace= STR_foto.replaceAll("[+]", ".");
////				Log.d("Debug", "Hasil Encode Photo = " + STR_foto);
//
////				String height_data="760";
////				String width_data="1080";
//				String height_data=String.valueOf(height);
//				String width_data=String.valueOf(width);
//				String quality_data="100";
//				Photo = photo;
//
//				if(height_data.trim().length() <= 0 && width_data.trim().length() > 0){
//					if(quality_data.trim().length() <= 0){
//						Toast.makeText(getApplicationContext(), "Please insert quality", Toast.LENGTH_SHORT).show();
//					}else {
//						check_size = 1;
//						widthPict = Integer.parseInt(width_data);
//						qualityPict = Integer.parseInt(quality_data);
//						if(qualityPict > 0 && qualityPict <= 100){
//							ResizeImage();
//						}else{
//							Toast.makeText(getApplicationContext(), "Wrong quality input.try again !", Toast.LENGTH_SHORT).show();
//						}
//					}
//				}
//				else if(height_data.trim().length() > 0 && width_data.trim().length() <= 0){
//					Toast.makeText(getApplicationContext(), "Please insert width", Toast.LENGTH_SHORT).show();
//				}else{
//					if(quality_data.trim().length() <= 0){
//						Toast.makeText(getApplicationContext(), "Please insert quality", Toast.LENGTH_SHORT).show();
//					}else {
//						check_size = 0;
//						heightPict = Integer.parseInt(height_data);
//						widthPict = Integer.parseInt(width_data);
//						qualityPict = Integer.parseInt(quality_data);
//						if(qualityPict > 0 && qualityPict <= 100){
//							ResizeImage();
//						}else{
//							Toast.makeText(getApplicationContext(), "Wrong quality input.try again !", Toast.LENGTH_SHORT).show();
//						}
//					}
//				}
//			}
//		}
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (result != null) {
			//if qrcode has nothing in it
			if (result.getContents() == null) {
				Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
			} else {
				//if qr contains data
				try {
					//converting the data to json
					JSONObject obj = new JSONObject(result.getContents());
					//setting values to textviews
					Log.d("Name = ", obj.getString("name"));
					Log.d("Address", obj.getString("address"));
				} catch (JSONException e) {
					e.printStackTrace();
//					ET_waybill.setText(result.getContents());
					STR_waybill = result.getContents();
					ET_waybill.setText(STR_waybill);
					Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
				}
			}
		} else {
//			super.onActivityResult(requestCode, resultCode, data);
			// Check which request we're responding to
			if (requestCode == 0) {
				// Make sure the request was successful
				if (resultCode == RESULT_OK) {
					ET_waybill.setText(data.getStringExtra("SCAN_RESULT"));
				} else if (resultCode == RESULT_CANCELED) {

				}

			} else if (requestCode == CAMERA_REQUEST) {
                if (resultCode == RESULT_OK) {
					fl_progress.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            compressAndLoadPhoto();
                        }
                    }, 1000);

                } else {
					if (photoFile.exists()) {
						//noinspection ResultOfMethodCallIgnored
						photoFile.delete();
					}
				}
//				if (resultCode == RESULT_OK) {
//					File out = new File(getFilesDir(), "newImage.jpg");
//					if (!out.exists()) {
//						Toast.makeText(getBaseContext(),
//								"Error while capturing image", Toast.LENGTH_LONG)
//								.show();
//						return;
//					}
//					Bitmap photo = BitmapFactory.decodeFile(out.getAbsolutePath());
//					IV_foto.setImageBitmap(photo);
//					ByteArrayOutputStream bao = new ByteArrayOutputStream();
//					photo.compress(Bitmap.CompressFormat.JPEG, 100, bao);
//					byte[] byte_arr = bao.toByteArray();
//					String image_encode = "data:image/png;base64," + Base64.encodeBytes(byte_arr);
//					String base64Image = image_encode.split(",")[1];
//					Integer pnj = image_encode.trim().length();
//					Log.d("panjang foto", String.valueOf(pnj));
//					int width = photo.getWidth();
//					int height = photo.getHeight();
//					Log.d("Debug", "Scale Foto = " + String.valueOf(width) + " px : " + String.valueOf(height) + " px");
//					Log.d("Debug", String.format("Size image from camera: %s", getReadableFileSize(image_encode.length())));
//
//					String height_data = String.valueOf(height);
//					String width_data = String.valueOf(width);
//					String quality_data = "100";
//					Photo = photo;
//
//					if (height_data.trim().length() <= 0 && width_data.trim().length() > 0) {
//						if (quality_data.trim().length() <= 0) {
//							Toast.makeText(getApplicationContext(), "Please insert quality", Toast.LENGTH_SHORT).show();
//						} else {
//							check_size = 1;
//							widthPict = Integer.parseInt(width_data);
//							qualityPict = Integer.parseInt(quality_data);
//							if (qualityPict > 0 && qualityPict <= 100) {
//								ResizeImage();
//							} else {
//								Toast.makeText(getApplicationContext(), "Wrong quality input.try again !", Toast.LENGTH_SHORT).show();
//							}
//						}
//					} else if (height_data.trim().length() > 0 && width_data.trim().length() <= 0) {
//						Toast.makeText(getApplicationContext(), "Please insert width", Toast.LENGTH_SHORT).show();
//					} else {
//						if (quality_data.trim().length() <= 0) {
//							Toast.makeText(getApplicationContext(), "Please insert quality", Toast.LENGTH_SHORT).show();
//						} else {
//							check_size = 0;
//							heightPict = Integer.parseInt(height_data);
//							widthPict = Integer.parseInt(width_data);
//							qualityPict = Integer.parseInt(quality_data);
//							if (qualityPict > 0 && qualityPict <= 100) {
//								ResizeImage();
//							} else {
//								Toast.makeText(getApplicationContext(), "Wrong quality input.try again !", Toast.LENGTH_SHORT).show();
//							}
//						}
//					}
//				}
			}
		}
	}
	 
	 public void reset() {
			IV_foto.setImageResource(R.drawable.no_image);
			STR_foto = "";
			ET_waybill.setText("");
		}

	public void ResizeImage() {
		if (Photo == null) {
			showError("Please choose an image!");
		} else {
			// Compress image in main thread using custom Compressor
			try {

				compressedImageBitmap = Photo;
				float aspectRatio = compressedImageBitmap.getWidth() /
						(float) compressedImageBitmap.getHeight();

				int width = Photo.getWidth();
				int height = Photo.getHeight();
				Log.d("Debug", "Pixel Foto = " + String.valueOf(width)+ " px : " + String.valueOf(height) + " px");

				float maxRatio = maxWidth / maxHeight;
				if (width > maxHeight || height > maxWidth) {
					if (aspectRatio < maxRatio) {
						aspectRatio = maxHeight / width;
						height = (int) (aspectRatio * height);
						width = (int) maxHeight;
					} else if (aspectRatio > maxRatio) {
						aspectRatio = maxWidth / height;
						width = (int) (aspectRatio * width);
						height = (int) maxWidth;
					} else {
						width = (int) maxHeight;
						height = (int) maxWidth;
					}
				}

				try {
					Bitmap compressedImageBitmap2 = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
					int widthcompressedImageBitmap2 = compressedImageBitmap2.getWidth();
					int heightcompressedImageBitmap2 = compressedImageBitmap2.getHeight();
					Log.d("Debug", "Pixel Foto 2 = " + String.valueOf(widthcompressedImageBitmap2)+ " px : " + String.valueOf(heightcompressedImageBitmap2) + " px");

					ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
					compressedImageBitmap = Bitmap.createScaledBitmap(compressedImageBitmap, compressedImageBitmap2.getWidth(), compressedImageBitmap2.getHeight(), false);
					compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 98, stream2);
					byte[] imageInByte2;
					imageInByte2 = stream2.toByteArray();
					Log.d("Debug", String.format("Size Image Compress - Pixel Foto 2 : %s", getReadableFileSize(imageInByte2.length)));
					String image_encode2 = "data:image/png;base64," + Base64.encodeBytes(imageInByte2);
					String base64Image2 = image_encode2.split(",")[1];

					STR_foto = base64Image2;
					Log.d("Debug","Hasil Encode Photo = "+STR_foto);
					STR_replace= STR_foto.replaceAll("[+]", ".");
					Log.d("Debug","Replace Encode Re-Image Photo = "+STR_replace);

					STR_FileNamePhoto = "PoOutstanding_" + STR_Date+"_"+STR_Time + ".jpg";
//					File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Image Compact");
					File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact");
					File file = new File(mediaStorageDir, STR_FileNamePhoto);
					Log.d("Debug", "Path Save = " + file);
					Log.d("Debug", "Name File = " + STR_FileNamePhoto);
					try {
						Log.i("Debug", "Save Photo");
						FileOutputStream out2 = new FileOutputStream(file);
						compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
						out2.flush();
						out2.close();
						Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
						File path = new File(Environment.getExternalStorageDirectory(), "Image Compact");
						Log.d("Debug", "Save - Path = " + path);
					}catch (Exception e) {
						e.printStackTrace();
					}
				} catch (OutOfMemoryError exception) {
					exception.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
				showError(e.getMessage());
			}
		}
	}

	public void UploadImageToAWS() {
		Intent intent = new Intent(this, AWSUploadService.class);
		intent.putExtra(AWSUploadService.EXTRA_PHOTO_FILE, photoFile.toURI());
		intent.putExtra(AWSUploadService.EXTRA_AWS_KEY_CODE, STR_AWS_Key_Code);
		intent.putExtra(AWSUploadService.EXTRA_AWS_SECRET_CODE, STR_AWS_Secret_Code);
		intent.putExtra(AWSUploadService.EXTRA_AWS_PATH_FOLDER, STR_AWS_Path_Folder);

		AWSUploadService.enqueueWork(this, intent);
//		credentials = new BasicAWSCredentials(STR_AWS_Key_Code,STR_AWS_Secret_Code);
//		s3 = new AmazonS3Client(credentials);
//		transferUtility = new TransferUtility(s3, multiScanPoOutstanding.this);
//
////		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Image Compact");
//		File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact");
//		File file = new File(mediaStorageDir, STR_FileNamePhoto);
//		Log.d("Debug", "Path Upload AWS From = " + file);
//		if(!file.exists()) {
//			Toast.makeText(multiScanPoOutstanding.this, "File Not Found!", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		STR_FullFileNamePhoto = "PoOutstanding_"+STR_waybill+"_"+STR_Date+"_"+STR_Time+ ".jpg";
//		Log.d("Debug", "Full File Name " + STR_FullFileNamePhoto);
////                observer = transferUtility.upload(
////                        "Compact",
////                        "Test_Video",
////                        file
////                );
//		observer = transferUtility.upload(
//                STR_AWS_Path_Folder,
//				STR_FullFileNamePhoto,
//				file,
//				CannedAccessControlList.PublicRead
//		);
//
//		observer.setTransferListener(new TransferListener() {
//			@Override
//			public void onStateChanged(int id, TransferState state) {
//
//				if (state.COMPLETED.equals(observer.getState())) {
//
//					Toast.makeText(multiScanPoOutstanding.this, "File Upload Complete", Toast.LENGTH_SHORT).show();
//				}
//			}
//
//			@Override
//			public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//				long _bytesCurrent = bytesCurrent;
//				long _bytesTotal = bytesTotal;
//
//				float percentage =  ((float)_bytesCurrent /(float)_bytesTotal * 100);
//				Log.d("percentage","" +percentage);
//				PB_PersentaseUpload.setProgress((int) percentage);
////				String strPercentage = String.format("%.02f", percentage);
//				DecimalFormat df = new DecimalFormat("#.##");
//				String strPercentage = df.format(percentage);
//				TV_PersentaseUpload.setText(strPercentage + "%");
//
//				final int progressMax = 100;
//				DecimalFormat df2 = new DecimalFormat("#");
//				String strPercentage2 = df2.format(percentage);
////				final NotificationCompat.Builder notification = new NotificationCompat.Builder(multiScanPOD.this)
////						.setSmallIcon(R.drawable.ic_launcher)
////						.setContentTitle("Upload")
////						.setContentText("Upload in progress")
////						.setPriority(NotificationCompat.PRIORITY_LOW)
////						.setOngoing(true)
////						.setOnlyAlertOnce(true)
////						.setProgress(progressMax, 0, true);
////				notificationManager.notify(2, notification.build());
////
////				notification.setProgress(progressMax, Integer.valueOf(strPercentage2), false);
////				notificationManager.notify(2, notification.build());
//
//				final NotificationCompat.Builder notification = new NotificationCompat.Builder(multiScanPoOutstanding.this)
//						.setSmallIcon(R.drawable.ic_launcher)
//						.setContentTitle("Upload")
//						.setContentText("Upload in progress")
//						.setPriority(NotificationCompat.PRIORITY_LOW)
//						.setOngoing(true)
//						.setOnlyAlertOnce(true);
//				notificationManager.notify(2, notification.build());
//
////				notification.setProgress(progressMax, Integer.valueOf(strPercentage2), false);
////				notificationManager.notify(2, notification.build());
//
//				notification.setContentText("Upload complete")
//						.setProgress(progressMax,Integer.valueOf(strPercentage2),false);
//				notificationManager.notify(2, notification.build());
//			}
//
//			@Override
//			public void onError(int id, Exception ex) {
//
//				Toast.makeText(multiScanPoOutstanding.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
//			}
//		});
	}

	private void setCompressedImage() {
		Log.d("Debug",String.format("Size Compressed : %s", getReadableFileSize(imageInByte.length)));
	}

	public String getReadableFileSize(long size) {
		if (size <= 0) {
			return "0";
		}
		final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public void showError(String errorMessage) {
		Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View view) {
		//initiating the qr code scan
		qrScan.initiateScan();
	}

	public File createImageFile() throws IOException {
		// Create a JPG image file name
		File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File storageDir = new File(pictureFolder, "Compact");

		boolean dirExists = true;

		if (!storageDir.exists()) {
			dirExists = storageDir.mkdirs();
		}

		STR_waybill = ET_waybill.getText().toString();
		if (dirExists) {
			String fileName = "PoOutstanding_" + STR_waybill + "_" + STR_Date + "_" + STR_Time + "-";
			return File.createTempFile(fileName, ".jpg", storageDir);
		}

		Toast.makeText(this, "Failed to create directory", Toast.LENGTH_LONG).show();
		return null;
	}

	private boolean deleteDirectory(File dir, boolean deleteEmptyFile) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					deleteDirectory(file, deleteEmptyFile);
				} else {
					if (deleteEmptyFile) {
						if (!file.delete()) {
							return false;
						}
					} else {
						if (file.length() > 0) {
							if (!file.delete()) {
								return false;
							}
						}
					}

				}
			}
		}
		return (dir.delete());
	}

//	@Override
//	protected void onPause() {
//		super.onPause();
//
//		deleteCompactDir(false);
//	}

	@Override
	protected void onDestroy() {
		deleteCompactDir(true);

		super.onDestroy();
	}

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    if (photoUri != null) {
      outState.putSerializable(sharedpref.EXTRA_PHOTO_FILE, photoFile);
      outState.putString(AWSUploadService.EXTRA_AWS_KEY_CODE, STR_AWS_Key_Code);
      outState.putString(AWSUploadService.EXTRA_AWS_SECRET_CODE, STR_AWS_Secret_Code);
      outState.putString(AWSUploadService.EXTRA_AWS_PATH_FOLDER, STR_AWS_Path_Folder);
    }
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);

    if (savedInstanceState != null) {
      photoFile = (File) savedInstanceState.getSerializable(sharedpref.EXTRA_PHOTO_FILE);
      STR_AWS_Key_Code = savedInstanceState.getString(AWSUploadService.EXTRA_AWS_KEY_CODE);
      STR_AWS_Secret_Code = savedInstanceState.getString(AWSUploadService.EXTRA_AWS_SECRET_CODE);
      STR_AWS_Path_Folder = savedInstanceState.getString(AWSUploadService.EXTRA_AWS_PATH_FOLDER);
    }
  }

	private void deleteCompactDir(boolean deleteEmptyFile) {
		File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File storageDir = new File(pictureFolder, "Compact");

		deleteDirectory(storageDir, deleteEmptyFile);
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

}