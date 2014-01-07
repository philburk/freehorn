package org.frogpeak.horn;

import java.util.Enumeration;

/**
 * Pick Harmonic based on closest frequency.
 * @author Phil Burk (C) 2003
 */
public class ReplacementByClosestPitch extends ReplacementAlgorithm
{

	/**
	 * @param pModel
	 */
	public ReplacementByClosestPitch(HornModel pModel)
	{
		super(pModel);
	}

	public HarmonicPlayer chooseReplacement(
		int sectionIndex,
		int harmonicIndex)
	{
		double frequency =
			model.calculateFrequency(sectionIndex, harmonicIndex);
		return getHarmonicClosestInPitch(frequency);
	}


	/**
	 * Compare based on logarithmic pitch value.
	 * @param frequency will be converted to pitch internally
	 * @return
	 */
	private HarmonicPlayer getHarmonicClosestInPitch(double frequency)
	{
		// We don't have to use an absolute pitch because we are just comparing.
		double pitch = Math.log(frequency);
		double minDistance = Double.MAX_VALUE;
		HarmonicPlayer closestPlayer = null;
		Enumeration en = harmonicPlayers.elements();
		while (en.hasMoreElements())
		{
			HarmonicPlayer player = (HarmonicPlayer) en.nextElement();
			double testPitch = Math.log(player.getFrequency());
			double distance = Math.abs(pitch - testPitch);
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
		return "Closest Pitch";
	}
}
