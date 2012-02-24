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

import com.hamdyghanem.holyquran.R.color;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class spinneradapter extends ArrayAdapter<CharSequence> {
	private Typeface arabicFont;
	private Context m_cContext;

	public spinneradapter(Context context, int textViewResourceId,
			CharSequence[] strings, Typeface mArabicFont) {
		super(context, textViewResourceId, strings);

		arabicFont = mArabicFont;
		m_cContext = context;
	}

	public static spinneradapter createFromResource(Context context,
			int textArrayResId, int textViewResId, Typeface mArabicFont) {

		Resources resources = context.getResources();
		CharSequence[] strings = resources.getTextArray(textArrayResId);
		return new spinneradapter(context, textViewResId, strings, mArabicFont);
	}

	@Override
	public TextView getView(int position, View convertView, ViewGroup parent) {
		TextView v = (TextView) super.getView(position, convertView, parent);
		v.setTypeface(arabicFont);
		v.setTextSize(20.f);
        v.setTextColor(color.black);//choose your color :)         

		return v;
	}

	@Override
	public TextView getDropDownView(int position, View convertView,
			ViewGroup parent) {
		TextView v = (TextView) super.getView(position, convertView, parent);
		v.setTypeface(arabicFont);
		v.setTextSize(27.f);
		v.setGravity(Gravity.CENTER);
        v.setTextColor(color.black);//choose your color :)         

		return v;
	}

}
