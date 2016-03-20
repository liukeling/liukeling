package com.example.SQLite;

import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class sqliteUtils {

	private static SQLiteDatabase readDatabase;
	private static SQLiteDatabase writeDatabase;

	static{
		MyHelper myHelper = new MyHelper(AppContext.getContext(), "dataDB", null, 2);
		readDatabase = myHelper.getReadableDatabase();
		writeDatabase = myHelper.getWritableDatabase();
	}
	
	public static long insert(ContentValues values){
		Long l = writeDatabase.insert("historData", null, values);
		return l;
	}
	
	public static float[] getValue(String type, int i){
		float[] f;
		if(i == 0){
			f = new float[60];
			String[] columns = {type};
			Cursor curr = readDatabase.query("historData", columns, null, null, null, null, null);
			while(curr.moveToNext()){
				System.out.println(curr.getPosition()+"getValue");
			}
		}else{
			f = new float[5];
			
			
			
		}
		return f;
	}
	
}
