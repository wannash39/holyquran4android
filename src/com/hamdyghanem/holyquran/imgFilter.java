package com.hamdyghanem.holyquran;

import java.io.File;
import java.io.FileFilter;

class imgFilter implements FileFilter {

	private String[] extension = { "img", "img" };

	@Override
	public boolean accept(File pathname) {

		if (pathname.isDirectory()) {

			return true;

		}

		String name = pathname.getName().toLowerCase();

		for (String anExt : extension) {

			if (name.endsWith(anExt)) {

				return true;

			}

		}

		return false;

	}

}
