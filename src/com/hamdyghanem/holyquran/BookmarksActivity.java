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

import java.util.ArrayList;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.hamdyghanem.holyquran.R;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class BookmarksActivity extends Activity implements AdListener {
	/** Called when the activity is first created. */
	ApplicationController AC;
	Typeface arabicFont = null;
	RadioGroup radioGroup = null;
	private AdView adView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// final boolean customTitleSupported =
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.bookmarks);
		AC = (ApplicationController) getApplicationContext();

		// /////////CHANGE THE TITLE BAR///////////////
		arabicFont = Typeface.createFromAsset(getAssets(),
				"fonts/DroidSansArabic.ttf");
		this.setTitle(AC.getTextbyLanguage(R.string.BookmarksActivity));

		// if (customTitleSupported) {
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.mytitle);
		// }
		//
		// final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		// if (myTitleText != null) {
		// // myTitleText.setTypeface(arabicFont);
		// myTitleText.setText(AC
		// .getTextbyLanguage(R.string.BookmarksActivity));
		// myTitleText.setBackgroundColor(R.color.blackblue);
		// }
		// //////////////////////
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		// Add Radio buttons for bookmarks
		radioGroup = (RadioGroup) findViewById(R.id.RadioGroup01);
		//
		LoadSpinner();
		// ((Button) findViewById(R.id.ButOK)).setTypeface(arabicFont);
		// ((Button) findViewById(R.id.ButCancel)).setTypeface(arabicFont);
		((Button) findViewById(R.id.ButAddNew)).setTypeface(arabicFont);
		((Button) findViewById(R.id.ButAddNew)).setText(AC
				.getTextbyLanguage(R.string.ButtNewCurrent));

		((Button) findViewById(R.id.ButEditBookmark)).setTypeface(arabicFont);
		((Button) findViewById(R.id.ButEditBookmark)).setText(AC
				.getTextbyLanguage(R.string.BookmarkEdit));
		//

		// Set the AdListener.
		adView = new AdView(this, AdSize.BANNER, AC.MY_INTERSTITIAL_UNIT_ID);
		adView.setAdListener(this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.adhost);

		layout.addView(adView);
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice("6FF5360DEE298C4BDCF32E5F6C3EEDEC");
		adView.loadAd(adRequest);

		Log.d("AdExamples_Class", "loadInterstitial");
	}

	@Override
	public void onStop() {
		AC.saveBookmarksDefalut();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		if (adView != null) {
			// Destroy the AdView.
			adView.destroy();
		}

		super.onDestroy();
	}

	/*
	 * @Override protected void onStop() { Intent intent = new Intent();
	 * intent.putExtra("returnKey", 1); setResult(RESULT_OK, intent); finish();
	 * };
	 */
	public class OnBookmarkSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Intent intent = new Intent();
			intent.putExtra("returnKey", pos);
			setResult(RESULT_OK, intent);
			finish();
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	public void NewBookmarkClick(View v) {

		// Check Already exist
		String strName = AC.GetSora(AC.iCurrentPage);
		if (!CheckBookmarkName(strName)) {
			AC.bookmarkUtitliy.arr.add(new Bookmark(strName, AC.iCurrentPage,
					0, 0));
			LoadSpinner();
			AC.bookmarkUtitliy.setDefault(AC.bookmarkUtitliy.arr.size() - 1);
			((Button) findViewById(R.id.ButAddNew)).setEnabled(false);
		}
	}

	private Boolean CheckBookmarkName(String strName) {

		// Check Already exist
		for (int i = 0; i < AC.bookmarkUtitliy.arr.size(); i++) {
			if (AC.bookmarkUtitliy.arr.get(i).getBookmarkName().equals(strName)) {
				Toast.makeText(this, getString(R.string.alreadyexistbookmark),
						Toast.LENGTH_LONG).show();

				return true;
			}
		}
		return false;
	}

	public void EditBookmarkClick(View v) {
		startActivityForResult(new Intent(this, BookmarkEditActivity.class), 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (data == null || data.getExtras() == null)
				return;
			Bundle extras = data.getExtras();
			Integer i = extras.getInt("returnKey");
			LoadSpinner();
			super.onActivityResult(requestCode, resultCode, data);
			AC.bookmarkUtitliy.setDefault(i);
			AC.saveBookmarksDefalut();
			// Toast.makeText(this, Integer.toString(i),
			// Toast.LENGTH_LONG).show();
			// radioGroup .getChildAt(i).setSelected(true);

		}
	}

	private void LoadSpinner() {
		radioGroup.setOrientation(RadioGroup.VERTICAL);// or
		radioGroup.removeAllViews();
		Display display = getWindowManager().getDefaultDisplay();
		for (int i = 0; i < AC.bookmarkUtitliy.arr.size(); i++) {
			Bookmark b = AC.bookmarkUtitliy.arr.get(i);
			String strBookmarkName = b.getBookmarkName();
			// strBookmarkName += "  " + Integer.toString(b.getPage()) + "";
			//
			RadioButton rb = new RadioButton(this);
			radioGroup.addView(rb); // the RadioButtons are added to the
			//
			rb.setText(strBookmarkName);
			rb.setTag(i);
			rb.setChecked(b.getDefault() == 1);
			rb.setOnClickListener(radio_listener);

			// rb.setWidth(getWallpaperDesiredMinimumWidth());
			rb.setWidth(display.getWidth());
			rb.setGravity(Gravity.CENTER);
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

	@Override
	public void onReceiveAd(Ad ad) {
		Log.d("AdExamples_Class", "I received an ad");
		// if (ad == interstitial) {
		// interstitial.show();
		// super.onReceiveAd ( adView );

		// }
	}

	@Override
	public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode error) {
		Log.d("AdExamples_Class", "I failed to receive an ad");
		Log.d("AdExamples_Class", error.toString());
	}

	@Override
	public void onPresentScreen(Ad ad) {
		Log.d("AdExamples_Class", "Presenting screen");
		// Deactivate buttons so interstitial returns before they can be
		// clicked.
		// if (bannerButton != null) {
		// bannerButton.setEnabled(false);
		// }
		// if (interstitialButton != null) {
		// interstitialButton.setEnabled(false);
		// }
	}

	@Override
	public void onDismissScreen(Ad ad) {
		Log.d("AdExamples_Class", "Dismissing screen");
		// Reactivate buttons after interstitial is dismissed.
		// if (bannerButton != null) {
		// bannerButton.setEnabled(true);
		// }
		// if (interstitialButton != null) {
		// interstitialButton.setEnabled(true);
		// }
	}

	@Override
	public void onLeaveApplication(Ad ad) {
		Log.d("AdExamples_Class", "Leaving application");
	}
}