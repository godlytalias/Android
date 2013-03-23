package com.example.gtacampus;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;

public class DataManip {
	private static final  String DATABASE_NAME = "mydb1.db";
	private static final int DATABASE_VERSION = 1;
	static final String TABLE_NAME = "notes";
	private static Context context;
	static SQLiteDatabase db;

	private SQLiteStatement insertStmt;
	
    private static final String INSERT = "insert into "
		+ TABLE_NAME + " (title,notes) values (?,?)";

	public DataManip(Context context) {
		DataManip.context = context;
		OpenHelper openHelper = new OpenHelper(DataManip.context);
		DataManip.db = openHelper.getWritableDatabase();
		this.insertStmt = DataManip.db.compileStatement(INSERT);

	}
	public long insert(String course,String slot) {
		this.insertStmt.bindString(1, course);
		this.insertStmt.bindString(2, slot);
		
		return this.insertStmt.executeInsert();
	}

	public void deleteAll() {
		db.delete(TABLE_NAME, null, null);
	}

	public List<String[]> selectAll()
	{

		List<String[]> list = new ArrayList<String[]>();
		Cursor cursor = db.query(TABLE_NAME, new String[] { "id", "title","notes" },
				null, null, null, null, "title asc"); 

		int x=0;
		if (cursor.moveToFirst()) {
			do {
				String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2)};

				list.add(b1);

				x=x+1;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		} 
		cursor.close();

		return list;
	}


	public void delete(int rowId) {
		db.delete(TABLE_NAME, null, null); 
	}



	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY, title TEXT, notes TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}



}
