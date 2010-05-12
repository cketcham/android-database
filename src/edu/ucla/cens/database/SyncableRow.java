package edu.ucla.cens.database;

import android.content.Context;

public abstract class SyncableRow extends Row {
	public SyncableRow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Boolean synced = false;

	// public MultipartEntity uploadEntity() {
	// MultipartEntity entity = new MultipartEntity();
	//
	// Field[] fields = getFields();
	// for (int i = 0; i < fields.length; i++) {
	// try {
	// entity.addPart(fields[i].getName(), new
	// StringBody(fields[i].get(this).toString()));
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// return entity;
	// }
}