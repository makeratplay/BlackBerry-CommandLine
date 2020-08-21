/*
 * TitlebarManager.java
 *
 * MLH Software
 * Copyright 2010
 */

package com.mlhsoftware.commandline.model;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import java.io.*;
import net.rim.device.api.system.EventLogger;

public class FileUtil 
{

  public FileUtil()
  {
  // default constructor
  }

  public final static Vector listRoots()
  {
    Vector roots = new Vector();
    Enumeration e = FileSystemRegistry.listRoots();
    while (e.hasMoreElements()) 
    {
      roots.addElement((String) e.nextElement());
    }
    return roots;
  }

  public static String getInternalRoot()
  {
    return "file:///store/home/user";
  }

  public static String getExternalRoot()
  {
    return "file:///SDCard";
  }

  public final static String getRootFileSystem()
  {
    String root = null;
    Enumeration e = FileSystemRegistry.listRoots();
    while (e.hasMoreElements()) 
    {
      String tmp = (String) e.nextElement();
      if ( tmp.equalsIgnoreCase( "sdcard/" ) ) 
      {
        return getExternalRoot();
      }
      else if ( tmp.equalsIgnoreCase( "store/" ) ) 
      {
        root = getInternalRoot();
      }
    }
    return root;
  }
  
  static public String isRootDrive( String cmdName )
  {
    String root = null;
    if ( cmdName != null && cmdName.length() > 0 )
    {
      if ( cmdName.equalsIgnoreCase( "c:" ) )
      {
        cmdName = "system";
      }
      else if ( cmdName.equalsIgnoreCase( "a:" ) )
      {
        cmdName = "store";
      }
    }    
    
    if ( cmdName.charAt( cmdName.length() - 1 ) != '/' )
    {
      cmdName = cmdName + '/';
    }
    
    Enumeration e = FileSystemRegistry.listRoots();
    while (e.hasMoreElements()) 
    {
      String tmp = (String) e.nextElement();
      if ( tmp.equalsIgnoreCase( cmdName ) ) 
      {
        root = tmp;
        break;
      }
    }
    
    return root;
  }  

  public final static boolean checkExists( String passFileOrDirName )
  {
    boolean result = false;
    FileConnection fconn;
    try
    {
      fconn = (FileConnection)Connector.open( passFileOrDirName, Connector.READ );
      if (!fconn.exists())
      {
        result = false;
      }
      else
      {
        result = true;
      }
      fconn.close();
    }
    catch (Exception e) 
    {
      Logger.logError("Error: " + e.getClass() + " " + e.getMessage());
    }
    return result;
  }

  public final static boolean createFileOrDir( String passFileOrDirName, boolean asFile )
  {
    boolean result = false;
    FileConnection fconn;
    try 
    {
      fconn = (FileConnection)Connector.open( passFileOrDirName, Connector.READ_WRITE );
      if (fconn.exists()) 
      {
        Logger.logDebug( "File/Dir already exists: " + passFileOrDirName );
        fconn.close();
      }
      else
      {
        fconn.close();
        try 
        {
          fconn = (FileConnection)Connector.open( passFileOrDirName, Connector.READ_WRITE );
          if (asFile)
          {
            fconn.create();
          }
          else
          {
            fconn.mkdir();
          }
          result = true;
          fconn.close();
        }
        catch (Exception e) 
        {
          Logger.logError( "Error: " + e.getClass() + " " + e.getMessage() );
        }
      }
    }
    catch (Exception e) 
    {
      Logger.logError( "Error accessing root filesystem " + e.toString() );
    }
    return result;
  }

  public final static boolean writeTextFile( String passFname, String passContent )
  {
    boolean result = false;
    try 
    {
      Logger.logDebug( "Write File: " + passFname );
      if ( EventLogger.getMinimumLevel() <= EventLogger.DEBUG_INFO )
      {
        System.out.println( "Write File: " + passContent );
      }
      FileConnection fconn = (FileConnection)Connector.open( passFname, Connector.READ_WRITE );
      // If no exception is thrown, then the URI is valid, but the file may or may not exist.
      if (!fconn.exists()) 
      {
        fconn.create();
      }
      
     
      DataOutputStream dos = fconn.openDataOutputStream();
      // dos.writeChars is used for example purposes only
      // to understand why, view the resultant file
      dos.writeUTF(passContent);
      dos.close();
      
      /*
      OutputStream os = fconn.openOutputStream();
      os.write( passContent.getBytes() );
      os.close();
      */
      fconn.close();
      result = true;
    }
    catch (Exception e) 
    {
      Logger.logError( "Error writing file: " + passFname + ";" + e.toString() );
    }
    return result;
  }

  public final static String readTextFile( String passFname )
  {
    String result = null;
    try 
    {
      FileConnection fconn = (FileConnection)Connector.open( passFname, Connector.READ_WRITE );
      // If no exception is thrown, then the URI is valid, but the file may or may not exist.
      if (fconn.exists()) 
      {
        
        DataInputStream dos = fconn.openDataInputStream();
        result = dos.readUTF();
        dos.close();
        
        /*
        InputStream is = fconn.openInputStream();
        byte[] data = new byte[];
        int len = is.read( data );
        result = new String( data );
        */
        
        fconn.close();
      }
    }
    catch (Exception e) 
    {
      Logger.logError( "Error reading file: " + passFname + ";" + e.toString() );
    }
    if ( EventLogger.getMinimumLevel() <= EventLogger.DEBUG_INFO )
    {
      System.out.println( "Read File: " + result );
    }
    return result;
  }

  public final static String readBinaryFile( String passFname, byte[] data )
  {
    String result = null;
    try 
    {
      FileConnection fconn = (FileConnection)Connector.open( passFname, Connector.READ_WRITE );
      // If no exception is thrown, then the URI is valid, but the file may or may not exist.
      if (fconn.exists()) 
      {
        int length = (int)fconn.fileSize();
        DataInputStream dos = fconn.openDataInputStream();
        dos.readFully( data );
        dos.close();
        fconn.close();
      }
    }
    catch (Exception e) 
    {
      Logger.logError( "Error accessing root filesystem " + e.toString() );
    }
    return result;
  }

  public final static int getFileSize( String passFname )
  {
    int length = 0;
    try 
    {
      FileConnection fconn = (FileConnection)Connector.open( passFname, Connector.READ_WRITE );
      // If no exception is thrown, then the URI is valid, but the file may or may not exist.
      if (fconn.exists()) 
      {
        length = (int)fconn.fileSize();
        fconn.close();
      }
    }
    catch (Exception e) 
    {
      Logger.logError( "Error accessing root filesystem " + e.toString() );
    }
    return length;
  }
}
