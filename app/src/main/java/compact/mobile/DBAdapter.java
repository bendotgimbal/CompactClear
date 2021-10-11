
package compact.mobile;

import compact.mobile.SuratJalan.DB.ListADSDBAdapter;
import compact.mobile.SuratJalan.DB.ListAWBOthersDBAdapter;
import compact.mobile.SuratJalan.DB.ListDEXChooseDBAdapter;
import compact.mobile.SuratJalan.DB.ListDEXDBAdapter;
import compact.mobile.SuratJalan.DB.ListFinishADSDBAdapter;
import compact.mobile.SuratJalan.DB.ListFinishAWBOthersDBAdapter;
import compact.mobile.SuratJalan.DB.ListFinishPincodeADSDBAdapter;
import compact.mobile.SuratJalan.DB.ListFinishPoOutstandingDBAdapter;
import compact.mobile.SuratJalan.DB.ListPODChooseDBAdapter;
import compact.mobile.SuratJalan.DB.ListPODDBAdapter;
import compact.mobile.SuratJalan.DB.ListPoOutstandingDBAdapter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBAdapter
{
//	public static final String	DB_NAME		= "clear_db";
//	public static final String	DB_NAME		= "clear_mobile_db.db";
	public static final String	DB_NAME		= "compact_mobile_db.db";
	public static final int	DB_VER		= 3;

	
	private static final String	TAG			= "ContactDBAdapter";
	private DatabaseHelper		dbHelper;
	private static final String	HOSTDB_CREATE	= "create table hosturl (host text primary key, username text not null, password text not null);";
	private static final String	USERDB_CREATE	= "create table mst_user (username text primary key, password text not null, location text not null);";
	private static final String	POSDB_CREATE	= "create table user_position (imei text not null, waktu text not null, user text not null, lat_long text not null, status text not null, PRIMARY KEY (imei, waktu));";
	private static final String	WAYBILLDB_CREATE	= "create table waybill (waybill text primary key, penerima text, kota text, tujuan text, mswb_pk text, locpk text, username text, tiperem text, telp text, tipe text, keterangan text, lat_long text, waktu text, status text,po text);";
	private static final String	IMAGEDB_CREATE	= "create table image (name text primary key, penerima text, image text, status text);";
	private static final String TRANSDB_CREATE = "create table trans_wb (mswb_no text primary key, mswb_pk text , customer text, shipper text, consignee text,org text, origin text, dest text, destination text, content text, qty text, kg text, status text);";
	private static final String JX_TBSC_CREATE = "create table jx_tbsc (BSC_KODE_SETORAN text primary key, BSC_TANGGAL_BOOKING text, BSC_TIME_BOOKING text, BSC_STATUS text, BSC_TANGGAL_UPDATE text, BSC_TIME_UPDATE text, BSC_LIST_WAYBILL text, BSC_KODE_KURIR text, BSC_TOTAL_NILAI_COD text, SC_KODE_STORE text, TBSC_UPDATE_DATE text);";
	private static final String BOOKING_CREATE = "create table listbooking (kode_booking text primary key, nama text, alamat text, latlang text);";
	private static final String ASIGMEN_CREATE = "create table asigment_kurir (nomor_manifes text primary key, tanggal text, jenis text, po text);";
	private static final String DETAILS_ASIGMENT = "create table details_list_pickup (waybill text primary key, tanggal text,asigment text,jenis text);";
	private static final String ASIGMENT_PO_OUTSTANDING = "create table po_outstanding (waybill text primary key, locpk text, lat_long text, username text,po text,  assigment text, image text, kota text, waktu text);";
	private static final String ASIGMENT_ADS = "create table ads (waybill text primary key, locpk text, lat_long text, username text, assigment text, image text, waktu text, customer text);";
	private static final String ASIGMENT_AWB_OTHERS = "create table awb_others (waybill text primary key, locpk text, lat_long text, image text, username text, assigment text, waktu text);";
	private static final String ASIGMENT_POD = "create table pod (waybill text primary key, locpk text, username text, lat_long text, image text, assigment text, keterangan text, penerima text, tiperem text, telp text, waktu text)";
	private static final String ASIGMENT_DEX = "create table dex (waybill text primary key, locpk text, username text, lat_long text, image text, assigment text, keterangan text, tiperem text, waktu text)";
	private static final String ASIGMENT_PO_OUTSTANDING_FINISH = "create table finish_po_outstanding (po text primary key, assigment text);";
	private static final String ASIGMENT_ADS_FINISH = "create table finish_ads (waktu text primary key, assigment text, customer text, username text, kota text, locpk text);";
//	private static final String ASIGMENT_ADS_FINISH_PINCODE = "create table finish_pincode_ads (pincode text primary key, assigment text, customer text, username text, locpk text, lat_long text, waktu text);";
	private static final String ASIGMENT_ADS_FINISH_PINCODE = "create table finish_pincode_ads (waybill text primary key, assigment text, customer text, username text, locpk text, lat_long text, waktu text, pincode text, kota text);";
	private static final String ASIGMENT_AWB_OTHERS_FINISH = "create table finish_awb_others (waktu text primary key, assigment text, customer text, username text, kota text, locpk text);";
	private static final String ASIGMENT_DEX_LIST = "create table dex_ListChoose (mrem_kode text primary key, mrem_keterangan text)";
	private static final String ASIGMENT_POD_LIST = "create table pod_ListChoose (mrem_kode text primary key, mrem_keterangan text)";
	
	private final Context		context;

	private static class DatabaseHelper extends SQLiteOpenHelper
	{

		public DatabaseHelper(Context context)
		{
			// TODO Auto-generated constructor stub
			super(context, DB_NAME, null, DB_VER);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// TODO Auto-generated method stub
			Log.d(TAG, "oncreate DB");
			db.execSQL(HOSTDB_CREATE);
			Log.d("create", HOSTDB_CREATE);
			db.execSQL(USERDB_CREATE);
			Log.d("create", USERDB_CREATE);
			db.execSQL(POSDB_CREATE);
			db.execSQL(WAYBILLDB_CREATE);
			db.execSQL(IMAGEDB_CREATE);
			db.execSQL(TRANSDB_CREATE);
			db.execSQL(JX_TBSC_CREATE);
			db.execSQL(BOOKING_CREATE);
			db.execSQL(ASIGMEN_CREATE);
			db.execSQL(DETAILS_ASIGMENT);
			db.execSQL(ASIGMENT_PO_OUTSTANDING);
			db.execSQL(ASIGMENT_ADS);
			db.execSQL(ASIGMENT_AWB_OTHERS);
			db.execSQL(ASIGMENT_POD);
			db.execSQL(ASIGMENT_DEX);
			db.execSQL(ASIGMENT_PO_OUTSTANDING_FINISH);
			db.execSQL(ASIGMENT_ADS_FINISH);
			db.execSQL(ASIGMENT_AWB_OTHERS_FINISH);
			db.execSQL(ASIGMENT_ADS_FINISH_PINCODE);
			db.execSQL(ASIGMENT_DEX_LIST);
			db.execSQL(ASIGMENT_POD_LIST);
			
			Log.d("create", "oncreate DB");
			//Log.d("create", ASIGMEN_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			// TODO Auto-generated method stub
			Log.d(TAG, "upgrade DB");
			db.execSQL("DROP TABLE IF EXISTS " + HostAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + UserAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + PosAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + WaybillAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ImageAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + TransAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + SetorcodAdapter.TABLE_TBSC);
			db.execSQL("DROP TABLE IF EXISTS " + BookingAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + AsigmentKurirAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + DetailListAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ListPoOutstandingDBAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ListADSDBAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ListAWBOthersDBAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ListPODDBAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ListDEXDBAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ListFinishPoOutstandingDBAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ListFinishADSDBAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ListFinishAWBOthersDBAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ListFinishPincodeADSDBAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ListDEXChooseDBAdapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ListPODChooseDBAdapter.TABLE_NAME);
			onCreate(db);
		}

	}

	public DBAdapter(Context context)
	{
		this.context = context;
		this.dbHelper = new DatabaseHelper(this.context);
		// TODO Auto-generated constructor stub
	}

	public DBAdapter open() throws SQLException
	{
		this.dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		this.dbHelper.close();
	}

	public List<String> getAllLabelsDEX(){
		List<String> labelsDEX = new ArrayList<String>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + ListDEXChooseDBAdapter.TABLE_NAME;

		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				labelsDEX.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();
		db.close();

		// returning lables
		return labelsDEX;
	}

	public List<String> getAllLabelsPOD(){
		List<String> labelsPOD = new ArrayList<String>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + ListPODChooseDBAdapter.TABLE_NAME;

		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				labelsPOD.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();
		db.close();

		// returning lables
		return labelsPOD;
	}

	public List<String> getAllFinishPincodeADS(){
		List<String> labelsFinishPincodeADS = new ArrayList<String>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + ListFinishPincodeADSDBAdapter.TABLE_NAME;

		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				labelsFinishPincodeADS.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();
		db.close();

		// returning lables
		return labelsFinishPincodeADS;
	}

	public List<String> getPosisi(){
		List<String> labelsPosisi = new ArrayList<String>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + PosAdapter.TABLE_NAME;

		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				labelsPosisi.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();
		db.close();

		// returning lables
		return labelsPosisi;
	}

}
