package compact.mobile.SuratJalan.DB;

import compact.mobile.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ListFinishPoOutstandingDBAdapter {
	public static final String	TABLE_NAME	= "finish_po_outstanding";
	public static final String DB_PO = "po";
	public static final String DB_NO_ASSIGMENT = "assigment";
	
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
	
	public ListFinishPoOutstandingDBAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public ListFinishPoOutstandingDBAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public void createContact(C_Finish_PoOutstanding finish_po_outstanding)
	{
		ContentValues val = new ContentValues();
		val.put(DB_PO, finish_po_outstanding.getPo());
		val.put(DB_NO_ASSIGMENT, finish_po_outstanding.getAssigment());
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
				DB_PO, 
				DB_NO_ASSIGMENT
		}, null, null, null, null, null);
	}
	
//	public Cursor getIdleAWBOthers(String status)
	public Cursor getIdleFinishPoOutstanding(String status)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				DB_PO, 
				DB_NO_ASSIGMENT
		}, DB_PO + "='" + status +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}
	
	public boolean updateContact(C_Finish_PoOutstanding finish_po_outstanding)
	{
//		String waybill;
//		waybill = ads.getWaybill();
//		waybill = waybill.replace("'", "''");
		ContentValues val = new ContentValues();
		val.put(DB_PO, finish_po_outstanding.getPo());
		val.put(DB_NO_ASSIGMENT, finish_po_outstanding.getAssigment());
		return db.update(TABLE_NAME, val, DB_NO_ASSIGMENT + " = '" + finish_po_outstanding.getAssigment() +"' ", null) > 0;
	}
}
