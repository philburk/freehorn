package org.frogpeak.horn;

import com.softsynth.jsyn.Filter_BandStop;
import com.softsynth.jsyn.Filter_PeakingEQ;
import com.softsynth.jsyn.LinearLag;
import com.softsynth.jsyn.SynthCircuit;
import com.softsynth.jsyn.SynthException;
import com.softsynth.jsyn.SynthInput;
import com.softsynth.jsyn.SynthVariable;

public class EQCircuit extends SynthCircuit{
	
	Filter_PeakingEQ[] passBands = new Filter_PeakingEQ[30];
	LinearLag[] lag = new LinearLag[30];
	SynthInput input;
	SynthVariable[] gain = new SynthVariable[30];
//	SynthVariable gain0;
//	SynthVariable gain1;
//	SynthVariable gain2;
//	SynthVariable gain3;
//	SynthVariable gain4;
//	SynthVariable gain5;
//	SynthVariable gain6;
//	SynthVariable gain7;
//	SynthVariable gain8;
//	SynthVariable gain9;
	
	//MultiplyUnit mult = new MultiplyUnit();
	//Filter_BandStop test = new Filter_BandStop();
	
	
	public EQCircuit() throws SynthException
	{
		for(int i = 0; i < passBands.length; i++){
			add(passBands[i] = new Filter_PeakingEQ());
			add(lag[i] = new LinearLag());
			if(i > 0){
				passBands[i-1].output.connect(passBands[i].input);
			}
			lag[i].output.connect(passBands[i].gain);
			lag[i].time.set(.01);
			lag[i].input.set(1);
			passBands[i].frequency.set(Math.pow(2, 5+i/3.));
			passBands[i].gain.set(1);
			passBands[i].amplitude.set(1);
			//passBands[i].start();
			
			addPort(gain[i] = lag[i].input);
		}
		
//		addPort(lag[0].input);
//		addPort(gain1 = lag[1].input);
//		addPort(gain2 = lag[2].input);
//		addPort(gain3 = lag[3].input);
//		addPort(gain4 = lag[4].input);
//		addPort(gain5 = lag[5].input);
//		addPort(gain6 = lag[6].input);
//		addPort(gain7 = lag[7].input);
//		addPort(gain8 = lag[8].input);
//		addPort(gain9 = lag[9].input);
		
		addPort(input = passBands[0].input);
//		addPort(input = mult.inputA);
//		mult.inputB.set(1);
//		addPort(output = mult.output);
//		mult.start();
		addPort(output = passBands[passBands.length-1].output);
		
//		test.fregainuency.set(250);
//		test.gain.set(1);
//		test.amplitude.set(1.);
//		addPort(input = test.input);
//		addPort(output = test.output);
//		test.start();
		
	}

}
