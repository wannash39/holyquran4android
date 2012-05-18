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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.hamdyghanem.holyquran.R;
import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EnglishActivity extends Activity {
	/** Called when the activity is first created. */
	ApplicationController AC;
	String strFile = "";
	public SQLiteDatabase db;
	public ExternalStorageReadOnlyOpenHelper objdb;
	WebView myWebView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 		setContentView(R.layout.english);
		AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL
		this.setTitle(AC.getTextbyLanguage(R.string.English));

		
		// //////////////////////
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		// TextView lbl = (TextView) findViewById(R.id.TextView01);
		objdb = new ExternalStorageReadOnlyOpenHelper(this);

		try {
			myWebView = (WebView) findViewById(R.id.webviewenglish);
			strFile = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/hQuran/English/"
					+ Integer.toString(AC.iCurrentPage) + ".html";
			File f = new File(strFile);
			if (f.length() < 500)
				f.delete();
			if (!f.exists()) {
				// WriteEnglishFile(strFile);
				SelectDataTask task = new SelectDataTask(this);
				// tell how much u found it
				task.execute(Integer.toString(AC.iCurrentPage));
			}
			myWebView.loadUrl("file:///sdcard/hQuran/English/"
					+ Integer.toString(AC.iCurrentPage) + ".html");
			myWebView.getSettings().setBuiltInZoomControls(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// lbl.setText(R.string.notexisttafser);
			Toast.makeText(this, getString(R.string.notexisttafser),
					Toast.LENGTH_LONG).show();
			finish();

		}
	}

	private class SelectDataTask extends AsyncTask<String, Void, String> {

		private Context context;
		ProgressDialog dialog;

		public SelectDataTask(Context cxt) {
			context = cxt;
			dialog = new ProgressDialog(context);
		}

		// can use UI thread here
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage(AC.getTextbyLanguage(R.string.Searching));
			this.dialog.show();
		}

		// automatically done on worker thread (separate from UI thread)
		@Override
		protected String doInBackground(String... whereClause) {
			try {
				// AC.iCurrentPage
				String data = "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><html><body style='tab-interval: .5in'><div class='Section1'><div>";
				data += "<p class='MsoNormal'  dir='LTR' style='text-align: left; unicode-bidi: embed'><span lang='AR-EG'>";
				// FileWriter writer;
				// writer = new FileWriter(file);
				FileOutputStream fos = new FileOutputStream(strFile);

				Writer out = new OutputStreamWriter(fos, "UTF-8");
				objdb = new ExternalStorageReadOnlyOpenHelper(
						EnglishActivity.this);
				db = objdb.getReadableDatabase();

				String strwhereClause = whereClause[0];
				Cursor mcursor = objdb.getQuerybyPageID(strwhereClause, db);
				if (mcursor != null) {
					mcursor.moveToFirst();
					// Log.d("tetst>>>>>>>>>>>>>>>>>>>>>>>>>", tl.toString());
					while (mcursor.isAfterLast() == false) {
						// quran.verse, quran.page , quran.surah , quran.content
						// , quranen1.content as contenten
						data += mcursor.getString(0) + "<br />";
						data += mcursor.getString(3) + "<br />";
						data += mcursor.getString(4) + "<br />";
						mcursor.moveToNext();
					}
					mcursor.close();
				}
				data += "</span></p></div></body></html>";
				out.write(data);
				out.flush();
				out.close();

				objdb.close();
				return "";

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			//
			// String[] rows = result.split("\n");
			// if (rows.length > 0)

			// for (int i = 0; i < rows.length ; i++) {
			// String[] cols = rows[i].split(";");
			// addRow(soranames[Integer.parseInt(cols[1])-1], cols[0], cols[2],
			// cols[3] + "\r\n");
			// }
			myWebView.loadUrl("file:///sdcard/hQuran/English/"
					+ Integer.toString(AC.iCurrentPage) + ".html");
			dialog.dismiss();
		}

	}
}