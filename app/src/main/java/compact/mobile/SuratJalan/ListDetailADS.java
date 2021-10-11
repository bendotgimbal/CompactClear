package compact.mobile.SuratJalan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

import compact.mobile.DBAdapter;
import compact.mobile.JSONParser;
import compact.mobile.SessionManager;
import compact.mobile.gps_tracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import compact.mobile.R;
import compact.mobile.SuratJalan.DB.C_Pincode_ADS;
import compact.mobile.SuratJalan.DB.ListFinishPincodeADSDBAdapter;
import compact.mobile.SuratJalan.Scan.multiScanADS;
import compact.mobile.SuratJalan.helper.ListDetailADSAdapter;
import compact.mobile.SuratJalan.helper.ListDetailADSAdapter2;
import compact.mobile.SuratJalan.helper.ListDetailADSAdapter3;
import compact.mobile.SuratJalan.helper.ListDetailCheckBoxADSAdapter;
import compact.mobile.SuratJalan.helper.ListDetailCheckBoxADSAdapter2;
import compact.mobile.config.AWBList;
import compact.mobile.config.Koneksi;
import compact.mobile.config.sharedpref;

public class ListDetailADS extends Activity {
	//09-08-2017
	Button BTN_keluar, BTN_refresh, BTN_scan, BTN_finish, BTN_finish_tester, BTN_finish_testing;
	public Button OnNextScan;
	TextView TV_status_assigment, TV_response_code;
	EditText ET_pincode;
	
	EditText ET_ListAWB;
	ListView LV_ListAWB;
	
	private CheckBox CB_NoAWB;

	CustomAWBListAdapter dataAdapter = null;
	
	private  ProgressDialog progressBar;
	gps_tracker gps;
	
	private static final String AR_NO_ASSIGMENT = "asigment";
	private static final String AR_NO_AWB = "no_awb";
	private static final String AR_NAMA_TOKO = "nama_toko";
	private static final String AR_STATUS = "status";
	private static final String AR_KODE_TOKO = "kode_toko";
	private static final String AR_ALAMAT_TOKO = "alamat";
	private static final String AR_SERVICE = "service";
	private static final String AR_TYPE = "type";
	private static final String AR_CHECKBOX = "checkbox";
	
	String STR_urlphp, STR_URL_API, STR_url_ads_detail, STR_url_finish_pincode_finish_ads, STR_url_finish_pincode_finish_ads_Pincode;
	String Str_sp_url_assigment, Str_sp_url_scan_finish_PO;
	String STR_assigment, STR_no_awb, STR_nama_toko, STR_status_assigment, STR_status, STR_pincode, STR_lat_long;
	String STR_no_assigment, STR_kode_toko, STR_alamat, STR_service, STR_type, STR_NamaToko, STR_passcode_toko;
	String STR_data;
	String STR_date_time;
	String RespnseMessage, RespnseCode, RespnseMessagePincodePostADS, RespnseCodePincodePostADS, RespnseMessagePincodePostADS2, RespnseCodePincodePostADS2, RespnseCodeListDetailADS, RespnseMessageListDetailADS, STR_response_code;
	public String Str_lo_Koneksi, Str_LinkDetailListADS, Str_LinkScanListADS, Str_LinkScanListADSPincode;
    int intLengthPasscode, intLengthPasscodeET;
    String strPasscode, strPasscodeET;
	
	public String STR_swaybill="";
//	String STR_swaybill;
	
	ListDetailADSAdapter listItemAdapter;
	ListDetailADSAdapter2 listItemAdapter2;
	ListDetailADSAdapter3 adapterDetailADS2;
	ListDetailCheckBoxADSAdapter listItemAdapter3;
	ListDetailCheckBoxADSAdapter2 listItemAdapterAWBList;
	SparseBooleanArray mChecked = new SparseBooleanArray();
	
	public String locpk ,username;
	
	final ArrayList<HashMap<String, String>> daftar_list_ads_detail = new ArrayList<HashMap<String, String>>();
	final ArrayList<HashMap<String, String>> daftar_list_ads_detail2 = new ArrayList<HashMap<String, String>>();
	final ArrayList<HashMap<String, String>> daftar_list_ads_CheckBox = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> daftar_validation_waybill_list = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, Object>> listDataAWB =new ArrayList<HashMap<String,Object>>();
	ArrayList<HashMap<String, String>> list_detail_ads = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> mapADSDetail = new HashMap<String, String>();
	HashMap<String, String> mapADSDetail2 = new HashMap<String, String>();
	HashMap<String, String> mapADSCheckBox = new HashMap<String, String>();
	
	SessionManager session;
    FrameLayout fl_progress;
	
	SharedPreferences myPrefs;
	SharedPreferences.Editor spEditor;
    SharedPreferences sp;
    
//  private ListPincodeADSDBAdapter pincode_ads;
	private ListFinishPincodeADSDBAdapter finish_pincode_ads;
//	ListPincodeADSDBAdapter db;
	ListFinishPincodeADSDBAdapter dbFinish;
	
	private ArrayAdapter<String> listAdapter ;
	private ArrayAdapter<String> listAdapter_ads_detail ;
	ListView mainListView;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ads_detail);
        
        Koneksi Str_lo_Koneksi = new Koneksi();
        Str_LinkDetailListADS = Str_lo_Koneksi.ConnADSDetail();
        Str_LinkScanListADS = Str_lo_Koneksi.ConnADSFinish();
        Str_LinkScanListADSPincode = Str_lo_Koneksi.ConnADSFinishPincode();
        
        myPrefs = getSharedPreferences("sharedpref_assignment",MODE_PRIVATE); 
//		this.context = context;
		spEditor = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE).edit();
        sp = getApplicationContext().getSharedPreferences(sharedpref.SP_PREFS_NAME, Context.MODE_PRIVATE);
        spEditor = myPrefs.edit();
        
        Time now = new Time(Time.getCurrentTimezone());
		now.setToNow();
		String sTgl = Integer.toString(now.year)
				+ "-"
				+ ("00" + Integer.toString(now.month + 1))
						.substring(("00" + Integer.toString(now.month + 1))
								.length() - 2)
				+ "-"
				+ ("00" + Integer.toString(now.monthDay))
						.substring(("00" + Integer.toString(now.monthDay))
								.length() - 2)
				+ " "
				+ ("00" + Integer.toString(now.hour)).substring(("00" + Integer
						.toString(now.hour)).length() - 2)
				+ ":"
				+ ("00" + Integer.toString(now.minute))
						.substring(("00" + Integer.toString(now.minute))
								.length() - 2)
				+ ":"
				+ ("00" + Integer.toString(now.second))
						.substring(("00" + Integer.toString(now.second))
								.length() - 2);

		STR_date_time = sTgl;
		Log.d("Debug", "Cek Tanggal " + STR_date_time);
		
//		TextView TV_pincode = (TextView) findViewById(R.id.jdl_pincode);
//		TV_pincode.setText("Kode");
//		TV_pincode.append(System.getProperty("line.separator"));
//		TV_pincode.append("Alfatrex");
		
//		String STR_lineSeparator_TextView = "Kode" + System.getProperty("line.separator") + "\n Alfatrex";
//		TV_pincode.setText(STR_lineSeparator_TextView);
//		String STR_lineSeparator_TextView = "Kode <br/> Alfatrex";
//		TV_pincode.setText(Html.fromHtml(STR_lineSeparator_TextView));
		
		ET_ListAWB = (EditText) findViewById(R.id.add_ET_awb_list);
		LV_ListAWB = (ListView) findViewById( R.id.add_LV_awb_list );

		ArrayList<String> wbList = new ArrayList<String>();
		listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, wbList);
        
        mainListView = (ListView) findViewById( R.id.mainListView );
        
        TV_status_assigment=(TextView)findViewById(R.id.txt_status_assigment);
        ET_pincode = (EditText) findViewById(R.id.pincode);
        
        BTN_scan=(Button)findViewById(R.id.btnScan);
        BTN_keluar=(Button)findViewById(R.id.btnKeluar);
        BTN_refresh=(Button)findViewById(R.id.btnRefresh);
        BTN_finish=(Button)findViewById(R.id.btnFinish);
        BTN_finish_tester=(Button)findViewById(R.id.btnFinishTester);
        BTN_finish_testing=(Button)findViewById(R.id.btnFinishTesting);
//        BTN_finish.setEnabled(false);
        fl_progress = (FrameLayout) findViewById(R.id.fl_progress);
        
        BTN_refresh.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				Intent a = new Intent(ListDetailADS.this, ListDetailADS.class);
				startActivity(a);

			}
		});
	 
		BTN_keluar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		BTN_scan.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String STR_assigment_in_button = STR_no_assigment;
				String STR_awb_in_button = STR_no_awb;
				String STR_status_in_button = STR_status;
				String STR_status_assigment_in_button = STR_status_assigment;
				Log.d("Debug","Isi Di Button Scan"
				+" No.Assigment = "+STR_assigment
				+" No.AWB = "+STR_awb_in_button
				+" Status = "+STR_status_in_button
				+" Status Assigment = "+STR_status_assigment_in_button);
				
				ArrayList<HashMap<String, String>> daftar_list_ads_detail = new ArrayList<HashMap<String, String>>();
				ArrayList<HashMap<String, String>> list_detail_ads = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> mapADSDetail = new HashMap<String, String>();
				Log.e("Debug", "Hashmap >>>" 
						+" Hashmap 1 "+ daftar_list_ads_detail
						+" Hashmap 2 "+ list_detail_ads
						+" Hashmap 3 "+ mapADSDetail);
				
////				String status = STR_status_assigment_in_button;
//				if (STR_status_assigment_in_button.equals("0")) {
//					Log.i("Debug", "Status Open");
////					show_dialog1();
					Intent a = new Intent(ListDetailADS.this, multiScanADS.class);
					a.putExtra("asigment", STR_no_assigment);
					a.putExtra("po", STR_assigment);
					a.putExtra("awb", STR_no_awb);
					a.putExtra("kode_toko", STR_kode_toko);
					a.putExtra("array_list_detail_ads", daftar_list_ads_detail);
					a.putExtra("array_list_detail_ads_2", list_detail_ads);
					a.putExtra("array_list_detail_ads_3", mapADSDetail);
                a.putExtra("daftar_validation_waybill_list", daftar_validation_waybill_list);
					
					Bundle args = new Bundle();
					 args.putSerializable("array_list_detail_ads_4",(Serializable)list_detail_ads);
					 a.putExtra("BUNDLE",args);
					 startActivityForResult(a, 1);
					
//					Log.i("Debug", "All Intent" + a);
////					startActivityForResult(a, 1);
////					startActivityForResult(a, 500);
//					startActivity(a);
//				} else if (STR_status_assigment_in_button.equals("1")) {
//					Log.i("Debug", "Status Closed");
////					show_dialog2();
//				}
				
////				Toast.makeText(getApplicationContext(),STR_assigment+ " Selected", Toast.LENGTH_LONG).show();
//				Intent a = new Intent(ListDetailADS.this, multiScanADS.class);
//				a.putExtra("asigment", STR_no_assigment);
//				a.putExtra("po", STR_assigment);
//				a.putExtra("awb", STR_no_awb);
//				startActivityForResult(a, 1);
////				finish();
			}
		});
		
		BTN_finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressBar = new ProgressDialog(ListDetailADS.this);
                progressBar.setCancelable(false);
                progressBar.setMessage("Loading...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);;
                progressBar.show();
                
                ET_pincode = (EditText) findViewById(R.id.pincode);
                STR_pincode = ET_pincode.getText().toString();
                Log.d("Debug","Cek Isi >>> "+ " Pincode = "+STR_pincode);
                
                if (ET_pincode.getText().toString().trim().length() == 0) {
	       			Toast.makeText(v.getContext(), "Pincode tidak boleh kosong.", Toast.LENGTH_SHORT).show();
	       		}
//                else if (LV_ListAWB.getAdapter().getCount() <= 0) {
//                	Toast.makeText(v.getContext(), "ListView tidak boleh kosong. = " + LV_ListAWB.getAdapter().getCount(), Toast.LENGTH_SHORT).show();
//	       		}
                else {
//                	update_data_finish_ads_with_pincode();
                	update_data_finish_ads_with_pincode2();
                }
                progressBar.dismiss();
            }
            
        });
		
