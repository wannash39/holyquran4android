package com.hamdyghanem.holyquran;

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class BookmarksActivity extends Activity {
	/** Called when the activity is first created. */
	ApplicationController AC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			
		setContentView(R.layout.bookmarks);
///////////CHANGE THE TITLE BAR///////////////
		Typeface arabicFont = Typeface.createFromAsset(getAssets(),
		"fonts/DroidSansArabic.ttf");

		if ( customTitleSupported ) {
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
	    }
	
	    final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
	    if ( myTitleText != null ) {
	    	myTitleText.setTypeface(arabicFont);
	        myTitleText.setText(R.string.BookmarksActivity );
	        //myTitleText.setBackgroundColor(R.color.blackblue);
	    }
////////////////////////
	    
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		// Add Radio buttons for bookmarks
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RadioGroup01);
		radioGroup.setOrientation(RadioGroup.VERTICAL);// or
		AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL
		// //RadioGroup.HORIZONTAL
		int i = 0;
		for (Bookmark b : AC.bookmarkUtitliy.arr) {
			RadioButton rb = new RadioButton(this);
			radioGroup.addView(rb); // the RadioButtons are added to the
			// radioGroup instead of the layout
			String strBookmarkName = b.getBookmarkName();
			strBookmarkName += "  :   " + Integer.toString(b.getPage()) ;
			//
			rb.setText(strBookmarkName);
			rb.setTag(i);
			rb.setChecked(b.getDefault() == 1);
			rb.setOnClickListener(radio_listener);

			rb.setWidth(getWallpaperDesiredMinimumWidth());
			rb.setGravity(Gravity.CENTER);

			i++;
		}
	}

	private OnClickListener radio_listener = new OnClickListener() {
		public void onClick(View v) {
			// Perform action on clicks
			RadioButton rb = (RadioButton) v;
			Intent intent = new Intent();
			Integer i = (Integer) rb.getTag();
			intent.putExtra("returnKey", i);
			setResult(RESULT_OK, intent);
			finish();
		}
	};
}