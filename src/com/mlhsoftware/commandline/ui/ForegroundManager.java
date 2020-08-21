package com.mlhsoftware.commandline.ui;


import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.container.VerticalFieldManager;


public class ForegroundManager extends VerticalFieldManager
{
  private int BACKGROUND_COLOR = Color.BLACK;

  public ForegroundManager()
  {
    super( USE_ALL_HEIGHT | VERTICAL_SCROLL | VERTICAL_SCROLLBAR | USE_ALL_WIDTH );
  }


  protected void paintBackground( Graphics graphics )
  {
    int mgrWidth = getWidth();
    int mgrHeight = getHeight();

    graphics.clear();
    int oldColor = graphics.getColor();
    try
    {
      graphics.setColor( BACKGROUND_COLOR );
      graphics.fillRect( 0, getVerticalScroll(), mgrWidth, mgrHeight );
    }
    finally
    {
      graphics.setColor( oldColor );
    }
    super.paintBackground( graphics );
  }  
}
