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

public class CustomTextFieldDouble extends CustomTextField
{
	double min = Double.MIN_VALUE;
	double max = Double.MAX_VALUE;
	
	public CustomTextFieldDouble( double val, double pMin, double pMax )
	{
		super();
		setDoubleValue( val );
		min = pMin;
		max = pMax;
	}
	
	public double getDoubleValue() throws NumberFormatException
	{
		double val = Double.valueOf( getText() ).doubleValue();
		boolean clipped = false;
		if( val > max )
		{
			clipped = true;
			val = max;
		}
		else if( val < min )
		{
			clipped = true;
			val = min;
		}
		if( clipped )
		{
			setDoubleValue( val );
			markClipped();
		}
		return val;
	}
	
	public void setDoubleValue( double value )
	{
		super.setText( NumericOutput.doubleToString( value, 1, 4 ) );
	}		

	/**
	 * @return
	 */
	public double getMax()
	{
		return max;
	}

	/**
	 * @return
	 */
	public double getMin()
	{
		return min;
	}

	/**
	 * @param d
	 */
	public void setMax(double d)
	{
		max = d;
	}

	/**
	 * @param d
	 */
	public void setMin(double d)
	{
		min = d;
	}

}
