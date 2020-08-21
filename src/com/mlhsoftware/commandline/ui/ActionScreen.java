/**

 */
package com.mlhsoftware.commandline.ui;

import java.util.Enumeration;
import java.util.Vector;

import com.mlhsoftware.commandline.AppInfo;
import com.mlhsoftware.commandline.model.Action;
import com.mlhsoftware.commandline.model.ActionListener;

import net.rim.device.api.system.ControlledAccessException;
import net.rim.device.api.ui.Manager;

import net.rim.device.api.ui.component.StandardTitleBar;
import net.rim.device.api.ui.container.MainScreen;

public class ActionScreen extends MainScreen
{

  protected Vector actionListeners = new Vector();

  protected ActionScreen()
  {
    super( DEFAULT_MENU | DEFAULT_CLOSE | Manager.NO_VERTICAL_SCROLL );
    
    try
    {
      StandardTitleBar myTitleBar = new StandardTitleBar();
      if ( myTitleBar != null )
      {
        //myTitleBar.addIcon("icon.png");
        myTitleBar.addTitle( AppInfo.APP_NAME + "     "  );
        myTitleBar.addClock();
        myTitleBar.addNotifications();
        myTitleBar.addSignalIndicator();
        
        myTitleBar.setPropertyValue(StandardTitleBar.PROPERTY_BATTERY_VISIBILITY, StandardTitleBar.BATTERY_VISIBLE_LOW_OR_CHARGING);
        try
        {
          setTitleBar(myTitleBar);
        }
        catch ( ControlledAccessException e2 )
        {
          
        }
        catch ( SecurityException e1 )
        {
          
        }
      }
    }
    catch( Exception e )
    {
    }     
  }

  protected ActionScreen( long style )
  {
    super( style );
  }

  public void addActionListener( ActionListener actionListener )
  {
    if ( actionListener != null )
    {
      actionListeners.addElement( actionListener );
    }
  }

  protected void fireAction( String action )
  {
    fireAction( action, null );
  }

  protected void fireAction( String action, Object data )
  {
    Enumeration listenersEnum = actionListeners.elements();
    while ( listenersEnum.hasMoreElements() )
    {
      ( (ActionListener)listenersEnum.nextElement() ).onAction( new Action( this, action, data ) );
    }
  }

}
