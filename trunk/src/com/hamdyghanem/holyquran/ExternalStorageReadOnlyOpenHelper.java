package com.hamdyghanem.holyquran;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.AndroidRuntimeException;
import android.widget.Toast;

public class ExternalStorageReadOnlyOpenHelper extends Application {
	private SQLiteDatabase database;
	private File dbFile;
	private SQLiteDatabase.CursorFactory factory;
	private String DB_NAME = "hquran.dat";
	private String DB_PATH = Environment.getExternalStorageDirectory()
			+ "/hQuran/";
	private final Context myContext;

	public ExternalStorageReadOnlyOpenHelper(Context context) {
		// this.factory = factory;
		this.myContext = context;
		this.dbFile = new File(DB_PATH, DB_NAME);
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			throw new AndroidRuntimeException(
					"External storage (SD-Card) not mounted");
		}
		File appDbDir = new File(DB_PATH);
		if (!appDbDir.exists()) {
			appDbDir.mkdirs();
		}
	}

	public boolean databaseFileExists() {
		return dbFile.exists();
	}

	private void open() {
		if (dbFile.exists()) {
			database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(),
					factory, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		}
	}

	public void close() {
		if (database != null) {
			database.close();
			database = null;
		}
	}

	public SQLiteDatabase getReadableDatabase() {
		return getDatabase();
	}

	private SQLiteDatabase getDatabase() {
		if (database == null) {
			open();
		}
		return database;
	}

	public String getID(String pqr, SQLiteDatabase db) {
		String data = "";
		String count = "SELECT count(*) FROM quran";

		/*
		 * Cursor mCursor = db.query(true, "quran", new String[] { "docid",
		 * "surah", "verse" }, "verse" + "=?", new String[] { pqr }, null,
		 * null,null, null);
		 */
		Cursor mcursor = db.rawQuery(count, null);
		mcursor.moveToFirst();
		int icount = mcursor.getInt(0);

		/*
		 * if (mCursor != null) { mCursor.moveToFirst(); if
		 * (mCursor.moveToFirst()) { do { data = mCursor.getString(1); // do
		 * what ever you want here } while (mCursor.moveToNext()); }
		 */
		mcursor.close();

		return Integer.toString(icount);
	}

	public Cursor getQuery(String pqr, SQLiteDatabase db) {
		// String data = "";
		// String count = "SELECT count(*) FROM quran";

		// Cursor cursor= db.query(TABLE_IMAGES, new String[]{"_id"}, "name"
		// +" = ?", new String[]{compareToThis}, null, null, null);

		Cursor mcursor = db.query("quran", new String[] { "page", "surah",
				"verse", "content" }, "content like " + "'%" + pqr + "%'",
				null, null, null, null);

		// Cursor mcursor = db.rawQuery(count, null);
		mcursor.moveToFirst();

		/*
		 * if (mCursor != null) { mCursor.moveToFirst(); if
		 * (mCursor.moveToFirst()) { do { data = mCursor.getString(1); // do
		 * what ever you want here } while (mCursor.moveToNext()); }
		 */
		// mcursor.close();

		return mcursor;
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

}