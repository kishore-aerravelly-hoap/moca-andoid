package com.pearl.network.request.handlers;

/*package com.pearl.network.request.handlers;

 import java.io.ByteArrayInputStream;

 import javax.xml.parsers.SAXParser;
 import javax.xml.parsers.SAXParserFactory;

 import org.xml.sax.InputSource;
 import org.xml.sax.XMLReader;

 import com.pearl.logger.Logger;
 import com.pearl.network.request.RequestHandlerInterface;
 import com.pearl.network.request.ResponseStatus;
 import com.pearl.network.request.exception.ImproperResponseHandlerException;
 import com.pearl.parsers.xml.LoginResponseParser;
 import com.pearl.users.User;

 public class LoginRequest extends RequestHandlerInterface{
 private User user;
 private ResponseStatus responseMessage;

 public LoginRequest(String requestUrl) {
 super(requestUrl);

 responseMessage = new ResponseStatus();
 responseMessage.setStaus(ResponseStatus.IN_PROGRESS);

 user = new User();

 Logger.info("Login Request", "processing url:"+requestUrl);
 }

 @Override
 protected void onResponse() throws ImproperResponseHandlerException {
 try{
 Logger.info("Login Request", "in on response handler");

 ByteArrayInputStream bis = new ByteArrayInputStream(response.getBytes());

 *//** Handling XML */
/*
 * SAXParserFactory spf = SAXParserFactory.newInstance(); SAXParser sp =
 * spf.newSAXParser(); XMLReader xr = sp.getXMLReader();
 * 
 * LoginResponseParser parser = new LoginResponseParser();
 * 
 * xr.setContentHandler(parser);
 * 
 * InputSource is = new InputSource(bis); is.setEncoding("utf-8"); xr.parse(is);
 * bis.close();
 * 
 * user = parser.getUser(); responseMessage = parser.getResponseStatus();
 * }catch(Exception e){ Logger.error("Login Request", e);
 * 
 * throw new ImproperResponseHandlerException(e); } }
 * 
 * public User getUser(){ return this.user; }
 * 
 * public ResponseStatus getResponseMessage(){ return this.responseMessage; } }
 */