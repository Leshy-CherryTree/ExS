/****************************************/
/* LFOModule.java						*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.UnitOscillator;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class LFOModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	private UnitOscillator osc;
	
	private OscilatorType type;
	
	//--------------------------------------------------------------------------
	
	public LFOModule(Synthesizer synth)
	{
		super(synth);
	}
	
	//--------------------------------------------------------------------------
	
	public void setType(OscilatorType type)
	{
		if (type.getType() != osc.getClass())
		{
			this.type = type;
			rebuild();
		}
	}
	
	//--------------------------------------------------------------------------
	
	public void setRate(float rate)
	{
		osc.frequency.set(rate);
	}
	
	//--------------------------------------------------------------------------
	
	public void setAmplitude(float amplitude)
	{
		osc.amplitude.set(amplitude);
	}
			
	//--------------------------------------------------------------------------
	
	private void rebuild()
	{
		getSynthesizer().remove(osc);
		
		osc = type.create();
		
		getSynthesizer().add(osc);
	}
	
	//--------------------------------------------------------------------------
	
	public UnitOutputPort getOutput()
	{
		return osc.output;
	}
	
	//--------------------------------------------------------------------------
}
