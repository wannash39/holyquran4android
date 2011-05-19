package com.hamdyghanem.holyquran;

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity {
	/** Called when the activity is first created. */
	ApplicationController AC;
	TableLayout tl;
	int iCurrentDefault = 0;
	Typeface arabicFont = null;
	EditText edit = null;
	SQLiteDatabase db;
	ExternalStorageReadOnlyOpenHelper objdb;
	ProgressDialog dialog;
	int increment = 0;

	// LinearLayout linearScrollView;
	// TableLayout tablelayout;
	// TableRow tablerow;
	// TextView textview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.search);
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
				myTitleText.setText(R.string.Search);
				// myTitleText.setBackgroundColor(R.color.blackblue);
			}
			// //////////////////////
			AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL
			tl = (TableLayout) findViewById(R.id.TableLayoutBody);
			//
			edit = (EditText) findViewById(R.id.editSearch);
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(15);
			edit.setFilters(FilterArray);
			edit.setSingleLine(true);
			Display display = getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			width = width - (60);
			edit.setWidth(width);

			findViewById(R.id.ButtSearch).setOnClickListener(search_listener);
			//
			// Arabizarion
			((Button) findViewById(R.id.ButtSearch)).setTypeface(arabicFont);

			objdb = new ExternalStorageReadOnlyOpenHelper(this);
			//
			if (!objdb.databaseFileExists()) {
				Toast.makeText(this, R.string.notexistdb, Toast.LENGTH_LONG)
				.show();
		
				finish();
			}

		} catch (Throwable t) {
			Toast.makeText(this, "err ->" + t.toString(), Toast.LENGTH_LONG)
					.show();
		}
	}

	private void addRow(String strSuraName, String iPage, String strContent) {
		TableRow tr = new TableRow(this);
		//
		TextView vPage = new CheckBox(this);
		TextView vSuraName = new TextView(this);
		TextView vContenet = new TextView(this);
		RadioButton rb = new RadioButton(this);
		//
		vPage.setTypeface(arabicFont);
		vSuraName.setTypeface(arabicFont);
		vContenet.setTypeface(arabicFont);

		// but.setTextOff("");
		// but.setTextOn("");
		vPage.setText(strSuraName);
		vSuraName.setText(iPage);
		vContenet.setText(strContent);
		//
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		tr.addView(vPage);
		tr.addView(vSuraName);
		tr.addView(vContenet);
		tr.addView(rb);
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

	}

	private OnClickListener search_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (edit.getText() != null
					&& edit.getText().toString().length() > 0)
				doSearchWait();
		}
	};

	private void doSearch() {
		try {
			db = objdb.getReadableDatabase();

			// Cursor mcursor = obj.getQuery("»”„ «··Â «·—Õ„‰ «·—ÕÌ„", db);
			Cursor mcursor = objdb.getQuery("«··Â", db);
			String data = "";
			mcursor.moveToFirst();

			while (mcursor.isAfterLast() == false) {
				for (int i = 0; i < mcursor.getColumnCount(); i++) {
					// view.append("n" + mcursor.getString(i));
					data = mcursor.getString(i);
					addRow(mcursor.getString(i), mcursor.getString(i),
							mcursor.getString(i));
				}
				mcursor.moveToNext();
			}
			mcursor.close();
			objdb.close();

			Toast.makeText(this, "Request failed: " + data, Toast.LENGTH_LONG)
					.show();

		} catch (Throwable t) {
			Toast.makeText(this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	private void doSearchWait() {
		dialog = new ProgressDialog(this);
		dialog.setCancelable(true);
		dialog.setMessage(this.getString(R.string.Searching));
		// set the progress to be horizontal
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// reset the bar to the default value of 0
		increment = 0;
		// dialog.setProgress(0);
		// dialog.setMax(605);
		// display the progressbar
		dialog.show();
		// create a thread for updating the progress bar
		Thread background = new Thread(new Runnable() {
			public void run() {
				doSearch();
				// active the update handler
				progressHandler.sendMessage(progressHandler.obtainMessage());

				// Finished
				dialog.cancel();
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