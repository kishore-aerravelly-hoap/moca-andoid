package com.pearl.network.request.handlers;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.RequestHandlerInterface;
import com.pearl.network.request.exception.ImproperResponseHandlerException;
import com.pearl.network.request.parameters.FileParameter;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.network.request.parameters.exceptions.UnknownParameterTypeException;

// TODO: Auto-generated Javadoc
/**
 * The Class PostRequestHandler.
 */
public class PostRequestHandler extends RequestHandlerInterface {
    
    /** The request params. */
    ArrayList<RequestParameter> requestParams = new ArrayList<RequestParameter>();

    // DownloadCallback callback;

    /**
     * Instantiates a new post request handler.
     *
     * @param requestUrl the request url
     */
    public PostRequestHandler(String requestUrl) {
	super(requestUrl);
    }

    /**
     * Instantiates a new post request handler.
     *
     * @param requestUrl the request url
     * @param params the params
     */
    public PostRequestHandler(String requestUrl,
	    ArrayList<RequestParameter> params) {
	super(requestUrl);

	requestParams = params;
    }

    /**
     * Instantiates a new post request handler.
     *
     * @param requestUrl the request url
     * @param params the params
     * @param callback the callback
     */
    public PostRequestHandler(String requestUrl,
	    ArrayList<RequestParameter> params, DownloadCallback callback) {
	this(requestUrl, params);

	this.callback = callback;
    }

    /* (non-Javadoc)
     * @see com.pearl.network.request.RequestHandlerInterface#processRequest(java.lang.String)
     */
    @Override
    protected String processRequest(String requrl) throws Exception {
	final HttpClient httpClient = new DefaultHttpClient();

	final MultipartEntity entity = new MultipartEntity(
		HttpMultipartMode.BROWSER_COMPATIBLE);

	if (null != requestParams) {
	    for (final RequestParameter entry : requestParams) {
		Logger.error("Post Request Handler",
			"param type:" + entry.getParameterType()
			+ " - tostring: " + entry.toString());

		if (RequestParameter.STRING_PARAM.equals(entry
			.getParameterType())) {
		    final StringParameter param = (StringParameter) entry;

		    Logger.error("Post Request Handler",
			    "param name:" + param.getFieldName()
			    + ": param value:" + param.getValue());

		    entity.addPart(param.getFieldName(),
			    new StringBody(param.getValue()));
		} else if (RequestParameter.FILE_PARAM.equals(entry
			.getParameterType())) {
		    final FileParameter param = (FileParameter) entry;

		    final File file = param.getFile();

		    final FileBody fileBody = new FileBody(file,
			    param.getContentType());
		    entity.addPart(param.getFieldName(), fileBody);
		} else {
		    throw new UnknownParameterTypeException();
		}
	    }
	}

	final HttpPost httpPost = new HttpPost(requrl);
	httpPost.setEntity(entity);

	// Create a response handler
	final ResponseHandler<String> responseHandler = new BasicResponseHandler();
	httpClient.getParams().setParameter("http.socket.timeout",
		Integer.valueOf(60000));
	httpClient.getParams().setParameter("http.connection.timeout",
		Integer.valueOf(60000));
	httpClient.getParams().setParameter("http.protocol.content-charset",
		"UTF-8");
	final String responseBody = httpClient.execute(httpPost, responseHandler);
	if (responseBody != null) {
	    Logger.warn("PostRequestHandler", "response body is : "
		    + responseBody);
	    ApplicationData.writeToFile(responseBody, appData.getAppTempPath()
		    + "downloadExamjson.txt");
	} else
	    Logger.warn("PostRequestHandler", "response body is null");
	httpClient.getConnectionManager().shutdown();
	return responseBody;
	/*
	 * // Create a response handler ResponseHandler<String> responseHandler
	 * = new BasicResponseHandler(); HttpResponse responseBody =
	 * httpClient.execute(httpPost);//, responseHandler);
	 * 
	 * httpClient.getConnectionManager().shutdown();
	 * 
	 * return responseBody.toString();
	 */
    }

    /* (non-Javadoc)
     * @see com.pearl.network.request.RequestHandlerInterface#onResponse()
     */
    @Override
    protected void onResponse() throws ImproperResponseHandlerException {
	callback.onSuccess(response);
    }
}