package org.frogpeak.horn;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
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
		super("Clock");
		model = pModel;
		app = pApp;
		player = pPlayer;
		
		setPreferredSize(new Dimension(650, 300));
		
		Font fontTimeLabel = new Font("SansSerif", Font.BOLD, 28 );
		//Font fontCPU = new Font("SansSerif", Font.BOLD, 14 );
		setFont( fontTimeLabel );

		setLayout(new BorderLayout());
		add( "North", new HornClockView(player) );
		
		Panel nextPanel = new Panel();
		nextPanel.setLayout(new BorderLayout());

		//nextPanel.add( "North", new Label("time = ") );
		//setFont( fontCPU );
		nextPanel.add( "South", new CPULoadView(player) );
		nextPanel.add( "Center", new HornActivityView(player) );

		add( "Center", nextPanel );
		
		validate();
	}

}
