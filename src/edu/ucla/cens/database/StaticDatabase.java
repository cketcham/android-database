package edu.ucla.cens.database;

import android.content.Context;

public class StaticDatabase extends Database {
	private static final String TAG = "StaticDatabase";

	public StaticDatabase(Context context, int resource, Row row) {
		super((new StaticDatabaseHelper(context, row.getName(), resource)), row.getName(), row);
	}
}
