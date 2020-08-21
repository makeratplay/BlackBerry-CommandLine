package com.mlhsoftware.commandline.model;

public class BaseCmd
{
  protected CmdTokener tokener;
  protected String cmdName;
  
  protected BaseCmd( String cmdName, CmdTokener tokener )
  {
    this.cmdName = cmdName;
    this.tokener = tokener;
  }

  public String getCmdName() 
  {
    return cmdName;
  }
  
  public String toString()
  {
    return "";
  }
  
  public String getPromptText()
  {
    String retVal = "";
    if ( tokener != null )
    {
      retVal = tokener.toString();
    }
      
    return retVal;
  }
}
