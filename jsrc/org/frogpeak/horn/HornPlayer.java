package org.frogpeak.horn;

/**
 * Master controller.
 * March through the sections of a Horn piece.
 * Determine the length of each section.
 * Trigger the playing of each section.
 * @author Phil Burk (C) 2003
 */
public class HornPlayer extends HornThread
{
	private static final double SECONDS_PER_MINUTE = 60.0;
	private final HornModel model;
	
	private double sectionDuration;
	SectionPlayer currentPlayer;
	SectionPlayer previousPlayer;
	private double nextTime;
	/**
	 * @param model
	 */
	public HornPlayer(HornModel pModel)
	{
		model = pModel;
	}

	private void setup()
	{
		sectionDuration =
			SECONDS_PER_MINUTE
				* model.getTotalLength()
				/ (model.getNumSections() + 1); // TODO change duration if sudden stop

		nextTime = getStartTime();
		previousPlayer = null;
	}

	/**
	 * 
	 * @return duration of this section in seconds
	 */
	private double playNextSection(double time, int sectionIndex )
	{
		previousPlayer = currentPlayer;
		currentPlayer = 
			new SectionPlayer(model, sectionIndex);
		currentPlayer.setPrevious(previousPlayer);
		setChanged();
		this.notifyObservers(currentPlayer);
		currentPlayer.start(time, sectionDuration);
		return sectionDuration;
	}

	public void run()
	{
		setup();

		int currentSectionIndex = -1;
		while (go && ((currentSectionIndex+1) < model.getNumSections()))
		{
			currentSectionIndex += 1;
			nextTime += playNextSection(nextTime,currentSectionIndex);
			try
			{
				HornClock.sleepUntil(nextTime);
			} catch (InterruptedException e)
			{
			}
		}
		// Play end build down.
		if( go )
		{
			nextTime += playNextSection(nextTime,-1);
			try
			{
				HornClock.sleepUntil(nextTime);
			} catch (InterruptedException e)
			{
			}
		}

		// Stop all the remaining harmonic players.
		if( previousPlayer != null )
		{
			previousPlayer.stop();
		}
		// Stop all the remaining harmonic players.
		if( currentPlayer != null )
		{
			currentPlayer.stop();
		}
		System.out.println("HornPlayer finished...");
	}

	/**
	 * @return
	 */
	public HornModel getModel()
	{
		return model;
	}

}
