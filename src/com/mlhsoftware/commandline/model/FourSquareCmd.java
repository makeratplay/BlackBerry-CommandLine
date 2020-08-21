package com.mlhsoftware.commandline.model;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.UiApplication;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mlhsoftware.commandline.RESTAPI.FourSquareClient;
import com.mlhsoftware.commandline.model.CmdTokener.TokenException;
import com.mlhsoftware.commandline.network.CookieManager;
import com.mlhsoftware.commandline.network.HttpClient;
import com.mlhsoftware.commandline.network.HttpConnectionFactory;
import com.mlhsoftware.commandline.oAuth.FourSquareLoginScreen;
import com.mlhsoftware.commandline.ui.CommandResultText;

public class FourSquareCmd extends BaseCmd implements ActionListener
{
  private FourSquareClient foursquareClient = null;
  private HttpConnectionFactory connFactory = null;
  private FourSquareLoginScreen fsLoginScreen = null;
  private CookieManager cookieManager = null;
  private CommandResultText resultObj;
  
  private String outputText;
  
  public FourSquareCmd(  String cmdName, CmdTokener tokener, CommandResultText resultObj  )
  {
    super(cmdName,tokener);
    this.outputText = "";
    this.resultObj = resultObj;
    String param;
    try 
    {
      param = tokener.nextValue();
      if ( param.equalsIgnoreCase( "friends" ))
      {
        DisplayFriendsList();
      }
      else if ( param.equalsIgnoreCase( "history" ))
      {
        DisplayHistoryList();
      }
      else if ( param.equalsIgnoreCase( "search" ))
      {
        String log = tokener.nextValue();
        String lat = tokener.nextValue();
        DisplaySearchList( log, lat );
      }
    } 
    catch ( TokenException e ) 
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    
    
    //LoginFourSquare();
  }
  
  private void DisplayFriendsList()
  {
    this.outputText = "fetching data...";
    ( new friedsThread() ).start();
  }
  
  private void DisplayHistoryList()
  {
    this.outputText = "fetching data...";
    ( new historyThread() ).start();
  }
  
  private void DisplaySearchList( String log, String lat )
  {
    this.outputText = "searching...";
    searchThread thread = new searchThread();
    thread.lat = lat;
    thread.log = log;
    thread.start();
  }
  
  private void LoginFourSquare()
  {

    //Browser.getDefaultSession().displayPage( fsq_url );

    if ( this.foursquareClient == null )
    {
      this.cookieManager = new CookieManager();
      
      this.foursquareClient = new FourSquareClient( "your applicationKey", 
                                                    "your secretKey", 
                                                    "your redirectUri", this.connFactory );
    }

    this.fsLoginScreen = new FourSquareLoginScreen( this.foursquareClient, this.connFactory, this.cookieManager, "Command" );
   // this.fsLoginScreen.addActionListener( this );
   // this.fsLoginScreen.login();
    UiApplication.getUiApplication().pushScreen( this.fsLoginScreen ); 
  }

  public void onAction( Action event ) 
  {
  }
  
  public String toString()
  {
    return this.outputText;
  }
  
  
  
  private class friedsThread extends Thread
  {
    public void run()
    {
      String displayResults = "no data";
      try
      {
        HttpClient httpClient = new HttpClient(  new HttpConnectionFactory() );
        String url = "https://api.foursquare.com/v2/users/self/friends?oauth_token=XXXXX";
        StringBuffer responseSB = httpClient.doGet( url );
        
        JSONObject results = new JSONObject( responseSB.toString() );
        
        StringBuffer sb = new StringBuffer();
        
        JSONObject response = results.optJSONObject( "response" );
        if ( response != null )
        {
          JSONObject friends = response.optJSONObject( "friends" );
          if ( friends != null )
          {
            JSONArray items = friends.optJSONArray( "items" );
            if ( items != null  )
            {
              int len = items.length();
              for ( int index = 0; index < len; index++ )
              {
                try
                {
                  JSONObject friend = items.getJSONObject( index );
                  sb.append( friend.optString( "firstName" ) );
                  sb.append( " " );
                  sb.append( friend.optString( "lastName" ) );
                  sb.append( "\n" );
                }
                catch ( JSONException e )
                {
                  Logger.logError( e.toString() );
                }
              }
            }
          }          
        }
      
        displayResults = sb.toString();
        
        //System.out.println( "THE RESULTS: " + response.toString() );
      }
      catch ( Exception e )
      {
        displayResults = "Error: " + e.toString();
        //log.error( e.getMessage() );
      }
      
      Object eventLock = Application.getApplication().getAppEventLock();
      synchronized ( eventLock ) // obtain lock for thread synchronization
      {
        resultObj.setText( displayResults );
      }        
    }
  }

