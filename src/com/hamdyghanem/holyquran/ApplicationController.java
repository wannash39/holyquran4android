package com.hamdyghanem.holyquran;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

public class ApplicationController extends Application {
	public BookmarkUtil bookmarkUtitliy;
	public Integer iCurrentPage = 0;
	public Integer iCurrentAya = -1;
	public Integer iCurrenSura = 0;

	public Integer iLanguage = 0;
	public String CurrentImageType = "0";
	public String LastVersion = "";
	public Boolean AudioOn = false;

	public String ActivePath = "http://dl.dropbox.com/u/32200142/";// gmail:
																	// 27675084
																	// yahoo:32200142
	public boolean ScreenOn = false;

	@Override
	public void onCreate() {

		super.onCreate();

		// Do Application initialization over here
	}

	/*public Boolean NeedDownload() {
		String baseDir = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/hQuran/img/";
		File file = new File(baseDir);
		return file.listFiles().length < 604;
	}*/

	public void saveBookmarksDefalut() {
		WriteBookmarks(bookmarkUtitliy.getBookmarksString());
	}

	public Boolean isNetConnected() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// ARE WE CONNECTED TO THE NET

		if (conMgr.getActiveNetworkInfo() != null

		&& conMgr.getActiveNetworkInfo().isAvailable()

		&& conMgr.getActiveNetworkInfo().isConnected()) {

			return true;

		} else {
			return false;

		}
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

	/*
	 * public void WriteSettings() { String data = ""; String strFileSettings =
	 * Environment.getExternalStorageDirectory() .getAbsolutePath() +
	 * File.separator + "hQuran" + File.separator + "Settings.dat"; data =
	 * Integer.toString(iLanguage) + "#"; File file = new File(strFileSettings);
	 * FileWriter writer; try { writer = new FileWriter(file);
	 * writer.write(data); writer.flush(); writer.close(); } catch (IOException
	 * e) { // TODO Auto-generated catch block e.printStackTrace();
	 * Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show(); } //
	 * Toast.makeText(this, data, Toast.LENGTH_SHORT).show(); }
	 */

