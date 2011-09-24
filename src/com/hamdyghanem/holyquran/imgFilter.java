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
