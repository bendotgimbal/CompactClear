package compact.mobile.SuratJalan.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBSpj {
//	public static final String	DB_NAME		= "assigment_db";
	public static final String	DB_NAME		= "assigment_db.db";
	public static final int	DB_VER			= 7;
	private static final String	TAG			= "ContactDBAdapter";
	
	public static final String	TABLE_NAME_PO_OUTSTANDING	= "po_outstanding";
	
//	private static final String PO_OUTSTANDING = "create table po_outstanding (asigment text primary key, no_po text, kode_customer text, nama_customer text,status text);";
	//private static final String PO_OUTSTANDING = "create table "+ TABLE_NAME_PO_OUTSTANDING +"(asigment text primary key, no_po text, kode_customer text, nama_customer text, status text);";
	private static final String ASIGMENT_PO_OUTSTANDING = "create table po_outstanding (waybill text primary key, locpk text, lat_long text, username text,po text,  assigment text, image text, waktu text);";
	private static final String ADS = "create table ads (asigment text primary key, no_awb text, nama_toko text, status text);";
	
	private DatabaseHelper		dbHelper;
	private final Context		context;
	private static class DatabaseHelper extends SQLiteOpenHelper
	{

		public DatabaseHelper(Context context) {
			// TODO Auto-generated constructor stub
			super(context, DB_NAME, null, DB_VER);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			Log.d(TAG, "OnCreate DB");
			db.execSQL(ASIGMENT_PO_OUTSTANDING);
			db.execSQL(ADS);
			Log.d("Create DB PO Outstanding", ASIGMENT_PO_OUTSTANDING +" "+ ADS);
			Log.d("Create", "OnCreate DB");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.d(TAG, "OnCreate DB");
			db.execSQL("DROP TABLE IF EXISTS " + ListPoOutstandingDBAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ListADSDBAdapter.TABLE_NAME);
			onCreate(db);
			
		}
		
	}
	
	public DBSpj(Context context)
	{
		this.context = context;
		this.dbHelper = new DatabaseHelper(this.context);
		// TODO Auto-generated constructor stub
	}

	public DBSpj open() throws SQLException
	{
		this.dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		this.dbHelper.close();
	}
	
}
