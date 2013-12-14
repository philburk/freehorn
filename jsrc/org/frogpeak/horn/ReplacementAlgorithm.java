package org.frogpeak.horn;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Phil Burk (C) 2003
 */
public abstract class ReplacementAlgorithm
{
	protected Hashtable harmonicPlayers; // available for "replacement"
	protected HornModel model;
	
	public ReplacementAlgorithm( HornModel pModel )
	{
		model = pModel;
		harmonicPlayers = new Hashtable();
	}

	/**
	 * Stop all the harmonic players.
	 * @param sectionDuration
	 */
	public void stop()
	{
		Enumeration enum = harmonicPlayers.elements();
		while (enum.hasMoreElements())
		{
			HarmonicPlayer player = (HarmonicPlayer) enum.nextElement();
			System.out.println("ReplacementAlgorithm: stopping " + player );
			player.stop();
		}

	}

	public void addHarmonic(HarmonicPlayer player)
	{
		Integer key = new Integer(player.getHarmonicIndex());
		harmonicPlayers.put(key, player);
	}

	public void removeHarmonic(HarmonicPlayer player)
	{
		Integer key = new Integer(player.getHarmonicIndex());
		harmonicPlayers.remove(key);
	}

	public static String getName()
	{
		return "unnamed";
	}
	
	public abstract HarmonicPlayer chooseReplacement(
		int sectionIndex,
		int harmonicIndex);
}
