package edu.ucla.cens.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StaticDatabaseHelper extends SQLiteOpenHelper {
	private final Context ctx;
	private String name;

	public StaticDatabaseHelper(Context ctx, String name, int resource) {
		super(ctx, name, null, 1);
		this.ctx = ctx;
		try {
			copyDatabase(name, resource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	private void copyDatabase(String name, int resource) throws IOException {

		// don't copy the database again if it exists
		if (new File("/data/data/edu.ucla.cens.budburst/databases/" + name).exists())
			return;

		// first make the directory if it doesn't exist
		new File("/data/data/edu.ucla.cens.budburst/databases/").mkdirs();

		OutputStream databaseOutputStream = new FileOutputStream("/data/data/edu.ucla.cens.budburst/databases/" + name);
		InputStream databaseInputStream;

		byte[] buffer = new byte[1024];
		databaseInputStream = ctx.getResources().openRawResource(resource);
		while ((databaseInputStream.read(buffer)) > 0) {
			databaseOutputStream.write(buffer);
		}
		databaseInputStream.close();

		databaseOutputStream.flush();
		databaseOutputStream.close();
	}

}
