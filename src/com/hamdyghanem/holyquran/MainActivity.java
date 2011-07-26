package com.hamdyghanem.holyquran;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.util.LangUtils;

import com.hamdyghanem.holyquran.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.preference.PreferenceManager;
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
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class MainActivity extends Activity {
	public static final String MYPREFS = "mySharedPreferences";
	int mode = Activity.MODE_WORLD_WRITEABLE;
	int duration = Toast.LENGTH_SHORT;
	ApplicationController AC;
	Gallery g;
	Button vwCurrentAya;
	TextView myTitleText;
	Typeface arabicFont = null;
	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;
	MediaPlayer mediaPlayer;
	private int stateMediaPlayer;
	private final int stateMP_Error = 0;
	private final int stateMP_NotStarter = 1;
	private final int stateMP_Playing = 2;
	private final int stateMP_Pausing = 3;
	private final int stateMP_Stop = 4;
	private final int stateMP_PlayNext = 5;
	private final int stateMP_PlayBack = 6;
	private ImageButton buttonPlayPause;
	private ImageButton buttonRecitationSettings;
	private String strCurrentAudioFileName = "";
	private String strCurrentAudioFilePath = "";
	private Integer iLastAya = 0;
	private String baseDir = "";
	//

	protected PowerManager pm;
	protected PowerManager.WakeLock mWakeLock;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			// final boolean customTitleSupported =
			// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			// Hide title
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.main);

			pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
					"My Tag");
			// /////////CHANGE THE TITLE BAR/////////////// Typeface
			arabicFont = Typeface.createFromAsset(getAssets(),
					"fonts/DroidSansArabic.ttf");
			AC = (ApplicationController) getApplicationContext();
			AC.GetActivePath();
			/*
			 * if (customTitleSupported) {
			 * getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
			 * R.layout.mytitle); }
			 * 
			 * myTitleText = (TextView) findViewById(R.id.myTitle); if
			 * (myTitleText != null) { myTitleText.setTypeface(arabicFont);
			 * myTitleText.setText(AC.getTextbyLanguage(R.string.holyquran)); //
			 * // myTitleText.setBackgroundColor(R.color.blackblue); } //
			 */
			//

			// mySharedPreferences = getSharedPreferences(
			// MYPREFS, mode);

			// Begin
			// RecetationLayout
			buttonPlayPause = (ImageButton) findViewById(R.id.buttonPlayPause);
			buttonRecitationSettings = (ImageButton) findViewById(R.id.buttonRecitationSettings);
			buttonRecitationSettings.setVisibility(View.GONE);

			vwCurrentAya = (Button) findViewById(R.id.vwCurrentAya);
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
			baseDir = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/hQuran/";
			File file = new File(baseDir);
			if (!file.exists()) {
				file.mkdirs();
				bFirstTime = true;
			}

			AC.bookmarkUtitliy = new BookmarkUtil(AC.ReadBookmarks());
			//Toast.makeText(this, AC.ActivePath, Toast.LENGTH_LONG).show();

			mySharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			ReadSettings();
			//message for the new version
			String versionName = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
			String strNewFeatures = "New features : Recitation , Please if you fins any error mail me first";
			if (!versionName.equals(AC.LastVersion)) {
				if (AC.iLanguage == 0)
					strNewFeatures = "�������� ������: ������� , ������  �� ���� ��� ���� �� ����� ";
				Toast.makeText(
						this,
						AC.getTextbyLanguage(R.string.newversion)
								+ strNewFeatures, Toast.LENGTH_LONG).show();
			}

			if (AC.ScreenOn) {
				this.mWakeLock.acquire();
			}

			// Check first time
			AC.ReadBookmarks();
			if (bFirstTime) {
				// Toast.makeText(this, "not exist",
				// Toast.LENGTH_LONG).show();
				CreateFolders();
				ImageManager.saveToSDCard(BitmapFactory.decodeResource(
						this.getResources(), R.drawable.img_0), "0");
				ImageManager.saveToSDCard(BitmapFactory.decodeResource(
						this.getResources(), R.drawable.img_1), "1");
				ImageManager.saveToSDCard(BitmapFactory.decodeResource(
						this.getResources(), R.drawable.img_no), "no");
				// Open settings to load images directly
				g.setSelection(604);
				// callOptionsItemSelected(null, R.id.mnu_settings);
				Toast.makeText(this,
						AC.getTextbyLanguage(R.string.notexistimage),
						Toast.LENGTH_LONG).show();

				startActivityForResult(new Intent(this,
						SelectLanguageActivity.class), 5);

			} else {
				// if (AC.NeedDownload())
				// callOptionsItemSelected(null, R.id.mnu_settings);
				AC.iCurrentPage = AC.bookmarkUtitliy.arr.get(
						AC.bookmarkUtitliy.getDefault()).getPage();
				AC.iCurrenSura = AC.GetSoraIndex(AC.iCurrentPage);
				g.setSelection(604 - AC.iCurrentPage);
			}
			CreateFolders();

		} catch (Throwable t) {
			Toast.makeText(this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	public void ReadSettings() {

		String strLanguage = mySharedPreferences.getString(
				"language_preference", "0");
		AC.iLanguage = Integer.parseInt(strLanguage);
		AC.LastVersion = mySharedPreferences.getString(
				"LastVersion_preference", "0");
		AC.AudioOn = mySharedPreferences.getBoolean("audioon_preference",
				AC.AudioOn);

		AC.ScreenOn = mySharedPreferences.getBoolean("screenon_preference",
				false);
		// Toast.makeText(this,
		// "Request failed: " + Integer.toString(AC.iLanguage),
		// Toast.LENGTH_LONG).show();
		LinearLayout lay = (LinearLayout) findViewById(R.id.RecetationLayout);
		if (AC.AudioOn)
			lay.setVisibility(1);
		else
			lay.setVisibility(View.GONE);
		// Toast.makeText(this,
		// "Request failed: " + Boolean.toString(AC.AudioOn),
		// Toast.LENGTH_LONG).show();
	}

	public void WriteSettings() {
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		String versionName = "";
		try {
			versionName = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Toast.makeText(this, "Request failed: " + e.toString(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		//
		editor.putString("LastVersion_preference", versionName);
		editor.putString("language_preference", Integer.toString(AC.iLanguage));
		editor.putBoolean("screenon_preference", AC.ScreenOn);
		editor.putBoolean("audioon_preference", AC.AudioOn);
		//
		editor.commit();

	}

	private void CreateFolders() {
		File file = new File(baseDir);
		file.mkdirs();
		file = new File(baseDir + "tafseer/");
		file.mkdirs();
		file = new File(baseDir + "img/");
		file.mkdirs();
		file = new File(baseDir + "dictionary/");
		file.mkdirs();
		file = new File(baseDir + "taareef/");
		file.mkdirs();
		file = new File(baseDir + "English/");
		file.mkdirs();
		file = new File(baseDir + "Audio/");
		file.mkdirs();
		file = new File(baseDir + "Audio/Mashary");
		file.mkdirs();

	}

	@Override
	public void onStop() {
		AC.saveBookmarks(g.getSelectedItemPosition());
		WriteSettings();
		StopRecitation(true);

		super.onStop();
	}

	@Override
	public void onDestroy() {
		try {
			if (AC.ScreenOn) {
				this.mWakeLock.release();
			}
			super.onDestroy();
		} catch (Throwable t) {
			Toast.makeText(this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.hq_menu, menu);
		//
		menu.getItem(0).setTitle(AC.getTextbyLanguage(R.string.bookmark));
		menu.getItem(1).setTitle(AC.getTextbyLanguage(R.string.gotomenu));
		menu.getItem(2).setTitle(AC.getTextbyLanguage(R.string.Search));
		menu.getItem(3).setTitle(AC.getTextbyLanguage(R.string.detailsmenu));
		menu.getItem(4).setTitle(AC.getTextbyLanguage(R.string.settings));
		menu.getItem(5).setTitle(AC.getTextbyLanguage(R.string.about));

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
		case -100:
			if (AC.iLanguage == 0)
				startActivity(new Intent(this, TafseerActivity.class));
			else
				startActivity(new Intent(this, EnglishActivity.class));
			return true;

		case R.id.mnu_settings:
			startActivityForResult(new Intent(this, SettingsActivity.class), 4);
			// startActivity(new Intent(this, SettingsActivity.class));

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
		try {
			// Toast.makeText(this, "aaaaaaaaaaa", Toast.LENGTH_LONG).show();
			// Toast.makeText(this, Integer.toString(requestCode),
			// Toast.LENGTH_LONG).show();

			if (requestCode == 1) {
				if (!(data == null || data.getExtras() == null)) {

					Bundle extras = data.getExtras();
					Integer i = extras.getInt("returnKey");
					AC.bookmarkUtitliy.setDefault(i);
					// Toast.makeText(this, Integer.toString(i),
					// Toast.LENGTH_LONG)
					// .show();
					AC.saveBookmarksDefalut();
				}
				// Toast.makeText(this, "Request failed: " +
				// Integer.toString(i),
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
			// Settinngs
			else if (requestCode == 4) {
				ReadSettings();
				WriteSettings();
				// Toast.makeText(this, Boolean.toString(AC.ScreenOn),
				// Toast.LENGTH_LONG).show();

				if (AC.ScreenOn)
					this.mWakeLock.acquire();
				else
					this.mWakeLock.release();

			} else if (requestCode == 5) {
				if (!(data == null || data.getExtras() == null)) {
					Bundle extras = data.getExtras();
					Integer i = extras.getInt("returnKey");
					AC.iLanguage = i;
					WriteSettings();
				}
			}/*
			 * else if (requestCode == 4) { this.mWakeLock =
			 * pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag"); if
			 * (AC.bScreenOn) { this.mWakeLock.acquire(); } else
			 * this.mWakeLock.release(); }
			 */

		} catch (Throwable t) {
			if (requestCode == 4) {
			} else {
				Toast.makeText(this, "Request failed: " + t.toString(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	// Recitiation methods
	public void OnPlayRecitation(View view) {
		AC.iCurrentPage = 604 - g.getSelectedItemPosition();

		// if (AC.iCurrentAya != -1) {
		// AC.iCurrentAya -= 1;
		// }
		if (stateMediaPlayer == stateMP_Playing) {
			mediaPlayer.pause();
			buttonPlayPause.setImageResource(R.drawable.play);
			// buttonPlayPause.setText("Play");
			// textState.setText("- PAUSING -");
			stateMediaPlayer = stateMP_Pausing;
			return;
		}
		if (stateMediaPlayer == stateMP_Pausing) {
			mediaPlayer.start();
			buttonPlayPause.setImageResource(R.drawable.pause);
			// buttonPlayPause.setText("Play");
			// textState.setText("- PAUSING -");
			stateMediaPlayer = stateMP_Playing;
			return;
		}
		PlayRecitation();
	}

	public void PlayRecitation() {
		try {

			// check the audio files

			// first time or after stop

			if (AC.iCurrentAya == -1) {
				AC.iCurrentAya = 0;
				strCurrentAudioFileName = AC
						.GetFirstRecitationFile(AC.iCurrentPage);
			} else {
				// Check if next sura
				if (AC.iCurrentAya == -2) {
					AC.iCurrentAya = -1;
				}
				AC.iCurrentAya += 1;
				strCurrentAudioFileName = AC.GetFirstRecitationFile();
			}

			strCurrentAudioFilePath = baseDir + "Audio/Mashary/"
					+ strCurrentAudioFileName;

			// Toast.makeText(this,Integer.toString(AC.iCurrentAya),
			// Toast.LENGTH_LONG).show();
			vwCurrentAya.setText(Integer.toString(AC.iCurrentPage) + "/"
					+ Integer.toString(AC.iCurrentAya));

			// buttonPlayPause.set(Integer.toString(AC.iCurrentAya));

			File f = new File(strCurrentAudioFilePath);
			//
			// Toast.makeText(this, strCurrentAudioFilePath, Toast.LENGTH_SHORT)
			// .show();
			/*
			 * Toast.makeText( this, Integer.toString( AC.iCurrenSura) + ">" +
			 * AC.GetSoraPage( AC.iCurrenSura) + ">" +
			 * Integer.toString(AC.getAyaPage(AC.iCurrenSura, AC.iCurrentAya)),
			 * Toast.LENGTH_LONG).show();
			 */
			if (AC.iCurrentAya > 0) {
				AC.iCurrentPage = AC.getAyaPage(AC.iCurrenSura, AC.iCurrentAya);
				g.setSelection(604 - AC.iCurrentPage);

			} else {
				AC.iCurrentPage = AC.getAyaPage(AC.iCurrenSura,
						AC.iCurrentAya + 1);
				g.setSelection(604 - AC.iCurrentPage);
			}
			// if the Verrese is not in this sura, means sura is finished
			// check if sura is finished
			Integer iAyaCount = AC.getAyaCount(AC.iCurrenSura);
			// Toast.makeText(this, Integer.toString(iAyaCount),
			// Toast.LENGTH_LONG).show();
			if (AC.iCurrentAya > iAyaCount) {
				// Toast.makeText(this, "Stopped ", Toast.LENGTH_SHORT).show();
				// StopRecitation(true);
				if (AC.iCurrenSura == 114) {
					// finished
					StopRecitation(true);
				} else {
					AC.iCurrenSura += 1;
					AC.iCurrentAya = -2;
					PlayRecitation();
				}
				return;
			}
			if (!f.exists()) {

				// open the download activity
				// AC.iCurrentPage = iPage;
				//
				StopRecitation(true);
				startActivity(new Intent(this, DownloadRecitationActivity.class));
				//

				return;
			}
			//
			buttonPlayPause.setImageResource(R.drawable.pause);
			initMediaPlayer();
			mediaPlayer.start();
			// buttonPlayPause.setText("Pause");
			// textState.setText("- PLAYING -");
			stateMediaPlayer = stateMP_Playing;
			//
			// Toast.makeText(this, strCurrentAudioFilePath, Toast.LENGTH_LONG)
			// .show();
		} catch (Throwable t) {

			Toast.makeText(this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	public void OnRecitationSettings(View view) {
	}

	public void OnPlayRecitationBack(View view) {
		StopRecitation(false);
		AC.iCurrentAya -= 2;
		if (AC.iCurrentAya < 0)
			AC.iCurrentAya = 0;

		stateMediaPlayer = stateMP_PlayBack;

		iLastAya -= 2;
		PlayRecitation();

	}

	public void OnPlayRecitationNext(View view) {
		StopRecitation(false);
		stateMediaPlayer = stateMP_PlayNext;
		PlayRecitation();

	}

	public void OnStopRecitation(View view) {
		iLastAya = 0;
		StopRecitation(true);

	}

	private void StopRecitation(Boolean bChangeCurrentAya) {
		if (bChangeCurrentAya) {
			AC.iCurrentAya = -1;
			vwCurrentAya.setText(Integer.toString(AC.iCurrentPage) + "/0");
		}// Toast.makeText(this, "OnStopRecitation", Toast.LENGTH_LONG).show();
		if (mediaPlayer != null)
			mediaPlayer.stop();
		// buttonPlayPause.setText("Pause");
		// textState.setText("- PLAYING -");
		stateMediaPlayer = stateMP_Stop;
		buttonPlayPause.setImageResource(R.drawable.play);
	}

	MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			initMediaPlayer();
			PlayRecitation();
			// buttonPlayPause.setEnabled(true);
		}
	};

	private void initMediaPlayer() {
		mediaPlayer = new MediaPlayer();

		mediaPlayer.setOnCompletionListener(completionListener);

		try {
			mediaPlayer.setDataSource(strCurrentAudioFilePath);
			mediaPlayer.prepare();
			// Toast.makeText(this, strCurrentAudioFilePath,
			// Toast.LENGTH_SHORT).show();
			stateMediaPlayer = stateMP_NotStarter;
			// textState.setText("- IDLE -");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			stateMediaPlayer = stateMP_Error;
			// textState.setText("- ERROR!!! -");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			stateMediaPlayer = stateMP_Error;
			// textState.setText("- ERROR!!! -");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "Error-> " + e.toString(), Toast.LENGTH_LONG)
					.show();
			Toast.makeText(this, "Error-> " + strCurrentAudioFilePath,
					Toast.LENGTH_LONG).show();

			stateMediaPlayer = stateMP_Error;
			// textState.setText("- ERROR!!! -");
		}
	}

	//
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(AC.getTextbyLanguage(R.string.holyquran));
		menu.add(0, R.id.mnu_bookmark, 0,
				AC.getTextbyLanguage(R.string.bookmark));
		menu.add(0, R.id.mnu_goto, 0,
				AC.getTextbyLanguage(R.string.GoToActivity));
		menu.add(0, R.id.mnu_search, 0, AC.getTextbyLanguage(R.string.Search));
		menu.add(0, -100, 0, AC.getTextbyLanguage(R.string.tafseer));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		callOptionsItemSelected(null, item.getItemId());
		return true;
	}

	public class ImageAdapter extends BaseAdapter {
		String baseImgDir = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/hQuran/img/";
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
			// imgView.setScaleType(ScaleType.FIT_XY);
			// if (!bNavigateByCode) {
			// StopRecitation(true);
			// bNavigateByCode = false;
			// }
			Integer iPage = 604 - g.getSelectedItemPosition();
			Integer iAya = AC.iCurrentAya;
			if (iAya == -1)
				iAya = 0;
			// myTitleText.setText(AC.getTextbyLanguage( R.string.holyquran) +
			// "/" + Integer.toString(iPage)); //
			vwCurrentAya.setText(Integer.toString(iPage) + "/"
					+ Integer.toString(iAya));
			// to let it work like Arabic we will subtract position by 604
			// imgView.setImageResource(mImageIds[position]);

			String strFile = baseImgDir + Integer.toString(604 - position)
					+ ".img";
			String strFileOld = baseImgDir + Integer.toString(604 - position)
					+ ".gif";
			File f = new File(strFileOld);

			if (f.exists()) {
				f.renameTo(new File(strFile));
				f.delete();
			}

			f = new File(strFile);
			// http://dl.dropbox.com/u/" + AC.Dropbox + "/img/9.gif
			if (!f.exists()) {
				// ImageManager.DownloadFromUrl(Integer.toString(position),strFile);
				// callOptionsItemSelected(null, R.id.mnu_settings);
				strFile = baseImgDir + "no.img";
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
			int iMinus = 50;

			if (AC.AudioOn) {
				// iMinus = iMinus + buttonPlayPause.getHeight();
				// iMinus =120;
			}
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				float myFlo = (float) (width * 1.5);
				height = (int) myFlo;
				imgView.setLayoutParams(new Gallery.LayoutParams(width, height
						- iMinus));
				imgView.setScaleType(ImageView.ScaleType.FIT_XY);
			} else { //
				imgView.setLayoutParams(new Gallery.LayoutParams(width, //
						height - iMinus));
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
