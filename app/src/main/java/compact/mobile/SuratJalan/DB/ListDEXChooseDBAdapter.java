package compact.mobile.SuratJalan.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import compact.mobile.DBAdapter;

/**
 * Created by yosua on 29/06/2018.
 */

public class ListDEXChooseDBAdapter {
    public static final String	TABLE_NAME	= "dex_ListChoose";
    public static final String DB_DEX_KODE = "mrem_kode";
    public static final String DB_DEX_KETERANGAN= "mrem_keterangan";

    private ListDEXChooseDBAdapter.DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private final Context context;

    private static class DatabaseHelper extends SQLiteOpenHelper
    {

        public DatabaseHelper(Context context)
        {
            // TODO Auto-generated constructor stub
            super(context, DBAdapter.DB_NAME, null, DBAdapter.DB_VER);
        }

        @Override
        public void onCreate(SQLiteDatabase arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub

        }

    }

    public ListDEXChooseDBAdapter(Context context)
    {
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public ListDEXChooseDBAdapter open() throws SQLException
    {
        dbHelper = new ListDEXChooseDBAdapter.DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        dbHelper.close();
    }

    public void createContact(C_DEX_List dex_ListChoose) {
        ContentValues val = new ContentValues();
        val.put(DB_DEX_KODE, dex_ListChoose.getDEX_Kode());
        val.put(DB_DEX_KETERANGAN, dex_ListChoose.getDEX_Keterangan());
        db.insert(TABLE_NAME, null, val);
    }

    public boolean deleteContact(String dex_ListChoose)
    {
        dex_ListChoose = dex_ListChoose.replaceAll("'","\'");
        return db.delete(TABLE_NAME, DB_DEX_KODE + "='" + dex_ListChoose +"'", null) > 0;
    }

    public boolean deleteAllContact()
    {
        return db.delete(TABLE_NAME, null, null) > 0;
    }

    public Cursor getAllContact()
    {
        return db.query(TABLE_NAME, new String[]
                {
                        DB_DEX_KODE,
                        DB_DEX_KETERANGAN
                }, null, null, null, null, null);
    }

    public Cursor getIdleDEX_List(String status)
    {
        Cursor cursor = db.query(TABLE_NAME, new String[]
                {
                        DB_DEX_KODE,
                        DB_DEX_KETERANGAN
                }, DB_DEX_KODE + "='" + status +"'", null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    public boolean updateContact(C_DEX_List dex_ListChoose)
    {
        ContentValues val = new ContentValues();
        val.put(DB_DEX_KODE, dex_ListChoose.getDEX_Kode());
        val.put(DB_DEX_KETERANGAN, dex_ListChoose.getDEX_Keterangan());
        return db.update(TABLE_NAME, val, DB_DEX_KODE + " = '" + dex_ListChoose.getDEX_Kode() +"' ", null) > 0;
    }
}
