package compact.mobile.config;

import java.util.HashMap;

import compact.mobile.MainActivity;
import compact.mobile.menu_utama;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// nama sharepreference
	private static final String PREF_NAME = "Sesi";
	
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	public static final String KEY_NAME_COURIER = "muse_name";
	public static final String KEY_COURIER_ID = "muse_code";
	public static final String KEY_COURIER_LOCPK = "muse_mloc_pk";
	
	public static final String KEY_URL = "url";
    public static final String KEY_USER = "username";
    public static final String KEY_STATUS = "status";
    public static final String KEY_LOCPK = "locpk";
    public static final String KEY_ID_AGEN = "agen";

    public static final String KEY_ID_KURIR = "IdKurir";
    public static final String KEY_ID_FIREBASE = "IdFirebase";

    public static final String KEY_PREF_ID_KURIR = "muse_code";
    public static final String KEY_PREF_KODE_TOKO = "muse_code_store";
    public static final String KEY_PREF_NAMA_TOKO = "muse_nama_toko";
    public static final String KEY_PREF_ALAMAT_TOKO = "muse_alamat_toko";
    public static final String KEY_PREF_LATLONG_TOKO = "muse_latlong_toko";
    public static final String KEY_PREF_MAPI = "muse_mapi";
    public static final String KEY_PREF_WEBVIEW = "muse_webview";
    
 // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
	
	public void createLoginSessionNew(String StrCourierID, String StrCourierLoc, String StrCourierName){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_COURIER_ID,StrCourierID);
		editor.putString(KEY_COURIER_LOCPK, StrCourierLoc);
		editor.putString(KEY_NAME_COURIER, StrCourierName);
		editor.commit();
	}
	
	public void createLoginSession(String user,String url,String status,String locpk){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_URL, url);
        editor.putString(KEY_USER, user);
        editor.putString(KEY_STATUS, status);
        editor.putString(KEY_LOCPK, locpk);
        editor.commit();
    }

    public void createLoginSessionFirebase(String id_kurir,String id_firebase){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID_KURIR, id_kurir);
        editor.putString(KEY_ID_FIREBASE, id_firebase);
        editor.commit();
    }
	
	public void createidagen(String id){
        editor.putString(KEY_ID_AGEN, id);
        editor.commit();
    }
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, menu_utama.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
           // ((Activity)_context).finish();
        }

    }

    public void createKurirStore(String StrIDCourier, String StrCourierCodeStore, String StrCourierNamaToko, String StrCourierAlamatToko, String StrCourierLocToko, String StrMapi, String StrWebview){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_PREF_ID_KURIR,StrIDCourier);
        editor.putString(KEY_PREF_KODE_TOKO,StrCourierCodeStore);
        editor.putString(KEY_PREF_NAMA_TOKO, StrCourierNamaToko);
        editor.putString(KEY_PREF_ALAMAT_TOKO, StrCourierAlamatToko);
        editor.putString(KEY_PREF_LATLONG_TOKO, StrCourierLocToko);
        editor.putString(KEY_PREF_MAPI, StrMapi);
        editor.putString(KEY_PREF_WEBVIEW, StrWebview);
        editor.commit();
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_URL, pref.getString(KEY_URL, null));
         user.put(KEY_USER, pref.getString(KEY_USER, null));
        user.put(KEY_STATUS, pref.getString(KEY_STATUS, null));
        user.put(KEY_LOCPK, pref.getString(KEY_LOCPK, null));
        user.put(KEY_ID_AGEN, pref.getString(KEY_ID_AGEN, null));
        return user;
    }

    public HashMap<String, String> getUserIDFirebaseDetails(){
        HashMap<String, String> UserIdFirebase = new HashMap<String, String>();

        UserIdFirebase.put(KEY_ID_KURIR, pref.getString(KEY_ID_KURIR, null));
        UserIdFirebase.put(KEY_ID_FIREBASE, pref.getString(KEY_ID_FIREBASE, null));
        return UserIdFirebase;
    }

    public HashMap<String, String> getUserDetailsStore(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_PREF_ID_KURIR, pref.getString(KEY_PREF_ID_KURIR, null));
        user.put(KEY_PREF_KODE_TOKO, pref.getString(KEY_PREF_KODE_TOKO, null));
        user.put(KEY_PREF_NAMA_TOKO, pref.getString(KEY_PREF_NAMA_TOKO, null));
        user.put(KEY_PREF_ALAMAT_TOKO, pref.getString(KEY_PREF_ALAMAT_TOKO, null));
        user.put(KEY_PREF_LATLONG_TOKO, pref.getString(KEY_PREF_LATLONG_TOKO, null));
        user.put(KEY_PREF_MAPI, pref.getString(KEY_PREF_MAPI, null));
        user.put(KEY_PREF_WEBVIEW, pref.getString(KEY_PREF_WEBVIEW, null));
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public void logoutUser2(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
