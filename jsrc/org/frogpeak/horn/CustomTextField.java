package org.frogpeak.horn;
import java.awt.*;
import java.awt.event.*;
import com.softsynth.util.*;

/**
 * TextField that turns pink when modified, and white when the value
 * is read from it.
 * 
 * @author (C) 2000 Phil Burk, SoftSynth.com, All Rights Reserved
 * @version 14.2
*/

public class CustomTextField extends TextField
{
	boolean modified = false;

	private void setupKeyListener()
	{
		addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				markDirty();
			}
		});
	}

	public CustomTextField()
	{
		super();
		setupKeyListener();
	}

	public CustomTextField(int numCharacters)
	{
		super(numCharacters);
		setupKeyListener();
	}
	
	public CustomTextField(String text)
	{
		super(text);
		setupKeyListener();
	}

	void markDirty()
	{
		modified = true;
		setBackground(Color.pink);
		repaint();
	}

	void markClean()
	{
		modified = false;
		setBackground(Color.white);
		repaint();
	}

	public void setText(String text)
	{
		markClean();
		super.setText(text);
	}

	public String getText()
	{
		markClean();
		return super.getText();
	}

	/**
	 * Show user that value was clipped.
	 */
	protected void markClipped()
	{
		setBackground(Color.yellow);
		repaint();
	}

}
