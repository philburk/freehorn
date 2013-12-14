package org.frogpeak.horn;

import java.util.Random;

/**
 * @author Phil Burk (C) 2003
 */
public class MathTools
{
	static Random random = new Random();
	
	/** Scales the normalized Gaussian. */
	public static double nextGaussian( double mean, double range, double min, double max )
	{
		double gauss = (random.nextGaussian() * range) + mean;
		if( gauss > max ) gauss = max;
		else if( gauss < min ) gauss = min;
		return gauss;
	}
}
