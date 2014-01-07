package org.frogpeak.horn;

import java.awt.BorderLayout;
import java.awt.Font;
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
	Panel textFieldPanel;
	/**
	 * @param string description of field
	 * @param d initial valud
	 * @param i num characters
	 */
	public TextEntry(String msg, String text)
	{
		setLayout(new GridLayout(1, 1));
		Panel extraPanel = new Panel();
		extraPanel.setLayout(new BorderLayout());
		textFieldPanel = new Panel();
		textField = new CustomTextField(text, 35);
		//TextField textField2 = new CustomTextField("woohoo");
		setFontSize(12);
		textFieldPanel.add(textField);
		extraPanel.add(textFieldPanel, BorderLayout.WEST);
		extraPanel.add(label = new Label(msg), BorderLayout.NORTH);
		add(extraPanel);

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

	public void setFont(Font textFieldFont) {
		textField.setFont(textFieldFont);
	}

	public void setFontSize(int size) {
		setFont(new Font("Monospaced", Font.BOLD, size));
	}

	public void valueChanged(String value)
	{
	}

}
