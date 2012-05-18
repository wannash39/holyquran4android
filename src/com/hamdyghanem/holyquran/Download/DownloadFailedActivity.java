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
package com.hamdyghanem.holyquran.Download;


import com.hamdyghanem.holyquran.ApplicationController;
import com.hamdyghanem.holyquran.R;
import com.hamdyghanem.holyquran.R.color;
import com.hamdyghanem.holyquran.R.id;
import com.hamdyghanem.holyquran.R.layout;
import com.hamdyghanem.holyquran.R.string;

import android.app.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadFailedActivity extends Activity {
	/** Called when the activity is first created. */
	
	ApplicationController AC;

	// CheckBox chkScreenOn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {

			setContentView(R.layout.downloadfailed);

			AC = (ApplicationController) getApplicationContext();

			// /////////CHANGE THE TITLE BAR///////////////
			Typeface arabicFont = Typeface.createFromAsset(getAssets(),
					"fonts/DroidSansArabic.ttf");

			this.setTitle(AC.getTextbyLanguage(R.string.DownloadFailed));

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
			TextView lbl = (TextView) findViewById(R.id.txtRecitationDownload);
			lbl.setTypeface(arabicFont);

			String strAbout = "<center><H1>";
			strAbout += "<font color='green'>";
			strAbout += "  <br />";
			if (AC.iLanguage == 0) {
				strAbout += "  نظرا للتحمل علي السيفير نرجو الذاهب لصفحة البرنامج علي الفيس بوك للحصول علي روابط مباشرة <br />";

			} else {
				strAbout += " Due to overload on the server we apologise that you could not download from your mobile , to get direct links and the way you install it please visit us on our page on facbook  <br />";

			}
			strAbout += "  <br />";
			strAbout += "<a href=\'http://www.facebook.com/HolyQuran4Android'>www.facebook.com/HolyQuran4Android </a> <br />";
			strAbout += "<br />";

			strAbout += "</font>";
			//
			lbl.setSingleLine(false);
			lbl.setMovementMethod(LinkMovementMethod.getInstance());
			lbl.setText(Html.fromHtml(strAbout));
			lbl.setGravity(Gravity.CENTER);
			lbl.setTextColor(getResources().getColor(R.color.blackblue));
			/*
			 * chkScreenOn = ((CheckBox) findViewById(R.id.chkScreenOn));
			 * chkScreenOn.setTypeface(arabicFont);
			 * chkScreenOn.setText(AC.getTextbyLanguage(R.string.ScreenOn));
			 */
			//
			Display display = getWindowManager().getDefaultDisplay();
			lbl.setWidth(display.getWidth());


		} catch (Throwable t) {
			Toast.makeText(this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	
}