package com.mlhsoftware.commandline.model;

import java.io.IOException;
import java.util.Enumeration;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class DirCmd extends BaseCmd
{
  public DirCmd( String cmdName, CmdTokener tokener ) 
  {
    super( cmdName, tokener );
  }
  
  public String toString()
  {
    String path = CmdFactory.getCurrentPath();
    StringBuffer sb = new StringBuffer();
    
    FileConnection fconn = null;
    try
    {
      fconn = (FileConnection)Connector.open( path, Connector.READ );
      if (fconn.exists())
      {
        Enumeration e = fconn.list();
        while (e.hasMoreElements()) 
        {
          String tmp = (String) e.nextElement();
          sb.append( tmp );
          sb.append( "\n" );
        }       
      }
    }
    catch (Exception e) 
    {
      Logger.logError("Error: " + e.getClass() + " " + e.getMessage());
    }
    finally
    {
      try 
      {
        fconn.close();
      } catch ( IOException e ) 
      {
        Logger.logError("Error: " + e.getClass() + " " + e.getMessage());
      }
    }
   
    return sb.toString();
  }
}
