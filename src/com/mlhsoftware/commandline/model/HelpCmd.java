package com.mlhsoftware.commandline.model;

import java.util.Date;

public class HelpCmd extends BaseCmd
{
  public HelpCmd( String cmdName, CmdTokener tokener ) 
  {
    super( cmdName, tokener );
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    
    sb.append( "CLS        Clears the screen.\n" );
    sb.append( "TIME       Displays the system time.\n" );
    sb.append( "DATE       Displays the date.\n" );
    sb.append( "VER        Displays the BlackBerry version.\n" );
    sb.append( "EXIT       Quits the program.\n" );
    sb.append( "SYSTEMINFO Displays BlackBerry specific properties and configuration.\n" );    
    return sb.toString();
  }
}
