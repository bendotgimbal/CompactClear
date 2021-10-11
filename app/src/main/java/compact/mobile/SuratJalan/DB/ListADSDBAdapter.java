package compact.mobile.SuratJalan.DB;

import compact.mobile.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ListADSDBAdapter {
	public static final String	TABLE_NAME	= "ads";
	private static final String DB_NO_ASSIGMENT = "asigment";
	private static final String DB_NO_AWB= "no_awb";
	private static final String DB_NAMA_TOKO = "nama_toko";
	private static final String DB_STATUS = "status";
	
	public static final String DB_WAYBILL = "waybill";
	public static final String DB_LOCPK= "locpk";
	public static final String DB_LAT_LONG = "lat_long";
	public static final String DB_USERNAME = "username";
//	private static final String DB_SPJ = "spj";
	public static final String DB_ASSIGMENT = "assigment";
	public static final String DB_IMAGE= "image";
//	private static final String DB_KOTA = "kota";
	public static final String DB_WAKTU = "waktu";
	public static final String DB_CUSTOMER = "customer";
	
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
	
	public ListADSDBAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public ListADSDBAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public void createContact(C_ADS ads)
	{
		ContentValues val = new ContentValues();
		val.put(DB_WAYBILL, ads.getWaybill());
		val.put(DB_LOCPK, ads.getLocpk());
		val.put(DB_LAT_LONG, ads.getLat_Long());
		val.put(DB_USERNAME, ads.getUsername());
//		val.put(DB_SPJ, ads.getSpj());
		val.put(DB_ASSIGMENT, ads.getAssigment());
//		val.put(DB_IMAGE, ads.getImage());
//		val.put(DB_KOTA, ads.getKota());
		val.put(DB_WAKTU, ads.getWaktu());
		val.put(DB_CUSTOMER, ads.getCustomer());
		db.insert(TABLE_NAME, null, val);
	}
	
	public boolean deleteContact(String ads)
	{
		ads = ads.replaceAll("'","\'");
		return db.delete(TABLE_NAME, DB_WAYBILL + "='" + ads +"'", null) > 0;
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
//				DB_IMAGE,
//				DB_KOTA,
				DB_WAKTU,
				DB_CUSTOMER
		}, null, null, null, null, null);
	}
	
	public Cursor getIdleADS(String status)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				DB_WAYBILL, 
				DB_LOCPK,
				DB_LAT_LONG,
				DB_USERNAME,
//				DB_SPJ,
				DB_ASSIGMENT,
//				DB_IMAGE,
//				DB_KOTA,
				DB_WAKTU,
				DB_CUSTOMER
		}, DB_WAYBILL + "='" + status +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}
	
	public boolean updateContact(C_ADS ads)
	{
//		String waybill;
//		waybill = ads.getWaybill();
//		waybill = waybill.replace("'", "''");
		ContentValues val = new ContentValues();
		val.put(DB_WAYBILL, ads.getWaybill());
		val.put(DB_LOCPK, ads.getLocpk());
		val.put(DB_LAT_LONG, ads.getLat_Long());
		val.put(DB_USERNAME, ads.getUsername());
//		val.put(DB_SPJ, ads.getSpj());
		val.put(DB_ASSIGMENT, ads.getAssigment());
//		val.put(DB_IMAGE, ads.getImage());
//		val.put(DB_KOTA, ads.getKota());
		val.put(DB_WAKTU, ads.getWaktu());
		val.put(DB_CUSTOMER, ads.getCustomer());
		return db.update(TABLE_NAME, val, DB_WAYBILL + " = '" + ads.getWaybill() +"' ", null) > 0;
	}
}
