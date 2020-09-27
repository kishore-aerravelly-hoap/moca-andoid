package com.pearl.network.request;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.exception.ImproperResponseHandlerException;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestHandlerInterface.
 */
public abstract class RequestHandlerInterface extends
AsyncTask<String, Integer, String> {
    
    /** The request url. */
    protected String requestURL;
    
    /** The response. */
    protected String response;
    
    /** The response status. */
    protected ResponseStatus responseStatus;
    
    /** The callback. */
    protected DownloadCallback callback = null;
    
    /** The app data. */
    protected ApplicationData appData = new ApplicationData();

    /**
     * Instantiates a new request handler interface.
     *
     * @param requestUrl the request url
     */
    public RequestHandlerInterface(String requestUrl) {
	requestURL = requestUrl;

	response = "";
	responseStatus = new ResponseStatus();
	updateStatus(ResponseStatus.IN_PROGRESS);
	// this.responseStatus.setStaus(ResponseStatus.IN_PROGRESS);
    }

    /**
     * Request.
     *
     * @param dc the dc
     */
    public final void request(DownloadCallback dc) {
	callback = dc;

	execute("");// no use of parameters in our case
    }

    /**
     * Request.
     */
    public final void request() {
	execute("");// no use of parameters in our case
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
     */
    @Override
    protected final String doInBackground(String... args) {
	doRequest();

	return getResponseStatus();
    }

    /**
     * Do request.
     */
    protected final void doRequest() {
	Logger.info("Request Handler Interface", "processing url:" + requestURL);

	try {
	    Logger.error("REquest handler", "need to send request");
	    response = processRequest(requestURL);
	    responseStatus.setStaus(ResponseStatus.SUCCESS);
	    updateStatus(ResponseStatus.SUCCESS);
	    try {
		onResponse();
	    } catch (final ImproperResponseHandlerException e) {
		Logger.error("Request Handler Interface", e);
	    }
	} catch (final Exception e) {
	    // this.responseStatus.setStaus(ResponseStatus.FAIL);
	    new Thread(new Runnable() {
	        
	        @Override
	        public void run() {
	            Toast.makeText(appData.getApplicationContext(),"Unable to establish connection", Toast.LENGTH_LONG).show();
	    	
	        }
	    });
	    
	    updateStatus(ResponseStatus.FAIL);
	    Logger.error("Request Handler Interface", "Request Failed due to "
		    + ApplicationData.getExceptionStackTrace(e));
	    Logger.error("REquest handler", "Errorrrrrrrrrrrrrrrr");
	
	
	} finally {
	    // Do any final operations here
	}

	// calls onResponse as soon as we got the response
	/*
	 * try { onResponse(); } catch (ImproperResponseHandlerException e) {
	 * Logger.error("Request Handler Interface", e); }
	 */
    }

    /**
     * Process request.
     *
     * @param requrl the requrl
     * @return the string
     * @throws Exception the exception
     */
    protected String processRequest(String requrl) throws Exception {
	Logger.error("RequestaHandler", "Processing post request");
	final URL url = new URL(requrl);
	final HttpURLConnection huc = (HttpURLConnection) url.openConnection();
	// huc.connect();
	huc.setConnectTimeout(1500);

	Log.w("tag", "DOWNLOAD: HTTP Status = " + huc.getResponseCode());

	/*
	 * if (huc.getResponseCode() != HttpURLConnection.HTTP_OK) {
	 * updateStatus(ResponseStatus.FAIL); return false; }
	 */

	// download the file
	final InputStream input = new BufferedInputStream(huc.getInputStream());

	final ByteArrayBuffer baf = new ByteArrayBuffer(150);
	final byte data[] = new byte[1024];
	int count = 0;

	while ((count = input.read(data)) != -1) {
	    baf.append(data, 0, count);
	}

	input.close();

	// TODO validate response here
	Logger.warn("RequestHandler", "POST - json downloaded from server is:"
		+ baf.toString());
	ApplicationData.writeToFile(baf.toString(), appData.getAppTempPath()
		+ "downloadexam.txt");
	return baf.toString();
    }

    /**
     * This gets executed as soon as the response is obtained.
     *
     * @throws ImproperResponseHandlerException the improper response handler exception
     */
    protected abstract void onResponse()
	    throws ImproperResponseHandlerException;

    /**
     * Gets the response.
     *
     * @return the response
     */
    public String getResponse() {
	return response;
    }

    /**
     * Gets the response status.
     *
     * @return the response status
     */
    public String getResponseStatus() {
	return responseStatus.getStatus();
    }

    /**
     * Gets the callback.
     *
     * @return the callback
     */
    public DownloadCallback getCallback() {
	return callback;
    }

    /**
     * Sets the callback.
     *
     * @param callback the callback to set
     */
    public void setCallback(DownloadCallback callback) {
	this.callback = callback;
    }

    /**
     * Update status.
     *
     * @param status the status
     */
    protected void updateStatus(String status) {
	responseStatus.setStaus(status);

	if (callback != null) {
	    if (ResponseStatus.SUCCESS.equals(status)) {
		// callback.onSuccess("success");
	    } else if (ResponseStatus.FAIL.equals(status)) {
		callback.onFailure("fail");
	    }
	}
    }
}