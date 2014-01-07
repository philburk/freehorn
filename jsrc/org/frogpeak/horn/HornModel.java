package org.frogpeak.horn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

/**
 * This class contains all the data that describes the piece.
 * It will have section ratios, fundamental, probabilities, etc.
 * @author Phil Burk (C) 2003
 */
public class HornModel
{
	private File saveFile;
	private double totalLength = 3.0;
	private double restProbability = 0.5;
	private double spectralComplexity = 0.1;
	private double loudness = 0.5;
	private double fundamentalFrequency = 110.0;
	private int numHarmonics = 17;
	private double[] sectionRatios = {1,1.25,1.5,1};
	private String sectionRatiosString = "1:1.25:1.5:1";
	private int maxPolyphony = 100;
	private double durationMean = 2;
	private double durationSpread = 0.1;
	private double durationMin = 0.3;
	private double durationMax = 1.5;
	private double interEventRest = 0.5;
	private double fadeFactor = 1.0;
	private double attackMin = 0.2;
	private double attackMax = 1.0;
	private double decayMin = 0.2;
	private double decayMax = 1.0;
	
	private double[] gain = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

	
	// fadeTime = fadeFactor * (sectionDuration/humHarmonics)
	private String replacementAlgorithm =
		ReplacementAlgorithmFactory.getChoices()[0];

	private double pitchScalingFactor = 0.2;

	private PitchScalingAlgorithm pitchScalingAlgorithm =
		new PitchScalingAlgorithm();
	

	public double getGain(int i)
	{
		return gain[i];
	}

	public void setGain(int i, double g)
	{
		gain[i] = g;
	}

	/**
	 * @return
	 */
	public double getTotalLength()
	{
		return totalLength;
	}

	/**
	 * @param d
	 */
	public void setTotalLength(double d)
	{
		totalLength = d;
		System.out.println("totalLength = " + d);
	}

	/**
	 * @return
	 */
	public double getFundamentalFrequency()
	{
		return fundamentalFrequency;
	}

	/**
	 * @param d
	 */
	public void setFundamentalFrequency(double d)
	{
		fundamentalFrequency = d;
		System.out.println("fundamentalFrequency = " + d);
	}

	/**
	 * @return
	 */
	public int getMaxPolyphony()
	{
		return maxPolyphony;
	}

	/**
	 * @return
	 */
	public int getNumHarmonics()
	{
		return numHarmonics;
	}

	/**
	 * @return
	 */
	public double[] getSectionRatios()
	{
		return sectionRatios;
	}

	/**
	 * @param i
	 */
	public void setMaxPolyphony(int i)
	{
		maxPolyphony = i;
	}

	/**
	 * @param i
	 */
	public void setNumHarmonics(int i)
	{
		numHarmonics = i;
	}

	/**
	 * @param ds
	 */
	public void setSectionRatios(double[] ds)
	{
		sectionRatios = ds;
	}

	private static Vector parseDelimitedStrings(
		String input,
		int delimiterChar)
	{
		Vector subs = new Vector();
		while (input.length() > 0)
		{
			int delimiterIndex = input.indexOf(delimiterChar);
			String part;
			if (delimiterIndex >= 0)
			{
				part = input.substring(0, delimiterIndex);
				input = input.substring(delimiterIndex + 1, input.length());
			} else
			{
				part = input;
				input = "";
			}
			subs.addElement(part);
		}
		return subs;
	}
	/**
	 * Parse string and setup sections.
	 * @param text
	 */
	protected void setSectionRatios(String text)
	{

		sectionRatiosString = text;
		Vector subs = parseDelimitedStrings(text, ':');
		double[] ratios = new double[subs.size()];
		for (int i = 0; i < ratios.length; i++)
		{
			ratios[i] = Double.parseDouble((String) subs.elementAt(i));
			// may throw NumberFormatException
			//System.out.println("section " + ratios[i]);
		}
		sectionRatios = ratios;
	}

	public int getNumSections()
	{
		if (sectionRatios == null)
			return 0;
		else
			return sectionRatios.length;
	}

	public double getSectionRatio(int sectionIndex)
	{
		return sectionRatios[sectionIndex];
	}

	/**
	 * @return
	 */
	public double getFadeFactor()
	{
		return fadeFactor;
	}

	/**
	 * @param d
	 */
	public void setFadeFactor(double d)
	{
		fadeFactor = d;
	}