//		ListView list = (ListView) findViewById(R.id.listviews_ads_detail);
//		listItemAdapter3 = new ListDetailCheckBoxADSAdapter(this, listDataAWB);
//		        list.setAdapter(listItemAdapter3);
		        BTN_finish_tester.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		                progressBar = new ProgressDialog(ListDetailADS.this);
		                progressBar.setCancelable(false);
		                progressBar.setMessage("Loading...");
		                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);;
		                progressBar.show();
		                
		                HashMap<Integer, Boolean> state =listItemAdapter3.state;
		                String options="List:";     
		                for(int j=0;j<listItemAdapter3.getCount();j++){
		                    System.out.println("state.get("+j+")=="+state.get(j));
		                    if(state.get(j)!=null){
		                        @SuppressWarnings("unchecked")
		                        HashMap<String, Object> map=(HashMap<String, Object>) listItemAdapter3.getItem(j);
		                        String username=map.get("friend_username").toString();  
		                        String id=map.get("friend_id").toString();  
		                        options+="\n"+id+"."+username;
		                    }               
		                }
		                Toast.makeText(getApplicationContext(), options, Toast.LENGTH_LONG).show();
		                progressBar.dismiss();
		            }
		        });
		        
		        ListView list2 = (ListView) findViewById(R.id.listviews_ads_detail);
		        listItemAdapterAWBList= new ListDetailCheckBoxADSAdapter2(this, listDataAWB);
		        list2.setAdapter(listItemAdapterAWBList);
		        BTN_finish_testing.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		                progressBar = new ProgressDialog(ListDetailADS.this);
		                progressBar.setCancelable(false);
		                progressBar.setMessage("Loading...");
		                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);;
		                progressBar.show();
		                
		                ET_pincode = (EditText) findViewById(R.id.pincode);
		                STR_pincode = ET_pincode.getText().toString();
		                Log.d("Debug","Cek Isi >>> "+ " Pincode = "+STR_pincode);
		                
		                if (ET_pincode.getText().toString().trim().length() == 0) {
			       			Toast.makeText(v.getContext(), "Pincode tidak boleh kosong.", Toast.LENGTH_SHORT).show();
			       		}
		                else {

                            intLengthPasscodeET = ET_pincode.getText().toString().length();
                            strPasscodeET = ET_pincode.getText().toString().trim().toLowerCase();
                            Log.d("Debug","Length Passcode Edittext " + intLengthPasscodeET);
                            Log.d("Debug","Length Passcode Intent " + intLengthPasscode);
                            Log.d("Debug","Isi Passcode Intent " + strPasscodeET);
//                            if(intLengthPasscode == intLengthPasscodeET ) {
//                                Log.d("Debug","Length Passcode Sama");
//                                if(strPasscodeET.equals(strPasscode)) {
//                                    Log.d("Debug","Passcode Valid");
                                    HashMap<Integer, Boolean> ListAWBDetail = listItemAdapterAWBList.ListAWBDetail;
                                    String options = "List:";
                                    if(ListAWBDetail.size() != 0) {
                                        for (int j = 0; j < listItemAdapterAWBList.getCount(); j++) {
                                            System.out.println("ListAWBDetail.get(" + j + ")==" + ListAWBDetail.get(j));
                                            if (ListAWBDetail.get(j) != null) {
                                                @SuppressWarnings("unchecked")
                                                HashMap<String, Object> map = (HashMap<String, Object>) listItemAdapterAWBList.getItem(j);
                                                String STR_NoAWB = map.get("AR_NO_AWB").toString();
                                                String STR_Status = map.get("AR_STATUS").toString();
                                                String STR_Type = map.get("AR_TYPE").toString();
                                                options += "\n" + STR_NoAWB;

                                                //add ListView
                                                String STR_AWBList = map.get("AR_NO_AWB").toString();
                                                String STR_TYPEList = map.get("AR_TYPE").toString();
                                                Log.d("Debug", "AWB " + STR_AWBList);

                                                ET_ListAWB.setText(STR_AWBList + "-" + STR_TYPEList);
                                                String STR_GetTextAWBList = ET_ListAWB.getText().toString();
                                                Log.d("Debug", "AWB 2 " + STR_GetTextAWBList);

                                                if (ET_ListAWB.getText().toString().trim().length() == 0) {
                                                    //Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
                                                    ET_ListAWB.requestFocus();
                                                } else {
                                                    add_list2();
//				            		add_list3();
//				            		Log.d("Debug","Lewat Simpan Di ListView");
                                                    Log.d("Debug", "Lewat Button Finish");
                                                }
                                            }
                                        }
                                        Toast.makeText(getApplicationContext(), options, Toast.LENGTH_LONG).show();
                                        Log.w("DEBUG", "!!! WAYBILL LAGI " + STR_swaybill);
                                        update_data_finish_ads_with_pincode2();
//		                progressBar.dismiss();
                                    }else{
                                        Log.d("Debug","Tidak Ada Waybill Yang Di Pilih");
                                        Toast.makeText(v.getContext(), "Mohon Cek Waybill Yang Akan Di Proses.", Toast.LENGTH_SHORT).show();
                                    }
//                                }else{
//                                    Log.d("Debug","Passcode Tidak Valid");
//                                    Toast.makeText(v.getContext(), "Passcode Salah.", Toast.LENGTH_SHORT).show();
//                                }
//                            }else{
//                                Log.d("Debug","Length Passcode Tidak Sama");
//                                Toast.makeText(v.getContext(), "Passcode Tidak Sesuai.", Toast.LENGTH_SHORT).show();
//                            }
		                }
		                Log.d("Debug","Dismiss");
		                progressBar.dismiss();
		            }
		        });
		        
		        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		            	// TODO Auto-generated method stub
//		            	show_dialog_detail();
		            }
		        });
		
		Intent in = getIntent();
     	////No Assigment
        STR_no_assigment = in.getStringExtra("AR_NO_ASSIGMENT");
        STR_kode_toko = in.getStringExtra("AR_KODE_TOKO");
        STR_status_assigment = in.getStringExtra("AR_STATUS");
        STR_NamaToko = in.getStringExtra("AR_NAMA_TOKO");
        STR_passcode_toko = in.getStringExtra("AR_NO_PASCODE");
        Log.d("Debug", "Halaman ListDetailADS " +"Intent From List ADS >>>" 
        + " Nomor Asigment " + STR_no_assigment
        + " Kode Toko " + STR_kode_toko
        + " Nama Toko " + STR_NamaToko
        + " Status Assigment " + STR_status_assigment
        + " Passcode Toko " + STR_passcode_toko);
        intLengthPasscode = STR_passcode_toko.length();
        strPasscode = STR_passcode_toko;
        Log.d("Debug","Length Passcode " + intLengthPasscode);
        Log.d("Debug","Isi Passcode " + strPasscode);
        
        TV_status_assigment=(TextView)findViewById(R.id.txt_status_assigment);
        TV_status_assigment.setText(STR_status_assigment);
        String STR_assigment_status = TV_status_assigment.getText().toString();
        Log.d("Debug","String Status Assigment " + STR_assigment_status);
        
        Str_sp_url_assigment = (myPrefs.getString("sp_url_assigment", ""));
        Log.d("Debug", "URL Shared Preferences"+" || URL Assigment = " + Str_sp_url_assigment);
        
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        STR_urlphp = user.get(SessionManager.KEY_URL);
        username = user.get(SessionManager.KEY_USER);
		locpk = user.get(SessionManager.KEY_LOCPK);
        Log.e("Debug", "Halaman ListDetailADS " +"ISI >>> " + "URL = " + STR_urlphp);
        
//        if (STR_urlphp.equals("http://43.252.144.14:81/android/")) {
//	    	Log.i("Debug", "Halaman ListADS " +"Replace URL Lokal Server");
//	    	STR_URL_API = STR_urlphp.replace(STR_urlphp, "http://43.252.144.14:81/compact_mobile");
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
//		Log.i("Debug", "Halaman ListDetailADS " +"Test String Replace URL " + STR_URL_API);
		
