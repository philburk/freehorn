package org.frogpeak.horn;

public class SimplePitchScaling extends PitchScalingAlgorithm
{

	public String getName()
	{
		return "2/harm";
	}

	public double calculate(double scalingFactor, int harmonicIndex)
	{
		return 2.0 / harmonicIndex;
	}
}
