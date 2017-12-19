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

import eu.cherrytree.synth.Voice;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class AmpModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	private MixerMono mixer;
	private LFOUnitGeneratorModule lfoModule;
	
	private float amplitude = 1.0f;
	
	//--------------------------------------------------------------------------

	public AmpModule(Synthesizer synth, int voiceCount)
	{
		super(synth);
		
		mixer = new MixerMono(voiceCount);
		synth.add(mixer);	
		
		lfoModule = new LFOUnitGeneratorModule(synth, mixer.amplitude, amplitude, 0.5f);
	}
	
	//--------------------------------------------------------------------------
	
	public void setAmplitude(float amplitude)
	{
		this.amplitude = amplitude;
		
		lfoModule.setValue(amplitude);
	}
	
	//--------------------------------------------------------------------------
	
	public float getAmplitude()
	{
		return amplitude;
	}
	
	//--------------------------------------------------------------------------

	public LFOModule getLFO()
	{
		return lfoModule.getLFO();
	}
	
	//--------------------------------------------------------------------------
	
	public void rebuild(Voice[] voices)
	{
		mixer.input.disconnectAll();
		mixer.output.disconnectAll();
		
		for (int i = 0 ; i < voices.length ; i++)
			voices[i].getOutput().connect(0, mixer.input, i);
		
		lfoModule.rebuild();
	}
	
	//--------------------------------------------------------------------------
	
	public UnitOutputPort getOutput()
	{
		return mixer.getOutput();
	}
	
	//--------------------------------------------------------------------------
}
