package org.frogpeak.horn;
import java.awt.*;
import com.softsynth.jsyn.*;
import com.softsynth.jsyn.view102.*;

/***************************************************************
 * Play notes using a WaveShapingOscillator.
 * Allocate the notes using a BussedVoiceAllocator. 
 */
public class HornSynth extends TJ_Devices
{
	ChebyshevOscAllocator allocator;
	LineOut unitOut;
	EQCircuit eqCircuit;
	SynthScope scope;
	boolean go = false;
	final static int CHEBYSHEV_ORDER = 9;
	double tickPeriod;
	double minRange = 0.03;
	double timeAdvance = 0.25;
	boolean startFlag = false;

	static HornSynth singleton = null;

	/* Can be run as either an application or as an applet. */
	public static void main(String args[])
	{
		HornSynth synth = new HornSynth();

		try
		{
			//synth.start( 16 );
			double time = 0.0;
			double incr = 0.4;
			double freq = 200.0;
			for (int i = 0; i < 8; i++)
			{
				synth.playNote(time, 0.7, freq, 0.5, 0.02, 0.5, 0.5);
				synth.sleepUntil(time);
				time += incr;
				freq *= 4.0 / 3.0;
			}
			synth.sleepUntil(time);
			synth.stop();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		System.exit(0);

	}

	public static synchronized HornSynth getInstance()
	{
		if (singleton == null)
		{
			singleton = new HornSynth();
		}
		return singleton;
	}

	public int getNumVoices()
	{
		return (allocator == null) ? 0 : allocator.getNumVoices();
	}
	/*
	 * Setup synthesis.
	 */
	public void start( int maxNotes,  TJ_Devices devices)
	{

		startFlag = true;
		//TJ_Devices devices = new TJ_Devices();
		//AppletFrame devicesFrame = new AppletFrame( "Test JSyn Devices", devices );
		//devicesFrame.resize( 600, 300 );
		//devicesFrame.show();
		/*
		 * Begin test after frame opened so that DirectSound will use Java
		 * window.
		 */
		//devicesFrame.test();

		/* Start synthesis engine. */
		//Synth.startEngine(0);

		//devices.start();
		//devices.startAudio();

		tickPeriod = 1.0 / Synth.getTickRate();

		// Create a voice allocator and connect it to a LineOut.
		allocator = new ChebyshevOscAllocator( maxNotes, CHEBYSHEV_ORDER, 1024);
		//unitOut = new LineOut();
		//allocator.getOutput().connect(0, unitOut.input, 0);
		//allocator.getOutput().connect(0, unitOut.input, 1);

		//unitOut.start();
		
		eqCircuit = new EQCircuit();
		
		allocator.getOutput().connect( eqCircuit.input );
		
		//MultiplyUnit mult = new MultiplyUnit();
		//allocator.getOutput().connect( eqCircuit.input );
		//mult.inputB.set(1);
		
		//mult.start();

		for( int i = 0; i < devices.outputs.length; i++ )
		{
			//allocator.getOutput().connect( devices.outputs[i].input );
			eqCircuit.output.connect( devices.outputs[i].input );

		}
		
		eqCircuit.start();

		// Show signal on a scope.
		//Frame frame = new Frame("JSyn Scope");
		//frame.setLayout(new BorderLayout());
		//scope = new SynthScope();
		//scope.createProbe(allocator.getOutput(), "Bus out", Color.yellow);
		//scope.finish();
		//scope.hideControls();
		//frame.setBounds(ScreenLayout.scopeRect);
		//frame.add("Center", scope);
		//frame.show();

	}

	public void stop()
	{
		Synth.stopEngine();
	}

	private int secondsToTicks(double seconds)
	{
		return (int) ((Synth.getTickRate() * seconds) + 0.5);
	}

	private double ticksToSeconds(int ticks)
	{
		return tickPeriod * ticks;
	}

	public void playNote(
			double startSeconds,
			double durSeconds,
			double frequency,
			double amplitude,
			double attackTime,
			double decayTime,
			double complexity)
	{
		int startTick = secondsToTicks(startSeconds);
		int durTicks = secondsToTicks(durSeconds);
		int totalTicks = secondsToTicks(durSeconds + decayTime);

		// allocate a new note, stealing one if necessary
		WaveShapingCircuit note =
			(WaveShapingCircuit) allocator.steal(
					startTick,
					//startTick + durTicks);
					startTick + totalTicks + 1);
		//System.out.println( "NumVoices = " + allocator.getNumVoices() );

		// Set waveshaping range of driver.
		double range = minRange + (complexity * (1.0 - minRange));
		note.range.set(range);

		note.setAttackTime( attackTime );
		note.setDecayTime( decayTime );

		// play note using event buffer for accurate timing
		note.noteOnFor(startTick, durTicks, frequency, amplitude);

	}
	
	public void updateEq(int i, double v){
		if(startFlag){
			eqCircuit.gain[i].set(v);
		}
	}

	/**
	 * @return
	 */
	public double getSeconds()
	{
		return ticksToSeconds(Synth.getTickCount());
	}

	/**
	 * 
	 */
	public void sleepUntil(double seconds) throws InterruptedException
	{
		double earlyWakeUp = seconds - timeAdvance;
		while (earlyWakeUp > getSeconds())
		{
			int msec = (int) ((1000.0 * (earlyWakeUp - getSeconds())) + 0.5);
			if (msec < 1)
				msec = 1;
			Thread.sleep(msec);
		}
	}

	public double getCPULoad()
	{
		return Synth.getUsage();
	}

}