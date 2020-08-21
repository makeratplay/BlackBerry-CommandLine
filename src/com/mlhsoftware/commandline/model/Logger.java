
package com.mlhsoftware.commandline.model;

import net.rim.device.api.system.EventLogger;

import com.mlhsoftware.commandline.AppInfo;

public class Logger 
{
  private static boolean registered = false;
  /*
  private static Logger instance = null;
  static public Logger GetInstance()
  {
    if ( instance == null )
    {
      instance = new Logger();
    }
    return instance;
  }
  */

  static private void register()
  {
    if ( !registered )
    {
      EventLogger.register( AppInfo.LOGGER_ID, AppInfo.APP_NAME, EventLogger.VIEWER_STRING );
      registered = true;
    }
  }

  static public void logError( String msg )
  {
    register();
    EventLogger.logEvent( AppInfo.LOGGER_ID, msg.getBytes(), EventLogger.SEVERE_ERROR );
    System.out.println( AppInfo.APP_NAME + ": " + msg );
  }

  static public void logDebug( String msg )
  {
    register();
    EventLogger.logEvent( AppInfo.LOGGER_ID, msg.getBytes(), EventLogger.DEBUG_INFO );
    if ( EventLogger.getMinimumLevel() <= EventLogger.DEBUG_INFO )
    {
      System.out.println( AppInfo.APP_NAME + ": " + msg );
    }
  }

  static public void enableDebugLogging()
  {
    EventLogger.setMinimumLevel( EventLogger.DEBUG_INFO );
  }

  static public void openEventLogViewer()
  {
    EventLogger.startEventLogViewer();
  }
}
