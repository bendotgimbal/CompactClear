
package compact.mobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BookingAdapter
{
	public static final String	TABLE_NAME	= "listbooking";
	public static final String	KODE_BOOKING	= "kode_booking";
	public static final String	NAMA	= "nama";
	public static final String	ALAMAT	= "alamat";
	public static final String	LATLANG	= "latlang";
	
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

	public BookingAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public BookingAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		dbHelper.close();
	}

	public void createContact(ContactListBooking contact)
	{
		ContentValues val = new ContentValues();
		val.put(KODE_BOOKING, contact.getBooking());
		val.put(NAMA, contact.getNama());
		val.put(ALAMAT, contact.getAlamat());
		val.put(LATLANG, contact.getLatlang());
		db.insert(TABLE_NAME, null, val);
		Log.d("Insert data","table = "+ TABLE_NAME + "host = "+contact.getBooking());
	}

	public boolean deleteContact(String booking)
	{
		return db.delete(TABLE_NAME, KODE_BOOKING + "='" + booking +"'", null) > 0;
	}

	public boolean deleteAllContact()
	{
		return db.delete(TABLE_NAME, null, null) > 0;
	}

	
	public Cursor getAllContact()
	{
		return db.query(TABLE_NAME, new String[]
		{
				KODE_BOOKING, NAMA, ALAMAT,LATLANG
		}, null, null, null, null, null);
	}

	public Cursor getSingleContact(String booking)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				KODE_BOOKING, NAMA, ALAMAT,LATLANG
		}, KODE_BOOKING + "='" + booking+"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}

	public boolean updateContact(ContactListBooking contact)
	{
		ContentValues val = new ContentValues();
		val.put(KODE_BOOKING, contact.getBooking());
		val.put(NAMA, contact.getNama());
		val.put(ALAMAT, contact.getAlamat());
		val.put(LATLANG, contact.getLatlang());
		return db.update(TABLE_NAME, val, KODE_BOOKING + "='" + contact.getBooking()+"'", null) > 0;
	}

}
