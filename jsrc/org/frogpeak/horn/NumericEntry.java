package org.frogpeak.horn;

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
	Label label;
	CustomTextFieldDouble textField;
	/**
	 * @param string description of field
	 * @param d initial valud
	 * @param i num characters
	 */
	public NumericEntry(String string, double d, double pMin, double pMax )
	{
		setLayout(new GridLayout(1, 2));
		add(label = new Label(string, Label.RIGHT));
		add(textField = new CustomTextFieldDouble(d, pMin, pMax ));

		textField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					valueChanged(textField.getDoubleValue());
				} catch( NumberFormatException exc ) {
					textField.setText("error");
				}
			}
		});
	}

	public void valueChanged(double value)
	{
	}

}