  private class historyThread extends Thread
  {
    public void run()
    {
      String displayResults = "no data";
      try
      {
        HttpClient httpClient = new HttpClient(  new HttpConnectionFactory() );
        String url = "https://api.foursquare.com/v2/users/self/checkins?oauth_token=XXXX";
        StringBuffer responseSB = httpClient.doGet( url );
        
        JSONObject results = new JSONObject( responseSB.toString() );
        
        StringBuffer sb = new StringBuffer();
        
        JSONObject response = results.optJSONObject( "response" );
        if ( response != null )
        {
          JSONObject checkins = response.optJSONObject( "checkins" );
          if ( checkins != null )
          {
            JSONArray items = checkins.optJSONArray( "items" );
            if ( items != null  )
            {
              int len = items.length();
              for ( int index = 0; index < len; index++ )
              {
                try
                {
                  sb.append( items.getJSONObject( index ).getJSONObject( "venue" ).getString( "name" ) );
                  sb.append( "\n" );
                }
                catch ( JSONException e )
                {
                  Logger.logError( e.toString() );
                }
              }
            }
          }          
        }
      
        displayResults = sb.toString();
        
        //System.out.println( "THE RESULTS: " + response.toString() );
      }
      catch ( Exception e )
      {
        displayResults = "Error: " + e.toString();
        //log.error( e.getMessage() );
      }
      
      Object eventLock = Application.getApplication().getAppEventLock();
      synchronized ( eventLock ) // obtain lock for thread synchronization
      {
        resultObj.setText( displayResults );
      }        
    }      
  } 
  
  private class searchThread extends Thread
  {
    public String log;
    public String lat;
    
    public void run()
    {
      String displayResults = "no data";
      try
      {
        HttpClient httpClient = new HttpClient(  new HttpConnectionFactory() );
        String url = "https://api.foursquare.com/v2/venues/search?ll=" + log + "," + lat + "&limit=20&oauth_token=XXXX";
        StringBuffer responseSB = httpClient.doGet( url );
        
        JSONObject results = new JSONObject( responseSB.toString() );
        
        StringBuffer sb = new StringBuffer();
        
        JSONObject response = results.optJSONObject( "response" );
        if ( response != null )
        {
//          JSONObject checkins = response.optJSONObject( "checkins" );
//          if ( checkins != null )
          {
            JSONArray items = response.optJSONArray( "venues" );
            if ( items != null  )
            {
              int len = items.length();
              for ( int index = 0; index < len; index++ )
              {
                try
                {
                  sb.append( items.getJSONObject( index ).getString( "name" ) );
                  sb.append( " (" );
                  sb.append( items.getJSONObject( index ).getJSONObject( "location" ).getString( "lng" ) );
                  sb.append( "," );
                  sb.append( items.getJSONObject( index ).getJSONObject( "location" ).getString( "lat" ) );
                  sb.append( ") - " );
                  sb.append( items.getJSONObject( index ).getJSONObject( "location" ).getString( "distance" ) );
                  sb.append( "\n" );
                }
                catch ( JSONException e )
                {
                  Logger.logError( e.toString() );
                }
              }
            }
          }          
        }
      
        displayResults = sb.toString();
        
        //System.out.println( "THE RESULTS: " + response.toString() );
      }
      catch ( Exception e )
      {
        displayResults = "Error: " + e.toString();
        //log.error( e.getMessage() );
      }
      
      Object eventLock = Application.getApplication().getAppEventLock();
      synchronized ( eventLock ) // obtain lock for thread synchronization
      {
        resultObj.setText( displayResults );
      }        
    }      
  }   
}
