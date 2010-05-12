package edu.ucla.cens.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database {
	private final SQLiteOpenHelper dbHelper;

	private final String name;

	private final Row rowInstance;
	protected static SQLiteDatabase db;

	private static final String TAG = "Database";

	public Database(SQLiteOpenHelper helper, String name, Row row) {
		dbHelper = helper;
		this.name = name;
		this.rowInstance = row;
	}

	public Database(Row row) {
		this(new DatabaseHelper(row.getContext(), row), row.getName(), row);
	}

	public Database openRead() throws SQLException {
		db = dbHelper.getReadableDatabase();

		return this;
	}

	public void close() {
		db.close();
	}

	public ArrayList<Row> find(String filter) {
		openRead();
		ArrayList<Row> ret = CursorToArrayList(db.query(name, fields(), filter, null, null, null, null));
		close();
		return ret;
	}

	public Row find(long id) {
		openRead();
		Cursor c = db.query(name, fields(), "_id=?", new String[]{String.valueOf(id)}, null, null, null);

		if (c.moveToFirst()) {
			rowInstance.readCursor(c);
		}

		c.close();
		close();
		return rowInstance;
	}

	public ArrayList<Row> CursorToArrayList(Cursor c) {
		ArrayList<Row> ret = new ArrayList<Row>();
		int numRows = c.getCount();

		c.moveToFirst();

		for (int i = 0; i < numRows; ++i) {
			Row newRow = rowInstance.newRow();
			newRow.readCursor(c);
			ret.add(newRow);

			c.moveToNext();

		}
		c.close();

		return ret;
	}

	public String[] fields() {
		ArrayList<String> ret = new ArrayList<String>();
		Field[] fields = rowInstance.getFields();
		for (int i = 0; i < fields.length; i++) {
			ret.add(fields[i].getName());
		}
		return ret.toArray(new String[0]);
	}

	public String getName() {
		return name;
	}

	// finds all rows which have values for the name
	public ArrayList<Row> find(ArrayList<Row> array, String name, String infilter) {
		return find(infilter + constraintFromArrayList(array, name));
	}

	protected String constraintFromArrayList(ArrayList<Row> rows, String fieldName) {
		String filter = " (";
		for (Iterator<Row> i = rows.iterator(); i.hasNext();)
			try {
				Row current = i.next();
				filter += "_id=" + current.getClass().getField(fieldName).get(current);
				if (i.hasNext())
					filter += " OR ";
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return filter + ") ";
	}

	public ArrayList<Row> all() {
		return find(null);
	}
}
