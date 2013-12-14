package org.frogpeak.horn;

import java.awt.Button;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Phil Burk (C) 2003
 */
public class PlayController extends Panel
{
	Button startButton;
	Button stopButton;
	Button pauseButton;
	Button holdButton;
	HornThread player;

	/**
	 * @param player
	 */
	public PlayController(HornThread pPlayer)
	{
		player = pPlayer;
		
		add(startButton = new Button("Start"));
		startButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				player.start( HornClock.getTime() );
			}
		});
		
		add(stopButton = new Button("Stop"));
		stopButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				player.stop();
			}
		});
		
		add(pauseButton = new Button("Pause"));
		pauseButton.setEnabled( false ); // TODO implement pause
		pauseButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if( player.isPaused() )
				{
					player.resume();
					pauseButton.setLabel("Pause");
				}
				else
				{
					player.pause();
					pauseButton.setLabel("Resume");
				}
				getParent().validate();
			}
		});
		
		add(holdButton = new Button("Hold"));
		holdButton.setEnabled( false ); // TODO implement hold
		holdButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("held = " +  player.isHeld());
				if( player.isHeld() )
				{
					player.unHold();
					holdButton.setLabel("Hold");
				}
				else
				{
					player.hold();
					holdButton.setLabel("Continue");
				}
				getParent().validate();
			}
		});
	}

}
