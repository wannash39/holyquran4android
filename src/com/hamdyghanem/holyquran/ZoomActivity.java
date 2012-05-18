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

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class ZoomActivity extends Activity {
	/** Called when the activity is first created. */
	public int iLanguage = 0;
	private static final String TAG = "Touch";
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	ApplicationController AC;
	int statusBarHeight;

	private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;

	private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;

	private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;
	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	String baseImgDir = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/hQuran/img/";
	String strFile = "";
	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	// Limit zoomable/pannable image
	private ImageView img;
	private float[] matrixValues = new float[9];
	private float maxZoom = 2f;
	private float minZoom = 0.25f;
	private float displayheight;
	private float displaywidth;
	private RectF displayRect = new RectF();
	Bitmap bitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.zoom);

		getStatusbarHeight();
		img = (ImageView) findViewById(R.id.img);
		AC = (ApplicationController) getApplicationContext(); // RadioGroup.VERTICAL
		this.setTitle(AC.getTextbyLanguage(R.string.zoomdefault));

		strFile = baseImgDir + AC.CurrentImageType + "/"
				+ Integer.toString(AC.iCurrentPage) + ".img";

		Display display = getWindowManager().getDefaultDisplay();
		displaywidth = display.getWidth();
		displayheight = display.getHeight();
		displayheight = displayheight - statusBarHeight;

		displayRect.set(0, 0, displaywidth, displayheight);
		img.setScaleType(ScaleType.MATRIX);
		// img.setAdjustViewBounds(true);
		img.setImageMatrix(matrix);
		bitmap = BitmapFactory.decodeFile(strFile);
		img.setImageBitmap(bitmap);

		Toast.makeText(this, AC.getTextbyLanguage(R.string.zoommode),
				Toast.LENGTH_LONG).show();

		float scale = displaywidth / bitmap.getWidth() * AC.imageScale;
		matrix.postScale(scale, scale);
		adjustPan();

		img.setImageMatrix(matrix);
		//
		img.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// ImageView img = (ImageView) v;
				img = (ImageView) v;
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				// MotionEvent class constant signifying a finger-down event
				case MotionEvent.ACTION_DOWN: {
					savedMatrix.set(matrix);
					start.set(event.getX(), event.getY());
					Log.d(TAG, "mode=DRAG");
					mode = DRAG;
					break;
				}
				case MotionEvent.ACTION_POINTER_DOWN:
					oldDist = spacing(event);
					Log.d(TAG, "oldDist=" + oldDist);
					if (oldDist > 10f) {
						savedMatrix.set(matrix);
						midPoint(mid, event);
						mode = ZOOM;
						Log.d(TAG, "mode=ZOOM");
					}
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					Log.d(TAG, "mode=NONE");
					break;
				case MotionEvent.ACTION_MOVE:
					if (mode == DRAG) {
						// Log.d(TAG, "DRAGging");
						// mLastTouchX = x;
						// mLastTouchY = y;
						//
						matrix.set(savedMatrix);
						matrix.postTranslate(event.getX() - start.x,
								event.getY() - start.y);
						//
						adjustPan();
						img.setImageMatrix(matrix);
					} else if (mode == ZOOM) {
						float newDist = spacing(event);
						if (newDist > 10f) {
							matrix.set(savedMatrix);
							float scale = newDist / oldDist;
							Log.d(TAG, "scale =" + scale);
							// Log.d(TAG, "savedMatrix =" + savedMatrix);

							matrix.postScale(scale, scale, mid.x, mid.y);
							// the left page
							//
							// The range
							float rad = matrix.mapRadius(1);
							if (rad > minZoom & rad < maxZoom) {
								AC.imageScale = scale;
								// AC.imageScale = scale*
								// bitmap.getWidth()/displaywidth ;
								Log.d(TAG, "scale =" + AC.imageScale);

								img.setImageMatrix(matrix);
							} else {
								matrix.set(savedMatrix);
							}
						}
					}
					if (mode == DRAG | mode == ZOOM) {

					}
					break;
				}
				return true;
			}
		});
	}

	private void adjustPan() {
		matrix.getValues(matrixValues);
		float currentY = matrixValues[Matrix.MTRANS_Y];
		float currentX = matrixValues[Matrix.MTRANS_X];
		float currentScale = matrixValues[Matrix.MSCALE_X];
		float currentHeight = bitmap.getHeight() * currentScale;
		float currentWidth = bitmap.getWidth() * currentScale;

		float newX = currentX;
		float newY = currentY;

		RectF drawingRect = new RectF(newX, newY, newX + currentWidth, newY
				+ currentHeight);
		float diffUp = Math.min(displayRect.bottom - drawingRect.bottom,
				displayRect.top - drawingRect.top);
		float diffDown = Math.max(displayRect.bottom - drawingRect.bottom,
				displayRect.top - drawingRect.top);
		float diffLeft = Math.min(displayRect.left - drawingRect.left,
				displayRect.right - drawingRect.right);
		float diffRight = Math.max(displayRect.left - drawingRect.left,
				displayRect.right - drawingRect.right);

		float x = 0, y = 0;

		if (diffUp > 0)
			y += diffUp;
		if (diffDown < 0)
			y += diffDown;
		if (diffLeft > 0)
			x += diffLeft;
		if (diffRight < 0)
			x += diffRight;
		if (currentWidth < displayRect.width())
			x = -currentX + (displayRect.width() - currentWidth) / 2;
		if (currentHeight < displayRect.height())
			y = -currentY + (displayRect.height() - currentHeight) / 2;

		matrix.postTranslate(x, y);
	}

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

	private void getStatusbarHeight() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		// ((WindowManager)
		// getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
		this.getWindowManager().getDefaultDisplay();
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
}