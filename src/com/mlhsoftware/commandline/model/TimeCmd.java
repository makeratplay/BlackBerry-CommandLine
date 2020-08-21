package com.mlhsoftware.commandline.model;

import java.util.Date;

public class TimeCmd extends BaseCmd
{
  public TimeCmd( String cmdName, CmdTokener tokener ) 
  {
    super( cmdName, tokener );
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append( "The current time is: " );
    sb.append( DateUtil.formatTimestamp( new Date(), "HH:mm:ss" ) ); // hh:mm:ss
    return sb.toString();
  }
}
