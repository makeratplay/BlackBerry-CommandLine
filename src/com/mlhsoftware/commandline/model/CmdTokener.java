package com.mlhsoftware.commandline.model;


public class CmdTokener
{

  /**
     * The index of the next character.
   */
  private int myIndex;


  /**
     * The source string being tokenized.
   */
  private String mySource;


  /**
     * Construct a JSONTokener from a string.
   *
   * @param s     A source string.
   */
  public CmdTokener( String s )
  {
    this.myIndex = 0;
    this.mySource = prepText(s);
  }

  private String prepText( String text )
  { 
    StringBuffer sb = new StringBuffer();
    if ( text != null )
    {
      int len = text.length();
      for ( int i = 0; i < len; i++ )
      {
        if ( text.charAt( i ) != '\n' )
        {
          sb.append( text.charAt( i ) );
        }
      }
    }
    return sb.toString();
  }  
  

  /**
     * Back up one character. This provides a sort of lookahead capability,
     * so that you can test for a digit or letter before attempting to parse
   * the next number or identifier.
   */
  public void back()
  {
    if ( this.myIndex > 0 )
    {
      this.myIndex -= 1;
    }
  }


  /**
     * Determine if the source string still contains characters that next()
     * can consume.
     * @return true if not yet at the end of the source.
     */
  public boolean more()
  {
    return this.myIndex < this.mySource.length();
  }


  /**
 * Get the next character in the source string.
 *
   * @return The next character, or 0 if past the end of the source string.
   */
  public char next()
  {
    if ( more() )
    {
      char c = this.mySource.charAt( this.myIndex );
      this.myIndex += 1;
      return c;
    }
    return 0;
  }

  /**
   * Get the next n characters.
   *
   * @param n     The number of characters to take.
   * @return      A string of n characters.
   * @throws FSONException
   *   Substring bounds error if there are not
   *   n characters remaining in the source string.
   */
  public String next( int n ) throws TokenException
  {
    int i = this.myIndex;
    int j = i + n;
    if ( j >= this.mySource.length() )
    {
      throw syntaxError( "Substring bounds error" );
    }
    this.myIndex += n;
    return this.mySource.substring( i, j );
  }


  /**
 * Get the next char in the string, skipping whitespace
   * and comments (slashslash, slashstar, and hash).
   * @throws FSONException
   * @return  A character, or 0 if there are no more characters.
   */
  public char nextClean() throws TokenException
  {
    for ( ; ; )
    {
      char c = next();
      if ( c == '/' )
      {
        switch ( next() )
        {
          case '/':
          do
          {
            c = next();
          } while ( c != '\n' && c != '\r' && c != 0 );
          break;
          case '*':
          for ( ; ; )
          {
            c = next();
            if ( c == 0 )
            {
              throw syntaxError( "Unclosed comment." );
            }
            if ( c == '*' )
            {
              if ( next() == '/' )
              {
                break;
              }
              back();
            }
          }
          break;
          default:
          back();
          return '/';
        }
      }
      else if ( c == '#' )
      {
        do
        {
          c = next();
        } while ( c != '\n' && c != '\r' && c != 0 );
      }
      else if ( c == 0 || c > ' ' )
      {
        return c;
      }
    }
  }

  /**
     * Return the characters up to the next close quote character.
     * Backslash processing is done. The formal JSON format does not
     * allow strings in single quotes, but an implementation is allowed to
     * accept them.
     * @param quote The quoting character, either
     *      <code>"</code>&nbsp;<small>(double quote)</small> or
     *      <code>'</code>&nbsp;<small>(single quote)</small>.
     * @return      A String.
     * @throws FSONException Unterminated string.
     */
  public String nextString( char quote ) throws TokenException
  {
    char c;
    StringBuffer sb = new StringBuffer();
    for ( ; ; )
    {
      c = next();
      switch ( c )
      {
        case 0:
        case '\n':
        case '\r':
        {
          throw syntaxError( "Unterminated string" );
        }
        case '\\':
        {
          c = next();
          switch ( c )
          {
            case 'b':
            {
              sb.append( '\b' );
              break;
            }
            case 't':
            {
              sb.append( '\t' );
              break;
            }
            case 'n':
            {
              sb.append( '\n' );
              break;
            }
            case 'f':
            {
              sb.append( '\f' );
              break;
            }
            case 'r':
            {
              sb.append( '\r' );
              break;
            }
            case 'u':
            {
              sb.append( (char)Integer.parseInt( next( 4 ), 16 ) );
              break;
            }
            case 'x':
            {
              sb.append( (char)Integer.parseInt( next( 2 ), 16 ) );
              break;
            }
            default:
            {
              sb.append( c );
              break;
            }
          }
          break;
        }
        default:
        {
          if ( c == quote )
          {
            return sb.toString();
          }
          sb.append( c );
        }
      }
    }
  }

