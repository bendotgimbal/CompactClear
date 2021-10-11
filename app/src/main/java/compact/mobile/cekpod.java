package compact.mobile;


import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.widget.ArrayAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class cekpod extends Activity {
	 TextView  error;
	 ImageView foto;
	 EditText ewaybill, epenerima,eketerangan, etelp;
	 Button proses,reset,keluar,scan,bfoto;
	 Spinner spin ;
	 gps_tracker gps;
	 WaybillAdapter	db;
	 ImageAdapter	idb;
	 TransAdapter	tsdb;
	 private static final int CAMERA_REQUEST = 1888;
	 JSONArray total_scd = null;
	 JSONArray limit_scd = null;
	 private  ProgressDialog progressBar;
	 public String nilai,nilai_limit,tfoto="";
	 
	 public String locpk,username, mswb_pk, origin, destination, tppenerima;
	 String[] pilihan = { "PENERIMA DI WAYBILL / BERSANGKUTAN",
			 "KELUARGA YG TINGGAL SERUMAH",
			 "PEMBANTU",
			 "SECURITY",
			 "RECEPTIONIST",
			 "MAIL ROOM",
			 "SECRETARY",
			 "STAFF",
			 "ON BEHALF/ ATAS NAMA " };
	 String org,dest,urlphp, awb, pen, nomor;
	 Integer cek_koneksi = 1;
	 SessionManager session;
    @Override
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cekpod);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
 	        username = user.get(SessionManager.KEY_USER);
 	        urlphp = user.get(SessionManager.KEY_URL);
 	        locpk = user.get(SessionManager.KEY_LOCPK);
       
 	        
        ewaybill=(EditText)findViewById(R.id.waybill);
        epenerima=(EditText)findViewById(R.id.penerima);
        etelp=(EditText)findViewById(R.id.telp);
        eketerangan=(EditText)findViewById(R.id.keterangan);
        error = (TextView)findViewById(R.id.error);   
        bfoto = (Button)findViewById(R.id.foto);
        foto = (ImageView)findViewById(R.id.image);
        proses=(Button)findViewById(R.id.btnProses);
        keluar=(Button)findViewById(R.id.btnKeluar);
        reset=(Button)findViewById(R.id.btnBersih);
        scan=(Button)findViewById(R.id.btnscan);
	       HandleClick hc = new HandleClick();
	       scan.setOnClickListener(hc);
        spin = (Spinner)findViewById(R.id.spinner);
     
        
		
	       noWaybill obnomor = noWaybill.getInstance();
	       nomor = obnomor.getData();
		
		if (nomor.equals("")) {
			ewaybill.setText("") ;
			}
		else
	       {
	    	   
			ewaybill.setText(nomor) ;   
	       }

		
        
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, pilihan);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        
        spin .setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        bfoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});
        reset.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ewaybill.setText("");
				epenerima.setText("");
				etelp.setText("");
    			org = "";
    			dest = "";
			}
		});
        
        keluar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				main_menu(v);
			}
		});
        
        proses.setOnClickListener(new View.OnClickListener() {
        	
        	public void onClick(View v) {
           		progressBar = new ProgressDialog(cekpod.this);
    		    progressBar.setCancelable(false);
    		    progressBar.setMessage("Loading...");
    		    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);;
    		    progressBar.show();
    		    if (ewaybill.getText().toString().trim().length() == 0) {
        			Toast.makeText(v.getContext(), "Waybill tidak boleh kosong.", Toast.LENGTH_SHORT).show();
        		}else if (epenerima.getText().toString().trim().length() == 0) {
        			Toast.makeText(v.getContext(), "Penerima tidak boleh kosong.", Toast.LENGTH_SHORT).show();
        		}else if(etelp.getText().toString().length() == 0){
        			Toast.makeText(v.getContext(), "No Telp Harus Di isi", Toast.LENGTH_SHORT).show();
        		}else if(eketerangan.getText().toString().length() == 0){
        			Toast.makeText(v.getContext(), "Remark Harus Di isi", Toast.LENGTH_SHORT).show();
        		}else if(tfoto.equals("")){
        			Toast.makeText(v.getContext(), "Upload Foto POD", Toast.LENGTH_SHORT).show();
        		}
        		else  {
        			
		    		tpPenerima S2 = tpPenerima.getInstance(); 
		    		tppenerima  = S2.getData(); 
		    		gps = new gps_tracker(cekpod.this);
		    		
		    		String[] sWB;
	       			String sTemp1, sTemp2;
	       			if (ewaybill.getText().toString().trim().indexOf("-") > 0)
	       			{
	       				sWB = ewaybill.getText().toString().trim().split("-");
	       				sTemp1 = (sWB[0]+"0000").substring(0,4);
	       				sTemp2 = "000000000000"+sWB[1]; 
	       				sTemp2 = sTemp2.substring(sTemp2.length() - 9);
	       				ewaybill.setText(sTemp1 + sTemp2);
	       				Log.d("POD","waybill : "+ewaybill.getText().toString().trim());
	       			}
		    		
        			ArrayList<NameValuePair> masukparam1 = new ArrayList<NameValuePair>();
        			masukparam1.add(new BasicNameValuePair("waybill", ewaybill.getText().toString()));
        			masukparam1.add(new BasicNameValuePair("penerima", epenerima.getText().toString()));
        			masukparam1.add(new BasicNameValuePair("kota", org));
        			masukparam1.add(new BasicNameValuePair("tujuan", dest));
        			masukparam1.add(new BasicNameValuePair("mswb_pk", mswb_pk));
        			masukparam1.add(new BasicNameValuePair("locpk", locpk));
        			masukparam1.add(new BasicNameValuePair("username", username));
        			masukparam1.add(new BasicNameValuePair("tiperem", tppenerima));
        			masukparam1.add(new BasicNameValuePair("telp", etelp.getText().toString()));
        			masukparam1.add(new BasicNameValuePair("tipe", "7"));
        			masukparam1.add(new BasicNameValuePair("keterangan", eketerangan.getText().toString()));
        			masukparam1.add(new BasicNameValuePair("lat_long",Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude())));
        			masukparam1.add(new BasicNameValuePair("waktu",""));
        			masukparam1.add(new BasicNameValuePair("foto",tfoto));
        			Log.d("cek parameter", "waybill =>" + ewaybill.getText().toString() + ", penerima => " + epenerima.getText().toString() + ", kota => " + org +
        					"tujuan => "+dest + ", mswb_pk => " +mswb_pk + ",locpk => "+locpk + ",username =>" + username +", tiperem => "+tppenerima + ", telp => " + etelp.getText().toString()
        					+ ",tipe => 7 , keterangan => " + eketerangan.getText().toString() + ",lat_long => " + Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude()) +
        					", waktu=>  ");
        			awb = ewaybill.getText().toString();
        			pen = epenerima.getText().toString();
        			String response = null;
        			try {
        				response = CustomHttpClient.executeHttpPost(urlphp +   "update.php", masukparam1);
        				String res = response.toString();
        				res = res.trim();
        				res = res.replaceAll("\\s+","");
        				Log.d("respon",res);
        				//error.setText(res);
        				if (res.equals("f")) {
		       				 Toast.makeText(v.getContext(), "Waybill tidak terdaftar !!", Toast.LENGTH_SHORT).show();
		       				reset();
							 //enotifier.setText(masukparam + "::" + res);
		       			}if (res.equals("f2")) {
		       				Toast.makeText(v.getContext(), "Waybill Sudah POD !!" , Toast.LENGTH_SHORT).show();
		       				reset();
		       			}else {
		       				
		       				upload_image(tfoto, "7",ewaybill.getText().toString(), epenerima.getText().toString());
		       				//Toast.makeText(v.getContext(), "Scan POD Success " , Toast.LENGTH_SHORT).show();
		       				reset();
		       			}
        				//show_dialog(v);
        				
        				// Intent intent = new Intent(cekpod.this, CaptureSignature.class);
    	                // startActivityForResult(intent,1);
          
        				}
       		 		catch (Exception e) {
       		 			Toast.makeText(v.getContext(), "Can't connect to server, Save to local database ", Toast.LENGTH_SHORT).show();
       		 			savetolocal();
       		 			saveImage(tfoto,"7"+ewaybill.getText().toString());
       		 			//Intent intent = new Intent(cekpod.this, CaptureSignature.class);
       		 			//startActivityForResult(intent,1);
       		 		reset();
	                 
       		 			}
        			
        			ewaybill.setText("");
        			epenerima.setText("");
        			etelp.setText("");
        			eketerangan.setText("");
        			org = "";
        			dest = "";
        					
        		}
        		progressBar.dismiss();
        	}
        });
        
        cek_total();
        
        if(cek_koneksi == 0){
        	offline();
        }else{
        	cek_scd();
        }
        
        
        }
    public void reset(){
    	 foto.setImageResource(R.drawable.no_image);
    	 tfoto = "";
    }
    
    public void cek_total(){
    	
    	JSONParser jParser = new JSONParser();
    	
    	ArrayList<NameValuePair> paramtes = new ArrayList<NameValuePair>();
		paramtes.add(new BasicNameValuePair("tes", "ok"));
  		String cek = null;
  		try {
  			
  			//cek koneksi internet
  			cek = CustomHttpClient.executeHttpPost(urlphp + "atex_get_total_scd.php", paramtes);
  			
			JSONObject json = jParser.getJSONFromUrl(urlphp + "atex_get_total_scd.php?kode_kurir=" + username);
			String cek_result = json.getString("code");
			Log.d("tes respone code", cek_result);
			if (cek_result.equals("1")){
				//String data = json.getString("data");
				//Log.d("respone data", data);
				total_scd = json.getJSONArray("data");

				for(int i = 0; i < total_scd.length(); i++){
					JSONObject ar = total_scd.getJSONObject(i);
					nilai = ar.getString("total");
					nilai_limit = ar.getString("limit");
					Log.d("tes respone nilai", nilai);
					Log.d("tes respone nilai_limit", nilai_limit);
				}
			}

			
		} catch (Exception e) {
			Log.d("offline","tidak bisa cek total");
			cek_koneksi = 0;
		}
    	
    }
    

    
    public void cek_scd(){
    	
    	
    	int a = 0;
    	int b = 0;
    	
    	a = Integer.parseInt(nilai);
    	b = Integer.parseInt(nilai_limit);
    	
    	if (a <= b){
    		
    	}else{
    		
    		warning();
    		
    	}
    	
    }
    
    public void warning(){
    	
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(
    	        cekpod.this);
    	
    	alertDialog.setCancelable(false);
    	 
    	// Setting Dialog Title
    	alertDialog.setTitle("Warning");
    	 
    	// Setting Dialog Message
    	alertDialog.setMessage("Setoran COD telah mencapai limit !");
    	 
    	// Setting Positive "Yes" Btn
    	alertDialog.setPositiveButton("Close",
    	        new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	            	finish();
    	            }
    	        });
    	
    	alertDialog.show();
    	
    }
    public void offline(){
    	
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(
    	        cekpod.this);
    	
    	alertDialog.setCancelable(false);
    	 
    	// Setting Dialog Title
    	alertDialog.setTitle("Warning");
    	 
    	// Setting Dialog Message
    	alertDialog.setMessage("POD harus dalam keadaan Online !");
    	 
    	// Setting Positive "Yes" Btn
    	alertDialog.setPositiveButton("Close",
    	        new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	            	finish();
    	            }
    	        });
    	
    	alertDialog.show();
    	
    }
    public void sukses(String msg){
    	
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(
    	        cekpod.this);
    	
    	alertDialog.setCancelable(false);
    	 
    	// Setting Dialog Title
    	alertDialog.setTitle("Warning");
    	 
    	// Setting Dialog Message
    	alertDialog.setMessage(msg);
    	 
    	// Setting Positive "Yes" Btn
    	alertDialog.setPositiveButton("OK",
    	        new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	            	
    	            }
    	        });
    	
    	alertDialog.show();
    	
    }
    
    @TargetApi(3)
  	private void savetolocal(){
  		 
  		 Log.d("POD","prepare for POD -> local" );
    		Waybill waybill = new Waybill();
   		Time now = new Time(Time.getCurrentTimezone());
   		now.setToNow();
   		String sTgl =  Integer.toString(now.year) + "-" + ("00" + Integer.toString(now.month + 1)).substring(("00" + Integer.toString(now.month + 1)).length() - 2) + "-" + ("00" + Integer.toString(now.monthDay)).substring(("00" + Integer.toString(now.monthDay)).length() - 2) + " " + 
   				("00" + Integer.toString(now.hour)).substring(("00" + Integer.toString(now.hour)).length() - 2)  + ":" + ("00" + Integer.toString(now.minute)).substring(("00" + Integer.toString(now.minute)).length() - 2) + ":" + ("00" + Integer.toString(now.second)).substring(("00" + Integer.toString(now.second)).length() - 2);
  			waybill.setWaybill(ewaybill.getText().toString());
  			waybill.setPenerima("");
  			waybill.setKota(org);
  			waybill.setTujuan(dest);
  			waybill.setMswb_pk(mswb_pk);
  			waybill.setLocpk(locpk);
  			waybill.setUser(username);
  			waybill.setTiperem(tppenerima);
  			waybill.setTelp(etelp.getText().toString());
  			waybill.setTipe("7");
  			waybill.setKeterangan(eketerangan.getText().toString());
  			waybill.setLat_Long(Double.toString(gps.getLatitude()) + ", " + Double.toString(gps.getLongitude()));
  			waybill.setWaktu(sTgl);
  			waybill.setStatus("0");
  			waybill.setPo("");
  			Log.d("POD","add POD -> local" );
  			db = new WaybillAdapter(cekpod.this);
  			db.open();
  			db.createContact(waybill);
  			db.close();
  			Log.d("POD","POD add to local database ..");
  			
  	 }
    
    public void show_dialog(View theButton){
    	
    	AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
    	        cekpod.this);
    	 
    	// Setting Dialog Title
    	alertDialog2.setTitle("Proof of Delivery");
    	 
    	// Setting Dialog Message
    	alertDialog2.setMessage("Choose POD verification with :");
    	 
    	// Setting Icon to Dialog
    	//alertDialog2.setIcon(R.drawable.delete);
    	 
    	// Setting Positive "Yes" Btn
    	alertDialog2.setPositiveButton("Signature",
    	        new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	   //         	 Intent intent = new Intent(cekpod.this, CaptureSignature.class);
    	    //             startActivityForResult(intent,1);
    	            }
    	        });
    	// Setting Negative "NO" Btn
    	alertDialog2.setNegativeButton("Photo",
    	        new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	                // Write your code here to execute after dialog
    	                //Toast.makeText(getApplicationContext(),"You clicked on photo ...", Toast.LENGTH_SHORT).show();
    	                //dialog.cancel();
    	            	
    	            	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    	                startActivityForResult(intent, 2);
    	            }
    	        });
    	alertDialog2.show();
    	
    };
    
    public void ambil_data(View theButton) {
    	ArrayList<NameValuePair> masukparam = new ArrayList<NameValuePair>();

		Log.d("ambil data","try konek server => " + ewaybill.getText().toString());
		masukparam.add(new BasicNameValuePair("waybill", ewaybill.getText().toString()));
    	masukparam.add(new BasicNameValuePair("locpk", locpk));
    	masukparam.add(new BasicNameValuePair("kurir", username));
    	masukparam.add(new BasicNameValuePair("tipe", "pod"));
    	
		String response = null;
		try {
			response = CustomHttpClient.executeHttpPost(urlphp + "cek_waybill.php", masukparam);
			String res = response.toString();
			//HttpEntity entity = response.getEntity();
			//String res = EntityUtils.toString(entity);
			
			Log.d("cek waybill","respon = " + res + " - " + response.toString());
			res = res.trim();
			res = res.replaceAll("\\s+","");
			if (res.equals("2"))
			 {
				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromUrl(urlphp + "get_data.php?waybill=" + ewaybill.getText().toString());
				try {
		    		String customer = json.getString("customer");
		    		origin = json.getString("origin");
		    		destination = json.getString("destination");
		    		String shipper = json.getString("shipper");
		    		String consignee = json.getString("consignee");
		    		String content = json.getString("content");
		    		String qty = json.getString("qty");
		    		String kg = json.getString("kg");
		    		mswb_pk  = json.getString("mswb_pk");
		    		org = json.getString("org");
		    		dest = json.getString("dest");
     				
     						    				    		
					epenerima.setText("");
					etelp.setText("");
					eketerangan.setText("");
		    		
		    	} catch (JSONException e) {
		    		e.printStackTrace();
		    	}
			 }
			 else if (res.equals("1")) {
				 Log.d("cek waybill",res + "waybill tidak ada di DR hari ini");
				 Toast.makeText(theButton.getContext(), "Waybill tidak ada di DR hari ini !! ["+res+"]", Toast.LENGTH_SHORT).show();
				 //ewaybill.setText(masukparam + "::" + res);
			 }
			 else if (res.equals("3")) {
				 Log.d("cek waybill",res + "waybill sudah di Void atau POID");
				 Toast.makeText(theButton.getContext(), "Waybill sudah di Void atau POD !! ["+res+"]", Toast.LENGTH_SHORT).show();
				 //ewaybill.setText(masukparam + "::" + res);
			 }
			 else if (res.equals("4")) {
				 Log.d("cek waybill",res + "waybill sudah DEX hari ini");
				 Toast.makeText(theButton.getContext(), "Waybill sudah DEX hari ini !! ["+res+"]", Toast.LENGTH_SHORT).show();
				 //ewaybill.setText(masukparam + "::" + res);
			 }
			 else if (res.equals("5")) {
					 Log.d("cek waybill",res + "waybill sudah di Void atau POID");
					 Toast.makeText(theButton.getContext(), "Waybill sudah di Void atau POD !! ["+res+"]", Toast.LENGTH_SHORT).show();
					 //ewaybill.setText(masukparam + "::" + res);
			 }
			 else if (res.equals("6")) {
				 Log.d("cek waybill",res + "waybill sudah DEX hari ini");
				 Toast.makeText(theButton.getContext(), "Waybill sudah DEX hari ini !! ["+res+"]", Toast.LENGTH_SHORT).show();
				 //ewaybill.setText(masukparam + "::" + res);
			 }

			 else if (res.equals("0")) {
				 Log.d("cek waybill",res + "waybill belum di EDP");
				 Toast.makeText(theButton.getContext(), "Waybill Belum di EDP !! ["+res+"]", Toast.LENGTH_SHORT).show();
				 //ewaybill.setText(masukparam + "::" + res);
			 }					      			
			}
	 	catch (Exception e) {
		 		Toast.makeText(theButton.getContext(), "Connection fail, using local database -> " + e.toString(), Toast.LENGTH_SHORT).show();
				// ewaybill.setText(e.toString());
		 		Log.d("debug","koneksi ngaco, coba ambil lokal data " );
		 		tsdb = new TransAdapter(cekpod.this);
   		 		tsdb.open();
	   		 	Cursor cur = tsdb.getWaybill(ewaybill.getText().toString());
	   			if (cur != null)  {
	   				cur.moveToFirst();
	   				if (cur.getCount() > 0) {
	   					
						epenerima.setText("");
						etelp.setText("");
						eketerangan.setText("");
						mswb_pk  = cur.getString(cur.getColumnIndexOrThrow(TransAdapter.MSWB_PK));
			    		org = cur.getString(cur.getColumnIndexOrThrow(TransAdapter.ORG));
			    		dest = cur.getString(cur.getColumnIndexOrThrow(TransAdapter.DEST));
			    		origin = cur.getString(cur.getColumnIndexOrThrow(TransAdapter.ORIGIN));
			    		destination = cur.getString(cur.getColumnIndexOrThrow(TransAdapter.DESTINATION));
	   				}
       				else
       				{
       					Toast.makeText(this.getBaseContext(), "Waybill tidak ditemukan !", Toast.LENGTH_SHORT).show();
       					Log.d("data local","waybill tidak ditemukan");
       				}
       			}
       			else
       			{
       				Toast.makeText(this.getBaseContext(),  "Waybill tidak ditemukan !", Toast.LENGTH_SHORT).show();	
       				Log.d("data local","Tidak ada data");
       			}
   		 		tsdb.close();
	 		}
    	
    }

    public void main_menu (View theButton)
    {
    finish();
    //Intent a = new Intent (cekpod.this,main_menu.class);
    //startActivity(a);
    }
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
	/*
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	switch(requestCode){
	    	case 0 : 
	    	      if (resultCode == RESULT_OK) {
	    	    	  Log.d("Case 0", "foto");
	    	        //tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
	    	        ewaybill.setText(intent.getStringExtra("SCAN_RESULT"));
	    	      } else if (resultCode == RESULT_CANCELED) {
	
	    	      }
	    	      break;
	    	case 1 :
	    	
	            if (resultCode == RESULT_OK) {
	            	Log.d("Case 1", "foto");
	                Bundle bundle = intent.getExtras();
	                String status  = bundle.getString("status");
	                if(status.equalsIgnoreCase("done")){
	                    Toast toast = Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT);
	                    toast.setGravity(Gravity.TOP, 105, 50);
	                    toast.show();
	                    ttd myttd = ttd.getInstance();
	                    String image_str = myttd.getData();
	                    upload_image(image_str,"signpod");
	                }
	                Intent intent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	                startActivityForResult(intent2, 2);
	            }
	 
	            break;
	    	case 2 :
	    		if (resultCode == RESULT_OK) {
	    			Log.d("Case 2", "foto");
	    			Bitmap photo = (Bitmap) intent.getExtras().get("data"); 
	    	        foto.setImageBitmap(photo);
	    	        
	    	        ByteArrayOutputStream bao = new ByteArrayOutputStream();
	    	        
	    	        photo.compress(Bitmap.CompressFormat.PNG, 90, bao);
	    	        byte [] byte_arr = bao.toByteArray();
	    	        String image_encode = Base64.encodeBytes(byte_arr);
	    			
	    			//set image hasil encode
	    	        tfoto = image_encode;
	    		}
	    		break;
    	}
	  }   
    	*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
    	 if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
            	ewaybill.setText(data.getStringExtra("SCAN_RESULT"));
	  	      } else if (resultCode == RESULT_CANCELED) {
	
	  	      }
            
        }else{
        	if (resultCode == RESULT_OK) {
        	Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            foto.setImageBitmap(photo);
            
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            
            photo.compress(Bitmap.CompressFormat.JPEG, 50, bao);
            byte [] byte_arr = bao.toByteArray();
            String image_encode = Base64.encodeBytes(byte_arr);
            Integer pnj = image_encode.trim().length();
	        Log.d("panjang foto", String.valueOf(pnj));
    		//set image hasil encode
            tfoto = image_encode;
        	}
        }
    }
   
  private void upload_image(String str, String tipe,String awb,String pen) {
	  try {
          ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
          nameValuePairs.add(new BasicNameValuePair("image",str));
          nameValuePairs.add(new BasicNameValuePair("name",tipe+awb));
          nameValuePairs.add(new BasicNameValuePair("penerima",pen));

          String response = null;
          try{
              response = CustomHttpClient.executeHttpPost(urlphp + "upload_image.php", nameValuePairs);
 				String res = response.toString();
 				res = res.trim();
 				res = res.replaceAll("\\s+","");
 				//String the_string_response = convertResponseToString(response);
 				sukses("Scan POD sukses, "+res);
              //Toast.makeText(cekpod.this, "Response " + res, Toast.LENGTH_LONG).show();
          }catch(Exception e){
                Toast.makeText(cekpod.this, "ERROR " +e.toString() + " -> " + e.getMessage(), Toast.LENGTH_LONG).show();
                //System.out.println("Error in http connection "+e.toString());
                saveImage(str, tipe);
          } 
      } catch (Exception e) {
          e.printStackTrace();
      }
  }  
  
  @TargetApi(3)
  private void saveImage(String str, String tipe) {
  	 
  	 	Log.d("IMG","prepare for Image -> local" );
  		gambar gbr = new gambar();
  		gbr.setImage(str);
  		gbr.setName(tipe);
  		gbr.setPenerima(pen);
  		gbr.setStatus("0");
  		Log.d("IMG","add IMG -> local" );
  		idb = new ImageAdapter(cekpod.this);
  		idb.open();
  		idb.createBitmap(gbr);
  		idb.close();
  		Log.d("IMG","IMG add to local database ..");
   }

 

}
