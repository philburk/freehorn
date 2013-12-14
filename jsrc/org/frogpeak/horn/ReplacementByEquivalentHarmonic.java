package org.frogpeak.horn;

/**
 * @author Phil Burk (C) 2003
 */
public class ReplacementByEquivalentHarmonic extends ReplacementAlgorithm
{
	/**
	 * @param pModel
	 */
	public ReplacementByEquivalentHarmonic(HornModel pModel)
	{
		super(pModel);
	}

	public HarmonicPlayer chooseReplacement(
		int sectionIndex,
		int harmonicIndex)
	{
		return (HarmonicPlayer) harmonicPlayers.get(new Integer(harmonicIndex));
	}

	public static String getName()
	{
		return "Equivalent Harmonic";
	}
}