  /**
   * Return the characters up to the next } or {.
   * Backslash processing is done. 
   * @return      A String.
   * @throws FSONException Unterminated string.
   */
  public String getCmd() throws TokenException
  {
    return nextValue();
  }  
    

 

  /**
     * Get the text up but not including the specified character or the
     * end of line, whichever comes first.
     * @param  d A delimiter character.
     * @return   A string.
     */
  public String nextTo( char d )
  {
    StringBuffer sb = new StringBuffer();
    for ( ; ; )
    {
      char c = next();
      if ( c == d || c == 0 || c == '\n' || c == '\r' )
      {
        if ( c != 0 )
        {
          back();
        }
        return sb.toString().trim();
      }
      sb.append( c );
    }
  }


  /**
     * Get the text up but not including one of the specified delimeter
     * characters or the end of line, whichever comes first.
     * @param delimiters A set of delimiter characters.
     * @return A string, trimmed.
     */
  public String nextTo( String delimiters )
  {
    char c;
    StringBuffer sb = new StringBuffer();
    for ( ; ; )
    {
      c = next();
      if ( delimiters.indexOf( c ) >= 0 || c == 0 ||
              c == '\n' || c == '\r' )
      {
        if ( c != 0 )
        {
          back();
        }
        return sb.toString().trim();
      }
      sb.append( c );
    }
  }


  public String nextValue() throws TokenException
  {
    char c = nextClean();
    String retVal;

    if ( c == '"' || c == '\'' )
    {
      retVal = nextString( c );
    }
    else
    {
      StringBuffer sb = new StringBuffer();
      while ( c != ' ' && c != 0 && c != '\n'  )
      {
        sb.append( c );
        c = next();
      }
      //back();
      retVal = sb.toString();
    }
    return retVal;
  }


  /**
     * Skip characters until the next character is the requested character.
     * If the requested character is not found, no characters are skipped.
     * @param to A character to skip to.
     * @return The requested character, or zero if the requested character
     * is not found.
 */
  public char skipTo( char to )
  {
    char c;
    int index = this.myIndex;
    do
    {
      c = next();
      if ( c == 0 )
      {
        this.myIndex = index;
        return c;
      }
    } while ( c != to );
    back();
    return c;
  }


  /**
     * Skip characters until past the requested string.
     * If it is not found, we are left at the end of the source.
     * @param to A string to skip past.
     */
  public void skipPast( String to )
  {
    this.myIndex = this.mySource.indexOf( to, this.myIndex );
    if ( this.myIndex < 0 )
    {
      this.myIndex = this.mySource.length();
    }
    else
    {
      this.myIndex += to.length();
    }
  }


  /**
     * Make a JSONException to signal a syntax error.
     *
     * @param message The error message.
     * @return  A JSONException object, suitable for throwing
     */
  public TokenException syntaxError( String message )
  {
    return new TokenException( message + toString() );
  }


  /**
     * Make a printable string of this JSONTokener.
     *
     * @return " at character [this.myIndex] of [this.mySource]"
     */
  public String toString()
  {
    return this.mySource;
  }
  
  public class TokenException extends Exception 
  {
      private Throwable cause;

      /**
       * Constructs a JSONException with an explanatory message.
       * @param message Detail about the reason for the exception.
       */
      public TokenException(String message) 
      {
          super(message);
      }

      public TokenException(Throwable t) 
      {
          super(t.getMessage());
          this.cause = t;
      }

      public Throwable getCause() 
      {
          return this.cause;
      }
  }  
}