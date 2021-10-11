package compact.mobile.SuratJalan.Scan;

import androidx.core.app.ActivityCompat;
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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import compact.mobile.Base64;
import compact.mobile.BuildConfig;
import compact.mobile.DBAdapter;
import compact.mobile.SessionManager;
import compact.mobile.gps_tracker;
import compact.mobile.noWaybill;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import compact.mobile.R;
import compact.mobile.SuratJalan.DB.C_POD;
import compact.mobile.SuratJalan.DB.ListPODDBAdapter;
import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;
import compact.mobile.service.AWSUploadService;
import id.zelory.compressor.Compressor;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

//public class multiScanPOD extends Activity{
public class multiScanPOD extends Activity implements View.OnClickListener {
	
	String[] pilihan = { "PENERIMA DI WAYBILL / BERSANGKUTAN",
			 "KELUARGA YG TINGGAL SERUMAH",
			 "PEMBANTU",
			 "SECURITY",
			 "RECEPTIONIST",
			 "MAIL ROOM",
			 "SECRETARY",
			 "STAFF",
			 "ON BEHALF/ ATAS NAMA " };
	public String locpk,username,urlphp;
	public String STR_foto="";
	public String STR_replace="";
	String STR_no_assigment, STR_nomor;
	String STR_waybill, STR_waybill_convert, STR_lat_long, STR_kota, STR_spj, STR_tlp, STR_keterangan, STR_penerima, STR_tipe_penerima;
	String STR_date_time;
	String STR_URL_API, STR_url_pod;
	String Str_sp_url_scan_POD;
    String STR_validation_code, STR_spref_validation_code;
	String RespnseMessage, RespnseCode, RespnseDebug, STR_response_code;
	String STR_FileNamePhoto, STR_FullFileNamePhoto, STR_DateTime, STR_Date, STR_Time;
	public String Str_lo_Koneksi, Str_LinkScanPOD;
	private static final int CAMERA_REQUEST = 1888;
	private ArrayAdapter<String> listWaybillAdapter ;
	ArrayList<HashMap<String, String>> hashmapArraylist_validation_code_list = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> ListDataValidation = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> HashmapCodeValidation = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> HashmapWaybillValidation = new ArrayList<HashMap<String, String>>();
    private static final String KODE_VALIDATION = "ttwb_validation";
    private static final String WAYBILL = "no_awb";
	private static final String AR_VALIDATION_CODE = "validation_kode";
    String[] columnTags = new String[] {KODE_VALIDATION, WAYBILL};
    int[] columnIds = new int[] {R.id.kode_validation, R.id.waybill};
    String strCodeValidationStatus = "";
	String strWaybillValidationStatus = "";
	String strValidationStatus = "";
    String STR_validation_kode;
	
	TextView TV_response_code, TV_PersentaseUpload;
	EditText ET_waybill, ET_penerima, ET_keterangan, ET_telp;
	EditText ET_validasi, ET_validasi2;
	Spinner SP_tipe_penerima;
	ImageView IV_foto;
	Button BTN_scan, BTN_take_pict, BTN_submit, BTN_submit2, BTN_reset, BTN_close, BTN_add;
	ListView LV_waybill;
    ListView LV_list_validation;
	ProgressBar PB_PersentaseUpload;
	FrameLayout fl_progress;
	
	gps_tracker gps;
	private ListPODDBAdapter pod;
	ListPODDBAdapter db;
	DBAdapter dbListPOD;
	private  ProgressDialog progressBar;
	SessionManager session;

	ProgressDialog progressBar2;
	int progressBarStatus = 0;
	Handler progressBarHandler = new Handler();
	Runnable runCekSelesai, runUtama;
	
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
	public static final String MyPREFERENCES = "MyPrefs" ;
	public static final String IMG_SERIAL = "serial";
	SharedPreferences sharedpreferences;

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
//	String STR_filename;

	private NotificationManagerCompat notificationManager;

	//qr code scanner object
	private IntentIntegrator qrScan;

    Uri photoUri = null;
    File photoFile = null;

	private static final int PERMISSION_REQUEST_CODE = 200;

	public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

	       Koneksi Str_lo_Koneksi = new Koneksi();
	       Str_LinkScanPOD = Str_lo_Koneksi.ConnPODScan();
		Str_LinkScanSignature = Str_lo_Koneksi.ConnScanSignature();
	       
	       setContentView(R.layout.multi_scan_pod);
	       ET_waybill = (EditText)findViewById(R.id.waybill);
	       ET_penerima = (EditText)findViewById(R.id.penerima);
	       ET_telp = (EditText)findViewById(R.id.telp);
	       ET_keterangan = (EditText)findViewById(R.id.keterangan);
	       IV_foto = (ImageView)findViewById(R.id.image);
	       SP_tipe_penerima = (Spinner)findViewById(R.id.spinner);
	       BTN_scan = (Button)findViewById(R.id.btnScan);
	       BTN_take_pict = (Button)findViewById(R.id.btnFoto);
	       BTN_submit = (Button)findViewById(R.id.btnProses);
//		BTN_submit2 = (Button)findViewById(R.id.btnProses2);
		BTN_submit.setEnabled(false);
	       BTN_reset = (Button)findViewById(R.id.btnBersih);
	       BTN_close = (Button)findViewById(R.id.btnKeluar);
	       LV_waybill = (ListView) findViewById(R.id.ListViewWaybill);
	       TV_response_code = (TextView)findViewById(R.id.txt_Response_Code);
		ET_validasi = (EditText)findViewById(R.id.validasi);
		ET_validasi2 = (EditText)findViewById(R.id.validasi2);
		ET_validasi2.setEnabled(false);
        LV_list_validation = (ListView) findViewById(R.id.lv_search);

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
	       
//	       Log.e("Debug", "Halaman Multi Scan POD " +"ISI >>> " + "URL = " + urlphp);
//	 	      if (urlphp.equals("http://43.252.144.14:81/android/")) {
//	 		    	Log.i("Debug", "Halaman Multi Scan POD " +"Replace URL Lokal Server");
//	 		    	STR_URL_API = urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/compact_mobile");
//	 			}
//	 	     else if (urlphp.equals("http://api-mobile.atex.co.id/android/")) {
//	 	    	Log.i("Debug", "Halaman Multi Scan POD " +"Replace URL Online Server");
//	 	    	STR_URL_API = urlphp.replace(urlphp, "http://api-mobile.atex.co.id/compact_mobile");
//	 	    }
//	 	    else {
//		    	Toast.makeText(getApplicationContext(), "Server Tidak Tersedia",0).show();
//		    }
//	 	      
//	 	     STR_URL_API = urlphp.replace("http://43.252.144.14:81/android/", "http://43.252.144.14:81/compact_mobile");
//	 	    Log.i("Debug", "Halaman Multi Scan POD " +"Test String Replace URL " + STR_URL_API);
	 	    
//	 	   STR_url_pod = STR_URL_API + "/" +"scan_pod";
	       STR_url_pod = urlphp + Str_LinkScanPOD;
			Log.d("Debug","Halaman Multi Scan POD " +"Test URL " + STR_url_pod);
			
