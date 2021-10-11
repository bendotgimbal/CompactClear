package compact.mobile;


import org.json.JSONArray;


import java.util.Timer;
import java.util.TimerTask;


import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;


public class Pickup extends Activity {
	 Button getgps,po,multipup,pup,runsit,edp,ads,manifes,upload,pickup;
	 TextView showurl;
	 gps_tracker gps;
	 String urlphp, urlnya, username, lat_long,sv;
	 JSONArray version = null;
	 DBAdapter database;
	PosAdapter db,	posdb;
	 WaybillAdapter wbdb;
	 ImageAdapter idb;
	 TransAdapter tsdb;
	 UserAdapter usdb;
	 SessionManager session;
	 TimerTask mTimertask;
	 final Handler handler = new Handler();
	 String myimei  ;
	 
	 Timer t = new Timer();
	 
	 ProgressDialog progressBar;
	 int progressBarStatus = 0;
	 Handler progressBarHandler = new Handler();
	 private HostAdapter hostdb;
	 String	HostUrl;
	 @TargetApi(3)
	public void onCreate(Bundle savedInstanceState) {
		 
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.pickup);

	        Log.d("debug","masuk session = " );
   	   			session = new SessionManager(getApplicationContext());
   		        Log.d("debug","5" );
   		        po=(Button)findViewById(R.id.btnpo);
   		        pup=(Button)findViewById(R.id.btnpup);

   		        ads=(Button)findViewById(R.id.btnads);

   		        showurl=(TextView)findViewById(R.id.showurl);

   		        
		        
   		      po.setOnClickListener(new View.OnClickListener() {
   					public void onClick(View v) {
   						Intent a = new Intent (Pickup.this,ListAsigmentKurir.class);
 						 startActivity(a);
   						
   					}
   				});



   		        
   		      pup.setOnClickListener(new View.OnClickListener() {
   					public void onClick(View v) {
   						Intent a = new Intent (Pickup.this,pup.class);
  						 startActivity(a);

   					}
   				});

   		       
   		        
   		     ads.setOnClickListener(new View.OnClickListener() {
   					public void onClick(View v) {
   						Intent a = new Intent (Pickup.this,ListPickupBooking.class);
 						startActivity(a);
   					}
   				});
   		        

	       
	 }
	 

	
	
}

