
package com.mlhsoftware.commandline.oAuth;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.LabelField;
import org.json.me.*;

import com.mlhsoftware.commandline.RESTAPI.FourSquareClient;
import com.mlhsoftware.commandline.model.Action;
import com.mlhsoftware.commandline.model.ActionListener;
import com.mlhsoftware.commandline.network.CookieManager;
import com.mlhsoftware.commandline.network.HttpClient;
import com.mlhsoftware.commandline.network.HttpConnectionFactory;

public class FourSquareLoginScreen extends BrowserScreen // implements ActionListener
{
  public static final String ACTION_WEB_RESPONSE = "webResponse";
  public static final String ACTION_LOGGED_IN = "loggedIn";
  public static final String ACTION_ERROR = "error";

  private FourSquareClient restClient = null;
  private HttpClient httpClient = null;

  private String url;

  public FourSquareLoginScreen( FourSquareClient restClient, HttpConnectionFactory factory, CookieManager cookieManager, String title )
  {
    super( new StringBuffer().append( "https://foursquare.com/oauth2/authenticate?" ).append( "client_id=" ).append( restClient.getApplicationKey() ).append( '&' ).append( "response_type=code" ).append( '&' ).append( "redirect_uri=" ).append( restClient.getRedirectUri() ).append("&display=touch").toString() );
    
//    super( new StringBuffer().append( "https://foursquare.com/oauth2/authenticate?" ).append( "client_id=" ).append( restClient.getApplicationKey() ).append( '&' ).append( "response_type=code" ).append( '&' ).append( "redirect_uri=" ).append( restClient.getRedirectUri() ).append("&display=touch").toString(), factory, cookieManager, title );
    System.out.println( "URL: " + url );
    this.restClient = restClient;
//    addActionListener( this );
    //foreground.add( new LabelField( "Accessing Foursquare..." ) );

    
    this.httpClient = new HttpClient( factory );


  }
/*
  public void login()
  {
    browse();
  }

  public void onAction( Action event )
  {
    if ( event.getSource() == this )
    {
      String tmpUrl =  getUrl();
      
      if ( event.getAction().equals( ACTION_SUCCESS ) && getUrl().startsWith( restClient.getRedirectUri() ) )
      {
        String url = getUrl();
        int startIndex = url.indexOf( "code" );

        if ( startIndex > -1 )
        {
          int stopIndex = url.length();

          if ( url.indexOf( '&', startIndex ) > -1 )
          {
            stopIndex = url.indexOf( '&', startIndex );
          }
          else if ( url.indexOf( ';', startIndex ) > -1 )
          {
            stopIndex = url.indexOf( ';', startIndex );
          }

          String code = url.substring( url.indexOf( '=', startIndex ) + 1, stopIndex );
          fetchAccessToken( code );
        }
      }
      else if ( event.getAction().equals( ACTION_SUCCESS ) && getUrl().startsWith( "https://foursquare.com/oauth2/access_token" )  )
      {
        try
        {
          StringBuffer responseBuffer = (StringBuffer)event.getData();
          if ( responseBuffer != null )
          {
            JSONObject response = new JSONObject( responseBuffer.toString() );
            String authToken = response.getString( "access_token" );
            fireAction( ACTION_LOGGED_IN, authToken );
          }
        }
        catch ( Exception e )
        {
          System.out.println( "Exception in onAction" + ".\nError Exception: " + e.toString() );
        }
      }
    }
  }

  private void fetchAccessToken( String code )
  {
    StringBuffer urlBuffer = new StringBuffer();
    urlBuffer.append( "https://foursquare.com/oauth2/authenticate?" );
    urlBuffer.append( "client_id=" ).append( restClient.getApplicationKey() );
    urlBuffer.append( '&' );
    urlBuffer.append( "client_secret=" ).append( restClient.getSecretKey() );
    urlBuffer.append( '&' );
    urlBuffer.append( "grant_type=authorization_code" );
    urlBuffer.append( '&' );
    urlBuffer.append( "redirect_uri=" ).append( restClient.getRedirectUri() );
    urlBuffer.append( '&' );
    urlBuffer.append( "code=" ).append( code );

    url = urlBuffer.toString();
    ( new PrimaryFetchThread() ).start();
  }

  private class PrimaryFetchThread extends Thread
  {
    public void run()
    {
      try
      {
        final StringBuffer response = httpClient.doGet( url );

        UiApplication.getUiApplication().invokeLater( new Runnable() { public void run() { fireAction( ACTION_WEB_RESPONSE, response ); } } );
      }
      catch ( Exception e )
      {
        System.out.println( "Exception in PrimaryFetchThread" +
                   ".\nError Exception: " + e.toString() );
      }
    }
  }
*/
}
