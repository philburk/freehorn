package org.frogpeak.horn;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.util.Observable;
import java.util.Observer;

/**
 * Draw status info in a Canvas.
 * @author Phil Burk (C) 2004
 *
 */
public class HornActivityView extends Panel implements Observer
{
	HornPlayer player;
	Canvas statusCanvas;
	HornModel model;
	SectionPlayer sectionPlayer;
	ReplacementEvent evt;

	public HornActivityView(HornPlayer player)
	{
		this.player = player;
		model = player.getModel();
		setLayout(new BorderLayout());
		add("Center", statusCanvas = new StatusCanvas());
		player.addObserver(this);
	}

	class StatusCanvas extends Canvas
	{
		public void paint(Graphics g)
		{

			int x = 50;
			int y = 100;
			int lineHeight = 40;
			g.setColor(getForeground());
			
			if (sectionPlayer != null)
			{
				int sectionIndex = sectionPlayer.getSectionIndex();
				String text;
				if (sectionIndex < 0)
				{
					text = "Section End";
				} else
				{
					text = "Section #" + (sectionPlayer.getSectionIndex() + 1);
				}
				g.drawString(text, x, y);
				y += lineHeight;
			}
			if (evt != null)
			{
				String text = null;
				switch (evt.action)
				{
					case ReplacementEvent.STARTING :
						text =
							" Starting "
								+ model.getSectionRatio(evt.newSection)
								+ " ("
								+ evt.newHarmonic + ")";
						break;
					case ReplacementEvent.REPLACING :
						text =
							model.getSectionRatio(evt.newSection)
								+ " ("
								+ evt.newHarmonic + ")"
								+ " replacing "
								+ model.getSectionRatio(evt.oldSection)
								+ " ("
								+ evt.oldHarmonic + ")";
						break;
					case ReplacementEvent.STOPPING :
						text =
							" Stopping "
								+ model.getSectionRatio(evt.oldSection)
								+ " ("
								+ evt.oldHarmonic + ")";
						break;
				}
				if (text != null)
				{
					g.drawString(text, x, y);
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable arg0, Object arg1)
	{
		if (arg1 instanceof SectionPlayer)
		{
			sectionPlayer = (SectionPlayer) arg1;
			sectionPlayer.addObserver(this);
			evt = null;
		} else if (arg1 instanceof ReplacementEvent)
		{
			evt = (ReplacementEvent) arg1;
		}
		statusCanvas.repaint();
	}
}
