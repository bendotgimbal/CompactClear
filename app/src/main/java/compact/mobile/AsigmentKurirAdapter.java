package compact.mobile;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AsigmentKurirAdapter
{
	public static final String	TABLE_NAME	= "asigment_kurir";
	public static final String	MANIFES	= "nomor_manifes";
	public static final String	TANGGAL	= "tanggal";
	public static final String	JENIS	= "jenis";
	public static final String	PO	= "po";

	
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

	public AsigmentKurirAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public AsigmentKurirAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		dbHelper.close();
	}

	public void createContact(ContactAsigmentKurir contact)
	{
		ContentValues val = new ContentValues();
		val.put(MANIFES, contact.getManifes());
		val.put(TANGGAL, contact.getTanggal());
		val.put(JENIS, contact.getJenis());
		val.put(PO, contact.getPo());
		db.insert(TABLE_NAME, null, val);
		Log.d("Insert data","table = "+ TABLE_NAME + "MANIFES = "+contact.getManifes());
	}

	public boolean deleteContact(String manifes)
	{
		return db.delete(TABLE_NAME, MANIFES + "='" + manifes +"'", null) > 0;
	}

	public boolean deleteAllContact()
	{
		return db.delete(TABLE_NAME, null, null) > 0;
	}

	
	public Cursor getAllContact()
	{
		return db.query(TABLE_NAME, new String[]
		{
				MANIFES, TANGGAL , JENIS,PO
		}, null, null, null, null, null);
	}

	public Cursor getSingleContact(String manifes)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				MANIFES, TANGGAL,JENIS,PO
		}, MANIFES + "='" + manifes+"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}

	public boolean updateContact(ContactAsigmentKurir contact)
	{
		ContentValues val = new ContentValues();
		val.put(MANIFES, contact.getManifes());
		val.put(TANGGAL, contact.getTanggal());
		val.put(JENIS, contact.getJenis());
		val.put(PO, contact.getPo());

		return db.update(TABLE_NAME, val, MANIFES + "='" + contact.getManifes()+"'", null) > 0;
	}

}

