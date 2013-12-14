package org.frogpeak.horn;

public class ExponentialPitchScaling extends PitchScalingAlgorithm
{

	public String getName()
	{
		return "2/(2**(ps*harm))";
	}

	public double calculate(double scalingFactor, int harmonicIndex)
	{
		return 2.0 / Math.pow(2.0, (scalingFactor * harmonicIndex));
	}
}