			Str_sp_url_scan_POD = (myPrefs.getString("sp_pod", ""));
			Log.d("Debug", "URL Shared Preferences" 
			+ " || URL Scan POD = " + Str_sp_url_scan_POD);

			//september 2019
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

		ArrayList<String> daftar_pod_list = extras.getStringArrayList("pod_list");
		Log.i("List", "Passed Array POD List :: " + daftar_pod_list);

//		ArrayList<String> arraylist_validation_code = (ArrayList<String>) getIntent().getSerializableExtra("daftar_validation_code_list");
//		Log.d("Debug", "Arraylist Validation Code = "+ arraylist_validation_code);
//
//		for (int i = 0; i < arraylist_validation_code.size(); i++) {
//
//		}

//		TextWatcher validationTextWatcher = new TextWatcher() {
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
////				String validationInput = ET_validasi.getText().toString().trim();
////
////				BTN_submit.setEnabled(!validationInput.isEmpty());
//
//                String str_validasi = ET_waybill.getText().toString().trim().toLowerCase();
//				int LengthValidasiCode = ET_waybill.getText().toString().length();
//				Log.d("Debug","Length TextChanged "+LengthValidasiCode);
//                if(!TextUtils.isEmpty(str_validasi)) {
//                    Log.i("Debug", "If ");
//					for (int i = 0; i < hashmapArraylist_validation_code_list.size(); i++) {
//						String strValidationCode = hashmapArraylist_validation_code_list.get(i).get("validation_kode");
//						String strAWB = hashmapArraylist_validation_code_list.get(i).get("no_awb");
//						String strValidationStatus = hashmapArraylist_validation_code_list.get(i).get("validation_status");
////						Log.i("Debug", "Array Validation Code " + strValidationCode+" Array AWB " + strAWB);
////						if (strValidationCode.contains(str_validasi) || strAWB.contains(str_validasi))  {
////							Log.i("Debug", "If - If");
////						}
//
//						int LengthValidasiCode2 = strAWB.length();
//
//						if(LengthValidasiCode == LengthValidasiCode2 ){
//							Log.i("Debug", "If - If");
//							if (strAWB.contains(str_validasi))  {
//								Log.i("Debug", "Valid");
//								Log.i("Debug", "Isi "+strValidationCode+" - "+strAWB+" - "+strValidationStatus);
////								ET_validasi.setVisibility(View.VISIBLE);
////								ET_validasi2.setVisibility(View.GONE);
////								Log.i("Debug", "Hasil "+strAWB.contains(str_validasi));
//								if (strValidationStatus.equals("1")) {
//									Log.i("Debug", "Satu");
////									ET_validasi.setVisibility(View.VISIBLE);
////									ET_validasi2.setVisibility(View.GONE);
//////                                    BTN_submit.setEnabled(true);
//									if(strValidationCode.equals("0")){
//										Log.i("Debug", "Validation Status = 1, Validation Code Kosong");
//										ET_validasi.setVisibility(View.GONE);
//										ET_validasi2.setVisibility(View.VISIBLE);
//										BTN_submit.setEnabled(true);
//									}else{
//										Log.i("Debug", "Disini");
//										ET_validasi.setVisibility(View.VISIBLE);
//										ET_validasi2.setVisibility(View.GONE);
//										ET_validasi.setText("");
//										ET_validasi.setEnabled(true);
//										HashmapCodeValidation.clear();
//										HashMap<String, String> mapValidationCode;
//										mapValidationCode = new HashMap<String, String>();
//										mapValidationCode.put(AR_VALIDATION_CODE, strValidationCode);
//										HashmapCodeValidation.add(mapValidationCode);
//										Log.d("Debug", "Hashmap Validation Code " + HashmapCodeValidation);
//									}
//								}else{
//									Log.i("Debug", "Nol");
//                                    BTN_submit.setEnabled(true);
//								}
//							}
//						}else{
//							Log.i("Debug", "If - Else");
//							ET_validasi.setVisibility(View.GONE);
//							ET_validasi2.setVisibility(View.VISIBLE);
//                            BTN_submit.setEnabled(false);
//						}
//					}
//                }else{
//                    Log.i("Debug", "Else ");
//                }
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//
//			}
//		};
//        ET_waybill.addTextChangedListener(validationTextWatcher);

