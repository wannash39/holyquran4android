package com.hamdyghanem.holyquran;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import android.widget.AdapterView.OnItemClickListener;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class SelectImageType extends Activity {
	private Gallery gallery;
	private ImageView imgView;
	private Integer[] Imgid = { R.drawable.img_0_50, R.drawable.img_1_50 };
	ApplicationController AC;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectimagetype);
		AC = (ApplicationController) getApplicationContext();

		imgView = (ImageView) findViewById(R.id.ImageView01);
		// imgView.setImageResource(Imgid[0]);
		Integer i = Integer.parseInt(AC.CurrentImageType);
		imgView.setImageResource(Imgid[i]);

		gallery = (Gallery) findViewById(R.id.examplegallery);

		gallery.setAdapter(new AddImgAdp(this));

		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {

				imgView.setImageResource(Imgid[position]);
				AC.CurrentImageType = Integer.toString(position);

			}

		});
	}
	@Override
	public void onStop() {
		//AC.CurrentImageType = Integer.toString(gallery.getSelectedItemPosition());
		super.onStop();
	}

	public class AddImgAdp extends BaseAdapter {

		int GalItemBg;

		private Context cont;

		public AddImgAdp(Context c) {

			cont = c;

			TypedArray typArray = obtainStyledAttributes(R.styleable.GalleryTheme);

			GalItemBg = typArray.getResourceId(
					R.styleable.GalleryTheme_android_galleryItemBackground, 0);

			typArray.recycle();

		}

		public int getCount() {

			return Imgid.length;

		}

		public Object getItem(int position) {

			return position;

		}

		public long getItemId(int position) {

			return position;

		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imgView = new ImageView(cont);

			imgView.setImageResource(Imgid[position]);

			imgView.setLayoutParams(new Gallery.LayoutParams(180, 170));

			imgView.setScaleType(ImageView.ScaleType.FIT_XY);

			imgView.setBackgroundResource(GalItemBg);

			return imgView;

		}

	}
}