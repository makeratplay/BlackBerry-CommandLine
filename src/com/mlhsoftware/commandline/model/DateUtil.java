
package com.mlhsoftware.commandline.model;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import net.rim.device.api.i18n.SimpleDateFormat;

public class DateUtil 
{

  public DateUtil()
  {
  // default constructor
  }

  public final static String convertToLocalTime( String timestamp )
  {
    String retVal = "";
    try
    {
      //0123456789012345678901234
      //2011-02-09 13:30:07 +0000

      int year = Integer.parseInt( timestamp.substring( 0, 4 ) );
      int month = Integer.parseInt( timestamp.substring( 5, 7 ) ) - 1;
      int day = Integer.parseInt( timestamp.substring( 8, 10 ) ) - 1;
      int hour = Integer.parseInt( timestamp.substring( 11, 13 ) );
      int minute = Integer.parseInt( timestamp.substring( 14, 16 ) );

      Date timstamp = convertToLocalTime( year, month, day, hour, minute );
      retVal = formatTimestamp( timstamp, "MM/dd/yyyy HH:mm" );
      //retVal = friendlyTime( timstamp );
    }
    catch ( Exception e )
    {
      String msg = "convertToLocalTime failed: " + e.toString();
      System.out.println( msg );
    }
    return retVal;
  }

  public final static Date convertToLocalTime(int year, int month, int day, int hour, int minute )
  {
    Calendar utcTime = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
    utcTime.set( Calendar.YEAR, year );
    utcTime.set( Calendar.MONTH, month );
    utcTime.set( Calendar.DAY_OF_MONTH, day );
    utcTime.set( Calendar.HOUR, hour );
    utcTime.set( Calendar.MINUTE, minute );

    Calendar localTime = Calendar.getInstance();
    localTime.setTime( new Date( utcTime.getTime().getTime() ) );

    return localTime.getTime();
  }

  public final static String formatTimestamp( Date timestamp, String format )
  {
    String retVal = "";
    Calendar localTime = Calendar.getInstance();
    localTime.setTime( timestamp );

    SimpleDateFormat formatToday = new SimpleDateFormat( format );
    StringBuffer sb;
    sb = new StringBuffer();

    formatToday.format( localTime, sb, null );
    retVal = sb.toString();
    return retVal;
  }

  // If less than 10 seconds it will say "just now". 
  // If less than a minute it will say "x seconds ago". 
  // If less than an hour it will say "x minutes ago". 
  // If less than 12 hours, will say "x hours ago". 
  // If more than 12 hours the social aspect has long passed and no longer matters, so will just show the normal timestamp.

  public final static String friendlyTime( Date timestamp )
  {
    String retVal = "";

    Date currentTime = new Date();
    long diffInSeconds = ( timestamp.getTime() - currentTime.getTime() ) / 1000;

    long diff[] = new long[] { 0, 0, 0, 0 };
    /* sec */
    diff[3] = ( diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds );
    /* min */
    diff[2] = ( diffInSeconds = ( diffInSeconds / 60 ) ) >= 60 ? diffInSeconds % 60 : diffInSeconds;
    /* hours */
    diff[1] = ( diffInSeconds = ( diffInSeconds / 60 ) ) >= 24 ? diffInSeconds % 24 : diffInSeconds;
    /* days */
    diff[0] = ( diffInSeconds = ( diffInSeconds / 24 ) );

    if ( diffInSeconds < 10 )
    {
      retVal = "now";
    }
    else if ( diffInSeconds < 60 )
    {
      retVal = diff[3] + "secounds ago";
    }
    else if ( diffInSeconds < (60 * 60) )
    {
      retVal = diff[2] + "minutes ago";
    }
    else if ( diffInSeconds < ( 60 * 60 * 12 ) )
    {
      retVal = diff[1] + "hours ago";
    }
    else
    {
      retVal = formatTimestamp( timestamp, "MM/dd/yyyy HH:mm" );
    }
    return retVal;
  }


}