		TextWatcher validationTextWatcher = new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				String str_validasi = ET_waybill.getText().toString().trim().toLowerCase();
				int LengthWaybillLength = ET_waybill.getText().toString().length();
				Log.d("Debug","Length TextChanged "+LengthWaybillLength);
				if(!TextUtils.isEmpty(str_validasi)) {
					Log.i("Debug", "If ");
					for (int i = 0; i < hashmapArraylist_validation_code_list.size(); i++) {
						String strValidationCodeArray = hashmapArraylist_validation_code_list.get(i).get("validation_kode");
						String strAWBArray = hashmapArraylist_validation_code_list.get(i).get("no_awb");
						String strValidationStatusArray = hashmapArraylist_validation_code_list.get(i).get("validation_status");

						int LengthWaybillLength2 = strAWBArray.length();

						if(LengthWaybillLength == LengthWaybillLength2 ) {
							Log.i("Debug", "Jumlah Waybill Sama");
							if (strAWBArray.contains(str_validasi)) {
								Log.d("Debug", str_validasi + " was found in the list");
								strWaybillValidationStatus = "1";
								Log.i("Debug", "Waybill Valid ==> " + strWaybillValidationStatus);

								if (strValidationStatusArray.equals("1")) {
									Log.i("Debug", "Validation Status = 1");
									strValidationStatus = "1";
									Log.i("Debug", "Validation Status ==> " + strValidationStatus);
									if(strValidationCodeArray.equals("0")){
										Log.i("Debug", "Validation Code Kosong");
										ET_validasi.setVisibility(View.GONE);
										ET_validasi2.setVisibility(View.VISIBLE);
										BTN_submit.setEnabled(true);
									}else{
										Log.i("Debug", " Validation Code Isi");
										ET_validasi.setVisibility(View.VISIBLE);
										ET_validasi2.setVisibility(View.GONE);
										ET_validasi.setText("");
										ET_validasi.setEnabled(true);
										HashmapCodeValidation.clear();
										HashMap<String, String> mapValidationCode;
										mapValidationCode = new HashMap<String, String>();
										mapValidationCode.put(AR_VALIDATION_CODE, strValidationCodeArray);
										HashmapCodeValidation.add(mapValidationCode);
										Log.d("Debug", "Hashmap Validation Code " + HashmapCodeValidation);
									}
								}else{
									Log.i("Debug", "Validation Status = 0");
									strValidationStatus = "0";
									Log.i("Debug", "Validation Status ==> " + strValidationStatus);
								}

								return;
							}
						}else{
							Log.i("Debug", "Jumlah Waybill Tidak Sama");
							strWaybillValidationStatus = "0";
							Log.i("Debug", "Waybill Invalid ==> " + strWaybillValidationStatus);
							ET_validasi.setText("");
							HashmapCodeValidation.clear();
							ET_validasi.setVisibility(View.GONE);
							ET_validasi2.setVisibility(View.VISIBLE);
                            ET_validasi.setBackgroundColor(Color.parseColor("#ffffff"));
                            ET_validasi2.setBackgroundColor(Color.parseColor("#c2c2c2"));
							BTN_submit.setEnabled(false);
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

		TextWatcher validationCodeTextWatcher = new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str_validasi = ET_validasi.getText().toString().trim().toLowerCase();
				int LengthValidasiCode = ET_validasi.getText().toString().length();
				Log.d("Debug","Length TextChanged "+LengthValidasiCode);
				if(!TextUtils.isEmpty(str_validasi)) {
					Log.i("Debug", "If - Validation Code");
//					for (int i = 0; i < hashmapArraylist_validation_code_list.size(); i++) {
//						String strValidationCode = hashmapArraylist_validation_code_list.get(i).get("validation_kode");
//						String strAWB = hashmapArraylist_validation_code_list.get(i).get("no_awb");
//						String strValidationStatus = hashmapArraylist_validation_code_list.get(i).get("validation_status");
//						int LengthValidasiCode2 = strValidationCode.length();
//
//						if (strValidationCode.contains(str_validasi))  {
//							Log.i("Debug", "Valid - Validation Code");
//							Log.i("Debug", "Isi "+strValidationCode+" - "+strAWB+" - "+strValidationStatus);
//							if (strValidationCode.equals(str_validasi)) {
//								Log.i("Debug", "Satu - Validation Code");
//								strCodeValidationStatus = "1";
//								Log.i("Debug", "Code ==> "+strCodeValidationStatus);
//                                STR_validation_kode = strValidationCode;
//                                Log.i("Debug", "Validation Code ==> "+STR_validation_kode);
//							}else{
//								Log.i("Debug", "Nol - Validation Code");
//								strCodeValidationStatus = "0";
//								Log.i("Debug", "Code ==> "+strCodeValidationStatus);
//                                STR_validation_kode = strValidationCode;
//                                Log.i("Debug", "Validation Code ==> "+STR_validation_kode);
//							}
//						}else{
//							Log.i("Debug", "Invalid - Validation Code");
//						}
//					}
					for (int i = 0; i < HashmapCodeValidation.size(); i++) {
						Log.d("Debug", "Hashmap Validation Code " + HashmapCodeValidation);
						String strValidationCode = HashmapCodeValidation.get(i).get("validation_kode");
						if (strValidationCode.equals(str_validasi))  {
							Log.i("Debug", "Valid");
							strCodeValidationStatus = "1";
							Log.i("Debug", "Code ==> "+strCodeValidationStatus);
						}else{
							Log.i("Debug", "Invalid");
							strCodeValidationStatus = "0";
							Log.i("Debug", "Code ==> "+strCodeValidationStatus);
						}
					}
				}else{
					Log.i("Debug", "Else - Validation Code ");
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		};
		ET_validasi.addTextChangedListener(validationCodeTextWatcher);

        ET_waybill.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Toast.makeText(multiScanPOD.this, "focus loosed", Toast.LENGTH_LONG).show();
//                    String validationInput = ET_waybill.getText().toString().trim();

//                    BTN_submit.setEnabled(!validationInput.isEmpty());
//					diProses();
					Log.i("Debug", "Status Waybill ==> " + strWaybillValidationStatus);
					Log.i("Debug", "Status Code ==> "+strCodeValidationStatus);
					Log.i("Debug", "Status Validation ==> "+strValidationStatus);
					if(strWaybillValidationStatus != "1"){
						Log.i("Debug", "No.Waybill Tidak Valid");
						AlertDialog.Builder builder = new AlertDialog.Builder(multiScanPOD.this);
						builder.setTitle("No.Waybill Tidak Valid");
						builder.setItems(new CharSequence[] { "OK" },
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();
					}else if(strWaybillValidationStatus == "1" && strValidationStatus == "0"){
						Log.i("Debug", "No.Waybill Valid, Status Validasi Kosong");
						BTN_submit.setEnabled(true);
					}else if(strWaybillValidationStatus == "1" && strCodeValidationStatus == "1" && strValidationStatus == "1"){
                        Log.i("Debug", "No.Waybill Valid, Status Validasi 1, Status Code 1");
                        BTN_submit.setEnabled(true);
                    }else{
						Log.i("Debug", "Lain - Lain");
						BTN_submit.setEnabled(false);
					}
                } else {
                    Toast.makeText(multiScanPOD.this, "focused", Toast.LENGTH_LONG).show();
//					String validationInput = ET_waybill.getText().toString().trim();

//					BTN_submit.setEnabled(!validationInput.isEmpty());
                }
            }
        });

