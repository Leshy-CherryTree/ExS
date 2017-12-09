/****************************************/
/* Synth.java							*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package synth;


import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.FilterStateVariable;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.MixerMono;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitOscillator;



/**
 * Play a tone using a JSyn oscillator.
 * 
 * @author Phil Burk (C) 2010 Mobileer Inc
 */
public class Synth
{

	Synthesizer synth;
	
	UnitOscillator osc1;
	UnitOscillator osc2;
	
	MixerMono mixer;
	
	LineOut lineOut;
	
	FilterStateVariable filter;

	private void test()
	{

		// Create a context for the synthesizer.
		synth = JSyn.createSynthesizer();

		// Start synthesizer using default stereo output at 44100 Hz.
		synth.start();

		// Add a tone generator.
		synth.add(osc1 = new SineOscillator());
		synth.add(osc2 = new SineOscillator());
		
		synth.add(mixer = new MixerMono(2));
		
		// Add a stereo audio output unit.
		synth.add(lineOut = new LineOut());
		
		synth.add(filter = new FilterStateVariable());

//		// Connect the oscillator to both channels of the output.
//		osc1.output.connect(osc2.amplitude);
		
//		//osc1.output.connect(lineOut.input);
//		osc2.output.connect(lineOut.input);

		osc1.output.connect(0, mixer.input, 0);
		osc2.output.connect(0, mixer.input, 1);
		
		mixer.output.connect(filter.input);
		filter.bandPass.connect(lineOut.input);
		
		filter.frequency.set(500);
		filter.amplitude.set(0.7);
		filter.resonance.set(0.1);

		// Set the frequency and amplitude for the sine wave.
		osc1.frequency.set(145.0);
		osc1.amplitude.set(0.1);
		
		osc2.frequency.set(345.0);
		osc2.amplitude.set(1.0);

		// We only need to start the LineOut. It will pull data from the
		// oscillator.
		lineOut.start();

		System.out.println("You should now be hearing a sine wave. ---------");

		// Sleep while the sound is generated in the background.
		try
		{
			double time = synth.getCurrentTime();
			System.out.println("time = " + time);
			// Sleep for a few seconds.
			synth.sleepUntil(time + 2.0);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		System.out.println("Stop playing. -------------------");
		// Stop everything.
		synth.stop();
	}

	public static void main(String[] args)
	{
		new Synth().test();
	}
}
