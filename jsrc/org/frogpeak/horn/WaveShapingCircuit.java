package org.frogpeak.horn;
import com.softsynth.jsyn.*;

/** WaveShaping sound circuit. Table index is driven by an internal sine wave
 * to generate an output.
 * 
 * @author (C) 1997 Phil Burk, SoftSynth.com, All Rights Reserved
 * @see JSynExamples.TJ_ChebyshevSong
**/
public class WaveShapingCircuit extends SynthNote
{
	SineOscillator unitOsc; /* used to scan small the table */
	EnvelopePlayer rangeEnv;
	EnvelopePlayer ampEnv;
	SynthEnvelope rangeEnvData;
	SynthEnvelope ampEnvData;
	WaveShaper unitWaveShaper;
	SynthInput range;
	double[] ampData = { 0.02, 0.9, /* duration,value pair for frame[0] */
		0.50, 0.0, /* duration,value pair for frame[1] */
	};

	public WaveShapingCircuit(SynthTable table) throws SynthException
	{
		add(unitWaveShaper = new WaveShaper());
		add(unitOsc = new SineOscillator());
		add(rangeEnv = new EnvelopePlayer());
		add(ampEnv = new EnvelopePlayer());

		unitWaveShaper.tablePort.setTable(table);
		// Create a range envelope and fill it with data. */
		double[] rangeData = { 0.02, 1.00, /* duration,value pairs */
			0.20, 0.20, 0.20, 0.30, 2.00, 0.01, };
		rangeEnvData = new SynthEnvelope(rangeData);
		rangeEnvData.setSustainLoop(2, 2 + 2);

		// Create an amplitude envelope and fill it with data.
		ampEnvData = new SynthEnvelope(ampData);
		// stop after attack
		ampEnvData.setSustainLoop(1,1);

		addPort(frequency = unitOsc.frequency);
		addPort(amplitude = ampEnv.amplitude);
		addPort(output = unitWaveShaper.output);
		addPort(range = rangeEnv.amplitude);

		/* Connect units */
		rangeEnv.output.connect(unitOsc.amplitude);
		ampEnv.output.connect(unitWaveShaper.amplitude);
		unitOsc.output.connect(unitWaveShaper.input);

		frequency.setup(0.0, 200.0, 800.0);
		amplitude.setup(0.0, 0.5, 1.0);
	}
	public void setStage(int time, int stage) throws SynthException
	{
		switch (stage)
		{
			case 0 :
				//			stop( time );
				rangeEnv.envelopePort.clear(time);
				ampEnv.envelopePort.clear(time);
				rangeEnv.envelopePort.queueOn(time, rangeEnvData);
				ampEnv.envelopePort.queueOn(time, ampEnvData);
				start(time);
				break;
			case 1 :
				rangeEnv.envelopePort.queueOff(time, rangeEnvData);
				// Set AutoStop feature of amp envelope so that unit stops when data gone.
				ampEnv.envelopePort.queueOff(time, ampEnvData, true);
				break;
		}
	}

	private void setAmplitudeFrameDuration(int frameIndex, double duration)
	{
		ampData[frameIndex * 2] = duration;
		ampEnvData.write(ampData);
	}
	public void setAttackTime(double duration)
	{
		setAmplitudeFrameDuration(0, duration);
	}
	public void setDecayTime(double duration)
	{
		setAmplitudeFrameDuration(1, duration);
	}

}
