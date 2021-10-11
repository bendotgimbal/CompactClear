package compact.mobile.SuratJalan.Scan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import compact.mobile.BuildConfig;
import compact.mobile.DBAdapter;
import compact.mobile.R;
import compact.mobile.SessionManager;
import compact.mobile.SuratJalan.DB.C_DEX;
import compact.mobile.SuratJalan.DB.ListDEXDBAdapter;
import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;
import compact.mobile.gps_tracker;
import compact.mobile.noWaybill;
import compact.mobile.service.AWSUploadService;
import id.zelory.compressor.Compressor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

//public class multiScanDEX extends Activity{
public class multiScanDEX extends Activity implements View.OnClickListener {
	
	String[] pilihan = { "ALAMAT TUJUAN TIDAK BERPENGHUNI/ KOSONG",
			 "ALAMAT TIDAK DITEMUKAN",
			 "DITOLAK OLEH PENERIMA",
			 "KANTOR TUTUP",
			 "TUJUAN KIRIMAN MENGGUNAKAN PO BOX",
			 "PERMOHONAN PELANGGAN UNTUK DIKIRIM DI HARI SELANJUTNYA",
			 "PENGIRIMAN BERUBAH RUTE",
			 "BARANG KIRIMAN HANCUR/ RUSAK",
			 "PENERIMA SUDAH PINDAH ALAMAT",
			 "PENERIMA SUDAH MENINGGAL DUNIA",
			 "BARANG KIRIMAN TIDAK LENGKAP"};
	public String locpk,username,urlphp;
	public String STR_foto="";
	public String STR_replace="";
	String STR_no_assigment, STR_nomor;
	String STR_waybill, STR_waybill_convert, STR_lat_long, STR_kota, STR_spj, STR_tlp, STR_keterangan, STR_penerima, STR_tipe_penerima;
	String STR_date_time;
	String STR_URL_API, STR_url_pod;
	String Str_sp_url_scan_DEX;
	String RespnseMessage, RespnseCode, RespnseDebug, STR_response_code;
	String STR_FileNamePhoto, STR_FullFileNamePhoto, STR_DateTime, STR_Date, STR_Time;
	public String Str_lo_Koneksi, Str_LinkScanDEX;
	private static final int CAMERA_REQUEST = 1888;
	private ArrayAdapter<String> listWaybillAdapter ;
	ArrayList<HashMap<String, String>> hashmapArraylist_validation_code_list = new ArrayList<HashMap<String, String>>();
	String strWaybillValidationStatus = "";

	TextView TV_response_code, TV_PersentaseUpload;
	EditText ET_waybill, ET_penerima, ET_keterangan, ET_telp;
	Spinner SP_tipe_penerima;
	ImageView IV_foto;
	Button BTN_scan, BTN_take_pict, BTN_submit, BTN_reset, BTN_close, BTN_add;
	ListView LV_waybill;
	ProgressBar PB_PersentaseUpload;
	FrameLayout fl_progress;
	
	gps_tracker gps;
	private ListDEXDBAdapter dex;
	ListDEXDBAdapter db;
	DBAdapter dbListDEX;
	private  ProgressDialog progressBar;
	SessionManager session;
	
	SharedPreferences myPrefs;
	SharedPreferences.Editor spEditor;
    SharedPreferences sp;

	static Bitmap compressedImageBitmap;
	static Bitmap Photo;
	byte[] imageInByte;

	private static final float maxHeight = 720.0f;
	private static final float maxWidth = 1280.0f;

	private static final int REQUEST_WRITE_STORAGE = 112;

	private BasicAWSCredentials credentials;
	AmazonS3Client s3;
	TransferUtility transferUtility;
	TransferObserver observer;
	String STR_AWS_Key_Code, STR_AWS_Secret_Code;

	public String STR_URLScanSignature, Str_LinkScanSignature, STR_AWS_Path_Folder;

