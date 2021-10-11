package compact.mobile.SuratJalan.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import compact.mobile.DBAdapter;

/**
 * Created by Bendot Gimbal on 18/09/2018.
 */

public class ListPODChooseDBAdapter {
    public static final String	TABLE_NAME	= "pod_ListChoose";
    public static final String DB_POD_KODE = "mrem_kode";
    public static final String DB_POD_KETERANGAN= "mrem_keterangan";

    private ListPODChooseDBAdapter.DatabaseHelper dbHelper;
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

    public ListPODChooseDBAdapter(Context context)
    {
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public ListPODChooseDBAdapter open() throws SQLException
    {
        dbHelper = new ListPODChooseDBAdapter.DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        dbHelper.close();
    }

    public void createContact(C_POD_List pod_ListChoose) {
        ContentValues val = new ContentValues();
        val.put(DB_POD_KODE, pod_ListChoose.getPOD_Kode());
        val.put(DB_POD_KETERANGAN, pod_ListChoose.getPOD_Keterangan());
        db.insert(TABLE_NAME, null, val);
    }

    public boolean deleteContact(String pod_ListChoose)
    {
        pod_ListChoose = pod_ListChoose.replaceAll("'","\'");
        return db.delete(TABLE_NAME, DB_POD_KODE + "='" + pod_ListChoose +"'", null) > 0;
    }

    public boolean deleteAllContact()
    {
        return db.delete(TABLE_NAME, null, null) > 0;
    }

    public Cursor getAllContact()
    {
        return db.query(TABLE_NAME, new String[]
                {
                        DB_POD_KODE,
                        DB_POD_KETERANGAN
                }, null, null, null, null, null);
    }

    public Cursor getIdlePOD_List(String status)
    {
        Cursor cursor = db.query(TABLE_NAME, new String[]
                {
                        DB_POD_KODE,
                        DB_POD_KETERANGAN
                }, DB_POD_KODE + "='" + status +"'", null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    public boolean updateContact(C_POD_List pod_ListChoose)
    {
        ContentValues val = new ContentValues();
        val.put(DB_POD_KODE, pod_ListChoose.getPOD_Kode());
        val.put(DB_POD_KETERANGAN, pod_ListChoose.getPOD_Keterangan());
        return db.update(TABLE_NAME, val, DB_POD_KODE + " = '" + pod_ListChoose.getPOD_Kode() +"' ", null) > 0;
    }
}
