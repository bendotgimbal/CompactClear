package compact.mobile;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;

public class setup_menu extends Activity {
	 Button getgps,host,data,upl,menu,logout,keluar;
	 TextView showgps,showurl;
	 gps_tracker gps;
	 String urlphp, urlnya, username, lat_long;
	 PosAdapter	 posdb;
	 UserAdapter	usdb;
	 TransAdapter	tsdb;
	 ImageAdapter idb;
	 WaybillAdapter wbdb;
	 
	// TimerTask mTimertask;
	// final Handler handler = new Handler();
	 String myimei ;
	// Timer t = new Timer();

	 ProgressDialog progressBar;
	 int progressBarStatus = 0;
	 Handler progressBarHandler = new Handler();
	 
	 
	 
	 public void onCreate(Bundle savedInstanceState) {
		 	super.onCreate(savedInstanceState);
	        setContentView(R.layout.setup_menu);
	        
	        usdb = new UserAdapter(this);
			usdb.open();
			tsdb = new TransAdapter(this);
			tsdb.open();
			
	        UserName S1 = UserName.getInstance(); 
   	  		username  = S1.getData();
   	  		
   	  		urlnya = "";
  	 		urlgw url = urlgw.getInstance();
  	 		tipekoneksi conn = tipekoneksi.getInstance();
  	 		urlphp = "";
  	 		if (url.getData()!=null){
  	 			urlphp = url.getData();
  	 			urlnya = urlphp.replace("/android/", "");
  	 		}
  	 		
  	 		
	        getgps=(Button)findViewById(R.id.getgps);
	        host=(Button)findViewById(R.id.btnhost);
	        data=(Button)findViewById(R.id.btndata);
	        upl=(Button)findViewById(R.id.btnupl);
	        menu=(Button)findViewById(R.id.btnmenu);
	        logout=(Button)findViewById(R.id.btnlogout);
	        keluar=(Button)findViewById(R.id.btnexit);
	        showgps=(TextView)findViewById(R.id.showgps);
	        showurl=(TextView)findViewById(R.id.showurl);
	        
	        if (conn.getData().equals("Online")){
	        	showurl.setText(urlnya);
	        }
	        else
	        {
	        	showurl.setText("Local Database");
	        }	

	        
	        keluar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					 finish();
				}
			});
	        
	        getgps.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					gps = new gps_tracker(setup_menu.this);
					if(gps.canGetLocation()){
					
						showgps.setText(Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude()));
						String CURRENT_LOCATION = Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude());
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse("http://maps.google.co.id/maps?q="+ CURRENT_LOCATION ));
						startActivity(intent);
					}
					else
					{
						gps.showSettingsAlert();
					}
					
				}
			});
	        
	        logout.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					 finish();
					 Intent a = new Intent (setup_menu.this,MainActivity.class);
					 startActivity(a);
				}
			});

	        
	        
	        host.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					 finish();
					 Intent a = new Intent (setup_menu.this,SettingHost.class);
					 startActivity(a);
				}
			});
	        
	        menu.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					 finish();
					 Intent a = new Intent (setup_menu.this,menu_utama.class);
					 startActivity(a);
				}
			});
	        
	        upl.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO bikin unggah data
					upload_data();
				}
			});
	        
	        data.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					//tarik_data_user(); 
					//tarik_data_waybill();
					tarik_data();
					Toast.makeText(v.getContext(), "Download data successfull..", Toast.LENGTH_SHORT).show();
				}
			});
	 }
	 
	 @TargetApi(3)
	 private void tarik_data(){
		 Log.d("Progress Bar","Prepare.."); 
			progressBar = new ProgressDialog(this);
			progressBar.setCancelable(true);
			progressBar.setMessage("Downloading database ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			 if (urlphp.equals("")){
				 Intent a = new Intent (setup_menu.this,SettingHost.class);
				 startActivity(a);
				 return;
			 }
 			 
			 //	ambil data user
			 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			 String response = null;
		 		try {
		 			Log.d("Progress Bar","getting user data");
		 			response = CustomHttpClient.executeHttpPost(urlphp + "get_data_user.php",postParameters);
		 			Log.e("hasil respon", response);
		 			if (response == null) {
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
			 				final String res = response;
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
												User usr = new User();
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

		 		Log.d("Progress Bar","Near launch");
				 
			   	 long yourDateMillis = System.currentTimeMillis() - (14 * 24 * 60 * 60 * 1000);
			   	 Time yourDate = new Time();
			   	 yourDate.set(yourDateMillis);
			   	 String sTgl = yourDate.format("%Y-%m-%d");
			   	 Log.d("Tanggal","set date to : " + sTgl);
			   	 
				 ArrayList<NameValuePair> newParameters = new ArrayList<NameValuePair>();
				 newParameters.add(new BasicNameValuePair("tgl", sTgl));
				    response = null;
			 		try {
			 			Log.d("Progress Bar","get data waybill");
			 			response = CustomHttpClient.executeHttpPost(urlphp + "get_data.php",newParameters);
			 			Log.e("hasil respon", response);
			 			if (response == null) {
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
				 				final String res = response;
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
	 
	 private void upload_data(){
			
			posdb = new PosAdapter(setup_menu.this);
			Log.d("Progress Bar","Prepare data user .."); 
			progressBar = new ProgressDialog(this);
			progressBar.setCancelable(true);
			progressBar.setMessage("Upload GPS database ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			 if (urlphp.equals("")){
				 Intent a = new Intent (setup_menu.this,SettingHost.class);
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
			
			wbdb = new WaybillAdapter(setup_menu.this);
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
				idb = new ImageAdapter(setup_menu.this);
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
						              Toast.makeText(setup_menu.this, "Fail to connect server, using local -> " +e.toString() + " -> " + e.getMessage(), Toast.LENGTH_LONG).show();
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
	 private void tarik_data_user(){
		
		Log.d("Progress Bar","Prepare.."); 
		progressBar = new ProgressDialog(this);
		progressBar.setCancelable(true);
		progressBar.setMessage("Downloading UserKurirFirebase data ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBarStatus = 0;
		 if (urlphp.equals("")){
			 Intent a = new Intent (setup_menu.this,SettingHost.class);
			 startActivity(a);
			 return;
		 }
		 Log.d("Progress Bar","Near launch");
		 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		 String response = null;
	 		try {
	 			Log.d("Progress Bar","connectiong to server");
	 			response = CustomHttpClient.executeHttpPost(urlphp + "get_data_user.php",postParameters);
	 			Log.e("hasil respon", response);
	 			if (response == null) {
	 				Toast.makeText(this.getBaseContext(), "Data user tidak ada !!", Toast.LENGTH_SHORT).show();
	     			return;
	 			}
	 			usdb.deleteAllContact();
	 			
	 			Log.d("Progress Bar","cacthed you ... !!");
	 			try
	 			{
					Log.d("Progress Bar","show progressbar");
					progressBar.show();

	 				final String res = response;
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
										User usr = new User();
						    			usr.setUsername(json.getString("kode"));
						    			usr.setPassword(json.getString("passwd"));
						    			usr.setLocation(json.getString("lokasi"));
						    			usdb.createContact(usr);
						    			Log.e("input data", json.getString("kode") + ", " + json.getString("passwd") + "," + json.getString("lokasi"));
						    			progressBarStatus = (int) ((i/j)*100);
						    			Log.e("Progress bar","status -> (" + i +" / " + j + ") * 100 = "+ (int) ((i/j)*100) +" = " +progressBarStatus ); 
						    			progressBarHandler.post(new Runnable() {
											public void run() {
											  progressBar.setProgress(progressBarStatus);
											}
										  });
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
			 	catch (Exception e) {
		 			Log.e("Fail 4", e.toString());
		 			Toast.makeText(this.getBaseContext(), "Error = " + e.toString(), Toast.LENGTH_SHORT).show();
		 			usdb.close();
			 	}
	 }
	 
	 @TargetApi(3)
	private void tarik_data_waybill(){
			
			Log.d("Progress Bar","Prepare.."); 
			progressBar = new ProgressDialog(this);
			progressBar.setCancelable(true);
			progressBar.setMessage("Downloading waybill data..");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.setProgress(0);
			progressBar.setMax(100);
			progressBarStatus = 0;
			 if (urlphp.equals("")){
				 Intent a = new Intent (setup_menu.this,SettingHost.class);
				 startActivity(a);
				 return;
			 }
			 Log.d("Progress Bar","Near launch");
			 
		   	 long yourDateMillis = System.currentTimeMillis() - (14 * 24 * 60 * 60 * 1000);
		   	 Time yourDate = new Time();
		   	 yourDate.set(yourDateMillis);
		   	 String sTgl = yourDate.format("%Y-%m-%d");
		   	 Log.d("Tanggal","set date to : " + sTgl);
		   	 
			 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			 postParameters.add(new BasicNameValuePair("tgl", sTgl));
			 String response = null;
		 		try {
		 			Log.d("Progress Bar","connectiong to server");
		 			response = CustomHttpClient.executeHttpPost(urlphp + "get_data.php",postParameters);
		 			Log.e("hasil respon", response);
		 			if (response == null) {
		 				Toast.makeText(this.getBaseContext(), "Data EDP tidak ada !!", Toast.LENGTH_SHORT).show();
		     			return;
		 			}
		 			tsdb.deleteAllContact();
		 			
		 			Log.d("Progress Bar","cacthed you ... !!");
		 			try
		 			{
						Log.d("Progress Bar","show progressbar");
						progressBar.show();

		 				final String res = response;
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
							    			progressBarHandler.post(new Runnable() {
												public void run() {
												  progressBar.setProgress(progressBarStatus);
												}
											  });
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
				 	catch (Exception e) {
			 			Log.e("Fail 4", e.toString());
			 			Toast.makeText(this.getBaseContext(), "Error = " + e.toString(), Toast.LENGTH_SHORT).show();
			 			tsdb.close();
				 	}
		 }	 
	 
}
