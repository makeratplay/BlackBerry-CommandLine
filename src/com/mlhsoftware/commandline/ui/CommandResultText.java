/*
 * ListStyleLabelField.java
 *
 * MLH Software
 * Copyright 2010
 */

package com.mlhsoftware.commandline.ui;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.RichTextField;


public class CommandResultText extends RichTextField
{
  public CommandResultText( String text )
  {
    super( text, Field.NON_FOCUSABLE );
  }

  protected void paint( Graphics g )
  {
    // change font to grey
    int currentColor = g.getColor();
    g.setColor( Color.WHITE );
    super.paint( g );
    g.setColor( currentColor );
  }
}



