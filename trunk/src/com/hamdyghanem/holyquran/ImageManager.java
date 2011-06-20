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
	public static void DownloadFromUrl(String strDropbox, String imgName, String fileName) { // this
		// is
		// the
		// downloader
		// method
		try {
			URL url = new URL("http://dl.dropbox.com/u/" + strDropbox + "/img/" + imgName
					+ ".img");
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

	public static void DownloadDBFromUrl(String strDropbox) {
		// is // the // downloader // method
		try {
			// http://dl.dropbox.com/u/" + strDropbox + "/tafseer/1.TXT
			URL url = new URL(
					"http://dl.dropbox.com/u/" + strDropbox + "/database/hquran.dat");
			File file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/hQuran/hquran.dat");
			file.deleteOnExit();
			long startTime = System.currentTimeMillis();
			// Log.d("ImageManager", "download begining");
			// Log.d("ImageManager", "download url:" + url);
			Log.d("ImageManager", "downloaded file name:" + "hquran.dat");
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();
			int lenghtOfFile = ucon.getContentLength();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			FileOutputStream fos = new FileOutputStream(file);
			InputStream is = ucon.getInputStream();
			byte data[] = new byte[1024];

			int count = 0;
			long total = 0;
			int progress = 0;

			while ((count = is.read(data)) != -1) {
				total += count;
				int progress_temp = (int) total * 100 / lenghtOfFile;
				if (progress_temp % 10 == 0 && progress != progress_temp) {
					progress = progress_temp;
					Log.v("Downloading", "total = " + progress);
				}
				fos.write(data, 0, count);
			}

			is.close();
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
				+ "/hQuran/img/" + finalName + ".img";
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

	public static void DownloadTafserFromUrl(String strDropbox,String imgName, String fileName) { // this
		// is // the // downloader // method
		try {
			// http://dl.dropbox.com/u/" + strDropbox + "/tafseer/1.TXT
			URL url = new URL("http://dl.dropbox.com/u/" + strDropbox + "/tafseer_html/"
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

	public static void DownloadTareefFromUrl(String strDropbox,String imgName, String fileName) { // this
		// is
		// the
		// downloader
		// method
		try {
			// http://dl.dropbox.com/u/" + strDropbox + "/tafseer/1.TXT
			URL url = new URL("http://dl.dropbox.com/u/" + strDropbox + "/taareef/"
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

	public static void DownloadDictionaryFromUrl(String strDropbox,String imgName, String fileName) { // this
		// is
		// the
		// downloader
		// method
		try {
			// http://dl.dropbox.com/u/" + strDropbox + "/tafseer/1.TXT
			URL url = new URL("http://dl.dropbox.com/u/" + strDropbox + "/dictionary/"
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