    private NotificationManagerCompat notificationManager;

	//qr code scanner object
	private IntentIntegrator qrScan;

	Uri photoUri = null;
	File photoFile = null;

	private static final int PERMISSION_REQUEST_CODE = 200;

	public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(compact.mobile.R.layout.multi_scan_dex);
	       
	       Koneksi Str_lo_Koneksi = new Koneksi();
	       Str_LinkScanDEX = Str_lo_Koneksi.ConnDEXScan();
		Str_LinkScanSignature = Str_lo_Koneksi.ConnScanSignature();
	       
	       ET_waybill = (EditText)findViewById(compact.mobile.R.id.waybill);
	       ET_penerima = (EditText)findViewById(compact.mobile.R.id.penerima);
	       ET_telp = (EditText)findViewById(compact.mobile.R.id.telp);
	       ET_keterangan = (EditText)findViewById(compact.mobile.R.id.keterangan);
	       IV_foto = (ImageView)findViewById(compact.mobile.R.id.image);
	       SP_tipe_penerima = (Spinner)findViewById(compact.mobile.R.id.spinner);
	       BTN_scan = (Button)findViewById(compact.mobile.R.id.btnScan);
	       BTN_take_pict = (Button)findViewById(compact.mobile.R.id.btnFoto);
	       BTN_submit = (Button)findViewById(compact.mobile.R.id.btnProses);
		BTN_submit.setEnabled(false);
	       BTN_reset = (Button)findViewById(compact.mobile.R.id.btnBersih);
	       BTN_close = (Button)findViewById(compact.mobile.R.id.btnKeluar);
	       LV_waybill = (ListView) findViewById(compact.mobile.R.id.ListViewWaybill);
	       TV_response_code = (TextView)findViewById(compact.mobile.R.id.txt_Response_Code);

		PB_PersentaseUpload = (ProgressBar) findViewById(R.id.progressBar1);
		TV_PersentaseUpload = (TextView) findViewById(R.id.txt_progress);

		fl_progress = (FrameLayout) findViewById(R.id.fl_progress);
	       
	       myPrefs = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE); 
	       spEditor = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE).edit();
	       sp = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE);
	       spEditor = myPrefs.edit();

        notificationManager = NotificationManagerCompat.from(this);
	       
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

	       STR_url_pod = urlphp + Str_LinkScanDEX;
			Log.d("Debug","Halaman Multi Scan DEX " +"Test URL " + STR_url_pod);
			
			Str_sp_url_scan_DEX = (myPrefs.getString("sp_dex", ""));
			Log.d("Debug", "URL Shared Preferences" 
			+ " || URL Scan DEX = " + Str_sp_url_scan_DEX);

		Intent intent = getIntent();
		hashmapArraylist_validation_code_list = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("daftar_validation_code_list");
		Log.d("Debug", "Arraylist Hashmap = "+ hashmapArraylist_validation_code_list);

		//september 2019
		for (int i = 0; i < hashmapArraylist_validation_code_list.size(); i++) {
//			String str_ValidationCode = hashmapArraylist_validation_code_list.get(i).get("ttwb_validation").toLowerCase();
			String str_ValidationCode = hashmapArraylist_validation_code_list.get(i).get("validation_kode");
			String str_ValidationCodeAWB = hashmapArraylist_validation_code_list.get(i).get("no_awb");
			String str_ValidationStatus = hashmapArraylist_validation_code_list.get(i).get("validation_status");
			Log.d("Debug", "Arraylist Validation Code = "+ str_ValidationCode+" Arraylist AWB = "+ str_ValidationCodeAWB+" Arraylist Validation Status = "+ str_ValidationStatus);
		}
	       
	       Bundle extras = getIntent().getExtras();
	       STR_no_assigment = extras.getString("asigment");
	       Log.d("Debug", "Intent From List POD / DEX >>>"+" No.Assigment = "+ STR_no_assigment);

