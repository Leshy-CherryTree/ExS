/****************************************/
/* OscillatorModule.java					*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;

import com.jsyn.unitgen.MixerMono;
import com.jsyn.unitgen.UnitOscillator;




/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class OscillatorModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	public enum MixMode
	{
		Mix,
		Modulate;
	}
	
	//--------------------------------------------------------------------------
	
	private UnitOscillator osc1;
	private UnitOscillator osc2;
	
	private MixerMono mixer;
	
	private OscilatorType osc1Type;
	private OscilatorType osc2Type;
	private MixMode mode;

	private float frequency;
	private float osc1Amplitude;
	private float osc2Amplitude;
	
	private float osc1Detune;
	private float osc2Detune;
	
	//--------------------------------------------------------------------------
	
	public OscillatorModule(Synthesizer synth)
	{
		super(synth);
	}
			
	//--------------------------------------------------------------------------
	
	private void rebuild()
	{
		if (osc1 != null)
			getSynthesizer().remove(osc1);
		
		if (osc2 != null)
			getSynthesizer().remove(osc2);
		
		if (mixer != null)
			getSynthesizer().remove(mixer);
		
		osc1 = osc1Type.create();
		osc2 = osc2Type.create();
		
		getSynthesizer().add(osc1);
		getSynthesizer().add(osc2);
		
		if (mode == MixMode.Mix)
		{
			mixer = new MixerMono(2);
			getSynthesizer().add(mixer);
			
			osc1.output.connect(0, mixer.input, 0);
			osc2.output.connect(0, mixer.input, 1);
		}
		else
		{
			mixer = null;
			
			osc1.output.connect(osc2.amplitude);
		}
	}
	
	//--------------------------------------------------------------------------


	public UnitOutputPort getOutput()
	{
		if (mode == MixMode.Mix)
			return mixer.output;
		else
			return osc2.output;
	}
	
	//--------------------------------------------------------------------------
}
