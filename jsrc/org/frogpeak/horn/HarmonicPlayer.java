package org.frogpeak.horn;

/**
 * Play one harmonic of a section.
 * @author Phil Burk (C) 2003
 */
public class HarmonicPlayer extends HornThread
{
	HornModel model;
	int sectionIndex;
	int harmonicIndex;

	/**
	 * @param model
	 * @param sectionIndex
	 * @param harmonicIndex
	 */
	public HarmonicPlayer(
		HornModel pModel,
		int pSectionIndex,
		int pHarmonicIndex)
	{
		model = pModel;
		sectionIndex = pSectionIndex;
		harmonicIndex = pHarmonicIndex;
	}

	public double getFrequency()
	{
		return model.calculateFrequency(sectionIndex, harmonicIndex);
	}

	private double playNextNote(double nextTime)
	{
		// get value used to scale all times
		double pitchScaler =
			model.getPitchScalingAlgorithm().calculate(
				model.getPitchScalingFactor(),
				harmonicIndex);

		double duration =
			pitchScaler
				* MathTools.nextGaussian(
					model.getDurationMean(),
					model.getDurationRange(),
					model.getDurationMin(),
					model.getDurationMax());

		// Use scaled duration values for the rest calculation.
		double ier = model.getInterEventRest();
		double interEventTime =
			pitchScaler
				* MathTools.nextGaussian(
					ier * model.getDurationMean(),
					ier * model.getDurationRange(),
					ier * model.getDurationMin(),
					ier * model.getDurationMax());

		double odds = Math.random();
		if (odds > model.getRestProbability())
		{
			double attackTime =
				pitchScaler
					* randomRange(model.getAttackMin(), model.getAttackMax());
			double decayTime =
				pitchScaler
					* randomRange(model.getDecayMin(), model.getDecayMax());
			double rampTime = attackTime + decayTime;
			// middle of note where amplitude is held
			double holdTime = duration - rampTime;
			// do we fit within duration?
			if (holdTime < 0)
			{
				holdTime = 0.0;
				// scale attack and decay so they fit within duration
				double scaler = duration / rampTime;
				attackTime *= scaler;
				decayTime *= scaler;
			}
			//System.out.println("attackTime = " + attackTime + ", decayTime = " + decayTime  );
			double noteOnTime = attackTime + holdTime;
			HornSynth.getInstance().playNote(
				nextTime,
				noteOnTime,
				getFrequency(),
				0.15 * model.getLoudness(),
				attackTime,
				decayTime,
				model.getSpectralComplexity());
		}
		return nextTime + duration + interEventTime;
	}

	/**
	 * @param min
	 * @param max
	 * @return
	 */
	private double randomRange(double min, double max)
	{
		double range = max - min;
		if (range < 0.0)
			return min;
		else
			return (range * Math.random()) + min;
	}

	public void run()
	{
		double nextTime = getStartTime();
		while (go)
		{
			nextTime = playNextNote(nextTime);
			try
			{
				HornClock.sleepUntil(nextTime);
			} catch (InterruptedException e)
			{
			}
		}
	}
	/**
	 * @return
	 */
	public int getSectionIndex()
	{
		return sectionIndex;
	}

	/**
	 * @return
	 */
	public int getHarmonicIndex()
	{
		return harmonicIndex;
	}

}
