/*
 * EditFieldManager.java
 *
 * MLH Software
 * Copyright 2010
 */

package com.mlhsoftware.commandline.ui;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BasicEditField;

/**
 * 
 */
public class CommandField extends BasicEditField
{

  public CommandField(String prompt, String text )
  {
    super( prompt, text );
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
