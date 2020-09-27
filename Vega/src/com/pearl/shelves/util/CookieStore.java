/*
 * Copyright (C) 2008 Romain Guy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pearl.shelves.util;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpHead;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

// TODO: Auto-generated Javadoc
/**
 * The Class CookieStore.
 */
public class CookieStore {
    
    /** The Constant LOG_TAG. */
    private static final String LOG_TAG = "Shelves";

    /** The Constant sCookieStore. */
    private static final CookieStore sCookieStore;
    static {
	sCookieStore = new CookieStore();
    }

    /**
     * Instantiates a new cookie store.
     */
    private CookieStore() {
    }

    /**
     * Initialize.
     *
     * @param context the context
     */
    public static void initialize(Context context) {
	CookieSyncManager.createInstance(context);
	CookieManager.getInstance().removeExpiredCookie();
    }

    /**
     * Gets the.
     *
     * @return the cookie store
     */
    public static CookieStore get() {
	return sCookieStore;
    }

    /**
     * Gets the cookie.
     *
     * @param url the url
     * @return the cookie
     */
    public String getCookie(String url) {
	final CookieManager cookieManager = CookieManager.getInstance();
	String cookie = cookieManager.getCookie(url);

	if (cookie == null || cookie.length() == 0) {
	    final HttpHead head = new HttpHead(url);
	    HttpEntity entity = null;
	    try {
		final HttpResponse response = HttpManager.execute(head);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		    entity = response.getEntity();

		    final Header[] cookies = response.getHeaders("set-cookie");
		    for (final Header cooky : cookies) {
			cookieManager.setCookie(url, cooky.getValue());
		    }

		    CookieSyncManager.getInstance().sync();
		    cookie = cookieManager.getCookie(url);
		}
	    } catch (final IOException e) {
		Log.e(LOG_TAG, "Could not retrieve cookie", e);
	    } finally {
		if (entity != null) {
		    try {
			entity.consumeContent();
		    } catch (final IOException e) {
			Log.e(LOG_TAG, "Could not retrieve cookie", e);
		    }
		}
	    }
	}

	return cookie;
    }
}
