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
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TafseerActivity extends Activity {
	/** Called when the activity is first created. */
	ApplicationController AC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.tafseer);
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
			myTitleText.setText(R.string.tafseer);
			// myTitleText.setBackgroundColor(R.color.blackblue);
		}
		// //////////////////////
		// TextView lbl = (TextView) findViewById(R.id.TextView01);
		AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL
		// Integer i =
		// AC.bookmarkUtitliy.arr.get(AC.bookmarkUtitliy.getDefault())
		// .getPage();
		// .getPage();
		try {
			// String baseDir = Environment.getExternalStorageDirectory()
			// .getAbsolutePath()
			// + "/hQuran/tafseer/" + Integer.toString(i) + ".txt";
			WebView myWebView = (WebView) findViewById(R.id.webviewtafseer);
			// myWebView.loadUrl("http://dl.dropbox.com/u/27675084/tafseer_html/1.html");
			myWebView.loadUrl("file:///sdcard/hQuran/tafseer/"
					+ Integer.toString(AC.iCurrentPage) + ".html");
			/*
			 * File f = new File(baseDir); FileInputStream fileIS;
			 * 
			 * fileIS = new FileInputStream(f);
			 * 
			 * BufferedReader buf = new BufferedReader(new InputStreamReader(
			 * fileIS, "UTF-8")); String readString = new String(); String
			 * strAbout = ""; while ((readString = buf.readLine()) != null) {
			 * 
			 * strAbout += readString;
			 * 
			 * }
			 * 
			 * //
			 * 
			 * lbl.setSingleLine(false);
			 * lbl.setMovementMethod(LinkMovementMethod.getInstance());
			 * lbl.setText(strAbout); lbl.setGravity(Gravity.RIGHT);
			 * 
			 * lbl.setMovementMethod(new ScrollingMovementMethod());
			 * 
			 * 
			 * lbl.setTypeface(arabicFont);
			 * lbl.setBackgroundResource(R.color.whiteyellow);
			 * lbl.setTextColor(getResources().getColor(R.color.blackblue));
			 */

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// lbl.setText(R.string.notexisttafser);
			Toast.makeText(this, getString(R.string.notexisttafser),
					Toast.LENGTH_LONG).show();

		}
	}
}