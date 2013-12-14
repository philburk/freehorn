package org.frogpeak.horn;

import java.awt.*;

/**
 * @author Phil Burk (C) 2004
 *
 */
public class HornClockView extends Label implements Runnable
{
	HornPlayer player;
	
	public HornClockView( HornPlayer player )
	{
		super(formatTime(0));
		this.player = player;
		new Thread(this).start();
	}

	public void run()
	{
		double time =  HornClock.getTime();
		while (true)
		{
			double hornTime = 0.0;
			if( player.isAlive() )
			{
				hornTime = time - player.getStartTime();
			}
			String text = formatTime( hornTime );
			setText(text);
			time += 0.1;
			try
			{
				HornClock.sleepUntil(time);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			repaint();
		}
	}

	/**
	 * @param d
	 * @return
	 */
	private static String formatTime(double seconds)
	{
		int iMinutes = (int)(seconds / 60.0);
		double remSeconds = seconds - (60 * iMinutes);
		int iSeconds = (int) remSeconds;
		double fract = remSeconds - iSeconds;
		int iDeciSeconds = (int) (fract * 10.0);
		
		StringBuffer buf = new StringBuffer();
		buf.append("Time = ");
		buf.append(iMinutes);
		buf.append(':');
		if( iSeconds < 10 )buf.append('0');
		buf.append(iSeconds);
		buf.append('.');
		buf.append(iDeciSeconds);
		
		return buf.toString();
	}
}
