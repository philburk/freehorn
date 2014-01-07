package org.frogpeak.horn;

import java.awt.*;
import java.awt.event.*;

/** A class that combines a horizontal Scrollbar and a TextField
 *  (to the right of the Scrollbar). The TextField shows the
 *  current scrollbar value, plus, if setEditable(true) is set,
 *  it can be used to change the value as well.
 *
 *  Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted. 
 */

public class Slider extends Panel implements ActionListener,
AdjustmentListener {
	private Scrollbar scrollbar;
	private Label label;
	private TextField textfield;
	private Panel textfieldPanel;
	private ScrollbarPanel scrollbarPanel;
	private int preferredWidth = 250;
	private double min;
	private double max;
	private double scalar;
	private int orientation;
	public int identification;
//	private boolean integerFlag = false;


	/** Construct a slider with the specified min, max,and initial
	 *  values, plus the specified "bubble" (thumb) value. This
	 *  bubbleSize should be specified in the units that min and
	 *  max use, not in pixels. Thus, if min is 20 and max is 320,
	 *  then a bubbleSize of 30 is 10% of the visible range.
	 */

	public Slider(String name, double initialValue, double minValue, double maxValue, int orient) {
		setLayout(new BorderLayout());
		//maxValue = maxValue + bubbleSize;
		orientation = orient;
		min = minValue;
		max = maxValue;
		scalar = (int) ((max - min) * 1000.);
		if(name.equals("Num Harmonics")){
			scalar = (int) ((max - min) * 1.);
		}
		int initialScrollBarValue = (int) ((initialValue-min)/(max - min)*(scalar-1));
		//System.out.println(initialScrollBarValue);
		if(orientation == 0){
			scrollbar = new Scrollbar(Scrollbar.HORIZONTAL,
					initialScrollBarValue + 1, 1, 0, (int) scalar + 1);
		} else {
			scrollbar = new Scrollbar(Scrollbar.VERTICAL,
					initialScrollBarValue + 1, 1, 0, (int) scalar + 1);
		}
		scrollbar.addAdjustmentListener(this);
		scrollbarPanel = new ScrollbarPanel(6);
		scrollbarPanel.add(scrollbar, BorderLayout.CENTER);
		add(scrollbarPanel, BorderLayout.CENTER);
		textfieldPanel = new Panel();
		if(orientation == 0){
			textfield = new TextField(7);
		} else {
			textfield = new TextField(3);
		}
		textfield.addActionListener(this);
		textfield.setEditable(true);
		//textfield.setMaximumSize(new Dimension(numDigits(2048) + 1, 0));
		//setTextFieldValue();
		textfieldPanel.add(textfield);
		if(orientation == 0){
			add(textfieldPanel, BorderLayout.EAST);
		} else{
			add(textfieldPanel, BorderLayout.SOUTH);
		}
		add(new Label(name), BorderLayout.NORTH);
	}
	
	public Slider(String name, double initialValue, double minValue, double maxValue, int orient, int id) {
		identification = id;
		setLayout(new BorderLayout());
		//maxValue = maxValue + bubbleSize;
		orientation = orient;
		min = minValue;
		max = maxValue;
		scalar = (int) ((max - min) * 1000.);
		if(name.equals("Num Harmonics")){
			scalar = (int) ((max - min) * 1.);
		}
		int initialScrollBarValue = (int) ((initialValue-min)/(max - min)*(scalar-1));
		//System.out.println(initialScrollBarValue);
		if(orientation == 0){
			scrollbar = new Scrollbar(Scrollbar.HORIZONTAL,
					initialScrollBarValue + 1, 1, 0, (int) scalar + 1);
		} else {
			scrollbar = new Scrollbar(Scrollbar.VERTICAL,
					initialScrollBarValue + 1, 1, 0, (int) scalar + 1);
		}
		scrollbar.addAdjustmentListener(this);
		scrollbarPanel = new ScrollbarPanel(6);
		scrollbarPanel.add(scrollbar, BorderLayout.CENTER);
		add(scrollbarPanel, BorderLayout.CENTER);
		textfieldPanel = new Panel();
		if(orientation == 0){
			textfield = new TextField(7);
		} else {
			textfield = new TextField(3);
		}
		textfield.addActionListener(this);
		textfield.setEditable(true);
		//textfield.setMaximumSize(new Dimension(numDigits(2048) + 1, 0));
		//setTextFieldValue();
		textfieldPanel.add(textfield);
		if(orientation == 0){
			add(textfieldPanel, BorderLayout.EAST);
		} else{
			add(textfieldPanel, BorderLayout.SOUTH);
		}
		label = new Label(name);
		add(label, BorderLayout.NORTH);
		textfield.setVisible(false);
		setFontSize(9);
	}
	
//	public void setIntegerFlag(boolean value) {
//		integerFlag = value;
//	}

	/** A place holder to override for action to be taken when
	 *  scrollbar changes.
	 */

	public void doAction(int value) {
	}

	public void valueChanged(double value)
	{
	}

	/** When textfield changes, sets the scrollbar */

	public void actionPerformed(ActionEvent event) {
		String value = textfield.getText();
		double oldValue = getValue();
		try {
			double val = Double.parseDouble(value.trim());
			setValue(val);
			valueChanged(val);
		} catch(NumberFormatException nfe) {
			setValue(oldValue);
		}
	}

	/** When scrollbar changes, sets the textfield */

	public void adjustmentValueChanged(AdjustmentEvent event) {
		setTextFieldValue();
		doAction(scrollbar.getValue());
	}

	/** Returns the Scrollbar part of the Slider. */

	public Scrollbar getScrollbar() {
		return(scrollbar);
	}

	/** Returns the TextField part of the Slider */

	public TextField getTextField() {
		return(textfield);
	}

	/** Changes the preferredSize to take a minimum width, since
	 *  super-tiny scrollbars are hard to manipulate.
	 */

	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.height = textfield.getPreferredSize().height;
		d.width = Math.max(d.width, preferredWidth);
		return(d);
	}

	/** This just calls preferredSize */

	public Dimension getMinimumSize() {
		return(getPreferredSize());
	}

	/** To keep scrollbars legible, a minimum width is set. This
	 *  returns the current value (default is 150).
	 */

	public int getPreferredWidth() {
		return(preferredWidth);
	}

	/** To keep scrollbars legible, a minimum width is set. This
	 *  sets the current value (default is 150).
	 */

	public void setPreferredWidth(int preferredWidth) {
		this.preferredWidth = preferredWidth;
	}

	/** This returns the current scrollbar value */

	public double getValue() {
		double value;
		if(orientation == 0){
			value = (scrollbar.getValue()/ (scalar)  * (max-min)+min);
		} else {
			value = Math.abs((scrollbar.getValue()/ (scalar)  * (max-min)+min) - 1);
		}
		return value;
	}

	/** This assigns the scrollbar value. If it is below the
	 *  minimum value or above the maximum, the value is set to
	 *  the min and max value, respectively.
	 */

	public void setValue(double value) {
		if(orientation == 0){
			scrollbar.setValue((int) ((value-min)/(max - min)*(scalar)));
		} else {
			scrollbar.setValue((int) ((Math.abs(value-1)-min)/(max - min)*(scalar)));
		}
		setTextFieldValue();
	}

	/** Sometimes horizontal scrollbars look odd if they are very
	 *  tall. So empty top/bottom margins can be set. This returns
	 *  the margin setting. The default is four.
	 */

	public int getMargins() {
		return(scrollbarPanel.getMargins());
	}

	/** Sometimes horizontal scrollbars look odd if they are very
	 *  tall. So empty top/bottom margins can be set. This sets
	 *  the margin setting.
	 */

	public void setMargins(int margins) {
		scrollbarPanel.setMargins(margins);
	}

	/** Returns the current textfield string. In most cases this
	 *  is just the same as a String version of getValue, except
	 *  that there may be padded blank spaces at the left.
	 */

	public String getText() {
		return(textfield.getText());
	}

	/** This sets the TextField value directly. Use with extreme
	 *  caution since it does not right-align or check if value
	 *  is numeric.
	 */

	public void setText(String text) {
		textfield.setText(text);
	}

	/** Returns the Font being used by the textfield.
	 *  Courier bold 12 is the default.
	 */

	public Font getFont() {
		return(textfield.getFont());
	}

	/** Changes the Font being used by the textfield. */

	public void setFont(Font textFieldFont) {
		textfield.setFont(textFieldFont);
		label.setFont(textFieldFont);
	}

	/** The size of the current font */

	public int getFontSize() {
		return(getFont().getSize());
	}

	/** Rather than setting the whole font, you can just set the
	 *  size (Monospaced bold will be used for the family/face).
	 */

	public void setFontSize(int size) {
		setFont(new Font("Monospaced", Font.BOLD, size));
	}

	/** Determines if the textfield is editable. If it is, you can
	 *  enter a number to change the scrollbar value. In such a
	 *  case, entering a value outside the legal range results in
	 *  the min or max legal value. A non-integer is ignored.
	 */

	public boolean isEditable() {
		return(textfield.isEditable());
	}

	/** Determines if you can enter values directly into the
	 *  textfield to change the scrollbar.
	 */

	public void setEditable(boolean editable) {
		textfield.setEditable(editable);
	}
	
	public void setMax(double m) {
		double oldValue = getValue();
		max = m;
		scalar = (int) ((max - min) * 1000.);
		scrollbar.setMaximum((int) (scalar + 1));
		//scrollbar.setValue((int) ((getValue()-min)/(max - min)*(scalar-1)));
		setTextFieldValue();
		//setValue(oldValue);
	}
	
	public void setMin(double m) {
		double oldValue = getValue();
		min = m;
		//scalar = (int) ((max - min) * 1000.);
		setValue(oldValue);
	}

	// Sets a right-aligned textfield number.

	public void setTextFieldValue() {
		double value = getValue();
		//int digits = numDigits(4096);
		//String valueString = padString(value, digits);
		
		String valueString = "";
		
		char[] result = String.valueOf(value).toCharArray();
		
		int i = 0;
		while(result[i] != '.'){
			valueString = valueString + result[i];
			i++;
		}
		
		valueString = valueString + result[i];
		i++;
		
		for(int j = i; j < i + 3; j++){
			//System.out.println(j);
			
			if(result.length > j){
				valueString = valueString + result[j];
			}
			
		}
		
		
		textfield.setText(valueString);
		valueChanged(value);
	}

	// Repeated String concatenation is expensive, but this is
	// only used to add a small amount of padding, so converting
	// to a StringBuffer would not pay off.

	//  private String padString(double value, int digits) {
	//    String result = String.valueOf(value);
	//    for(int i=result.length(); i<5; i++) {
	//      result = " " + result;
	//    }
	//    return(result + " ");
	//  }

	// Determines the number of digits in a decimal number.

	//  private static final double LN10 = Math.log(10.0);
	//
	//  private static int numDigits(double maxValue) {
	//    return(1 + (int)Math.floor(Math.log((double)maxValue)/LN10));
	//  }
}