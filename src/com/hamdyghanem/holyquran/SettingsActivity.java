package com.hamdyghanem.holyquran;

import java.io.File;

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class SettingsActivity extends Activity {
	/** Called when the activity is first created. */
	ProgressDialog dialog;
	int increment = 0;
	String baseDir = "";
	ApplicationController AC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		AC = (ApplicationController) getApplicationContext();
		if (AC.NeedDownload())
			downloadNow();
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
	public void downloadNow() {
		// final TextView tv = (TextView) findViewById(R.id.TextView01);
		baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/hQuran/img/";

		dialog = new ProgressDialog(this);
		dialog.setCancelable(true);
		dialog.setMessage(this.getString(R.string.downloading));
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
					//Log.d("is file exist b4 download?", Integer.toString(increment));
					File f = new File(strFile);
					if (!f.exists()) {
						ImageManager.DownloadFromUrl(Integer
								.toString(increment), strFile);
					}
					increment++;
					// active the update handler
					progressHandler
							.sendMessage(progressHandler.obtainMessage());
				}
				// Finished
				dialog.cancel();
				finish();
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
		dialog.setMessage(this.getString(R.string.downloading));
		// set the progress to be horizontal
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// reset the bar to the default value of 0
		increment=0;
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
							+ ".txt";
					// http://dl.dropbox.com/u/27675084/img/9.gif
					//Log.d("is file exist b4 download?", Integer.toString(increment));
					File f = new File(strFile);
					if (!f.exists()) {
						ImageManager.DownloadTafserFromUrl(Integer
								.toString(increment), strFile);
					}
					increment++;
					// active the update handler
					progressHandler
							.sendMessage(progressHandler.obtainMessage());
				}
				// Finished
				dialog.cancel();
				finish();
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

}