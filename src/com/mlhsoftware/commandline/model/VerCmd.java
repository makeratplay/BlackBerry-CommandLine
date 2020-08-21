package com.mlhsoftware.commandline.model;

import net.rim.device.api.system.CodeModuleManager;
import net.rim.device.api.system.DeviceInfo;

public class VerCmd extends BaseCmd
{
  public VerCmd( String cmdName, CmdTokener tokener ) 
  {
    super( cmdName, tokener );
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    
    String deviceName = DeviceInfo.getDeviceName();
    String deviceOS = CodeModuleManager.getModuleVersion( CodeModuleManager.getModuleHandleForObject( "" ) );
    
    
    sb.append( "BlackBerry " );
    sb.append( deviceName );
    sb.append( "\n" );
    sb.append( "OS Version " );
    sb.append( deviceOS );
    return sb.toString();
  }
}
