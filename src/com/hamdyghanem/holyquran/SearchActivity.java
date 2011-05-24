package com.hamdyghanem.holyquran;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import org.apache.http.util.EncodingUtils;

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
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
	String[] soranames = null;

	int iSearchLanguage = 0;// 0 is Arabic , 1 is English

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
				myTitleText.setText(AC.getTextbyLanguage(R.string.Search));
			}
			// //////////////////////
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
			//
			if (AC.iLanguage == 0)
				soranames = getResources().getStringArray(
						R.array.SoraName_array);
			else
				soranames = getResources().getStringArray(
						R.array.SoraNameEn_array);
			//

			// Arabizarion
			((Button) findViewById(R.id.ButtSearch)).setTypeface(arabicFont);
			((Button) findViewById(R.id.ButtSearch)).setText(AC
					.getTextbyLanguage(R.string.Search));

			RadioButton rb = ((RadioButton) findViewById(R.id.optAtabic));
			rb.setTypeface(arabicFont);
			rb.setText(R.string.LangArabic);
			rb.setChecked(AC.iLanguage == 0);
			rb = ((RadioButton) findViewById(R.id.optEnglish));
			rb.setTypeface(arabicFont);
			rb.setText(R.string.LangEnglish);
			rb.setChecked(AC.iLanguage == 1);
			objdb = new ExternalStorageReadOnlyOpenHelper(this);
			//
			if (!objdb.databaseFileExists()) {
				Toast.makeText(this, AC.getTextbyLanguage(R.string.notexistdb),
						Toast.LENGTH_LONG).show();
				finish();
			}
			// edit.addTextChangedListener(new TextWatcher() {
			// public void afterTextChanged(Editable s) {
			// //Log.d("seachScreen", "afterTextChanged");
			// }
			//
			// public void beforeTextChanged(CharSequence s, int start, int
			// count,
			// int after) {
			// //Log.d("seachScreen", "beforeTextChanged");
			// }
			//
			// public void onTextChanged(CharSequence s, int start, int before,
			// int count) {
			// //Log.d("seachScreen", s.toString());
			// //if(s.charAt(start))
			// //{
			// // iSearchLanguage=1;
			// //}
			//
			// }
			// });
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
		lbl1.setText(AC.getTextbyLanguage(R.string.searchPage));
		lbl2.setText(AC.getTextbyLanguage(R.string.searchSura));
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		
		if(AC.iLanguage==0)
			width = width - (3 * 80);
		else
			width = width - (3 * 100);
		
		lbl4.setWidth(width);
		lbl3.setText(AC.getTextbyLanguage(R.string.searchAya));
		lbl4.setText(AC.getTextbyLanguage(R.string.searchContent));
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		//
		lbl4.setGravity(Gravity.CENTER); // attempt at justifying text
		//
		tr.addView(lbl1);
		tr.addView(lbl2);
		tr.addView(lbl3);
		tr.addView(lbl4);

		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

	}

	private void addRow(String strSuraName, String iPage, String strAya,
			String strContent) {
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
		
		if(AC.iLanguage==0)
			width = width - (3 * 70);
		else
			width = width - (3 * 85);

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
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

	}

	private OnClickListener pagebtn_listener = new OnClickListener() {
		public void onClick(View v) {
			try {
				// Perform action on clicks
				Button btn = (Button) v;
				Integer pos = Integer.parseInt(btn.getText().toString());
				Intent intent = new Intent();
				intent.putExtra("returnKey", pos);
				setResult(RESULT_OK, intent);
				finish();
			} catch (Throwable t) {
				Toast.makeText(SearchActivity.this,
						"Request failed:" + t.toString(), Toast.LENGTH_LONG)
						.show();
			}
		}
	};

	public void DoSearch(View view) {
		if (edit.getText() != null && edit.getText().toString().length() > 2) {

			SelectDataTask task = new SelectDataTask(this);
			String strResult = edit.getText().toString();
			RadioButton rb = ((RadioButton) findViewById(R.id.optEnglish));
			String strlang = "";
			if (rb.isChecked())
				strlang = "en";
			// tell how much u found it
			task.execute(strResult, strlang);
		} else {
			Toast.makeText(this, AC.getTextbyLanguage(R.string.SearchNoData),
					Toast.LENGTH_LONG).show();
		}
	}

	private class SelectDataTask extends AsyncTask<String, Void, String> {

		private Context context;
		ProgressDialog dialog;

		public SelectDataTask(Context cxt) {
			context = cxt;
			dialog = new ProgressDialog(context);
		}

		// can use UI thread here
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage(AC.getTextbyLanguage(R.string.Searching));
			this.dialog.show();
		}

		// automatically done on worker thread (separate from UI thread)
		@Override
		protected String doInBackground(String... whereClause) {
			try {
				objdb = new ExternalStorageReadOnlyOpenHelper(
						SearchActivity.this);
				db = objdb.getReadableDatabase();

				String strwhereClause = whereClause[0];
				String strTableName = "quran" + whereClause[1] ;
				// String strwhereClause = "الله";
				Cursor mcursor = objdb.getQuery(strwhereClause, db,
						strTableName);
				StringBuilder sb = new StringBuilder();
				if (mcursor != null) {
					mcursor.moveToFirst();
					// Log.d("tetst>>>>>>>>>>>>>>>>>>>>>>>>>", tl.toString());
					while (mcursor.isAfterLast() == false) {
						for (int i = 0; i < mcursor.getColumnCount(); i++) {
							// page", "surah","verse", "content"
							sb.append(mcursor.getString(i) + ";");
						}
						sb.append("\n");
						mcursor.moveToNext();
					}

					mcursor.close();
				}
				objdb.close();
				return sb.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			//
			tl.removeAllViews();
			addHeaderRow();
			String[] rows = result.split("\n");
			if (rows.length > 0)

				for (int i = 0; i < rows.length; i++) {
					String[] cols = rows[i].split(";");
					addRow(soranames[Integer.parseInt(cols[1]) - 1], cols[0],
							cols[2], cols[3] + "\r\n");
				}
			Toast.makeText(
					SearchActivity.this,
					AC.getTextbyLanguage(R.string.found) + " : "
							+ Integer.toString((tl.getChildCount() - 1)),
					Toast.LENGTH_LONG).show();
			dialog.dismiss();
		}

	}

}