package edu.ucla.cens.database;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public abstract class Row {
	private static final String TAG = "Row";
	public Long _id;
	protected Context mContext;
	protected WritableDatabase db;

	public Row(Context context) {
		mContext = context;
		db = new WritableDatabase(this);
	}

	protected Context getContext() {
		return mContext;
	}

	public ContentValues vals() {
		ContentValues vals = new ContentValues();

		Field[] fields = getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				Object o = fields[i].get(this);
				if (o != null)
					vals.put(fields[i].getName(), o.toString());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return vals;
	}

	public Row newRow() {
		try {
			return this.getClass().newInstance();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void readCursor(Cursor c) {
		Field[] fields = getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				if (fields[i].getType().equals(Long.class))
					fields[i].set(this, c.getLong(i));
				else if (fields[i].getType().equals(String.class))
					fields[i].set(this, c.getString(i));
				else if (fields[i].getType().equals(Boolean.class))
					fields[i].set(this, c.getString(i).equals("true"));
				else if (fields[i].getType().equals(Double.class))
					fields[i].set(this, c.getDouble(i));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		String ret = this.getClass().getSimpleName();
		Field[] fields = getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				try {
					ret += " " + fields[i].getName() + ":" + fields[i].get(this);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}

	public Field[] getFields() {
		ArrayList<Field> ret = new ArrayList<Field>();
		Field[] fields = this.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			if (Modifier.isPublic(fields[i].getModifiers()))
				ret.add(fields[i]);
		}
		return ret.toArray(new Field[0]);
	}

	// returns the name of the database for this row (just the name before Row)
	public String getName() {
		return this.getClass().getSimpleName().toLowerCase().split("row")[0];
	}

	public ArrayList<String> primaryKeys() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add("_id");
		return ret;
	}

	protected Row hasOne(Row row, Long id) {
		return new Database(row).find(id);
	}
	//
	// protected ArrayList<Row> hasMany(Row rows, String filter) {
	// ArrayList<Row> this_that = new Database(getName() + "_" +
	// rows.getName()).find(getName() + "_id=" + _id);
	// for (Iterator<Row> i = this_that.iterator(); i.hasNext();) {
	// Log.d(TAG, i.next().toString());
	// }
	// if (filter != null && !filter.trim().equals(""))
	// filter += " AND ";
	// return new Database(rows).find(this_that, rows.getName() + "_id",
	// filter);
	// }
	//
	// protected ArrayList<Row> hasMany(String name) {
	// return hasMany(name, "");
	// }

	public void put() {
		db.insertRow(this);
	}

	public ArrayList<NameValuePair> parameters() {
		ArrayList<NameValuePair> ret = new ArrayList<NameValuePair>();
		Field[] fields = getFields();

		for (int i = 0; i < fields.length; i++) {
			try {
				ret.add(new BasicNameValuePair(fields[i].getName(), fields[i].get(this).toString()));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub
		return ret;
	}

}