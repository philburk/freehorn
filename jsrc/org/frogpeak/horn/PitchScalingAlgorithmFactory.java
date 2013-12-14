package org.frogpeak.horn;

/**
 * @author Phil Burk (C) 2003
 */
public class PitchScalingAlgorithmFactory
{
	static String[] names =
		{
			new PitchScalingAlgorithm().getName(),
			new SimplePitchScaling().getName(),
			new ExponentialPitchScaling().getName(),
			new StochasticPitchScaling().getName()};

	/** Return array of name strings for possible replacement algorithms. */
	public static String[] getChoices()
	{
		return names;
	}

	public static PitchScalingAlgorithm createPitchScalingAlgorithm(String name)
	{
		if (name.equals(new PitchScalingAlgorithm().getName()))
		{
			return new PitchScalingAlgorithm();
		} else if (name.equals(new ExponentialPitchScaling().getName()))
		{
			return new ExponentialPitchScaling();
		} else if (name.equals(new SimplePitchScaling().getName()))
		{
			return new SimplePitchScaling();
		} else if (name.equals(new StochasticPitchScaling().getName()))
		{
			return new StochasticPitchScaling();
		} else
			throw new RuntimeException(
				"Invalid PitchScalingAlgorithm = " + name);

	}
}
