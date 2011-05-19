package com.hamdyghanem.holyquran;

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
///////////CHANGE THE TITLE BAR///////////////
		Typeface arabicFont = Typeface.createFromAsset(getAssets(),
		"fonts/DroidSansArabic.ttf");

		if ( customTitleSupported ) {
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
	    }
	
	    final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
	    if ( myTitleText != null ) {
	    	myTitleText.setTypeface(arabicFont);
	        myTitleText.setText(R.string.AboutActivity );
	        //myTitleText.setBackgroundColor(R.color.blackblue);
	    }
////////////////////////
	    
		String strAbout = "<center><H1>";
		strAbout += "<font color='black'>";
		strAbout += " القرآن الكريم <br />";
		strAbout += "مصحف المدينة <br />";
		strAbout += " الإصدار الأول V 1.0 <br />";
		
		strAbout += "</H1>";
		strAbout += "محاولة لدعم البرامج الاسلامية على سوق الاندرويد <br />";
		strAbout += "البرنامج مفتوح المصدر و يمكنك الذهاب الي موقع المصدر حيث يمكنكم الدعم او النسخ من  <br />";
		strAbout += "<a href=\'https://www.facebook.com/HolyQuran4Android'>هنا</a> <br />";

		strAbout += " للمساعدة و الشرح يمكنك التصفح من  ";
		strAbout += "<a href=\'http://knol.google.com/k/hamdy-ghanem/holyquran4android/3n0yrj5rx5x7a/29?hd=ns# '>هنا </a> <br />";
		strAbout += "<br />";
		strAbout += "للتواصل";
		strAbout += "<a href='mailto:Hamdy.ghanem@gmail.com'>Hamdy.ghanem@gmail.com</a> ";
		strAbout += "<br />";
		strAbout += "</font>";
		//
		TextView lbl = (TextView) findViewById(R.id.TextView01);

		lbl.setSingleLine(false);
		lbl.setMovementMethod(LinkMovementMethod.getInstance());
		lbl.setText(Html.fromHtml(strAbout));
		lbl.setGravity(Gravity.CENTER);
		lbl.setTextColor(getResources().getColor(R.color.blackblue));

		lbl.setTypeface(arabicFont);
		
	}
}
