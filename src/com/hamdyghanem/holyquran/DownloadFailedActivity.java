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

public class DownloadFailedActivity extends Activity {
	/** Called when the activity is first created. */
	
	ApplicationController AC;

	// CheckBox chkScreenOn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

			setContentView(R.layout.downloadfailed);

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
			TextView lbl = (TextView) findViewById(R.id.txtRecitationDownload);
			lbl.setTypeface(arabicFont);

			String strAbout = "<center><H1>";
			strAbout += "<font color='green'>";
			strAbout += "  <br />";
			if (AC.iLanguage == 0) {
				strAbout += "  ‰Ÿ—« ·· Õ„Ì· ⁄·Ì «·Œ«œ„ ‰√”› ·⁄œ„  „ﬂ‰‰« „‰  ‰“Ì· «·„·›«  „‰ «·„Êﬁ⁄ Ì„ﬂ‰ﬂ «·œŒÊ· ⁄·Ì ’›Õ… «·»—‰«„Ã ⁄·Ì «·›Ì” »Êﬂ · Ãœ —Ê«»ÿ „»«‘—… Ê ﬂÌ›Ì…  ‰“Ì·Â« ⁄·Ì «·ÃÊ«· <br />";

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