package compact.mobile;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SetorcodAdapter {
	public static final String TABLE_TBSC = "jx_tbsc";
	public final String BSC_KODE_SETORAN = "BSC_KODE_SETORAN";
	public static final String BSC_TANGGAL_BOOKING = "BSC_TANGGAL_BOOKING";
	public static final String BSC_TIME_BOOKING = "BSC_TIME_BOOKING";
	public final String BSC_STATUS = "BSC_STATUS";
	public static final String BSC_TANGGAL_UPDATE = "BSC_TANGGAL_UPDATE";
	public static final String BSC_TIME_UPDATE = "BSC_TIME_UPDATE";
	public final String BSC_LIST_WAYBILL = "BSC_LIST_WAYBILL";
	public static final String BSC_KODE_KURIR = "BSC_KODE_KURIR";
	public final String BSC_TOTAL_NILAI_COD = "BSC_TOTAL_NILAI_COD";
	public static final String SC_KODE_STORE = "SC_KODE_STORE";
	public static final String TBSC_UPDATE_DATE = "TBSC_UPDATE_DATE";
	// ------------------------------------------------------------------------------------------------------

	// -- PAKET 101 ---------------------------------------------- PAKET 101
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private final Context context;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			// TODO Auto-generated constructor stub
			super(context, DBAdapter.DB_NAME, null, DBAdapter.DB_VER);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	public SetorcodAdapter(Context context) {
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public SetorcodAdapter open() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	// -- PAKET 101 ---------------------------------------------- PAKET 101

	// -- PAKET 202 ---------------------------------------------- PAKET 202
	public Cursor getAllContact() {
		return db.query(TABLE_TBSC,
				new String[] { BSC_KODE_SETORAN, BSC_TANGGAL_BOOKING, BSC_TIME_BOOKING, BSC_STATUS, BSC_TANGGAL_UPDATE,
						BSC_TIME_UPDATE, BSC_LIST_WAYBILL, BSC_KODE_KURIR, BSC_TOTAL_NILAI_COD, SC_KODE_STORE,
						TBSC_UPDATE_DATE },
				null, null, null, null, null);
	}
	// -- PAKET 202 ---------------------------------------------- PAKET 202

	// -- PAKET 203 ---------------------------------------------- PAKET 203
	public Cursor getID() {
		return db.query(TABLE_TBSC, new String[] { BSC_KODE_SETORAN }, null, null, null, null, null);
	}
	// -- PAKET 203 ---------------------------------------------- PAKET 203

	// -- PAKET 204 ---------------------------------------------- PAKET 204
	public void insertData(JSONArray jSONArray) {
		try {
			for (int i = 0; i < jSONArray.length(); i++) {
				JSONObject ar = jSONArray.getJSONObject(i);
				ContentValues val = new ContentValues();

				val.put(BSC_KODE_SETORAN, ar.getString("bsc_kode_setoran"));
				val.put(BSC_LIST_WAYBILL, ar.getString("airwaybill"));
				val.put(BSC_TANGGAL_BOOKING, ar.getString("bsc_tanggal_booking"));
				val.put(BSC_TIME_BOOKING, ar.getString("bsc_time_booking"));
				val.put(BSC_TOTAL_NILAI_COD, ar.getString("bsc_total_nilai_cod"));
				val.put(BSC_STATUS, ar.getString("bsc_status"));

				db.insert(TABLE_TBSC, null, val);
				Log.d("SQLITE_ADAPTER",
						"Success InsertData [Jx_ListSetorCodAdapter] : " + ar.getString("bsc_total_nilai_cod"));
			}

		} catch (Exception e) {
			Log.d("SQLITE_ADAPTER", "Error InsertData [Jx_ListSetorCodAdapter] : " + e.toString());
		}
	}
	// -- PAKET 204 ---------------------------------------------- PAKET 204

	public boolean deleteAllContact() {
		Log.d("SQLITE_ADAPTER", "deleteAllContact [Jx_ListSetorCodAdapter] : ");
		return db.delete(TABLE_TBSC, null, null) > 0;
	}

}
