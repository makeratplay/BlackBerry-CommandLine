package com.mlhsoftware.commandline.model;

import java.util.Enumeration;
import java.util.Vector;

import com.mlhsoftware.commandline.model.CmdTokener.TokenException;
import com.mlhsoftware.commandline.ui.CommandResultText;

public class CmdFactory
{
  private static final String CMD_CLS = "cls";
  private static final String CMD_TIME = "time";
  private static final String CMD_DATE = "date";
  private static final String CMD_VER = "ver";
  private static final String CMD_EXIT = "exit";
  private static final String CMD_SYSTEMINFO = "systeminfo";
  private static final String CMD_HELP = "help";
  private static final String CMD_LISTDRIVES = "listdrives";
  private static final String CMD_DIR = "dir";
  private static final String CMD_CD = "cd";
  private static final String CMD_4SQ = "4sq";

  
  private static Vector folders = new Vector();
  
  public static String currentRoot = "store/";
  
  static public BaseCmd getCommandClass( String cmdString, CommandResultText resultObj )
  {
    BaseCmd retVal = null;
    CmdTokener cmdTokener = new CmdTokener( cmdString );
    
    try 
    {
      String cmdName = cmdTokener.getCmd();
      if ( cmdName.length() == 0 )
      {
        retVal = new NullCmd();
      }
      else if ( cmdName.equalsIgnoreCase( CMD_CLS ) )
      {
        retVal = new ClsCmd( cmdName, cmdTokener );
      }
      else if ( cmdName.equalsIgnoreCase( CMD_TIME ) )
      {
        retVal = new TimeCmd( cmdName, cmdTokener );
      }
      else if ( cmdName.equalsIgnoreCase( CMD_DATE ) )
      {
        retVal = new DateCmd( cmdName, cmdTokener );
      }
      else if ( cmdName.equalsIgnoreCase( CMD_VER ) )
      {
        retVal = new VerCmd( cmdName, cmdTokener );
      }
      else if ( cmdName.equalsIgnoreCase( CMD_EXIT ) )
      {
        retVal = new ExitCmd( cmdName, cmdTokener );
      }
      else if ( cmdName.equalsIgnoreCase( CMD_SYSTEMINFO ) )
      {
        retVal = new SystemInfoCmd( cmdName, cmdTokener );
      }
      else if ( cmdName.equalsIgnoreCase( CMD_HELP ) )
      {
        retVal = new HelpCmd( cmdName, cmdTokener );
      }
      else if ( cmdName.equalsIgnoreCase( CMD_LISTDRIVES ) )
      {
        retVal = new ListDrivesCmd( cmdName, cmdTokener );
      }
      else if ( cmdName.equalsIgnoreCase( CMD_DIR ) )
      {
        retVal = new DirCmd( cmdName, cmdTokener );
      }      
      else if ( cmdName.equalsIgnoreCase( CMD_CD ) )
      {
        retVal = new CdCmd( cmdName, cmdTokener );
      }      
      else if ( cmdName.equalsIgnoreCase( CMD_4SQ ) )
      {
        retVal = new FourSquareCmd( cmdName, cmdTokener, resultObj );
      }      
      else if ( FileUtil.isRootDrive( cmdName ) != null )
      {
        currentRoot = FileUtil.isRootDrive( cmdName );
        retVal = new NullCmd();
      }
      else
      {
        retVal = new UnrecognizedCmd( cmdName, cmdTokener );
      }
    } 
    catch ( TokenException e ) 
    {
    }
    return retVal;    
  }
  
  static public String getPrompt()
  {
    StringBuffer sb = new StringBuffer();
    sb.append( currentRoot );
    Enumeration e = folders.elements();
    while (e.hasMoreElements()) 
    {
      String folder = (String) e.nextElement();
      sb.append( folder );
      sb.append( "/" );
    }      
    sb.append( ">" );
    return  sb.toString();    
  }
  
  static public String getCurrentPath()
  {
    StringBuffer sb = new StringBuffer();
    sb.append( "file:///" );
    sb.append( currentRoot );
    Enumeration e = folders.elements();
    while (e.hasMoreElements()) 
    {
      String folder = (String) e.nextElement();
      sb.append( folder );
      sb.append( "/" );
    }      
    return  sb.toString();
  }
  
  static public void addPath( String path )
  {
    if ( path.equalsIgnoreCase( ".." ) )
    {
      folders.removeElementAt( folders.size() - 1 );
    }
    else if ( path.equalsIgnoreCase( "\\" ) || path.equalsIgnoreCase( "/" ) )
    {
      folders.removeAllElements();
    }
    else if ( path.equalsIgnoreCase( "." ) )
    {
      // do nothing
    }
    else
    {
      folders.addElement( path );
    }
  }
 
}
