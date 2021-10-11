package compact.mobile;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SettingHost extends Activity implements OnClickListener
{
	EditText	inputHost;
	Button		btnAdd;
	HostAdapter	db;

	private static final int REQUEST_WRITE_STORAGE = 112;
	private static final int PERMISSION_REQUEST_CODE = 200;
	TelephonyManager telephonyManager;
	LocationManager locationManager;
	String myimei  ;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sethost);
		Log.d("cek host masuk 2","host ");
		inputHost = (EditText) findViewById(R.id.host);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		String urlnya = "";
		Log.d("Settig Host","sebelum ambil default");
		urlgw url = urlgw.getInstance();
		Log.d("Settig Host","setelah get instance => " + url.getData());
		if (url.getData()!=null){
		urlnya = url.getData();
		urlnya = urlnya.replace("/android/", "");
		}
		
		Log.d("Debug", "|Remove Host URL (1) - /android/ | " + urlnya);
		String urlnya2 = "";
		urlnya2 = urlnya.replace("http://", "");
		Log.d("Debug", "|Remove Host URL (2) - http:// | " + urlnya2);
		
//		inputHost.setText(urlnya);
		inputHost.setText(urlnya2);

		if (checkPermissions()) {
			Log.d("Debug", "if checkPermissions" + " || startApplication");
			startApplication();
			boolean success = true;
			if (success) {
				Log.d("Debug", "success to create directory");
				Log.d("Debug", "Setting Host - 1a success");
				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
				Log.d("Debug", "Setting Host - If");
				String intStorageDirectory = getFilesDir().toString();
				Log.d("Debug", "Dir Storage " + intStorageDirectory);
				File mediaStorageDirFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				File mediaStorageDir = new File(mediaStorageDirFolder + File.separator + "Compact" + File.separator);
				Log.d("Debug", "Media Storage " + mediaStorageDir);

				if (!mediaStorageDir.exists()) {
					if (!mediaStorageDir.mkdirs()) {
						Log.d("Debug", "failed to create directory");
						Log.d("Debug", "Setting Host - 1a");
					} else {
						Log.d("Debug", "success to create directory");
						Log.d("Debug", "Setting Host - 1b");
					}
				}
			} else {
				Log.d("Debug", "failed to create directory");
				Log.d("Debug", "Setting Host - 1b failed");
			}
		}else {
			Log.d("Debug", "else checkPermissions" + " || setPermissions");
			setPermissions();
			boolean success=true;
			if(success){
				Log.d("Debug", "success to create directory");
				Log.d("Debug", "Setting Host - 2a success");
				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
				Log.d("Debug", "Else");
				String intStorageDirectory = getFilesDir().toString();
				Log.d("Debug", "Dir Storage "+intStorageDirectory);
				File mediaStorageDirFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				File mediaStorageDir = new File(mediaStorageDirFolder+ File.separator + "Compact" + File.separator);
				Log.d("Debug", "Media Storage "+mediaStorageDir);

				if (!mediaStorageDir.exists()) {
					if (!mediaStorageDir.mkdirs()) {
						Log.d("Debug", "failed to create directory");
						Log.d("Debug", "Setting Host - 2a");
					} else {
						Log.d("Debug", "success to create directory");
						Log.d("Debug", "Setting Host - 2b");
					}
				}
			}else{
				Log.d("Debug", "failed to create directory");
				Log.d("Debug", "Setting Host - 2b failed");
			}
		}
		
	}

	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
			case R.id.btnAdd:
				HostURL url = new HostURL();
				url.setHost(inputHost.getText().toString());
				url.setUsername("-");
				url.setPassword("-");

				db = new HostAdapter(this);
				db.open();
				db.deleteAllContact();
				db.createContact(url);
				db.close();
				finish();
				startActivity(new Intent(SettingHost.this, MainActivity.class));
				break;
			default:
				break;
		}

	}

	private boolean checkPermissions() {
		int read_phone_state = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
		int access_fine_location = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
		int write_esternal_storage= ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
		int read_esternal_storage= ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
		int camera= ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

		return read_phone_state == PackageManager.PERMISSION_GRANTED && access_fine_location == PackageManager.PERMISSION_GRANTED && write_esternal_storage == PackageManager.PERMISSION_GRANTED && read_esternal_storage == PackageManager.PERMISSION_GRANTED && camera == PackageManager.PERMISSION_GRANTED;
//		return read_phone_state == PackageManager.PERMISSION_GRANTED && access_fine_location == PackageManager.PERMISSION_GRANTED && read_esternal_storage == PackageManager.PERMISSION_GRANTED;
	}

	private void setPermissions() {
		ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE, ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
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
		Location locationPhone = locationManager.getLastKnownLocation(provider);
	}

	public void onBackPressed() {
		super.onBackPressed();
		Log.d("Debug","Kembali Ke Halaman Login");
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Intent a = new Intent(SettingHost.this, MainActivity.class);
		startActivity(a);
	}

}
