package com.example.gtacampus;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;

public class DataManipulator {
	private static final  String DATABASE_NAME = "mydb.db";
	private static final int DATABASE_VERSION = 1;
	static final String TABLE_NAME1 = "campus";
	static final String TABLE_NAME2 = "notes";
	private static Context context;
	static SQLiteDatabase db;

	private SQLiteStatement insertStmt;
	
    private static final String INSERT1 = "insert into "
		+ TABLE_NAME1 + " (course,slot,bunk) values (?,?,?)";
    private static final String INSERT2 = "insert into "
    		+ TABLE_NAME2 + " (title,notes) values (?,?)";

	public DataManipulator(Context context) {
		DataManipulator.context = context;
		OpenHelper openHelper = new OpenHelper(DataManipulator.context);
		DataManipulator.db = openHelper.getWritableDatabase();
		this.insertStmt = DataManipulator.db.compileStatement(INSERT1);

	}
	public long insert(String course,String slot,String bunk) {
		this.insertStmt.bindString(1, course);
		this.insertStmt.bindString(2, slot);
		this.insertStmt.bindString(3, bunk);
		return this.insertStmt.executeInsert();
	}
	
	public void insertnote(String title, String note)
	{
		db.execSQL("insert into "+TABLE_NAME2+" (title,notes) values ('" + title + "','" + note + "')");
	}
	public void update(String idval , String newval)
	{
		ContentValues val=new ContentValues();
		val.put("bunk", newval);
		db.update(TABLE_NAME1, val, String.format("%s = ?", "id"), new String[]{idval});
	}

	public void deleteAll() {
		db.delete(TABLE_NAME1, null, null);
	}

	public List<String[]> selectAll()
	{

		List<String[]> list = new ArrayList<String[]>();
		Cursor cursor = db.query(TABLE_NAME1, new String[] { "id", "course","slot","bunk"},
				null, null, null, null, "slot asc"); 

		int x=0;
		if (cursor.moveToFirst()) {
			do {
				String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)};

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

	
	public List<String[]> selectAllnotes()
	{
		List<String[]> list = new ArrayList<String[]>();
		Cursor cursor=db.query(TABLE_NAME2, new String[] {"id","title","notes"}, null, null, null, null, null);
		if(cursor.moveToFirst())
		{
		do
		{
			String[] b = new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2)};
			list.add(b);
		}while(cursor.moveToNext());
		
		}
		cursor.close();
		return list;
		
				}

	
	public void deletenote(String note){
		db.delete(TABLE_NAME2, String.format("%s=?","notes"), new String[]{note});
	}



	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME1 + " (id INTEGER PRIMARY KEY, course TEXT, slot TEXT,bunk INTEGER)");
			db.execSQL("CREATE TABLE " + TABLE_NAME2 + " (id INTEGER PRIMARY KEY, title TEXT, notes TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
			onCreate(db);
		}
	}



}
