package com.hamdyghanem.holyquran;

import java.util.ArrayList;

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;

import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
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
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {
	/** Called when the activity is first created. */
	ApplicationController AC;
	Typeface arabicFont = null;
	RadioGroup radioGroup = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		arabicFont = Typeface.createFromAsset(getAssets(),
				"fonts/DroidSansArabic.ttf");
		// //////////////////////
		AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL

		setPreferenceScreen(createPreferenceHierarchy());
	}

	private PreferenceScreen createPreferenceHierarchy() {
		// Root
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(
				this);
		// Inline preferences
		PreferenceCategory inlinePrefDownload = new PreferenceCategory(this);
		inlinePrefDownload.setTitle(AC.getTextbyLanguage(R.string.ImgType));
		root.addPreference(inlinePrefDownload);
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
		inlinePrefDownload = new PreferenceCategory(this);
		inlinePrefDownload.setTitle(AC.getTextbyLanguage(R.string.download));
		root.addPreference(inlinePrefDownload);
		intentPref = getPreferenceManager().createPreferenceScreen(this);
		intent = new Intent(this, DownloadActivity.class);
		intentPref.setIntent(intent);
		intentPref.setTitle(AC.getTextbyLanguage(R.string.download));
		intentPref.setSummary(AC.getTextbyLanguage(R.string.downloadSumm));
		inlinePrefDownload.addPreference(intentPref);
		// /
		PreferenceCategory inlinePrefSetting = new PreferenceCategory(this);
		inlinePrefSetting.setTitle(AC.getTextbyLanguage(R.string.settings));
		root.addPreference(inlinePrefSetting);
		//
		//
		ListPreference listPref = new ListPreference(this);
		listPref.setEntries(R.array.Languages);
		listPref.setDialogTitle(AC.getTextbyLanguage(R.string.Language));
		listPref.setEntryValues(R.array.LanguagesValues);
		listPref.setKey("language_preference");
		listPref.setValueIndex(AC.iLanguage);
		listPref.setTitle(AC.getTextbyLanguage(R.string.Language));
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
		togglePref = new CheckBoxPreference(this);
		togglePref.setKey("screenon_preference");
		togglePref.setTitle(AC.getTextbyLanguage(R.string.ScreenOn));
		togglePref.setSummary(AC.getTextbyLanguage(R.string.ScreenOnAlert));
		inlinePrefSetting.addPreference(togglePref);

		return root;
	}
}