//		if (null != extras) {
			ArrayList<String> daftar_dex_list = extras.getStringArrayList("dex_list");
			Log.i("List", "Passed Array DEX List :: " + daftar_dex_list);
//		}

		TextWatcher validationTextWatcher = new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String strWaybill = ET_waybill.getText().toString().trim().toLowerCase();
				int LengthWaybillLength = ET_waybill.getText().toString().length();
				Log.d("Debug","Length TextChanged "+LengthWaybillLength);
				if(!TextUtils.isEmpty(strWaybill)) {
					Log.i("Debug", "If ");
					for (int i = 0; i < hashmapArraylist_validation_code_list.size(); i++) {
						String strAWBArray = hashmapArraylist_validation_code_list.get(i).get("no_awb");
						int LengthWaybillLength2 = strAWBArray.length();

						if(LengthWaybillLength == LengthWaybillLength2 ) {
							Log.i("Debug", "Jumlah Waybill Sama");
							if (strAWBArray.contains(strWaybill)) {
								Log.d("Debug", strWaybill + " was found in the list");
								strWaybillValidationStatus = "1";
								Log.i("Debug", "Waybill Valid ==> " + strWaybillValidationStatus);
								return;
							}
						}else{
							Log.i("Debug", "Jumlah Waybill Tidak Sama");
							strWaybillValidationStatus = "0";
							Log.i("Debug", "Waybill Invalid ==> " + strWaybillValidationStatus);
						}
					}
					return;
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		};
		ET_waybill.addTextChangedListener(validationTextWatcher);

		ET_waybill.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					Toast.makeText(multiScanDEX.this, "focus loosed", Toast.LENGTH_LONG).show();
					Log.i("Debug", "Status Waybill ==> " + strWaybillValidationStatus);
					if(strWaybillValidationStatus == "0"){
						Log.i("Debug", "No.Waybill Tidak Valid");
						AlertDialog.Builder builder = new AlertDialog.Builder(multiScanDEX.this);
						builder.setTitle("No.Waybill Tidak Valid");
						builder.setItems(new CharSequence[] { "OK" },
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();
						BTN_submit.setEnabled(false);
						ToastWaybillTrue();
					}else{
						Log.i("Debug", "Lain - Lain");
						BTN_submit.setEnabled(true);
						ToastWaybillFalse();
					}
				}else {
					Toast.makeText(multiScanDEX.this, "focused", Toast.LENGTH_LONG).show();
				}
			}
		});

//	       HandleClick hc = new HandleClick();
//	       BTN_scan.setOnClickListener(hc);
		//intializing scan object
		qrScan = new IntentIntegrator(this);
		//attaching onclick listener
		BTN_scan.setOnClickListener(this);
	       
	       noWaybill obnomor = noWaybill.getInstance();
	       STR_nomor = obnomor.getData();
	       if (STR_nomor != null && STR_nomor.equals("")) {
				ET_waybill.setText("");
			} else {

				ET_waybill.setText(STR_nomor);
			}

		loadSpinnerData();

	       BTN_take_pict.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					if (checkPermissions()) {
						Log.d("Debug", "if checkPermissions" + " || startApplication");
						boolean success = true;
						if (success) {
							Log.d("Debug", "if-if success to create directory");
							ActivityCompat.requestPermissions(multiScanDEX.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA}, REQUEST_WRITE_STORAGE);
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
							ActivityCompat.requestPermissions(multiScanDEX.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA},REQUEST_WRITE_STORAGE);
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
						Toast.makeText(multiScanDEX.this, "Input/Scan Waybill terlebih dahulu", Toast.LENGTH_LONG).show();
						return;
					}

					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

					if (cameraIntent.resolveActivity(getPackageManager()) == null) {
						Toast.makeText(multiScanDEX.this, "Camera not found", Toast.LENGTH_LONG).show();
						return;
					}

					try {
						photoFile = createImageFile();
					} catch (IOException e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "Create file failed", Toast.LENGTH_LONG).show();
					}

					if (photoFile != null) {
						photoUri = FileProvider.getUriForFile(multiScanDEX.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
					}

					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
					startActivityForResult(cameraIntent, CAMERA_REQUEST);
				}
			});
	       
	       BTN_reset.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					ET_waybill.setText("");
					ET_penerima.setText("");
					ET_telp.setText("");
					ET_keterangan.setText("");
