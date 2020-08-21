package com.mlhsoftware.commandline.model;

import java.util.Enumeration;

import javax.microedition.io.file.FileSystemRegistry;

public class ListDrivesCmd extends BaseCmd
{
  public ListDrivesCmd( String cmdName, CmdTokener tokener ) 
  {
    super( cmdName, tokener );
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    
    Enumeration e = FileSystemRegistry.listRoots();
    while (e.hasMoreElements()) 
    {
      String tmp = (String) e.nextElement();
      sb.append( tmp );
      sb.append( "\n" );
    }    
    return sb.toString();
  }
}
