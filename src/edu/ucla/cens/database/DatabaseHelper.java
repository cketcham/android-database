package edu.ucla.cens.database;

import java.lang.reflect.Field;

import edu.ucla.cens.database.Row;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseHelper";
	private final Row row;

	public DatabaseHelper(Context ctx, Row row) {
		super(ctx, row.getName(), null, 1);
		this.row = row;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, makeCreate(row));
		db.execSQL(makeCreate(row));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE EXISTS " + row.getName());
		onCreate(db);
	}

	public String makeCreate(Row row) {
		String create = "create table " + row.getName() + " (_id integer primary key autoincrement,";
		Field[] fields = row.getFields();
		for (int i = 0; i < fields.length; i++) {
			if (!fields[i].getName().equals("_id")) {
				create += " " + fields[i].getName() + " ";
				if (fields[i].getType().equals(Long.class) || fields[i].getType().equals(Double.class))
					create += "integer,";
				else if (fields[i].getType().equals(String.class) || fields[i].getType().equals(Boolean.class))
					create += "text,";
			}
		}
		create = create.substring(0, create.length() - 1);
		create += ");";

		return create;
	}
}
