/**
 * 
 */
package com.pearl.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class SeedGenerator.
 */
public class SeedGenerator {

    /** The calendar. */
    private static Calendar calendar= new GregorianCalendar(2010, Calendar.JANUARY, 1);
    
    /** The calendar1. */
    private static Calendar calendar1 = Calendar.getInstance();
    
    /** The calendar2. */
    private static Calendar calendar2= new GregorianCalendar(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH)+1, calendar1.get(Calendar.DAY_OF_MONTH));
    
    /** The server id. */
    private static String serverId=null;

    /** The rand. */
    private static Random rand= new Random(); 

    /**
     * The main method.
     *
     * @param strings the arguments
     */
    public static void main(String...strings){
	getNextSeed();
    }
    static{
	serverId=System.getProperty("pressmart.server.id");
	if(serverId==null || serverId.length()==0){
	    serverId="R";
	}
    }
    
    /**
     * Gets the next seed.
     *
     * @return the next seed
     */
    public static synchronized String getNextSeed() {
	final String seedId = serverId+Long.toString( System.currentTimeMillis()-calendar.getTime().getTime()  , 36 )+Integer.toString(randomInt());
	Logger.error("Seed generator", "SEED id:"+seedId);
	return seedId;
    }

    /**
     * Gets the next token.
     *
     * @return the next token
     */
    public static synchronized String getNextToken() {
	final String seedId = "T"+Long.toString( calendar2.getTime().getTime()-System.currentTimeMillis()  , 36 )+Integer.toString(randomInt());
	System.out.println(seedId);
	return seedId;
    }
    
    /**
     * Random int.
     *
     * @return the int
     */
    public static synchronized int randomInt(){
	final int min=11111111;
	final int max=99999999;
	return rand.nextInt(max - min + 1) + min;

    }
    
    /** Minimum length for a decent password. */  
    public static final int MIN_LENGTH = 10;   



    /* Set of characters that is valid. Must be printable, memorable,  
     * and "won't break HTML" (i.e., not '<', '>', '&', '=', ...).  
     * or break shell commands (i.e., not '<', '>', '$', '!', ...).  
     * I, L and O are good to leave out, as are numeric zero and one.  
     */  
    /** The good char. */
    protected static char[] goodChar = {   
	// Comment out next two lines to make upper-case-only, then   
	// use String toUpper() on the user's input before validating.   
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',   
	'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',   
	'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N',   
	'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',   
	'1','2', '3', '4', '5', '6', '7', '8', '9',   
	'+', '@',   
    };   

    /* Generate a Password object with a random password. */  
    /**
     * Gets the pass phrase.
     *
     * @return the pass phrase
     */
    public static String getPassPhrase() {   
	return getPassPhrase(MIN_LENGTH);   
    }   

    /* Generate a Password object with a random password. */  
    /**
     * Gets the pass phrase.
     *
     * @param length the length
     * @return the pass phrase
     */
    public static String getPassPhrase(int length) {   
	if (length < 1) {   
	    throw new IllegalArgumentException(   
		    "Ridiculous password length " + length);   
	}   
	final StringBuffer sb = new StringBuffer();   
	for (int i = 0; i < length; i++) {   
	    sb.append(goodChar[rand.nextInt(goodChar.length)]);   
	}   
	return sb.toString();   
    }   


}
