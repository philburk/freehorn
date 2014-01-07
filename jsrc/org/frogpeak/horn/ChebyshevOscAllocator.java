package org.frogpeak.horn;

import com.softsynth.jsyn.*;
import com.softsynth.jsyn.util.*;
import com.softsynth.math.*;


/* Allocate Chebyshev based WaveShapingOscillators */
class ChebyshevOscAllocator extends BussedVoiceAllocator
{
	PolynomialTableData chebData;
	SynthTable table;
	SynthContext synthContext;

	public ChebyshevOscAllocator( int maxVoices,
				int order, int numFrames ) throws SynthException
	{
		super( maxVoices );
	// make table with Chebyshev polynomial to share among voices
		PolynomialTableData chebData =
		          new PolynomialTableData(ChebyshevPolynomial.T(order), numFrames);
		table = new SynthTable( chebData.getData() );
	}
	
	public SynthCircuit makeVoice() throws SynthException
	{
		WaveShapingCircuit circ = new WaveShapingCircuit( table);
		circ.amplitude.set( 1.0 / getMaxVoices() );
		return (SynthCircuit) addVoiceToMix( circ );
	}
}
