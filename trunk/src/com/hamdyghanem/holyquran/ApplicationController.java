package com.hamdyghanem.holyquran;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Application;
import android.os.Environment;
import android.widget.Toast;

public class ApplicationController extends Application {
	public BookmarkUtil bookmarkUtitliy;
	public Integer iCurrentPage = 0;
	public Integer iLanguage = 0;

	@Override
	public void onCreate() {

		super.onCreate();

		// Do Application initialization over here
	}

	public Boolean NeedDownload() {
		String baseDir = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/hQuran/img/";
		File file = new File(baseDir);
		return file.listFiles().length < 604;
	}

	public void saveBookmarksDefalut() {
		WriteBookmarks(bookmarkUtitliy.getBookmarksString());
	}

	public void saveBookmarks(Integer iPos) {
		// check the static value
		iCurrentPage = 604 - iPos;
		if (bookmarkUtitliy.arr.get(bookmarkUtitliy.getDefault()).getStatic() == 0) {
			//
			bookmarkUtitliy.arr.get(bookmarkUtitliy.getDefault()).setPage(
					604 - iPos);
			WriteBookmarks(bookmarkUtitliy.getBookmarksString());
		}
	}

	public void WriteBookmarks(String data) {
		String strFileBookmarks = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "hQuran"
				+ File.separator
				+ "bookmarks.dat";

		File file = new File(strFileBookmarks);
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write(data);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		}
		// Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
	}

	public String ReadBookmarks() {
		String strFileBookmarks = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "hQuran"
				+ File.separator
				+ "bookmarks.dat";
		// SaveBookmark
		String data = getString(R.string.defaultbookmark);
		File file = new File(strFileBookmarks);
		if (!file.exists())
			WriteBookmarks(getString(R.string.defaultbookmark));
		FileReader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(reader);
		String thisLine = null;
		try {
			while ((thisLine = br.readLine()) != null) {
				data = thisLine;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Toast.makeText(this, "Request failed: " + data,
		// Toast.LENGTH_LONG).show();
		// Toast.makeText(this, data, Toast.LENGTH_SHORT).show();

		return data.trim();

	}

	public void WriteSettings() {
		String data = "";
		String strFileSettings = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "hQuran"
				+ File.separator
				+ "Settings.dat";
		data = Integer.toString(iLanguage) + "\n";
		File file = new File(strFileSettings);
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write(data);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		}
		// Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
	}

	public void ReadSettings() {
		String strFileSettings = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "hQuran"
				+ File.separator
				+ "Settings.dat";
		String data = "";
		File file = new File(strFileSettings);
		if (!file.exists())
			WriteSettings();
		FileReader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(reader);
		String thisLine = null;
		try {
			while ((thisLine = br.readLine()) != null) {
				data = thisLine;
			}

			String[] separated = data.split("\n");
			iLanguage = Integer.parseInt(separated[0]);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String GetSora(Integer iPage) {
		String[] soranames = getResources().getStringArray(
				R.array.SoraName_array);
		String[] sorapages = getResources().getStringArray(
				R.array.SoraValue_array);
		if (iPage <= 0)
			iPage = 1;
		if (iPage > 604)
			iPage = 604;
		// if (i == 591) i = 5911;
		// if (i >= 595) i = Integer.parseInt(Integer.toString(i) + "1");
		;
		for (Integer i = 0; i < sorapages.length; i++) {
			if (iPage < Integer.parseInt(sorapages[i]))
				return soranames[i - 1].trim();
		}
		return "";
	}

	public Integer GetSoraIndex(Integer iPage) {
		String[] sorapages = getResources().getStringArray(
				R.array.SoraValue_array);
		if (iPage <= 0)
			iPage = 1;
		if (iPage > 604)
			iPage = 604;
		// if (i == 591) i = 5911;
		// if (i >= 595) i = Integer.parseInt(Integer.toString(i) + "1");
		for (Integer i = 0; i < sorapages.length; i++) {
			if (iPage < Integer.parseInt(sorapages[i]))
				return i - 1;
		}
		return 0;
	}

	public Integer GetSoraPage(Integer iPage) {
		String[] sorapages = getResources().getStringArray(
				R.array.SoraValue_array);
		if (iPage <= 0)
			iPage = 1;
		if (iPage > 604)
			iPage = 604;
		// if (i == 591) i = 5911;
		// if (i >= 595) i = Integer.parseInt(Integer.toString(i) + "1");
		for (Integer i = 0; i < sorapages.length; i++) {
			if (iPage < Integer.parseInt(sorapages[i]))
				return Integer.parseInt(sorapages[i - 1]);
		}
		return 0;

	}

	public CharSequence getTextbyLanguage(int i) {
		// Button but = (Button) findViewById(R.id.Button01);
		// but.setText(AC.getTextbyLanguage(R.string.hello));
		if (iLanguage == 0)
			return getText(i);
		else
			return getText(i + 1);
	}
}
