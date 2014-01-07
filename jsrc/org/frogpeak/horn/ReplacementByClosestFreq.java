package org.frogpeak.horn;

import java.util.Enumeration;

/**
 * Pick Harmonic based on closest frequency.
 * @author Phil Burk (C) 2003
 */
public class ReplacementByClosestFreq extends ReplacementAlgorithm
{

	/**
	 * @param pModel
	 */
	public ReplacementByClosestFreq(HornModel pModel)
	{
		super(pModel);
	}

	public HarmonicPlayer chooseReplacement(
		int sectionIndex,
		int harmonicIndex)
	{
		double frequency =
			model.calculateFrequency(sectionIndex, harmonicIndex);
		return getHarmonicClosestInFrequency(frequency);
	}

	/** Return harmonic closest in frequency. */
	private HarmonicPlayer getHarmonicClosestInFrequency(double frequency)
	{
		double minDistance = Double.MAX_VALUE;
		HarmonicPlayer closestPlayer = null;
		Enumeration en = harmonicPlayers.elements();
		while (en.hasMoreElements())
		{
			HarmonicPlayer player = (HarmonicPlayer) en.nextElement();
			double distance = Math.abs(frequency - player.getFrequency());
			if (distance < minDistance)
			{
				closestPlayer = player;
				minDistance = distance;
			}
		}
		return closestPlayer;
	}

	public static String getName()
	{
		return "Closest Frequency";
	}
}
