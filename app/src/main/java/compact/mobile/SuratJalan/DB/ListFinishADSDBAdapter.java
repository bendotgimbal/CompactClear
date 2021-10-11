package compact.mobile.SuratJalan.DB;

import compact.mobile.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ListFinishADSDBAdapter {
	public static final String	TABLE_NAME	= "finish_ads";
	public static final String DB_WAKTU = "waktu";
	public static final String DB_NO_ASSIGMENT = "assigment";
	public static final String DB_CUSTOMER= "customer";
	public static final String DB_USERNAME = "username";
//	private static final String DB_KOTA = "kota";
	public static final String DB_LOCPK = "locpk";
	public static final String DB_PINCODE = "pincode";
	
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
	
	public ListFinishADSDBAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public ListFinishADSDBAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public void createContact(C_Finish_ADS finish_ads)
	{
		ContentValues val = new ContentValues();
		val.put(DB_WAKTU, finish_ads.getWaktu());
		val.put(DB_NO_ASSIGMENT, finish_ads.getAssigment());
		val.put(DB_CUSTOMER, finish_ads.getCustomer());
		val.put(DB_USERNAME, finish_ads.getUsername());
//		val.put(DB_KOTA, finish_ads.getKota());
		val.put(DB_LOCPK, finish_ads.getLocpk());
		db.insert(TABLE_NAME, null, val);
	}
	
	public boolean deleteContact(String finish_ads)
	{
		finish_ads = finish_ads.replaceAll("'","\'");
		return db.delete(TABLE_NAME, DB_NO_ASSIGMENT + "='" + finish_ads +"'", null) > 0;
	}
	
	public boolean deleteAllContact()
	{
		return db.delete(TABLE_NAME, null, null) > 0;
	}
	
	public Cursor getAllContact()
	{
		return db.query(TABLE_NAME, new String[]
		{
				DB_WAKTU, 
				DB_NO_ASSIGMENT,
				DB_CUSTOMER,
				DB_USERNAME,
//				DB_KOTA,
				DB_LOCPK
		}, null, null, null, null, null);
	}
	
//	public Cursor getIdleAWBOthers(String status)
	public Cursor getIdleFinishADS(String status)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				DB_WAKTU, 
				DB_NO_ASSIGMENT,
				DB_CUSTOMER,
				DB_USERNAME,
//				DB_KOTA,
				DB_LOCPK
		}, DB_NO_ASSIGMENT + "='" + status +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}
	
	public boolean updateContact(C_Finish_ADS finish_ads)
	{
//		String waybill;
//		waybill = ads.getWaybill();
//		waybill = waybill.replace("'", "''");
		ContentValues val = new ContentValues();
		val.put(DB_WAKTU, finish_ads.getWaktu());
		val.put(DB_NO_ASSIGMENT, finish_ads.getAssigment());
		val.put(DB_CUSTOMER, finish_ads.getCustomer());
		val.put(DB_USERNAME, finish_ads.getUsername());
//		val.put(DB_KOTA, finish_ads.getKota());
		val.put(DB_LOCPK, finish_ads.getLocpk());
		return db.update(TABLE_NAME, val, DB_NO_ASSIGMENT + " = '" + finish_ads.getAssigment() +"' ", null) > 0;
	}
}
