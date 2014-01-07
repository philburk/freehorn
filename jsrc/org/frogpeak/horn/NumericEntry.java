package org.frogpeak.horn;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Phil Burk (C) 2003
 */
public class NumericEntry extends Panel
{
	//deprecated all
	
	
	Label label;
	//CustomTextFieldDouble textField;
	/**
	 * @param string description of field
	 * @param d initial valud
	 * @param i num characters
	 */
	public NumericEntry(String string, double d, double pMin, double pMax )
	{
		setLayout(new GridLayout(1, 1));
		//setPreferredSize(new Dimension(100, 50));
		//add(label = new Label(string, Label.LEFT), Component.BOTTOM_ALIGNMENT);
		
		//add(textField = new CustomTextFieldDouble(d, pMin, pMax ));
		//Slider slider = new Slider(pMin, pMax, d, string);
		//add(slider);

//		textField.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent e)
//			{
//				try
//				{
//					valueChanged(textField.getDoubleValue());
//				} catch( NumberFormatException exc ) {
//					textField.setText("error");
//				}
//			}
//		});
	}

	public void valueChanged(double value)
	{
	}

}
