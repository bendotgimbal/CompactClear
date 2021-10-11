
package compact.mobile;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserAdapter
{
	public static final String	TABLE_NAME	= "mst_user";
	public static final String	USER	= "username";
	public static final String	PASSWD	= "password";
	public static final String	LOCATION	= "location";

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

	public UserAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public UserAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		dbHelper.close();
	}

	public void createContact(User contact)
	{
		ContentValues val = new ContentValues();
		val.put(LOCATION, contact.getLocation());
		val.put(USER, contact.getUsername());
		val.put(PASSWD, contact.getPassword());
		db.insert(TABLE_NAME, null, val);
	}

	public boolean deleteContact(String user)
	{
		return db.delete(TABLE_NAME, USER + "='" + user +"'", null) > 0;
	}

	public boolean deleteAllContact()
	{
		return db.delete(TABLE_NAME, null, null) > 0;
	}

	
	public Cursor getAllContact()
	{
		return db.query(TABLE_NAME, new String[]
		{
				USER, PASSWD, LOCATION 
		}, null, null, null, null, null);
	}

	public Cursor getSingleContact(String user)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				USER, PASSWD, LOCATION
		}, USER + "='" + user +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}

	public boolean updateContact(User contact)
	{
		ContentValues val = new ContentValues();
		val.put(LOCATION, contact.getLocation());
		val.put(USER, contact.getUsername());
		val.put(PASSWD, contact.getPassword());

		return db.update(TABLE_NAME, val, USER + "='" + contact.getUsername()+"'", null) > 0;
	}

}
