package com.pearl.epub;

import java.io.Serializable;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * Table of Content.
 */
public class TableOfContent implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8757154805331298337L;

    /** The uid. */
    private String uid;
    
    /** The depth. */
    private String depth;
    
    /** The chapter list. */
    private final ArrayList<Chapter> chapterList = new ArrayList<Chapter>();

    /**
     * Gets the uid.
     *
     * @return the uid
     */
    public String getUid() {
	return uid;
    }

    /**
     * Sets the uid.
     *
     * @param uid the new uid
     */
    public void setUid(String uid) {
	this.uid = uid;
    }

    /**
     * Gets the depth.
     *
     * @return the depth
     */
    public String getDepth() {
	return depth;
    }

    /**
     * Sets the depth.
     *
     * @param depth the new depth
     */
    public void setDepth(String depth) {
	this.depth = depth;
    }

    /**
     * Adds the chapter.
     *
     * @param chapter the chapter
     */
    public void addChapter(final Chapter chapter) {
	chapterList.add(chapter);
    }

    /**
     * Gets the chapter.
     *
     * @param num the num
     * @return the chapter
     */
    public Chapter getChapter(final int num) {
	int chapNum = num;
	if (chapNum >= chapterList.size()) {
	    chapNum = chapterList.size() - 1;
	}
	return chapterList.get(chapNum);
    }

    /**
     * Gets the chapter list.
     *
     * @return the chapter list
     */
    public ArrayList<Chapter> getChapterList() {
	return chapterList;
    }

    /**
     * Gets the title array.
     *
     * @return the title array
     */
    public String[] getTitleArray() {
	final int totalSize = getTotalSize();
	final String[] titles = new String[totalSize];
	for (int i = 0; i < totalSize; i++) {
	    final Chapter chap = chapterList.get(i);
	    titles[i] = chap.getTitle();
	}
	return titles;
    }

    /**
     * Gets the url array.
     *
     * @return the url array
     */
    public String[] getUrlArray() {
	final int totalSize = getTotalSize();
	final String[] urls = new String[totalSize];
	for (int i = 0; i < totalSize; i++) {
	    final Chapter chap = chapterList.get(i);
	    urls[i] = chap.getUrl();
	}
	return urls;
    }

    /**
     * Gets the total size.
     *
     * @return the total size
     */
    public int getTotalSize() {
	return chapterList.size();
    }

    /**
     * Chapter.
     */
    public static class Chapter {

	/** The seq. */
	String seq;
	
	/** The title. */
	String title;
	
	/** The url. */
	String url;
	
	/** The anchor. */
	String anchor;

	/**
	 * Gets the seq.
	 *
	 * @return the seq
	 */
	public String getSeq() {
	    return seq;
	}

	/**
	 * Sets the seq.
	 *
	 * @param seq the new seq
	 */
	public void setSeq(String seq) {
	    this.seq = seq;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
	    this.title = title;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
	    this.url = url;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
	    return title;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
	    return url;
	}

	/**
	 * Gets the anchor.
	 *
	 * @return the anchor
	 */
	public String getAnchor() {
	    return anchor;
	}

	/**
	 * Sets the anchor.
	 *
	 * @param anchor the new anchor
	 */
	public void setAnchor(String anchor) {
	    this.anchor = anchor;
	}
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "TableOfContent [uid=" + uid + ", depth=" + depth
		+ ", chapterList=" + chapterList + "]";
    }

}
