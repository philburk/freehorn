package org.frogpeak.horn;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JSlider;

/**
 * @author Phil Burk (C) 2003
 */
public class HornGUI extends Frame
{
	HornModel model;
	HornThread player;
	PlayController playController;
	FreeHorn app;
	private Choice replacementChoice;
	private Choice pitchScalingChoice;
	
	Slider totalLengthSlider;
	Slider numHarmonicsSlider;
	Slider fundFreqSlider;
	Slider durMeanSlider;
	Slider durSpreadSlider;
	Slider durMinSlider;
	Slider durMaxSlider;
	Slider attackMinSlider;
	Slider decayMinSlider;
	Slider attackMaxSlider;
	Slider decayMaxSlider;
	Slider interEventSlider;
	Slider restSlider;
	Slider spectrumSlider;
	Slider loudnessSlider;
	Slider pitchScaleSlider;

	/**
	 * @param model
	 */
	public HornGUI(FreeHorn pApp, HornModel pModel, HornThread pPlayer)
	{
		super("freeHorn by Larry Polansky V" + FreeHorn.VERSION);
		model = pModel;
		app = pApp;
		player = pPlayer;
		
		setPreferredSize(new Dimension(600, 800));
		//pack();

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				app.quit();
			}
		});

		GridLayout gL = new GridLayout(0, 2);
		gL.setHgap(20);
		setLayout(gL);
		
		add(new Label("Static Variables:"));
		add(new Label(""));
		

		add(totalLengthSlider = new Slider(
			"Total Length (minutes)",
			(double) model.getTotalLength(),
			0.1,
			360.0,
			0)
		{
			// This will get called when user hits enter in field.
			public void valueChanged(double value)
			{
				model.setTotalLength(value);
			}
		});
		
		final int FPS_MIN = 0;
		final int FPS_MAX = 30;
		final int FPS_INIT = 15;
		
		JSlider test = new JSlider(JSlider.HORIZONTAL,
                FPS_MIN, FPS_MAX, FPS_INIT);

		
		//add(test);

		//model.setSectionRatios("1:3:5:1");
		double[] sectionRatios = model.getSectionRatios();
		String sectionRatiosString = "";
		for(int i = 0; i < sectionRatios.length; i++){
			sectionRatiosString = sectionRatiosString + sectionRatios[i];
			if(i < sectionRatios.length - 1){
			sectionRatiosString = sectionRatiosString + ":";
			}
		}
		
		add(new TextEntry("Section Ratios", sectionRatiosString)
		{
			public void valueChanged(String text)
			{
				model.setSectionRatios(text);
			}
		});
		
		add(numHarmonicsSlider = new Slider("Num Harmonics", model.getNumHarmonics(), 1, 100, 0)
		{
			public void valueChanged(double value)
			{
				model.setNumHarmonics((int) Math.round(value));
			}
		});
		
		// ReplacementAlgorithm
		Panel replacementPanel = new Panel();
		replacementPanel.setLayout(new GridLayout(2,1));
		replacementPanel.add(new Label("Replace By:"));
		replacementPanel.add(replacementChoice = new Choice());
		add(replacementPanel);
		// The order of these must match the order of the 
		String[] replacementNames = ReplacementAlgorithmFactory.getChoices();
		for (int n = 0; n < replacementNames.length; n++)
		{
			replacementChoice.add(replacementNames[n]);
		}
		replacementChoice.select(model.getReplacementAlgorithm());
		replacementChoice.addItemListener(new ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				model.setReplacementAlgorithm(
					replacementChoice.getSelectedItem());
			}
		});
		
		add(new Label("-----------------------------------------"));
		add(new Label("-----------------------------------------"));
		
		add(new Label("Dynamic Variables:"));
		add(new Label(""));

		add(fundFreqSlider = new Slider(
			"Fundamental Freq",
			model.getFundamentalFrequency(),
			10.0,
			10000.0,
			0)
		{
			public void valueChanged(double value)
			{
				model.setFundamentalFrequency(value);
			}
		});
		
		add(new Label(""));

		add(durMeanSlider = new Slider(
			"Duration Mean",
			model.getDurationMean(),
			0.02,
			40,
			0)
		{
			public void valueChanged(double value)
			{
				model.setDurationMean(value);
			}
		});

		add(durSpreadSlider = new Slider(
			"Duration Spread",
			model.getDurationSpread(),
			0.02,
			20.0,
			0)
		{
			public void valueChanged(double value)
			{
				model.setDurationSpread(value);
			}
		});
		
		add(durMinSlider = new Slider(
				"Duration Min",
				model.getDurationMin(),
				0.02,
				100.0,
				0)
			{
				public void valueChanged(double value)
				{
					model.setDurationMin(value);
					attackMinSlider.setMax(value);
					decayMinSlider.setMax(value);
				}
			});
			add(durMaxSlider = new Slider(
				"Duration Max",
				model.getDurationMax(),
				0.02,
				100.0,
				0)
			{
				public void valueChanged(double value)
				{
					model.setDurationMax(value);
					attackMaxSlider.setMax(value);
					decayMaxSlider.setMax(value);
				}
			});

		add(attackMinSlider = new Slider("Attack Min", model.getAttackMin(), 0.01, 20.0, 0)
		{
			public void valueChanged(double value)
			{
				model.setAttackMin(value);
			}
		});
		add(attackMaxSlider = new Slider("Attack Max", model.getAttackMax(), 0.01, 20.0, 0)
		{
			public void valueChanged(double value)
			{
				model.setAttackMax(value);
			}
		});
		add(decayMinSlider = new Slider("Decay Min", model.getDecayMin(), 0.01, 20.0, 0)
		{
			public void valueChanged(double value)
			{
				model.setDecayMin(value);
			}
		});
		add(decayMaxSlider = new Slider("Decay Max", model.getDecayMax(), 0.01, 20.0, 0)
		{
			public void valueChanged(double value)
			{
				model.setDecayMax(value);
			}
		});
		add(interEventSlider = new Slider(
			"Inter Event Rest",
			model.getInterEventRest(),
			0.0,
			10.0,
			0)
		{
			public void valueChanged(double value)
			{
				model.setInterEventRest(value);
			}
		});
		/*
				NumericEntry fadeFactorEntry;
				add(fadeFactorEntry = new NumericEntry("Fade Factor", model.getFadeFactor(), 0.0, 1.0)
				{
					public void valueChanged(double value)
					{
						model.setFadeFactor(value);
					}
				});
				fadeFactorEntry.setEnabled( false );
		
				add(new NumericEntry("Max Polyphony", model.getMaxPolyphony(), 1.0, 200.0)
				{
					public void valueChanged(double value)
					{
						model.setMaxPolyphony((int) Math.round(value));
					}
				});
		*/

		add(restSlider = new Slider(
			"Rest Probability",
			model.getRestProbability(),
			0.0,
			1.0,
			0)
		{
			public void valueChanged(double value)
			{
				model.setRestProbability(value);
			}
		});

		add(spectrumSlider = new Slider(
			"Spectral Complexity",
			model.getSpectralComplexity(),
			0.0,
			1.0,
			0)
		{
			public void valueChanged(double value)
			{
				model.setSpectralComplexity(value);
			}
		});

		add(loudnessSlider = new Slider("Loudness", model.getLoudness(), 0.0, 1.0, 0)
		{
			public void valueChanged(double value)
			{
				model.setLoudness(value);
			}
		});


		add(pitchScaleSlider = new Slider("Pitch Scaling Factor", model.getPitchScalingFactor(), 0.0, 1.0, 0)
		{
			public void valueChanged(double value)
			{
				model.setPitchScalingFactor(value);
			}
		});

		// PitchScalingAlgorithm
		Panel pitchScalingPanel = new Panel();
		pitchScalingPanel.setLayout(new GridLayout(2,1));
		pitchScalingPanel.add(new Label("Pitch Scale by :"));
		pitchScalingPanel.add(pitchScalingChoice = new Choice());
		add(pitchScalingPanel);
		// The order of these must match the order of the 
		String[] pitchScaleNames = PitchScalingAlgorithmFactory.getChoices();
		for (int n = 0; n < pitchScaleNames.length; n++)
		{
			pitchScalingChoice.add(pitchScaleNames[n]);
		}
		pitchScalingChoice.select(model.getPitchScalingAlgorithm().getName());
		pitchScalingChoice.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				model.setPitchScalingAlgorithmByName(pitchScalingChoice.getSelectedItem());
			}
		});
		
		totalLengthSlider.setTextFieldValue();
		numHarmonicsSlider.setTextFieldValue();
		fundFreqSlider.setTextFieldValue();
		durMeanSlider.setTextFieldValue();
		durSpreadSlider.setTextFieldValue();
		durMinSlider.setTextFieldValue();
		durMaxSlider.setTextFieldValue();
		attackMinSlider.setTextFieldValue();
		decayMinSlider.setTextFieldValue();
		attackMaxSlider.setTextFieldValue();
		decayMaxSlider.setTextFieldValue();
		interEventSlider.setTextFieldValue();
		restSlider.setTextFieldValue();
		spectrumSlider.setTextFieldValue();
		loudnessSlider.setTextFieldValue();
		pitchScaleSlider.setTextFieldValue();
		
		add(new Label("-----------------------------------------"));
		add(new Label("-----------------------------------------"));
		add(new Label("Play Controls:"));
		add(new Label(""));

		add(playController = new PlayController(player));

		validate();

		setupMenus();

		setBounds(ScreenLayout.guiRect);
	}

	/**
	 * 
	 */
	private void setupMenus()
	{
		MenuBar menuBar = new MenuBar();

		Menu fileMenu = new Menu("File");

		MenuItem openItem = new MenuItem("Open...");
		openItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				app.openPiece();
			}

		});
		fileMenu.add(openItem);

		MenuItem saveItem = new MenuItem("Save");
		saveItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				app.savePiece();
			}
		});
		fileMenu.add(saveItem);

		MenuItem saveAsItem = new MenuItem("Save As...");
		saveAsItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				app.savePieceAs();
			}
		});
		fileMenu.add(saveAsItem);

		MenuItem quitItem = new MenuItem("Quit");
		quitItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				app.quit();
			}
		});
		fileMenu.add(quitItem);

		menuBar.add(fileMenu);

		setMenuBar(menuBar);
	}

}