////		STR_url_ads_detail = STR_URL_API + "/" +"ads"+"/"+STR_kode_toko+"/"+ STR_no_assigment;
////		STR_url_ads_detail = Str_sp_url_assigment + "/" +"ads"+"/"+STR_kode_toko+"/"+ STR_no_assigment;
////      STR_url_ads_detail = STR_urlphp + Str_LinkDetailListADS +STR_kode_toko+"/"+ STR_no_assigment;
//        STR_url_ads_detail = STR_urlphp + Str_LinkDetailListADS +STR_kode_toko+"/"+ STR_no_assigment+"/"+ STR_passcode_toko;		//20 Maret 2018
//		Log.d("Debug","Halaman ListDetailADS " +"Test URL " + STR_url_ads_detail);
//
////		String finish_pincode = Str_sp_url_scan_finish_PO;
////		Log.d("Debug","1. FINISH PINCODE " +"Test URL " + finish_pincode);
////
////		String finish_pincode2 = Str_sp_url_assigment;
////		Log.d("Debug","2. FINISH PINCODE " +"Test URL " + finish_pincode2);
////
////		STR_url_finish_pincode_finish_ads = Str_sp_url_assigment + "/" + "closing_po";
//		STR_url_finish_pincode_finish_ads = STR_urlphp + Str_LinkScanListADS;
//		Log.d("Debug","!!! URL FINISH PINCODE" +" ==== " + STR_url_finish_pincode_finish_ads);
//
//		STR_url_finish_pincode_finish_ads_Pincode = STR_urlphp + Str_LinkScanListADSPincode;
//		Log.d("Debug","!!! URL FINISH ADS WITH PINCODE" +" ++++ " + STR_url_finish_pincode_finish_ads_Pincode);
//
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpGet request = new HttpGet(STR_url_ads_detail);
//        Log.d("Debug","Halaman ListDetailADS " +"Test URL Assigment " + request);
//        try {
//        	HttpResponse response = client.execute(request);
//			HttpEntity entity = response.getEntity();
//			STR_data = EntityUtils.toString(entity);
//			System.out.println(response.getStatusLine());
//			Log.e("Debug", STR_data);
//			daftar_list_ads_detail.clear();
//			HashMap<String, String> mapADSDetail;
//			HashMap<String, String> mapADSDetail2;
//			try {
//				JSONObject jsonObject = new JSONObject(STR_data);
//				JSONArray ListObjADSDetail = jsonObject.getJSONArray("data");
//				RespnseMessage = jsonObject.getString("response_message");
//				RespnseCode = jsonObject.getString("response_code");
//				Log.e("Debug", "Response API " + RespnseCode + " " +RespnseMessage);
//				for (int i = 0; i < ListObjADSDetail.length(); i++) {
//					JSONObject obj = ListObjADSDetail.getJSONObject(i);
//					STR_assigment = obj.getString("asigment");
//					STR_no_awb = obj.getString("no_awb");
//					STR_nama_toko = obj.getString("nama_toko");
//					STR_status = obj.getString("status");
//					STR_alamat = obj.getString("alamat");
//					STR_type = obj.getString("type");
//					STR_service = obj.getString("service");
//
//					String CheckboxIsi = "n";
//
//					mapADSDetail = new HashMap<String, String>();
//					mapADSDetail.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
//					mapADSDetail.put(AR_NO_AWB, obj.getString("no_awb"));
//					mapADSDetail.put(AR_NAMA_TOKO, obj.getString("nama_toko"));
//					mapADSDetail.put(AR_STATUS, obj.getString("status"));
//					mapADSDetail.put(AR_ALAMAT_TOKO, obj.getString("alamat"));
//					mapADSDetail.put(AR_SERVICE, obj.getString("service"));
//					mapADSDetail.put(AR_TYPE, obj.getString("type"));
////					mapADSDetail.put(AR_CHECKBOX, obj.getString("checkbox"));
//					mapADSDetail.put(AR_CHECKBOX, CheckboxIsi);
//					daftar_list_ads_detail.add(mapADSDetail);
//					list_detail_ads.add(mapADSDetail);
//					Log.d("Debug", "Hashmap ADS " + daftar_list_ads_detail);
//					Log.d("Debug", "Hashmap 2 ADS " + list_detail_ads);
//
//					mapADSDetail2 = new HashMap<String, String>();
//					mapADSDetail2.put("AR_NO_ASSIGMENT", obj.getString("asigment"));
//					mapADSDetail2.put("AR_NO_AWB", obj.getString("no_awb"));
//					mapADSDetail2.put("AR_NAMA_TOKO", obj.getString("nama_toko"));
//					mapADSDetail2.put("AR_STATUS", obj.getString("status"));
//					mapADSDetail2.put("AR_ALAMAT_TOKO", obj.getString("alamat"));
//					mapADSDetail2.put("AR_SERVICE", obj.getString("service"));
//					mapADSDetail2.put("AR_TYPE", obj.getString("type"));
//					mapADSDetail2.put("AR_CHECKBOX", CheckboxIsi);
//					daftar_list_ads_detail2.add(mapADSDetail2);
//					Log.d("Debug", "!!! New Hashmap ADS " + daftar_list_ads_detail2);
//
//					ArrayList<AWBList> Store_AWBList = new ArrayList<AWBList>();
//					AWBList _AWBList = new AWBList(AR_NO_ASSIGMENT,obj.getString("asigment"),false);
//							_AWBList = new AWBList(AR_NO_AWB,obj.getString("no_awb"),false);
//							_AWBList = new AWBList(AR_NAMA_TOKO,obj.getString("nama_toko"),false);
//							_AWBList = new AWBList(AR_STATUS,obj.getString("status"),false);
//							_AWBList = new AWBList(AR_ALAMAT_TOKO,obj.getString("alamat"),false);
//							_AWBList = new AWBList(AR_SERVICE,obj.getString("service"),false);
//							_AWBList = new AWBList(AR_TYPE,obj.getString("type"),false);
//					Store_AWBList.add(_AWBList);
//
////					for(int i1=0;i1<2;i1++){
//			    		HashMap<String, Object> mapAWB=new HashMap<String, Object>();
////			    		mapAWB.put("friend_image", R.drawable.icon);
//			    		mapAWB.put("AR_NO_ASSIGMENT", obj.getString("asigment"));
//			    		mapAWB.put("AR_NO_AWB", obj.getString("no_awb"));
//			    		mapAWB.put("AR_NAMA_TOKO", obj.getString("nama_toko"));
//			    		mapAWB.put("AR_STATUS", obj.getString("status"));
//			    		mapAWB.put("AR_ALAMAT_TOKO", obj.getString("alamat"));
//			    		mapAWB.put("AR_SERVICE", obj.getString("service"));
//			    		mapAWB.put("AR_TYPE", obj.getString("type"));
//			    		mapAWB.put("selected", false);
//			    		listDataAWB.add(mapAWB);
////			    	}
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
//	}
////        this.adapter_listview_ads_detail();
////        this.adapter_listview_ads_detail2();
////        this.adapter_listview_ads_detail3();
//        adapter_listview_ads_Firstupdate();

		//Update 02-10-2018
		JSONParseListDetailADS doItInBackGround = new JSONParseListDetailADS();
		doItInBackGround.execute();
	}

	//Update 02-10-2018
	private class JSONParseListDetailADS extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("Debug", "Lewat PreExecute");
			STR_url_ads_detail = STR_urlphp + Str_LinkDetailListADS +STR_kode_toko+"/"+ STR_no_assigment+"/"+ STR_passcode_toko;		//20 Maret 2018
			Log.d("Debug","Halaman ListDetailADS " +"Test URL " + STR_url_ads_detail);
			STR_url_finish_pincode_finish_ads = STR_urlphp + Str_LinkScanListADS;
            fl_progress.setVisibility(View.VISIBLE);

		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			STR_url_finish_pincode_finish_ads = STR_urlphp + Str_LinkScanListADS;
			Log.d("Debug","!!! URL FINISH PINCODE" +" ==== " + STR_url_finish_pincode_finish_ads);

			STR_url_finish_pincode_finish_ads_Pincode = STR_urlphp + Str_LinkScanListADSPincode;
			Log.d("Debug","!!! URL FINISH ADS WITH PINCODE" +" ++++ " + STR_url_finish_pincode_finish_ads_Pincode);

			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(STR_url_ads_detail);
			Log.d("Debug","Halaman ListDetailADS " +"Test URL Assigment " + request);

			try {
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				STR_data = EntityUtils.toString(entity);
				System.out.println(response.getStatusLine());
				Log.e("Debug", STR_data);
				daftar_list_ads_detail.clear();
                daftar_validation_waybill_list.clear();
				HashMap<String, String> mapADSDetail;
				HashMap<String, String> mapADSDetail2;
                HashMap<String, String> mapValidationWaybill;
				try {
					JSONObject jsonObject = new JSONObject(STR_data);
					JSONArray ListObjADSDetail = jsonObject.getJSONArray("data");
					RespnseMessageListDetailADS = jsonObject.getString("response_message");
					RespnseCodeListDetailADS = jsonObject.getString("response_code");
					Log.e("Debug", "Response API " + RespnseCodeListDetailADS + " " +RespnseMessageListDetailADS);
					for (int i = 0; i < ListObjADSDetail.length(); i++) {
						JSONObject obj = ListObjADSDetail.getJSONObject(i);
						STR_assigment = obj.getString("asigment");
						STR_no_awb = obj.getString("no_awb");
						STR_nama_toko = obj.getString("nama_toko");
						STR_status = obj.getString("status");
						STR_alamat = obj.getString("alamat");
						STR_type = obj.getString("type");
						STR_service = obj.getString("service");

						String CheckboxIsi = "n";

						mapADSDetail = new HashMap<String, String>();
						mapADSDetail.put(AR_NO_ASSIGMENT, obj.getString("asigment"));
						mapADSDetail.put(AR_NO_AWB, obj.getString("no_awb"));
						mapADSDetail.put(AR_NAMA_TOKO, obj.getString("nama_toko"));
						mapADSDetail.put(AR_STATUS, obj.getString("status"));
						mapADSDetail.put(AR_ALAMAT_TOKO, obj.getString("alamat"));
						mapADSDetail.put(AR_SERVICE, obj.getString("service"));
						mapADSDetail.put(AR_TYPE, obj.getString("type"));
						mapADSDetail.put(AR_CHECKBOX, CheckboxIsi);
						daftar_list_ads_detail.add(mapADSDetail);
						list_detail_ads.add(mapADSDetail);
						Log.d("Debug", "Hashmap ADS " + daftar_list_ads_detail);
						Log.d("Debug", "Hashmap 2 ADS " + list_detail_ads);

						mapADSDetail2 = new HashMap<String, String>();
						mapADSDetail2.put("AR_NO_ASSIGMENT", obj.getString("asigment"));
						mapADSDetail2.put("AR_NO_AWB", obj.getString("no_awb"));
						mapADSDetail2.put("AR_NAMA_TOKO", obj.getString("nama_toko"));
						mapADSDetail2.put("AR_STATUS", obj.getString("status"));
						mapADSDetail2.put("AR_ALAMAT_TOKO", obj.getString("alamat"));
						mapADSDetail2.put("AR_SERVICE", obj.getString("service"));
						mapADSDetail2.put("AR_TYPE", obj.getString("type"));
						mapADSDetail2.put("AR_CHECKBOX", CheckboxIsi);
						daftar_list_ads_detail2.add(mapADSDetail2);
						Log.d("Debug", "!!! New Hashmap ADS " + daftar_list_ads_detail2);

						ArrayList<AWBList> Store_AWBList = new ArrayList<AWBList>();
						AWBList _AWBList = new AWBList(AR_NO_ASSIGMENT,obj.getString("asigment"),false);
						_AWBList = new AWBList(AR_NO_AWB,obj.getString("no_awb"),false);
						_AWBList = new AWBList(AR_NAMA_TOKO,obj.getString("nama_toko"),false);
						_AWBList = new AWBList(AR_STATUS,obj.getString("status"),false);
						_AWBList = new AWBList(AR_ALAMAT_TOKO,obj.getString("alamat"),false);
						_AWBList = new AWBList(AR_SERVICE,obj.getString("service"),false);
						_AWBList = new AWBList(AR_TYPE,obj.getString("type"),false);
						Store_AWBList.add(_AWBList);

						HashMap<String, Object> mapAWB=new HashMap<String, Object>();
						mapAWB.put("AR_NO_ASSIGMENT", obj.getString("asigment"));
						mapAWB.put("AR_NO_AWB", obj.getString("no_awb"));
						mapAWB.put("AR_NAMA_TOKO", obj.getString("nama_toko"));
						mapAWB.put("AR_STATUS", obj.getString("status"));
						mapAWB.put("AR_ALAMAT_TOKO", obj.getString("alamat"));
						mapAWB.put("AR_SERVICE", obj.getString("service"));
						mapAWB.put("AR_TYPE", obj.getString("type"));
						mapAWB.put("selected", false);
						listDataAWB.add(mapAWB);

                        mapValidationWaybill = new HashMap<String, String>();
                        mapValidationWaybill.put(AR_NO_AWB, obj.getString("no_awb"));
                        daftar_validation_waybill_list.add(mapValidationWaybill);
                        Log.d("Debug", "Hashmap Validation Waybill " + daftar_validation_waybill_list);
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
			adapter_listview_ads_Firstupdate2();
			Log.d("Debug", "1.Test Sampai Sini ");
            fl_progress.setVisibility(View.GONE);
		}
	}
	
	private void adapter_listview_ads_Firstupdate() {
		// TODO Auto-generated method stub
		Log.d("Debug", "Lewat Listview");
//		this.adapter_listview_ads_detail();
//		this.adapter_listview_ads_detail2();
//		this.adapter_listview_ads_detail3();
//		this.adapter_listview_ads_detail4();
//		this.adapter_listview_ads_detail5();
//		this.adapter_listview_ads_detail6();
//		this.adapter_listview_ads_update(mapADSDetail, 0);
	}

	//Update 02-10-2018
	private void adapter_listview_ads_Firstupdate2() {
		// TODO Auto-generated method stub
		Log.d("Debug", "Lewat Listview");
		ListView list2 = (ListView) findViewById(R.id.listviews_ads_detail);
		listItemAdapterAWBList= new ListDetailCheckBoxADSAdapter2(this, listDataAWB);
		list2.setAdapter(listItemAdapterAWBList);
		list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//		            	show_dialog_detail();
			}
		});
	}
	
//	public void adapter_listview_ads_detail() {
//		Log.e("Debug", "Listview ADS Detail");
//		ListDetailADSAdapter adapterDetailADS= new ListDetailADSAdapter(this, daftar_list_ads_detail);
//		ListView listViewDetailADS = (ListView) findViewById(R.id.listviews_ads_detail);
//		listViewDetailADS.setAdapter(adapterDetailADS);
//		adapterDetailADS.notifyDataSetChanged();
//		
//		listViewDetailADS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            	// TODO Auto-generated method stub
////				Toast.makeText(getApplicationContext(),daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString()+ " Selected", Toast.LENGTH_LONG).show();
//				STR_no_assigment = daftar_list_ads_detail.get(position).get(AR_NO_ASSIGMENT).toString();
//				STR_no_awb = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//				
////				AWBList AWBlistViewDetailADS = (AWBList) parent.getItemAtPosition(position);
////			    Toast.makeText(getApplicationContext(),"Clicked on Row: " + AWBlistViewDetailADS.getName(), Toast.LENGTH_LONG).show();
//				ListView listViewDetailADS = (ListView) parent;
////			    Toast.makeText(getApplicationContext(),"Clicked on Row: " + daftar_list_ads_detail.get(position).get(AR_NO_AWB), Toast.LENGTH_LONG).show();
//			    
//				int count = listViewDetailADS.getAdapter().getCount();
//				Log.i("Debug", "Get Count = "+count);
//				String STR_Position = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//				Log.i("Debug", "Get Position = "+STR_Position);
//			    CB_NoAWB = (CheckBox)findViewById(R.id.checkbox_awb);
//			    STR_no_assigment = (CB_NoAWB.isChecked())?"y":"n";
//			    Log.i("Debug", "CheckBox AWB "+STR_no_assigment);
//			    CB_NoAWB.setText(STR_no_assigment);
//			    
//			    if (STR_no_assigment == "n") {
//                	CB_NoAWB.setChecked(true);
//                	Toast.makeText(getBaseContext(), "You checked " + STR_no_awb, Toast.LENGTH_SHORT).show();
//    			} 
//			    else{
//                	CB_NoAWB.setChecked(false);
//                	Toast.makeText(getBaseContext(), "You unchecked " + STR_no_awb, Toast.LENGTH_SHORT).show();
//    			}
//				
////				int count = listViewDetailADS.getAdapter().getCount();
////				for (int i = 0; i < count; i++) {
////					LinearLayout itemLayout = (LinearLayout)listViewDetailADS.getChildAt(i);
////					CheckBox CB_NoAWBList = (CheckBox)itemLayout.findViewById(R.id.checkbox_awb);
////					if (CB_NoAWBList.isChecked()) {
////						Log.d("Item " + String.valueOf(i), CB_NoAWBList.getTag().toString());
////						Toast.makeText(ListDetailADS.this,CB_NoAWBList.getTag().toString(),Toast.LENGTH_LONG).show();
////					}
////				}
//				
////				ListView listViewDetailADS = (ListView) parent;
////                if(listViewDetailADS.isItemChecked(position)){
////                    Toast.makeText(getBaseContext(), "You checked " + STR_no_awb, Toast.LENGTH_SHORT).show();
////                }else{
////                    Toast.makeText(getBaseContext(), "You unchecked " + STR_no_awb, Toast.LENGTH_SHORT).show();
////                }
////                
////                if (((CheckBox) view).isChecked()) {
////                	Toast.makeText(getBaseContext(), "You checked " + STR_no_awb, Toast.LENGTH_SHORT).show();
////    			} else {
////    				Toast.makeText(getBaseContext(), "You unchecked " + STR_no_awb, Toast.LENGTH_SHORT).show();
////    			}
//				
//                @SuppressWarnings("unchecked")
//                HashMap<String, String> o = (HashMap<String, String>) daftar_list_ads_detail.get(position);
////                waybill = o.get("Waybill");
//                show_dialog_detail();
////               String status = o.get("status");
//////                Log.d("tes waybill", waybill);
////                if(status.equals("0")){
//////                	show_dialog1();
//////                	show_dialog2();
////                	show_dialog_detail();
////                }else if (status.equals("1")){
//////                	show_dialog2();
////                	show_dialog_detail();
////                }
////			     Log.d("error 2", "manifes = " +waybill);
//            }
//        });
//		
//    }
	
