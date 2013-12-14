package org.frogpeak.horn;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;


/**
 * @author Phil Burk (C) 2003
 */
public class HornView extends Frame
{
	HornModel model;
	HornPlayer player;
	FreeHorn app;
	

	/**
	 * @param model
	 */
	public HornView(FreeHorn pApp, HornModel pModel, HornPlayer pPlayer)
	{
		super("freeHorn View by Larry Polansky");
		model = pModel;
		app = pApp;
		player = pPlayer;
		setSize(400, 300);
		
		Font font = new Font("SansSerif", Font.BOLD, 28 );
		setFont( font );

		setLayout(new BorderLayout());
		add( "North", new HornClockView(player) );
		
		Panel nextPanel = new Panel();
		nextPanel.setLayout(new BorderLayout());

		nextPanel.add( "North", new CPULoadView(player) );
		nextPanel.add( "Center", new HornActivityView(player) );

		add( "Center", nextPanel );
		
		validate();
	}

}
