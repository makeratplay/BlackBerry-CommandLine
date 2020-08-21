package com.mlhsoftware.commandline.model;

import java.util.Date;

import com.mlhsoftware.commandline.model.DateUtil;

public class DateCmd extends BaseCmd
{
  public DateCmd( String cmdName, CmdTokener tokener ) 
  {
    super( cmdName, tokener );
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append( "The current date is: " );
    sb.append( DateUtil.formatTimestamp( new Date(), "EEE MM/dd/yyyy" ) ); // Sun 10/02/2011
    return sb.toString();
  }
}
