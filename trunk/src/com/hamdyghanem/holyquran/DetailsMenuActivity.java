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

import com.hamdyghanem.holyquran.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;

import android.os.Bundle;
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

public class DetailsMenuActivity extends ListActivity {
	/** Called when the activity is first created. */
	ApplicationController AC;
	Typeface arabicFont = null;
	RadioGroup radioGroup = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		// setContentView(R.layout.detailsmenu);
		// /////////CHANGE THE TITLE BAR///////////////
		arabicFont = Typeface.createFromAsset(getAssets(),
				"fonts/DroidSansArabic.ttf");
 
		// //////////////////////
		AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL
		this.setTitle(AC.getTextbyLanguage(R.string.detailsmenu));

		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		String[] names = new String[5];
		names[0] = (String) AC.getTextbyLanguage(R.string.mnuTafseer);
		names[1] = (String) AC.getTextbyLanguage(R.string.English);
		names[2] = (String) AC.getTextbyLanguage(R.string.mnuTaareef);
		names[3] = (String) AC.getTextbyLanguage(R.string.mnuDictionary);
		names[4] = (String) AC.getTextbyLanguage(R.string.mnuZoom);

		//
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, names));
		//
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		// Object o = this.getListAdapter().getItem(position);
		// String keyword = o.toString();
		// Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG)
		// .show();
		switch (position) {
		case 0:
			startActivity(new Intent(this, TafseerActivity.class));
			return;
		case 1:
			startActivity(new Intent(this, EnglishActivity.class));
			return;
		case 2:
			startActivity(new Intent(this, TaareefActivity.class));
			return;
		case 3:
			startActivity(new Intent(this, DictionaryActivity.class));
			return;
		case 4:
			startActivity(new Intent(this, ZoomActivity.class));
			return;
		}
	}

}