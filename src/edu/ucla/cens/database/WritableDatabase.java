package edu.ucla.cens.database;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WritableDatabase extends Database {
	private static final String TAG = "WritableDatabase";

	private static Object dbLock = new Object();
	private static boolean databaseOpen = false;

	private final SQLiteOpenHelper dbHelper;

	private final Row rowInstance;

	public WritableDatabase(SQLiteOpenHelper helper, String name, Row row) {
		super(helper, name, row);
		dbHelper = helper;
		rowInstance = row;
	}

	public WritableDatabase(Row row) {
		super(new DatabaseHelper(row.getContext(), row), row.getName(), row);
		dbHelper = new DatabaseHelper(row.getContext(), row);
		rowInstance = row;
	}

	public Database openWrite() throws SQLException {

		synchronized (dbLock) {
			while (databaseOpen) {
				try {
					dbLock.wait();
				} catch (InterruptedException e) {
				}

			}

			databaseOpen = true;
			db = dbHelper.getWritableDatabase();

			return this;
		}
	}

	@Override
	public void close() {
		if (db.isReadOnly()) {
			db.close();
		} else {
			synchronized (dbLock) {
				dbHelper.close();
				databaseOpen = false;

				dbLock.notify();
			}
		}
	}

	public long insertRow(Row row) {
		return insertRow(row.vals());
	}

	public long insertRow(ContentValues vals) {
		String constraint = "";
		for (Iterator<String> i = rowInstance.primaryKeys().iterator(); i.hasNext();) {
			String current = i.next();
			constraint += current + "=" + vals.getAsString(current);
			if (i.hasNext())
				constraint += " AND ";
		}
		Log.d(TAG, constraint);
		Log.d(TAG, rowInstance.getName());
		ArrayList<Row> previousItems = find(constraint);
		if (previousItems.isEmpty()) {
			openWrite();
			long rowid = db.insert(rowInstance.getName(), null, vals);
			close();
			return rowid;
		} else {
			String whereClause = constraintFromArrayList(previousItems, "_id");
			openWrite();
			long rowid = db.update(rowInstance.getName(), vals, whereClause, null);
			close();
			return rowid;
		}

	}

}
