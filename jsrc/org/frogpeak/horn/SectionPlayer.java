package org.frogpeak.horn;

/**
 * Plays the harmonics of a section.
 * Implements replacement algorithms.
 * @author Phil Burk (C) 2003
 */
public class SectionPlayer extends HornThread
{
	private ReplacementAlgorithm replacementAlgorithm;
	HornModel model;
	double duration;
	int sectionIndex;
	SectionPlayer previous;
	int[] harmonicOrder;

	/**
	 * @param model
	 * @param currentSectionIndex
	 */
	public SectionPlayer(HornModel pModel, int pSectionIndex)
	{
		model = pModel;
		sectionIndex = pSectionIndex;

	}

	/**
	 * @param sectionDuration
	 */
	public void start(double startTime, double sectionDuration)
	{
		duration = sectionDuration;
		replacementAlgorithm = model.createReplacementAlgorithm();
		super.start(startTime);
	}

	/**
	 */
	public void stop()
	{
		replacementAlgorithm.stop();
		super.stop();
	}

	public double getHarmonicDuration()
	{
		return duration / model.getNumHarmonics();
	}
	/**
	 * @return
	 */
	public double getFadeTime()
	{
		return model.getFadeFactor() * (duration / model.getNumHarmonics());
	}

	public void run()
	{
		double nextTime = getStartTime();
		for (int i = 0;(go && (i < model.getNumHarmonics())); i++)
		{
			// Use Order of Entry algorithm to pick next harmonic.
			int harmonicNumber = pickNthHarmonic(i);
			HarmonicPlayer newPlayer = null;
			HarmonicPlayer oldPlayer = null;
			if (sectionIndex >= 0)
			{
				newPlayer =
					new HarmonicPlayer(model, sectionIndex, harmonicNumber);
				replacementAlgorithm.addHarmonic(newPlayer);
				newPlayer.start(nextTime);
			}

			// Tell previous section to stop one of its harmonics. 
			if (previous != null)
			{
				oldPlayer =
					previous.chooseAndStopPlayer(sectionIndex, harmonicNumber);
			}

			notifyObservers(oldPlayer, newPlayer);

			nextTime += getHarmonicDuration();

			try
			{
				HornClock.sleepUntil(nextTime);
			} catch (InterruptedException e)
			{
			}
		}

		System.out.println("SectionPlayer: " + sectionIndex + " finished...");
	}

	private void notifyObservers(
		HarmonicPlayer oldPlayer,
		HarmonicPlayer newPlayer)
	{
		ReplacementEvent evt;
		if (oldPlayer == null)
		{
			evt =
				new ReplacementEvent(
					ReplacementEvent.STARTING,
					0,
					0,
					newPlayer.getSectionIndex(),
					newPlayer.getHarmonicIndex());

		} else if (newPlayer == null)
		{
			evt =
				new ReplacementEvent(
					ReplacementEvent.STOPPING,
					oldPlayer.getSectionIndex(),
					oldPlayer.getHarmonicIndex(),
					0,
					0);
		} else
		{
			evt =
				new ReplacementEvent(
					ReplacementEvent.REPLACING,
					oldPlayer.getSectionIndex(),
					oldPlayer.getHarmonicIndex(),
					newPlayer.getSectionIndex(),
					newPlayer.getHarmonicIndex());

		}
		setChanged();
		notifyObservers(evt);
	}
	/**
	 * Called by other Section player to tell this one to shut up.
	 * @param sectionIndex
	 * @param harmonicIndex
	 */
	private HarmonicPlayer chooseAndStopPlayer(
		int otherSectionIndex,
		int harmonicIndex)
	{
		// Handle stopping last section.
		if (otherSectionIndex < 0)
			otherSectionIndex = sectionIndex;

		HarmonicPlayer harmonicPlayer =
			replacementAlgorithm.chooseReplacement(
				otherSectionIndex,
				harmonicIndex);

		if (harmonicPlayer != null)
		{
			harmonicPlayer.stop(); // TODO tell it to start gradual fade
			replacementAlgorithm.removeHarmonic(harmonicPlayer);
		}
		return harmonicPlayer;
	}

	/**
	 * @param i
	 * @return
	 */
	private int pickNthHarmonic(int i)
	{
		if (harmonicOrder == null)
		{
			harmonicOrder =
				Primes.createIntegersSortedByPrimeComplexity(
					model.getNumHarmonics());
		}
		int nextHarmonic;

		// In first section, use reverse order.
		if (sectionIndex == 0)
		{
			nextHarmonic = harmonicOrder[i];
		} else
		{
			nextHarmonic = harmonicOrder[(model.getNumHarmonics() - i) - 1];
		}
		return nextHarmonic;
	}

	/**
	 * @return
	 */
	public SectionPlayer getPrevious()
	{
		return previous;
	}

	/**
	 * @param player
	 */
	public void setPrevious(SectionPlayer player)
	{
		previous = player;
	}

	/**
	 * @return
	 */
	public int getSectionIndex()
	{
		return sectionIndex;
	}

	/**
	 * @param i
	 */
	public void setSectionIndex(int i)
	{
		sectionIndex = i;
	}

}
