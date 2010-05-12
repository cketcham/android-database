package edu.ucla.cens.database;

import java.util.HashMap;

import edu.ucla.cens.database.Database;
import edu.ucla.cens.database.Row;
import edu.ucla.cens.database.StaticDatabase;
import edu.ucla.cens.database.SyncableDatabase;
import edu.ucla.cens.database.SyncableRow;

import android.content.Context;

public class DatabaseManager {
	private final Context context;
	private final HashMap<String, Integer> resources = new HashMap<String, Integer>();
	private final HashMap<String, Row> rows = new HashMap<String, Row>();
	private final HashMap<String, String> upUrls = new HashMap<String, String>();
	private final HashMap<String, String> downUrls = new HashMap<String, String>();

	public DatabaseManager(Context context) {
		this.context = context;
	}

	public void createDatabase(String name, int resource, Row row) {
		resources.put(name, resource);
		rows.put(name, row);
	}

	public void createSyncableDatabase(String name, String downUrl, String upUrl, SyncableRow row) {
		upUrls.put(name, upUrl);
		downUrls.put(name, downUrl);
		rows.put(name, row);
	}

	public void createSyncableDatabase(String name, int resource, String downUrl, String upUrl, SyncableRow row) {
		createSyncableDatabase(name, downUrl, upUrl, row);
		createDatabase(name, resource, row);
	}

	public Database getDatabase(String name) {
		if (!downUrls.containsKey(name) || !upUrls.containsKey(name))
			return new StaticDatabase(context, resources.get(name), rows.get(name));
		else if (downUrls.containsKey(name) && upUrls.containsKey(name) && resources.containsKey(name))
			return new SyncableDatabase(context, resources.get(name), downUrls.get(name), upUrls.get(name), (SyncableRow) rows.get(name));
		else if (downUrls.containsKey(name) && upUrls.containsKey(name))
			return new SyncableDatabase(context, downUrls.get(name), upUrls.get(name), (SyncableRow) rows.get(name));
		return null;
	}
}
