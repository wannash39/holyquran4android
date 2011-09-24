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

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
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

public class DownloadRecitationActivity extends Activity {
	/** Called when the activity is first created. */
	ProgressDialog dialog;
	int increment = 0;
	int incrementSura = 0;
	int incrementAya = 0;

	int imax = 0;
	int imaxAya = 0;
	String strResult = "";

	String baseDir = "";
	Integer iSura = 0;
	ApplicationController AC;

	// CheckBox chkScreenOn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

			setContentView(R.layout.downloadrecitation);

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
				myTitleText.setText(AC
						.getTextbyLanguage(R.string.downloadaudiofiles));
				// myTitleText.setBackgroundColor(R.color.blackblue);
			}
			// //////////////////////

			getWindow().setLayout(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			// Arabizarion
			((Button) findViewById(R.id.btndownloadaudiosura))
					.setTypeface(arabicFont);
			((Button) findViewById(R.id.btndownloadaudiosura)).setText(AC
					.getTextbyLanguage(R.string.btndownloadaudiosura));

			((Button) findViewById(R.id.btndownloadaudioquran))
					.setTypeface(arabicFont);
			((Button) findViewById(R.id.btndownloadaudioquran)).setText(AC
					.getTextbyLanguage(R.string.btndownloadaudioquran));

			//
			Display display = getWindowManager().getDefaultDisplay();
			((Button) findViewById(R.id.btndownloadaudiosura)).setWidth(display
					.getWidth());
			((Button) findViewById(R.id.btndownloadaudioquran))
					.setWidth(display.getWidth());

			baseDir = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/hQuran/Audio/Mashary/";

		} catch (Throwable t) {
			Toast.makeText(this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onStop() {
		if (dialog != null) {
			dialog.dismiss();
		}
		super.onStop();
	}

	private Boolean isConnected() {
		Boolean bOK = AC.isNetConnected();
		if (!bOK) {
			Toast.makeText(this, AC.getTextbyLanguage(R.string.NoInternet),
					Toast.LENGTH_LONG).show();
		} else
			AC.GetActivePath();
		// Check u can download from the path
		baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()
		+ "/hQuran/img/0/";

		String strFile = baseDir + "1.img";
		strResult = ImageManager.DownloadFromUrl("0", AC.ActivePath, "1",
				strFile);
		Log.d(">>>>>>>>", strResult);
		if (strResult.length() > 0) {
			Toast.makeText(this, AC.getTextbyLanguage(R.string.DownloadFailed),
					Toast.LENGTH_LONG).show();
			// startActivity(new Intent(this,
			// DownloadRecitationActivity.class));
			Intent intent = new Intent();
			intent.setClass(this, DownloadFailedActivity.class);
			startActivity(intent);
			bOK = false;

		}
		baseDir = Environment.getExternalStorageDirectory()
		.getAbsolutePath() + "/hQuran/Audio/Mashary/";
		return bOK;
	}

	public void downloadSuraAudio(View view) {
		Toast.makeText(this,
				"Request failed: " + Integer.toString(AC.iCurrenSura),
				Toast.LENGTH_LONG).show();

		downloadSuraAudioNow();
	}

	public void downloadQuranAudio(View view) {
		downloadQuranAudioNow();
	}

	private String getFileName(Integer iSura, Integer iAya) {
		String strSoraIndex = String.format("%03d", iSura);
		String strFirstAya = String.format("%03d", iAya);

		return strSoraIndex + strFirstAya;

	}

	public void downloadSuraAudioNow() {
		// final TextView tv = (TextView) findViewById(R.id.TextView01);
		try {
			if (!isConnected())
				return;

			dialog = new ProgressDialog(this);
			dialog.setCancelable(false);
			dialog.setMessage(AC.getTextbyLanguage(R.string.downloadaudiofiles));
			// set the progress to be horizontal
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// reset the bar to the default value of 0
			increment = 0;

			// iSura = AC.GetSoraIndex(AC.iCurrentPage);
			iSura = AC.iCurrenSura;
			imax = AC.getAyaCount(iSura);
			dialog.setProgress(0);
			dialog.setMax(imax);
			// display the progressbar
			dialog.show();

			// create a thread for updating the progress bar
			Thread background = new Thread(new Runnable() {
				public void run() {
					while (increment <= imax) {
						String strFileName = getFileName(iSura, increment);
						String strFile = baseDir + strFileName + ".aud";
						File f = new File(strFile);
						if (f.exists()) {
							if (f.length() == 0)
								f.delete();
						}

						if (!f.exists()) {
							strResult = ImageManager.DownloadAudioFromUrl(
									AC.ActivePath, strFileName, strFile);
							if (strResult.length() > 0) {
								Log.d("DOWNLOAD FAILED", strResult);
								dialog.cancel();
								return;
							}
						}
						increment++;
						// active the update handler
						progressHandler.sendMessage(progressHandler
								.obtainMessage());
					}
					// Finished
					dialog.cancel();
					dialog.dismiss();
					finish();
				}
			});

			// start the background thread
			background.start();
		} catch (Throwable t) {
			Toast.makeText(this,
					AC.getTextbyLanguage(R.string.downloadaudiofiles),
					Toast.LENGTH_LONG).show();
			// Toast.makeText(this, "Request failed: " +
			// t.toString(),Toast.LENGTH_LONG).show();
		}
	}

	public void downloadQuranAudioNow() {
		try {
			if (!isConnected())
				return;

			dialog = new ProgressDialog(this);
			dialog.setCancelable(true);
			dialog.setMessage(AC.getTextbyLanguage(R.string.downloadaudiofiles));
			// set the progress to be horizontal
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// reset the bar to the default value of 0
			increment = 0;
			incrementSura = 1;
			imax = 6236;
			dialog.setProgress(0);
			dialog.setMax(imax);
			// display the progressbar
			dialog.show();

			// create a thread for updating the progress bar
			Thread background = new Thread(new Runnable() {
				public void run() {
					while (incrementSura < 115) {
						imaxAya = AC.getAyaCount(incrementSura);
						incrementAya = 0;
						while (incrementAya <= imaxAya) {
							String strFileName = getFileName(incrementSura,
									incrementAya);
							String strFile = baseDir + strFileName + ".aud";
							File f = new File(strFile);
							if (f.exists()) {
								if (f.length() == 0)
									f.delete();
							}

							if (!f.exists()) {
								strResult = ImageManager.DownloadAudioFromUrl(
										AC.ActivePath, strFileName, strFile);
								if (strResult.length() > 0) {
									Log.d("DOWNLOAD FAILED", strResult);
									dialog.cancel();
									return;
								}
							}
							increment++;
							incrementAya++;
							// active the update handler
							progressHandler.sendMessage(progressHandler
									.obtainMessage());
						}
						incrementSura++;
					}
					// Finished
					dialog.cancel();
					dialog.dismiss();

					finish();
				}
			});

			// start the background thread
			background.start();
		} catch (Throwable t) {
			Toast.makeText(this,
					AC.getTextbyLanguage(R.string.downloadaudiofiles),
					Toast.LENGTH_LONG).show();
			// Toast.makeText(this, "Request failed: " +
			// t.toString(),Toast.LENGTH_LONG).show();
		}
	}

	Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			dialog.setProgress(increment);
		}
	};
}