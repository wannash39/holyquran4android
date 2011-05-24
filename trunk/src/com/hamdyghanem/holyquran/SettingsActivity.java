package com.hamdyghanem.holyquran;

import java.io.File;

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	/** Called when the activity is first created. */
	ProgressDialog dialog;
	int increment = 0;
	String baseDir = "";
	ApplicationController AC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.settings);
		AC = (ApplicationController) getApplicationContext();

		// /////////CHANGE THE TITLE BAR///////////////
		Typeface arabicFont = Typeface.createFromAsset(getAssets(),
				"fonts/DroidSansArabic.ttf");

		if (customTitleSupported) {
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.mytitle);
		}

		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		if (myTitleText != null) {
			myTitleText.setTypeface(arabicFont);
			myTitleText.setText(AC.getTextbyLanguage(R.string.settings));
			// myTitleText.setBackgroundColor(R.color.blackblue);
		}
		// //////////////////////

		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		// Arabizarion
		((Button) findViewById(R.id.btnSettDownload)).setTypeface(arabicFont);
		((Button) findViewById(R.id.btnSettDownload)).setText(AC
				.getTextbyLanguage(R.string.btn_download_pages));

		((Button) findViewById(R.id.btnTafserDownload)).setTypeface(arabicFont);
		((Button) findViewById(R.id.btnTafserDownload)).setText(AC
				.getTextbyLanguage(R.string.btn_download_tafser));

		((Button) findViewById(R.id.ButCancel)).setTypeface(arabicFont);
		((Button) findViewById(R.id.ButCancel)).setText(AC
				.getTextbyLanguage(R.string.ButtCancel));

		((Button) findViewById(R.id.ButOK)).setTypeface(arabicFont);
		((Button) findViewById(R.id.ButOK)).setText(AC
				.getTextbyLanguage(R.string.ButtOK));

		((Button) findViewById(R.id.btnTaareefDownload))
				.setTypeface(arabicFont);
		((Button) findViewById(R.id.btnTaareefDownload)).setText(AC
				.getTextbyLanguage(R.string.btn_download_tareef));

		((Button) findViewById(R.id.btnDictionayDownload))
				.setTypeface(arabicFont);
		((Button) findViewById(R.id.btnDictionayDownload)).setText(AC
				.getTextbyLanguage(R.string.btn_download_dictionary));

		((Button) findViewById(R.id.btnDatabaseDownload))
				.setTypeface(arabicFont);
		((Button) findViewById(R.id.btnDatabaseDownload)).setText(AC
				.getTextbyLanguage(R.string.btn_download_database));
