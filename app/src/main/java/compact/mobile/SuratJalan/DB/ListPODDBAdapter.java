package compact.mobile.SuratJalan.DB;

import compact.mobile.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ListPODDBAdapter {
	public static final String	TABLE_NAME	= "pod";
	public static final String DB_WAYBILL = "waybill";
	public static final String DB_LOCPK= "locpk";
	public static final String DB_USERNAME = "username";
	public static final String DB_LAT_LONG = "lat_long";
	public static final String DB_IMAGE= "image";
	public static final String DB_KOTA = "kota";
	public static final String DB_ASSIGMENT = "assigment";
	public static final String DB_KETERANGAN = "keterangan";
	public static final String DB_PENERIMA = "penerima";
	public static final String DB_TIPEREM = "tiperem";
	public static final String DB_TLP = "telp";
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
	
	public ListPODDBAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public ListPODDBAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public void createContact(C_POD pod)
	{
		ContentValues val = new ContentValues();
		val.put(DB_WAYBILL, pod.getWaybill());
		val.put(DB_LOCPK, pod.getLocpk());
		val.put(DB_USERNAME, pod.getUsername());
		val.put(DB_LAT_LONG, pod.getLat_Long());
		val.put(DB_IMAGE, pod.getImage());
//		val.put(DB_KOTA, pod.getKota());
		val.put(DB_ASSIGMENT, pod.getAssigment());
		val.put(DB_KETERANGAN, pod.getKeterangan());
		val.put(DB_PENERIMA, pod.getPenerima());
		val.put(DB_TIPEREM, pod.getTiperem());
		val.put(DB_TLP, pod.getTelp());
		val.put(DB_WAKTU, pod.getWaktu());
		db.insert(TABLE_NAME, null, val);
	}
	
	public boolean deleteContact(String pod)
	{
		pod = pod.replaceAll("'","\'");
		return db.delete(TABLE_NAME, DB_WAYBILL + "='" + pod +"'", null) > 0;
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
				DB_USERNAME,
				DB_LAT_LONG,
				DB_IMAGE,
//				DB_KOTA,
				DB_ASSIGMENT, 
				DB_KETERANGAN,
				DB_PENERIMA,
				DB_TIPEREM,
				DB_TLP,
				DB_WAKTU
		}, null, null, null, null, null);
	}
	
	public Cursor getIdlePOD(String status)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				DB_WAYBILL, 
				DB_LOCPK,
				DB_USERNAME,
				DB_LAT_LONG,
				DB_IMAGE,
//				DB_KOTA,
				DB_ASSIGMENT, 
				DB_KETERANGAN,
				DB_PENERIMA,
				DB_TIPEREM,
				DB_TLP,
				DB_WAKTU
		}, DB_WAYBILL + "='" + status +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}
	
	public boolean updateContact(C_POD pod)
	{
//		String waybill;
//		waybill = ads.getWaybill();
//		waybill = waybill.replace("'", "''");
		ContentValues val = new ContentValues();
		val.put(DB_WAYBILL, pod.getWaybill());
		val.put(DB_LOCPK, pod.getLocpk());
		val.put(DB_USERNAME, pod.getUsername());
		val.put(DB_LAT_LONG, pod.getLat_Long());
		val.put(DB_IMAGE, pod.getImage());
//		val.put(DB_KOTA, pod.getKota());
		val.put(DB_ASSIGMENT, pod.getAssigment());
		val.put(DB_KETERANGAN, pod.getKeterangan());
		val.put(DB_PENERIMA, pod.getPenerima());
		val.put(DB_TIPEREM, pod.getTiperem());
		val.put(DB_TLP, pod.getTelp());
		val.put(DB_WAKTU, pod.getWaktu());
		return db.update(TABLE_NAME, val, DB_WAYBILL + " = '" + pod.getWaybill() +"' ", null) > 0;
	}
}
