/*This Code Devleoped by :Hamdy Ghanem
 *  
 * You can use this code to create your own program or to leearn
 * but without remving this comments
 * 
 *  for any queation or help you can contact me
 *  hamdy.ghanem@gmail.com
 *  
 *  The Fan page of my program is
 *  https://www.facebook.com/HolyQuran4Android
 */
package com.hamdyghanem.holyquran;

import java.util.ArrayList;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * The HelloWorldApp class implements an application that simply displays
 * "Hello World!" to the standard output.
 */

public class BookmarkUtil {
	public ArrayList<Bookmark> arr = new ArrayList<Bookmark>();
	private Integer iDefault = 0;

	// SharedPreferences mySharedPreferences;
	// SharedPreferences.Editor editor;

	// public BookmarkUtil(SharedPreferences myShared, String strBookmarks) {
	// mySharedPreferences = myShared;
	// editor = mySharedPreferences.edit();
	// LoadBookmarks(strBookmarks);
	// }
	public BookmarkUtil(String strBookmarks) {
		LoadBookmarks(strBookmarks);
	}

	
	protected String getBookmarksString() {
		String strBookmarks = "";
		for (Bookmark b : arr) {
			strBookmarks += b.getBookmarkName() + ","
					+ Integer.toString(b.getPage()) + ","
					+ Integer.toString(b.getStatic()) + ","
					+ Integer.toString(b.getDefault()) + "#";
		}
		strBookmarks = strBookmarks.substring(0, strBookmarks.length() - 1);
		return strBookmarks;
	}

	protected Integer getDefault() {
		return iDefault;
	}

	protected void setDefault(Integer iDef) {
		iDefault = iDef;
		for (Bookmark b : arr) {
			b.setDefault(0);
		}
		arr.get(iDefault).setDefault(1);
	}

	// protected void InitiateBookmars() {
	// arr.clear();
	// arr.add(new Bookmark("AlWird", 1, 1));
	// arr.add(new Bookmark("AlKahf", 300, 0));

	// editor.putString("Bookmarks", DefBookmarks);
	// editor.commit();
	// iDefault = 0;
	// }

	protected void LoadBookmarks(String strBookmarks) {
		try {
			arr.clear();

			String[] row = strBookmarks.split("#");
			for (int i = 0; i < row.length; i++) {
				String[] col = row[i].split(",");
				if (Integer.parseInt(col[3].trim()) == 1)
					iDefault = i;
				arr.add(new Bookmark(col[0], Integer.parseInt(col[1].trim()),
						Integer.parseInt(col[2].trim()), Integer
								.parseInt(col[3].trim())));

			}
		} catch (Throwable t) {
			Log.d("err ->", t.toString());
			// InitiateBookmars();
		}
	}
}
