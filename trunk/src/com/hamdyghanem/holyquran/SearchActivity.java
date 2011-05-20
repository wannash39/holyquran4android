package com.hamdyghanem.holyquran;

import java.util.concurrent.ExecutionException;

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity {
	/** Called when the activity is first created. */
	ApplicationController AC;
	public TableLayout tl;
	int iCurrentDefault = 0;
	Typeface arabicFont = null;
	EditText edit = null;
	public SQLiteDatabase db;
	public ExternalStorageReadOnlyOpenHelper objdb;
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
			tl = (TableLayout) findViewById(R.id.searchTableLayoutResult);

			
			//
			edit = (EditText) findViewById(R.id.editSearch);
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(15);
			edit.setFilters(FilterArray);
			edit.setSingleLine(true);
			Display display = getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			width = width - (100);
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
	private void addHeaderRow() {

		TableRow tr = new TableRow(this);
		TextView lbl1 = new TextView(this);
		TextView lbl2 = new TextView(this);
		TextView lbl3 = new TextView(this);
		TextView lbl4 = new TextView(this);
		//
		// Arabizarion

		lbl1.setTypeface(arabicFont);
		lbl2.setTypeface(arabicFont);
		lbl3.setTypeface(arabicFont);
		lbl4.setTypeface(arabicFont);
		//
		lbl1.setText(R.string.searchPage);
		lbl2.setText(R.string.searchSura);
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		width = width - (3 * 70);
lbl4.setWidth(width);
		lbl3.setText(R.string.searchAya);
		lbl4.setText(R.string.searchContent);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		tr.addView(lbl1);
		tr.addView(lbl2);
		tr.addView(lbl3);
		tr.addView(lbl4);
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

	}
	private void addSepratorRow() {
		TableRow tr = new TableRow(this);
		tl. addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

	}
	private void addRow(String strSuraName, String iPage,String strAya, String strContent) {
		TableRow tr = new TableRow(this);
		//
		Button vPage = new Button(this);
		TextView vSuraName = new TextView(this);
		TextView vAya = new TextView(this);
		TextView vContenet = new TextView(this);
		//
		vPage.setTypeface(arabicFont);
		vSuraName.setTypeface(arabicFont);
		vAya.setTypeface(arabicFont);
		vContenet.setTypeface(arabicFont);
		
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		width = width - (3 * 70);
		vContenet.setWidth(width);
		
		// but.setTextOff("");
		// but.setTextOn("");
		vPage.setText(iPage);
		vSuraName.setText(strSuraName);
		vAya.setText(strAya);
		vContenet.setText(strContent);
		//
		vAya.setGravity(Gravity.CENTER); // attempt at justifying text 
		vContenet.setGravity(Gravity.CENTER); // attempt at justifying text 
		vSuraName.setGravity(Gravity.CENTER); // attempt at justifying text 
		
		//
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
//
		vPage.setOnClickListener(pagebtn_listener);

		//
		tr.addView(vPage);
		tr.addView(vSuraName);
		tr.addView(vAya);
		tr.addView(vContenet);
		tl. addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

	}
	private OnClickListener pagebtn_listener = new OnClickListener() {
		public void onClick(View v) {
			try {
				// Perform action on clicks
				Button btn = (Button) v;
				Integer i = Integer.parseInt( btn.getText().toString());
				
				Toast.makeText(SearchActivity.this,
						"Request failed:" + btn.getText().toString(), Toast.LENGTH_LONG)
						.show();
			
				// Log.d("err ->", Integer.toString(iCurrentDefault));
			} catch (Throwable t) {
				Toast.makeText(SearchActivity.this,
						"Request failed:" + t.toString(), Toast.LENGTH_LONG)
						.show();
			}
		}
	};

	private OnClickListener search_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (edit.getText() != null
					&& edit.getText().toString().length() > 0) {
				SelectDataTask task = new SelectDataTask();
				String strResult = "";
				task.execute(edit.getText().toString(), "", "");
				
				try {
					strResult = task.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	};

	private class SelectDataTask extends AsyncTask<String, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				SearchActivity.this);

		// can use UI thread here
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Selecting data...");
			this.dialog.show();
		}

		// automatically done on worker thread (separate from UI thread)
		@Override
		protected String doInBackground(String... whereClause) {
			db = objdb.getReadableDatabase();
			Cursor mcursor = objdb.getQuery("ÇáÌäÉ", db);
			StringBuilder sb = new StringBuilder();
			 mcursor.moveToFirst();
				 Log.d("tetst>>>>>>>>>>>>>>>>>>>>>>>>>",tl.toString());

			while (mcursor.isAfterLast() == false) {
				for (int i = 0; i < mcursor.getColumnCount(); i++) {
					// view.append("n" + mcursor.getString(i));
					// data = mcursor.getString(i);
					// addRow(mcursor.getString(i), mcursor.getString(i),
					// mcursor.getString(i));
					// page", "surah","verse", "content"
					sb.append(mcursor.getString(i) + ";");
				}
			
				//addRow("ÇáÝÇÊÍÉ", mcursor.getString(0), mcursor.getString(3));
				sb.append("\n");

				mcursor.moveToNext();
			}
			mcursor.close();
			objdb.close();
			// StringBuilder sb = new StringBuilder();
			// for (String name : names) {
			// sb.append(name + "\n");
			// }
			//
			//if (this.dialog.isShowing()) {
			//	this.dialog.dismiss();
			//}
			return sb.toString();

		}

		@Override
		protected void onPostExecute(String result) {
			//
			tl.removeAllViews();
			addHeaderRow();
			String[] soranames = getResources().getStringArray(
					R.array.SoraName_array);
			
			String[] rows = result.split("\n");
			for (int i = 0; i < rows.length - 1; i++) {
				String[] cols = rows[i].split(";");
					//addRow("ÇáÝÇÊÍÉ", cols[0], cols[3]);
				addRow(soranames [Integer.parseInt(cols[1])], cols[0],cols[2], cols[3] + "\r\n");
				addSepratorRow();
			}
			this.dialog.dismiss();
		}

	}

}