
package compact.mobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WaybillAdapter
{
	public static final String	TABLE_NAME	= "waybill";
	public static final String	WAYBILL		= "waybill";
	public static final String	PENERIMA	= "penerima";
	public static final String	KOTA		= "kota";
	public static final String	TUJUAN		= "tujuan";
	public static final String	MSWB_PK		= "mswb_pk";
	public static final String	LOCPK		= "locpk";
	public static final String	USERNAME 	= "username";
	public static final String	TIPEREM		= "tiperem";
	public static final String	TELP 		= "telp";
	public static final String	TIPE 		= "tipe";
	public static final String	KETERANGAN 	= "keterangan";
	public static final String	LAT_LONG	= "lat_long";
	public static final String	WAKTU 		= "waktu";
	public static final String	STATUS 		= "status";
	public static final String	PO 		= "po";
	public static final String	ASSIGMENT 	= "assigment";
	
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
		public void onCreate(SQLiteDatabase db)
		{
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
		}

	}

	public WaybillAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public WaybillAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		dbHelper.close();
	}

	public void createContact(Waybill waybill)
	{
		ContentValues val = new ContentValues();
		val.put(WAYBILL, waybill.getWaybill());
		val.put(PENERIMA, waybill.getPenerima());
		val.put(KOTA, waybill.getKota());
		val.put(TUJUAN, waybill.getTujuan());
		val.put(MSWB_PK, waybill.getMswb_pk());
		val.put(LOCPK, waybill.getLocpk());
		val.put(USERNAME, waybill.getUser());
		val.put(TIPEREM, waybill.getTiperem());
		val.put(TELP, waybill.getTelp());
		val.put(TIPE, waybill.getTipe());
		val.put(KETERANGAN, waybill.getKeterangan());
		val.put(LAT_LONG, waybill.getLat_Long());
		val.put(WAKTU, waybill.getWaktu());
		val.put(STATUS, waybill.getStatus());
		val.put(PO, waybill.getPo());
//		val.put(ASSIGMENT, waybill.getAssigment());
		db.insert(TABLE_NAME, null, val);
	}

	public boolean deleteContact(String waybill)
	{
		waybill = waybill.replaceAll("'","\'");
		return db.delete(TABLE_NAME, WAYBILL + "='" + waybill +"'", null) > 0;
	}

	public boolean deleteAllContact()
	{
		return db.delete(TABLE_NAME, null, null) > 0;
	}

	
	public Cursor getAllContact()
	{
		return db.query(TABLE_NAME, new String[]
		{
				 WAYBILL, 
				 PENERIMA,
				 KOTA, 
				 TUJUAN,  
				 MSWB_PK,  
				 LOCPK,  
				 USERNAME,  
				 TIPEREM,  
				 TELP,  
				 TIPE,  
				 KETERANGAN,  
				 LAT_LONG,  
				 WAKTU,
				 STATUS,
				 PO
//				 ASSIGMENT
		}, null, null, null, null, null);
	}

	public Cursor getIdleWaybill(String status)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				 WAYBILL, 
				 PENERIMA,
				 KOTA, 
				 TUJUAN,  
				 MSWB_PK,  
				 LOCPK,  
				 USERNAME,  
				 TIPEREM,  
				 TELP,  
				 TIPE,  
				 KETERANGAN,  
				 LAT_LONG,  
				 WAKTU,
				 STATUS,
				 PO
//				 ASSIGMENT
		}, STATUS + "='" + status +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}

	public boolean updateContact(Waybill waybill)
	{
		String awb;
		awb = waybill.getWaybill();
		awb = awb.replace("'", "''");
		ContentValues val = new ContentValues();
		val.put(WAYBILL, waybill.getWaybill());
		val.put(PENERIMA, waybill.getPenerima());
		val.put(KOTA, waybill.getKota());
		val.put(TUJUAN, waybill.getTujuan());
		val.put(MSWB_PK, waybill.getMswb_pk());
		val.put(LOCPK, waybill.getLocpk());
		val.put(USERNAME, waybill.getUser());
		val.put(TIPEREM, waybill.getTiperem());
		val.put(TELP, waybill.getTelp());
		val.put(TIPE, waybill.getTipe());
		val.put(KETERANGAN, waybill.getKeterangan());
		val.put(LAT_LONG, waybill.getLat_Long());
		val.put(WAKTU, waybill.getWaktu());
		val.put(STATUS, waybill.getStatus());
		val.put(PO, waybill.getPo());
//		val.put(ASSIGMENT, waybill.getAssigment());
		return db.update(TABLE_NAME, val, WAYBILL + " = '" + awb +"' ", null) > 0;
	}

}
