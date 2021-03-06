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

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

// TODO: Auto-generated Javadoc
/**
 * The Class HttpManager.
 */
public class HttpManager {
    
    /** The Constant sClient. */
    private static final DefaultHttpClient sClient;
    static {
	final HttpParams params = new BasicHttpParams();
	HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	HttpProtocolParams.setContentCharset(params, "UTF-8");

	HttpConnectionParams.setStaleCheckingEnabled(params, false);
	HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
	HttpConnectionParams.setSoTimeout(params, 20 * 1000);
	HttpConnectionParams.setSocketBufferSize(params, 8192);

	HttpClientParams.setRedirecting(params, false);

	HttpProtocolParams.setUserAgent(params, "Shelves/1.1");

	final SchemeRegistry schemeRegistry = new SchemeRegistry();
	schemeRegistry.register(new Scheme("http", PlainSocketFactory
		.getSocketFactory(), 80));
	schemeRegistry.register(new Scheme("https", SSLSocketFactory
		.getSocketFactory(), 443));

	final ClientConnectionManager manager = new ThreadSafeClientConnManager(
		params, schemeRegistry);
	sClient = new DefaultHttpClient(manager, params);
    }

    /**
     * Instantiates a new http manager.
     */
    private HttpManager() {
    }

    /**
     * Execute.
     *
     * @param head the head
     * @return the http response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static HttpResponse execute(HttpHead head) throws IOException {
	return sClient.execute(head);
    }

    /**
     * Execute.
     *
     * @param host the host
     * @param get the get
     * @return the http response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static HttpResponse execute(HttpHost host, HttpGet get)
	    throws IOException {
	return sClient.execute(host, get);
    }

    /**
     * Execute.
     *
     * @param get the get
     * @return the http response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static HttpResponse execute(HttpGet get) throws IOException {
	return sClient.execute(get);
    }
}
