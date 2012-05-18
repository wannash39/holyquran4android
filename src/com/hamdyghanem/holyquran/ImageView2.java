package com.hamdyghanem.holyquran;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class ImageView2 extends ImageView {

//	private int lineHeight = 30;
//
//	private int fromX = 0;
//	private int fromY = 0;
//	private int toX = 0;
//	private int toY = 0;
	//
//	int width = 0;
//	int height = 0;
	//float iScaleX = 1;
	//float iScaleY = 1;
	//private MainActivity mcontext;

	public ImageView2(Context context) {
		super(context);
		//mcontext = (MainActivity) context;

		// TODO Auto-generated constructor stub
		//Display display = ((WindowManager) context
		//		.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		//width = display.getWidth();
	//	height = display.getHeight();
		//
		// iScaleX = 480 / width;
		// iScaleY = 743 / height;
	}

	// */@Override
	// */ public boolean onTouchEvent(MotionEvent event) {
	// */ super.onTouchEvent(event);
	// */ int x = (int) (event.getX() * iScaleX);
	// */ int y = (int) (event.getY() * iScaleY);
	// */ switch (event.getAction() & MotionEvent.ACTION_MASK) {
	// */ case MotionEvent.ACTION_DOWN:
	// */ }
	// */ boolean bResult = mcontext.AC.getQuranPoint(mcontext, (int) x, y);
	// */ if (bResult) {
	// Manage scale
	// */ this.invalidate();
	// Toast.makeText( mcontext, ">>" + bResult + "<" + width + "<" +
	// height + "-" + iScaleY + ">" + mcontext.AC.iCurrentFromX + " " +
	// mcontext.AC.iCurrentFromY, Toast.LENGTH_SHORT).show();
	// Toast.makeText(mcontext, fromX + " " + fromY,
	// Toast.LENGTH_SHORT).show();
	// check the database
	// */}
	// If it reurn true
	// means cancel the super event // the drag will not work
	// */return false;
	// */} //

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//
		//fromX = (int) (mcontext.AC.iCurrentFromX / iScaleX);
		//fromY = (int) (mcontext.AC.iCurrentFromY / iScaleY);
	//	toX = (int) (mcontext.AC.iCurrentToX / iScaleX);
	//	toY = (int) (mcontext.AC.iCurrentToY / iScaleY);
		//
	//	Paint paint = new Paint();
	//	paint.setStyle(Paint.Style.FILL);
	//	paint.setAntiAlias(false);
	//	paint.setColor(Color.RED);
	//	paint.setAlpha(30);
	//	if (fromX == 0 && toX == 0 && fromY == 0 && toY == 0)
	//		return;
		// (left, top, right, bottom, paint)
	//	if (Math.abs(fromY - toY) > 4) {
			// canvas.drawRect(fromX, fromY, toX, toY, paint);
		//	canvas.drawRect(0, fromY, fromX, fromY + lineHeight, paint);
		//	int numItemsToProcess = fromY;
		//	while (Math.abs(numItemsToProcess - toY) > lineHeight) {
		//		// process an item
		//		Log.d(">>>>",
		//				Integer.toString(numItemsToProcess - toY - lineHeight));
		//		numItemsToProcess += lineHeight;
		//		canvas.drawRect(0, numItemsToProcess, width, numItemsToProcess
		//				+ lineHeight, paint);
		//	}
		//	numItemsToProcess += lineHeight;
		//	canvas.drawRect(0, numItemsToProcess, toX, toY + lineHeight, paint);

		//} else {
		//	canvas.drawRect(fromX, fromY, toX, toY, paint);
		//}

		// create and draw triangles
	}
}