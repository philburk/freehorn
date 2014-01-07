package org.frogpeak.horn;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Line.Info;

import com.softsynth.jsyn.*;

/**
 * Test Hardware Audio Device Query and Selection
 * <P>
 * This Applet provides a section of available devices by name. The maximum
 * number of channels is given in square bracket, eg. "[2]". If both input and
 * output are started, then the input is connected to output. If only output is
 * selected, then sine waves are connected to the outputs.
 * <P>
 * The underlying device interface is based on the PortAudio library which is
 * available at http://www.portaudio.com
 * 
 * @author (C) 2001-2007 Phil Burk, Mobileer Inc, All Rights Reserved
 */

public class TJ_Devices extends Applet
{

	FreeHorn freeHorn;
	// Arrays to hold units for multiple channels.
	ChannelIn inputs[];
	ChannelOut outputs[];
	SineOscillator sines[];
	DeviceSelector inDevSel;
	DeviceSelector outDevSel;
	Button startButton;
	Button stopButton;
	public final static int INPUT_MODE = 0;
	public final static int OUTPUT_MODE = 1;

	/* Can be run as either an application or as an applet. */
	public static void main( String args[] )
	{
		TJ_Devices applet = new TJ_Devices();
		AppletFrame frame = new AppletFrame( "Audio Settings", applet );
		frame.resize( 600, 300 );
		frame.show();
		/*
		 * Begin test after frame opened so that DirectSound will use Java
		 * window.
		 */
		frame.test();
	}

	/*
	 * Setup synthesis.
	 */
	public void start(FreeHorn fH)
	{
		freeHorn = fH;
		setLayout( new BorderLayout() );
		Panel centerPanel = new Panel();
		//centerPanel.setLayout( new GridLayout( 2, 1 ) );
		centerPanel.setLayout( new GridLayout( 1, 1 ) );
		add( BorderLayout.CENTER, centerPanel );

		// Make sure we are using the necessary version of JSyn.
		// We are using the new V144 latency methods.
		Synth.requestVersion( 144 );

		// Synth.setTrace( Synth.VERBOSE );
		try
		{
			// Create a unique SynthContext so that this Applet will not
			// interfere with other Applets. */
			Synth.initialize();

			// Create objects that select and enable a device.
			//inDevSel = new DeviceSelector( "Input", INPUT_MODE );
			inDevSel = new DeviceSelector( "Input", INPUT_MODE, this);
			//centerPanel.add( inDevSel = new DeviceSelector( "Input", INPUT_MODE, this) );
			centerPanel.add( outDevSel = new DeviceSelector( "Output", OUTPUT_MODE, this) );

			// Add buttons to start and stop engine using selected devices.
			/*Panel bottomPanel = new Panel();
			bottomPanel.setLayout( new GridLayout( 1, 2 ) );
			add( BorderLayout.SOUTH, bottomPanel );
			bottomPanel.add( startButton = new Button( "Start" ) );
			startButton.addActionListener( new ActionListener()
			{
				public void actionPerformed( ActionEvent e )
				{
					stopAudio();
					startAudio();
				}
			} );

			bottomPanel.add( stopButton = new Button( "Stop" ) );
			stopButton.addActionListener( new ActionListener()
			{
				public void actionPerformed( ActionEvent e )
				{
					stopAudio();
				}
			} );*/

		} catch( SynthException e )
		{
			SynthAlert.showError( this, e );
		}

		getParent().validate();
		getToolkit().sync();
	}

