package org.frogpeak.horn;

public class StochasticPitchScaling extends PitchScalingAlgorithm
{

	public String getName()
	{
		return "maybe 2/harm";
	}

	public double calculate(double scalingFactor, int harmonicIndex)
	{
		if (Math.random() < scalingFactor)
		{
			return 2.0 / harmonicIndex;
		} else
		{
			return 1.0;
		}
	}
}
