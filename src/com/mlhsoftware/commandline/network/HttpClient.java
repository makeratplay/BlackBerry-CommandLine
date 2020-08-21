package com.mlhsoftware.commandline.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.lang.StringBuffer;
import net.rim.device.api.util.DataBuffer;

import javax.microedition.io.HttpConnection;
import net.rim.device.api.io.http.HttpHeaders;
import net.rim.device.api.io.http.HttpProtocolConstants;

import net.rim.blackberry.api.browser.URLEncodedPostData;

import javax.microedition.io.file.*;
import javax.microedition.io.*;
import java.io.*;

public class HttpClient
{

  private HttpConnectionFactory factory;

  public HttpClient( HttpConnectionFactory pFactory )
  {
    factory = pFactory;
  }

  public StringBuffer doGet( String url ) throws Exception
  {
    return doGet( url, null, factory );
  }

  public StringBuffer doGet( String url, Hashtable args ) throws Exception
  {
    return doGet( url, args, factory );
  }

  public StringBuffer doGet( String url, Hashtable args, HttpConnectionFactory factory ) throws Exception
  {
    StringBuffer urlBuffer = new StringBuffer( url );

    if ( ( args != null ) && ( args.size() > 0 ) )
    {
      urlBuffer.append( '?' );
      Enumeration keysEnum = args.keys();

      while ( keysEnum.hasMoreElements() )
      {
        String key = (String)keysEnum.nextElement();
        String val = (String)args.get( key );
        urlBuffer.append( key ).append( '=' ).append( val );

        if ( keysEnum.hasMoreElements() )
        {
          urlBuffer.append( '&' );
        }
      }
    }

    return doGet( urlBuffer.toString(), factory );
  }

  /**
         * Perform GET operation.
         * 
         * @param url
         *            the GET URL.
         * @return response.
         * @throws Exception
         *             when any error occurs.
         */
  public StringBuffer doGet( String url, HttpConnectionFactory factory ) throws Exception
  {
    HttpConnection connection = null;
    StringBuffer buffer = new StringBuffer();

    try
    {
      if ( ( url == null ) || url.equalsIgnoreCase( "" ) || ( factory == null ) )
      {
        return null;
      }
      connection = factory.getHttpConnection( url );

      switch ( connection.getResponseCode() )
      {

        case HttpConnection.HTTP_OK:
        {
          InputStream inputStream = connection.openInputStream();
          int c;

          while ( ( c = inputStream.read() ) != -1 )
          {
            buffer.append( (char)c );
          }

          inputStream.close();
          break;
        }

        case HttpConnection.HTTP_TEMP_REDIRECT:
        case HttpConnection.HTTP_MOVED_TEMP:
        case HttpConnection.HTTP_MOVED_PERM:
        {
          url = connection.getHeaderField( "Location" );
          buffer = doGet( url, factory );
          break;
        }

        default:
        break;
      }
    }
    catch ( Exception e )
    {
      throw e;
    }
    finally
    {
      if ( connection != null )
      {
        try
        {
          connection.close();
        }
        catch ( IOException e )
        {
        }
      }
    }

    return buffer;
  }

  /**
         * Perform POST operation.
         * 
         * @param url
         *            the POST url.
         * @param data
         *            the data to be POSTed
         * @return response.
         * @throws Exception
         *             when any error occurs.
         */

  public StringBuffer doPost( String url, Hashtable data ) throws Exception
  {
    return doPost( url, data, null );
  }

  public StringBuffer doPost( String url, Hashtable data, byte[] imageData ) throws Exception
  {
    return doPost( url, data, factory, imageData );
  }

