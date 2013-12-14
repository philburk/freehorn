package org.frogpeak.horn;

import java.util.Observable;

/**
 * Handles a thread safely.
 * @author Phil Burk (C) 2003
 */
public class HornThread extends Observable implements Runnable
{
	private boolean paused;
	private boolean held;
	protected boolean  go;
	private Thread thread;
	private double startTime;
	
	/**
	 * 
	 */
	protected synchronized void start(double startTime)
	{
		this.startTime = startTime;
		go = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized boolean isAlive()
	{
		if( thread == null ) return false;
		else return thread.isAlive();
	}
	/**
	 * 
	 */
	protected synchronized void stop()
	{
		go = false;
		if (thread != null)
		{
			thread.interrupt();
			try
			{
				thread.join(2000); // wait 2 seconds until thread finished
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			thread = null;
		}
	}
	/**
	 * 
	 */
	protected void pause()
	{
		paused = true;
		// TODO Auto-generated method stub

	}
	/**
	 * @return
	 */
	protected boolean isPaused()
	{
		// TODO Auto-generated method stub
		return paused;
	}
	/**
	 * 
	 */
	protected void resume()
	{
		paused = false;
		// TODO Auto-generated method stub

	}
	/**
	 * 
	 */

	protected boolean isHeld()
	{

		// TODO Auto-generated method stub
		return held;
	}
	protected void hold()
	{
		held = true;
	}

	protected void unHold()
	{
		// TODO Auto-generated method stub

		held = false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		while (go)
		{
			System.out.println("Running...");
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
			}
		}
		System.out.println("Finished...");
	}

	/**
	 * @return
	 */
	public double getStartTime()
	{
		return startTime;
	}

	/**
	 * @param d
	 */
	public void setStartTime(double d)
	{
		startTime = d;
	}

}
