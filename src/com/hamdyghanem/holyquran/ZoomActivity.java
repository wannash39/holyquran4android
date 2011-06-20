package com.hamdyghanem.holyquran;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.hamdyghanem.holyquran.R;

import android.R.integer;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ZoomActivity extends Activity {
	/** Called when the activity is first created. */
	ApplicationController AC;
	String baseDir = "";
	String strFile = "";
	String strFileOld = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.tafseer);

		AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL

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
			myTitleText.setText(AC.getTextbyLanguage(R.string.mnuZoom));
			// myTitleText.setBackgroundColor(R.color.blackblue);
		}
		// //////////////////////
		// TextView lbl = (TextView) findViewById(R.id.TextView01);
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		try {
			baseDir = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/hQuran/img/";
			strFile = baseDir + Integer.toString(AC.iCurrentPage) + ".img";
			strFileOld = baseDir + Integer.toString(AC.iCurrentPage) + ".gif";
			//
			WebView myWebView = (WebView) findViewById(R.id.webviewtafseer);
			// myWebView.loadUrl("http://dl.dropbox.com/u/" + AC.Dropbox + "/tafseer_html/1.html");

			File f = new File(strFile);
			if (f.exists()) {
				f.renameTo(new File(strFileOld));
			}
			myWebView.loadUrl("file:///sdcard/hQuran/img/"
					+ Integer.toString(AC.iCurrentPage) + ".gif");
			myWebView.getSettings().setBuiltInZoomControls(true);
			// wv.getSettings().setUseWideViewPort(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// lbl.setText(R.string.notexisttafser);
			Toast.makeText(this, getString(R.string.notexisttafser),
					Toast.LENGTH_LONG).show();

		}
	}

	@Override
	public void onStop() {
		File f = new File(strFileOld);
		f.deleteOnExit();
		super.onStop();
	}

}