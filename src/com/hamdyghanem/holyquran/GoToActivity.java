package com.hamdyghanem.holyquran;

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GoToActivity extends Activity {
	/** Called when the activity is first created. */
	ApplicationController AC;
	TableLayout tl;
	EditText editPage;
	Integer iPage = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			 final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
				
			setContentView(R.layout.navigate);
	///////////CHANGE THE TITLE BAR///////////////
			Typeface arabicFont = Typeface.createFromAsset(getAssets(),
			"fonts/DroidSansArabic.ttf");

			if ( customTitleSupported ) {
		        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
		    }
		
		    final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		    if ( myTitleText != null ) {
		    	myTitleText.setTypeface(arabicFont);
		        myTitleText.setText(R.string.GoToActivity );
		        //myTitleText.setBackgroundColor(R.color.blackblue);
		    }
	////////////////////////
			AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL
			//
			tl = (TableLayout) findViewById(R.id.TableLayoutBody);
			editPage = (EditText) findViewById(R.id.txtPageNum);
			Spinner snpChapter = (Spinner) findViewById(R.id.spnChapter);
			Spinner snpSora = (Spinner) findViewById(R.id.spnSora);

			// CHAPTER
			// ArrayAdapter.createFromResource(context, textArrayResId,
			// textViewResId)
			spinneradapter adapter = spinneradapter.createFromResource(this,
					R.array.Chapter_array,
					android.R.layout.simple_spinner_item, arabicFont);

			// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			snpChapter.setAdapter(adapter);
			snpChapter
					.setOnItemSelectedListener(new OnChapterSelectedListener());
			// SORA
			adapter = spinneradapter.createFromResource(this,
					R.array.SoraName_array,
					android.R.layout.simple_spinner_item, arabicFont);

			// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			snpSora.setAdapter(adapter);
			snpSora.setOnItemSelectedListener(new OnSoraSelectedListener());
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(3);
			editPage.setFilters(FilterArray);
			// Only integer
			editPage.setInputType(InputType.TYPE_CLASS_NUMBER);
			//
			findViewById(R.id.ButOK).setOnClickListener(ok_listener);
			findViewById(R.id.ButCancel).setOnClickListener(cancel_listener);
			// Arabizarion
			((Button) findViewById(R.id.ButOK)).setTypeface(arabicFont);
			((Button) findViewById(R.id.ButCancel)).setTypeface(arabicFont);
			((TextView) findViewById(R.id.lblChapter)).setTypeface(arabicFont);
			((TextView) findViewById(R.id.lblSora)).setTypeface(arabicFont);
			((TextView) findViewById(R.id.lblPageNum)).setTypeface(arabicFont);
			((TextView) findViewById(R.id.TextViewHeader)).setTypeface(arabicFont);
			
		} catch (Throwable t) {
			Toast.makeText(this, "err ->" + t.toString(), Toast.LENGTH_LONG)
					.show();
		}
	}

	public class OnChapterSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Integer iChapter = pos;
			iPage = ((iChapter) * 20) + 2;
			// exception of the forumula
			if (iChapter == 0)
				iPage = 1;
			editPage.setText(Integer.toString(iPage));
			// Toast.makeText(
			// parent.getContext(),
			// "The planet is " + parent.getItemAtPosition(pos).toString(),
			// Toast.LENGTH_LONG).show();
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	public class OnSoraSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			String[] sorapages = getResources().getStringArray(
					R.array.SoraValue_array);
			editPage.setText(sorapages[pos]);
			// Toast.makeText(
			// parent.getContext(),
			// "The planet is " + parent.getItemAtPosition(pos).toString(),
			// Toast.LENGTH_LONG).show();
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	private OnClickListener ok_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				iPage = Integer.parseInt(editPage.getText().toString());
				if (iPage > 604)
					iPage = 604;
				if (iPage < 1)
					iPage = 1;
				// Send back to the main
				Log.d("Page", Integer.toString(iPage));
				Integer i = AC.bookmarkUtitliy.getDefault();
				AC.bookmarkUtitliy.arr.get(i).setPage(iPage);
				Intent intent = new Intent();
				intent.putExtra("returnKey", i);
				setResult(RESULT_OK, intent);
				finish();

			} catch (Throwable t) {
				Log.d("err ->", t.toString());
			}
		}
	};

	private OnClickListener cancel_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();

		}
	};
}