//	public void adapter_listview_ads_detail2() {
//		Log.e("Debug", "Listview ADS Detail");
//		ListDetailADSAdapter2 adapterDetailADS= new ListDetailADSAdapter2(this, daftar_list_ads_detail);
//		ListView listViewDetailADS = (ListView) findViewById(R.id.listviews_ads_detail);
//		listViewDetailADS.setAdapter(adapterDetailADS);
//		
//		listViewDetailADS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            	// TODO Auto-generated method stub
////				Toast.makeText(getApplicationContext(),daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString()+ " Selected", Toast.LENGTH_LONG).show();
//				STR_no_assigment = daftar_list_ads_detail.get(position).get(AR_NO_ASSIGMENT).toString();
//				STR_no_awb = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//				
////				AWBList AWBlistViewDetailADS = (AWBList) parent.getItemAtPosition(position);
////			    Toast.makeText(getApplicationContext(),"Clicked on Row: " + AWBlistViewDetailADS.getName(), Toast.LENGTH_LONG).show();
//				ListView listViewDetailADS = (ListView) parent;
////			    Toast.makeText(getApplicationContext(),"Clicked on Row: " + daftar_list_ads_detail.get(position).get(AR_NO_AWB), Toast.LENGTH_LONG).show();
//			    
//				int count = listViewDetailADS.getAdapter().getCount();
//				Log.i("Debug", "Get Count = "+count);
//				String STR_Position = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//				Log.i("Debug", "Get Position = "+STR_Position);
//			    CB_NoAWB = (CheckBox)findViewById(R.id.checkbox_awb);
//			    String STR_no_assigmentCheckBox = (CB_NoAWB.isChecked())?"y":"n";
//			    Log.i("Debug", "CheckBox AWB"+STR_no_awb+" = "+STR_no_assigmentCheckBox);
//			    CB_NoAWB.setText(STR_no_assigmentCheckBox);
//			    
//			    String STR_convertChangedCheckbox = daftar_list_ads_detail.get(position).get(AR_CHECKBOX).toString();
//			    Log.d("Debug","Test Convert Changed Checkbox 1 " + STR_convertChangedCheckbox);
//			    TextView TV_ChangedCheckbox = (TextView) findViewById(R.id.status_ChangedCheckbox);
//			    TV_ChangedCheckbox.setText(STR_convertChangedCheckbox);
//			    
//			    
//			    
//			    if (STR_no_assigmentCheckBox == "n") {
////			    if (TV_ChangedCheckbox.getText().toString().equals("n")) {
//			    	
//			    	//add ListView
//				    String STR_AWBList = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//				    String STR_TYPEList = daftar_list_ads_detail.get(position).get(AR_TYPE).toString();
//				    Log.d("Debug","AWB " + STR_AWBList);
////				    TextView TV_ChangedCheckbox = (TextView) findViewById(R.id.status_ChangedCheckbox);
////				    ET_ListAWB.setText(STR_AWBList);
//				    ET_ListAWB.setText(STR_AWBList+"-"+STR_TYPEList);
//				    String STR_GetTextAWBList = ET_ListAWB.getText().toString();
//				    Log.d("Debug","AWB 2 " + STR_GetTextAWBList);
//				    
//	            	if (ET_ListAWB.getText().toString().trim().length() == 0) {
//		       			//Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
//	            		ET_ListAWB.requestFocus();
//		       		}
//	            	else {
//	            		add_list();
//	            	}
//			    	
//                	CB_NoAWB.setChecked(true);
//                	Log.i("Debug", "You checked AWB Row = "+STR_no_awb);
//                	Toast.makeText(getBaseContext(), "You checked " + STR_no_awb, Toast.LENGTH_SHORT).show();
//                	TV_ChangedCheckbox.setText("y");
//                	
//                	String AR_CNVRT_STATUS_CHECKBOX = TV_ChangedCheckbox.getText().toString();
//                	
//                	mapADSDetail = new HashMap<String, String>();
////			    	mapADSDetail.put(AR_CHECKBOX, "y");
//                	mapADSDetail.put(AR_CHECKBOX, daftar_list_ads_detail.get(position).get(AR_CNVRT_STATUS_CHECKBOX));
//			    	daftar_list_ads_detail.remove(mapADSDetail);
//					Log.i("Debug", "Get HashMap if = "+daftar_list_ads_detail);
//    			} 
//			    else{
//			    	
//			    	//add ListView
//				    String STR_AWBList = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//				    String STR_TYPEList = daftar_list_ads_detail.get(position).get(AR_TYPE).toString();
//				    Log.d("Debug","AWB " + STR_AWBList);
////				    TextView TV_ChangedCheckbox = (TextView) findViewById(R.id.status_ChangedCheckbox);
////				    ET_ListAWB.setText(STR_AWBList);
//				    ET_ListAWB.setText(STR_AWBList+"-"+STR_TYPEList);
//				    String STR_GetTextAWBList = ET_ListAWB.getText().toString();
//				    Log.d("Debug","AWB 2 " + STR_GetTextAWBList);
//				    
//	            	if (ET_ListAWB.getText().toString().trim().length() == 0) {
//		       			//Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
//	            		ET_ListAWB.requestFocus();
//		       		}
//	            	else {
//	            		add_list();
//	            	}
//			    	
//                	CB_NoAWB.setChecked(false);
//                	Log.i("Debug", "You unchecked AWB Row = "+STR_no_awb);
//                	Toast.makeText(getBaseContext(), "You unchecked " + STR_no_awb, Toast.LENGTH_SHORT).show();
//                	TV_ChangedCheckbox.setText("n");
//                	
//                	String AR_CNVRT_STATUS_CHECKBOX = TV_ChangedCheckbox.getText().toString();
//                	
//                	mapADSDetail = new HashMap<String, String>();
////			    	mapADSDetail.put(AR_CHECKBOX, "n");
//                	mapADSDetail.put(AR_CHECKBOX, daftar_list_ads_detail.get(position).get(AR_CNVRT_STATUS_CHECKBOX));
//			    	daftar_list_ads_detail.remove(mapADSDetail);
//					Log.i("Debug", "Get HashMap else = "+daftar_list_ads_detail);
//    			}
//			    
//			    Log.d("Debug","Test Changed Checkbox 2 " + TV_ChangedCheckbox);
//				String AR_CNVRT_STATUS_CHECKBOX = TV_ChangedCheckbox.getText().toString();
//				Log.d("Debug","Result Checkbox " + AR_CNVRT_STATUS_CHECKBOX);
//				
////				mapADSDetail = new HashMap<String, String>();
////				mapADSDetail.put(AR_NO_ASSIGMENT, daftar_list_ads_detail.get(position).get(AR_NO_ASSIGMENT).toString());
////				mapADSDetail.put(AR_NO_AWB, daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString());
////				mapADSDetail.put(AR_NAMA_TOKO, daftar_list_ads_detail.get(position).get(AR_NAMA_TOKO).toString());
////				mapADSDetail.put(AR_STATUS, daftar_list_ads_detail.get(position).get(AR_STATUS).toString());
////				mapADSDetail.put(AR_ALAMAT_TOKO, daftar_list_ads_detail.get(position).get(AR_ALAMAT_TOKO).toString());
////				mapADSDetail.put(AR_TYPE, daftar_list_ads_detail.get(position).get(AR_TYPE).toString());
////				mapADSDetail.put(AR_CHECKBOX, AR_CNVRT_STATUS_CHECKBOX);
////				mapADSDetail.put(AR_CHECKBOX, daftar_list_ads_detail.get(position).get(AR_CNVRT_STATUS_CHECKBOX));
////				daftar_list_ads_detail.remove(mapADSDetail);
//				
//                @SuppressWarnings("unchecked")
//                HashMap<String, String> o = (HashMap<String, String>) daftar_list_ads_detail.get(position);
////                HashMap<String, String> o2 = (HashMap<String, String>) daftar_list_ads_CheckBox.get(position);
////                waybill = o.get("Waybill");
//                String AWB = o.get("no_awb");
//                String CheckBox = o.get("checkbox");
//                Log.d("Debug","Result QWERTY >>> AWB = " + AWB + " || Checkbox = " + CheckBox);
////                String AWB2 = o2.get("no_awb");
////                String CheckBox2 = o2.get("checkbox");
////                Log.d("Debug","Result QWERTY 2 >>> AWB = " + AWB2 + " || Checkbox = " + CheckBox2);
//                show_dialog_detail();
//            }
//        });
//		adapterDetailADS.notifyDataSetChanged();
//		adapterDetailADS.updateData(adapterDetailADS);
//		adapterDetailADS.clear();
//		
//    }
	
//	public void adapter_listview_ads_detail3() {
//		Log.e("Debug", "Listview ADS Detail");
//		ListDetailADSAdapter2 adapterDetailADS= new ListDetailADSAdapter2(this, daftar_list_ads_detail);
//		ListView listViewDetailADS = (ListView) findViewById(R.id.listviews_ads_detail);
//		listViewDetailADS.setAdapter(adapterDetailADS);
//		
//		listViewDetailADS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            	// TODO Auto-generated method stub
//            	Toast.makeText(getApplicationContext(),daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString()+ " Selected", Toast.LENGTH_LONG).show();
////            	adapter_listview_ads_update_y(daftar_list_ads_detail.get(position),position);
//            	String STR_awbCheckBox = daftar_list_ads_detail.get(position).get(AR_CHECKBOX).toString();
//            	if (STR_awbCheckBox == "n") {
//            		mapADSDetail.put(AR_CHECKBOX, "");
//            		daftar_list_ads_detail.remove(mapADSDetail);
//            		Log.d("Debug","Result ASDF First = " + daftar_list_ads_detail);
//            		adapter_listview_ads_update_y(daftar_list_ads_detail.get(position),position);
//            	}
//            	else if (STR_awbCheckBox == "y") {
//            		mapADSDetail.put(AR_CHECKBOX, "");
//            		daftar_list_ads_detail.remove(mapADSDetail);
//            		Log.d("Debug","Result ASDF First = " + daftar_list_ads_detail);
//            		adapter_listview_ads_update_n(daftar_list_ads_detail.get(position),position);
//            	}
//            	else {
//            		mapADSDetail.put(AR_CHECKBOX, "");
//            		daftar_list_ads_detail.remove(mapADSDetail);
//            		Log.d("Debug","Result ASDF First = " + daftar_list_ads_detail);
//            		adapter_listview_ads_update(daftar_list_ads_detail.get(position),position);
//            		Log.d("Debug","STAY HERE");
//            	}
//            }
//        });
//		
//    }
	
