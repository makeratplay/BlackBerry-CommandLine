
package com.mlhsoftware.commandline.RESTAPI;

import java.io.IOException;
import java.util.Hashtable;

import net.rim.device.api.collection.util.SortedReadableList;
import net.rim.device.api.crypto.MD5Digest;
import net.rim.device.api.util.StringComparator;

import org.json.me.*;

import com.mlhsoftware.commandline.network.HttpClient;
import com.mlhsoftware.commandline.network.HttpConnectionFactory;

public class RestClient
{

  /**
         * URL to REST server.
   */
  private String url = null;

  /**
         * Application Key.
   */
  private String applicationKey = null;

  /**
         * Session key.
   */
  private String sessionKey = null;

  /**
         * Secret key. Can either be the Application secret or the session secret.
   */
  private String secretKey = null;

  private String redirectUri = null;

  private HttpConnectionFactory factory;

  private HttpClient httpClient;


  /**
         * Create a REST client given its URL and application key.
         * 
         * @param url
         *            the URL to the REST server.
   * @param applicationKey
   *            the application key.
   * @param secretKey
   *            the secret key used to calculate signature.
   */
  public RestClient( String url, String applicationKey, String secretKey, String redirectUri, HttpConnectionFactory pFactory )
  {
    this.url = url;
    this.applicationKey = applicationKey;
    this.secretKey = secretKey;
    this.redirectUri = redirectUri;
    factory = pFactory;
    httpClient = new HttpClient( factory );
  }

  /**
         * Obtain the REST server's URL.
         * 
         * @return the URL.
         */
  public String getUrl()
  {
    return url;
  }

  /**
         * Change the REST server's URL.
         * 
         * @param url
         *            the new URL.
         */
  public void setUrl( String url )
  {
    this.url = url;
  }

  /**
         * Obtain the REST server's URL.
         * 
         * @return the URL.
         */
  public String getRedirectUri()
  {
    return redirectUri;
  }

  /**
         * Change the REST server's URL.
         * 
         * @param url
         *            the new URL.
         */
  public void setRedirectUri( String redirectUri )
  {
    this.redirectUri = redirectUri;
  }

  /**
   * Obtain the application key.
         * 
         * @return the application key.
         */
  public String getApplicationKey()
  {
    return applicationKey;
  }

  /**
   * Change the application key.
   * 
         * @param applicationKey
         *            the application key.
         */
  public void setApplicationKey( String applicationKey )
  {
    this.applicationKey = applicationKey;
  }

  /**
   * Obtain the session key.
   * 
   * @return the session key.
         */
  public String getSessionKey()
  {
    return sessionKey;
  }

  /**
   * Change the session key.
   * 
   * @param sessionKey
   *            the new session key.
         */
  public void setSessionKey( String sessionKey )
  {
    this.sessionKey = sessionKey;
  }

  /**
   * Obtain the secret key used to calculate the signature.
   * 
   * @return the secret key.
   */
  public String getSecretKey()
  {
    return secretKey;
  }

  /**
         * Change the secret key used to calculate the signature.
         * 
   * @param secretKey
   *            the new secret key.
   */
  public void setSecretKey( String secretKey )
  {
    this.secretKey = secretKey;
  }

  /**
         * Send request to the REST server given data.
         * 
   * @param data
   *            the data to send.
   * @return response from the REST server as a JSON object.
   * @throws RestException
   *             when encounters any error.
         */
  public JSONObject send( Hashtable data ) throws RestException
  {
    try
    {
      if ( sessionKey != null )
      {
        data.put( "session_key", sessionKey );
      }

      data.put( "api_key", applicationKey );
      data.put( "call_id", String.valueOf( System.currentTimeMillis() ) );
      data.put( "v", "1.0" );
      data.put( "format", "JSON" );
      data.put( "sig", getSignature( data, secretKey ) );

      StringBuffer response = httpClient.doPost( url, data );

      if ( response.length() == 0 )
      {
        throw new RestException( "Empty response" );
      }

      return new JSONObject( new JSONTokener( response.toString() ) );
    }
    catch ( RestException e )
    {
      throw e;
    }
    catch ( Exception e )
    {
      throw new RestException( e.getMessage() );
    }
  }

  /**
         * Calculate the signature given arguments and secret key.
         * 
         * @param arguments
         *            the supplied arguments.
         * @param secret
         *            the secret key.
         * @return the signature string.
         */
  private static String getSignature( Hashtable arguments, String secret )
  {
    try
    {
      SortedReadableList keysList = new SortedReadableList( StringComparator.getInstance( true ) );
      keysList.loadFrom( arguments.keys() );
      keysList.sort();
      StringBuffer requestString = new StringBuffer();

      for ( int i = 0; i < keysList.size(); i++ )
      {
        String key = (String)keysList.getAt( i );
        String val = (String)arguments.get( key );
        requestString.append( key + "=" + val );
      }

      requestString.append( secret );

      MD5Digest digest = new MD5Digest();
      digest.update( requestString.toString().getBytes( "iso-8859-1" ), 0, requestString.length() );
      byte[] digestResult = digest.getDigest();

      return convertToHex( digestResult );
    }
    catch ( IOException e )
    {
      return null;
    }
  }

  /**
         * Convert binary data to its Hex string.
         * 
         * @param data
         *            the binary data to convert.
         * @return the equivalent Hex string.
         */
  private static String convertToHex( byte[] data )
  {
    StringBuffer buf = new StringBuffer();

    for ( int i = 0; i < data.length; i++ )
    {
      int halfbyte = ( data[i] >>> 4 ) & 0x0F;
      int two_halfs = 0;

      do
      {
        if ( ( 0 <= halfbyte ) && ( halfbyte <= 9 ) )
        {
          buf.append( (char)( '0' + halfbyte ) );
        }
        else
        {
          buf.append( (char)( 'a' + ( halfbyte - 10 ) ) );
        }

        halfbyte = data[i] & 0x0F;
      } while ( two_halfs++ < 1 );
    }

    return buf.toString();
  }

}
