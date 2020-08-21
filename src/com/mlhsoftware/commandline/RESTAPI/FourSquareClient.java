
package com.mlhsoftware.commandline.RESTAPI;

import com.mlhsoftware.commandline.network.HttpConnectionFactory;



public class FourSquareClient extends RestClient
{
  static final String URL = "https://api.foursquare.com/v2";

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
  public FourSquareClient( String applicationKey, String secretKey, String redirectUri, HttpConnectionFactory pFactory )
  {
    super( URL, applicationKey, secretKey, redirectUri, pFactory );
  }

  
}
