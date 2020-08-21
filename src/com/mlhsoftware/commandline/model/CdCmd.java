package com.mlhsoftware.commandline.model;

import com.mlhsoftware.commandline.model.CmdTokener.TokenException;


public class CdCmd extends BaseCmd
{
  private String path;
  private String resultText;
  public CdCmd( String cmdName, CmdTokener tokener ) 
  {
    super( cmdName, tokener );
    try 
    {
      this.resultText = null;
      path = tokener.nextValue();
      
      if ( path != null && path.length() > 0 )
      {
        if ( path.equals( ".." ) )
        {
          CmdFactory.addPath( path );
        }
        else if ( FileUtil.checkExists( CmdFactory.getCurrentPath() + path  ) )
        {
          CmdFactory.addPath( path );
        }
        else
        {
          this.resultText = "The system cannot find the path specified.";
        }
      }
      else
      {
        this.resultText = CmdFactory.getCurrentPath();
      }
    } 
    catch ( TokenException e ) 
    {
      Logger.logError( "CdCmd Error: " + e.toString() );
    }
  }
  
  public String toString()
  {
    if ( this.resultText != null )
    {
      return this.resultText;
    }
    return "";
  }
}
