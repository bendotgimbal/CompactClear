
package compact.mobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HostAdapter
{
	public static final String	TABLE_NAME	= "hosturl";
	public static final String	HOST	= "host";
	public static final String	USER	= "username";
	public static final String	PASSWD	= "password";

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

	public HostAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public HostAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		dbHelper.close();
	}

	public void createContact(HostURL contact)
	{
		ContentValues val = new ContentValues();
		val.put(HOST, contact.getHost());
		val.put(USER, contact.getUsername());
		val.put(PASSWD, contact.getPassword());
		db.insert(TABLE_NAME, null, val);
		Log.d("Insert data","table = "+ TABLE_NAME + "host = "+contact.getHost());
	}

	public boolean deleteContact(String Host)
	{
		return db.delete(TABLE_NAME, HOST + "='" + Host +"'", null) > 0;
	}

	public boolean deleteAllContact()
	{
		return db.delete(TABLE_NAME, null, null) > 0;
	}

	
	public Cursor getAllContact()
	{
		return db.query(TABLE_NAME, new String[]
		{
				HOST, USER, PASSWD
		}, null, null, null, null, null);
	}

	public Cursor getSingleContact(String host)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				HOST, USER, PASSWD
		}, HOST + "='" + host+"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}

	public boolean updateContact(HostURL contact)
	{
		ContentValues val = new ContentValues();
		val.put(HOST, contact.getHost());
		val.put(USER, contact.getUsername());
		val.put(PASSWD, contact.getPassword());

		return db.update(TABLE_NAME, val, HOST + "='" + contact.getHost()+"'", null) > 0;
	}

}
