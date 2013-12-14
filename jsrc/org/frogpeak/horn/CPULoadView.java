package org.frogpeak.horn;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Phil Burk (C) 2004
 *
 */
public class CPULoadView extends Label implements Runnable
{
	HornPlayer player;
	static DecimalFormat format = new DecimalFormat("CPU Load = ##%");
	
	public CPULoadView( HornPlayer player )
	{
		super(format.format(0.0));
		this.player = player;
		new Thread(this).start();
	}

	public void run()
	{
		double time = HornClock.getTime();
		while (true)
		{
			String text = format.format( HornSynth.getInstance().getCPULoad() ) +
			", " + HornSynth.getInstance().getNumVoices() + " voices";
			setText(text);
			time += 0.5;
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
}
