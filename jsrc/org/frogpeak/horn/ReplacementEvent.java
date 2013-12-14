package org.frogpeak.horn;

/**
 * @author Phil Burk (C) 2004
 *
 */
public class ReplacementEvent
{
	int newSection;
	int newHarmonic;
	int oldSection;
	int oldHarmonic;
	int action;
	final static int STARTING = 0;
	final static int REPLACING = 1;
	final static int STOPPING = 2;

	ReplacementEvent(
		int action,
		int oldSection,
		int oldHarmonic,
		int newSection,
		int newHarmonic)
	{
		this.action = action;
		this.newSection = newSection;
		this.oldSection = oldSection;
		this.newHarmonic = newHarmonic;
		this.oldHarmonic = oldHarmonic;
	}
}
