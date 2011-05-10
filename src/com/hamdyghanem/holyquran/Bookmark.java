package com.hamdyghanem.holyquran;

public class Bookmark {
	private String bookmarkName = "";
	private Integer page = 0;
	private int sttc = 0;
	private int def = 0;

	

	public Bookmark(String strbookmarkName, Integer iPage,int isttc, int idef) {
		setBookmarkName(strbookmarkName);
		setPage(iPage);
		setStatic(isttc);
		setDefault(idef);
		
	}

	public String getBookmarkName() {
		return bookmarkName;
	}

	public void setBookmarkName(String strBookmarkName) {
		bookmarkName = strBookmarkName;

	}

	public Integer getPage() {
		if (page == null)
			page = 0;
		return page;
	}

	public void setPage(Integer ipage) {
		page = ipage;

	}

	public int getDefault() {
		return def;
	}

	public void setDefault(int beDfault) {
		def = beDfault;

	}
	public int getStatic() {
		return sttc;
	}

	public void setStatic(int bStatic) {
		sttc= bStatic;

	}

}