//	public void adapter_listview_ads_detail4() {
//		Log.e("Debug", "Listview ADS Detail");
//		ListDetailADSAdapter adapterDetailADS= new ListDetailADSAdapter(this, daftar_list_ads_detail);
//		ListView listViewDetailADS = (ListView) findViewById(R.id.listviews_ads_detail);
//		listViewDetailADS.setAdapter(adapterDetailADS);
//		adapterDetailADS.notifyDataSetChanged();
//		
//		listViewDetailADS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            	// TODO Auto-generated method stub
//            	STR_no_assigment = daftar_list_ads_detail.get(position).get(AR_NO_ASSIGMENT).toString();
//				STR_no_awb = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//				
//				ListView listViewDetailADS = (ListView) parent;
//
////				int count = listViewDetailADS.getAdapter().getCount();
////				Log.i("Debug", "Get Count = "+count);
////				String STR_Position = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
////				Log.i("Debug", "Get Position = "+STR_Position);
//			    CB_NoAWB = (CheckBox)findViewById(R.id.checkbox_awb);
//			    STR_no_assigment = (CB_NoAWB.isChecked())?"y":"n";
//			    Log.i("Debug", "CheckBox AWB "+STR_no_assigment);
//			    CB_NoAWB.setText(STR_no_assigment);
//			    
//			    //add ListView
//			    String STR_convertChangedCheckbox = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//			    Log.d("Debug","AWB " + STR_convertChangedCheckbox);
////			    TextView TV_ChangedCheckbox = (TextView) findViewById(R.id.status_ChangedCheckbox);
//			    ET_ListAWB.setText(STR_convertChangedCheckbox);
//			    String STR_GetTextAWBList = ET_ListAWB.getText().toString();
//			    Log.d("Debug","AWB 2 " + STR_GetTextAWBList);
//			    
//            	if (ET_ListAWB.getText().toString().trim().length() == 0) {
//	       			//Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
//            		ET_ListAWB.requestFocus();
//	       		}
//            	else {
//            		add_list();
//            	}
//			    
//			    if (STR_no_assigment == "n") {
//                	CB_NoAWB.setChecked(true);
//                	Toast.makeText(getBaseContext(), "You checked " + STR_no_awb, Toast.LENGTH_SHORT).show();
//    			} 
//			    else{
//                	CB_NoAWB.setChecked(false);
//                	Toast.makeText(getBaseContext(), "You unchecked " + STR_no_awb, Toast.LENGTH_SHORT).show();
//    			}
//				
//                @SuppressWarnings("unchecked")
//                HashMap<String, String> o = (HashMap<String, String>) daftar_list_ads_detail.get(position);
////                waybill = o.get("Waybill");
//                show_dialog_detail();
//            }
//        });
//		
//    }
	
//	public void adapter_listview_ads_detail5() {
//		Log.e("Debug", "Listview ADS Detail");
//		ListDetailADSAdapter2 adapterDetailADS= new ListDetailADSAdapter2(this, daftar_list_ads_detail);
//		ListView listViewDetailADS = (ListView) findViewById(R.id.listviews_ads_detail);
//		listViewDetailADS.setAdapter(adapterDetailADS);
//		
//		listViewDetailADS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            	// TODO Auto-generated method stub
////				Toast.makeText(getApplicationContext(),daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString()+ " Selected", Toast.LENGTH_LONG).show();
//				STR_no_assigment = daftar_list_ads_detail.get(position).get(AR_NO_ASSIGMENT).toString();
//				STR_no_awb = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//				
//				ListView listViewDetailADS = (ListView) parent;
//				
//				int count = listViewDetailADS.getAdapter().getCount();
//				Log.i("Debug", "Get Count = "+count);
//				String STR_Position = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//				Log.i("Debug", "Get Position = "+STR_Position);
//			    CB_NoAWB = (CheckBox)findViewById(R.id.checkbox_awb);
//			    String STR_no_assigmentCheckBox = (CB_NoAWB.isChecked())?"y":"n";
//			    Log.i("Debug", "CheckBox AWB"+STR_no_awb+" = "+STR_no_assigmentCheckBox);
//			    CB_NoAWB.setText(STR_no_assigmentCheckBox);
//			    
////			    String STR_convertChangedCheckbox = daftar_list_ads_detail.get(position).get(AR_CHECKBOX).toString();
////			    Log.d("Debug","Test Convert Changed Checkbox 1 " + STR_convertChangedCheckbox);
////			    TextView TV_ChangedCheckbox = (TextView) findViewById(R.id.status_ChangedCheckbox);
////			    TV_ChangedCheckbox.setText(STR_convertChangedCheckbox);
//			    
//			    //add ListView
//			    String STR_AWBList = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//			    String STR_TYPEList = daftar_list_ads_detail.get(position).get(AR_TYPE).toString();
//			    Log.d("Debug","AWB " + STR_AWBList);
////			    TextView TV_ChangedCheckbox = (TextView) findViewById(R.id.status_ChangedCheckbox);
////			    ET_ListAWB.setText(STR_AWBList);
//			    ET_ListAWB.setText(STR_AWBList+"-"+STR_TYPEList);
//			    String STR_GetTextAWBList = ET_ListAWB.getText().toString();
//			    Log.d("Debug","AWB 2 " + STR_GetTextAWBList);
//			    
//            	if (ET_ListAWB.getText().toString().trim().length() == 0) {
//	       			//Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
//            		ET_ListAWB.requestFocus();
//	       		}
//            	else {
//            		add_list();
//            	}
//			    
////			    Log.d("Debug","Test Changed Checkbox 2 " + TV_ChangedCheckbox);
////				String AR_CNVRT_STATUS_CHECKBOX = TV_ChangedCheckbox.getText().toString();
////				Log.d("Debug","Result Checkbox " + AR_CNVRT_STATUS_CHECKBOX);
//				
//                @SuppressWarnings("unchecked")
//                HashMap<String, String> o = (HashMap<String, String>) daftar_list_ads_detail.get(position);
//                String AWB = o.get("no_awb");
//                String CheckBox = o.get("checkbox");
//                Log.d("Debug","Result QWERTY >>> AWB = " + AWB + " || Checkbox = " + CheckBox);
//                show_dialog_detail();
//            }
//        });
//		adapterDetailADS.notifyDataSetChanged();
//		adapterDetailADS.updateData(adapterDetailADS);
//		adapterDetailADS.clear();
//		
//    }
	
//	public void adapter_listview_ads_detail6() {
//		Log.e("Debug", "Listview ADS Detail");
//		ListDetailADSAdapter2 adapterDetailADS= new ListDetailADSAdapter2(this, daftar_list_ads_detail);
//		ListView listViewDetailADS = (ListView) findViewById(R.id.listviews_ads_detail);
//		listViewDetailADS.setAdapter(adapterDetailADS);
//		
//		listViewDetailADS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            	// TODO Auto-generated method stub
////				Toast.makeText(getApplicationContext(),daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString()+ " Selected", Toast.LENGTH_LONG).show();
//				STR_no_assigment = daftar_list_ads_detail.get(position).get(AR_NO_ASSIGMENT).toString();
//				STR_no_awb = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//				
//				ListView listViewDetailADS = (ListView) parent;
//				
//				int count = listViewDetailADS.getAdapter().getCount();
//				Log.i("Debug", "Get Count = "+count);
//				String STR_Position = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//				Log.i("Debug", "Get Position = "+STR_Position);
//			    CB_NoAWB = (CheckBox)findViewById(R.id.checkbox_awb);
//			    String STR_no_assigmentCheckBox = (CB_NoAWB.isChecked())?"y":"n";
//			    Log.i("Debug", "CheckBox AWB"+STR_no_awb+" = "+STR_no_assigmentCheckBox);
//			    CB_NoAWB.setText(STR_no_assigmentCheckBox);
//			    
//			    //add ListView
//			    String STR_AWBList = daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString();
//			    String STR_TYPEList = daftar_list_ads_detail.get(position).get(AR_TYPE).toString();
//			    Log.d("Debug","AWB " + STR_AWBList);
//			    
//			    ET_ListAWB.setText(STR_AWBList+"-"+STR_TYPEList);
//			    String STR_GetTextAWBList = ET_ListAWB.getText().toString();
//			    Log.d("Debug","AWB 2 " + STR_GetTextAWBList);
//			    
//            	if (ET_ListAWB.getText().toString().trim().length() == 0) {
//	       			//Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
//            		ET_ListAWB.requestFocus();
//	       		}
//            	else {
//            		add_list();
//            		Log.d("Debug","Lewat Simpan Di ListView");
//            	}
//			    
////			    mapADSDetail = new HashMap<String, String>();
////				mapADSDetail.put(AR_NO_ASSIGMENT, daftar_list_ads_detail.get(position).get(AR_NO_ASSIGMENT).toString());
////				mapADSDetail.put(AR_NO_AWB, daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString());
////				mapADSDetail.put(AR_NAMA_TOKO, daftar_list_ads_detail.get(position).get(AR_NAMA_TOKO).toString());
////				mapADSDetail.put(AR_STATUS, daftar_list_ads_detail.get(position).get(AR_STATUS).toString());
////				mapADSDetail.put(AR_ALAMAT_TOKO, daftar_list_ads_detail.get(position).get(AR_ALAMAT_TOKO).toString());
////				mapADSDetail.put(AR_TYPE, daftar_list_ads_detail.get(position).get(AR_TYPE).toString());
////				mapADSDetail.put(AR_CHECKBOX, daftar_list_ads_detail.get(position).get(AR_CHECKBOX).toString());
////				daftar_list_ads_detail.remove(mapADSDetail);
////				Log.d("Debug","Result Default " + daftar_list_ads_detail);
//				
//				String STR_awbCheckBox = daftar_list_ads_detail.get(position).get(AR_CHECKBOX).toString();
//            	if (STR_awbCheckBox == "n" && STR_awbCheckBox != "y") {
//            		Log.d("Debug","Lewat CheckBox " + STR_awbCheckBox);
//            		adapter_listview_ads_update_y(mapADSDetail, 0);
//            	}
//            	else if (STR_awbCheckBox == "y" && STR_awbCheckBox != "n") {
//            		Log.d("Debug","Lewat CheckBox " + STR_awbCheckBox);
//            		adapter_listview_ads_update_n(mapADSDetail, 0);
//            	}
//            	else {
//            		Log.d("Debug","Lewat CheckBox " + STR_awbCheckBox);
//            		adapter_listview_ads_update(mapADSDetail, 0);
//            	}
//				
//                @SuppressWarnings("unchecked")
//                HashMap<String, String> o = (HashMap<String, String>) daftar_list_ads_detail.get(position);
//                String AWB = o.get("no_awb");
//                String CheckBox = o.get("checkbox");
//                Log.d("Debug","Result QWERTY >>> AWB = " + AWB + " || Checkbox = " + CheckBox);
//                show_dialog_detail();
//            }
//        });
//		adapterDetailADS.notifyDataSetChanged();
//		adapterDetailADS.updateData(adapterDetailADS);
//		adapterDetailADS.clear();
//		
//    }
	