//	    			org = "";
//	    			dest = "";
					reset();
				}
			});
	       
	       BTN_close.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
//					main_menu(v);
					deleteCompactDir(true);
					finish();
				}
			});

	       BTN_submit.setOnClickListener(new View.OnClickListener() {
	        	
	        	public void onClick(View v) {
	        		progressBar = new ProgressDialog(multiScanDEX.this);
	        		progressBar.setCancelable(false);
	    		    progressBar.setMessage("Loading...");
	    		    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);;
	    		    progressBar.show();
	    		    
	    		    ET_waybill = (EditText) findViewById(compact.mobile.R.id.waybill);
					ET_penerima = (EditText) findViewById(compact.mobile.R.id.penerima);
					ET_telp = (EditText) findViewById(compact.mobile.R.id.telp);
					ET_keterangan = (EditText) findViewById(compact.mobile.R.id.keterangan);
	    		    
//	    		    String Lat = "0";
//	    	        String Lang = "0";
//	    	        STR_lat_long = Lat+","+Lang;
	    	        STR_kota = " ";
//	    	        STR_spj = STR_no_assigment;
	    	        String Str_assigment = STR_no_assigment;
//	    	        STR_waybill_convert = ET_waybill.getText().toString();
//	    	        STR_waybill = "'"+STR_waybill_convert+"'";
	    	        STR_waybill = ET_waybill.getText().toString();
//	    	        STR_tlp = ET_telp.getText().toString();
	    	        STR_keterangan = ET_keterangan.getText().toString();
//	    	        STR_penerima = ET_penerima.getText().toString();
	    	        STR_tipe_penerima = SP_tipe_penerima.getSelectedItem().toString();
	    	        Log.d("Debug","Cek Isi >>> "
//	    	        + " LatLong = "+STR_lat_long
	    	        + " Kota = "+STR_kota
	    	        + " No.Assigment = "+Str_assigment
	    	        + " Waybill = "+STR_waybill
//	    	        + " No.Tlp = "+STR_tlp
	    	        + " Keterangan = "+STR_keterangan
//	    	        + " Penerima = "+STR_penerima
	    	        + " Tipe Penerima = "+STR_tipe_penerima);
	    		    
	    		    if (ET_waybill.getText().toString().trim().length() == 0) {
	        			Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
	        		}
//	    		    else if (ET_penerima.getText().toString().trim().length() == 0) {
//	        			Toast.makeText(v.getContext(), "Penerima tidak boleh kosong.", Toast.LENGTH_SHORT).show();
//	        		}else if(ET_telp.getText().toString().length() == 0){
//	        			Toast.makeText(v.getContext(), "No Telp Harus Di isi", Toast.LENGTH_SHORT).show();
//	        		}
	        		else if(ET_keterangan.getText().toString().length() == 0){
	        			Toast.makeText(v.getContext(), "Remark Harus Di isi", Toast.LENGTH_SHORT).show();
	        		}
//	        		else if(STR_foto.equals("")){
//	        			Toast.makeText(v.getContext(), "Foto DEX tidak boleh kosong", Toast.LENGTH_SHORT).show();
//	        		}
					else if(photoFile == null || !photoFile.exists() || photoFile.length() == 0){
						Toast.makeText(v.getContext(), "Foto DEX tidak boleh kosong", Toast.LENGTH_SHORT).show();
					}
	        		else  {
	        			update_data_dex();
//	        			try {
//	        				cekPostDEX(STR_waybill, locpk, username, STR_lat_long, STR_foto, STR_kota, STR_spj, STR_tlp, STR_keterangan, STR_penerima, STR_tipe_penerima);
//							Log.d("Debug","Trace String");
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//							displayExceptionMessage(e.getMessage());
//						}
	        		}
	    		    progressBar.dismiss();
	        	}
	       });

	}

	public void ToastWaybillTrue() {
		Log.d("Debug", "Waybill Valid...");
		Toast.makeText(multiScanDEX.this, "Waybill Valid...", Toast.LENGTH_LONG).show();
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_success_custom_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
		final Toast toast = new Toast(getApplicationContext());
		TextView tv = (TextView) layout.findViewById(R.id.toast);
		tv.setText("Waybill Valid");
		tv.setHeight(100);
		tv.setWidth(200);
		//Set toast gravity to bottom
		toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
		//Set toast duration
		toast.setDuration(Toast.LENGTH_LONG);
		//Set the custom layout to Toast
		toast.setView(layout);
		//Display toast
		toast.show();
		new CountDownTimer(500, 500){
			public void onTick(long millisUntilFinished) {toast.show();}
			public void onFinish() {toast.show();}
		}.start();
	}

	public void ToastWaybillFalse() {
		Log.d("Debug", "Waybill Invalid...");
		Toast.makeText(multiScanDEX.this, "Waybill Invalid...", Toast.LENGTH_LONG).show();
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_custom_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
		final Toast toast = new Toast(getApplicationContext());
		TextView tv = (TextView) layout.findViewById(R.id.toast);
		tv.setText("Waybill Invalid");
		tv.setHeight(100);
		tv.setWidth(200);
		//Set toast gravity to bottom
		toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
		//Set toast duration
		toast.setDuration(Toast.LENGTH_LONG);
		//Set the custom layout to Toast
		toast.setView(layout);
		//Display toast
		toast.show();
		new CountDownTimer(500, 500){
			public void onTick(long millisUntilFinished) {toast.show();}
			public void onFinish() {toast.show();}
		}.start();
	}
	
	protected void displayExceptionMessage(String message) {
		// TODO Auto-generated method stub

	}

	private void loadSpinnerData() {
		Log.d("Debug","Lewat Load Spinner Data");
		// database handler
		DBAdapter dbListDEX = new DBAdapter(getApplicationContext());

		// Spinner Drop down elements
		List<String> lables = dbListDEX.getAllLabelsDEX();
		Log.i("Debug", "Array List DEX " + lables);

		// Creating adapter for spinner_icon
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lables);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner_icon
		SP_tipe_penerima.setAdapter(dataAdapter);
		SP_tipe_penerima.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// On selecting a spinner item
				String item = parent.getItemAtPosition(position).toString();
				if (parent.getChildAt(0) == null) {
					return;
				}
				if (!(parent.getChildAt(0) instanceof TextView)) {
					return;
				}
				((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

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
					STR_waybill = result.getContents();
					ET_waybill.setText(STR_waybill);
					Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
				}
			}
		} else {
			//super.onActivityResult(requestCode, resultCode, data);
			// Check which request we're responding to
			if (requestCode == 0) {
				// Make sure the request was successful
				if (resultCode == RESULT_OK) {
					STR_waybill = data.getStringExtra("SCAN_RESULT");
					ET_waybill.setText(STR_waybill);
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
			}
		}
	}

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
	
	public void update_data_dex() {
		Log.d("Debug", "Update Data DEX");
		
		gps = new gps_tracker(multiScanDEX.this);
		
		String Str_check_connection_dex = null;
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
		if (networkinfo != null && networkinfo.isConnected()) {
			// aksi ketika ada koneksi internet
			Str_check_connection_dex = "Yes Signal";
			Log.d("Debug", "Koneksi Internet Tersedia");
		} else {
			// aksi ketika tidak ada koneksi internet
			Str_check_connection_dex = "No Signal";
			Log.d("Debug", "Koneksi Internet Tidak Tersedia");
		}
		
		ArrayList<NameValuePair> masukparam = new ArrayList<NameValuePair>();
		String Str_waybill = STR_waybill;
		String Str_locpk = locpk;
		String Str_username = username;
//		String Str_lat_long = STR_lat_long;
		String Str_lat_long = Double.toString(gps.getLatitude()) + ", "
				+ Double.toString(gps.getLongitude());
		String Str_foto = STR_replace;
//		String Str_kota = STR_kota;
//		String Str_asigment = STR_spj;
		String Str_assigment = STR_no_assigment;
//		String Str_telp = STR_tlp;
		String Str_keterangan = STR_keterangan;
//		String Str_penerima = STR_penerima;
		String Str_tiperem = STR_tipe_penerima;
		Log.d("Debug", "Cek Update Data DEX" 
				+ " Waybill = " + Str_waybill
				+ " Locpk = " + Str_locpk
				+ " Username = " + Str_username
				+ " LatLong = " + Str_lat_long 
				+ " Foto = " + Str_foto
//				+ " Kota = " + Str_kota
				+ " Assigment = " + Str_assigment
//				+ " Tlp = " + Str_telp
				+ " Keterangan = " + Str_keterangan
//				+ " Penerima = " + Str_penerima
				+ " Tiperem = " + Str_tiperem);
		
		String response = null;
		
		ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
		masukparam1.add(new BasicNameValuePair("waybill", Str_waybill));
		masukparam1.add(new BasicNameValuePair("locpk", locpk));
		masukparam1.add(new BasicNameValuePair("username", Str_username));
		masukparam1.add(new BasicNameValuePair("lat_long", Str_lat_long));
		masukparam1.add(new BasicNameValuePair("foto", STR_replace));
//		masukparam1.add(new BasicNameValuePair("kota", Str_kota));
		masukparam1.add(new BasicNameValuePair("assigment", Str_assigment));
//		masukparam1.add(new BasicNameValuePair("telp", Str_telp));
		masukparam1.add(new BasicNameValuePair("keterangan", Str_keterangan));
//		masukparam1.add(new BasicNameValuePair("penerima", Str_penerima));
		masukparam1.add(new BasicNameValuePair("tiperem", Str_tiperem));
		masukparam1.add(new BasicNameValuePair("waktu", "sTgl"));
		
		String response1;
		
		try {
			Log.d("Debug", "Update Data DEX " + "Lewat Try");
			// cekPostPoOutstanding(STR_waybill, username, locpk, po, STR_foto);
//			cekPostDEX(STR_waybill, locpk, username, Str_lat_long, Str_foto, STR_kota, Str_assigment, STR_tlp, STR_keterangan, STR_penerima, STR_tipe_penerima);
//			cekPostDEX(STR_waybill, locpk, username, Str_lat_long, Str_foto, Str_assigment, STR_tlp, STR_keterangan, STR_penerima, STR_tipe_penerima);
//			cekPostDEX(STR_waybill, locpk, username, Str_lat_long, Str_foto, Str_assigment, STR_keterangan, STR_tipe_penerima);
//			cekPostDEX(STR_waybill, locpk, username, Str_lat_long, Str_foto, Str_assigment, STR_keterangan
//					, STR_tipe_penerima, STR_date_time, Str_check_connection_dex);
			new android.os.Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					new cekPostDEXAsync().execute();
				}
			},500);
			new android.os.Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					new cekPostSignatureAsync().execute();
				}
			},500);
			UploadImageToAWS();
			Log.d("Debug", "Trace String");
			savetoLocalDB(STR_no_assigment, Str_foto, Str_lat_long);
			reset();
		} catch (Exception e) {
			Log.d("Debug", "Update Data DEX " + "Catch");
			Log.d("Gagal", e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
			displayExceptionMessage(e.getMessage());
			savetoLocalDB(STR_no_assigment, Str_foto, Str_lat_long);
			reset();
		}
	}

	public class cekPostDEXAsync extends AsyncTask<String, String, String>
	{
		ProgressDialog pDialog;
		String Str_status;
		String Str_foto = STR_replace;
		String Str_assigment = STR_no_assigment;
		String Str_lat_long = Double.toString(gps.getLatitude()) + ", "
				+ Double.toString(gps.getLongitude());
		String STR_lat_long = Str_lat_long;

		protected void onPreExecute(){
			super.onPreExecute();

			pDialog = new ProgressDialog(multiScanDEX.this);
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
					+ "&image=" + Str_foto
//					+ "&kota=" + STR_kota
					+ "&asigment=" + Str_assigment
					+ "&telp=" + STR_tlp
					+ "&keterangan=" + STR_keterangan
					+ "&penerima=" + STR_penerima
					+ "&tiperem=" + STR_tipe_penerima
					+ "&waktu=" + STR_date_time;
			Log.d("Debug", "Parameters " + parameters);

			try
			{
				String urlphp;
				urlphp = "";
				Log.d("debug", "Host Server -> " + STR_url_pod);
				url = new URL(STR_url_pod);
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
				Log.d("Debug", "Trace Json = " + json);
				RespnseCode = json.getString("response_code");
				RespnseMessage = json.getString("response_message");
				Log.d("Debug", "Response Code = " + RespnseCode +" || "+"Response Message  = " + RespnseMessage +" || "+"Response Debug  = " + RespnseDebug);

//				TV_response_code = (TextView)findViewById(R.id.txt_Response_Code);
//				TV_response_code.setText(RespnseCode);
//				STR_response_code = TV_response_code.getText().toString();
//
//				Str_status = STR_response_code;
//				Log.d("Debug", "String Response Code = " + Str_status);
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
			TV_response_code = (TextView)findViewById(R.id.txt_Response_Code);
			TV_response_code.setText(RespnseCode);
			STR_response_code = TV_response_code.getText().toString();

			Str_status = STR_response_code;
			Log.d("Debug", "String Response Code = " + Str_status);
			if (Str_status.equals("1")) {
				Toast.makeText(getApplicationContext(), RespnseMessage, Toast.LENGTH_SHORT).show();
				Log.i("Debug", "Status Success");
				show_dialog1();
			} else if (Str_status.equals("2")) {
				Toast.makeText(getApplicationContext(), RespnseMessage, Toast.LENGTH_SHORT).show();
				Log.i("Debug", "Status Success Sebagian");
				show_dialog2();
			} else {
				Toast.makeText(getApplicationContext(), RespnseMessage, Toast.LENGTH_SHORT).show();
				Log.i("Debug", "Status Gagal");
				show_dialog3();
			}
		}
	}

	public class cekPostSignatureAsync extends AsyncTask<String, String, String>
	{
		ProgressDialog pDialog;
		String Str_status;
		String Str_assigment = STR_no_assigment;
		String STR_typescan = "DEX";
		String STR_filename = STR_FullFileNamePhoto;

		protected void onPreExecute(){
			super.onPreExecute();

			pDialog = new ProgressDialog(multiScanDEX.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			Log.d("Debug", "Post Scan DEX Signature");
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
	
	private void savetoLocalDB(String STR_no_assigment, String Str_foto, String Str_lat_long) {
		Log.d("Debug","SaveLocalDB DEX");
		C_DEX dex = new C_DEX();

		String Str_waybill = STR_waybill;
		String Str_locpk = locpk;
		String Str_username = username;
//		String Str_lat_long = STR_lat_long;
//		String Str_foto = STR_foto;
//		String Str_kota = STR_kota;
//		String Str_assigment = STR_spj;
		String Str_assigment = STR_no_assigment;
//		String Str_telp = STR_tlp;
		String Str_keterangan = STR_keterangan;
//		String Str_penerima = STR_penerima;
		String Str_tiperem = STR_tipe_penerima;
		Log.d("Debug", "Cek Save Data DEX"
				+ " Waybill = " + Str_waybill
				+ " Locpk = " + locpk
				+ " Username = " + Str_username
				+ " LatLong = " + Str_lat_long
				+ " Foto = " + Str_foto
//				+ " Kota = " + Str_kota
				+ " Assigment = " + Str_assigment
//				+ " Tlp = " + Str_telp
				+ " Keterangan = " + Str_keterangan
//				+ " Penerima = " + Str_penerima
				+ " Tiperem = " + Str_tiperem);

		dex.setWaybill(Str_waybill);
		dex.setLocpk(Str_locpk);
		dex.setUsername(Str_username);
		dex.setLat_Long(Str_lat_long);
		dex.setImage(Str_foto);
//		dex.setKota(Str_kota);
		dex.setAssigment(Str_assigment);
//		dex.setTelp(Str_telp);
		dex.setKeterangan(Str_keterangan);
//		dex.setPenerima(Str_penerima);
		dex.setTiperem(Str_tiperem);
		dex.setWaktu(STR_date_time);
		Log.d("Debug","add DEX -> local" );
		db = new ListDEXDBAdapter(multiScanDEX.this);
		db.open();
		db.createContact(dex);
		db.close();
		Log.d("PUP","AWB add to local database ..");
//		listAdapter.clear();
//		LV_scan_waybill.setAdapter(listAdapter);
	}
	
	public void show_dialog1() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanDEX.this);
			builder.setTitle("No.Waybill Berhasil Di DEX");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }
	 
	 public void show_dialog2() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanDEX.this);
			builder.setTitle("No.Waybill Berhasil Di DEX Sebagian");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }
	 
	 public void show_dialog3() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanDEX.this);
			builder.setTitle("No.Waybill Gagal Di DEX, Mohon Ulangi Kembali");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }

	 public void show_dialog4() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanDEX.this);
			builder.setTitle("No.Waybill Gagal Di Submit Ke Server, Data Di Simpan Sementara Di Lokal Database.");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }

	 public void show_dialog5() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanDEX.this);
			builder.setTitle("No.Waybill Gagal Di Finish Ke Server, Data Di Simpan Sementara Di Lokal Database.");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }

	public void reset() {
		IV_foto.setImageResource(compact.mobile.R.drawable.no_image);
		STR_foto = "";
		ET_waybill.setText("");
//		ET_penerima.setText("");
//		ET_telp.setText("");
		ET_keterangan.setText("");
	}
	
	public void main_menu(View theButton) {
		finish();
		// Intent a = new Intent (cekpod.this,main_menu.class);
		// startActivity(a);
	}

	public void UploadImageToAWS() {
		Intent intent = new Intent(this, AWSUploadService.class);
		intent.putExtra(AWSUploadService.EXTRA_PHOTO_FILE, photoFile.toURI());
		intent.putExtra(AWSUploadService.EXTRA_AWS_KEY_CODE, STR_AWS_Key_Code);
		intent.putExtra(AWSUploadService.EXTRA_AWS_SECRET_CODE, STR_AWS_Secret_Code);
		intent.putExtra(AWSUploadService.EXTRA_AWS_PATH_FOLDER, STR_AWS_Path_Folder);

		AWSUploadService.enqueueWork(this, intent);

	}

	@Override
	public void onClick(View view) {
		//initiating the qr code scan
		qrScan.initiateScan();
	}

	/**
	 * Create file with current timestamp name
	 *
	 * @return Created file
	 * @throws IOException
	 */
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
			String fileName = "DEX_" + STR_waybill + "_" + STR_Date + "_" + STR_Time;
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
