package com.hamdyghanem.holyquran;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.hamdyghanem.holyquran.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String MYPREFS = "mySharedPreferences";
	int mode = Activity.MODE_PRIVATE;
	int duration = Toast.LENGTH_SHORT;
	ApplicationController AC;
	Gallery g;
	String DefBookmarks = "Al-Wird,0,1#Al-Kahf,293,0";
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);
			// Create Folder
			AC = (ApplicationController) getApplicationContext();
			SharedPreferences mySharedPreferences = getSharedPreferences(
					MYPREFS, mode);
			// Begin
			
			// Reference the Gallery view
			g = (Gallery) findViewById(R.id.Gallery01);
			g.setAdapter(new ImageAdapter(this));
			// Set the adapter to our custom adapter (below)
			//
			String baseDir = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/hQuran";
			File file = new File(baseDir);
			// Check first time
			
			if (!file.exists()) {
				WriteSettings(DefBookmarks);
				// Toast.makeText(this, "not exist",
				// Toast.LENGTH_LONG).show();
				file.mkdirs();
				file = new File(baseDir + "/tafseer/");
				file.mkdirs();
				file = new File(baseDir + "/img/");
				file.mkdirs();
				ImageManager.saveToSDCard(BitmapFactory.decodeResource(this
						.getResources(), R.drawable.img_0), "0");
				ImageManager.saveToSDCard(BitmapFactory.decodeResource(this
						.getResources(), R.drawable.img_1), "1");
				ImageManager.saveToSDCard(BitmapFactory.decodeResource(this
						.getResources(), R.drawable.img_no), "no");
				// Open settings to load images directly
				g.setSelection(604);
				callOptionsItemSelected(null, R.id.mnu_settings);
				AC.bookmarkUtitliy = new BookmarkUtil(mySharedPreferences,
						ReadSettings());
			
			} else {
				AC.bookmarkUtitliy = new BookmarkUtil(mySharedPreferences,
						ReadSettings());
			
				file = new File(baseDir + "/tafseer/");
				if (!file.exists())
					file.mkdirs();

				if (AC.NeedDownload())
					callOptionsItemSelected(null, R.id.mnu_settings);
				g.setSelection(604 - AC.bookmarkUtitliy.arr.get(
						AC.bookmarkUtitliy.getDefault()).getPage());
			}
		} catch (Throwable t) {
			Toast.makeText(this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onStop() {
		saveBookmarks();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.hq_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return callOptionsItemSelected(item, item.getItemId());
	}

	private void saveBookmarks() {
		AC.bookmarkUtitliy.arr.get(AC.bookmarkUtitliy.getDefault()).setPage(
				604 - g.getSelectedItemPosition());
		String strData = AC.bookmarkUtitliy.saveBookmarks();
		WriteSettings(strData);
	}

	public boolean callOptionsItemSelected(MenuItem item, int iItem) {
		// Save Now
		saveBookmarks();

		// Handle item selection
		switch (iItem) {
		case R.id.mnu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.mnu_tafseer:
			startActivity(new Intent(this, TafseerActivity.class));
			return true;

		case R.id.mnu_bookmark:
			// startActivity(new Intent(this, BookmarksActivity.class));
			startActivityForResult(new Intent(this, BookmarksActivity.class), 1);
			return true;
		case R.id.mnu_newbookmark:
			startActivityForResult(
					new Intent(this, BookmarkEditActivity.class), 1);
			return true;
		case R.id.mnu_goto:
			startActivityForResult(new Intent(this, GoToActivity.class), 1);

			return true;
		case R.id.mnu_about:
			startActivity(new Intent(this, AboutActivity.class));
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (data == null || data.getExtras() == null)
				return;
			Bundle extras = data.getExtras();
			Integer i = extras.getInt("returnKey");
			// Toast.makeText(this,
			// if (AC.bookmarkUtitliy.getDefault() != i) {
			AC.bookmarkUtitliy.setDefault(i);
			//
			i = AC.bookmarkUtitliy.arr.get(AC.bookmarkUtitliy.getDefault())
					.getPage();
			// Toast.makeText(this, "Request failed: " + Integer.toString(i),
			// Toast.LENGTH_LONG).show();
			//
			g.setSelection(604 - AC.bookmarkUtitliy.arr.get(
					AC.bookmarkUtitliy.getDefault()).getPage());
			// }
		}
	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		// Toast.makeText(this, "Request failed: " + t.toString(),
		// Toast.LENGTH_LONG).show();
		// for (Bookmark b : bookmarkUtitliy.arr) {
		// String str = b.getBookmarkName();

		private Context mContext;
		File path = Environment.getExternalStorageDirectory();

		public ImageAdapter(Context c) {
			mContext = c;

			// See res/values/attrs.xml for the <declare-styleable> that defines
			// Gallery1.
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
			mGalleryItemBackground = a.getResourceId(
					R.styleable.Gallery1_android_galleryItemBackground, 0);
			a.recycle();
		}

		public int getCount() {
			// return mImageIds.length;
			return 605;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imgView = new ImageView(mContext);
			String baseDir = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/hQuran/img/";

			/*
			 * int i =
			 * AC.bookmarkUtitliy.arr.get(AC.bookmarkUtitliy.getDefault())
			 * .getPage(); Toast.makeText(mContext, Integer.toString(i),
			 * Toast.LENGTH_LONG) .show();
			 */
			// Toast.makeText(mContext, Integer.toString(position),
			// Toast.LENGTH_LONG)
			// .show();
			// to let it work like Arabic we will subtract position by 604
			// imgView.setImageResource(mImageIds[position]);
			String strFile = baseDir + Integer.toString(604 - position)
					+ ".gif";

			// http://dl.dropbox.com/u/27675084/img/9.gif
			File f = new File(strFile);
			if (!f.exists()) {
				// Toast.makeText(mContext, R.string.notexistpage,
				// Toast.LENGTH_LONG).show();
				// Download
				// ImageManager.DownloadFromUrl(Integer.toString(position),strFile);
				// callOptionsItemSelected(null, R.id.mnu_settings);
				strFile = baseDir + "no.gif";
				Drawable d = Drawable.createFromPath(strFile);
				imgView.setImageDrawable(d);
			} else {
				Drawable d = Drawable.createFromPath(strFile);
				imgView.setImageDrawable(d);
			}
			// i.setScaleType(ImageView.ScaleType.FIT_XY);
			// i.setLayoutParams(new Gallery.LayoutParams(136, 88));

			// The preferred Gallery item background
			imgView.setBackgroundResource(mGalleryItemBackground);
			// imgView. setOnLongClickListener(new OnLongClickListener() {
			// @Override
			// public boolean onLongClick(View v) {
			// MainActivity ma = (MainActivity) mContext;
			// return ma.callOptionsItemSelected(null, R.id.mnu_bookmark);
			// }
			// });

			return imgView;
		}
		// private Integer[] mImageIds = { R.drawable.img_10, R.drawable.img_11,
		// R.drawable.img_12, R.drawable.img_13, R.drawable.img_14,
		// R.drawable.img_15, R.drawable.img_16, R.drawable.img_17,
		// R.drawable.img_18, R.drawable.img_19, R.drawable.img_20 };
	}

	public void WriteSettings(String data) {
		FileOutputStream fOut = null;
		OutputStreamWriter osw = null;
		try {
			fOut = openFileOutput("bookmarks.dat", MODE_PRIVATE);
			osw = new OutputStreamWriter(fOut);
			osw.write(data);
			osw.flush();
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("err ->", e.toString());
		} finally {
			try {
				osw.close();
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.d("err ->", e.toString());
			}
		}
	}

	public String ReadSettings() {
		String strFile="\\data\\data\\com.hamdyghanem.holyquran\files\bookmarks.dat";
		File f=new File(strFile);
		if (!f.exists())
			WriteSettings(DefBookmarks);
		
		// SaveBookmark
		String data = "Al-Wird,0,1#Al-Kahf,293,0";
		//File file = new File("bookmarks.dat");
		FileInputStream fIn = null;
		InputStreamReader isr = null;
		char[] inputBuffer = new char[255];
		try {
			fIn = openFileInput("bookmarks.dat");
			isr = new InputStreamReader(fIn);
			isr.read(inputBuffer);
			data = new String(inputBuffer);
		} catch (Exception e) {
			Log.d("err ->", e.toString());
			WriteSettings(DefBookmarks);
			e.printStackTrace();
		} finally {
			try {
				isr.close();
				fIn.close();
			} catch (IOException e) {
				Log.d("err ->", e.toString());
				e.printStackTrace();
			}
		}
		// Toast.makeText(this, "Request failed: " + data,
		// Toast.LENGTH_LONG).show();
		return data.trim();

	}

}