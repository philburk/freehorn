package org.frogpeak.horn;

/**
 * @author Phil Burk (C) 2003
 */
public class HornClock
{
	public static double getTime()
	{
		return HornSynth.getInstance().getSeconds();
	}

	/**
	 * @param nextTime
	 */
	public static void sleepUntil(double time ) throws InterruptedException
	{
		HornSynth.getInstance().sleepUntil( time );
		//System.out.println("SleepUntil " + time + ", now = " + HornClock.getTime());
	}
}
