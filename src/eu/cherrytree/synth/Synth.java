/****************************************/
/* Synth.java							*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;

import eu.cherrytree.synth.modules.AmpModule;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class Synth
{
	//--------------------------------------------------------------------------
	
	private Synthesizer synthesizer;
	private LineOut lineOut;
	private AmpModule amp;
	
	private Voice[] voices;

	//--------------------------------------------------------------------------
	
	public Synth(int voiceCount)
	{
		synthesizer = JSyn.createSynthesizer();
		synthesizer.start();
		
		voices = new Voice[voiceCount];
		
		for (int i = 0 ; i < voiceCount ; i++)
			voices[i] = new Voice(synthesizer);
		
		amp = new AmpModule(synthesizer, voiceCount);		
		amp.getOutput().connect(lineOut.input);
		
		synthesizer.add(lineOut);
	}
	
	//--------------------------------------------------------------------------
}
