package com.mlhsoftware.commandline;

import com.mlhsoftware.commandline.ui.CommandLineScreen;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.ui.UiApplication;

/**
 * This class extends the UiApplication class, providing a graphical user
 * interface.
 */
public class CommandLine extends UiApplication
{
  /**
   * Entry point for application
   * 
   * @param args
   *          Command line arguments (not used)
   */
  public static void main( String[] args ) 
  {
    // Create a new instance of the application and make the currently
    // running thread the application's event dispatch thread.
    
    assertHasPermissions();
    
    CommandLine theApp = new CommandLine();
    theApp.enterEventDispatcher();
  }

  /**
   * Creates a new CommandLine object
   */
  public CommandLine()
  {
    // Push a screen onto the UI stack for rendering.
    pushScreen( new CommandLineScreen() );
  }
  
  // ASK FOR PERMISSIONS
  private static void assertHasPermissions()
  {
    // Capture the current state of permissions and check against the requirements.
    ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
    ApplicationPermissions original = apm.getApplicationPermissions();
    ApplicationPermissions permRequest = new ApplicationPermissions();
    int[] permissions = original.getPermissionKeys();
    for ( int i = 0; i < permissions.length; i++ )
    {
      permRequest.addPermission( i );
    }
    apm.invokePermissionsRequest( permRequest );
  }   
}