	/**
	 * @param sectionIndex
	 * @param harmonicIndex
	 * @return
	 */
	public double calculateFrequency(int sectionIndex, int harmonicIndex)
	{
		return getFundamentalFrequency()
			* sectionRatios[sectionIndex]
			* harmonicIndex;
	}

	/**
	 * @return
	 */
	public ReplacementAlgorithm createReplacementAlgorithm()
	{
		return ReplacementAlgorithmFactory.createReplacementAlgorithm(
			replacementAlgorithm,
			this);
	}
	/**
	 * @return
	 */
	public String getReplacementAlgorithm()
	{
		return replacementAlgorithm;
	}

	/**
	 * @param name
	 */
	public void setReplacementAlgorithm(String name)
	{
		replacementAlgorithm = name;
	}

	/**
	 * @return
	 */
	public PitchScalingAlgorithm getPitchScalingAlgorithm()
	{
		return pitchScalingAlgorithm;
	}

	/**
	 * @param string
	 */
	protected void setPitchScalingAlgorithmByName(String name)
	{
		PitchScalingAlgorithm algo =
			PitchScalingAlgorithmFactory.createPitchScalingAlgorithm(name);
		setPitchScalingAlgorithm(algo);
	}

	/**
	 * @param string
	 */
	protected void setPitchScalingAlgorithm(PitchScalingAlgorithm algo)
	{
		pitchScalingAlgorithm = algo;
	}

	/**
	 * @return
	 */
	public double getRestProbability()
	{
		return restProbability;
	}

	/**
	 * @param d
	 */
	public void setRestProbability(double d)
	{
		restProbability = d;
	}

	/**
	 * @return
	 */
	public double getSpectralComplexity()
	{
		return spectralComplexity;
	}

	/**
	 * @param d
	 */
	public void setSpectralComplexity(double d)
	{
		spectralComplexity = d;
	}

	/**
	 * @return
	 */
	public double getLoudness()
	{
		return loudness;
	}

	/**
	 * @param d
	 */
	public void setLoudness(double d)
	{
		loudness = d;
	}

	/**
	 * @return
	 */
	public double getDurationMax()
	{
		return durationMax;
	}

	/**
	 * @return
	 */
	public double getDurationMean()
	{
		return durationMean;
	}

	/**
	 * @return
	 */
	public double getDurationMin()
	{
		return durationMin;
	}

	/**
	 * @return
	 */
	public double getDurationSpread()
	{
		return durationSpread;
	}

	/**
	 * @param d
	 */
	public void setDurationMax(double d)
	{
		durationMax = d;
	}

	/**
	 * @param d
	 */
	public void setDurationMean(double d)
	{
		durationMean = d;
	}

	/**
	 * @param d
	 */
	public void setDurationMin(double d)
	{
		durationMin = d;
	}

	/**
	 * @param d
	 */
	public void setDurationSpread(double d)
	{
		durationSpread = d;
	}

	/**
	 * @return
	 */
	public double getInterEventRest()
	{
		return interEventRest;
	}

	/**
	 * @param d
	 */
	public void setInterEventRest(double d)
	{
		interEventRest = d;
	}

	/**
	 * @return
	 */
	public File getSaveFile()
	{
		return saveFile;
	}

	/**
	 * 
	 */
	public void save() throws IOException
	{
		if (saveFile != null)
			saveAs(saveFile);
	}

	/**
	 * @param file
	 */
	public void saveAs(File file) throws IOException
	{
		saveFile = file;
		FileOutputStream outStream = new FileOutputStream(saveFile);
		BufferedOutputStream bufStream = new BufferedOutputStream(outStream);
		Properties properties = new Properties();

		putDoubleProperty(properties, "totalLength", totalLength);
		properties.setProperty("sectionRatios", sectionRatiosString);
		putDoubleProperty(properties, "numHarmonics", numHarmonics);
		putDoubleProperty(properties, "restProbability", restProbability);
		putDoubleProperty(properties, "spectralComplexity", spectralComplexity);
		putDoubleProperty(properties, "loudness", loudness);
		putDoubleProperty(
			properties,
			"fundamentalFrequency",
			fundamentalFrequency);
		putDoubleProperty(properties, "durationMean", durationMean);
		putDoubleProperty(properties, "durationSpread", durationSpread);
		putDoubleProperty(properties, "durationMin", durationMin);
		putDoubleProperty(properties, "durationMax", durationMax);
		putDoubleProperty(properties, "attackMin", attackMin);
		putDoubleProperty(properties, "attackMax", attackMax);
		putDoubleProperty(properties, "decayMin", decayMin);
		putDoubleProperty(properties, "decayMax", decayMax);
		putDoubleProperty(properties, "interEventRest", interEventRest);
		properties.setProperty("replacementAlgorithm", replacementAlgorithm);
		
		properties.setProperty(
			"pitchScalingAlgorithm",
			pitchScalingAlgorithm.getName());
		putDoubleProperty(properties, "pitchScalingfactor", pitchScalingFactor);
		
		for(int i = 0; i < 30; i++){
			putDoubleProperty(properties, "gain" + i, gain[i]);
		}
		
		properties.save(bufStream, "Preferences for FreeHorn");
		bufStream.close();
	}

