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

import java.io.File;
import java.io.IOException;
import com.hamdyghanem.holyquran.R;
import com.hamdyghanem.holyquran.Download.CreateTabImages;
import com.hamdyghanem.holyquran.Download.DownloadRecitationActivity;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String MYPREFS = "mySharedPreferences";
	int mode = Activity.MODE_WORLD_WRITEABLE;
	int duration = Toast.LENGTH_SHORT;

	int iButtonWidth = 0;

	public ApplicationController AC;
	public Gallery g;
	TextView myTitleText;
	TextView myHeaderText;
	Typeface arabicFont = null;
	LinearLayout headerLayout = null;
	ScrollView scroller1 = null;
	HorizontalScrollView scroller2 = null;

	int width = 0;
	int height = 0;
	LinearLayout recetationLayout = null;
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
	private long lastTouchTime = -1;

	protected PowerManager pm;
	protected PowerManager.WakeLock mWakeLock;
	public boolean isTabletDevice = false;

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
			this.mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,
					"My Tag");
			// /////////CHANGE THE TITLE BAR/////////////// Typeface
			arabicFont = Typeface.createFromAsset(getAssets(),
					"fonts/DroidSansArabic.ttf");
			getTitleColor();
			//
			//
			AC = (ApplicationController) getApplicationContext();
			isTabletDevice = AC.isTabletDevice();

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
			myHeaderText = (TextView) findViewById(R.id.txtHeader);
			//
			Display display = getWindowManager().getDefaultDisplay();
			width = display.getWidth();
			height = display.getHeight();
			Log.d("---------", Integer.toString(width));
			iButtonWidth = 70;
			// Toast.makeText(this,
			// Integer.toString(iButtonWidth),Toast.LENGTH_LONG).show();
			if (AC.ManualNavigation)
				myHeaderText.setWidth(width - (iButtonWidth * 2));
			else
				myHeaderText.setWidth(width);

			buttonPlayPause = (ImageButton) findViewById(R.id.buttonPlayPause);
			buttonRecitationSettings = (ImageButton) findViewById(R.id.buttonRecitationSettings);

			// buttonRecitationSettings.setVisibility(View.GONE);
			TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			if (mgr != null) {
				mgr.listen(phoneStateListener,
						PhoneStateListener.LISTEN_CALL_STATE);
			}
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
			CreateFolders();
			// fixImagesForOldVersion();

			File file = new File(baseDir);
			if (!file.exists()) {
				file.mkdirs();
				bFirstTime = true;
			}
			//

			//
			AC.bookmarkUtitliy = new BookmarkUtil(AC.ReadBookmarks());
			// Toast.makeText(this, AC.ActivePath, Toast.LENGTH_LONG).show();
			mySharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			//
			ReadSettings();
			if (AC.HideStatusBar)
				getWindow().setFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);

			// message for the new version
			String versionName = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
			String strNewFeatures = "New features : Add nee feature to repeat Aya while recitation and setting to hid the status bar.";
			if (!versionName.equals(AC.LastVersion)) {
				if (AC.iLanguage == 0)
					strNewFeatures = " اصدار جديد : تمت اضافة خاصية التحفيظ لشريط التلاوة و اضافة اعداد لاخفاء الشريط العلوي";
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
				// CreateFolders();
				// Open settings to load images directly
				g.setSelection(g.getCount());
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
				if (AC.iCurrentPage == 0)
					AC.iCurrentPage = 1;
				AC.iCurrenSura = AC.GetSoraIndex(AC.iCurrentPage);
				//
				// Toast.makeText(this, Integer.toString(g.getCount()),
				// Toast.LENGTH_LONG).show();
				if (g.getCount() == 302) {
					if (AC.isOdd(AC.iCurrentPage))
						AC.iCurrentPage = AC.iCurrentPage + 1;
					AC.iCurrentPage = AC.iCurrentPage / 2;
				}
				// Integer ii = g.getCount() - AC.iCurrentPage;
				// Toast.makeText(this, Integer.toString(ii), Toast.LENGTH_LONG)
				// .show();
				g.setSelection(g.getCount() - AC.iCurrentPage);
			}
			//
			setBackLightValue();

			// CreateFolders();
			// Set a item click listener, and just Toast the clicked position
			myHeaderText.setBackgroundColor(R.color.blackblue);
			g.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {
					// myHeaderText.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_settings));
					myHeaderText.setBackgroundColor(R.color.blackblue); //
					ShowHeader();
					DoubleClick();
					//

				}
			});
			//

			// if (AC.isTabletDevice())
			// Toast.makeText(this, "isT abletDevice", Toast.LENGTH_SHORT)
			// .show();
			// else
			// Toast.makeText(this, "is not TabletDevice", Toast.LENGTH_SHORT)
			// .show();

		} catch (Throwable t) {
			Toast.makeText(this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	public void DoubleClick() {

		long thisTime = System.currentTimeMillis();
		if (thisTime - lastTouchTime < 250) {
			// Double tap
			// startActivityForResult(new Intent(this, ZoomActivity.class), 6);
			lastTouchTime = -1;
		} else {
			// Too slow :)
			lastTouchTime = thisTime;
			// zoomLayer.setVisibility(View.GONE);
		}

	}

	public void ShowHeader() {
		if (headerLayout.getVisibility() == View.VISIBLE) {
			headerLayout.setVisibility(View.GONE);
			if (AC.AudioOn)
				recetationLayout.setVisibility(View.GONE);
		} else {
			headerLayout.setVisibility(View.VISIBLE);
			if (AC.AudioOn)
				recetationLayout.setVisibility(View.VISIBLE);
		}
		// Toast.makeText(MainActivity.this,
		// "Request failed: >>>>>>>>>>.", Toast.LENGTH_LONG).show();

	}

	public void ReadSettings() {
		try {
			String strLanguage = mySharedPreferences.getString(
					"language_preference", "0");
			AC.iLanguage = Integer.parseInt(strLanguage);
			AC.LastVersion = mySharedPreferences.getString(
					"LastVersion_preference", "0");
			AC.AudioOn = mySharedPreferences.getBoolean("audioon_preference",
					AC.AudioOn);
			AC.ManualNavigation = mySharedPreferences.getBoolean(
					"manualnavigation_preference", AC.ManualNavigation);
			AC.HideStatusBar = mySharedPreferences.getBoolean(
					"hidestatusbar_preference", AC.HideStatusBar);

			AC.ScreenOn = mySharedPreferences.getBoolean("screenon_preference",
					false);
			AC.CurrentImageType = mySharedPreferences.getString(
					"currentimagetype_preference", AC.CurrentImageType);
			AC.CurrentReciter = mySharedPreferences.getString(
					"currentreciter_preference", AC.CurrentReciter);
			//
			AC.CurrentSCREEN_ORIENTATION = Integer.parseInt(mySharedPreferences
					.getString("currentscreenorientation_preference", "1"));

			// ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
			setRequestedOrientation(AC.CurrentSCREEN_ORIENTATION);

			//
			String strBackLightValue = mySharedPreferences.getString(
					"BackLightValue_preference", "0.5");
			AC.BackLightValue = Float.parseFloat(strBackLightValue);
			AC.TahfeezStatus = mySharedPreferences.getBoolean(
					"tahfeez_preference", AC.TahfeezStatus);
			AC.TahfeezChapter = mySharedPreferences.getInt(
					"tahfeezchapter_preference", AC.TahfeezChapter);
			AC.TahfeezSura = mySharedPreferences.getInt(
					"tahfeezsura_preference", AC.TahfeezSura);
			AC.TahfeezFromAya = mySharedPreferences.getInt(
					"tahfeezfromaya_preference", AC.TahfeezFromAya);
			AC.TahfeezToAya = mySharedPreferences.getInt(
					"tahfeeztoaya_preference", AC.TahfeezToAya);
			// Zoom
			AC.imageScale = 1;// mySharedPreferences.getFloat("zoom_preference",AC.imageScale);
			// Toast.makeText(this,
			// "Request failed: " + Integer.toString(AC.iLanguage),
			// Toast.LENGTH_LONG).show();
			recetationLayout = (LinearLayout) findViewById(R.id.RecetationLayout);
			headerLayout = (LinearLayout) findViewById(R.id.HeaderLayout);
			scroller1 = (ScrollView) findViewById(R.id.ScrollView01);
			// scroller2 = (HorizontalScrollView)
			// findViewById(R.id.ScrollView02);
			// //
			// Toast.makeText(this,
			// "Request failed: " + String.valueOf(AC.imageScale),
			// Toast.LENGTH_LONG).show();

			if (AC.AudioOn)
				recetationLayout.setVisibility(1);
			else
				recetationLayout.setVisibility(View.GONE);

			if (AC.ManualNavigation) {
				findViewById(R.id.buttonNavNext).setVisibility(View.VISIBLE);
				findViewById(R.id.buttonNavBack).setVisibility(View.VISIBLE);
				myHeaderText.setWidth(width - (iButtonWidth * 2));
			} else {
				findViewById(R.id.buttonNavNext).setVisibility(View.GONE);
				findViewById(R.id.buttonNavBack).setVisibility(View.GONE);
				myHeaderText.setWidth(width);
			}
		} catch (Throwable t) {
			Toast.makeText(this, "Error : " + t.toString(), Toast.LENGTH_LONG)
					.show();
		}
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
		editor.putBoolean("manualnavigation_preference", AC.ManualNavigation);
		editor.putBoolean("hidestatusbar_preference", AC.HideStatusBar);
		editor.putString("currentimagetype_preference", AC.CurrentImageType);
		editor.putString("currentreciter_preference", AC.CurrentReciter);
		editor.putString("currentscreenorientation_preference",
				Integer.toString(AC.CurrentSCREEN_ORIENTATION));
		editor.putString("BackLightValue_preference",
				Float.toString(AC.BackLightValue));

		editor.putFloat("zoom_preference", AC.imageScale);

		//
		editor.commit();

	}

	public void WriteSettings1() {
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putString("currentimagetype_preference", AC.CurrentImageType);
		// editor.putString("currentreciter_preference", AC.CurrentReciter);
		editor.putFloat("zoom_preference", AC.imageScale);
		editor.putString("BackLightValue_preference",
				Float.toString(AC.BackLightValue));
		//
		setBackLightValue();
		//
		editor.commit();

	}

	public void WriteSettingsTahfeez() {
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putBoolean("tahfeez_preference", AC.TahfeezStatus);
		editor.putInt("tahfeezchapter_preference", AC.TahfeezChapter);
		editor.putInt("tahfeezsura_preference", AC.TahfeezSura);
		editor.putInt("tahfeezfromaya_preference", AC.TahfeezFromAya);
		editor.putInt("tahfeeztoaya_preference", AC.TahfeezToAya);

		//
		editor.commit();

	}

	private void CreateFolders() {
		File file = new File(baseDir);
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "tafseer/");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "img/");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "img/0/");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "img/0_tab/");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "img/2/");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "img/1/");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "dictionary/");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "taareef/");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "English/");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "Audio/");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "Audio/Mashary");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "Audio/Huzifi");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "Audio/Menshawi");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "Audio/Muaiqly");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "Audio/Rifai");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "Audio/Sudais");
		if (!file.exists())
			file.mkdirs();

		file = new File(baseDir + "Audio/Ajmi");
		if (!file.exists())
			file.mkdirs();

		file = new File(baseDir + "Audio/Alshatri");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "Audio/Ghamdi");
		if (!file.exists())
			file.mkdirs();
		file = new File(baseDir + "Audio/Husari");
		if (!file.exists())
			file.mkdirs();

		file = new File(baseDir + "imgTypes");
		if (!file.exists())
			file.mkdirs();
		//
		File f = new File(baseDir + "imgTypes/img_1_50");
		if (!f.exists()) {
			ImageManager.saveToSDCard(BitmapFactory.decodeResource(
					this.getResources(), R.drawable.img_no), "no", false);
			ImageManager.saveToSDCard(BitmapFactory.decodeResource(
					this.getResources(), R.drawable.img_0_50), "0", true);
			ImageManager.saveToSDCard(BitmapFactory.decodeResource(
					this.getResources(), R.drawable.img_1_50), "1", true);
			ImageManager.saveToSDCard(BitmapFactory.decodeResource(
					this.getResources(), R.drawable.img_2_50), "2", true);
		}
	}

	public void fixImagesForOldVersion() {

		File folder = new File(baseDir + "img/");
		File[] files = folder.listFiles(new imgFilter());
		// }
		// Move old files to "\img\0" folder
		for (int i = 1; i < files.length; i++) {
			if (!files[i].getName().equals("no.img"))
				files[i].renameTo(new File(baseDir + "img/0", files[i]
						.getName()));
		}

	}

	@Override
	public void onStop() {
		AC.saveBookmarks(g.getSelectedItemPosition());
		WriteSettings();
		// StopRecitation(false);
		super.onStop();
	}

	@Override
	public void onDestroy() {
		try {
			StopRecitation(true);
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
			startActivityForResult(new Intent(this, GoToActivity.class), 11);

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

					AC.iCurrentPage = AC.bookmarkUtitliy.arr.get(
							AC.bookmarkUtitliy.getDefault()).getPage();
					AC.saveBookmarksDefalut();

					// Toast.makeText(this, "Request failed: " +
					// Integer.toString(i),
					// Toast.LENGTH_LONG).show();
					//

					g.setSelection(g.getCount()
							- AC.bookmarkUtitliy.arr.get(
									AC.bookmarkUtitliy.getDefault()).getPage());
				}
			} else if (requestCode == 11) {
				if (!(data == null || data.getExtras() == null)) {

					Bundle extras = data.getExtras();
					Integer i = extras.getInt("returnKey");
					AC.iCurrentPage = i;
					AC.saveBookmarksDefalut();

					g.setSelection(g.getCount() - i);
				}
			} else if (requestCode == 3) {
				if (!(data == null || data.getExtras() == null)) {
					Bundle extras = data.getExtras();
					Integer i = extras.getInt("returnKey");
					g.setSelection(g.getCount() - i);
				}
			}
			// Settinngs
			else if (requestCode == 4) {
				// AC.CurrentImageType = "1";
				// Save CurrentImageType first
				WriteSettings1();
				// refresh the gallery
				((BaseAdapter) g.getAdapter()).notifyDataSetChanged();
				//
				ReadSettings();
				WriteSettings();
				// Toast.makeText(this, Boolean.toString(AC.ScreenOn),
				// Toast.LENGTH_LONG).show();
				// Toast.makeText(this, AC.CurrentReciter,
				// Toast.LENGTH_SHORT).show();

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
			} else if (requestCode == 6) {
				// if (!(data == null || data.getExtras() == null)) {
				// Bundle extras = data.getExtras();
				// Integer i = extras.getInt("returnKey");
				// AC.iLanguage = i;
				WriteSettings1();
				// refresh the gallery
				((BaseAdapter) g.getAdapter()).notifyDataSetChanged();
				//
				ReadSettings();
				WriteSettings();
				// }
			} else if (requestCode == 7) {
				//
				if (!(data == null || data.getExtras() == null)) {
					Bundle extras = data.getExtras();

					Integer i = extras.getInt("returnKey");
					//

					WriteSettingsTahfeez();
					// Toast.makeText(this, "To be done, Hamdy",
					// Toast.LENGTH_LONG)
					// .show();
				}
				// hamdy

			}

		} catch (Throwable t) {
			if (requestCode == 4) {
			} else {
				Toast.makeText(this, "Request failed: " + t.toString(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public void OnNavNext(View view) {

		AC.iCurrentPage -= 1;
		if (AC.iCurrentPage < 0)
			AC.iCurrentPage = 0;
		g.setSelection(g.getCount() - AC.iCurrentPage);

	}

	public void OnNavBack(View view) {
		AC.iCurrentPage += 1;
		if (AC.iCurrentPage > g.getCount())
			AC.iCurrentPage = g.getCount();
		g.setSelection(g.getCount() - AC.iCurrentPage);
	}

	PhoneStateListener phoneStateListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (state == TelephonyManager.CALL_STATE_RINGING) {
				// Incoming call: Pause music
				if (stateMediaPlayer == stateMP_Playing) {
					mediaPlayer.pause();
					buttonPlayPause.setImageResource(R.drawable.play);
					stateMediaPlayer = stateMP_Pausing;
					return;
				}
			} else if (state == TelephonyManager.CALL_STATE_IDLE) {
				// Not in call: Play music
			} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
				// A call is dialing, active or on hold
				// Incoming call: Pause music
				if (stateMediaPlayer == stateMP_Playing) {
					mediaPlayer.pause();
					buttonPlayPause.setImageResource(R.drawable.play);
					stateMediaPlayer = stateMP_Pausing;
					return;
				}
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	};

	// Recitiation methods
	public void OnPlayRecitation(View view) {
		AC.iCurrentPage = g.getCount() - g.getSelectedItemPosition();
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
			//
			// Check if user will use tahfeez(repetaion)
			if (AC.TahfeezStatus) {
				if (AC.iCurrentAya < AC.TahfeezFromAya)
					AC.iCurrentAya = AC.TahfeezFromAya - 1;
				// check if it reach last aya
				if (AC.iCurrentAya > AC.TahfeezToAya - 1)
					AC.iCurrentAya = AC.TahfeezFromAya - 1;
			}
			// first time or after stop
			if (AC.iCurrentAya == -1) {
				AC.iCurrentAya = 0;
				strCurrentAudioFileName = AC
						.GetFirstRecitationFile(AC.iCurrentPage);
			} else {
				// Check if next sura
				if (AC.iCurrentAya == -2) {
					AC.iCurrentAya = -1;
					strCurrentAudioFileName = AC.GetFirstRecitationFile();
				}

				AC.iCurrentAya += 1;
				strCurrentAudioFileName = AC.GetFirstRecitationFile();
			}

			strCurrentAudioFilePath = baseDir + "Audio/" + AC.CurrentReciter
					+ "/" + strCurrentAudioFileName;

			// Toast.makeText(this,Integer.toString(AC.iCurrentAya),
			// Toast.LENGTH_LONG).show();
			PrintAya(AC.iCurrentPage, AC.iCurrentAya);
			// buttonPlayPause.set(Integer.toString(AC.iCurrentAya));

			File f = new File(strCurrentAudioFilePath);
			// Toast.makeText(this, strCurrentAudioFilePath, Toast.LENGTH_SHORT)
			// .show();
			/*
			 * Toast.makeText( this, Integer.toString( AC.iCurrenSura) + ">" +
			 * AC.GetSoraPage( AC.iCurrenSura) + ">" +
			 * Integer.toString(AC.getAyaPage(AC.iCurrenSura, AC.iCurrentAya)),
			 * Toast.LENGTH_LONG).show();
			 */
			if (AC.iCurrentAya > 0) {
				if (AC.iCurrenSura == 9)
					AC.iCurrentPage = AC.getAyaPage(AC.iCurrenSura,
							AC.iCurrentAya - 1);
				else
					AC.iCurrentPage = AC.getAyaPage(AC.iCurrenSura,
							AC.iCurrentAya);

			} else {
				// Log.d("DOWNLOAD FAILED",Integer.toString( AC.iCurrenSura));
				AC.iCurrentPage = AC.getAyaPage(AC.iCurrenSura,
						AC.iCurrentAya + 1);
			}
			g.setSelection(g.getCount() - AC.iCurrentPage);
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
				return;
			} else {
				// It happenes
				if (f.length() == 0) {
					StopRecitation(true);
					startActivity(new Intent(this,
							DownloadRecitationActivity.class));
					f.delete();
					return;
				}
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
			StopRecitation(false);
			Toast.makeText(this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	public void OnRecitationSettings(View view) {
		startActivityForResult(new Intent(this, TahfeezActivity.class), 7);

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
			PrintAya(AC.iCurrentPage, 0);
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
			StopRecitation(false);

			e.printStackTrace();
			Toast.makeText(this, "Error-> " + e.toString(), Toast.LENGTH_LONG)
					.show();
			Toast.makeText(this, "Error-> " + strCurrentAudioFilePath,
					Toast.LENGTH_LONG).show();

			stateMediaPlayer = stateMP_Error;
			// textState.setText("- ERROR!!! -");
		}
	}

	public void PrintAya(Integer iPage, Integer iAya) {
		if (AC.iCurrenSura == 9)
			iAya += 1;
		iPage += 1;
		String strHeader = AC.GetChapter(iPage);
		String strSuraName = AC.GetSora(iPage);
		strHeader += " ( " + AC.strCurrenSura + " ) " + strSuraName;
		strHeader += "\r\n" + Integer.toString(iPage) + "/"
				+ Integer.toString(iAya);
		myHeaderText.setText(strHeader);
		// Highlight
		// boolean bResult = AC.getQuranPointBySura(this, iPage, iAya);
		// if (bResult) {
		// Manage scale
		// this.invalidate();
		// }
	}

	//
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(AC.getTextbyLanguage(R.string.holyquran));
		menu.add(0, R.id.mnu_bookmark, 0,
				AC.getTextbyLanguage(R.string.bookmark));
		menu.add(0, R.id.mnu_settings, 0,
				AC.getTextbyLanguage(R.string.settings));
		menu.add(0, R.id.mnu_details, 0,
				AC.getTextbyLanguage(R.string.detailsmenu));
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

	private void setBackLightValue() {
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		layoutParams.screenBrightness = AC.BackLightValue;
		getWindow().setAttributes(layoutParams);

	}
}
