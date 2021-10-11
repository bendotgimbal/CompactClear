package compact.mobile.SuratJalan.DB;

import compact.mobile.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ListPoOutstandingDBAdapter {
	public static final String	TABLE_NAME	= "po_outstanding";
	private static final String DB_NO_ASSIGMENT = "asigment";
	private static final String DB_NO_PO= "no_po";
	private static final String DB_KODE_CUST = "kode_customer";
	private static final String DB_NAMA_CUST = "nama_customer";
	private static final String DB_STATUS = "status";
	
	public static final String DB_WAYBILL = "waybill";
	public static final String DB_LOCPK = "locpk";
	public static final String DB_LAT_LONG = "lat_long";
	public static final String DB_USERNAME = "username";
	public static final String DB_PO = "po";
	public static final String DB_ASSIGMENT = "assigment";
	public static final String DB_IMAGE = "image";
	public static final String DB_KOTA = "kota";
	public static final String DB_WAKTU = "waktu";
	
	private DatabaseHelper		dbHelper;
	private SQLiteDatabase		db;

	private final Context		context;
	
	static String DROP_TABLE_PO_OUTSTANDING = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{

		public DatabaseHelper(Context context)
		{
			// TODO Auto-generated constructor stub
			super(context, DBAdapter.DB_NAME, null, DBAdapter.DB_VER);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL(DROP_TABLE_PO_OUTSTANDING);
		}
		
	}
	
	public ListPoOutstandingDBAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public ListPoOutstandingDBAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public void createContact(C_PoOutstanding po_outstanding)
	{
		ContentValues val = new ContentValues();
		val.put(DB_WAYBILL, po_outstanding.getWaybill());
		val.put(DB_LOCPK, po_outstanding.getLocpk());
		val.put(DB_LAT_LONG, po_outstanding.getLat_Long());
		val.put(DB_USERNAME, po_outstanding.getUsername());
		val.put(DB_PO, po_outstanding.getPo());
		val.put(DB_ASSIGMENT, po_outstanding.getAssigment());
		val.put(DB_IMAGE, po_outstanding.getImage());
		val.put(DB_KOTA, po_outstanding.getKota());
		val.put(DB_WAKTU, po_outstanding.getWaktu());
		db.insert(TABLE_NAME, null, val);
		Log.d("Insert data","table = "+ TABLE_NAME + " WAYBILL = "+po_outstanding.getWaybill());
	}
	
	public boolean deleteContact(String po_outstanding)
	{
		po_outstanding = po_outstanding.replaceAll("'","\'");
		return db.delete(TABLE_NAME, DB_NO_ASSIGMENT + "='" + po_outstanding +"'", null) > 0;
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
				DB_PO,
				DB_ASSIGMENT,
				DB_IMAGE,
				DB_KOTA,
				DB_WAKTU
		}, null, null, null, null, null);
	}
	
	public Cursor getIdlePoOutstanding(String status)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				DB_WAYBILL, 
				DB_LOCPK,
				DB_LAT_LONG,
				DB_USERNAME,
				DB_PO,
				DB_ASSIGMENT,
				DB_IMAGE,
				DB_KOTA,
				DB_WAKTU
		}, DB_WAYBILL + "='" + status +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}
	
	public boolean updateContact(C_PoOutstanding po_outstanding)
	{
//		String waybill;
//		waybill = po_outstanding.getWaybill();
//		waybill = waybill.replace("'", "''");
		ContentValues val = new ContentValues();
		val.put(DB_WAYBILL, po_outstanding.getWaybill());
		val.put(DB_LOCPK, po_outstanding.getLocpk());
		val.put(DB_LAT_LONG, po_outstanding.getLat_Long());
		val.put(DB_USERNAME, po_outstanding.getUsername());
		val.put(DB_PO, po_outstanding.getPo());
		val.put(DB_ASSIGMENT, po_outstanding.getAssigment());
		val.put(DB_IMAGE, po_outstanding.getImage());
		val.put(DB_KOTA, po_outstanding.getKota());
		val.put(DB_WAKTU, po_outstanding.getWaktu());
//		return db.update(TABLE_NAME, val, DB_WAYBILL + " = '" + waybill +"' ", null) > 0;
		return db.update(TABLE_NAME, val, DB_WAYBILL + "='" + po_outstanding.getWaybill()+"'", null) > 0;
	}
}
