package compact.mobile;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DetailListAdapter
{
	public static final String	TABLE_NAME	= "details_list_pickup";
	public static final String	WAYBILL	= "waybill";
	public static final String	TANGGAL	= "tanggal";
	public static final String	ASIGMENT = "asigment";
	public static final String	JENIS = "jenis";
	
	
	
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

	public DetailListAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public DetailListAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		dbHelper.close();
	}

	public void createContact(ContactDetailList contact)
	{
		ContentValues val = new ContentValues();
		val.put(WAYBILL, contact.getWaybill());
		val.put(TANGGAL, contact.getTanggal());
		val.put(ASIGMENT, contact.getAsigment());
		val.put(JENIS, contact.getJenis());
		db.insert(TABLE_NAME, null, val);
		Log.d("Insert data","table = "+ TABLE_NAME + "WAYBILL = "+contact.getWaybill());
	}

	public boolean deleteContact(String asigment)
	{
		return db.delete(TABLE_NAME, ASIGMENT + "='" + asigment +"'", null) > 0;
	}

	public boolean deleteAllContact()
	{
		return db.delete(TABLE_NAME, null, null) > 0;
	}

	
	public Cursor getAllContact()
	{
		return db.query(TABLE_NAME, new String[]
		{
				WAYBILL, TANGGAL,JENIS
		}, null, null, null, null, null);
	}

	public Cursor getSingleContact(String asigment)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				WAYBILL, TANGGAL , JENIS
		}, ASIGMENT + "='" + asigment+"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}

	public boolean updateContact(ContactDetailList contact)
	{
		ContentValues val = new ContentValues();
		val.put(WAYBILL, contact.getWaybill());
		val.put(TANGGAL, contact.getTanggal());
		val.put(JENIS, contact.getJenis());
		return db.update(TABLE_NAME, val, WAYBILL + "='" + contact.getWaybill()+"'", null) > 0;
	}

}

