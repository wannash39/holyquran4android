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
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SelectLanguageActivity extends Activity {
	/** Called when the activity is first created. */
	ProgressDialog dialog;
	int increment = 0;
	String baseDir = "";
	ApplicationController AC;

	// CheckBox chkScreenOn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

			setContentView(R.layout.selectlanguage);
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
			//
			RadioButton rb = ((RadioButton) findViewById(R.id.optAtabic));
			rb.setTypeface(arabicFont);
			rb.setTag(0);
			rb.setText(R.string.LangArabic);
			rb.setChecked(AC.iLanguage == 0);
			rb.setOnClickListener(pagebtn_listener);
			//
			rb = ((RadioButton) findViewById(R.id.optEnglish));
			rb.setTypeface(arabicFont);
			rb.setText(R.string.LangEnglish);
			rb.setChecked(AC.iLanguage == 1);
			rb.setTag(1);
			rb.setOnClickListener(pagebtn_listener);

			//
			((TextView) findViewById(R.id.vwLanguage)).setTypeface(arabicFont);
			((TextView) findViewById(R.id.vwLanguage))
					.setText(R.string.BILanguage);
			//
		} catch (Throwable t) {
			Toast.makeText(this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	private OnClickListener pagebtn_listener = new OnClickListener() {
		public void onClick(View v) {
			try {
				// Perform action on clicks
				RadioButton rb = (RadioButton) v;
				Integer lang= Integer.parseInt(rb.getTag().toString());
				Intent intent = new Intent();
				intent.putExtra("returnKey", lang);
				setResult(RESULT_OK, intent);
				finish();
			} catch (Throwable t) {
				Toast.makeText(SelectLanguageActivity.this,
						"Request failed:" + t.toString(), Toast.LENGTH_LONG)
						.show();
			}
		}
	};
}