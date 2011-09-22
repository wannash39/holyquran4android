package com.hamdyghanem.holyquran;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

public class AboutActivity extends Activity {
	/** Called when the activity is first created. */
	ApplicationController AC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.about);
		//
		AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL
		//
		// /////////CHANGE THE TITLE BAR///////////////
		Typeface arabicFont = Typeface.createFromAsset(getAssets(),
				"fonts/DroidSansArabic.ttf");

		if (customTitleSupported) {
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.mytitle);
		}

		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		if (myTitleText != null) {
			myTitleText.setTypeface(arabicFont);
			myTitleText.setText(AC.getTextbyLanguage(R.string.AboutActivity));
			// myTitleText.setBackgroundColor(R.color.blackblue);
		}
		// //////////////////////
		String strAbout = "<center><H1>";
		strAbout += "<font color='black'>";
		if (AC.iLanguage == 0) {
			strAbout += " القرآن الكريم <br />";
			strAbout += "مصحف المدينة <br />";
			strAbout += " الإصدار الأول V 1.2 <br />";

			strAbout += "</H1>";
			strAbout += "محاولة لدعم البرامج الاسلامية على سوق الاندرويد <br />";
			strAbout += "البرنامج مفتوح المصدر و يمكنك الذهاب الي موقع المصدر حيث يمكنكم الدعم او النسخ من  <br />";
			strAbout += "<a href=\'http://code.google.com/p/holyquran4android/'>هنا</a> <br />";

			strAbout += " للمساعدة و الشرح و الدعم اشترك معنا علي صفحة الفيس بوك  ";
			strAbout += "<a href=\'http://www.facebook.com/HolyQuran4Android'>هنا </a> <br />";
			strAbout += "<br />";
			strAbout += "للتواصل";
		} else {
			strAbout += " Holy Quran <br />";
			strAbout += "Mushaf Al Madina <br />";
			strAbout += " V 1.2 <br />";

			strAbout += "</H1>";
			strAbout += "Attempt to support the islamic  software on Android market <br />";
			strAbout += "The program is open source and you can go to the source site where you can support us or copy <br />";
			strAbout += "<a href=\'http://code.google.com/p/holyquran4android/'>here</a> <br />";

			strAbout += " For more details and support please join us in facebook";
			strAbout += "<a href=\'http://www.facebook.com/HolyQuran4Android'>here </a> <br />";
			strAbout += "<br />";
			strAbout += "Contact us";
		}
		strAbout += "<a href='mailto:Hamdy.ghanem@gmail.com'>hamdy.ghanem@gmail.com</a> ";
		strAbout += "<br />";
		strAbout += "</font>";
		//
		TextView lbl = (TextView) findViewById(R.id.txtData);
		lbl.setSingleLine(false);
		lbl.setMovementMethod(LinkMovementMethod.getInstance());
		lbl.setText(Html.fromHtml(strAbout));
		lbl.setGravity(Gravity.CENTER);
		lbl.setTextColor(getResources().getColor(R.color.blackblue));

		lbl.setTypeface(arabicFont);
	}
}