        ET_validasi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
				Log.i("Debug", "Code Validation ==> "+strCodeValidationStatus);
                Log.i("Debug", "Validation CODE ==> "+STR_validation_kode);
                if (!hasFocus) {
                    Toast.makeText(multiScanPOD.this, "validation field focus loosed", Toast.LENGTH_LONG).show();
                    String validationInput = ET_validasi.getText().toString().trim();
					diProsesValidasi();
                } else {
                    Toast.makeText(multiScanPOD.this, "validation field focused", Toast.LENGTH_LONG).show();
                    String validationInput = ET_validasi.getText().toString().trim();
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
	       
//	       ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, pilihan);
//	       aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	       SP_tipe_penerima.setAdapter(aa);
//	       SP_tipe_penerima .setOnItemSelectedListener(new MyOnItemSelectedListener());
	       
	       BTN_take_pict.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
//					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
//	                startActivityForResult(cameraIntent, CAMERA_REQUEST);

					if (checkPermissions()) {
						Log.d("Debug", "if checkPermissions" + " || startApplication");
						boolean success = true;
						if (success) {
							Log.d("Debug", "if-if success to create directory");
							ActivityCompat.requestPermissions(multiScanPOD.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA}, REQUEST_WRITE_STORAGE);
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
							ActivityCompat.requestPermissions(multiScanPOD.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA},REQUEST_WRITE_STORAGE);
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
                        Toast.makeText(multiScanPOD.this, "Input/Scan Waybill terlebih dahulu", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    if (cameraIntent.resolveActivity(getPackageManager()) == null) {
                        Toast.makeText(multiScanPOD.this, "Camera not found", Toast.LENGTH_LONG).show();
                        return;
                    }

                    try {
                        photoFile = createImageFile(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Create file failed", Toast.LENGTH_LONG).show();
                    }

                    if (photoFile != null) {
                        photoUri = FileProvider.getUriForFile(multiScanPOD.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
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
                    ET_validasi.setText("");
                    ET_validasi2.setText("");
                    ET_validasi.setVisibility(View.GONE);
					ET_validasi2.setVisibility(View.VISIBLE);
					BTN_submit.setEnabled(false);
					ET_validasi.setBackgroundColor(Color.parseColor("#ffffff"));
					ET_validasi2.setBackgroundColor(Color.parseColor("#c2c2c2"));
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
	       
//	       BTN_add.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//				if (ET_waybill.getText().toString().trim().length() == 0) {
//	       			//Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
//					ET_waybill.requestFocus();
//	       		} else {
//	       			
//	       			String[] sWB;
//	       			String sTemp1, sTemp2;
//	       			if (ET_waybill.getText().toString().trim().indexOf("-") > 0)
//	       			{
//	       				sWB = ET_waybill.getText().toString().trim().split("-");
//	       				sTemp1 = (sWB[0]+"0000").substring(0,4);
//	       				sTemp2 = "000000000000"+sWB[1]; 
//	       				sTemp2 = sTemp2.substring(sTemp2.length() - 9);
//	       				ET_waybill.setText(sTemp1 + sTemp2);
//	       				Log.d("add waybill","waybill : "+ET_waybill.getText().toString().trim());
//	       			}
//	       			
//	       			add_list();
//	       			
//	       		}
//				}
//			});
	       
	       BTN_submit.setOnClickListener(new View.OnClickListener() {
	        	
	        	public void onClick(View v) {
	        		progressBar = new ProgressDialog(multiScanPOD.this);
	        		progressBar.setCancelable(false);
	    		    progressBar.setMessage("Loading...");
	    		    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);;
	    		    progressBar.show();
	    		    
	    		    ET_waybill = (EditText) findViewById(R.id.waybill);
					ET_penerima = (EditText) findViewById(R.id.penerima);
					ET_telp = (EditText) findViewById(R.id.telp);
					ET_keterangan = (EditText) findViewById(R.id.keterangan);
	    		    
//	    		    String Lat = "0";
//	    	        String Lang = "0";
//	    	        STR_lat_long = Lat+","+Lang;
	    	        STR_kota = " ";
//	    	        STR_spj = STR_no_assigment;
	    	        String Str_assigment = STR_no_assigment;
//	    	        STR_waybill_convert = ET_waybill.getText().toString();
//	    	        STR_waybill = "'"+STR_waybill_convert+"'";
	    	        STR_waybill = ET_waybill.getText().toString();
	    	        STR_tlp = ET_telp.getText().toString();
	    	        STR_keterangan = ET_keterangan.getText().toString();
	    	        STR_penerima = ET_penerima.getText().toString();
	    	        STR_tipe_penerima = SP_tipe_penerima.getSelectedItem().toString();
	    	        Log.d("Debug","Cek Isi >>> "
//	    	        + " LatLong = "+STR_lat_long
	    	        + " Kota = "+STR_kota
	    	        + " No.Assigment = "+Str_assigment
	    	        + " Waybill = "+STR_waybill
	    	        + " No.Tlp = "+STR_tlp
	    	        + " Keterangan = "+STR_keterangan
	    	        + " Penerima = "+STR_penerima
	    	        + " Tipe Penerima = "+STR_tipe_penerima);
	    		    
	    		    if (ET_waybill.getText().toString().trim().length() == 0) {
	        			Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
	        		}
	        		else if (ET_penerima.getText().toString().trim().length() == 0) {
	        			Toast.makeText(v.getContext(), "Penerima tidak boleh kosong.", Toast.LENGTH_SHORT).show();
	        		}else if(ET_telp.getText().toString().length() == 0){
	        			Toast.makeText(v.getContext(), "No Telp Harus Di isi", Toast.LENGTH_SHORT).show();
	        		}
	        		else if(ET_keterangan.getText().toString().length() == 0){
	        			Toast.makeText(v.getContext(), "Remark Harus Di isi", Toast.LENGTH_SHORT).show();
	        		}
//	        		else if(STR_foto.equals("")){
//	        			Toast.makeText(v.getContext(), "Foto POD tidak boleh kosong", Toast.LENGTH_SHORT).show();
//	        		}
					else if(photoFile == null || !photoFile.exists() || photoFile.length() == 0){
						Toast.makeText(v.getContext(), "Foto POD tidak boleh kosong", Toast.LENGTH_SHORT).show();
					}
	        		else  {
	        			update_data_pod();
//	        			try {
//	        				cekPostPOD(STR_waybill, locpk, username, STR_lat_long, STR_foto, STR_kota, STR_spj, STR_tlp, STR_keterangan, STR_penerima, STR_tipe_penerima);
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
	
	protected void displayExceptionMessage(String message) {
		// TODO Auto-generated method stub

	}

	public void diProses(){
		Log.d("Debug", "Sedang diproses...");

		//membuat progress dialog
		progressBar2 = new ProgressDialog(multiScanPOD.this);
		progressBar2.setMax(100); //nilai maksimum
		progressBar2.setMessage("Loading");
		progressBar2.setTitle("Tunggu Sebentar");
		progressBar2.setIcon(R.drawable.ic_launcher);
		progressBar2.setCancelable(false); //menonaktifkan tombol BACK
//		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar2.show();

		progressBarStatus = 0; //inisialisasi nilai awal

//		//membuat Thread jika nilai sudah mencapai 100
//		runCekSelesai = new Runnable() {
//			@Override
//			public void run() {
//				progressBar2.setProgress(progressBarStatus);
//				if (progressBarStatus >= 100){
//					Log.d("Debug", "Selesai");
//					progressBar2.dismiss();
//				}
//			}
//		};

//		//membuat Thread jika nilai masih kecil dari 100
//		runUtama = new Runnable() {
//			@Override
//			public void run() {
//				while (progressBarStatus < 100) {
//					progressBarStatus += 10;
//					try {
//						/*simulasi seolah-olah setiap 10%
//						 * membutuhkan waktu 0.5 detik*/
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					//pengecekan Thread runCekSelesai
//					progressBarHandler.post(runCekSelesai);
//				}
//			}
//		};

		runUtama = new Runnable() {
			@Override
			public void run() {
				try {
//					Thread.sleep(10000);
					Thread.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.d("Debug", "Selesai");
				progressBar2.dismiss();
			}
		};

		//jalankan Thread(runUtama)
		Thread jalan = new Thread(runUtama);
		jalan.start();
	}

	public void diProsesValidasi(){
		Log.d("Debug", "Sedang diproses...");
		Log.i("Debug", "Validation Status ==> "+strCodeValidationStatus);

		//membuat progress dialog
		progressBar2 = new ProgressDialog(multiScanPOD.this);
		progressBar2.setMax(100); //nilai maksimum
		progressBar2.setMessage("Loading");
		progressBar2.setTitle("Tunggu Sebentar");
		progressBar2.setIcon(R.drawable.ic_launcher);
		progressBar2.setCancelable(false); //menonaktifkan tombol BACK
		progressBar2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar2.show();

		progressBarStatus = 0; //inisialisasi nilai awal

		runUtama = new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.d("Debug", "Selesai");
				progressBar2.dismiss();
			}
		};

		//jalankan Thread(runUtama)
		Thread jalan = new Thread(runUtama);
		jalan.start();

		if(strCodeValidationStatus == "1"){
			BTN_submit.setEnabled(true);
			ToastStoreCodeTrue();
			ET_validasi.setEnabled(false);
			ET_validasi.setBackgroundColor(Color.parseColor("#c2c2c2"));
			ET_validasi2.setBackgroundColor(Color.parseColor("#ffffff"));
		}else{
			BTN_submit.setEnabled(false);
			ToastStoreCodeFalse();
		}
	}

    public void ToastStoreCodeTrue() {
		Log.d("Debug", "Kode Valid...");
		Toast.makeText(multiScanPOD.this, "Kode Valid...", Toast.LENGTH_LONG).show();
//        BTN_submit.setEnabled(true);
//        ET_validasi.setEnabled(false);
//        ET_validasi.setBackgroundColor(Color.parseColor("#c2c2c2"));
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_success_custom_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
        final Toast toast = new Toast(getApplicationContext());
        TextView tv = (TextView) layout.findViewById(R.id.toast);
        tv.setText("Kode Valid");
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
        new CountDownTimer(1000, 1000){
            public void onTick(long millisUntilFinished) {toast.show();}
            public void onFinish() {toast.show();}
        }.start();
    }

    public void ToastStoreCodeFalse() {
		Log.d("Debug", "Kode Invalid...");
		Toast.makeText(multiScanPOD.this, "Kode Invalid...", Toast.LENGTH_LONG).show();
//        BTN_submit.setEnabled(false);
//        ET_validasi.setEnabled(true);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
        final Toast toast = new Toast(getApplicationContext());
        TextView tv = (TextView) layout.findViewById(R.id.toast);
        tv.setText("Kode Invalid");
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
        new CountDownTimer(1000, 1000){
            public void onTick(long millisUntilFinished) {toast.show();}
            public void onFinish() {toast.show();}
        }.start();
    }

	private void loadSpinnerData() {
		Log.d("Debug","Lewat Load Spinner Data");
		// database handler
		DBAdapter dbListPOD = new DBAdapter(getApplicationContext());

		// Spinner Drop down elements
		List<String> lables = dbListPOD.getAllLabelsPOD();
		Log.i("Debug", "Array List POD " + lables);

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
	
//	public void add_list(){
//		int x = listWaybillAdapter.getPosition(ET_waybill.getText().toString().trim()) ;
//		Log.d("add waybill","cek posisi " + ET_waybill.getText().toString().trim() + " => " + x);
//		if (x < 0 ) {
//			listWaybillAdapter.add(ET_waybill.getText().toString().trim());
//			Log.d("add waybill", "=> " + ET_waybill.getText().toString().trim() );
//			LV_waybill.setAdapter(listWaybillAdapter);
//			
//			int i,a;
//			String STR_waybill_in_list = "";
//			
//			i = this.LV_waybill.getCount();
//			for (a = 0; a<i; a++) {
//				if(a<i-1){
//					STR_waybill_in_list += "'"+this.LV_waybill.getAdapter().getItem(a)+ "',";
//				}
//				else{
//					STR_waybill_in_list += "'"+this.LV_waybill.getAdapter().getItem(a)+ "'";
//				}
//			}
//			Log.d("add list waybill", STR_waybill_in_list);
//			STR_waybill = STR_waybill;
//			Log.d("Debug", "String Trim AWB => " + STR_waybill);
//		}
//		ET_waybill.requestFocus();
//		ET_waybill.setText("");
//	}
	
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
	
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//    	 if (requestCode == 0) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//            	ET_waybill.setText(data.getStringExtra("SCAN_RESULT"));
//	  	      } else if (resultCode == RESULT_CANCELED) {
//
//	  	      }
//
//        }else{
//        	if (resultCode == RESULT_OK) {
//        	Bitmap photo = (Bitmap) data.getExtras().get("data");
//        	IV_foto.setImageBitmap(photo);
//
//            ByteArrayOutputStream bao = new ByteArrayOutputStream();
//
//            photo.compress(Bitmap.CompressFormat.JPEG, 100, bao);
//            byte [] byte_arr = bao.toByteArray();
////            String image_encode = Base64.encodeBytes(byte_arr);
//				String image_encode = "data:image/jpg;base64,"+Base64.encodeBytes(byte_arr);
//				String base64Image = image_encode.split(",")[1];
//            Integer pnj = image_encode.trim().length();
//	        Log.d("panjang foto", String.valueOf(pnj));
//				int width = photo.getWidth();
//				int height = photo.getHeight();
//				Log.d("Debug", "Scale Foto = " + String.valueOf(width)+ " px : " + String.valueOf(height) + " px");
//				Log.d("Debug", String.format("Size Foto: %s", getReadableFileSize(byte_arr.length)));
//    		//set image hasil encode
//////            STR_foto = image_encode;
////				STR_foto = base64Image;
////            Log.d("Debug","Hasil Encode Photo = "+STR_foto);
////				STR_replace= STR_foto.replaceAll("[+]", ".");
////				Log.d("Debug","Replace Encode Photo = "+STR_replace);
//
//				Bitmap photo2 = (Bitmap) data.getExtras().get("data");
//				ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
//				photo2.compress(Bitmap.CompressFormat.JPEG, 100, bao2);
////                final Bitmap myImage2 = photo2;
//				byte [] byte_arr2 = bao2.toByteArray();
//				int width2 = photo2.getWidth();
//				int height2 = photo2.getHeight();
//				Log.d("Debug", "Ukuran From Camera Original = " + String.valueOf(width2)+ " px : " + String.valueOf(height2) + " px");
//				Log.d("Debug", String.format("Size Image From Camera Original: %s", getReadableFileSize(byte_arr2.length)));
//
////				String height_data="240";
////				String width_data="240";
////				String quality_data="80";
////                Bitmap photo3 = (Bitmap) data.getExtras().get("data");
//				final Bitmap photo3 = (Bitmap) data.getExtras().get("data");
//				ByteArrayOutputStream baoOriginal = new ByteArrayOutputStream();
//				photo3.compress(Bitmap.CompressFormat.JPEG, 100, baoOriginal);
//				byte [] byte_arrOriginal = baoOriginal.toByteArray();
//				int widthOriginal = photo3.getWidth();
//				int heightOriginal = photo3.getHeight();
//				Log.d("Debug", "Ukuran From Camera Original 2 = " + String.valueOf(widthOriginal)+ " px : " + String.valueOf(heightOriginal) + " px");
//				Log.d("Debug", String.format("Size Image From Camera Original 2 : %s", getReadableFileSize(byte_arrOriginal.length)));
//				Photo = photo3;
//
////				if(height_data.trim().length() <= 0 && width_data.trim().length() > 0){
////					if(quality_data.trim().length() <= 0){
////						Toast.makeText(getApplicationContext(), "Please insert quality", Toast.LENGTH_SHORT).show();
////					}else {
////						check_size = 1;
////						widthPict = Integer.parseInt(width_data);
////						qualityPict = Integer.parseInt(quality_data);
////						if(qualityPict > 0 && qualityPict <= 100){
////							ResizeImage();
////						}else{
////							Toast.makeText(getApplicationContext(), "Wrong quality input.try again !", Toast.LENGTH_SHORT).show();
////						}
////					}
////				}
////				else if(height_data.trim().length() > 0 && width_data.trim().length() <= 0){
////					Toast.makeText(getApplicationContext(), "Please insert width", Toast.LENGTH_SHORT).show();
////				}else{
////					if(quality_data.trim().length() <= 0){
////						Toast.makeText(getApplicationContext(), "Please insert quality", Toast.LENGTH_SHORT).show();
////					}else {
////						check_size = 0;
////						heightPict = Integer.parseInt(height_data);
////						widthPict = Integer.parseInt(width_data);
////						qualityPict = Integer.parseInt(quality_data);
////						if(qualityPict > 0 && qualityPict <= 100){
////							ResizeImage();
////						}else{
////							Toast.makeText(getApplicationContext(), "Wrong quality input.try again !", Toast.LENGTH_SHORT).show();
////						}
////					}
////
////				}
//
//				imageInByte = baoOriginal.toByteArray();
//				Log.d("Debug", String.format("Size Image : %s", getReadableFileSize(imageInByte.length)));
//				Log.d("Debug", String.format("Size Image 1 : %s", getReadableFileSize(image_encode.length())));
//				STR_foto = base64Image;
//				Log.d("Debug","Hasil Encode Photo If = "+STR_foto);
//				STR_replace= STR_foto.replaceAll("[+]", ".");
//				Log.d("Debug","Replace Encode Re-Image Photo = "+STR_replace);
//
//        	}
//        }
//    }

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
//
////				new Compressor(this)
////						.compressToFileAsFlowable(out)
////						.subscribeOn(Schedulers.io())itu 100kb
////						.observeOn(AndroidSchedulers.mainThread())
////						.subscribe(new Consumer<File>() {
////							@Override
////							public void accept(File file) {
////								compressedImage = file;
////								setCompressedImage();
////							}
////						}, new Consumer<Throwable>() {
////							@Override
////							public void accept(Throwable throwable) {
////								throwable.printStackTrace();
////								showError(throwable.getMessage());
////							}
////						});
//
////				File file = new File(new File("/sdcard/Image Resize/"),"Comp_img_"+ sharedpreferences.getString(IMG_SERIAL,"1")+".jpg");
////				try {
////					Log.i("Debug", "Save Photo");
////					FileOutputStream out2 = new FileOutputStream(file);
////					photo.compress(Bitmap.CompressFormat.JPEG, 100, out2);
////					out2.flush();
////					out2.close();
////					Toast.makeText(getApplicationContext(),"Saved", Toast.LENGTH_SHORT).show();
////					SharedPreferences.Editor editor = sharedpreferences.edit();
////					int abc = Integer.parseInt(sharedpreferences.getString(IMG_SERIAL,"1"));
////					abc++;
////					editor.putString(IMG_SERIAL,String.valueOf(abc));
////					editor.commit();
////					String path =  Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
////					Log.d("Debug", "Save - Path = " + path);
////				} catch (Exception e) {
////					e.printStackTrace();
////				}
//
////				if (image_encode.length() >= 5000) {
//////					File file = new File(new File("/sdcard/Image Resize Compact/"), "Comp_img_" + sharedpreferences.getString(IMG_SERIAL, "1") + ".jpg");
//////					File file = new File(new File("/sdcard/Image Compact/"), "Comp_img_" + sharedpreferences.getString(IMG_SERIAL, "1") + ".jpg");
//////					File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
////					File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Image Compact");
////					File file = new File(mediaStorageDir, "Comp_img_" + sharedpreferences.getString(IMG_SERIAL, "1") + ".jpg");
////					try {
////						Log.i("Debug", "Save Photo Up 5MB");
////						FileOutputStream out2 = new FileOutputStream(file);
////						photo.compress(Bitmap.CompressFormat.JPEG, 80, out2);
////						out2.flush();
////						out2.close();
////						Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
////						SharedPreferences.Editor editor = sharedpreferences.edit();
////						int abc = Integer.parseInt(sharedpreferences.getString(IMG_SERIAL, "1"));
////						abc++;
////						editor.putString(IMG_SERIAL, String.valueOf(abc));
////						editor.commit();
//////						String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
//////						String path = Environment.getExternalStorageDirectory() + File.separator + "Image Compact" + File.separator;
//////						File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
////						File path = new File(Environment.getExternalStorageDirectory(), "Image Compact");
////						Log.d("Debug", "Save - Path = " + path);
////					} catch (Exception e) {
////						e.printStackTrace();
////					}
////				} else {
//////					File file = new File(new File("/sdcard/Image Resize Compact/"), "Comp_img_" + sharedpreferences.getString(IMG_SERIAL, "1") + ".jpg");
//////					File file = new File(new File("/sdcard/Image Compact/"), "Comp_img_" + sharedpreferences.getString(IMG_SERIAL, "1") + ".jpg");
//////					File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
////					File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Image Compact");
////					File file = new File(mediaStorageDir, "Comp_img_" + sharedpreferences.getString(IMG_SERIAL, "1") + ".jpg");
////					try {
////						Log.i("Debug", "Save Photo Under 5MB");
////						FileOutputStream out2 = new FileOutputStream(file);
////						photo.compress(Bitmap.CompressFormat.JPEG, 100, out2);
////						out2.flush();
////						out2.close();
////						Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
////						SharedPreferences.Editor editor = sharedpreferences.edit();
////						int abc = Integer.parseInt(sharedpreferences.getString(IMG_SERIAL, "1"));
////						abc++;
////						editor.putString(IMG_SERIAL, String.valueOf(abc));
////						editor.commit();
//////						String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
//////						String path = Environment.getExternalStorageDirectory() + File.separator + "Image Compact" + File.separator;
//////						File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
////						File path = new File(Environment.getExternalStorageDirectory(), "Image Compact");
////						Log.d("Debug", "Save - Path = " + path);
////					} catch (Exception e) {
////						e.printStackTrace();
////					}
////				}
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

			} else if (requestCode == CAMERA_REQUEST){
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
	
	public void update_data_pod() {
		Log.d("Debug", "Update Data POD");
		
		gps = new gps_tracker(multiScanPOD.this);
		
		String Str_check_connection_pod = null;
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
		if (networkinfo != null && networkinfo.isConnected()) {
			// aksi ketika ada koneksi internet
			Str_check_connection_pod = "Yes Signal";
			Log.d("Debug", "Koneksi Internet Tersedia");
		} else {
			// aksi ketika tidak ada koneksi internet
			Str_check_connection_pod = "No Signal";
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
		String Str_telp = STR_tlp;
		String Str_keterangan = STR_keterangan;
		String Str_penerima = STR_penerima;
		String Str_tiperem = STR_tipe_penerima;
        String Str_validation_kode = STR_validation_kode;
		Log.d("Debug", "Cek Update Data POD" 
				+ " Waybill = " + STR_waybill
				+ " Locpk = " + Str_locpk
				+ " Username = " + Str_username
				+ " LatLong = " + Str_lat_long 
				+ " Foto = " + Str_foto
//				+ " Kota = " + Str_kota
				+ " Assigment = " + Str_assigment
				+ " Tlp = " + Str_telp
				+ " Keterangan = " + Str_keterangan
				+ " Penerima = " + Str_penerima
				+ " Tiperem = " + Str_tiperem
                + " Validation Code = " + Str_validation_kode);
		
		String response = null;
		
		ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
		masukparam1.add(new BasicNameValuePair("waybill", Str_waybill));
		masukparam1.add(new BasicNameValuePair("locpk", locpk));
		masukparam1.add(new BasicNameValuePair("username", Str_username));
		masukparam1.add(new BasicNameValuePair("lat_long", Str_lat_long));
		masukparam1.add(new BasicNameValuePair("foto", STR_replace));
//		masukparam1.add(new BasicNameValuePair("kota", Str_kota));
		masukparam1.add(new BasicNameValuePair("assigment", Str_assigment));
		masukparam1.add(new BasicNameValuePair("telp", Str_telp));
		masukparam1.add(new BasicNameValuePair("keterangan", Str_keterangan));
		masukparam1.add(new BasicNameValuePair("penerima", Str_penerima));
		masukparam1.add(new BasicNameValuePair("tiperem", Str_tiperem));
		masukparam1.add(new BasicNameValuePair("waktu", "sTgl"));
		
		String response1;
		
		try {
			Log.d("Debug", "Update Data POD " + "Lewat Try");
			// cekPostPoOutstanding(STR_waybill, username, locpk, po, STR_foto);
//			cekPostPOD(STR_waybill, locpk, username, Str_lat_long, Str_foto, STR_kota, Str_assigment, STR_tlp, STR_keterangan, STR_penerima, STR_tipe_penerima);
//			cekPostPOD(STR_waybill, locpk, username, Str_lat_long, Str_foto, Str_assigment, STR_tlp, STR_keterangan, STR_penerima, STR_tipe_penerima);
//			cekPostPOD(STR_waybill, locpk, username, Str_lat_long, Str_foto, Str_assigment, STR_tlp, STR_keterangan, STR_penerima
//					, STR_tipe_penerima, STR_date_time, Str_check_connection_pod);
//			Log.d("Debug", "Trace String");
			new android.os.Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					new cekPostPODAsync().execute();
				}
			},500);
			new android.os.Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					new cekPostSignatureAsync().execute();
				}
			},500);
			UploadImageToAWS();
			savetoLocalDB(STR_no_assigment, Str_foto, Str_lat_long);
			reset();
		} catch (Exception e) {
			Log.d("Debug", "Update Data POD " + "Catch");
			Log.d("Gagal", e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
			displayExceptionMessage(e.getMessage());
			savetoLocalDB(STR_no_assigment, Str_foto, Str_lat_long);
			reset();
		}
	}
	
	public void cekPostPOD(String STR_waybill, String locpk, String username, String STR_lat_long, String Str_foto
			, String Str_assigment, String STR_tlp, String STR_keterangan, String STR_penerima
			, String STR_tipe_penerima, String STR_date_time, String Str_check_connection_pod) throws JSONException {
		
		Log.d("Debug", "Lewat Post POD");	
	 	String Str_2nd_check_connection_pod = Str_check_connection_pod;
		Log.d("Debug", "2nd Response Check Connection  = " + Str_2nd_check_connection_pod);
		if (Str_2nd_check_connection_pod.equals("No Signal")) {
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
				+ "&lat_long=" + STR_lat_long
				+ "&image=" + Str_foto
//				+ "&kota=" + STR_kota
				+ "&asigment=" + Str_assigment
				+ "&telp=" + STR_tlp
				+ "&keterangan=" + STR_keterangan
				+ "&penerima=" + STR_penerima
				+ "&tiperem=" + STR_tipe_penerima
				+ "&waktu=" + STR_date_time;
		Log.d("Debug", "Parameters " + parameters);
		try {
			url = new URL(STR_url_pod);
//			url = new URL(Str_sp_url_scan_POD);
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
				sb.append("response_debug");
				Log.d("Debug", "TraceLine = " + line);
			}
			JSONObject json = new JSONObject(sb.toString());
			Log.d("Debug", "Trace Json = " + json);
			RespnseCode = json.getString("response_code");
			RespnseMessage = json.getString("response_message");
//			RespnseDebug = json.getString("response_debug");
			Log.d("Debug", "Response Code = " + RespnseCode +" || "+"Response Message  = " + RespnseMessage +" || "+"Response Debug  = " + RespnseDebug);
//			Log.d("Debug", "Response Message  = " + RespnseMessage);
			
			TV_response_code = (TextView)findViewById(R.id.txt_Response_Code);
			TV_response_code.setText(RespnseCode);
			STR_response_code = TV_response_code.getText().toString();
			
			String Str_status = STR_response_code;
			Log.d("Debug", "String Response Code = " + Str_status);
			
//			if (TV_response_code.equals("0")) {
//				Toast.makeText(getApplicationContext(), "Mohon Periksa Kembali",0).show();
//			} else {
//				Toast.makeText(getApplicationContext(), "AWB Berhasil Di Proses",0).show();
//				Intent a = new Intent(multiScanPOD.this, ListPOD_DEX.class);
//				reset();
//			}
			
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
			
		} catch (IOException e) {
			// Error
			Log.d("Debug", "Trace = ERROR ");
		}
		}
	}

	public class cekPostPODAsync extends AsyncTask<String, String, String>
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

			pDialog = new ProgressDialog(multiScanPOD.this);
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
					+ "&waktu=" + STR_date_time
                    + "&validation_kode=" + STR_validation_kode;
			Log.d("Debug", "Parameters cekPostPODAsync " + parameters);

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
			TV_response_code = (TextView)findViewById(R.id.txt_Response_Code);
			TV_response_code.setText(RespnseCode);
			STR_response_code = TV_response_code.getText().toString();

			Str_status = STR_response_code;
			Log.d("Debug", "String Response Code = " + Str_status);
			Log.d("Debug", "1.Test Sampai Sini ");
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
		String STR_typescan = "POD";
		String STR_filename = STR_FullFileNamePhoto;

		protected void onPreExecute(){
			super.onPreExecute();

			pDialog = new ProgressDialog(multiScanPOD.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			Log.d("Debug", "Post Scan POD Signature");
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
		Log.d("Debug","SaveLocalDB POD");
		C_POD pod = new C_POD();
		
		String Str_waybill = STR_waybill;
		String Str_locpk = locpk;
		String Str_username = username;
//		String Str_lat_long = STR_lat_long;
//		String Str_foto = STR_foto;
//		String Str_kota = STR_kota;
//		String Str_assigment = STR_spj;
		String Str_assigment = STR_no_assigment;
		String Str_telp = STR_tlp;
		String Str_keterangan = STR_keterangan;
		String Str_penerima = STR_penerima;
		String Str_tiperem = STR_tipe_penerima;
		Log.d("Debug", "Cek Save Data POD" 
				+ " Waybill = " + Str_waybill
				+ " Locpk = " + locpk
				+ " Username = " + Str_username
				+ " LatLong = " + Str_lat_long 
				+ " Foto = " + Str_foto
//				+ " Kota = " + Str_kota
				+ " Assigment = " + Str_assigment
				+ " Tlp = " + Str_telp
				+ " Keterangan = " + Str_keterangan
				+ " Penerima = " + Str_penerima
				+ " Tiperem = " + Str_tiperem);
		
		pod.setWaybill(Str_waybill);
		pod.setLocpk(Str_locpk);
		pod.setUsername(Str_username);
		pod.setLat_Long(Str_lat_long);
		pod.setImage(Str_foto);
//		pod.setKota(Str_kota);
		pod.setAssigment(Str_assigment);
		pod.setTelp(Str_telp);
		pod.setKeterangan(Str_keterangan);
		pod.setPenerima(Str_penerima);
		pod.setTiperem(Str_tiperem);
		pod.setWaktu(STR_date_time);
		Log.d("Debug","add POD -> local" );
		db = new ListPODDBAdapter(multiScanPOD.this);
		db.open();
		db.createContact(pod);
		db.close();
		Log.d("PUP","AWB add to local database ..");
//		listAdapter.clear();
//		LV_scan_waybill.setAdapter(listAdapter);
	}
	
	public void show_dialog1() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanPOD.this);
			builder.setTitle("No.Waybill Berhasil Di POD");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }
	 
	 public void show_dialog2() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanPOD.this);
			builder.setTitle("No.Waybill Berhasil Di POD Sebagian");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }
	 