//	public void adapter_listview_ads_update_y(HashMap<String, String> hashMap, int position) {
//		mapADSDetail = new HashMap<String, String>();
//		mapADSDetail.put(AR_NO_ASSIGMENT, daftar_list_ads_detail.get(position).get(AR_NO_ASSIGMENT).toString());
//		mapADSDetail.put(AR_NO_AWB, daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString());
//		mapADSDetail.put(AR_NAMA_TOKO, daftar_list_ads_detail.get(position).get(AR_NAMA_TOKO).toString());
//		mapADSDetail.put(AR_STATUS, daftar_list_ads_detail.get(position).get(AR_STATUS).toString());
//		mapADSDetail.put(AR_ALAMAT_TOKO, daftar_list_ads_detail.get(position).get(AR_ALAMAT_TOKO).toString());
//		mapADSDetail.put(AR_TYPE, daftar_list_ads_detail.get(position).get(AR_TYPE).toString());
//		mapADSDetail.put(AR_CHECKBOX, "y");
////		daftar_list_ads_detail.add(mapADSDetail);
//		Log.d("Debug","Result ASDF = " + daftar_list_ads_detail);
//	}
//	public void adapter_listview_ads_update_n(HashMap<String, String> hashMap, int position) {
//		mapADSDetail = new HashMap<String, String>();
//		mapADSDetail.put(AR_NO_ASSIGMENT, daftar_list_ads_detail.get(position).get(AR_NO_ASSIGMENT).toString());
//		mapADSDetail.put(AR_NO_AWB, daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString());
//		mapADSDetail.put(AR_NAMA_TOKO, daftar_list_ads_detail.get(position).get(AR_NAMA_TOKO).toString());
//		mapADSDetail.put(AR_STATUS, daftar_list_ads_detail.get(position).get(AR_STATUS).toString());
//		mapADSDetail.put(AR_ALAMAT_TOKO, daftar_list_ads_detail.get(position).get(AR_ALAMAT_TOKO).toString());
//		mapADSDetail.put(AR_TYPE, daftar_list_ads_detail.get(position).get(AR_TYPE).toString());
//		mapADSDetail.put(AR_CHECKBOX, "n");
////		daftar_list_ads_detail.add(mapADSDetail);
//		Log.d("Debug","Result ASDF = " + daftar_list_ads_detail);
//	}
//	public void adapter_listview_ads_update(HashMap<String, String> hashMap, int position) {
//		mapADSDetail = new HashMap<String, String>();
//		mapADSDetail.put(AR_NO_ASSIGMENT, daftar_list_ads_detail.get(position).get(AR_NO_ASSIGMENT).toString());
//		mapADSDetail.put(AR_NO_AWB, daftar_list_ads_detail.get(position).get(AR_NO_AWB).toString());
//		mapADSDetail.put(AR_NAMA_TOKO, daftar_list_ads_detail.get(position).get(AR_NAMA_TOKO).toString());
//		mapADSDetail.put(AR_STATUS, daftar_list_ads_detail.get(position).get(AR_STATUS).toString());
//		mapADSDetail.put(AR_ALAMAT_TOKO, daftar_list_ads_detail.get(position).get(AR_ALAMAT_TOKO).toString());
//		mapADSDetail.put(AR_TYPE, daftar_list_ads_detail.get(position).get(AR_TYPE).toString());
//		mapADSDetail.put(AR_CHECKBOX, daftar_list_ads_detail.get(position).get(AR_CHECKBOX).toString());
////		daftar_list_ads_detail.add(mapADSDetail);
//		Log.d("Debug","Result ASDF = " + daftar_list_ads_detail);
////		this.adapter_listview_ads_detail3();
//	}
	
	public void add_list(){
		 
		 int x = listAdapter.getPosition(ET_ListAWB.getText().toString().trim()) ;
			
			Log.d("add waybill","cek posisi " + ET_ListAWB.getText().toString().trim() + " => " + x);
			
			if (x < 0 )
			{	
			listAdapter.add(ET_ListAWB.getText().toString().trim());
			Log.d("add waybill", "=> " + ET_ListAWB.getText().toString().trim() );
			LV_ListAWB.setAdapter( listAdapter );
			Toast.makeText(getBaseContext(), "You checked " + STR_no_awb, Toast.LENGTH_SHORT).show();
			}
			else if (x == x )
			{
				listAdapter.remove(ET_ListAWB.getText().toString().trim());
				Log.d("add waybill 2", "=> " + ET_ListAWB.getText().toString().trim() );
				LV_ListAWB.setAdapter( listAdapter );
				Toast.makeText(getBaseContext(), "You unchecked " + STR_no_awb, Toast.LENGTH_SHORT).show();
			}
			
			//ewaybill.setFocusableInTouchMode(true);
			ET_ListAWB.requestFocus();
			ET_ListAWB.setText("");
			update_data();
	 }
	
	public void add_list2(){
		 
		 int x = listAdapter.getPosition(ET_ListAWB.getText().toString().trim()) ;
			
			Log.d("add waybill","cek posisi " + ET_ListAWB.getText().toString().trim() + " => " + x);
			
			if (x < 0 )
			{	
			listAdapter.add(ET_ListAWB.getText().toString().trim());
			Log.d("add waybill", "=> " + ET_ListAWB.getText().toString().trim() );
			LV_ListAWB.setAdapter( listAdapter );
			Toast.makeText(getBaseContext(), "You add " + STR_no_awb, Toast.LENGTH_SHORT).show();
			}
			else if (x == x )
			{
				listAdapter.clear();
				Log.d("Debug", "remove waybill all");
				LV_ListAWB.setAdapter( listAdapter );
				Toast.makeText(getBaseContext(), "You remove all waybill", Toast.LENGTH_SHORT).show();
				
				int i,a;
				String swaybill2 = "";
				i = this.LV_ListAWB.getCount();
				Log.d("Debug","Isi ListView remove all = " + i);
				for (a = 0; a<i; a++)
				{
					if(a<i-1){
//						swaybill2 += "'"+this.LV_ListAWB.getAdapter().getItem(a)+ "',"; }
						swaybill2 += ""+this.LV_ListAWB.getAdapter().getItem(a)+ ","; }
					else{
//						swaybill2 += "'"+this.LV_ListAWB.getAdapter().getItem(a)+ "'";
						swaybill2 += ""+this.LV_ListAWB.getAdapter().getItem(a)+ "";
					}
				}
				Log.w("get waybill remove all ", swaybill2);
				
				if (i == 0)
				{	
					listAdapter.add(ET_ListAWB.getText().toString().trim());
					Log.d("add again waybill", "=> " + ET_ListAWB.getText().toString().trim() );
					LV_ListAWB.setAdapter( listAdapter );
					Toast.makeText(getBaseContext(), "You add " + STR_no_awb, Toast.LENGTH_SHORT).show();
					
					int i2,a2;
					String swaybill3 = "";
					i2 = this.LV_ListAWB.getCount();
					Log.d("Debug","Isi ListView add = " + i2);
					for (a2 = 0; a2<i; a2++)
					{
						if(a2<i2-1){
//							swaybill3 += "'"+this.LV_ListAWB.getAdapter().getItem(a2)+ "',"; }
							swaybill3 += ""+this.LV_ListAWB.getAdapter().getItem(a2)+ ","; }
						else{
//							swaybill3 += "'"+this.LV_ListAWB.getAdapter().getItem(a2)+ "'";
							swaybill3 += ""+this.LV_ListAWB.getAdapter().getItem(a2)+ "";
						}
					}
					Log.w("get waybill add ", swaybill3);
				}
			}
			
			//ewaybill.setFocusableInTouchMode(true);
			ET_ListAWB.requestFocus();
			ET_ListAWB.setText("");
			update_data();
	 }
	
	public void add_list3(){
		 
	 	int x = listAdapter.getPosition(ET_ListAWB.getText().toString().trim()) ;
		Log.d("Debug","1. cek posisi " + ET_ListAWB.getText().toString().trim() + " => " + x);
		listAdapter.remove(ET_ListAWB.getText().toString().trim());
		Log.d("remove waybill","XXX cek isi waybill " + ET_ListAWB.getText().toString().trim() + " => " + x);
		
		int i1,a1;
		String swaybill1 = "";
		gps = new gps_tracker(ListDetailADS.this);
		i1 = this.LV_ListAWB.getCount();
		Log.w("Debug","Isi ListView Awal = " + i1);
		
		if (i1 < 0){
			
			listAdapter.clear();
			Log.d("Debug", "remove waybill all");
			LV_ListAWB.setAdapter( listAdapter );
			Toast.makeText(getBaseContext(), "You remove all waybill", Toast.LENGTH_SHORT).show();
			
			int i1a,a1a;
			String swaybill1a = "";
			gps = new gps_tracker(ListDetailADS.this);
			i1a = this.LV_ListAWB.getCount();
			Log.w("Debug","Isi ListView Lebih Dari 0 = " + i1a);
			
			if (i1a == 0){
				listAdapter.add(ET_ListAWB.getText().toString().trim());
				Log.d("Tambah Waybill If If ", "=> " + ET_ListAWB.getText().toString().trim() );
				LV_ListAWB.setAdapter( listAdapter );
				Toast.makeText(getBaseContext(), "You add " + STR_no_awb, Toast.LENGTH_SHORT).show();
				
				int i1a2,a1a2;
				String swaybill1a2 = "";
				gps = new gps_tracker(ListDetailADS.this);
				i1a2 = this.LV_ListAWB.getCount();
				Log.w("Debug","Isi ListView Sama Dengan 0 = " + i1a2);
			}
		}
		
		else {
			
			listAdapter.add(ET_ListAWB.getText().toString().trim());
			Log.d("Tambah Waybill Else ", "=> " + ET_ListAWB.getText().toString().trim() );
			LV_ListAWB.setAdapter( listAdapter );
			Toast.makeText(getBaseContext(), "You add " + STR_no_awb, Toast.LENGTH_SHORT).show();
			
			int i1b,a1b;
			String swaybill66 = "";
			gps = new gps_tracker(ListDetailADS.this);
			i1b = this.LV_ListAWB.getCount();
			Log.w("Debug","Isi ListView Tidak Lebih Dari 0 = " + i1b);
		}
		update_data();
	}
	
	public void update_data() {
		int i,a;
		 
		String swaybill = "";

		gps = new gps_tracker(ListDetailADS.this);
		i = this.LV_ListAWB.getCount();
		Log.d("Debug","Isi ListView = " + i);
		for (a = 0; a<i; a++)
		{
			if(a<i-1){
//				swaybill += "'"+this.LV_ListAWB.getAdapter().getItem(a)+ "',"; }
				swaybill += ""+this.LV_ListAWB.getAdapter().getItem(a)+ ","; }
			else{
//				swaybill += "'"+this.LV_ListAWB.getAdapter().getItem(a)+ "'";
				swaybill += ""+this.LV_ListAWB.getAdapter().getItem(a)+ "";
			}
		}

		Log.w("get waybill", swaybill);
		STR_swaybill = swaybill;
		Log.w("Debug","log waybill " + STR_swaybill);
//		update_data_finish_ads_with_pincode2();
	}
	
	private class CustomAWBListAdapter extends ArrayAdapter<AWBList> {
		TextView mNoAssigment, mNoAWB, mNamaToko, mStatus, mType;
		CheckBox CB_NoAWB;
		private static final String AR_NO_ASSIGMENT = "asigment";
		private static final String AR_NO_AWB = "no_awb";
		private static final String AR_NAMA_TOKO = "nama_toko";
		private static final String AR_STATUS = "status";
		private static final String AR_ALAMAT = "alamat";
		private static final String AR_SERVICE = "service";
		private static final String AR_TYPE = "type";
//		private ArrayList<AWBList> AWBListArray;
//
//		public CustomAWBListAdapter(Context context, int textViewResourceId,
//				ArrayList<AWBList> AWBListArray) {
//			super(context, textViewResourceId, AWBListArray);
//			this.AWBListArray = new ArrayList<AWBList>();
//			this.AWBListArray.addAll(AWBListArray);
//		}
		
		private ArrayList<AWBList> daftar_list_ads_detail;

		public CustomAWBListAdapter(Context context, int textViewResourceId,
				ArrayList<AWBList> Store_AWBList) {
			super(context, textViewResourceId, Store_AWBList);
			this.daftar_list_ads_detail = new ArrayList<AWBList>();
			this.daftar_list_ads_detail.addAll(Store_AWBList);
		}

		private class ViewHolder {
			TextView code;
			CheckBox name;
		}
		
//		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				convertView = vi.inflate(R.layout.list_awb_row, null);
				convertView = vi.inflate(R.layout.list_ads_detail_adapter, null);

				holder = new ViewHolder();
				holder.code = (TextView) convertView.findViewById(R.id.code);
				holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
				convertView.setTag(holder);

				holder.name.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						AWBList problem = (AWBList) cb.getTag();
						Toast.makeText(getApplicationContext(),"Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(),Toast.LENGTH_LONG).show();
						problem.setSelected(cb.isChecked());
					}
				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

//			AWBList problem = AWBListArray.get(position);
			AWBList AWBlistViewDetailADS = daftar_list_ads_detail.get(position);
			holder.code.setText("");
			holder.name.setText(AWBlistViewDetailADS.getName());
			holder.name.setChecked(AWBlistViewDetailADS.isSelected());
			holder.name.setTag(AWBlistViewDetailADS);

			return convertView;

		}
	}
	
	public void OnNextScan(View view, int position) {
		Log.d("Debug", "Next Scan");
		HashMap<String, String> o = (HashMap<String, String>) daftar_list_ads_detail.get(position);
		String status = o.get("status");
		// Log.d("tes waybill", waybill);
		if (status.equals("0")) {
			show_dialog1();
		} else if (status.equals("1")) {
			show_dialog2();
		}
	}

	//Tidak Di Gunakan
	public void update_data_finish_ads_with_pincode() {
        Log.d("Debug", "Update Pincode Data Finish ADS");
        gps = new gps_tracker(ListDetailADS.this);
        
        String Str_check_connection_ads = null;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            // aksi ketika ada koneksi internet
            Str_check_connection_ads = "Yes Signal";
            Log.d("Debug", "Koneksi Internet Tersedia");
        } else {
            // aksi ketika tidak ada koneksi internet
            Str_check_connection_ads = "No Signal";
            Log.d("Debug", "Koneksi Internet Tidak Tersedia");
        }
        
        ArrayList<NameValuePair> masukparam = new ArrayList<NameValuePair>();
        String Str_NoAssigment = STR_no_assigment;
        String Str_code_store = STR_kode_toko;
        String Str_username = username;
        String Str_locpk = locpk;
        String Str_pincode = STR_pincode;
        String Str_lat_long = Double.toString(gps.getLatitude()) + ", "
                + Double.toString(gps.getLongitude());
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();  
        String STR_date_time = dateformat.format(date);
        String Str_waktu = STR_date_time;
        
        Log.d("Debug", "Cek Pincode Update Data ADS" 
        		+ "1.No.Assigment = " + Str_NoAssigment
        		+ "2.Kode Store = " + Str_code_store
        		+ "3.Username = " + Str_username
        		+ "4.Locpk = " + Str_locpk
        		+ "5.Pincode = " + Str_pincode
        		+ "6.LatLong = " + Str_lat_long
        		+ "7.Date Time = " + STR_date_time);
        
        String response = null;
        
        ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
		masukparam1.add(new BasicNameValuePair("asigment", Str_NoAssigment));
		masukparam1.add(new BasicNameValuePair("customer", Str_code_store));
		masukparam1.add(new BasicNameValuePair("username", Str_username));
		masukparam1.add(new BasicNameValuePair("locpk", Str_locpk));
		masukparam1.add(new BasicNameValuePair("pincode", Str_pincode));
		masukparam1.add(new BasicNameValuePair("lat_long", Str_lat_long));
		masukparam1.add(new BasicNameValuePair("waktu", Str_waktu));
		
		String response1;
		
		try {
			Log.d("Debug", "Try"+"Update Pincode Data ADS ");
			cekPincodePostADS(Str_check_connection_ads, Str_NoAssigment, Str_code_store, Str_username, Str_locpk
					, Str_pincode, Str_lat_long, Str_waktu);
			Log.d("Debug","Trace String");
//			savetoLocalDB(Str_check_connection_ads, Str_NoAssigment, Str_code_store, Str_username, Str_locpk
//					, Str_pincode, Str_lat_long, Str_waktu);
			savetoLocalDB(STR_no_assigment);
			reset();
		} catch (Exception e) {
			Log.d("Debug", "Catch"+"Update Pincode Data ADS ");
			Log.d("Gagal", e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
			displayExceptionMessage(e.getMessage());
//			savetoLocalDB(Str_check_connection_ads, Str_NoAssigment, Str_code_store, Str_username, Str_locpk
//					, Str_pincode, Str_lat_long, Str_waktu);
			savetoLocalDB(STR_no_assigment);
			reset();
		}
        
    }
	
	public void update_data_finish_ads_with_pincode2() {
        Log.d("Debug", "Update Pincode Data Finish ADS Ke-2");
        gps = new gps_tracker(ListDetailADS.this);
        
        Log.w("DEBUG","LIAT WAYBILL LAGI " + STR_swaybill);
        
        String Str_check_connection_ads = null;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            // aksi ketika ada koneksi internet
            Str_check_connection_ads = "Yes Signal";
            Log.d("Debug", "Koneksi Internet Tersedia");
        } else {
            // aksi ketika tidak ada koneksi internet
            Str_check_connection_ads = "No Signal";
            Log.d("Debug", "Koneksi Internet Tidak Tersedia");
        }
        
        ArrayList<NameValuePair> masukparam = new ArrayList<NameValuePair>();
        String Str_NoAssigment = STR_no_assigment;
        String Str_code_store = STR_kode_toko;
        String Str_username = username;
        String Str_locpk = locpk;
        String Str_pincode = STR_pincode;
        String Str_lat_long = Double.toString(gps.getLatitude()) + ", "
                + Double.toString(gps.getLongitude());
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();  
        String STR_date_time = dateformat.format(date);
        String Str_waktu = STR_date_time;
