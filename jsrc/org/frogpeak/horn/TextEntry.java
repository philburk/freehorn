package org.frogpeak.horn;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Phil Burk (C) 2003
 */
public class TextEntry extends Panel
{
	Label label;
	TextField textField;
	/**
	 * @param string description of field
	 * @param d initial valud
	 * @param i num characters
	 */
	public TextEntry(String msg, String text)
	{
		setLayout(new GridLayout(1, 2));
		add(label = new Label(msg, Label.RIGHT));
		add(textField = new CustomTextField(text));

		textField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					valueChanged(textField.getText());
				} catch( Exception exc ) {
					textField.setText("error");
					exc.printStackTrace();
				}
			}
		});
	}

	public void valueChanged(String value)
	{
	}

}
