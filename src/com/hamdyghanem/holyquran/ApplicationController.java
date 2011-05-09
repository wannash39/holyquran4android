package com.hamdyghanem.holyquran;

import java.io.File;

import android.app.Application;
import android.os.Environment;

public class ApplicationController extends Application {
	public BookmarkUtil bookmarkUtitliy;
public Integer iCurrentPage =0;
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

	public String GetSora(Integer iPage) {
		String[] soranames = getResources().getStringArray(
				R.array.SoraName_array);
		String[] sorapages = getResources().getStringArray(
				R.array.SoraValue_array);
		if (iPage <= 0)
			iPage = 1;
		if (iPage > 604)
			iPage = 604;
		// if (i == 591) i = 5911;
		// if (i >= 595) i = Integer.parseInt(Integer.toString(i) + "1");
		;
		for (Integer i = 0;i<sorapages.length; i++) { 
			if (iPage < Integer.parseInt(sorapages[i]))
				return soranames[i-1].trim();
		}
		return "";
	}

}