	 public void show_dialog3() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanPOD.this);
			builder.setTitle("No.Waybill Gagal Di POD, Mohon Ulangi Kembali");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }
	 
	 public void show_dialog4() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanPOD.this);
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
		 AlertDialog.Builder builder = new AlertDialog.Builder(multiScanPOD.this);
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
		IV_foto.setImageResource(R.drawable.no_image);
		STR_foto = "";
		ET_waybill.setText("");
		ET_penerima.setText("");
		ET_telp.setText("");
		ET_keterangan.setText("");
		ET_validasi.setVisibility(View.GONE);
		ET_validasi2.setVisibility(View.VISIBLE);
		BTN_submit.setEnabled(false);
		ET_validasi.setBackgroundColor(Color.parseColor("#ffffff"));
		ET_validasi2.setBackgroundColor(Color.parseColor("#c2c2c2"));
	}
	
	public void main_menu(View theButton) {
		finish();
		// Intent a = new Intent (cekpod.this,main_menu.class);
		// startActivity(a);
	}

//	public void ResizeImage() {
//		if (Photo == null) {
//			showError("Please choose an image!");
//		} else {
//			// Compress image in main thread using custom Compressor
//			try {
//
//				compressedImageBitmap = Photo;
//				float aspectRatio = compressedImageBitmap.getWidth() /
//						(float) compressedImageBitmap.getHeight();
//				if(check_size == 0){
//					ByteArrayOutputStream stream = new ByteArrayOutputStream();
//					compressedImageBitmap = Bitmap.createScaledBitmap(
//							compressedImageBitmap, widthPict, heightPict, false);
//					compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, qualityPict, stream);
//					imageInByte = stream.toByteArray();
//					setCompressedImage();
//
//					Log.d("Debug", String.format("Size Image Compress If : %s", getReadableFileSize(imageInByte.length)));
//					String image_encode = "data:image/png;base64," + Base64.encodeBytes(imageInByte);
//					String base64Image = image_encode.split(",")[1];
////					String STR_foto = base64Image;
////					Log.d("Debug", "Hasil Encode Re-Image = " + STR_foto);
//
//					int widthcompressedImageBitmap = compressedImageBitmap.getWidth();
//					int heightcompressedImageBitmap = compressedImageBitmap.getHeight();
//					Log.d("Debug", "Ukuran Image Compress If = " + String.valueOf(widthcompressedImageBitmap)+ " px : " + String.valueOf(heightcompressedImageBitmap) + " px");
//
////					STR_replace= STR_foto.replaceAll("[+]", ".");
////					Log.d("Debug","Replace Encode Photo = "+STR_replace);
//
//					STR_foto = base64Image;
//					Log.d("Debug","Hasil Encode Photo If = "+STR_foto);
//					STR_replace= STR_foto.replaceAll("[+]", ".");
//					Log.d("Debug","Replace Encode Re-Image Photo = "+STR_replace);
//				}else{
//					ByteArrayOutputStream stream = new ByteArrayOutputStream();
//					int heightless = Math.round(widthPict / aspectRatio);
//					compressedImageBitmap = Bitmap.createScaledBitmap(
//							compressedImageBitmap, widthPict, heightless, false);
//					compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, qualityPict, stream);
//					imageInByte = stream.toByteArray();
//					setCompressedImage();
//
//					Log.d("Debug", String.format("Size Image Compress Else : %s", getReadableFileSize(imageInByte.length)));
//					String image_encode = "data:image/png;base64," + Base64.encodeBytes(imageInByte);
//					String base64Image = image_encode.split(",")[1];
////					String STR_foto = base64Image;
////					Log.d("Debug", "Hasil Encode Re-Image = " + STR_foto);
//
//					int widthcompressedImageBitmap = compressedImageBitmap.getWidth();
//					int heightcompressedImageBitmap = compressedImageBitmap.getHeight();
//					Log.d("Debug", "Ukuran Image Compress Else = " + String.valueOf(widthcompressedImageBitmap)+ " px : " + String.valueOf(heightcompressedImageBitmap) + " px");
//
////					STR_replace= STR_foto.replaceAll("[+]", ".");
////					Log.d("Debug","Replace Encode Photo = "+STR_replace);
//
//					STR_foto = base64Image;
//					Log.d("Debug","Hasil Encode Photo Else = "+STR_foto);
//					STR_replace= STR_foto.replaceAll("[+]", ".");
//					Log.d("Debug","Replace Encode Re-Image Photo = "+STR_replace);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				showError(e.getMessage());
//			}
//		}
//	}

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

