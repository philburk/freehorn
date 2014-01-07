package org.frogpeak.horn;

/** 
 * Demonstrate JSyn's Envelope Editor
 *
 * @author Copyright (C) 2000 Phil Burk, All Rights Reserved
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import com.softsynth.jsyn.*;
import com.softsynth.jsyn.view11x.*;

public class TJ_EnvEdit1 extends Applet implements EditListener
{
	LineOut           lineOut;
	SineOscillator    osc;
	EnvelopeEditor    envEditor;
	SynthEnvelope     envelope;
	EnvelopePlayer    envPlayer;
	EnvelopePoints    points;
	final int         MAX_FRAMES = 16;
	Button            queueButton;
	Button            queueLoopButton;
	Button            clearButton;
	boolean           looping = false;

/* Can be run as either an application or as an applet. */
    public static void main(String args[])
	{
		TJ_EnvEdit1 applet = new TJ_EnvEdit1();
		AppletFrame frame = new AppletFrame("Test EnvelopeEditor", applet);
		frame.resize(600,400);
		frame.show();
/* Begin test after frame opened so that DirectSound will use Java window. */
		frame.test();
	}

/*
 * Setup synthesis.
 */
	public void start()  
	{
/* Make sure we are using the necessary version of JSyn */
		Synth.requestVersion( 141 );
		
		setLayout( new BorderLayout() );
		try
		{
/* Start synthesis engine. */
		Synth.startEngine( 0 );

		envelope = new SynthEnvelope( MAX_FRAMES );
		envPlayer = new EnvelopePlayer();
		osc = new SineOscillator();
		lineOut = new LineOut();

// Connect envelope to oscillator amplitude.
		envPlayer.output.connect( 0, osc.amplitude, 0 );
// Connect oscillator to output.
		osc.output.connect( 0, lineOut.input, 0 );
		osc.output.connect( 0, lineOut.input, 1 );

		osc.frequency.set(400.0);

// Add an envelope editor to the center of the panel.
		add( "Center", envEditor = new EnvelopeEditor() );
		envEditor.setBackground( Color.cyan.brighter() );
// Ask editor to inform us when envelope modified.
		envEditor.addEditListener( this );
		
// Create vector of points for editor.
		points = new EnvelopePoints();
		points.setName( osc.amplitude.getName() );
		
// Setup initial envelope shape.
		points.add( 0.5, 1.0 );
		points.add( 0.5, 0.2 );
		points.add( 0.5, 0.8 );
		points.add( 0.5, 0.0 );
		updateEnvelope();

// Tell editor to use these points.
		envEditor.setPoints( points ); 
		envEditor.setMaxPoints( MAX_FRAMES );
				
// Setup Queue buttons for triggering envelope.
		Panel buttonPanel = new Panel();
		add( buttonPanel, "South" );
		
		buttonPanel.add( queueButton = new Button("Queue") );
		queueButton.addActionListener(	new ActionListener() {
   				public void actionPerformed(ActionEvent e)
   				{ doQueue();	} } );

		buttonPanel.add( queueLoopButton = new Button("QueueLoop") );
		queueLoopButton.addActionListener(	new ActionListener() {
   				public void actionPerformed(ActionEvent e)
   				{ doQueueLoop();	} } );
		
		buttonPanel.add( clearButton = new Button("Clear") );
		clearButton.addActionListener(	new ActionListener() {
   				public void actionPerformed(ActionEvent e)
   				{ doClear();	} } );

/* Start execution of units. */
		envPlayer.start();
		lineOut.start();
		osc.start();
	
		} catch (SynthException e) {
			SynthAlert.showError(this,e);
		}

/* Synchronize Java display. */
		getParent().validate();
		getToolkit().sync();
	}
	
	public void stop()
	{
		try
		{
/* Delete unit peers. */
			lineOut.delete();
			lineOut = null;
			osc.delete();
			osc = null;
			envPlayer.delete();
			envPlayer = null;
			removeAll(); // remove portFaders
/* Turn off tracing. */
			Synth.setTrace( Synth.SILENT );
/* Stop synthesis engine. */
			Synth.stopEngine();

		} catch (SynthException e) {
			SynthAlert.showError(this,e);
		}
	}

/** This is called by the EnvelopeEditor when the envelope is modified.
 * If we are playing a loop, update envelope and requeue in case length changed. */
	public void objectEdited( Object editor, Object objPoints )
	{
		if( looping )
		{
			doQueueLoop();
		}
	}
	
/** The editor works on a vector of points, not a real envelope.
 * The data must be written to a real SynthEnvelope in order to use it.
 */
	public void updateEnvelope()
	{
		int numFrames = points.size();
		for( int i=0; i<numFrames; i++ )
		{
			envelope.write( i, points.getPoint( i ), 0, 1 );
		}
	}
/** Play the edited envelope once.
 */
	public void doQueue()
	{
		updateEnvelope();
		envPlayer.envelopePort.queue( envelope, 0, points.size() );
		looping = false;
	}
	
/** Play the edited envelope repeatedly.
 */
	public void doQueueLoop()
	{
		updateEnvelope();
		envPlayer.envelopePort.queueLoop( envelope, 0, points.size() );
		looping = true;
	}
	
/** Clear the envelope queue which will cause the value to freeze.
 */
	public void doClear()
	{
		envPlayer.envelopePort.clear();
		looping = false;
	}

}