//
		Display display = getWindowManager().getDefaultDisplay();
		((Button) findViewById(R.id.btnDatabaseDownload)).setWidth(display.getWidth());

		//
		RadioButton rb = ((RadioButton) findViewById(R.id.optAtabic));
		rb.setTypeface(arabicFont);
		rb.setText(R.string.LangArabic);
		rb.setChecked(AC.iLanguage == 0);
		rb = ((RadioButton) findViewById(R.id.optEnglish));
		rb.setTypeface(arabicFont);
		rb.setText(R.string.LangEnglish);
		rb.setChecked(AC.iLanguage == 1);

		//
		((TextView) findViewById(R.id.vwLanguage)).setTypeface(arabicFont);
		((TextView) findViewById(R.id.vwLanguage)).setText(AC
				.getTextbyLanguage(R.string.Language));
		//
		findViewById(R.id.ButOK).setOnClickListener(ok_listener);
		findViewById(R.id.ButCancel).setOnClickListener(cancel_listener);
	//
		if (AC.NeedDownload())
			Toast.makeText(this, AC.getTextbyLanguage(R.string.notexistimage),
					Toast.LENGTH_LONG).show();
		// downloadNow();
	}

	public Thread performOnBackgroundThread(final Runnable runnable) {
		final Thread t = new Thread() {
			@Override
			public void run() {
				try {
					runnable.run();
				} finally {

				}
			}
		};
		t.start();
		return t;
	}

	public void downloadPages(View view) {
		downloadNow();
	}

	public void downloadTafser(View view) {
		downloadTafserNow();
	}

	public void downloadDatabase(View view) {
		downloadDatabaseNow();
	}

	public void downloadTareef(View view) {
		downloadTareefNow();
	}

	public void downloadDicttonary(View view) {
		downloadDictionaryNow();
	}

	public void downloadNow() {
		// final TextView tv = (TextView) findViewById(R.id.TextView01);
		baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/hQuran/img/";

		dialog = new ProgressDialog(this);
		dialog.setCancelable(true);
		dialog.setMessage(AC.getTextbyLanguage(R.string.downloadingpages));
		// set the progress to be horizontal
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// reset the bar to the default value of 0
		dialog.setProgress(0);
		dialog.setMax(605);
		// display the progressbar
		dialog.show();

		// create a thread for updating the progress bar
		Thread background = new Thread(new Runnable() {
			public void run() {
				while (increment < 605) {
					// wait 500ms between each update
					String strFile = baseDir + Integer.toString(increment)
							+ ".gif";
					// http://dl.dropbox.com/u/27675084/img/9.gif
					// Log.d("is file exist b4 download?",
					// Integer.toString(increment));
					File f = new File(strFile);
					if (!f.exists()) {
						ImageManager.DownloadFromUrl(
								Integer.toString(increment), strFile);
					}
					increment++;
					// active the update handler
					progressHandler.sendMessage(progressHandler.obtainMessage());
				}
				// Finished
				dialog.cancel();
				// finish();
			}
		});

		// start the background thread
		background.start();
	}

	public void downloadDatabaseNow() {
		dialog = new ProgressDialog(this);
		dialog.setCancelable(true);
		dialog.setMessage(AC.getTextbyLanguage( R.string.downloadingdatabase));
		// set the progress to be horizontal
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// reset the bar to the default value of 0
		increment = 0;
		// dialog.setProgress(0);
		// dialog.setMax(605);
		// display the progressbar
		dialog.show();
		// create a thread for updating the progress bar
		Thread background = new Thread(new Runnable() {
			public void run() {
				ImageManager.DownloadDBFromUrl();
				// active the update handler
				progressHandler.sendMessage(progressHandler.obtainMessage());

				// Finished
				dialog.cancel();
			}
		});

		// start the background thread
		background.start();
	}

	public void downloadTafserNow() {
		// final TextView tv = (TextView) findViewById(R.id.TextView01);
		baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/hQuran/tafseer/";

		dialog = new ProgressDialog(this);
		dialog.setCancelable(true);
		dialog.setMessage(AC.getTextbyLanguage(R.string.downloadingtafseer));
		// set the progress to be horizontal
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// reset the bar to the default value of 0
		increment = 0;
		dialog.setProgress(0);
		dialog.setMax(605);
		// display the progressbar
		dialog.show();

		// create a thread for updating the progress bar
		Thread background = new Thread(new Runnable() {
			public void run() {
				while (increment < 605) {
					// wait 500ms between each update
					String strFile = baseDir + Integer.toString(increment)
							+ ".html";
					// http://dl.dropbox.com/u/27675084/img/9.gif
					// Log.d("is file exist b4 download?",
					// Integer.toString(increment));
					File f = new File(strFile);
					if (!f.exists()) {
						ImageManager.DownloadTafserFromUrl(
								Integer.toString(increment), strFile);
					}
					increment++;
					// active the update handler
					progressHandler.sendMessage(progressHandler.obtainMessage());
				}
				// Finished
				dialog.cancel();
			}
		});

		// start the background thread
		background.start();
	}

	public void downloadTareefNow() {
		// final TextView tv = (TextView) findViewById(R.id.TextView01);
		baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/hQuran/taareef/";

		dialog = new ProgressDialog(this);
		dialog.setCancelable(true);
		dialog.setMessage(AC.getTextbyLanguage(R.string.downloadingtareef));
		// set the progress to be horizontal
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// reset the bar to the default value of 0
		increment = 0;
		dialog.setProgress(0);
		dialog.setMax(605);
		// display the progressbar
		dialog.show();

		// create a thread for updating the progress bar
		Thread background = new Thread(new Runnable() {
			public void run() {
				while (increment < 605) {
					// wait 500ms between each update
					String strFile = baseDir + Integer.toString(increment)
							+ ".html";
					// http://dl.dropbox.com/u/27675084/img/9.gif
					// Log.d("is file exist b4 download?",
					// Integer.toString(increment));
					File f = new File(strFile);
					if (!f.exists()) {
						ImageManager.DownloadTareefFromUrl(
								Integer.toString(increment), strFile);
					}
					increment++;
					// active the update handler
					progressHandler.sendMessage(progressHandler.obtainMessage());
				}
				// Finished
				dialog.cancel();
			}
		});

		// start the background thread
		background.start();
	}

	public void downloadDictionaryNow() {
		// final TextView tv = (TextView) findViewById(R.id.TextView01);
		baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/hQuran/dictionary/";

		dialog = new ProgressDialog(this);
		dialog.setCancelable(true);
		dialog.setMessage(AC.getTextbyLanguage(R.string.downloadingdictionary));
		// set the progress to be horizontal
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// reset the bar to the default value of 0
		increment = 0;
		dialog.setProgress(0);
		dialog.setMax(605);
		// display the progressbar
		dialog.show();

		// create a thread for updating the progress bar
		Thread background = new Thread(new Runnable() {
			public void run() {
				while (increment < 605) {
					// wait 500ms between each update
					String strFile = baseDir + Integer.toString(increment)
							+ ".html";
					// http://dl.dropbox.com/u/27675084/img/9.gif
					// Log.d("is file exist b4 download?",
					// Integer.toString(increment));
					File f = new File(strFile);
					if (!f.exists()) {
						ImageManager.DownloadDictionaryFromUrl(
								Integer.toString(increment), strFile);
					}
					increment++;
					// active the update handler
					progressHandler.sendMessage(progressHandler.obtainMessage());
				}
				// Finished
				dialog.cancel();
			}
		});

		// start the background thread
		background.start();
	}

	Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			dialog.setProgress(increment);
		}
	};
	private OnClickListener ok_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			AC.iLanguage = 0;
			RadioButton rb = ((RadioButton) findViewById(R.id.optEnglish));
			if (rb.isChecked())
				AC.iLanguage = 1;
			//
			AC.WriteSettings();
			finish();
		}
	};

	private OnClickListener cancel_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();

		}
	};
}