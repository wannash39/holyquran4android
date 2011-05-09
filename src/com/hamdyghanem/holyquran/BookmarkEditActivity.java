package com.hamdyghanem.holyquran;

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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
	///////////CHANGE THE TITLE BAR///////////////
			Typeface arabicFont = Typeface.createFromAsset(getAssets(),
			"fonts/DroidSansArabic.ttf");

			if ( customTitleSupported ) {
		        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
		    }
		
		    final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		    if ( myTitleText != null ) {
		    	myTitleText.setTypeface(arabicFont);
		        myTitleText.setText(R.string.BookmarkEdit );
		        //myTitleText.setBackgroundColor(R.color.blackblue);
		    }
	////////////////////////
			AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL
			int i = 0;
			tl = (TableLayout) findViewById(R.id.TableLayoutBody);
			addHeaderRow();
			for (Bookmark b : AC.bookmarkUtitliy.arr) {
				addRow(b.getBookmarkName(), b.getPage(),  b.getStatic(),b.getDefault());
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

	private void addRow(String strName, Integer iPage, int iSttc, int iDef) {
		TableRow tr = new TableRow(this);
		CheckBox chk = new CheckBox(this);
		CheckBox chkStatic = new CheckBox(this);
		EditText edit = new EditText(this);
		EditText editPage = new EditText(this);
		RadioButton rb = new RadioButton(this);
		//
		// but.setTextOff("");
		// but.setTextOn("");
		edit.setText(strName);
		// max length
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(15);
		edit.setFilters(FilterArray);
		// Max Length
		FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(3);
		editPage.setFilters(FilterArray);
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
		Typeface arabicFont = Typeface.createFromAsset(getAssets(),
		"fonts/DroidSansArabic.ttf");
		lbl1.setTypeface(arabicFont);
		lbl2.setTypeface(arabicFont);
		lbl3.setTypeface(arabicFont);
		lbl4.setTypeface(arabicFont);
		lbl5.setTypeface(arabicFont);
		//
		lbl1.setText(R.string.Delete);
		lbl2.setText(R.string.Name);
		lbl3.setText(R.string.Page);
		lbl4.setText(R.string.Static);
		lbl5.setText(R.string.Default);
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
				RadioButton rdo = (RadioButton) row.getChildAt(3);
				if (i != iCurrentDefault)
					rdo.setChecked(false);
				iCurrentDefault = i;
				// Log.d("err ->", Integer.toString(iCurrentDefault));

			} catch (Throwable t) {
				Log.d("err ->", t.toString());
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
								.toString(), iPage,chkStatic.isChecked()?1:0, 0));
					} else {
						if (i <= iCurrentDefault)
							iCurrentDefault--;
					}
				}
				if (AC.bookmarkUtitliy.arr.size() == 0)
					AC.bookmarkUtitliy.arr.add(new Bookmark(
							getString(R.string.defaultname), 0,0, 0));

				AC.bookmarkUtitliy.setDefault(iCurrentDefault);
				// Send back to the main
				Intent intent = new Intent();
				intent.putExtra("returnKey", iCurrentDefault);
				setResult(RESULT_OK, intent);
				finish();

			} catch (Throwable t) {
				Log.d("err ->", t.toString());
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
				// Toast.makeText(this, edit.getText().toString(),
				// Toast.LENGTH_LONG).show();
				if (edit.getText().toString().length() == 0) {
					edit.requestFocus();

					return;
				}
			}
			addRow("",0, 0, 0);

		}

	};
	private OnClickListener cancel_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();

		}
	};
}