	void startAudio()
	{

		int numIn, numOut;

		try
		{

			Synth.getSharedContext().setSuggestedInputLatency( inDevSel
					.getSuggestedLatencyMSec() * 0.001 );
			Synth.getSharedContext().setSuggestedOutputLatency( outDevSel
					.getSuggestedLatencyMSec() * 0.001 );

			/*
			 * Start synthesis engine. Use half default frame rate because many
			 * PC cards do not support FullDuplex operation at the full sample
			 * rate. Use devices and numChannels from DeviceSelectors. We don't
			 * need to set Synth.FLAG_ENABLE_INPUT because we pass devices
			 * explicitly.
			 */
			int inDevID = inDevSel.getDeviceID();
			int outDevID = outDevSel.getDeviceID();
			Synth.start( 0, Synth.DEFAULT_FRAME_RATE, inDevID, inDevSel
					.getNumChannels( inDevID ), outDevID, outDevSel
					.getNumChannels( outDevID ) );

			// Open all of the channels available on the input device.
			/*numIn = inDevSel.getNumChannels( inDevID );
			if( numIn > 0 )
			{
				inputs = new ChannelIn[numIn];
				for( int i = 0; i < numIn; i++ )
				{
					inputs[i] = new ChannelIn( Synth.getSharedContext(), i );
					inputs[i].start();
				}
			}*/

			// Open all of the channels available on the output device.
			numOut = outDevSel.getNumChannels( outDevID );
			if( numOut > 0 )
			{
				outputs = new ChannelOut[numOut];

				for( int i = 0; i < numOut; i++ )
				{
					outputs[i] = new ChannelOut( Synth.getSharedContext(), i );
					outputs[i].start();
				}
			}

		} catch( SynthException e )
		{
			SynthAlert.showError( this, e );
		}

		inDevSel.started();
		outDevSel.started();

		getParent().validate();
		getToolkit().sync();

		HornSynth.getInstance().start( freeHorn.model.getMaxPolyphony(), this);

		freeHorn.finalizeSetup();
	}

	public void stop()
	{
		try
		{
			stopAudio();
			Synth.getSharedContext().delete();
		} catch( SynthException e )
		{
			SynthAlert.showError( this, e );
		}
	}

	/**
	 * Delete an array of units.
	 */
	void killUnits( SynthUnit units[] )
	{
		if( units != null )
		{
			for( int i = 0; i < units.length; i++ )
			{
				units[i].stop();
				units[i].delete();
			}
		}
	}

	void stopAudio()
	{
		inDevSel.stopped();
		outDevSel.stopped();

		killUnits( outputs );
		outputs = null;
		killUnits( inputs );
		inputs = null;
		killUnits( sines );
		sines = null;

		Synth.stop();

		getParent().validate();
		getToolkit().sync();

		freeHorn.disposeGui();
	}

	class DeviceMenuItem
	{
		String name;
		int maxChannels;
		int devID;

		DeviceMenuItem(int pDevID, String pName, int pMaxChannels)
		{
			devID = pDevID;
			name = pName;
			maxChannels = pMaxChannels;
		}

		public String toString()
		{
			return name + " [" + maxChannels + "]";
		}

		int getDeviceID()
		{
			return devID;
		}
	}

	/**
	 * Puts up a menu that allows the user to select an available device
	 * and control the number of channels and the latency.
	 */
	class DeviceSelector extends Panel implements ItemListener
	{
		String text;
		//Label currentNameLabel;
		Label currentLatencyLabel;
		Choice deviceNames;
		Checkbox devEnable;
		//TextField numChannelText;
		TextField suggestedLatencyText;
		int mode;
		Hashtable namesToDevice;
		private final static String ACTUAL_LATENCY_TEXT = "Actual Latency (msec) = ";

		Panel topPanel;
		Panel bottomPanel;
		Panel middlePanel;

		Checkbox[] connectionArray;
		TJ_Devices devices;

		public DeviceSelector(String text, int mode, TJ_Devices devices)
		{
			this.text = text;
			this.mode = mode;
			this.devices = devices;
			namesToDevice = new Hashtable();

			setLayout( new GridLayout( 3, 1 ) );

			topPanel = new Panel();

			topPanel
			.add( devEnable = new Checkbox( text, (mode == OUTPUT_MODE) ) );
			deviceNames = new Choice();
			deviceNames.addItemListener(this);
			topPanel.add( deviceNames );

			int defaultDeviceID = (mode == OUTPUT_MODE) ? AudioDevice.getDefaultOutputDeviceID()
					: AudioDevice.getDefaultInputDeviceID();
			int defaultSelection = 0;
			// Loop though all of the available devices and fill
			// the choice menu with valid options.
			for( int i = 0; i < AudioDevice.getNumDevices(); i++ )
			{
				int maxChannels = getMaxChannels( i );
				if( maxChannels > 0 )
				{
					DeviceMenuItem item = new DeviceMenuItem( i, AudioDevice
							.getName( i ), maxChannels );
					deviceNames.addItem( item.toString() );
					namesToDevice.put( item.toString(), item );
					if( i == defaultDeviceID )
					{
						defaultSelection = deviceNames.getItemCount() - 1;
					}
				}
			}
			deviceNames.select( defaultSelection );

			//topPanel.add( new Label( "Channels to use =" ) );
			//topPanel.add( numChannelText = new TextField( "10", 3 ) );
			//topPanel.add( currentNameLabel = new Label( "Stopped" ) );
			add( topPanel );

			middlePanel = new Panel();
			add( middlePanel );

			bottomPanel = new Panel();
			bottomPanel.add( new Label( "Suggested Latency (msec) =" ) );
			bottomPanel.add( suggestedLatencyText = new TextField( "100", 8 ) );
			bottomPanel.add( currentLatencyLabel = new Label(
			"Actual (msec) = ?" ) );

			add( bottomPanel );

			buildChannelArray();
		}