	/*
	 * public void ReadSettings() {
	 * 
	 * iLanguage = mySharedPreferences.getInt("language_preference", 0);
	 */
	/*
	 * String strFileSettings = Environment.getExternalStorageDirectory()
	 * .getAbsolutePath() + File.separator + "hQuran" + File.separator +
	 * "Settings.dat"; String data = ""; File file = new File(strFileSettings);
	 * if (!file.exists()) WriteSettings(); FileReader reader = null; try {
	 * reader = new FileReader(file); } catch (FileNotFoundException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } BufferedReader br
	 * = new BufferedReader(reader); String thisLine = null; try { while
	 * ((thisLine = br.readLine()) != null) { data += thisLine; } String[]
	 * separated = data.split("#"); if (separated.length > 0) if (separated[0]
	 * != "1" | separated[0] != "0") { file.delete(); WriteSettings(); } else
	 * iLanguage = Integer.parseInt(separated[0]); //if (separated.length > 1)
	 * // bScreenOn = Boolean.parseBoolean(separated[1]); // meanse there is new
	 * settings if (separated.length <= 1) { file.delete(); WriteSettings(); } }
	 * catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); file.delete(); WriteSettings(); }
	 */
	// }
	public void GetActivePath() {
		ImageManager.DownloadActivePath();
		String strFile = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "hQuran"
				+ File.separator
				+ "ActivePath.txt";
		ActivePath = getString(R.string.defaultbookmark);
		File file = new File(strFile);
		ActivePath = "http://dl.dropbox.com/u/32200142/";
		if (file.exists()) {
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
					ActivePath = thisLine;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String GetChapter(Integer iPage) {
		String[] chapternames = getResources().getStringArray(
				R.array.Chapter_array);
		if (iLanguage == 1)
			chapternames = getResources().getStringArray(
					R.array.Chapter_array_en);
		Integer iChapter = 0;
		iChapter = Math.abs((iPage - 2) / 20);

		return chapternames[iChapter ].trim();

	}

	public String GetSora(Integer iPage) {
		String[] soranames = getResources().getStringArray(
				R.array.SoraName_array);
		if (iLanguage == 1)
			soranames = getResources().getStringArray(R.array.SoraNameEn_array);

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
			if (iPage == Integer.parseInt(sorapages[i]))
				return i + 1;
			if (iPage < Integer.parseInt(sorapages[i]))
				return i;
		}
		return 0;
	}

	public Integer GetSoraPage(Integer iSura) {
		String[] sorapages = getResources().getStringArray(
				R.array.SoraValue_array);
		if (iSura <= 0)
			iSura = 1;
		return Integer.parseInt(sorapages[iSura - 1]);

	}

	public String GetVerseByPage(Integer iPage) {
		// String[] sorapages = getResources().getStringArray(
		// R.array.PageVerse_array);
		String sorapage = "-1,1,6,17,25,30,38,49,58,62,70,77,84,89,94,102,106,113,120,127,135,142,146,154,164,170,177,182,187,191,197,203,211,216,220,225,231,234,238,246,249,253,257,260,265,270,275,282,283,1,10,16,23,30,38,46,53,62,71,78,84,92,101,109,116,122,133,141,149,154,158,166,174,181,187,195,1,7,12,15,20,24,27,34,38,45,52,60,66,75,80,87,92,95,102,106,114,122,128,135,141,148,155,163,171,1,3,6,10,14,18,24,32,37,42,46,51,58,65,71,78,84,91,96,104,109,114,1,9,19,28,36,45,53,60,69,74,82,91,95,102,111,119,125,131,138,143,147,152,158,1,12,23,31,38,44,52,58,68,74,82,88,96,105,121,131,138,144,150,156,160,164,171,179,188,196,1,9,17,26,34,41,46,53,62,70,1,7,14,21,27,32,37,41,48,55,62,69,73,80,87,94,100,107,112,118,123,1,7,15,21,26,34,43,54,62,71,79,89,98,1,6,13,20,29,38,46,54,63,72,82,89,98,109,1,5,15,23,31,38,44,53,64,70,79,87,96,104,1,6,14,19,29,35,1,6,11,19,25,34,43,1,16,22,52,71,1,7,15,27,35,43,55,65,73,80,88,94,103,111,119,1,8,18,28,39,50,59,67,76,87,97,1,5,16,21,28,35,46,54,62,75,84,98,1,12,26,39,52,65,77,1,13,38,52,65,77,88,99,114,126,1,11,25,36,45,58,73,82,91,102,1,6,16,24,31,39,47,56,65,73,1,18,28,43,60,75,90,105,1,11,21,28,32,37,44,54,59,1,3,12,21,33,44,56,68,1,20,40,61,84,112,137,160,184,207,1,14,23,36,45,56,64,77,1,6,14,22,29,36,44,51,60,71,78,1,7,15,24,31,39,46,53,1,6,16,25,33,42,51,1,12,20,29,1,12,21,1,7,16,23,31,36,44,51,55,63,1,8,15,23,32,40,1,4,12,19,31,39,1,13,28,41,55,71,1,25,52,77,103,127,154,1,17,27,43,62,1,6,11,22,32,41,48,57,68,1,8,17,26,34,41,50,59,67,78,1,12,21,30,39,47,1,11,16,23,32,45,1,11,23,34,48,61,74,1,19,40,1,14,23,1,6,15,21,29,1,12,20,30,1,10,16,24,1,5,12,1,16,1,7,31,1,15,32,1,27,1,7,28,1,19,42,1,17,51,1,4,12,19,25,1,7,12,1,4,10,17,1,6,1,6,1,1,5,1,10,1,6,1,8,1,13,1,17,1,9,1,11,1,11,1,14,1,1,19,1,1,6,1,20,1,1,17,1,1,1,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1";
		String[] sorapages = sorapage.split(",");
		Integer i = Integer.parseInt(sorapages[iPage - 1]);
		if (i == 1)
			i = 0;
		if (i == -1)
			i = 1;
		if (iCurrenSura == 9 && i > 0)
			i -= 1;
		return Integer.toString(i);
	}

	public String GetFirstRecitationFile(Integer iPage) {
		iCurrenSura = GetSoraIndex(iPage);
		String strFirstAya = GetVerseByPage(iPage);
		iCurrentAya = Integer.parseInt(strFirstAya);
		Integer iAya = iCurrentAya;
		// Only for toba
		// if (iCurrenSura == 9 && iAya > 0)
		// iCurrentAya -= 1;
		return GetFirstRecitationFile(iCurrenSura, iAya);
	}

	public String GetFirstRecitationFile() {
		return GetFirstRecitationFile(iCurrenSura, iCurrentAya);
	}

	public String GetFirstRecitationFile(Integer iSura, Integer iAya) {
		String strFirstAya = String.format("%03d", iAya);
		String strSoraIndex = String.format("%03d", iSura);
		return strSoraIndex + strFirstAya + ".aud";
	}

	public Integer getAyaCount(Integer iSura) {
		String sorapage = "7,286,200,176,120,165,206,75,129,109,123,111,43,52,99,128,111,110,98,135,112,78,118,64,77,227,93,88,69,60,34,30,73,54,45,83,182,88,75,85,54,53,89,59,37,35,38,29,18,45,60,49,62,55,78,96,29,22,24,13,14,11,11,18,12,12,30,52,52,44,28,28,20,56,40,31,50,40,46,42,29,19,36,25,22,17,19,26,30,20,15,21,11,8,8,19,5,8,8,11,11,8,3,9,5,4,7,3,6,3,5,4,5,6";
		String[] sorapages = sorapage.split(",");
		return Integer.parseInt(sorapages[iSura - 1]);
	}

	// By current page and current aya
	public Integer getAyaPage(Integer iSura, Integer iAya) {
		iCurrentPage = GetSoraPage(iSura);
		// For Toba only
		if (iSura == 9 && iAya > 0)
			iAya += 1;

		for (Integer i = iCurrentPage + 1; i <= 604; i++) {
			Integer j = Integer.parseInt(GetVerseByPage(i));
			if (j == 0) {
				iCurrentPage = i - 1;
				return iCurrentPage;
			}
			if (j == iAya) {
				iCurrentPage = i;
				return iCurrentPage;
			}

			if (j > iAya) {
				iCurrentPage = i - 1;
				return iCurrentPage;
			}
		}

		return iCurrentPage;
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
