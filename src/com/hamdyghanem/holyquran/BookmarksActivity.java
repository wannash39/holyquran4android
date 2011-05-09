package com.hamdyghanem.holyquran;

import java.util.ArrayList;

import com.hamdyghanem.holyquran.R;
import com.hamdyghanem.holyquran.GoToActivity.OnChapterSelectedListener;
import com.hamdyghanem.holyquran.GoToActivity.OnSoraSelectedListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class BookmarksActivity extends Activity {
	/** Called when the activity is first created. */
	ApplicationController AC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.bookmarks);
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
			myTitleText.setText(R.string.BookmarksActivity);
			// myTitleText.setBackgroundColor(R.color.blackblue);
		}
		// //////////////////////

		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		// Add Radio buttons for bookmarks
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RadioGroup01);
		radioGroup.setOrientation(RadioGroup.VERTICAL);// or
		AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL
		//
		Spinner spnBookmarks = (Spinner) findViewById(R.id.spnBookmarks);

		// CHAPTER
		// ArrayList<String> strings = new ArrayList<String>();
		String[] strings = new String[AC.bookmarkUtitliy.arr.size() ];
		// //RadioGroup.HORIZONTAL
		int i = 0;
		for (Bookmark b : AC.bookmarkUtitliy.arr) {
			RadioButton rb = new RadioButton(this);
			radioGroup.addView(rb); // the RadioButtons are added to the
			// radioGroup instead of the layout
			String strBookmarkName = b.getBookmarkName();
			strBookmarkName += "  :   " + Integer.toString(b.getPage());
			//
			rb.setText(strBookmarkName);
			strings[i] = strBookmarkName;

			rb.setTag(i);
			rb.setChecked(b.getDefault() == 1);
			// rb.setOnClickListener(radio_listener);

			rb.setWidth(getWallpaperDesiredMinimumWidth());
			rb.setGravity(Gravity.CENTER);

			i++;
		}
		spinneradapter adapter = new spinneradapter(this,
				android.R.layout.simple_spinner_item, strings, arabicFont);
		spnBookmarks.setAdapter(adapter);
		spnBookmarks
				.setOnItemSelectedListener(new OnBookmarkSelectedListener());
		//
		//((Button) findViewById(R.id.ButOK)).setTypeface(arabicFont);
		//((Button) findViewById(R.id.ButCancel)).setTypeface(arabicFont);
		((Button) findViewById(R.id.ButAddNew)).setTypeface(arabicFont);
		((Button) findViewById(R.id.ButEditBookmark)).setTypeface(arabicFont);
		//
		findViewById(R.id.ButAddNew).setOnClickListener(new_listener);
		findViewById(R.id.ButEditBookmark).setOnClickListener(setting_listener );

	}

	public class OnBookmarkSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			// editPage.setText(sorapages[pos]);
			// Toast.makeText(
			// parent.getContext(),
			// "The planet is " + parent.getItemAtPosition(pos).toString(),
			// Toast.LENGTH_LONG).show();
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	/*
	 * private OnClickListener radio_listener = new OnClickListener() { public
	 * void onClick(View v) { // Perform action on clicks RadioButton rb =
	 * (RadioButton) v; Intent intent = new Intent(); Integer i = (Integer)
	 * rb.getTag(); intent.putExtra("returnKey", i); setResult(RESULT_OK,
	 * intent); finish(); } }
	 */;
		private OnClickListener new_listener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				// addRow(AC.GetSora(AC.iCurrentPage),AC.iCurrentPage, 0, 0, true);
			}

		};

		private OnClickListener setting_listener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				// addRow(AC.GetSora(AC.iCurrentPage),AC.iCurrentPage, 0, 0, true);
			}

		};

//	private OnClickListener ok_listener = new OnClickListener() {
//
		@Override
//		public void onClick(View v) {
//			try {
				/*
				 * (RadioButton) v; Intent intent = new Intent(); Integer i =
				 * (Integer) rb.getTag(); intent.putExtra("returnKey", i);
				 * setResult(RESULT_OK, intent); finish(); } }
				 */
//			} catch (Throwable t) {
//				Log.d("err ->", t.toString());
//			}
//		}
//	};

//	private OnClickListener cancel_listener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			finish();
//		}
//	};

}