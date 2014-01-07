package org.frogpeak.horn;

import java.awt.*;

/** A Panel with adjustable top/bottom insets value.
 *  Used to hold a Scrollbar in the Slider class.
 *
 *  Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted. 
 */

public class ScrollbarPanel extends Panel {
  private Insets insets;

  public ScrollbarPanel(int margins) {
    setLayout(new BorderLayout());
    setMargins(margins);
  }

  public Insets insets() {
    return(insets);
  }

  public int getMargins() {
    return(insets.top);
  }

  public void setMargins(int margins) {
    this.insets = new Insets(margins, 0, margins, 0);
  }
}