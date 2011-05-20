package com.hamdyghanem.holyquran;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.hamdyghanem.holyquran.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class MainActivity extends Activity {
	public static final String MYPREFS = "mySharedPreferences";
	int mode = Activity.MODE_PRIVATE;
	int duration = Toast.LENGTH_SHORT;
	ApplicationController AC;
	Gallery g;
	Typeface arabicFont = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			// final boolean customTitleSupported =
			// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			// android:theme="@android:style/Theme.NoTitleBar"
			setContentView(R.layout.main);

			// /////////CHANGE THE TITLE BAR/////////////// Typeface
			// arabicFont = Typeface.createFromAsset(getAssets(),
			// "fonts/DroidSansArabic.ttf");

			// if (customTitleSupported) {
			// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
			// R.layout.mytitle);
			// }

			// /final TextView myTitleText = (TextView)
			// findViewById(R.id.myTitle);
			// if (myTitleText != null) {
			// myTitleText.setTypeface(arabicFont);
			// myTitleText.setText(R.string.holyquran); //
			// myTitleText.setBackgroundColor(R.color.blackblue);
			// } //
			// ////////////////////

			//

			AC = (ApplicationController) getApplicationContext();
			// SharedPreferences mySharedPreferences = getSharedPreferences(
			// MYPREFS, mode);
			// Begin

			// Reference the Gallery view
			g = (Gallery) findViewById(R.id.Gallery01);
			g.setAdapter(new ImageAdapter(this));
			registerForContextMenu(g);
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.main, g, false);

			// Set the adapter to our custom adapter (below)
			//
			Boolean bFirstTime = false;
			String baseDir = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/hQuran";
			File file = new File(baseDir);
			if (!file.exists()) {
				file.mkdirs();
				bFirstTime = true;
			}
			// Check first time
			AC.bookmarkUtitliy = new BookmarkUtil(AC.ReadSettings());

			if (bFirstTime) {
				// Toast.makeText(this, "not exist",
				// Toast.LENGTH_LONG).show();
				file.mkdirs();
				file = new File(baseDir + "/tafseer/");
				file.mkdirs();
				file = new File(baseDir + "/img/");
				file.mkdirs();
				file = new File(baseDir + "/dictionary/");
				file.mkdirs();
				file = new File(baseDir + "/tareef/");
				file.mkdirs();

				ImageManager.saveToSDCard(BitmapFactory.decodeResource(
						this.getResources(), R.drawable.img_0), "0");
				ImageManager.saveToSDCard(BitmapFactory.decodeResource(
						this.getResources(), R.drawable.img_1), "1");
				ImageManager.saveToSDCard(BitmapFactory.decodeResource(
						this.getResources(), R.drawable.img_no), "no");
				// Open settings to load images directly
				g.setSelection(604);
				// callOptionsItemSelected(null, R.id.mnu_settings);
				Toast.makeText(this, getText(R.string.notexistimage),
						Toast.LENGTH_LONG).show();

			} else {
				// if (AC.NeedDownload())
				// callOptionsItemSelected(null, R.id.mnu_settings);
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
		AC.saveBookmarks(g.getSelectedItemPosition());
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.hq_menu, menu);
		//
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return callOptionsItemSelected(item, item.getItemId());
	}

	public boolean callOptionsItemSelected(MenuItem item, int iItem) {
		// Save Now
		AC.saveBookmarks(g.getSelectedItemPosition());

		// Handle item selection
		switch (iItem) {
		case R.id.mnu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
			// case R.id.mnu_tafseer:
		case R.id.mnu_details:
			startActivity(new Intent(this, DetailsMenuActivity.class));
			// registerForContextMenu( t );
			// openContextMenu(g);
			return true;
		case R.id.mnu_search:
			startActivityForResult(new Intent(this, SearchActivity.class), 3);
			// registerForContextMenu( t );
			// openContextMenu(g);
			return true;
		case R.id.mnu_bookmark:
			// startActivity(new Intent(this, BookmarksActivity.class));
			startActivityForResult(new Intent(this, BookmarksActivity.class), 1);
			return true;
			// case R.id.mnu_newbookmark:
			// startActivityForResult(
			// new Intent(this, BookmarkEditActivity.class), 1);
			// return true;
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
		// Toast.makeText(this, "aaaaaaaaaaa", Toast.LENGTH_LONG).show();
		// Toast.makeText(this, Integer.toString(requestCode),
		// Toast.LENGTH_LONG).show();

		if (requestCode == 1) {
			if (!(data == null || data.getExtras() == null)) {

				Bundle extras = data.getExtras();
				Integer i = extras.getInt("returnKey");
				AC.bookmarkUtitliy.setDefault(i);
				// Toast.makeText(this, Integer.toString(i), Toast.LENGTH_LONG)
				// .show();
				AC.saveBookmarksDefalut();
			}
			// Toast.makeText(this, "Request failed: " + Integer.toString(i),
			// Toast.LENGTH_LONG).show();
			//
			g.setSelection(604 - AC.bookmarkUtitliy.arr.get(
					AC.bookmarkUtitliy.getDefault()).getPage());
			// }
		} else if (requestCode == 3) {
			if (!(data == null || data.getExtras() == null)) {
				Bundle extras = data.getExtras();
				Integer i = extras.getInt("returnKey");
				
				 
				g.setSelection(604 - i);
			}
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.holyquran);
		menu.add(0, R.id.mnu_bookmark, 0, R.string.bookmark);
		menu.add(0, R.id.mnu_goto, 0, R.string.GoToActivity);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		callOptionsItemSelected(null, item.getItemId());
		return true;
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
			// TypedArray a = obtainStyledAttributes(R.styleable.Gallery01);
			// mGalleryItemBackground = a.getResourceId(
			// R.styleable.Gallery01_android_galleryItemBackground, 0);
			// a.recycle();
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
					.getAbsolutePath() + "/hQuran/img/";
			// imgView.setScaleType(ScaleType.FIT_XY);

			// to let it work like Arabic we will subtract position by 604
			// imgView.setImageResource(mImageIds[position]);
			String strFile = baseDir + Integer.toString(604 - position)
					+ ".gif";

			// http://dl.dropbox.com/u/27675084/img/9.gif
			File f = new File(strFile);
			if (!f.exists()) {
				// ImageManager.DownloadFromUrl(Integer.toString(position),strFile);
				// callOptionsItemSelected(null, R.id.mnu_settings);
				strFile = baseDir + "no.gif";
				Drawable d = Drawable.createFromPath(strFile);
				imgView.setImageDrawable(d);
			} else {
				Drawable d = Drawable.createFromPath(strFile);
				imgView.setImageDrawable(d);
			}

			Display display = getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			int height = display.getHeight();
			// matrix.setScale(scale, scale);
			imgView.setPadding(1, 1, 1, 1);

			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				float myFlo = (float) (width * 1.5);
				height = (int) myFlo;
				imgView.setLayoutParams(new Gallery.LayoutParams(width,
						height - 50));
				imgView.setScaleType(ImageView.ScaleType.FIT_XY);
			} else { //
				imgView.setLayoutParams(new Gallery.LayoutParams(width, //
						height - 50));
				imgView.setScaleType(ImageView.ScaleType.FIT_XY);
			}

			// imgView.setLayoutParams(new Gallery.LayoutParams(width, height -
			// 50));

			imgView.setScaleType(ImageView.ScaleType.FIT_XY);
			// imgView.setScaleType(ScaleType.MATRIX);
			// Matrix matrix=new Matrix();
			// imgView.setMinimumWidth(width);
			// imgView.setMinimumHeight(height);

			// imgView.setMaxWidth(width);
			// imgView.setMaxHeight(height);

			imgView.setAdjustViewBounds(true);
			imgView.setBackgroundColor(getTitleColor());

			// imgView.setBackgroundResource(mGalleryItemBackground);

			// imgView. setOnLongClickListener(new OnLongClickListener() {
			// @Override
			// public boolean onLongClick(View v) {
			// MainActivity ma = (MainActivity) mContext;
			// return ma.callOptionsItemSelected(null, R.id.mnu_bookmark);
			// }
			// });

			return imgView;
		}
	}

}
