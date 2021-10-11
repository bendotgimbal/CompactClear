package compact.mobile.SuratJalan.DB;

import compact.mobile.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ListAWBOthersDBAdapter {
	public static final String	TABLE_NAME	= "awb_others";
	public static final String DB_WAYBILL = "waybill";
	public static final String DB_LOCPK= "locpk";
	public static final String DB_LAT_LONG = "lat_long";
	public static final String DB_USERNAME = "username";
//	private static final String DB_SPJ = "spj";
	public static final String DB_ASSIGMENT = "assigment";
	public static final String DB_IMAGE= "image";
//	private static final String DB_KOTA = "kota";
	public static final String DB_WAKTU = "waktu";
	
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
	
	public ListAWBOthersDBAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public ListAWBOthersDBAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public void createContact(C_AWBOthers awb_others)
	{
		ContentValues val = new ContentValues();
		val.put(DB_WAYBILL, awb_others.getWaybill());
		val.put(DB_LOCPK, awb_others.getLocpk());
		val.put(DB_LAT_LONG, awb_others.getLat_Long());
		val.put(DB_USERNAME, awb_others.getUsername());
//		val.put(DB_SPJ, awb_others.getSpj());
		val.put(DB_ASSIGMENT, awb_others.getAssigment());
		val.put(DB_IMAGE, awb_others.getImage());
//		val.put(DB_KOTA, awb_others.getKota());
		val.put(DB_WAKTU, awb_others.getWaktu());
		db.insert(TABLE_NAME, null, val);
	}
	
	public boolean deleteContact(String awb_others)
	{
		awb_others = awb_others.replaceAll("'","\'");
		return db.delete(TABLE_NAME, DB_WAYBILL + "='" + awb_others +"'", null) > 0;
	}
	
	public boolean deleteAllContact()
	{
		return db.delete(TABLE_NAME, null, null) > 0;
	}
	
	public Cursor getAllContact()
	{
		return db.query(TABLE_NAME, new String[]
		{
				DB_WAYBILL, 
				DB_LOCPK,
				DB_LAT_LONG,
				DB_USERNAME,
//				DB_SPJ,
				DB_ASSIGMENT,
				DB_IMAGE,
//				DB_KOTA,
				DB_WAKTU
		}, null, null, null, null, null);
	}
	
	public Cursor getIdleAWBOthers(String status)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				DB_WAYBILL, 
				DB_LOCPK,
				DB_LAT_LONG,
				DB_USERNAME,
//				DB_SPJ,
				DB_ASSIGMENT,
				DB_IMAGE,
//				DB_KOTA,
				DB_WAKTU
		}, DB_WAYBILL + "='" + status +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}
	
	public boolean updateContact(C_AWBOthers awb_others)
	{
//		String waybill;
//		waybill = ads.getWaybill();
//		waybill = waybill.replace("'", "''");
		ContentValues val = new ContentValues();
		val.put(DB_WAYBILL, awb_others.getWaybill());
		val.put(DB_LOCPK, awb_others.getLocpk());
		val.put(DB_LAT_LONG, awb_others.getLat_Long());
		val.put(DB_USERNAME, awb_others.getUsername());
//		val.put(DB_SPJ, awb_others.getSpj());
		val.put(DB_ASSIGMENT, awb_others.getAssigment());
		val.put(DB_IMAGE, awb_others.getImage());
//		val.put(DB_KOTA, awb_others.getKota());
		val.put(DB_WAKTU, awb_others.getWaktu());
		return db.update(TABLE_NAME, val, DB_WAYBILL + " = '" + awb_others.getWaybill() +"' ", null) > 0;
	}
}
