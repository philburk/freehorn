package org.frogpeak.horn;

/**
 * @author Phil Burk (C) 2003
 */
public class ReplacementAlgorithmFactory
{
	static String[] names;
	
	/** Return array of name strings for possible replacement algorithms. */
	static
	{
		names = new String[4];
		int i=0;
		names[i++] = ReplacementByClosestPitch.getName(); // default
		names[i++] = ReplacementByEquivalentHarmonic.getName();
		names[i++] = ReplacementByClosestFreq.getName();
		names[i++] = "Mixed";
	}
	
	/** Return array of name strings for possible replacement algorithms. */
	public static String[] getChoices()
	{
		return names;
	}
	
	public static ReplacementAlgorithm createReplacementAlgorithm( String name, HornModel pModel  )
	{
		if( name.equals( ReplacementByEquivalentHarmonic.getName() ) )
		{
			return new ReplacementByEquivalentHarmonic(pModel);
		}
		else if( name.equals( ReplacementByClosestFreq.getName() ) )
		{
			return new ReplacementByClosestFreq(pModel);
		}
		else if( name.equals( ReplacementByClosestPitch.getName() ) )
		{
			return new ReplacementByClosestPitch(pModel);
		}
		else /* Mixed */
		{
			int choice = (int) (Math.random() * (names.length - 1.0000001));
			return createReplacementAlgorithm( names[choice], pModel );
		}
	}
}
