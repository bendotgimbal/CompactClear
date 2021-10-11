package compact.mobile.SuratJalan.DB;

import compact.mobile.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ListFinishPincodeADSDBAdapter {
	public static final String	TABLE_NAME	= "finish_pincode_ads";
	public static final String DB_NO_ASSIGMENT = "assigment";
	public static final String DB_CUSTOMER= "customer";
	public static final String DB_USERNAME = "username";
	public static final String DB_LOCPK = "locpk";
	public static final String DB_PINCODE = "pincode";
	public static final String DB_LAT_LONG = "lat_long";
	public static final String DB_WAKTU = "waktu";
	public static final String DB_WAYBILL = "waybill";
	public static final String DB_KOTA = "kota";
	
	private DatabaseHelper		dbHelper;
	private SQLiteDatabase		db;

	private final Context		context;
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		public DatabaseHelper(Context context)
		{
			// TODO Auto-generated constructor stub
			super(context, DBAdapter.DB_NAME, null, DBAdapter.DB_VER);
		}

		@Override
		public void onCreate(SQLiteDatabase arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public ListFinishPincodeADSDBAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public ListFinishPincodeADSDBAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public void createContact(C_Pincode_ADS finish_pincode_ads)
	{
		ContentValues val = new ContentValues();
		val.put(DB_NO_ASSIGMENT, finish_pincode_ads.getAssigment());
		val.put(DB_CUSTOMER, finish_pincode_ads.getCustomer());
		val.put(DB_USERNAME, finish_pincode_ads.getUsername());
		val.put(DB_LOCPK, finish_pincode_ads.getLocpk());
		val.put(DB_PINCODE, finish_pincode_ads.getPincode());
		val.put(DB_LAT_LONG, finish_pincode_ads.getLat_Long());
		val.put(DB_WAKTU, finish_pincode_ads.getWaktu());
		val.put(DB_WAYBILL, finish_pincode_ads.getWaybill());
		val.put(DB_KOTA, finish_pincode_ads.getKota());
		db.insert(TABLE_NAME, null, val);
	}
	
	public boolean deleteContact(String finish_pincode_ads)
	{
		finish_pincode_ads = finish_pincode_ads.replaceAll("'","\'");
		return db.delete(TABLE_NAME, DB_NO_ASSIGMENT + "='" + finish_pincode_ads +"'", null) > 0;
	}
	
	public boolean deleteAllContact()
	{
		return db.delete(TABLE_NAME, null, null) > 0;
	}
	
	public Cursor getAllContact()
	{
		return db.query(TABLE_NAME, new String[]
		{
				DB_NO_ASSIGMENT,
				DB_CUSTOMER,
				DB_USERNAME,
				DB_LOCPK,
				DB_PINCODE,
				DB_LAT_LONG,
				DB_WAKTU,
				DB_WAYBILL,
				DB_KOTA
		}, null, null, null, null, null);
	}
	
	public Cursor getIdleFinishADS(String status)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				DB_NO_ASSIGMENT,
				DB_CUSTOMER,
				DB_USERNAME,
				DB_LOCPK,
				DB_PINCODE,
				DB_LAT_LONG,
				DB_WAKTU,
				DB_WAYBILL,
				DB_KOTA
		}, DB_NO_ASSIGMENT + "='" + status +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}
	
	public boolean updateContact(C_Pincode_ADS finish_pincode_ads)
	{
		ContentValues val = new ContentValues();
		val.put(DB_NO_ASSIGMENT, finish_pincode_ads.getAssigment());
		val.put(DB_CUSTOMER, finish_pincode_ads.getCustomer());
		val.put(DB_USERNAME, finish_pincode_ads.getUsername());
		val.put(DB_LOCPK, finish_pincode_ads.getLocpk());
		val.put(DB_PINCODE, finish_pincode_ads.getPincode());
		val.put(DB_LAT_LONG, finish_pincode_ads.getLat_Long());
		val.put(DB_WAKTU, finish_pincode_ads.getWaktu());
		val.put(DB_WAYBILL, finish_pincode_ads.getWaybill());
		val.put(DB_KOTA, finish_pincode_ads.getKota());
		return db.update(TABLE_NAME, val, DB_NO_ASSIGMENT + " = '" + finish_pincode_ads.getAssigment() +"' ", null) > 0;
	}
}