		/* Set label to indicate state. */
		public void started()
		{
			if( devEnable.getState() )
			{
				//currentNameLabel.setText( "Started" );

				double latency = (mode == INPUT_MODE) ? Synth.getSharedContext()
						.getInputLatency() : Synth.getSharedContext().getOutputLatency();
						int msec = (int) Math.round( latency * 1000.0 );
						currentLatencyLabel.setText( ACTUAL_LATENCY_TEXT + msec );
						validate();
			}
			//numChannelText.setEnabled( false );
			suggestedLatencyText.setEnabled( false );
		}

		public void stopped()
		{
			//currentNameLabel.setText( "Stopped" );
			currentLatencyLabel.setText( ACTUAL_LATENCY_TEXT + "?" );
			//numChannelText.setEnabled( true );
			suggestedLatencyText.setEnabled( true );
		}

		public int getMaxChannels( int id )
		{
			int maxChannels = (mode == INPUT_MODE) ? AudioDevice
					.getMaxInputChannels( id ) : AudioDevice
					.getMaxOutputChannels( id );
					return maxChannels;
		}

		/*public void setNumChannels( int numChannels )
		{
				numChannelText.setText( "" + numChannels );
		}*/

		public int getSuggestedLatencyMSec()
		{
			int val = 100;
			try
			{
				val = Integer.parseInt( suggestedLatencyText.getText() );
			} catch( NumberFormatException e )
			{
				suggestedLatencyText.setText( "" + val );
			}
			return val;
		}

		/*private int parseChannelText()
		{
			int val = 2;
			try
			{
				val = Integer.parseInt( numChannelText.getText() );
			} catch( NumberFormatException e )
			{
				setNumChannels( val );
			}
			return val;
		}*/

		public int getNumChannels( int id )
		{
			int numChannels = 0;

			if( id != Synth.NO_DEVICE )
			{

				numChannels = getMaxChannels( id );

				/*numChannels = getMaxChannels( id );
				numChannels = parseChannelText();
				int maxChannels = getMaxChannels( id );
				if( (numChannels <= 0) || (numChannels > maxChannels) )
				{
					numChannels = maxChannels;
					setNumChannels( numChannels );
				}*/
			}
			return numChannels;
		}

		public int getSelectedDeviceID()
		{
			DeviceMenuItem item = (DeviceMenuItem) namesToDevice
			.get( deviceNames.getSelectedItem() );
			return item.getDeviceID();
		}

		public int getDeviceID()
		{
			return devEnable.getState() ? getSelectedDeviceID()
					: Synth.NO_DEVICE;
		}

		public void buildChannelArray(){
			middlePanel.removeAll();
			connectionArray = new Checkbox[getMaxChannels(getSelectedDeviceID())];
			for(int i = 0; i < connectionArray.length; i++){
				connectionArray[i] = new Checkbox("" + (i+1), true);
				connectionArray[i].addItemListener(this);
				//connectionArray[i].setState(false);
				middlePanel.add(connectionArray[i] );
			}

			middlePanel.validate();
			//devices.stopAudio();
			//devices.startAudio();
		}


		public void itemStateChanged(ItemEvent e) {
			if(e.getItemSelectable() == deviceNames){
				buildChannelArray();

				devices.stopAudio();
				devices.startAudio();

			} else {
				int channel = Integer.parseInt( ((Checkbox) e.getItemSelectable()).getLabel() ) - 1;
				if(((Checkbox) e.getItemSelectable()).getState()){
					outputs[channel].start();
				} else {
					outputs[channel].stop();
				}
			}
		}
	}
}
