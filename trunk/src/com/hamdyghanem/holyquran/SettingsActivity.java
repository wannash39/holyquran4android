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
import com.hamdyghanem.holyquran.Download.CreateTabImages;
import com.hamdyghanem.holyquran.Download.DownloadActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {
	/** Called when the activity is first created. */
	ApplicationController AC;
	Typeface arabicFont = null;
	RadioGroup radioGroup = null;
	private SettingsActivity mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		arabicFont = Typeface.createFromAsset(getAssets(),
				"fonts/DroidSansArabic.ttf");

		// //////////////////////
		AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL
		this.setTitle(AC.getTextbyLanguage(R.string.settings));
		mContext = this;

		setPreferenceScreen(createPreferenceHierarchy());
	}

	private PreferenceScreen createPreferenceHierarchy() {
		try {
			// Root
			PreferenceScreen root = getPreferenceManager()
					.createPreferenceScreen(this);
			// Inline preferences
			PreferenceCategory inlinePrefDownload = new PreferenceCategory(this);
			inlinePrefDownload.setTitle(AC.getTextbyLanguage(R.string.ImgType));
			root.addPreference(inlinePrefDownload);
			//
			//
			PreferenceScreen intentPref = getPreferenceManager()
					.createPreferenceScreen(this);
			// intent.putExtra( EXTRAS_KEY, extras );
			Intent intent = new Intent(this, SelectImageType.class);
			intentPref.setIntent(intent);
			intentPref.setTitle(AC.getTextbyLanguage(R.string.ImgType));
			// intentPref.setKey("language_preference");
			intentPref.setSummary(AC.getTextbyLanguage(R.string.ImgTypeSumm));
			inlinePrefDownload.addPreference(intentPref);
			//
			//
			// reciter
			intentPref = getPreferenceManager().createPreferenceScreen(this);
			ListPreference listPref = new ListPreference(this);
			if (AC.iLanguage == 0)
				listPref.setEntries(R.array.Reciter);
			else
				listPref.setEntries(R.array.Reciter_en);
			listPref.setDialogTitle(AC.getTextbyLanguage(R.string.Reciter));
			listPref.setEntryValues(R.array.ReciterValues);
			listPref.setKey("currentreciter_preference");
			listPref.setValue(AC.CurrentReciter);
			listPref.setTitle(AC.getTextbyLanguage(R.string.Reciter));
			listPref.setSummary(AC.getTextbyLanguage(R.string.ReciterSumm));
			inlinePrefDownload.addPreference(listPref);
			//
			//
			inlinePrefDownload = new PreferenceCategory(this);
			inlinePrefDownload
					.setTitle(AC.getTextbyLanguage(R.string.download));
			root.addPreference(inlinePrefDownload);
			intentPref = getPreferenceManager().createPreferenceScreen(this);
			intent = new Intent(this, DownloadActivity.class);
			intentPref.setIntent(intent);
			intentPref.setTitle(AC.getTextbyLanguage(R.string.download));
			intentPref.setSummary(AC.getTextbyLanguage(R.string.downloadSumm));
			inlinePrefDownload.addPreference(intentPref);
			// Create tab images
			Preference createtabPref = new Preference(this);
			createtabPref.setTitle(AC
					.getTextbyLanguage(R.string.createtabimage));
			createtabPref.setSummary(AC
					.getTextbyLanguage(R.string.createtabimagesumm));
			//intentPref.addPreference(createtabPref);
			inlinePrefDownload.addPreference(createtabPref);

			createtabPref
					.setOnPreferenceClickListener(new OnPreferenceClickListener() {
						@Override
						public boolean onPreferenceClick(Preference preference) {
							// TODO Auto-generated method stub
							CreateTabImages cti = new CreateTabImages(mContext,
									AC);
							cti.SaveTabImage();
							return false;
						}
					});
			// ////////////////////////////
			PreferenceCategory inlinePrefSetting = new PreferenceCategory(this);
			inlinePrefSetting.setTitle(AC.getTextbyLanguage(R.string.settings));
			root.addPreference(inlinePrefSetting);
			//
			//
			listPref = new ListPreference(this);
			listPref.setEntries(R.array.Languages);
			listPref.setDialogTitle(AC.getTextbyLanguage(R.string.Language));
			listPref.setEntryValues(R.array.LanguagesValues);
			listPref.setKey("language_preference");
			listPref.setValueIndex(AC.iLanguage);
			listPref.setTitle(AC.getTextbyLanguage(R.string.Language));
			inlinePrefSetting.addPreference(listPref);
			//
			// SCREEN_ORIENTATION
			listPref = new ListPreference(this);
			if (AC.iLanguage == 0)
				listPref.setEntries(R.array.SCREEN_ORIENTATION);
			else
				listPref.setEntries(R.array.SCREEN_ORIENTATION_EN);
			listPref.setDialogTitle(AC
					.getTextbyLanguage(R.string.SCREEN_ORIENTATION));
			listPref.setEntryValues(R.array.SCREEN_ORIENTATION_Values);
			listPref.setKey("currentscreenorientation_preference");
			listPref.setValue(Integer.toString(AC.CurrentSCREEN_ORIENTATION));
			listPref.setTitle(AC.getTextbyLanguage(R.string.SCREEN_ORIENTATION));
			listPref.setSummary(AC
					.getTextbyLanguage(R.string.SCREEN_ORIENTATION_enSumm));
			inlinePrefSetting.addPreference(listPref);
			// if (AC.iLanguage == 0)
			// listPref.setSummary(R.string.LangArabic);
			// else
			// listPref.setSummary(R.string.LangEnglish);
			//
			CheckBoxPreference togglePref = new CheckBoxPreference(this);
			togglePref.setKey("audioon_preference");
			togglePref.setTitle(AC.getTextbyLanguage(R.string.AudioOn));
			togglePref.setSummary(AC.getTextbyLanguage(R.string.AudioOnSumm));
			inlinePrefSetting.addPreference(togglePref);
			//
			togglePref = new CheckBoxPreference(this);
			togglePref.setKey("manualnavigation_preference");
			togglePref.setTitle(AC.getTextbyLanguage(R.string.ManualNav));
			togglePref.setSummary(AC.getTextbyLanguage(R.string.ManualNavSum));
			inlinePrefSetting.addPreference(togglePref);
			//
			//
			togglePref = new CheckBoxPreference(this);
			togglePref.setKey("hidestatusbar_preference");
			togglePref.setTitle(AC.getTextbyLanguage(R.string.HideStatusBar));
			togglePref.setSummary(AC
					.getTextbyLanguage(R.string.HideStatusBarSum));
			inlinePrefSetting.addPreference(togglePref);
			//
			togglePref = new CheckBoxPreference(this);
			togglePref.setKey("screenon_preference");
			togglePref.setTitle(AC.getTextbyLanguage(R.string.ScreenOn));
			togglePref.setSummary(AC.getTextbyLanguage(R.string.ScreenOnAlert));
			inlinePrefSetting.addPreference(togglePref);
			//
			//
			root.addPreference(inlinePrefDownload);
			intentPref = getPreferenceManager().createPreferenceScreen(this);
			intent = new Intent(this, BacklightsettingActivity.class);
			intentPref.setIntent(intent);
			intentPref.setTitle(AC.getTextbyLanguage(R.string.Backlight));
			intentPref.setSummary(AC.getTextbyLanguage(R.string.BacklightSumm));
			inlinePrefSetting.addPreference(intentPref);
			//
			// Preference zoomPref = new Preference(this);
			// zoomPref.setTitle(AC.getTextbyLanguage(R.string.zoomdefault));
			// zoomPref.setSummary(AC.getTextbyLanguage(R.string.zoomdefault));
			// inlinePrefSetting.addPreference(zoomPref);
			// zoomPref.setOnPreferenceClickListener(new
			// OnPreferenceClickListener()
			// {
			// @Override
			// public boolean onPreferenceClick(Preference preference) {
			// // TODO Auto-generated method stub
			// AC.imageScale=1;
			// return false;
			// }
			// });

			return root;
		} catch (Throwable t) {
			Toast.makeText(this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();
		}
		return null;
	}
}