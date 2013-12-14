package org.frogpeak.horn;


/**
 * TextField that turns pink when modified, and white when the value
 * is read from it.
 * 
 * @author (C) 2000 Phil Burk, SoftSynth.com, All Rights Reserved
 * @version 14.2
*/

public class CustomTextFieldInteger extends CustomTextField
{
	
	public CustomTextFieldInteger( int val, int numCharacters )
	{
		super( numCharacters );
		setIntegerValue( val );
	}
	
	public int getIntegerValue() throws NumberFormatException
	{
		int val = Integer.parseInt( getText() );
		return val;
	}
	
	public void setIntegerValue( int value )
	{
		super.setText( Integer.toString( value ) );
		markClean();
	}		
}
