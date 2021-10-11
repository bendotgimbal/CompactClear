
package compact.mobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageAdapter
{
	public static final String	TABLE_NAME	= "image";
	public static final String	IMAGE	= "image";
	public static final String	NAME	= "name";
	public static final String	PENERIMA	= "penerima";
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

	public ImageAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public ImageAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		dbHelper.close();
	}

	public void createBitmap(gambar contact)
	{
		ContentValues val = new ContentValues();
		val.put(IMAGE, contact.getImage());
		val.put(NAME, contact.getName());
		val.put(PENERIMA, contact.getPenerima());
		val.put(STATUS, contact.getStatus());
		db.insert(TABLE_NAME, null, val);
	}

	public boolean deleteBitmap(String image)
	{
		return db.delete(TABLE_NAME, IMAGE + "='" + image +"'", null) > 0;
	}

	public boolean deleteAllPosition()
	{
		return db.delete(TABLE_NAME, null, null) > 0;
	}

	
	public Cursor getAllPosition()
	{
		return db.query(TABLE_NAME, new String[]
		{
				IMAGE, NAME , PENERIMA,  STATUS
		}, null, null, null, null, null);
	}

	public Cursor getSinglePosition(String imei)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				IMAGE, NAME , PENERIMA,  STATUS
		}, IMAGE + "='" + imei +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}

	public Cursor getIdlePosition(String status)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				IMAGE, NAME, PENERIMA, STATUS
		}, STATUS + "='" + status +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}
	
	
	public boolean updatePosition(gambar contact)
	{
		ContentValues val = new ContentValues();
		val.put(IMAGE, contact.getImage());
		val.put(NAME, contact.getName());
		val.put(PENERIMA, contact.getPenerima());
		val.put(STATUS, contact.getStatus());
		return db.update(TABLE_NAME, val, IMAGE + "='" + contact.getImage()+"'", null) > 0;
	}

	public boolean updateStatus(gambar contact)
	{
		ContentValues val = new ContentValues();
		val.put(IMAGE, contact.getImage());
		val.put(NAME, contact.getName());
		val.put(PENERIMA, contact.getPenerima());
		val.put(STATUS, contact.getStatus());
		
		return db.update(TABLE_NAME, val, IMAGE + "='" + contact.getImage()+"' and " + NAME + "='" + contact.getName()+"'", null) > 0;
	}
	
}
