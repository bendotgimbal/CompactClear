
package compact.mobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PosAdapter
{
	public static final String	TABLE_NAME	= "user_position";
	public static final String	USER	= "user";
	public static final String	TIME	= "waktu";
	public static final String	IMEI	= "imei";
	public static final String	LAT_LONG	= "lat_long";
	public static final String	STATUS	= "status";
	
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

	public PosAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public PosAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		dbHelper.close();
	}

	public void createPosition(Posisi contact)
	{
		ContentValues val = new ContentValues();
		val.put(IMEI, contact.getImei());
		val.put(USER, contact.getUser());
		val.put(TIME, contact.getTime());
		val.put(LAT_LONG, contact.getLat_Long());
		val.put(STATUS, contact.getStatus());
		db.insert(TABLE_NAME, null, val);
	}

	public boolean deletePosition(String imei)
	{
		return db.delete(TABLE_NAME, IMEI + "='" + imei +"'", null) > 0;
	}

	public boolean deleteAllPosition()
	{
		return db.delete(TABLE_NAME, null, null) > 0;
	}

	
	public Cursor getAllPosition()
	{
		return db.query(TABLE_NAME, new String[]
		{
				IMEI, USER, TIME, LAT_LONG, STATUS
		}, null, null, null, null, null);
	}

	public Cursor getSinglePosition(String imei)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				IMEI, USER, TIME, LAT_LONG, STATUS
		}, IMEI + "='" + imei +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}

	public Cursor getIdlePosition(String status)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				IMEI, USER, TIME, LAT_LONG, STATUS
		}, STATUS + "='" + status +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}
	
	
	public boolean updatePosition(Posisi contact)
	{
		ContentValues val = new ContentValues();
		val.put(IMEI, contact.getImei());
		val.put(USER, contact.getUser());
		val.put(TIME, contact.getTime());
		val.put(LAT_LONG, contact.getLat_Long());
		val.put(STATUS, contact.getStatus());
		return db.update(TABLE_NAME, val, IMEI + "='" + contact.getImei()+"'", null) > 0;
	}

	public boolean updateStatus(Posisi contact)
	{
		ContentValues val = new ContentValues();
		val.put(IMEI, contact.getImei());
		val.put(USER, contact.getUser());
		val.put(TIME, contact.getTime());
		val.put(LAT_LONG, contact.getLat_Long());
		val.put(STATUS, contact.getStatus());
		return db.update(TABLE_NAME, val, IMEI + "='" + contact.getImei()+"' and " + TIME + "='" + contact.getTime()+"'", null) > 0;
	}
	
}
