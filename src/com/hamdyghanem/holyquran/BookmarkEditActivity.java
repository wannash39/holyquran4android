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

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BookmarkEditActivity extends Activity {
	/** Called when the activity is first created. */
	ApplicationController AC;
	TableLayout tl;
	int iCurrentDefault = 0;
	Typeface arabicFont = null;

	// LinearLayout linearScrollView;
	// TableLayout tablelayout;
	// TableRow tablerow;
	// TextView textview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.bookmarkedit);
			AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL

			// /////////CHANGE THE TITLE BAR///////////////
			arabicFont = Typeface.createFromAsset(getAssets(),
					"fonts/DroidSansArabic.ttf");

			if (customTitleSupported) {
				getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
						R.layout.mytitle);
			}

			final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
			if (myTitleText != null) {
				myTitleText.setTypeface(arabicFont);
				myTitleText.setText(AC
						.getTextbyLanguage(R.string.BookmarkEdit));
				// myTitleText.setBackgroundColor(R.color.blackblue);
			}
			// //////////////////////
			int i = 0;
			tl = (TableLayout) findViewById(R.id.TableLayoutBody);
			addHeaderRow();
			for (Bookmark b : AC.bookmarkUtitliy.arr) {
				addRow(b.getBookmarkName(), b.getPage(), b.getStatic(),
						b.getDefault(), false);
				if (b.getDefault() == 1)
					iCurrentDefault = i;
				// rb.setOnClickListener(radio_listener);
				i++;
			}

			findViewById(R.id.ButNew).setOnClickListener(new_listener);
			findViewById(R.id.ButOK).setOnClickListener(ok_listener);
			findViewById(R.id.ButCancel).setOnClickListener(cancel_listener);
			//
			// Arabizarion
			((Button) findViewById(R.id.ButNew)).setTypeface(arabicFont);
			((Button) findViewById(R.id.ButOK)).setTypeface(arabicFont);
			((Button) findViewById(R.id.ButCancel)).setTypeface(arabicFont);

		} catch (Throwable t) {
			Toast.makeText(this, "err ->" + t.toString(), Toast.LENGTH_LONG)
					.show();
		}
	}

	private void addRow(String strName, Integer iPage, int iSttc, int iDef,
			Boolean bCheck) {
		TableRow tr = new TableRow(this);
		// Check duplicate

		if (bCheck) {
			if (CheckBookmarkName(strName))
				strName = "";

		}
		CheckBox chk = new CheckBox(this);
		CheckBox chkStatic = new CheckBox(this);
		EditText edit = new EditText(this);
		EditText editPage = new EditText(this);
		RadioButton rb = new RadioButton(this);
		//
		edit.setTypeface(arabicFont);

		// but.setTextOff("");
		// but.setTextOn("");
		edit.setText(strName);
		// max length
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(15);
		edit.setFilters(FilterArray);
		edit.setSingleLine(true);
		// Max Length
		FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(3);
		editPage.setFilters(FilterArray);
		editPage.setSingleLine(true);
		// Only integer
		editPage.setInputType(InputType.TYPE_CLASS_NUMBER);

		editPage.setText(iPage.toString());

		rb.setChecked(iDef == 1);
		chkStatic.setChecked(iSttc == 1);
		rb.setTag(tl.getChildCount() - 1);
		rb.setOnClickListener(radio_listener);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		tr.addView(chk);
		tr.addView(edit);
		tr.addView(editPage);
		tr.addView(chkStatic);
		tr.addView(rb);
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		if (bCheck)
		edit.requestFocus();
	}

	private Boolean CheckBookmarkName(String strName) {
		// Check Already exist
		for (int i = 1; i < tl.getChildCount(); i++) {
			TableRow row = (TableRow) tl.getChildAt(i);
			EditText edit = (EditText) row.getChildAt(1);
			if (edit.getText().toString().equals(strName)) {
				Toast.makeText(this, getString(R.string.alreadyexistbookmark),
						Toast.LENGTH_LONG).show();
				return true;
			}
		}

		return false;
	}

	private void addHeaderRow() {
		TableRow tr = new TableRow(this);
		TextView lbl1 = new TextView(this);
		TextView lbl2 = new TextView(this);
		TextView lbl3 = new TextView(this);
		TextView lbl4 = new TextView(this);
		TextView lbl5 = new TextView(this);
		//
		// Arabizarion

		lbl1.setTypeface(arabicFont);
		lbl2.setTypeface(arabicFont);
		lbl3.setTypeface(arabicFont);
		lbl4.setTypeface(arabicFont);
		lbl5.setTypeface(arabicFont);
		//
		lbl1.setText(AC.getTextbyLanguage(R.string.Delete));
		lbl2.setText(AC.getTextbyLanguage(R.string.Name));
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		width = width - (4 * 70);
		lbl2.setWidth(width);
		lbl3.setText(AC.getTextbyLanguage(R.string.Page));
		lbl4.setText(AC.getTextbyLanguage(R.string.Static));
		lbl5.setText(AC.getTextbyLanguage(R.string.Default));
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		tr.addView(lbl1);
		tr.addView(lbl2);
		tr.addView(lbl3);
		tr.addView(lbl4);
		tr.addView(lbl5);
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

	}

	private OnClickListener radio_listener = new OnClickListener() {
		public void onClick(View v) {
			try {
				// Perform action on clicks
				RadioButton rb = (RadioButton) v;
				Integer i = (Integer) rb.getTag();
				// Remove the old radio checked
				TableRow row = (TableRow) tl.getChildAt(iCurrentDefault + 1);
				RadioButton rdo = (RadioButton) row.getChildAt(4);
				if (i != iCurrentDefault)
					rdo.setChecked(false);
				iCurrentDefault = i;
				// Log.d("err ->", Integer.toString(iCurrentDefault));
			} catch (Throwable t) {
				Toast.makeText(BookmarkEditActivity.this,
						"Request failed:" + t.toString(), Toast.LENGTH_LONG)
						.show();
			}
		}
	};

	private OnClickListener ok_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				// check if any one empty
				AC.bookmarkUtitliy.arr.clear();
				for (int i = 1; i < tl.getChildCount(); i++) {
					TableRow row = (TableRow) tl.getChildAt(i);
					CheckBox chk = (CheckBox) row.getChildAt(0);
					EditText edit = (EditText) row.getChildAt(1);
					EditText editPage = (EditText) row.getChildAt(2);
					CheckBox chkStatic = (CheckBox) row.getChildAt(3);
					//
					String strPage = editPage.getText().toString();
					Integer iPage = Integer.parseInt(strPage);
					if (iPage > 604)
						iPage = 604;
					if (iPage < 1)
						iPage = 1;

					if (!chk.isChecked()
							&& edit.getText().toString().length() != 0) {
						AC.bookmarkUtitliy.arr.add(new Bookmark(edit.getText()
								.toString(), iPage, chkStatic.isChecked() ? 1
								: 0, 0));
					} else {
						if (i <= iCurrentDefault)
							iCurrentDefault--;
					}
				}
				if (AC.bookmarkUtitliy.arr.size() == 0)
					AC.bookmarkUtitliy.arr.add(new Bookmark(
							getString(R.string.defaultname), 0, 0, 0));
				if (iCurrentDefault >= AC.bookmarkUtitliy.arr.size())
					iCurrentDefault = AC.bookmarkUtitliy.arr.size() - 1;
				// Toast.makeText(BookmarkEditActivity.this,
				// Integer.toString(iCurrentDefault), Toast.LENGTH_LONG)
				// .show();

				AC.bookmarkUtitliy.setDefault(iCurrentDefault);
				// Send back to the main
				Intent intent = new Intent();
				intent.putExtra("returnKey", iCurrentDefault);
				setResult(RESULT_OK, intent);
				finish();

			} catch (Throwable t) {
				Toast.makeText(BookmarkEditActivity.this,
						"Request failed:" + t.toString(), Toast.LENGTH_LONG)
						.show();

			}
		}
	};
	private OnClickListener new_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			for (int i = 1; i < tl.getChildCount(); i++) {
				// then, you can remove the the row you want...
				// for instance...
				TableRow row = (TableRow) tl.getChildAt(i);
				EditText edit = (EditText) row.getChildAt(1);
				// Toast.makeText(this, edit.AC.getTextbyLanguage().toString(),
				// Toast.LENGTH_LONG).show();
				if (edit.getText().toString().length() == 0) {
					edit.requestFocus();

					return;
				}
			}
			addRow(AC.GetSora(AC.iCurrentPage), AC.iCurrentPage, 0, 0, true);
		}

	};

	private OnClickListener cancel_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();

		}
	};

}