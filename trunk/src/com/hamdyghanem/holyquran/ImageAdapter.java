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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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

public class ImageAdapter extends BaseAdapter {
	float X = 0f;
	float Y = 0f;
	int width = 0;
	int height = 0;
	int statusBarHeight;
	Matrix matrix = new Matrix();
	float oldimageScale = 1;
	String strFile = "";
	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	public int iLanguage = 0;
	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	int increment = 0;

	private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;

	private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;

	private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;

	String baseImgDir = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/hQuran/img/";

	private MainActivity mContext;
	File path = Environment.getExternalStorageDirectory();
	Integer iImage = 0;
	public boolean bDoubleOrintation = false;
	Integer iAya = 0;
	Integer iPage = 0;
	Integer imax = 604;
	Bitmap combinedBitmap = null;
	ProgressDialog dialog;

	public ImageAdapter(Context c) {
		mContext = (MainActivity) c;
		getStatusbarHeight();
		// See res/values/attrs.xml for the <declare-styleable> that defines
		// Gallery1.
		// TypedArray a = obtainStyledAttributes(R.styleable.Gallery01);
		// mGalleryItemBackground = a.getResourceId(
		// R.styleable.Gallery01_android_galleryItemBackground, 0);
		// a.recycle();
		iAya = mContext.AC.iCurrentAya;
		if (mContext.isTabletDevice) {
			if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				bDoubleOrintation = true;
				imax = 302;
				// ///////////////////////////////////////////////////////////
				String strf = baseImgDir + mContext.AC.CurrentImageType
						+ "/100.img";
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
		}

	}

	public int getCount() {
		// return mImageIds.length;
		if (!bDoubleOrintation)
			return 604;
		else
			return 302;

	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	private void getStatusbarHeight() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		// ((WindowManager)
		// getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
		mContext.getWindowManager().getDefaultDisplay();
		switch (displayMetrics.densityDpi) {
		case DisplayMetrics.DENSITY_HIGH:
			statusBarHeight = HIGH_DPI_STATUS_BAR_HEIGHT;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
			break;
		case DisplayMetrics.DENSITY_LOW:
			statusBarHeight = LOW_DPI_STATUS_BAR_HEIGHT;
			break;
		default:
			statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (!bDoubleOrintation) {
			mContext.scroller1.fullScroll(ScrollView.FOCUS_UP);
		} // final ImageView imgView = new ImageView(mContext);
			// final ImageView2 imgView = new ImageView2(mContext);
		ImageView imgView = new ImageView(mContext);
		iPage = imax - (mContext.g.getSelectedItemPosition() + 1);
		if (bDoubleOrintation) {
			iPage = imax - (mContext.g.getSelectedItemPosition() / 2);
			iPage = iPage * 2;
			iPage = iPage - 2;
		}
		iAya = mContext.AC.iCurrentAya;
		if (iAya == -1)
			iAya = 0;
		mContext.PrintAya(iPage, iAya);
		setImage(imgView, position);
		//
		if (mContext.AC.ManualNavigation) {
			imgView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mContext.ShowHeader();
					mContext.DoubleClick();
				}
			});
		}
		return imgView;
	}

	private void setImage(ImageView imgView, int position) {
		iImage = imax - position;
		strFile = baseImgDir + mContext.AC.CurrentImageType + "/"
				+ Integer.toString(iImage) + ".img";
		File f = new File(strFile);
		if (!f.exists()) {
			// ImageManager.DownloadFromUrl(Integer.toString(position),strFile);
			// callOptionsItemSelected(null, R.id.mnu_settings);
			strFile = baseImgDir + "no.img";
		}
		Drawable d = Drawable.createFromPath(strFile);
		imgView.setImageDrawable(d);
		//
		if (bDoubleOrintation) {
			iPage = imax - (mContext.g.getSelectedItemPosition() / 2);
			iPage = iPage * 2;
			iImage = imax - (position / 2);
			iImage = iImage * 2;
			//
			//
			if (iImage == 0)
				iImage = 1; //
			if (!mContext.AC.isOdd(iImage))
				iImage = iImage - 1;
			// Check if the file not exist
			strFile = baseImgDir + mContext.AC.CurrentImageType + "_tab" + "/"
					+ Integer.toString(iImage) + ".img";
			f = new File(strFile);
			if (!f.exists()) {
				// SaveTabImage();
			}
			//
			if (position > mContext.g.getSelectedItemPosition()) {
				iImage = iImage - 2;
			} else if (position < mContext.g.getSelectedItemPosition()) {
				iImage = iImage + 2;

			}

			// if (position < mContext.g.getSelectedItemPosition()) {
			// if (mContext.AC.isOdd(position))
			// iImage = iImage + 1;
			// }

			d = Drawable.createFromPath(strFile);
			imgView.setImageDrawable(d);
			//
		}
		//
		Display display = mContext.getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		// matrix.setScale(scale, scale);
		imgView.setPadding(1, 1, 1, 1);
		if (mContext.AC.AudioOn) {
			// iMinus = iMinus + buttonPlayPause.getHeight();
			// iMinus =120;
		}
		if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (!mContext.isTabletDevice) {
				//float myFlo = (float) (width * 1.5);
				float myFlo = (float) (width * 1);
				height = (int) myFlo;
				imgView.setLayoutParams(new Gallery.LayoutParams(width, height
						- statusBarHeight));
			} else {
				 //imgView.setScaleType(ScaleType.FIT_CENTER);
				 imgView.setScaleType(ScaleType.CENTER_INSIDE);
				//imgView.setScaleType(ScaleType.MATRIX);

				width = width - 5;
				height = height - statusBarHeight;
				mContext.scroller1.fullScroll(ScrollView.FOCUS_UP);
				mContext.scroller1.fullScroll(ScrollView.FOCUS_RIGHT);
				//
				width = (int) ((int) width * mContext.AC.imageScale);
				height = (int) ((int) height * mContext.AC.imageScale);

				imgView.setLayoutParams(new Gallery.LayoutParams(width, height));
			}
		} else { //

			imgView.setScaleType(ScaleType.MATRIX);
			// width = width-5 ;
			height = height - statusBarHeight;
			mContext.scroller1.fullScroll(ScrollView.FOCUS_UP);
			mContext.scroller1.fullScroll(ScrollView.FOCUS_RIGHT);
			//

			width = (int) ((int) width * mContext.AC.imageScale);
			height = (int) ((int) height * mContext.AC.imageScale);

			imgView.setLayoutParams(new Gallery.LayoutParams(width, height));
			imgView.setAdjustViewBounds(true);
			imgView.setBackgroundColor(mContext.getTitleColor());
		}

	}

	// private ImageView CombiningPages( ImageView imgView, int position) {
	// try {

	// return imgView;

	// } catch (Exception e) {
	// e.printStackTrace();
	// Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
	// Log.e("hQuran", e.getMessage());
	// return null;

	// }
	// }

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

}