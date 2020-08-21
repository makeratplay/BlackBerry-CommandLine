package com.mlhsoftware.commandline.model;


public class ExitCmd extends BaseCmd
{
  public ExitCmd( String cmdName, CmdTokener tokener ) 
  {
    super( cmdName, tokener );
  }
  
  public String toString()
  {
    System.exit( 0 ); // exit the app
    return "";
  }
}
