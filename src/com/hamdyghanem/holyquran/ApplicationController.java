package com.hamdyghanem.holyquran;

import java.io.File;

import android.app.Application;
import android.os.Environment;

public class ApplicationController extends Application {
	public BookmarkUtil bookmarkUtitliy;

	@Override
	public void onCreate() {

		super.onCreate();

		// Do Application initialization over here
	}

	public Boolean NeedDownload() {
		String baseDir = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "//hQuran//img//";
		File file = new File(baseDir);
		return file.listFiles().length < 604;
	}

}
