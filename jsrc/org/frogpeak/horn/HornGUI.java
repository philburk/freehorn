package org.frogpeak.horn;

import java.awt.Choice;
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

	/**
	 * @param model
	 */
	public HornGUI(FreeHorn pApp, HornModel pModel, HornThread pPlayer)
	{
		super("freeHorn by Larry Polansky V" + FreeHorn.VERSION);
		model = pModel;
		app = pApp;
		player = pPlayer;

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				app.quit();
			}
		});

		setLayout(new GridLayout(0, 2));

		add(new NumericEntry(
			"Total Length (minutes)",
			(double) model.getTotalLength(),
			0.1,
			1000000.0)
		{
			// This will get called when user hits enter in field.
			public void valueChanged(double value)
			{
				model.setTotalLength(value);
			}
		});

		model.setSectionRatios("1:3:5:1");
		add(new TextEntry("Section Ratios", "1:3:5:1")
		{
			public void valueChanged(String text)
			{
				model.setSectionRatios(text);
			}
		});

		add(new NumericEntry(
			"Fundamental Freq",
			model.getFundamentalFrequency(),
			10.0,
			10000.0)
		{
			public void valueChanged(double value)
			{
				model.setFundamentalFrequency(value);
			}
		});

		add(new NumericEntry("Num Harmonics", model.getNumHarmonics(), 1, 100)
		{
			public void valueChanged(double value)
			{
				model.setNumHarmonics((int) Math.round(value));
			}
		});

		add(new NumericEntry(
			"Duration Mean",
			model.getDurationMean(),
			0.01,
			100.0)
		{
			public void valueChanged(double value)
			{
				model.setDurationMean(value);
			}
		});

		add(new NumericEntry(
			"Duration Range",
			model.getDurationRange(),
			0.01,
			100.0)
		{
			public void valueChanged(double value)
			{
				model.setDurationRange(value);
			}
		});

		add(new NumericEntry("Attack Min", model.getAttackMin(), 0.01, 100.0)
		{
			public void valueChanged(double value)
			{
				model.setAttackMin(value);
			}
		});
		add(new NumericEntry("Attack Max", model.getAttackMax(), 0.01, 100.0)
		{
			public void valueChanged(double value)
			{
				model.setAttackMax(value);
			}
		});
		add(new NumericEntry("Decay Min", model.getDecayMin(), 0.01, 100.0)
		{
			public void valueChanged(double value)
			{
				model.setDecayMin(value);
			}
		});
		add(new NumericEntry("Decay Max", model.getDecayMax(), 0.01, 100.0)
		{
			public void valueChanged(double value)
			{
				model.setDecayMax(value);
			}
		});
		add(new NumericEntry(
			"Duration Min",
			model.getDurationMin(),
			0.01,
			100.0)
		{
			public void valueChanged(double value)
			{
				model.setDurationMin(value);
			}
		});
		add(new NumericEntry(
			"Duration Max",
			model.getDurationMax(),
			0.01,
			100.0)
		{
			public void valueChanged(double value)
			{
				model.setDurationMax(value);
			}
		});
		add(new NumericEntry(
			"Inter Event Rest",
			model.getInterEventRest(),
			0.0,
			10.0)
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

		add(new NumericEntry(
			"Rest Probability",
			model.getRestProbability(),
			0.0,
			1.0)
		{
			public void valueChanged(double value)
			{
				model.setRestProbability(value);
			}
		});

		add(new NumericEntry(
			"Spectral Complexity",
			model.getSpectralComplexity(),
			0.0,
			1.0)
		{
			public void valueChanged(double value)
			{
				model.setSpectralComplexity(value);
			}
		});

		add(new NumericEntry("Loudness", model.getLoudness(), 0.0, 11.0)
		{
			public void valueChanged(double value)
			{
				model.setLoudness(value);
			}
		});


		add(new NumericEntry("Pitch Scaling Factor", model.getPitchScalingFactor(), 0.0, 1.0)
		{
			public void valueChanged(double value)
			{
				model.setPitchScalingFactor(value);
			}
		});

		// PitchScalingAlgorithm
		Panel pitchScalingPanel = new Panel();
		pitchScalingPanel.add(new Label("Pitch Scale by :"));
		pitchScalingPanel.add(pitchScalingChoice = new Choice());
		add(pitchScalingPanel);
		// The order of these must match the order of the 
		String[] names = PitchScalingAlgorithmFactory.getChoices();
		for (int n = 0; n < names.length; n++)
		{
			pitchScalingChoice.add(names[n]);
		}
		pitchScalingChoice.select(model.getPitchScalingAlgorithm().getName());
		pitchScalingChoice.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				model.setPitchScalingAlgorithmByName(pitchScalingChoice.getSelectedItem());
			}
		});

		// ReplacementAlgorithm
		Panel replacementPanel = new Panel();
		replacementPanel.add(new Label("Replace By:"));
		replacementPanel.add(replacementChoice = new Choice());
		add(replacementPanel);
		// The order of these must match the order of the 
		names = ReplacementAlgorithmFactory.getChoices();
		for (int n = 0; n < names.length; n++)
		{
			replacementChoice.add(names[n]);
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
