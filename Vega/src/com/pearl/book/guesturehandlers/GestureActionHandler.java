package com.pearl.book.guesturehandlers;

// TODO: Auto-generated Javadoc
/**
 * The Interface GestureActionHandler.
 */
public interface GestureActionHandler {
    
    /**
     * Move to next page.
     */
    void moveToNextPage();

    /**
     * Move to previous page.
     */
    void moveToPreviousPage();

    /**
     * Toggle header footer visibility.
     */
    void toggleHeaderFooterVisibility();

    /**
     * Toggle bookmarks notes list visibility.
     */
    void toggleBookmarksNotesListVisibility();

    /**
     * Scroll.
     */
    void scroll();

    /**
     * Toggle table ofcontents list visibility.
     */
    void toggleTableOfcontentsListVisibility();

    /**
     * On just touch.
     */
    void onJustTouch();
}