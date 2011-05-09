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
		setContentView(R.layout.about);
		//
		String strAbout = "<center><H1>";
		strAbout += "<font color='black'>";
		strAbout += "������ ������ <br />";
		strAbout += "���� ������� <br />";
		strAbout += "</H1>";
		strAbout += "������ ���� ������� ��������� �� ��� ��������� <br />";
		strAbout += "�������� ����� ������ � ����� ������ ��� ���� ������ ��� ������ ����� �� ����� ��  <br />";
		strAbout += "<a href=\'http://code.google.com/p/holyquran4android/'>���</a> <br />";

		strAbout += " �������� � ����� ����� ������ ��  ";
		strAbout += "<a href=\'http://knol.google.com/k/hamdy-ghanem/holyquran4android/3n0yrj5rx5x7a/29?hd=ns# '>��� </a> <br />";
		strAbout += "<br />";
		strAbout += "�������";
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

		Typeface arabicFont = Typeface.createFromAsset(getAssets(),
				"fonts/DroidSansArabic.ttf");
		lbl.setTypeface(arabicFont);
		
	}
}