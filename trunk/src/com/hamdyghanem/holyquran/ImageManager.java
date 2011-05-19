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

import org.apache.http.util.ByteArrayBuffer;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class ImageManager {
	public static void DownloadFromUrl(String imgName, String fileName) { // this
		// is
		// the
		// downloader
		// method
		try {
			// http://dl.dropbox.com/u/27675084/img240/0.gif
			URL url = new URL("http://dl.dropbox.com/u/27675084/img/" + imgName
					+ ".gif");
			File file = new File(fileName);

			long startTime = System.currentTimeMillis();
			// Log.d("ImageManager", "download begining");
			// Log.d("ImageManager", "download url:" + url);
			Log.d("ImageManager", "downloaded file name:" + fileName);
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, 8192);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
			Log.d("ImageManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

		} catch (IOException e) {
			Log.d("ImageManager", "Error: " + e);
		}

	}

	public static void DownloadDBFromUrl() {
		// is // the // downloader // method
		try {
			// http://dl.dropbox.com/u/27675084/tafseer/1.TXT
			URL url = new URL(
					"http://dl.dropbox.com/u/27675084/database/hquran.dat");
			File file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/hQuran/hquran.dat");

			long startTime = System.currentTimeMillis();
			// Log.d("ImageManager", "download begining");
			// Log.d("ImageManager", "download url:" + url);
			Log.d("ImageManager", "downloaded file name:" + "hquran.dat");
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, 1118192);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
			Log.d("ImageManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

		} catch (IOException e) {
			Log.d("ImageManager", "Error: " + e);
		}

	}

	public static String saveToSDCard(Bitmap resourceImage, String finalName) {
		StringBuffer createdFile = new StringBuffer();

		finalName = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/hQuran/img/" + finalName + ".gif";
		File externalStorageFile = new File(finalName);

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		resourceImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		byte b[] = bytes.toByteArray();

		try {
			externalStorageFile.createNewFile();
			OutputStream filoutputStream = new FileOutputStream(
					externalStorageFile);
			filoutputStream.write(b);
			filoutputStream.flush();
			filoutputStream.close();
			createdFile.append(externalStorageFile.getAbsolutePath());
		} catch (IOException e) {
		}

		return createdFile.toString();
	}

	public static void DownloadTafserFromUrl(String imgName, String fileName) { // this
		// is // the // downloader // method
		try {
			// http://dl.dropbox.com/u/27675084/tafseer/1.TXT
			URL url = new URL("http://dl.dropbox.com/u/27675084/tafseer_html/"
					+ imgName + ".html");
			File file = new File(fileName);

			long startTime = System.currentTimeMillis();
			// Log.d("ImageManager", "download begining");
			// Log.d("ImageManager", "download url:" + url);
			Log.d("ImageManager", "downloaded file name:" + fileName);
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, 8192);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
			Log.d("ImageManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

		} catch (IOException e) {
			Log.d("ImageManager", "Error: " + e);
		}

	}

	public static void DownloadTareefFromUrl(String imgName, String fileName) { // this
		// is
		// the
		// downloader
		// method
		try {
			// http://dl.dropbox.com/u/27675084/tafseer/1.TXT
			URL url = new URL("http://dl.dropbox.com/u/27675084/taareef/"
					+ imgName + ".html");
			File file = new File(fileName);

			long startTime = System.currentTimeMillis();
			// Log.d("ImageManager", "download begining");
			// Log.d("ImageManager", "download url:" + url);
			Log.d("ImageManager", "downloaded file name:" + fileName);
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, 8192);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
			Log.d("ImageManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

		} catch (IOException e) {
			Log.d("ImageManager", "Error: " + e);
		}

	}

	public static void DownloadDictionaryFromUrl(String imgName, String fileName) { // this
		// is
		// the
		// downloader
		// method
		try {
			// http://dl.dropbox.com/u/27675084/tafseer/1.TXT
			URL url = new URL("http://dl.dropbox.com/u/27675084/dictionary/"
					+ imgName + ".html");
			File file = new File(fileName);

			long startTime = System.currentTimeMillis();
			// Log.d("ImageManager", "download begining");
			// Log.d("ImageManager", "download url:" + url);
			Log.d("ImageManager", "downloaded file name:" + fileName);
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, 8192);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
			Log.d("ImageManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

		} catch (IOException e) {
			Log.d("ImageManager", "Error: " + e);
		}

	}
}