//        String swaybill = null;
		String STR_waybill = STR_swaybill;
		String STR_kota = locpk;
        
        Log.d("Debug", "Cek Pincode Update Data ADS" 
        		+ " || 1.No.Assigment = " + Str_NoAssigment
        		+ " || 2.Kode Store = " + Str_code_store
        		+ " || 3.Username = " + Str_username
        		+ " || 4.Locpk = " + Str_locpk
        		+ " || 5.Pincode = " + Str_pincode
        		+ " || 6.LatLong = " + Str_lat_long
        		+ " || 7.Date Time = " + STR_date_time
        		+ " || 8.Waybill = " + STR_waybill
        		+ " || 9.Kota = " + STR_kota);
        
        String response = null;
        
        ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
		masukparam1.add(new BasicNameValuePair("asigment", Str_NoAssigment));
		masukparam1.add(new BasicNameValuePair("customer", Str_code_store));
		masukparam1.add(new BasicNameValuePair("username", Str_username));
		masukparam1.add(new BasicNameValuePair("locpk", Str_locpk));
		masukparam1.add(new BasicNameValuePair("pincode", Str_pincode));
		masukparam1.add(new BasicNameValuePair("lat_long", Str_lat_long));
		masukparam1.add(new BasicNameValuePair("waktu", Str_waktu));
		masukparam1.add(new BasicNameValuePair("waybill", STR_waybill));
		masukparam1.add(new BasicNameValuePair("kota", STR_kota));
		
		String response1;
		
		try {
			Log.d("Debug", "Try"+"Update Pincode Data ADS ");
//			cekPincodePostADS2(Str_check_connection_ads, Str_NoAssigment, Str_code_store
//					, Str_username, Str_locpk, Str_pincode, Str_lat_long, Str_waktu, STR_waybill, STR_kota);
			new android.os.Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					new cekPostPincodePostADS2Async().execute();
				}
			},500);
			Log.d("Debug","Trace String");
			savetoLocalDB2(STR_no_assigment);
			reset();
		} catch (Exception e) {
			Log.d("Debug", "Catch"+"Update Pincode Data ADS ");
			Log.d("Gagal", e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
			displayExceptionMessage(e.getMessage());
			savetoLocalDB2(STR_no_assigment);
			reset();
		}
        
    }
	
	private void displayExceptionMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	private void reset() {
		// TODO Auto-generated method stub
		
	}

	public void cekPincodePostADS(String Str_check_connection_ads, String Str_NoAssigment, String Str_code_store, String Str_username, String Str_locpk
			, String Str_pincode, String Str_lat_long, String Str_waktu) throws JSONException {
		 Log.d("Debug", "Lewat Pincode Post ADS");
		 String Str_2nd_check_connection_ads_finish = Str_check_connection_ads;
			Log.d("Debug", "2nd Response Check Connection  = " + Str_2nd_check_connection_ads_finish);
			if (Str_2nd_check_connection_ads_finish.equals("No Signal")) {
				Toast.makeText(getApplicationContext(), "Lost Signal...!! Data Disimpan Sementara Di Lokal Database", Toast.LENGTH_SHORT).show();
				show_dialog5();
			} else {
				HttpURLConnection connection;
				OutputStreamWriter request = null;
				URL url = null;
				String URI = null;
				String response = null;
				String parameters = "asigment=" + Str_NoAssigment 
						+ "&customer=" + Str_code_store
						+ "&username=" + Str_username
						+ "&locpk=" + Str_locpk
						+ "&pincode=" + Str_pincode
						+ "&lat_long=" + Str_lat_long
						+ "&waktu=" + Str_waktu;
				Log.d("Debug", "Parameters " + parameters);
				try {
					url = new URL(STR_url_finish_pincode_finish_ads);
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
					Log.d("Debug", "Trace Customer = " + json);
					RespnseCodePincodePostADS = json.getString("response_code");
					RespnseMessagePincodePostADS = json.getString("response_message");
					Log.d("Debug", "Response Code = " + RespnseCodePincodePostADS);
					Log.d("Debug", "Response Message  = " + RespnseMessagePincodePostADS);
					
					TV_response_code = (TextView) findViewById(R.id.txt_Response_Code);
					TV_response_code.setText(RespnseCodePincodePostADS);
					STR_response_code = TV_response_code.getText().toString();
					
					String Str_status = STR_response_code;
					Log.d("Debug", "String Response Code = " + Str_status);
					
//					if (RespnseCode.toString().trim().length() == 0) {
////		       			Toast.makeText(getApplicationContext(), "Periksa kembali Waybill yang anda scan.", Toast.LENGTH_SHORT).show();
//						Toast.makeText(getApplicationContext(), RespnseMessage, Toast.LENGTH_SHORT).show();
//		       		}
//					else {
////						Toast.makeText(getApplicationContext(), "AWB Berhasil Di Proses",0).show();
//						Toast.makeText(getApplicationContext(), RespnseMessage, Toast.LENGTH_SHORT).show();
//					}
					
					if (Str_status.equals("1")) {
						Log.i("Debug", "Status Pincode Success Finish");
						show_dialog5();
					} else {
						Log.i("Debug", "Status Pincode Gagal Finish");
						show_dialog6();
					}
				} catch (IOException e) {
					// Error
					Log.d("Debug", "Trace = ERROR ");
				}
			}
	}
	
	public void cekPincodePostADS2(String Str_check_connection_ads, String Str_NoAssigment, String Str_code_store, String Str_username, String Str_locpk
			, String Str_pincode, String Str_lat_long, String Str_waktu, String STR_waybill, String STR_kota) throws JSONException {
		 Log.d("Debug", "Lewat Pincode Post ADS");
		 String Str_2nd_check_connection_ads_finish = Str_check_connection_ads;
			Log.d("Debug", "2nd Response Check Connection  = " + Str_2nd_check_connection_ads_finish);
			if (Str_2nd_check_connection_ads_finish.equals("No Signal")) {
				Toast.makeText(getApplicationContext(), "Lost Signal...!! Data Disimpan Sementara Di Lokal Database", Toast.LENGTH_SHORT).show();
				show_dialog5();
			} else {
				HttpURLConnection connection;
				OutputStreamWriter request = null;
				URL url = null;
				String URI = null;
				String response = null;
				String parameters = "asigment=" + Str_NoAssigment 
						+ "&customer=" + Str_code_store
						+ "&username=" + Str_username
						+ "&locpk=" + Str_locpk
						+ "&pincode=" + Str_pincode
						+ "&lat_long=" + Str_lat_long
						+ "&waktu=" + Str_waktu
						+ "&waybill=" + STR_waybill
						+ "&kota=" + STR_kota;
				Log.d("Debug", "Parameters " + parameters);
				try {
					url = new URL(STR_url_finish_pincode_finish_ads_Pincode);
//					url = new URL("#");
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
					Log.d("Debug", "Trace Customer = " + json);
					RespnseCode = json.getString("response_code");
					RespnseMessage = json.getString("response_message");
					Log.d("Debug", "Response Code = " + RespnseCode);
					Log.d("Debug", "Response Message  = " + RespnseMessage);
					
					TV_response_code = (TextView) findViewById(R.id.txt_Response_Code);
					TV_response_code.setText(RespnseCode);
					STR_response_code = TV_response_code.getText().toString();
					
					String Str_status = STR_response_code;
					Log.d("Debug", "String Response Code = " + Str_status);
					
					if (Str_status.equals("1")) {
						Log.i("Debug", "Status Pincode Success Finish");
						show_dialog5();
					} else {
						Log.i("Debug", "Status Pincode Gagal Finish");
						show_dialog6();
					}
				} catch (IOException e) {
					// Error
					Log.d("Debug", "Trace = ERROR ");
				}
			}
	}

	public class cekPostPincodePostADS2Async extends AsyncTask<String, String, String>
	{
		ProgressDialog pDialog;
		String Str_status;
		String Str_NoAssigment = STR_no_assigment;
		String Str_code_store = STR_kode_toko;
		String Str_username = username;
		String Str_locpk = locpk;
		String Str_pincode = STR_pincode;
		String Str_lat_long = Double.toString(gps.getLatitude()) + ", "
				+ Double.toString(gps.getLongitude());
		String STR_lat_long = Str_lat_long;
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String STR_date_time = dateformat.format(date);
		String Str_waktu = STR_date_time;
		String STR_waybill = STR_swaybill;
		String STR_kota = locpk;

		protected void onPreExecute(){
			super.onPreExecute();

//			pDialog = new ProgressDialog(ListDetailADS.this);
//			pDialog.setMessage("Loading...");
//			pDialog.setIndeterminate(false);
//			pDialog.setCancelable(false);
//			pDialog.show();
            fl_progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			HttpURLConnection connection;
			OutputStreamWriter request = null;
			URL url = null;
			String URI = null;
			String response = null;
			String parameters = "asigment=" + Str_NoAssigment
					+ "&customer=" + Str_code_store
					+ "&username=" + Str_username
					+ "&locpk=" + Str_locpk
					+ "&pincode=" + Str_pincode
					+ "&lat_long=" + Str_lat_long
					+ "&waktu=" + Str_waktu
					+ "&waybill=" + STR_waybill
					+ "&kota=" + STR_kota;
			Log.d("Debug", "Parameters " + parameters);

			try
			{
				String urlphp;
				urlphp = "";
				Log.d("debug", "Host Server -> " + STR_url_finish_pincode_finish_ads_Pincode);
				url = new URL(STR_url_finish_pincode_finish_ads_Pincode);
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
				RespnseCodePincodePostADS2 = json.getString("response_code");
				RespnseMessagePincodePostADS2 = json.getString("response_message");
				Log.d("Debug", "Response Code = " + RespnseCodePincodePostADS2);
				Log.d("Debug", "Response Message  = " + RespnseMessagePincodePostADS2);

//				TV_response_code = (TextView) findViewById(R.id.txt_Response_Code);
//				TV_response_code.setText(RespnseCode);
//				STR_response_code = TV_response_code.getText().toString();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						TV_response_code = findViewById(R.id.txt_Response_Code);
						TV_response_code.setText(RespnseCodePincodePostADS2);
						STR_response_code = TV_response_code.getText().toString();
					}
				});

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(1000);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        Log.d("Debug", "Selesai");
//                        TV_response_code = findViewById(R.id.txt_Response_Code);
//                        TV_response_code.setText(RespnseCode);
//                        STR_response_code = TV_response_code.getText().toString();
//                        Log.d("Debug", "Selesai");
//                        fl_progress.setVisibility(View.GONE);
//                    }
//                });

