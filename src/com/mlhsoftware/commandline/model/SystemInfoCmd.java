package com.mlhsoftware.commandline.model;



import net.rim.device.api.system.CodeModuleManager;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.RadioInfo;

public class SystemInfoCmd extends BaseCmd
{
  public SystemInfoCmd( String cmdName, CmdTokener tokener ) 
  {
    super( cmdName, tokener );
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append( "BlackBerry System Info\n" );
    
    
    String deviceName = DeviceInfo.getDeviceName();
    String deviceOS = CodeModuleManager.getModuleVersion( CodeModuleManager.getModuleHandleForObject( "" ) );

    sb.append( "Model " );
    sb.append( deviceName );
    sb.append( "\n" );
    
    sb.append( "OS Version " );
    sb.append( deviceOS );
    sb.append( "\n" );

    int deviceId = DeviceInfo.getDeviceId();
    String deviceIdText = java.lang.Integer.toHexString( deviceId );
    String pin = deviceIdText.toUpperCase();
    sb.append( "PIN " );
    sb.append( pin );
    sb.append( "\n" );

    String battery = DeviceInfo.getBatteryLevel() + "%";
    sb.append( "Battery " );
    sb.append( battery );
    sb.append( "\n" );
    
    String signal = RadioInfo.getSignalLevel() + " dBm";
    sb.append( "Signal " );
    sb.append( signal );
    sb.append( "\n" );
    
    
    return sb.toString();
  }
}
