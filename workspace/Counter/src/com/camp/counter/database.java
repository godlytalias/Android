package com.camp.counter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class database{
	
	private static Context context;
	openhelper ophelper;
	static SQLiteDatabase db;

private static final String DATABASE_NAME = "counter.db";
private static final int DATABASE_VERSION = 3;
private static final String TABLE_NAME1="question";
private static final String TABLE_NAME2="answers";

public database(Context context) {
	database.context = context;
	ophelper = new openhelper(database.context);
	database.db = ophelper.getWritableDatabase();
}

public void setquestion(String question){
	ContentValues qn = new ContentValues();
	qn.put("question", question);
	db.insert(TABLE_NAME1, null, qn);
}

public void setanswers(ContentValues vals){
	db.insert(TABLE_NAME2, null, vals);
}

public void close(){
	ophelper.close();
	db.close();
}

public Cursor getoption(){
	return db.query(TABLE_NAME2, new String[]{"answers"}, null, null, null, null, null);
}

public Cursor getvalues(){
	return db.query(TABLE_NAME2, new String[]{"count"}, null, null, null, null, null);
}

public String getquestion(){
	Cursor qn = db.query(TABLE_NAME1, new String[]{"question"},null,null,null,null,null);
	qn.moveToFirst();
	return qn.getString(0);
}

public void subtract(String ans){
	db.execSQL("UPDATE "+TABLE_NAME2 + " SET count=count-1 WHERE answers = '" + ans + "'");
}

public void add(String ans){
	db.execSQL("UPDATE "+TABLE_NAME2 + " SET count=count+1 WHERE answers = '" + ans + "'");
}

public void clear_table(){
	db.delete(TABLE_NAME2, null, null);
	db.delete(TABLE_NAME1, null, null);
}

private static class openhelper extends SQLiteOpenHelper {
	
	openhelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + TABLE_NAME1 + " (question TEXT)");
		db.execSQL("CREATE TABLE " + TABLE_NAME2 + " (id INTEGER PRIMARY KEY, answers TEXT, count INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int old, int new_v) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
		onCreate(db);
	}
	
}

}
