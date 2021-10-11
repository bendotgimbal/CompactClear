
package compact.mobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransAdapter
{
	public static final String	TABLE_NAME	= "trans_wb";
		
	public static final String MSWB_PK = "mswb_pk";
	public static final String MSWB_NO = "mswb_no";
	public static final String CUSTOMER = "customer";
	public static final String SHIPPER = "shipper";
	public static final String CONSIGNEE = "consignee";
	public static final String ORG = "org";
	public static final String ORIGIN = "origin";
	public static final String DEST = "dest";
	public static final String DESTINATION = "destination";
	public static final String CONTENT = "content";
	public static final String QTY = "qty";
	public static final String KG = "kg";
	public static final String STATUS = "status";
	
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

	public TransAdapter(Context context)
	{
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public TransAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		dbHelper.close();
	}

	public void createContact(TransWB wb)
	{
		ContentValues val = new ContentValues();
		
		val.put(MSWB_PK, wb.getMswb_pk());
		val.put(MSWB_NO, wb.getMswb_no());
		val.put(CUSTOMER, wb.getCustomer());
		val.put(SHIPPER,wb.getShipper());
		val.put(CONSIGNEE,wb.getConsignee());
		val.put(ORG, wb.getOrg());
		val.put(ORIGIN, wb.getOrigin());
		val.put(DEST, wb.getDest());
		val.put(DESTINATION, wb.getDestination());
		val.put(CONTENT, wb.getContent());
		val.put(QTY, wb.getQTY());
		val.put(KG, wb.getKG());
		val.put(STATUS, wb.getStatus());
		
		db.insert(TABLE_NAME, null, val);
	}

	public boolean deleteContact(String waybill)
	{
		return db.delete(TABLE_NAME, MSWB_NO + "='" + waybill +"'", null) > 0;
	}

	public boolean deleteAllContact()
	{
		return db.delete(TABLE_NAME, null, null) > 0;
	}

	
	public Cursor getAllWaybill()
	{
		return db.query(TABLE_NAME, new String[]
		{
				MSWB_PK, MSWB_NO, CUSTOMER, SHIPPER, 
				CONSIGNEE,ORG, ORIGIN, DEST, DESTINATION,
				CONTENT, QTY,KG, STATUS
		}, null, null, null, null, null);
	}

	public Cursor getWaybill(String waybill)
	{
		Cursor cursor = db.query(TABLE_NAME, new String[]
		{
				MSWB_PK, MSWB_NO, CUSTOMER, SHIPPER, 
				CONSIGNEE,ORG, ORIGIN, DEST, DESTINATION,
				CONTENT, QTY,KG, STATUS
		}, MSWB_NO + "='" + waybill +"'", null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return cursor;
	}

	public boolean updateContact(TransWB wb)
	{
		ContentValues val = new ContentValues();
		val.put(MSWB_PK, wb.getMswb_pk());
		val.put(MSWB_NO, wb.getMswb_no());
		val.put(CUSTOMER, wb.getCustomer());
		val.put(SHIPPER,wb.getShipper());
		val.put(CONSIGNEE,wb.getConsignee());
		val.put(ORG, wb.getOrg());
		val.put(ORIGIN, wb.getOrigin());
		val.put(DEST, wb.getDest());
		val.put(DESTINATION, wb.getDestination());
		val.put(CONTENT, wb.getContent());
		val.put(QTY, wb.getQTY());
		val.put(KG, wb.getKG());
		val.put(STATUS, wb.getStatus());

		return db.update(TABLE_NAME, val, MSWB_NO + "='" + wb.getMswb_no() +"'", null) > 0;
	}

}