	void putDoubleProperty(Properties properties, String name, double value)
	{
		properties.setProperty(name, Double.toString(value));

	}
	
	double getDoubleProperty(
		Properties properties,
		String name,
		double defaultValue)
	{
		double value = defaultValue;
		String text = properties.getProperty(name);
		if (text != null)
		{
			value = Double.parseDouble(text);
			System.out.println("Load " + name + " = " + value);
		}
		return value;
	}
	/**
	 * @param file
	 */
	public void load(File file) throws IOException
	{
		saveFile = file;
		FileInputStream inStream = new FileInputStream(file);
		BufferedInputStream bufStream = new BufferedInputStream(inStream);
		Properties properties = new Properties();
		properties.load(bufStream);

		totalLength = getDoubleProperty(properties, "totalLength", totalLength);
		numHarmonics = (int) getDoubleProperty(properties, "numHarmonics", numHarmonics);
		setSectionRatios(properties.getProperty("sectionRatios"));
		restProbability =
			getDoubleProperty(properties, "restProbability", restProbability);
		spectralComplexity =
			getDoubleProperty(
				properties,
				"spectralComplexity",
				spectralComplexity);
		loudness = getDoubleProperty(properties, "loudness", loudness);
		fundamentalFrequency =
			getDoubleProperty(
				properties,
				"fundamentalFrequency",
				fundamentalFrequency);
		durationMean =
			getDoubleProperty(properties, "durationMean", durationMean);
		durationSpread =
			getDoubleProperty(properties, "durationSpread", durationSpread);
		durationMin = getDoubleProperty(properties, "durationMin", durationMin);
		durationMax = getDoubleProperty(properties, "durationMax", durationMax);
		attackMin = getDoubleProperty(properties, "attackMin", attackMin);
		attackMax = getDoubleProperty(properties, "attackMax", attackMax);
		decayMin = getDoubleProperty(properties, "decayMin", decayMin);
		decayMax = getDoubleProperty(properties, "decayMax", decayMax);
		interEventRest =
			getDoubleProperty(properties, "interEventRest", interEventRest);
		replacementAlgorithm = properties.getProperty("replacementAlgorithm");
		
		String psName = properties.getProperty("pitchScalingAlgorithm");
		if (psName != null)
		{
			setPitchScalingAlgorithmByName( psName );
		}
		pitchScalingFactor = getDoubleProperty(properties, "pitchScalingFactor", pitchScalingFactor);

		for(int i = 0; i < 30; i++){
			gain[i] = getDoubleProperty(properties, "gain" + i, gain[0]);
		}

		
		bufStream.close();
	}

	/**
	 * @return
	 */
	public double getAttackMax()
	{
		return attackMax;
	}

	/**
	 * @return
	 */
	public double getAttackMin()
	{
		return attackMin;
	}

	/**
	 * @return
	 */
	public double getDecayMax()
	{
		return decayMax;
	}

	/**
	 * @return
	 */
	public double getDecayMin()
	{
		return decayMin;
	}

	/**
	 * @param d
	 */
	public void setAttackMax(double d)
	{
		attackMax = d;
	}

	/**
	 * @param d
	 */
	public void setAttackMin(double d)
	{
		attackMin = d;
	}

	/**
	 * @param d
	 */
	public void setDecayMax(double d)
	{
		decayMax = d;
	}

	/**
	 * @param d
	 */
	public void setDecayMin(double d)
	{
		decayMin = d;
	}

	/**
	 * @return
	 */
	public double getPitchScalingFactor()
	{
		return pitchScalingFactor;
	}

	/**
	 * @param d
	 */
	public void setPitchScalingFactor(double d)
	{
		pitchScalingFactor = d;
	}

}
