package com.pearl.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import android.util.Log;

import com.sun.mail.smtp.SMTPSSLTransport;

// TODO: Auto-generated Javadoc
/**
 * The Class MailSender.
 */
public class MailSender implements ReportSender {

    /** The msg. */
    MimeMessage msg;
    
    /** The session. */
    Session session;
    
    /** The host. */
    String host = "smtp.gmail.com";
    
    /** The userid. */
    String userid = "sahitya.pasnoor@gmail.com";
    
    /** The password. */
    String password = "nokia@123";

    /* (non-Javadoc)
     * @see org.acra.sender.ReportSender#send(org.acra.collector.CrashReportData)
     */
    @Override
    public void send(CrashReportData report) throws ReportSenderException {
	// TODO Auto-generated method stub

	try {
	    final String enable = "true";
	    final List<String> toEmails = new ArrayList<String>();
	    toEmails.add("pearlsupportlog@pressmart.com");
	    final Properties props = System.getProperties();
	    props.put("mail.smtp.starttls.enable", enable);
	    props.put("mail.smtp.host", "smtp.rediffmail.com");
	    props.setProperty("mail.transport.protocol", "smptp");
	    props.put("mail.smtp.user", userid);
	    props.put("mail.smtp.password", "nokia@123");
	    props.put("mail.smtp.port", "25");
	    props.put("mail.smtps.auth", "true");
	    session = Session.getDefaultInstance(props, null);
	    msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(userid));

	    for (final String email : toEmails) {
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
			email));
	    }
	    msg.setSubject("Fatal Exception in Pearl on--" + new Date());
	    final HashMap<ReportField, String> reportData = new HashMap<ReportField, String>();

	    reportData.put(ReportField.ANDROID_VERSION,
		    report.getProperty(ReportField.ANDROID_VERSION));
	    reportData.put(ReportField.APP_VERSION_NAME,
		    report.getProperty(ReportField.APP_VERSION_NAME));
	    reportData.put(ReportField.USER_APP_START_DATE,
		    report.get(ReportField.USER_APP_START_DATE));
	    reportData.put(ReportField.STACK_TRACE,
		    report.get(ReportField.STACK_TRACE));
	    reportData.put(ReportField.CRASH_CONFIGURATION,
		    report.get(ReportField.CRASH_CONFIGURATION));
	    reportData.put(ReportField.USER_CRASH_DATE,
		    report.get(ReportField.USER_CRASH_DATE));
	    reportData.put(ReportField.INSTALLATION_ID,
		    report.get(ReportField.INSTALLATION_ID));
	    msg.setContent(report.toString(), "text/html");
	    

	    final SMTPSSLTransport transport = (SMTPSSLTransport) session
		    .getTransport("smtps");
	    transport.connect(host, userid, password);
	    msg.setContent(report, "text/html");
	    transport.sendMessage(msg, msg.getAllRecipients());
	    transport.close();

	} catch (final Exception e) {
	    Log.e("MAIL SENDER", "ERROR WHILE SENDING MAIL" + e);
	}

    }

}