  public StringBuffer doPost( String url, Hashtable data, HttpConnectionFactory factory, byte[] imageData ) throws Exception
  {
    URLEncodedPostData encoder = new URLEncodedPostData( "UTF-8", false );


    HttpHeaders headers = null;
    byte[] byteData = null;
    if ( imageData != null )
    {
      //url = "http://hawkins.is-a-geek.com";

      DataBuffer dataBuffer = new DataBuffer();
      //dataBuffer.setBigEndian( false );

      String boundary = "----WebKitFormBoundarywmF6oZWgnAAxOsY5"; // + System.currentTimeMillis();
      String separator = "--" + boundary;
      String endofpost = "--" + boundary + "--";
      headers = new HttpHeaders();

      // access_token feild
      dataBuffer.write( separator.getBytes() );
      dataBuffer.write( ( "\r\n" ).getBytes() );
      dataBuffer.write( ( "Content-Disposition: form-data; name=\"access_token\"" ).getBytes() );
      dataBuffer.write( ( "\r\n" ).getBytes() );
      dataBuffer.write( ( "\r\n" ).getBytes() );
      dataBuffer.write( ( "103785996360240|f0b782e642d8ab853e12145f-1388555447|Scy0jOTkyFTHsfYD_XJlLQVhYdw" ).getBytes() );
      dataBuffer.write( ( "\r\n" ).getBytes() );

      // message feild
      dataBuffer.write( separator.getBytes() );
      dataBuffer.write( ( "\r\n" ).getBytes() );
      dataBuffer.write( ( "Content-Disposition: form-data; name=\"message\"" ).getBytes() );
      dataBuffer.write( ( "\r\n" ).getBytes() );
      dataBuffer.write( ( "\r\n" ).getBytes() );
      dataBuffer.write( ( "test from bb app" ).getBytes() );
      dataBuffer.write( ( "\r\n" ).getBytes() );

      // image file feild
      dataBuffer.write( separator.getBytes() );
      dataBuffer.write( ( "\r\n" ).getBytes() );
      dataBuffer.write( ( "Content-Disposition: form-data; name=\"files\"; filename=\"wallpaper.png\"" ).getBytes() );
      dataBuffer.write( ( "\r\n" ).getBytes() );
      dataBuffer.write( ( "Content-Type: image/png" ).getBytes() );
      dataBuffer.write( ( "\r\n" ).getBytes() );
      //dataBuffer.write( ( "\r\n" ).getBytes() );
      dataBuffer.write( imageData );

      dataBuffer.write( separator.getBytes() );
      dataBuffer.write( endofpost.getBytes() );
      byteData = dataBuffer.toArray();


      try
      {
        String PHOTO_DIR = System.getProperty( "fileconn.dir.photos" );
        String fileName = PHOTO_DIR + "test.dat";

        FileConnection fconn = (FileConnection)Connector.open( fileName, Connector.READ_WRITE );
        if ( !fconn.exists() )
        {
          fconn.create();
        }
        OutputStream out = fconn.openOutputStream();
        out.write( byteData );
        out.flush();
        out.close();

        fconn.close();
      }
      catch ( Exception e )
      {
      }


      headers.addProperty( HttpProtocolConstants.HEADER_CONTENT_LENGTH, String.valueOf( byteData.length ) );
      headers.addProperty( HttpProtocolConstants.HEADER_CONTENT_TYPE, "multipart/form-data; boundary=" + boundary );

      //log.debug( "buffer = Content-Length: " + String.valueOf( byteData.length ) );
    }
    else
    {
      Enumeration keysEnum = data.keys();

      while ( keysEnum.hasMoreElements() )
      {
        String key = (String)keysEnum.nextElement();
        String val = (String)data.get( key );
        encoder.append( key, val );
      }
      byteData = encoder.getBytes();
    }

    return doPost( url, headers, byteData, factory );
  }

  /**
 * Perform POST operation.
 * 
 * @param url
 *            the POST url.
 * @param data
 *            the data to be POSTed
 * @return response.
 * @throws Exception
 *             when any error occurs.
 */
  public StringBuffer doPost( String url, HttpHeaders headers, byte[] data, HttpConnectionFactory factory ) throws Exception
  {
    HttpConnection connection = null;
    StringBuffer buffer = new StringBuffer();

    try
    {
      if ( ( url == null ) || url.equalsIgnoreCase( "" ) || ( factory == null ) )
      {
        return null;
      }
      connection = factory.getHttpConnection( url, headers, data );

      switch ( connection.getResponseCode() )
      {

        case HttpConnection.HTTP_OK:
        {
          InputStream inputStream = connection.openInputStream();
          int c;

          while ( ( c = inputStream.read() ) != -1 )
          {
            buffer.append( (char)c );
          }

          inputStream.close();
          break;
        }

        case HttpConnection.HTTP_TEMP_REDIRECT:
        case HttpConnection.HTTP_MOVED_TEMP:
        case HttpConnection.HTTP_MOVED_PERM:
        {
          url = connection.getHeaderField( "Location" );
          buffer = doPost( url, headers, data, factory );
          break;
        }

        default:
        {
          InputStream inputStream = connection.openInputStream();
          int c;

          while ( ( c = inputStream.read() ) != -1 )
          {
            buffer.append( (char)c );
          }

          inputStream.close();
          break;
        }
      }
    }
    catch ( Exception e )
    {
      throw e;
    }
    finally
    {
      if ( connection != null )
      {
        try
        {
          connection.close();
        }
        catch ( IOException e )
        {
        }
      }
    }

    return buffer;
  }

 

}