//					STR_FileNamePhoto = STR_Date+"_"+STR_Time;
					STR_FileNamePhoto = "POD_" + STR_Date+"_"+STR_Time + ".jpg";
//					File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Image Compact");
					File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Image Compact");
//					File file = new File(mediaStorageDir, "POD_" + STR_FileNamePhoto + ".jpg");
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

//				ByteArrayOutputStream stream = new ByteArrayOutputStream();
//				int heightless = Math.round(widthPict / aspectRatio);
//				compressedImageBitmap = Bitmap.createScaledBitmap(
//						compressedImageBitmap, widthPict, heightless, false);
//				compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, qualityPict, stream);
//				imageInByte = stream.toByteArray();
//				setCompressedImage();
//
//				Log.d("Debug", String.format("Size Image Compress Else : %s", getReadableFileSize(imageInByte.length)));
//				String image_encode = "data:image/png;base64," + Base64.encodeBytes(imageInByte);
//				String base64Image = image_encode.split(",")[1];
////					String STR_foto = base64Image;
////					Log.d("Debug", "Hasil Encode Re-Image = " + STR_foto);
//
//				int widthcompressedImageBitmap = compressedImageBitmap.getWidth();
//				int heightcompressedImageBitmap = compressedImageBitmap.getHeight();
//				Log.d("Debug", "Ukuran Image Compress Else = " + String.valueOf(widthcompressedImageBitmap)+ " px : " + String.valueOf(heightcompressedImageBitmap) + " px");
//
////					STR_replace= STR_foto.replaceAll("[+]", ".");
////					Log.d("Debug","Replace Encode Photo = "+STR_replace);
//
//				STR_foto = base64Image;
//				Log.d("Debug","Hasil Encode Photo Else = "+STR_foto);
//				STR_replace= STR_foto.replaceAll("[+]", ".");
//				Log.d("Debug","Replace Encode Re-Image Photo = "+STR_replace);

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

        new Handler().postDelayed(
				new Runnable() {
					@Override
					public void run() {
						try {
							photoFile = createImageFile(false);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				},200
		);
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

    public File createImageFile(boolean first) throws IOException {
        // Create a JPG image file name
        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = new File(pictureFolder, "Compact");

        boolean dirExists = true;

        if (!storageDir.exists()) {
            dirExists = storageDir.mkdirs();
        }

        if (first){
            STR_waybill = ET_waybill.getText().toString();
        }

        if (dirExists) {
            String fileName = "POD_" + STR_waybill + "_" + STR_Date + "_" + STR_Time + "-";
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
