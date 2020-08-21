package com.mlhsoftware.commandline.ui;

import net.rim.device.api.system.ControlledAccessException;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.StandardTitleBar;
import net.rim.device.api.ui.container.MainScreen;

import com.mlhsoftware.commandline.model.BaseCmd;
import com.mlhsoftware.commandline.model.ClsCmd;
import com.mlhsoftware.commandline.model.CmdFactory;

public class CommandLineScreen extends MainScreen
{
  private ForegroundManager forgroundMgr;
  private CommandField input;
  private String prompt;

  public CommandLineScreen()
  {
    super( DEFAULT_MENU | DEFAULT_CLOSE |   Manager.NO_VERTICAL_SCROLL );
    
    try
    {
      StandardTitleBar myTitleBar = new StandardTitleBar();
      if ( myTitleBar != null )
      {
        //myTitleBar.addIcon("icon.png");
        myTitleBar.addTitle( "CMD" + "     "  );
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
    
    this.prompt = CmdFactory.getPrompt();
    
    this.forgroundMgr = new ForegroundManager();
    add( this.forgroundMgr );
     
    CommandResultText line = new CommandResultText( "MLH Software [Version 0.0.0.1]\n" );
    this.forgroundMgr.add(  line );
    this.input = new CommandField( this.prompt , "" );
    this.input.setChangeListener( new FieldChangeListener()
    {
      public void fieldChanged( Field field, int context )
      {
        onCommandFieldChanged( field, context );
      }
    } );
    this.forgroundMgr.add(  input );
    input.setFocus();
    
  }
  
  public boolean isDirty() { return false; };
  public boolean isMuddy() { return false; };  
  
  private void onCommandFieldChanged(  Field field, int context )
  {
    String text = field.toString();
    if ( text.indexOf('\n') != -1 )
    {
      processCommand( text );       
    }
  }
  
  private void processCommand( String text )
  {
    CommandResultText resultObj = new CommandResultText( "" );
    BaseCmd cmd = CmdFactory.getCommandClass( text, resultObj );
    if ( cmd != null )
    {
      // add current command prompt to screen history
      int index = this.input.getIndex();
      CommandResultText line = new CommandResultText( this.prompt + cmd.getPromptText()  );
      this.forgroundMgr.insert( line, index );

      // reset the command prompt
      this.prompt = CmdFactory.getPrompt();
      this.input.setText( "" );
      this.input.setLabel( this.prompt );
      
      if ( cmd instanceof ClsCmd )
      {
        this.forgroundMgr.deleteAll();
        this.forgroundMgr.add(  input );
      }
      else
      {
        String resultText = cmd.toString();
        if ( resultText != null && resultText.length() > 0 )
        {
          index = this.input.getIndex();
          resultObj.setText( resultText );
          this.forgroundMgr.insert( resultObj, index );
        }
      }
      
      int position = this.input.getExtent().y + this.input.getExtent().height; 
      this.forgroundMgr.setVerticalScroll( position, true );
    }
  }

}
