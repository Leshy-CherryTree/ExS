/****************************************/
/* AmpModule.java						*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.Synthesizer;

import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.MixerMono;
import com.jsyn.unitgen.Multiply;

import eu.cherrytree.synth.Voice;
import eu.cherrytree.synth.types.LFOType;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class AmpModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	private MixerMono mixer;
	private Multiply multiply;
	private LFOModule lfo;
	
	private float amplitude = 1.0f;
	
	//--------------------------------------------------------------------------

	public AmpModule(Synthesizer synth, int voiceCount)
	{
		super(synth);
		
		mixer = new MixerMono(voiceCount);
		synth.add(mixer);	
		
		multiply = new Multiply();
		synth.add(multiply);
		
		lfo = new LFOModule(synth);
		
		multiply.inputA.set(1.0f);		
		multiply.inputB.set(amplitude);
		
		mixer.amplitude.connect(multiply.output);
	}
	
	//--------------------------------------------------------------------------
	
	public void setAmplitude(float amplitude)
	{
		this.amplitude = amplitude;
		
		if (lfo.getType() != LFOType.Off)
			multiply.inputA.set(1.0f);
		
		multiply.inputB.set(amplitude);
	}
	
	//--------------------------------------------------------------------------
	
	public float getAmplitude()
	{
		return amplitude;
	}
	
	//--------------------------------------------------------------------------

	public LFOModule getLFO()
	{
		return lfo;
	}
	
	//--------------------------------------------------------------------------
	
	public void rebuild(Voice[] voices)
	{
		lfo.rebuild();
		
		mixer.input.disconnectAll();
		mixer.output.disconnectAll();
		
		multiply.inputA.disconnectAll();
		
		for (int i = 0 ; i < voices.length ; i++)
			voices[i].getOutput().connect(0, mixer.input, i);
		
		if (lfo.getType() != LFOType.Off)
			lfo.getOutput().connect(multiply.inputA);
		else
			multiply.inputA.set(1.0f);				
	}
	
	//--------------------------------------------------------------------------
	
	public UnitOutputPort getOutput()
	{
		return mixer.getOutput();
	}
	
	//--------------------------------------------------------------------------
}
