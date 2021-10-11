package compact.mobile.SuratJalan.DB;

import compact.mobile.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ListFinishAWBOthersDBAdapter {
	public static final String	TABLE_NAME	= "finish_awb_others";
	public static final String DB_WAKTU = "waktu";
	public static final String DB_NO_ASSIGMENT = "assigment";
	public static final String DB_CUSTOMER= "customer";
	public static final String DB_USERNAME = "username";
//	private static final String DB_KOTA = "kota";
	public static final String DB_LOCPK = "locpk";
	
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
	
	public ListFinishAWBOthersDBAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public ListFinishAWBOthersDBAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public void createContact(C_Finish_AWBOthers finish_awb_others)
	{
		ContentValues val = new ContentValues();
		val.put(DB_WAKTU, finish_awb_others.getWaktu());
		val.put(DB_NO_ASSIGMENT, finish_awb_others.getAssigment());
		val.put(DB_CUSTOMER, finish_awb_others.getCustomer());
		val.put(DB_USERNAME, finish_awb_others.getUsername());
//		val.put(DB_KOTA, finish_awb_others.getKota());
		val.put(DB_LOCPK, finish_awb_others.getLocpk());
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
	public Cursor getIdleFinishAWBOthers(String status)
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
	
	public boolean updateContact(C_Finish_AWBOthers finish_awb_others)
	{
//		String waybill;
//		waybill = ads.getWaybill();
//		waybill = waybill.replace("'", "''");
		ContentValues val = new ContentValues();
		val.put(DB_WAKTU, finish_awb_others.getWaktu());
		val.put(DB_NO_ASSIGMENT, finish_awb_others.getAssigment());
		val.put(DB_CUSTOMER, finish_awb_others.getCustomer());
		val.put(DB_USERNAME, finish_awb_others.getUsername());
//		val.put(DB_KOTA, finish_awb_others.getKota());
		val.put(DB_LOCPK, finish_awb_others.getLocpk());
		return db.update(TABLE_NAME, val, DB_NO_ASSIGMENT + " = '" + finish_awb_others.getAssigment() +"' ", null) > 0;
	}
}