//				Str_status = STR_response_code;
				Str_status = RespnseCodePincodePostADS2;
				Log.d("Debug", "String Response Code = " + Str_status);
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
//			super.onPostExecute(result);
//			pDialog.dismiss();
            fl_progress.setVisibility(View.GONE);
			Log.d("Debug", "1.Test Sampai Sini ");
			if (Str_status.equals("1")) {
				Log.i("Debug", "Status Pincode Success Finish");
				show_dialog5();
			} else {
				Log.i("Debug", "Status Pincode Gagal Finish");
				show_dialog6();
			}
		}
	}

	//Tidak Di Gunakan
//	private void savetoLocalDB(String Str_check_connection_ads, String Str_NoAssigment, String Str_code_store, String Str_username, String Str_locpk
//			, String Str_pincode, String Str_lat_long, String Str_waktu) {
	private void savetoLocalDB(String STR_no_assigment) {
		Log.d("Debug","SaveLocalDBPincodeADS");
		C_Pincode_ADS finish_pincode_ads = new C_Pincode_ADS();
		
//		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date date = new Date();  
//        String STR_date_time = dateformat.format(date);
        String Str_finish_waktu = STR_date_time;
		String Str_finish_assigment = STR_no_assigment;
        String Str_finish_kode_toko = STR_kode_toko;
        String Str_finish_username = username;
        String Str_finish_locpk = locpk;
        String Str_finish_pincode = STR_pincode;
        
        Log.d("Debug", "Isi SaveDB ADS >>>" 
	    		+ " DateTime = " + Str_finish_waktu 
	    		+ " Assigment = " + Str_finish_assigment
	    		+ " Kode Toko = " + Str_finish_kode_toko 
	    		+ " Username = " + Str_finish_username 
	    		+ " Locpk = " + Str_finish_locpk
	    		+ " Pincode = " + Str_finish_pincode);
		
		STR_lat_long = Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude());
		Log.d("Debug", "Latlong " + STR_lat_long);
		
		finish_pincode_ads.setWaktu(STR_date_time);
		finish_pincode_ads.setAssigment(STR_no_assigment);
		finish_pincode_ads.setCustomer(STR_kode_toko);
		finish_pincode_ads.setUsername(username);
		finish_pincode_ads.setLocpk(Str_finish_locpk);
		finish_pincode_ads.setPincode(STR_pincode);
		finish_pincode_ads.setLat_Long(STR_lat_long);
		Log.d("Debug","add Pincode Detail ADS Finish -> local" );
		dbFinish = new ListFinishPincodeADSDBAdapter(ListDetailADS.this);
		 dbFinish.open();
		 dbFinish.createContact(finish_pincode_ads);
		 dbFinish.close();
		 Log.d("PUP","AWB add to local database ..");
		 listAdapter.clear();
		 mainListView.setAdapter(listAdapter);
	}
	
	private void savetoLocalDB2(String STR_no_assigment) {
		DBAdapter dbListFinishPincodeADS = new DBAdapter(getApplicationContext());
		List<String> labelsFinishPincodeADS = dbListFinishPincodeADS.getAllFinishPincodeADS();
		Log.i("Debug", "Cek Array List Save Finish Pincode ADS " + labelsFinishPincodeADS);
		boolean retval_finishPincodeADS = labelsFinishPincodeADS.isEmpty();
		if (retval_finishPincodeADS == true) {
			Log.i("Debug", "Array List Finish Pincode Kosong");

			Log.d("Debug", "SaveLocalDBPincodeADS"+" || List Kosong");
			C_Pincode_ADS finish_pincode_ads = new C_Pincode_ADS();

			String Str_finish_waktu = STR_date_time;
			String Str_finish_assigment = STR_no_assigment;
			String Str_finish_kode_toko = STR_kode_toko;
			String Str_finish_username = username;
			String Str_finish_locpk = locpk;
			String Str_finish_pincode = STR_pincode;
			String Str_finish_waybill = STR_swaybill;
			String Str_finish_kota = STR_pincode;

			Log.d("Debug", "Isi SaveDB ADS >>>"
					+ " DateTime = " + Str_finish_waktu
					+ " Assigment = " + Str_finish_assigment
					+ " Kode Toko = " + Str_finish_kode_toko
					+ " Username = " + Str_finish_username
					+ " Locpk = " + Str_finish_locpk
					+ " Pincode = " + Str_finish_pincode
					+ " Waybill = " + STR_swaybill
					+ " Kota = " + Str_finish_locpk);

			STR_lat_long = Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude());
			Log.d("Debug", "Latlong " + STR_lat_long);

			finish_pincode_ads.setWaktu(STR_date_time);
			finish_pincode_ads.setAssigment(STR_no_assigment);
			finish_pincode_ads.setCustomer(STR_kode_toko);
			finish_pincode_ads.setUsername(username);
			finish_pincode_ads.setLocpk(Str_finish_locpk);
			finish_pincode_ads.setPincode(STR_pincode);
			finish_pincode_ads.setLat_Long(STR_lat_long);
			finish_pincode_ads.setWaybill(STR_swaybill);
			finish_pincode_ads.setKota(Str_finish_locpk);
			Log.d("Debug", "add Pincode Detail ADS Finish -> local");
			dbFinish = new ListFinishPincodeADSDBAdapter(ListDetailADS.this);
			dbFinish.open();
			dbFinish.createContact(finish_pincode_ads);
			dbFinish.close();
			Log.d("PUP", "AWB add to local database ..");
			listAdapter.clear();
			mainListView.setAdapter(listAdapter);
		}else {
			Log.i("Debug", "Array List Finish Pincode Isi");

//			Log.d("Debug", "SaveLocalDBPincodeADS"+" || List Isi");
//			C_Pincode_ADS finish_pincode_ads = new C_Pincode_ADS();
//
//			String Str_finish_waktu = STR_date_time;
//			String Str_finish_assigment = STR_no_assigment;
//			String Str_finish_kode_toko = STR_kode_toko;
//			String Str_finish_username = username;
//			String Str_finish_locpk = locpk;
//			String Str_finish_pincode = STR_pincode;
//			String Str_finish_waybill = STR_swaybill;
//			String Str_finish_kota = STR_pincode;
//
//			Log.d("Debug", "Isi SaveDB ADS >>>"
//					+ " DateTime = " + Str_finish_waktu
//					+ " Assigment = " + Str_finish_assigment
//					+ " Kode Toko = " + Str_finish_kode_toko
//					+ " Username = " + Str_finish_username
//					+ " Locpk = " + Str_finish_locpk
//					+ " Pincode = " + Str_finish_pincode
//					+ " Waybill = " + STR_swaybill
//					+ " Kota = " + Str_finish_locpk);
//
//			STR_lat_long = Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude());
//			Log.d("Debug", "Latlong " + STR_lat_long);
//
//			finish_pincode_ads.setWaktu(STR_date_time);
//			finish_pincode_ads.setAssigment(STR_no_assigment);
//			finish_pincode_ads.setCustomer(STR_kode_toko);
//			finish_pincode_ads.setUsername(username);
//			finish_pincode_ads.setLocpk(Str_finish_locpk);
//			finish_pincode_ads.setPincode(STR_pincode);
//			finish_pincode_ads.setLat_Long(STR_lat_long);
//			finish_pincode_ads.setWaybill(STR_swaybill);
//			finish_pincode_ads.setKota(Str_finish_locpk);
//			Log.d("Debug", "add Pincode Detail ADS Finish -> local");
//			dbFinish = new ListFinishPincodeADSDBAdapter(ListDetailADS.this);
//			dbFinish.open();
//			dbFinish.updateContact(finish_pincode_ads);
//			dbFinish.close();
//			Log.d("PUP", "AWB add to local database ..");
//			listAdapter.clear();
//			mainListView.setAdapter(listAdapter);
		}
	}
	
	public void show_dialog1() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailADS.this);
		builder.setTitle("Action ");
		builder.setItems(new CharSequence[] { "Pickup", "Batal" },

		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {
				case 0:
//					ArrayList<HashMap<String, String>> daftar_list_ads_detail = new ArrayList<HashMap<String, String>>();
//					ArrayList<HashMap<String, String>> list_detail_ads = new ArrayList<HashMap<String, String>>();
//					HashMap<String, String> mapADSDetail = new HashMap<String, String>();
//					Log.e("Debug", "Hashmap >>>" 
//							+" Hashmap 1 "+ daftar_list_ads_detail
//							+" Hashmap 2 "+ list_detail_ads
//							+" Hashmap 3 "+ mapADSDetail);
//					Intent a = new Intent(ListDetailADS.this, multi_pup.class);
					
					String Str_kode_asal_kurir = STR_kode_toko;
					Log.d("Debug", "Kode Asal Kurir " + Str_kode_asal_kurir);
					
					Intent a = new Intent(ListDetailADS.this, multiScanADS.class);
					a.putExtra("asigment", STR_no_assigment);
					a.putExtra("po", STR_assigment);
					a.putExtra("awb", STR_no_awb);
					a.putExtra("kode_toko", STR_kode_toko);
//					a.putExtra("array_list_detail_ads", daftar_list_ads_detail);
//					a.putExtra("array_list_detail_ads_2", list_detail_ads);
//					a.putExtra("array_list_detail_ads_3", mapADSDetail);
//					startActivityForResult(a, 1);
					startActivityForResult(a, 500);
//					finish();
					break;
				case 1:

					break;
				}
			}
		});
		builder.create().show();
	}
	
	public void show_dialog2() {

	}
	
	public void show_dialog5() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailADS.this);
			builder.setTitle("Pincode No.Assigment Berhasil Di Finish Ke Server.");
			builder.setItems(new CharSequence[] { "OK" },
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
//					finish();
				}
			});
			builder.create().show();
	 }
	
	public void show_dialog6() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailADS.this);
		builder.setTitle("Pincode No.Assigment Gagal Di Finish Ke Server, Data Di Simpan Sementara Di Lokal Database.");
		builder.setItems(new CharSequence[] { "OK" },
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	public void show_dialog_detail() {
		String STR_AlamatPickup = "Alamat Pickup";
		String STR_Nama_Toko = STR_NamaToko;
		String STR_alamat_result = STR_alamat;
		String STR_alamat_replace = STR_alamat_result.replace("<br>"," ");
		Log.d("Debug", "Replace <br> --> " + STR_alamat_replace);
		AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailADS.this);
//		builder.setTitle(STR_AlamatPickup);
		builder.setTitle(STR_Nama_Toko);
		builder.setMessage(STR_alamat_replace);
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {   
//                Toast.makeText(getApplicationContext(),"Clicked Cancel!", Toast.LENGTH_SHORT).show();
              return;   
            	}
            });
		builder.create().show();
	}

}
