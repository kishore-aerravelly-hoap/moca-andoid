package com.pearl.parsers.xml;

/*package com.pearl.parsers.xml;

 import org.xml.sax.Attributes;
 import org.xml.sax.SAXException;
 import org.xml.sax.helpers.DefaultHandler;

 import android.util.Log;

 import com.pearl.network.request.ResponseStatus;
 import com.pearl.users.Role;
 import com.pearl.users.User;

 public class LoginResponseParser extends DefaultHandler {
 private User user;
 private ResponseStatus response;
 private String versionName;

 private boolean currentElement;
 private String currentValue;

 public LoginResponseParser() {
 user = new User();
 response = new ResponseStatus();
 versionName = "1.0";

 currentElement = false;
 currentValue = "";
 }

 public User getUser(){
 return this.user;
 }

 public ResponseStatus getResponseStatus(){
 return this.response;
 }

 public String getAppLatestVersion(){
 return this.versionName;
 }

 *//**
 * Called when tag starts ( ex:- <name>Book Name</name> -- <name> )
 */
/*
 @Override
 public void startElement(String uri, String localName, String qName,
 Attributes attributes) throws SAXException {

 currentElement = true;
 currentValue = "";

 if (localName.equals("response")) {
 // Initiate/reset response object
 response = new ResponseStatus();
 } else if (localName.equals("user")) {
 // Initiate/reset user object
 user = new User();
 }
 }

 *//**
 * Called when tag closing ( ex:- <name>Book Name</name> -- </name> )
 */
/*
 @Override
 public void endElement(String uri, String localName, String qName)
 throws SAXException {

 currentElement = false;
 if (response == null){
 return; //start only after this gets initialized
 }

 *//** set value */
/*
 if (localName.equalsIgnoreCase("status")){
 if("success".equalsIgnoreCase(currentValue)){
 response.setStaus(ResponseStatus.SUCCESS);
 }else{
 Log.i("LoginResponseParser", "Value of status :" + currentValue+":");
 response.setStaus(ResponseStatus.FAIL);
 }
 }else if(localName.equalsIgnoreCase("message")){
 response.setMessage(currentValue);
 }else if(localName.equalsIgnoreCase("id")){
 if("".equals(currentValue.trim())){
 currentValue = ""+0;
 }

 user.setId(currentValue);
 }else if("grade".equalsIgnoreCase(localName)){
 if("".equals(currentValue.trim())){
 currentValue = ""+0;
 }

 // TODO as of now considering only one grade value
 user.setGrade(Integer.parseInt(currentValue));
 }else if(localName.equalsIgnoreCase("username")){
 user.setUserName(currentValue);
 }else if(localName.equalsIgnoreCase("role")){
 if(currentValue.equalsIgnoreCase("student")){
 user.setRole(Role.STUDENT);
 }else if(currentValue.equalsIgnoreCase("teacher")){
 user.setRole(Role.TEACHER);
 }else{
 user.setRole(Role.GUEST);
 }
 }else if("lastlogin".equalsIgnoreCase(localName)){
 user.setLastLoginTime(localName);
 }else if("version".equalsIgnoreCase(localName)){
 this.versionName = currentValue;
 }

 currentValue = "";
 }

 *//**
 * Called to get tag characters ( ex:- <name>Book Name</name> -- to get
 * 'Book Name' Character )
 */
/*
 * @Override public void characters(char[] ch, int start, int length) throws
 * SAXException { if (currentElement) { currentValue = new String(ch, start,
 * length); currentElement = false;
 * 
 * currentValue = currentValue.trim(); } } }
 */