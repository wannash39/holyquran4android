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
package com.hamdyghanem.holyquran.Download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.hamdyghanem.holyquran.ApplicationController;
import com.hamdyghanem.holyquran.MainActivity;
import com.hamdyghanem.holyquran.R;
import com.hamdyghanem.holyquran.R.string;
import com.hamdyghanem.holyquran.SettingsActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ScrollView;
import android.widget.Toast;

public class CreateTabImages {

	String strFile = "";
	public int iLanguage = 0;
	int iImage = 1;
	int imax = 604;

	String baseImgDir = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/hQuran/img/";

	private Context mContext;
	File path = Environment.getExternalStorageDirectory();

	Bitmap combinedBitmap = null;
	ProgressDialog dialog;
	ApplicationController AC;

	public CreateTabImages(SettingsActivity c, ApplicationController _AC) {
		AC = _AC;
		mContext = c;
		Create();
	}

	public CreateTabImages(SettingsActivity c, ApplicationController _AC,
			int img) {
		AC = _AC;
		mContext = c;
		Create();
		imax = img;
		iImage = img;
	}

	private void Create() {
		String strf = baseImgDir + AC.CurrentImageType + "/100.img";
		new BitmapFactory();
		Bitmap btmp = BitmapFactory.decodeFile(strf);
		File f = new File(strFile);
		if (!f.exists())
			combinedBitmap = Bitmap.createBitmap(btmp.getWidth() * 2,
					btmp.getHeight(), Bitmap.Config.ARGB_8888);
		else
			combinedBitmap = Bitmap.createBitmap(800 * 2, 1115,
					Bitmap.Config.ARGB_8888);
	}

	public void SaveTabImage() {
		dialog = new ProgressDialog(mContext);
		dialog.setCancelable(true);
		dialog.setMessage(AC.getTextbyLanguage(R.string.createtabimagedialog));
		// set the progress to be horizontal
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// reset the bar to the default value of 0
		dialog.setProgress(0);
		dialog.setMax(imax);
		// display the progressbar
		dialog.show();
		// create a thread for updating the progress bar
		Thread background = new Thread(new Runnable() {
			public void run() {
				try {
					while (iImage <= imax) {
						Canvas comboImage = new Canvas(combinedBitmap);
						strFile = baseImgDir + AC.CurrentImageType + "_tab"
								+ "/" + Integer.toString(iImage) + ".img";
						Log.d("hQuran", strFile);

						File f = new File(strFile);
						if (!f.exists()) {
							strFile = baseImgDir + AC.CurrentImageType + "/"
									+ Integer.toString(iImage) + ".img";
							String strFile2 = baseImgDir + AC.CurrentImageType
									+ "/" + Integer.toString(iImage + 1)
									+ ".img";
							f = new File(strFile);
							if (!f.exists())
								dialog.cancel();
							f = new File(strFile2);
							if (!f.exists())
								dialog.cancel();
							// CombiningImages
							Bitmap imgRight = BitmapFactory.decodeFile(strFile);
							Bitmap imgLeft = BitmapFactory.decodeFile(strFile2);
							//
							comboImage.drawBitmap(imgLeft, 0f, 0f, null);
							comboImage.drawBitmap(imgRight,
									imgRight.getWidth(), 0f, null);
							strFile = baseImgDir + AC.CurrentImageType + "_tab"
									+ "/" + Integer.toString(iImage) + ".img";
							FileOutputStream out = new FileOutputStream(strFile);
							combinedBitmap.compress(Bitmap.CompressFormat.PNG,
									100, out);

						}
						// active the update handler
						progressHandler.sendMessage(progressHandler
								.obtainMessage());
						iImage = iImage + 2;

					}
					// Finished
					dialog.cancel();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("hQuran", e.getMessage());
					dialog.cancel();
				}
			}
		});
		// start the background thread
		background.start();

	}

	Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			dialog.setProgress(iImage);
			// dialog.setMessage(AC
			// .getTextbyLanguage(R.string.createtabimagedialog)
			// + Integer.toString(iImage));
		}
	};
}