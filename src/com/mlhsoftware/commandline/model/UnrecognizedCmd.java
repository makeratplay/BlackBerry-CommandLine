package com.mlhsoftware.commandline.model;

public class UnrecognizedCmd extends BaseCmd
{
  public UnrecognizedCmd( String cmdName, CmdTokener tokener ) 
  {
    super( cmdName, tokener );
  }
  
  public String toString()
  {
    return  "'" + this.cmdName + "' is not recognized as an internal or external command, operable program or batch file.\n";
  }
}
