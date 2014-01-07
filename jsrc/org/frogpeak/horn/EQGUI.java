package org.frogpeak.horn;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;


/**
 * @author Phil Burk (C) 2003
 */
public class EQGUI extends Frame
{
	HornModel model;
	HornPlayer player;
	FreeHorn app;
	
	Slider[] gainSlider = new Slider[30];


	/**
	 * @param model
	 */
	public EQGUI(FreeHorn pApp, HornModel pModel, HornPlayer pPlayer)
	{
		super("Equalizer");
		model = pModel;
		app = pApp;
		player = pPlayer;

		setPreferredSize(new Dimension(650, 300));
		
		GridLayout gL = new GridLayout(0, 30);
		gL.setHgap(5);
		setLayout(gL);

		for(int i = 0; i < 30; i++){
			String s;
			if(i == 0){
				s = "kHz";
			} else if((int) Math.pow(2, 5 + i/3.) == 128){
				s = ".125";
			} else if((int) Math.pow(2, 5 + i/3.) == 256){
				s = ".25";
			} else if((int) Math.pow(2, 5 + i/3.) == 512){
				s = ".5";
			} else if((int) Math.pow(2, 5 + i/3.) == 1024){
				s = "1";
			} else if((int) Math.pow(2, 5 + i/3.) == 2048){
				s = "2";
			} else if((int) Math.pow(2, 5 + i/3.) == 4096){
				s = "4";
			} else if((int) Math.pow(2, 5 + i/3.) == 8192){
				s = "8";
			} else {
				s = "";
			}
			
			add(gainSlider[i] = new Slider(s,
					//((int) Math.pow(2, 5 + i/3.)) + "",
					Math.abs((double) model.getGain(i) - 1),
					0,
					.99,
					1,
					i)
			{
				// This will get called when user hits enter in field.
				public void valueChanged(double value)
				{
					model.setGain(identification, value);
					//System.out.println("called");
					HornSynth.getInstance().updateEq(identification, value);
				}
			});

			gainSlider[i].setTextFieldValue();
		}

		validate();
	}

}
